# NOTE - UML diagrams and further documentation to be added in future iterations.
# NOTE - Repasted hud_crawler.py to create test report like FMR crawler

import pandas as pd
from typing import Optional, Dict
import xml.etree.ElementTree as ET

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
        """Load CSV or Excel into raw dataframe."""
        p = path or self.dataset_path
        if p is None:
            raise ValueError("You must provide a path to HUD dataset (CSV or XLSX).")
        if p.lower().endswith(('.xls', '.xlsx')):
            self.raw = pd.read_excel(p, dtype=str)
        else:
            self.raw = pd.read_csv(p, dtype=str)
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
        for col in required:
            if col not in df.columns:
                df[col] = None

        # Type conversions for numeric fields to avoid error handling (when applicable)
        numeric_cols = ["ami_median_family_income","ami_30_percent_limit","ami_50_percent_limit","ami_80_percent_limit",
                        "total_units","occupied_units","vacancy_rate","assisted_units","hud_inspection_score","construction_year"]
        for c in numeric_cols:
            df[c] = pd.to_numeric(df[c], errors='coerce')

        # Handling of date fields
        df["last_inspection_date"] = pd.to_datetime(df["last_inspection_date"], errors='coerce')
        df["dataset_last_updated"] = pd.to_datetime(df["dataset_last_updated"], errors='coerce')

        self.df = df[required].copy()
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
        if df is None:
            df = self.df
        if df is None or df.empty:
            raise ValueError("No data available to write XML report.")

        root = ET.Element(root_name)
        temp_id = 0
        for _, row in df.iterrows():
            report_elem = ET.SubElement(root, item_name, id=str(temp_id))
            for col in df.columns:
                val = row[col]
                text = "" if pd.isna(val) else str(val)
                tag = str(col).replace(" ", "")
                ET.SubElement(report_elem, tag).text = text
            temp_id += 1

        # Print formatting (pretty printing) for user readability
        try:
            ET.indent(root, space="  ", level=0)
        except Exception:
            # Ignore if Python version is older
            pass

        tree = ET.ElementTree(root)
        with open(out_path, "wb") as f:
            tree.write(f, encoding="utf-8", xml_declaration=True, short_empty_elements=False)
