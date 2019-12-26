package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyAddressResponse {

    @SerializedName("success")
    @Expose
    public Integer success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<Address> data = null;

    public class Address {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("customer_id")
        @Expose
        public Integer customerId;
        @SerializedName("region")
        @Expose
        public Region region;
        @SerializedName("region_id")
        @Expose
        public Integer regionId;
        @SerializedName("country_id")
        @Expose
        public String countryId;
        @SerializedName("street")
        @Expose
        public List<String> street = null;
        @SerializedName("telephone")
        @Expose
        public String telephone;
        @SerializedName("postcode")
        @Expose
        public String postcode;
        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("firstname")
        @Expose
        public String firstname;
        @SerializedName("lastname")
        @Expose
        public String lastname;
        @SerializedName("default_shipping")
        @Expose
        public boolean defaultShipping = false;
        @SerializedName("default_billing")
        @Expose
        public boolean defaultBilling = false;
        public boolean isSelected = false;

    }

    public class Region {

        @SerializedName("region_code")
        @Expose
        public String regionCode;
        @SerializedName("region")
        @Expose
        public String region;
        @SerializedName("region_id")
        @Expose
        public Integer regionId;

    }
}
