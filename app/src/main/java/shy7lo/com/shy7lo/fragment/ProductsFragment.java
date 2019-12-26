package shy7lo.com.shy7lo.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.BuildConfig;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.adapter.HomeCategoryAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.AppInit;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;
import shy7lo.com.shy7lo.widget.HomeTabPagerAdapterWithTitle;

/**
 * Created by jiten on 2/9/16.
 */

//public class ProductsFragment extends Fragment implements View.OnClickListener {
//
//    public static String TAG_PRODUCT_FRAGMENT = "ProductsFragment";
//    public static String BNDL_PRODUCT_CATEGORY = "BNDL_PRODUCT_CATEGORY";
//
//    View mView;
//
//    @BindView(R.id.mLine)
//    View mLine;
//    @BindView(R.id.mTopLayout)
//    View mTopLayout;
//    @BindView(R.id.tvTitle)
//    TextView tvTitle;
//    @BindView(R.id.ibMore)
//    ImageButton ibMore;
//    @BindView(R.id.ibRightIcons)
//    ImageButton ibRightIcons;
//    @BindView(R.id.ibCloseIcons)
//    ImageButton ibCloseIcons;
//    @BindView(R.id.lnrSearchBar)
//    LinearLayout lnrSearchBar;
//    @BindView(R.id.etSearch)
//    EditText etSearch;
//
//    @BindView(R.id.mViewPager)
//    public ViewPager mViewPager;
//    @BindView(R.id.sliding_tabs)
//    TabLayout sliding_tabs;
//
//    private int mTabMinSize;
//
//    Runnable mRunnable;
//    Handler mHandler = new Handler();
//
////    Animation animClose, animOpen;
//
//    HomeTabPagerAdapterWithTitle mHomeTabPagerAdapterWithTitle;
//
//    private static ProductsFragment productsNewFragment;
//
//    List<AppInit.BaseScreen> mBaseScreenList;
//
//    public static ProductsFragment getInstance() {
////        if (productsNewFragment == null) {
//        productsNewFragment = new ProductsFragment();
////        }
//        return productsNewFragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setRetainInstance(true);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        mView = inflater.inflate(R.layout.fragment_products_new, container, false);
//        Utils.setLocale(getActivity());
//        ButterKnife.bind(this, mView);
////        setRetainInstance(true);
//        InitializeControls();
//        InitializeControlsAction();
//
//        return mView;
//    }
//
//
//    private void InitializeControls() {
//        LogUtils.e(TAG_PRODUCT_FRAGMENT, "InitializeControls");
//
////        animOpen = AnimationUtils.loadAnimation(getActivity(),
////                R.anim.slide_in_left);
////        animClose = AnimationUtils.loadAnimation(getActivity(),
////                R.anim.slide_in_right);
//
//        mTabMinSize = Utils.getScreenResolution(getActivity()) / 3;
//
//        if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//
////            tvTitle.setBackgroundResource(R.drawable.ic_top_logo_ar);
//
//            mTopLayout.setScaleX(-1f);
//            tvTitle.setScaleX(-1f);
//            etSearch.setScaleX(-1f);
//            etSearch.setGravity(Gravity.RIGHT);
////            itemBadgeView.setScaleX(-1f);
////            sliding_tabs.setScaleX(-1f);
////            mViewPager.setScaleX(-1f);
//
//
//        } else {
//
//
//            mTopLayout.setScaleX(1f);
//            tvTitle.setScaleX(1f);
//            etSearch.setScaleX(1f);
//            etSearch.setGravity(Gravity.LEFT);
////            itemBadgeView.setScaleX(1f);
////            sliding_tabs.setScaleX(1f);
////            mViewPager.setScaleX(1f);
//
//        }
//
//
//        getHomeTabList();
//
////        animClose.setAnimationListener(new Animation.AnimationListener() {
////            @Override
////            public void onAnimationStart(Animation animation) {
////                lnrSearchBar.setVisibility(View.VISIBLE);
////            }
////
////            @Override
////            public void onAnimationEnd(Animation animation) {
////
////            }
////
////            @Override
////            public void onAnimationRepeat(Animation animation) {
////
////            }
////        });
////
////        animClose.setAnimationListener(new Animation.AnimationListener() {
////            @Override
////            public void onAnimationStart(Animation animation) {
////
////            }
////
////            @Override
////            public void onAnimationEnd(Animation animation) {
////                lnrSearchBar.setVisibility(View.GONE);
////            }
////
////            @Override
////            public void onAnimationRepeat(Animation animation) {
////
////            }
////        });
//
//        mRunnable = new Runnable() {
//            @Override
//            public void run() {
//                String searchString = etSearch.getText().toString();
//                if (!TextUtils.isEmpty(searchString) && searchString.length() > 2) {
//                    if (getActivity() instanceof HomeActivity) {
//                        etSearch.setText("");
//                        Bundle bundle = new Bundle();
//                        bundle.putString(SearchAlgoliaFragment.BNDL_SEARCH_KEY, searchString);
//                        HomeActivity activity = (HomeActivity) getActivity();
//                        activity.loadSearchWithBundle(bundle);
//                    }
//                }
//            }
//        };
//
//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                mHandler.removeCallbacks(mRunnable);
//                if (lnrSearchBar.getVisibility() == View.VISIBLE) {
//                    mHandler.postDelayed(mRunnable, 1000);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//    }
//
////    public void getHomeTabList() {
////        LogUtils.e(TAG_PRODUCT_FRAGMENT, "getHomeTabList call");
////        Utils.showProgressDialog(getActivity());
////
////        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API_SHYLABS_V2_URL, true);
////        Call<HomeTabList> call = serviceAPI.getHomeTabList(Shy7lo.mLangCode);
////        call.enqueue(new Callback<HomeTabList>() {
////            @Override
////            public void onResponse(Call<HomeTabList> call, Response<HomeTabList> response) {
////
////                Utils.closeProgressDialog();
////
////                if (response.isSuccessful()) {
////                    try {
////                        HomeTabList mHomeTabList = response.body();
////
////                        if (mHomeTabList != null && mHomeTabList.success && mHomeTabList.data != null) {
////
////                            List<HomeTabList.BaseScreen> mBaseScreenList = mHomeTabList.data.baseScreens;
////                            if (mBaseScreenList != null && mBaseScreenList.size() > 0) {
////
////                                mLine.setVisibility(View.VISIBLE);
////
////                                mHomeTabPagerAdapterWithTitle = new HomeTabPagerAdapterWithTitle(getChildFragmentManager());
////                                LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreenList::" + mBaseScreenList.size());
////                                for (int i = 0; i < mBaseScreenList.size(); i++) {
////                                    HomeTabList.BaseScreen mBaseScreen = mBaseScreenList.get(i);
////                                    LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreen::" + mBaseScreen.titleEn);
////
////                                    Fragment mFragment = new HomeTabFragment();
////
////                                    Bundle bundle = new Bundle();
//////                                    bundle.putString(HomeTabFragment.BNDL_BASE_SCREEN, new Gson().toJson(mBaseScreen));
////                                    LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreen.url::" + mBaseScreen.url);
////                                    bundle.putString(HomeTabFragment.BNDL_BASE_SCREEN, mBaseScreen.url);
////                                    bundle.putString(HomeTabFragment.BNDL_CHILD_SCREEN, new Gson().toJson(mHomeTabList.data));
////                                    if (bundle != null) {
////                                        if (mFragment.getArguments() == null) {
////                                            mFragment.setArguments(bundle);
////                                        } else {
////                                            mFragment.getArguments().putAll(bundle);
////                                        }
////                                    }
////
////                                    if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
////                                        mHomeTabPagerAdapterWithTitle.addFragment(mFragment, mBaseScreen.titleAr);
////                                    } else {
////                                        mHomeTabPagerAdapterWithTitle.addFragment(mFragment, mBaseScreen.titleEn);
////                                    }
////                                }
////
////                                LogUtils.e(TAG_PRODUCT_FRAGMENT, "mHomeTabPagerAdapterWithTitle.getCount();:" + mHomeTabPagerAdapterWithTitle.getCount());
////                                mViewPager.setAdapter(mHomeTabPagerAdapterWithTitle);
////                                mViewPager.setCurrentItem(0);
////
////                                sliding_tabs.setupWithViewPager(mViewPager, true);
////
////                                if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
////
//////                                            mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//////                                    mViewPager.setScaleX(-1f);
////                                    Utils.setTabsFont(sliding_tabs, Shy7lo.DroidKufiRegular);
////
//////                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//////                                                    tlSlidingTabs.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//////                                                } else {
////                                    sliding_tabs.setScaleX(-1f);
////                                    LinearLayout tabStrip = (LinearLayout) sliding_tabs.getChildAt(0);
////                                    for (int i = 0; i < tabStrip.getChildCount(); i++) {
////                                        View tabView = tabStrip.getChildAt(i);
////                                        if (tabView != null) {
////                                            tabView.setScaleX(-1f);
////                                            tabView.setMinimumWidth(mTabMinSize);
////                                        }
////                                    }
////
//////
////                                } else {
////
//////                                            mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//////                                    mViewPager.setScaleX(1f);
////                                    Utils.setTabsFont(sliding_tabs, Shy7lo.RalewayRegular);
////
//////                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//////                                                    tlSlidingTabs.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//////                                                } else {
////                                    sliding_tabs.setScaleX(1f);
////
////                                    LinearLayout tabStrip = (LinearLayout) sliding_tabs.getChildAt(0);
////                                    for (int i = 0; i < tabStrip.getChildCount(); i++) {
////                                        View tabView = tabStrip.getChildAt(i);
////                                        if (tabView != null) {
////                                            tabView.setScaleX(1f);
////                                            tabView.setMinimumWidth(mTabMinSize);
////                                        }
////                                    }
////
////                                }
////
////                            } else {
////                                LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreenList is null");
////                            }
////                        } else {
////                            LogUtils.e("", "mHomeTabList is null");
////                        }
////
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
////                }
////
////            }
////
////            @Override
////            public void onFailure(Call<HomeTabList> call, Throwable t) {
////
////            }
////        });
////    }
//
//    public void getHomeTabList() {
//        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.landingScreens.baseScreens != null) {
//            mBaseScreenList = Shy7lo.mAppInit.landingScreens.baseScreens;
//            showHomeTab();
//        } else {
//            getInitApi();
//        }
//    }
//
//    private void getInitApi() {
//
//        if (!Utils.isInternetConnected(getActivity())) {
//            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
//                @Override
//                public void onRetryClicked(Dialog dialog) {
//                    if (Utils.isInternetConnected(getActivity())) {
//                        if (dialog != null && dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        getInitApi();
//                    }
//                }
//            });
//            return;
//        }
//
//        Utils.showProgressDialog(getActivity());
//
//        Map<String, Object> jsonParams = new ArrayMap<>();
//        jsonParams.put("device_type", "android");
//        jsonParams.put("app_version", "" + BuildConfig.VERSION_NAME);
//        jsonParams.put("device_token", "" + Utils.getDeviceToken(getActivity()));
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
//                LogUtils.e(TAG_PRODUCT_FRAGMENT, "response code:" + response.code());
//
//                Utils.closeProgressDialog();
//
//                if (response.isSuccessful()) {
//                    try {
//
//                        Shy7lo.mAppInit = response.body();
//                        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.success.equalsIgnoreCase("1")) {
//                            if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.landingScreens != null) {
//                                mBaseScreenList = Shy7lo.mAppInit.landingScreens.baseScreens;
//                                showHomeTab();
//                            }
//                        }
//
//                        AppInit.Integrations mIntegrations = Shy7lo.mAppInit.integrations;
//                        if (mIntegrations != null) {
//
//
//                            LogUtils.e("", "mIntegrations.tune::" + mIntegrations.tune);
//                            LogUtils.e("", "mIntegrations.fabric::" + mIntegrations.fabric);
//                            LogUtils.e("", "mIntegrations.oneSginal::" + mIntegrations.oneSginal);
//                            LogUtils.e("", "mIntegrations.criteo::" + mIntegrations.criteo);
//
//                            MyPref.setPref(getActivity(), MyPref.APP_TUNE, mIntegrations.tune);
//                            MyPref.setPref(getActivity(), MyPref.APP_FABRIC, mIntegrations.fabric);
//                            MyPref.setPref(getActivity(), MyPref.APP_ONE_SIGNAL, mIntegrations.oneSginal);
//                            MyPref.setPref(getActivity(), MyPref.APP_CRITEO, mIntegrations.criteo);
//
//                            Utils.setInitConfig(getActivity());
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
//
//    private void showHomeTab() {
//
//        if (mBaseScreenList != null && mBaseScreenList.size() > 0) {
//
//            mLine.setVisibility(View.VISIBLE);
//
//            mHomeTabPagerAdapterWithTitle = new HomeTabPagerAdapterWithTitle(getChildFragmentManager());
//            LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreenList::" + mBaseScreenList.size());
//            for (int i = 0; i < mBaseScreenList.size(); i++) {
//                AppInit.BaseScreen mBaseScreen = mBaseScreenList.get(i);
//                LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreen::" + mBaseScreen.titleEn);
//
//                String mMainCategoryName = "";
//                if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    mMainCategoryName = mBaseScreen.titleAr;
//                } else {
//                    mMainCategoryName = mBaseScreen.titleEn;
//                }
//
//                Fragment mFragment = new HomeTabFragment();
//
//                Bundle bundle = new Bundle();
////                                    bundle.putString(HomeTabFragment.BNDL_BASE_SCREEN, new Gson().toJson(mBaseScreen));
//                LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreen.url::" + mBaseScreen.url);
//                bundle.putString(HomeTabFragment.BNDL_BASE_SCREEN, mBaseScreen.url);
//                bundle.putString(HomeTabFragment.BNDL_MAIN_CATE_ID, mBaseScreen.cat_id);
//                bundle.putString(HomeTabFragment.BNDL_MAIN_CATE_NAME, mMainCategoryName);
//                bundle.putString(HomeTabFragment.BNDL_CHILD_SCREEN, new Gson().toJson(Shy7lo.mAppInit.landingScreens.childScreens));
//                if (bundle != null) {
//                    if (mFragment.getArguments() == null) {
//                        mFragment.setArguments(bundle);
//                    } else {
//                        mFragment.getArguments().putAll(bundle);
//                    }
//                }
//
//                if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    mHomeTabPagerAdapterWithTitle.addFragment(mFragment, mBaseScreen.titleAr);
//                } else {
//                    mHomeTabPagerAdapterWithTitle.addFragment(mFragment, mBaseScreen.titleEn);
//                }
//            }
//
//            LogUtils.e(TAG_PRODUCT_FRAGMENT, "mHomeTabPagerAdapterWithTitle.getCount();:" + mHomeTabPagerAdapterWithTitle.getCount());
//            mViewPager.setAdapter(mHomeTabPagerAdapterWithTitle);
//            int position = MyPref.getPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0);
//            mViewPager.setCurrentItem(position);
//
//            sliding_tabs.setupWithViewPager(mViewPager, true);
//
//            if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//
////                                            mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
////                                    mViewPager.setScaleX(-1f);
//                Utils.setTabsFont(sliding_tabs, Shy7lo.DroidKufiRegular);
//
//                sliding_tabs.setScaleX(-1f);
//                LinearLayout tabStrip = (LinearLayout) sliding_tabs.getChildAt(0);
//                for (int i = 0; i < tabStrip.getChildCount(); i++) {
//                    View tabView = tabStrip.getChildAt(i);
//                    if (tabView != null) {
//                        tabView.setScaleX(-1f);
//                        tabView.setMinimumWidth(mTabMinSize);
//                    }
//                }
//
////
//            } else {
//
////                                            mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
////                                    mViewPager.setScaleX(1f);
//                Utils.setTabsFont(sliding_tabs, Shy7lo.RalewayRegular);
//
//                sliding_tabs.setScaleX(1f);
//
//                LinearLayout tabStrip = (LinearLayout) sliding_tabs.getChildAt(0);
//                for (int i = 0; i < tabStrip.getChildCount(); i++) {
//                    View tabView = tabStrip.getChildAt(i);
//                    if (tabView != null) {
//                        tabView.setScaleX(1f);
//                        tabView.setMinimumWidth(mTabMinSize);
//                    }
//                }
//
//            }
//
//        } else {
//            LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreenList is null");
//        }
//
//    }
//
//    public String getMainCategoryId() {
//        String mCategoryID = "143";
//        if (mBaseScreenList != null && mBaseScreenList.size() > 0 && sliding_tabs != null) {
//            AppInit.BaseScreen mBaseScreen = mBaseScreenList.get(sliding_tabs.getSelectedTabPosition());
//            mCategoryID = mBaseScreen.cat_id;
//        }
//        LogUtils.e("", "mViewPager.getCurrentItem()::" + sliding_tabs.getSelectedTabPosition());
//        return mCategoryID;
//    }
//
//    public String getMainCategoryName() {
//        String mCategoryName = "" + getString(R.string.women);
//        if (mBaseScreenList != null && mBaseScreenList.size() > 0 && sliding_tabs != null) {
//            AppInit.BaseScreen mBaseScreen = mBaseScreenList.get(sliding_tabs.getSelectedTabPosition());
//            if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                mCategoryName = mBaseScreen.titleAr;
//            } else {
//                mCategoryName = mBaseScreen.titleEn;
//            }
//        }
//        return mCategoryName;
//    }
//
//    private void InitializeControlsAction() {
//        ibMore.setOnClickListener(this);
//        ibRightIcons.setOnClickListener(this);
//        ibCloseIcons.setOnClickListener(this);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (TextUtils.isEmpty(getPref(getActivity(), MyPref.GUEST_CART_ID, ""))) {
//            Utils.getGuestCartToken(getActivity());
//        }
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (view == ibMore) {
//            if (getActivity() instanceof HomeActivity) {
//                HomeActivity activity = (HomeActivity) getActivity();
//                activity.openDrawer();
//            }
//        } else if (view == ibRightIcons) {
//
//            showSearchBar();
////            if (getActivity() instanceof HomeActivity) {
////                HomeActivity activity = (HomeActivity) getActivity();
////                activity.loadSeach();
////            }
//        } else if (view == ibCloseIcons) {
//
//            hideSearchBar();
//        }
//    }
//
//    private void showSearchBar() {
////        lnrSearchBar.startAnimation(animOpen);
//        lnrSearchBar.setVisibility(View.VISIBLE);
//    }
//
//    private void hideSearchBar() {
//        etSearch.setText("");
////        lnrSearchBar.startAnimation(animClose);
//        lnrSearchBar.setVisibility(View.GONE);
//    }
//
//
//    public void updateBundles(final Bundle bundle) {
////        getActivity().runOnUiThread(new Runnable() {
////            public void run() {
////                String category = bundle.getString(BNDL_PRODUCT_CATEGORY);
////                LogUtils.e("", "push category::" + category);
////                if (!TextUtils.isEmpty(category)) {
////                    if (mViewPager != null && category.equalsIgnoreCase("1")) {
////                        mViewPager.setCurrentItem(0);
////                    } else if (mViewPager != null && category.equalsIgnoreCase("2")) {
////                        mViewPager.setCurrentItem(1);
////                    } else if (mViewPager != null && category.equalsIgnoreCase("3")) {
////                        mViewPager.setCurrentItem(2);
////                    }
////                }
////            }
////        });
//    }
//
//
//}

public class ProductsFragment extends Fragment implements View.OnClickListener {

    public static String TAG_PRODUCT_FRAGMENT = "ProductsFragment";
    public static String BNDL_PRODUCT_CATEGORY = "BNDL_PRODUCT_CATEGORY";

    View mView;

    @BindView(R.id.mLine)
    View mLine;
    @BindView(R.id.mTopLayout)
    View mTopLayout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ibMore)
    ImageButton ibMore;
    @BindView(R.id.ibRightIcons)
    ImageButton ibRightIcons;
    @BindView(R.id.ibCloseIcons)
    ImageButton ibCloseIcons;
    @BindView(R.id.lnrSearchBar)
    LinearLayout lnrSearchBar;
    @BindView(R.id.etSearch)
    EditText etSearch;

    @BindView(R.id.mViewPager)
    public ViewPager mViewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout sliding_tabs;

    private int mTabMinSize;

    Runnable mRunnable;
    Handler mHandler = new Handler();

//    Animation animClose, animOpen;

    HomeTabPagerAdapterWithTitle mHomeTabPagerAdapterWithTitle;

    private static ProductsFragment productsNewFragment;

    List<AppInit.BaseScreen> mBaseScreenList;

    PopupWindow popupWindowCategory;
    ListView lvCategory;
    HomeCategoryAdapter mCategoryAdapter;

    public static ProductsFragment getInstance() {
//        if (productsNewFragment == null) {
        productsNewFragment = new ProductsFragment();
//        }
        return productsNewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_products_new, container, false);
        Utils.setLocale(getActivity());
        ButterKnife.bind(this, mView);
//        setRetainInstance(true);
        InitializeControls();
        InitializeControlsAction();

        return mView;
    }


    private void InitializeControls() {
        LogUtils.e(TAG_PRODUCT_FRAGMENT, "InitializeControls");

//        animOpen = AnimationUtils.loadAnimation(getActivity(),
//                R.anim.slide_in_left);
//        animClose = AnimationUtils.loadAnimation(getActivity(),
//                R.anim.slide_in_right);

        popupWindowCategory = new PopupWindow(getActivity());
        lvCategory = new ListView(getActivity());
        mCategoryAdapter = new HomeCategoryAdapter(getActivity());

        mTabMinSize = Utils.getScreenResolution(getActivity()) / 5;


        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

            tvTitle.setBackgroundResource(R.drawable.ic_top_logo_ar);

            mTopLayout.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            etSearch.setScaleX(-1f);
            etSearch.setGravity(Gravity.RIGHT);
//            itemBadgeView.setScaleX(-1f);
//            sliding_tabs.setScaleX(-1f);
//            mViewPager.setScaleX(-1f);


        } else {

            tvTitle.setBackgroundResource(R.drawable.ic_top_logo);

            mTopLayout.setScaleX(1f);
            tvTitle.setScaleX(1f);
            etSearch.setScaleX(1f);
            etSearch.setGravity(Gravity.LEFT);
//            itemBadgeView.setScaleX(1f);
//            sliding_tabs.setScaleX(1f);
//            mViewPager.setScaleX(1f);

        }


        getHomeTabList();

//        animClose.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                lnrSearchBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        animClose.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                lnrSearchBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });

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

//        tvTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mBaseScreenList != null && mBaseScreenList.size() > 0) {
//                    showCategoryWindow();
//                }
//            }
//        });

    }

    private void showCategoryWindow() {

        popupWindowCategory.setFocusable(true);
        popupWindowCategory.setWidth(getResources().getDimensionPixelSize(R.dimen._90sdp));
        popupWindowCategory.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindowCategory.setContentView(lvCategory);
//        popupWindowCategory.setBackgroundDrawable(null);
        popupWindowCategory.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindowCategory.setElevation(20);
        }
        popupWindowCategory.showAsDropDown(tvTitle, 0, -(getResources().getDimensionPixelSize(R.dimen._2sdp)));
//        popupWindowCategory.showAtLocation(tvTitle, Gravity.TOP, 0, 0);
    }


//    public void getHomeTabList() {
//        LogUtils.e(TAG_PRODUCT_FRAGMENT, "getHomeTabList call");
//        Utils.showProgressDialog(getActivity());
//
//        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API_SHYLABS_V2_URL, true);
//        Call<HomeTabList> call = serviceAPI.getHomeTabList(Shy7lo.mLangCode);
//        call.enqueue(new Callback<HomeTabList>() {
//            @Override
//            public void onResponse(Call<HomeTabList> call, Response<HomeTabList> response) {
//
//                Utils.closeProgressDialog();
//
//                if (response.isSuccessful()) {
//                    try {
//                        HomeTabList mHomeTabList = response.body();
//
//                        if (mHomeTabList != null && mHomeTabList.success && mHomeTabList.data != null) {
//
//                            List<HomeTabList.BaseScreen> mBaseScreenList = mHomeTabList.data.baseScreens;
//                            if (mBaseScreenList != null && mBaseScreenList.size() > 0) {
//
//                                mLine.setVisibility(View.VISIBLE);
//
//                                mHomeTabPagerAdapterWithTitle = new HomeTabPagerAdapterWithTitle(getChildFragmentManager());
//                                LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreenList::" + mBaseScreenList.size());
//                                for (int i = 0; i < mBaseScreenList.size(); i++) {
//                                    HomeTabList.BaseScreen mBaseScreen = mBaseScreenList.get(i);
//                                    LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreen::" + mBaseScreen.titleEn);
//
//                                    Fragment mFragment = new HomeTabFragment();
//
//                                    Bundle bundle = new Bundle();
////                                    bundle.putString(HomeTabFragment.BNDL_BASE_SCREEN, new Gson().toJson(mBaseScreen));
//                                    LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreen.url::" + mBaseScreen.url);
//                                    bundle.putString(HomeTabFragment.BNDL_BASE_SCREEN, mBaseScreen.url);
//                                    bundle.putString(HomeTabFragment.BNDL_CHILD_SCREEN, new Gson().toJson(mHomeTabList.data));
//                                    if (bundle != null) {
//                                        if (mFragment.getArguments() == null) {
//                                            mFragment.setArguments(bundle);
//                                        } else {
//                                            mFragment.getArguments().putAll(bundle);
//                                        }
//                                    }
//
//                                    if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                                        mHomeTabPagerAdapterWithTitle.addFragment(mFragment, mBaseScreen.titleAr);
//                                    } else {
//                                        mHomeTabPagerAdapterWithTitle.addFragment(mFragment, mBaseScreen.titleEn);
//                                    }
//                                }
//
//                                LogUtils.e(TAG_PRODUCT_FRAGMENT, "mHomeTabPagerAdapterWithTitle.getCount();:" + mHomeTabPagerAdapterWithTitle.getCount());
//                                mViewPager.setAdapter(mHomeTabPagerAdapterWithTitle);
//                                mViewPager.setCurrentItem(0);
//
//                                sliding_tabs.setupWithViewPager(mViewPager, true);
//
//                                if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//
////                                            mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
////                                    mViewPager.setScaleX(-1f);
//                                    Utils.setTabsFont(sliding_tabs, Shy7lo.DroidKufiRegular);
//
////                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
////                                                    tlSlidingTabs.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
////                                                } else {
//                                    sliding_tabs.setScaleX(-1f);
//                                    LinearLayout tabStrip = (LinearLayout) sliding_tabs.getChildAt(0);
//                                    for (int i = 0; i < tabStrip.getChildCount(); i++) {
//                                        View tabView = tabStrip.getChildAt(i);
//                                        if (tabView != null) {
//                                            tabView.setScaleX(-1f);
//                                            tabView.setMinimumWidth(mTabMinSize);
//                                        }
//                                    }
//
////
//                                } else {
//
////                                            mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
////                                    mViewPager.setScaleX(1f);
//                                    Utils.setTabsFont(sliding_tabs, Shy7lo.RalewayRegular);
//
////                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
////                                                    tlSlidingTabs.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
////                                                } else {
//                                    sliding_tabs.setScaleX(1f);
//
//                                    LinearLayout tabStrip = (LinearLayout) sliding_tabs.getChildAt(0);
//                                    for (int i = 0; i < tabStrip.getChildCount(); i++) {
//                                        View tabView = tabStrip.getChildAt(i);
//                                        if (tabView != null) {
//                                            tabView.setScaleX(1f);
//                                            tabView.setMinimumWidth(mTabMinSize);
//                                        }
//                                    }
//
//                                }
//
//                            } else {
//                                LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreenList is null");
//                            }
//                        } else {
//                            LogUtils.e("", "mHomeTabList is null");
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
//            public void onFailure(Call<HomeTabList> call, Throwable t) {
//
//            }
//        });
//    }

    public void getHomeTabList() {
        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.landingScreens.baseScreens != null) {
            mBaseScreenList = Shy7lo.mAppInit.landingScreens.baseScreens;
            showHomeTab();
        } else {
            getInitApi();
        }
    }

    private void getInitApi() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getInitApi();
                    }
                }
            });
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

                LogUtils.e(TAG_PRODUCT_FRAGMENT, "response code:" + response.code());

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    try {

                        Shy7lo.mAppInit = response.body();
                        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.success.equalsIgnoreCase("1")) {
                            if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.landingScreens != null) {
                                mBaseScreenList = Shy7lo.mAppInit.landingScreens.baseScreens;
                                showHomeTab();
                            }
                        }

                        AppInit.Integrations mIntegrations = Shy7lo.mAppInit.integrations;
                        if (mIntegrations != null) {


                            LogUtils.e("", "mIntegrations.tune::" + mIntegrations.tune);
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

    }

    private void showHomeTab() {

        if (mBaseScreenList != null && mBaseScreenList.size() > 0) {

            mLine.setVisibility(View.VISIBLE);

            mHomeTabPagerAdapterWithTitle = new HomeTabPagerAdapterWithTitle(getChildFragmentManager());
            LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreenList::" + mBaseScreenList.size());
            for (int i = 0; i < mBaseScreenList.size(); i++) {
                AppInit.BaseScreen mBaseScreen = mBaseScreenList.get(i);
                LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreen::" + mBaseScreen.titleEn);

//                String mMainCategoryName = "";
//                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    mMainCategoryName = mBaseScreen.titleAr;
//                } else {
//                    mMainCategoryName = mBaseScreen.titleEn;
//                }

                Fragment mFragment = new HomeTabFragment();

                Bundle bundle = new Bundle();
//                                    bundle.putString(HomeTabFragment.BNDL_BASE_SCREEN, new Gson().toJson(mBaseScreen));
//                LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreen.url::" + mBaseScreen.url);
                bundle.putString(HomeTabFragment.BNDL_BASE_ATTRIBUTE, new Gson().toJson(mBaseScreen));
//                bundle.putString(HomeTabFragment.BNDL_BASE_SCREEN, mBaseScreen.url);
//                bundle.putString(HomeTabFragment.BNDL_MAIN_CATE_ID, mBaseScreen.cat_id);
//                bundle.putString(HomeTabFragment.BNDL_MAIN_CATE_NAME, mMainCategoryName);
//                bundle.putString(HomeTabFragment.BNDL_CHILD_SCREEN, new Gson().toJson(Shy7lo.mAppInit.landingScreens.childScreens));
                if (bundle != null) {
                    if (mFragment.getArguments() == null) {
                        mFragment.setArguments(bundle);
                    } else {
                        mFragment.getArguments().putAll(bundle);
                    }
                }

                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    mHomeTabPagerAdapterWithTitle.addFragment(mFragment, mBaseScreen.titleAr);
                } else {
                    mHomeTabPagerAdapterWithTitle.addFragment(mFragment, mBaseScreen.titleEn);
                }
            }

            LogUtils.e(TAG_PRODUCT_FRAGMENT, "mHomeTabPagerAdapterWithTitle.getCount();:" + mHomeTabPagerAdapterWithTitle.getCount());
            mViewPager.setAdapter(mHomeTabPagerAdapterWithTitle);
//            int position = MyPref.getPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0);
            int position = 0;
            mViewPager.setCurrentItem(position);

            sliding_tabs.setupWithViewPager(mViewPager, true);

            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

//                                            mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//                                    mViewPager.setScaleX(-1f);
                Utils.setTabsFont(sliding_tabs, Shy7lo.DroidKufiRegular);

                sliding_tabs.setScaleX(-1f);
                LinearLayout tabStrip = (LinearLayout) sliding_tabs.getChildAt(0);
                for (int i = 0; i < tabStrip.getChildCount(); i++) {
                    View tabView = tabStrip.getChildAt(i);
                    if (tabView != null) {
                        tabView.setScaleX(-1f);
                        tabView.setMinimumWidth(mTabMinSize);
                    }
                }

//
            } else {

//                                            mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//                                    mViewPager.setScaleX(1f);
                Utils.setTabsFont(sliding_tabs, Shy7lo.RalewayRegular);

                sliding_tabs.setScaleX(1f);

                LinearLayout tabStrip = (LinearLayout) sliding_tabs.getChildAt(0);
                for (int i = 0; i < tabStrip.getChildCount(); i++) {
                    View tabView = tabStrip.getChildAt(i);
                    if (tabView != null) {
                        tabView.setScaleX(1f);
                        tabView.setMinimumWidth(mTabMinSize);
                    }
                }

            }

            if (mBaseScreenList.size() < 2) {
                sliding_tabs.setVisibility(View.GONE);
                mLine.setVisibility(View.GONE);
            } else {
                sliding_tabs.setVisibility(View.VISIBLE);
                mLine.setVisibility(View.VISIBLE);
            }

            sliding_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    AppInit.BaseScreen mBaseScreen = mBaseScreenList.get(sliding_tabs.getSelectedTabPosition());
                    if (mBaseScreen != null) {

                        if (mBaseScreen.mode.equalsIgnoreCase("query")) {

                            String mMainCategoryName = "";
                            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                                mMainCategoryName = mBaseScreen.titleAr;
                            } else {
                                mMainCategoryName = mBaseScreen.titleEn;
                            }

                            String mSearchCriteria = mBaseScreen.attribute.query;
                            Bundle bundle = new Bundle();
                            bundle.putString(ProductsItemsFragment.BK_PRODUCT_ITEMS, mSearchCriteria);
                            bundle.putString(ProductsItemsFragment.BNDL_CATEGORY, "" + mMainCategoryName);
                            bundle.putString(ProductsItemsFragment.BNDL_MAIN_CATE_ID, "");
                            bundle.putBoolean(ProductsItemsFragment.IS_FROM_BANNER, true);

                            HomeActivity activity = (HomeActivity) getActivity();
                            if (activity != null) {
                                activity.loadBannerItems(bundle);
                            }
                        }
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {

                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });

        } else {
            LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreenList is null");
        }

    }

    private void setSelectedCategory(int position) {

        mHomeTabPagerAdapterWithTitle = new HomeTabPagerAdapterWithTitle(getChildFragmentManager());
        AppInit.BaseScreen mBaseScreen = mBaseScreenList.get(position);
        String mMainCategoryName = "", mUrl = "";
        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            mMainCategoryName = mBaseScreen.titleAr;
            mUrl = mBaseScreen.attribute.urlAr;
        } else {
            mMainCategoryName = mBaseScreen.titleEn;
            mUrl = mBaseScreen.attribute.url;
        }

        Fragment mFragment = new HomeTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HomeTabFragment.BNDL_BASE_SCREEN, new Gson().toJson(mBaseScreen));
        bundle.putString(HomeTabFragment.BNDL_BASE_SCREEN, mUrl);
        bundle.putString(HomeTabFragment.BNDL_MAIN_CATE_ID, mBaseScreen.attribute.cat_id);
        bundle.putString(HomeTabFragment.BNDL_MAIN_CATE_NAME, mMainCategoryName);
//        bundle.putString(HomeTabFragment.BNDL_CHILD_SCREEN, new Gson().toJson(Shy7lo.mAppInit.landingScreens.childScreens));
        if (bundle != null) {
            if (mFragment.getArguments() == null) {
                mFragment.setArguments(bundle);
            } else {
                mFragment.getArguments().putAll(bundle);
            }
        }

        mHomeTabPagerAdapterWithTitle.addFragment(mFragment, getString(R.string.home));

        String mCategoryName = "";
        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            mCategoryName = mBaseScreen.titleAr;
        } else {
            mCategoryName = mBaseScreen.titleEn;
        }
        Fragment mCategoryFragment = MainCategoryForProductFragment.getInstance();
        Bundle bundleCategory = new Bundle();
        bundleCategory.putString(MainCategoryForProductFragment.BK_PRODUCT_ID, mBaseScreen.attribute.cat_id);
        bundleCategory.putString(MainCategoryForProductFragment.BNDL_MAIN_CATE_ID, "" + mBaseScreen.attribute.cat_id);
        bundleCategory.putString(MainCategoryForProductFragment.BNDL_MAIN_CATE_NAME, "" + mCategoryName);
        bundleCategory.putBoolean(MainCategoryForProductFragment.BNDL_IS_TITLE_HIDE, true);
        if (bundleCategory != null) {
            if (mCategoryFragment.getArguments() == null) {
                mCategoryFragment.setArguments(bundleCategory);
            } else {
                mCategoryFragment.getArguments().putAll(bundleCategory);
            }
        }
        mHomeTabPagerAdapterWithTitle.addFragment(mCategoryFragment, getString(R.string.categories));

        mViewPager.setAdapter(mHomeTabPagerAdapterWithTitle);
        mViewPager.setCurrentItem(0);

        sliding_tabs.setupWithViewPager(mViewPager, true);
        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

//                                            mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//                                    mViewPager.setScaleX(-1f);
            Utils.setTabsFont(sliding_tabs, Shy7lo.DroidKufiBold);

            sliding_tabs.setScaleX(-1f);
            LinearLayout tabStrip = (LinearLayout) sliding_tabs.getChildAt(0);
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                View tabView = tabStrip.getChildAt(i);
                if (tabView != null) {
                    tabView.setScaleX(-1f);
                    tabView.setMinimumWidth(mTabMinSize);
                }
            }

//
        } else {

//                                            mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//                                    mViewPager.setScaleX(1f);
            Utils.setTabsFont(sliding_tabs, Shy7lo.RalewayBold);

            sliding_tabs.setScaleX(1f);

            LinearLayout tabStrip = (LinearLayout) sliding_tabs.getChildAt(0);
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                View tabView = tabStrip.getChildAt(i);
                if (tabView != null) {
                    tabView.setScaleX(1f);
                    tabView.setMinimumWidth(mTabMinSize);
                }
            }

        }
    }

//    private void showHomeTab() {
//
//        if (mBaseScreenList != null && mBaseScreenList.size() > 0) {
//
//            mLine.setVisibility(View.VISIBLE);
//
//            int position = MyPref.getPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0);
////            position = 0;
//            LogUtils.e("DEFAULT_CATEGORY_POSITION", "DEFAULT_CATEGORY_POSITION position::"+position);
////            AppInit.BaseScreen mCurrentBaseScreen = mBaseScreenList.get(position);
////            String mTitle = "";
////            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
////                mTitle = mCurrentBaseScreen.titleAr;
////            } else {
////                mTitle = mCurrentBaseScreen.titleEn;
////            }
////            tvTitle.setText("" + mTitle);
//
//            mCategoryAdapter.addAll(mBaseScreenList);
//            lvCategory.setAdapter(mCategoryAdapter);
//            lvCategory.setDividerHeight(0);
//            lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
////                    setCategoryByPosition(i);
//                    if (mBaseScreenList != null && mBaseScreenList.size() > 0) {
//                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            tvTitle.setText("" + mBaseScreenList.get(position).titleAr);
//                        } else {
//                            tvTitle.setText("" + mBaseScreenList.get(position).titleEn);
//                        }
//                        setSelectedCategory(position);
//                    }
//                    popupWindowCategory.dismiss();
//                    MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, position);
//                }
//            });
//
//            setSelectedCategory(position);
//
////            for (int i = 0; i < mBaseScreenList.size(); i++) {
////                AppInit.BaseScreen mBaseScreen = mBaseScreenList.get(i);
////                LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreen::" + mBaseScreen.titleEn);
////
////                String mMainCategoryName = "";
////                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
////                    mMainCategoryName = mBaseScreen.titleAr;
////                } else {
////                    mMainCategoryName = mBaseScreen.titleEn;
////                }
////
////                Fragment mFragment = new HomeTabFragment();
////
////                Bundle bundle = new Bundle();
//////                                    bundle.putString(HomeTabFragment.BNDL_BASE_SCREEN, new Gson().toJson(mBaseScreen));
////                LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreen.url::" + mBaseScreen.url);
////                bundle.putString(HomeTabFragment.BNDL_BASE_SCREEN, mBaseScreen.url);
////                bundle.putString(HomeTabFragment.BNDL_MAIN_CATE_ID, mBaseScreen.cat_id);
////                bundle.putString(HomeTabFragment.BNDL_MAIN_CATE_NAME, mMainCategoryName);
////                bundle.putString(HomeTabFragment.BNDL_CHILD_SCREEN, new Gson().toJson(Shy7lo.mAppInit.landingScreens.childScreens));
////                if (bundle != null) {
////                    if (mFragment.getArguments() == null) {
////                        mFragment.setArguments(bundle);
////                    } else {
////                        mFragment.getArguments().putAll(bundle);
////                    }
////                }
////
////                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
////                    mHomeTabPagerAdapterWithTitle.addFragment(mFragment, mBaseScreen.titleAr);
////                } else {
////                    mHomeTabPagerAdapterWithTitle.addFragment(mFragment, mBaseScreen.titleEn);
////                }
////            }
////
////            LogUtils.e(TAG_PRODUCT_FRAGMENT, "mHomeTabPagerAdapterWithTitle.getCount();:" + mHomeTabPagerAdapterWithTitle.getCount());
////            mViewPager.setAdapter(mHomeTabPagerAdapterWithTitle);
////            int position = MyPref.getPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0);
////            mViewPager.setCurrentItem(position);
////
////            sliding_tabs.setupWithViewPager(mViewPager, true);
////
////            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
////
//////                                            mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//////                                    mViewPager.setScaleX(-1f);
////                Utils.setTabsFont(sliding_tabs, Shy7lo.DroidKufiRegular);
////
////                sliding_tabs.setScaleX(-1f);
////                LinearLayout tabStrip = (LinearLayout) sliding_tabs.getChildAt(0);
////                for (int i = 0; i < tabStrip.getChildCount(); i++) {
////                    View tabView = tabStrip.getChildAt(i);
////                    if (tabView != null) {
////                        tabView.setScaleX(-1f);
////                        tabView.setMinimumWidth(mTabMinSize);
////                    }
////                }
////
//////
////            } else {
////
//////                                            mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//////                                    mViewPager.setScaleX(1f);
////                Utils.setTabsFont(sliding_tabs, Shy7lo.RalewayRegular);
////
////                sliding_tabs.setScaleX(1f);
////
////                LinearLayout tabStrip = (LinearLayout) sliding_tabs.getChildAt(0);
////                for (int i = 0; i < tabStrip.getChildCount(); i++) {
////                    View tabView = tabStrip.getChildAt(i);
////                    if (tabView != null) {
////                        tabView.setScaleX(1f);
////                        tabView.setMinimumWidth(mTabMinSize);
////                    }
////                }
////
////            }
//
//        } else {
//            LogUtils.e(TAG_PRODUCT_FRAGMENT, "mBaseScreenList is null");
//        }
//
//    }

    public String getMainCategoryId() {
        String mCategoryID = "143";
        try {
            if (mBaseScreenList != null && mBaseScreenList.size() > 0 && sliding_tabs != null) {
//            AppInit.BaseScreen mBaseScreen = mBaseScreenList.get(sliding_tabs.getSelectedTabPosition());
                AppInit.BaseScreen mBaseScreen = mBaseScreenList.get(0);
                mCategoryID = mBaseScreen.attribute.cat_id;
            }
            LogUtils.e("", "mViewPager.getCurrentItem()::" + sliding_tabs.getSelectedTabPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mCategoryID;
    }

    public String getMainCategoryName() {
        String mCategoryName = "" + getString(R.string.women);
        if (mBaseScreenList != null && mBaseScreenList.size() > 0 && sliding_tabs != null) {
//            AppInit.BaseScreen mBaseScreen = mBaseScreenList.get(sliding_tabs.getSelectedTabPosition());
            AppInit.BaseScreen mBaseScreen = mBaseScreenList.get(0);
            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                mCategoryName = mBaseScreen.titleAr;
            } else {
                mCategoryName = mBaseScreen.titleEn;
            }
        }
        return mCategoryName;
    }

    private void InitializeControlsAction() {
        ibMore.setOnClickListener(this);
        ibRightIcons.setOnClickListener(this);
        ibCloseIcons.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""))) {
//            Utils.getGuestCartToken(getActivity());
//        }
    }

    @Override
    public void onClick(View view) {
        if (view == ibMore) {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.openDrawer();
            }
        } else if (view == ibRightIcons) {

//            showSearchBar()
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


    public void updateBundles(final Bundle bundle) {
//        getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                String category = bundle.getString(BNDL_PRODUCT_CATEGORY);
//                LogUtils.e("", "push category::" + category);
//                if (!TextUtils.isEmpty(category)) {
//                    if (mViewPager != null && category.equalsIgnoreCase("1")) {
//                        mViewPager.setCurrentItem(0);
//                    } else if (mViewPager != null && category.equalsIgnoreCase("2")) {
//                        mViewPager.setCurrentItem(1);
//                    } else if (mViewPager != null && category.equalsIgnoreCase("3")) {
//                        mViewPager.setCurrentItem(2);
//                    }
//                }
//            }
//        });
    }
}