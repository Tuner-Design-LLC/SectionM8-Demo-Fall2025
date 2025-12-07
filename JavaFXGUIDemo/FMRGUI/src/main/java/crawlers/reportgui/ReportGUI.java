package crawlers.reportgui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class ReportGUI {
    private String filePath;
    private Boolean filterEnabledFMR;
    private Boolean filterEnabledPHA;

    private int currentReportFMR;
    private int currentReportPHA;
    private ArrayList<FMRReport> FMRreports = new ArrayList<>();
    private ArrayList<PHAReport> PHAreports = new ArrayList<>();
    private ArrayList<FMRReport> FMRReportsFiltered = new ArrayList<>();
    private ArrayList<PHAReport> PHAReportsFiltered = new ArrayList<>();
    private ArrayList<HUDReport> HUDreports = new ArrayList<>();
    private ArrayList<HUDReport> HUDReportsFiltered = new ArrayList<>();
    private Boolean filterEnabledHUD;
     

    public ReportGUI(){
        filterEnabledFMR = false;
        filterEnabledPHA = false;
        filterEnabledHUD = false;
       
        currentReportFMR =0;
        currentReportPHA=0;
        currentReportHUD = 0;
    }

    //HUD report tracking
    private int currentReportHUD;

    public int getNumOfReportsHUD(){
        return HUDreports.size();
    }

    public int getNumOfFilteredReportsHUD(){
        return HUDReportsFiltered.size();
    }

    //create a filtered list of reports by state HUD
    public void filterReportsByStateHUD(String stateKey){
        for(HUDReport report: HUDreports){
            if(Objects.equals(report.getStateName(), stateKey)){
                HUDReportsFiltered.add(report);
            }
        }
    }

    //remove all filtered data HUD
    public void resetFilterReportListHUD(){
        HUDReportsFiltered = new ArrayList<>();
    }

    //toggle the filter on / off HUD
    public void toggleFilterHUD(){
        filterEnabledHUD = !filterEnabledHUD;
    }

    // Returns true if at least one parsed HUD report contains expected HUD fields
    public boolean hasValidHUDReports(){
        for (HUDReport r : HUDreports){
            if (r == null) continue;
            String pid = r.getPropertyId();
            String addr = r.getPropertyAddress();
            String ami = r.getAmiMedianFamilyIncome();
            if ((pid != null && !pid.trim().isEmpty()) || (addr != null && !addr.trim().isEmpty()) || (ami != null && !ami.trim().isEmpty())){
                return true;
            }
        }
        return false;
    }

    public int getCurrentReportHUD(){
        return currentReportHUD;
    }

    public void setCurrentReportHUD(int report){
        currentReportHUD = report;
    }

    public void increaseCurrentReportHUD(){
        if (currentReportHUD +1 < getNumOfReportsHUD()){
            currentReportHUD++;
        }
    }

    public void decreaseCurrentReportHUD(){
        if (currentReportHUD > 0){
            currentReportHUD--;
        }
    }

    //remove all HUD reports
    public void resetHUDReportList(){
        HUDreports = new ArrayList<>();
        currentReportHUD = 0;
    }

    //create a filtered list of reports by state PHA
    public void filterReportsByStatePHA(String stateKey){
        for(PHAReport report: PHAreports){
            if(Objects.equals(report.getStateName(), stateKey)){
                PHAReportsFiltered.add(report);
            }
        }
    }

    //remove all filtered data PHA
    public void resetFilterReportListPHA(){
        PHAReportsFiltered = new ArrayList<>();
    }

    //get all num of filtered reports PHA
    public int getNumOfFilteredReportsPHA(){
        return PHAReportsFiltered.size();
    }

    //toggle the filter on / off PHA
    public void toggleFilterPHA(){
        filterEnabledPHA = !filterEnabledPHA;
    }

    
    //get Median Tenant Income for PHA reports
    public double getMedianTenantIncomePHA(){
        List<Double> incomes = new ArrayList<>();
        if(filterEnabledPHA) {
            for(PHAReport report: PHAReportsFiltered){
                try {
                    incomes.add(Double.valueOf(report.getAvgTenantIncome()));
                } catch (NumberFormatException e) {
                    // Skip invalid numbers
                }
            }
        }else {
            for(PHAReport report: PHAreports){
                try {
                    incomes.add(Double.valueOf(report.getAvgTenantIncome()));
                } catch (NumberFormatException e) {
                    // Skip invalid numbers
                }
            }
        }

        if (incomes.isEmpty()) {
            return Double.NaN;
        }

        Collections.sort(incomes);
        int size = incomes.size();
        if (size % 2 == 1) {
            return incomes.get(size / 2);
        } else {
            return (incomes.get((size / 2) - 1) + incomes.get(size / 2)) / 2.0;
        }
    }

    //get average HCV utilization rate for PHA reports
    public double getAverageHcvUtilRatePHA(){
        double temp = 0;
        int count = 0;
        if(filterEnabledPHA) {
            for(PHAReport report: PHAReportsFiltered){
                temp += Double.parseDouble(report.getHcvUtilRate());
            }
            count = PHAReportsFiltered.size();
        }else {
            for(PHAReport report: PHAreports){
                temp += Double.parseDouble(report.getHcvUtilRate());
            }
            count = PHAreports.size();
        }

        if (count == 0) {
            return Double.NaN;
        }

        double avg = temp / count;
        return avg;
    }

    // get average Occupancy Rate for PHA reports
    public double getAvgOccupancyRatePHA() {
        double temp = 0;
        int count = 0;

        if (filterEnabledPHA) {
            for (PHAReport report : PHAReportsFiltered) {
                temp += Double.parseDouble(report.getOccupancyRate());
            }
            count = PHAReportsFiltered.size();
        } else {
            for (PHAReport report : PHAreports) {
                temp += Double.parseDouble(report.getOccupancyRate());
            }
            count = PHAreports.size();
        }

        if (count == 0) {
            return Double.NaN;
        }

        return temp / count;
    }

    // get average Inspection Compliance Rate for PHA reports
    public double getAvgInspectionRatePHA() {
        double temp = 0;
        int count = 0;

        if (filterEnabledPHA) {
            for (PHAReport report : PHAReportsFiltered) {
                temp += Double.parseDouble(report.getInspectionComplianceRate());
            }
            count = PHAReportsFiltered.size();
        } else {
            for (PHAReport report : PHAreports) {
                temp += Double.parseDouble(report.getInspectionComplianceRate());
            }
            count = PHAreports.size();
        }

        if (count == 0) {
            return Double.NaN;
        }

        return temp / count;
    }

    // get average number of HCV units for PHA reports
    public double getAvgHcvUnitsPHA() {
        double temp = 0;
        int count = 0;

        if (filterEnabledPHA) {
            for (PHAReport report : PHAReportsFiltered) {
                temp += Double.parseDouble(report.getNumHcvUnits());
            }
            count = PHAReportsFiltered.size();
        } else {
            for (PHAReport report : PHAreports) {
                temp += Double.parseDouble(report.getNumHcvUnits());
            }
            count = PHAreports.size();
        }

        if (count == 0) {
            return Double.NaN;
        }

        double avg = temp / count;
        return (double) Math.round(avg);   // rounded integer as a double



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

    //opens the XML file at the given path and extracts HUD data into reports
    public void openXMLReportHUD() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        ReportHandlerHUD handler = new ReportHandlerHUD();
        parser.parse(new File(filePath), handler);

        ArrayList<HUDReport> tempReports = new ArrayList<>(handler.getReports());
        for (HUDReport report: tempReports) {
            boolean flag = true;
            for (HUDReport baseReport: HUDreports) {
                if (report.getReportID() != null && baseReport.getReportID() != null &&
                        Integer.parseInt(report.getReportID()) == Integer.parseInt(baseReport.getReportID())) {
                    flag = false;
                    break;
                }
            }
            if (flag)
                HUDreports.add(report);
        }
    }

    //methods for dealing with the current number of reports and the selected report
    public int getTotalNumOfReports(){
        return FMRreports.size()+PHAreports.size()+HUDreports.size();
    }

    public int getNumOfReportsFMR(){
        return FMRreports.size();
    }

    // Returns true if at least one parsed FMR report contains expected FMR fields
    public boolean hasValidFMRReports(){
        for (FMRReport r : FMRreports){
            if (r == null) continue;
            String fmr = r.getFairMarketRent();
            String fy = r.getFiscalYear();
            if ((fmr != null && !fmr.trim().isEmpty()) || (fy != null && !fy.trim().isEmpty())){
                return true;
            }
        }
        return false;
    }

    public int getNumOfReportsPHA(){
        return PHAreports.size();
    }

    // Returns true if at least one parsed PHA report contains expected PHA fields
    public boolean hasValidPHAReports(){
        for (PHAReport r : PHAreports){
            if (r == null) continue;
            String code = r.getPhaCode();
            String city = r.getCity();
            String addr = r.getAddress();
            if ((code != null && !code.trim().isEmpty()) || (city != null && !city.trim().isEmpty()) || (addr != null && !addr.trim().isEmpty())){
                return true;
            }
        }
        return false;
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
        double temp = 0;
        int count = 0;
        if(filterEnabledFMR) {
            for(FMRReport report: FMRReportsFiltered){
                temp += Double.parseDouble(report.getNumBedrooms());
            }
            count = FMRReportsFiltered.size();
        }else {
            for(FMRReport report: FMRreports){
                temp += Double.parseDouble(report.getNumBedrooms());
            }
            count = FMRreports.size();
        }

        if (count == 0) {
            return Double.NaN;
        }

        double avg = temp / count;
        return (double) Math.round(avg);
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

    //remove all FMR reports
    public void resetFMRReportList(){
        FMRreports = new ArrayList<>();
        currentReportFMR = 0;
    }

    //get all num of filtered reports FMR
    public int getNumOfFilteredReportsFMR(){
        return FMRReportsFiltered.size();
    }

    //toggle the filter on / off FMR
    public void toggleFilterFMR(){
        filterEnabledFMR = !filterEnabledFMR;
    }

    //remove all PHA reports
    public void resetPHAReportList(){
        PHAreports = new ArrayList<>();
        currentReportPHA = 0;
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

    //get methods for PHA reports
  

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

    public String getCurrentPHAReportJurisdiction() {
        return PHAreports.get(currentReportPHA).getJurisdiction();
    }

    public String getCurrentPHAReportCode() {
        return PHAreports.get(currentReportPHA).getPhaCode();
    }

    public String getCurrentPHAReportDirector() {
        return PHAreports.get(currentReportPHA).getExecutiveDirector();
    }

    public String getCurrentPHAReportPhone() {
        return PHAreports.get(currentReportPHA).getPhoneNumber();
    }

    public String getCurrentPHAReportFax() {
        return PHAreports.get(currentReportPHA).getFaxLine();
    }

    public String getCurrentPHAReportEmail() {
        return PHAreports.get(currentReportPHA).getEmail();
    }

    public String getCurrentPHANumberUnits() {
        return PHAreports.get(currentReportPHA).getNumHcvUnits();
    }

    public String getCurrentPHAOccupancyRate() {
        return PHAreports.get(currentReportPHA).getOccupancyRate();
    }

    public String getCurrentPHAHCVUtilRate() {
        return PHAreports.get(currentReportPHA).getHcvUtilRate();
    }

    public String getCurrentPHAInspectionComplianceRate() {
        return PHAreports.get(currentReportPHA).getInspectionComplianceRate();

    }

    public String getCurrentPHAAvgTenantIncome() {
        return PHAreports.get(currentReportPHA).getAvgTenantIncome();
    }

    //get methods for HUD reports (basic selection getters)
    public String getCurrentHUDReportPropertyAddress(){
        return HUDreports.get(currentReportHUD).getPropertyAddress();
    }

    public String getCurrentHUDReportState(){
        return HUDreports.get(currentReportHUD).getStateName();
    }

    public String getCurrentHUDReportCounty(){
        return HUDreports.get(currentReportHUD).getCountyName();
    }

    public String getCurrentHUDReportZipCode(){
        return HUDreports.get(currentReportHUD).getZipCode();
    }

    public String getCurrentHUDReportPropertyID(){
        return HUDreports.get(currentReportHUD).getPropertyId();
    }

    public String getCurrentHUDReportFiscalYear(){
        return HUDreports.get(currentReportHUD).getFiscalYear();
    }

    public String getCurrentHUDReportAMIByCounty(){
        return HUDreports.get(currentReportHUD).getAmiMedianFamilyIncome();
    }

    public String getCurrentHUDReportOwner(){
        return HUDreports.get(currentReportHUD).getOwnerEntity();
    }

    // Average HUD median family income across HUDreports. Returns Double.NaN if no valid values.
    public double getAverageHudMedianIncome(){
        double sum = 0.0;
        int count = 0;
        ArrayList<HUDReport> source = filterEnabledHUD ? HUDReportsFiltered : HUDreports;
        for (HUDReport r : source){
            if (r == null) continue;
            String ami = r.getAmiMedianFamilyIncome();
            if (ami == null) continue;
            ami = ami.trim();
            if (ami.isEmpty()) continue;
            try{
                double v = Double.parseDouble(ami);
                sum += v;
                count++;
            } catch (NumberFormatException ex){
                // skip invalid
            }
        }
        if (count == 0) return Double.NaN;
        return sum / count;
    }

    // Average HUD vacancy rate across HUDreports (expects decimal like 0.065). Returns Double.NaN if no valid values.
    public double getAverageHudVacancyRate(){
        double sum = 0.0;
        int count = 0;
        ArrayList<HUDReport> source = filterEnabledHUD ? HUDReportsFiltered : HUDreports;
        for (HUDReport r : source){
            if (r == null) continue;
            String val = r.getVacancyRate();
            if (val == null) continue;
            val = val.trim();
            if (val.isEmpty()) continue;
            try{
                double v = Double.parseDouble(val);
                sum += v;
                count++;
            } catch (NumberFormatException ex){
                // skip invalid
            }
        }
        if (count == 0) return Double.NaN;
        return sum / count;
    }

    // Average HUD inspection score across HUDreports. Returns Double.NaN if no valid values.
    public double getAverageHudInspectionScore(){
        double sum = 0.0;
        int count = 0;
        ArrayList<HUDReport> source = filterEnabledHUD ? HUDReportsFiltered : HUDreports;
        for (HUDReport r : source){
            if (r == null) continue;
            String val = r.getHudInspectionScore();
            if (val == null) continue;
            val = val.trim();
            if (val.isEmpty()) continue;
            try{
                double v = Double.parseDouble(val);
                sum += v;
                count++;
            } catch (NumberFormatException ex){
                // skip invalid
            }
        }
        if (count == 0) return Double.NaN;
        return sum / count;
    }

    //available units = total_units - occupied_units (returns empty string if values missing)
    public String getCurrentHUDAvailableUnits(){
        HUDReport r = HUDreports.get(currentReportHUD);
        if (r == null) return "";
        String total = r.getTotalUnits();
        String occupied = r.getOccupiedUnits();
        if (total == null || total.trim().isEmpty() || occupied == null || occupied.trim().isEmpty()) {
            return "";
        }
        try{
            int t = Integer.parseInt(total);
            int o = Integer.parseInt(occupied);
            int avail = t - o;
            return String.format("%d", avail);
        }catch(NumberFormatException ex){
            return "";
        }
    }

    // --- Non-destructive parsers: parse file into temp lists without mutating main lists ---
    public ArrayList<FMRReport> parseReportsFMR() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        ReportHandlerFMR handler = new ReportHandlerFMR();
        parser.parse(new File(filePath), handler);
        return new ArrayList<>(handler.getReports());
    }

    public ArrayList<PHAReport> parseReportsPHA() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        ReportHandlerPHA handler = new ReportHandlerPHA();
        parser.parse(new File(filePath), handler);
        return new ArrayList<>(handler.getReports());
    }

    public ArrayList<HUDReport> parseReportsHUD() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        ReportHandlerHUD handler = new ReportHandlerHUD();
        parser.parse(new File(filePath), handler);
        return new ArrayList<>(handler.getReports());
    }

    // --- Merge helpers: add parsed reports into main lists with de-duplication ---
    public void addFMRReports(ArrayList<FMRReport> tempReports){
        for (FMRReport report: tempReports){
            boolean flag = true;
            for(FMRReport baseReport: FMRreports){
                if (report.getReportID() != null && baseReport.getReportID() != null &&
                        Integer.parseInt(report.getReportID()) == Integer.parseInt(baseReport.getReportID())) {
                    flag = false;
                    break;
                }
            }
            if(flag)
                FMRreports.add(report);
        }
    }

    public void addPHAReports(ArrayList<PHAReport> tempReports){
        for (PHAReport report: tempReports){
            boolean flag = true;
            for(PHAReport baseReport: PHAreports){
                if (report.getReportID() != null && baseReport.getReportID() != null &&
                        Integer.parseInt(report.getReportID()) == Integer.parseInt(baseReport.getReportID())) {
                    flag = false;
                    break;
                }
            }
            if(flag)
                PHAreports.add(report);
        }
    }

    public void addHUDReports(ArrayList<HUDReport> tempReports){
        for (HUDReport report: tempReports){
            boolean flag = true;
            for(HUDReport baseReport: HUDreports){
                if (report.getReportID() != null && baseReport.getReportID() != null &&
                        Integer.parseInt(report.getReportID()) == Integer.parseInt(baseReport.getReportID())) {
                    flag = false;
                    break;
                }
            }
            if(flag)
                HUDreports.add(report);
        }
    }

    // Expose HUD reports for visualization/analysis
    public ArrayList<HUDReport> getHudReports() {
        return new ArrayList<>(HUDreports);
    }

    // Expose PHA reports for visualization/analysis
    public ArrayList<PHAReport> getPhaReports() {
        return new ArrayList<>(PHAreports);
    }

    // --- List validators for temporary parsed lists ---
    public boolean listHasValidFMRReports(ArrayList<FMRReport> list){
        if (list == null) return false;
        for (FMRReport r : list){
            if (r == null) continue;
            String fmr = r.getFairMarketRent();
            String fy = r.getFiscalYear();
            if ((fmr != null && !fmr.trim().isEmpty()) || (fy != null && !fy.trim().isEmpty())){
                return true;
            }
        }
        return false;
    }

    public boolean listHasValidPHAReports(ArrayList<PHAReport> list){
        if (list == null) return false;
        for (PHAReport r : list){
            if (r == null) continue;
            String code = r.getPhaCode();
            String city = r.getCity();
            String addr = r.getAddress();
            if ((code != null && !code.trim().isEmpty()) || (city != null && !city.trim().isEmpty()) || (addr != null && !addr.trim().isEmpty())){
                return true;
            }
        }
        return false;
    }

    public boolean listHasValidHUDReports(ArrayList<HUDReport> list){
        if (list == null) return false;
        for (HUDReport r : list){
            if (r == null) continue;
            String pid = r.getPropertyId();
            String addr = r.getPropertyAddress();
            String ami = r.getAmiMedianFamilyIncome();
            if ((pid != null && !pid.trim().isEmpty()) || (addr != null && !addr.trim().isEmpty()) || (ami != null && !ami.trim().isEmpty())){
                return true;
            }
        }
        return false;
    }

}
