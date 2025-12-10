package crawlers.reportgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.application.Platform;
// import java.util.Arrays; // unused after changes
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.Map;
import java.util.HashMap;
import javafx.geometry.Side;
// unused imports removed
import javafx.stage.FileChooser;

import java.io.File;
import org.xml.sax.SAXException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class ReportGUIController {
    //BASE GUI USED FOR ALL REPORTS
    private ReportGUI GUI1 = new ReportGUI();
    // remember last directory used by the file chooser during this session
    private File lastBrowseDir = null;

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
    private TextField ManualEnterReportHUD;

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

        // If we have a remembered directory from this session, open there
        try {
            if (lastBrowseDir != null && lastBrowseDir.exists() && lastBrowseDir.isDirectory()) {
                chooser.setInitialDirectory(lastBrowseDir);
            } else {
                // fall back to user's home folder
                File home = new File(System.getProperty("user.home"));
                if (home.exists() && home.isDirectory()) chooser.setInitialDirectory(home);
            }
        } catch (Exception ignored) {}

        File selected = chooser.showOpenDialog(ReportPath.getScene().getWindow());
        if (selected != null) {
            String path = selected.getAbsolutePath();
            // populate the existing ReportPath field (top browse field)
            ReportPath.setText(path);
            // remember the folder for next time
            try { lastBrowseDir = selected.getParentFile(); } catch (Exception ignored) {}
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
            ArrayList<HUDReport> tmp = GUI1.parseReportsHUD();
            if (tmp == null || tmp.size() == 0 || !GUI1.listHasValidHUDReports(tmp)){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Open Report");
                a.setHeaderText("Wrong report type");
                a.setContentText("The selected file is not a HUD report. Please select the correct report type.");
                a.showAndWait();
                return;
            }

            GUI1.addHUDReports(tmp);
            // ensure the HUD selection is set to first report so view populates
            if (GUI1.getNumOfReportsHUD() > 0) GUI1.setCurrentReportHUD(0);
            System.out.println("[DEBUG] OpenReportHUD: added tmp=" + (tmp == null ? 0 : tmp.size()) + " totalHUD=" + GUI1.getNumOfReportsHUD());

            updateReportGUIHUD();
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
        } catch (IndexOutOfBoundsException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Open Report");
            a.setHeaderText("Wrong report type");
            a.setContentText("The selected file is not a HUD report. Please select the correct report type.");
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
    private TextField TotalFilteredReportsHUD;

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
    private TextField TenantIncomePHA;

    @FXML
    private TextField MedTenantIncomePHA;

    @FXML
    private TextField AvgHCVUnitsPHA;

    @FXML
    private TextField HCVUnitsPHA;

    @FXML
    private TextField AvgHCVUtilRatePHA;

    @FXML
    private TextField HCVUtilRatePHA;

    @FXML
    private TextField AvgInspectCompPHA;

    @FXML
    private TextField InspectCompPHA;

    @FXML
    private TextField AvgOccupancyRatePHA;

    @FXML
    private TextField OccupancyRatePHA;

    @FXML
    private TextField CurrentReportHUD;

    @FXML
    private TextField TotalReportsHUD;

    @FXML
    private ImageView chart1View;

    @FXML
    private ImageView chart2View;

    @FXML
    private ImageView chart3View;

    @FXML
    private ImageView chart4View;

    @FXML
    private ImageView chart5View;

    @FXML
    private ImageView chart6View;

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
    private Button FilterButtonHUD;

    @FXML
    private CheckBox UseFiltersHUD;

    @FXML
    private TextField avgMedianIncomeHUD;

    @FXML
    private TextField avgVacancyHUD;

    @FXML
    private TextField avgInspectScoreHUD;

    @FXML
    private Button stateSelectionButtonHUD;

    private ContextMenu hudContextMenu;
    // hudSelectedStateAbbrev removed; we only use full state names for filtering
    private String filterStateValueHUD = ""; // full state name for HUD filtering

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
                MenuItem mi = new MenuItem(full);
                mi.setOnAction(ae -> {
                    if (stateSelectionButtonHUD != null) {
                        stateSelectionButtonHUD.setText(full);
                        filterStateValueHUD = full;
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
    void generateAnalysis(ActionEvent event) {
        // Require at least one of each report type (FMR, PHA, HUD) before generating analysis
        int numFMR = 0, numPHA = 0, numHUD = 0;
        try {
            numFMR = GUI1.getNumOfReportsFMR();
            numPHA = GUI1.getNumOfReportsPHA();
            numHUD = GUI1.getNumOfReportsHUD();
        } catch (Exception ignored) {}
        if (numFMR == 0 || numPHA == 0 || numHUD == 0) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Analysis");
            a.setHeaderText("Insufficient reports loaded");
            a.setContentText("Please load at least one FMR, one PHA, and one HUD report before generating analysis.");
            a.showAndWait();
            return;
        }

        final boolean hasHUD = numHUD > 0;
        final boolean hasPHA = numPHA > 0;

        // Run the Python analysis script in a background thread, then load generated PNGs
        Thread t = new Thread(() -> {
            try {
                String projectDir = System.getProperty("user.dir");
                File script = new File(projectDir + File.separator + "scripts" + File.separator + "analysis_charts.py");
                File outDir = new File(projectDir + File.separator + "scripts" + File.separator + "analysis_output");
                if (!script.exists()) {
                    Platform.runLater(() -> {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Analysis");
                        a.setHeaderText("Script not found");
                        a.setContentText("Could not find analysis script: " + script.getAbsolutePath());
                        a.showAndWait();
                    });
                    return;
                }

                // Build command and pass flags indicating which charts to produce (HUD/ PHA)
                java.util.List<String> cmd = new java.util.ArrayList<>();
                cmd.add("python");
                cmd.add(script.getAbsolutePath());
                cmd.add(outDir.getAbsolutePath());
                if (hasHUD) cmd.add("--hud");
                if (hasPHA) cmd.add("--pha");
                ProcessBuilder pb = new ProcessBuilder(cmd);
                pb.directory(new File(projectDir));
                pb.redirectErrorStream(true);
                Process p = pb.start();
                // read output (optional)
                try (java.io.InputStream is = p.getInputStream();
                     java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A")) {
                    String out = s.hasNext() ? s.next() : "";
                    System.out.println(out);
                }
                p.waitFor();

                // After script runs, load images if present
                File c1 = new File(outDir, "chart_inspection.png");
                File c2 = new File(outDir, "chart_vacancy.png");
                File c3 = new File(outDir, "chart_ami.png");
                File c4 = new File(outDir, "chart_median_tenant_income.png");
                File c5 = new File(outDir, "chart_assisted_units.png");
                File c6 = new File(outDir, "chart_rent_burden.png");

                Platform.runLater(() -> {
                    try {
                        if (c1.exists() && chart1View != null) chart1View.setImage(new Image(c1.toURI().toString()));
                        if (c2.exists() && chart2View != null) chart2View.setImage(new Image(c2.toURI().toString()));
                        if (c3.exists() && chart3View != null) chart3View.setImage(new Image(c3.toURI().toString()));
                        if (c4.exists() && chart4View != null) chart4View.setImage(new Image(c4.toURI().toString()));
                        if (c5.exists() && chart5View != null) chart5View.setImage(new Image(c5.toURI().toString()));
                        if (c6.exists() && chart6View != null) chart6View.setImage(new Image(c6.toURI().toString()));
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setTitle("Analysis");
                        a.setHeaderText("Charts generated");
                        a.setContentText("Analysis charts generated and loaded.");
                        a.showAndWait();
                    } catch (Exception ex) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Analysis");
                        a.setHeaderText("Error loading charts");
                        a.setContentText(ex.getMessage());
                        a.showAndWait();
                    }
                });
            } catch (Exception ex) {
                // If Python script fails, fall back to JavaFX chart generation
                Platform.runLater(() -> {
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Analysis");
                    a.setHeaderText("Python analysis failed");
                    a.setContentText("Falling back to internal JavaFX charts: " + ex.getMessage());
                    a.showAndWait();
                    generateChartsJavaFX();
                });
            }
        });
        t.setDaemon(true);
        t.start();
    }

    // Fallback: create simple bar charts with JavaFX from loaded HUD reports and snapshot to ImageViews
    private void generateChartsJavaFX() {
        // Gather HUD reports
        java.util.List<HUDReport> reports = GUI1.getHudReports();

        // Compute inspection values for histogram (we'll bucket into ranges)
        java.util.List<Double> inspections = new java.util.ArrayList<>();
        Map<String, java.util.List<Double>> vacancyByCounty = new HashMap<>();
        Map<String, java.util.List<Double>> amiByState = new HashMap<>();

        for (HUDReport r : reports) {
            try {
                String s = r.getHudInspectionScore();
                if (s != null && !s.trim().isEmpty()) inspections.add(Double.parseDouble(s));
            } catch (Exception ignored) {}
            try {
                String v = r.getVacancyRate();
                if (v != null && !v.trim().isEmpty()) {
                    double dv = Double.parseDouble(v);
                    vacancyByCounty.computeIfAbsent(r.getCountyName()==null?"":r.getCountyName(), k -> new java.util.ArrayList<>()).add(dv);
                }
            } catch (Exception ignored) {}
            try {
                String a = r.getAmiMedianFamilyIncome();
                if (a != null && !a.trim().isEmpty()) {
                    double da = Double.parseDouble(a);
                    amiByState.computeIfAbsent(r.getStateName()==null?"":r.getStateName(), k -> new java.util.ArrayList<>()).add(da);
                }
            } catch (Exception ignored) {}
        }

        // Create charts on JavaFX thread
        Platform.runLater(() -> {
            try {
                // Chart 1: Inspection histogram as bar chart of buckets
                CategoryAxis x1 = new CategoryAxis();
                NumberAxis y1 = new NumberAxis();
                BarChart<String, Number> bc1 = new BarChart<>(x1, y1);
                bc1.setLegendVisible(false);
                bc1.setTitle("HUD Inspection Score Distribution");
                // buckets 60-70,70-80,...,100
                Map<String, Integer> buckets = new HashMap<>();
                for (double v = 60; v < 100; v += 5) buckets.put(String.format("%.0f-%.0f", v, v+5), 0);
                for (Double val : inspections) {
                    double vv = Math.max(60, Math.min(99.9, val));
                    double floor = Math.floor((vv - 60) / 5) * 5 + 60;
                    String key = String.format("%.0f-%.0f", floor, floor+5);
                    buckets.put(key, buckets.getOrDefault(key, 0) + 1);
                }
                XYChart.Series<String, Number> s1 = new XYChart.Series<>();
                for (Map.Entry<String, Integer> e : buckets.entrySet()) s1.getData().add(new XYChart.Data<>(e.getKey(), e.getValue()));
                bc1.getData().add(s1);

                // Chart 2: Average vacancy by county
                CategoryAxis x2 = new CategoryAxis();
                NumberAxis y2 = new NumberAxis();
                BarChart<String, Number> bc2 = new BarChart<>(x2, y2);
                bc2.setLegendVisible(false);
                bc2.setTitle("Average Vacancy Rate by County");
                XYChart.Series<String, Number> s2 = new XYChart.Series<>();
                for (Map.Entry<String, java.util.List<Double>> e : vacancyByCounty.entrySet()) {
                    double avg = e.getValue().stream().mapToDouble(d -> d).average().orElse(0.0);
                    s2.getData().add(new XYChart.Data<>(e.getKey()==null?"":e.getKey(), avg * 100));
                }
                bc2.getData().add(s2);

                // Chart 3: Average AMI by state
                CategoryAxis x3 = new CategoryAxis();
                NumberAxis y3 = new NumberAxis();
                BarChart<String, Number> bc3 = new BarChart<>(x3, y3);
                bc3.setLegendVisible(false);
                bc3.setTitle("Average AMI by State");
                XYChart.Series<String, Number> s3 = new XYChart.Series<>();
                for (Map.Entry<String, java.util.List<Double>> e : amiByState.entrySet()) {
                    double avg = e.getValue().stream().mapToDouble(d -> d).average().orElse(0.0);
                    s3.getData().add(new XYChart.Data<>(e.getKey()==null?"":e.getKey(), avg));
                }
                bc3.getData().add(s3);

                // snapshot these charts to images and set into ImageViews
                SnapshotParameters sp = new SnapshotParameters();
                sp.setFill(Color.TRANSPARENT);
                WritableImage wi1 = bc1.snapshot(sp, new WritableImage(800, 400));
                WritableImage wi2 = bc2.snapshot(sp, new WritableImage(800, 400));
                WritableImage wi3 = bc3.snapshot(sp, new WritableImage(800, 400));
                if (chart1View != null) chart1View.setImage(wi1);
                if (chart2View != null) chart2View.setImage(wi2);
                if (chart3View != null) chart3View.setImage(wi3);
                // Create chart 4 (median tenant income by county) from PHA reports
                try {
                    java.util.List<PHAReport> pha = GUI1.getPhaReports();
                    Map<String, java.util.List<Double>> incByCounty = new HashMap<>();
                    for (PHAReport p : pha) {
                        try {
                            String county = p.getCountyName();
                            String ai = p.getAvgTenantIncome();
                            if (county != null && ai != null && !ai.trim().isEmpty()) {
                                double v = Double.parseDouble(ai);
                                incByCounty.computeIfAbsent(county==null?"":county, k -> new java.util.ArrayList<>()).add(v);
                            }
                        } catch (Exception ignored) {}
                    }
                    CategoryAxis x4 = new CategoryAxis();
                    NumberAxis y4 = new NumberAxis();
                    BarChart<String, Number> bc4 = new BarChart<>(x4, y4);
                    bc4.setLegendVisible(false);
                    bc4.setTitle("Median Tenant Income by County");
                    XYChart.Series<String, Number> s4 = new XYChart.Series<>();
                    for (Map.Entry<String, java.util.List<Double>> e : incByCounty.entrySet()) {
                        java.util.List<Double> vals = e.getValue();
                        double median = 0.0;
                        if (!vals.isEmpty()) {
                            java.util.Collections.sort(vals);
                            int n = vals.size();
                            if (n % 2 == 1) median = vals.get(n/2);
                            else median = (vals.get(n/2 - 1) + vals.get(n/2)) / 2.0;
                        }
                        s4.getData().add(new XYChart.Data<>(e.getKey()==null?"":e.getKey(), median));
                    }
                    bc4.getData().add(s4);
                    WritableImage wi4 = bc4.snapshot(sp, new WritableImage(800, 400));
                    if (chart4View != null) chart4View.setImage(wi4);
                    // Chart 5: Total assisted units by county (HUD)
                    try {
                        Map<String, Integer> assistedByCounty = new HashMap<>();
                        for (HUDReport h : reports) {
                            try {
                                String county = h.getCountyName();
                                String au = h.getAssistedUnits();
                                if (county != null && au != null && !au.trim().isEmpty()) {
                                    int v = Integer.parseInt(au);
                                    assistedByCounty.put(county==null?"":county, assistedByCounty.getOrDefault(county==null?"":county, 0) + v);
                                }
                            } catch (Exception ignored) {}
                        }
                        CategoryAxis x5 = new CategoryAxis();
                        NumberAxis y5 = new NumberAxis();
                        BarChart<String, Number> bc5 = new BarChart<>(x5, y5);
                        bc5.setLegendVisible(false);
                        bc5.setTitle("Total Assisted Units by County");
                        XYChart.Series<String, Number> s5 = new XYChart.Series<>();
                        for (Map.Entry<String, Integer> e : assistedByCounty.entrySet()) {
                            s5.getData().add(new XYChart.Data<>(e.getKey()==null?"":e.getKey(), e.getValue()));
                        }
                        bc5.getData().add(s5);
                        WritableImage wi5 = bc5.snapshot(sp, new WritableImage(800, 400));
                        if (chart5View != null) chart5View.setImage(wi5);
                    } catch (Exception ignored) {}
                    // Chart 6: Average tenant rent share by county (PHA)
                    try {
                        Map<String, java.util.List<Double>> rentShareByCounty = new HashMap<>();
                        for (PHAReport p : pha) {
                            try {
                                String county = p.getCountyName();
                                String rs = p.getAvgTenantRentShare();
                                if (county != null && rs != null && !rs.trim().isEmpty()) {
                                    double v = Double.parseDouble(rs);
                                    rentShareByCounty.computeIfAbsent(county==null?"":county, k -> new java.util.ArrayList<>()).add(v);
                                }
                            } catch (Exception ignored) {}
                        }
                        CategoryAxis x6 = new CategoryAxis();
                        NumberAxis y6 = new NumberAxis();
                        BarChart<String, Number> bc6 = new BarChart<>(x6, y6);
                        bc6.setLegendVisible(false);
                        bc6.setTitle("Average Tenant Rent Share by County");
                        XYChart.Series<String, Number> s6 = new XYChart.Series<>();
                        for (Map.Entry<String, java.util.List<Double>> e : rentShareByCounty.entrySet()) {
                            double avg = e.getValue().stream().mapToDouble(d -> d).average().orElse(0.0);
                            s6.getData().add(new XYChart.Data<>(e.getKey()==null?"":e.getKey(), avg * 100));
                        }
                        bc6.getData().add(s6);
                        WritableImage wi6 = bc6.snapshot(sp, new WritableImage(800, 400));
                        if (chart6View != null) chart6View.setImage(wi6);
                    } catch (Exception ignored) {}
                } catch (Exception ignored) {}
            } catch (Exception ex) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Analysis");
                a.setHeaderText("Error generating charts");
                a.setContentText(ex.getMessage());
                a.showAndWait();
            }
        });
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
        ArrayList<FMRReport> tmpFMR = GUI1.parseReportsFMR();
        if (tmpFMR != null && tmpFMR.size() > 0 && GUI1.listHasValidFMRReports(tmpFMR)) {
            GUI1.addFMRReports(tmpFMR);
        }

        GUI1.setFilePath(System.getProperty("user.dir") + "\\Test Reports\\TestPHAReport.xml");
        ArrayList<PHAReport> tmpPHA = GUI1.parseReportsPHA();
        if (tmpPHA != null && tmpPHA.size() > 0 && GUI1.listHasValidPHAReports(tmpPHA)) {
            GUI1.addPHAReports(tmpPHA);
        }

        String testHudPath = System.getProperty("user.dir") + "\\Test Reports\\TestHUDReport.xml";
        try {
            loadAndSelectFirstHUD(testHudPath);
            System.out.println("[DEBUG] TestLoad: helper loaded totalHUD=" + GUI1.getNumOfReportsHUD());
        } catch (Exception e) {
            System.out.println("[DEBUG] TestLoad: failed to load HUD test file: " + e.getMessage());
        }

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
            ArrayList<FMRReport> tmp = GUI1.parseReportsFMR();
            if (tmp == null || tmp.size() == 0 || !GUI1.listHasValidFMRReports(tmp)){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Open Report");
                a.setHeaderText("Wrong report type");
                a.setContentText("The selected file is not an FMR report. Please select the correct report type.");
                a.showAndWait();
                return;
            }
            GUI1.addFMRReports(tmp);
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

    // Helper: load HUD reports from given path, add them, select the first report and update GUI
    private void loadAndSelectFirstHUD(String path) throws org.xml.sax.SAXException, javax.xml.parsers.ParserConfigurationException, IOException {
        GUI1.setFilePath(path);
        ArrayList<HUDReport> tmp = GUI1.parseReportsHUD();
        if (tmp == null || tmp.size() == 0 || !GUI1.listHasValidHUDReports(tmp)){
            throw new IndexOutOfBoundsException("Not a HUD report or no HUD reports found");
        }
        GUI1.addHUDReports(tmp);
        if (GUI1.getNumOfReportsHUD() > 0) GUI1.setCurrentReportHUD(0);
        updateReportGUIHUD();
    }
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
            ArrayList<PHAReport> tmp = GUI1.parseReportsPHA();
            if (tmp == null || tmp.size() == 0 || !GUI1.listHasValidPHAReports(tmp)){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Open Report");
                a.setHeaderText("Wrong report type");
                a.setContentText("The selected file is not a PHA report. Please select the correct report type.");
                a.showAndWait();
                return;
            }

            GUI1.addPHAReports(tmp);

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

    @FXML //get the ID that the user manually entered and go to it (HUD)
    void GetManualReportHUD(ActionEvent event) {
        int tempReportID = Integer.parseInt(this.ManualEnterReportHUD.getText());

        if (tempReportID > 0 && tempReportID < GUI1.getNumOfReportsHUD()+1){
            GUI1.setCurrentReportHUD(tempReportID-1);
            updateReportGUIHUD();
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

    @FXML //apply HUD filter and update GUI
    void FilterButtonHUD(ActionEvent event) {
        GUI1.resetFilterReportListHUD();
        String state = (filterStateValueHUD == null) ? "" : filterStateValueHUD;
        GUI1.filterReportsByStateHUD(state);
        if (TotalFilteredReportsHUD != null)
            TotalFilteredReportsHUD.setText(String.format("%d", GUI1.getNumOfFilteredReportsHUD()));
        updateReportGUIHUD();
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

    @FXML//toggle the HUD filter on/off and update GUI
    void ToggleFiltersHUD(ActionEvent event) {
        GUI1.toggleFilterHUD();
        updateReportGUIHUD();
    }

    //update all FMR text fields
    private void updateReportGUIFMR(){
        CurrentReport.setText(String.format("%d",GUI1.getCurrentReportFMR()+1));
        TotalReports.setText(String.format("%d",GUI1.getTotalNumOfReports()));
        TotalReportsFMR.setText(String.format("%d",GUI1.getNumOfReportsFMR()));

        String fy = GUI1.getCurrentFMRReportFiscalYear();
        FiscalYear.setText(fy == null ? "" : fy);
        String county = GUI1.getCurrentFMRReportCountyName();
        CountyName.setText(county == null ? "" : county);
        String nb = GUI1.getCurrentFMRReportNumOfBedrooms();
        NumBedrooms.setText(nb == null ? "" : nb);
        String fmrNum = GUI1.getCurrentFMRReportFMRNumber();
        FMRNumber.setText(fmrNum == null ? "" : String.format("$%s", fmrNum));
        String hh = GUI1.getCurrentFMRReportHouseholdIncome();
        HouseholdIncome.setText(hh == null ? "" : String.format("$%s", hh));
        String mtype = GUI1.getCurrentFMRReportMarketType();
        MarketType.setText(mtype == null ? "" : mtype);
        String zip = GUI1.getCurrentFMRReportZipCode();
        ZipCode.setText(zip == null ? "" : zip);
        String state = GUI1.getCurrentFMRReportState();
        State.setText(state == null ? "" : state);

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
    private void updateReportGUIPHA() {
        CurrentReportPHA.setText(String.format("%d", GUI1.getCurrentReportPHA() + 1));
        TotalReports.setText(String.format("%d", GUI1.getTotalNumOfReports()));
        TotalReportsPHA.setText(String.format("%d", GUI1.getNumOfReportsPHA()));

        String sPHA = GUI1.getCurrentPHAReportState();
        StatePHA.setText(sPHA == null ? "" : sPHA);
        String city = GUI1.getCurrentPHAReportCity();
        CityPHA.setText(city == null ? "" : city);
        String countyPHA = GUI1.getCurrentPHAReportCounty();
        CountyNamePHA.setText(countyPHA == null ? "" : countyPHA);
        String zipPHA = GUI1.getCurrentPHAReportZipCode();
        ZipCodePHA.setText(zipPHA == null ? "" : zipPHA);
        String addrPHA = GUI1.getCurrentPHAReportAddress();
        AddressPHA.setText(addrPHA == null ? "" : addrPHA);
        String jur = GUI1.getCurrentPHAReportJurisdiction();
        JurisdictionPHA.setText(jur == null ? "" : jur);
        String code = GUI1.getCurrentPHAReportCode();
        PHACode.setText(code == null ? "" : code);
        String dir = GUI1.getCurrentPHAReportDirector();
        DirectorPHA.setText(dir == null ? "" : dir);
        String phone = GUI1.getCurrentPHAReportPhone();
        PhonePHA.setText(phone == null ? "" : phone);
        String fax = GUI1.getCurrentPHAReportFax();
        FaxPHA.setText(fax == null ? "" : fax);
        String email = GUI1.getCurrentPHAReportEmail();
        EmailPHA.setText(email == null ? "" : email);
        String numUnitsPHA = GUI1.getCurrentPHANumberUnits();
        HCVUnitsPHA.setText(numUnitsPHA == null ? "" : numUnitsPHA);
        String occupRatePHA = GUI1.getCurrentPHAOccupancyRate();
        if (OccupancyRatePHA != null) {
            if (occupRatePHA == null || occupRatePHA.trim().isEmpty()) {
                OccupancyRatePHA.setText("");
            } else {
                try {
                    double v = Double.parseDouble(occupRatePHA);
                    OccupancyRatePHA.setText(String.format("%.0f%%", v * 100));
                } catch (NumberFormatException ex) {
                    OccupancyRatePHA.setText(occupRatePHA);
                }
            }
        }

        String HCVUtilPHA = GUI1.getCurrentPHAHCVUtilRate();
        if (HCVUtilRatePHA != null) {
            if (HCVUtilPHA == null || HCVUtilPHA.trim().isEmpty()) {
                HCVUtilRatePHA.setText("");
            } else {
                try {
                    double v = Double.parseDouble(HCVUtilPHA);
                    HCVUtilRatePHA.setText(String.format("%.0f%%", v * 100));
                } catch (NumberFormatException ex) {
                    HCVUtilRatePHA.setText(HCVUtilPHA);
                }
            }
        }

        String compPHA = GUI1.getCurrentPHAInspectionComplianceRate();
        if (InspectCompPHA != null) {
            if (compPHA == null || compPHA.trim().isEmpty()) {
                InspectCompPHA.setText("");
            } else {
                try {
                    double v = Double.parseDouble(compPHA);
                    InspectCompPHA.setText(String.format("%.0f%%", v * 100));
                } catch (NumberFormatException ex) {
                    InspectCompPHA.setText(compPHA);
                }
            }
        }
        String tenIncomePHA = GUI1.getCurrentPHAAvgTenantIncome();
        if (TenantIncomePHA != null) {
            if (tenIncomePHA == null || tenIncomePHA.trim().isEmpty()) {
                TenantIncomePHA.setText("");
            } else {
                try {
                    double v = Double.parseDouble(tenIncomePHA);
                    TenantIncomePHA.setText(String.format("$%.2f", v));
                } catch (NumberFormatException ex) {
                    TenantIncomePHA.setText(tenIncomePHA);
                }
            }
        }
        // ... existing PHA field updates ...

        //Display average HCV Utilisation Rate
        double avgHcvUtil = GUI1.getAverageHcvUtilRatePHA();
        if (Double.isNaN(avgHcvUtil))
            AvgHCVUtilRatePHA.setText("");
        else
            AvgHCVUtilRatePHA.setText(String.format("%.2f%%", avgHcvUtil * 100));  // Display as percentage

        //Display average Occupancy Rate
        double avgOcc = GUI1.getAvgOccupancyRatePHA();
        if (Double.isNaN(avgOcc))
            AvgOccupancyRatePHA.setText("");
        else
            AvgOccupancyRatePHA.setText(String.format("%.2f%%", avgOcc * 100));

        //Display average Inspection Compliance Rate
        double avgComp = GUI1.getAvgInspectionRatePHA();
        if (Double.isNaN(avgComp))
            AvgInspectCompPHA.setText("");
        else
            AvgInspectCompPHA.setText(String.format("%.2f%%", avgComp * 100));

        //Display median Tenant Income
        double medIncome = GUI1.getMedianTenantIncomePHA();
        if (Double.isNaN(medIncome))
            MedTenantIncomePHA.setText("0.00");
        else
            MedTenantIncomePHA.setText(String.format("$%.2f", medIncome));
           

        //Display average HCV Units
        double avgUnits = GUI1.getAvgHcvUnitsPHA();

        if (Double.isNaN(avgUnits))
            AvgHCVUnitsPHA.setText("");
        else
            AvgHCVUnitsPHA.setText(String.format("%.0f", avgUnits));

    }

    //update HUD text fields (basic currently: current/total counts)
    private void updateReportGUIHUD(){
        if (GUI1.getNumOfReportsHUD() == 0) {
            if (CurrentReportHUD != null) CurrentReportHUD.setText("");
            if (TotalReportsHUD != null) TotalReportsHUD.setText("0");
            if (TotalFilteredReportsHUD != null) TotalFilteredReportsHUD.setText("0");
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
        if (TotalFilteredReportsHUD != null)
            TotalFilteredReportsHUD.setText(String.format("%d", GUI1.getNumOfFilteredReportsHUD()));

        if (propertyAddressHUD != null)
            propertyAddressHUD.setText(GUI1.getCurrentHUDReportPropertyAddress() == null ? "" : GUI1.getCurrentHUDReportPropertyAddress());
        if (stateHUD != null)
            stateHUD.setText(GUI1.getCurrentHUDReportState() == null ? "" : GUI1.getCurrentHUDReportState());
        if (countyHUD != null)
            countyHUD.setText(GUI1.getCurrentHUDReportCounty() == null ? "" : GUI1.getCurrentHUDReportCounty());
        if (zipcodeHUD != null)
            zipcodeHUD.setText(GUI1.getCurrentHUDReportZipCode() == null ? "" : GUI1.getCurrentHUDReportZipCode());
        if (propertyIDHUD != null)
            propertyIDHUD.setText(GUI1.getCurrentHUDReportPropertyID() == null ? "" : GUI1.getCurrentHUDReportPropertyID());
        if (fiscalYearHUD != null)
            fiscalYearHUD.setText(GUI1.getCurrentHUDReportFiscalYear() == null ? "" : GUI1.getCurrentHUDReportFiscalYear());
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

        // Populate report-set averages for HUD
        double avgAmi = GUI1.getAverageHudMedianIncome();
        if (avgMedianIncomeHUD != null) {
            if (Double.isNaN(avgAmi)) avgMedianIncomeHUD.setText("");
            else avgMedianIncomeHUD.setText(String.format("$%.2f", avgAmi));
        }

        double avgVac = GUI1.getAverageHudVacancyRate();
        if (avgVacancyHUD != null) {
            if (Double.isNaN(avgVac)) avgVacancyHUD.setText("");
            else avgVacancyHUD.setText(String.format("%.2f%%", avgVac * 100));
        }

        double avgInspect = GUI1.getAverageHudInspectionScore();
        if (avgInspectScoreHUD != null) {
            if (Double.isNaN(avgInspect)) avgInspectScoreHUD.setText("");
            else avgInspectScoreHUD.setText(String.format("%.1f%%", avgInspect));
        }
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
        HCVUnitsPHA.setText("");
        OccupancyRatePHA.setText("");
        HCVUtilRatePHA.setText("");
        InspectCompPHA.setText("");
        TenantIncomePHA.setText("");

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
