package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JITEN-PC on 11-05-2017.
 */

public class Wishlist {

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

        @SerializedName("wishlistData")
        @Expose
        private List<WishlistData> wishlistData = null;

        public List<WishlistData> getWishlistData() {
            return wishlistData;
        }

        public void setWishlistData(List<WishlistData> wishlistData) {
            this.wishlistData = wishlistData;
        }

    }

    public class WishlistData {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("sku")
        @Expose
        private String sku;
        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("type")
        @Expose
        private String typeId;
        @SerializedName("stock_status")
        @Expose
        private String stockStatus;
        @SerializedName("stock_qty")
        @Expose
        private int stockQty;
        @SerializedName("qty")
        @Expose
        private Integer qty;
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
        @SerializedName("image")
        @Expose
        private String thumbNail;
        @SerializedName("brand")
        @Expose
        private String brand;
        @SerializedName("rating")
        @Expose
        private Integer rating;

        @SerializedName("configurable_attributes")
        @Expose
        public ConfigurableAttributes configurableAttributes;

        private String isGuest;

        private String token;

        private String soft_delete;

        private String is_on_server;

        private String size;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            this.qty = qty;
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

        public String getThumbNail() {
            return thumbNail;
        }

        public void setThumbNail(String thumbNail) {
            this.thumbNail = thumbNail;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
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

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getStockStatus() {
            return stockStatus;
        }

        public void setStockStatus(String stockStatus) {
            this.stockStatus = stockStatus;
        }

        public int getStockQty() {
            return stockQty;
        }

        public void setStockQty(int stockQty) {
            this.stockQty = stockQty;
        }
    }

    public class ConfigurableAttributes {

        @SerializedName("TotalRecord")
        @Expose
        public Integer totalRecord;
        @SerializedName("attribute_details")
        @Expose
        public List<AttributeDetail> attributeDetails = null;

    }

    public class AttributeDetail {

        @SerializedName("attribute_id")
        @Expose
        public String attributeId;
        @SerializedName("attribute_code")
        @Expose
        public String attributeCode;
        @SerializedName("option")
        @Expose
        public List<Option> option = null;

    }

    public class Option {

        @SerializedName("entity_id")
        @Expose
        public String entityId;
        @SerializedName("sku")
        @Expose
        public String sku;
        @SerializedName("qty")
        @Expose
        public Integer qty;
        @SerializedName("is_in_stock")
        @Expose
        public Boolean isInStock;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("price")
        @Expose
        public float price;
        @SerializedName("special_price")
        @Expose
        public float specialPrice;
        @SerializedName("special_from_date")
        @Expose
        public String specialFromDate;
        @SerializedName("special_to_date")
        @Expose
        public String specialToDate;
        @SerializedName("value_index")
        @Expose
        public String valueIndex;
        @SerializedName("default_label")
        @Expose
        public String defaultLabel;
        @SerializedName("label")
        @Expose
        public String label;
        @SerializedName("uk_label")
        @Expose
        public String ukLabel;
        @SerializedName("eu_label")
        @Expose
        public String euLabel;

    }

}
