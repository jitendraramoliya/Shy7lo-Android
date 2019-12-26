package shy7lo.com.shy7lo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.fragment.MainCategoryForProductFragment;
import shy7lo.com.shy7lo.model.AppInit;
import shy7lo.com.shy7lo.model.Maintenance;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.sync.WishListSync;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by Jiten on 13-08-2017.
 */

public class InitialScreenActivity extends Activity {

    WebView wvInitialScreen;
    TextView tvTitle;

    String url_en = RestClient.API_PORTAL_SHYLABS_URL + "/mobile/new/main/en";
    String url_ar = RestClient.API_PORTAL_SHYLABS_URL + "/mobile/new/main/ar";

    public static String IS_EXIT = "IS_EXIT";
    public static int RC_INITIAL_CODE = 1520;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 50;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    DBAdapter dbAdapter;

    List<AppInit.BaseScreen> mBaseScreenList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_initial_screen);

        InitializeControls();

//        MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0);
//
//        IntentHandler.startActivity(getActivity(), HomeActivity.class);
//        finish();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(InitialScreenActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            LogUtils.i("", "Displaying permission rationale to provide additional context.");

            Utils.showSnackbar(InitialScreenActivity.this, getString(R.string.permission_rationale), getString(android.R.string.ok),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(InitialScreenActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });

        } else {
            LogUtils.i("", "Requesting permission");
            ActivityCompat.requestPermissions(InitialScreenActivity.this,
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
                Log.i("", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {

                Utils.showSnackbar(InitialScreenActivity.this, getString(R.string.permission_denied_explanation), getString(R.string.settings),
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

    private void getLocation() {

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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
//                        Tune.getInstance().setLocation(task.getResult());
                        LatLng mLatLng = new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());
                        if (mLatLng != null) {
                            LogUtils.e("", "Last Location Lat::" + mLatLng.latitude + " Long::" + mLatLng.longitude);
                        }
                    } else {
                        LogUtils.d("", "Current location is null. Using defaults.");
                        LogUtils.e("", "Exception:" + task.getException());
                        getLocation();
                    }
                }
            });
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLocation();
        }

        Shy7lo.setActivityContext(getActivity());
        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        onResume();
                    }
                }
            });
            return;
        }

        loadUrl();

    }

    private void loadUrl() {
        String url = "";
        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            wvInitialScreen.loadUrl(url_ar);
            url = url_ar;
            tvTitle.setBackgroundResource(R.drawable.ic_top_logo_ar);
        } else {
//            wvInitialScreen.loadUrl(url_en);
            url = url_en;
            tvTitle.setBackgroundResource(R.drawable.ic_top_logo);
        }

        if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.WEB_CACHE_VERSION, ""))) {
            url = url + "?version=" + MyPref.getPref(getActivity(), MyPref.WEB_CACHE_VERSION, "");
        }

        wvInitialScreen.loadUrl(url);
    }

    private void InitializeControls() {
        dbAdapter = new DBAdapter(getActivity());
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        wvInitialScreen = (WebView) findViewById(R.id.wvInitialScreen);
        wvInitialScreen.getSettings().setJavaScriptEnabled(true);
        wvInitialScreen.getSettings().setLoadsImagesAutomatically(true);
        wvInitialScreen.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getCacheDir().getAbsolutePath();
        LogUtils.e("", "appCachePath::" + appCachePath);
        wvInitialScreen.getSettings().setAppCachePath(appCachePath);
        wvInitialScreen.getSettings().setAppCacheEnabled(true);
        wvInitialScreen.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wvInitialScreen.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        checkForMaintenance();

        wvInitialScreen.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                LogUtils.e("", "shouldOverrideUrlLoading url::" + url);
                if (url.contains("refresh")) {
                    loadUrl();
                }
                if (url.contains("target=browser?")) {

                    String mTargetURL = url.split("target=browser?")[1];
                    if (String.valueOf(mTargetURL.charAt(0)).equalsIgnoreCase("?")) {
                        mTargetURL = mTargetURL.substring(1);
                    }
                    LogUtils.e("", "mTargetURL::" + mTargetURL);
                    if (!TextUtils.isEmpty(mTargetURL) && mTargetURL.startsWith("http")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTargetURL));
                        startActivity(browserIntent);
                    }

                } else {
                    if (url.contains("category_id=")) {
                        String mCategoryID = url.split("category_id=")[1];
                        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.landingScreens.baseScreens != null) {
                            mBaseScreenList = Shy7lo.mAppInit.landingScreens.baseScreens;
                            goToHomeScreen(mCategoryID);
                        } else {
                            if (!Utils.isInternetConnected(getActivity())) {

                                MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0);
                                IntentHandler.startActivity(getActivity(), HomeActivity.class, RC_INITIAL_CODE);
                                Bundle bundle = new Bundle();
                                bundle.putString(MainCategoryForProductFragment.BNDL_MAIN_CATE_ID, mCategoryID);
                                IntentHandler.startActivity(getActivity(), HomeActivity.class, bundle);
                                finish();
                            } else {
                                getInitApi(mCategoryID);
                            }

                        }
//                        String position = url.split("main-cat=")[1];
//                        String mMainStr = url.split("main-cat=")[1];
//                        String position = mMainStr.split("&category_id=")[0];
//                        String mCategoryID = mMainStr.split("&category_id=")[1];
//                        LogUtils.e("", "position::" + position);
//                        if (position.equals("3")) {
//                            MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 2);
//                        } else if (position.equals("2")) {
//                            MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 1);
//                        } else {
//                            MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0);
//                        }
//                        LogUtils.e("", "DEFAULT_CATEGORY_POSITION::" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0));
//                        IntentHandler.startActivity(getActivity(), HomeActivity.class, RC_INITIAL_CODE);
//                        Bundle bundle = new Bundle();
////                        bundle.putString(MainCategoryForProductFragment.BNDL_MAIN_CATE_ID, mCategoryID);
//                        IntentHandler.startActivity(getActivity(), HomeActivity.class, bundle);
//                        finish();
                    }
                }
                return true;
            }


            // here you execute an action when the URL you want is about to load
            @Override
            public void onLoadResource(WebView view, String url) {
                LogUtils.e("", "onLoadResource url::" + url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (getActivity() != null) {
                    Utils.showProgressDialog(getActivity());
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Utils.closeProgressDialog();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LogUtils.e("", "onReceivedError");
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                LogUtils.e("", "onReceivedHttpError::");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                LogUtils.e("", "onReceivedSslError");
            }
        });

//        if (!MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, false)) {
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
//        }
//
//        if (MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, false) && !MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_IS_REGISTERED, false)) {
//            Map<String, Object> jsonParams = new ArrayMap<>();
//            jsonParams.put("device_token", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
//            jsonParams.put("device_type", "android");
//            jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));
//
//            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
//
//            ApiInterface apiService =
//                    RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//            Call<JsonElement> callCode = apiService.registerPushToken(Shy7lo.mLangCode, body);
//            callCode.enqueue(new Callback<JsonElement>() {
//                @Override
//                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                    if (response.isSuccessful()){
//                        MyPref.setPref(getActivity(), MyPref.ONE_SIGNAL_IS_REGISTERED, true);
////                        Utils.showToast(getActivity(), "Register Token Api Called: "+MyPref.getPref(getApplicationContext(), MyPref.ONE_SIGNAL_USER_ID, ""));
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<JsonElement> call, Throwable t) {
//
//                }
//            });
//        }
//        checkForMaintenance();
//        ShoppingBagSync mShoppingBagSync = new ShoppingBagSync(getActivity());
//        mShoppingBagSync.syncShoppingBagList(getActivity(), dbAdapter);

        // for offline uncoment this
        WishListSync mWishListSync = new WishListSync(getActivity());
        mWishListSync.syncWishList(getActivity(), dbAdapter);

//        final String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
//        final String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
//        if (!TextUtils.isEmpty(userToken) || !TextUtils.isEmpty(guestToken)) {
//            getTotalAmount();
//        }

    }

    private void goToHomeScreen(String mCategoryID) {
        if (!TextUtils.isEmpty(mCategoryID)) {
            if (mBaseScreenList != null && mBaseScreenList.size() > 0) {
                for (int i = 0; i < mBaseScreenList.size(); i++) {
//                    if (mBaseScreenList.get(i).cat_id.equalsIgnoreCase("" + mCategoryID)) { // uncomment when instial screen active
//                        MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, i);
                        MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0);
                        IntentHandler.startActivity(getActivity(), HomeActivity.class, RC_INITIAL_CODE);
                        Bundle bundle = new Bundle();
                        bundle.putString(MainCategoryForProductFragment.BNDL_MAIN_CATE_ID, mCategoryID);
                        IntentHandler.startActivity(getActivity(), HomeActivity.class, bundle);
                        finish();
                        break;
//                    }
                }
            }
        } else {
            MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0);
            IntentHandler.startActivity(getActivity(), HomeActivity.class, RC_INITIAL_CODE);
            Bundle bundle = new Bundle();
//            bundle.putString(MainCategoryForProductFragment.BNDL_MAIN_CATE_ID, mCategoryID);
            IntentHandler.startActivity(getActivity(), HomeActivity.class, bundle);
            finish();
        }
    }

    private void getInitApi(final String mCategoryID) {

        if (!Utils.isInternetConnected(getActivity())) {
            return;
        }

        Utils.showProgressDialog(getActivity());

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

                LogUtils.e("", "response code:" + response.code());

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    try {

                        Shy7lo.mAppInit = response.body();
                        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.success.equalsIgnoreCase("1")) {
                            if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.landingScreens != null) {
                                mBaseScreenList = Shy7lo.mAppInit.landingScreens.baseScreens;
                                goToHomeScreen(mCategoryID);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
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

    private void checkForMaintenance() {

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<Maintenance> callCode = apiService.getMaintenanceStatus(Shy7lo.mLangCode);
        callCode.enqueue(new Callback<Maintenance>() {
            @Override
            public void onResponse(Call<Maintenance> call, Response<Maintenance> response) {
                if (response.isSuccessful()) {
                    Maintenance maintenance = response.body();
                    if (maintenance != null && maintenance.success.equalsIgnoreCase("1")) {

                    } else if (maintenance != null && maintenance.success.equalsIgnoreCase("2")) {
                        Utils.showInitialScreen(getActivity());
                    }

                }
            }

            @Override
            public void onFailure(Call<Maintenance> call, Throwable t) {

            }
        });

    }

//    Dialog mSessionDialog;
//    private BroadcastReceiver mSessionExpireReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            LogUtils.e("", "mSessionExpireReceiver call");
//
//            try {
//
//                if (mSessionDialog != null && mSessionDialog.isShowing()) {
//                    return;
//                }
//
//                mSessionDialog = new Dialog(getActivity());
//                mSessionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                mSessionDialog.setCanceledOnTouchOutside(false);
//                mSessionDialog.setCancelable(false);
//                mSessionDialog.setContentView(R.layout.dialog_login);
//
//                TextView tvFavouriteText = (TextView) mSessionDialog.findViewById(R.id.tvFavouriteText);
//                Button btnLoginNow = (Button) mSessionDialog.findViewById(R.id.btnLoginNow);
//                Button btnGuest = (Button) mSessionDialog.findViewById(btnCancel);
//                tvFavouriteText.setText(getResources().getString(R.string.msg_session_expire));
//                btnLoginNow.setText(getResources().getString(R.string.login_now));
//                btnGuest.setText(getResources().getString(R.string.continue_guest));
//
//                btnGuest.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if (mSessionDialog != null && mSessionDialog.isShowing()) {
//                            mSessionDialog.dismiss();
//                            mSessionDialog.cancel();
//                        }
//
//                        getGuestCartToken();
//
//                    }
//                });
//
//                btnLoginNow.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if (mSessionDialog != null && mSessionDialog.isShowing()) {
//                            mSessionDialog.dismiss();
//                            mSessionDialog.cancel();
//                        }
//
//                        Bundle bundle = new Bundle();
//                        bundle.putString(SignInUpActivity.BNDL_VIEW_TYPE, "SignIn");
//                        bundle.putString(SignInUpActivity.BNDL_SCREEN_FROM, "SessionExpired");
//                        IntentHandler.startActivity(getActivity(), SignInUpActivity.class, bundle);
//
//                    }
//                });
//
//                mSessionDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                    @Override
//                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                        return keyCode == KeyEvent.KEYCODE_BACK;
//                    }
//                });
//                mSessionDialog.show();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };

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
//                            MyPref.setPref(getActivity(), CART_ITEM_COUNT, 0);
//                            MyPref.setPref(getActivity(), MyPref.USER_ID, "");
//                            MyPref.setPref(getActivity(), MyPref.USER_FIRSTNAME, "");
//                            MyPref.setPref(getActivity(), MyPref.USER_LASTNAME, "");
//                            MyPref.setPref(getActivity(), MyPref.USER_PHONE, "");
//                            MyPref.setPref(getActivity(), MyPref.USER_ADDRESS, "");
//                            MyPref.setPref(getActivity(), MyPref.USER_CITY, "");
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_INITIAL_CODE && resultCode == RESULT_OK) {
//            if (data.getExtras() != null && data.getExtras().containsKey(IS_EXIT)) {
//                if (data.getExtras().getBoolean(IS_EXIT)) {
//                    finish();
//                }
//            }
//        }
//
//    }

//    private void getTotalAmount() {
//
//        Call<JsonElement> callCode;
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
//        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
//
//        if (!TextUtils.isEmpty(userToken)) {
//            callCode = apiService.getUserTotalAmount("en", "Bearer " + userToken);
//        } else {
//            String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
//            callCode = apiService.getGuestTotalAmount(Shy7lo.mLangCode, guestToken);
//        }
//
//        callCode.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                Utils.closeProgressDialog();
//
//                if (response.isSuccessful()) {
//                    try {
//
//                        JSONObject jsonResponse = new JSONObject(response.body().toString());
//                        if (jsonResponse != null && jsonResponse.getString("success").equals("1")) {
//                            JSONObject jsonObject= jsonResponse.getJSONObject("data");
//                            JSONArray jsonArray = null;
//                            if (jsonObject != null ){
//                                jsonObject.getJSONArray("totals");
//                            }
//                            CartTotalAmount mCartTotalAmount = new Gson().fromJson(jsonArray.toString(), CartTotalAmount.class);
//                            MyPref.setPref(getActivity(), MyPref.TOTAL_AMT_LIST, "" + new Gson().toJson(mCartTotalAmount));
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//            }
//        });
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mSessionExpireReceiver != null) {
//            unregisterReceiver(mSessionExpireReceiver);
//        }
    }

    private Context getActivity() {
        return InitialScreenActivity.this;
    }
}
