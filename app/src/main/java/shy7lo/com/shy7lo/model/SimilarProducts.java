package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jiten on 09-04-2018.
 */

public class SimilarProducts {

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
        public Integer totalCount;
        @SerializedName("request_params")
        @Expose
        public RequestParams requestParams;
        @SerializedName("product_listing")
        @Expose
        public List<ProductListing> productListing = null;
    }

    public class ProductListing {

        @SerializedName("entity_id")
        @Expose
        public String entityId;
        @SerializedName("sku")
        @Expose
        public String sku;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("type_id")
        @Expose
        public String typeId;
        @SerializedName("brand")
        @Expose
        public String brand;
        @SerializedName("price")
        @Expose
        public Float price;
        @SerializedName("special_price")
        @Expose
        public Float specialPrice;
        @SerializedName("special_from_date")
        @Expose
        public String specialFromDate;
        @SerializedName("special_to_date")
        @Expose
        public String specialToDate;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("brand_id")
        @Expose
        public Integer brandId;

    }

    public class RequestParams {

        //        @SerializedName("category_id")
//        @Expose
//        public String categoryId = null;
        @SerializedName("limit")
        @Expose
        public String limit;
        @SerializedName("page")
        @Expose
        public String page;

    }

}
