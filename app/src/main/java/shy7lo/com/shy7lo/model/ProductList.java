package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JITEN-PC on 28-12-2016.
 */

public class ProductList {

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

        @SerializedName("total_count")
        @Expose
        private Integer totalCount;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("product_listing")
        @Expose
        private List<ProductInfo> productInfoData = null;
//        @SerializedName("sub_categories")
//        @Expose
//        public List<ChildCategory> category;
        @SerializedName("category_banner")
        @Expose
        public CategoryBanner category_banner;
        @SerializedName("sub_categories")
        @Expose
        public Category category;

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public List<ProductInfo> getProductInfoData() {
            return productInfoData;
        }

        public void setProductInfoData(List<ProductInfo> productInfoData) {
            this.productInfoData = productInfoData;
        }

//        public List<ChildCategory getCategory() {
//            return category;
//        }
//
//        public void setCategory(ChildCategory category) {
//            this.category = category;
//        }

    }

    public class CategoryBanner{
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("url")
        @Expose
        public String url;
    }

    public class Category {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("child_count")
        @Expose
        public Integer childCount;
        @SerializedName("child_data")
        @Expose
        public List<ChildCategory> childCategory = null;

    }

    public class ChildCategory {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("thumb")
        @Expose
        public String thumb;
        @SerializedName("count")
        @Expose
        public int count;
//        @SerializedName("in_stock_items")
//        @Expose
//        public int in_stock_items;
//        @SerializedName("product_listing_flag")
//        @Expose
//        public boolean product_listing_flag;

    }

    public class ProductInfo {

        @SerializedName("entity_id")
        @Expose
        private String entityId;
        @SerializedName("sku")
        @Expose
        private String sku;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("description")
        @Expose
        private String shortDescription;
        @SerializedName("type_id")
        @Expose
        private String typeId;
        @SerializedName("price")
        @Expose
        private float price;
        @SerializedName("special_price")
        @Expose
        private float specialPrice;
        @SerializedName("percent_off")
        @Expose
        private float percentOff;
        @SerializedName("special_from_date")
        @Expose
        private String special_from_date;
        @SerializedName("special_to_date")
        @Expose
        private String special_to_date;

        @SerializedName("formatedSpecialPrice")
        @Expose
        private String formatedSpecialPrice;
        @SerializedName("image")
        @Expose
        private String thumbNail;
        @SerializedName("brand")
        @Expose
        private String brand;
        @SerializedName("tag")
        @Expose
        private boolean tag;
        @SerializedName("tag_status")
        @Expose
        private TagStatus tagStatus;

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFormatedSpecialPrice() {
            return formatedSpecialPrice;
        }

        public void setFormatedSpecialPrice(String formatedSpecialPrice) {
            this.formatedSpecialPrice = formatedSpecialPrice;
        }

        public float getSpecialPrice() {
            return specialPrice;
        }

        public void setSpecialPrice(float specialPrice) {
            this.specialPrice = specialPrice;
        }

        public float getPercentOff() {
            return percentOff;
        }

        public void setPercentOff(float percentOff) {
            this.percentOff = percentOff;
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

        public boolean isTag() {
            return tag;
        }

        public void setTag(boolean tag) {
            this.tag = tag;
        }

        public TagStatus getTagStatus() {
            return tagStatus;
        }

        public void setTagStatus(TagStatus tagStatus) {
            this.tagStatus = tagStatus;
        }
    }

    public class TagStatus implements Serializable {

        @SerializedName("mode")
        @Expose
        public String mode;
        @SerializedName("en")
        @Expose
        public String en;
        @SerializedName("ar")
        @Expose
        public String ar;
        @SerializedName("color")
        @Expose
        public String color;
        @SerializedName("bg_color")
        @Expose
        public String bg_color;
        @SerializedName("url_ar")
        @Expose
        public String url_ar;
        @SerializedName("url_en")
        @Expose
        public String url_en;
    }


//    public class Option implements Serializable {
//
//        @SerializedName("label")
//        @Expose
//        private String label;
//        @SerializedName("id")
//        @Expose
//        private String id;
//        @SerializedName("count")
//        @Expose
//        private String count;
//
//        public boolean status;
//
//        public String getLabel() {
//            return label;
//        }
//
//        public void setLabel(String label) {
//            this.label = label;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getCount() {
//            return count;
//        }
//
//        public void setCount(String count) {
//            this.count = count;
//        }
//
//        public boolean isStatus() {
//            return status;
//        }
//
//        public void setStatus(boolean status) {
//            this.status = status;
//        }
//    }


}
