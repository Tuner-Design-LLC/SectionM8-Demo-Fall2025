# Consolidated PHA attributes
class Primary:
    def __init__(self, code, name, regCode, juris, address, city, stateCode, stateName, zip, countyName, fips):
        self.__pha_code = code
        self.__name = name
        self.__hud_regional_code = regCode
        self.__jurisdiction = juris
        self.__address = address
        self.__city = city
        self.__state_code = stateCode
        self.__state_name = stateName
        self.__zip_code = zip
        self.__county_name = countyName
        self.__fips_code = fips

class HousingProgram:
    def __init__(self, progType, hvc, public, total, waitingList, occRate, hcvUtil):
        self.__program_type = progType
        self.__num_hcv_units = hvc
        self.__num_public_units = public
        self.__num_total_assisted = total
        self.__waiting_list_status = waitingList
        self.__occupancy_rate = occRate
        self.__hcv_util_rate = hcvUtil

class GeographicContextual:
    def __init__(self, latitude, longitude, msa, hud, rural):
        self.__latitude = latitude
        self.__longitude = longitude
        self.__msa_code = msa
        self.__hud_regional_code = hud
        self.__rural_indicator = rural

class FinanceFunding:
    def __init__(self, annual, capital, financial):
        self.__annual_budget = annual
        self.__capital_fund_allocation = capital
        self.__financial_report_year = financial

class DemographicOccupancy:
    def __init__(self, income, median, household, rentShare, amount):
        self.__avg_tenant_income = income
        self.__median_rent_dist = median
        self.__household_type_dist = household
        self.__avg_tenant_rent_share = rentShare
        self.__avg_subsidy_amount = amount

class CompliancePerformance:
    def __init__(self, semap, hud, inspection, performance, last):
        self.__semap_score = semap
        self.__last_hud_audit_date = hud
        self.__inspection_compliance_rate = inspection
        self.__performance_category = performance
        self.__last_updated = last

class AnalyticsMetadata:
    def __init__(self, source, scrape, version, dataset, update, license, crawler):
        self.__source_url = source
        self.__scrape_date = scrape
        self.__version_hash = version
        self.__dataset_last_update = dataset
        self.__update_freq = update
        self.__data_license = license
        self.__crawler_run_data = crawler

class AdministrativeContact:
    def __init__(self, number, fax, email, exDirect, contactLast):
        self.__phone_number = number
        self.__fax_line = fax
        self.__email = email
        self.__executive_director = exDirect
        self.__contact_last_verified = contactLast
