package crawlers.reportgui;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;

public class ReportHandlerHUD extends DefaultHandler {
    private ArrayList<HUDReport> reports = new ArrayList<>();
    private HUDReport currentReport;
    private StringBuilder content = new StringBuilder();

    public ArrayList<HUDReport> getReports() { return reports; }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("Report")) {
            currentReport = new HUDReport();
            currentReport.setReportID(attributes.getValue(0));
        }
        content.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        content.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (currentReport != null) {
            switch (qName) {
                case "dataset_name":
                    currentReport.setDatasetName(content.toString());
                    break;
                case "fiscal_year":
                    currentReport.setFiscalYear(content.toString());
                    break;
                case "state_name":
                    currentReport.setStateName(content.toString());
                    break;
                case "state_code":
                    currentReport.setStateCode(content.toString());
                    break;
                case "county_name":
                    currentReport.setCountyName(content.toString());
                    break;
                case "fips_code":
                    currentReport.setFipsCode(content.toString());
                    break;
                case "msa_code":
                    currentReport.setMsaCode(content.toString());
                    break;
                case "area_type":
                    currentReport.setAreaType(content.toString());
                    break;
                case "ami_median_family_income":
                    currentReport.setAmiMedianFamilyIncome(content.toString());
                    break;
                case "property_id":
                    currentReport.setPropertyId(content.toString());
                    break;
                case "property_name":
                    currentReport.setPropertyName(content.toString());
                    break;
                case "property_address":
                    currentReport.setPropertyAddress(content.toString());
                    break;
                case "owner_entity":
                    currentReport.setOwnerEntity(content.toString());
                    break;
                case "total_units":
                    currentReport.setTotalUnits(content.toString());
                    break;
                case "occupied_units":
                    currentReport.setOccupiedUnits(content.toString());
                    break;
                case "assisted_units":
                    currentReport.setAssistedUnits(content.toString());
                    break;
                case "vacancy_rate":
                    currentReport.setVacancyRate(content.toString());
                    break;
                case "hud_inspection_score":
                    currentReport.setHudInspectionScore(content.toString());
                    break;
                case "zip_code":
                    currentReport.setZipCode(content.toString());
                    break;
                case "Report":
                    reports.add(currentReport);
                    break;
            }
        }
    }
}
