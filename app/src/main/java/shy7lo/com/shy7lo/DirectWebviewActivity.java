package shy7lo.com.shy7lo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

public class DirectWebviewActivity extends AppCompatActivity {

    public static final String TAG = "DirectWebviewActivity";

    @BindView(R.id.wvWeb)
    WebView wvWeb;

    public static String BNDL_URL = "BNDL_URL";

    private String mUrl = "";

    private Activity getActivity() {
        return DirectWebviewActivity.this;
    }

    private FirebaseAnalytics mFirebaseAnalytics;
//    private int marginTop = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_direct_webview);
        Utils.setLocale(getActivity());
        ButterKnife.bind(this);

        InitializeControls();
//        checkForMaintenance();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        mFirebaseAnalytics.setMinimumSessionDuration(500);
        mFirebaseAnalytics.setSessionTimeoutDuration(1000000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Shy7lo.setActivityContext(getActivity());
//        if (BuildConfig.DEBUG) {
//            mFirebaseAnalytics.setAnalyticsCollectionEnabled(false);
//        } else {
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
//        }
        mFirebaseAnalytics.logEvent("DirectWebview", new Bundle());


    }

    private void InitializeControls() {

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
        } else {
        }

        wvWeb.getSettings().setJavaScriptEnabled(true);
        wvWeb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.e("", "url::"+url);

                if (url.contains("cout=")){

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);

                    return true;
                }else{
                    view.loadUrl(url);
                    return false;
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
//                wvWeb.loadUrl("javascript:(function() { " +
//                        "document.querySelector('.page-header').style.display ='none';" +
//                        "document.querySelector('.slick-list').style.display ='none';" +
//                        "document.querySelector('.page-footer').style.display ='none';" +
//                        "document.body.style.marginTop = '-"+marginTop+"px';" +
//                        "})()");



            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Utils.showProgressDialog(getActivity());

//                tvTopLayer.setVisibility(View.VISIBLE);
//
//                wvWeb.loadUrl("javascript:(function() { " +
//                        "document.querySelector('.page-header').style.display ='none';" +
//                        "document.querySelector('.slick-list').style.display ='none';" +
//                        "document.querySelector('.page-footer').style.display ='none';" +
//                        "document.body.style.marginTop = '-"+marginTop+"px';" +
//                        "})()");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Utils.closeProgressDialog();

//                tvTopLayer.setVisibility(View.GONE);
//
//                wvWeb.loadUrl("javascript:(function() { " +
//                        "document.querySelector('.page-header').style.display ='none';" +
//                        "document.querySelector('.slick-list').style.display ='none';" +
//                        "document.querySelector('.page-footer').style.display ='none';" +
//                        "document.body.style.marginTop = '-"+marginTop+"px';" +
//                        "})()");

            }

        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUrl = bundle.getString(BNDL_URL);
            LogUtils.e("", "url::" + mUrl);
            if (!TextUtils.isEmpty(mUrl)) {
                wvWeb.loadUrl(mUrl);
            }
        }

    }

}