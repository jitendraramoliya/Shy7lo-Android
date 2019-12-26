package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrderResponse {

    @SerializedName("success")
    @Expose
    public Integer success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Data data;

    public class Data {

        @SerializedName("totalCount")
        @Expose
        public int totalCount = 0;

        @SerializedName("allOrders")
        @Expose
        public List<AllOrder> currentOrders = null;
//        @SerializedName("oldOrders")
//        @Expose
//        public List<AllOrder> oldOrders = null;

    }

    public class AllOrder {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("order_id")
        @Expose
        public String orderId;
        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("order_total")
        @Expose
        public String orderTotal;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("items_count")
        @Expose
        public Integer itemsCount;
        @SerializedName("items")
        @Expose
        public List<Item> items = null;

    }

    public class Item {

        @SerializedName("product_id")
        @Expose
        public String productId;
        @SerializedName("image")
        @Expose
        public String image;

    }

}
