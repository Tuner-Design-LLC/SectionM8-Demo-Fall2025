package crawlers.reportgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.geometry.Side;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import java.io.File;
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
    private Button stateFilterButtonFMR;

    @FXML
    private Button stateFilterButtonPHA;

    private ContextMenu filterContextMenuFMR;
    private ContextMenu filterContextMenuPHA;

    private String filterStateValueFMR = "";
    private String filterStateValuePHA = "";

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
    private TabPane MainTabPane;


    @FXML
    void BrowseReport(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Report File");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selected = chooser.showOpenDialog(ReportPath.getScene().getWindow());
        if (selected != null) {
            String path = selected.getAbsolutePath();
            // populate the existing ReportPath field (top browse field)
            ReportPath.setText(path);
        }
    }

    @FXML
    void OpenReports(ActionEvent event) {
        if (MainTabPane == null || MainTabPane.getSelectionModel().getSelectedItem() == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Reports");
            a.setHeaderText("No tab selected");
            a.setContentText("Cannot determine which report type to open.");
            a.showAndWait();
            return;
        }

        String tabText = MainTabPane.getSelectionModel().getSelectedItem().getText();

        try {
            if ("FMR Reports".equals(tabText)) {
                OpenReportFMR(event);
            } else if ("PHA Reports".equals(tabText)) {
                OpenReportPHA(event);
            } else if ("HUD Reports".equals(tabText)) {
                OpenReportHUD(event);
            } else {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Open Reports");
                a.setHeaderText("Unknown tab");
                a.setContentText("Unrecognized tab: " + tabText);
                a.showAndWait();
            }
        } catch (Exception ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Reports");
            a.setHeaderText("Error opening report");
            a.setContentText(ex.getMessage());
            a.showAndWait();
        }
    }

    @FXML //opens the selected HUD report path and update GUI
    void OpenReportHUD(ActionEvent event) {
        String path = (this.ReportPath == null) ? null : this.ReportPath.getText();
        if (path == null || path.trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Open Report");
            a.setHeaderText("No file selected");
            a.setContentText("Please select a report file before opening.");
            a.showAndWait();
            return;
        }

        File f = new File(path);
        if (!f.exists() || !f.isFile()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("File not found");
            a.setContentText("The selected file does not exist: " + path);
            a.showAndWait();
            return;
        }

        GUI1.setFilePath(path);
        try {
            GUI1.resetHUDReportList();
            GUI1.openXMLReportHUD();
            updateReportGUIHUD();
        } catch (IndexOutOfBoundsException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("Wrong report type");
            a.setContentText("The selected file is not a HUD report. Please select the correct report type.");
            a.showAndWait();
        } catch (org.xml.sax.SAXException | javax.xml.parsers.ParserConfigurationException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("Invalid XML or wrong report type");
            a.setContentText("The selected file could not be parsed as a HUD report. Please select a valid HUD XML file.");
            a.showAndWait();
        } catch (IOException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("I/O error");
            a.setContentText(ex.getMessage());
            a.showAndWait();
        } catch (Exception ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("Error opening report");
            a.setContentText(ex.getMessage());
            a.showAndWait();
        }
    }

    @FXML
    private TextField State;

    @FXML
    private TextField StatePHA;

    @FXML
    private TextField TotalFilteredReports;

    @FXML
    private TextField TotalFilteredReportsPHA;

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

    @FXML
    private TextField CurrentReportHUD;

    @FXML
    private TextField TotalReportsHUD;

    @FXML
    private TextField propertyAddressHUD;

    @FXML
    private TextField stateHUD;

    @FXML
    private TextField countyHUD;

    @FXML
    private TextField zipcodeHUD;

    @FXML
    private TextField propertyIDHUD;

    @FXML
    private TextField fiscalYearHUD;

    @FXML
    private TextField AMIByCountyHUD;

    @FXML
    private TextField ownerHUD;

    @FXML
    private TextField availableUnitsHUD;

    @FXML
    private Button stateSelectionButtonHUD;

    private ContextMenu hudContextMenu;
    private String hudSelectedStateAbbrev = "";

    @FXML
    public void initialize() {
        // Use full state names for display but keep abbreviations for filtering
        String[][] states = new String[][]{
                {"Pennsylvania", "PA"},
                {"Maryland", "MD"},
                {"Delaware", "DE"},
                {"New Jersey", "NJ"},
                {"New York", "NY"}
        };

        // HUD main selection menu
        if (stateSelectionButtonHUD != null) {
            hudContextMenu = new ContextMenu();
            for (String[] pair : states) {
                String full = pair[0];
                String abbr = pair[1];
                MenuItem mi = new MenuItem(full);
                mi.setOnAction(ae -> {
                    if (stateSelectionButtonHUD != null) {
                        stateSelectionButtonHUD.setText(full);
                        hudSelectedStateAbbrev = abbr;
                    }
                });
                hudContextMenu.getItems().add(mi);
            }
        }

        // FMR filter menu
        if (stateFilterButtonFMR != null) {
            filterContextMenuFMR = new ContextMenu();
            for (String[] pair : states) {
                String full = pair[0];
                String abbr = pair[1];
                MenuItem mi = new MenuItem(full);
                mi.setOnAction(ae -> {
                    // store full state name to match ReportGUI.filterReportsByStateFMR which
                    // compares against report.getStateName()
                    filterStateValueFMR = full;
                    stateFilterButtonFMR.setText(full);
                });
                filterContextMenuFMR.getItems().add(mi);
            }
        }

        // PHA filter menu
        if (stateFilterButtonPHA != null) {
            filterContextMenuPHA = new ContextMenu();
            for (String[] pair : states) {
                String full = pair[0];
                String abbr = pair[1];
                MenuItem mi = new MenuItem(full);
                mi.setOnAction(ae -> {
                    // store full state name to match ReportGUI.filterReportsByStatePHA
                    filterStateValuePHA = full;
                    stateFilterButtonPHA.setText(full);
                });
                filterContextMenuPHA.getItems().add(mi);
            }
        }
    }

    @FXML
    void showHUDContextMenu(ActionEvent event) {
        if (hudContextMenu == null || stateSelectionButtonHUD == null) return;
        if (hudContextMenu.isShowing()) {
            hudContextMenu.hide();
        } else {
            hudContextMenu.show(stateSelectionButtonHUD, Side.BOTTOM, 0, 0);
        }
    }

    @FXML
    void showFilterMenuFMR(ActionEvent event) {
        if (filterContextMenuFMR == null || stateFilterButtonFMR == null) return;
        if (filterContextMenuFMR.isShowing()) filterContextMenuFMR.hide();
        else filterContextMenuFMR.show(stateFilterButtonFMR, Side.BOTTOM, 0, 0);
    }

    @FXML
    void showFilterMenuPHA(ActionEvent event) {
        if (filterContextMenuPHA == null || stateFilterButtonPHA == null) return;
        if (filterContextMenuPHA.isShowing()) filterContextMenuPHA.hide();
        else filterContextMenuPHA.show(stateFilterButtonPHA, Side.BOTTOM, 0, 0);
    }


    @FXML //A testing load method that loads reports without having to input the file path by hand
    //NOTE if you want to use this test method just add your own file paths
    void TestLoad(ActionEvent event) throws ParserConfigurationException, IOException, SAXException {
        // Use project-level 'Test Reports' directory relative to working directory
        GUI1.setFilePath(System.getProperty("user.dir") + "\\Test Reports\\TestFMRReport.xml");
        GUI1.openXMLReportFMR();

        GUI1.setFilePath(System.getProperty("user.dir") + "\\Test Reports\\TestPHAReport.xml");
        GUI1.openXMLReportPHA();

        GUI1.setFilePath(System.getProperty("user.dir") + "\\Test Reports\\TestHUDReport.xml");
        GUI1.openXMLReportHUD();


        updateReportGUIFMR();
        updateReportGUIPHA();
        updateReportGUIHUD();
    }

    @FXML //opens the selected report path and update GUI
    void OpenReportFMR(ActionEvent event) {
        String path = (this.ReportPath == null) ? null : this.ReportPath.getText();
        if (path == null || path.trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Open Report");
            a.setHeaderText("No file selected");
            a.setContentText("Please select a report file before opening.");
            a.showAndWait();
            return;
        }

        File f = new File(path);
        if (!f.exists() || !f.isFile()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("File not found");
            a.setContentText("The selected file does not exist: " + path);
            a.showAndWait();
            return;
        }

        GUI1.setFilePath(path);
        try {
            GUI1.openXMLReportFMR();
            updateReportGUIFMR();
        } catch (IndexOutOfBoundsException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("Wrong report type");
            a.setContentText("The selected file is not an FMR report. Please select the correct report type.");
            a.showAndWait();
        } catch (org.xml.sax.SAXException | javax.xml.parsers.ParserConfigurationException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("Invalid XML or wrong report type");
            a.setContentText("The selected file could not be parsed as an FMR report. Please select a valid FMR XML file.");
            a.showAndWait();
        } catch (IOException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("I/O error");
            a.setContentText(ex.getMessage());
            a.showAndWait();
        } catch (Exception ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("Error opening report");
            a.setContentText(ex.getMessage());
            a.showAndWait();
        }
    }

    @FXML //opens the selected report path and update GUI
    void OpenReportPHA(ActionEvent event) {
        String path = (this.ReportPath == null) ? null : this.ReportPath.getText();
        if (path == null || path.trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Open Report");
            a.setHeaderText("No file selected");
            a.setContentText("Please select a report file before opening.");
            a.showAndWait();
            return;
        }

        File f = new File(path);
        if (!f.exists() || !f.isFile()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("File not found");
            a.setContentText("The selected file does not exist: " + path);
            a.showAndWait();
            return;
        }

        GUI1.setFilePath(path);
        try {
            GUI1.openXMLReportPHA();
            updateReportGUIPHA();
        } catch (IndexOutOfBoundsException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("Wrong report type");
            a.setContentText("The selected file is not a PHA report. Please select the correct report type.");
            a.showAndWait();
        } catch (org.xml.sax.SAXException | javax.xml.parsers.ParserConfigurationException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("Invalid XML or wrong report type");
            a.setContentText("The selected file could not be parsed as a PHA report. Please select a valid PHA XML file.");
            a.showAndWait();
        } catch (IOException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("I/O error");
            a.setContentText(ex.getMessage());
            a.showAndWait();
        } catch (Exception ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("Error opening report");
            a.setContentText(ex.getMessage());
            a.showAndWait();
        }
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

    @FXML //decrease the currently selected HUD report by one and update GUI
    void DecreaseCurrentReportHUD(ActionEvent event) {
        GUI1.decreaseCurrentReportHUD();

        updateReportGUIHUD();
    }

    @FXML //increase the currently selected HUD report by one and update GUI
    void IncreaseCurrentReportHUD(ActionEvent event) {
        GUI1.increaseCurrentReportHUD();

        updateReportGUIHUD();
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
        String state = (filterStateValueFMR == null) ? "" : filterStateValueFMR;
        GUI1.filterReportsByStateFMR(state);
        TotalFilteredReports.setText(String.format("%d",GUI1.getNumOfFilteredReportsFMR()));
        updateReportGUIFMR();
    }

    @FXML //apply the PHA filters and update the GUI
    void FilterButtonPHA(ActionEvent event) {
        GUI1.resetFilterReportListPHA();
        String state = (filterStateValuePHA == null) ? "" : filterStateValuePHA;
        GUI1.filterReportsByStatePHA(state);
        TotalFilteredReportsPHA.setText(String.format("%d", GUI1.getNumOfFilteredReportsPHA()));
        updateReportGUIPHA();
    }

    @FXML//toggle the filter on/off and update GUI
    void ToggleFiltersFMR(ActionEvent event) {
        GUI1.toggleFilterFMR();
        updateReportGUIFMR();
    }

    @FXML//toggle the PHA filter on/off and update GUI
    void ToggleFiltersPHA(ActionEvent event) {
        GUI1.toggleFilterPHA();
        updateReportGUIPHA();
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

    //update HUD text fields (basic currently: current/total counts)
    private void updateReportGUIHUD(){
        if (GUI1.getNumOfReportsHUD() == 0) {
            if (CurrentReportHUD != null) CurrentReportHUD.setText("");
            if (TotalReportsHUD != null) TotalReportsHUD.setText("0");
            if (propertyAddressHUD != null) propertyAddressHUD.setText("");
            if (stateHUD != null) stateHUD.setText("");
            if (countyHUD != null) countyHUD.setText("");
            if (zipcodeHUD != null) zipcodeHUD.setText("");
            if (propertyIDHUD != null) propertyIDHUD.setText("");
            if (fiscalYearHUD != null) fiscalYearHUD.setText("");
            if (AMIByCountyHUD != null) AMIByCountyHUD.setText("");
            if (ownerHUD != null) ownerHUD.setText("");
            if (availableUnitsHUD != null) availableUnitsHUD.setText("");
            return;
        }

        if (CurrentReportHUD != null)
            CurrentReportHUD.setText(String.format("%d", GUI1.getCurrentReportHUD()+1));
        if (TotalReports != null)
            TotalReports.setText(String.format("%d", GUI1.getTotalNumOfReports()));
        if (TotalReportsHUD != null)
            TotalReportsHUD.setText(String.format("%d", GUI1.getNumOfReportsHUD()));

        if (propertyAddressHUD != null)
            propertyAddressHUD.setText(String.format(GUI1.getCurrentHUDReportPropertyAddress()));
        if (stateHUD != null)
            stateHUD.setText(String.format(GUI1.getCurrentHUDReportState()));
        if (countyHUD != null)
            countyHUD.setText(String.format(GUI1.getCurrentHUDReportCounty()));
        if (zipcodeHUD != null)
            zipcodeHUD.setText(String.format(GUI1.getCurrentHUDReportZipCode()));
        if (propertyIDHUD != null)
            propertyIDHUD.setText(String.format(GUI1.getCurrentHUDReportPropertyID()));
        if (fiscalYearHUD != null)
            fiscalYearHUD.setText(String.format(GUI1.getCurrentHUDReportFiscalYear()));
        if (AMIByCountyHUD != null) {
            String ami = GUI1.getCurrentHUDReportAMIByCounty();
            if (ami == null || ami.trim().isEmpty()) {
                AMIByCountyHUD.setText("");
            } else {
                AMIByCountyHUD.setText(String.format("$%s", ami));
            }
        }
        if (ownerHUD != null)
            ownerHUD.setText(String.format(GUI1.getCurrentHUDReportOwner()));
        if (availableUnitsHUD != null)
            availableUnitsHUD.setText(String.format(GUI1.getCurrentHUDAvailableUnits()));
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

        // Clear HUD fields
        if (CurrentReportHUD != null) CurrentReportHUD.setText("");
        if (TotalReportsHUD != null) TotalReportsHUD.setText("0");
        if (propertyAddressHUD != null) propertyAddressHUD.setText("");
        if (stateHUD != null) stateHUD.setText("");
        if (countyHUD != null) countyHUD.setText("");
        if (zipcodeHUD != null) zipcodeHUD.setText("");
        if (propertyIDHUD != null) propertyIDHUD.setText("");
        if (fiscalYearHUD != null) fiscalYearHUD.setText("");
        if (AMIByCountyHUD != null) AMIByCountyHUD.setText("");
        if (ownerHUD != null) ownerHUD.setText("");
        if (availableUnitsHUD != null) availableUnitsHUD.setText("");

    }

}
