package shy7lo.com.shy7lo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONObject;

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
import shy7lo.com.shy7lo.notification.NotificationUtils;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.service.OneSignalSubcriptionService;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

import static shy7lo.com.shy7lo.pref.MyPref.getPref;

/**
 * Created by JitenRamen on 20-08-2016.
 */
public class LandingLanguageActivity extends Activity implements View.OnClickListener {

    private final String TAG = "LandingLanguageActivity";

    @BindView(R.id.tvWelcome)
    TextView tvWelcome;
    @BindView(R.id.tvYourStyle)
    TextView tvYourStyle;
    @BindView(R.id.tvHaveAccount)
    TextView tvHaveAccount;
    @BindView(R.id.tvEnglish)
    TextView tvEnglish;
    @BindView(R.id.tvArabic)
    TextView tvArabic;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.ivLangLogo)
    ImageView ivLangLogo;

    public static String FILTER_FINISH = "FILTER_FINISH";

    private Activity getActivity() {
        return LandingLanguageActivity.this;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_landing_language);
        ButterKnife.bind(getActivity());

        InitializeConrols();
        InitializeConrolsAction();

    }

    private void InitializeConrols() {

        tvWelcome.setTypeface(Shy7lo.SFUIDisplayBold);
        tvYourStyle.setTypeface(Shy7lo.SFUIDisplayRegular);
        tvHaveAccount.setTypeface(Shy7lo.SFUIDisplayRegular);
        tvEnglish.setTypeface(Shy7lo.TahomaBold);
        tvArabic.setTypeface(Shy7lo.TahomaBold);
        tvLogin.setTypeface(Shy7lo.SFUIDisplaySemibold);

        registerReceiver(mFinishReceiver, new IntentFilter(FILTER_FINISH));

        SpannableString content = new SpannableString("" + tvLogin.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvLogin.setText(content);

        // set Logo According Language
        if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, ""))) {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
                ivLangLogo.setImageResource(R.drawable.logo_english);
            } else {
                ivLangLogo.setImageResource(R.drawable.logo_arabic);
            }
        } else {
            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, MyPref.LANGUAGE_Arabic).equals(MyPref.LANGUAGE_Arabic)) {
                ivLangLogo.setImageResource(R.drawable.logo_arabic);
            } else {
                ivLangLogo.setImageResource(R.drawable.logo_english);
            }
        }

    }

    private void InitializeConrolsAction() {
        tvEnglish.setOnClickListener(this);
        tvArabic.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFinishReceiver != null){
            unregisterReceiver(mFinishReceiver);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == tvLogin) {
            IntentHandler.startActivity(getActivity(), SignInActivity.class);
        } else if (v == tvEnglish) {
            MyPref.setPref(getActivity(), MyPref.DEFAULT_LANGUAGE, MyPref.LANGUAGE_English);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN);
            LogUtils.e("", "DEFAULT_LANGUAGE_CODE::"+MyPref.getPref(this, MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN));
            setLocale("en");
            Shy7lo.mLangCode = MyPref.getPref(this, MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN);
            setOnsSignalSubcription();
            Utils.getCMSPage(getActivity());
            MyPref.setPref(getActivity(), MyPref.IS_LANGUAGE_SELECT, true);

            IntentHandler.startActivity(getActivity(), LandingCountryActivity.class);
            finish();
        } else if (v == tvArabic) {
            MyPref.setPref(getActivity(), MyPref.DEFAULT_LANGUAGE, MyPref.LANGUAGE_Arabic);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_AR);
            LogUtils.e("", "DEFAULT_LANGUAGE_CODE::"+MyPref.getPref(this, MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN));
            setLocale("ar");
            Shy7lo.mLangCode = MyPref.getPref(this, MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN);
            setOnsSignalSubcription();
            Utils.getCMSPage(getActivity());
            MyPref.setPref(getActivity(), MyPref.IS_LANGUAGE_SELECT, true);

            IntentHandler.startActivity(getActivity(), LandingCountryActivity.class);
            finish();
        }
    }

    private void setOnsSignalSubcription(){

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



    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        LogUtils.e("", "language::" + getResources().getConfiguration().locale);

    }

    private BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

          finish();

        }
    };

}
