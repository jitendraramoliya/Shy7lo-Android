package shy7lo.com.shy7lo.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.plugin.AdjustCriteo;
import com.adjust.sdk.plugin.CriteoProduct;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.PurchaseEvent;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
import com.payfort.fort.android.sdk.base.callbacks.FortCallback;
import com.tune.TuneEventItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.AddAddressActivity;
import shy7lo.com.shy7lo.AddressMapActivity;
import shy7lo.com.shy7lo.BuildConfig;
import shy7lo.com.shy7lo.ChangeAddressActivity;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.adapter.CustomCityListAdapter;
import shy7lo.com.shy7lo.adapter.ReviewOrderAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.model.ApiResponse;
import shy7lo.com.shy7lo.model.AppInit;
import shy7lo.com.shy7lo.model.CityResponse;
import shy7lo.com.shy7lo.model.MyAddressResponse;
import shy7lo.com.shy7lo.model.PaymentMethodRespnose;
import shy7lo.com.shy7lo.model.RegionsResponse;
import shy7lo.com.shy7lo.model.ShoppingBag;
import shy7lo.com.shy7lo.notification.NotificationUtils;
import shy7lo.com.shy7lo.payfort.IPaymentRequestCallBack;
import shy7lo.com.shy7lo.payfort.PayFortData;
import shy7lo.com.shy7lo.payfort.PayFortPayment;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.Constant;
import shy7lo.com.shy7lo.utils.CustomTypefaceSpan;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;
import shy7lo.com.shy7lo.wheel.WheelCountryPicker;
import shy7lo.com.shy7lo.wheel.WheelStringPicker;
import shy7lo.com.shy7lo.widget.PinView;

import static shy7lo.com.shy7lo.application.Shy7lo.TAG;
import static shy7lo.com.shy7lo.utils.Constant.BNDL_USER_TYPE;

/**
 * Created by jiten on 2/9/16.
 */
public class ShipmentPayFragment extends Fragment implements View.OnClickListener, IPaymentRequestCallBack {

    public static String TAG_SHIPMENT_PAY_FRAGMENT = "ShipmentPayFragment";

    @BindView(R.id.rltTopLayout)
    RelativeLayout rltTopLayout;
    @BindView(R.id.tbCountry)
    TableRow tbCountry;
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.ibLocation)
    ImageButton ibLocation;
    @BindView(R.id.lnrCoupon)
    LinearLayout lnrCoupon;
    @BindView(R.id.lnrShipment)
    LinearLayout lnrShipment;
    @BindView(R.id.lnrReviewPay)
    LinearLayout lnrReviewPay;
    @BindView(R.id.lnrCashOnDelivery)
    LinearLayout lnrCashOnDelivery;
    @BindView(R.id.lnrCreditDebitCard)
    LinearLayout lnrCreditDebitCard;
    @BindView(R.id.lnrAmount)
    LinearLayout lnrAmount;
    @BindView(R.id.lnrShippingAmount)
    LinearLayout lnrShippingAmount;
    @BindView(R.id.tvInvalidCity)
    TextView tvInvalidCity;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvCity)
    TextView tvCity;
    @BindView(R.id.tvPhoneNumberTitle)
    TextView tvPhoneNumberTitle;
    @BindView(R.id.etFullName)
    EditText etFullName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etRegion)
    EditText etRegion;
    @BindView(R.id.etCouponCode)
    EditText etCouponCode;
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etCityOther)
    EditText etCityOther;
    @BindView(R.id.etCity)
    AutoCompleteTextView etCity;
    //    EditText etCity;
//    @BindView(R.id.etNeighborhood)
//    EditText etNeighborhood;
    @BindView(R.id.tvProceedPayment)
    TextView tvProceedPayment;
    @BindView(R.id.tvBankDescription)
    TextView tvBankDescription;
    @BindView(R.id.tvCODDescription)
    TextView tvCODDescription;
    @BindView(R.id.tvCardDescription)
    TextView tvCardDescription;
    @BindView(R.id.tvBuyNow)
    TextView tvBuyNow;
    @BindView(R.id.btnDeleteCoupon)
    Button btnDeleteCoupon;
    @BindView(R.id.btnValidateCoupon)
    Button btnValidateCoupon;
    @BindView(R.id.lvOrders)
    ListView lvOrders;
    @BindView(R.id.svMain)
    ScrollView svMain;
    @BindView(R.id.lnrPaymentOption)
    LinearLayout lnrPaymentOption;
    @BindView(R.id.lnrBankTransfer)
    LinearLayout lnrBankTransfer;
    @BindView(R.id.tvBankTransfer)
    TextView tvBankTransfer;
    @BindView(R.id.tvCashOnDelivery)
    TextView tvCashOnDelivery;
    @BindView(R.id.tvCreditDebitCard)
    TextView tvCreditDebitCard;
    @BindView(R.id.lnrStandardDelivery)
    LinearLayout lnrStandardDelivery;
    @BindView(R.id.lnrExpressDelivery)
    LinearLayout lnrExpressDelivery;
    @BindView(R.id.lnrSaveAddress)
    LinearLayout lnrSaveAddress;
    @BindView(R.id.cbSaveAddress)
    CheckBox cbSaveAddress;
    @BindView(R.id.cbStandardDelivery)
    CheckBox cbStandardDelivery;
    @BindView(R.id.cbExpressDelivery)
    CheckBox cbExpressDelivery;
    @BindView(R.id.cbBankTransfer)
    CheckBox cbBankTransfer;
    @BindView(R.id.cbCashOnDelivery)
    CheckBox cbCashOnDelivery;
    @BindView(R.id.cbCreditDebitCard)
    CheckBox cbCreditDebitCard;
    @BindView(R.id.tvView)
    TextView tvView;
    @BindView(R.id.tvSaveAddress)
    TextView tvSaveAddress;
    @BindView(R.id.tvYourCart)
    TextView tvYourCart;
    @BindView(R.id.tvCouponCode)
    TextView tvCouponCode;
    @BindView(R.id.tvPaymentMethod)
    TextView tvPaymentMethod;
    @BindView(R.id.tvExpressDelivery)
    TextView tvExpressDelivery;
    @BindView(R.id.tvStandardDelivery)
    TextView tvStandardDelivery;
    @BindView(R.id.tvExpressDeliveryDetails)
    TextView tvExpressDeliveryDetails;
    @BindView(R.id.tvStandardDeliveryDetails)
    TextView tvStandardDeliveryDetails;
    @BindView(R.id.tvShippingOptions)
    TextView tvShippingOptions;
    //    @BindView(R.id.tvNeighborhood)
//    TextView tvNeighborhood;
    @BindView(R.id.tvDeliveryAddress)
    TextView tvDeliveryAddress;
    @BindView(R.id.tvPersonalInfo)
    TextView tvPersonalInfo;
    @BindView(R.id.tvCountryTitle)
    TextView tvCountryTitle;
    @BindView(R.id.tvCountry)
    TextView tvCountry;
    @BindView(R.id.tvCountryCode)
    TextView tvCountryCode;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvFullName)
    TextView tvFullName;
    @BindView(R.id.tvLastName)
    TextView tvLastName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvRegion)
    TextView tvRegion;
    @BindView(R.id.tvShipingAddresses)
    TextView tvShipingAddresses;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvChangeAddress)
    TextView tvChangeAddress;
    @BindView(R.id.tvUserAddress)
    TextView tvUserAddress;
    @BindView(R.id.tvUserCity)
    TextView tvUserCity;
    @BindView(R.id.tvUserCountry)
    TextView tvUserCountry;
    @BindView(R.id.tvUserTelephone)
    TextView tvUserTelephone;
    @BindView(R.id.tvUserAddAddress)
    TextView tvUserAddAddress;
    @BindView(R.id.lnrUserAddress)
    LinearLayout lnrUserAddress;
    @BindView(R.id.lnrUserDetails)
    LinearLayout lnrUserDetails;


    DBAdapter dbAdapter;

    View mView;

    enum DeliveryType {Standard, Express}

    private String mCurrencyCode = "";
    private float mExchangeRate;
    String mUserType = "", mTotalAmount = "", mLogTotalAmount = "";
    private boolean isDefaultShipping;

    private boolean isGuestUser = false;
    String token = "", mSelectedPaymentCode = "", mSelectedPaymentMethod = "", mCountryCode = "", mCountryID = "";
    //    String userEmail = "";
    private boolean isBankTransfer = false, isCashOnDelivery = false,
            isCreditCard = false;
//    private int mSelectedPaymentPosition = -1;

    public List<MyAddressResponse.Address> mAddressList = null;
    MyAddressResponse.Address mAddressUser;

    public FortCallBackManager fortCallback = null;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 50;
    public static final int RC_ADDRESS_CITY = 5050;

    static ShipmentPayFragment shipmentPayFragment;

    private String mUserFirtname = "", mUserLastname = "", mUserEmail = "", mUserAddress = "", mUserMobile = "", mUserRegion = "", mUserCity = "";

    //    List<AppInit.ExchangeRate> mExchangeRateList;
    List<AppInit.Country> mCountryList;
    private int mInCountryPosition = 1;
    private String mInCountryCode = "+966";
    private String mInCountryId = "SA";
    private float mInExchangeRate = 1f;
    private String mInCurrencyCode = "SAR";
    private String mInCurrencyArCode = "ر.س";

    private List<PaymentMethodRespnose.PaymentMethod> mPaymentMethodList = new ArrayList<>();

    List<RegionsResponse.Region> mRegionList;
    List<CityResponse.City> mCityList;

    Animation animClose, animOpen;

    public static ShipmentPayFragment getInstance() {
        if (shipmentPayFragment == null) {
            shipmentPayFragment = new ShipmentPayFragment();
        }
        return shipmentPayFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_shipment, container, false);
        Utils.setLocale(getActivity());
        ButterKnife.bind(this, mView);
        resetData();
        initilizePayFortSDK();
        InitializeControls();
        InitializeControlsAction();
        showLayoutView1();

//        etFullName.setText("Test");
//        etPhoneNumber.setText("1234567");
//        etAddress.setText("Test");
//        etCity.setText("Test");

        LogUtils.e("", "language::" + getResources().getConfiguration().locale);
        return mView;
    }

    private void initilizePayFortSDK() {
        fortCallback = FortCallback.Factory.create();
    }

    private void InitializeControls() {

        dbAdapter = new DBAdapter(getActivity());

//        mSelectedPaymentPosition = -1;

        animOpen = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_open);
        animClose = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_close);


        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            tvTitle.setBackgroundResource(R.drawable.ic_top_logo_ar);
            tvTitle.setTypeface(Shy7lo.DroidKufiBold);
            tvFullName.setTypeface(Shy7lo.DroidKufiRegular);
            tvLastName.setTypeface(Shy7lo.DroidKufiRegular);
            tvEmail.setTypeface(Shy7lo.DroidKufiRegular);
            tvRegion.setTypeface(Shy7lo.DroidKufiRegular);
            etFullName.setTypeface(Shy7lo.DroidKufiRegular);
            etLastName.setTypeface(Shy7lo.DroidKufiRegular);
            etEmail.setTypeface(Shy7lo.DroidKufiRegular);
            etRegion.setTypeface(Shy7lo.DroidKufiRegular);
            tvCountryCode.setTypeface(Shy7lo.DroidKufiRegular);
            tvPhoneNumberTitle.setTypeface(Shy7lo.DroidKufiRegular);
            etPhoneNumber.setTypeface(Shy7lo.DroidKufiRegular);
            tvAddress.setTypeface(Shy7lo.DroidKufiRegular);
            etAddress.setTypeface(Shy7lo.DroidKufiRegular);
            tvCity.setTypeface(Shy7lo.DroidKufiRegular);
            etCity.setTypeface(Shy7lo.DroidKufiRegular);
            etCityOther.setTypeface(Shy7lo.DroidKufiRegular);
            tvProceedPayment.setTypeface(Shy7lo.DroidKufiRegular);
            tvBankTransfer.setTypeface(Shy7lo.DroidKufiRegular);
            tvCashOnDelivery.setTypeface(Shy7lo.DroidKufiRegular);
            tvCreditDebitCard.setTypeface(Shy7lo.DroidKufiRegular);
            etCouponCode.setTypeface(Shy7lo.DroidKufiRegular);
            btnValidateCoupon.setTypeface(Shy7lo.DroidKufiRegular);
            btnDeleteCoupon.setTypeface(Shy7lo.DroidKufiRegular);
            tvBuyNow.setTypeface(Shy7lo.DroidKufiRegular);
            tvBankDescription.setTypeface(Shy7lo.DroidKufiRegular);
            tvCODDescription.setTypeface(Shy7lo.DroidKufiRegular);
            tvCardDescription.setTypeface(Shy7lo.DroidKufiRegular);
            tvInvalidCity.setTypeface(Shy7lo.DroidKufiRegular);
            tvCouponCode.setTypeface(Shy7lo.DroidKufiBold);

            rltTopLayout.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            lnrShipment.setScaleX(-1f);
            tvFullName.setScaleX(-1f);
            tvLastName.setScaleX(-1f);
            tvEmail.setScaleX(-1f);
            tvRegion.setScaleX(-1f);
            tvAddress.setScaleX(-1f);
            tvCity.setScaleX(-1f);
            tbCountry.setScaleX(-1f);
//            tvProceedPayment.setScaleX(-1f);
            tvInvalidCity.setScaleX(-1f);
            etFullName.setScaleX(-1f);
            etLastName.setScaleX(-1f);
            etEmail.setScaleX(-1f);
            etRegion.setScaleX(-1f);
            etAddress.setScaleX(-1f);
            etCity.setScaleX(-1f);
            etCityOther.setScaleX(-1f);
            lnrReviewPay.setScaleX(-1f);
            tvBankTransfer.setScaleX(-1f);
            tvCashOnDelivery.setScaleX(-1f);
            tvCreditDebitCard.setScaleX(-1f);
//            tvBuyNow.setScaleX(-1f);
            etCouponCode.setScaleX(-1f);
            btnValidateCoupon.setScaleX(-1f);
            btnDeleteCoupon.setScaleX(-1f);
            tvBankDescription.setScaleX(-1f);
            tvCODDescription.setScaleX(-1f);
            tvCardDescription.setScaleX(-1f);
            ibLocation.setScaleX(-1f);
            tvCountryTitle.setScaleX(-1f);
            tvCountry.setScaleX(-1f);
            tvPersonalInfo.setScaleX(-1f);
            tvPhoneNumberTitle.setScaleX(-1f);
//            tvCountryCode.setScaleX(-1f);
//            etPhoneNumber.setScaleX(-1f);
            tvDeliveryAddress.setScaleX(-1f);
//            tvNeighborhood.setScaleX(-1f);
//            etNeighborhood.setScaleX(-1f);
            tvShippingOptions.setScaleX(-1f);
            tvShipingAddresses.setScaleX(-1f);
            tvStandardDelivery.setScaleX(-1f);
            tvStandardDeliveryDetails.setScaleX(-1f);
            tvExpressDelivery.setScaleX(-1f);
            tvExpressDeliveryDetails.setScaleX(-1f);
            tvPaymentMethod.setScaleX(-1f);
            tvCouponCode.setScaleX(-1f);
            tvYourCart.setScaleX(-1f);
            tvView.setScaleX(-1f);
            cbSaveAddress.setScaleX(-1f);
            tvSaveAddress.setScaleX(-1f);
            tvUserAddress.setScaleX(-1f);
            tvUserAddAddress.setScaleX(-1f);
            tvChangeAddress.setScaleX(-1f);
            tvUserName.setScaleX(-1f);
            tvUserCity.setScaleX(-1f);
            tvUserCountry.setScaleX(-1f);
            tvUserTelephone.setScaleX(-1f);

            tvUserName.setGravity(Gravity.RIGHT);
            tvChangeAddress.setGravity(Gravity.RIGHT);
            tvUserCity.setGravity(Gravity.RIGHT);
            tvUserCountry.setGravity(Gravity.RIGHT);
            tvUserTelephone.setGravity(Gravity.RIGHT);
            tvYourCart.setGravity(Gravity.RIGHT);
            tvStandardDelivery.setGravity(Gravity.RIGHT);
            tvStandardDeliveryDetails.setGravity(Gravity.RIGHT);
            tvExpressDelivery.setGravity(Gravity.RIGHT);
            tvExpressDeliveryDetails.setGravity(Gravity.RIGHT);
//            etNeighborhood.setGravity(Gravity.RIGHT);
            tvCountry.setGravity(Gravity.RIGHT);
            etFullName.setGravity(Gravity.RIGHT);
            etLastName.setGravity(Gravity.RIGHT);
            etEmail.setGravity(Gravity.RIGHT);
            etRegion.setGravity(Gravity.RIGHT);
            etPhoneNumber.setGravity(Gravity.RIGHT);
            etAddress.setGravity(Gravity.RIGHT);
            etCity.setGravity(Gravity.RIGHT);
            etCityOther.setGravity(Gravity.RIGHT);
            tvCountryCode.setGravity(Gravity.LEFT);
            tvBankTransfer.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tvCashOnDelivery.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tvCreditDebitCard.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            etCouponCode.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tvBankDescription.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tvCODDescription.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tvCardDescription.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tvInvalidCity.setGravity(Gravity.RIGHT);

            tvCountry.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_details_arrow_left, 0, 0, 0);
            tvCardDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_done, 0);
            tvCODDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_empty, 0);
            tvBankDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_empty, 0);
//            tvCODDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            tvBankDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        } else {

//            tvTitle.setBackgroundResource(R.drawable.ic_top_logo);
            tvTitle.setTypeface(Shy7lo.RalewayBold);
            tvFullName.setTypeface(Shy7lo.RalewayRegular);
            tvLastName.setTypeface(Shy7lo.RalewayRegular);
            tvEmail.setTypeface(Shy7lo.RalewayRegular);
            tvRegion.setTypeface(Shy7lo.RalewayRegular);
            etFullName.setTypeface(Shy7lo.RalewayRegular);
            etLastName.setTypeface(Shy7lo.RalewayRegular);
            etEmail.setTypeface(Shy7lo.RalewayRegular);
            etRegion.setTypeface(Shy7lo.RalewayRegular);
            tvCountryCode.setTypeface(Shy7lo.RalewayRegular);
            tvPhoneNumberTitle.setTypeface(Shy7lo.RalewayRegular);
            etPhoneNumber.setTypeface(Shy7lo.RalewayRegular);
            tvAddress.setTypeface(Shy7lo.RalewayRegular);
            etAddress.setTypeface(Shy7lo.RalewayRegular);
            tvInvalidCity.setTypeface(Shy7lo.RalewayRegular);
            tvCity.setTypeface(Shy7lo.RalewayRegular);
            etCity.setTypeface(Shy7lo.RalewayRegular);
            etCityOther.setTypeface(Shy7lo.RalewayRegular);
            tvProceedPayment.setTypeface(Shy7lo.RalewayRegular);
            tvBankTransfer.setTypeface(Shy7lo.RalewayRegular);
            tvCashOnDelivery.setTypeface(Shy7lo.RalewayRegular);
            tvCreditDebitCard.setTypeface(Shy7lo.RalewayRegular);
            etCouponCode.setTypeface(Shy7lo.RalewayRegular);
            btnValidateCoupon.setTypeface(Shy7lo.RalewayRegular);
            btnDeleteCoupon.setTypeface(Shy7lo.RalewayRegular);
            tvBuyNow.setTypeface(Shy7lo.RalewayRegular);
            tvBankDescription.setTypeface(Shy7lo.RalewayRegular);
            tvCODDescription.setTypeface(Shy7lo.RalewayRegular);
            tvCardDescription.setTypeface(Shy7lo.RalewayRegular);
            tvCouponCode.setTypeface(Shy7lo.RalewayBold);

            rltTopLayout.setScaleX(1f);
            tvTitle.setScaleX(1f);
            lnrShipment.setScaleX(1f);
            tvFullName.setScaleX(1f);
            tvLastName.setScaleX(1f);
            tvEmail.setScaleX(1f);
            tvRegion.setScaleX(1f);
            tvCountryCode.setScaleX(1f);
            tvPhoneNumberTitle.setScaleX(1f);
            tvAddress.setScaleX(1f);
            tvCity.setScaleX(1f);
            tbCountry.setScaleX(1f);
            tvProceedPayment.setScaleX(1f);
            etFullName.setScaleX(1f);
            etLastName.setScaleX(1f);
            etEmail.setScaleX(1f);
            etRegion.setScaleX(1f);
            etPhoneNumber.setScaleX(1f);
            etAddress.setScaleX(1f);
            tvInvalidCity.setScaleX(1f);
            etCity.setScaleX(1f);
            etCityOther.setScaleX(1f);
            lnrReviewPay.setScaleX(1f);
            tvBankTransfer.setScaleX(1f);
            tvCashOnDelivery.setScaleX(1f);
            tvCreditDebitCard.setScaleX(1f);
            tvBuyNow.setScaleX(1f);
            etCouponCode.setScaleX(1f);
            btnValidateCoupon.setScaleX(1f);
            btnDeleteCoupon.setScaleX(1f);
            tvBankDescription.setScaleX(1f);
            tvCODDescription.setScaleX(1f);
            tvCardDescription.setScaleX(1f);
            ibLocation.setScaleX(1f);
            tvCountryTitle.setScaleX(1f);
            tvCountry.setScaleX(1f);
            tvPersonalInfo.setScaleX(1f);
            tvDeliveryAddress.setScaleX(1f);
//            tvNeighborhood.setScaleX(1f);
//            etNeighborhood.setScaleX(1f);
            tvShippingOptions.setScaleX(1f);
            tvShipingAddresses.setScaleX(1f);
            tvStandardDelivery.setScaleX(1f);
            tvStandardDeliveryDetails.setScaleX(1f);
            tvExpressDelivery.setScaleX(1f);
            tvExpressDeliveryDetails.setScaleX(1f);
            tvPaymentMethod.setScaleX(1f);
            tvCouponCode.setScaleX(1f);
            tvYourCart.setScaleX(1f);
            tvView.setScaleX(1f);
            cbSaveAddress.setScaleX(1f);
            tvSaveAddress.setScaleX(1f);
            tvUserAddress.setScaleX(1f);
            tvUserAddAddress.setScaleX(1f);
            tvChangeAddress.setScaleX(1f);
            tvUserName.setScaleX(1f);
            tvUserCity.setScaleX(1f);
            tvUserCountry.setScaleX(1f);
            tvUserTelephone.setScaleX(1f);

            tvUserName.setGravity(Gravity.LEFT);
            tvChangeAddress.setGravity(Gravity.LEFT);
            tvUserCity.setGravity(Gravity.LEFT);
            tvUserCountry.setGravity(Gravity.LEFT);
            tvUserTelephone.setGravity(Gravity.LEFT);
            tvYourCart.setGravity(Gravity.LEFT);
            tvStandardDelivery.setGravity(Gravity.LEFT);
            tvStandardDeliveryDetails.setGravity(Gravity.LEFT);
            tvExpressDelivery.setGravity(Gravity.LEFT);
            tvExpressDeliveryDetails.setGravity(Gravity.LEFT);
//            etNeighborhood.setGravity(Gravity.LEFT);
            tvCountry.setGravity(Gravity.LEFT);
            etFullName.setGravity(Gravity.LEFT);
            etLastName.setGravity(Gravity.LEFT);
            etEmail.setGravity(Gravity.LEFT);
            etRegion.setGravity(Gravity.LEFT);
            etPhoneNumber.setGravity(Gravity.LEFT);
            etAddress.setGravity(Gravity.LEFT);
            etCity.setGravity(Gravity.LEFT);
            etCityOther.setGravity(Gravity.LEFT);
            tvCountryCode.setGravity(Gravity.LEFT);
            tvBankTransfer.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            tvCashOnDelivery.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            tvCreditDebitCard.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            etCouponCode.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            tvBankDescription.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            tvCODDescription.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            tvCardDescription.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            tvInvalidCity.setGravity(Gravity.LEFT);

            tvCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_details_arrow_right, 0);
            tvCardDescription.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_done, 0, 0, 0);
            tvCODDescription.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_empty, 0, 0, 0);
            tvBankDescription.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_empty, 0, 0, 0);
//            tvCODDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            tvBankDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

//            SpannableStringBuilder sb = new SpannableStringBuilder(tvStandardDelivery.getText().toString());
//            sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            tvStandardDelivery.setText(sb);

        }

        cbSaveAddress.setChecked(false);
        lnrSaveAddress.setVisibility(View.GONE);

        tvChangeAddress.setPaintFlags(tvChangeAddress.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        isDefaultShipping = false;
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUserType = bundle.getString(BNDL_USER_TYPE);


            String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");

            if (!TextUtils.isEmpty(userToken)) {
//            if (mUserType.equalsIgnoreCase(Constant.BNDL_USER_TYPE_ME)) {

                isGuestUser = false;

                etCityOther.setText("");
                etCityOther.setVisibility(View.GONE);

//                String email = bundle.getString(Constant.BNDL_USER_EMAIL, "");
//                userEmail = email;
//                MyPref.setPref(getActivity(), Constant.BNDL_USER_EMAIL, "" + email);

                getAllAddress();


            } else if (mUserType.equalsIgnoreCase(Constant.BNDL_USER_TYPE_GUEST)) {

                lnrUserAddress.setVisibility(View.VISIBLE);
                lnrUserDetails.setVisibility(View.GONE);


                isGuestUser = true;

//                String email = bundle.getString(Constant.BNDL_USER_EMAIL, "");
//                userEmail = email;
//                MyPref.setPref(getActivity(), Constant.BNDL_USER_EMAIL, "" + email);

                etFullName.setText("");
                etLastName.setText("");
                etEmail.setText("");
                etPhoneNumber.setText("");
                etRegion.setText("");
                etCity.setText("");
                etCityOther.setText("");
                etCityOther.setVisibility(View.GONE);
//                etNeighborhood.setText("");
                etAddress.setText("");

                if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_FULLNAME, ""))) {
                    etFullName.setText("" + MyPref.getPref(getActivity(), Constant.BNDL_USER_FULLNAME, ""));
                }

                if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_LASTNAME, ""))) {
                    etLastName.setText("" + MyPref.getPref(getActivity(), Constant.BNDL_USER_LASTNAME, ""));
                }

                if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_EMAIL, ""))) {
                    etEmail.setText("" + MyPref.getPref(getActivity(), Constant.BNDL_USER_EMAIL, ""));
                }

                if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_CONTACT_NUMBER, ""))) {
                    etPhoneNumber.setText("" + MyPref.getPref(getActivity(), Constant.BNDL_USER_CONTACT_NUMBER, ""));
                }

//                if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_REGION, ""))) {
//                    etRegion.setText("" + MyPref.getPref(getActivity(), Constant.BNDL_USER_REGION, ""));
//                }

                LogUtils.e("", "BNDL_USER_CITY_AR::" + MyPref.getPref(getActivity(), Constant.BNDL_USER_CITY_AR, ""));
                LogUtils.e("", "BNDL_USER_CITY::" + MyPref.getPref(getActivity(), Constant.BNDL_USER_CITY, ""));
                LogUtils.e("", "DEFAULT_LANGUAGE::" + MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic));
                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

                    if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_CITY_AR, ""))) {
                        etCity.setText("" + MyPref.getPref(getActivity(), Constant.BNDL_USER_CITY_AR, ""));
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_CITY_AR, ""))) {
                                etCity.setText("" + MyPref.getPref(getActivity(), Constant.BNDL_USER_CITY_AR, ""));
                            }
                        }
                    }, 250);

//                if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_REGION_AR, ""))) {
//                    etRegion.setText("" + MyPref.getPref(getActivity(), Constant.BNDL_USER_REGION_AR, ""));
//                }

                } else {

                    if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_CITY, ""))) {
                        etCity.setText("" + MyPref.getPref(getActivity(), Constant.BNDL_USER_CITY, ""));
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_CITY, ""))) {
                                etCity.setText("" + MyPref.getPref(getActivity(), Constant.BNDL_USER_CITY, ""));
                            }
                        }
                    }, 250);
//                if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_REGION, ""))) {
//                    etRegion.setText("" + MyPref.getPref(getActivity(), Constant.BNDL_USER_REGION, ""));
//                }
                }

                LogUtils.e("", "etCity.getText().toString()::" + etCity.getText().toString());
                LogUtils.e("", "Constant.BNDL_USER_OTHER_CITY::" + MyPref.getPref(getActivity(), Constant.BNDL_USER_OTHER_CITY, "") + " " + isOtherCity(etCity.getText().toString()));
                if (isOtherCity(etCity.getText().toString())) {
                    if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_OTHER_CITY, ""))) {
                        etCityOther.setVisibility(View.VISIBLE);
                        etCityOther.setText("" + MyPref.getPref(getActivity(), Constant.BNDL_USER_OTHER_CITY, ""));
                    }
                }

//                if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_NEIGHBERHOOD, ""))) {
//                    etNeighborhood.setText("" + MyPref.getPref(getActivity(), Constant.BNDL_USER_NEIGHBERHOOD, ""));
//                }

                if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_ADDRESS, ""))) {
                    etAddress.setText("" + MyPref.getPref(getActivity(), Constant.BNDL_USER_ADDRESS, ""));
                }
            }


        }

        etFullName.setOnFocusChangeListener(mUserOnFocusChangeListener);
        etLastName.setOnFocusChangeListener(mUserOnFocusChangeListener);
        etEmail.setOnFocusChangeListener(mUserOnFocusChangeListener);
        etPhoneNumber.setOnFocusChangeListener(mUserOnFocusChangeListener);
        etAddress.setOnFocusChangeListener(mUserOnFocusChangeListener);
//        etCity.setOnFocusChangeListener(mUserOnFocusChangeListener);

        LogUtils.e("", "2 etCity.getText().toString()::" + etCity.getText().toString());
        etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LogUtils.e("", "beforeTextChanged:" + charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LogUtils.e("", "onTextChanged:" + charSequence);
                if (charSequence.length() < 5) {
                    etCityOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                LogUtils.e("", "afterTextChanged:" + etCity.getText().toString());
            }
        });

        etCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (!isFocus && mCountryCode.contains("966")) {
                    checkCity();
                }
            }
        });

        LogUtils.e("", "3etCity.getText().toString()::" + etCity.getText().toString());

        etCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                etCityOther.setText("");
                MyPref.setPref(getActivity(), Constant.BNDL_USER_OTHER_CITY, "");
                checkCity();
            }
        });

        tvUserAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt(Constant.BNDL_ADDRESS_SIZE, (mAddressList == null ? 0 : mAddressList.size()));
                bundle.putBoolean(Constant.BNDL_IS_FROM_SHIPMENT, true);
                IntentHandler.startActivityForResult(getActivity(), AddAddressActivity.class, bundle, AddAddressActivity.RC_ADD_ADDRESS);

            }
        });

        tvChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Bundle bundle = new Bundle();
//                bundle.putInt(Constant.BNDL_ADDRESS_SIZE, (mAddressList == null ? 0 : mAddressList.size()));
//                bundle.putBoolean(Constant.BNDL_IS_FROM_SHIPMENT, true);
//                bundle.putString(Constant.BNDL_ADDRESS, new Gson().toJson(mAddressUser));
//                IntentHandler.startActivityForResult(getActivity(), AddAddressActivity.class, bundle, AddAddressActivity.RC_ADD_ADDRESS);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.BNDL_ADDRESS, new Gson().toJson(mAddressUser));
                IntentHandler.startActivityForResult(getActivity(), ChangeAddressActivity.class, bundle, AddAddressActivity.RC_ADD_ADDRESS);

            }
        });


//        setCountryConfig();
        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.countries != null) {
            mCountryList = Shy7lo.mAppInit.countries;
            setCountryConfig();
        } else {
            getInitApi();
        }
        LogUtils.e("", "4etCity.getText().toString()::" + etCity.getText().toString());
        showDeliveryType(DeliveryType.Standard);

        etCouponCode.setText("");
        etCouponCode.setEnabled(true);
        if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.USER_COUPON_CODE, ""))) {
            etCouponCode.setText("" + MyPref.getPref(getActivity(), MyPref.USER_COUPON_CODE, ""));
            etCouponCode.setEnabled(false);
            btnValidateCoupon.setVisibility(View.GONE);
            btnDeleteCoupon.setVisibility(View.VISIBLE);
        }

        etCouponCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    if (btnDeleteCoupon.getVisibility() != View.VISIBLE) {
                        btnValidateCoupon.setVisibility(View.VISIBLE);
                    }
                } else {
                    btnValidateCoupon.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        getShippingTotalAmount();

//        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.currencies != null) {
//            mExchangeRateList = Shy7lo.mAppInit.currencies.exchangeRates;
//        } else {
//            getInitApi();
//        }

    }

    private void checkCity() {

        String mCity = etCity.getText().toString();
        if (!TextUtils.isEmpty(mCity) && mCityList != null && mCityList.size() > 0) {
            boolean isFound = false;
            if (isOtherCity(mCity)) {
                etCityOther.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(mCity)) {
                    isFound = true;
                } else {
                    isFound = false;
                }
            } else {
                etCityOther.setVisibility(View.GONE);
                for (int i = 0; i < mCityList.size(); i++) {
                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                        isFound = mCity.equalsIgnoreCase(mCityList.get(i).city);
                    } else {
                        isFound = mCity.equalsIgnoreCase(mCityList.get(i).cityEn);
                    }
                    if (isFound) {
                        break;
                    }
                }
            }
            if (isFound) {
                tvInvalidCity.setVisibility(View.GONE);
            } else {
                tvInvalidCity.setVisibility(View.VISIBLE);
            }
        } else {
            tvInvalidCity.setVisibility(View.GONE);
        }
    }

    private View.OnFocusChangeListener mUserOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean isFocused) {

            if (!isFocused && !isGuestUser) {

                lnrSaveAddress.setVisibility(View.GONE);

                if (!mUserFirtname.equals(etFullName.getText().toString())) {
                    lnrSaveAddress.setVisibility(View.VISIBLE);
                } else if (!mUserLastname.equals(etLastName.getText().toString())) {
                    lnrSaveAddress.setVisibility(View.VISIBLE);
                } else if (!mUserMobile.equals(etPhoneNumber.getText().toString())) {
                    lnrSaveAddress.setVisibility(View.VISIBLE);
                } else if (!mUserAddress.equals(etAddress.getText().toString())) {
                    lnrSaveAddress.setVisibility(View.VISIBLE);
                }
//                else if (!mUserCity.equals(etCity.getText().toString())) {
//                    lnrSaveAddress.setVisibility(View.VISIBLE);
//                }

            }


        }
    };

    private void setUserDetails() {

        //                LogUtils.e("", "USER_FIRSTNAME::" + MyPref.getPref(getActivity(), MyPref.USER_FIRSTNAME, ""));
//                LogUtils.e("", "USER_LASTNAME::" + MyPref.getPref(getActivity(), MyPref.USER_NEIGHBERHOOD, ""));
//                LogUtils.e("", "USER_PHONE::" + MyPref.getPref(getActivity(), MyPref.USER_PHONE, ""));
//                LogUtils.e("", "USER_ADDRESS::" + MyPref.getPref(getActivity(), MyPref.USER_ADDRESS, ""));
//                LogUtils.e("", "USER_CITY::" + MyPref.getPref(getActivity(), MyPref.USER_CITY, ""));

        if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.USER_EMAIL, ""))) {
            etEmail.setText("" + MyPref.getPref(getActivity(), MyPref.USER_EMAIL, ""));
        }
        LogUtils.e("", "etEmail::" + etEmail.getText().toString());
        mUserEmail = etEmail.getText().toString();

        if (mAddressList == null && mAddressList.size() == 0) {

            if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.USER_FIRSTNAME, ""))) {
                etFullName.setText("" + MyPref.getPref(getActivity(), MyPref.USER_FIRSTNAME, ""));
            }
            LogUtils.e("", "etFullName::" + etFullName.getText().toString());

            if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.USER_LASTNAME, ""))) {
                etLastName.setText("" + MyPref.getPref(getActivity(), MyPref.USER_LASTNAME, ""));
            }
            LogUtils.e("", "etLastName::" + etLastName.getText().toString());

            if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.USER_PHONE, ""))) {
                etPhoneNumber.setText("" + MyPref.getPref(getActivity(), MyPref.USER_PHONE, ""));
            }
            LogUtils.e("", "etPhoneNumber::" + etPhoneNumber.getText().toString());

            if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.USER_REGION, ""))) {
                etRegion.setText("" + MyPref.getPref(getActivity(), MyPref.USER_REGION, ""));
            }
            LogUtils.e("", "etCity::" + etCity.getText().toString());

            if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.USER_CITY, ""))) {
                etCity.setText("" + MyPref.getPref(getActivity(), MyPref.USER_CITY, ""));
            }
            LogUtils.e("", "etCity::" + etCity.getText().toString());

//            if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.USER_NEIGHBERHOOD, ""))) {
//                etNeighborhood.setText("" + MyPref.getPref(getActivity(), MyPref.USER_NEIGHBERHOOD, ""));
//            }
//            LogUtils.e("", "etNeighborhood::" + etNeighborhood.getText().toString());

            if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.USER_ADDRESS, ""))) {
                etAddress.setText("" + MyPref.getPref(getActivity(), MyPref.USER_ADDRESS, ""));
            }
            LogUtils.e("", "etAddress::" + etAddress.getText().toString());

            mUserFirtname = etFullName.getText().toString();
            mUserLastname = etLastName.getText().toString();
            mUserMobile = etPhoneNumber.getText().toString();
            mUserAddress = etAddress.getText().toString();
            mUserCity = etCity.getText().toString();
        }
    }

    private void setCountryConfig() {

        mInCountryPosition = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_POSITION, 1);
        mInCurrencyCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "SAR");
        mInCurrencyArCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_ARABIC_CURRENCY_CODE, "ر.س");
        mInExchangeRate = MyPref.getPref(getActivity(), MyPref.DEFAULT_EXCHANGE_RATE, 1f);

        mInCountryCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_CODE, "+966");
        mInCountryId = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_ID, "SA");


        mCurrencyCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR));
        mExchangeRate = MyPref.getPref(getActivity(), MyPref.DEFAULT_EXCHANGE_RATE, 1f);
        mCountryCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_CODE, "+966");
        mCountryID = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_ID, "SA");

        mCountryCode = mCountryCode.replace("+", "");

        tvCountryCode.setText("" + mCountryCode);

        if (mCountryList != null && mCountryList.size() > 0) {
            for (int i = 0; i < mCountryList.size(); i++) {
                if (mCountryList.get(i).id.equalsIgnoreCase(MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_ID, "SA"))) {

                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                        tvCountry.setText("" + mCountryList.get(i).fullNameArabic);
                    } else {
                        tvCountry.setText("" + mCountryList.get(i).fullNameEnglish);
                    }

                }
            }
        }

        if (!TextUtils.isEmpty(mCountryCode) && mCountryCode.contains("966")) {

//            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                etRegion.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_down, 0, 0, 0);
//            } else {
//                etRegion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
//            }

//            getRegionList();
//            if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), Constant.BNDL_USER_REGION_ID, ""))) {
            getCityList();
//            }
//
//
//            etRegion.setFocusable(false);
//            etRegion.setClickable(false);
//
//            etRegion.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    showRegionDialog();
//                }
//            });
//
            etCity.setDropDownHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        } else {
//            etRegion.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            etRegion.setOnClickListener(null);
//            etRegion.setClickable(true);
//            etRegion.setFocusableInTouchMode(true);
            etCity.setDropDownHeight(0);
        }

    }

    private void InitializeControlsAction() {

        ibBack.setOnClickListener(this);
        tvProceedPayment.setOnClickListener(this);
        tvBuyNow.setOnClickListener(this);
        lnrBankTransfer.setOnClickListener(this);
        lnrCashOnDelivery.setOnClickListener(this);
        lnrCreditDebitCard.setOnClickListener(this);
        btnValidateCoupon.setOnClickListener(this);
        btnDeleteCoupon.setOnClickListener(this);
        ibLocation.setOnClickListener(this);
        lnrStandardDelivery.setOnClickListener(this);
        lnrExpressDelivery.setOnClickListener(this);
        tvCountry.setOnClickListener(this);
        lnrSaveAddress.setOnClickListener(this);
        tvView.setOnClickListener(this);

    }

    private void showDeliveryType(DeliveryType mDeliveryType) {
        switch (mDeliveryType) {
            case Standard:
                cbStandardDelivery.setChecked(true);
                cbExpressDelivery.setChecked(false);
                lnrStandardDelivery.setBackgroundColor(getResources().getColor(R.color.yellow_delivery));
                lnrExpressDelivery.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case Express:
                cbStandardDelivery.setChecked(false);
                cbExpressDelivery.setChecked(true);
                lnrStandardDelivery.setBackgroundColor(getResources().getColor(R.color.white));
                lnrExpressDelivery.setBackgroundColor(getResources().getColor(R.color.yellow_delivery));
                break;

        }
    }

    @Override
    public void onClick(View view) {

        if (view == ibBack) {

            if (lnrShipment.getVisibility() == View.VISIBLE) {
                if (getActivity() instanceof HomeActivity) {
                    HomeActivity activity = (HomeActivity) getActivity();
                    activity.onBackPressed();
                }
            } else {
                showLayoutView1();
            }


//            if (getActivity() instanceof HomeActivity) {
//                HomeActivity activity = (HomeActivity) getActivity();
//                activity.openDrawer();
//            }
        } else if (view == tvProceedPayment) {

            if (TextUtils.isEmpty(etFullName.getText().toString().trim())) {
                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_firstname));
                return;
            }

            if (TextUtils.isEmpty(etLastName.getText().toString().trim())) {
                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_lastname));
                return;
            }

            if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_email_address));
                return;
            }

            if (!Utils.isValidEmail(etEmail.getText().toString().trim())) {
                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_email_not_valid));
                return;
            }

            if (TextUtils.isEmpty(etPhoneNumber.getText().toString().trim())) {
                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_contact_number));
                return;
            }

//            if (TextUtils.isEmpty(etRegion.getText().toString().trim())) {
//                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_region));
//                return;
//            }

            if (TextUtils.isEmpty(etCity.getText().toString().trim())) {
                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_city));
                return;
            }

            if (isOtherCity(etCity.getText().toString().trim()) && TextUtils.isEmpty(etCityOther.getText().toString().trim())) {
                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_other_city));
                return;
            }

//            if (TextUtils.isEmpty(etNeighborhood.getText().toString().trim())) {
//                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_neighberhood));
//                return;
//            }

            if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_address));
                return;
            }


            if (isDefaultShipping && !isGuestUser) {
                submitAddress();
            } else {
                checkCity();

                if (tvInvalidCity.getVisibility() != View.VISIBLE) {
                    submitAddress();
                }
            }


//            submitAddress();


//            MyPref.setPref(getActivity(), Constant.BNDL_USER_FULLNAME, "" + etFullName.getText().toString());
//            MyPref.setPref(getActivity(), Constant.BNDL_USER_CONTACT_NUMBER, "" + etPhoneNumber.getText().toString());
//            MyPref.setPref(getActivity(), Constant.BNDL_USER_ADDRESS, "" + etAddress.getText().toString());
//            MyPref.setPref(getActivity(), Constant.BNDL_USER_CITY, "" + etCity.getText().toString());
//
//            tvShippingName.setText("" + etFullName.getText().toString());
//            tvShippingAddress.setText("" + etAddress.getText().toString() + ", " + etCity.getText().toString() + "\n" + etPhoneNumber.getText().toString());
//
//            showLayoutView2();

//        } else if (view == tvBack || view == tvBottomBack) {
//
//            showLayoutView1();

        } else if (view == tvBuyNow) {

//            if (getActivity() instanceof HomeActivity) {
//                HomeActivity activity = (HomeActivity) getActivity();
//                activity.loadShoppingBagsWithClearStack();
//            }


            if (!TextUtils.isEmpty(mSelectedPaymentCode)) {

                if (mSelectedPaymentCode.equalsIgnoreCase("payfort_fort_cc")) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        placeOrder();
                    } else {
                        Dexter.withActivity(getActivity())
                                .withPermission(Manifest.permission.READ_PHONE_STATE)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response) {
                                        placeOrder();
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {

                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                    }
                                })
                                .check();
                    }
                } else {
                    placeOrder();
                }


            } else {
                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_select_payment));
            }

        } else if (view == lnrBankTransfer) {

            showViewSelection(3);


        } else if (view == lnrCashOnDelivery) {

            showViewSelection(2);

        } else if (view == lnrCreditDebitCard) {

            showViewSelection(1);

        } else if (view == btnValidateCoupon) {

            String couponCode = etCouponCode.getText().toString();
            if (!TextUtils.isEmpty(couponCode)) {
                validateCouponCode(couponCode);
            } else {
                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_coupon_code));
            }
        } else if (view == btnDeleteCoupon) {
            String couponCode = etCouponCode.getText().toString();
            if (!TextUtils.isEmpty(couponCode)) {
                deleteCouponCode();
            } else {
                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_coupon_code));
            }
        } else if (view == ibLocation) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!checkPermissions()) {
                    requestPermissions();
                } else {
                    goMapActivity();
                }
            } else {
                goMapActivity();
            }
        } else if (view == lnrStandardDelivery) {
            showDeliveryType(DeliveryType.Standard);
        } else if (view == lnrExpressDelivery) {
            showDeliveryType(DeliveryType.Express);
        } else if (view == lnrSaveAddress) {
            cbSaveAddress.setChecked(!cbSaveAddress.isChecked());
        } else if (view == tvCountry) {
            showCountryDialog();
        } else if (view == tvView) {

            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.onBackPressed();
            }
        }

    }

    private void getAllAddress() {

        if (!Utils.isInternetConnected(getActivity())) {
            return;
        }

//        Utils.showProgressDialog(getActivity());
        ApiInterface serviceAPI =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        Call<MyAddressResponse> call = serviceAPI.getAllAddress(Shy7lo.mLangCode, "Bearer " + userToken);
        call.enqueue(new Callback<MyAddressResponse>() {
            @Override
            public void onResponse(Call<MyAddressResponse> call, Response<MyAddressResponse> response) {
//                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    MyAddressResponse mMyAddressResponse = response.body();
                    if (mMyAddressResponse != null && mMyAddressResponse.success == 1) {
                        mAddressList = mMyAddressResponse.data;
                        if (mAddressList != null && mAddressList.size() > 0) {
                            for (int i = 0; i < mAddressList.size(); i++) {
                                MyAddressResponse.Address mAddress = mAddressList.get(i);
                                if (mAddress.defaultShipping) {
                                    mAddressUser = mAddress;
                                    setUserAddress(mAddress);
                                    break;
                                }
                            }

                        } else {
                            lnrUserAddress.setVisibility(View.VISIBLE);
                            lnrUserDetails.setVisibility(View.GONE);
                            lnrSaveAddress.setVisibility(View.VISIBLE);
                            cbSaveAddress.setChecked(true);
                        }

                    } else if (mMyAddressResponse != null && mMyAddressResponse.success == 0) {
                        Utils.showToast(getActivity(), "" + mMyAddressResponse.message);
                    } else {
//                        Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
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
            public void onFailure(Call<MyAddressResponse> call, Throwable t) {
//                Utils.closeProgressDialog();
            }
        });
    }

    private void setUserAddress(MyAddressResponse.Address mAddress) {

        if (mAddress != null) {
            isDefaultShipping = true;
            etFullName.setText("" + mAddress.firstname);
            etLastName.setText("" + mAddress.lastname);
            etCity.setText("" + mAddress.city);
            if (mAddress.street != null && mAddress.street.size() > 0) {
                etAddress.setText("" + mAddress.street.get(0));
            }
            if (mAddress.telephone.contains("+")) {
                tvCountryCode.setText("" + mAddress.telephone.substring(0, 4));
                mCountryCode = mAddress.telephone.substring(0, 4);
                LogUtils.e("", "mCountryCode:" + mCountryCode);
                etPhoneNumber.setText("" + mAddress.telephone.substring(4));
            } else {
                etPhoneNumber.setText("" + mAddress.telephone);
            }

            etEmail.setText("" + MyPref.getPref(getActivity(), MyPref.USER_EMAIL, ""));

//                                    if (mAddress.region != null) {
//                                        etRegion.setText("" + mAddress.region.region);
//                                    }

            setUserDetails();
//                                    mStateID = "" + mAddress.regionId;
            getCityList();

            lnrUserAddress.setVisibility(View.GONE);
            lnrUserDetails.setVisibility(View.VISIBLE);
            tvUserName.setText(mAddress.firstname + " " + mAddress.lastname);
//            if (!TextUtils.isEmpty(mAddress.postcode)) {
//                tvUserCity.setText(mAddress.city + ", " + mAddress.postcode);
//            } else {
            tvUserCity.setText(mAddress.city);
//            }
            tvUserCountry.setText(mAddress.countryId);
            tvUserTelephone.setText(getString(R.string.mobile_no) + ": " + mAddress.telephone);

            if (mAddress.street != null && mAddress.street.size() > 0) {
                tvUserAddress.setVisibility(View.VISIBLE);
                tvUserAddress.setText(mAddress.street.get(0));
            } else {
                tvUserAddress.setVisibility(View.GONE);
            }

            mCountryID = mAddress.countryId;

            if (mAddress.countryId.equalsIgnoreCase("SA")) {
                tvUserCountry.setText("" + getString(R.string.saudi_arabia));
                mInCountryPosition = 1;
                mInCountryCode = "966";
            } else if (mAddress.countryId.equalsIgnoreCase("AE")) {
                tvUserCountry.setText("" + getString(R.string.uae));
                mInCountryPosition = 2;
                mInCountryCode = "971";
            } else if (mAddress.countryId.equalsIgnoreCase("QA")) {
                tvUserCountry.setText("" + getString(R.string.qatar));
                mInCountryPosition = 3;
                mInCountryCode = "974";
            } else if (mAddress.countryId.equalsIgnoreCase("KW")) {
                tvUserCountry.setText("" + getString(R.string.kuwait));
                mInCountryPosition = 4;
                mInCountryCode = "965";
            } else if (mAddress.countryId.equalsIgnoreCase("BH")) {
                tvUserCountry.setText("" + getString(R.string.bahrin));
                mInCountryPosition = 5;
                mInCountryCode = "973";
            } else if (mAddress.countryId.equalsIgnoreCase("OM")) {
                tvUserCountry.setText("" + getString(R.string.oman));
                mInCountryPosition = 5;
                mInCountryCode = "968";
            }

            if (mCountryList != null && mCountryList.size() > 0) {
                for (int j = 0; j < mCountryList.size(); j++) {
                    if (mCountryList.get(j).id.equalsIgnoreCase(mAddress.countryId)) {
                        mInCurrencyCode = "" + mCountryList.get(j).currencyEn;
                        mInCurrencyArCode = "" + mCountryList.get(j).currencyAr;
                        mInExchangeRate = mCountryList.get(j).exchangeRate;
                    }
                }

            }

        }
    }

//    private void getRegionList() {
//
//        if (!Utils.isInternetConnected(getActivity())) {
//            return;
//        }
//
////        Utils.showProgressDialog(getActivity());
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
//        Call<RegionsResponse> call = apiService.getRegionList(Shy7lo.mLangCode, "SA");
//        call.enqueue(new Callback<RegionsResponse>() {
//            @Override
//            public void onResponse(Call<RegionsResponse> call, Response<RegionsResponse> response) {
//
//                LogUtils.e(ContentValues.TAG, "response code:" + response.code());
////                Utils.closeProgressDialog();
//
//                if (response.isSuccessful()) {
//                    try {
//
//                        RegionsResponse mRegionsResponse = response.body();
//                        if (mRegionsResponse != null && mRegionsResponse.success == 1) {
//                            mRegionList = mRegionsResponse.data;
////                            if (mRegionList != null && mRegionList.size() > 0) {
////                                getCityList(mRegionList.get(0).id);
////                            }
//                        }
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RegionsResponse> call, Throwable t) {
//                System.out.println(t.getMessage());
////                Utils.closeProgressDialog();
//            }
//        });
//
//    }

    private void getCityList() {

        if (mCityList != null && mCityList.size() > 0) {
            setCityList();
        } else {

            if (!Utils.isInternetConnected(getActivity())) {
                Utils.showToast(getActivity(), getString(R.string.pf_no_connection));
                return;
            }

//        Utils.showProgressDialog(getActivity());

            ApiInterface apiService =
                    RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
            Call<CityResponse> call = apiService.getCityList(Shy7lo.mLangCode);
            call.enqueue(new Callback<CityResponse>() {
                @Override
                public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
//                Utils.closeProgressDialog();
                    LogUtils.e(ContentValues.TAG, "response code:" + response.code());

                    if (response.isSuccessful()) {
                        try {

                            CityResponse mCityResponse = response.body();
                            if (mCityResponse != null && mCityResponse.success == 1) {
                                mCityList = mCityResponse.data;
                                setCityList();

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
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
                public void onFailure(Call<CityResponse> call, Throwable t) {
//                Utils.closeProgressDialog();
                    System.out.println(t.getMessage());
                }
            });

        }

    }

    private void setCityList() {

        ArrayList<String> mCityNameList = new ArrayList<>();
        for (int i = 0; i < mCityList.size(); i++) {
            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                mCityNameList.add("" + mCityList.get(i).city);
            } else {
                mCityNameList.add("" + mCityList.get(i).cityEn);
            }
        }
//                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                                    (getActivity(), android.R.layout.select_dialog_item, mCityNameList);
        CustomCityListAdapter adapter = new CustomCityListAdapter(getActivity(),
                R.layout.list_item_autocomplete_city, mCityNameList, etCity);
        etCity.setAdapter(adapter);

    }

    private boolean isOtherCity(String city) {
        if (!TextUtils.isEmpty(city)) {
            if (city.equalsIgnoreCase("Other") || city.equalsIgnoreCase("اخرى")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    String mStateID = "";

    private void showRegionDialog() {

        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_picker_list);

        final RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.rlMain);
        final LinearLayout lnrContainer = (LinearLayout) dialog.findViewById(R.id.lnrContainer);
        final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        final TextView tvDone = (TextView) dialog.findViewById(R.id.tvDone);

        final WheelStringPicker mWeelPicker = (WheelStringPicker) dialog.findViewById(R.id.pickerList);
        mWeelPicker.updateRegionDataList(mRegionList);

        if (!TextUtils.isEmpty(etRegion.getText().toString())) {
            int position = 0;
            for (int i = 0; i < mRegionList.size(); i++) {
                String mRegionName = "";
                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    mRegionName = mRegionList.get(i).region;
                } else {
                    mRegionName = mRegionList.get(i).regionEn;
                }
                if (mRegionName.equalsIgnoreCase(etRegion.getText().toString())) {
                    position = i;
                    break;
                }
            }
            mWeelPicker.updateData(position);
        }


        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            lnrContainer.setScaleX(-1f);
            tvCancel.setScaleX(-1f);
            mWeelPicker.setScaleX(-1f);
            tvDone.setScaleX(-1f);
        } else {
            lnrContainer.setScaleX(1f);
            tvCancel.setScaleX(1f);
            mWeelPicker.setScaleX(1f);
            tvDone.setScaleX(1f);
        }

        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lnrContainer.startAnimation(animClose);
                    }
                }, 10);

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlMain.performClick();
            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mRegionList != null && mRegionList.size() > 0) {

                    int position = mWeelPicker.getCurrentItemPosition();

                    String mRegionName = "";
                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                        mRegionName = mRegionList.get(position).region;
                    } else {
                        mRegionName = mRegionList.get(position).regionEn;
                    }

                    etRegion.setText("" + mRegionName);
                    etCity.setText("");
                    tvInvalidCity.setVisibility(View.GONE);

                    mStateID = mRegionList.get(position).id;

//                    getCityList();

                    rlMain.performClick();
                }

            }
        });

        animClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    }
                }, 10);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        lnrContainer.startAnimation(animOpen);
        dialog.show();

    }

    private void goMapActivity() {
        if (!Utils.isLocationEnabled(getActivity())) {
            Utils.showAlertOkCancelDialog(getActivity(), getString(R.string.enable_location), getString(R.string.settings), new Utils.OnAlertOkayDialogClick() {
                @Override
                public void onOkayClicked(Dialog dialog) {
                    try {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    } catch (Exception e) {

                    }
                }
            });
            return;
        }

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }
            });
            return;
        }
        IntentHandler.startActivity(getActivity(), AddressMapActivity.class, RC_ADDRESS_CITY);
//        IntentHandler.startActivity(getActivity(), AddressMap2Activity.class, RC_ADDRESS_CITY);
    }


    private void showCountryDialog() {

//        final Dialog dialog = new Dialog(new ContextThemeWrapper(getActivity(), R.style.DialogSlideAnim));
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_country_list);

        final RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.rlMain);
        final LinearLayout lnrContainer = (LinearLayout) dialog.findViewById(R.id.lnrContainer);
        final TextView tvCountry = (TextView) dialog.findViewById(R.id.tvCountry);
        final TextView tvDone = (TextView) dialog.findViewById(R.id.tvDone);

        final WheelCountryPicker pickerCountry = (WheelCountryPicker) dialog.findViewById(R.id.pickerCountry);
//        pickerCountry.updateCountry(MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_POSITION, 0) - 1);
        pickerCountry.updateCountryList(mCountryList);
        for (int i = 0; i < mCountryList.size(); i++) {
            if (mCountryList.get(i).id.equalsIgnoreCase(MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_ID, "SA"))) {
                pickerCountry.updateCountry(i);
            }
        }


        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            lnrContainer.setScaleX(-1f);
            tvCountry.setScaleX(-1f);
            pickerCountry.setScaleX(-1f);
            tvDone.setScaleX(-1f);
        } else {
            lnrContainer.setScaleX(1f);
            tvCountry.setScaleX(1f);
            pickerCountry.setScaleX(1f);
            tvDone.setScaleX(1f);
        }

        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lnrContainer.startAnimation(animClose);
                    }
                }, 10);

            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = pickerCountry.getCurrentItemPosition();

                if (mCountryList.get(position).id.equals(mInCountryId)) {
                    return;
                }

                if (mCountryList.get(position).id.equalsIgnoreCase("SA")) {

                    mInCountryPosition = 1;
                    mInCountryCode = "966";

                } else if (mCountryList.get(position).id.equalsIgnoreCase("AE")) {

                    mInCountryPosition = 2;
                    mInCountryCode = "971";

                } else if (mCountryList.get(position).id.equalsIgnoreCase("QA")) {

                    mInCountryPosition = 3;
                    mInCountryCode = "974";

                } else if (mCountryList.get(position).id.equalsIgnoreCase("KW")) {

                    mInCountryPosition = 4;
                    mInCountryCode = "965";

                } else if (mCountryList.get(position).id.equalsIgnoreCase("OM")) {

                    mInCountryPosition = 5;
                    mInCountryCode = "968";

                } else if (mCountryList.get(position).id.equalsIgnoreCase("BH")) {

                    mInCountryPosition = 5;
                    mInCountryCode = "973";

                }

                mInCountryId = "" + mCountryList.get(position).id;
                mInCurrencyCode = "" + mCountryList.get(position).currencyEn;
                mInCurrencyArCode = "" + mCountryList.get(position).currencyAr;
                mInExchangeRate = mCountryList.get(position).exchangeRate;

                etFullName.setText("");
                etLastName.setText("");
                etPhoneNumber.setText("");
                etEmail.setText("");
                etAddress.setText("");
                etCity.setText("");
                etCityOther.setText("");
                etCityOther.setVisibility(View.GONE);

//                switch (pickerCountry.getCurrentItemPosition()) {
//                    case 0:
//                        mInCountryPosition = 1;
//                        mInCountryCode = "966";
//                        mInCountryId = "SA";
//                        mInCurrencyCode = "SAR";
//                        mInExchangeRate = 1f;
//
//                        if (mExchangeRateList != null && mExchangeRateList.size() > 0) {
//                            for (int i = 0; i < mExchangeRateList.size(); i++) {
//                                if (mExchangeRateList.get(i).currencyTo.equalsIgnoreCase("SAR")) {
//                                    mInExchangeRate = mExchangeRateList.get(i).rate;
//                                    break;
//                                }
//                            }
//                        }
//
//                        break;
//                    case 1:
//                        mInCountryPosition = 2;
//                        mInCountryCode = "971";
//                        mInCountryId = "AE";
//                        mInCurrencyCode = "AED";
//                        mInExchangeRate = 0.9789f;
//
//                        if (mExchangeRateList != null && mExchangeRateList.size() > 0) {
//                            for (int i = 0; i < mExchangeRateList.size(); i++) {
//                                if (mExchangeRateList.get(i).currencyTo.equalsIgnoreCase("AED")) {
//                                    mInExchangeRate = mExchangeRateList.get(i).rate;
//                                    break;
//                                }
//                            }
//                        }
//
//                        break;
//                    case 2:
//                        mInCountryPosition = 3;
//                        mInCountryCode = "974";
//                        mInCountryId = "QA";
//                        mInCurrencyCode = "QAR";
//                        mInExchangeRate = 0.971f;
//
//                        if (mExchangeRateList != null && mExchangeRateList.size() > 0) {
//                            for (int i = 0; i < mExchangeRateList.size(); i++) {
//                                if (mExchangeRateList.get(i).currencyTo.equalsIgnoreCase("QAR")) {
//                                    mInExchangeRate = mExchangeRateList.get(i).rate;
//                                    break;
//                                }
//                            }
//                        }
//
//                        break;
//                    case 3:
//                        mInCountryPosition = 4;
//                        mInCountryCode = "965";
//                        mInCountryId = "KW";
//                        mInCurrencyCode = "KWD";
//                        mInExchangeRate = 0.0809f;
//
//                        if (mExchangeRateList != null && mExchangeRateList.size() > 0) {
//                            for (int i = 0; i < mExchangeRateList.size(); i++) {
//                                if (mExchangeRateList.get(i).currencyTo.equalsIgnoreCase("KWD")) {
//                                    mInExchangeRate = mExchangeRateList.get(i).rate;
//                                    break;
//                                }
//                            }
//                        }
//
//                        break;
//                    case 4:
//                        mInCountryPosition = 5;
//                        mInCountryCode = "968";
//                        mInCountryId = "OM";
//                        mInCurrencyCode = "OMR";
//                        mInExchangeRate = 0.1026f;
//
//                        if (mExchangeRateList != null && mExchangeRateList.size() > 0) {
//                            for (int i = 0; i < mExchangeRateList.size(); i++) {
//                                if (mExchangeRateList.get(i).currencyTo.equalsIgnoreCase("OMR")) {
//                                    mInExchangeRate = mExchangeRateList.get(i).rate;
//                                    break;
//                                }
//                            }
//                        }
//
//                        break;
//                    case 5:
//                        mInCountryPosition = 6;
//                        mInCountryCode = "973";
//                        mInCountryId = "BH";
//                        mInCurrencyCode = "BHD";
//                        mInExchangeRate = 0.1005f;
//
//                        if (mExchangeRateList != null && mExchangeRateList.size() > 0) {
//                            for (int i = 0; i < mExchangeRateList.size(); i++) {
//                                if (mExchangeRateList.get(i).currencyTo.equalsIgnoreCase("BHD")) {
//                                    mInExchangeRate = mExchangeRateList.get(i).rate;
//                                    break;
//                                }
//                            }
//                        }
//
//                        break;
//                }
//
//                String mCode = "";
//                if (mInCurrencyCode.equalsIgnoreCase("SAR")) {
//                    mCode = getResources().getString(R.string.SAR);
//                } else if (mInCurrencyCode.equalsIgnoreCase("AED")) {
//                    mCode = getResources().getString(R.string.AED);
//                } else if (mInCurrencyCode.equalsIgnoreCase("QAR")) {
//                    mCode = getResources().getString(R.string.QAR);
//                } else if (mInCurrencyCode.equalsIgnoreCase("OMR")) {
//                    mCode = getResources().getString(R.string.OMR);
//                } else if (mInCurrencyCode.equalsIgnoreCase("KWD")) {
//                    mCode = getResources().getString(R.string.KWD);
//                } else if (mInCurrencyCode.equalsIgnoreCase("BHD")) {
//                    mCode = getResources().getString(R.string.BHD);
//                }
//
//                LogUtils.e("", "mInCurrencyCode::" + mInCurrencyCode + " mCode::" + mCode);
                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    MyPref.setPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "" + mInCurrencyArCode);
                } else {
                    MyPref.setPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "" + mInCurrencyCode);
                }
                MyPref.setPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "" + mInCurrencyCode);
                MyPref.setPref(getActivity(), MyPref.DEFAULT_ARABIC_CURRENCY_CODE, "" + mInCurrencyArCode);
                MyPref.setPref(getActivity(), MyPref.DEFAULT_EXCHANGE_RATE, mInExchangeRate);

                MyPref.setPref(getActivity(), MyPref.DEFAULT_COUNTRY_POSITION, mInCountryPosition);
                MyPref.setPref(getActivity(), MyPref.DEFAULT_COUNTRY_CODE, "" + mInCountryCode);
                MyPref.setPref(getActivity(), MyPref.DEFAULT_COUNTRY_ID, "" + mInCountryId);

                etRegion.setText("");
                etCity.setText("");

                setCountryConfig();

                getShippingTotalAmount();

                rlMain.performClick();
            }
        });

        animClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    }
                }, 10);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        lnrContainer.startAnimation(animOpen);
        dialog.show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            LogUtils.i("", "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                // Request permission
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_PERMISSIONS_REQUEST_CODE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

        } else {
            LogUtils.i("", "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        LogUtils.i("", "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i("", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goMapActivity();
            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }


    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(getActivity().findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private void showViewSelection(int i) {
//        if (mSelectedPaymentPosition != i) {
//            mSelectedPaymentPosition = i;
//        } else {
//            return;
//        }

        lnrBankTransfer.setBackgroundColor(getResources().getColor(R.color.white));
        lnrCashOnDelivery.setBackgroundColor(getResources().getColor(R.color.white));
        lnrCreditDebitCard.setBackgroundColor(getResources().getColor(R.color.white));

        tvBankDescription.setVisibility(View.GONE);
        tvCODDescription.setVisibility(View.GONE);
        tvCardDescription.setVisibility(View.GONE);

        cbCreditDebitCard.setChecked(false);
        cbCashOnDelivery.setChecked(false);
        cbBankTransfer.setChecked(false);

        switch (i) {
            case 1:

                cbCreditDebitCard.setChecked(true);
                lnrCreditDebitCard.setBackgroundColor(getResources().getColor(R.color.yellow_delivery));

                if (mPaymentMethodList != null && mPaymentMethodList.size() > 0) {
                    for (int j = 0; j < mPaymentMethodList.size(); j++) {
                        if (mPaymentMethodList.get(j).getCode().contains("payfort_fort_cc")
                                || mPaymentMethodList.get(j).getTitle().contains("Card")
                                || mPaymentMethodList.get(j).getTitle().contains("البطاقة الائت")) {
                            mSelectedPaymentCode = "" + mPaymentMethodList.get(j).getCode();
                            mSelectedPaymentMethod = "" + mPaymentMethodList.get(j).getTitle();
                            break;
                        }
                    }
                }

                tvCardDescription.setVisibility(View.VISIBLE);

                break;
            case 2:
                cbCashOnDelivery.setChecked(true);
                lnrCashOnDelivery.setBackgroundColor(getResources().getColor(R.color.yellow_delivery));

                if (mPaymentMethodList != null && mPaymentMethodList.size() > 0) {
                    for (int j = 0; j < mPaymentMethodList.size(); j++) {
                        if (mPaymentMethodList.get(j).getCode().contains("msp_cashondelivery")
                                || mPaymentMethodList.get(j).getTitle().contains("Cash")
                                || mPaymentMethodList.get(j).getTitle().contains("الدفع نقدًا عند الاستل")) {
                            mSelectedPaymentCode = "" + mPaymentMethodList.get(j).getCode();
                            mSelectedPaymentMethod = "" + mPaymentMethodList.get(j).getTitle();
                            break;
                        }
                    }
                }

                tvCODDescription.setVisibility(View.VISIBLE);

                break;
            case 3:
                cbBankTransfer.setChecked(true);
                lnrBankTransfer.setBackgroundColor(getResources().getColor(R.color.yellow_delivery));

                if (mPaymentMethodList != null && mPaymentMethodList.size() > 0) {
                    for (int j = 0; j < mPaymentMethodList.size(); j++) {
                        if (mPaymentMethodList.get(j).getCode().contains("banktransfer")
                                || mPaymentMethodList.get(j).getTitle().contains("Bank")
                                || mPaymentMethodList.get(j).getTitle().contains("حوالة بنكية")) {
                            mSelectedPaymentCode = "" + mPaymentMethodList.get(j).getCode();
                            mSelectedPaymentMethod = "" + mPaymentMethodList.get(j).getTitle();
                            break;
                        }
                    }
                }

                tvBankDescription.setVisibility(View.VISIBLE);

                break;
        }

        setPaymentMethod();
    }


    private void showLayoutView1() {

        tvTitle.setText("" + getString(R.string.shipping_delivery));

        lnrShipment.setVisibility(View.VISIBLE);
        lnrReviewPay.setVisibility(View.GONE);
        tvProceedPayment.setVisibility(View.VISIBLE);
        tvBuyNow.setVisibility(View.GONE);

        svMain.fullScroll(ScrollView.FOCUS_UP);

        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.setFirebaseLog("Checkout_address");
        }
    }

    private void showLayoutView2() {

        tvTitle.setText("" + getString(R.string.review_payment));

        lnrShipment.setVisibility(View.GONE);
//        lnrReviewPay.setVisibility(View.VISIBLE);
        tvProceedPayment.setVisibility(View.GONE);
        tvBuyNow.setVisibility(View.VISIBLE);

        svMain.fullScroll(ScrollView.FOCUS_UP);

        getPaymentMethod();


    }

    private void validateCouponCode(final String couponCode) {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        validateCouponCode(couponCode);

                    }
                }
            });
            return;
        }

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("coupon", "" + couponCode);

        String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
        if (isGuestUser) {

            jsonParams.put("cart_id", guestToken);
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        Call<JsonElement> callCode;
        if (isGuestUser) {
            callCode = apiService.validateGuestCouponCode(Shy7lo.mLangCode, body, guestToken);
        } else {
            String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
            callCode = apiService.validateUserCouponCode(Shy7lo.mLangCode, "Bearer " + userToken, body);
        }


        callCode.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jResponse = new JSONObject(response.body().toString());
                        if (jResponse != null && jResponse.getString("success").equals("1")) {
                            if (jResponse.getString("data").contains("true")) {
                                etCouponCode.setEnabled(false);
                                btnValidateCoupon.setVisibility(View.GONE);
                                btnDeleteCoupon.setVisibility(View.VISIBLE);

                                MyPref.setPref(getActivity(), MyPref.USER_COUPON_CODE, etCouponCode.getText().toString().trim());

                                getTotalAmount();
                                if (getActivity() instanceof HomeActivity) {
                                    HomeActivity activity = (HomeActivity) getActivity();
                                    activity.setFirebaseLog("Coupon_applied");
                                }
                            }

                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
                            Utils.showInitialScreen(getActivity());
                            if (getActivity() instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) getActivity();
                                activity.loadShoppingCartsWithClearStack();
                            }
                            return;
                        } else {
                            Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_valid_coupon_code));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                    if (response.code() == 404) {
//                        Utils.showToast(getActivity(), response.code() + "" + getResources().getString(R.string.msg_valid_coupon_code));
//                    }
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
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Utils.showAlertDialog(getActivity(), "" + t.getMessage());
            }
        });

    }

//    private void getCouponCode() {
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
//
//        Call<JsonElement> callCode;
//        if (isGuestUser) {
//            String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
//            callCode = apiService.getGuestCouponCode(Shy7lo.mLangCode, guestToken);
//        } else {
//            String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
//            callCode = apiService.getUserCouponCode(Shy7lo.mLangCode, "Bearer " + userToken);
//        }
//
//
//        callCode.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//
//                if (response.isSuccessful()) {
//                    try {
//
//                        JSONObject jResponse = new JSONObject(response.body().toString());
//                        if (jResponse != null && jResponse.getString("success").equals("1")) {
//                            String mCouponCode = jResponse.getJSONObject("data").getString("coupon_code");
//                            if (!TextUtils.isEmpty(mCouponCode) && !mCouponCode.equals("[]")) {
//                                etCouponCode.setText("" + mCouponCode);
//                                etCouponCode.setEnabled(false);
//                                btnValidateCoupon.setVisibility(View.GONE);
//                                btnDeleteCoupon.setVisibility(View.VISIBLE);
//                                lnrCoupon.setVisibility(View.VISIBLE);
//                            }
//
//                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
//                            Utils.showInitialScreen(getActivity());
//                            if (getActivity() instanceof HomeActivity) {
//                                HomeActivity activity = (HomeActivity) getActivity();
//                                activity.loadShoppingCartsWithClearStack();
//                            }
//                            return;
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                getReviewOrderList();
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                getReviewOrderList();
//            }
//        });
//
//    }

    private void deleteCouponCode() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        deleteCouponCode();

                    }
                }
            });
            return;
        }

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        Call<JsonElement> callCode;
        if (isGuestUser) {
            String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
            callCode = apiService.deleteGuestCouponCode(Shy7lo.mLangCode, guestToken);
        } else {
            String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
            callCode = apiService.deleteUserCouponCode(Shy7lo.mLangCode, "Bearer " + userToken);
        }

        callCode.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                if (response.isSuccessful()) {
                    try {

                        JSONObject jResponse = new JSONObject(response.body().toString());
                        if (jResponse != null && jResponse.getString("success").equals("1")) {
                            if (jResponse.getString("data").contains("true")) {
                                etCouponCode.setText("");
                                etCouponCode.setEnabled(true);
                                btnValidateCoupon.setVisibility(View.GONE);
                                btnDeleteCoupon.setVisibility(View.GONE);
                                MyPref.setPref(getActivity(), MyPref.USER_COUPON_CODE, "");
                                getTotalAmount();
                            }

                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
                            Utils.showInitialScreen(getActivity());
                            if (getActivity() instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) getActivity();
                                activity.loadShoppingCartsWithClearStack();
                            }
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                    Utils.showAlertDialog(getActivity(), "" + response.code());
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
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Utils.showAlertDialog(getActivity(), "" + t.getMessage());
            }
        });

    }

    private void submitAddress() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        submitAddress();

                    }
                }
            });
            return;
        }

        Utils.showProgressDialog(getActivity());

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("firstname", etFullName.getText().toString().trim());
        jsonParams.put("lastname", etLastName.getText().toString().trim());
//        jsonParams.put("email", userEmail);
        jsonParams.put("email", etEmail.getText().toString());
        jsonParams.put("street", etAddress.getText().toString().trim());
        jsonParams.put("region", ""); //Alyasmeen
        jsonParams.put("city", isOtherCity(etCity.getText().toString().trim()) ? etCityOther.getText().toString().trim() : etCity.getText().toString().trim());
        jsonParams.put("country", "" + mCountryID);
        jsonParams.put("phone", mCountryCode + "" + etPhoneNumber.getText().toString().trim());

        String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
        if (isGuestUser) {
            jsonParams.put("cart_id", guestToken);
        }

        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        Call<JsonElement> addressCall;
        if (isGuestUser) {
            addressCall = serviceAPI.submitGuestAddress(Shy7lo.mLangCode, body);
        } else {
            String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
            addressCall = serviceAPI.submitUserAddress(Shy7lo.mLangCode, "Bearer " + userToken, body);
        }

        addressCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    JsonElement jsonElement = response.body();

                    LogUtils.e("", "Response is great::" + jsonElement);

                    MyPref.setPref(getActivity(), Constant.BNDL_USER_FULLNAME, "" + etFullName.getText().toString().trim());
                    MyPref.setPref(getActivity(), Constant.BNDL_USER_LASTNAME, "" + etLastName.getText().toString().trim());
                    MyPref.setPref(getActivity(), Constant.BNDL_USER_EMAIL, "" + etEmail.getText().toString().trim());
                    MyPref.setPref(getActivity(), Constant.BNDL_USER_CONTACT_NUMBER, "" + etPhoneNumber.getText().toString().trim());
                    MyPref.setPref(getActivity(), Constant.BNDL_USER_ADDRESS, "" + etAddress.getText().toString().trim());
                    MyPref.setPref(getActivity(), Constant.BNDL_USER_REGION, "");
                    MyPref.setPref(getActivity(), Constant.BNDL_USER_REGION_ID, "" + mStateID);
//                    MyPref.setPref(getActivity(), Constant.BNDL_USER_NEIGHBERHOOD, "" + etNeighborhood.getText().toString().trim());


                    if (mCountryCode.contains("966")) {

//                        if (mRegionList != null && mRegionList.size() > 0) {
//                            for (int i = 0; i < mRegionList.size(); i++) {
//                                if (mRegionList.get(i).id.equals(mStateID)) {
//                                    MyPref.setPref(getActivity(), Constant.BNDL_USER_REGION_AR, "" + mRegionList.get(i).region.trim());
//                                    MyPref.setPref(getActivity(), Constant.BNDL_USER_REGION, "" + mRegionList.get(i).regionEn.trim());
//                                    break;
//                                }
//                            }
//                        }

                        if (isOtherCity(etCity.getText().toString().trim())) {

                            MyPref.setPref(getActivity(), Constant.BNDL_USER_OTHER_CITY, "" + etCityOther.getText().toString().trim());
                            MyPref.setPref(getActivity(), Constant.BNDL_USER_CITY_AR, "");
                            MyPref.setPref(getActivity(), Constant.BNDL_USER_CITY, "Other");

                        } else {
                            if (mCityList != null && mCityList.size() > 0) {
                                LogUtils.e("", "City:" + etCity.getText().toString());
                                for (int i = 0; i < mCityList.size(); i++) {
                                    LogUtils.e("", mCityList.get(i).cityEn + " Arabic:" + mCityList.get(i).cityEn.equals(etCity.getText().toString()));
                                    LogUtils.e("", mCityList.get(i).city + " English:" + mCityList.get(i).city.equals(etCity.getText().toString()));
                                    if (mCityList.get(i).cityEn.equals(etCity.getText().toString()) || mCityList.get(i).city.equals(etCity.getText().toString())) {
                                        LogUtils.e("", "Position for cty:" + i);
                                        MyPref.setPref(getActivity(), Constant.BNDL_USER_CITY_AR, "" + mCityList.get(i).city.trim());
                                        MyPref.setPref(getActivity(), Constant.BNDL_USER_CITY, "" + mCityList.get(i).cityEn.trim());
                                        break;
                                    }
                                }

                                LogUtils.e("", "BNDL_USER_CITY_AR::" + MyPref.getPref(getActivity(), Constant.BNDL_USER_CITY_AR, ""));
                                LogUtils.e("", "BNDL_USER_CITY::" + MyPref.getPref(getActivity(), Constant.BNDL_USER_CITY, ""));

                                LogUtils.e("", "isOtherCity(etCity.getText().toString().trim())::" + isOtherCity(etCity.getText().toString().trim()) + " " + etCity.getText().toString().trim());

                                LogUtils.e("", "Other City::" + MyPref.getPref(getActivity(), Constant.BNDL_USER_OTHER_CITY, ""));
                            }
                        }

                    } else {
                        MyPref.setPref(getActivity(), Constant.BNDL_USER_CITY, "" + etCity.getText().toString().trim());
                        MyPref.setPref(getActivity(), Constant.BNDL_USER_CITY_AR, "" + etCity.getText().toString().trim());
//                        MyPref.setPref(getActivity(), Constant.BNDL_USER_REGION, "" + etRegion.getText().toString().trim());
//                        MyPref.setPref(getActivity(), Constant.BNDL_USER_REGION_AR, "" + etRegion.getText().toString().trim());
                    }

                    LogUtils.e("", "City Submit:" + MyPref.getPref(getActivity(), Constant.BNDL_USER_CITY, ""));
                    LogUtils.e("", "City AR Submit:" + MyPref.getPref(getActivity(), Constant.BNDL_USER_CITY_AR, ""));

                    try {
                        AdjustEvent event = new AdjustEvent("1umt1q");
                        event.addPartnerParameter("Name", "" + "" + etFullName.getText().toString() + " " + etLastName.getText().toString());
                        event.addPartnerParameter("Email", "" + "" + etEmail.getText().toString().trim());
                        event.addPartnerParameter("Country Code", "" + "" + mCountryCode);
                        event.addPartnerParameter("Mobile No", "" + "" + etPhoneNumber.getText().toString().trim());
                        event.addPartnerParameter("Address", "" + "" + etAddress.getText().toString().trim());
                        event.addPartnerParameter("City", "" + "" + (isOtherCity(etCity.getText().toString().trim()) ? etCityOther.getText().toString().trim() : etCity.getText().toString().trim()));
                        event.addPartnerParameter("Region", "" + "");

                        //callback
                        event.addCallbackParameter("Name", "" + "" + etFullName.getText().toString() + " " + etLastName.getText().toString());
                        event.addCallbackParameter("Email", "" + "" + etEmail.getText().toString().trim());
                        event.addCallbackParameter("Country Code", "" + "" + mCountryCode);
                        event.addCallbackParameter("Mobile No", "" + "" + etPhoneNumber.getText().toString().trim());
                        event.addCallbackParameter("Address", "" + "" + etAddress.getText().toString().trim());
                        event.addCallbackParameter("City", "" + "" + (isOtherCity(etCity.getText().toString().trim()) ? etCityOther.getText().toString().trim() : etCity.getText().toString().trim()));
                        event.addCallbackParameter("Region", "");

                        String mCriteo = MyPref.getPref(getActivity(), MyPref.APP_CRITEO, "");
                        if (TextUtils.isEmpty(mCriteo) || mCriteo.equalsIgnoreCase("1")) {
                            String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
                            AdjustCriteo.injectCustomerIdIntoCriteoEvents(TextUtils.isEmpty(userToken) ? userToken : MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""));
                        }

                        Adjust.trackEvent(event);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    showLayoutView2();

                    if (getActivity() instanceof HomeActivity) {
                        HomeActivity activity = (HomeActivity) getActivity();
                        activity.setFirebaseLog("Checkout_payment");
                    }

                } else {
                    LogUtils.e("", "Response is not great");
//                    Utils.showAlertDialog(getActivity(), "" + response.code());
//                    Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
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
            public void onFailure(Call<JsonElement> call, Throwable t) {
                System.out.println(t.getMessage());
                Utils.closeProgressDialog();
//                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
                Utils.showAlertDialog(getActivity(), "" + t.getMessage());
            }
        });

        if (lnrSaveAddress.getVisibility() == View.VISIBLE && cbSaveAddress.isChecked() && !isGuestUser) {
            addAddress();
        }

    }

    private void addAddress() {

        if (!Utils.isInternetConnected(getActivity())) {
            return;
        }

        ApiInterface serviceAPI =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("firstname", "" + etFullName.getText().toString());
        jsonParams.put("lastname", "" + etLastName.getText().toString());
        jsonParams.put("street", "" + etAddress.getText().toString());
        jsonParams.put("city", "" + (isOtherCity(etCity.getText().toString().trim()) ? etCityOther.getText().toString().trim() : etCity.getText().toString().trim()));
        jsonParams.put("postcode", "");
        jsonParams.put("country_id", "" + mInCountryId);
        jsonParams.put("default_shipping", "" + ((mAddressList.size() < 1) ? true : false));
        jsonParams.put("default_billing", "" + ((mAddressList.size() < 1) ? true : false));
        jsonParams.put("telephone", etPhoneNumber.getText().toString().trim());

        if (!TextUtils.isEmpty(tvCountryCode.getText().toString()) && tvCountryCode.getText().toString().contains("966")) {
            jsonParams.put("region", "" + etRegion.getText().toString());
            jsonParams.put("region_id", "" + mStateID);
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        Call<ApiResponse> call = serviceAPI.addAddress(Shy7lo.mLangCode, "Bearer " + userToken, body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }

    private void getPaymentMethod() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getPaymentMethod();

                    }
                }
            });
            return;
        }

        isBankTransfer = false;
        isCreditCard = false;
        isCashOnDelivery = false;

        Utils.showProgressDialog(getActivity());

        String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<PaymentMethodRespnose> callCode;
        if (isGuestUser) {
            callCode = apiService.getGuestPaymentMethod(Shy7lo.mLangCode, BuildConfig.VERSION_NAME, guestToken);
        } else {
            callCode = apiService.getUserPaymentMethod(Shy7lo.mLangCode, BuildConfig.VERSION_NAME, "Bearer " + userToken);
        }


        callCode.enqueue(new Callback<PaymentMethodRespnose>() {
            @Override
            public void onResponse(Call<PaymentMethodRespnose> call, Response<PaymentMethodRespnose> response) {

                if (response.isSuccessful()) {
                    try {
                        PaymentMethodRespnose paymentMethodRespnose = response.body();

                        if (paymentMethodRespnose != null && paymentMethodRespnose.success.equals("1")) {

                            mPaymentMethodList = paymentMethodRespnose.getData().getPaymentMethods();

                            if (mPaymentMethodList != null && mPaymentMethodList.size() > 0) {
                                for (int j = 0; j < mPaymentMethodList.size(); j++) {

                                    if (mPaymentMethodList.get(j).getCode().contains("banktransfer")
                                            || mPaymentMethodList.get(j).getTitle().contains("Bank")
                                            || mPaymentMethodList.get(j).getTitle().contains("حوالة بنكية")) {

//                                        tvBankDescription.setText(mPaymentMethodList.get(j).getDescription());
                                        tvBankDescription.setText(Html.fromHtml(mPaymentMethodList.get(j).getDescription()));

//                                        String prefix = "<h3>";
//                                        String postfix = "</h3>";
//                                        String myHtmlString = prefix + mPaymentMethodList.get(j).getDescription() + postfix;
//                                        tvBankDescription.setText(Html.fromHtml(myHtmlString));

                                        if (mPaymentMethodList.get(j).info != null && !TextUtils.isEmpty(mPaymentMethodList.get(j).info.msg)) {
//                                            tvBankTransfer.setText(getResources().getString(R.string.bank_transfer) + "  " + mPaymentMethodList.get(j).info.msg, TextView.BufferType.SPANNABLE);
                                            tvBankTransfer.setText(mPaymentMethodList.get(j).getTitle() + "  " + mPaymentMethodList.get(j).info.msg, TextView.BufferType.SPANNABLE);
                                            Spannable s = (Spannable) tvBankTransfer.getText();
                                            int start = getResources().getString(R.string.bank_transfer).length() + 2;
                                            int end = start + mPaymentMethodList.get(j).info.msg.length();
                                            LogUtils.e("", "banktransfer::" + start + " end:" + end);

                                            if (!TextUtils.isEmpty(mPaymentMethodList.get(j).info.color)) {
                                                s.setSpan(new ForegroundColorSpan(Color.parseColor(mPaymentMethodList.get(j).info.color)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            }

                                            if (!TextUtils.isEmpty(mPaymentMethodList.get(j).info.type)) {
                                                if (mPaymentMethodList.get(j).info.type.equalsIgnoreCase("bold")) {
                                                    s.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                } else {
                                                    s.setSpan(new StyleSpan(Typeface.NORMAL), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                }
                                            }
                                        } else {
                                            tvBankTransfer.setText(mPaymentMethodList.get(j).getTitle());
                                        }
                                        isBankTransfer = true;
                                    } else if (mPaymentMethodList.get(j).getCode().contains("payfort_fort_cc")
                                            || mPaymentMethodList.get(j).getTitle().contains("Credit")
                                            || mPaymentMethodList.get(j).getTitle().contains("البطاقة الائت")) {

                                        if (mPaymentMethodList.get(j).info != null && !TextUtils.isEmpty(mPaymentMethodList.get(j).info.msg)) {
                                            tvCreditDebitCard.setText(mPaymentMethodList.get(j).getTitle() + "  " + mPaymentMethodList.get(j).info.msg, TextView.BufferType.SPANNABLE);
                                            Spannable s = (Spannable) tvCreditDebitCard.getText();
                                            int start = getResources().getString(R.string.credit_debit_card).length() + 2;
                                            int end = start + mPaymentMethodList.get(j).info.msg.length();
                                            LogUtils.e("", "Credit::" + start + " end:" + end);
                                            if (!TextUtils.isEmpty(mPaymentMethodList.get(j).info.color)) {
                                                s.setSpan(new ForegroundColorSpan(Color.parseColor(mPaymentMethodList.get(j).info.color)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            }

                                            if (!TextUtils.isEmpty(mPaymentMethodList.get(j).info.type)) {
                                                if (mPaymentMethodList.get(j).info.type.equalsIgnoreCase("bold")) {
                                                    s.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                } else {
                                                    s.setSpan(new StyleSpan(Typeface.NORMAL), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                }
                                            }
                                        } else {
                                            tvCreditDebitCard.setText(mPaymentMethodList.get(j).getTitle());
                                        }
//                                        tvCardDescription.setText(mPaymentMethodList.get(j).getDescription());
                                        tvCardDescription.setText(Html.fromHtml(mPaymentMethodList.get(j).getDescription()));
                                        isCreditCard = true;
                                    } else if (mPaymentMethodList.get(j).getCode().contains("msp_cashondelivery")
                                            || mPaymentMethodList.get(j).getTitle().contains("Cash")
                                            || mPaymentMethodList.get(j).getTitle().contains("الدفع نقدًا عند الاستل")) {

                                        if (mPaymentMethodList.get(j).info != null && !TextUtils.isEmpty(mPaymentMethodList.get(j).info.msg)) {
                                            tvCashOnDelivery.setText(mPaymentMethodList.get(j).getTitle() + "  " + mPaymentMethodList.get(j).info.msg, TextView.BufferType.SPANNABLE);
                                            Spannable s = (Spannable) tvCashOnDelivery.getText();
                                            int start = getResources().getString(R.string.cash_delivery).length() + 2;
                                            int end = start + mPaymentMethodList.get(j).info.msg.length();
                                            LogUtils.e("", "Cash::" + start + " end:" + end);

                                            if (!TextUtils.isEmpty(mPaymentMethodList.get(j).info.color)) {
                                                s.setSpan(new ForegroundColorSpan(Color.parseColor(mPaymentMethodList.get(j).info.color)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            }

                                            if (!TextUtils.isEmpty(mPaymentMethodList.get(j).info.type)) {
                                                if (mPaymentMethodList.get(j).info.type.equalsIgnoreCase("bold")) {
                                                    s.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                } else {
                                                    s.setSpan(new StyleSpan(Typeface.NORMAL), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                }
                                            }
                                        } else {
                                            tvCashOnDelivery.setText(mPaymentMethodList.get(j).getTitle());
                                        }
//                                        tvCODDescription.setText(mPaymentMethodList.get(j).getDescription());
                                        tvCODDescription.setText(Html.fromHtml(mPaymentMethodList.get(j).getDescription()));
                                        isCashOnDelivery = true;
                                    }
                                }

                                tvBankDescription.setVisibility(View.GONE);
                                tvCODDescription.setVisibility(View.GONE);
                                tvCardDescription.setVisibility(View.GONE);

                                if (isBankTransfer) {
                                    lnrBankTransfer.setVisibility(View.VISIBLE);
                                } else {
                                    lnrBankTransfer.setVisibility(View.GONE);
                                }

                                if (isCreditCard) {
                                    lnrCreditDebitCard.setVisibility(View.VISIBLE);
                                } else {
                                    lnrCreditDebitCard.setVisibility(View.GONE);
                                }


                                if (isCashOnDelivery && !TextUtils.isEmpty(mCountryID) && mCountryID.equalsIgnoreCase("SA")) {
                                    lnrCashOnDelivery.setVisibility(View.VISIBLE);
                                } else {
                                    lnrCashOnDelivery.setVisibility(View.GONE);
                                }

                                LogUtils.e("", "Payment comes");
                                lnrPaymentOption.setVisibility(View.VISIBLE);


                            } else {
                                lnrPaymentOption.setVisibility(View.GONE);
                            }


                        } else if (paymentMethodRespnose != null && paymentMethodRespnose.success.equals("0")) {
                            Utils.showToast(getActivity(), "" + paymentMethodRespnose.getMessage());

                        } else if (paymentMethodRespnose != null && paymentMethodRespnose.success.equals("2")) {
                            Utils.showInitialScreen(getActivity());
                            if (getActivity() instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) getActivity();
                                activity.loadShoppingCartsWithClearStack();
                            }
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
//                    Utils.showAlertDialog(getActivity(), "" + response.code());
                    if (String.valueOf(response.code()).startsWith("5")) {
                        Utils.showAlertDialog(getActivity(), "" + response.code() + "-1");
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
//                getCouponCode();
                getReviewOrderList();
            }

            @Override
            public void onFailure(Call<PaymentMethodRespnose> call, Throwable t) {
                Utils.showAlertDialog(getActivity(), "" + t.getMessage());
//                getCouponCode();
                getReviewOrderList();
            }
        });

    }

    private void setPaymentMethod() {

        Utils.showProgressDialog(getActivity());

        String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("payment_method", mSelectedPaymentCode);
        jsonParams.put("firstname", etFullName.getText().toString().trim());
        jsonParams.put("lastname", etLastName.getText().toString().trim());
//        jsonParams.put("email", userEmail);
        jsonParams.put("email", etEmail.getText().toString());
        jsonParams.put("street", etAddress.getText().toString().trim());
        jsonParams.put("region", ""); //Alyasmeen
        jsonParams.put("city", (isOtherCity(etCity.getText().toString().trim()) ? etCityOther.getText().toString().trim() : etCity.getText().toString().trim()));
        jsonParams.put("country_id", "" + mCountryID);
        jsonParams.put("phone", mCountryCode + "" + etPhoneNumber.getText().toString().trim());

        if (isGuestUser) {
            jsonParams.put("cart_id", guestToken);
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        Call<JsonElement> callCode;
        if (isGuestUser) {
            callCode = apiService.setGuestPaymentMethod(Shy7lo.mLangCode, body);
        } else {
            callCode = apiService.setUserPaymentMethod(Shy7lo.mLangCode, "Bearer " + userToken, body);
        }

        callCode.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonResponse = new JSONObject(response.body().toString());
                        if (jsonResponse != null && jsonResponse.getString("success").equals("1")) {
                            getTotalAmount();
                        } else if (jsonResponse != null && jsonResponse.getString("success").equals("0")) {
                            Utils.closeProgressDialog();
                        } else if (jsonResponse != null && jsonResponse.getString("success").equals("2")) {
                            Utils.closeProgressDialog();
                            Utils.showInitialScreen(getActivity());
                            if (getActivity() instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) getActivity();
                                activity.loadShoppingCartsWithClearStack();
                            }
                            return;
                        }

                        if (getActivity() instanceof HomeActivity) {
                            HomeActivity activity = (HomeActivity) getActivity();
                            activity.setFirebaseLog("Payment_selected");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.closeProgressDialog();
                    }
                } else {
//                    Utils.showToast(getActivity(), response.code() + " " + getResources().getString(R.string.msg_something_wrong));
                    if (String.valueOf(response.code()).startsWith("5")) {
                        Utils.showAlertDialog(getActivity(), "" + response.code() + "-4");
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
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Utils.closeProgressDialog();
                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
            }
        });

    }

    private void getShippingTotalAmount() {

        final boolean isProgressShowing = Utils.isProgressDialogShowing();
        if (!isProgressShowing) {
            Utils.showProgressDialog(getActivity());
        }

        Call<JsonElement> callCode;
        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        if (isGuestUser) {
            String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
            callCode = apiService.getGuestTotalAmount(Shy7lo.mLangCode, guestToken);

        } else {
            String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
            callCode = apiService.getUserTotalAmount(Shy7lo.mLangCode, "Bearer " + userToken);
        }

        callCode.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonResponse = new JSONObject(response.body().toString());
                        if (jsonResponse != null && jsonResponse.getString("success").equals("1")) {
//                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            JSONObject jsonObject = jsonResponse.getJSONObject("data");
                            JSONArray jsonArray = null;
                            if (jsonObject != null) {
                                jsonArray = jsonObject.getJSONArray("totals");
//                                String mShippingMsg = jsonObject.getString("shipping_msg");
                                JSONObject mShippingObj = jsonObject.getJSONObject("shipping_msg");
                                if (mShippingObj != null) {
                                    String shippingCost = mShippingObj.getString("shipping_cost");
                                    String title = mShippingObj.getString("title");
                                    String mShippingMsg = mShippingObj.getString("description");

                                    if (!TextUtils.isEmpty(mShippingMsg)) {
                                        LogUtils.e("", "mShippingMsg::" + mShippingMsg);
                                        if (!TextUtils.isEmpty(shippingCost)) {
                                            int shippingPrice = Integer.parseInt(shippingCost);
                                            if (shippingPrice > 0) {
                                                String mTitle = mCurrencyCode + " " + Utils.getIntPrice(mExchangeRate, shippingPrice) + " " + title;
                                                tvStandardDelivery.setText(mTitle);
                                                SpannableStringBuilder sb = new SpannableStringBuilder(tvStandardDelivery.getText().toString());
                                                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, mTitle.indexOf(title) - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                                tvStandardDelivery.setText(sb);
                                            } else {
                                                String mFree = getString(R.string.free);
                                                tvStandardDelivery.setText(mFree + " " + title);
                                                SpannableStringBuilder sb = new SpannableStringBuilder(tvStandardDelivery.getText().toString());
                                                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, mFree.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                                tvStandardDelivery.setText(sb);
                                            }
                                        } else {
                                            tvStandardDelivery.setText(title);
                                            SpannableStringBuilder sb = new SpannableStringBuilder(tvStandardDelivery.getText().toString());
                                            sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                            tvStandardDelivery.setText(sb);
                                        }

                                        tvShippingOptions.setVisibility(View.VISIBLE);
                                        lnrStandardDelivery.setVisibility(View.VISIBLE);

                                        tvStandardDeliveryDetails.setText(mShippingMsg);
                                    }
                                }

                            }

                            if (jsonArray != null && jsonArray.length() > 0) {

                                lnrShippingAmount.removeAllViews();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jObj = jsonArray.getJSONObject(i);

                                    if (jObj != null) {
                                        String title = jObj.getString("title");
                                        String value = jObj.getString("value");

//                                        if (title.contains("Total")) {
                                        if (i == jsonArray.length() - 1) {
//                                            tvTotalTitle.setText("" + title);
////                                            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
////                                                tvTotal.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, Float.parseFloat(value)));
////                                            } else {
//                                            tvTotal.setText(Utils.getRealPrice(mExchangeRate, Float.parseFloat(value)) + " " + mCurrencyCode);
////                                            }
//
                                            mTotalAmount = Utils.getRealPrice(mExchangeRate, Float.parseFloat(value));
//                                            continue;
                                        }

                                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_amount, null);

                                        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                                        TextView tvAmount = (TextView) view.findViewById(R.id.tvAmount);

                                        if (i == jsonArray.length() - 1) {
                                            tvTitle.setTypeface(Shy7lo.DroidKufiBold);
                                            tvAmount.setTypeface(Shy7lo.DroidKufiBold);
                                        } else {
                                            tvTitle.setTypeface(Shy7lo.DroidKufiRegular);
                                            tvAmount.setTypeface(Shy7lo.DroidKufiRegular);
                                        }

                                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                                            tvTitle.setScaleX(-1f);
                                            tvAmount.setScaleX(-1f);
                                            tvTitle.setGravity(Gravity.RIGHT);
                                        } else {
                                            tvTitle.setScaleX(1f);
                                            tvAmount.setScaleX(1f);
                                            tvTitle.setGravity(Gravity.LEFT);
                                        }

                                        tvTitle.setText("" + title);

                                        if (value != null && !TextUtils.isEmpty(value)) {
                                            tvAmount.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, Float.parseFloat(value)));
                                        } else {
                                            tvAmount.setText(mCurrencyCode + " 0");
                                        }

                                        lnrShippingAmount.addView(view);

                                    }
                                }
                            }
                        } else if (jsonResponse != null && jsonResponse.getString("success").equals("0")) {
                            Utils.showToast(getActivity(), "" + jsonResponse.getString("message"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
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
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }

    private void getTotalAmount() {

        final boolean isProgressShowing = Utils.isProgressDialogShowing();
        if (!isProgressShowing) {
            Utils.showProgressDialog(getActivity());
        }

        Call<JsonElement> callCode;
        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        if (isGuestUser) {
            String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
            callCode = apiService.getGuestTotalAmount(Shy7lo.mLangCode, guestToken);

        } else {
            String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
            callCode = apiService.getUserTotalAmount(Shy7lo.mLangCode, "Bearer " + userToken);
        }

        callCode.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                Utils.closeProgressDialog();
                lnrReviewPay.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonResponse = new JSONObject(response.body().toString());
                        if (jsonResponse != null && jsonResponse.getString("success").equals("1")) {
                            JSONObject jsonObject = jsonResponse.getJSONObject("data");
                            JSONArray jsonArray = null;
                            if (jsonObject != null) {
                                jsonArray = jsonObject.getJSONArray("totals");
                            }
                            if (jsonArray != null && jsonArray.length() > 0) {

                                lnrAmount.removeAllViews();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jObj = jsonArray.getJSONObject(i);

                                    if (jObj != null) {
                                        String title = jObj.getString("title");
                                        String value = jObj.getString("value");

                                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_amount, null);

                                        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                                        TextView tvAmount = (TextView) view.findViewById(R.id.tvAmount);

                                        if (i == jsonArray.length() - 1) {
                                            tvTitle.setTypeface(Shy7lo.DroidKufiBold);
                                            tvAmount.setTypeface(Shy7lo.DroidKufiBold);

                                            mTotalAmount = Utils.getRealPrice(mExchangeRate, Float.parseFloat(value));

                                        } else {
                                            tvTitle.setTypeface(Shy7lo.DroidKufiRegular);
                                            tvAmount.setTypeface(Shy7lo.DroidKufiRegular);
                                        }

                                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                                            tvTitle.setScaleX(-1f);
                                            tvAmount.setScaleX(-1f);
                                            tvTitle.setGravity(Gravity.RIGHT);
                                        } else {
                                            tvTitle.setScaleX(1f);
                                            tvAmount.setScaleX(1f);
                                            tvTitle.setGravity(Gravity.LEFT);
                                        }

                                        tvTitle.setText("" + title);

                                        if (value != null && !TextUtils.isEmpty(value)) {
                                            tvAmount.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, Float.parseFloat(value)));
                                        } else {
                                            tvAmount.setText(mCurrencyCode + " 0");
                                        }

                                        lnrAmount.addView(view);

                                    }
                                }
                            }
                        } else if (jsonResponse != null && jsonResponse.getString("success").equals("0")) {
                            Utils.showToast(getActivity(), "" + jsonResponse.getString("message"));
                        } else if (jsonResponse != null && jsonResponse.getString("success").equals("2")) {
                            Utils.showInitialScreen(getActivity());
                            if (getActivity() instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) getActivity();
                                activity.loadShoppingCartsWithClearStack();
                            }
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (String.valueOf(response.code()).startsWith("5")) {
                        Utils.showAlertDialog(getActivity(), "" + response.code() + "-3");
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
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Utils.closeProgressDialog();
                lnrReviewPay.setVisibility(View.VISIBLE);
            }
        });

    }

    int count = 0;
    List<ShoppingBag.Item> mOrderItemList;

    private void getReviewOrderList() {
        LogUtils.e("", "getReviewOrderList call");

        String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);


        Call<ShoppingBag> callCode;
        if (isGuestUser) {
            callCode = apiService.getGuestCartList(Shy7lo.mLangCode, guestToken);
        } else {
            Map<String, Object> jsonParams = new ArrayMap<>();
            jsonParams.put("cart_id", guestToken);
//            jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

            callCode = apiService.getUserCartList(Shy7lo.mLangCode, "Bearer " + userToken);
        }


        callCode.enqueue(new Callback<ShoppingBag>() {
            @Override
            public void onResponse(Call<ShoppingBag> call, Response<ShoppingBag> response) {
                if (response.isSuccessful()) {
                    try {

                        ShoppingBag shoppingBag = response.body();
                        if (shoppingBag != null && shoppingBag.success.equals("1")) {

                            if (shoppingBag.data != null) {
                                mOrderItemList = shoppingBag.data.getItems();
                                if (mOrderItemList != null && mOrderItemList.size() > 0) {
                                    count = 0;

                                    ReviewOrderAdapter adapter = new ReviewOrderAdapter(getActivity(), mOrderItemList);
                                    lvOrders.setAdapter(adapter);
                                }
                            }
//                            ReviewOrder reviewOrder = new Gson().fromJson(jsonResponse.getString("data"), ReviewOrder.class);
//                            if (reviewOrder != null) {
//                                mOrderItemList = reviewOrder.getItems();
//
//                                if (mOrderItemList != null && mOrderItemList.size() > 0) {
//
//                                    count = 0;
//
//                                    ReviewOrderAdapter adapter = new ReviewOrderAdapter(getActivity(), mOrderItemList);
//                                    lvOrders.setAdapter(adapter);
//
//                                }
//
//                            }

                        } else if (shoppingBag != null && shoppingBag.success.equals("0")) {
                            Utils.showToast(getActivity(), "" + shoppingBag.message);
                        } else if (shoppingBag != null && shoppingBag.success.equals("2")) {
                            Utils.showInitialScreen(getActivity());
                            if (getActivity() instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) getActivity();
                                activity.loadShoppingCartsWithClearStack();
                            }
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (String.valueOf(response.code()).startsWith("5")) {
                        Utils.showAlertDialog(getActivity(), "" + response.code() + "-2");
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
                getTotalAmount();
            }

            @Override
            public void onFailure(Call<ShoppingBag> call, Throwable t) {

                getTotalAmount();
            }
        });

    }

    String mOrderId = "";

    private void placeOrder() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        placeOrder();

                    }
                }
            });
            return;
        }

        LogUtils.e("", "placeOrder call");
        Utils.showProgressDialog(getActivity());

        String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
        SharedPreferences pref = getActivity().getSharedPreferences(NotificationUtils.SHARED_PREF, 0);

        Map<String, Object> jsonParams = new ArrayMap<>();

        jsonParams.put("payment_method", mSelectedPaymentCode);
        jsonParams.put("firstname", etFullName.getText().toString().trim());
        jsonParams.put("lastname", etLastName.getText().toString().trim());
        jsonParams.put("email", etEmail.getText().toString());
//        jsonParams.put("email", userEmail);
        jsonParams.put("street", etAddress.getText().toString().trim());
        jsonParams.put("region", ""); //Alyasmeen
        jsonParams.put("city", (isOtherCity(etCity.getText().toString().trim()) ? etCityOther.getText().toString().trim() : etCity.getText().toString().trim()));
        jsonParams.put("country_id", mCountryID);
        jsonParams.put("phone", mCountryCode + "" + etPhoneNumber.getText().toString().trim());
        jsonParams.put("device_token", "" + pref.getString("regId", ""));
        jsonParams.put("adjust_tracking_info", "" + MyPref.getPref(getActivity(), MyPref.ADJUST_GOOGLE_ID, ""));
//        jsonParams.put("device_token", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
        jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));

        if (isGuestUser) {
            jsonParams.put("cart_id", guestToken);
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        Call<JsonElement> callCode = null;
        if (isGuestUser) {
            callCode = apiService.guestPlaceOrder(Shy7lo.mLangCode, body);
        } else {
            String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
            callCode = apiService.userPlaceOrder(Shy7lo.mLangCode, "Bearer " + userToken, body);
        }

        callCode.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonResponse = new JSONObject(response.body().toString());
                        if (jsonResponse != null && jsonResponse.getString("success").equals("1")) {

                            MyPref.setPref(getActivity(), MyPref.USER_COUPON_CODE, "");

                            JSONObject jData = jsonResponse.getJSONObject("data");
                            if (jData != null) {
                                mOrderId = jData.getString("order_id");
                            }
                            LogUtils.e("", "mOrderId::" + mOrderId);

//                            Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_order_successful));
                            MyPref.setPref(getActivity(), MyPref.GUEST_CART_ID, "");
                            MyPref.setPref(getActivity(), MyPref.CART_ITEM_COUNT, 0);

                            if (getActivity() instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) getActivity();
                                activity.updateBadgetCount();
                            }

                            getGuestCartToken();

                            if (getActivity() instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) getActivity();
                                activity.setFirebaseLog("Order_placed");
                            }


                        } else if (jsonResponse != null && jsonResponse.getString("success").equals("0")) {
                            Utils.showToast(getActivity(), "" + jsonResponse.getString("message"));
                        } else if (jsonResponse != null && jsonResponse.getString("success").equals("2")) {
                            Utils.showInitialScreen(getActivity());
                            if (getActivity() instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) getActivity();
                                activity.loadShoppingCartsWithClearStack();
                            }
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                    Utils.showAlertDialog(getActivity(), "" + response.code());
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
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Utils.closeProgressDialog();
                Utils.showAlertDialog(getActivity(), "" + t.getMessage());
            }
        });

    }

    private void getGuestCartToken() {

//        LogUtils.e(Shy7lo.TAG, "getGuestCartToken call");
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
//        Call<JsonElement> call = apiService.getGuestCartToken(Shy7lo.mLangCode);
//        call.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//
//                LogUtils.e(TAG, "response code:" + response.code());
//
//                if (response.isSuccessful()) {
//                    try {
//
//                        JSONObject jResponse = new JSONObject(response.body().toString());
//                        if (jResponse != null && jResponse.getString("success").equalsIgnoreCase("1")) {
//                            JSONObject jData = jResponse.getJSONObject("data");
//                            if (jData != null && jData.has("cart_id")) {
//                                String token = jData.getString("cart_id");
//                                LogUtils.e(TAG, "response token:" + token);
//
//                                if (!TextUtils.isEmpty(token)) {
//                                    MyPref.setPref(getActivity(), MyPref.GUEST_CART_ID, token);
//                                }

        mLogTotalAmount = mTotalAmount;

        if (mSelectedPaymentCode.equalsIgnoreCase("payfort_fort_cc")) {
            LogUtils.e("", "mOrderId::" + mOrderId + " mTotalAmount::" + mTotalAmount);
            if (!TextUtils.isEmpty(mOrderId)) {
                if (!TextUtils.isEmpty(mTotalAmount)) {
                    mTotalAmount = "" + (int) (((Float.parseFloat(mTotalAmount) < 1) ? 1f : Float.parseFloat(mTotalAmount)) * 100);
                }
                LogUtils.e("", " mTotalAmount::" + mTotalAmount);
                requestForPayfortPayment("" + mOrderId, mTotalAmount, "SAR", etEmail.getText().toString());
//                                        requestForPayfortPayment("" + mOrderId, mTotalAmount, "SAR", userEmail);
//                                        requestForPayfortPayment("" + mOrderId, "100", "SAR", userEmail);
            }
        } else {

            showCongratualtionDialog();
//                                    if (getActivity() instanceof HomeActivity) {
//                                        HomeActivity activity = (HomeActivity) getActivity();
//                                        activity.loadShoppingBagsWithClearStack();
//                                    }
        }

//                            }
//                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
//                            Utils.showInitialScreen(getActivity());
//                            return;
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                System.out.println(t.getMessage());
//            }
//        });

    }

    private void cancelOrder() {

        LogUtils.e(Shy7lo.TAG, "cancelOrder call::" + mOrderId);

        if (!TextUtils.isEmpty(mOrderId)) {
            ApiInterface apiService =
                    RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
            Call<JsonElement> call = apiService.cancelOrder(Shy7lo.mLangCode, mOrderId);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                    LogUtils.e(TAG, "response code:" + response.code());

                    if (response.isSuccessful()) {

                        if (getActivity() instanceof HomeActivity) {
                            HomeActivity activity = (HomeActivity) getActivity();
                            activity.loadShoppingBagsWithClearStack();
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
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
        }

    }

    public void requestForPayfortPayment(String mOrderId, String mTotalAmount, String mCurrencyCode, String userEmail) {
        PayFortData payFortData = new PayFortData();
        if (!TextUtils.isEmpty(mTotalAmount)) {
            payFortData.amount = mTotalAmount;
            payFortData.command = PayFortPayment.PURCHASE;
//            payFortData.command = PayFortPayment.AUTHORIZATION;
            payFortData.currency = mCurrencyCode;
            payFortData.customerEmail = userEmail;
            payFortData.language = MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN);
            payFortData.merchantReference = mOrderId;
//            String mTimeStamp = "Shy7lo" + System.currentTimeMillis();
//            payFortData.merchantReference = mTimeStamp;

            PayFortPayment payFortPayment = new PayFortPayment(getActivity(), fortCallback, this);
            payFortPayment.requestForPayment(payFortData);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e(TAG_SHIPMENT_PAY_FRAGMENT, "onActivityResult call::" + requestCode + " resultCode::" + resultCode);
        if (requestCode == PayFortPayment.RESPONSE_PURCHASE) {
            fortCallback.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == RC_ADDRESS_CITY && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                if (!TextUtils.isEmpty(data.getStringExtra(AddressMapActivity.BNDL_MAP_ADDRESS))) {
                    etAddress.setText("" + data.getStringExtra(AddressMapActivity.BNDL_MAP_ADDRESS));
                }
//                if (!TextUtils.isEmpty(data.getStringExtra(AddressMapActivity.BNDL_MAP_CITY))) {
//                    etCity.setText("" + data.getStringExtra(AddressMapActivity.BNDL_MAP_CITY));
//                }
            }
        } else if (requestCode == AddAddressActivity.RC_ADD_ADDRESS && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {

                String id = data.getStringExtra("id");
                String firstname = data.getStringExtra("firstname");
                String lastname = data.getStringExtra("lastname");
                String street = data.getStringExtra("street");
                String city = data.getStringExtra("city");
//                String postcode = data.getStringExtra("postcode");
                String countryId = data.getStringExtra("country_id");
                String telephone = data.getStringExtra("telephone");

                mAddressUser.id = id;
                mAddressUser.firstname = firstname;
                mAddressUser.lastname = lastname;
                mAddressUser.city = city;
//                mAddressUser.postcode = postcode;
                mAddressUser.countryId = countryId;
                mAddressUser.telephone = telephone;
                if (mAddressUser.street != null && mAddressUser.street.size() > 0) {
                    mAddressUser.street.clear();
                    mAddressUser.street.add(street);
                } else {
                    mAddressUser.street = new ArrayList<>();
                    mAddressUser.street.add(street);
                }

                etFullName.setText("" + firstname);
                etLastName.setText("" + lastname);
                etCity.setText("" + city);
                etAddress.setText("" + street);

                if (telephone.contains("+")) {
                    tvCountryCode.setText("" + telephone.substring(0, 4));
                    mCountryCode = telephone.substring(0, 4);
                    LogUtils.e("", "mCountryCode:" + mCountryCode);
                    etPhoneNumber.setText("" + telephone.substring(4));
                } else {
                    etPhoneNumber.setText("" + telephone);
                }

                etEmail.setText("" + MyPref.getPref(getActivity(), MyPref.USER_EMAIL, ""));

                setUserDetails();
                getCityList();

                lnrUserAddress.setVisibility(View.GONE);
                lnrUserDetails.setVisibility(View.VISIBLE);
                tvUserName.setText(firstname + " " + lastname);
//                if (!TextUtils.isEmpty(postcode)) {
//                    tvUserCity.setText(city + ", " + postcode);
//                } else {
                tvUserCity.setText(city);
//                }
                tvUserCountry.setText(countryId);
                tvUserTelephone.setText(getString(R.string.mobile_no) + ": " + telephone);

                if (street != null) {
                    tvUserAddress.setVisibility(View.VISIBLE);
                    tvUserAddress.setText(street);
                } else {
                    tvUserAddress.setVisibility(View.GONE);
                }

                mCountryID = countryId;

                if (countryId.equalsIgnoreCase("SA")) {
                    tvUserCountry.setText("Saudi Arabia");
                    mInCountryPosition = 1;
                    mInCountryCode = "966";
                } else if (countryId.equalsIgnoreCase("AE")) {
                    tvUserCountry.setText("UAE");
                    mInCountryPosition = 2;
                    mInCountryCode = "971";
                } else if (countryId.equalsIgnoreCase("QA")) {
                    tvUserCountry.setText("Qatar");
                    mInCountryPosition = 3;
                    mInCountryCode = "974";
                } else if (countryId.equalsIgnoreCase("KW")) {
                    tvUserCountry.setText("Kuwait");
                    mInCountryPosition = 4;
                    mInCountryCode = "965";
                } else if (countryId.equalsIgnoreCase("BH")) {
                    tvUserCountry.setText("Bahrain");
                    mInCountryPosition = 5;
                    mInCountryCode = "973";
                } else if (countryId.equalsIgnoreCase("OM")) {
                    tvUserCountry.setText("Oman");
                    mInCountryPosition = 5;
                    mInCountryCode = "968";
                }

                if (mCountryList != null && mCountryList.size() > 0) {
                    for (int j = 0; j < mCountryList.size(); j++) {
                        if (mCountryList.get(j).id.equalsIgnoreCase(countryId)) {
                            mInCurrencyCode = "" + mCountryList.get(j).currencyEn;
                            mInCurrencyArCode = "" + mCountryList.get(j).currencyAr;
                            mInExchangeRate = mCountryList.get(j).exchangeRate;
                        }
                    }

                }

            }
        }
    }


    @Override
    public void onPaymentRequestResponse(int responseType, PayFortData responseData) {
        LogUtils.e("", "onPaymentRequestResponse call");
        LogUtils.e("", "responseData deviceId:" + responseData.deviceId);
        LogUtils.e("", "responseData responseCode:" + responseData.responseCode);
        LogUtils.e("", "responseData responseMessage:" + responseData.responseMessage);
        LogUtils.e("", "responseData fortId:" + responseData.fortId);

        if (responseType == PayFortPayment.RESPONSE_GET_TOKEN) {
            LogUtils.e("onPaymentResponse", "Token not generated");
            removeAllCartItem();
            if (responseData != null) {
                if (!BuildConfig.DEBUG) {
                    Answers.getInstance().logCustom(new CustomEvent("Payfort Token")
                            .putCustomAttribute("Order ID", "" + mOrderId)
                            .putCustomAttribute("Response code", "" + responseData.responseCode)
//                        .putCustomAttribute("Device ID", "" + responseData.deviceId)
                            .putCustomAttribute("Response message", "" + responseData.responseMessage)
                            .putCustomAttribute("userType", (isGuestUser ? "Guest" : "LoggedIn")));
                }
            } else {
                if (!BuildConfig.DEBUG) {
                    Answers.getInstance().logCustom(new CustomEvent("Payfort Token")
                            .putCustomAttribute("Order ID", "" + mOrderId)
                            .putCustomAttribute("userType", (isGuestUser ? "Guest" : "LoggedIn")));
                }
            }
            cancelOrder();
        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_CANCEL) {
            Utils.showToast(getActivity(), "Payment cancelled");
            removeAllCartItem();
            LogUtils.e("onPaymentResponse", "Payment cancelled");

            if (responseData != null) {
                if (!BuildConfig.DEBUG) {
                    Answers.getInstance().logCustom(new CustomEvent("Payfort Cancel")
                            .putCustomAttribute("Order ID", "" + mOrderId)
                            .putCustomAttribute("Response code", "" + responseData.responseCode)
//                        .putCustomAttribute("Device ID", "" + responseData.deviceId)
                            .putCustomAttribute("Response message", "" + responseData.responseMessage)
                            .putCustomAttribute("userType", (isGuestUser ? "Guest" : "LoggedIn")));
                }
            } else {
                if (!BuildConfig.DEBUG) {
                    Answers.getInstance().logCustom(new CustomEvent("Payfort Cancel")
                            .putCustomAttribute("Order ID", "" + mOrderId)
                            .putCustomAttribute("userType", (isGuestUser ? "Guest" : "LoggedIn")));
                }
            }
            cancelOrder();
        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_FAILURE) {
            Utils.showToast(getActivity(), "Payment failed");
            removeAllCartItem();
            if (responseData != null) {
                if (!BuildConfig.DEBUG) {
                    Answers.getInstance().logCustom(new CustomEvent("Payfort")
                            .putCustomAttribute("Order ID", "" + mOrderId)
                            .putCustomAttribute("Response code", "" + responseData.responseCode)
                            .putCustomAttribute("Response message", "" + responseData.responseMessage)
                            .putCustomAttribute("userType", (isGuestUser ? "Guest" : "LoggedIn")));
                }
            } else {
                if (!BuildConfig.DEBUG) {
                    Answers.getInstance().logCustom(new CustomEvent("Payfort")
                            .putCustomAttribute("Order ID", "" + mOrderId)
                            .putCustomAttribute("userType", (isGuestUser ? "Guest" : "LoggedIn")));
                }
            }
            Log.e("onPaymentResponse", "Payment failed");
            cancelOrder();
        } else {
            Toast.makeText(getActivity(), "Payment successful", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Payment successful");
//            if (getActivity() instanceof HomeActivity) {
//                HomeActivity activity = (HomeActivity) getActivity();
//                activity.loadShoppingBagsWithClearStack();
//            }
            removeAllCartItem();
            showCongratualtionDialog();

            if (RestClient.isFacebookLive) {
                try {
                    // FB Log Added payment info
                    AppEventsLogger logger = AppEventsLogger.newLogger(getActivity());

                    Bundle parameters = new Bundle();
                    parameters.putString(AppEventsConstants.EVENT_PARAM_SUCCESS, "true");

                    logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_PAYMENT_INFO, parameters);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void removeAllCartItem() {
//        if (dbAdapter != null) {
//            dbAdapter.removeAllShoppingItem();
//        }
    }

    private void showCongratualtionDialog() {
        LogUtils.e("", "showCongratualtionDialog call::mLogTotalAmount" + mLogTotalAmount + " mTotalAmount::" + mTotalAmount);

        try {
            if (mOrderItemList != null && mOrderItemList.size() > 0) {
                List<TuneEventItem> mTuneEventItemsList = new ArrayList<TuneEventItem>();
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (int i = 0; i < mOrderItemList.size(); i++) {

                    TuneEventItem mTuneEventItem = new TuneEventItem("" + mOrderItemList.get(i).getName());
                    mTuneEventItem.quantity = mOrderItemList.get(i).getQty();
                    mTuneEventItem.itemname = mOrderItemList.get(i).getName();
                    mTuneEventItem.attribute1 = "" + mOrderItemList.get(i).getSku();
                    mTuneEventItem.attribute2 = "" + (mOrderItemList.get(i).getProductType().equalsIgnoreCase("configurable") ? "Configurable" : "Simple");
//                    mTuneEventItem.attribute3 = "" + mOrderId;
//                    mTuneEventItem.attribute4 = "" + mSelectedPaymentMethod;
//                    mTuneEventItem.attribute5 = "" + (isGuestUser ? "Guest" : "LoggedIn");

                    boolean isSpecialPrice = false;
                    if (mOrderItemList.get(i).getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(mOrderItemList.get(i).getSpecial_price()))) {

                        if (!TextUtils.isEmpty(mOrderItemList.get(i).getSpecial_from_date()) && !TextUtils.isEmpty(mOrderItemList.get(i).getSpecial_to_date())) {

                            Date fromDate = sdfDate.parse(mOrderItemList.get(i).getSpecial_from_date());
                            Date toDate = sdfDate.parse(mOrderItemList.get(i).getSpecial_to_date());
                            Date currentDate = new Date();

                            if (currentDate.after(fromDate) && currentDate.before(toDate)) {
                                isSpecialPrice = true;
                            }

                        }

                    }

                    if (isSpecialPrice) {
                        try {
                            if (!BuildConfig.DEBUG) {
                                Answers.getInstance().logPurchase(new PurchaseEvent()
                                        .putItemPrice(BigDecimal.valueOf(Utils.getAnswerPrice(mExchangeRate, mOrderItemList.get(i).getSpecial_price())))
                                        .putCurrency(Currency.getInstance("" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE,
                                                getActivity().getResources().getString(R.string.SAR))))
                                        .putItemName("" + mOrderItemList.get(i).getName())
                                        .putItemType("" + (mOrderItemList.get(i).getProductType().equalsIgnoreCase("configurable") ? "Configurable" : "Simple"))
                                        .putItemId("" + mOrderItemList.get(i).getSku())
                                        .putSuccess(true)
                                        .putCustomAttribute("Order ID", "" + mOrderId)
                                        .putCustomAttribute("Payment Method", "" + mSelectedPaymentMethod)
                                        .putCustomAttribute("userType", (isGuestUser ? "Guest" : "LoggedIn")));
                            }
                            mTuneEventItem.unitPrice = Utils.getAnswerPrice(mExchangeRate, mOrderItemList.get(i).getSpecial_price());
                            mTuneEventItem.revenue = mOrderItemList.get(i).getQty() * Utils.getAnswerPrice(mExchangeRate, mOrderItemList.get(i).getSpecial_price());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            if (!BuildConfig.DEBUG) {
                                Answers.getInstance().logPurchase(new PurchaseEvent()
                                        .putItemPrice(BigDecimal.valueOf(Utils.getAnswerPrice(mExchangeRate, mOrderItemList.get(i).getPrice())))
                                        .putCurrency(Currency.getInstance("" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE,
                                                getActivity().getResources().getString(R.string.SAR))))
                                        .putItemName("" + mOrderItemList.get(i).getName())
                                        .putItemType("" + (mOrderItemList.get(i).getProductType().equalsIgnoreCase("configurable") ? "Configurable" : "Simple"))
                                        .putItemId("" + mOrderItemList.get(i).getSku())
                                        .putSuccess(true)
                                        .putCustomAttribute("Order ID", "" + mOrderId)
                                        .putCustomAttribute("Payment Method", "" + mSelectedPaymentMethod)
                                        .putCustomAttribute("userType", (isGuestUser ? "Guest" : "LoggedIn")));
                            }
                            mTuneEventItem.unitPrice = Utils.getAnswerPrice(mExchangeRate, mOrderItemList.get(i).getPrice());
                            mTuneEventItem.revenue = mOrderItemList.get(i).getQty() * Utils.getAnswerPrice(mExchangeRate, mOrderItemList.get(i).getPrice());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    mTuneEventItemsList.add(mTuneEventItem);
                }


//                if (RestClient.isTuneEnable) {
//                    try {
//                        Tune tune = Tune.getInstance();
//                        tune.measureEvent(new TuneEvent(TuneEvent.PURCHASE)
//                                .withEventItems(mTuneEventItemsList)
//                                .withContentId(mOrderId)
//                                .withRevenue(Utils.getAnswerPrice(mExchangeRate, Float.parseFloat(mTotalAmount)))
//                                .withAttribute1(mSelectedPaymentMethod)
//                                .withAttribute2((isGuestUser ? "Guest" : "LoggedIn"))
//                                .withCurrencyCode("" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR))));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }

                if (RestClient.isFacebookLive) {
                    try {
                        // FB Log Purchase
                        AppEventsLogger logger = AppEventsLogger.newLogger(getActivity());

                        Bundle parameters = new Bundle();
                        parameters.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR)));
//                        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "" + productDetails.getTypeId());
                        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, "" + mOrderId);
                        parameters.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, mSelectedPaymentMethod);
                        parameters.putString(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, "" + mOrderItemList.size());

                        logger.logEvent(AppEventsConstants.EVENT_NAME_PURCHASED, Utils.getAnswerPrice(mExchangeRate, Float.parseFloat(mLogTotalAmount)), parameters);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    AdjustEvent event = new AdjustEvent("hy369e");
                    event.addPartnerParameter("Currency Code", "" + "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR)));
                    event.addPartnerParameter("Payment Method", "" + "" + mSelectedPaymentMethod);
                    event.addPartnerParameter("No Of Items", "" + "" + mOrderItemList.size());

                    //callback
                    event.addCallbackParameter("Currency Code", "" + "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR)));
                    event.addCallbackParameter("Payment Method", "" + "" + mSelectedPaymentMethod);
                    event.addCallbackParameter("No Of Items", "" + "" + mOrderItemList.size());
                    event.setOrderId(mOrderId);
                    event.setRevenue(Utils.getAnswerPrice(mExchangeRate, Float.parseFloat(mLogTotalAmount)), MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR)));

                    List<CriteoProduct> products = new ArrayList<>();
                    for (int i = 0; i < mOrderItemList.size(); i++) {

                        float mPrice = mOrderItemList.get(i).getPrice();
                        if (mOrderItemList.get(i).getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(mOrderItemList.get(i).getSpecial_price()))) {

                            if (!TextUtils.isEmpty(mOrderItemList.get(i).getSpecial_from_date()) && !TextUtils.isEmpty(mOrderItemList.get(i).getSpecial_to_date())) {

                                Date fromDate = sdfDate.parse(mOrderItemList.get(i).getSpecial_from_date());
                                Date toDate = sdfDate.parse(mOrderItemList.get(i).getSpecial_to_date());
                                Date currentDate = new Date();

                                if (currentDate.after(fromDate) && currentDate.before(toDate)) {
                                    mPrice = mOrderItemList.get(i).getSpecial_price();
                                }
                            }
                        }

                        CriteoProduct product = new CriteoProduct((float) Utils.getAnswerPrice(mExchangeRate, mPrice), mOrderItemList.get(i).getQty(), "" + mOrderItemList.get(i).getSku());
                        products.add(product);

                    }

                    String mCriteo = MyPref.getPref(getActivity(), MyPref.APP_CRITEO, "");
                    if (TextUtils.isEmpty(mCriteo) || mCriteo.equalsIgnoreCase("1")) {
                        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
                        AdjustCriteo.injectTransactionConfirmedIntoEvent(event, products, "" + mOrderId, TextUtils.isEmpty(userToken) ? "1" : "0");
                        AdjustCriteo.injectCustomerIdIntoCriteoEvents(TextUtils.isEmpty(userToken) ? userToken : MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""));
                    }

                    Adjust.trackEvent(event);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        etCouponCode.setText("");
        etCouponCode.setEnabled(true);
        btnValidateCoupon.setVisibility(View.GONE);
        btnDeleteCoupon.setVisibility(View.GONE);
        MyPref.setPref(getActivity(), MyPref.USER_COUPON_CODE, "");

        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_congratulation);

        final TextView tvOrderId = (TextView) dialog.findViewById(R.id.tvOrderId);
        tvOrderId.setText("" + mOrderId);
        final TextView tvContinueShopping = (TextView) dialog.findViewById(R.id.tvContinueShopping);

        tvContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }

                if (getActivity() instanceof HomeActivity) {
                    HomeActivity activity = (HomeActivity) getActivity();
                    activity.loadShoppingBagsWithClearStack();
                }

            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });


        dialog.show();

    }

    private void showVerificationNumberDialog() {

        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_confirm_number);

        final TextView tvToNumber = (TextView) dialog.findViewById(R.id.tvToNumber);
        tvToNumber.setText("+" + mCountryCode + " " + etPhoneNumber.getText().toString());
        final TextView tvResendCode = (TextView) dialog.findViewById(R.id.tvResendCode);
        final TextView tvInvalideCode = (TextView) dialog.findViewById(R.id.tvInvalideCode);
        final ImageButton ibCancel = (ImageButton) dialog.findViewById(R.id.ibCancel);
        final PinView mPinView = (PinView) dialog.findViewById(R.id.mPinView);

        tvResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                    dialog.cancel();
//                }

            }
        });

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            }
        });

        mPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String mOTP = charSequence.toString();
                if (!TextUtils.isEmpty(mOTP) && mOTP.length() >= 4) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                    placeOrder();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });


        dialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
//        if (getActivity() instanceof HomeActivity){
//            HomeActivity activity = (HomeActivity) getActivity();
//            activity.setFirebaseLog("Shipment_Products");
//        }
    }

    private void getInitApi() {

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("device_type", "android");
        jsonParams.put("app_version", "" + BuildConfig.VERSION_NAME);
        jsonParams.put("device_token", "" + Utils.getDeviceToken(getActivity()));
        jsonParams.put("country", "SA");

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<AppInit> call = apiService.getAppInit(Shy7lo.mLangCode, "S0iv6TrBsAh9Xfz0zcTil5qF3Yn8Yt391523174021", body);
        call.enqueue(new Callback<AppInit>() {
            @Override
            public void onResponse(Call<AppInit> call, Response<AppInit> response) {

                LogUtils.e(TAG, "response code:" + response.code());

                if (response.isSuccessful()) {
                    try {

                        Shy7lo.mAppInit = response.body();
                        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.success.equalsIgnoreCase("1")) {
                            if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.countries != null) {
                                mCountryList = Shy7lo.mAppInit.countries;
                                setCountryConfig();
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
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
            public void onFailure(Call<AppInit> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    private void resetData() {

        mCurrencyCode = "";
        mExchangeRate = 0;
        mUserType = "";
        mTotalAmount = "";
        mLogTotalAmount = "";

        isGuestUser = false;
        token = "";
        mSelectedPaymentCode = "";
        mSelectedPaymentMethod = "";
        mCountryCode = "";
        mCountryID = "";
        isBankTransfer = false;
        isCashOnDelivery = false;
        isCreditCard = false;

        mAddressList = null;

        mUserFirtname = "";
        mUserLastname = "";
        mUserEmail = "";
        mUserAddress = "";
        mUserMobile = "";
        mUserRegion = "";
        mUserCity = "";

        if (mPaymentMethodList != null && mPaymentMethodList.size() > 0) {
            mPaymentMethodList.clear();
        }

    }

}
