package shy7lo.com.shy7lo.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.LogLevel;
import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonElement;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.BuildConfig;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.model.AppInit;
import shy7lo.com.shy7lo.model.CMSPage;
import shy7lo.com.shy7lo.model.SortingPojo;
import shy7lo.com.shy7lo.model.WebCacheVersion;
import shy7lo.com.shy7lo.notification.NotificationUtils;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.service.OneSignalSubcriptionService;
import shy7lo.com.shy7lo.typeface.MyTypeFace;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by JitenRamen on 23-08-2016.
 */
public class Shy7lo extends Application implements OSSubscriptionObserver {
//public class Shy7lo extends Application {

    public static final String SCREEN_NAME = "SCREEN_NAME";
    public static final String SCREEN_MESSAGE = "SCREEN_MESSAGE";
    public static final String SCREEN_MAIN = "SCREEN_MAIN";
    public static final String SCREEN_PRODUCT_GENERAL = "SCREEN_PRODUCT_GENERAL";
    public static final String SCREEN_PRODUCT_WOMEN = "SCREEN_PRODUCT_WOMEN";
    public static final String SCREEN_PRODUCT_MEN = "SCREEN_PRODUCT_MEN";
    public static final String SCREEN_PRODUCT_KIDS = "SCREEN_PRODUCT_KIDS";
    public static final String SCREEN_PRODUCT_LIST = "SCREEN_PRODUCT_LIST";
    public static final String SCREEN_DETAILS = "SCREEN_DETAILS";
    public static final String SCREEN_CART = "SCREEN_CART";

    public static final String TAG = Shy7lo.class.getSimpleName();

//    public static Typeface RalewayBold, RalewayExtraBold, RalewayExtraLight, RalewayHeavy, RalewayLight,
//    RalewayMedium, RalewayRegular, RalewaySemiBold, RalewayThin, DroidKufiBold, DroidKufiRegular,
//    DroidNaskhBold, DroidNaskhRegular;

    public static Typeface RalewayBold, RalewayRegular, DroidKufiBold, DroidKufiRegular,
            DroidNaskhBold, DroidNaskhRegular;

    public static Typeface SFUIDisplayBold, SFUIDisplaySemibold, SFUIDisplayRegular, TahomaBold, TahomaRegular, RobotoMedium;

    public static String mLangCode = "en";
    public static Context mAppContext;

    private int numRunningActivities = 0;
    private static Context mActivityContext;

    DBAdapter dbAdapter;
    public static AppInit mAppInit;
    public static List<CMSPage.Data> mCMSPage;
    public static List<SortingPojo.SortingData> sortingDataList;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dbAdapter = new DBAdapter(this);

//Fabric Integation
        LogUtils.e("", "BuildConfig.DEBUG::" + BuildConfig.DEBUG);
//        if (!BuildConfig.DEBUG) {
//            final Fabric fabric = new Fabric.Builder(this)
//                    .kits(new Crashlytics())
//                    .debuggable(true)
//                    .build();
//            Fabric.with(fabric);
        Fabric.with(this, new Crashlytics());
//        }
//        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
//        Fabric.with(this, new Crashlytics.Builder().core(core).build());
        if (!BuildConfig.DEBUG) {
            Branch.getAutoInstance(this);
        }
//        IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
//        registerReceiver(mLanguageChangeReceiver, filter);

//        setLanguageLocal();

        mAppContext = getApplicationContext();
        Utils.setMint(this);

        //Adjust
        String appToken = "rl7sm6spfaio";
        String environment = RestClient.ADJUST_ENVIRONMENT;
        AdjustConfig config = new AdjustConfig(this, appToken, environment);
        config.setLogLevel(LogLevel.VERBOSE);
        config.setSendInBackground(true);
        Adjust.onCreate(config);

        // One sigla Integration
        OneSignal.startInit(this)
                .autoPromptLocation(false)
                .setNotificationReceivedHandler(new OneNotificationReceivedHandler())
                .setNotificationOpenedHandler(new OneNotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init();
        LogUtils.e("", "OneSignal Initialized");
//        Utils.showToast(getAppContext(), "OneSignal Initialized");
        OneSignal.addSubscriptionObserver(this);


        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        if (status != null) {
            boolean isEnabled = status.getPermissionStatus().getEnabled();

            boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
            String mUserID = status.getSubscriptionStatus().getUserId();
            String mPushToken = status.getSubscriptionStatus().getPushToken();

            LogUtils.e("", "isEnabled: " + isEnabled + "\nisSubscribed: " + isSubscribed);
            LogUtils.e("", "PlayerID: " + mUserID + "\nPushToken: " + mPushToken);

            MyPref.setPref(getApplicationContext(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, isSubscribed);
            if (isSubscribed) {
                if (!TextUtils.isEmpty(mUserID)) {
                    MyPref.setPref(getApplicationContext(), MyPref.ONE_SIGNAL_USER_ID, mUserID);
                }
//            if (!TextUtils.isEmpty(mPushToken)) {
//                MyPref.setPref(getApplicationContext(), MyPref.ONE_SIGNAL_PUSH_TOKEN, mPushToken);
//            }
            } else {
                if (!MyPref.getPref(getApplicationContext(), MyPref.ONE_SIGNAL_IS_REGISTERED, false)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        IntentHandler.startForgroundService(getApplicationContext(), OneSignalSubcriptionService.class);
                    } else {
                        IntentHandler.startService(getApplicationContext(), OneSignalSubcriptionService.class);
                    }
                }
            }
        }

//        //Tune Integration
//        Tune.init(this, Constant.TUNE_ADVERTISER_ID, Constant.TUNE_CONVERSION_KEY, true);
//
//        if (Build.VERSION.SDK_INT >= 14) {
//            registerActivityLifecycleCallbacks(new TuneActivityLifecycleCallbacks());
//        }
//
////        Tune.getInstance().enableSmartwhere();
////
////        TuneSmartwhereConfiguration configuration = new TuneSmartwhereConfiguration();
////        configuration.grant(TuneSmartwhereConfiguration.GRANT_SMARTWHERE_TUNE_EVENTS);
////        Tune.getInstance().configureSmartwhere(configuration);
//
//        String mAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        LogUtils.e("", "mAndroidID::" + mAndroidID);
//        Tune.getInstance().setAndroidId(mAndroidID);
//
//        try {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                String mDeviceId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//                LogUtils.e("", "mDeviceId::" + mDeviceId);
//                Tune.getInstance().setDeviceId(mDeviceId);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//            LogUtils.e("", "MacAddress::" + wm.getConnectionInfo().getMacAddress());
//            Tune.getInstance().setMacAddress(wm.getConnectionInfo().getMacAddress());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        mLangCode = MyPref.getPref(getApplicationContext(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_AR);
        setFont();

//        if (Utils.isInternetConnected(getActivity())) {
//            getAppInit();
//        }

        getWebCacheVersion(getActivity());
        if (Shy7lo.sortingDataList == null || Shy7lo.sortingDataList.size() == 0) {
            getSortingList(getActivity());
        }

        if (mCMSPage == null) {
            Utils.getCMSPage(getActivity());
        }

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStarted(Activity activity) {
                ++numRunningActivities;
                if (numRunningActivities == 1) {
                    LogUtils.e("APPLICATION", "APP IN FOREGROUND");
//                    checkForSession();
                }

            }

            @Override
            public void onActivityStopped(Activity activity) {

                --numRunningActivities;
                if (numRunningActivities == 0) {
                    LogUtils.e("APPLICATION", "APP IS IN BACKGROUND");
                }
            }


            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Adjust.onResume();
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Adjust.onPause();
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }
        });
    }

    public static void setActivityContext(Context mContext) {
        mActivityContext = mContext;
    }

//    private BroadcastReceiver mLanguageChangeReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
////            Utils.showToast(context, "Language Changed::" + Locale.getDefault().getLanguage());
//            setLanguageLocal();
//
//            Intent broadIntent = new Intent();
//            broadIntent.setAction(HomeActivity.FILTER_LANGUGE_CHANGE);
//            sendBroadcast(broadIntent);
//
//            setOneSignalRegister();
//
//        }
//    };


    private void setFont() {
//        RalewayBold = MyTypeFace.RalewayBold(getContext());
////        RalewayExtraBold = MyTypeFace.RalewayExtraBold(getContext());
////        RalewayExtraLight = MyTypeFace.RalewayExtraLight(getContext());
////        RalewayHeavy = MyTypeFace.RalewayHeavy(getContext());
////        RalewayLight = MyTypeFace.RalewayLight(getContext());
////        RalewayMedium = MyTypeFace.RalewayMedium(getContext());
//        RalewayRegular = MyTypeFace.RalewayRegular(getContext());
////        RalewaySemiBold = MyTypeFace.RalewaySemiBold(getContext());
////        RalewayThin = MyTypeFace.RalewayThin(getContext());
//        DroidKufiBold = MyTypeFace.DroidKufiBold(getContext());
//        DroidKufiRegular = MyTypeFace.DroidKufiRegular(getContext());
//        DroidNaskhBold = MyTypeFace.DroidNaskhBold(getContext());
//        DroidNaskhRegular = MyTypeFace.DroidNaskhRegular(getContext());

        RalewayBold = MyTypeFace.TahomaBold(getContext());
        RalewayRegular = MyTypeFace.TahomaRegular(getContext());
        DroidKufiBold = MyTypeFace.TahomaBold(getContext());
        DroidKufiRegular = MyTypeFace.TahomaRegular(getContext());
        DroidNaskhBold = MyTypeFace.TahomaBold(getContext());
        DroidNaskhRegular = MyTypeFace.TahomaRegular(getContext());
        SFUIDisplayBold = MyTypeFace.SFUIDisplayBold(getContext());
        SFUIDisplaySemibold = MyTypeFace.SFUIDisplaySemibold(getContext());
        SFUIDisplayRegular = MyTypeFace.SFUIDisplayRegular(getContext());
        TahomaBold = MyTypeFace.TahomaBold(getContext());
        TahomaRegular = MyTypeFace.TahomaRegular(getContext());
        RobotoMedium = MyTypeFace.RobotoMedium(getContext());
    }

    private Context getContext() {
        return getApplicationContext();
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    @Override
    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        if (!stateChanges.getFrom().getSubscribed() &&
                stateChanges.getTo().getSubscribed()) {
            LogUtils.e("", "onOSSubscriptionChanged");

            OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
            if (status != null) {
                boolean isEnabled = status.getPermissionStatus().getEnabled();

                boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
                String mUserID = status.getSubscriptionStatus().getUserId();
                String mPushToken = status.getSubscriptionStatus().getPushToken();

                LogUtils.e("", "stateChanges.getTo().getPushToken();::" + status.getSubscriptionStatus().toJSONObject().toString());

                LogUtils.e("", "isEnabled: " + isEnabled + "\nisSubscribed: " + isSubscribed);
                LogUtils.e("", "PlayerID: " + mUserID + "\nPushToken: " + mPushToken);

                MyPref.setPref(getApplicationContext(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, isSubscribed);
                if (isSubscribed) {
                    if (!TextUtils.isEmpty(mUserID)) {
                        MyPref.setPref(getApplicationContext(), MyPref.ONE_SIGNAL_USER_ID, mUserID);
                    }
//                if (!TextUtils.isEmpty(mPushToken)) {
//                    MyPref.setPref(getApplicationContext(), MyPref.ONE_SIGNAL_PUSH_TOKEN, mPushToken);
//                }
                    setOneSignalRegister();
                }
            }

        }
    }

    private class OneNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {

        }
    }


    private class OneNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

        @Override
        public void notificationOpened(OSNotificationOpenResult result) {

            LogUtils.e("", "notificationOpened result::" + result);

            String payload = result.notification.payload.rawPayload;
            LogUtils.e("", "payload::" + payload);

//            {"google.delivered_priority":"normal","google.sent_time":1553507471210,"google.ttl":259200,"google.original_priority":"normal"
// ,"custom":"{\"a\":{\"m_t\":\"0\",\"d_t\":\"kids\"},\"i\":\"9b815134-3108-42cd-a299-29691e4eeefc\"}","oth_chnl":"","pri":"5","vis"
// :"1","from":"874263957361","alert":"t","title":"t","grp_msg":"","google.message_id":"0:1553507471217467%ddd251fbf9fd7ecd","notificationId":-1514158572}

            if (MyPref.getPref(getApplicationContext(), MyPref.IS_DIRECT_WEBVIEW, false)) {

            } else {

                if (!TextUtils.isEmpty(payload) && isAppIsInBackground(getApplicationContext())) {
                    try {
                        JSONObject jsonObject = new JSONObject(payload);
                        JSONObject jsonCustom = new JSONObject(jsonObject.getString("custom"));
                        if (jsonCustom != null && jsonCustom.has("a")) {
                            LogUtils.e("", "jsonCustom::" + jsonCustom.getString("i"));

                            String screen = "";
                            Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);

                            JSONObject Ja = jsonCustom.getJSONObject("a");
                            String mOption = "", mDetails = "";
                            if (Ja != null && Ja.has("screen") || Ja.has("category") || Ja.has("product")) {

                                if (Ja.has("screen")) {
                                    String mScreen = Ja.getString("screen");
                                    if (mScreen.equalsIgnoreCase("women")) {
                                        mOption = "0";
                                        mDetails = "women";
                                    } else if (mScreen.equalsIgnoreCase("men")) {
                                        mOption = "0";
                                        mDetails = "men";
                                    } else if (mScreen.equalsIgnoreCase("kids")) {
                                        mOption = "0";
                                        mDetails = "kids";
                                    } else if (mScreen.equalsIgnoreCase("cart")) {
                                        mOption = "3";
                                        mDetails = "cart";
                                    }
                                } else if (Ja.has("category")) {
                                    String mCategory = Ja.getString("category");
                                    if (!TextUtils.isEmpty(mCategory)) {
                                        String mBrand = "";
                                        if (Ja.has("brand")) {
                                            mBrand = Ja.getString("brand");
                                        } else {
                                            mBrand = "null";
                                        }

                                        mOption = "1";
                                        mDetails = "?category_id=" + mCategory + "&sortby=created_at&direction=DESC&brand=" + mBrand;
                                    }
                                } else if (Ja.has("product")) {
                                    String mSku = Ja.getString("product");
                                    mOption = "2";
                                    mDetails = "" + mSku;
                                }

                            } else {
                                mOption = Ja.getString("m_t");
                                mDetails = Ja.getString("d_t");
                            }


                            LogUtils.e("", "mOption::" + mOption);
                            LogUtils.e("", "mDetails::" + mDetails);

                            if (mOption.equalsIgnoreCase("0")) {

                                if (mDetails.equalsIgnoreCase("women")) {
                                    resultIntent.putExtra(SCREEN_NAME, SCREEN_PRODUCT_WOMEN);
                                } else if (mDetails.equalsIgnoreCase("men")) {
                                    resultIntent.putExtra(SCREEN_NAME, SCREEN_PRODUCT_MEN);
                                } else if (mDetails.equalsIgnoreCase("kids")) {
                                    resultIntent.putExtra(SCREEN_NAME, SCREEN_PRODUCT_KIDS);
                                } else {
                                    resultIntent.putExtra(SCREEN_NAME, SCREEN_PRODUCT_GENERAL);
                                }

                                resultIntent.putExtra(SCREEN_MESSAGE, mDetails);
                                screen = resultIntent.getStringExtra(SCREEN_NAME);
                                LogUtils.e("", "Main screen " + screen);

                            } else if (mOption.equalsIgnoreCase("1")) {

                                resultIntent.putExtra(SCREEN_NAME, SCREEN_PRODUCT_LIST);
                                resultIntent.putExtra(SCREEN_MESSAGE, "" + mDetails);

                                screen = resultIntent.getStringExtra(SCREEN_NAME);
                                LogUtils.e("", "Product Listing " + screen);

                            } else if (mOption.equalsIgnoreCase("2")) {

                                resultIntent.putExtra(SCREEN_NAME, SCREEN_DETAILS);
                                resultIntent.putExtra(SCREEN_MESSAGE, "" + mDetails);

                                screen = resultIntent.getStringExtra(SCREEN_NAME);
                                LogUtils.e("", "Sku " + screen);

                            } else if (mOption.equalsIgnoreCase("3")) {

                                resultIntent.putExtra(SCREEN_NAME, SCREEN_CART);
                                screen = resultIntent.getStringExtra(SCREEN_NAME);
                                resultIntent.putExtra(SCREEN_MESSAGE, "" + mDetails);
                                LogUtils.e("", "Main screen " + screen);

                            }

                            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(resultIntent);
                        } else {
//                        Intent resultIntent = new Intent(getApplicationContext(), InitialScreenActivity.class);
                            Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(resultIntent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
//                Intent resultIntent = new Intent(getApplicationContext(), InitialScreenActivity.class);
                    Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(resultIntent);
                }

            }

        }
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
                        isInBackground = false;
                    }
                }
            }
        }
//        } else {
//            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
//            ComponentName componentInfo = taskInfo.get(0).topActivity;
//            if (componentInfo.getPackageName().equals(context.getPackageName())) {
//                isInBackground = false;
//            }
//        }

        return isInBackground;
    }

//    private void setLanguageLocal() {
//        if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
//            MyPref.setPref(getApplicationContext(), MyPref.DEFAULT_LANGUAGE, MyPref.LANGUAGE_English);
//            MyPref.setPref(getApplicationContext(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN);
//            Utils.setLocale(getApplicationContext(), "en");
//        } else {
//            MyPref.setPref(getApplicationContext(), MyPref.DEFAULT_LANGUAGE, MyPref.LANGUAGE_Arabic);
//            MyPref.setPref(getApplicationContext(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_AR);
//            Utils.setLocale(getApplicationContext(), "ar");
//        }
//        setCurrencyCode();
//        mLangCode = MyPref.getPref(getApplicationContext(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_AR);
//        setOneSignalRegister();
//    }

//    private void setCurrencyCode() {
//        String mCurrencyCode = MyPref.getPref(getApplicationContext(), MyPref.DEFAULT_CURRENCY_CODE, "");
//        if (!TextUtils.isEmpty(mCurrencyCode)) {
//            boolean isChanged = false;
//            String mCode = "";
//            if (mCurrencyCode.equalsIgnoreCase("SAR") || mCurrencyCode.equalsIgnoreCase("ر.س")) {
//                mCode = getResources().getString(R.string.SAR);
//                isChanged = true;
//            } else if (mCurrencyCode.equalsIgnoreCase("AED") || mCurrencyCode.equalsIgnoreCase("د.إ")) {
//                mCode = getResources().getString(R.string.AED);
//                isChanged = true;
//            } else if (mCurrencyCode.equalsIgnoreCase("QAR") || mCurrencyCode.equalsIgnoreCase("ر.ق")) {
//                mCode = getResources().getString(R.string.QAR);
//                isChanged = true;
//            } else if (mCurrencyCode.equalsIgnoreCase("OMR") || mCurrencyCode.equalsIgnoreCase("ر.ع")) {
//                mCode = getResources().getString(R.string.OMR);
//                isChanged = true;
//            } else if (mCurrencyCode.equalsIgnoreCase("KWD") || mCurrencyCode.equalsIgnoreCase("د.ك")) {
//                mCode = getResources().getString(R.string.KWD);
//                isChanged = true;
//            } else if (mCurrencyCode.equalsIgnoreCase("BHD") || mCurrencyCode.equalsIgnoreCase("د.ب")) {
//                mCode = getResources().getString(R.string.BHD);
//                isChanged = true;
//            }
//
//            LogUtils.e("", "mCurrencyCode::" + mCurrencyCode);
//            LogUtils.e("", "mCode::" + mCode);
//
//            if (isChanged) {
//                MyPref.setPref(getApplicationContext(), MyPref.DEFAULT_CURRENCY_CODE, "" + mCode);
//            }
//        }
//    }

    private void setOneSignalRegister() {
        if (MyPref.getPref(getApplicationContext(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, false)) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(NotificationUtils.SHARED_PREF, 0);
            if (!TextUtils.isEmpty(pref.getString("regId", ""))) {
                Map<String, Object> jsonParams = new ArrayMap<>();
//            jsonParams.put("device_token", "" + MyPref.getPref(getApplicationContext(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
                jsonParams.put("device_token", "" + pref.getString("regId", ""));
                jsonParams.put("device_type", "android");
                jsonParams.put("subcribe_id", "" + MyPref.getPref(getApplicationContext(), MyPref.ONE_SIGNAL_USER_ID, ""));

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

                ApiInterface apiService =
                        RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
                Call<JsonElement> callCode = apiService.registerPushToken(Shy7lo.mLangCode, body);
                callCode.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        if (response.isSuccessful()) {
                            MyPref.setPref(getApplicationContext(), MyPref.ONE_SIGNAL_IS_REGISTERED, true);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {

                    }
                });
            }
        }
    }

    Dialog mSessionDialog;

//    private void checkForSession() {
//
//
//        String userToken = MyPref.getPref(getApplicationContext(), MyPref.USER_CART_ID, "");
//
//        if (!TextUtils.isEmpty(userToken)) {
//
//            final String userPWD = MyPref.getPref(getApplicationContext(), MyPref.USER_PWD, "");
//
//
//            ApiInterface apiService =
//                    RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//            Call<JsonElement> call = apiService.getUserDashboard(Shy7lo.mLangCode, "Bearer " + userToken);
//            call.enqueue(new Callback<JsonElement>() {
//                @Override
//                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//
//                    LogUtils.e(TAG, "response code:" + response.code());
//
//                    if (response.code() == 401) {
//                        LogUtils.e("", "userPWD::" + userPWD);
//                        if (!TextUtils.isEmpty(userPWD)) {
//
//                            ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//
//                            Map<String, Object> jsonParams = new ArrayMap<>();
//                            jsonParams.put("email", MyPref.getPref(getApplicationContext(), MyPref.USER_EMAIL, ""));
//                            jsonParams.put("password", MyPref.getPref(getApplicationContext(), MyPref.USER_PWD, ""));
//                            String guestToken = MyPref.getPref(getApplicationContext(), MyPref.GUEST_CART_ID, "");
//                            jsonParams.put("cart_id", "" + guestToken);
//
//                            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
//                            Call<JsonElement> signInCall = serviceAPI.getSignIn(Shy7lo.mLangCode, body);
//                            signInCall.enqueue(new Callback<JsonElement>() {
//                                @Override
//                                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                                    if (response.isSuccessful()) {
//                                        try {
//
//                                            JsonElement jsonElement = response.body();
//                                            JSONObject jResponse = new JSONObject(jsonElement.toString());
//                                            if (jResponse != null && jResponse.getString("success").equals("1")) {
//
//                                                JSONObject jData = jResponse.getJSONObject("data");
//                                                if (jData != null && jData.has("token")) {
//                                                    String userToken = jData.getString("token");
//                                                    if (!TextUtils.isEmpty(userToken)) {
//                                                        MyPref.setPref(getApplicationContext(), MyPref.USER_CART_ID, "" + userToken);
//                                                    } else {
//                                                        MyPref.setPref(getApplicationContext(), MyPref.USER_CART_ID, "");
//                                                    }
//                                                }
//                                            }
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<JsonElement> call, Throwable t) {
//
//                                }
//                            });
//
//                        } else {
//
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    try {
//
//                                        if (mSessionDialog != null && mSessionDialog.isShowing()) {
//                                            return;
//                                        }
//
//                                        mSessionDialog = new Dialog(getActivity());
//                                        mSessionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                        mSessionDialog.setCanceledOnTouchOutside(false);
//                                        mSessionDialog.setCancelable(false);
//                                        mSessionDialog.setContentView(R.layout.dialog_session_expire);
//
//                                        TextView tvFavouriteText = (TextView) mSessionDialog.findViewById(R.id.tvFavouriteText);
//                                        Button btnLoginNow = (Button) mSessionDialog.findViewById(R.id.btnLoginNow);
//                                        Button btnGuest = (Button) mSessionDialog.findViewById(btnCancel);
//                                        tvFavouriteText.setText(getResources().getString(R.string.msg_session_expire));
//                                        btnLoginNow.setText(getResources().getString(R.string.login_now));
//                                        btnGuest.setText(getResources().getString(R.string.continue_guest));
//
//                                        btnGuest.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//
//                                                if (mSessionDialog != null && mSessionDialog.isShowing()) {
//                                                    mSessionDialog.dismiss();
//                                                    mSessionDialog.cancel();
//                                                }
//
//                                                getGuestCartToken();
//
//                                            }
//                                        });
//
//                                        btnLoginNow.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//
//                                                if (mSessionDialog != null && mSessionDialog.isShowing()) {
//                                                    mSessionDialog.dismiss();
//                                                    mSessionDialog.cancel();
//                                                }
//
//                                                IntentHandler.startActivityReorderFront(getActivity(), SignInActivity.class);
//
//                                            }
//                                        });
//
//                                        mSessionDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                                            @Override
//                                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                                                return keyCode == KeyEvent.KEYCODE_BACK;
//                                            }
//                                        });
//                                        mSessionDialog.show();
//
//                                    } catch (
//                                            Exception e)
//
//                                    {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }, 5000);
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<JsonElement> call, Throwable t) {
//                    System.out.println(t.getMessage());
//                }
//            });
//
//
//        }
//
//
//    }

//    private void getGuestCartToken() {
//
//        LogUtils.e(Shy7lo.TAG, "getGuestCartToken call");
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
//        Call<JsonElement> call = apiService.getGuestCartToken(Shy7lo.mLangCode);
//        call.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//
//                LogUtils.e(Shy7lo.TAG, "response code:" + response.code());
//
//                if (response.isSuccessful()) {
//                    try {
//
//                        JSONObject jResponse = new JSONObject(response.body().toString());
//                        if (jResponse != null && jResponse.getString("success").equalsIgnoreCase("1")) {
//
//                            JSONObject jData = jResponse.getJSONObject("data");
//                            if (jData != null && jData.has("cart_id")) {
//                                String token = jData.getString("cart_id");
//                                LogUtils.e(Shy7lo.TAG, "response token:" + token);
//
//                                if (!TextUtils.isEmpty(token)) {
//                                    MyPref.setPref(getApplicationContext(), MyPref.GUEST_CART_ID, token);
//                                }
//                            }
//
//                            MyPref.setPref(getActivity(), MyPref.USER_CART_ID, "");
//                            MyPref.setPref(getActivity(), MyPref.CART_ITEM_COUNT, 0);
//                            MyPref.setPref(getActivity(), MyPref.USER_ID, "");
//                            MyPref.setPref(getActivity(), MyPref.USER_FIRSTNAME, "");
//                            MyPref.setPref(getActivity(), MyPref.USER_LASTNAME, "");
//                            MyPref.setPref(getActivity(), MyPref.USER_PHONE, "");
//                            MyPref.setPref(getActivity(), MyPref.USER_ADDRESS, "");
//                            MyPref.setPref(getActivity(), MyPref.USER_CITY, "");
//
//                            Intent broadIntent = new Intent();
//                            broadIntent.setAction(HomeActivity.FILTER_SESSION_EXPIRE);
//                            sendBroadcast(broadIntent);
//
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
//
//    }

    private void getWebCacheVersion(final Context mContext) {

        LogUtils.e(Shy7lo.TAG, "getWebCacheVersion call");

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<WebCacheVersion> call = apiService.getWebCacheVersion(Shy7lo.mLangCode);
        call.enqueue(new Callback<WebCacheVersion>() {
            @Override
            public void onResponse(Call<WebCacheVersion> call, Response<WebCacheVersion> response) {

                LogUtils.e(Shy7lo.TAG, "response code:" + response.code());

                if (response.isSuccessful()) {
                    try {
                        WebCacheVersion mWebCacheVersion = response.body();
                        if (mWebCacheVersion != null && mWebCacheVersion.success.equals("1")) {
                            MyPref.setPref(getActivity(), MyPref.WEB_CACHE_VERSION, "" + mWebCacheVersion.data.version);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
                    if (String.valueOf(response.code()).startsWith("5")) {
                        Utils.showAlertDialog(mContext, "" + response.code());
                    } else {
                        JSONObject jResponse = null;
                        try {
                            jResponse = new JSONObject(response.errorBody().string());
                            if (jResponse != null && jResponse.getString("success").equals("0")) {
                                Utils.showToast(mContext, "" + jResponse.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<WebCacheVersion> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    private void getSortingList(final Context mContext) {

        LogUtils.e(Shy7lo.TAG, "getSortingList call");

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<SortingPojo> call = apiService.getSortingList(Shy7lo.mLangCode);
        call.enqueue(new Callback<SortingPojo>() {
            @Override
            public void onResponse(Call<SortingPojo> call, Response<SortingPojo> response) {

                LogUtils.e(Shy7lo.TAG, "response code:" + response.code());

                if (response.isSuccessful()) {
                    try {
                        SortingPojo mSortingPojo = response.body();
                        if (mSortingPojo != null && mSortingPojo.success.equals("1")) {
                            if (mSortingPojo.data != null && mSortingPojo.data.sortingData != null) {
                                Shy7lo.sortingDataList = mSortingPojo.data.sortingData;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SortingPojo> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

//    private void getAppInit() {
//
//        Map<String, Object> jsonParams = new ArrayMap<>();
//        jsonParams.put("device_type", "android");
//        jsonParams.put("app_version", "" + BuildConfig.VERSION_NAME);
//        jsonParams.put("device_token", "" + Utils.getDeviceToken(this));
//        jsonParams.put("country", "SA");
//
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
//        Call<AppInit> call = apiService.getAppInit(Shy7lo.mLangCode, "S0iv6TrBsAh9Xfz0zcTil5qF3Yn8Yt391523174021", body);
//        call.enqueue(new Callback<AppInit>() {
//            @Override
//            public void onResponse(Call<AppInit> call, Response<AppInit> response) {
//
//                LogUtils.e(TAG, "response code:" + response.code() + " " + response.isSuccessful());
//
//                if (response.isSuccessful()) {
//                    try {
//
//                        mAppInit = response.body();
//                        LogUtils.e(TAG, "mAppInit initialize");
//                        if (mAppInit != null && mAppInit.success) {
//                            AppInit.Integrations mIntegrations = mAppInit.integrations;
//                            if (mIntegrations != null) {
//
//
//                                LogUtils.e("", "mIntegrations.tune::" + mIntegrations.tune);
//                                LogUtils.e("", "mIntegrations.fabric::" + mIntegrations.fabric);
//                                LogUtils.e("", "mIntegrations.oneSginal::" + mIntegrations.oneSginal);
//                                LogUtils.e("", "mIntegrations.criteo::" + mIntegrations.criteo);
//
//                                MyPref.setPref(getActivity(), MyPref.APP_TUNE, mIntegrations.tune);
//                                MyPref.setPref(getActivity(), MyPref.APP_FABRIC, mIntegrations.fabric);
//                                MyPref.setPref(getActivity(), MyPref.APP_ONE_SIGNAL, mIntegrations.oneSginal);
//                                MyPref.setPref(getActivity(), MyPref.APP_CRITEO, mIntegrations.criteo);
//
//                                Utils.setInitConfig(getActivity());
//                            }
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
//            public void onFailure(Call<AppInit> call, Throwable t) {
//                System.out.println(t.getMessage());
//            }
//        });
//
//    }

    private Context getActivity() {
        return mActivityContext;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
//        if (mLanguageChangeReceiver != null) {
//            unregisterReceiver(mLanguageChangeReceiver);
//        }
    }
}
