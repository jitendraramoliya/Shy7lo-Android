package shy7lo.com.shy7lo.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

import static shy7lo.com.shy7lo.pref.MyPref.getPref;

/**
 * Created by Jiten on 03-04-2018.
 */

public class HomeLandingFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "HomeLandingFragment";

    View mView;

    @BindView(R.id.mTopLayout)
    View mTopLayout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ibMore)
    ImageButton ibMore;
    @BindView(R.id.ibRightIcons)
    ImageButton ibRightIcons;
    @BindView(R.id.wvLandingCategory)
    WebView wvLandingCategory;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ibCloseIcons)
    ImageButton ibCloseIcons;
    @BindView(R.id.lnrSearchBar)
    LinearLayout lnrSearchBar;

    Runnable mRunnable;
    Handler mHandler = new Handler();

    String url = "", mMainCateID = "", mMainCateName = "";

    public static final String BNDL_BASE_SCREEN = "BNDL_BASE_SCREEN";
    public static String BNDL_MAIN_CATE_ID = "BNDL_MAIN_CATE_ID";
    public static final String BNDL_MAIN_CATE_NAME = "BNDL_MAIN_CATE_NAME";

    private static HomeLandingFragment homeLandingFragment;

    public static HomeLandingFragment getInstance() {
//        if (homeLandingFragment == null) {
        homeLandingFragment = new HomeLandingFragment();
//        }
        return homeLandingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_home_landing, container, false);
        Utils.setLocale(getActivity());
        ButterKnife.bind(this, mView);
        InitializeControls();
        InitializeControlsAction();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.setDrawerSwipe(false);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.setDrawerSwipe(true);
        }

    }

    private void InitializeControls() {

        ibMore.setImageResource(R.drawable.ic_back);

        if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            mTopLayout.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            etSearch.setScaleX(-1f);
            etSearch.setGravity(Gravity.RIGHT);
        } else {
            mTopLayout.setScaleX(1f);
            tvTitle.setScaleX(1f);
            etSearch.setScaleX(1f);
            etSearch.setGravity(Gravity.LEFT);
        }

        wvLandingCategory.getSettings().setJavaScriptEnabled(true);
        wvLandingCategory.getSettings().setLoadsImagesAutomatically(true);
        wvLandingCategory.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getActivity().getCacheDir().getAbsolutePath();
        LogUtils.e("", "appCachePath::" + appCachePath);
        wvLandingCategory.getSettings().setAppCachePath(appCachePath);
        wvLandingCategory.getSettings().setAppCacheEnabled(true);
        wvLandingCategory.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wvLandingCategory.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mRunnable = new Runnable() {
            @Override
            public void run() {
                String searchString = etSearch.getText().toString();
                if (!TextUtils.isEmpty(searchString) && searchString.length() > 2) {
                    if (getActivity() instanceof HomeActivity) {
                        etSearch.setText("");
                        Bundle bundle = new Bundle();
                        bundle.putString(SearchAlgoliaFragment.BNDL_SEARCH_KEY, searchString);
                        HomeActivity activity = (HomeActivity) getActivity();
                        activity.loadSearchWithBundle(bundle);
                    }
                }
            }
        };

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mHandler.removeCallbacks(mRunnable);
                if (lnrSearchBar.getVisibility() == View.VISIBLE) {
                    mHandler.postDelayed(mRunnable, 1000);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Bundle extras = getArguments();
        if (extras != null) {
            url = extras.getString(BNDL_BASE_SCREEN);
            mMainCateID = extras.getString(BNDL_MAIN_CATE_ID);
            mMainCateName = extras.getString(BNDL_MAIN_CATE_NAME);
            LogUtils.e("", "main_category_id::" + mMainCateID);
            LogUtils.e(TAG, "url:::" + url);
        }

        wvLandingCategory.loadUrl(url);

        wvLandingCategory.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                LogUtils.e("", "shouldOverrideUrlLoading url::" + url);
//                view.loadUrl(url);
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

                } else if (url.contains("sub_category=")) {

                    String mCategoryID = url.split("sub_category=")[1];
                    Bundle bundle = new Bundle();
                    bundle.putString(SubCategoryForProductFragment.BK_PRODUCT_ID, mCategoryID);
                    bundle.putString(SubCategoryForProductFragment.BNDL_MAIN_CATE_ID, "" + mMainCateID);
                    bundle.putString(SubCategoryForProductFragment.BNDL_MAIN_CATE_NAME, "" + mMainCateName);

                    HomeActivity activity = (HomeActivity) getActivity();
                    if (activity != null) {
                        activity.loadSubCategory(bundle);
                    }

                } else {

                    String mSearchCriteria = url;
                    Bundle bundle = new Bundle();
                    bundle.putString(ProductsItemsFragment.BK_PRODUCT_ITEMS, mSearchCriteria);
                    bundle.putString(ProductsItemsFragment.BNDL_CATEGORY, ""+mMainCateName);
                    bundle.putString(ProductsItemsFragment.BNDL_MAIN_CATE_ID, "" + mMainCateID);
                    bundle.putBoolean(ProductsItemsFragment.IS_FROM_BANNER, true);

                    HomeActivity activity = (HomeActivity) getActivity();
                    if (activity != null) {
                        activity.loadBannerItems(bundle);
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

    }

    private void InitializeControlsAction() {
        ibMore.setOnClickListener(this);
        ibRightIcons.setOnClickListener(this);
        ibCloseIcons.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == ibMore) {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
//                activity.openDrawer();
                activity.onBackPressed();
            }
        } else if (view == ibRightIcons) {
//            showSearchBar();
//            if (getActivity() instanceof HomeActivity) {
//                HomeActivity activity = (HomeActivity) getActivity();
//                activity.loadSeach();
//            }

            if (getActivity() instanceof HomeActivity) {

                Bundle bundle = new Bundle();
                bundle.putString(SearchAlgoliaFragment.BNDL_SEARCH_KEY, "");
                HomeActivity activity = (HomeActivity) getActivity();
                activity.loadSearchWithBundle(bundle);

            }

        } else if (view == ibCloseIcons) {

            hideSearchBar();
        }
    }

    private void showSearchBar() {
//        lnrSearchBar.startAnimation(animOpen);
        lnrSearchBar.setVisibility(View.VISIBLE);
    }

    private void hideSearchBar() {
        etSearch.setText("");
//        lnrSearchBar.startAnimation(animClose);
        lnrSearchBar.setVisibility(View.GONE);
    }
}
