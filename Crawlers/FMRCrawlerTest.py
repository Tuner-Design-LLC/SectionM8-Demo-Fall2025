# Basic class to represent a Fair Market Rent (FMR) Crawler
# This class is desigened to create report objects and export them to XML format.
# The scraper class will impliment this class to create report objects from scraped data.
import xml.etree.ElementTree as ET
import FMRReport
import pandas as pd
from pathlib import Path
import os

class FMRCrawler:

    #the report list represents a list of all reports created by the crawler which can be exported to XML
    def __init__(self):
        self._reportsList = []

    #create an XML report from a list of report objects
    def createXMLReport(self, output_path: str = None):
        # Create the root element (this it the top level element on the XML document)
        root = ET.Element("FMRReports")

        #TODO I believe that we should agree on a better way to create IDs (maybe based off of the scrapeDate?) but this will work for now.
        TempIDTracker=0

        # create a report inside the report element where each report has a unique ID
        for report in self._reportsList:
            report_elem = ET.SubElement(root, "Report", id=str(TempIDTracker))
            ET.SubElement(report_elem, "FiscalYear").text = report.get_fiscal_year()
            ET.SubElement(report_elem, "StateName").text = report.get_state_name()
            ET.SubElement(report_elem, "StateCode").text = report.get_state_code()
            ET.SubElement(report_elem, "CountyName").text = report.get_county_name()
            ET.SubElement(report_elem, "FIPSCode").text = report.get_fips_code()
            ET.SubElement(report_elem, "HUDGeoID").text = report.get_hud_geo_id()
            ET.SubElement(report_elem, "MSACode").text = report.get_msa_code()
            ET.SubElement(report_elem, "AreaType").text = report.get_area_type()
            ET.SubElement(report_elem, "HUDRegionCode").text = report.get_hud_region_code()
            ET.SubElement(report_elem, "ZipCode").text = report.get_zip_code()
            ET.SubElement(report_elem, "NumBedrooms").text = report.get_num_bedrooms()
            ET.SubElement(report_elem, "FairMarketRent").text = report.get_fair_market_rent()
            ET.SubElement(report_elem, "PercentileType").text = report.get_percentile_type()
            ET.SubElement(report_elem, "BedroomDistSource").text = report.get_bedroom_dist_source()
            ET.SubElement(report_elem, "SurveySourceYear").text = report.get_survey_source_year()
            ET.SubElement(report_elem, "AdjustmentFactor").text = report.get_adjustment_factor()
            ET.SubElement(report_elem, "IsSmallAreaFMR").text = report.get_is_small_area_fmr()
            ET.SubElement(report_elem, "MedianHouseholdIncome").text = report.get_median_household_income()
            ET.SubElement(report_elem, "SourceURL").text = report.get_source_url()
            ET.SubElement(report_elem, "ScrapeDate").text = report.get_scrape_date()
            ET.SubElement(report_elem, "VersionHash").text = report.get_version_hash()
            ET.SubElement(report_elem, "UpdateFrequency").text = report.get_update_freq()
            ET.SubElement(report_elem, "CrawlerRunData").text = report.get_crawler_run_data()
            TempIDTracker+=1

        #TODO this indents the XML so the file is more human readable (this can be removed once a GUI for viewing reports is created as it adds extra size to the file)
        ET.indent(root, space="  ", level=0)

        # create a tree from the root which is used to create the XML file
        tree = ET.ElementTree(root)

        # Determine output path: default to repository-root/Test Reports/TestFMRReport.xml
        if output_path is None:
            repo_root = Path(__file__).resolve().parents[1]
            output_path = repo_root / "Test Reports" / "TestFMRReport.xml"
        else:
            output_path = Path(output_path)

        # Ensure the parent directory exists
        output_path.parent.mkdir(parents=True, exist_ok=True)

        # Write the tree to the XML file
        with open(output_path, "wb") as file:
            tree.write(file, encoding="utf-8", xml_declaration=True)

    # Create and return a report object that has basic test data
    def generate_test_report(self):
        return FMRReport.FMRCrawlerReport("Test00","Test01","Test02","Test03","Test04","Test05",
            "Test06","Test07","Test08", "Test09","Test10", "Test11",
            "Test12","Test13","Test14","Test15", "Test16", "Test17", "Test18", "Test19","Test20","Test21",
            "Test22")
    
    # Append a report to the list of reports
    def appendReportToList(self, report):
        self._reportsList.append(report)
    # This function reads an Excel file and generates a test report XML from its rows
    def generate_reports_from_excel(self, excel_path: str = None):
        """Read rows from an Excel file and append reports to the internal list.

        If excel_path is None, looks for `FMR_Crawler_Test_Excel.xlsx` in the same folder as this script.
        Returns True if at least one row was added, False otherwise.
        """
        if excel_path is None:
            repo_root = Path(__file__).resolve().parents[1]
            excel_file = repo_root / "Test Excels" / "FMR_Crawler_Test_Excel.xlsx"
        else:
            excel_file = Path(excel_path)

        if not excel_file.exists():
            print(f"Excel file not found: {excel_file}")
            return False

        try:
            df = pd.read_excel(str(excel_file))
        except Exception as e:
            print(f"Failed to read Excel file: {e}")
            return False

        for _, row in df.iterrows():
            report = FMRReport.FMRCrawlerReport(
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

            self.appendReportToList(report)

        return len(self._reportsList) > 0

if __name__ == "__main__":
    crawler = FMRCrawler()

    # Try to generate reports from the test Excel file next to this script.
    generated = crawler.generate_reports_from_excel()

    if not generated:
        # Fallback to the old behavior (3 test reports)
        crawler.appendReportToList(crawler.generate_test_report())
        crawler.appendReportToList(crawler.generate_test_report())
        crawler.appendReportToList(crawler.generate_test_report())

    # write the XML to the Crawlers folder as `TestFMRReport.xml`
    crawler.createXMLReport()

    print("Exported all reports to TestFMRReport.xml successfully!")
    
