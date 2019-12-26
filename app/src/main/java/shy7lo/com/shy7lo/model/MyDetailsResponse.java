package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyDetailsResponse {

    @SerializedName("success")
    @Expose
    public Integer success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Details details;

    public class Details {

        @SerializedName("firstname")
        @Expose
        public String firstname;
        @SerializedName("lastname")
        @Expose
        public String lastname;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("dob")
        @Expose
        public String dob;

    }

}
