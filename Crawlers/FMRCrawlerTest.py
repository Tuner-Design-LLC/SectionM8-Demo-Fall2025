# Basic class to represent a Fair Market Rent (FMR) Crawler
# This class is desigened to create report objects and export them to XML format.
# The scraper class will impliment this class to create report objects from scraped data.
import os
import xml.etree.ElementTree as ET
import FMRReport

class FMRCrawler:

    #the report list represents a list of all reports created by the crawler which can be exported to XML
    def __init__(self):
        self._reportsList = []

    #create an XML report from a list of report objects
    def createXMLReport(self):
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

        # write the tree to an XML file inside the repository-level `Test XMLs` folder
        repo_root = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
        out_dir = os.path.join(repo_root, "Test XMLs")
        os.makedirs(out_dir, exist_ok=True)
        out_path = os.path.join(out_dir, "TestReport.xml")
        with open(out_path, "wb") as file:
            tree.write(file, encoding="utf-8", xml_declaration=True)

    #create and return a report object that has basic test data
    def generate_test_report(self):
        return FMRReport.FMRCrawlerReport("Test00","Test01","Test02","Test03","Test04","Test05",
            "Test06","Test07","Test08", "Test09","Test10", "Test11",
            "Test12","Test13","Test14","Test15", "Test16", "Test17", "Test18", "Test19","Test20","Test21",
            "Test22")
    
    #append a report to the list of reports
    def appendReportToList(self, report):
        self._reportsList.append(report)

if __name__ == "__main__":
    crawler = FMRCrawler()

    crawler.appendReportToList(crawler.generate_test_report())
    crawler.appendReportToList(crawler.generate_test_report())
    crawler.appendReportToList(crawler.generate_test_report())

    crawler.createXMLReport()

    print("Exported all reports to TestReport.xml successfully!")
    
