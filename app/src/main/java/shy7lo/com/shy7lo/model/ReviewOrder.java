package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JITEN-PC on 17-03-2017.
 */

public class ReviewOrder {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("is_active")
    @Expose
    private Boolean isActive;
    @SerializedName("is_virtual")
    @Expose
    private Boolean isVirtual;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("items_count")
    @Expose
    private Integer itemsCount;
    @SerializedName("items_qty")
    @Expose
    private Integer itemsQty;
    //    @SerializedName("customer")
//    @Expose
//    private Customer customer;
//    @SerializedName("billing_address")
//    @Expose
//    private BillingAddress billingAddress;
    @SerializedName("orig_order_id")
    @Expose
    private Integer origOrderId;
    //    @SerializedName("currency")
//    @Expose
//    private Currency currency;
    @SerializedName("customer_is_guest")
    @Expose
    private Boolean customerIsGuest;
    @SerializedName("customer_note_notify")
    @Expose
    private Boolean customerNoteNotify;
    @SerializedName("customer_tax_class_id")
    @Expose
    private Integer customerTaxClassId;
    @SerializedName("store_id")
    @Expose
    private Integer storeId;
//    @SerializedName("extension_attributes")
//    @Expose
//    private ExtensionAttributes extensionAttributes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(Boolean isVirtual) {
        this.isVirtual = isVirtual;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(Integer itemsCount) {
        this.itemsCount = itemsCount;
    }

    public Integer getItemsQty() {
        return itemsQty;
    }

    public void setItemsQty(Integer itemsQty) {
        this.itemsQty = itemsQty;
    }

//    public Customer getCustomer() {
//        return customer;
//    }
//
//    public void setCustomer(Customer customer) {
//        this.customer = customer;
//    }
//
//    public BillingAddress getBillingAddress() {
//        return billingAddress;
//    }
//
//    public void setBillingAddress(BillingAddress billingAddress) {
//        this.billingAddress = billingAddress;
//    }

    public Integer getOrigOrderId() {
        return origOrderId;
    }

    public void setOrigOrderId(Integer origOrderId) {
        this.origOrderId = origOrderId;
    }

//    public Currency getCurrency() {
//        return currency;
//    }
//
//    public void setCurrency(Currency currency) {
//        this.currency = currency;
//    }

    public Boolean getCustomerIsGuest() {
        return customerIsGuest;
    }

    public void setCustomerIsGuest(Boolean customerIsGuest) {
        this.customerIsGuest = customerIsGuest;
    }

    public Boolean getCustomerNoteNotify() {
        return customerNoteNotify;
    }

    public void setCustomerNoteNotify(Boolean customerNoteNotify) {
        this.customerNoteNotify = customerNoteNotify;
    }

    public Integer getCustomerTaxClassId() {
        return customerTaxClassId;
    }

    public void setCustomerTaxClassId(Integer customerTaxClassId) {
        this.customerTaxClassId = customerTaxClassId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }


//    public ExtensionAttributes getExtensionAttributes() {
//        return extensionAttributes;
//    }
//
//    public void setExtensionAttributes(ExtensionAttributes extensionAttributes) {
//        this.extensionAttributes = extensionAttributes;
//    }

    public class Item {

        @SerializedName("item_id")
        @Expose
        private Integer itemId;
        @SerializedName("sku")
        @Expose
        private String sku;
        @SerializedName("qty")
        @Expose
        private Integer qty;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("brand")
        @Expose
        private String brand;
        @SerializedName("price")
        @Expose
        private Float price;
        @SerializedName("special_price")
        @Expose
        private Float special_price;
        @SerializedName("special_from_date")
        @Expose
        private String special_from_date;
        @SerializedName("special_to_date")
        @Expose
        private String special_to_date;
        @SerializedName("product_type")
        @Expose
        private String productType;
        @SerializedName("quote_id")
        @Expose
        private String quoteId;
        @SerializedName("image")
        @Expose
        private String image;

        private String imageFIle;

        @SerializedName("configure_option")
        @Expose
        private List<ConfigureOption> configureOptionList = null;

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            this.qty = qty;
        }

        public String getName() {
            return name;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }

        public Float getSpecial_price() {
            return special_price;
        }

        public void setSpecial_price(Float special_price) {
            this.special_price = special_price;
        }

        public String getSpecial_from_date() {
            return special_from_date;
        }

        public void setSpecial_from_date(String special_from_date) {
            this.special_from_date = special_from_date;
        }

        public String getSpecial_to_date() {
            return special_to_date;
        }

        public void setSpecial_to_date(String special_to_date) {
            this.special_to_date = special_to_date;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getQuoteId() {
            return quoteId;
        }

        public void setQuoteId(String quoteId) {
            this.quoteId = quoteId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImageFIle() {
            return imageFIle;
        }

        public void setImageFIle(String imageFIle) {
            this.imageFIle = imageFIle;
        }

        public List<ConfigureOption> getConfigureOptionList() {
            return configureOptionList;
        }

        public void setConfigureOptionList(List<ConfigureOption> configureOptionList) {
            this.configureOptionList = configureOptionList;
        }
    }

    public class ConfigureOption {

        @SerializedName("option_label")
        @Expose
        private String option_label;
        @SerializedName("option_value")
        @Expose
        private String option_value;

        public String getOption_label() {
            return option_label;
        }

        public void setOption_label(String option_label) {
            this.option_label = option_label;
        }

        public String getOption_value() {
            return option_value;
        }

        public void setOption_value(String option_value) {
            this.option_value = option_value;
        }
    }

//    public class Region {
//
//        @SerializedName("region_code")
//        @Expose
//        private Object regionCode;
//        @SerializedName("region")
//        @Expose
//        private Object region;
//        @SerializedName("region_id")
//        @Expose
//        private Integer regionId;
//
//        public Object getRegionCode() {
//            return regionCode;
//        }
//
//        public void setRegionCode(Object regionCode) {
//            this.regionCode = regionCode;
//        }
//
//        public Object getRegion() {
//            return region;
//        }
//
//        public void setRegion(Object region) {
//            this.region = region;
//        }
//
//        public Integer getRegionId() {
//            return regionId;
//        }
//
//        public void setRegionId(Integer regionId) {
//            this.regionId = regionId;
//        }
//
//    }
//
//    public class Shipping {
//
//        @SerializedName("address")
//        @Expose
//        private ShippingAddress address;
//        @SerializedName("method")
//        @Expose
//        private String method;
//
//        public ShippingAddress getAddress() {
//            return address;
//        }
//
//        public void setAddress(ShippingAddress address) {
//            this.address = address;
//        }
//
//        public String getMethod() {
//            return method;
//        }
//
//        public void setMethod(String method) {
//            this.method = method;
//        }
//
//    }
//
//    public class ShippingAddress {
//
//        @SerializedName("id")
//        @Expose
//        private Integer id;
//        @SerializedName("region")
//        @Expose
//        private String region;
//        @SerializedName("region_id")
//        @Expose
//        private Object regionId;
//        @SerializedName("region_code")
//        @Expose
//        private String regionCode;
//        @SerializedName("country_id")
//        @Expose
//        private String countryId;
//        @SerializedName("street")
//        @Expose
//        private List<String> street = null;
//        @SerializedName("telephone")
//        @Expose
//        private String telephone;
//        @SerializedName("postcode")
//        @Expose
//        private Object postcode;
//        @SerializedName("city")
//        @Expose
//        private String city;
//        @SerializedName("firstname")
//        @Expose
//        private String firstname;
//        @SerializedName("lastname")
//        @Expose
//        private String lastname;
//        @SerializedName("customer_id")
//        @Expose
//        private Integer customerId;
//        @SerializedName("email")
//        @Expose
//        private String email;
//        @SerializedName("same_as_billing")
//        @Expose
//        private Integer sameAsBilling;
//        @SerializedName("save_in_address_book")
//        @Expose
//        private Integer saveInAddressBook;
//
//        public Integer getId() {
//            return id;
//        }
//
//        public void setId(Integer id) {
//            this.id = id;
//        }
//
//        public String getRegion() {
//            return region;
//        }
//
//        public void setRegion(String region) {
//            this.region = region;
//        }
//
//        public Object getRegionId() {
//            return regionId;
//        }
//
//        public void setRegionId(Object regionId) {
//            this.regionId = regionId;
//        }
//
//        public String getRegionCode() {
//            return regionCode;
//        }
//
//        public void setRegionCode(String regionCode) {
//            this.regionCode = regionCode;
//        }
//
//        public String getCountryId() {
//            return countryId;
//        }
//
//        public void setCountryId(String countryId) {
//            this.countryId = countryId;
//        }
//
//        public List<String> getStreet() {
//            return street;
//        }
//
//        public void setStreet(List<String> street) {
//            this.street = street;
//        }
//
//        public String getTelephone() {
//            return telephone;
//        }
//
//        public void setTelephone(String telephone) {
//            this.telephone = telephone;
//        }
//
//        public Object getPostcode() {
//            return postcode;
//        }
//
//        public void setPostcode(Object postcode) {
//            this.postcode = postcode;
//        }
//
//        public String getCity() {
//            return city;
//        }
//
//        public void setCity(String city) {
//            this.city = city;
//        }
//
//        public String getFirstname() {
//            return firstname;
//        }
//
//        public void setFirstname(String firstname) {
//            this.firstname = firstname;
//        }
//
//        public String getLastname() {
//            return lastname;
//        }
//
//        public void setLastname(String lastname) {
//            this.lastname = lastname;
//        }
//
//        public Integer getCustomerId() {
//            return customerId;
//        }
//
//        public void setCustomerId(Integer customerId) {
//            this.customerId = customerId;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public void setEmail(String email) {
//            this.email = email;
//        }
//
//        public Integer getSameAsBilling() {
//            return sameAsBilling;
//        }
//
//        public void setSameAsBilling(Integer sameAsBilling) {
//            this.sameAsBilling = sameAsBilling;
//        }
//
//        public Integer getSaveInAddressBook() {
//            return saveInAddressBook;
//        }
//
//        public void setSaveInAddressBook(Integer saveInAddressBook) {
//            this.saveInAddressBook = saveInAddressBook;
//        }
//
//    }
//
//    public class Customer {
//
//        @SerializedName("id")
//        @Expose
//        private Integer id;
//        @SerializedName("group_id")
//        @Expose
//        private Integer groupId;
//        @SerializedName("default_billing")
//        @Expose
//        private String defaultBilling;
//        @SerializedName("default_shipping")
//        @Expose
//        private String defaultShipping;
//        @SerializedName("created_at")
//        @Expose
//        private String createdAt;
//        @SerializedName("updated_at")
//        @Expose
//        private String updatedAt;
//        @SerializedName("created_in")
//        @Expose
//        private String createdIn;
//        @SerializedName("email")
//        @Expose
//        private String email;
//        @SerializedName("firstname")
//        @Expose
//        private String firstname;
//        @SerializedName("lastname")
//        @Expose
//        private String lastname;
//        @SerializedName("store_id")
//        @Expose
//        private Integer storeId;
//        @SerializedName("website_id")
//        @Expose
//        private Integer websiteId;
//        @SerializedName("addresses")
//        @Expose
//        private List<Address> addresses = null;
//        @SerializedName("disable_auto_group_change")
//        @Expose
//        private Integer disableAutoGroupChange;
//
//        public Integer getId() {
//            return id;
//        }
//
//        public void setId(Integer id) {
//            this.id = id;
//        }
//
//        public Integer getGroupId() {
//            return groupId;
//        }
//
//        public void setGroupId(Integer groupId) {
//            this.groupId = groupId;
//        }
//
//        public String getDefaultBilling() {
//            return defaultBilling;
//        }
//
//        public void setDefaultBilling(String defaultBilling) {
//            this.defaultBilling = defaultBilling;
//        }
//
//        public String getDefaultShipping() {
//            return defaultShipping;
//        }
//
//        public void setDefaultShipping(String defaultShipping) {
//            this.defaultShipping = defaultShipping;
//        }
//
//        public String getCreatedAt() {
//            return createdAt;
//        }
//
//        public void setCreatedAt(String createdAt) {
//            this.createdAt = createdAt;
//        }
//
//        public String getUpdatedAt() {
//            return updatedAt;
//        }
//
//        public void setUpdatedAt(String updatedAt) {
//            this.updatedAt = updatedAt;
//        }
//
//        public String getCreatedIn() {
//            return createdIn;
//        }
//
//        public void setCreatedIn(String createdIn) {
//            this.createdIn = createdIn;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public void setEmail(String email) {
//            this.email = email;
//        }
//
//        public String getFirstname() {
//            return firstname;
//        }
//
//        public void setFirstname(String firstname) {
//            this.firstname = firstname;
//        }
//
//        public String getLastname() {
//            return lastname;
//        }
//
//        public void setLastname(String lastname) {
//            this.lastname = lastname;
//        }
//
//        public Integer getStoreId() {
//            return storeId;
//        }
//
//        public void setStoreId(Integer storeId) {
//            this.storeId = storeId;
//        }
//
//        public Integer getWebsiteId() {
//            return websiteId;
//        }
//
//        public void setWebsiteId(Integer websiteId) {
//            this.websiteId = websiteId;
//        }
//
//        public List<Address> getAddresses() {
//            return addresses;
//        }
//
//        public void setAddresses(List<Address> addresses) {
//            this.addresses = addresses;
//        }
//
//        public Integer getDisableAutoGroupChange() {
//            return disableAutoGroupChange;
//        }
//
//        public void setDisableAutoGroupChange(Integer disableAutoGroupChange) {
//            this.disableAutoGroupChange = disableAutoGroupChange;
//        }
//
//    }
//
//    public class Address {
//
//        @SerializedName("id")
//        @Expose
//        private Integer id;
//        @SerializedName("customer_id")
//        @Expose
//        private Integer customerId;
//        @SerializedName("region")
//        @Expose
//        private Region region;
//        @SerializedName("region_id")
//        @Expose
//        private Integer regionId;
//        @SerializedName("country_id")
//        @Expose
//        private String countryId;
//        @SerializedName("street")
//        @Expose
//        private List<String> street = null;
//        @SerializedName("telephone")
//        @Expose
//        private String telephone;
//        @SerializedName("city")
//        @Expose
//        private String city;
//        @SerializedName("firstname")
//        @Expose
//        private String firstname;
//        @SerializedName("lastname")
//        @Expose
//        private String lastname;
//        @SerializedName("default_shipping")
//        @Expose
//        private Boolean defaultShipping;
//        @SerializedName("default_billing")
//        @Expose
//        private Boolean defaultBilling;
//
//        public Integer getId() {
//            return id;
//        }
//
//        public void setId(Integer id) {
//            this.id = id;
//        }
//
//        public Integer getCustomerId() {
//            return customerId;
//        }
//
//        public void setCustomerId(Integer customerId) {
//            this.customerId = customerId;
//        }
//
//        public Region getRegion() {
//            return region;
//        }
//
//        public void setRegion(Region region) {
//            this.region = region;
//        }
//
//        public Integer getRegionId() {
//            return regionId;
//        }
//
//        public void setRegionId(Integer regionId) {
//            this.regionId = regionId;
//        }
//
//        public String getCountryId() {
//            return countryId;
//        }
//
//        public void setCountryId(String countryId) {
//            this.countryId = countryId;
//        }
//
//        public List<String> getStreet() {
//            return street;
//        }
//
//        public void setStreet(List<String> street) {
//            this.street = street;
//        }
//
//        public String getTelephone() {
//            return telephone;
//        }
//
//        public void setTelephone(String telephone) {
//            this.telephone = telephone;
//        }
//
//        public String getCity() {
//            return city;
//        }
//
//        public void setCity(String city) {
//            this.city = city;
//        }
//
//        public String getFirstname() {
//            return firstname;
//        }
//
//        public void setFirstname(String firstname) {
//            this.firstname = firstname;
//        }
//
//        public String getLastname() {
//            return lastname;
//        }
//
//        public void setLastname(String lastname) {
//            this.lastname = lastname;
//        }
//
//        public Boolean getDefaultShipping() {
//            return defaultShipping;
//        }
//
//        public void setDefaultShipping(Boolean defaultShipping) {
//            this.defaultShipping = defaultShipping;
//        }
//
//        public Boolean getDefaultBilling() {
//            return defaultBilling;
//        }
//
//        public void setDefaultBilling(Boolean defaultBilling) {
//            this.defaultBilling = defaultBilling;
//        }
//
//    }
//
//    public class BillingAddress {
//
//        @SerializedName("id")
//        @Expose
//        private Integer id;
//        @SerializedName("region")
//        @Expose
//        private Object region;
//        @SerializedName("region_id")
//        @Expose
//        private Object regionId;
//        @SerializedName("region_code")
//        @Expose
//        private Object regionCode;
//        @SerializedName("country_id")
//        @Expose
//        private String countryId;
//        @SerializedName("street")
//        @Expose
//        private List<String> street = null;
//        @SerializedName("telephone")
//        @Expose
//        private String telephone;
//        @SerializedName("postcode")
//        @Expose
//        private Object postcode;
//        @SerializedName("city")
//        @Expose
//        private String city;
//        @SerializedName("firstname")
//        @Expose
//        private String firstname;
//        @SerializedName("lastname")
//        @Expose
//        private String lastname;
//        @SerializedName("customer_id")
//        @Expose
//        private Integer customerId;
//        @SerializedName("email")
//        @Expose
//        private String email;
//        @SerializedName("same_as_billing")
//        @Expose
//        private Integer sameAsBilling;
//        @SerializedName("customer_address_id")
//        @Expose
//        private Integer customerAddressId;
//        @SerializedName("save_in_address_book")
//        @Expose
//        private Integer saveInAddressBook;
//
//        public Integer getId() {
//            return id;
//        }
//
//        public void setId(Integer id) {
//            this.id = id;
//        }
//
//        public Object getRegion() {
//            return region;
//        }
//
//        public void setRegion(Object region) {
//            this.region = region;
//        }
//
//        public Object getRegionId() {
//            return regionId;
//        }
//
//        public void setRegionId(Object regionId) {
//            this.regionId = regionId;
//        }
//
//        public Object getRegionCode() {
//            return regionCode;
//        }
//
//        public void setRegionCode(Object regionCode) {
//            this.regionCode = regionCode;
//        }
//
//        public String getCountryId() {
//            return countryId;
//        }
//
//        public void setCountryId(String countryId) {
//            this.countryId = countryId;
//        }
//
//        public List<String> getStreet() {
//            return street;
//        }
//
//        public void setStreet(List<String> street) {
//            this.street = street;
//        }
//
//        public String getTelephone() {
//            return telephone;
//        }
//
//        public void setTelephone(String telephone) {
//            this.telephone = telephone;
//        }
//
//        public Object getPostcode() {
//            return postcode;
//        }
//
//        public void setPostcode(Object postcode) {
//            this.postcode = postcode;
//        }
//
//        public String getCity() {
//            return city;
//        }
//
//        public void setCity(String city) {
//            this.city = city;
//        }
//
//        public String getFirstname() {
//            return firstname;
//        }
//
//        public void setFirstname(String firstname) {
//            this.firstname = firstname;
//        }
//
//        public String getLastname() {
//            return lastname;
//        }
//
//        public void setLastname(String lastname) {
//            this.lastname = lastname;
//        }
//
//        public Integer getCustomerId() {
//            return customerId;
//        }
//
//        public void setCustomerId(Integer customerId) {
//            this.customerId = customerId;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public void setEmail(String email) {
//            this.email = email;
//        }
//
//        public Integer getSameAsBilling() {
//            return sameAsBilling;
//        }
//
//        public void setSameAsBilling(Integer sameAsBilling) {
//            this.sameAsBilling = sameAsBilling;
//        }
//
//        public Integer getCustomerAddressId() {
//            return customerAddressId;
//        }
//
//        public void setCustomerAddressId(Integer customerAddressId) {
//            this.customerAddressId = customerAddressId;
//        }
//
//        public Integer getSaveInAddressBook() {
//            return saveInAddressBook;
//        }
//
//        public void setSaveInAddressBook(Integer saveInAddressBook) {
//            this.saveInAddressBook = saveInAddressBook;
//        }
//
//    }
//
//
//    public class Currency {
//
//        @SerializedName("global_currency_code")
//        @Expose
//        private String globalCurrencyCode;
//        @SerializedName("base_currency_code")
//        @Expose
//        private String baseCurrencyCode;
//        @SerializedName("store_currency_code")
//        @Expose
//        private String storeCurrencyCode;
//        @SerializedName("quote_currency_code")
//        @Expose
//        private String quoteCurrencyCode;
//        @SerializedName("store_to_base_rate")
//        @Expose
//        private Integer storeToBaseRate;
//        @SerializedName("store_to_quote_rate")
//        @Expose
//        private Integer storeToQuoteRate;
//        @SerializedName("base_to_global_rate")
//        @Expose
//        private Integer baseToGlobalRate;
//        @SerializedName("base_to_quote_rate")
//        @Expose
//        private Integer baseToQuoteRate;
//
//        public String getGlobalCurrencyCode() {
//            return globalCurrencyCode;
//        }
//
//        public void setGlobalCurrencyCode(String globalCurrencyCode) {
//            this.globalCurrencyCode = globalCurrencyCode;
//        }
//
//        public String getBaseCurrencyCode() {
//            return baseCurrencyCode;
//        }
//
//        public void setBaseCurrencyCode(String baseCurrencyCode) {
//            this.baseCurrencyCode = baseCurrencyCode;
//        }
//
//        public String getStoreCurrencyCode() {
//            return storeCurrencyCode;
//        }
//
//        public void setStoreCurrencyCode(String storeCurrencyCode) {
//            this.storeCurrencyCode = storeCurrencyCode;
//        }
//
//        public String getQuoteCurrencyCode() {
//            return quoteCurrencyCode;
//        }
//
//        public void setQuoteCurrencyCode(String quoteCurrencyCode) {
//            this.quoteCurrencyCode = quoteCurrencyCode;
//        }
//
//        public Integer getStoreToBaseRate() {
//            return storeToBaseRate;
//        }
//
//        public void setStoreToBaseRate(Integer storeToBaseRate) {
//            this.storeToBaseRate = storeToBaseRate;
//        }
//
//        public Integer getStoreToQuoteRate() {
//            return storeToQuoteRate;
//        }
//
//        public void setStoreToQuoteRate(Integer storeToQuoteRate) {
//            this.storeToQuoteRate = storeToQuoteRate;
//        }
//
//        public Integer getBaseToGlobalRate() {
//            return baseToGlobalRate;
//        }
//
//        public void setBaseToGlobalRate(Integer baseToGlobalRate) {
//            this.baseToGlobalRate = baseToGlobalRate;
//        }
//
//        public Integer getBaseToQuoteRate() {
//            return baseToQuoteRate;
//        }
//
//        public void setBaseToQuoteRate(Integer baseToQuoteRate) {
//            this.baseToQuoteRate = baseToQuoteRate;
//        }
//
//    }
//
//    public class ShippingAssignment {
//
//        @SerializedName("shipping")
//        @Expose
//        private Shipping shipping;
//        @SerializedName("items")
//        @Expose
//        private List<ShippingItem> items = null;
//
//        public Shipping getShipping() {
//            return shipping;
//        }
//
//        public void setShipping(Shipping shipping) {
//            this.shipping = shipping;
//        }
//
//        public List<ShippingItem> getItems() {
//            return items;
//        }
//
//        public void setItems(List<ShippingItem> items) {
//            this.items = items;
//        }
//
//    }
//
//    public class ShippingItem {
//
//        @SerializedName("item_id")
//        @Expose
//        private Integer itemId;
//        @SerializedName("sku")
//        @Expose
//        private String sku;
//        @SerializedName("qty")
//        @Expose
//        private Integer qty;
//        @SerializedName("name")
//        @Expose
//        private String name;
//        @SerializedName("price")
//        @Expose
//        private Integer price;
//        @SerializedName("product_type")
//        @Expose
//        private String productType;
//        @SerializedName("quote_id")
//        @Expose
//        private String quoteId;
//
//        public Integer getItemId() {
//            return itemId;
//        }
//
//        public void setItemId(Integer itemId) {
//            this.itemId = itemId;
//        }
//
//        public String getSku() {
//            return sku;
//        }
//
//        public void setSku(String sku) {
//            this.sku = sku;
//        }
//
//        public Integer getQty() {
//            return qty;
//        }
//
//        public void setQty(Integer qty) {
//            this.qty = qty;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public Integer getPrice() {
//            return price;
//        }
//
//        public void setPrice(Integer price) {
//            this.price = price;
//        }
//
//        public String getProductType() {
//            return productType;
//        }
//
//        public void setProductType(String productType) {
//            this.productType = productType;
//        }
//
//        public String getQuoteId() {
//            return quoteId;
//        }
//
//        public void setQuoteId(String quoteId) {
//            this.quoteId = quoteId;
//        }
//
//    }
//
//    public class ExtensionAttributes {
//
//        @SerializedName("shipping_assignments")
//        @Expose
//        private List<ShippingAssignment> shippingAssignments = null;
//
//        public List<ShippingAssignment> getShippingAssignments() {
//            return shippingAssignments;
//        }
//
//        public void setShippingAssignments(List<ShippingAssignment> shippingAssignments) {
//            this.shippingAssignments = shippingAssignments;
//        }
//
//    }
}
