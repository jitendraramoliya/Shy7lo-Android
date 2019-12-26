package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jiten on 22-11-2017.
 */

public class SearchAlgoriaProductList {

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

        @SerializedName("hits")
        @Expose
        public List<Hit> hits = null;
        @SerializedName("nbHits")
        @Expose
        public Integer nbHits;
//        @SerializedName("page")
//        @Expose
//        public Integer page;
//        @SerializedName("nbPages")
//        @Expose
//        public Integer nbPages;
//        @SerializedName("hitsPerPage")
//        @Expose
//        public Integer hitsPerPage;
//        @SerializedName("processingTimeMS")
//        @Expose
//        public Integer processingTimeMS;
//        @SerializedName("exhaustiveNbHits")
//        @Expose
//        public Boolean exhaustiveNbHits;
//        @SerializedName("query")
//        @Expose
//        public String query;
        @SerializedName("params")
        @Expose
        public String params;

    }

    public class Hit {

        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("url")
        @Expose
        public String url;
//        @SerializedName("visibility_search")
//        @Expose
//        public Integer visibilitySearch;
//        @SerializedName("visibility_catalog")
//        @Expose
//        public Integer visibilityCatalog;
//        @SerializedName("categories")
//        @Expose
//        public Categories categories;
//        @SerializedName("categories_without_path")
//        @Expose
//        public List<String> categoriesWithoutPath = null;
//        @SerializedName("thumbnail_url")
//        @Expose
//        public String thumbnailUrl;
        @SerializedName("image_url")
        @Expose
        public String imageUrl;
//        @SerializedName("in_stock")
//        @Expose
//        public Integer inStock;
//        @SerializedName("stock_qty")
//        @Expose
//        public Integer stockQty;
        @SerializedName("brand")
        @Expose
        public String brand;
        @SerializedName("color")
        @Expose
        public String color;
//        @SerializedName("sku")
//        @Expose
//        public String sku;
        @SerializedName("price")
        @Expose
        public Price price;
//        @SerializedName("created_at")
//        @Expose
//        public String createdAt;
        @SerializedName("visibility")
        @Expose
        public String visibility;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("type_id")
        @Expose
        public String typeId;
//        @SerializedName("algoliaLastUpdateAtCET")
//        @Expose
//        public String algoliaLastUpdateAtCET;
        @SerializedName("objectID")
        @Expose
        public String objectID;
//        @SerializedName("special_price")
//        @Expose
//        public List<String> specialPrice = null;
//        @SerializedName("adult_shoe_sizes")
//        @Expose
//        public List<String> adultShoeSizes = null;
//        @SerializedName("special_from_date")
//        @Expose
//        public List<String> specialFromDate = null;
//        @SerializedName("adult_clothing_sizes")
//        @Expose
//        public List<String> adultClothingSizes = null;

    }

    public class Price {

        @SerializedName("SAR")
        @Expose
        public SAR sAR;

    }

    public class SAR {

//        @SerializedName("default")
//        @Expose
//        public Float _default;
//        @SerializedName("default_formated")
//        @Expose
//        public String defaultFormated;
//        @SerializedName("special_from_date")
//        @Expose
//        public Boolean specialFromDate;
//        @SerializedName("special_to_date")
//        @Expose
//        public Boolean specialToDate;
        @SerializedName("origional_price")
        @Expose
        public float origionalPrice;
        @SerializedName("special_price")
        @Expose
        public float specialPrice;
        @SerializedName("percent_off")
        @Expose
        public float percentOff;

    }

//    public class Categories {
//
//        @SerializedName("level0")
//        @Expose
//        public List<String> level0 = null;
//        @SerializedName("level1")
//        @Expose
//        public List<String> level1 = null;
//        @SerializedName("level2")
//        @Expose
//        public List<String> level2 = null;
//
//    }

}
