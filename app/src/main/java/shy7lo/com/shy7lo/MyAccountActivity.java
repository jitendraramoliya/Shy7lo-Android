//package shy7lo.com.shy7lo;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ImageButton;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.firebase.analytics.FirebaseAnalytics;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import shy7lo.com.shy7lo.application.Shy7lo;
//import shy7lo.com.shy7lo.model.Maintenance;
//import shy7lo.com.shy7lo.pref.MyPref;
//import shy7lo.com.shy7lo.rest.ApiInterface;
//import shy7lo.com.shy7lo.rest.RestClient;
//import shy7lo.com.shy7lo.utils.LogUtils;
//import shy7lo.com.shy7lo.utils.Utils;
//
//import static shy7lo.com.shy7lo.pref.MyPref.CART_ITEM_COUNT;
//
///**
// * Created by JITEN-PC on 12-05-2017.
// */
//
//public class MyAccountActivity extends Activity {
//
//
//    @BindView(R.id.ibCancel)
//    ImageButton ibCancel;
//    @BindView(R.id.rltTitle)
//    RelativeLayout rltTitle;
//    @BindView(R.id.tvTitle)
//    TextView tvTitle;
//    @BindView(R.id.tvTopLayer)
//    TextView tvTopLayer;
//    @BindView(R.id.ibLogout)
//    ImageButton ibLogout;
//
//    @BindView(R.id.wvMyOrder)
//    WebView wvMyOrder;
//
//
//    public static String BNDL_USER_ID = "BNDL_USER_ID";
//    String url = "";
//
//    private FirebaseAnalytics mFirebaseAnalytics;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Utils.setMint(this.getApplication());
//        setContentView(R.layout.activity_my_account);
//
//        Utils.setLocale(getActivity());
//        ButterKnife.bind(getActivity());
//        InitializeControls();
//        InitializeControlsAction();
//        checkForMaintenance();
//
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
//        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
//        mFirebaseAnalytics.setMinimumSessionDuration(500);
//        mFirebaseAnalytics.setSessionTimeoutDuration(1000000);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Shy7lo.setActivityContext(getActivity());
//        if (!Utils.isInternetConnected(getActivity())) {
//            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
//                @Override
//                public void onRetryClicked(Dialog dialog) {
//                    if (Utils.isInternetConnected(getActivity())) {
//                        if (dialog != null && dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        onResume();
//                    }
//                }
//            });
//            return;
//        }
//
//        if (BuildConfig.DEBUG) {
//            mFirebaseAnalytics.setAnalyticsCollectionEnabled(false);
//        } else {
//            mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
//        }
//        mFirebaseAnalytics.logEvent("My_Order", new Bundle());
//
//        LogUtils.e("", "url::" + url);
//
//        wvMyOrder.loadUrl(url);
//    }
//
//    private void InitializeControls() {
//
//        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            tvTitle.setBackgroundResource(R.drawable.ic_top_logo_ar);
//            rltTitle.setScaleX(-1f);
//            tvTitle.setScaleX(-1f);
//        } else {
//            tvTitle.setBackgroundResource(R.drawable.ic_top_logo);
//            rltTitle.setScaleX(1f);
//            tvTitle.setScaleX(1f);
//        }
//
//        wvMyOrder.getSettings().setJavaScriptEnabled(true);
//        wvMyOrder.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public void onLoadResource(WebView view, String url) {
//                wvMyOrder.loadUrl("javascript:(function() { " +
//                        "var head = document.getElementsByTagName('header')[0];"
//                        + "head.parentNode.removeChild(head);" +
//                        "document.querySelector('ul li.item:nth-of-type(8)').style.display ='none';" +
//                        "var foot = document.getElementsByTagName('footer')[0];"
//                        + "foot.parentNode.removeChild(foot);" +
//                        "document.body.style.marginTop = '-120px';" +
//                        "})()");
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                Utils.showProgressDialog(getActivity());
//
//                tvTopLayer.setVisibility(View.VISIBLE);
//
//                wvMyOrder.loadUrl("javascript:(function() { " +
//                        "var head = document.getElementsByTagName('header')[0];"
//                        + "head.parentNode.removeChild(head);" +
//                        "document.querySelector('ul li.item:nth-of-type(8)').style.display ='none';" +
//                        "var foot = document.getElementsByTagName('footer')[0];"
//                        + "foot.parentNode.removeChild(foot);" +
//                        "document.body.style.marginTop = '-120px';" +
//                        "})()");
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                Utils.closeProgressDialog();
//
//                tvTopLayer.setVisibility(View.GONE);
//
//                wvMyOrder.loadUrl("javascript:(function() { " +
//                        "var head = document.getElementsByTagName('header')[0];"
//                        + "head.parentNode.removeChild(head);" +
//                        "document.querySelector('ul li.item:nth-of-type(8)').style.display ='none';" +
//                        "var foot = document.getElementsByTagName('footer')[0];"
//                        + "foot.parentNode.removeChild(foot);" +
//                        "document.body.style.marginTop = '-120px';" +
//                        "})()");
//
//            }
//
//        });
//
//
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            String id = bundle.getString(BNDL_USER_ID);
//            if (!TextUtils.isEmpty(id)) {
//
//                LogUtils.e("", "id::" + id);
//
//                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    url = RestClient.API_NEW_URL + "/ar/shy7locheckout/checkout/login/id/" + id + "/token/5u8vv8q8s6244i523reTvs5w4f5r5ok4";
//                } else {
//                    url = RestClient.API_NEW_URL + "/en/shy7locheckout/checkout/login/id/" + id + "/token/5u8vv8q8s6244i523reTvs5w4f5r5ok4";
//                }
//
//
//            }
//        }
//
//    }
//
//    private void InitializeControlsAction() {
//        ibCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//        ibLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                MyPref.setPref(getActivity(), MyPref.USER_CART_ID, "");
//                MyPref.setPref(getActivity(), CART_ITEM_COUNT, 0);
//                MyPref.setPref(getActivity(), MyPref.USER_ID, "");
//                MyPref.setPref(getActivity(), MyPref.USER_FIRSTNAME, "");
//                MyPref.setPref(getActivity(), MyPref.USER_LASTNAME, "");
//                finish();
//
//            }
//        });
//    }
//
//    private void checkForMaintenance() {
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
//        Call<Maintenance> callCode = apiService.getMaintenanceStatus(Shy7lo.mLangCode);
//        callCode.enqueue(new Callback<Maintenance>() {
//            @Override
//            public void onResponse(Call<Maintenance> call, Response<Maintenance> response) {
//                if (response.isSuccessful()) {
//                    Maintenance maintenance = response.body();
//                    if (maintenance != null && maintenance.success.equalsIgnoreCase("1")) {
//
//                    } else if (maintenance != null && maintenance.success.equalsIgnoreCase("2")) {
//                        Utils.showInitialScreen(getActivity());
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Maintenance> call, Throwable t) {
//
//            }
//        });
//
//    }
//
//    private Activity getActivity() {
//        return MyAccountActivity.this;
//    }
//}
