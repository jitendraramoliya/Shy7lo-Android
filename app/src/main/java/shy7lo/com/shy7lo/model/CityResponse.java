package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityResponse {

    @SerializedName("success")
    @Expose
    public Integer success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<City> data = null;


    public class City {

//        @SerializedName("id")
//        @Expose
//        public String id;
//        @SerializedName("id_region")
//        @Expose
//        public String idRegion;
        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("city_en")
        @Expose
        public String cityEn;
//        @SerializedName("city_other")
//        @Expose
//        public Object cityOther;
//        @SerializedName("status")
//        @Expose
//        public String status;
//        @SerializedName("created_at")
//        @Expose
//        public String createdAt;
//        @SerializedName("update_at")
//        @Expose
//        public String updateAt;

    }

}
