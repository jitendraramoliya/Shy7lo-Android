package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jiten on 03-06-2017.
 */

public class CardConfigration {

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

        @SerializedName("merchant_identifier")
        @Expose
        public String merchantIdentifier;
        @SerializedName("access_code")
        @Expose
        public String accessCode;

    }


}
