package shy7lo.com.shy7lo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.Maintenance;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by Jiten on 13-08-2017.
 */

public class MaintenanceScreenActivity extends Activity {

    WebView wvInitialScreen;
    TextView tvTitle;

    String url_en = RestClient.API_PORTAL_SHYLABS_URL + "/mobile/main-landing-screen/en";
    String url_ar = RestClient.API_PORTAL_SHYLABS_URL + "/mobile/main-landing-screen/ar";

    public static int RC_MAINTANANCE_CODE = 1555;
    public static String IS_RETURN = "IS_RETURN";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_initial_screen);

        InitializeControls();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            wvInitialScreen.loadUrl(url_ar);
            tvTitle.setBackgroundResource(R.drawable.ic_top_logo_ar);
        } else {
            wvInitialScreen.loadUrl(url_en);
            tvTitle.setBackgroundResource(R.drawable.ic_top_logo);
        }

    }

    private void InitializeControls() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        wvInitialScreen = (WebView) findViewById(R.id.wvInitialScreen);
        wvInitialScreen.getSettings().setJavaScriptEnabled(true);
        wvInitialScreen.getSettings().setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wvInitialScreen.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        wvInitialScreen.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                LogUtils.e("", "shouldOverrideUrlLoading url::" + url);
                if (url.contains("refresh")) {
                    checkForMaintenance();
                } else if (url.contains("target=browser?")) {

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
                    if (url.contains("main-cat=")) {
                        String position = url.split("main-cat=")[1];
//                        if (position.equals("3")) {
//                            MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 2);
//                        } else if (position.equals("2")) {
//                            MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 1);
//                        } else {
                            MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0);
//                        }

                        IntentHandler.startActivity(getActivity(), HomeActivity.class);
                        finish();
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
                Utils.showProgressDialog(getActivity());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Utils.closeProgressDialog();
            }
        });

        checkForMaintenance();

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
                        Intent back = new Intent();
                        back.putExtra(IS_RETURN, true);
                        setResult(RESULT_OK, back);
                        finish();
                    } else if (maintenance != null && maintenance.success.equalsIgnoreCase("2")) {

                    }

                }
            }

            @Override
            public void onFailure(Call<Maintenance> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private Context getActivity() {
        return MaintenanceScreenActivity.this;
    }
}
