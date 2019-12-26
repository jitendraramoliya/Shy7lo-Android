package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JITEN-PC on 16-02-2017.
 */

public class ShoppingBag {

    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Data data;

    public class Data {

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
        //        @SerializedName("customer")
//        @Expose
//        private Customer customer;
//        @SerializedName("billing_address")
//        @Expose
//        private BillingAddress billingAddress;
        @SerializedName("orig_order_id")
        @Expose
        private Integer origOrderId;
        //        @SerializedName("currency")
//        @Expose
//        private Currency currency;
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
        @SerializedName("cart_promotion_msg")
        @Expose
        private String cartPromotionMsg;
//        @SerializedName("extension_attributes")
//        @Expose
//        private ExtensionAttributes extensionAttributes;

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

//        public Customer getCustomer() {
//            return customer;
//        }
//
//        public void setCustomer(Customer customer) {
//            this.customer = customer;
//        }
//
//        public BillingAddress getBillingAddress() {
//            return billingAddress;
//        }
//
//        public void setBillingAddress(BillingAddress billingAddress) {
//            this.billingAddress = billingAddress;
//        }

        public Integer getOrigOrderId() {
            return origOrderId;
        }

        public void setOrigOrderId(Integer origOrderId) {
            this.origOrderId = origOrderId;
        }

//        public Currency getCurrency() {
//            return currency;
//        }
//
//        public void setCurrency(Currency currency) {
//            this.currency = currency;
//        }

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

        public String getCartPromotionMsg() {
            return cartPromotionMsg;
        }

        public void setCartPromotionMsg(String cartPromotionMsg) {
            this.cartPromotionMsg = cartPromotionMsg;
        }

        //        public ExtensionAttributes getExtensionAttributes() {
//            return extensionAttributes;
//        }
//
//        public void setExtensionAttributes(ExtensionAttributes extensionAttributes) {
//            this.extensionAttributes = extensionAttributes;
//        }

    }

    public class Item {

        @SerializedName("item_id")
        @Expose
        private Integer itemId;
        @SerializedName("sku")
        @Expose
        private String sku;
        @SerializedName("parent_sku")
        @Expose
        private String parent_sku;
        @SerializedName("qty")
        @Expose
        private Integer qty;
        @SerializedName("name")
        @Expose
        private String name;
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
        @SerializedName("price_excl_tax")
        @Expose
        private float price_excl_tax;
        @SerializedName("product_type")
        @Expose
        private String productType;
        @SerializedName("quote_id")
        @Expose
        private String quoteId;
        @SerializedName("stock_qty")
        @Expose
        private int stockQty;
        @SerializedName("stock_status")
        @Expose
        private String stockStatus;
        @SerializedName("image")
        @Expose
        private String imageFIle;
        @SerializedName("brand")
        @Expose
        private String brand;
        private String option_lable;
        private String option_value;
        private String attribute_id;
        private String value_index;

        @SerializedName("configure_option")
        @Expose
        private List<ConfigureOption> configureOptionList = null;

        @SerializedName("custom_msg")
        @Expose
        private CustomMsg custom_msg;

        private String isGuest;

        private String token;

        private String soft_delete;

        private String is_on_server;

        private boolean isWishListItem = false;

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

        public String getParent_sku() {
            return parent_sku;
        }

        public void setParent_sku(String parent_sku) {
            this.parent_sku = parent_sku;
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

        public float getPrice_excl_tax() {
            return price_excl_tax;
        }

        public void setPrice_excl_tax(float price_excl_tax) {
            this.price_excl_tax = price_excl_tax;
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

        public int getStockQty() {
            return stockQty;
        }

        public void setStockQty(int stockQty) {
            this.stockQty = stockQty;
        }

        public String getStockStatus() {
            return stockStatus;
        }

        public String getAttribute_id() {
            return attribute_id;
        }

        public void setAttribute_id(String attribute_id) {
            this.attribute_id = attribute_id;
        }

        public String getValue_index() {
            return value_index;
        }

        public void setValue_index(String value_index) {
            this.value_index = value_index;
        }

        public void setStockStatus(String stockStatus) {
            this.stockStatus = stockStatus;
        }

        public String getImageFIle() {
            return imageFIle;
        }

        public void setImageFIle(String imageFIle) {
            this.imageFIle = imageFIle;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public List<ConfigureOption> getConfigureOptionList() {
            return configureOptionList;
        }

        public void setConfigureOptionList(List<ConfigureOption> configureOptionList) {
            this.configureOptionList = configureOptionList;
        }

        public CustomMsg getCustom_msg() {
            return custom_msg;
        }

        public void setCustom_msg(CustomMsg custom_msg) {
            this.custom_msg = custom_msg;
        }

        public boolean isWishListItem() {
            return isWishListItem;
        }

        public void setWishListItem(boolean wishListItem) {
            isWishListItem = wishListItem;
        }

        public String getIsGuest() {
            return isGuest;
        }

        public void setIsGuest(String isGuest) {
            this.isGuest = isGuest;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getOption_lable() {
            return option_lable;
        }

        public void setOption_lable(String option_lable) {
            this.option_lable = option_lable;
        }

        public String getOption_value() {
            return option_value;
        }

        public void setOption_value(String option_value) {
            this.option_value = option_value;
        }

        public String getSoft_delete() {
            return soft_delete;
        }

        public void setSoft_delete(String soft_delete) {
            this.soft_delete = soft_delete;
        }

        public String getIs_on_server() {
            return is_on_server;
        }

        public void setIs_on_server(String is_on_server) {
            this.is_on_server = is_on_server;
        }
    }

    public class CustomMsg {

        @SerializedName("msg")
        @Expose
        private String msg;
        @SerializedName("color")
        @Expose
        private String color;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
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

//    public class Customer {
//
//        @SerializedName("email")
//        @Expose
//        private Object email;
//        @SerializedName("firstname")
//        @Expose
//        private Object firstname;
//        @SerializedName("lastname")
//        @Expose
//        private Object lastname;
//
//        public Object getEmail() {
//            return email;
//        }
//
//        public void setEmail(Object email) {
//            this.email = email;
//        }
//
//        public Object getFirstname() {
//            return firstname;
//        }
//
//        public void setFirstname(Object firstname) {
//            this.firstname = firstname;
//        }
//
//        public Object getLastname() {
//            return lastname;
//        }
//
//        public void setLastname(Object lastname) {
//            this.lastname = lastname;
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
//        private Object countryId;
//        @SerializedName("street")
//        @Expose
//        private List<String> street = null;
//        @SerializedName("telephone")
//        @Expose
//        private Object telephone;
//        @SerializedName("postcode")
//        @Expose
//        private Object postcode;
//        @SerializedName("city")
//        @Expose
//        private Object city;
//        @SerializedName("firstname")
//        @Expose
//        private Object firstname;
//        @SerializedName("lastname")
//        @Expose
//        private Object lastname;
//        @SerializedName("email")
//        @Expose
//        private Object email;
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
//        public Object getCountryId() {
//            return countryId;
//        }
//
//        public void setCountryId(Object countryId) {
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
//        public Object getTelephone() {
//            return telephone;
//        }
//
//        public void setTelephone(Object telephone) {
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
//        public Object getCity() {
//            return city;
//        }
//
//        public void setCity(Object city) {
//            this.city = city;
//        }
//
//        public Object getFirstname() {
//            return firstname;
//        }
//
//        public void setFirstname(Object firstname) {
//            this.firstname = firstname;
//        }
//
//        public Object getLastname() {
//            return lastname;
//        }
//
//        public void setLastname(Object lastname) {
//            this.lastname = lastname;
//        }
//
//        public Object getEmail() {
//            return email;
//        }
//
//        public void setEmail(Object email) {
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
//
//    public class ShippingAssignment {
//
//        @SerializedName("shipping")
//        @Expose
//        private Shipping shipping;
//        @SerializedName("items")
//        @Expose
//        private List<Item_> items = null;
//
//        public Shipping getShipping() {
//            return shipping;
//        }
//
//        public void setShipping(Shipping shipping) {
//            this.shipping = shipping;
//        }
//
//        public List<Item_> getItems() {
//            return items;
//        }
//
//        public void setItems(List<Item_> items) {
//            this.items = items;
//        }
//
//    }
//
//    public class Shipping {
//
//        @SerializedName("address")
//        @Expose
//        private Address address;
//        @SerializedName("method")
//        @Expose
//        private Object method;
//
//        public Address getAddress() {
//            return address;
//        }
//
//        public void setAddress(Address address) {
//            this.address = address;
//        }
//
//        public Object getMethod() {
//            return method;
//        }
//
//        public void setMethod(Object method) {
//            this.method = method;
//        }
//
//    }
//
//    public class Item_ {
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
//    public class Address {
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
//        private Object countryId;
//        @SerializedName("street")
//        @Expose
//        private List<String> street = null;
//        @SerializedName("telephone")
//        @Expose
//        private Object telephone;
//        @SerializedName("postcode")
//        @Expose
//        private Object postcode;
//        @SerializedName("city")
//        @Expose
//        private Object city;
//        @SerializedName("firstname")
//        @Expose
//        private Object firstname;
//        @SerializedName("lastname")
//        @Expose
//        private Object lastname;
//        @SerializedName("email")
//        @Expose
//        private Object email;
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
//        public Object getCountryId() {
//            return countryId;
//        }
//
//        public void setCountryId(Object countryId) {
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
//        public Object getTelephone() {
//            return telephone;
//        }
//
//        public void setTelephone(Object telephone) {
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
//        public Object getCity() {
//            return city;
//        }
//
//        public void setCity(Object city) {
//            this.city = city;
//        }
//
//        public Object getFirstname() {
//            return firstname;
//        }
//
//        public void setFirstname(Object firstname) {
//            this.firstname = firstname;
//        }
//
//        public Object getLastname() {
//            return lastname;
//        }
//
//        public void setLastname(Object lastname) {
//            this.lastname = lastname;
//        }
//
//        public Object getEmail() {
//            return email;
//        }
//
//        public void setEmail(Object email) {
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


}
