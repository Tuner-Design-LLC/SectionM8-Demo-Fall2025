package crawlers.reportgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class ReportGUIController {
    //BASE GUI USED FOR ALL REPORTS
    private ReportGUI GUI1 = new ReportGUI();

    @FXML
    private TextField AddressPHA;

    @FXML
    private TextField BedroomAverage;

    @FXML
    private TextField CityPHA;

    @FXML
    private TextField CountyName;

    @FXML
    private TextField CountyNamePHA;

    @FXML
    private TextField CurrentReport;

    @FXML
    private TextField CurrentReportPHA;

    @FXML
    private TextField DirectorPHA;

    @FXML
    private TextField EmailPHA;

    @FXML
    private TextField FMRAverage;

    @FXML
    private TextField FMRNumber;

    @FXML
    private TextField FaxPHA;

    @FXML
    private TextField FilterState;

    @FXML
    private TextField FiscalYear;

    @FXML
    private TextField HouseholdIncome;

    @FXML
    private TextField IncomeAverage;

    @FXML
    private TextField JurisdictionPHA;

    @FXML
    private TextField ManualEnterReport;

    @FXML
    private TextField ManualEnterReportPHA;

    @FXML
    private TextField MarketType;

    @FXML
    private TextField NumBedrooms;

    @FXML
    private TextField PHACode;

    @FXML
    private TextField PhonePHA;

    @FXML
    private TextField ReportPath;

    @FXML
    private TextField State;

    @FXML
    private TextField StatePHA;

    @FXML
    private TextField TotalFilteredReports;

    @FXML
    private TextField TotalReports;

    @FXML
    private TextField TotalReportsFMR;

    @FXML
    private TextField TotalReportsPHA;

    @FXML
    private TextField ZipCode;

    @FXML
    private TextField ZipCodePHA;


    @FXML //A testing load method that loads reports without having to input the file path by hand
    //NOTE if you want to use this test method just add your own file paths
    void TestLoad(ActionEvent event) throws ParserConfigurationException, IOException, SAXException {
        // Use project-level 'Test Reports' directory relative to working directory
        GUI1.setFilePath(System.getProperty("user.dir") + "\\Test Reports\\TestFMRReport.xml");
        GUI1.openXMLReportFMR();

        GUI1.setFilePath(System.getProperty("user.dir") + "\\Test Reports\\TestPHAReport.xml");
        GUI1.openXMLReportPHA();

        updateReportGUIFMR();
        updateReportGUIPHA();
    }

    @FXML //opens the selected report path and update GUI
    void OpenReportFMR(ActionEvent event) throws ParserConfigurationException, IOException, SAXException {
        GUI1.setFilePath(this.ReportPath.getText());

        GUI1.openXMLReportFMR();

        updateReportGUIFMR();
    }

    @FXML //opens the selected report path and update GUI
    void OpenReportPHA(ActionEvent event) throws ParserConfigurationException, IOException, SAXException {
        GUI1.setFilePath(this.ReportPath.getText());

        GUI1.openXMLReportPHA();

        updateReportGUIPHA();
    }

    @FXML //clear the report lists and update GUI
    void ClearReports(ActionEvent event) {
        GUI1 = new ReportGUI();

        updateReportGUIClear();
    }

    @FXML //decrease the currently selected report by one and update GUI
    void DecreaseCurrentReportFMR(ActionEvent event) {
        GUI1.decreaseCurrentReportFMR();

        updateReportGUIFMR();
    }

    @FXML //increase the currently selected report by one and update GUI
    void IncreaseCurrentReportFMR(ActionEvent event) {
        GUI1.increaseCurrentReportFMR();

        updateReportGUIFMR();
    }

    @FXML //decrease the currently selected report by one and update GUI
    void DecreaseCurrentReportPHA(ActionEvent event) {
        GUI1.decreaseCurrentReportPHA();

        updateReportGUIPHA();
    }

    @FXML //increase the currently selected report by one and update GUI
    void IncreaseCurrentReportPHA(ActionEvent event) {
        GUI1.increaseCurrentReportPHA();

        updateReportGUIPHA();
    }

    @FXML //get the ID that the user manually entered and go to it
    void GetManualReportFMR(ActionEvent event) {
        int tempReportID = Integer.parseInt(this.ManualEnterReport.getText());

        if (tempReportID > 0 && tempReportID < GUI1.getNumOfReportsFMR()+1){
            GUI1.setCurrentReportFMR(tempReportID-1);
            updateReportGUIFMR();
        }
    }

    @FXML //get the ID that the user manually entered and go to it
    void GetManualReportPHA(ActionEvent event) {
        int tempReportID = Integer.parseInt(this.ManualEnterReportPHA.getText());

        if (tempReportID > 0 && tempReportID < GUI1.getNumOfReportsPHA()+1){
            GUI1.setCurrentReportPHA(tempReportID-1);
            updateReportGUIPHA();
        }
    }

    @FXML //apply the filters and update the GUI
    void FilterButtonFMR(ActionEvent event) {
        GUI1.resetFilterReportListFMR();
        GUI1.filterReportsByStateFMR(this.FilterState.getText());
        TotalFilteredReports.setText(String.format("%d",GUI1.getNumOfFilteredReportsFMR()));
        updateReportGUIFMR();
    }

    @FXML//toggle the filter on/off and update GUI
    void ToggleFiltersFMR(ActionEvent event) {
        GUI1.toggleFilterFMR();
        updateReportGUIFMR();
    }

    //update all FMR text fields
    private void updateReportGUIFMR(){
        CurrentReport.setText(String.format("%d",GUI1.getCurrentReportFMR()+1));
        TotalReports.setText(String.format("%d",GUI1.getTotalNumOfReports()));
        TotalReportsFMR.setText(String.format("%d",GUI1.getNumOfReportsFMR()));

        FiscalYear.setText(String.format(GUI1.getCurrentFMRReportFiscalYear()));
        CountyName.setText(String.format(GUI1.getCurrentFMRReportCountyName()));
        NumBedrooms.setText(String.format(GUI1.getCurrentFMRReportNumOfBedrooms()));
        FMRNumber.setText(String.format("$%s",GUI1.getCurrentFMRReportFMRNumber()));
        HouseholdIncome.setText(String.format("$%s",GUI1.getCurrentFMRReportHouseholdIncome()));
        MarketType.setText(String.format(GUI1.getCurrentFMRReportMarketType()));
        ZipCode.setText(String.format(GUI1.getCurrentFMRReportZipCode()));
        State.setText(String.format(GUI1.getCurrentFMRReportState()));

        double avgFMR = GUI1.getAverageFMRNumber();
        if (Double.isNaN(avgFMR))
            FMRAverage.setText("");
        else
            FMRAverage.setText(String.format("$%.2f", avgFMR));

        double avgIncome = GUI1.getAverageIncomeFMR();
        if (Double.isNaN(avgIncome))
            IncomeAverage.setText("");
        else
            IncomeAverage.setText(String.format("$%.2f", avgIncome));

        double avgBedrooms = GUI1.getAverageBedroomsFMR();
        if (Double.isNaN(avgBedrooms))
            BedroomAverage.setText("");
        else
            BedroomAverage.setText(String.format("%.0f", avgBedrooms));
    }

    //update all PHA text fields
    private void updateReportGUIPHA(){
        CurrentReportPHA.setText(String.format("%d",GUI1.getCurrentReportPHA()+1));
        TotalReports.setText(String.format("%d",GUI1.getTotalNumOfReports()));
        TotalReportsPHA.setText(String.format("%d",GUI1.getNumOfReportsPHA()));

        StatePHA.setText(String.format(GUI1.getCurrentPHAReportState()));
        CityPHA.setText(String.format(GUI1.getCurrentPHAReportCity()));
        CountyNamePHA.setText(String.format(GUI1.getCurrentPHAReportCounty()));
        ZipCodePHA.setText(String.format(GUI1.getCurrentPHAReportZipCode()));
        AddressPHA.setText(String.format(GUI1.getCurrentPHAReportAddress()));
        JurisdictionPHA.setText(String.format(GUI1.getCurrentPHAReportJurisdiction()));
        PHACode.setText(String.format(GUI1.getCurrentPHAReportCode()));
        DirectorPHA.setText(String.format(GUI1.getCurrentPHAReportDirector()));
        PhonePHA.setText(String.format(GUI1.getCurrentPHAReportPhone()));
        FaxPHA.setText(String.format(GUI1.getCurrentPHAReportFax()));
        EmailPHA.setText(String.format(GUI1.getCurrentPHAReportEmail()));

    }

    //clear all text fields to base values
    private void updateReportGUIClear(){
        TotalReports.setText("0");
        TotalReportsFMR.setText("0");
        TotalReportsPHA.setText("0");
        TotalFilteredReports.setText("0");
        CurrentReport.setText("");
        FiscalYear.setText("");
        CountyName.setText("");
        NumBedrooms.setText("");
        FMRNumber.setText("");
        HouseholdIncome.setText("");
        MarketType.setText("");
        ZipCode.setText("");
        State.setText("");
        FMRAverage.setText("");
        IncomeAverage.setText("");
        BedroomAverage.setText("");

        CurrentReportPHA.setText("");
        StatePHA.setText("");
        CityPHA.setText("");
        CountyNamePHA.setText("");
        ZipCodePHA.setText("");
        AddressPHA.setText("");
        JurisdictionPHA.setText("");
        PHACode.setText("");
        DirectorPHA.setText("");
        PhonePHA.setText("");
        FaxPHA.setText("");
        EmailPHA.setText("");

    }

}
