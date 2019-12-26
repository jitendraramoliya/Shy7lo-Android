package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JITEN-PC on 29-03-2017.
 */

public class PaymentMethodRespnose {

    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("payment_methods")
        @Expose
        private List<PaymentMethod> paymentMethods = null;

        public List<PaymentMethod> getPaymentMethods() {
            return paymentMethods;
        }

        public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
            this.paymentMethods = paymentMethods;
        }

    }

    public class PaymentMethod {

        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("info")
        @Expose
        public Info info;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public class Info {

        @SerializedName("msg")
        @Expose
        public String msg;
        @SerializedName("color")
        @Expose
        public String color;
        @SerializedName("type")
        @Expose
        public String type;

    }

}
