"""
FMRCrawlerTest.py

Lightweight FMR crawler test harness. Loads the repository-level test Excel,
normalizes expected columns and writes a FMR XML report into `Test Reports`.

This version uses the shared utilities in `Util/crawler_utils.py` for IO
and DataFrame -> XML conversion so it matches the other crawler modules.
"""

import os
from pathlib import Path
import sys
import pandas as pd
from FMRReport import FMRCrawlerReport

# When running this script directly from the Crawlers folder, the repository
# root isn't on sys.path which prevents from Util imports. Ensure the repo
# root is available so helper packages like Util can be imported.
repo_root = Path(__file__).resolve().parents[1]
if str(repo_root) not in sys.path:
    sys.path.insert(0, str(repo_root))

from Util.crawler_utils import load_dataset, ensure_columns, dataframe_to_xml

# This class provides basic FMR crawler functionality for testing purposes.
class FMRCrawler:
    def __init__(self, dataset_path: str = None):
        self.dataset_path = dataset_path
        self.raw = None
        self.df = pd.DataFrame()

    # Load dataset from the given path or the default dataset path.
    def load(self, path: str = None) -> pd.DataFrame:
        p = path or self.dataset_path
        self.raw = load_dataset(p, dtype=str)
        return self.raw
    
    # Normalize the DataFrame to ensure required columns are present.
    def normalize(self, df: pd.DataFrame = None) -> pd.DataFrame:
        if df is None:
            df = self.raw
        if df is None:
            raise ValueError("No data loaded to normalize.")

        # Canonical FMR columns used by the GUI
        required = [
            "fiscal_year","state_name","state_code","county_name","fips_code","hud_geo_id","msa_code",
            "area_type","hud_region_code","zip_code","num_bedrooms","fair_market_rent","percentile_type",
            "bedroom_dist_source","survey_source_year","adjustment_factor","is_small_area_fmr",
            "median_household_income","source_url","scrape_date","version_hash","update_freq","crawler_run_data"
        ]

        self.df = ensure_columns(df, required, fill_value=None)
        return self.df

    # Create an XML report from the DataFrame and save it to the specified path.
    def create_xml_report(self, out_path: str = None, df: pd.DataFrame = None,
                          root_name: str = "FMRReports", item_name: str = "Report") -> None:
        df = df or self.df
        if df is None or df.empty:
            raise ValueError("No data available to write XML report.")

        # Default output path -> repository-root/Test Reports/TestFMRReport.xml
        if out_path is None:
            repo_root = Path(__file__).resolve().parents[1]
            out_path = str(repo_root / "Test Reports" / "TestFMRReport.xml")

        # Map dataframe column names (snake_case) to the CamelCase element names
        # expected by the Java SAX handler `ReportHandlerFMR`.
        mapping = {
            "fiscal_year": "FiscalYear",
            "state_name": "StateName",
            "state_code": "StateCode",
            "county_name": "CountyName",
            "fips_code": "FIPSCode",
            "hud_geo_id": "HUDGeoID",
            "msa_code": "MSACode",
            "area_type": "AreaType",
            "hud_region_code": "HUDRegionCode",
            "zip_code": "ZipCode",
            "num_bedrooms": "NumBedrooms",
            "fair_market_rent": "FairMarketRent",
            "percentile_type": "PercentileType",
            "bedroom_dist_source": "BedroomDistSource",
            "survey_source_year": "SurveySourceYear",
            "adjustment_factor": "AdjustmentFactor",
            "is_small_area_fmr": "IsSmallAreaFMR",
            "median_household_income": "MedianHouseholdIncome",
            "source_url": "SourceURL",
            "scrape_date": "ScrapeDate",
            "version_hash": "VersionHash",
            "update_freq": "UpdateFrequency",
            "crawler_run_data": "CrawlerRunData"
        }

        # Function to normalize tags using the mapping
        def tag_normalizer(s):
            key = str(s).strip()
            return mapping.get(key, key)

        dataframe_to_xml(df, out_path, root_name=root_name, item_name=item_name,
                         tag_normalizer=tag_normalizer)
        
    # Generate a simple test report with placeholder values.
    def generate_test_report(self) -> FMRCrawlerReport:
        return FMRCrawlerReport(
            "Test00","Test01","Test02","Test03","Test04","Test05",
            "Test06","Test07","Test08","Test09","Test10","Test11",
            "Test12","Test13","Test14","Test15","Test16","Test17","Test18","Test19","Test20","Test21","Test22"
        )

    # Generate reports from the repository-level test Excel file.
    def generate_reports_from_excel(self, excel_path: str = None) -> bool:
        if excel_path is None:
            repo_root = Path(__file__).resolve().parents[1]
            excel_file = repo_root / "Test Excels" / "FMR_Crawler_Test_Excel.xlsx"
        else:
            excel_file = Path(excel_path)

        if not excel_file.exists():
            print(f"Excel file not found: {excel_file}")
            return False

        try:
            df = load_dataset(str(excel_file), dtype=str)
        except Exception as e:
            print(f"Failed to read Excel file: {e}")
            return False

        self.df = self.normalize(df)
        return not self.df.empty

# Main execution for testing the FMRCrawler
if __name__ == "__main__":
    crawler = FMRCrawler()
    generated = crawler.generate_reports_from_excel()

    if not generated:
        # Fallback: create a few placeholder reports
        df = pd.DataFrame([vars(crawler.generate_test_report().__dict__)])
        crawler.df = df

    # Write the XML to repository-level Test Reports folder
    try:
        crawler.create_xml_report()
        print("Exported FMR reports to Test Reports/TestFMRReport.xml successfully!")
    except Exception as e:
        print(f"Failed to export FMR XML: {e}")

