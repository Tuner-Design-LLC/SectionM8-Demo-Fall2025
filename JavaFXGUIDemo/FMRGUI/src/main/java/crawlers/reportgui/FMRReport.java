package crawlers.reportgui;

// FMRReport class encapsulates Fair Market Rent (FMR) report details.
public class FMRReport {
    private String reportID;
    private String reportType;
    private String fiscalYear;
    private String stateName;
    private String stateCode;
    private String countyName;
    private String fipsCode;
    private String hudGeoId;
    private String msaCode;
    private String areaType;
    private String hudRegionCode;
    private String zipCode;
    private String numBedrooms;
    private String fairMarketRent;
    private String percentileType;
    private String bedroomDistSource;
    private String surveySourceYear;
    private String adjustmentFactor;
    private String isSmallAreaFmr;
    private String medianHouseholdIncome;
    private String sourceUrl;
    private String scrapeDate;
    private String versionHash;
    private String updateFreq;
    private String crawlerRunData;

    FMRReport(){}

    // Getters
    public String getReportID() {
        return reportID;
    }

    public String getReportType() {
        return reportType;
    }

    public String getCrawlerRunData() {
        return crawlerRunData;
    }

    public String getSmallAreaFmr() {
        return isSmallAreaFmr;
    }

    public String getAdjustmentFactor() {
        return adjustmentFactor;
    }

    public String getFairMarketRent() {
        return fairMarketRent;
    }

    public String getMedianHouseholdIncome() {
        return medianHouseholdIncome;
    }

    public String getNumBedrooms() {
        return numBedrooms;
    }

    public String getSurveySourceYear() {
        return surveySourceYear;
    }

    public String getAreaType() {
        return areaType;
    }

    public String getBedroomDistSource() {
        return bedroomDistSource;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getFipsCode() {
        return fipsCode;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public String getHudGeoId() {
        return hudGeoId;
    }

    public String getHudRegionCode() {
        return hudRegionCode;
    }

    public String getMsaCode() {
        return msaCode;
    }

    public String getPercentileType() {
        return percentileType;
    }

    public String getScrapeDate() {
        return scrapeDate;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public String getUpdateFreq() {
        return updateFreq;
    }

    public String getVersionHash() {
        return versionHash;
    }

    public String getZipCode() {
        return zipCode;
    }

    // Setters
    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setFipsCode(String fipsCode) {
        this.fipsCode = fipsCode;
    }

    public void setHudGeoId(String hudGeoId) {
        this.hudGeoId = hudGeoId;
    }

    public void setMsaCode(String msaCode) {
        this.msaCode = msaCode;
    }

    public void setSmallAreaFmr(String isSmallAreaFmr) {
        this.isSmallAreaFmr = isSmallAreaFmr;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public void setHudRegionCode(String hudRegionCode) {
        this.hudRegionCode = hudRegionCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setNumBedrooms(String numBedrooms) {
        this.numBedrooms = numBedrooms;
    }

    public void setFairMarketRent(String fairMarketRent) {
        this.fairMarketRent = fairMarketRent;
    }

    public void setPercentileType(String percentileType) {
        this.percentileType = percentileType;
    }

    public void setBedroomDistSource(String bedroomDistSource) {
        this.bedroomDistSource = bedroomDistSource;
    }

    public void setSurveySourceYear(String surveySourceYear) {
        this.surveySourceYear = surveySourceYear;
    }

    public void setAdjustmentFactor(String adjustmentFactor) {
        this.adjustmentFactor = adjustmentFactor;
    }

    public void setMedianHouseholdIncome(String medianHouseholdIncome) {
        this.medianHouseholdIncome = medianHouseholdIncome;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public void setScrapeDate(String scrapeDate) {
        this.scrapeDate = scrapeDate;
    }

    public void setVersionHash(String versionHash) {
        this.versionHash = versionHash;
    }

    public void setUpdateFreq(String updateFreq) {
        this.updateFreq = updateFreq;
    }

    public void setCrawlerRunData(String crawlerRunData) {
        this.crawlerRunData = crawlerRunData;
    }

    // String representation of the FMRReport
    @Override
    public String toString() {
        return String.format("FMR ID: %-4s %-6s %-4s %s %-10s",Integer.parseInt(reportID)+1,fiscalYear,stateCode,countyName,zipCode);
    }
}

