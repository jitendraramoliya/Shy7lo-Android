package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrderDetailsResponse {

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

        @SerializedName("entity_id")
        @Expose
        public String entityId;
        @SerializedName("increment_id")
        @Expose
        public String incrementId;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("coupon_code")
        @Expose
        public Object couponCode;
        @SerializedName("shipping_description")
        @Expose
        public String shippingDescription;
        @SerializedName("store_id")
        @Expose
        public String storeId;
        @SerializedName("customer_id")
        @Expose
        public String customerId;
        @SerializedName("customer_email")
        @Expose
        public String customerEmail;
        @SerializedName("customer_firstname")
        @Expose
        public String customerFirstname;
        @SerializedName("customer_lastname")
        @Expose
        public String customerLastname;
        @SerializedName("msp_cod_amount")
        @Expose
        public float mspCodAmount;
        @SerializedName("discount_amount")
        @Expose
        public float discountAmount;
        @SerializedName("subtotal")
        @Expose
        public float subtotal;
        @SerializedName("grand_total")
        @Expose
        public float grandTotal;
        @SerializedName("shipping_amount")
        @Expose
        public float shippingAmount;
        @SerializedName("tax_amount")
        @Expose
        public float taxAmount;
        @SerializedName("total_qty_ordered")
        @Expose
        public int totalQtyOrdered;
        @SerializedName("subtotal_incl_tax")
        @Expose
        public float subtotalInclTax;
        @SerializedName("billing_address_id")
        @Expose
        public String billingAddressId;
        @SerializedName("shipping_address_id")
        @Expose
        public String shippingAddressId;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("tracking_number")
        @Expose
        public String tracking_number;
        @SerializedName("items")
        @Expose
        public List<Item> items = null;
        @SerializedName("shipping_address")
        @Expose
        public ShippingAddress shippingAddress;
        @SerializedName("billing_address")
        @Expose
        public BillingAddress billingAddress;
        @SerializedName("payment_info")
        @Expose
        public PaymentInfo paymentInfo;
        @SerializedName("tracking_info")
        @Expose
        public List<TrackingInfo> trackingInfo = null;
        @SerializedName("full_tracking_info")
        @Expose
        public List<FullTrackingInfo> fullTrackingInfo = null;

    }

    public class BillingAddress {

        @SerializedName("entity_id")
        @Expose
        public String entityId;
        @SerializedName("parent_id")
        @Expose
        public String parentId;
        @SerializedName("customer_id")
        @Expose
        public Object customerId;
        @SerializedName("customer_address_id")
        @Expose
        public Object customerAddressId;
        @SerializedName("region_id")
        @Expose
        public Object regionId;
        @SerializedName("region")
        @Expose
        public String region;
        @SerializedName("firstname")
        @Expose
        public String firstname;
        @SerializedName("lastname")
        @Expose
        public String lastname;
        @SerializedName("street")
        @Expose
        public String street;
        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("country_id")
        @Expose
        public String countryId;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("telephone")
        @Expose
        public String telephone;
        @SerializedName("address_type")
        @Expose
        public String addressType;

    }

    public class PaymentInfo {

        @SerializedName("method")
        @Expose
        public String method;
        @SerializedName("shipping_amount")
        @Expose
        public String shippingAmount;
        @SerializedName("amount_ordered")
        @Expose
        public String amountOrdered;

    }

    public class Item {

        @SerializedName("item_id")
        @Expose
        public String itemId;
        @SerializedName("order_id")
        @Expose
        public String orderId;
        @SerializedName("product_id")
        @Expose
        public String productId;
        @SerializedName("product_type")
        @Expose
        public String productType;
        @SerializedName("sku")
        @Expose
        public String sku;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("brand")
        @Expose
        public String brand;
        @SerializedName("applied_rule_ids")
        @Expose
        public Object appliedRuleIds;
        @SerializedName("qty_ordered")
        @Expose
        public int qtyOrdered;
        @SerializedName("qty_invoiced")
        @Expose
        public int qtyInvoiced;
        @SerializedName("qty_shipped")
        @Expose
        public int qtyShipped;
        @SerializedName("qty_refunded")
        @Expose
        public int qtyRefunded;
        @SerializedName("price")
        @Expose
        public float price;
        @SerializedName("price_incl_tax")
        @Expose
        public float priceInclTax;
        @SerializedName("tax_amount")
        @Expose
        public float taxAmount;
        @SerializedName("tax_percent")
        @Expose
        public String taxPercent;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("label")
        @Expose
        public String label;

    }

    public class ShippingAddress {

        @SerializedName("entity_id")
        @Expose
        public String entityId;
        @SerializedName("parent_id")
        @Expose
        public String parentId;
        @SerializedName("customer_id")
        @Expose
        public Object customerId;
        @SerializedName("customer_address_id")
        @Expose
        public Object customerAddressId;
        @SerializedName("region_id")
        @Expose
        public Object regionId;
        @SerializedName("region")
        @Expose
        public String region;
        @SerializedName("firstname")
        @Expose
        public String firstname;
        @SerializedName("lastname")
        @Expose
        public String lastname;
        @SerializedName("street")
        @Expose
        public String street;
        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("country_id")
        @Expose
        public String countryId;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("telephone")
        @Expose
        public String telephone;
        @SerializedName("address_type")
        @Expose
        public String addressType;

    }

    public class FullTrackingInfo {

        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("date")
        @Expose
        public String createdAt;

    }

    public class TrackingInfo {

        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("date")
        @Expose
        public String createdAt;

    }
}
