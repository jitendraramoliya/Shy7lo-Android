package shy7lo.com.shy7lo.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import java.util.List;

import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.model.AppInit;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;


/**
 * Created by jiten on 2/9/16.
 */
public class HomeTabFragment extends Fragment {

    public static final String TAG = "HomeTabFragment";

    View mView;

    WebView wvCategory;
    public static final String BNDL_BASE_ATTRIBUTE = "BNDL_BASE_ATTRIBUTE";
    public static final String BNDL_BASE_SCREEN = "BNDL_BASE_SCREEN";
    public static final String BNDL_MAIN_CATE_ID = "BNDL_MAIN_CATE_ID";
    public static final String BNDL_MAIN_CATE_NAME = "BNDL_MAIN_CATE_NAME";
    public static final String BNDL_CHILD_SCREEN = "BNDL_CHILD_SCREEN";
    String url = "", mMainCateID = "", mMainCateName = "";

    List<AppInit.ChildScreen> mChildScreens;

    AppInit.BaseScreen mBaseScreen;

    static HomeTabFragment mHomeTabFragment;

    public static HomeTabFragment getInstance() {
        if (mHomeTabFragment == null) {
            mHomeTabFragment = new HomeTabFragment();
        }
        return mHomeTabFragment;
    }

    private Context mContext;

    private Context getMyActivity() {
        return mContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_tab, container, false);
        mContext = getActivity();
        Utils.setLocale(getActivity());
        LogUtils.e(TAG, "onCreateView");
        InitializeControls();


        return mView;
    }

    private void InitializeControls() {

        wvCategory = (WebView) mView.findViewById(R.id.wvCategory);
        wvCategory.getSettings().setJavaScriptEnabled(true);
        wvCategory.getSettings().setLoadsImagesAutomatically(true);
        wvCategory.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getActivity().getCacheDir().getAbsolutePath();
        LogUtils.e("", "appCachePath::" + appCachePath);
        wvCategory.getSettings().setAppCachePath(appCachePath);
        wvCategory.getSettings().setAppCacheEnabled(true);
        wvCategory.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wvCategory.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        Bundle extras = getArguments();
        if (extras != null) {
//            url = extras.getString(BNDL_BASE_SCREEN);
//            mMainCateID = extras.getString(BNDL_MAIN_CATE_ID);
//            mMainCateName = extras.getString(BNDL_MAIN_CATE_NAME);
//            LogUtils.e(TAG, "url:::" + url);
//            String childList = extras.getString(BNDL_CHILD_SCREEN);
//            LogUtils.e(TAG, "childList:::" + childList);
//            if (!TextUtils.isEmpty(childList)) {
////                HomeTabList.Data data = new Gson().fromJson(childList, HomeTabList.Data.class);
////                mChildScreens = data.childScreens;
//                mChildScreens = new Gson().fromJson(childList, new TypeToken<ArrayList<AppInit.ChildScreen>>() {
//                }.getType());
//            }
//
////            String category = extras.getString(BNDL_BASE_SCREEN);
////            mBaseScreen = new Gson().fromJson(category, HomeTabList.BaseScreen.class);
            mBaseScreen = new Gson().fromJson(extras.getString(BNDL_BASE_ATTRIBUTE), AppInit.BaseScreen.class);
            if (mBaseScreen != null) {
                LogUtils.e("", "mBaseScreen.mode::"+mBaseScreen.mode);
                if (mBaseScreen.mode.equalsIgnoreCase("web")) {
                    mMainCateID = mBaseScreen.attribute.cat_id;
                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                        mMainCateName = mBaseScreen.titleAr;
                        url = mBaseScreen.attribute.urlAr;
                    } else {
                        mMainCateName = mBaseScreen.titleEn;
                        url = mBaseScreen.attribute.url;
                    }
                    LogUtils.e("", "url::"+url);
                } else  if (mBaseScreen.mode.equalsIgnoreCase("query")){

                    return;

                }
            }

        }

        if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.WEB_CACHE_VERSION, ""))) {
            url = url + "?version=" + MyPref.getPref(getActivity(), MyPref.WEB_CACHE_VERSION, "");
        }

        LogUtils.e("", "HomeTabFragment url::"+url);
        if (Utils.isInternetConnected(getActivity())){
            wvCategory.loadUrl(url);
        }else{
            Utils.showToast(getActivity(), getString(R.string.pf_no_connection));
        }



        wvCategory.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                LogUtils.e("", "shouldOverrideUrlLoading url::" + url);
                LogUtils.e("", "main_category_id::" + mMainCateID);
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

                } else if (url.contains("main_category=")) {

                    String mCategoryID = url.split("main_category=")[1];
                    Bundle bundle = new Bundle();
                    bundle.putString(MainCategoryForProductFragment.BK_PRODUCT_ID, mCategoryID);
                    bundle.putString(MainCategoryForProductFragment.BNDL_MAIN_CATE_ID, "" + mMainCateID);
                    bundle.putString(MainCategoryForProductFragment.BNDL_MAIN_CATE_NAME, "" + mMainCateName);

                    HomeActivity activity = (HomeActivity) getActivity();
                    if (activity != null) {
                        activity.loadMainCategory(bundle);
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

                } else if (url.contains("landing_screen")) {

//                    String mCategoryID = url.split("landing_screen=")[1];
//
//                    if (mChildScreens != null && mChildScreens.size() > 0) {
//                        for (int i = 0; i < mChildScreens.size(); i++) {
//                            if (mChildScreens.get(i).titleEn.equalsIgnoreCase(mCategoryID)) {
//                                Bundle bundle = new Bundle();
//                                bundle.putString(HomeLandingFragment.BNDL_BASE_SCREEN, mChildScreens.get(i).url);
//                                bundle.putString(HomeLandingFragment.BNDL_MAIN_CATE_ID, "" + mMainCateID);
//                                bundle.putString(HomeLandingFragment.BNDL_MAIN_CATE_NAME, "" + mMainCateName);
//
//                                HomeActivity activity = (HomeActivity) getActivity();
//                                if (activity != null) {
//                                    activity.loadHomeLanding(bundle);
//                                }
//                            }
//                        }
//                    }


                    Bundle bundle = new Bundle();
                    bundle.putString(HomeLandingFragment.BNDL_BASE_SCREEN, url);
                    bundle.putString(HomeLandingFragment.BNDL_MAIN_CATE_ID, "" + mMainCateID);
                    bundle.putString(HomeLandingFragment.BNDL_MAIN_CATE_NAME, "" + mMainCateName);

                    HomeActivity activity = (HomeActivity) getActivity();
                    if (activity != null) {
                        activity.loadHomeLanding(bundle);
                    }

                } else {

//                    String mSearchCriteria = url;
//                    Bundle bundle = new Bundle();
//                    bundle.putString(ProductsItemsFragment.BK_PRODUCT_ITEMS, mSearchCriteria);
//                    bundle.putString(ProductsItemsFragment.BNDL_CATEGORY, "" + mMainCateName);
//                    bundle.putString(ProductsItemsFragment.BNDL_MAIN_CATE_ID, "" + mMainCateID);
//                    bundle.putBoolean(ProductsItemsFragment.IS_FROM_BANNER, true);
//
//                    HomeActivity activity = (HomeActivity) getActivity();
//                    if (activity != null) {
//                        activity.loadBannerItems(bundle);
//                    }
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
                Utils.showProgressDialog(getMyActivity());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Utils.closeProgressDialog();
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && getView() != null) {

//            wvCategory.loadUrl(url);

            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.setFirebaseLog("Women_Banner");
            }
        }
    }

}
