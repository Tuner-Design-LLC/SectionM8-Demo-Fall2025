package crawlers.reportgui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class ReportGUI {
    private String filePath;
    private Boolean filterEnabledFMR;
    private int currentReportFMR;
    private int currentReportPHA;
    private ArrayList<FMRReport> FMRreports = new ArrayList<>();
    private ArrayList<PHAReport> PHAreports = new ArrayList<>();
    private ArrayList<FMRReport> FMRReportsFiltered = new ArrayList<>();

    public ReportGUI(){
        filterEnabledFMR = false;
        currentReportFMR =0;
        currentReportPHA=0;
    }

    //sets the file path to load from
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    //opens the XML file at the given path and extracts the data into reports
    public void openXMLReportFMR() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        ReportHandlerFMR handler = new ReportHandlerFMR();
        parser.parse(new File(filePath), handler);

        //create a temp list of imported reports then only add non-dupe ids
        ArrayList<FMRReport> tempReports = new ArrayList<>(handler.getReports());
        for (FMRReport report:tempReports){
            boolean flag = true;
            for(FMRReport baseReport: FMRreports){
                if (Integer.parseInt(report.getReportID()) == Integer.parseInt(baseReport.getReportID())) {
                    flag = false;
                    break;
                }
            }
            if(flag)
                FMRreports.add(report);
        }
    }

    //opens the XML file at the given path and extracts the data into reports
    public void openXMLReportPHA() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        ReportHandlerPHA handler = new ReportHandlerPHA();
        parser.parse(new File(filePath), handler);

        //create a temp list of imported reports then only add non-dupe ids
        ArrayList<PHAReport> tempReports = new ArrayList<>(handler.getReports());
        for (PHAReport report:tempReports){
            boolean flag = true;
            for(PHAReport baseReport:PHAreports){
                if (Integer.parseInt(report.getReportID()) == Integer.parseInt(baseReport.getReportID())) {
                    flag = false;
                    break;
                }
            }
            if(flag)
                PHAreports.add(report);
        }
    }

    //methods for dealing with the current number of reports and the selected report
    public int getTotalNumOfReports(){
        return FMRreports.size()+PHAreports.size();
    }

    public int getNumOfReportsFMR(){
        return FMRreports.size();
    }

    public int getNumOfReportsPHA(){
        return PHAreports.size();
    }

    public int getCurrentReportFMR(){
        return currentReportFMR;
    }

    public void setCurrentReportFMR(int report){
        currentReportFMR = report;
    }

    public int getCurrentReportPHA(){
        return currentReportPHA;
    }

    public void setCurrentReportPHA(int report){
        currentReportPHA = report;
    }

    //increase current report
    public void increaseCurrentReportFMR(){
        if (currentReportFMR +1 < getNumOfReportsFMR()){
            currentReportFMR++;
        }
    }

    //decrease current report
    public void decreaseCurrentReportFMR(){
        if (currentReportFMR > 0){
            currentReportFMR--;
        }
    }

    //increase current report PHP
    public void increaseCurrentReportPHA(){
        if (currentReportPHA+1 < getNumOfReportsPHA()){
            currentReportPHA++;
        }
    }

    //decrease current report PHP
    public void decreaseCurrentReportPHA(){
        if (currentReportPHA > 0){
            currentReportPHA--;
        }
    }

    //average methods include two cases depending on if the filter is enabled or not
    public double getAverageFMRNumber(){
        double temp=0;
        if(filterEnabledFMR){
            for(FMRReport report: FMRReportsFiltered){
                temp+= Double.parseDouble(report.getFairMarketRent());
            }
            return (temp/ FMRReportsFiltered.size());
        }
        else{
            for(FMRReport report: FMRreports){
                temp+= Double.parseDouble(report.getFairMarketRent());
            }
            return (temp/ FMRreports.size());
        }
    }

    public double getAverageIncomeFMR(){
        double temp=0;
        if(filterEnabledFMR){
            for(FMRReport report: FMRReportsFiltered){
                temp+= Double.parseDouble(report.getMedianHouseholdIncome());
            }
            return (temp/ FMRReportsFiltered.size());
        }
        else{
            for(FMRReport report: FMRreports){
                temp+= Double.parseDouble(report.getMedianHouseholdIncome());
            }
            return (temp/ FMRreports.size());
        }
    }

    public double getAverageBedroomsFMR(){
        double temp=0;
        if(filterEnabledFMR){
            for(FMRReport report: FMRReportsFiltered){
                temp+= Double.parseDouble(report.getNumBedrooms());
            }
            return (temp/ FMRReportsFiltered.size());
        }
        else{
            for(FMRReport report: FMRreports){
                temp+= Double.parseDouble(report.getNumBedrooms());
            }
            return (temp/ FMRreports.size());
        }
    }

    //create a filtered list of reports by state FMR
    public void filterReportsByStateFMR(String stateKey){
        for(FMRReport report: FMRreports){
            if(Objects.equals(report.getStateName(), stateKey)){
                FMRReportsFiltered.add(report);
            }
        }
    }

    //remove all filtered data FMR
    public void resetFilterReportListFMR(){
        FMRReportsFiltered = new ArrayList<>();
    }

    //get all num of filtered reports FMR
    public int getNumOfFilteredReportsFMR(){
        return FMRReportsFiltered.size();
    }

    //toggle the filter on / off FMR
    public void toggleFilterFMR(){
        filterEnabledFMR = !filterEnabledFMR;
    }

    //get methods for FMR reports
    public String getCurrentFMRReportFiscalYear(){
        return FMRreports.get(currentReportFMR).getFiscalYear();
    }

    public String getCurrentFMRReportCountyName(){
        return FMRreports.get(currentReportFMR).getCountyName();
    }

    public String getCurrentFMRReportNumOfBedrooms(){
        return FMRreports.get(currentReportFMR).getNumBedrooms();
    }

    public String getCurrentFMRReportFMRNumber(){
        return FMRreports.get(currentReportFMR).getFairMarketRent();
    }

    public String getCurrentFMRReportState(){
        return FMRreports.get(currentReportFMR).getStateName();
    }

    public String getCurrentFMRReportZipCode(){
        return FMRreports.get(currentReportFMR).getZipCode();
    }

    public String getCurrentFMRReportMarketType(){
        return FMRreports.get(currentReportFMR).getAreaType();
    }

    public String getCurrentFMRReportHouseholdIncome(){
        return FMRreports.get(currentReportFMR).getMedianHouseholdIncome();
    }

    //get methods for PHP reports
    public String getCurrentPHAReportState(){
        return PHAreports.get(currentReportPHA).getStateName();
    }

    public String getCurrentPHAReportCity(){
        return PHAreports.get(currentReportPHA).getCity();
    }

    public String getCurrentPHAReportCounty(){
        return PHAreports.get(currentReportPHA).getCountyName();
    }

    public String getCurrentPHAReportZipCode(){
        return PHAreports.get(currentReportPHA).getZipCode();
    }

    public String getCurrentPHAReportAddress(){
        return PHAreports.get(currentReportPHA).getAddress();
    }
}
