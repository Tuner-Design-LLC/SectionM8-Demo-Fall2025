import os
import pandas as pd
from FMRReport import FMRCrawlerReport
from FMRCrawlerTest import FMRCrawler

# This is the path to the Excel File (moved to repository-level "Test Excels" folder)
EXCEL_FILE = os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir, "Test Excels", "FMR_Crawler_Test_Excel.xlsx"))

def scrape_data(file_path):
    # Load Excel file
    df = pd.read_excel(file_path)

    # Initialize the crawler
    crawler = FMRCrawler()

    #Iterates through each row and convert it into a report object
    for _, row in df.iterrows():
        report = FMRCrawlerReport(
            fiscal_year=str(row.get("fiscal_year", "")),
            state_name=str(row.get("state_name", "")),
            state_code=str(row.get("state_code", "")),
            county_name=str(row.get("county_name", "")),
            fips_code=str(row.get("fips_code", "")),
            hud_geo_id=str(row.get("hud_geo_id", "")),
            msa_code=str(row.get("msa_code", "")),
            area_type=str(row.get("area_type", "")),
            hud_region_code=str(row.get("hud_region_code", "")),
            zip_code=str(row.get("zip_code", "")),
            num_bedrooms=str(row.get("num_bedrooms", "")),
            fair_market_rent=str(row.get("fair_market_rent", "")),
            percentile_type=str(row.get("percentile_type", "")),
            bedroom_dist_source=str(row.get("bedroom_dist_source", "")),
            survey_source_year=str(row.get("survey_source_year", "")),
            adjustment_factor=str(row.get("adjustment_factor", "")),
            is_small_area_fmr=str(row.get("is_small_area_fmr", "")),
            median_household_income=str(row.get("median_household_income", "")),
            source_url=str(row.get("source_url", "")),
            scrape_date=str(row.get("scrape_date", "")),
            version_hash=str(row.get("version_hash", "")),
            update_freq=str(row.get("update_freq", "")),
            crawler_run_data=str(row.get("crawler_run_data", ""))
        )

        crawler.appendReportToList(report)

    crawler.createXMLReport()
    print("XML file created")

if __name__ == "__main__":
    scrape_data(EXCEL_FILE)
