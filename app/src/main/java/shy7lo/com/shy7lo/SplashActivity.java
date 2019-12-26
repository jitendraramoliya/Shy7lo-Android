package shy7lo.com.shy7lo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.OnDeviceIdsRead;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.freshchat.consumer.sdk.FreshchatNotificationConfig;
import com.google.gson.JsonElement;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.model.AppInit;
import shy7lo.com.shy7lo.model.AppVersion;
import shy7lo.com.shy7lo.notification.MyFirebaseMessagingService;
import shy7lo.com.shy7lo.notification.NotificationUtils;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.service.OneSignalSubcriptionService;
import shy7lo.com.shy7lo.utils.Constant;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

import static shy7lo.com.shy7lo.pref.MyPref.getPref;

/**
 * Created by JitenRamen on 19-08-2016.
 */
public class SplashActivity extends Activity {

    private String TAG = "SplashActivity";

//    private FirebaseAnalytics mFirebaseAnalytics;
//    ProgressBar pbSplash;

    @BindView(R.id.ivSplashLogo)
    ImageView ivSplashLogo;
    @BindView(R.id.pbSplash)
    ProgressBar pbSplash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(getActivity());


//        pbSplash = (ProgressBar) findViewById(R.id.pbSplash);
//        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            setLocale("ar");
//        } else {
//            setLocale("en");
//        }

        // fresh chat
        FreshchatConfig freshConfig = new FreshchatConfig(Constant.FRESHCHAT_APP_ID, Constant.FRESHCHAT_APP_KEY);
        freshConfig.setCameraCaptureEnabled(true);
        freshConfig.setGallerySelectionEnabled(true);
        Freshchat.getInstance(getApplicationContext()).init(freshConfig);

        FreshchatNotificationConfig notificationConfig = new FreshchatNotificationConfig()
                .setNotificationSoundEnabled(true)
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setLargeIcon(R.drawable.ic_onesignal_large_icon_default)
                .launchActivityOnFinish(HomeActivity.class.getName())
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Freshchat.getInstance(getApplicationContext()).setNotificationConfig(notificationConfig);

        Adjust.getGoogleAdId(getActivity(), new OnDeviceIdsRead() {
            @Override
            public void onGoogleAdIdRead(String googleAdId) {
                LogUtils.e("123", "googleAdId: "+googleAdId);
                MyPref.setPref(getActivity(), MyPref.ADJUST_GOOGLE_ID, ""+googleAdId);
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences(NotificationUtils.SHARED_PREF, 0);
        String token = pref.getString("regId", "");
//        LogUtils.e("", "token::" + token);

        if (!MyPref.getPref(getActivity(), MyPref.IS_LANGUAGE_SELECT, false)) {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {

                MyPref.setPref(getActivity(), MyPref.DEFAULT_LANGUAGE, MyPref.LANGUAGE_English);
                MyPref.setPref(getActivity(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN);
                LogUtils.e("", "DEFAULT_LANGUAGE_CODE::"+MyPref.getPref(this, MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN));
                setLocale("en");
                Shy7lo.mLangCode = MyPref.getPref(this, MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN);
                setOnsSignalSubcription();

            } else {

                MyPref.setPref(getActivity(), MyPref.DEFAULT_LANGUAGE, MyPref.LANGUAGE_Arabic);
                MyPref.setPref(getActivity(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_AR);
                LogUtils.e("", "DEFAULT_LANGUAGE_CODE::"+MyPref.getPref(this, MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN));
                setLocale("ar");
                Shy7lo.mLangCode = MyPref.getPref(this, MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN);
                setOnsSignalSubcription();

            }
        }

        // set Logo According Language
//        if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, ""))) {
//            if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
//                ivSplashLogo.setImageResource(R.drawable.logo_english);
//            } else {
//                ivSplashLogo.setImageResource(R.drawable.logo_arabic);
//            }
//        } else {
//            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, MyPref.LANGUAGE_Arabic).equals(MyPref.LANGUAGE_Arabic)) {
//                ivSplashLogo.setImageResource(R.drawable.logo_arabic);
//            } else {
//                ivSplashLogo.setImageResource(R.drawable.logo_english);
//            }
//        }

//        if (Utils.isInternetConnected(getActivity())) {
        boolean isFirstTime = MyPref.getPref(getActivity(), MyPref.IS_FIRST_TIME, true);
        if (!isFirstTime) {
            getAppVersion();
        }

        getDatabase();
//        }

//        try {
//
//
//            String title = "shy7lo it where ever you want and you can set cookey and make more funny speech";
//            String message = "Check out these awesome deals! THis is another notfication and this is only example you don't know";
////        String message = "Check out these awesome deals! THis is another notfication and this is only example you don't know why this is running in this state. This is not in capacity but you can extend it where ever you want and you can set cookey and make more funny speech and like to get more thing";
////        String imageUrl = "";
//            String imageUrl = "http://www.tribitinfotech.in/uploads/app_icon3.png";
//            String main_screen = "";
//            String product_listing = "? category_id = 285 & sortby = created_at & direction = DESC & brand = null";
//            String mSku = "";
//
//            Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
////                Bundle bundle = new Bundle();
//
//            String screen = "";
//
//            if (!TextUtils.isEmpty(main_screen)) {
//
//                if (main_screen.equalsIgnoreCase("women")) {
//                    resultIntent.putExtra(SCREEN_NAME, SCREEN_PRODUCT_WOMEN);
//                } else if (main_screen.equalsIgnoreCase("men")) {
//                    resultIntent.putExtra(SCREEN_NAME, SCREEN_PRODUCT_MEN);
//                } else if (main_screen.equalsIgnoreCase("kids")) {
//                    resultIntent.putExtra(SCREEN_NAME, SCREEN_PRODUCT_KIDS);
//                }
//                resultIntent.putExtra(SCREEN_MESSAGE, message);
//                screen = resultIntent.getStringExtra(SCREEN_NAME);
//                LogUtils.e("", "Main screen " + screen);
//
//            } else if (!TextUtils.isEmpty(product_listing)) {
//
//                resultIntent.putExtra(SCREEN_NAME, SCREEN_PRODUCT_LIST);
//                resultIntent.putExtra(SCREEN_MESSAGE, "" + product_listing);
//
//                screen = resultIntent.getStringExtra(SCREEN_NAME);
//                LogUtils.e("", "Product Listing " + screen);
//            } else if (!TextUtils.isEmpty(mSku)) {
//
//                resultIntent.putExtra(SCREEN_NAME, SCREEN_DETAILS);
//                resultIntent.putExtra(SCREEN_MESSAGE, "" + mSku);
//
//                screen = resultIntent.getStringExtra(SCREEN_NAME);
//                LogUtils.e("", "Sku " + screen);
//            } else {
//
//                resultIntent.putExtra(SCREEN_NAME, SCREEN_MAIN);
//                resultIntent.putExtra(SCREEN_MESSAGE, message);
//
//                screen = resultIntent.getStringExtra(SCREEN_NAME);
//                LogUtils.e("", "Default " + screen);
//            }
//
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String timestamp = sdf.format(new Date());
//
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//
//            // check for image attachment
//            if (TextUtils.isEmpty(imageUrl)) {
//                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//            } else {
//                // image is present, show notification with image
//                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
//        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
//        mFirebaseAnalytics.setMinimumSessionDuration(500);
//        mFirebaseAnalytics.setSessionTimeoutDuration(1000000);

    }

    private void getDatabase() {

        try {

//            dba = new DBAdapter(getActivity());
            System.out.println("Database File Path:::"
                    + getDatabasePath(DBAdapter.DATABASE_NAME)
                    .getAbsolutePath());

            if (DBAdapter.isDatabaseAvailable()) {
                LogUtils.e("",
                        "database file exist:::"
                                + DBAdapter.getDatabaseFilePath());

            } else {
                LogUtils.e("",
                        "database file not exist:::"
                                + DBAdapter.getDatabaseFilePath());

                System.out.println("copying database .... ");

                DBAdapter.copyDataBase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        pbSplash.setProgress(50);

//        goNext();
        getAppInit();

    }

    private void getAppInit() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getAppInit();
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

                LogUtils.e(TAG, "getAppInit response code:" + response.code() + " " + response.isSuccessful());

                if (response.isSuccessful()) {
                    try {

                        Shy7lo.mAppInit = response.body();
                        LogUtils.e(TAG, "mAppInit initialize");
                        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.success.equalsIgnoreCase("1")) {
                            AppInit.Integrations mIntegrations = Shy7lo.mAppInit.integrations;
                            if (mIntegrations != null) {


                                LogUtils.e("", "mIntegrations.tune::" + mIntegrations.tune);
                                LogUtils.e("", "mIntegrations.adjust::" + mIntegrations.adjust);
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

        goNext();

    }

    private void goNext() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                pbSplash.setProgress(75);

                if (!MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, false)) {
                    OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
                    if (status != null) {
                        boolean isEnabled = status.getPermissionStatus().getEnabled();

                        boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
                        String mUserID = status.getSubscriptionStatus().getUserId();
                        String mPushToken = status.getSubscriptionStatus().getPushToken();

                        LogUtils.e("", "Splash isEnabled: " + isEnabled + "\nisSubscribed: " + isSubscribed);
                        LogUtils.e("", "Splash PlayerID: " + mUserID + "\nPushToken: " + mPushToken);

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
                } else {
                    LogUtils.e("", "Splash PlayerID::" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(NotificationUtils.SHARED_PREF, 0);
                    LogUtils.e("", "Splash PushToken::" + pref.getString("regId", ""));
//                    LogUtils.e("", "Splash PushToken::" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
                }


                pbSplash.setProgress(100);

                Uri data = getIntent().getData();
                if (data != null) {
                    String screen = data.getQueryParameter("screen");

                    LogUtils.e("", "screen:::" + screen);

                    if (!TextUtils.isEmpty(screen) && screen.equalsIgnoreCase("landing_screen")) {
//                        shy7lo://?screen=landing_screen&id=women
                        String id = data.getQueryParameter("id");
                        LogUtils.e("", "id:::" + id);

                        Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);

                        resultIntent.putExtra(MyFirebaseMessagingService.SCREEN_MESSAGE, "");
                        if (!TextUtils.isEmpty(id) && id.equalsIgnoreCase("women")) {
                            resultIntent.putExtra(MyFirebaseMessagingService.SCREEN_NAME, MyFirebaseMessagingService.SCREEN_PRODUCT_WOMEN);
                        } else if (!TextUtils.isEmpty(id) && id.equalsIgnoreCase("men")) {
                            resultIntent.putExtra(MyFirebaseMessagingService.SCREEN_NAME, MyFirebaseMessagingService.SCREEN_PRODUCT_MEN);
                        } else if (!TextUtils.isEmpty(id) && id.equalsIgnoreCase("kids")) {
                            resultIntent.putExtra(MyFirebaseMessagingService.SCREEN_NAME, MyFirebaseMessagingService.SCREEN_PRODUCT_KIDS);
                        }


                        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(resultIntent);
                        return;
                    } else if (!TextUtils.isEmpty(screen) && screen.equalsIgnoreCase("single_product")) {
//                        shy7lo://?screen=single_product&id=02030100100006
                        String id = data.getQueryParameter("id");
                        LogUtils.e("", "id:::" + id);

                        Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                        resultIntent.putExtra(MyFirebaseMessagingService.SCREEN_NAME, MyFirebaseMessagingService.SCREEN_DETAILS);
                        resultIntent.putExtra(MyFirebaseMessagingService.SCREEN_MESSAGE, "" + id);

                        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(resultIntent);
                        return;
                    } else if (!TextUtils.isEmpty(screen) && screen.equalsIgnoreCase("product_listing")) {
                        String category_id = data.getQueryParameter("id");
//                        String sortby = data.getQueryParameter("sortby");
                        String sortby = data.getQueryParameter("sort_by");
                        String direction = data.getQueryParameter("direction");
                        String brand = data.getQueryParameter("brand");

                        if (TextUtils.isEmpty(sortby)){
                            sortby = "product_position";
                        }

                        if (TextUtils.isEmpty(direction)){
                            direction = "DESC";
                        }

                        String mDetails = "?category_id=" + category_id + "&sortby=" + sortby + "&direction=" + direction + "&brand=" + brand;

                        Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                        resultIntent.putExtra(MyFirebaseMessagingService.SCREEN_NAME, MyFirebaseMessagingService.SCREEN_PRODUCT_LIST);
                        resultIntent.putExtra(MyFirebaseMessagingService.SCREEN_MESSAGE, "" + mDetails);

                        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(resultIntent);
                        return;
                    }
                }

                boolean isFirstTime = MyPref.getPref(getActivity(), MyPref.IS_FIRST_TIME, true);
                if (isFirstTime) {
//                    IntentHandler.startActivity(getActivity(), LandingPageActivity.class);
                    IntentHandler.startActivity(getActivity(), LandingLanguageActivity.class);
                } else {
                    IntentHandler.startActivity(getActivity(), HomeActivity.class);
//                    IntentHandler.startActivity(getActivity(), InitialScreenActivity.class);
                }

                finish();


            }
        }, 2500);

    }

//    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
//        NotificationUtils notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
//    }
//
//    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
//        NotificationUtils notificationUtils = new NotificationUtils(getActivity());
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
//    }

    private void getAppVersion() {

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("device_type", "android");

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
//        Call<AppVersion> call = apiService.getAppVersion(Shy7lo.mLangCode, body);
        Call<AppVersion> call = apiService.getAppVersion(Shy7lo.mLangCode);
        call.enqueue(new Callback<AppVersion>() {
            @Override
            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {

                LogUtils.e(TAG, "response code:" + response.code());

                if (response.isSuccessful()) {
                    try {

                        AppVersion mAppVersion = response.body();
                        if (mAppVersion != null && mAppVersion.success.equals("1")) {
                            if (mAppVersion.data != null && !TextUtils.isEmpty(mAppVersion.data.version)) {
                                LogUtils.e("", "mAppVersion.version::" + mAppVersion.data.version);
                                MyPref.setPref(getActivity(), MyPref.APP_VERSION, mAppVersion.data.version);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }

            @Override
            public void onFailure(Call<AppVersion> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

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


    @Override
    public void onStart() {
        super.onStart();

        if (!BuildConfig.DEBUG) {
            Branch branch = Branch.getInstance();

            branch.initSession(new Branch.BranchUniversalReferralInitListener() {
                @Override
                public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError error) {
                    if (error == null) {
                        if (branchUniversalObject != null) {
                            LogUtils.e("BranchTestBed", "referring Branch Universal Object: " + branchUniversalObject.toString());
                        }

                    } else {
                        Log.i("MyApp", error.getMessage());
                    }
                }
            }, this.getIntent().getData(), this);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mFirebaseAnalytics.logEvent("Splash", new Bundle());
    }

    private Activity getActivity() {
        return SplashActivity.this;
    }

    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }
}
