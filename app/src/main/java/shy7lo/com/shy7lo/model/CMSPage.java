package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CMSPage {

    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<Data> data;

    public class Data {

        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("title_ar")
        @Expose
        public String titleAr;
        @SerializedName("url")
        @Expose
        public String url;
        @SerializedName("url_ar")
        @Expose
        public String urlAr;
        @SerializedName("child")
        @Expose
        public List<Child> child = null;

    }

    public class Child {

        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("title_ar")
        @Expose
        public String titleAr;
        @SerializedName("url")
        @Expose
        public String url;
        @SerializedName("url_ar")
        @Expose
        public String urlAr;

    }



}
