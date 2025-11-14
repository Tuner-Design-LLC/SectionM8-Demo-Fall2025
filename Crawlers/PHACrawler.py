# PHACrawler.py
# Prototype PHA crawler using consolidated domain objects from PHADomain.py

import os
import pandas as pd
import xml.etree.ElementTree as ET
from datetime import datetime, timezone
from PHADomain import (
    Primary, HousingProgram, GeographicContextual, FinanceFunding,
    DemographicOccupancy, CompliancePerformance, AnalyticsMetadata,
    AdministrativeContact
)

class PHACrawler:
    """Prototype static file PHA crawler that normalizes data and creates XML reports."""

    def __init__(self, dataset_path=None):
        self.dataset_path = dataset_path
        self.raw = None
        self.df = pd.DataFrame()
        self.records = []

    # Load CSV or Excel
    def load(self, path=None):
        p = path or self.dataset_path
        if p is None:
            raise ValueError("Dataset path is required")
        if not os.path.exists(p):
            raise FileNotFoundError(f"Dataset file not found: {p}")
        if p.lower().endswith((".xls", ".xlsx")):
            self.raw = pd.read_excel(p, dtype=str)
        else:
            self.raw = pd.read_csv(p, dtype=str)
        return self.raw

    # Normalize the data into canonical columns
    def normalize(self, df=None):
        if df is None:
            df = self.raw
        if df is None:
            raise ValueError("No data loaded to normalize.")

        # Example canonical columns based on your PHA attributes
        required = [
            "pha_code","name","hud_regional_code","jurisdiction","address","city","state_code","state_name",
            "zip_code","county_name","fips_code","program_type","num_hcv_units","num_public_units","num_total_assisted",
            "waiting_list_status","occupancy_rate","hcv_util_rate","latitude","longitude","msa_code","rural_indicator",
            "annual_budget","capital_fund_allocation","financial_report_year","avg_tenant_income","median_rent_dist",
            "household_type_dist","avg_tenant_rent_share","avg_subsidy_amount","semap_score","last_hud_audit_date",
            "inspection_compliance_rate","performance_category","last_updated","source_url","scrape_date","version_hash",
            "dataset_last_update","update_freq","data_license","crawler_run_data","phone_number","fax_line","email",
            "executive_director","contact_last_verified"
        ]

        # Add missing columns with None
        for col in required:
            if col not in df.columns:
                df[col] = None

        self.df = df[required].copy()
        return self.df

    # Convert normalized DataFrame to XML
    def create_xml_report(self, out_path, df=None, root_name="PHAReports", item_name="PHA"):
        df = df or self.df
        if df.empty:
            raise ValueError("No data available for XML report.")

        root = ET.Element(root_name)
        for idx, row in df.iterrows():
            pha_elem = ET.SubElement(root, item_name, id=str(idx))
            for col in df.columns:
                ET.SubElement(pha_elem, col).text = "" if pd.isna(row[col]) else str(row[col])

        try:
            ET.indent(root, space="  ", level=0)  # Python 3.9+
        except AttributeError:
            pass

        tree = ET.ElementTree(root)
        tree.write(out_path, encoding="utf-8", xml_declaration=True, short_empty_elements=False)

    # Generate example records using your domain objects
    def generate_record_objects(self):
        self.records.clear()  # avoid duplicates
        for _, row in self.df.iterrows():
            primary = Primary(row["pha_code"], row["name"], row["hud_regional_code"], row["jurisdiction"],
                              row["address"], row["city"], row["state_code"], row["state_name"],
                              row["zip_code"], row["county_name"], row["fips_code"])
            program = HousingProgram(row["program_type"], row["num_hcv_units"], row["num_public_units"],
                                     row["num_total_assisted"], row["waiting_list_status"],
                                     row["occupancy_rate"], row["hcv_util_rate"])
            geo = GeographicContextual(row["latitude"], row["longitude"], row["msa_code"],
                                       row["hud_regional_code"], row["rural_indicator"])
            finance = FinanceFunding(row["annual_budget"], row["capital_fund_allocation"], row["financial_report_year"])
            demo = DemographicOccupancy(row["avg_tenant_income"], row["median_rent_dist"],
                                        row["household_type_dist"], row["avg_tenant_rent_share"],
                                        row["avg_subsidy_amount"])
            compliance = CompliancePerformance(row["semap_score"], row["last_hud_audit_date"],
                                               row["inspection_compliance_rate"], row["performance_category"],
                                               row["last_updated"])
            analytics = AnalyticsMetadata(row["source_url"], row["scrape_date"], row["version_hash"],
                                         row["dataset_last_update"], row["update_freq"], row["data_license"],
                                         row["crawler_run_data"])
            contact = AdministrativeContact(row["phone_number"], row["fax_line"], row["email"],
                                            row["executive_director"], row["contact_last_verified"])
            self.records.append((primary, program, geo, finance, demo, compliance, analytics, contact))


# Tester block

if __name__ == "__main__":
    crawler_folder = os.path.dirname(os.path.abspath(__file__))
    # place test outputs into the repository-level 'Test Reports' folder
    repo_root = os.path.abspath(os.path.join(crawler_folder, os.pardir))
    excel_path = os.path.join(repo_root, "Test Excels", "PHA_Crawler_Test_Excel.xlsx")
    xml_path = os.path.join(repo_root, "Test Reports", "TestPHAReport.xml")

    try:
        crawler = PHACrawler(excel_path)
        crawler.load()
        crawler.normalize()
        crawler.generate_record_objects()
        crawler.create_xml_report(xml_path)
        print(f"PHA XML report generated successfully at: {xml_path}")
        print(f"Total PHA records created: {len(crawler.records)}")
    except Exception as e:
        print(f"Error: {e}")
