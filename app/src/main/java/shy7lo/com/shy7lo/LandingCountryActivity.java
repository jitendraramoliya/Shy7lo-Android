package shy7lo.com.shy7lo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.AppInit;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;
import shy7lo.com.shy7lo.wheel.WheelCountryPicker;

/**
 * Created by JitenRamen on 20-08-2016.
 */
public class LandingCountryActivity extends Activity implements View.OnClickListener {

    private final String TAG = "LandingLanguageActivity";

    @BindView(R.id.tvSkip)
    TextView tvSkip;
    @BindView(R.id.tvGenuine)
    TextView tvFashShipping;
    @BindView(R.id.tvHighQuality)
    TextView tvGuaranteed;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.tvShoppingFor)
    TextView tvChooseCountry;
    @BindView(R.id.ivCountryMap)
    ImageView ivCountryMap;
    @BindView(R.id.pickerCountry)
    WheelCountryPicker pickerCountry;

    private int mCountryPosition = 1;
    private String mCountryCode = "+966";
    private String mCountryId = "SA";
    private float mExchangeRate = 1f;
    private String mCurrencyCode = "SAR";
    private String mCurrencyArCode = "ر.س";
    private int mDefaultCountryPos = 0;

    private Activity getActivity() {
        return LandingCountryActivity.this;
    }

    List<AppInit.Country> mCountryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_landing_country);
        ButterKnife.bind(getActivity());

        InitializeConrols();
        InitializeConrolsAction();

    }

    private void InitializeConrols() {

        tvSkip.setTypeface(Shy7lo.SFUIDisplayRegular);
        tvFashShipping.setTypeface(Shy7lo.SFUIDisplayBold);
        tvGuaranteed.setTypeface(Shy7lo.SFUIDisplayRegular);
        tvChooseCountry.setTypeface(Shy7lo.SFUIDisplayRegular);
        tvNext.setTypeface(Shy7lo.TahomaBold);

        pickerCountry.setOnCountrySelectedListener(new WheelCountryPicker.OnCountrySelectedListener() {
            @Override
            public void onCountrySelected(WheelCountryPicker picker, int position, String name) {
                setCountryMap(position);
            }
        });

        pickerCountry.setOnCountryScrollListener(new WheelCountryPicker.OnCountryScrollListener() {
            @Override
            public void onCountryScroll(WheelCountryPicker picker, int position, String name) {
                setCountryMap(position);
            }
        });


//        if (Utils.isInternetConnected(getActivity())) {
//            getInitApi();
//        }else{
        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.countries != null) {
            mCountryList = Shy7lo.mAppInit.countries;
            setCountryList();
        } else {
            getInitApi();
        }
//        }

    }

    private void setCountryMap(int position) {

        if (mCountryList != null && mCountryList.size() > 0) {

            if (mCountryList.get(position).id.equalsIgnoreCase("SA")) {

                ivCountryMap.setImageResource(R.drawable.ic_map_saudi_arabia);
                mCountryPosition = 1;
                mCountryCode = "966";

            } else if (mCountryList.get(position).id.equalsIgnoreCase("AE")) {

                ivCountryMap.setImageResource(R.drawable.ic_map_uae);
                mCountryPosition = 2;
                mCountryCode = "971";

            } else if (mCountryList.get(position).id.equalsIgnoreCase("QA")) {

                ivCountryMap.setImageResource(R.drawable.ic_map_qatar);
                mCountryPosition = 3;
                mCountryCode = "974";

            } else if (mCountryList.get(position).id.equalsIgnoreCase("KW")) {

                ivCountryMap.setImageResource(R.drawable.ic_map_kuwait);
                mCountryPosition = 4;
                mCountryCode = "965";

            } else if (mCountryList.get(position).id.equalsIgnoreCase("OM")) {

                ivCountryMap.setImageResource(R.drawable.ic_map_oman);
                mCountryPosition = 5;
                mCountryCode = "968";

            } else if (mCountryList.get(position).id.equalsIgnoreCase("BH")) {

                ivCountryMap.setImageResource(R.drawable.ic_map_oman);
                mCountryPosition = 5;
                mCountryCode = "973";

            }

            mCountryId = "" + mCountryList.get(position).id;
            mCurrencyCode = "" + mCountryList.get(position).currencyEn;
            mCurrencyArCode = "" + mCountryList.get(position).currencyAr;
            mExchangeRate = mCountryList.get(position).exchangeRate;

        }

//        switch (position) {
//            case 0:
//                ivCountryMap.setImageResource(R.drawable.ic_map_saudi_arabia);
//                mCountryPosition = 1;
//                mCountryCode = "966";
//                mCountryId = "SA";
//                mCurrencyCode = "SAR";
//                mExchangeRate = 1f;
//
//                if (mCountryList != null && mCountryList.size() > 0) {
//                    for (int i = 0; i < mCountryList.size(); i++) {
//                        if (mCountryList.get(i).currencyTo.equalsIgnoreCase("SAR")) {
//                            mExchangeRate = mCountryList.get(i).rate;
//                            break;
//                        }
//                    }
//                }
//
//                break;
//            case 1:
//                ivCountryMap.setImageResource(R.drawable.ic_map_uae);
//                mCountryPosition = 2;
//                mCountryCode = "971";
//                mCountryId = "AE";
//                mCurrencyCode = "AED";
//                mExchangeRate = 0.9789f;
//
//                if (mCountryList != null && mCountryList.size() > 0) {
//                    for (int i = 0; i < mCountryList.size(); i++) {
//                        if (mCountryList.get(i).currencyTo.equalsIgnoreCase("AED")) {
//                            mExchangeRate = mCountryList.get(i).rate;
//                            break;
//                        }
//                    }
//                }
//
//                break;
//            case 2:
//                ivCountryMap.setImageResource(R.drawable.ic_map_qatar);
//                mCountryPosition = 3;
//                mCountryCode = "974";
//                mCountryId = "QA";
//                mCurrencyCode = "QAR";
//                mExchangeRate = 0.971f;
//
//                if (mCountryList != null && mCountryList.size() > 0) {
//                    for (int i = 0; i < mCountryList.size(); i++) {
//                        if (mCountryList.get(i).currencyTo.equalsIgnoreCase("QAR")) {
//                            mExchangeRate = mCountryList.get(i).rate;
//                            break;
//                        }
//                    }
//                }
//
//                break;
//            case 3:
//                ivCountryMap.setImageResource(R.drawable.ic_map_kuwait);
//                mCountryPosition = 4;
//                mCountryCode = "965";
//                mCountryId = "KW";
//                mCurrencyCode = "KWD";
//                mExchangeRate = 0.0809f;
//
//                if (mCountryList != null && mCountryList.size() > 0) {
//                    for (int i = 0; i < mCountryList.size(); i++) {
//                        if (mCountryList.get(i).currencyTo.equalsIgnoreCase("KWD")) {
//                            mExchangeRate = mCountryList.get(i).rate;
//                            break;
//                        }
//                    }
//                }
//
//                break;
//            case 4:
//                ivCountryMap.setImageResource(R.drawable.ic_map_oman);
//                mCountryPosition = 5;
//                mCountryCode = "968";
//                mCountryId = "OM";
//                mCurrencyCode = "OMR";
//                mExchangeRate = 0.1026f;
//
//                if (mCountryList != null && mCountryList.size() > 0) {
//                    for (int i = 0; i < mCountryList.size(); i++) {
//                        if (mCountryList.get(i).currencyTo.equalsIgnoreCase("OMR")) {
//                            mExchangeRate = mCountryList.get(i).rate;
//                            break;
//                        }
//                    }
//                }
//
//                break;
//            case 5:
//                ivCountryMap.setImageResource(R.drawable.ic_map_bahrain);
//                mCountryPosition = 6;
//                mCountryCode = "973";
//                mCountryId = "BH";
//                mCurrencyCode = "BHD";
//                mExchangeRate = 0.1005f;
//
//                if (mCountryList != null && mCountryList.size() > 0) {
//                    for (int i = 0; i < mCountryList.size(); i++) {
//                        if (mCountryList.get(i).currencyTo.equalsIgnoreCase("BHD")) {
//                            mExchangeRate = mCountryList.get(i).rate;
//                            break;
//                        }
//                    }
//                }
//
//                break;
//        }
    }

    private void InitializeConrolsAction() {
        tvSkip.setOnClickListener(this);
        tvNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == tvNext) {
//            String mCode = "";
//            if (mCurrencyCode.equalsIgnoreCase("SAR")) {
//                mCode = getResources().getString(R.string.SAR);
//            } else if (mCurrencyCode.equalsIgnoreCase("AED")) {
//                mCode = getResources().getString(R.string.AED);
//            } else if (mCurrencyCode.equalsIgnoreCase("QAR")) {
//                mCode = getResources().getString(R.string.QAR);
//            } else if (mCurrencyCode.equalsIgnoreCase("OMR")) {
//                mCode = getResources().getString(R.string.OMR);
//            } else if (mCurrencyCode.equalsIgnoreCase("KWD")) {
//                mCode = getResources().getString(R.string.KWD);
//            } else if (mCurrencyCode.equalsIgnoreCase("BHD")) {
//                mCode = getResources().getString(R.string.BHD);
//            }

           // LogUtils.e("", "mCurrencyCode::" + mCurrencyCode + " mCurrencyArCode::" + mCurrencyArCode);

            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                MyPref.setPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "" + mCurrencyArCode);
            } else {
                MyPref.setPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "" + mCurrencyCode);
            }
            LogUtils.e("", "DEFAULT_CURRENCY_CODE::" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "SAR"));

            MyPref.setPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "" + mCurrencyCode);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_ARABIC_CURRENCY_CODE, "" + mCurrencyArCode);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_EXCHANGE_RATE, mExchangeRate);

            MyPref.setPref(getActivity(), MyPref.DEFAULT_COUNTRY_POSITION, mCountryPosition);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_COUNTRY_CODE, "" + mCountryCode);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_COUNTRY_ID, "" + mCountryId);

//            IntentHandler.startActivity(getActivity(), LandingCurrencyActivity.class);
           // IntentHandler.startActivity(getActivity(), LandingShoppingForActivity.class);
           // finish();

            if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.couponCode != null && !TextUtils.isEmpty(Shy7lo.mAppInit.couponCode.status)
                    && Shy7lo.mAppInit.couponCode.status.equals("1")) {
                Bundle bundle = new Bundle();
                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    bundle.putString(DirectWebviewActivity.BNDL_URL, Shy7lo.mAppInit.couponCode.url_ar);
                } else {
                    bundle.putString(DirectWebviewActivity.BNDL_URL, Shy7lo.mAppInit.couponCode.url_en);
                }

                IntentHandler.startActivity(getActivity(), DirectWebviewActivity.class, bundle);

            } else {
                IntentHandler.startActivity(getActivity(), HomeActivity.class);
            }
//            IntentHandler.startActivity(getActivity(), InitialScreenActivity.class);
            MyPref.setPref(getActivity(), MyPref.IS_FIRST_TIME, false);
            finish();

        } else if (v == tvSkip) {

            mCountryPosition = 1;
            mCountryCode = "+966";
            if (mCountryList != null) {
                for (int i = 0; i < mCountryList.size(); i++) {
                    if (mCountryList.get(i).id.equals("SA")) {

                        mCountryId = "" + mCountryList.get(i).id;
                        mCurrencyCode = "" + mCountryList.get(i).currencyEn;
                        mCurrencyArCode = "" + mCountryList.get(i).currencyAr;
                        mExchangeRate = mCountryList.get(i).exchangeRate;
                    }
                }
            } else {
                mCountryId = "SA";
                mExchangeRate = 1f;
                mCurrencyCode = "SAR";
                mCurrencyArCode = "ر.س";
            }

//            String mCode = getResources().getString(R.string.SAR);
            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                MyPref.setPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "" + mCurrencyArCode);
            } else {
                MyPref.setPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "" + mCurrencyCode);
            }
            LogUtils.e("", "DEFAULT_CURRENCY_CODE::" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "SAR"));

            MyPref.setPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "" + mCurrencyCode);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_ARABIC_CURRENCY_CODE, "" + mCurrencyArCode);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_EXCHANGE_RATE, mExchangeRate);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_COUNTRY_POSITION, mCountryPosition);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_COUNTRY_CODE, "" + mCountryCode);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_COUNTRY_ID, "" + mCountryId);

            if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.couponCode != null && !TextUtils.isEmpty(Shy7lo.mAppInit.couponCode.status)
                    && Shy7lo.mAppInit.couponCode.status.equals("1")) {
                Bundle bundle = new Bundle();
                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    bundle.putString(DirectWebviewActivity.BNDL_URL, Shy7lo.mAppInit.couponCode.url_ar);
                } else {
                    bundle.putString(DirectWebviewActivity.BNDL_URL, Shy7lo.mAppInit.couponCode.url_en);
                }

                IntentHandler.startActivity(getActivity(), DirectWebviewActivity.class, bundle);

            } else {
                IntentHandler.startActivity(getActivity(), HomeActivity.class);
            }
//            IntentHandler.startActivity(getActivity(), InitialScreenActivity.class);
            MyPref.setPref(getActivity(), MyPref.IS_FIRST_TIME, false);
            finish();
        }
    }

    private void setCountryList() {

        if (mCountryList != null && mCountryList.size() > 0) {
            pickerCountry.updateCountryList(mCountryList);
            for (int i = 0; i < mCountryList.size(); i++) {
                if (mCountryList.get(i).id.equals("SA")) {
                    pickerCountry.updateCountry(i);
                    mDefaultCountryPos = i;
                }
            }
        }
    }

    private void getInitApi() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getInitApi();
                    }
                }
            });
            return;
        }

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("device_type", "android");
        jsonParams.put("app_version", "" + BuildConfig.VERSION_NAME);
        jsonParams.put("device_token", "" + Utils.getDeviceToken(this));
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
                            if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.currencies != null) {
                                mCountryList = Shy7lo.mAppInit.countries;
                                setCountryList();
                            }
                        }

                        AppInit.Integrations mIntegrations = Shy7lo.mAppInit.integrations;
                        if (mIntegrations != null) {


                            LogUtils.e("", "mIntegrations.tune::" + mIntegrations.tune);
                            LogUtils.e("", "mIntegrations.fabric::" + mIntegrations.fabric);
                            LogUtils.e("", "mIntegrations.oneSginal::" + mIntegrations.oneSginal);
                            LogUtils.e("", "mIntegrations.criteo::" + mIntegrations.criteo);

                            MyPref.setPref(getActivity(), MyPref.APP_TUNE, mIntegrations.tune);
                            MyPref.setPref(getActivity(), MyPref.APP_FABRIC, mIntegrations.fabric);
                            MyPref.setPref(getActivity(), MyPref.APP_ONE_SIGNAL, mIntegrations.oneSginal);
                            MyPref.setPref(getActivity(), MyPref.APP_CRITEO, mIntegrations.criteo);
                            MyPref.setPref(getActivity(), MyPref.APP_ADJUST, mIntegrations.adjust);

                            Utils.setInitConfig(getActivity());
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


}
