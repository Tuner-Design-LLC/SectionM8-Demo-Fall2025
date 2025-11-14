# NOTE - This module provides utility functions to export data payloads into JSON and XML formats.
# NOTE - Further enhancements and documentation will be added in future iterations.
# NOTE - This module will work in conjunction with the TBD prototype PHA and FMR crawlers.

import json
import xml.etree.ElementTree as ET
import pandas as pd
from datetime import datetime

# This function exports a given payload dictionary to a JSON file at the specified path.
def export_to_json(payload: dict, path: str):
    with open(path, "w", encoding="utf-8") as f:
        json.dump(payload, f, default=str, indent=2)

# This function converts a pandas DataFrame to an XML ElementTree.
def df_to_xml_element(df: pd.DataFrame, root_name: str, row_name: str) -> ET.Element:
    root = ET.Element(root_name)
    for _, row in df.iterrows():
        r = ET.SubElement(root, row_name)
        for col in df.columns:
            child = ET.SubElement(r, col)
            val = row[col]
            child.text = "" if pd.isna(val) else str(val)
    return root

# This function exports an XML ElementTree to a file at the specified path.
def export_xml(root_element: ET.Element, path: str):
    tree = ET.ElementTree(root_element)
    tree.write(path, encoding='utf-8', xml_declaration=True)

# This function builds a comprehensive export payload from PHA, FMR, and HUD DataFrames.
def build_export_payload(pha_df, fmr_df, hud_df):
    payload = {
        # Metadata about the export including timestamps and row counts
        "metadata": {
            "exported_at": datetime.utcnow().isoformat() + "Z",
            "sources": {
                "pha_rows": int(len(pha_df)) if pha_df is not None else 0,
                "fmr_rows": int(len(fmr_df)) if fmr_df is not None else 0,
                "hud_rows": int(len(hud_df)) if hud_df is not None else 0
            }
        },
        # Data sections for PHA, FMR, and HUD datasets
        "pha": json.loads(pha_df.to_json(orient="records", date_format="iso")) if pha_df is not None else [],
        "fmr": json.loads(fmr_df.to_json(orient="records")) if fmr_df is not None else [],
        "hud": json.loads(hud_df.to_json(orient="records", date_format="iso")) if hud_df is not None else []
    }
    return payload
