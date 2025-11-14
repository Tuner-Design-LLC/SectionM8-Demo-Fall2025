from datetime import datetime, timezone
import os
import pandas as pd
import xml.etree.ElementTree as ET
from hud_crawler import HUDCrawler

"""
hud_crawler_test.py

Simple test adapter for the HUDCrawler. This module builds a small test DataFrame with
representative columns, runs the crawler's normalize() method, writes
a CSV for inspection, and prints a small summary.
"""

# This function generates a small test DataFrame using pandas with representative HUD data
def generate_test_df():
    # Minimal array of base fields; HUDCrawler.normalize() will add any missing columns
    base = {
        "dataset_name": "TestHUDDataset",
        "fiscal_year": "2025",
        "state_name": "TestState",
        "state_code": "TS",
        "county_name": None,
        "fips_code": None,
        "hud_geoid": None,
        "msa_code": None,
        "area_type": "Urban",
        "ami_median_family_income": None,
        "ami_30_percent_limit": None,
        "ami_50_percent_limit": None,
        "ami_80_percent_limit": None,
        "household_size": None,
        "program_type": "TestProgram",
        "sub_program": None,
        "total_units": None,
        "occupied_units": None,
        "vacancy_rate": None,
        "assisted_units": None,
        "funding_source": None,
        "property_id": None,
        "property_name": None,
        "property_address": None,
        "zip_code": None,
        "property_type": None,
        "construction_year": None,
        "owner_entity": None,
        "managing_agent": None,
        "hud_inspection_score": None,
        "last_inspection_date": None,
        "affordability_end_date": None,
        "latitude": None,
        "longitude": None,
        "census_tract": None,
        "cbsa_code": None,
        "hud_region_name": None,
        "rural_indicator": None,
        "source_url": "http://example.org/dataset",
    # Handling of datetime to bypass deprecation notices - TBD in future iteration post-class submission
    "scrape_date": datetime.now(timezone.utc).isoformat(),
    "dataset_last_updated": datetime.now(timezone.utc).isoformat(),
        "update_frequency": "manual",
        "data_license": "test",
        "crawler_run_id": "test-run-1",
        "version_hash": "testhash-001",
    }

    rows = []
    # Create three rows to ensure normalize() does column population and type coercion correctly
    for i in range(3):
        r = base.copy()
        r.update({
            "county_name": f"County{i}",
            "fips_code": f"{1000 + i}",
            "property_id": f"P{i}",
            "property_name": f"Property {i}",
            "property_address": f"{i} Test St, TestCity, TS",
            "zip_code": f"1000{i}",
            "total_units": 10 + i,
            "assisted_units": 2 + i,
            "latitude": 40.0 + i * 0.01,
            "longitude": -75.0 - i * 0.01,
        })
        rows.append(r)

    return pd.DataFrame(rows)

# This function runs the test using HUDCrawler and the generated test DataFrame
def run_test():
    crawler = HUDCrawler()
    # Read the provided Excel document if it exists in repo-level Test Excels; otherwise fall back on generated test DataFrame object
    repo_root = os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir))
    excel_path = os.path.join(repo_root, "Test Excels", "HUD_Crawler_Test_Excel.xlsx")
    if os.path.exists(excel_path):
        print(f"Loading test Excel fixture: {excel_path}")
        crawler.load(excel_path)
    else:
        print("Excel fixture not found; using generated test DataFrame.")
        crawler.raw = generate_test_df()

    normalized = crawler.normalize()

    # Create a simple CSV for inspection (optional) and a Test XML in repo 'Test Reports'
    repo_root = os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir))
    reports_dir = os.path.join(repo_root, "Test Reports")
    os.makedirs(reports_dir, exist_ok=True)

    out_csv = os.path.join(reports_dir, "TestHUDReport.csv")
    normalized.to_csv(out_csv, index=False)

    # Create XML report using the HUDCrawler helper
    out_xml = os.path.join(reports_dir, "TestHUDReport.xml")
    crawler.create_xml_report(out_xml, normalized)

    # Validate/error handling that the written XML can be parsed
    try:
        ET.parse(out_xml)
        xml_status = "OK"
    except Exception as e:
        xml_status = f"FAIL: {e}"

    print(f"Wrote {out_csv} ({len(normalized)} rows)")
    print(f"Wrote {out_xml} ({len(normalized)} rows) — XML parse: {xml_status}")
    print("Summary:", crawler.summary())
    print("\nExported all reports to TestHUDReport.xml successfully!")


if __name__ == "__main__":
    run_test()
