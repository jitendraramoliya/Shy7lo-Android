package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SortingPojo {

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

        @SerializedName("sortingData")
        @Expose
        public List<SortingData> sortingData = null;

    }

    public class SortingData {

        @SerializedName("title_en")
        @Expose
        public String titleEn;
        @SerializedName("title_ar")
        @Expose
        public String titleAr;
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("direction")
        @Expose
        public String direction;
        public boolean isSelected = false;

    }
}
