package shy7lo.com.shy7lo.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.ArrayMap;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.JsonElement;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import io.fabric.sdk.android.Fabric;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.BuildConfig;
import shy7lo.com.shy7lo.MaintenanceScreenActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.model.CMSPage;
import shy7lo.com.shy7lo.model.Wishlist;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;

import static android.content.ContentValues.TAG;

//import com.splunk.mint.Mint;


/**
 * Created by JitenRamen on 30-08-2016.
 */
public class Utils {

    public static void showToast(Context context, String msg) {
        if (context == null) {
            context = Shy7lo.getAppContext();
        }
        Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
    }

    public static int getPaddingPixels(Context context, int dpValue) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dpValue * scale + 0.5f);
    }

    // Convert pixel to dip
    public static int getDipsFromPixel(Context context, float pixels) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        String deviceId = telephonyManager.getDeviceId();
        if (deviceId != null) {
            if (deviceId.equalsIgnoreCase("000000000000000")) {
                deviceId = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        } else {
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    public static String getDeviceToken(Context context) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceId;
    }

    public final static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static void hideKeyboard(Activity mActivity) {

        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = mActivity.getCurrentFocus();
        if (view == null) {
            view = new View(mActivity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboardOnFocus(final Activity mActivity, final EditText editText) {
        editText.requestFocus();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 0);
    }

    static Dialog progressDialog;

    public static void showProgressDialog(Context context) {

        try {

            if (isProgressDialogShowing()) {
                return;
            }

            if (((Activity) context) != null && ((Activity) context).isFinishing()) {
                return;
            }

            if (context == null) {
                context = Shy7lo.getAppContext();
            }

            progressDialog = new Dialog(context, R.style.StyledDialog);
            if (progressDialog != null) {
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setContentView(R.layout.dialog_progressbar);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isProgressDialogShowing() {
        if (progressDialog != null) {
            return progressDialog.isShowing();
        }
        return false;
    }

    public static void closeProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRealPrice(float mExchangeRate, float price) {
        double mValue = (Math.round((mExchangeRate * price) * 100.00) / 100.00);
        BigDecimal bigDecimal = new BigDecimal(mValue);
        BigDecimal roundedWithScale = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return "" + roundedWithScale;
    }

    public static String getIntPrice(float mExchangeRate, float price) {
        return "" + (int) (Math.round((mExchangeRate * price) * 100.00) / 100.00);
    }

    public static double getAnswerPrice(float mExchangeRate, float price) {
        return Math.round((mExchangeRate * price) * 100.00) / 100.00;
    }

    public static String getRealPriceWithQty(float mExchangeRate, float price, int qty) {
        return "" + ((Math.round((mExchangeRate * price) * 100.00) / 100.00) * qty);
    }

    public static int getScreenResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return width;
    }

    public static Bitmap flipImage(Bitmap src) {
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static void setLocale(Activity activity) {
        String lang = "en";
        if (MyPref.getPref(activity, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            lang = "ar";
        } else {
            lang = "en";
        }

        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Resources res = activity.getResources();
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        LogUtils.e("", "Utils language::" + activity.getResources().getConfiguration().locale);

    }

    public static void setLocale(Context mContext, String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = mContext.getResources();
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        LogUtils.e("", "language::" + mContext.getResources().getConfiguration().locale);

    }

    public static void setTabsFont(TabLayout tabLayout, Typeface mTypeface) {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(mTypeface);
                }
            }
        }
    }

    public static boolean isInternetConnected(Context context) {

        try {

            if (context == null) {
                context = Shy7lo.getAppContext();
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static boolean isInternetConnectedDialog(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return connected;
    }

    public interface OnOfflineRetryDialogClick {
        public void onRetryClicked(Dialog dialog);
    }

    private static Dialog mOfflineDialog;

    public static void showOfflineMsgDialog(Context context, final OnOfflineRetryDialogClick onOfflineRetryDialogClick) {

        try {

            if (mOfflineDialog != null && mOfflineDialog.isShowing()) {
                return;
            }

            if (context == null) {
                context = Shy7lo.getAppContext();
            }

            mOfflineDialog = new Dialog(context);
            mOfflineDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mOfflineDialog.setCanceledOnTouchOutside(false);
            mOfflineDialog.setCancelable(false);
            mOfflineDialog.setContentView(R.layout.dialog_offline);

            Button btnRetry = (Button) mOfflineDialog.findViewById(R.id.btnRetry);

            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                    dialog.cancel();
//                }
                    onOfflineRetryDialogClick.onRetryClicked(mOfflineDialog);

                }
            });

            mOfflineDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });

            mOfflineDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Dialog mAlertDialog;

    public static void showAlertDialog(Context context, String mStatusCode) {

        try {

            if (mAlertDialog != null && mAlertDialog.isShowing()) {
                return;
            }

            if (((Activity) context) != null && ((Activity) context).isFinishing()) {
                return;
            }

            if (context == null) {
                context = Shy7lo.getAppContext();
            }

            mAlertDialog = new Dialog(context);
            mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mAlertDialog.setCanceledOnTouchOutside(false);
            mAlertDialog.setCancelable(false);
            mAlertDialog.setContentView(R.layout.dialog_alert);


            TextView tvOfflineText = (TextView) mAlertDialog.findViewById(R.id.tvOfflineText);
            Button btnOkay = (Button) mAlertDialog.findViewById(R.id.btnOkay);

            tvOfflineText.setText(mStatusCode + " \n " + context.getString(R.string.msg_inconvenience));

            btnOkay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mAlertDialog != null && mAlertDialog.isShowing()) {
                        mAlertDialog.dismiss();
                        mAlertDialog.cancel();
                    }

                }
            });

            mAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });

            mAlertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface OnAlertOkayDialogClick {
        public void onOkayClicked(Dialog dialog);
    }

    public static void showAlertOkCancelDialog(Context context, String msg, String setting, final OnAlertOkayDialogClick mOnAlertOkayDialogClick) {

        LogUtils.e("", "msg::" + msg);
        LogUtils.e("", "setting::" + setting);

        try {

            if (mAlertDialog != null && mAlertDialog.isShowing()) {
                return;
            }

            if (((Activity) context) != null && ((Activity) context).isFinishing()) {
                return;
            }

            if (context == null) {
                context = Shy7lo.getAppContext();
            }

            mAlertDialog = new Dialog(context);
            mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mAlertDialog.setCanceledOnTouchOutside(false);
            mAlertDialog.setCancelable(false);
            mAlertDialog.setContentView(R.layout.dialog_upgrade);

            Button btnOkay = (Button) mAlertDialog.findViewById(R.id.btnUpgrade);
            Button btnCancel = (Button) mAlertDialog.findViewById(R.id.btnCancel);
            TextView tvText = (TextView) mAlertDialog.findViewById(R.id.tvUpdateText);
            tvText.setText("" + msg);
            btnOkay.setText("" + setting);

            btnOkay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mAlertDialog != null && mAlertDialog.isShowing()) {
                        mAlertDialog.dismiss();
                        mAlertDialog.cancel();
                    }
                    mOnAlertOkayDialogClick.onOkayClicked(mAlertDialog);

                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mAlertDialog != null && mAlertDialog.isShowing()) {
                        mAlertDialog.dismiss();
                        mAlertDialog.cancel();
                    }

                }
            });

            mAlertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static boolean isMyServiceRunning(Context mContext, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void setMint(Application mContext) {
//        Mint.initAndStartSession(mContext, RestClient.MINT_KEY);
    }

    public static void showInitialScreen(Context mContext) {

        IntentHandler.startActivity(mContext, MaintenanceScreenActivity.class, MaintenanceScreenActivity.RC_MAINTANANCE_CODE);
//        if (mContext instanceof Activity){
//            ((Activity)mContext).finish();
//        }

    }

    public static void getGuestCartToken(final Context mContext) {

        LogUtils.e(TAG, "getGuestCartToken call");

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<JsonElement> call = apiService.getGuestCartToken(Shy7lo.mLangCode);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                LogUtils.e(TAG, "response code:" + response.code());

                if (response.isSuccessful()) {
                    try {

                        JSONObject jResponse = new JSONObject(response.body().toString());
                        if (jResponse != null && jResponse.getString("success").equalsIgnoreCase("1")) {
                            JSONObject jData = jResponse.getJSONObject("data");
                            if (jData != null && jData.has("cart_id")) {
                                String token = jData.getString("cart_id");
                                LogUtils.e(TAG, "response token:" + token);

                                if (!TextUtils.isEmpty(token)) {
                                    MyPref.setPref(mContext, MyPref.GUEST_CART_ID, token);
                                    DBAdapter dbAdapter = new DBAdapter(mContext);
                                    dbAdapter.updateTokenWishlist(token);
//                                    dbAdapter.updateTokenShopping(token);
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
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
            public void onFailure(Call<JsonElement> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }

    }

    public static void showSnackbar(Activity mActivity, String msg, String actionString,
                                    View.OnClickListener listener) {
        Snackbar.make(mActivity.findViewById(android.R.id.content),
                msg,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(actionString, listener).show();
    }

    public static void addProducToWishlist(final Context mContext, final DBAdapter dbAdapter, final Wishlist.WishlistData mWishItem) {

        if (!Utils.isInternetConnected(mContext)) {
            return;
        }

        final String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");

        if (!TextUtils.isEmpty(userToken)) {

            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("sku", "" + mWishItem.getSku());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());

            try {
                ApiInterface apiService =
                        RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
                Call<JsonElement> call = apiService.addProductToWishlist(Shy7lo.mLangCode, "Bearer " + userToken, body);
                call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                        if (response.isSuccessful()) {

                            try {
                                JSONObject jResponse = new JSONObject(response.body().toString());

                                if (jResponse != null && jResponse.getString("success").equals("1")) {
                                    dbAdapter.updateGuestWishItem(mWishItem.getSku(), "0", userToken, "1");
//                                    if (RestClient.isTuneEnable) {
//                                        try {
//                                            Tune tune = Tune.getInstance();
//                                            List<TuneEventItem> mTuneEventItemsList = new ArrayList<TuneEventItem>();
//                                            TuneEventItem mTuneEventItem = new TuneEventItem("" + mWishItem.getName());
//                                            mTuneEventItemsList.add(mTuneEventItem);
//                                            tune.measureEvent(new TuneEvent(TuneEvent.ADD_TO_WISHLIST)
//                                                    .withEventItems(mTuneEventItemsList)
//                                                    .withQuantity(1)
//                                                    .withContentId(mWishItem.getSku())
//                                                    .withRevenue(0)
//                                                    .withContentType(mWishItem.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
//                                                    .withCurrencyCode("" + MyPref.getPref(mContext, MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")));
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }

                                } else if (jResponse != null && jResponse.getString("success").equals("0")) {
                                    dbAdapter.updateIsOnServerWishItem(mWishItem.getSku(), "0");
                                } else if (jResponse != null && jResponse.getString("success").equals("2")) {
                                    dbAdapter.updateIsOnServerWishItem(mWishItem.getSku(), "0");
                                    return;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            if (String.valueOf(response.code()).startsWith("5")) {
                                Utils.showAlertDialog(mContext, "" + response.code());
                            } else {

                                try {
                                    JSONObject jResponse = new JSONObject(response.errorBody().string());
                                    if (jResponse != null && jResponse.getString("success").equals("0")) {
                                        Utils.showToast(mContext, "" + jResponse.getString("message"));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteProduct(final Context mContext, final DBAdapter dbAdapter, final String item_id, final String sku) {

        if (!Utils.isInternetConnected(mContext)) {
            return;
        }

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("item_id", "" + item_id);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                (new JSONObject(jsonParams)).toString());

        Call<JsonElement> callCode = apiService.deleteProductFromWishlist(Shy7lo.mLangCode, "Bearer " + userToken, body);

        callCode.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                if (response.isSuccessful()) {
                    try {

                        JSONObject jResponse = new JSONObject(response.body().toString());
                        if (jResponse != null && jResponse.getString("success").equals("1")) {
                            dbAdapter.removeWishItem(sku);
//                            wishlistFragment.showWishList();
                        } else if (jResponse != null && jResponse.getString("success").equals("0")) {
                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
                            return;
                        } else {
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (String.valueOf(response.code()).startsWith("5")) {
                        Utils.showAlertDialog(mContext, "" + response.code());
                    } else {

                        try {
                            JSONObject jResponse = new JSONObject(response.errorBody().string());
                            if (jResponse != null && jResponse.getString("success").equals("0")) {
                                Utils.showToast(mContext, "" + jResponse.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }
        });
    }

    public static void setInitConfig(Context mContext) {
        String mTune = MyPref.getPref(mContext, MyPref.APP_TUNE, "");
        String mFabric = MyPref.getPref(mContext, MyPref.APP_FABRIC, "");
        String mOneSignal = MyPref.getPref(mContext, MyPref.APP_ONE_SIGNAL, "");
        String mFirebase = MyPref.getPref(mContext, MyPref.APP_FIREBASE, "");
        String mCriteo = MyPref.getPref(mContext, MyPref.APP_CRITEO, "");
        String mAdjust = MyPref.getPref(mContext, MyPref.APP_ADJUST, "");

//        if (TextUtils.isEmpty(mTune) || mTune.equalsIgnoreCase("1")) {
//            RestClient.isTuneEnable = true;
//        } else {
//            RestClient.isTuneEnable = false;
//        }

//        if (TextUtils.isEmpty(mAdjust) || mAdjust.equalsIgnoreCase("1")) {
//            Adjust.setEnabled(true);
//        } else {
//            Adjust.setEnabled(false);
//        }

        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.couponCode != null && !TextUtils.isEmpty(Shy7lo.mAppInit.couponCode.status)
                && Shy7lo.mAppInit.couponCode.status.equals("1")) {
            MyPref.setPref(mContext, MyPref.IS_DIRECT_WEBVIEW, true);
        }else{
            MyPref.setPref(mContext, MyPref.IS_DIRECT_WEBVIEW, false);
        }

//
////        https://stackoverflow.com/questions/16986753/how-to-disable-crashlytics-while-developing
        if (TextUtils.isEmpty(mFabric) || mFabric.equalsIgnoreCase("1")) {
            RestClient.isFabricEnable = true;
            CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(false).build();
            Fabric.with(mContext, new Crashlytics.Builder().core(core).build());
        } else {
            RestClient.isFabricEnable = false;
            CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(true).build();
            Fabric.with(mContext, new Crashlytics.Builder().core(core).build());
        }

        if (TextUtils.isEmpty(mOneSignal) || mOneSignal.equalsIgnoreCase("1")) {
            RestClient.isOneSignalEnable = true;
            OneSignal.setSubscription(true);
        } else {
            RestClient.isOneSignalEnable = false;
            OneSignal.setSubscription(false);
        }

        if (TextUtils.isEmpty(mFirebase) || mFirebase.equalsIgnoreCase("1")) {
            FirebaseCrash.setCrashCollectionEnabled(true);
        } else {
            FirebaseCrash.setCrashCollectionEnabled(false);
        }


        LogUtils.e("", "BuildConfig.DEBUG::" + BuildConfig.DEBUG);
        if (BuildConfig.DEBUG) {
            RestClient.isFabricEnable = false;
            CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(true).build();
            Fabric.with(mContext, new Crashlytics.Builder().core(core).build());

//            RestClient.isTuneEnable = false;
            RestClient.isFirebaseEnable = false;
            FirebaseCrash.setCrashCollectionEnabled(false);
        }
    }

    public static boolean isValidPassword(final String password) {


//        Pattern pattern;
////        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
////        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[d$@$!%*?&#])[A-Za-z\\\\dd$@$!%*?&#]{8,}";
//        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[d$@$!%*?&#])[A-Za-z\\dd$@$!%*?&#]{8,}";
//        pattern = Pattern.compile(PASSWORD_PATTERN);
//
//        return pattern.matcher(password).matches();

        Pattern specailCharPatten = Pattern.compile("[#?!@$%^&*.*(),-]+");
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        boolean isUpperCase = false, isSmallCase = false, isDigit = false, isSpecialChar = false;
        if (UpperCasePatten.matcher(password).find())
            isUpperCase = true;
//            return false;  //nope no letters, stop checking and fail!

        if (lowerCasePatten.matcher(password).find())
            isSmallCase = true;
//            return false;
        //check if there are any numbers
        if (digitCasePatten.matcher(password).find())
            isDigit = true;
//            return false;  //nope no numbers, stop checking and fail!
        //check any valid special characters
        if (specailCharPatten.matcher(password).find())
            isSpecialChar = true;
//            return false;  //nope no special chars, fail!
        LogUtils.e("", "isUpperCase::" + isUpperCase);
        LogUtils.e("", "isSmallCase::" + isSmallCase);
        LogUtils.e("", "isDigit::" + isDigit);
        LogUtils.e("", "isSpecialChar::" + isSpecialChar);

        if (isUpperCase && isSmallCase && isDigit) {
            return true;
        } else if (isUpperCase && isSmallCase && isSpecialChar) {
            return true;
        } else if (isUpperCase && isDigit && isSpecialChar) {
            return true;
        } else if (isSmallCase && isDigit && isSpecialChar) {
            return true;
        }

        return false;
    }

    public static String getOrderDate(String date) {
        LogUtils.e("", "date::" + date);
        String mFormattedDate = "";
        if (!TextUtils.isEmpty(date)) {
            try {
                Date mDate = DateFormate.sdfDateTime.parse(date);
                mFormattedDate = DateFormate.sdfDate.format(mDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mFormattedDate;
    }

    public static void getCMSPage(final Context mContext) {

        if (Shy7lo.mCMSPage == null) {

            ApiInterface apiService =
                    RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
            Call<CMSPage> call = apiService.getCMSPage(Shy7lo.mLangCode);
            call.enqueue(new Callback<CMSPage>() {
                @Override
                public void onResponse(Call<CMSPage> call, Response<CMSPage> response) {

                    LogUtils.e(TAG, "CMSPage response code:" + response.code());

                    if (response.isSuccessful()) {
                        CMSPage mCMSPage = response.body();
                        if (mCMSPage != null && mCMSPage.success.equalsIgnoreCase("1") && mCMSPage.data != null) {
                            Shy7lo.mCMSPage = mCMSPage.data;
                        }

                    } else {
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
                public void onFailure(Call<CMSPage> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });

        }

    }


}
