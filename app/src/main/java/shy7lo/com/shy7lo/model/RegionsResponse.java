package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegionsResponse {

    @SerializedName("success")
    @Expose
    public Integer success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<Region> data = null;

    public class Region {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("region")
        @Expose
        public String region;
        @SerializedName("region_en")
        @Expose
        public String regionEn;

    }


}
