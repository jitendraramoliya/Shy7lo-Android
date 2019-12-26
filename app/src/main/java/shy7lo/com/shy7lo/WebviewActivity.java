package shy7lo.com.shy7lo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.Maintenance;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by Jiten on 14-09-2017.
 */

public class WebviewActivity extends AppCompatActivity {

    public static final String TAG = "WebviewActivity";


    @BindView(R.id.rltTitle)
    RelativeLayout rltTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvTopLayer)
    TextView tvTopLayer;
    @BindView(R.id.wvWeb)
    WebView wvWeb;

    @BindView(R.id.ibCancel)
    ImageButton ibCancel;

    public static String BNDL_URL = "BNDL_URL";
    public static String BNDL_TITLE = "BNDL_TITLE";

    private String mUrl = "";

    private Activity getActivity() {
        return WebviewActivity.this;
    }

    private FirebaseAnalytics mFirebaseAnalytics;
    private int marginTop = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_webview);
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
        mFirebaseAnalytics.logEvent("FAQ", new Bundle());

        if (!TextUtils.isEmpty(mUrl)) {
            wvWeb.loadUrl(mUrl);
        }
    }

    private void InitializeControls() {

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            tvTitle.setBackgroundResource(R.drawable.ic_top_logo_ar);
            rltTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            marginTop = 50;
        } else {
//            tvTitle.setBackgroundResource(R.drawable.ic_top_logo);
            rltTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);
            marginTop = 100;
        }

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wvWeb.getSettings().setJavaScriptEnabled(true);
        wvWeb.setWebViewClient(new WebViewClient() {

            @Override
            public void onLoadResource(WebView view, String url) {
                wvWeb.loadUrl("javascript:(function() { " +
                        "document.querySelector('.page-header').style.display ='none';" +
                        "document.querySelector('.slick-list').style.display ='none';" +
                        "document.querySelector('.page-footer').style.display ='none';" +
                        "document.body.style.marginTop = '-"+marginTop+"px';" +
                        "})()");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Utils.showProgressDialog(getActivity());

                tvTopLayer.setVisibility(View.VISIBLE);

                wvWeb.loadUrl("javascript:(function() { " +
                        "document.querySelector('.page-header').style.display ='none';" +
                        "document.querySelector('.slick-list').style.display ='none';" +
                        "document.querySelector('.page-footer').style.display ='none';" +
                        "document.body.style.marginTop = '-"+marginTop+"px';" +
                        "})()");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Utils.closeProgressDialog();

                tvTopLayer.setVisibility(View.GONE);

                wvWeb.loadUrl("javascript:(function() { " +
                        "document.querySelector('.page-header').style.display ='none';" +
                        "document.querySelector('.slick-list').style.display ='none';" +
                        "document.querySelector('.page-footer').style.display ='none';" +
                        "document.body.style.marginTop = '-"+marginTop+"px';" +
                        "})()");

            }

        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUrl = bundle.getString(BNDL_URL);
            String title = bundle.getString(BNDL_TITLE);
            if (!TextUtils.isEmpty(title)) {
                tvTitle.setText("" + title);
                if (title.equalsIgnoreCase(""+getString(R.string.sizes_table))) {
                    ibCancel.setImageResource(R.drawable.ic_back);
                }
            }
            LogUtils.e("", "url::" + mUrl);

        }

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


}
