package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jiten on 31-03-2018.
 */

public class CategoryProductList {

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
        @SerializedName("request_params")
        @Expose
        public RequestParams requestParams;
        @SerializedName("category")
        @Expose
        public Category category;

    }


    public class Category {

        @SerializedName("child_count")
        @Expose
        public Integer childCount;
        @SerializedName("child_data")
        @Expose
        public List<CategoryChild> childData = null;
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("banner")
        @Expose
        public List<Banner> banner = null;

    }

    public class Banner {

        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("url")
        @Expose
        public String url;

    }


    public class CategoryChild {

        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("count")
        @Expose
        public Integer count;
        @SerializedName("in_stock_items")
        @Expose
        public Integer inStockItems;
        @SerializedName("thumb")
        @Expose
        public String thumb;
        @SerializedName("product_listing_flag")
        @Expose
        public boolean productListingFlag;

    }

    public class RequestParams {

        @SerializedName("category_id")
        @Expose
        public String categoryId;

    }

}
