package shy7lo.com.shy7lo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

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
import shy7lo.com.shy7lo.wheel.WheelShoppingForPicker;

/**
 * Created by JitenRamen on 20-08-2016.
 */
public class LandingShoppingForActivity extends Activity implements View.OnClickListener {

    private final String TAG = "LandingShoppingForActivity";

    @BindView(R.id.tvSkip)
    TextView tvSkip;
    @BindView(R.id.tvGenuine)
    TextView tvGenuine;
    @BindView(R.id.tvHighQuality)
    TextView tvHighQuality;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.tvShoppingFor)
    TextView tvShoppingFor;
    @BindView(R.id.pickerShoppingFor)
    WheelShoppingForPicker pickerShoppingFor;

    private int mCategoryPosition = 0;

    List<AppInit.BaseScreen> mBaseScreenList;

    private Activity getActivity() {
        return LandingShoppingForActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_landing_shopping_for);
        ButterKnife.bind(getActivity());

        InitializeConrols();
        InitializeConrolsAction();

    }

    private void InitializeConrols() {

        tvSkip.setTypeface(Shy7lo.SFUIDisplayRegular);
        tvGenuine.setTypeface(Shy7lo.SFUIDisplayBold);
        tvHighQuality.setTypeface(Shy7lo.SFUIDisplayRegular);
        tvShoppingFor.setTypeface(Shy7lo.SFUIDisplayRegular);
        tvNext.setTypeface(Shy7lo.TahomaBold);

        // For One signal
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        if (status != null) {
            boolean isEnabled = status.getPermissionStatus().getEnabled();

            boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
            String mUserID = status.getSubscriptionStatus().getUserId();
            String mPushToken = status.getSubscriptionStatus().getPushToken();

            LogUtils.e("", "Initial isEnabled: " + isEnabled + "\nisSubscribed: " + isSubscribed);
            LogUtils.e("", "Initial PlayerID: " + mUserID + "\nPushToken: " + mPushToken);

            MyPref.setPref(getActivity(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, isSubscribed);
            if (isSubscribed) {
                if (!TextUtils.isEmpty(mUserID)) {
                    MyPref.setPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, mUserID);
                }
//                if (!TextUtils.isEmpty(mPushToken)) {
//                    MyPref.setPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, mPushToken);
//                }
            }
        }


        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.landingScreens != null) {
            mBaseScreenList = Shy7lo.mAppInit.landingScreens.baseScreens;
            LogUtils.e("", "mBaseScreenList size::" + mBaseScreenList.size());
            if (mBaseScreenList != null && mBaseScreenList.size() > 0) {
                pickerShoppingFor.updateShoppingFor(mBaseScreenList);
            }
        } else {
            getInitApi();
        }


    }

    private void InitializeConrolsAction() {
        tvSkip.setOnClickListener(this);
        tvNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == tvNext) {
            MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, pickerShoppingFor.getCurrentItemPosition());
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
            MyPref.setPref(getActivity(), MyPref.IS_FIRST_TIME, false);
            finish();

        } else if (v == tvSkip) {

            MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0);
//            IntentHandler.startActivity(getActivity(), InitialScreenActivity.class);
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
            MyPref.setPref(getActivity(), MyPref.IS_FIRST_TIME, false);
            finish();

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
                            if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.landingScreens != null) {
                                mBaseScreenList = Shy7lo.mAppInit.landingScreens.baseScreens;
                                if (mBaseScreenList != null && mBaseScreenList.size() > 0) {
                                    pickerShoppingFor.updateShoppingFor(mBaseScreenList);
                                }
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
