package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JITEN-PC on 16-03-2017.
 */

public class User {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("customer")
    @Expose
    private Customer customer;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public class Customer {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("group_id")
        @Expose
        private Integer groupId;
        @SerializedName("default_billing")
        @Expose
        private String defaultBilling;
        @SerializedName("default_shipping")
        @Expose
        private String defaultShipping;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("created_in")
        @Expose
        private String createdIn;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("lastname")
        @Expose
        private String lastname;
        @SerializedName("store_id")
        @Expose
        private Integer storeId;
        @SerializedName("website_id")
        @Expose
        private Integer websiteId;
        @SerializedName("addresses")
        @Expose
        private List<Address> addresses = null;
        @SerializedName("disable_auto_group_change")
        @Expose
        private Integer disableAutoGroupChange;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getGroupId() {
            return groupId;
        }

        public void setGroupId(Integer groupId) {
            this.groupId = groupId;
        }

        public String getDefaultBilling() {
            return defaultBilling;
        }

        public void setDefaultBilling(String defaultBilling) {
            this.defaultBilling = defaultBilling;
        }

        public String getDefaultShipping() {
            return defaultShipping;
        }

        public void setDefaultShipping(String defaultShipping) {
            this.defaultShipping = defaultShipping;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedIn() {
            return createdIn;
        }

        public void setCreatedIn(String createdIn) {
            this.createdIn = createdIn;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public Integer getStoreId() {
            return storeId;
        }

        public void setStoreId(Integer storeId) {
            this.storeId = storeId;
        }

        public Integer getWebsiteId() {
            return websiteId;
        }

        public void setWebsiteId(Integer websiteId) {
            this.websiteId = websiteId;
        }

        public List<Address> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<Address> addresses) {
            this.addresses = addresses;
        }

        public Integer getDisableAutoGroupChange() {
            return disableAutoGroupChange;
        }

        public void setDisableAutoGroupChange(Integer disableAutoGroupChange) {
            this.disableAutoGroupChange = disableAutoGroupChange;
        }

    }


    public class Address {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("customer_id")
        @Expose
        private Integer customerId;
        @SerializedName("region")
        @Expose
        private Region region;
        @SerializedName("region_id")
        @Expose
        private Integer regionId;
        @SerializedName("country_id")
        @Expose
        private String countryId;
        @SerializedName("street")
        @Expose
        private List<String> street = null;
        @SerializedName("telephone")
        @Expose
        private String telephone;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("lastname")
        @Expose
        private String lastname;
        @SerializedName("default_shipping")
        @Expose
        private Boolean defaultShipping;
        @SerializedName("default_billing")
        @Expose
        private Boolean defaultBilling;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Integer customerId) {
            this.customerId = customerId;
        }

        public Region getRegion() {
            return region;
        }

        public void setRegion(Region region) {
            this.region = region;
        }

        public Integer getRegionId() {
            return regionId;
        }

        public void setRegionId(Integer regionId) {
            this.regionId = regionId;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public List<String> getStreet() {
            return street;
        }

        public void setStreet(List<String> street) {
            this.street = street;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public Boolean getDefaultShipping() {
            return defaultShipping;
        }

        public void setDefaultShipping(Boolean defaultShipping) {
            this.defaultShipping = defaultShipping;
        }

        public Boolean getDefaultBilling() {
            return defaultBilling;
        }

        public void setDefaultBilling(Boolean defaultBilling) {
            this.defaultBilling = defaultBilling;
        }

    }

    public class Region {

        @SerializedName("region_code")
        @Expose
        private Object regionCode;
        @SerializedName("region")
        @Expose
        private Object region;
        @SerializedName("region_id")
        @Expose
        private Integer regionId;

        public Object getRegionCode() {
            return regionCode;
        }

        public void setRegionCode(Object regionCode) {
            this.regionCode = regionCode;
        }

        public Object getRegion() {
            return region;
        }

        public void setRegion(Object region) {
            this.region = region;
        }

        public Integer getRegionId() {
            return regionId;
        }

        public void setRegionId(Integer regionId) {
            this.regionId = regionId;
        }

    }

}
