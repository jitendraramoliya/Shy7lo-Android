package shy7lo.com.shy7lo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.JsonElement;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import shy7lo.com.shy7lo.notification.NotificationUtils;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.service.OneSignalSubcriptionService;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;
import shy7lo.com.shy7lo.wheel.WheelCountryPicker;
import shy7lo.com.shy7lo.wheel.WheelLanguagePicker;
import shy7lo.com.shy7lo.wheel.WheelSizePicker;

import static shy7lo.com.shy7lo.application.Shy7lo.TAG;
import static shy7lo.com.shy7lo.pref.MyPref.getPref;

/**
 * Created by Jiten on 26-06-2018.
 */

public class AccountSettingActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rltTopLayout)
    RelativeLayout rltTopLayout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvLanguage)
    TextView tvLanguage;
    @BindView(R.id.tvCountry)
    TextView tvCountry;
    @BindView(R.id.tvSize)
    TextView tvSize;
    @BindView(R.id.tvLanguageTitle)
    TextView tvLanguageTitle;
    @BindView(R.id.tvCountryTitle)
    TextView tvCountryTitle;
    @BindView(R.id.tvSizeTitle)
    TextView tvSizeTitle;
    @BindView(R.id.tvNotificationTitle)
    TextView tvNotificationTitle;
    @BindView(R.id.tvEmailTitle)
    TextView tvEmailTitle;
    @BindView(R.id.tvPasswordTitle)
    TextView tvPasswordTitle;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvEmailChange)
    TextView tvEmailChange;
    @BindView(R.id.tvNotification)
    TextView tvNotification;
    @BindView(R.id.tbSalesPromotion)
    ToggleButton tbSalesPromotion;
    @BindView(R.id.tvPasswordChange)
    TextView tvPasswordChange;
    @BindView(R.id.lnrSettingContent)
    LinearLayout lnrSettingContent;
    @BindView(R.id.lnrEmail)
    LinearLayout lnrEmail;
    @BindView(R.id.lnrPassword)
    LinearLayout lnrPassword;
    @BindView(R.id.lnrUserInfo)
    LinearLayout lnrUserInfo;
    @BindView(R.id.ibBack)
    ImageButton ibBack;

    private int mInCountryPosition = 1;
    private String mInCountryCode = "+966";
    private String mInCountryId = "SA";
    private float mInExchangeRate = 1f;
    private String mInCurrencyCode = "SAR";
    private String mInCurrencyArCode = "ر.س";

    List<AppInit.Country> mCountryList;

    String[] mLanguageArray = new String[]{"English", "العربية"};

    Animation animClose, animOpen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(getActivity());
        InitializeControls();
        InitializeControlsAction();
    }


    private void InitializeControls() {

        animOpen = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_open);
        animClose = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_close);

        setLanguage();
//        setCountry();
//        setCountryConfig();
        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.countries != null) {
            mCountryList = Shy7lo.mAppInit.countries;
            setCountryConfig();
//            setCountryList();
        } else {
            getInitApi();
        }

//        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.currencies != null) {
//            mExchangeRateList = Shy7lo.mAppInit.currencies.exchangeRates;
//        } else {
//            getInitApi();
//        }

        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        if (TextUtils.isEmpty(userToken)) {
            lnrUserInfo.setVisibility(View.GONE);
        } else {

            lnrUserInfo.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.USER_EMAIL, ""))) {
                etEmail.setText(MyPref.getPref(getActivity(), MyPref.USER_EMAIL, ""));
            }

            if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.USER_PWD, ""))) {
                etPassword.setText(MyPref.getPref(getActivity(), MyPref.USER_PWD, ""));
            }
        }
    }


    public void setViewByLanguge() {
        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            rltTopLayout.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            lnrSettingContent.setScaleX(-1f);
            tvNotification.setScaleX(-1f);
            tbSalesPromotion.setScaleX(-1f);
            tvEmailChange.setScaleX(-1f);
            tvPasswordChange.setScaleX(-1f);
            etEmail.setScaleX(-1f);
            etPassword.setScaleX(-1f);
            tvLanguageTitle.setScaleX(-1f);
            tvCountryTitle.setScaleX(-1f);
            tvSizeTitle.setScaleX(-1f);
            tvNotificationTitle.setScaleX(-1f);
            tvEmailTitle.setScaleX(-1f);
            tvPasswordTitle.setScaleX(-1f);
            tvLanguage.setScaleX(-1f);
            tvCountry.setScaleX(-1f);
            tvSize.setScaleX(-1f);
            etEmail.setGravity(Gravity.RIGHT);
            etPassword.setGravity(Gravity.RIGHT);

            tvLanguage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_details_arrow_down, 0, 0, 0);
            tvCountry.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_details_arrow_down, 0, 0, 0);
            tvSize.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_details_arrow_down, 0, 0, 0);

        } else {
            rltTopLayout.setScaleX(1f);
            tvTitle.setScaleX(1f);

            lnrSettingContent.setScaleX(1f);
            tvNotification.setScaleX(1f);
            tbSalesPromotion.setScaleX(1f);
            tvEmailChange.setScaleX(1f);
            tvPasswordChange.setScaleX(1f);
            etEmail.setScaleX(1f);
            etPassword.setScaleX(1f);
            tvLanguageTitle.setScaleX(1f);
            tvCountryTitle.setScaleX(1f);
            tvSizeTitle.setScaleX(1f);
            tvNotificationTitle.setScaleX(1f);
            tvEmailTitle.setScaleX(1f);
            tvPasswordTitle.setScaleX(1f);
            tvLanguage.setScaleX(1f);
            tvCountry.setScaleX(1f);
            tvSize.setScaleX(1f);
            etEmail.setGravity(Gravity.LEFT);
            etPassword.setGravity(Gravity.LEFT);

            tvLanguage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_details_arrow_down, 0);
            tvCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_details_arrow_down, 0);
            tvSize.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_details_arrow_down, 0);
        }

//        tvLanguage.setText(getString(R.string.language));
        tvLanguageTitle.setText(getString(R.string.language));
//        tvCountry.setText(getString(R.string.country));
        tvCountryTitle.setText(getString(R.string.country));
        tvSize.setText(getString(R.string.sizes));
        tvSizeTitle.setText(getString(R.string.sizes));
        tvNotification.setText(getString(R.string.sales_promotions));
        tvNotificationTitle.setText(getString(R.string.notifications));
        tvEmailTitle.setText(getString(R.string.e_mail));
        tvEmailChange.setText(getString(R.string.change_email));
        etEmail.setHint(getString(R.string.e_mail));
        tvTitle.setText(getString(R.string.settings));
        tvPasswordTitle.setText(getString(R.string.password));
        tvPasswordChange.setText(getString(R.string.change_password));
        etPassword.setHint(getString(R.string.password));

        setCountryConfig();

    }

    private void InitializeControlsAction() {
        ibBack.setOnClickListener(this);
        tvCountry.setOnClickListener(this);
        tvLanguage.setOnClickListener(this);
        tvEmailChange.setOnClickListener(this);
        tvPasswordChange.setOnClickListener(this);
        tvSize.setOnClickListener(this);

        if (MyPref.getPref(getActivity(), MyPref.PREF_ONE_SIGNAL_IS_SUBCRIBED, true)) {
            tbSalesPromotion.setChecked(true);
        } else {
            tbSalesPromotion.setChecked(false);
        }

        tbSalesPromotion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                MyPref.setPref(getActivity(), MyPref.PREF_ONE_SIGNAL_IS_SUBCRIBED, isChecked);
                OneSignal.setSubscription(isChecked);
            }
        });
    }

    public void setLanguage() {
        LogUtils.e("", "setLanguage");
        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            if (tvLanguage != null && mLanguageArray != null) {
                tvLanguage.setText("" + mLanguageArray[1]);
            }
        } else {
            if (tvLanguage != null && mLanguageArray != null) {
                tvLanguage.setText("" + mLanguageArray[0]);
            }
        }

        setViewByLanguge();
    }

//    public void setCountry() {
//        LogUtils.e("", "setCountry");
//        int mCountryPosition = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_POSITION, 1);
//
//        if (mCountryPosition == 1) {
//            tvCountry.setText("" + getString(R.string.saudi_arabia));
//        } else if (mCountryPosition == 2) {
//            tvCountry.setText("" + getString(R.string.uae));
//        } else if (mCountryPosition == 3) {
//            tvCountry.setText("" + getString(R.string.qatar));
//        } else if (mCountryPosition == 4) {
//            tvCountry.setText("" + getString(R.string.kuwait));
//        } else if (mCountryPosition == 5) {
//            tvCountry.setText("" + getString(R.string.oman));
//        } else if (mCountryPosition == 6) {
//            tvCountry.setText("" + getString(R.string.bahrin));
//        }
//    }

    private Activity getActivity() {
        return AccountSettingActivity.this;
    }

    @Override
    public void onClick(View view) {
        if (view == ibBack) {
            finish();
        } else if (view == tvLanguage) {
            showLanguageDialog();
        } else if (view == tvCountry) {
            showCountryDialog();
        } else if (view == tvPasswordChange) {
            showChangePasswordDialog();
        } else if (view == tvSize) {
            showSizeDialog();
        }
    }

    private void showLanguageDialog() {

//        final Dialog dialog = new Dialog(new ContextThemeWrapper(getActivity(), R.style.DialogSlideAnim));
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_language_list);

        final RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.rlMain);
        final LinearLayout lnrContainer = (LinearLayout) dialog.findViewById(R.id.lnrContainer);
        final TextView tvLanguage = (TextView) dialog.findViewById(R.id.tvLanguage);
        final TextView tvDone = (TextView) dialog.findViewById(R.id.tvDone);

        final WheelLanguagePicker pickerLanguage = (WheelLanguagePicker) dialog.findViewById(R.id.pickerLanguage);

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            pickerLanguage.setSelectedItemPosition(1);
        } else {
            pickerLanguage.setSelectedItemPosition(0);
        }


        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            lnrContainer.setScaleX(-1f);
            tvLanguage.setScaleX(-1f);
            pickerLanguage.setScaleX(-1f);
            tvDone.setScaleX(-1f);
        } else {
            lnrContainer.setScaleX(1f);
            tvLanguage.setScaleX(1f);
            pickerLanguage.setScaleX(1f);
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

                switch (pickerLanguage.getCurrentItemPosition()) {
                    case 0:

                        setLocale("en");
                        MyPref.setPref(getActivity(), MyPref.DEFAULT_LANGUAGE, MyPref.LANGUAGE_English);
                        MyPref.setPref(getActivity(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN);
                        Shy7lo.mLangCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN);
                        MyPref.setPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "" + mInCurrencyCode);
                        setLanguage();
                        setOnsSignalSubcription();
                        Utils.getCMSPage(getActivity());
                        MyPref.setPref(getActivity(), MyPref.IS_LANGUAGE_SELECT, true);
                        break;
                    case 1:

                        setLocale("ar");
                        MyPref.setPref(getActivity(), MyPref.DEFAULT_LANGUAGE, MyPref.LANGUAGE_Arabic);
                        MyPref.setPref(getActivity(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_AR);
                        Shy7lo.mLangCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN);
                        MyPref.setPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "" + mInCurrencyArCode);
                        setLanguage();
                        setOnsSignalSubcription();
                        Utils.getCMSPage(getActivity());
                        MyPref.setPref(getActivity(), MyPref.IS_LANGUAGE_SELECT, true);
                        break;
                }

                LogUtils.e("", "DEFAULT_CURRENCY_CODE::" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "SAR"));

                Intent back = new Intent();
                setResult(RESULT_OK, back);

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

    private void showChangePasswordDialog() {

//        final Dialog dialog = new Dialog(new ContextThemeWrapper(getActivity(), R.style.DialogSlideAnim));
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_change_password);

        final RelativeLayout rltTitle = (RelativeLayout) dialog.findViewById(R.id.rltTitle);
        final ImageButton ibCancel = (ImageButton) dialog.findViewById(R.id.ibCancel);
        final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        final TextView tvDone = (TextView) dialog.findViewById(R.id.tvDone);
        final EditText etCurrentPwd = (EditText) dialog.findViewById(R.id.etCurrentPwd);
        final EditText etNewPwd = (EditText) dialog.findViewById(R.id.etNewPwd);
        final EditText etConfirmPwd = (EditText) dialog.findViewById(R.id.etConfirmPwd);


        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            rltTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            ibCancel.setScaleX(-1f);
            tvDone.setScaleX(-1f);
            etCurrentPwd.setGravity(Gravity.RIGHT);
            etNewPwd.setGravity(Gravity.RIGHT);
            etConfirmPwd.setGravity(Gravity.RIGHT);
        } else {
            rltTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);
            ibCancel.setScaleX(1f);
            tvDone.setScaleX(1f);
            etCurrentPwd.setGravity(Gravity.LEFT);
            etNewPwd.setGravity(Gravity.LEFT);
            etConfirmPwd.setGravity(Gravity.LEFT);
        }

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }

            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (TextUtils.isEmpty(etCurrentPwd.getText().toString())) {
                    Utils.showToast(getActivity(), "" + getString(R.string.msg_enter_current_pwd));
                    return;
                }

//                if (!etCurrentPwd.getText().toString().equals(MyPref.getPref(getActivity(), USER_PWD, ""))) {
//                    Utils.showToast(getActivity(), "" + getString(R.string.msg_wrong_current_pwd));
//                    return;
//                }

                if (TextUtils.isEmpty(etNewPwd.getText().toString())) {
                    Utils.showToast(getActivity(), "" + getString(R.string.msg_enter_new_pwd));
                    return;
                }

                if (TextUtils.isEmpty(etConfirmPwd.getText().toString())) {
                    Utils.showToast(getActivity(), "" + getString(R.string.msg_enter_confirm_pwd));
                    return;
                }

                if (!etNewPwd.getText().toString().equals(etConfirmPwd.getText().toString())) {
                    Utils.showToast(getActivity(), "" + getString(R.string.msg_pwd_not_match));
                    return;
                }

                if (!Utils.isInternetConnected(getActivity())) {
                    Utils.showToast(getActivity(), getString(R.string.offline_text));
                    return;
                }


                Utils.showProgressDialog(getActivity());

                ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
                Map<String, Object> jsonParams = new ArrayMap<>();
                jsonParams.put("current_password", "" + etCurrentPwd.getText().toString());
                jsonParams.put("new_password", "" + etNewPwd.getText().toString());

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

                String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
                Call<ApiResponse> changePasswordCall = serviceAPI.changePassword(Shy7lo.mLangCode, "Bearer " + userToken, body);
                changePasswordCall.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        Utils.closeProgressDialog();
                        if (response.isSuccessful()) {
                            ApiResponse mApiResponse = response.body();
                            if (mApiResponse != null && mApiResponse.success == 1) {
                                Utils.showToast(getActivity(), "" + mApiResponse.message);
                                ibCancel.performClick();
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
        });

        dialog.show();
    }

    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        LogUtils.e("", "language::" + getResources().getConfiguration().locale);

    }

    private void setOnsSignalSubcription() {

        LogUtils.e("", "ONE_SIGNAL_IS_SUBCRIBED::" + getPref(getActivity(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, false));

//                if (!MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, false)) {
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        if (status != null) {
            boolean isEnabled = status.getPermissionStatus().getEnabled();

            boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
            String mUserID = status.getSubscriptionStatus().getUserId();
            String mPushToken = status.getSubscriptionStatus().getPushToken();

            LogUtils.e("", "Landing isEnabled: " + isEnabled + "\nisSubscribed: " + isSubscribed);
            LogUtils.e("", "Landing PlayerID: " + mUserID + "\nPushToken: " + mPushToken);

            MyPref.setPref(getActivity(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, isSubscribed);
            if (isSubscribed) {
                if (!TextUtils.isEmpty(mUserID)) {
                    MyPref.setPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, mUserID);
                }
//                        if (!TextUtils.isEmpty(mPushToken)) {
//                            MyPref.setPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, mPushToken);
//                        }
            }
        }

        if (MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, false)) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(NotificationUtils.SHARED_PREF, 0);

            Map<String, Object> jsonParams = new ArrayMap<>();
            jsonParams.put("device_token", "" + pref.getString("regId", ""));
//                    jsonParams.put("device_token", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
            jsonParams.put("device_type", "android");
            jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

            ApiInterface apiService =
                    RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
            Call<JsonElement> callCode = apiService.registerPushToken(Shy7lo.mLangCode, body);
            callCode.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    if (response.isSuccessful()) {
                        MyPref.setPref(getActivity(), MyPref.ONE_SIGNAL_IS_REGISTERED, true);
//                                Utils.showToast(getActivity(), "Register Token Api Called: " + MyPref.getPref(getApplicationContext(), MyPref.ONE_SIGNAL_USER_ID, ""));
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {

                }
            });
        } else {
            if (!Utils.isMyServiceRunning(getActivity(), OneSignalSubcriptionService.class)) {
                IntentHandler.startService(getApplicationContext(), OneSignalSubcriptionService.class);
            }
        }
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

                LogUtils.e("", "mInCurrencyCode::" + mInCurrencyCode + " mInCurrencyArCode::" + mInCurrencyArCode);

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

    private void setCountryConfig() {

        mInCountryPosition = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_POSITION, 1);
        mInCurrencyCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "SAR");
        mInCurrencyArCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_ARABIC_CURRENCY_CODE, "ر.س");
        mInExchangeRate = MyPref.getPref(getActivity(), MyPref.DEFAULT_EXCHANGE_RATE, 1f);

        mInCountryCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_CODE, "+966");
        mInCountryId = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_ID, "SA");

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

    private void showSizeDialog() {

        final Animation animClose, animOpen;
        animOpen = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_open);
        animClose = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_close);

//        final Dialog dialog = new Dialog(new ContextThemeWrapper(getActivity(), R.style.DialogSlideAnim));
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_size_list);

        final RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.rlMain);
        final LinearLayout lnrContainer = (LinearLayout) dialog.findViewById(R.id.lnrContainer);
        final TextView tvSelectSize = (TextView) dialog.findViewById(R.id.tvSelectSize);
        final TextView tvDone = (TextView) dialog.findViewById(R.id.tvDone);

        tvSelectSize.setText("" + getString(R.string.sizes));

        final WheelSizePicker pickerSize = (WheelSizePicker) dialog.findViewById(R.id.pickerSize);

        final ArrayList<String> mSizeList = new ArrayList<>();
        mSizeList.add("International");
        mSizeList.add("UK");
        mSizeList.add("EU");

        if (mSizeList != null && mSizeList.size() > 0) {
            pickerSize.updateSettinsSize(mSizeList);
            String mSize = MyPref.getPref(getActivity(), MyPref.DEFAULT_SIZE, "");

            if (!TextUtils.isEmpty(mSize)) {
                for (int i = 0; i < mSizeList.size(); i++) {
                    if (mSize.equalsIgnoreCase(mSizeList.get(i))) {
                        pickerSize.updateIndex(i);
                    }
                }
            } else {
                pickerSize.updateIndex(0);
            }

        }

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            lnrContainer.setScaleX(-1f);
            tvSelectSize.setScaleX(-1f);
            pickerSize.setScaleX(-1f);
            tvDone.setScaleX(-1f);
        } else {
            lnrContainer.setScaleX(1f);
            tvSelectSize.setScaleX(1f);
            pickerSize.setScaleX(1f);
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

                MyPref.setPref(getActivity(), MyPref.DEFAULT_SIZE, mSizeList.get(pickerSize.getCurrentItemPosition()));

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
}
