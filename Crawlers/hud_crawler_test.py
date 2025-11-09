"""
hud_crawler_test.py

Simple test harness for the HUDCrawler in the same style as
`FMRCrawlerTest.py`. This script builds a tiny test DataFrame with
representative columns, runs the crawler's normalize() method, writes
a CSV for inspection and prints a small summary.
"""
# NOTICE - This module is purely for testing to match the FMR Crawler
# Future iterations of FMR & HUD Crawler before class demo will read off of the demo Excel docs

from datetime import datetime, timezone
import os
import pandas as pd
import xml.etree.ElementTree as ET
from hud_crawler import HUDCrawler


def generate_test_df():
    # Minimal set of fields; HUDCrawler.normalize will add any missing columns
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
    # use timezone-aware UTC datetimes to avoid deprecation warnings
    "scrape_date": datetime.now(timezone.utc).isoformat(),
    "dataset_last_updated": datetime.now(timezone.utc).isoformat(),
        "update_frequency": "manual",
        "data_license": "test",
        "crawler_run_id": "test-run-1",
        "version_hash": "testhash-001",
    }

    rows = []
    # create three simple rows to ensure normalize does column population and type coercion
    for i in range(3):
        r = base.copy()
        r.update({
            "county_name": f"County{i}",
            "fips_code": f"{1000 + i}",
            "property_id": f"P{i}",
            "property_name": f"Property {i}",
            "property_address": f"{i} Test St, TestCity, TS",
            "total_units": 10 + i,
            "assisted_units": 2 + i,
            "latitude": 40.0 + i * 0.01,
            "longitude": -75.0 - i * 0.01,
        })
        rows.append(r)

    return pd.DataFrame(rows)


def run_test():
    crawler = HUDCrawler()

    # Build test dataframe and feed it to the crawler
    test_df = generate_test_df()
    crawler.raw = test_df

    normalized = crawler.normalize()

    # Persist to a simple CSV for inspection (optional)
    out_csv = os.path.join(os.path.dirname(__file__), "TestHUDReport.csv")
    normalized.to_csv(out_csv, index=False)

    # Create an XML report that mirrors the FMRCrawler.createXMLReport style
    def createXMLReport(df: pd.DataFrame, out_path: str):
        root = ET.Element("HUDReports")
        TempIDTracker = 0
        for _, row in df.iterrows():
            report_elem = ET.SubElement(root, "Report", id=str(TempIDTracker))
            # write each column as a child element; convert values to strings
            for col in df.columns:
                val = row[col]
                text = "" if pd.isna(val) else str(val)
                # sanitize tag name by removing spaces
                tag = str(col).replace(" ", "")
                ET.SubElement(report_elem, tag).text = text
            TempIDTracker += 1

        ET.indent(root, space="  ", level=0)
        tree = ET.ElementTree(root)
        # write with short_empty_elements=False so empty elements are written
        # with explicit start/end tags (e.g. <tag></tag>) instead of self-closing (e.g. <tag />)
        with open(out_path, "wb") as f:
            tree.write(f, encoding="utf-8", xml_declaration=True, short_empty_elements=False)

    out_xml = os.path.join(os.path.dirname(__file__), "TestHUDReport.xml")
    createXMLReport(normalized, out_xml)

    # Validate the written XML can be parsed
    try:
        ET.parse(out_xml)
        xml_status = "OK"
    except Exception as e:
        xml_status = f"FAIL: {e}"

    print(f"Wrote {out_csv} ({len(normalized)} rows)")
    print(f"Wrote {out_xml} ({len(normalized)} rows) — XML parse: {xml_status}")
    print("Summary:", crawler.summary())


if __name__ == "__main__":
    run_test()
