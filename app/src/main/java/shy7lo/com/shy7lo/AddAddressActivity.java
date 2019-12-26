package shy7lo.com.shy7lo;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.ApiResponse;
import shy7lo.com.shy7lo.model.AppInit;
import shy7lo.com.shy7lo.model.CityResponse;
import shy7lo.com.shy7lo.model.MyAddressResponse;
import shy7lo.com.shy7lo.model.RegionsResponse;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.Constant;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;
import shy7lo.com.shy7lo.wheel.WheelCountryPicker;

import static shy7lo.com.shy7lo.application.Shy7lo.TAG;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rltTitle)
    RelativeLayout rltTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDone)
    TextView tvDone;
    @BindView(R.id.tvCountry)
    TextView tvCountry;
    @BindView(R.id.tvCountryTitle)
    TextView tvCountryTitle;
    //    @BindView(R.id.tvGoShy7loTest)
//    TextView tvGoShy7loTest;
    @BindView(R.id.tvCountryCode)
    TextView tvCountryCode;
    @BindView(R.id.ibCancel)
    ImageButton ibCancel;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    //    @BindView(R.id.etRegion)
//    EditText etRegion;
    @BindView(R.id.etCity)
    AutoCompleteTextView etCity;
    @BindView(R.id.etZipcode)
    EditText etZipcode;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.tbCountry)
    TableRow tbCountry;
    @BindView(R.id.lnrDefaultShipping)
    LinearLayout lnrDefaultShipping;
    @BindView(R.id.lnrDefaultBilling)
    LinearLayout lnrDefaultBilling;
    @BindView(R.id.cbDefaultShipping)
    CheckBox cbDefaultShipping;
    @BindView(R.id.cbDefaultBilling)
    CheckBox cbDefaultBilling;
    @BindView(R.id.tvDefaultShipping)
    TextView tvDefaultShipping;
    @BindView(R.id.tvDefaultBilling)
    TextView tvDefaultBilling;
    @BindView(R.id.tvDefaultAddressCheckout)
    TextView tvDefaultAddressCheckout;

    MyAddressResponse.Address mAddress;
    private int mAddressCount;
    private boolean isFromAddAddress = false;

    List<AppInit.Country> mCountryList;
    List<RegionsResponse.Region> mRegionList;
    List<CityResponse.City> mCityList;

    private int mInCountryPosition = 1;
    private String mInCountryCode = "+966";
    private String mInCountryId = "SA";

    public static int RC_ADD_ADDRESS = 2515;

    private boolean isDefaultShipping = false, isDefaultBilling = false;

    Animation animClose, animOpen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);

        InitializeControls();
        InitializeControlsAction();
    }

    private void InitializeControls() {

        animOpen = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_open);
        animClose = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_close);

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            rltTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            tvDone.setScaleX(-1f);
            tbCountry.setScaleX(-1f);
            tvCountryCode.setScaleX(-1f);
            etPhoneNumber.setScaleX(-1f);

//            etFirstName.setScaleX(-1f);
//            etLastName.setScaleX(-1f);
//            etRegion.setScaleX(-1f);
//            etCity.setScaleX(-1f);
//            etZipcode.setScaleX(-1f);
//            etAddress.setScaleX(-1f);
//            tvCountryTitle.setScaleX(-1f);
//            tvCountry.setScaleX(-1f);
//            tvCountryCode.setScaleX(-1f);
//            etPhoneNumber.setScaleX(-1f);
            lnrDefaultShipping.setScaleX(-1f);
            lnrDefaultBilling.setScaleX(-1f);
            tvDefaultShipping.setScaleX(-1f);
            tvDefaultBilling.setScaleX(-1f);


            etFirstName.setGravity(Gravity.RIGHT);
            etLastName.setGravity(Gravity.RIGHT);
//            etRegion.setGravity(Gravity.RIGHT);
            etCity.setGravity(Gravity.RIGHT);
            etZipcode.setGravity(Gravity.RIGHT);
            etAddress.setGravity(Gravity.RIGHT);
            etPhoneNumber.setGravity(Gravity.RIGHT);
            tvCountryCode.setGravity(Gravity.LEFT);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvCountryTitle.getLayoutParams();
            params.gravity = Gravity.RIGHT;
            tvCountryTitle.setLayoutParams(params);

            tvCountry.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_details_arrow_down, 0, 0, 0);
        } else {
            rltTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);
            tvDone.setScaleX(1f);
            tbCountry.setScaleX(1f);
            tvCountryCode.setScaleX(1f);
            etPhoneNumber.setScaleX(1f);

//            etFirstName.setScaleX(1f);
//            etLastName.setScaleX(1f);
//            etRegion.setScaleX(1f);
//            etCity.setScaleX(1f);
//            etZipcode.setScaleX(1f);
//            etAddress.setScaleX(1f);
//            tvCountryTitle.setScaleX(1f);
//            tvCountry.setScaleX(1f);
//            tvCountryCode.setScaleX(1f);
//            etPhoneNumber.setScaleX(1f);
            lnrDefaultShipping.setScaleX(1f);
            lnrDefaultBilling.setScaleX(1f);
            tvDefaultShipping.setScaleX(1f);
            tvDefaultBilling.setScaleX(1f);

            etFirstName.setGravity(Gravity.LEFT);
            etLastName.setGravity(Gravity.LEFT);
//            etRegion.setGravity(Gravity.LEFT);
            etCity.setGravity(Gravity.LEFT);
            etZipcode.setGravity(Gravity.LEFT);
            etAddress.setGravity(Gravity.LEFT);
            etPhoneNumber.setGravity(Gravity.LEFT);
            tvCountryCode.setGravity(Gravity.RIGHT);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvCountryTitle.getLayoutParams();
            params.gravity = Gravity.LEFT;
            tvCountryTitle.setLayoutParams(params);

            tvCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_details_arrow_down, 0);
        }

        mInCountryPosition = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_POSITION, 1);
        mInCountryCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_CODE, "+966");
        mInCountryId = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_ID, "SA");

        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.countries != null) {
            mCountryList = Shy7lo.mAppInit.countries;
            setCountryConfig();
        } else {
            getInitApi();
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            mAddressCount = bundle.getInt(Constant.BNDL_ADDRESS_SIZE, 0);

            if (bundle.containsKey(Constant.BNDL_IS_FROM_SHIPMENT)) {
                isFromAddAddress = bundle.getBoolean(Constant.BNDL_IS_FROM_SHIPMENT, false);
            }

            tvTitle.setText(getResources().getString(R.string.add_new_address));

            if (bundle.containsKey(Constant.BNDL_ADDRESS)) {
                mAddress = new Gson().fromJson(bundle.getString(Constant.BNDL_ADDRESS), MyAddressResponse.Address.class);
                if (mAddress != null) {

                    tvTitle.setText(getResources().getString(R.string.update_address));

                    if (!TextUtils.isEmpty(mAddress.city)) {
                        etCity.setText("" + mAddress.city);
                    }

                    if (!TextUtils.isEmpty(mAddress.postcode)) {
                        etZipcode.setText("" + mAddress.postcode);
                    }

                    if (!TextUtils.isEmpty(mAddress.firstname)) {
                        etFirstName.setText("" + mAddress.firstname);
                    }

                    if (!TextUtils.isEmpty(mAddress.lastname)) {
                        etLastName.setText("" + mAddress.lastname);
                    }

                    if (!TextUtils.isEmpty(mAddress.telephone)) {
                        etPhoneNumber.setText("" + mAddress.telephone);
                    }

                    if (mAddress.street != null && mAddress.street.size() > 0) {
                        etAddress.setText("" + mAddress.street.get(0));
                    }

                    isDefaultShipping = mAddress.defaultShipping;
                    isDefaultBilling = mAddress.defaultBilling;

                    if (mCountryList != null && mCountryList.size() > 0) {
                        for (int i = 0; i < mCountryList.size(); i++) {
                            if (mCountryList.get(i).id.equalsIgnoreCase(mAddress.countryId)) {

                                mInCountryId = "" + mCountryList.get(i).id;
                                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                                    tvCountry.setText("" + mCountryList.get(i).fullNameArabic);
                                } else {
                                    tvCountry.setText("" + mCountryList.get(i).fullNameEnglish);
                                }

                                if (mCountryList.get(i).id.equalsIgnoreCase("SA")) {

                                    mInCountryPosition = 1;
                                    mInCountryCode = "966";


                                } else if (mCountryList.get(i).id.equalsIgnoreCase("AE")) {

                                    mInCountryPosition = 2;
                                    mInCountryCode = "971";

                                } else if (mCountryList.get(i).id.equalsIgnoreCase("QA")) {

                                    mInCountryPosition = 3;
                                    mInCountryCode = "974";

                                } else if (mCountryList.get(i).id.equalsIgnoreCase("KW")) {

                                    mInCountryPosition = 4;
                                    mInCountryCode = "965";

                                } else if (mCountryList.get(i).id.equalsIgnoreCase("OM")) {

                                    mInCountryPosition = 5;
                                    mInCountryCode = "968";

                                } else if (mCountryList.get(i).id.equalsIgnoreCase("BH")) {

                                    mInCountryPosition = 5;
                                    mInCountryCode = "973";

                                }

                                break;
                            }
                        }
                        setCountryConfig();
                    }
                }
            }
            cbDefaultShipping.setChecked(isDefaultShipping);
            cbDefaultBilling.setChecked(isDefaultBilling);

//            if (isDefaultShipping || isDefaultBilling) {
//                tvDefaultAddressCheckout.setVisibility(View.VISIBLE);
//            }


        }

    }


    private void InitializeControlsAction() {
        ibCancel.setOnClickListener(this);
        tvCountry.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        lnrDefaultShipping.setOnClickListener(this);
        lnrDefaultBilling.setOnClickListener(this);
    }


    private void setCountryConfig() {

        tvCountryCode.setText("" + mInCountryCode);

        if (mCountryList != null && mCountryList.size() > 0) {
            for (int i = 0; i < mCountryList.size(); i++) {
                if (mCountryList.get(i).id.equalsIgnoreCase(mInCountryId)) {

                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                        tvCountry.setText("" + mCountryList.get(i).fullNameArabic);
                    } else {
                        tvCountry.setText("" + mCountryList.get(i).fullNameEnglish);
                    }

                }
            }
        }

        if (!TextUtils.isEmpty(mInCountryCode) && mInCountryCode.contains("966")) {

//            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                etRegion.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_down, 0, 0, 0);
//            } else {
//                etRegion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
//            }
//
//            getRegionList();
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

            etCity.setDropDownHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

            if (mCityList != null && mCityList.size() > 0) {
                setCityAdapter();
            } else {
                getCityList();
            }

        } else {
//            etRegion.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            etRegion.setOnClickListener(null);
//            etRegion.setClickable(true);
//            etRegion.setFocusableInTouchMode(true);
            etCity.setDropDownHeight(0);
        }

    }

    private Context getActivity() {
        return AddAddressActivity.this;
    }

    @Override
    public void onClick(View view) {

        if (view == ibCancel) {
            finish();
        } else if (view == tvCountry) {
            showCountryDialog();
        } else if (view == tvDone) {
            if (isValidField()) {
                if (mAddress != null) {
                    updateAddress("" + mAddress.id);
                } else {
                    addAddress();
                }
            }

        } else if (view == lnrDefaultShipping) {
            cbDefaultShipping.setChecked(!cbDefaultShipping.isChecked());
//            if (cbDefaultShipping.isChecked() || cbDefaultBilling.isChecked()) {
//                tvDefaultAddressCheckout.setVisibility(View.VISIBLE);
//            } else {
//                tvDefaultAddressCheckout.setVisibility(View.GONE);
//            }
        } else if (view == lnrDefaultBilling) {
            cbDefaultBilling.setChecked(!cbDefaultBilling.isChecked());
//            if (cbDefaultShipping.isChecked() || cbDefaultBilling.isChecked()) {
//                tvDefaultAddressCheckout.setVisibility(View.VISIBLE);
//            } else {
//                tvDefaultAddressCheckout.setVisibility(View.GONE);
//            }
        }

    }

    private boolean isValidField() {

        if (TextUtils.isEmpty(etFirstName.getText().toString())) {
            Utils.showToast(getActivity(), getString(R.string.msg_firstname));
            return false;
        } else if (TextUtils.isEmpty(etLastName.getText().toString())) {
            Utils.showToast(getActivity(), getString(R.string.msg_lastname));
            return false;
        } else if (TextUtils.isEmpty(etPhoneNumber.getText().toString())) {
            Utils.showToast(getActivity(), getString(R.string.msg_telephone));
            return false;
        } else if (TextUtils.isEmpty(etCity.getText().toString())) {
            Utils.showToast(getActivity(), getString(R.string.msg_city));
            return false;
        }
//        else if (TextUtils.isEmpty(etZipcode.getText().toString())) {
//            Utils.showToast(getActivity(), getString(R.string.msg_enter_zipcode));
//            return false;
//        }
        else if (TextUtils.isEmpty(etAddress.getText().toString())) {
            Utils.showToast(getActivity(), getString(R.string.msg_address));
            return false;
        }


        return true;
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
            if (mCountryList.get(i).id.equalsIgnoreCase(mInCountryId)) {
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

                setCountryConfig();

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
//
//                            if (mAddress != null) {
//                                if (mRegionList != null && mRegionList.size() > 0) {
//                                    int position = 0;
//                                    for (int i = 0; i < mRegionList.size(); i++) {
//                                        if (mRegionList.get(i).id.equalsIgnoreCase("" + mAddress.regionId)) {
//                                            position = i;
//                                            break;
//                                        }
//                                    }
//                                    String mRegionName = "";
//                                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                                        mRegionName = mRegionList.get(position).region;
//                                    } else {
//                                        mRegionName = mRegionList.get(position).regionEn;
//                                    }
//
//                                    etRegion.setText("" + mRegionName);
////                                    etCity.setText("");
//
//                                    mStateID = mRegionList.get(position).id;
//
//                                    getCityList(mRegionList.get(position).id);
//
//                                }
//                            }
//
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
                            setCityAdapter();
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

    private void setCityAdapter() {
        ArrayList<String> mCityNameList = new ArrayList<>();
        for (int i = 0; i < mCityList.size(); i++) {
            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                mCityNameList.add("" + mCityList.get(i).city);
            } else {
                mCityNameList.add("" + mCityList.get(i).cityEn);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, mCityNameList);
        etCity.setAdapter(adapter);
    }

//    String mStateID = "";
//
//    private void showRegionDialog() {
//
//        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setContentView(R.layout.dialog_picker_list);
//
//        final RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.rlMain);
//        final LinearLayout lnrContainer = (LinearLayout) dialog.findViewById(R.id.lnrContainer);
//        final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
//        final TextView tvDone = (TextView) dialog.findViewById(R.id.tvDone);
//
//        final WheelStringPicker mWeelPicker = (WheelStringPicker) dialog.findViewById(R.id.pickerList);
//        mWeelPicker.updateRegionDataList(mRegionList);
//
//        if (!TextUtils.isEmpty(etRegion.getText().toString())) {
//            int position = 0;
//            for (int i = 0; i < mRegionList.size(); i++) {
//                String mRegionName = "";
//                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    mRegionName = mRegionList.get(i).region;
//                } else {
//                    mRegionName = mRegionList.get(i).regionEn;
//                }
//                if (mRegionName.equalsIgnoreCase(etRegion.getText().toString())) {
//                    position = i;
//                    break;
//                }
//            }
//            mWeelPicker.updateData(position);
//        }
//
//
//        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            lnrContainer.setScaleX(-1f);
//            tvCancel.setScaleX(-1f);
//            mWeelPicker.setScaleX(-1f);
//            tvDone.setScaleX(-1f);
//        } else {
//            lnrContainer.setScaleX(1f);
//            tvCancel.setScaleX(1f);
//            mWeelPicker.setScaleX(1f);
//            tvDone.setScaleX(1f);
//        }
//
//        rlMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        lnrContainer.startAnimation(animClose);
//                    }
//                }, 10);
//
//            }
//        });
//
//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                rlMain.performClick();
//            }
//        });
//
//        tvDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (mRegionList != null && mRegionList.size() > 0) {
//
//                    int position = mWeelPicker.getCurrentItemPosition();
//
//                    String mRegionName = "";
//                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                        mRegionName = mRegionList.get(position).region;
//                    } else {
//                        mRegionName = mRegionList.get(position).regionEn;
//                    }
//
//                    etRegion.setText("" + mRegionName);
//                    etCity.setText("");
//
//                    mStateID = mRegionList.get(position).id;
//
//                    getCityList(mRegionList.get(position).id);
//
//                    rlMain.performClick();
//                }
//
//            }
//        });
//
//        animClose.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (dialog != null && dialog.isShowing()) {
//                            dialog.dismiss();
//                            dialog.cancel();
//                        }
//                    }
//                }, 10);
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        lnrContainer.startAnimation(animOpen);
//        dialog.show();
//
//    }

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

                                mInCountryPosition = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_POSITION, 1);
                                mInCountryCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_CODE, "+966");
                                mInCountryId = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_ID, "SA");

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

    private void addAddress() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        addAddress();
                    }
                }
            });
            return;
        }

        Utils.showProgressDialog(getActivity());
        ApiInterface serviceAPI =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        if (mAddressCount < 1) {
            isDefaultShipping = true;
            isDefaultBilling = true;
        } else {
            isDefaultShipping = cbDefaultShipping.isChecked();
            isDefaultBilling = cbDefaultBilling.isChecked();
        }

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("firstname", "" + etFirstName.getText().toString());
        jsonParams.put("lastname", "" + etLastName.getText().toString());
        jsonParams.put("street", "" + etAddress.getText().toString());
        jsonParams.put("city", "" + etCity.getText().toString());
//        jsonParams.put("postcode", "" + etZipcode.getText().toString());
        jsonParams.put("postcode", "");
        jsonParams.put("country_id", "" + mInCountryId);
        jsonParams.put("default_shipping", "" + isDefaultShipping);
        jsonParams.put("default_billing", "" + isDefaultBilling);
        jsonParams.put("telephone", etPhoneNumber.getText().toString().trim());

//        if (!TextUtils.isEmpty(tvCountryCode.getText().toString()) && tvCountryCode.getText().toString().contains("966")) {
//            jsonParams.put("region", "" + etRegion.getText().toString());
//            jsonParams.put("region_id", "" + mStateID);
//        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        Call<ApiResponse> call = serviceAPI.addAddress(Shy7lo.mLangCode, "Bearer " + userToken, body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    ApiResponse mApiResponse = response.body();
                    if (mApiResponse != null && mApiResponse.success == 1) {
                        Utils.showToast(getActivity(), "" + mApiResponse.message);
                        if (isFromAddAddress) {
                            Intent back = new Intent();
                            back.putExtra("firstname", "" + etFirstName.getText().toString());
                            back.putExtra("lastname", "" + etLastName.getText().toString());
                            back.putExtra("street", "" + etAddress.getText().toString());
                            back.putExtra("city", "" + etCity.getText().toString());
//                            back.putExtra("postcode", "" + etZipcode.getText().toString());
                            back.putExtra("country_id", "" + mInCountryId);
                            back.putExtra("default_shipping", "" + isDefaultShipping);
                            back.putExtra("default_billing", "" + isDefaultBilling);
                            back.putExtra("telephone", etPhoneNumber.getText().toString().trim());
                            setResult(RESULT_OK, back);
                        }
                        finish();
                    } else if (mApiResponse != null && mApiResponse.success == 0) {
                        Utils.showToast(getActivity(), "" + mApiResponse.message);
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
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }

    private void updateAddress(final String mAddressId) {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        updateAddress(mAddressId);
                    }
                }
            });
            return;
        }

        Utils.showProgressDialog(getActivity());
        ApiInterface serviceAPI =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        if (mAddressCount < 1) {
            isDefaultShipping = true;
            isDefaultBilling = true;
        } else {
            isDefaultShipping = cbDefaultShipping.isChecked();
            isDefaultBilling = cbDefaultBilling.isChecked();
        }

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("firstname", "" + etFirstName.getText().toString());
        jsonParams.put("lastname", "" + etLastName.getText().toString());
        jsonParams.put("street", "" + etAddress.getText().toString());
        jsonParams.put("city", "" + etCity.getText().toString());
//        jsonParams.put("postcode", "" + etZipcode.getText().toString());
        jsonParams.put("postcode", "");
        jsonParams.put("country_id", "" + mInCountryId);
        jsonParams.put("default_shipping", "" + isDefaultShipping);
        jsonParams.put("default_billing", "" + isDefaultBilling);
        jsonParams.put("telephone", etPhoneNumber.getText().toString().trim());

//        if (!TextUtils.isEmpty(tvCountryCode.getText().toString()) && tvCountryCode.getText().toString().contains("966")) {
//            jsonParams.put("region", "" + etRegion.getText().toString());
//            jsonParams.put("region_id", "" + mStateID);
//        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        Call<ApiResponse> call = serviceAPI.updateAddress(Shy7lo.mLangCode, "Bearer " + userToken, body, mAddressId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    ApiResponse mApiResponse = response.body();
                    if (mApiResponse != null && mApiResponse.success == 1) {
                        Utils.showToast(getActivity(), "" + mApiResponse.message);
                        if (isFromAddAddress) {
                            Intent back = new Intent();
                            back.putExtra("firstname", "" + etFirstName.getText().toString());
                            back.putExtra("lastname", "" + etLastName.getText().toString());
                            back.putExtra("street", "" + etAddress.getText().toString());
                            back.putExtra("city", "" + etCity.getText().toString());
//                            back.putExtra("postcode", "" + etZipcode.getText().toString());
                            back.putExtra("country_id", "" + mInCountryId);
                            back.putExtra("default_shipping", "" + isDefaultShipping);
                            back.putExtra("default_billing", "" + isDefaultBilling);
                            back.putExtra("telephone", etPhoneNumber.getText().toString().trim());
                            setResult(RESULT_OK, back);
                        }
                        finish();
                    } else if (mApiResponse != null && mApiResponse.success == 0) {
                        Utils.showToast(getActivity(), "" + mApiResponse.message);
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
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }
}
