# Consolidated PHA attributes
# Updated Attributes, with setters and getters

# The Primary class encapsulates primary identification details of a Public Housing Agency (PHA).
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
# pha_code
    def get_pha_code(self):
        return self.__pha_code

    def set_pha_code(self, value):
        self.__pha_code = value

    # name
    def get_name(self):
        return self.__name

    def set_name(self, value):
        self.__name = value

    # hud_regional_code
    def get_hud_regional_code(self):
        return self.__hud_regional_code

    def set_hud_regional_code(self, value):
        self.__hud_regional_code = value

    # jurisdiction
    def get_jurisdiction(self):
        return self.__jurisdiction

    def set_jurisdiction(self, value):
        self.__jurisdiction = value

    # address
    def get_address(self):
        return self.__address

    def set_address(self, value):
        self.__address = value

    # city
    def get_city(self):
        return self.__city

    def set_city(self, value):
        self.__city = value

    # state_code
    def get_state_code(self):
        return self.__state_code

    def set_state_code(self, value):
        self.__state_code = value

    # state_name
    def get_state_name(self):
        return self.__state_name

    def set_state_name(self, value):
        self.__state_name = value

    # zip_code
    def get_zip_code(self):
        return self.__zip_code

    def set_zip_code(self, value):
        self.__zip_code = value

    # county_name
    def get_county_name(self):
        return self.__county_name

    def set_county_name(self, value):
        self.__county_name = value

    # fips_code
    def get_fips_code(self):
        return self.__fips_code

    def set_fips_code(self, value):
        self.__fips_code = value

# The HousingProgram class encapsulates housing program details of a Public Housing Agency (PHA).
class HousingProgram:
    def __init__(self, progType, hvc, public, total, waitingList, occRate, hcvUtil):
        self.__program_type = progType
        self.__num_hcv_units = hvc
        self.__num_public_units = public
        self.__num_total_assisted = total
        self.__waiting_list_status = waitingList
        self.__occupancy_rate = occRate
        self.__hcv_util_rate = hcvUtil

  # program_type
    def get_program_type(self):
        return self.__program_type

    def set_program_type(self, value):
        self.__program_type = value

    # num_hcv_units
    def get_num_hcv_units(self):
        return self.__num_hcv_units

    def set_num_hcv_units(self, value):
        self.__num_hcv_units = value

    # num_public_units
    def get_num_public_units(self):
        return self.__num_public_units

    def set_num_public_units(self, value):
        self.__num_public_units = value

    # num_total_assisted
    def get_num_total_assisted(self):
        return self.__num_total_assisted

    def set_num_total_assisted(self, value):
        self.__num_total_assisted = value

    # waiting_list_status
    def get_waiting_list_status(self):
        return self.__waiting_list_status

    def set_waiting_list_status(self, value):
        self.__waiting_list_status = value

    # occupancy_rate
    def get_occupancy_rate(self):
        return self.__occupancy_rate

    def set_occupancy_rate(self, value):
        self.__occupancy_rate = value

    # hcv_utilization
    def get_hcv_utilization(self):
        return self.__hcv_util_rate

    def set_hcv_utilization(self, value):
        self.__hcv_util_rate = value

# The GeographicContextual class encapsulates geographic and contextual details of a Public Housing Agency (PHA).
class GeographicContextual:
    def __init__(self, latitude, longitude, msa, hud, rural):
        self.__latitude = latitude
        self.__longitude = longitude
        self.__msa_code = msa
        self.__hud_regional_code = hud
        self.__rural_indicator = rural

# latitude
    def get_latitude(self):
        return self.__latitude

    def set_latitude(self, value):
        self.__latitude = value

    # longitude
    def get_longitude(self):
        return self.__longitude

    def set_longitude(self, value):
        self.__longitude = value

    # msa_code
    def get_msa_code(self):
        return self.__msa_code

    def set_msa_code(self, value):
        self.__msa_code = value

    # hud_regional_code
    def get_hud_regional_code(self):
        return self.__hud_regional_code

    def set_hud_regional_code(self, value):
        self.__hud_regional_code = value

    # rural_indicator
    def get_rural_indicator(self):
        return self.__rural_indicator

    def set_rural_indicator(self, value):
        self.__rural_indicator = value

# The FinanceFunding class encapsulates financial and funding details of a Public Housing Agency (PHA).
class FinanceFunding:
    def __init__(self, annual, capital, financial):
        self.__annual_budget = annual
        self.__capital_fund_allocation = capital
        self.__financial_report_year = financial

# annual_budget
    def get_annual_budget(self):
        return self.__annual_budget

    def set_annual_budget(self, value):
        self.__annual_budget = value

    # capital_fund_allocation
    def get_capital_fund_allocation(self):
        return self.__capital_fund_allocation

    def set_capital_fund_allocation(self, value):
        self.__capital_fund_allocation = value

    # financial_report_year
    def get_financial_report_year(self):
        return self.__financial_report_year

    def set_financial_report_year(self, value):
        self.__financial_report_year = value

# The DemographicOccupancy class encapsulates demographic and occupancy details of a Public Housing Agency (PHA).
class DemographicOccupancy:
    def __init__(self, income, median, household, rentShare, amount):
        self.__avg_tenant_income = income
        self.__median_rent_dist = median
        self.__household_type_dist = household
        self.__avg_tenant_rent_share = rentShare
        self.__avg_subsidy_amount = amount

  # avg_tenant_income
    def get_avg_tenant_income(self):
        return self.__avg_tenant_income

    def set_avg_tenant_income(self, value):
        self.__avg_tenant_income = value

    # median_rent_dist
    def get_median_rent_dist(self):
        return self.__median_rent_dist

    def set_median_rent_dist(self, value):
        self.__median_rent_dist = value

    # household_type_dist
    def get_household_type_dist(self):
        return self.__household_type_dist

    def set_household_type_dist(self, value):
        self.__household_type_dist = value

    # avg_tenant_rent_share
    def get_avg_tenant_rent_share(self):
        return self.__avg_tenant_rent_share

    def set_avg_tenant_rent_share(self, value):
        self.__avg_tenant_rent_share = value

    # avg_subsidy_amount
    def get_avg_subsidy_amount(self):
        return self.__avg_subsidy_amount

    def set_avg_subsidy_amount(self, value):
        self.__avg_subsidy_amount = value

# The CompliancePerformance class encapsulates compliance and performance details of a Public Housing Agency (PHA).
class CompliancePerformance:
    def __init__(self, semap, hud, inspection, performance, last):
        self.__semap_score = semap
        self.__last_hud_audit_date = hud
        self.__inspection_compliance_rate = inspection
        self.__performance_category = performance
        self.__last_updated = last

# semap_score
    def get_semap_score(self):
        return self.__semap_score

    def set_semap_score(self, value):
        self.__semap_score = value

    # last_hud_audit_date
    def get_last_hud_audit_date(self):
        return self.__last_hud_audit_date

    def set_last_hud_audit_date(self, value):
        self.__last_hud_audit_date = value

    # inspection_compliance_rate
    def get_inspection_compliance_rate(self):
        return self.__inspection_compliance_rate

    def set_inspection_compliance_rate(self, value):
        self.__inspection_compliance_rate = value

    # performance_category
    def get_performance_category(self):
        return self.__performance_category

    def set_performance_category(self, value):
        self.__performance_category = value

    # last_updated
    def get_last_updated(self):
        return self.__last_updated

    def set_last_updated(self, value):
        self.__last_updated = value

# The AnalyticsMetadata class encapsulates metadata details of a Public Housing Agency (PHA) dataset.
class AnalyticsMetadata:
    def __init__(self, source, scrape, version, dataset, update, license, crawler):
        self.__source_url = source
        self.__scrape_date = scrape
        self.__version_hash = version
        self.__dataset_last_update = dataset
        self.__update_freq = update
        self.__data_license = license
        self.__crawler_run_data = crawler

  # source_url
    def get_source_url(self):
        return self.__source_url

    def set_source_url(self, value):
        self.__source_url = value

    # scrape_date
    def get_scrape_date(self):
        return self.__scrape_date

    def set_scrape_date(self, value):
        self.__scrape_date = value

    # version_hash
    def get_version_hash(self):
        return self.__version_hash

    def set_version_hash(self, value):
        self.__version_hash = value

    # dataset_last_update
    def get_dataset_last_update(self):
        return self.__dataset_last_update

    def set_dataset_last_update(self, value):
        self.__dataset_last_update = value

    # update_freq
    def get_update_freq(self):
        return self.__update_freq

    def set_update_freq(self, value):
        self.__update_freq = value

    # data_license
    def get_data_license(self):
        return self.__data_license

    def set_data_license(self, value):
        self.__data_license = value

    # crawler_run_data
    def get_crawler_run_data(self):
        return self.__crawler_run_data

    def set_crawler_run_data(self, value):
        self.__crawler_run_data = value

# The AdministrativeContact class encapsulates administrative contact details of a Public Housing Agency (PHA).
class AdministrativeContact:
    def __init__(self, number, fax, email, exDirect, contactLast):
        self.__phone_number = number
        self.__fax_line = fax
        self.__email = email
        self.__executive_director = exDirect
        self.__contact_last_verified = contactLast
     # phone_number
    def get_phone_number(self):
        return self.__phone_number

    def set_phone_number(self, value):
        self.__phone_number = value

        # fax_line
    def get_fax_line(self):
        return self.__fax_line

    def set_fax_line(self, value):
        self.__fax_line = value

        # email
    def get_email(self):
        return self.__email

    def set_email(self, value):
        self.__email = value

        # executive_director
    def get_executive_director(self):
        return self.__executive_director

    def set_executive_director(self, value):
        self.__executive_director = value

        # contact_last_verified (this was public in your init)
    def get_contact_last_verified(self):
        return self.__contact_last_verified

    def set_contact_last_verified(self, value):
        self.__contact_last_verified = value

