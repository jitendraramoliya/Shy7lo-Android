package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JITEN-PC on 28-12-2016.
 */

public class SearchProductList {

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

        @SerializedName("items")
        @Expose
        private List<Item> items = null;
        @SerializedName("search_criteria")
        @Expose
        private SearchCriteria searchCriteria;
        @SerializedName("total_count")
        @Expose
        private Integer totalCount;

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public SearchCriteria getSearchCriteria() {
            return searchCriteria;
        }

        public void setSearchCriteria(SearchCriteria searchCriteria) {
            this.searchCriteria = searchCriteria;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

    }

    public class SearchCriteria {

        @SerializedName("filter_groups")
        @Expose
        private List<FilterGroup> filterGroups = null;
        @SerializedName("sort_orders")
        @Expose
        private List<SortOrder> sortOrders = null;
        @SerializedName("page_size")
        @Expose
        private Integer pageSize;
        @SerializedName("current_page")
        @Expose
        private Integer currentPage;

        public List<FilterGroup> getFilterGroups() {
            return filterGroups;
        }

        public void setFilterGroups(List<FilterGroup> filterGroups) {
            this.filterGroups = filterGroups;
        }

        public List<SortOrder> getSortOrders() {
            return sortOrders;
        }

        public void setSortOrders(List<SortOrder> sortOrders) {
            this.sortOrders = sortOrders;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }


        public class SortOrder {

            @SerializedName("field")
            @Expose
            private String field;
            @SerializedName("direction")
            @Expose
            private String direction;

            public String getField() {
                return field;
            }

            public void setField(String field) {
                this.field = field;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }
        }


        public class FilterGroup {

            @SerializedName("filters")
            @Expose
            private List<Filter> filters = null;

            public List<Filter> getFilters() {
                return filters;
            }

            public void setFilters(List<Filter> filters) {
                this.filters = filters;
            }

            public class Filter {

                @SerializedName("field")
                @Expose
                private String field;
                @SerializedName("value")
                @Expose
                private String value;
                @SerializedName("condition_type")
                @Expose
                private String conditionType;

                public String getField() {
                    return field;
                }

                public void setField(String field) {
                    this.field = field;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getConditionType() {
                    return conditionType;
                }

                public void setConditionType(String conditionType) {
                    this.conditionType = conditionType;
                }

            }

        }

    }

    public class Item {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("sku")
        @Expose
        private String sku;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("attribute_set_id")
        @Expose
        private Integer attributeSetId;
        @SerializedName("price")
        @Expose
        private Float price;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("visibility")
        @Expose
        private Integer visibility;
        @SerializedName("type_id")
        @Expose
        private String typeId;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("extension_attributes")
        @Expose
        private List<Object> extensionAttributes = null;
        @SerializedName("product_links")
        @Expose
        private List<Object> productLinks = null;
        @SerializedName("tier_prices")
        @Expose
        private List<Object> tierPrices = null;
        @SerializedName("custom_attributes")
        @Expose
        private List<CustomAttribute> customAttributes = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAttributeSetId() {
            return attributeSetId;
        }

        public void setAttributeSetId(Integer attributeSetId) {
            this.attributeSetId = attributeSetId;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getVisibility() {
            return visibility;
        }

        public void setVisibility(Integer visibility) {
            this.visibility = visibility;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
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

        public List<Object> getExtensionAttributes() {
            return extensionAttributes;
        }

        public void setExtensionAttributes(List<Object> extensionAttributes) {
            this.extensionAttributes = extensionAttributes;
        }

        public List<Object> getProductLinks() {
            return productLinks;
        }

        public void setProductLinks(List<Object> productLinks) {
            this.productLinks = productLinks;
        }

        public List<Object> getTierPrices() {
            return tierPrices;
        }

        public void setTierPrices(List<Object> tierPrices) {
            this.tierPrices = tierPrices;
        }

        public List<CustomAttribute> getCustomAttributes() {
            return customAttributes;
        }

        public void setCustomAttributes(List<CustomAttribute> customAttributes) {
            this.customAttributes = customAttributes;
        }

        public class CustomAttribute {

            @SerializedName("attribute_code")
            @Expose
            private String attributeCode;
            @SerializedName("value")
            @Expose
            private String value;

            public String getAttributeCode() {
                return attributeCode;
            }

            public void setAttributeCode(String attributeCode) {
                this.attributeCode = attributeCode;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

        }

    }


}
