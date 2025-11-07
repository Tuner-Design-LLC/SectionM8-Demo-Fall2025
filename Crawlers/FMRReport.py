# Basic class to represent a FMR Crawler Report
# This class is designed to be implemented by the FMRCrawler to create list of report objects that can be exported to XML format.
class FMRCrawlerReport:

    #create a report with all relevant data attributes
    def __init__(self,fiscal_year,state_name,state_code,county_name,fips_code,hud_geo_id,msa_code,area_type,hud_region_code,zip_code,num_bedrooms,fair_market_rent,percentile_type,bedroom_dist_source,survey_source_year,adjustment_factor,
                 is_small_area_fmr,median_household_income,source_url,scrape_date,version_hash,update_freq,crawler_run_data):
        
        self._fiscal_year = fiscal_year
        self._state_name = state_name
        self._state_code = state_code
        self._county_name = county_name
        self._fips_code = fips_code
        self._hud_geo_id = hud_geo_id
        self._msa_code = msa_code
        self._area_type = area_type
        self._hud_region_code = hud_region_code
        self._zip_code = zip_code
        self._num_bedrooms = num_bedrooms
        self._fair_market_rent = fair_market_rent
        self._percentile_type = percentile_type
        self._bedroom_dist_source = bedroom_dist_source
        self._survey_source_year = survey_source_year
        self._adjustment_factor = adjustment_factor
        self._is_small_area_fmr = is_small_area_fmr
        self._median_household_income = median_household_income
        self._source_url = source_url
        self._scrape_date = scrape_date
        self._version_hash = version_hash
        self._update_freq = update_freq
        self._crawler_run_data = crawler_run_data

    #getter methods for all data attributes
    def get_fiscal_year(self):
        return self._fiscal_year
    def get_state_name(self):
        return self._state_name
    def get_state_code(self):
        return self._state_code
    def get_county_name(self):
        return self._county_name
    def get_fips_code(self):
        return self._fips_code
    def get_hud_geo_id(self):
        return self._hud_geo_id
    def get_msa_code(self):
        return self._msa_code
    def get_area_type(self):
        return self._area_type
    def get_hud_region_code(self):
        return self._hud_region_code
    def get_zip_code(self):
        return self._zip_code
    def get_num_bedrooms(self):
        return self._num_bedrooms
    def get_fair_market_rent(self):
        return self._fair_market_rent
    def get_percentile_type(self):
        return self._percentile_type
    def get_bedroom_dist_source(self):
        return self._bedroom_dist_source
    def get_survey_source_year(self):
        return self._survey_source_year
    def get_adjustment_factor(self):
        return self._adjustment_factor
    def get_is_small_area_fmr(self):
        return self._is_small_area_fmr
    def get_median_household_income(self):
        return self._median_household_income
    def get_source_url(self):
        return self._source_url
    def get_scrape_date(self):
        return self._scrape_date
    def get_version_hash(self):
        return self._version_hash
    def get_update_freq(self):
        return self._update_freq
    def get_crawler_run_data(self):
        return self._crawler_run_data
