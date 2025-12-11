# NOTE - UML diagrams and further documentation to be added in future iterations.
# NOTE - Repasted hud_crawler.py to create test report like FMR crawler

import sys
import pandas as pd
from typing import Optional, Dict
import xml.etree.ElementTree as ET
from pathlib import Path

# Ensure repository root is on sys.path so sibling package Util can be imported
repo_root = Path(__file__).resolve().parents[1]
if str(repo_root) not in sys.path:
    sys.path.insert(0, str(repo_root))

from Util.crawler_utils import (
    load_dataset,
    ensure_columns,
    coerce_numeric,
    coerce_datetime,
    dataframe_to_xml,
)

"""
HUDCrawler - Prototype static file crawler for HUD General datasets.
Will normalize common fields across different HUD general files (Income Limits, Assisted Units, LIHTC, MF properties).
Designed to be replaced by live API calls later to page HTML; for now it will read CSV/XLSX files statically.

Core normalized data attributes (not all must exist in source file to run):
- dataset_name, fiscal_year, state_name, state_code, county_name, fips_code, hud_geoid, msa_code, area_type,
  ami_median_family_income, ami_30_percent_limit, ami_50_percent_limit, ami_80_percent_limit, household_size,
  program_type, sub_program, total_units, occupied_units, vacancy_rate, assisted_units, funding_source,
  property_id, property_name, property_address, property_type, construction_year, owner_entity, managing_agent,
  hud_inspection_score, last_inspection_date, affordability_end_date,
  latitude, longitude, census_tract, cbsa_code, hud_region_name, rural_indicator,
  source_url, scrape_date, dataset_last_updated, update_frequency, data_license, crawler_run_id, version_hash
"""

# This class is a test crawler that reads HUD datasets and creates XML reports similar to the FMR crawler test harness.
# It normalizes data read from test HUD Excel datasets into a canonical format and writes XML reports to send to the GUI
# Future iterations will include live HTML scraping and more complex data handling.
class HUDCrawler:
    def __init__(self, dataset_path: Optional[str] = None):
        self.dataset_path = dataset_path
        self.raw = None
        self.df = pd.DataFrame()

    # This function loads a CSV or Excel file into a raw pandas dataframe
    def load(self, path: Optional[str] = None) -> pd.DataFrame:
        """Load CSV or Excel into raw dataframe using shared utility."""
        p = path or self.dataset_path
        self.raw = load_dataset(p, dtype=str)
        return self.raw

    # This function normalizes a HUD general dataset into a canonical set of columns
    def normalize(self, df: Optional[pd.DataFrame] = None) -> pd.DataFrame:
        """Normalize a HUD general dataset into a canonical set of columns."""
        if df is None:
            df = self.raw
        if df is None:
            raise ValueError("No data loaded to normalize.")
        # Normalizing data column names to lower-case and strip
        cols_map = {c: c.strip() for c in df.columns}
        df = df.rename(columns=cols_map)
        # Generic required data attributes (must be created if missing)
        required = [
            "dataset_name","fiscal_year","state_name","state_code","county_name","fips_code","hud_geoid","msa_code","area_type",
            "ami_median_family_income","ami_30_percent_limit","ami_50_percent_limit","ami_80_percent_limit","household_size",
            "program_type","sub_program","total_units","occupied_units","vacancy_rate","assisted_units","funding_source",
            "property_id","property_name","property_address","property_type","construction_year","owner_entity","managing_agent",
            "zip_code",
            "hud_inspection_score","last_inspection_date","affordability_end_date",
            "latitude","longitude","census_tract","cbsa_code","hud_region_name","rural_indicator",
            "source_url","scrape_date","dataset_last_updated","update_frequency","data_license","crawler_run_id","version_hash"
        ]
        # ensure required columns exist and return subset copy
        df = ensure_columns(df, required, fill_value=None)

        # Type conversions
        numeric_cols = [
            "ami_median_family_income", "ami_30_percent_limit", "ami_50_percent_limit", "ami_80_percent_limit",
            "total_units", "occupied_units", "vacancy_rate", "assisted_units", "hud_inspection_score", "construction_year"
        ]
        coerce_numeric(df, numeric_cols)
        coerce_datetime(df, ["last_inspection_date", "dataset_last_updated"]) 

        self.df = df
        return self.df

    # Simple filter function to filter normalized data by county name    
    def filter_by_county(self, county_name: str) -> pd.DataFrame:
        if self.df.empty:
            raise ValueError("Data not normalized. Call load() and normalize() first.")
        return self.df[self.df['county_name'].str.contains(county_name, case=False, na=False)]

    # Summary function to return basic statistics for normalized datasets
    def summary(self) -> Dict:
        if self.df.empty:
            return {}
        return {
            "rows": len(self.df),
            "unique_counties": int(self.df["county_name"].nunique()),
            "unique_programs": int(self.df["program_type"].nunique())
        }

    # This function creates an XML report from the normalized DataFrame to send to the testing GUI
    def create_xml_report(self, out_path: str, df: Optional[pd.DataFrame] = None, root_name: str = "HUDReports", item_name: str = "Report"):

        # If no DataFrame is provided, use the normalized one
        # Use shared utility to write XML from DataFrame
        if df is None:
            df = self.df
        if df is None or df.empty:
            raise ValueError("No data available to write XML report.")

        dataframe_to_xml(df, out_path, root_name=root_name, item_name=item_name,
                         tag_normalizer=lambda s: str(s).replace(' ', ''))
