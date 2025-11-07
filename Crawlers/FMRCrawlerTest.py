# Basic class to represent a Fair Market Rent (FMR) Crawler
# At this moment the purpose of this class is to impliment the core functions required to create a report object than can be exported to an xml format for storage and display later down the line.
# THIS CLASS IS A PLACEHOLDER - it does not read in any data its mainly a test for creating reports and exporting them to XML format.
import xml.etree.ElementTree as ET
import FMRReport

class FMRCrawler:

    #crawler data attributes will be filled out when the crawler accesses the raw data
    def __init__(self):

        self._fiscal_year = None
        self._state_name = None
        self._state_code = None
        self._county_name = None
        self._fips_code = None
        self._hud_geo_id = None
        self._msa_code = None
        self._area_type = None
        self._hud_region_code = None
        self._zip_code = None
        self._num_bedrooms = None
        self._fair_market_rent = None
        self._percentile_type = None
        self._bedroom_dist_source = None
        self._survey_source_year = None
        self._adjustment_factor = None
        self._is_small_area_fmr = None
        self._median_household_income = None
        self._source_url = None
        self._scrape_date = None
        self._version_hash = None
        self._update_freq = None
        self._crawler_run_data = None

    #create an XML report from a list of report objects
    def createXMLReport(self, reportList):
        # Create the root element (this it the top level element on the XML document)
        root = ET.Element("FMRReports")

        #TODO I believe that we should agree on a better way to create IDs (maybe based off of the scrapeDate?) but this will work for now.
        TempIDTracker=0

        # create a report inside the report element where each report has a unique ID
        for report in reportList:
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

        #TODO indent the XML so the file is more human readable (this can be removed once a GUI for viewing reports is created as it adds extra size to the file)
        ET.indent(root, space="  ", level=0)

        # create a tree from the root which is used to create the XML file
        tree = ET.ElementTree(root)

        # write the tree to an XML file
        with open("TestReport.xml", "wb") as file:
            tree.write(file, encoding="utf-8", xml_declaration=True)

    #create and return a report object
    def generate_report(self):
        return FMRReport.FMRCrawlerReport(self._fiscal_year,self._state_name,self._state_code,self._county_name,self._fips_code,self._hud_geo_id,
            self._msa_code,self._area_type,self._hud_region_code, self._zip_code,self._num_bedrooms, self._fair_market_rent,
            self._percentile_type,self._bedroom_dist_source,self._survey_source_year,self._adjustment_factor, self._is_small_area_fmr,
            self._median_household_income, self._source_url, self._scrape_date,self._version_hash,self._update_freq,
            self._crawler_run_data)
    
    #create and return a report object that has basic test data
    def generate_test_report(self):
        return FMRReport.FMRCrawlerReport("Test00","Test01","Test02","Test03","Test04","Test05",
            "Test06","Test07","Test08", "Test09","Test10", "Test11",
            "Test12","Test13","Test14","Test15", "Test16", "Test17", "Test18", "Test19","Test20","Test21",
            "Test22")

if __name__ == "__main__":
    crawler = FMRCrawler()

    reports = []

    reports.append(crawler.generate_test_report ())
    reports.append(crawler.generate_test_report ())
    reports.append(crawler.generate_test_report ())

    crawler.createXMLReport(reports)

    print("Exported all reports to TestReport.xml successfully!")
    
