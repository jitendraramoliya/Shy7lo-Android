package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JITEN-PC on 14-02-2017.
 */

public class ProductDetails {

    @SerializedName("entity_id")
    @Expose
    private String entityId;
    @SerializedName("attribute_set_id")
    @Expose
    private String attributeSetId;
    @SerializedName("type_id")
    @Expose
    private String typeId;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("color")
    @Expose
    private CircleColor circleColor;
    @SerializedName("has_options")
    @Expose
    private String hasOptions;
    @SerializedName("required_options")
    @Expose
    private String requiredOptions;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("search_weight")
    @Expose
    private String searchWeight;
    @SerializedName("color_code")
    @Expose
    private String color;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("perfume_size")
    @Expose
    private String perfume_size;

    @SerializedName("frame_size")
    @Expose
    private String frame_size;

    @SerializedName("frame_type")
    @Expose
    private String frame_type;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("small_image")
    @Expose
    private String smallImage;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("options_container")
    @Expose
    private String optionsContainer;
    @SerializedName("msrp_display_actual_price_type")
    @Expose
    private String msrpDisplayActualPriceType;
    @SerializedName("url_path")
    @Expose
    private String urlPath;
    @SerializedName("url_key")
    @Expose
    private String urlKey;
    @SerializedName("size_chart")
    @Expose
    private String size_chart;
    @SerializedName("gift_message_available")
    @Expose
    private String giftMessageAvailable;
    @SerializedName("material")
    @Expose
    private String material;
    @SerializedName("pattern")
    @Expose
    private String pattern;
    @SerializedName("climate")
    @Expose
    private String climate;
    @SerializedName("manufacturer")
    @Expose
    private String manufacturer;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("visibility")
    @Expose
    private String visibility;
//    @SerializedName("quantity_and_stock_status")
//    @Expose
//    private StockQty stockQty;
    @SerializedName("tax_class_id")
    @Expose
    private String taxClassId;
    @SerializedName("eco_collection")
    @Expose
    private String ecoCollection;
    @SerializedName("performance_fabric")
    @Expose
    private String performanceFabric;
    @SerializedName("erin_recommends")
    @Expose
    private String erinRecommends;
    @SerializedName("new")
    @Expose
    private String _new;
    @SerializedName("sale")
    @Expose
    private String sale;
    @SerializedName("price")
    @Expose
    private float price;
    @SerializedName("special_price")
    @Expose
    private float special_price;
    @SerializedName("special_from_date")
    @Expose
    private String special_from_date;
    @SerializedName("special_to_date")
    @Expose
    private String special_to_date;
    @SerializedName("price_excl_tax")
    @Expose
    private Float price_excl_tax;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("related_product_status")
    @Expose
    public String related_product_status;
    @SerializedName("country_sizes")
    @Expose
    public List<CountrySizes> countrySizes = null;
    @SerializedName("category_ids")
    @Expose
    public List<String> category_ids = null;
    @SerializedName("media_gallery")
    @Expose
    private List<Image_> images = null;
    @SerializedName("custom_msg")
    @Expose
    private CustomMsg custommsg = null;
    @SerializedName("stock_status")
    @Expose
    private String stockStatus;
    @SerializedName("stock_qty")
    @Expose
    private int stockQty;
    @SerializedName("options")
    @Expose
    private List<Option> options = null;

    @SerializedName("configurable_attributes")
    @Expose
    private ConfigurableAttributes configurableAttributes;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getAttributeSetId() {
        return attributeSetId;
    }

    public void setAttributeSetId(String attributeSetId) {
        this.attributeSetId = attributeSetId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getFrame_size() {
        return frame_size;
    }

    public void setFrame_size(String frame_size) {
        this.frame_size = frame_size;
    }

    public String getFrame_type() {
        return frame_type;
    }

    public void setFrame_type(String frame_type) {
        this.frame_type = frame_type;
    }

    public String getPerfume_size() {
        return perfume_size;
    }

    public void setPerfume_size(String perfume_size) {
        this.perfume_size = perfume_size;
    }

    public String getHasOptions() {
        return hasOptions;
    }

    public void setHasOptions(String hasOptions) {
        this.hasOptions = hasOptions;
    }

    public String getRequiredOptions() {
        return requiredOptions;
    }

    public void setRequiredOptions(String requiredOptions) {
        this.requiredOptions = requiredOptions;
    }


    public CircleColor getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(CircleColor circleColor) {
        this.circleColor = circleColor;
    }

    public String getSize_chart() {
        return size_chart;
    }

    public void setSize_chart(String size_chart) {
        this.size_chart = size_chart;
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

    public String getSearchWeight() {
        return searchWeight;
    }

    public void setSearchWeight(String searchWeight) {
        this.searchWeight = searchWeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOptionsContainer() {
        return optionsContainer;
    }

    public void setOptionsContainer(String optionsContainer) {
        this.optionsContainer = optionsContainer;
    }

    public String getMsrpDisplayActualPriceType() {
        return msrpDisplayActualPriceType;
    }

    public void setMsrpDisplayActualPriceType(String msrpDisplayActualPriceType) {
        this.msrpDisplayActualPriceType = msrpDisplayActualPriceType;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public String getGiftMessageAvailable() {
        return giftMessageAvailable;
    }

    public void setGiftMessageAvailable(String giftMessageAvailable) {
        this.giftMessageAvailable = giftMessageAvailable;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
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

    //    public StockQty getStockQty() {
//        return stockQty;
//    }
//
//    public void setStockQty(StockQty stockQty) {
//        this.stockQty = stockQty;
//    }

    public String getTaxClassId() {
        return taxClassId;
    }

    public void setTaxClassId(String taxClassId) {
        this.taxClassId = taxClassId;
    }

    public String getEcoCollection() {
        return ecoCollection;
    }

    public void setEcoCollection(String ecoCollection) {
        this.ecoCollection = ecoCollection;
    }

    public String getPerformanceFabric() {
        return performanceFabric;
    }

    public void setPerformanceFabric(String performanceFabric) {
        this.performanceFabric = performanceFabric;
    }

    public String getErinRecommends() {
        return erinRecommends;
    }

    public void setErinRecommends(String erinRecommends) {
        this.erinRecommends = erinRecommends;
    }

    public String getNew() {
        return _new;
    }

    public void setNew(String _new) {
        this._new = _new;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getSpecial_price() {
        return special_price;
    }

    public void setSpecial_price(float special_price) {
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

    public Float getPrice_excl_tax() {
        return price_excl_tax;
    }

    public void setPrice_excl_tax(Float price_excl_tax) {
        this.price_excl_tax = price_excl_tax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Image_> getImages() {
        return images;
    }

    public void setImages(List<Image_> images) {
        this.images = images;
    }

    public CustomMsg getCustommsg() {
        return custommsg;
    }

    public void setCustommsg(CustomMsg custommsg) {
        this.custommsg = custommsg;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public ConfigurableAttributes getConfigurableAttributes() {
        return configurableAttributes;
    }

    public void setConfigurableAttributes(ConfigurableAttributes configurableAttributes) {
        this.configurableAttributes = configurableAttributes;
    }

    public class CountrySizes {
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("label_name")
        @Expose
        public String labelName;
    }

//    public class StockQty {
//
//        @SerializedName("is_in_stock")
//        @Expose
//        public boolean is_in_stock;
//        @SerializedName("qty")
//        @Expose
//        public Integer Qty;
//    }

    public class CustomMsg {

        @SerializedName("shipping_msg")
        @Expose
        public ShippingMsg shippingMsg;
        @SerializedName("brand_msg")
        @Expose
        public BrandMsg brandMsg;
        @SerializedName("return_msg")
        @Expose
        public ReturnMsg returnMsg;
        @SerializedName("cash_msg")
        @Expose
        public CashMsg cashMsg;
        @SerializedName("cart_msg")
        @Expose
        public CartMsg cartMsg;
    }

    public class ShippingMsg {
        @SerializedName("en")
        @Expose
        public String en;
        @SerializedName("ar")
        @Expose
        public String ar;
        @SerializedName("color")
        @Expose
        public String color;
    }

    public class BrandMsg {
        @SerializedName("en")
        @Expose
        public String en;
        @SerializedName("ar")
        @Expose
        public String ar;
        @SerializedName("color")
        @Expose
        public String color;
    }

    public class ReturnMsg {
        @SerializedName("en")
        @Expose
        public String en;
        @SerializedName("ar")
        @Expose
        public String ar;
        @SerializedName("color")
        @Expose
        public String color;
    }

    public class CashMsg {
        @SerializedName("en")
        @Expose
        public String en;
        @SerializedName("ar")
        @Expose
        public String ar;
        @SerializedName("color")
        @Expose
        public String color;
    }

    public class CartMsg {
        @SerializedName("en")
        @Expose
        public String en;
        @SerializedName("ar")
        @Expose
        public String ar;
        @SerializedName("color")
        @Expose
        public String color;
    }

    public class CircleColor {
        @SerializedName("mode")
        @Expose
        public String mode;
        @SerializedName("source")
        @Expose
        public String source;
    }

    public class Image_ {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("image")
        @Expose
        private String url;
        @SerializedName("position")
        @Expose
        private String position;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

    }

    public class Option {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("use_default")
        @Expose
        private String useDefault;
        @SerializedName("position")
        @Expose
        private String position;
        @SerializedName("values")
        @Expose
        private List<Value> values = null;
        @SerializedName("attribute_id")
        @Expose
        private String attributeId;
        @SerializedName("attribute_code")
        @Expose
        private String attributeCode;
        @SerializedName("frontend_label")
        @Expose
        private String frontendLabel;
        @SerializedName("store_label")
        @Expose
        private String storeLabel;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getUseDefault() {
            return useDefault;
        }

        public void setUseDefault(String useDefault) {
            this.useDefault = useDefault;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public List<Value> getValues() {
            return values;
        }

        public void setValues(List<Value> values) {
            this.values = values;
        }

        public String getAttributeId() {
            return attributeId;
        }

        public void setAttributeId(String attributeId) {
            this.attributeId = attributeId;
        }

        public String getAttributeCode() {
            return attributeCode;
        }

        public void setAttributeCode(String attributeCode) {
            this.attributeCode = attributeCode;
        }

        public String getFrontendLabel() {
            return frontendLabel;
        }

        public void setFrontendLabel(String frontendLabel) {
            this.frontendLabel = frontendLabel;
        }

        public String getStoreLabel() {
            return storeLabel;
        }

        public void setStoreLabel(String storeLabel) {
            this.storeLabel = storeLabel;
        }

    }

    public class Value {

        @SerializedName("value_index")
        @Expose
        private String valueIndex;
        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("product_super_attribute_id")
        @Expose
        private String productSuperAttributeId;
        @SerializedName("default_label")
        @Expose
        private String defaultLabel;
        @SerializedName("store_label")
        @Expose
        private String storeLabel;
        @SerializedName("use_default_value")
        @Expose
        private Boolean useDefaultValue;

        private Boolean isSelected = false;

        public String getValueIndex() {
            return valueIndex;
        }

        public void setValueIndex(String valueIndex) {
            this.valueIndex = valueIndex;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getProductSuperAttributeId() {
            return productSuperAttributeId;
        }

        public void setProductSuperAttributeId(String productSuperAttributeId) {
            this.productSuperAttributeId = productSuperAttributeId;
        }

        public String getDefaultLabel() {
            return defaultLabel;
        }

        public void setDefaultLabel(String defaultLabel) {
            this.defaultLabel = defaultLabel;
        }

        public String getStoreLabel() {
            return storeLabel;
        }

        public void setStoreLabel(String storeLabel) {
            this.storeLabel = storeLabel;
        }

        public Boolean getUseDefaultValue() {
            return useDefaultValue;
        }

        public void setUseDefaultValue(Boolean useDefaultValue) {
            this.useDefaultValue = useDefaultValue;
        }

        public Boolean getSelected() {
            return isSelected;
        }

        public void setSelected(Boolean selected) {
            isSelected = selected;
        }
    }

    public class ConfigurableAttributes {

        @SerializedName("TotalRecord")
        @Expose
        private Integer totalRecord;
        @SerializedName("attribute_details")
        @Expose
        private List<AttributeDetail> attributeDetails = null;

        public Integer getTotalRecord() {
            return totalRecord;
        }

        public void setTotalRecord(Integer totalRecord) {
            this.totalRecord = totalRecord;
        }

        public List<AttributeDetail> getAttributeDetails() {
            return attributeDetails;
        }

        public void setAttributeDetails(List<AttributeDetail> attributeDetails) {
            this.attributeDetails = attributeDetails;
        }

    }

    public class AttributeDetail {

        @SerializedName("attribute_name")
        @Expose
        private String attributeName;
        @SerializedName("attribute_id")
        @Expose
        private String attributeId;
        @SerializedName("option")
        @Expose
        private List<AttributeOption> option = null;

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public String getAttributeId() {
            return attributeId;
        }

        public void setAttributeId(String attributeId) {
            this.attributeId = attributeId;
        }

        public List<AttributeOption> getOption() {
            return option;
        }

        public void setOption(List<AttributeOption> option) {
            this.option = option;
        }

    }

    public class AttributeOption {

        @SerializedName("value_index")
        @Expose
        private String valueIndex;
        @SerializedName("us_label")
        @Expose
        private String usLabel;
        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("default_label")
        @Expose
        private String defaultLabel;
        @SerializedName("qty")
        @Expose
        private Integer qty;
        @SerializedName("price")
        @Expose
        private float price;
        @SerializedName("special_price")
        @Expose
        private float specialPrice;
        @SerializedName("special_from_date")
        @Expose
        private String specialFromDate;
        @SerializedName("special_to_date")
        @Expose
        private String specialToDate;

        public String data;

        private Boolean isSelected = false;

        public String getValueIndex() {
            return valueIndex;
        }

        public void setValueIndex(String valueIndex) {
            this.valueIndex = valueIndex;
        }

        public String getUsLabel() {
            return usLabel;
        }

        public void setUsLabel(String usLabel) {
            this.usLabel = usLabel;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getDefaultLabel() {
            return defaultLabel;
        }

        public void setDefaultLabel(String defaultLabel) {
            this.defaultLabel = defaultLabel;
        }

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            this.qty = qty;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public float getSpecialPrice() {
            return specialPrice;
        }

        public void setSpecialPrice(float specialPrice) {
            this.specialPrice = specialPrice;
        }

        public String getSpecialFromDate() {
            return specialFromDate;
        }

        public void setSpecialFromDate(String specialFromDate) {
            this.specialFromDate = specialFromDate;
        }

        public String getSpecialToDate() {
            return specialToDate;
        }

        public void setSpecialToDate(String specialToDate) {
            this.specialToDate = specialToDate;
        }

        public Boolean getSelected() {
            return isSelected;
        }

        public void setSelected(Boolean selected) {
            isSelected = selected;
        }

    }

}
