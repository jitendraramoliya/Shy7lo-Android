package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jiten on 06-08-2017.
 */

public class ProductCategoryList {

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

        @SerializedName("category")
        @Expose
        public Category category;

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

        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("count")
        @Expose
        public Integer count;

    }

}
