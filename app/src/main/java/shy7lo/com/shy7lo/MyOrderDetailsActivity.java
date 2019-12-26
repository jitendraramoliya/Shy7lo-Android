package shy7lo.com.shy7lo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.adapter.MyOrderDetailsAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.MyOrderDetailsResponse;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.Constant;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

public class MyOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ibCancel)
    ImageButton ibCancel;
    @BindView(R.id.rltTitle)
    RelativeLayout rltTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.lvOrders)
    ListView lvOrders;

    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvStatusTitle)
    TextView tvStatusTitle;
    @BindView(R.id.tvTracking)
    TextView tvTracking;
    @BindView(R.id.tvTrackingTitle)
    TextView tvTrackingTitle;
    @BindView(R.id.tvEstimatedDelivery)
    TextView tvEstimatedDelivery;
    @BindView(R.id.tvEstimatedDeliveryTitle)
    TextView tvEstimatedDeliveryTitle;
    //    @BindView(R.id.tvOvalSent)
//    TextView tvOvalSent;
//    @BindView(R.id.tvRectangle1)
//    TextView tvRectangle1;
//    @BindView(R.id.tvOvalInTransit)
//    TextView tvOvalInTransit;
//    @BindView(R.id.tvRectangle2)
//    TextView tvRectangle2;
    @BindView(R.id.tvOvalOrder)
    TextView tvOvalOrder;
    @BindView(R.id.tvOvalProgress)
    TextView tvOvalProgress;
    @BindView(R.id.tvOvalShip)
    TextView tvOvalShip;
    @BindView(R.id.tvOvalDelivered)
    TextView tvOvalDelivered;
    @BindView(R.id.tvOrder)
    TextView tvOrder;
    @BindView(R.id.tvOrderDate)
    TextView tvOrderDate;
    @BindView(R.id.tvOrderMonth)
    TextView tvOrderMonth;
    @BindView(R.id.tvProcessing)
    TextView tvProcessing;
    @BindView(R.id.tvProcessingDate)
    TextView tvProcessingDate;
    @BindView(R.id.tvProcessingMonth)
    TextView tvProcessingMonth;
    @BindView(R.id.tvShipped)
    TextView tvShipped;
    @BindView(R.id.tvShippedDate)
    TextView tvShippedDate;
    @BindView(R.id.tvShippedMonth)
    TextView tvShippedMonth;
    @BindView(R.id.tvDelivered)
    TextView tvDelivered;
    @BindView(R.id.tvDeliveredDate)
    TextView tvDeliveredDate;
    @BindView(R.id.tvDeliveredMonth)
    TextView tvDeliveredMonth;
    @BindView(R.id.tvLayerWhite)
    TextView tvLayerWhite;
    @BindView(R.id.tvOrderCancelled)
    TextView tvOrderCancelled;
    //    @BindView(R.id.tvSent)
//    TextView tvSent;
//    @BindView(R.id.tvInTransit)
//    TextView tvInTransit;
//    @BindView(R.id.tvDelivered)
//    TextView tvDelivered;
    @BindView(R.id.lnrTracking)
    LinearLayout lnrTracking;
    @BindView(R.id.lnrContainer)
    LinearLayout lnrContainer;
    @BindView(R.id.lnrStatus)
    LinearLayout lnrStatus;
    @BindView(R.id.rltStatus)
    RelativeLayout rltStatus;

    TextView tvSubTotal, tvDiscount, tvDelivery, tvTotal, tvTaxTitle, tvTax, tvTrackOrder, tvCancelOrder,
            tvSubTotalTitle, tvDiscountTitle, tvDeliveryTitle, tvTotalTitle,
            tvShippingInfo, tvNameTitle, tvName, tvAddressTitle, tvAddress, tvCountryTitle, tvCountry, tvPhoneTitle, tvPhone,
            tvBillingInfo, tvBillingNameTitle, tvBillingName, tvBillingAddressTitle, tvBillingAddress, tvBillingCountryTitle, tvBillingCountry, tvBillingPhoneTitle, tvBillingPhone;
    RelativeLayout rltDiscount, rltDelivery, rltTax;

    private String mOrderID = "";
    private boolean isCurrentOrder;
    List<MyOrderDetailsResponse.Item> mOrderDetailsList;
    MyOrderDetailsResponse mMyOrderDetailsResponse;

    MyOrderDetailsAdapter mMyOrderDetailsAdapter;

    View mFooterView;
    private String mCurrencyCode = "";
    private float mExchangeRate;

    private enum StatusType {Order, Processing, Shipped, Delivered}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_my_order_details);
        ButterKnife.bind(this);

        InitializeControls();
        InitializeControlsAction();
    }

    private void InitializeControls() {

        mMyOrderDetailsAdapter = new MyOrderDetailsAdapter(getActivity());
        lvOrders.setAdapter(mMyOrderDetailsAdapter);

        mCurrencyCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR));
        mExchangeRate = MyPref.getPref(getActivity(), MyPref.DEFAULT_EXCHANGE_RATE, 1f);

        mFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_order_details, null);
        tvSubTotal = mFooterView.findViewById(R.id.tvSubTotal);
        tvSubTotalTitle = mFooterView.findViewById(R.id.tvSubTotalTitle);
        tvDiscount = mFooterView.findViewById(R.id.tvDiscount);
        tvDiscountTitle = mFooterView.findViewById(R.id.tvDiscountTitle);
        tvTaxTitle = mFooterView.findViewById(R.id.tvTaxTitle);
        tvTax = mFooterView.findViewById(R.id.tvTax);
        tvDelivery = mFooterView.findViewById(R.id.tvDelivery);
        tvDeliveryTitle = mFooterView.findViewById(R.id.tvDeliveryTitle);
        tvTotal = mFooterView.findViewById(R.id.tvTotal);
        tvTotalTitle = mFooterView.findViewById(R.id.tvTotalTitle);
        tvTrackOrder = mFooterView.findViewById(R.id.tvTrackOrder);
        tvCancelOrder = mFooterView.findViewById(R.id.tvCancelOrder);
        tvShippingInfo = findViewById(R.id.tvShippingInfo);
        tvNameTitle = findViewById(R.id.tvNameTitle);
        tvName = findViewById(R.id.tvName);
        tvAddressTitle = findViewById(R.id.tvAddressTitle);
        tvAddress = findViewById(R.id.tvAddress);
        tvCountryTitle = findViewById(R.id.tvCountryTitle);
        tvCountry = findViewById(R.id.tvCountry);
        tvPhoneTitle = findViewById(R.id.tvPhoneTitle);
        tvPhone = findViewById(R.id.tvPhone);
        tvBillingInfo = findViewById(R.id.tvBillingInfo);
        tvBillingNameTitle = findViewById(R.id.tvBillingNameTitle);
        tvBillingName = findViewById(R.id.tvBillingName);
        tvBillingAddressTitle = findViewById(R.id.tvBillingAddressTitle);
        tvBillingAddress = findViewById(R.id.tvBillingAddress);
        tvBillingCountryTitle = findViewById(R.id.tvBillingCountryTitle);
        tvBillingCountry = findViewById(R.id.tvBillingCountry);
        tvBillingPhoneTitle = findViewById(R.id.tvBillingPhoneTitle);
        tvBillingPhone = findViewById(R.id.tvBillingPhone);

        rltDiscount = mFooterView.findViewById(R.id.rltDiscount);
        rltDelivery = mFooterView.findViewById(R.id.rltDelivery);
        rltTax = mFooterView.findViewById(R.id.rltTax);

        tvTrackOrder.setPaintFlags(tvTrackOrder.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvCancelOrder.setPaintFlags(tvCancelOrder.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvTrackingTitle.setPaintFlags(tvCancelOrder.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        lvOrders.addFooterView(mFooterView);
        mFooterView.setVisibility(View.GONE);

        lnrContainer.setVisibility(View.GONE);

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

            rltTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            lvOrders.setScaleX(-1f);
//            mFooterView.setScaleX(-1f);
            tvSubTotal.setScaleX(-1f);
            tvSubTotalTitle.setScaleX(-1f);
            tvDiscount.setScaleX(-1f);
            tvDiscountTitle.setScaleX(-1f);
            tvDelivery.setScaleX(-1f);
            tvDeliveryTitle.setScaleX(-1f);
            tvTax.setScaleX(-1f);
            tvTaxTitle.setScaleX(-1f);
            tvTotal.setScaleX(-1f);
            tvTotalTitle.setScaleX(-1f);
            tvTrackOrder.setScaleX(-1f);
            tvCancelOrder.setScaleX(-1f);

            tvShippingInfo.setScaleX(-1f);
            tvNameTitle.setScaleX(-1f);
            tvName.setScaleX(-1f);
            tvAddressTitle.setScaleX(-1f);
            tvAddress.setScaleX(-1f);
            tvCountryTitle.setScaleX(-1f);
            tvCountry.setScaleX(-1f);
            tvPhoneTitle.setScaleX(-1f);
            tvPhone.setScaleX(-1f);

            tvBillingInfo.setScaleX(-1f);
            tvBillingNameTitle.setScaleX(-1f);
            tvBillingName.setScaleX(-1f);
            tvBillingAddressTitle.setScaleX(-1f);
            tvBillingAddress.setScaleX(-1f);
            tvBillingCountryTitle.setScaleX(-1f);
            tvBillingCountry.setScaleX(-1f);
            tvBillingPhoneTitle.setScaleX(-1f);
            tvBillingPhone.setScaleX(-1f);

            lnrContainer.setScaleX(-1f);
            tvStatus.setScaleX(-1f);
            tvStatusTitle.setScaleX(-1f);
            tvTracking.setScaleX(-1f);
            tvTrackingTitle.setScaleX(-1f);
            tvEstimatedDelivery.setScaleX(-1f);
            tvEstimatedDeliveryTitle.setScaleX(-1f);

            tvOrder.setScaleX(-1f);
            tvOrderDate.setScaleX(-1f);
            tvOrderMonth.setScaleX(-1f);
            tvProcessing.setScaleX(-1f);
            tvProcessingDate.setScaleX(-1f);
            tvProcessingMonth.setScaleX(-1f);
            tvShipped.setScaleX(-1f);
            tvShippedDate.setScaleX(-1f);
            tvShippedMonth.setScaleX(-1f);
            tvDelivered.setScaleX(-1f);
            tvDeliveredDate.setScaleX(-1f);
            tvDeliveredMonth.setScaleX(-1f);

        } else {

            rltTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);
            lvOrders.setScaleX(1f);
//            mFooterView.setScaleX(1f);
            tvSubTotal.setScaleX(1f);
            tvSubTotalTitle.setScaleX(1f);
            tvDiscount.setScaleX(1f);
            tvDiscountTitle.setScaleX(1f);
            tvDelivery.setScaleX(1f);
            tvDeliveryTitle.setScaleX(1f);
            tvTax.setScaleX(1f);
            tvTaxTitle.setScaleX(1f);
            tvTotal.setScaleX(1f);
            tvTotalTitle.setScaleX(1f);
            tvTrackOrder.setScaleX(1f);
            tvCancelOrder.setScaleX(1f);

            tvShippingInfo.setScaleX(1f);
            tvNameTitle.setScaleX(1f);
            tvName.setScaleX(1f);
            tvAddressTitle.setScaleX(1f);
            tvAddress.setScaleX(1f);
            tvCountryTitle.setScaleX(1f);
            tvCountry.setScaleX(1f);
            tvPhoneTitle.setScaleX(1f);
            tvPhone.setScaleX(1f);

            tvBillingInfo.setScaleX(1f);
            tvBillingNameTitle.setScaleX(1f);
            tvBillingName.setScaleX(1f);
            tvBillingAddressTitle.setScaleX(1f);
            tvBillingAddress.setScaleX(1f);
            tvBillingCountryTitle.setScaleX(1f);
            tvBillingCountry.setScaleX(1f);
            tvBillingPhoneTitle.setScaleX(1f);
            tvBillingPhone.setScaleX(1f);


            lnrContainer.setScaleX(1f);
            tvStatus.setScaleX(1f);
            tvStatusTitle.setScaleX(1f);
            tvTracking.setScaleX(1f);
            tvTrackingTitle.setScaleX(1f);
            tvEstimatedDelivery.setScaleX(1f);
            tvEstimatedDeliveryTitle.setScaleX(1f);
            tvOrder.setScaleX(1f);
            tvOrderDate.setScaleX(1f);
            tvOrderMonth.setScaleX(1f);
            tvProcessing.setScaleX(1f);
            tvProcessingDate.setScaleX(1f);
            tvProcessingMonth.setScaleX(1f);
            tvShipped.setScaleX(1f);
            tvShippedDate.setScaleX(1f);
            tvShippedMonth.setScaleX(1f);
            tvDelivered.setScaleX(1f);
            tvDeliveredDate.setScaleX(1f);
            tvDeliveredMonth.setScaleX(1f);


        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mOrderID = bundle.getString(Constant.BNDL_ORDER_ID, "");
            isCurrentOrder = bundle.getBoolean(Constant.BNDL_IS_CURRENT_ORDER, false);
            if (!TextUtils.isEmpty(mOrderID)) {
                getOrderDetails(mOrderID);
            }
        }

    }

    private void InitializeControlsAction() {
        tvTrackOrder.setOnClickListener(this);
        ibCancel.setOnClickListener(this);
    }

    private void getOrderDetails(final String mOrderID) {
        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getOrderDetails(mOrderID);
                    }
                }
            });
            return;
        }

        Utils.showProgressDialog(getActivity());
        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("order_id", "" + mOrderID);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        Call<MyOrderDetailsResponse> call = serviceAPI.getOrderDetailsList(Shy7lo.mLangCode, "Bearer " + userToken, body);
        call.enqueue(new Callback<MyOrderDetailsResponse>() {
            @Override
            public void onResponse(Call<MyOrderDetailsResponse> call, Response<MyOrderDetailsResponse> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    mMyOrderDetailsResponse = response.body();
                    if (mMyOrderDetailsResponse != null && mMyOrderDetailsResponse.success == 1) {

                        if (mMyOrderDetailsResponse.data != null) {

                            tvLayerWhite.setVisibility(View.GONE);

                            lnrContainer.setVisibility(View.VISIBLE);
                            mOrderDetailsList = mMyOrderDetailsResponse.data.items;
                            mMyOrderDetailsAdapter.addAll(mOrderDetailsList);

                            mFooterView.setVisibility(View.VISIBLE);

                            tvSubTotal.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, mMyOrderDetailsResponse.data.subtotal));

                            tvStatus.setText("#" + mMyOrderDetailsResponse.data.incrementId);

                            if (mMyOrderDetailsResponse.data.mspCodAmount > 0) {
                                rltDelivery.setVisibility(View.VISIBLE);
                                tvDelivery.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, mMyOrderDetailsResponse.data.mspCodAmount));
                            } else {
                                rltDelivery.setVisibility(View.GONE);
                            }

                            if (!TextUtils.isEmpty("" + mMyOrderDetailsResponse.data.discountAmount) && !String.valueOf(mMyOrderDetailsResponse.data.discountAmount).equals("0.0000")) {
                                rltDiscount.setVisibility(View.VISIBLE);
                                tvDiscount.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, mMyOrderDetailsResponse.data.discountAmount));
                            } else {
                                rltDiscount.setVisibility(View.GONE);
                            }
                            if (mMyOrderDetailsResponse.data.taxAmount > 0) {
                                rltTax.setVisibility(View.VISIBLE);
                                tvTax.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, mMyOrderDetailsResponse.data.taxAmount));
                            } else {
                                rltTax.setVisibility(View.GONE);
                            }

                            tvTotal.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, mMyOrderDetailsResponse.data.grandTotal));

                            if (mMyOrderDetailsResponse.data.trackingInfo != null && mMyOrderDetailsResponse.data.trackingInfo.size() > 0) {
                                tvTrackOrder.setVisibility(View.VISIBLE);
                            } else {
                                tvTrackOrder.setVisibility(View.GONE);
                            }

                            MyOrderDetailsResponse.ShippingAddress mShippingAddress = mMyOrderDetailsResponse.data.shippingAddress;

                            if (mShippingAddress != null) {
                                tvName.setText("" + mShippingAddress.firstname + " " + mShippingAddress.lastname);
                                tvAddress.setText("" + mShippingAddress.street + " " + mShippingAddress.city);
                                tvCountry.setText("" + mShippingAddress.countryId);
                                tvPhone.setText("" + mShippingAddress.telephone);
                                if (mShippingAddress.countryId.equalsIgnoreCase("OM")) {
                                    tvCountry.setText("Oman");
                                } else if (mShippingAddress.countryId.equalsIgnoreCase("SA")) {
                                    tvCountry.setText("Saudi Arabia");
                                } else if (mShippingAddress.countryId.equalsIgnoreCase("AE")) {
                                    tvCountry.setText("UAE");
                                } else if (mShippingAddress.countryId.equalsIgnoreCase("QA")) {
                                    tvCountry.setText("Qatar");
                                } else if (mShippingAddress.countryId.equalsIgnoreCase("KW")) {
                                    tvCountry.setText("Kuwait");
                                } else if (mShippingAddress.countryId.equalsIgnoreCase("BH")) {
                                    tvCountry.setText("Bahrain");
                                }
                            }

                            MyOrderDetailsResponse.BillingAddress mBillingAddress = mMyOrderDetailsResponse.data.billingAddress;

                            if (mBillingAddress != null) {
                                tvBillingName.setText("" + mBillingAddress.firstname + " " + mBillingAddress.lastname);
                                tvBillingAddress.setText("" + mBillingAddress.street + " " + mBillingAddress.city);
                                tvBillingCountry.setText("" + mBillingAddress.countryId);
                                tvBillingPhone.setText("" + mBillingAddress.telephone);
                                if (mBillingAddress.countryId.equalsIgnoreCase("OM")) {
                                    tvBillingCountry.setText("Oman");
                                } else if (mBillingAddress.countryId.equalsIgnoreCase("SA")) {
                                    tvBillingCountry.setText("Saudi Arabia");
                                } else if (mBillingAddress.countryId.equalsIgnoreCase("AE")) {
                                    tvBillingCountry.setText("UAE");
                                } else if (mBillingAddress.countryId.equalsIgnoreCase("QA")) {
                                    tvBillingCountry.setText("Qatar");
                                } else if (mBillingAddress.countryId.equalsIgnoreCase("KW")) {
                                    tvBillingCountry.setText("Kuwait");
                                } else if (mBillingAddress.countryId.equalsIgnoreCase("BH")) {
                                    tvBillingCountry.setText("Bahrain");
                                }
                            }


                            LogUtils.e("", "mMyOrderDetailsResponse.data.status::"+mMyOrderDetailsResponse.data.status);
                            if (!TextUtils.isEmpty(mMyOrderDetailsResponse.data.status) && mMyOrderDetailsResponse.data.status.equalsIgnoreCase("canceled")) {
                                tvOrderCancelled.setVisibility(View.VISIBLE);
                            } else {
                                tvOrderCancelled.setVisibility(View.GONE);
                            }

                            if (!TextUtils.isEmpty(mMyOrderDetailsResponse.data.tracking_number)) {
                                tvTracking.setText("#" + mMyOrderDetailsResponse.data.tracking_number);
                                lnrTracking.setVisibility(View.VISIBLE);
                            } else {
                                lnrTracking.setVisibility(View.GONE);
                            }


                            if (mMyOrderDetailsResponse.data.trackingInfo != null && mMyOrderDetailsResponse.data.trackingInfo.size() > 0) {
                                lnrStatus.setVisibility(View.VISIBLE);
                                rltStatus.setVisibility(View.VISIBLE);

                                MyOrderDetailsResponse.TrackingInfo mTrackingInfo = mMyOrderDetailsResponse.data.trackingInfo.get(mMyOrderDetailsResponse.data.trackingInfo.size() - 1);

                                if (mTrackingInfo != null) {

                                    if (!TextUtils.isEmpty(mTrackingInfo.code)) {
                                        if (mTrackingInfo.code.equalsIgnoreCase("delivered")) {
                                            showStatusType(StatusType.Delivered, mMyOrderDetailsResponse.data.trackingInfo);
                                        } else if (mTrackingInfo.code.equalsIgnoreCase("ordered")) {
                                            showStatusType(StatusType.Order, mMyOrderDetailsResponse.data.trackingInfo);
                                        } else if (mTrackingInfo.code.equalsIgnoreCase("processed") || mTrackingInfo.code.equalsIgnoreCase("processing")) {
                                            showStatusType(StatusType.Processing, mMyOrderDetailsResponse.data.trackingInfo);
                                        } else if (mTrackingInfo.code.equalsIgnoreCase("shipped")) {
                                            showStatusType(StatusType.Shipped, mMyOrderDetailsResponse.data.trackingInfo);
                                        }
                                    }

                                }

                            } else {
                                lnrStatus.setVisibility(View.GONE);
                                rltStatus.setVisibility(View.GONE);
                            }

                        }

                    } else if (mMyOrderDetailsResponse != null && mMyOrderDetailsResponse.success == 0) {
                        Utils.showToast(getActivity(), "" + mMyOrderDetailsResponse.message);
                    } else {
                        Utils.showToast(getActivity(), "" + getString(R.string.msg_something_wrong));
                    }
                } else {
                    if (String.valueOf(response.code()).startsWith("5")) {
                        Utils.showAlertDialog(getActivity(), "" + response.code());
                    } else {

                        try {
                            JSONObject jResponse = new JSONObject(response.errorBody().string());
                            if (jResponse != null && jResponse.getString("success").equals("0")) {
                                Utils.showToast(getActivity(), "" + jResponse.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyOrderDetailsResponse> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });
    }

    private Context getActivity() {
        return MyOrderDetailsActivity.this;
    }

    private void showStatusType(StatusType mStatusType, List<MyOrderDetailsResponse.TrackingInfo> trackingInfo) {


        tvOvalOrder.setBackgroundResource(R.drawable.oval_grayf2);
        tvOvalProgress.setBackgroundResource(R.drawable.oval_grayf2);
        tvOvalShip.setBackgroundResource(R.drawable.oval_grayf2);
        tvOvalDelivered.setBackgroundResource(R.drawable.oval_grayf2);

        switch (mStatusType) {
            case Order:
                tvOrder.setTypeface(Shy7lo.RalewayBold);

                tvOvalOrder.setBackgroundResource(R.drawable.oval_green70);
                tvOrder.setTextColor(getResources().getColor(R.color.green_shipping));
                tvOrderDate.setText(trackingInfo.get(0).createdAt);
                tvOrderDate.setTextColor(getResources().getColor(R.color.gray_5a));
                tvOrderDate.setVisibility(View.VISIBLE);

                break;
            case Processing:
                tvProcessing.setTypeface(Shy7lo.RalewayBold);

                tvOvalProgress.setBackgroundResource(R.drawable.oval_green70);
                tvProcessing.setTextColor(getResources().getColor(R.color.green_shipping));
                tvProcessingDate.setText(trackingInfo.get(1).createdAt);
                tvProcessingDate.setTextColor(getResources().getColor(R.color.gray_5a));
                tvProcessingDate.setVisibility(View.VISIBLE);

                tvOvalOrder.setBackgroundResource(R.drawable.oval_grayb2);
                tvOrder.setTextColor(getResources().getColor(R.color.gray_b2));
                tvOrderDate.setText(trackingInfo.get(0).createdAt);
                tvOrderDate.setTextColor(getResources().getColor(R.color.gray_b2));
                tvOrderDate.setVisibility(View.VISIBLE);

                break;
            case Shipped:
                tvShipped.setTypeface(Shy7lo.RalewayBold);

                tvOvalShip.setBackgroundResource(R.drawable.oval_green70);
                tvShipped.setTextColor(getResources().getColor(R.color.green_shipping));
                tvShippedDate.setText(trackingInfo.get(2).createdAt);
                tvShippedDate.setTextColor(getResources().getColor(R.color.gray_5a));
                tvShippedDate.setVisibility(View.VISIBLE);

                tvOvalProgress.setBackgroundResource(R.drawable.oval_grayb2);
                tvProcessing.setTextColor(getResources().getColor(R.color.gray_b2));
                tvProcessingDate.setText(trackingInfo.get(1).createdAt);
                tvProcessingDate.setTextColor(getResources().getColor(R.color.gray_b2));
                tvProcessingDate.setVisibility(View.VISIBLE);

                tvOvalOrder.setBackgroundResource(R.drawable.oval_grayb2);
                tvOrder.setTextColor(getResources().getColor(R.color.gray_b2));
                tvOrderDate.setText(trackingInfo.get(0).createdAt);
                tvOrderDate.setTextColor(getResources().getColor(R.color.gray_b2));
                tvOrderDate.setVisibility(View.VISIBLE);

                break;

            case Delivered:
                tvDelivered.setTypeface(Shy7lo.RalewayBold);

                tvOvalDelivered.setBackgroundResource(R.drawable.oval_green70);
                tvDelivered.setTextColor(getResources().getColor(R.color.green_shipping));
                tvDeliveredDate.setText(trackingInfo.get(3).createdAt);
                tvDeliveredDate.setTextColor(getResources().getColor(R.color.gray_5a));
                tvDeliveredDate.setVisibility(View.VISIBLE);

                tvOvalShip.setBackgroundResource(R.drawable.oval_grayb2);
                tvShipped.setTextColor(getResources().getColor(R.color.gray_b2));
                tvShippedDate.setText(trackingInfo.get(2).createdAt);
                tvShippedDate.setTextColor(getResources().getColor(R.color.gray_b2));
                tvShippedDate.setVisibility(View.VISIBLE);

                tvOvalProgress.setBackgroundResource(R.drawable.oval_grayb2);
                tvProcessing.setTextColor(getResources().getColor(R.color.gray_b2));
                tvProcessingDate.setText(trackingInfo.get(1).createdAt);
                tvProcessingDate.setTextColor(getResources().getColor(R.color.gray_b2));
                tvProcessingDate.setVisibility(View.VISIBLE);

                tvOvalOrder.setBackgroundResource(R.drawable.oval_grayb2);
                tvOrder.setTextColor(getResources().getColor(R.color.gray_b2));
                tvOrderDate.setText(trackingInfo.get(0).createdAt);
                tvOrderDate.setTextColor(getResources().getColor(R.color.gray_b2));
                tvOrderDate.setVisibility(View.VISIBLE);

                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == ibCancel) {
            finish();
        } else if (view == tvTrackOrder) {
            if (mMyOrderDetailsResponse != null && mMyOrderDetailsResponse.data != null) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.BNDL_ORDER_TRACKING, new Gson().toJson(mMyOrderDetailsResponse.data));
                IntentHandler.startActivity(getActivity(), MyOrderTrackingActivity.class, bundle);
            }
        }
    }
}
