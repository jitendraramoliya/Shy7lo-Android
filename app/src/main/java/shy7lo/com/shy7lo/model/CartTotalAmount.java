package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jiten on 07-01-2018.
 */

public class CartTotalAmount {

    @SerializedName("data")
    @Expose
    public List<CartTotal> data = null;

    public class CartTotal {

        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("value")
        @Expose
        public String value;

    }

}
