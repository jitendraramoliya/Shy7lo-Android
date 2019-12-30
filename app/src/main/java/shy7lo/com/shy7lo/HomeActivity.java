package shy7lo.com.shy7lo;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ShareEvent;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.freshchat.consumer.sdk.exception.MethodNotAllowedException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.adapter.HelpAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.fragment.AccountFragment;
import shy7lo.com.shy7lo.fragment.HomeLandingFragment;
import shy7lo.com.shy7lo.fragment.MainCategoryForProductFragment;
import shy7lo.com.shy7lo.fragment.ProductDetailsFragment;
import shy7lo.com.shy7lo.fragment.ProductsFragment;
import shy7lo.com.shy7lo.fragment.ProductsItemsFragment;
import shy7lo.com.shy7lo.fragment.SearchAlgoliaFragment;
import shy7lo.com.shy7lo.fragment.ShipmentPayFragment;
import shy7lo.com.shy7lo.fragment.ShoppingBagsFragment;
import shy7lo.com.shy7lo.fragment.SignInFragment;
import shy7lo.com.shy7lo.fragment.SignUpFragment;
import shy7lo.com.shy7lo.fragment.SubCategoryForProductFragment;
import shy7lo.com.shy7lo.fragment.WishlistFragment;
import shy7lo.com.shy7lo.model.AppInit;
import shy7lo.com.shy7lo.model.CMSPage;
import shy7lo.com.shy7lo.model.CategoryList;
import shy7lo.com.shy7lo.model.Maintenance;
import shy7lo.com.shy7lo.model.SortingPojo;
import shy7lo.com.shy7lo.notification.MyFirebaseMessagingService;
import shy7lo.com.shy7lo.payfort.PayFortPayment;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.Constant;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

import static shy7lo.com.shy7lo.fragment.ProductsItemsFragment.RC_FILTER_CODE;
import static shy7lo.com.shy7lo.pref.MyPref.CART_ITEM_COUNT;
import static shy7lo.com.shy7lo.pref.MyPref.getPref;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener
//        , IPaymentRequestCallBack
{

    private DrawerLayout mDrawerLayout;
    private Fragment fragment;

    private Fragment mContent;
    FragmentTransaction mFragmentTransaction;
    FragmentManager mFragmentManager;
    String lastFragTag = "";

    @BindView(R.id.nvSidebar)
    NavigationView nvSidebar;

    @BindView(R.id.tvSidebarProducts)
    TextView tvSidebarProducts;
    @BindView(R.id.tvSidebarSearch)
    TextView tvSidebarSearch;
    @BindView(R.id.tvSidebarShoppingBag)
    TextView tvSidebarShoppingBag;
    @BindView(R.id.tvShoppingBagCounter)
    TextView tvShoppingBagCounter;
    @BindView(R.id.ivShoppingBag)
    ImageView ivShoppingBag;
    @BindView(R.id.lnrSidebarShoppingBag)
    LinearLayout lnrSidebarShoppingBag;
    @BindView(R.id.tvSidebarWishlist)
    TextView tvSidebarWishlist;
    @BindView(R.id.tvWishlistCounter)
    TextView tvWishlistCounter;
    @BindView(R.id.ivWishlist)
    ImageView ivWishlist;
    @BindView(R.id.lnrSidebarWishlist)
    LinearLayout lnrSidebarWishlist;
    @BindView(R.id.tvSidebarMyShy7lo)
    TextView tvSidebarMyShy7lo;
    //    @BindView(R.id.tvSidebarHelpFAQ)
//    TextView tvSidebarHelpFAQ;
//    @BindView(R.id.tvSidebarCustomers)
//    TextView tvSidebarCustomers;
    @BindView(R.id.lvSideHelp)
    ListView lvSideHelp;
    @BindView(R.id.tvSidebarLiveChat)
    TextView tvSidebarLiveChat;
    @BindView(R.id.tvSidebarAppInfo)
    TextView tvSidebarAppInfo;
    @BindView(R.id.tvSidebarSignOut)
    TextView tvSidebarSignOut;

    @BindView(R.id.tvWishlistText)
    TextView tvWishlistText;

    HelpAdapter mHelpAdapter;

    public boolean isShoppingBagRefreshed = false, isWishListRefreshed = false;

    //    public BadgeView itemBadgeView;
    private int mCartItemCount, mWishlistItemCount;
    private int fiveDp, threeDp;
    DBAdapter dbAdapter;

    //    public static String FILTER_LANGUGE_CHANGE = "FILTER_LANGUGE_CHANGE";
    public static String FILTER_SHOPPING_BAG_CHANGE = "FILTER_SHOPPING_BAG_CHANGE";
    public static String FILTER_WISHLIST_CHANGE = "FILTER_WISHLIST_CHANGE";
    public static String FILTER_SESSION_EXPIRE = "FILTER_SESSION_EXPIRE";

    public CategoryList categoryList;

    //    public FortCallBackManager fortCallback = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");

    private int RC_SHARE_CODE = 1515;

    private enum FragmentViewEnum {
        ProductsView, SearchView, ShoppingBagsView, WishlistView, AccountView
    }

    Animation animClose, animOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_home);
        Utils.setLocale(getActivity());
        ButterKnife.bind(getActivity());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
//        initilizePayFortSDK();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        mFirebaseAnalytics.setMinimumSessionDuration(500);
        mFirebaseAnalytics.setSessionTimeoutDuration(1000000);

        animOpen = AnimationUtils.loadAnimation(getActivity(),
                R.anim.fade_in);
        animClose = AnimationUtils.loadAnimation(getActivity(),
                R.anim.fade_out);

        dbAdapter = new DBAdapter(getActivity());

        InitializeControlAction();

        // set initial
        if (savedInstanceState == null) {
            mContent = ProductsFragment.getInstance();
            lastFragTag = ProductsFragment.TAG_PRODUCT_FRAGMENT;
            mFragmentManager = getSupportFragmentManager();

            mFragmentTransaction = mFragmentManager
                    .beginTransaction();
            mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
            mFragmentTransaction.addToBackStack(lastFragTag);
            lastProductCommitId = mFragmentTransaction.commit();
            mFragmentManager.executePendingTransactions();
            LogUtils.e("", "lastProductCommitId::" + lastProductCommitId);

            setSidebarView(FragmentViewEnum.ProductsView);
        }

        fiveDp = (int) Utils.pxFromDp(getActivity(), 5);
        threeDp = (int) Utils.pxFromDp(getActivity(), 2);

//        itemBadgeView = new BadgeView(getActivity(), tvSidebarShoppingBag, 11);
//        itemBadgeView.setBadgeMargin(threeDp, threeDp);


//        registerReceiver(mLanguageChangeReceiver, new IntentFilter(FILTER_LANGUGE_CHANGE));
        registerReceiver(mSessionExpireReceiver, new IntentFilter(FILTER_SESSION_EXPIRE));
        registerReceiver(mWishListReceiver, new IntentFilter(FILTER_WISHLIST_CHANGE));
        registerReceiver(mShoppingBagListReceiver, new IntentFilter(FILTER_SHOPPING_BAG_CHANGE));

        mHelpAdapter = new HelpAdapter(getActivity(), true);
        lvSideHelp.setAdapter(mHelpAdapter);

        if (Shy7lo.mCMSPage != null) {
            mHelpAdapter.setData(Shy7lo.mCMSPage);
        } else {
            Utils.getCMSPage(getActivity());
        }

        setDrawerGravity();
        if (Shy7lo.sortingDataList == null || Shy7lo.sortingDataList.size() == 0) {
            getSortingList(getActivity());
        }
        checkForMaintenance();

//        if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//
//            DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) nvSidebar.getLayoutParams();
//            params.gravity = Gravity.END;
//            nvSidebar.setLayoutParams(params);
//
//            tvSidebarProducts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_products_selector, 0);
//            tvSidebarSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_search_selector, 0);
//            tvSidebarShoppingBag.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_shopping_bags_selector, 0);
//            tvSidebarWishlist.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_wishlist_selector, 0);
//            tvSidebarMyShy7lo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_myshy7lo_selector, 0);
//
//            itemBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//        } else {
//
//            DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) nvSidebar.getLayoutParams();
//            params.gravity = Gravity.START;
//            nvSidebar.setLayoutParams(params);
//
//            tvSidebarProducts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_products_selector, 0, 0, 0);
//            tvSidebarSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_search_selector, 0, 0, 0);
//            tvSidebarShoppingBag.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_shopping_bags_selector, 0, 0, 0);
//            tvSidebarWishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_wishlist_selector, 0, 0, 0);
//            tvSidebarMyShy7lo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_myshy7lo_selector, 0, 0, 0);
//
//            itemBadgeView.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
//        }
//
//        updateBadgetCount();


        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        setBackChangeSideBar();
                    }
                });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(MyFirebaseMessagingService.SCREEN_NAME)) {

            String main_screen = bundle.getString(MyFirebaseMessagingService.SCREEN_NAME);
            String message = bundle.getString(MyFirebaseMessagingService.SCREEN_MESSAGE);
            LogUtils.e("", "Push main_screen::" + main_screen);
            LogUtils.e("", "Push message::" + message);

            if (!TextUtils.isEmpty(main_screen)) {
                if (main_screen.equalsIgnoreCase(MyFirebaseMessagingService.SCREEN_PRODUCT_GENERAL)) {
                    String mCategoryID = message;
                    if (!TextUtils.isEmpty(mCategoryID)) {
                        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.landingScreens.baseScreens != null) {
                            List<AppInit.BaseScreen> mBaseScreenList = Shy7lo.mAppInit.landingScreens.baseScreens;
                            if (mBaseScreenList != null && mBaseScreenList.size() > 0) {
                                for (int i = 0; i < mBaseScreenList.size(); i++) {
//                                    if (mBaseScreenList.get(i).cat_id.equalsIgnoreCase(mCategoryID)) {
//                                        MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, i);
                                    MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0);

                                    Bundle b = new Bundle();
                                    b.putString(ProductsFragment.BNDL_PRODUCT_CATEGORY, "1");
                                    loadProductCategory(b);

                                    break;
//                                    }
                                }
                            }
                        }

                    }

                } else if (main_screen.equalsIgnoreCase(MyFirebaseMessagingService.SCREEN_PRODUCT_WOMEN)) {

                    Bundle b = new Bundle();
                    b.putString(ProductsFragment.BNDL_PRODUCT_CATEGORY, "1");
                    loadProductCategory(b);

                } else if (main_screen.equalsIgnoreCase(MyFirebaseMessagingService.SCREEN_PRODUCT_MEN)) {

                    Bundle b = new Bundle();
                    b.putString(ProductsFragment.BNDL_PRODUCT_CATEGORY, "2");
                    loadProductCategory(b);

                } else if (main_screen.equalsIgnoreCase(MyFirebaseMessagingService.SCREEN_PRODUCT_KIDS)) {

                    Bundle b = new Bundle();
                    b.putString(ProductsFragment.BNDL_PRODUCT_CATEGORY, "3");
                    loadProductCategory(b);

                } else if (main_screen.equalsIgnoreCase(MyFirebaseMessagingService.SCREEN_CART)) {

                    loadShoppingBags();

                } else if (main_screen.equalsIgnoreCase(MyFirebaseMessagingService.SCREEN_PRODUCT_LIST)) {

                    String mSearchCriteria = message;
                    Bundle b = new Bundle();
                    b.putString(ProductsItemsFragment.BK_PRODUCT_ITEMS, mSearchCriteria);
                    b.putBoolean(ProductsItemsFragment.IS_FROM_BANNER, true);

                    loadBannerItems(b);

                } else if (main_screen.equalsIgnoreCase(MyFirebaseMessagingService.SCREEN_DETAILS)) {

                    Bundle b = new Bundle();
                    b.putString(Constant.BNDL_SKU, "" + message);

                    loadProductDetails(b);

                }
            }

        }

//        if (bundle != null && bundle.containsKey(MainCategoryForProductFragment.BNDL_MAIN_CATE_ID)) {
//            mCategoryID = bundle.getString(MainCategoryForProductFragment.BNDL_MAIN_CATE_ID);
//        }

        Utils.setInitConfig(getActivity());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
//        Log.i("", "onRequestPermissionResult");
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MyShy7loFragment.TAG_MYSHY7LO_FRAGMENT);
//        if (fragment != null) {
//            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
//            if (grantResults.length <= 0) {
//                // If user interaction was interrupted, the permission request is cancelled and you
//                // receive empty arrays.
//                Log.i("", "User interaction was cancelled.");
//            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted.
//                getAddress();
//            } else {
//                // Permission denied.
//
//                // Notify the user via a SnackBar that they have rejected a core permission for the
//                // app, which makes the Activity useless. In a real app, core permissions would
//                // typically be best requested during a welcome-screen flow.
//
//                // Additionally, it is important to remember that a permission might have been
//                // rejected without asking the user for permission (device policy or "Never ask
//                // again" prompts). Therefore, a user interface affordance is typically implemented
//                // when permissions are denied. Otherwise, your app could appear unresponsive to
//                // touches or interactions which have required permissions.
//                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                // Build intent that displays the App settings screen.
//                                Intent intent = new Intent();
//                                intent.setAction(
//                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                Uri uri = Uri.fromParts("package",
//                                        BuildConfig.APPLICATION_ID, null);
//                                intent.setData(uri);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            }
//                        });
//            }
//        }
    }

//    private void showSnackbar(final String text) {
//        View container = findViewById(android.R.id.content);
//        if (container != null) {
//            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
//        }
//    }
//
//    private void showSnackbar(final int mainTextStringId, final int actionStringId,
//                              View.OnClickListener listener) {
//        Snackbar.make(findViewById(android.R.id.content),
//                getString(mainTextStringId),
//                Snackbar.LENGTH_INDEFINITE)
//                .setAction(getString(actionStringId), listener).show();
//    }
//
    //
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (!checkPermissions()) {
//            requestPermissions();
//        } else {
//            getAddress();
//        }
//    }

    public void setBackChangeSideBar() {

        try {

            LogUtils.e("", "onBackStackChanged call");
            LogUtils.d("test", "backStackEntryCount: " + getSupportFragmentManager().getBackStackEntryCount());

            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                String tagLastFragment = getSupportFragmentManager().getBackStackEntryAt(i).getName();
                LogUtils.e("", i + " Stack " + tagLastFragment);
            }

            String tagLastFragment = getSupportFragmentManager().getBackStackEntryAt(
                    getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
            LogUtils.e("", "tagLastFragment::" + tagLastFragment);
            if (!TextUtils.isEmpty(tagLastFragment)) {
                if (tagLastFragment.equals(ProductsFragment.TAG_PRODUCT_FRAGMENT)
                        || tagLastFragment.equals(ProductsItemsFragment.TAG_PRODUCT_ITEMS_FRAGMENT)
                        || tagLastFragment.equals(ProductDetailsFragment.TAG_PRODUCT_DETIAILS_FRAGMENT)) {
                    setSidebarView(FragmentViewEnum.ProductsView);
                }
// else if (tagLastFragment.equals(SearchAlgoliaFragment.TAG_SEARCH_FRAGMENT)) {
//                    setSidebarView(FragmentViewEnum.SearchView);
//                }
//                else if (tagLastFragment.equals(ShoppingBagsFragment.TAG_SHOPPING_BAGS_FRAGMENT)
//                        || tagLastFragment.equals(LoginFragment.TAG_LOGIN_FRAGMENT)
//                        || tagLastFragment.equals(ShipmentPayFragment.TAG_SHIPMENT_PAY_FRAGMENT)) {
//                    setSidebarView(FragmentViewEnum.ShoppingBagsView);
////                    if (tagLastFragment.equals(ShoppingBagsFragment.TAG_SHOPPING_BAGS_FRAGMENT) && isShoppingBagRefreshed) {
////                        isShoppingBagRefreshed = false;
////                        ShoppingBagsFragment.getInstance().getShoppingListFromDB();
////                    }
//                    ShoppingBagsFragment.getInstance().onStart();
//                }
                else if (tagLastFragment.equals(WishlistFragment.TAG_WISHLIST_FRAGMENT)) {
                    setSidebarView(FragmentViewEnum.WishlistView);
                    if (isWishListRefreshed) {
                        isWishListRefreshed = false;
                        WishlistFragment.getInstance().getWishListFromDB();
                    }
                }
//                else if (tagLastFragment.equals(MyShy7loFragment.TAG_MYSHY7LO_FRAGMENT)) {
//                    setSidebarView(FragmentViewEnum.AccountView);
//                }
                else if (tagLastFragment.equals(AccountFragment.TAG_ACCOUNT_FRAGMENT)) {
                    setSidebarView(FragmentViewEnum.AccountView);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    @Override
//    public void onNewIntent(Intent intent) {
//        this.setIntent(intent);
//    }


    @Override
    protected void onResume() {
        super.onResume();
        Shy7lo.setActivityContext(getActivity());
//        if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
//            MyPref.setPref(getApplicationContext(), MyPref.DEFAULT_LANGUAGE, MyPref.LANGUAGE_English);
//            MyPref.setPref(getApplicationContext(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN);
//            Utils.setLocale(getApplicationContext(), "en");
//        } else {
//            MyPref.setPref(getApplicationContext(), MyPref.DEFAULT_LANGUAGE, MyPref.LANGUAGE_Arabic);
//            MyPref.setPref(getApplicationContext(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_AR);
//            Utils.setLocale(getApplicationContext(), "ar");
//        }

        setSignoutView();

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

//        LogUtils.e("", "GUEST_CART_ID::" + getPref(getActivity(), MyPref.GUEST_CART_ID, ""));
//        if (TextUtils.isEmpty(getPref(getActivity(), MyPref.GUEST_CART_ID, ""))) {
//            Utils.getGuestCartToken(getActivity());
//        }

        if (categoryList == null) {
            getCategoryList();
        }

        checkForUpgrade();

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


    public void getCategoryList() {
        LogUtils.e("", "getCategoryList call");

        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<CategoryList> call = serviceAPI.getCategoriesList(Shy7lo.mLangCode);
        call.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                if (response.isSuccessful()) {
                    try {
                        categoryList = response.body();
//                        if (categoryList != null && categoryList.success.equals("2")) {
//                            Utils.showInitialScreen(getActivity());
//                            return;
//                        }
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
            public void onFailure(Call<CategoryList> call, Throwable t) {

            }
        });
    }

    private void checkForUpgrade() {

        LogUtils.e("", "checkForUpgrade call");

        String mAppVersion = getPref(getActivity(), MyPref.APP_VERSION, "");
        String mVersionName = BuildConfig.VERSION_NAME;
        LogUtils.e("", "mAppVersion::" + mAppVersion);
        LogUtils.e("", "mVersionName::" + mVersionName);
        if (!TextUtils.isEmpty(mAppVersion) && !TextUtils.isEmpty(mVersionName) && !mAppVersion.equals(mVersionName)) {
            String mLastUpgradeDate = MyPref.getPref(getActivity(), MyPref.LAST_UPGRADE_DATE, "");
            LogUtils.e("", "mLastUpgradeDate::" + mLastUpgradeDate);
            if (TextUtils.isEmpty(mLastUpgradeDate)) {
                String date = sdf.format(new Date());
                MyPref.setPref(getActivity(), MyPref.LAST_UPGRADE_DATE, date);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showUpgradeDialog();
                    }
                }, 2000);

            } else {
                Date lastDate = null;
                try {
                    lastDate = sdf.parse(mLastUpgradeDate);
                    String currentdateStr = sdf.format(new Date());
                    LogUtils.e("", "currentdateStr::" + currentdateStr);
                    Date currentDate = sdf.parse(currentdateStr);
                    LogUtils.e("", "lastDate::" + lastDate);
                    LogUtils.e("", "currentDate::" + currentDate);
                    if (currentDate.after(lastDate)) {
                        String date = sdf.format(currentDate);
                        MyPref.setPref(getActivity(), MyPref.LAST_UPGRADE_DATE, date);
                        showUpgradeDialog();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        LogUtils.e("", "intent::" + intent);

        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey(MyFirebaseMessagingService.SCREEN_NAME)) {

            String main_screen = bundle.getString(MyFirebaseMessagingService.SCREEN_NAME);
            String message = bundle.getString(MyFirebaseMessagingService.SCREEN_MESSAGE);
            LogUtils.e("", "Push main_screen::" + main_screen);
            LogUtils.e("", "Push  message::" + message);

            if (!TextUtils.isEmpty(main_screen)) {
                if (main_screen.equalsIgnoreCase(MyFirebaseMessagingService.SCREEN_PRODUCT_GENERAL)) {
                    String mCategoryID = message;
                    if (!TextUtils.isEmpty(mCategoryID)) {
                        if (Shy7lo.mAppInit != null && Shy7lo.mAppInit.landingScreens.baseScreens != null) {
                            List<AppInit.BaseScreen> mBaseScreenList = Shy7lo.mAppInit.landingScreens.baseScreens;
                            if (mBaseScreenList != null && mBaseScreenList.size() > 0) {
                                for (int i = 0; i < mBaseScreenList.size(); i++) {
//                                    if (mBaseScreenList.get(i).cat_id.equalsIgnoreCase(mCategoryID)) {
//                                        MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, i);
                                    MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0);

                                    Bundle b = new Bundle();
                                    b.putString(ProductsFragment.BNDL_PRODUCT_CATEGORY, "1");
                                    loadProductCategory(b);

                                    break;
//                                    }
                                }
                            }
                        }

                    }

                } else if (main_screen.equalsIgnoreCase(MyFirebaseMessagingService.SCREEN_PRODUCT_WOMEN)) {

                    Bundle b = new Bundle();
                    b.putString(ProductsFragment.BNDL_PRODUCT_CATEGORY, "1");
                    loadProductCategory(b);

                } else if (main_screen.equalsIgnoreCase(MyFirebaseMessagingService.SCREEN_PRODUCT_MEN)) {

                    Bundle b = new Bundle();
                    b.putString(ProductsFragment.BNDL_PRODUCT_CATEGORY, "2");
                    loadProductCategory(b);

                } else if (main_screen.equalsIgnoreCase(MyFirebaseMessagingService.SCREEN_PRODUCT_KIDS)) {

                    Bundle b = new Bundle();
                    b.putString(ProductsFragment.BNDL_PRODUCT_CATEGORY, "3");
                    loadProductCategory(b);

                } else if (main_screen.equalsIgnoreCase(MyFirebaseMessagingService.SCREEN_CART)) {

                    loadShoppingBags();

                } else if (main_screen.equalsIgnoreCase(MyFirebaseMessagingService.SCREEN_PRODUCT_LIST)) {

                    String mSearchCriteria = message;
                    Bundle b = new Bundle();
                    b.putString(ProductsItemsFragment.BK_PRODUCT_ITEMS, mSearchCriteria);
                    b.putBoolean(ProductsItemsFragment.IS_FROM_BANNER, true);

                    loadBannerItems(b);

                } else if (main_screen.equalsIgnoreCase(MyFirebaseMessagingService.SCREEN_DETAILS)) {

                    Bundle b = new Bundle();
                    b.putString(Constant.BNDL_SKU, "" + message);
                    b.putBoolean(Constant.BNDL_IS_FROM_SEARCH, false);

                    loadProductDetails(b);

                }
            }

        }

    }

    public void updateBadgetCount() {

//        if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            itemBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//        } else {
//            itemBadgeView.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
//        }

        mCartItemCount = getPref(getActivity(), MyPref.CART_ITEM_COUNT, 0);
//        mCartItemCount = 5;
        tvShoppingBagCounter.setText("" + mCartItemCount);
        if (mCartItemCount > 0) {
            tvShoppingBagCounter.setVisibility(View.VISIBLE);
        } else {
            tvShoppingBagCounter.setVisibility(View.INVISIBLE);
        }
//        itemBadgeView.setText("" + mCartItemCount);
//
//        if (mCartItemCount > 0) {
//            itemBadgeView.show();
//        } else {
//            itemBadgeView.hide();
//        }
    }

    public void updateWishListBadgetCount() {

        mWishlistItemCount = dbAdapter.getWishListCounter();

        tvWishlistCounter.setText("" + mWishlistItemCount);

        if (mWishlistItemCount > 0) {
            tvWishlistCounter.setVisibility(View.VISIBLE);
        } else {
            tvWishlistCounter.setVisibility(View.INVISIBLE);
        }
    }


//    private void initilizePayFortSDK() {
//        fortCallback = FortCallback.Factory.create();
//    }

    private void InitializeControlAction() {
        tvSidebarProducts.setOnClickListener(this);
        tvSidebarSearch.setOnClickListener(this);
//        tvSidebarShoppingBag.setOnClickListener(this);
        lnrSidebarShoppingBag.setOnClickListener(this);
//        tvSidebarWishlist.setOnClickListener(this);
        lnrSidebarWishlist.setOnClickListener(this);
        tvSidebarMyShy7lo.setOnClickListener(this);
//        tvSidebarHelpFAQ.setOnClickListener(this);
//        tvSidebarCustomers.setOnClickListener(this);
        tvSidebarLiveChat.setOnClickListener(this);
        tvSidebarAppInfo.setOnClickListener(this);
        tvSidebarSignOut.setOnClickListener(this);
        lvSideHelp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                CMSPage.Data mCMSPage = Shy7lo.mCMSPage.get(position);
                if (mCMSPage.child != null) {

                    Bundle bundle = new Bundle();
                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                        bundle.putString(InfoOptionActivity.BNDL_TITLE, mCMSPage.titleAr);
                    } else {
                        bundle.putString(InfoOptionActivity.BNDL_TITLE, mCMSPage.title);
                    }
                    bundle.putString(InfoOptionActivity.BNDL_OPTION, new Gson().toJson(mCMSPage.child));

                    IntentHandler.startActivity(getActivity(), InfoOptionActivity.class, bundle);

                } else {

                    if (!Utils.isInternetConnected(getActivity())) {
                        Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                            @Override
                            public void onRetryClicked(Dialog dialog) {
                                if (Utils.isInternetConnected(getActivity())) {
                                    if (dialog != null && dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                }
                            }
                        });
                        return;
                    }

                    Bundle bundle = new Bundle();
                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                        bundle.putString(WebviewActivity.BNDL_URL, mCMSPage.urlAr);
                        bundle.putString(WebviewActivity.BNDL_TITLE, mCMSPage.titleAr);
                    } else {
                        bundle.putString(WebviewActivity.BNDL_URL, mCMSPage.url);
                        bundle.putString(WebviewActivity.BNDL_TITLE, mCMSPage.title);
                    }

                    IntentHandler.startActivity(getActivity(), WebviewActivity.class, bundle);

                }
                closeDrawer();

            }
        });
    }


    public boolean isProductFragment(Fragment mFragment) {
        boolean isProductFragment = false;
        if (mFragment instanceof ProductsFragment || mFragment instanceof ProductsItemsFragment || mFragment instanceof ProductDetailsFragment) {
            isProductFragment = true;
        } else {
            isProductFragment = false;
        }
        LogUtils.e("", "isProductFragment::" + isProductFragment);
        return isProductFragment;
    }

//    public boolean isSearchFragment(Fragment mFragment) {
//        boolean isSearchFragment = false;
//        if (mFragment instanceof SearchFragment) {
//            isSearchFragment = true;
//        } else {
//            isSearchFragment = false;
//        }
//        LogUtils.e("", "isSearchFragment::" + isSearchFragment);
//        return isSearchFragment;
//    }

    public boolean isSearchFragment(Fragment mFragment) {
        boolean isSearchFragment = false;
        if (mFragment instanceof SearchAlgoliaFragment) {
            isSearchFragment = true;
        } else {
            isSearchFragment = false;
        }
        LogUtils.e("", "isSearchFragment::" + isSearchFragment);
        return isSearchFragment;
    }


    public boolean isShoppingBagFragment(Fragment mFragment) {
        boolean isShoppingBagFragment = false;
        if (mFragment instanceof ShoppingBagsFragment) {
            isShoppingBagFragment = true;
        } else {
            isShoppingBagFragment = false;
        }
        LogUtils.e("", "isShoppingBagFragment::" + isShoppingBagFragment);
        return isShoppingBagFragment;
    }

    public boolean isWishlistFragment(Fragment mFragment) {
        boolean isWishlistFragment = false;
        if (mFragment instanceof WishlistFragment) {
            isWishlistFragment = true;
        } else {
            isWishlistFragment = false;
        }
        LogUtils.e("", "isWishlistFragment::" + isWishlistFragment);
        return isWishlistFragment;
    }

//    public boolean isMyShy7loFragment(Fragment mFragment) {
//        boolean isMyShy7loFragment = false;
//        if (mFragment instanceof MyShy7loFragment) {
//            isMyShy7loFragment = true;
//        } else {
//            isMyShy7loFragment = false;
//        }
//        LogUtils.e("", "isMyShy7loFragment::" + isMyShy7loFragment);
//        return isMyShy7loFragment;
//    }


    int lastProductCommitId = -1, lastCartCommitId = -1;

    public void setFirebaseLog(String screen) {
//        if (BuildConfig.DEBUG) {
//            mFirebaseAnalytics.setAnalyticsCollectionEnabled(false);
//        } else {
            mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
//        }
        mFirebaseAnalytics.logEvent(screen, new Bundle());
        if (screen.equalsIgnoreCase("MyShy7lo_Account")) {
            setSidebarView(FragmentViewEnum.AccountView);
        }

        LogUtils.e("", "mFirebaseAnalytics screen::" + screen);
    }

    @Override
    public void onClick(final View view) {

//        Fragment mFragment = mFragmentManager.findFragmentById(R.id.mainFrameLayout);
        Log.e("view", "view id" + view.getId());

        if (view == tvSidebarProducts) {

            mContent = ProductsFragment.getInstance();
            lastFragTag = ProductsFragment.TAG_PRODUCT_FRAGMENT;

//            if (!isProductFragment(mFragment)) {
//                loadProducts();
            setSidebarView(FragmentViewEnum.ProductsView);
//            } else {
//                closeDrawer();
//            }


        } else if (view == tvSidebarSearch) {

//            mContent = SearchFragment.getInstance();
//            lastFragTag = SearchFragment.TAG_SEARCH_FRAGMENT;
//            if (mFragmentManager == null) {
//                mFragmentManager = getSupportFragmentManager();
//            }
//            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            tvSidebarProducts.performClick();

//            setSidebarView(FragmentViewEnum.ProductsView);
            String mCategoryID = "", mCategoryName = "";
            ProductsFragment mProductsFragment = (ProductsFragment) mFragmentManager.findFragmentByTag(ProductsFragment.TAG_PRODUCT_FRAGMENT);
            if (mProductsFragment != null) {
                mCategoryID = mProductsFragment.getMainCategoryId();
                mCategoryName = mProductsFragment.getMainCategoryName();
            } else {
                LogUtils.e("", "mProductsFragment is null");
            }

            LogUtils.e("", "mCategoryID::" + mCategoryID);
            if (TextUtils.isEmpty(mCategoryID)) {
                mCategoryID = "143";
            }
            if (TextUtils.isEmpty(mCategoryName)) {
                mCategoryName = "" + getString(R.string.women);
            }
            Bundle bundle = new Bundle();
            bundle.putString(MainCategoryForProductFragment.BK_PRODUCT_ID, mCategoryID);
            bundle.putString(MainCategoryForProductFragment.BNDL_MAIN_CATE_ID, "" + mCategoryID);
            bundle.putString(MainCategoryForProductFragment.BNDL_MAIN_CATE_NAME, "" + mCategoryName);

            HomeActivity activity = (HomeActivity) getActivity();
            if (activity != null) {
                activity.loadMainCategory(bundle);
            }

//            mContent = SearchAlgoliaFragment.getInstance();
//            lastFragTag = SearchAlgoliaFragment.TAG_SEARCH_FRAGMENT;
//
////            if (!isSearchFragment(mFragment)) {
////                loadSeach();
//            setSidebarView(FragmentViewEnum.SearchView);
////            } else {
////                closeDrawer();
////            }

        } else if (view == tvSidebarShoppingBag || view == lnrSidebarShoppingBag) {

            mContent = ShoppingBagsFragment.getInstance();
            lastFragTag = ShoppingBagsFragment.TAG_SHOPPING_BAGS_FRAGMENT;

//            if (!isShoppingBagFragment(mFragment)) {
//                loadShoppingBags();
            setSidebarView(FragmentViewEnum.ShoppingBagsView);
//            } else {
//                closeDrawer();
//            }

        } else if (view == tvSidebarWishlist || view == lnrSidebarWishlist) {

            mContent = WishlistFragment.getInstance();
            lastFragTag = WishlistFragment.TAG_WISHLIST_FRAGMENT;

//            if (!isWishlistFragment(mFragment)) {
//                loadWishlist();
            setSidebarView(FragmentViewEnum.WishlistView);
//            } else {
//                closeDrawer();
//            }


        } else if (view == tvSidebarMyShy7lo) {

//            mContent = MyShy7loFragment.getInstance();
//            lastFragTag = MyShy7loFragment.TAG_MYSHY7LO_FRAGMENT;
            mContent = AccountFragment.getInstance();
            lastFragTag = AccountFragment.TAG_ACCOUNT_FRAGMENT;

//            if (!isMyShy7loFragment(mFragment)) {
//                loadMyShy7lo();
            setSidebarView(FragmentViewEnum.AccountView);
//            } else {
//                closeDrawer();
//            }

        } else if (view == tvSidebarLiveChat) {


            String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
            String userId = MyPref.getPref(getActivity(), MyPref.USER_ID, "");

            if (!TextUtils.isEmpty(userToken) && !TextUtils.isEmpty(userId)) {

                FreshchatUser freshUser = Freshchat.getInstance(getActivity()).getUser();

                freshUser.setFirstName("" + MyPref.getPref(getActivity(), MyPref.USER_FIRSTNAME, ""));
                freshUser.setLastName("" + MyPref.getPref(getActivity(), MyPref.USER_LASTNAME, ""));
                freshUser.setEmail("" + MyPref.getPref(getActivity(), MyPref.USER_EMAIL, ""));
                freshUser.setPhone("+" + (MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_CODE, "").replace("+", "")), "" + MyPref.getPref(getActivity(), MyPref.USER_PHONE, ""));

                try {
                    Freshchat.getInstance(getActivity()).setUser(freshUser);
                } catch (MethodNotAllowedException e) {
                    e.printStackTrace();
                }

            }

            Freshchat.showConversations(getActivity());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    closeDrawer();
                }
            }, 1000);

        } else if (view == tvSidebarAppInfo) {
            startActivity(new Intent(getActivity(), AppInfoActivity.class));
        } else if (view == tvSidebarSignOut) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_alert2);

            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
            Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
            TextView tvMsgText = (TextView) dialog.findViewById(R.id.tvMsgText);

            tvMsgText.setText("" + getString(R.string.msg_logout));

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                    getGuestCartToken();

                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                }
            });

            dialog.show();
        }

        if (view != tvSidebarSearch) {


//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {

            if (mFragmentManager == null) {
                mFragmentManager = getSupportFragmentManager();
            }

            mFragmentTransaction = mFragmentManager
                    .beginTransaction();
//        if (mContent.isAdded()) {
//            mFragmentTransaction.show(mContent);
//        } else {
            mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
//        }
            mFragmentTransaction.addToBackStack(lastFragTag);

            if (view == tvSidebarProducts) {
                lastProductCommitId = mFragmentTransaction.commitAllowingStateLoss();
                mFragmentManager.executePendingTransactions();
            } else if (view == tvSidebarShoppingBag || view == lnrSidebarShoppingBag) {
                lastCartCommitId = mFragmentTransaction.commitAllowingStateLoss();
                mFragmentManager.executePendingTransactions();
            } else {
                mFragmentTransaction.commitAllowingStateLoss();
                mFragmentManager.executePendingTransactions();
            }
//            }
//        });
//        mFragmentManager
//                .beginTransaction()
//                .replace(R.id.mainFrameLayout, mContent, lastFragTag).commit();
        }

        if (view != tvSidebarLiveChat) {
            closeDrawer();
        }
    }

    private void setSidebarView(FragmentViewEnum position) {

        tvSidebarProducts.setTypeface(null, Typeface.NORMAL);
        tvSidebarSearch.setTypeface(null, Typeface.NORMAL);
        tvSidebarShoppingBag.setTypeface(null, Typeface.NORMAL);
        tvShoppingBagCounter.setTypeface(null, Typeface.NORMAL);
        tvWishlistCounter.setTypeface(null, Typeface.NORMAL);
        tvSidebarWishlist.setTypeface(null, Typeface.NORMAL);
        tvSidebarMyShy7lo.setTypeface(null, Typeface.NORMAL);

        switch (position) {
            case ProductsView:
                tvSidebarProducts.setSelected(true);
                tvSidebarSearch.setSelected(false);
                tvSidebarShoppingBag.setSelected(false);
                tvSidebarWishlist.setSelected(false);
                tvSidebarMyShy7lo.setSelected(false);
                tvSidebarProducts.setTypeface(null, Typeface.BOLD);

                lnrSidebarWishlist.setSelected(false);
                tvWishlistCounter.setSelected(false);
                ivWishlist.setSelected(false);

                lnrSidebarShoppingBag.setSelected(false);
                tvShoppingBagCounter.setSelected(false);
                ivShoppingBag.setSelected(false);
                break;
            case SearchView:
                tvSidebarProducts.setSelected(false);
                tvSidebarSearch.setSelected(true);
                tvSidebarShoppingBag.setSelected(false);
                tvSidebarWishlist.setSelected(false);
                tvSidebarMyShy7lo.setSelected(false);
                tvSidebarSearch.setTypeface(null, Typeface.BOLD);

                lnrSidebarWishlist.setSelected(false);
                tvWishlistCounter.setSelected(false);
                ivWishlist.setSelected(false);

                lnrSidebarShoppingBag.setSelected(false);
                tvShoppingBagCounter.setSelected(false);
                ivShoppingBag.setSelected(false);
                break;
            case ShoppingBagsView:
                tvSidebarProducts.setSelected(false);
                tvSidebarSearch.setSelected(false);
                tvSidebarShoppingBag.setSelected(true);
                tvSidebarWishlist.setSelected(false);
                tvSidebarMyShy7lo.setSelected(false);
                tvSidebarShoppingBag.setTypeface(null, Typeface.BOLD);

                lnrSidebarWishlist.setSelected(false);
                tvWishlistCounter.setSelected(false);
                ivWishlist.setSelected(false);

                lnrSidebarShoppingBag.setSelected(true);
                tvShoppingBagCounter.setSelected(true);
                ivShoppingBag.setSelected(true);
                break;
            case WishlistView:
                tvSidebarProducts.setSelected(false);
                tvSidebarSearch.setSelected(false);
                tvSidebarShoppingBag.setSelected(false);
                tvSidebarWishlist.setSelected(true);
                tvSidebarMyShy7lo.setSelected(false);
                tvSidebarWishlist.setTypeface(null, Typeface.BOLD);

                lnrSidebarWishlist.setSelected(true);
                tvWishlistCounter.setSelected(true);
                ivWishlist.setSelected(true);

                lnrSidebarShoppingBag.setSelected(false);
                tvShoppingBagCounter.setSelected(false);
                ivShoppingBag.setSelected(false);
                break;
            case AccountView:
                tvSidebarProducts.setSelected(false);
                tvSidebarSearch.setSelected(false);
                tvSidebarShoppingBag.setSelected(false);
                tvSidebarWishlist.setSelected(false);
                tvSidebarMyShy7lo.setSelected(true);
                tvSidebarMyShy7lo.setTypeface(null, Typeface.BOLD);

                lnrSidebarWishlist.setSelected(false);
                tvWishlistCounter.setSelected(false);
                ivWishlist.setSelected(false);

                lnrSidebarShoppingBag.setSelected(false);
                tvShoppingBagCounter.setSelected(false);
                ivShoppingBag.setSelected(false);
                break;
        }
    }

    public void showUpgradeDialog() {

        try {

            if (!isFinishing()) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.dialog_upgrade);

                Button btnUpgrade = (Button) dialog.findViewById(R.id.btnUpgrade);
                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                TextView tvUpdateText = (TextView) dialog.findViewById(R.id.tvUpdateText);

                btnUpgrade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                        }

                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                        }


                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    }
                });

                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Activity getActivity() {
        return HomeActivity.this;
    }

    public void setDrawerSwipe(boolean isSwipable) {
        if (isSwipable) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

    }

    public void openMainLand() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        tvSidebarProducts.performClick();
    }

    private boolean isExitNeed = false;

    @Override
    public void onBackPressed() {

        try {


//        boolean isBackPressed = false;
//        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
//                mDrawerLayout.closeDrawer(GravityCompat.END);
//                return;
//            } else {
//                isBackPressed = true;
//            }
//
//        } else {
//            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//                mDrawerLayout.closeDrawer(GravityCompat.START);
//                return;
//            } else {
//                isBackPressed = true;
//            }
//        }

            if (mFragmentManager == null) {
                mFragmentManager = getSupportFragmentManager();
            }

            LogUtils.e("", "getBackStackEntryCount::" + mFragmentManager.getBackStackEntryCount());
            if (mFragmentManager.getBackStackEntryCount() > 0) {

                if (mFragmentManager.getBackStackEntryCount() == 1) {

                    setSidebarView(FragmentViewEnum.ProductsView);

                    if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                        if (!mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                            isExitNeed = false;
                        }
                    } else {
                        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                            isExitNeed = false;
                        }
                    }

                    if (isExitNeed) {
                        Intent back = new Intent();
                        back.putExtra(InitialScreenActivity.IS_EXIT, true);
                        setResult(RESULT_OK, back);
                        finish();
                    } else {
                        if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                            mDrawerLayout.openDrawer(GravityCompat.END);
                        } else {
                            mDrawerLayout.openDrawer(GravityCompat.START);
                        }
                        Utils.showToast(getActivity(), getString(R.string.press_exit));
                        isExitNeed = true;
                    }

                } else {

                    if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                            mDrawerLayout.closeDrawer(GravityCompat.END);
                            return;
                        }

                    } else {
                        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            return;
                        }
                    }

                    mFragmentManager.popBackStack();
                    isExitNeed = false;

                }

            } else {
//            super.onBackPressed();
//            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                mDrawerLayout.openDrawer(GravityCompat.END);
//            } else {
//                mDrawerLayout.openDrawer(GravityCompat.START);
//            }
                Intent back = new Intent();
                back.putExtra(InitialScreenActivity.IS_EXIT, true);
                setResult(RESULT_OK, back);
                finish();
            }

////        if (isBackPressed) {
//            super.onBackPressed();
//
//            LogUtils.e("", "getBackStackEntryCount::" + mFragmentManager.getBackStackEntryCount());
//            if (mFragmentManager.getBackStackEntryCount() > 0) {
//
//                isExitNeed = false;
//
//                String tagLastFragment = mFragmentManager.getBackStackEntryAt(
//                        mFragmentManager.getBackStackEntryCount() - 1).getName();
//                LogUtils.e("", "tagLastFragment::" + tagLastFragment);
//
//                if (tagLastFragment.equals(ProductsFragment.TAG_PRODUCT_FRAGMENT)
//                        || tagLastFragment.equals(ProductsItemsFragment.TAG_PRODUCT_CATEGORY_FRAGMENT)
//                        || tagLastFragment.equals(ProductDetailsFragment.TAG_PRODUCT_DETIAILS_FRAGMENT)) {
//                    setSidebarView(FragmentViewEnum.ProductsView);
//                } else if (tagLastFragment.equals(SearchFragment.TAG_SEARCH_FRAGMENT)) {
//                    setSidebarView(FragmentViewEnum.SearchView);
//                } else if (tagLastFragment.equals(ShoppingBagsFragment.TAG_SHOPPING_BAGS_FRAGMENT)
//                        || tagLastFragment.equals(LoginFragment.TAG_LOGIN_FRAGMENT)
//                        || tagLastFragment.equals(ShipmentPayFragment.TAG_SHIPMENT_PAY_FRAGMENT)) {
//                    setSidebarView(FragmentViewEnum.ShoppingBagsView);
//                } else if (tagLastFragment.equals(WishlistFragment.TAG_WISHLIST_FRAGMENT)) {
//                    setSidebarView(FragmentViewEnum.WishlistView);
//                } else if (tagLastFragment.equals(MyShy7loFragment.TAG_MYSHY7LO_FRAGMENT)) {
//                    setSidebarView(FragmentViewEnum.AccountView);
//                }
//            } else {
////                setSidebarView(FragmentViewEnum.ProductsView);
//                if (isExitNeed) {
//                    finish();
//                } else {
//                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                        mDrawerLayout.openDrawer(GravityCompat.END);
//                    } else {
//                        mDrawerLayout.openDrawer(GravityCompat.START);
//                    }
//                    Utils.showToast(getActivity(), getString(R.string.press_exit));
//                    isExitNeed = true;
//                }
//
//            }
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void loadProducts() {
        tvSidebarProducts.performClick();
//        ProductsFragment mProductsFragment = ProductsFragment.getInstance();
//        if (mProductsFragment != null) {
//            switchFragment(mProductsFragment,
//                    ProductsFragment.TAG_PRODUCT_FRAGMENT);
//        }
    }

    public void loadSeach() {
        tvSidebarSearch.performClick();
//        SearchFragment mSearchFragment = SearchFragment.getInstance();
//        if (mSearchFragment != null) {
//            switchFragment(mSearchFragment,
//                    SearchFragment.TAG_SEARCH_FRAGMENT);
//        }
    }

    public void loadShoppingBags() {
//        tvSidebarShoppingBag.performClick();
        lnrSidebarShoppingBag.performClick();
//        ShoppingBagsFragment mShoppingBagsFragment = ShoppingBagsFragment.getInstance();
//        if (mShoppingBagsFragment != null) {
//            switchFragment(mShoppingBagsFragment,
//                    ShoppingBagsFragment.TAG_SHOPPING_BAGS_FRAGMENT);
//        }
    }

//    public void loadShoppingBagsFromDetails() {
//        mContent = ShoppingBagsFragment.getInstance();
//        lastFragTag = ShoppingBagsFragment.TAG_SHOPPING_BAGS_FRAGMENT;
//
//        setSidebarView(FragmentViewEnum.ShoppingBagsView);
//
//        if (mFragmentManager == null) {
//            mFragmentManager = getSupportFragmentManager();
//        }
//
//        mFragmentTransaction = mFragmentManager
//                .beginTransaction();
//
//        Fragment lastFragment = mFragmentManager.findFragmentByTag(ProductDetailsFragment.TAG_PRODUCT_DETIAILS_FRAGMENT);
//        if (lastFragment != null) {
//            mFragmentTransaction.hide(lastFragment);
//        }
//
//        if (mContent != null) { // fragment not in back stack, create it.
//            if (mContent.isAdded()) {
//                mFragmentTransaction.show(mContent);
//            } else {
//                mFragmentTransaction.add(R.id.mainFrameLayout, mContent,
//                        lastFragTag);
//            }
//            mFragmentTransaction.addToBackStack(lastFragTag);
//            mFragmentTransaction.commit();
//            mFragmentManager.executePendingTransactions();
//        }
//    }

    public void loadShoppingBagsWithClearStack() {

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

//        LogUtils.e("", "lastProductCommitId:" + lastProductCommitId);
//        if (lastProductCommitId != -1) {
//            mFragmentManager.popBackStack(lastProductCommitId, 0);
//        } else {
//            mFragmentManager.popBackStack(ProductsFragment.TAG_PRODUCT_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        }
//        LogUtils.e("", "Fragment Size::"+mFragmentManager.getFragments());
//        for (Fragment fragment : mFragmentManager.getFragments()) {
//            if (fragment != null) {
//                LogUtils.e("", "Remove Fragment::"+fragment.getTag());
//                mFragmentManager.beginTransaction().remove(fragment).commit();
//            }
//        }

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        mContent = ProductsFragment.getInstance();
        lastFragTag = ProductsFragment.TAG_PRODUCT_FRAGMENT;
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {


        mFragmentTransaction = mFragmentManager
                .beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
        mFragmentTransaction.addToBackStack(lastFragTag);
        lastProductCommitId = mFragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();

        setSidebarView(FragmentViewEnum.ProductsView);
//            }
//        });

    }

    public void loadAccountWithClearStack(Bundle bundle) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        mContent = AccountFragment.getInstance();
        lastFragTag = AccountFragment.TAG_ACCOUNT_FRAGMENT;


        mFragmentTransaction = mFragmentManager
                .beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
        mFragmentTransaction.addToBackStack(lastFragTag);
        lastProductCommitId = mFragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();

        setSidebarView(FragmentViewEnum.AccountView);

    }

    public void loadShoppingCartsWithClearStack() {

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
//
//        LogUtils.e("", "lastCartCommitId:" + lastCartCommitId);
//        if (lastCartCommitId != -1) {
//            mFragmentManager.popBackStack(lastCartCommitId, 0);
//        } else {
//            mFragmentManager.popBackStack(ShoppingBagsFragment.TAG_SHOPPING_BAGS_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        }
//        loadShoppingBags();


        mContent = ShoppingBagsFragment.getInstance();
        lastFragTag = ShoppingBagsFragment.TAG_SHOPPING_BAGS_FRAGMENT;

//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {

        mFragmentTransaction = mFragmentManager
                .beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
        mFragmentTransaction.addToBackStack(lastFragTag);
        lastCartCommitId = mFragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();

        setSidebarView(FragmentViewEnum.ShoppingBagsView);
//            }
//        });

    }

    public void showContinueShopping() {
        LogUtils.e("", "CristMax showContinueShopping");

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ProductsItemsFragment.TAG_PRODUCT_ITEMS_FRAGMENT);
        if (fragment != null) {

            LogUtils.e("", "CristMax fragment is not null::" + mFragmentManager.getBackStackEntryCount());
            for (int i = mFragmentManager.getBackStackEntryCount() - 1; i > 0; i--) {
                LogUtils.e("", "getName::" + mFragmentManager.getBackStackEntryAt(i).getName());
                if (!mFragmentManager.getBackStackEntryAt(i).getName().equalsIgnoreCase(ProductsItemsFragment.TAG_PRODUCT_ITEMS_FRAGMENT)) {
                    mFragmentManager.popBackStack();
                } else {
                    break;
                }
            }

        } else {
            LogUtils.e("", "CristMax Not found in back stack::");
            loadProducts();
        }

    }

    public void loadMyShy7lo() {
        tvSidebarMyShy7lo.performClick();
//        MyShy7loFragment mMyShy7loFragment = MyShy7loFragment.getInstance();
//        if (mMyShy7loFragment != null) {
//            switchFragment(mMyShy7loFragment,
//                    MyShy7loFragment.TAG_MYSHY7LO_FRAGMENT);
//        }
    }

    public void loadWishlist() {
//        tvSidebarWishlist.performClick();
        lnrSidebarWishlist.performClick();
//        WishlistFragment mWishlistFragment = WishlistFragment.getInstance();
//        if (mWishlistFragment != null) {
//            switchFragment(mWishlistFragment,
//                    WishlistFragment.TAG_WISHLIST_FRAGMENT);
//        }
    }


//    public void loadLogin() {
//
//        mContent = LoginFragment.getInstance();
//        lastFragTag = LoginFragment.TAG_LOGIN_FRAGMENT;
//
//
////        new Handler().post(new Runnable() {
////            @Override
////            public void run() {
//
//        if (mFragmentManager == null) {
//            mFragmentManager = getSupportFragmentManager();
//        }
//
//        mFragmentTransaction = mFragmentManager
//                .beginTransaction();
//        mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
////        mFragmentTransaction.addToBackStack(lastFragTag);
//        mFragmentTransaction.commitAllowingStateLoss();
//        mFragmentManager.executePendingTransactions();
////            }
////        });
//        closeDrawer();
//
////        LoginFragment mLoginFragment = new LoginFragment();
////        if (mLoginFragment != null) {
////            switchFragment(mLoginFragment,
////                    LoginFragment.TAG_LOGIN_FRAGMENT);
////        }
//    }

    public void loadShipmentPay(Bundle bundle) {

        mContent = ShipmentPayFragment.getInstance();
        lastFragTag = ShipmentPayFragment.TAG_SHIPMENT_PAY_FRAGMENT;

        if (bundle != null) {
            if (mContent.getArguments() == null) {
                mContent.setArguments(bundle);
            } else {
                //Consider explicitly clearing arguments here
                mContent.getArguments().putAll(bundle);
            }
        }


//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        mFragmentTransaction = mFragmentManager
                .beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
        mFragmentTransaction.addToBackStack(lastFragTag);
        mFragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
//            }
//        });

        closeDrawer();

//        ShipmentPayFragment mShipmentPayFragment = new ShipmentPayFragment();
//        if (mShipmentPayFragment != null) {
//            switchFragment(mShipmentPayFragment,
//                    ShipmentPayFragment.TAG_SHIPMENT_PAY_FRAGMENT, bundle);
//        }
    }

    public void loadBannerItems(Bundle bundle) {

//        mContent = ProductsItemsFragment.getInstance();
//        lastFragTag = ProductsItemsFragment.TAG_PRODUCT_CATEGORY_FRAGMENT;
//
//        ProductsItemsFragment mProductsItemsFragment = (ProductsItemsFragment) mContent;
//        if (mProductsItemsFragment != null) {
//            mProductsItemsFragment.resetData();
//        }

        mContent = ProductsItemsFragment.getInstance();
        lastFragTag = ProductsItemsFragment.TAG_PRODUCT_ITEMS_FRAGMENT;

        ProductsItemsFragment mProductsItemsFragment = (ProductsItemsFragment) mContent;
        if (mProductsItemsFragment != null) {
            mProductsItemsFragment.resetData();
        }

        if (bundle != null) {

            if (mContent.getArguments() == null) {
                mContent.setArguments(bundle);
            } else {
                //Consider explicitly clearing arguments here
                mContent.getArguments().putAll(bundle);
            }

        }


//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        mFragmentTransaction = mFragmentManager
                .beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
        mFragmentTransaction.addToBackStack(lastFragTag);
        mFragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
//            }
//        });
//        Fragment lastFragment = mFragmentManager.findFragmentByTag(ProductsFragment.TAG_PRODUCT_FRAGMENT);
//        if (lastFragment != null) {
//            mFragmentTransaction.hide(lastFragment);
//        }
//
//        if (mContent != null) { // fragment not in back stack, create it.
//            if (mContent.isAdded()) {
//                mFragmentTransaction.show(mContent);
//            } else {
//                mFragmentTransaction.add(R.id.mainFrameLayout, mContent,
//                        lastFragTag);
////                lastFragTag = lastFragTag;
//            }
//            mFragmentTransaction.addToBackStack(lastFragTag);
//            mFragmentTransaction.commit();
//            mFragmentManager.executePendingTransactions();
//        }

        closeDrawer();

//        ProductsItemsFragment mProductsItemsFragment = new ProductsItemsFragment();
//        if (mProductsItemsFragment != null) {
//            switchFragment(mProductsItemsFragment,
//                    ProductsItemsFragment.TAG_PRODUCT_CATEGORY_FRAGMENT, ProductsItemsFragment.BK_PRODUCT_ITEMS, womenBanner);
//        }
    }

    public void loadSearchWithBundle(Bundle bundle) {

        mContent = SearchAlgoliaFragment.getInstance();
        lastFragTag = SearchAlgoliaFragment.TAG_SEARCH_FRAGMENT;

        if (bundle != null) {

            if (mContent.getArguments() == null) {
                mContent.setArguments(bundle);
            } else {
                mContent.getArguments().putAll(bundle);
            }

        }

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        mFragmentTransaction = mFragmentManager
                .beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
        mFragmentTransaction.addToBackStack(lastFragTag);
        mFragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();

        closeDrawer();

    }

    public void loadMainCategory(Bundle bundle) {

        mContent = MainCategoryForProductFragment.getInstance();
        lastFragTag = MainCategoryForProductFragment.TAG_MAIN_CATEGORY_FOR_PRODUCT_FRAGMENT;

        if (bundle != null) {

            if (mContent.getArguments() == null) {
                mContent.setArguments(bundle);
            } else {
                //Consider explicitly clearing arguments here
                mContent.getArguments().putAll(bundle);
            }
        }

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        mFragmentTransaction = mFragmentManager
                .beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
        mFragmentTransaction.addToBackStack(lastFragTag);
        mFragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
//        mFragmentTransaction.commit();

        closeDrawer();

    }

    public void openSignUp() {

        boolean isFragmentFound = false;


//        if (bundle != null) {
//
//            if (mContent.getArguments() == null) {
//                mContent.setArguments(bundle);
//            } else {
//                //Consider explicitly clearing arguments here
//                mContent.getArguments().putAll(bundle);
//            }
//        }

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        Fragment mFragment = mFragmentManager.findFragmentByTag(SignUpFragment.TAG_SIGNUP_FRAGMENT);
        if (mFragment != null) {
            mContent = mFragment;
            isFragmentFound = true;
        } else {
            mContent = SignUpFragment.getInstance();
        }
        lastFragTag = SignUpFragment.TAG_SIGNUP_FRAGMENT;

        mFragmentTransaction = mFragmentManager
                .beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
        if (isFragmentFound) {
            mFragmentTransaction.addToBackStack(null);
        } else {
            mFragmentTransaction.addToBackStack(lastFragTag);
        }
        mFragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
//        mFragmentTransaction.commit();

        closeDrawer();
    }

    public void openSignIn() {

        boolean isFragmentFound = false;


//        if (bundle != null) {
//
//            if (mContent.getArguments() == null) {
//                mContent.setArguments(bundle);
//            } else {
//                //Consider explicitly clearing arguments here
//                mContent.getArguments().putAll(bundle);
//            }
//        }

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        Fragment mFragment = mFragmentManager.findFragmentByTag(SignInFragment.TAG_SIGNIN_FRAGMENT);
        if (mFragment != null) {
            mContent = mFragment;
            isFragmentFound = true;
        } else {
            mContent = SignInFragment.getInstance();
        }

        lastFragTag = SignInFragment.TAG_SIGNIN_FRAGMENT;


        mFragmentTransaction = mFragmentManager
                .beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
        if (isFragmentFound) {
            mFragmentTransaction.addToBackStack(null);
        } else {
            mFragmentTransaction.addToBackStack(lastFragTag);
        }
        mFragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
//        mFragmentTransaction.commit();

        closeDrawer();

    }

    public void loadSubCategory(Bundle bundle) {

        mContent = SubCategoryForProductFragment.getInstance();
        lastFragTag = SubCategoryForProductFragment.TAG_SUB_CATEGORY_FOR_PRODUCT_FRAGMENT;

        if (bundle != null) {

            if (mContent.getArguments() == null) {
                mContent.setArguments(bundle);
            } else {
                //Consider explicitly clearing arguments here
                mContent.getArguments().putAll(bundle);
            }

        }

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        mFragmentTransaction = mFragmentManager
                .beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
        mFragmentTransaction.addToBackStack(lastFragTag);
        mFragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
//        mFragmentTransaction.commit();

        closeDrawer();

    }

    public void loadHomeLanding(Bundle bundle) {

        mContent = HomeLandingFragment.getInstance();
        lastFragTag = HomeLandingFragment.TAG;

        if (bundle != null) {

            if (mContent.getArguments() == null) {
                mContent.setArguments(bundle);
            } else {
                mContent.getArguments().putAll(bundle);
            }

        }

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        mFragmentTransaction = mFragmentManager
                .beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
        mFragmentTransaction.addToBackStack(lastFragTag);
        mFragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();

        closeDrawer();

    }


    public void loadProductDetails(Bundle bundle) {

        mContent = ProductDetailsFragment.getInstance();
        lastFragTag = ProductDetailsFragment.TAG_PRODUCT_DETIAILS_FRAGMENT;

        if (bundle != null) {
            if (mContent.getArguments() == null) {
                mContent.setArguments(bundle);
            } else {
                //Consider explicitly clearing arguments here
                mContent.getArguments().putAll(bundle);
            }
        }


//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        mFragmentTransaction = mFragmentManager
                .beginTransaction();
//        mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
//        mFragmentTransaction.addToBackStack(lastFragTag);
//        mFragmentTransaction.commit();
//        mFragmentManager.executePendingTransactions();

//                mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
        mFragmentTransaction.add(R.id.mainFrameLayout, mContent, lastFragTag);
        mFragmentTransaction.addToBackStack(lastFragTag);
        mFragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();


//                Fragment lastFragment = mFragmentManager.findFragmentByTag(ProductsItemsFragment.TAG_PRODUCT_ITEMS_FRAGMENT);
//                if (lastFragment != null) {
//                    mFragmentTransaction.hide(lastFragment);
//                }
//
//                if (mContent != null) { // fragment not in back stack, create it.
//                    if (mContent.isAdded()) {
//                        mFragmentTransaction.show(mContent);
//                    } else {
//                        mFragmentTransaction.add(R.id.mainFrameLayout, mContent,
//                                lastFragTag);
////                lastFragTag = lastFragTag;
//                    }
//                    mFragmentTransaction.addToBackStack(lastFragTag);
//                    mFragmentTransaction.commitAllowingStateLoss();
//                    mFragmentManager.executePendingTransactions();
//                }
//            }
//        });

        closeDrawer();

//        ProductDetailsFragment mProductDetailsFragment = new ProductDetailsFragment();
//        if (mProductDetailsFragment != null) {
//            switchFragment(mProductDetailsFragment,
//                    ProductDetailsFragment.TAG_PRODUCT_DETIAILS_FRAGMENT, bundle);
//        }
    }

    public void loadProductCategory(Bundle bundle) {

        mContent = ProductsFragment.getInstance();
        lastFragTag = ProductsFragment.TAG_PRODUCT_FRAGMENT;

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }

        ProductsFragment mProductsFragment = (ProductsFragment) mFragmentManager.findFragmentByTag(ProductsFragment.TAG_PRODUCT_FRAGMENT);
        if (mProductsFragment != null && mProductsFragment.isVisible()) {
            mProductsFragment.updateBundles(bundle);
            closeDrawer();
            return;
        }

        if (bundle != null) {

            if (mContent.getArguments() == null) {
                mContent.setArguments(bundle);
            } else {
                //Consider explicitly clearing arguments here
                mContent.getArguments().putAll(bundle);
            }
        }

//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
        try {
            mFragmentTransaction = mFragmentManager
                    .beginTransaction();
            mFragmentTransaction.replace(R.id.mainFrameLayout, mContent, lastFragTag);
            mFragmentTransaction.addToBackStack(lastFragTag);
            mFragmentTransaction.commitAllowingStateLoss();
            mFragmentManager.executePendingTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }

//            }
//        });


        closeDrawer();

    }


//    private void switchFragment(Fragment mFragment, String newFragTag, Bundle bundle) {
//        mFragmentTransaction = mFragmentManager.beginTransaction();
//        Log.e("sdfs", "newFragTag::" + newFragTag);
//
//        int count = mFragmentManager.getBackStackEntryCount();
//        if (count >= 1) {
//            String tagfrg = mFragmentManager.getBackStackEntryAt(
//                    mFragmentManager.getBackStackEntryCount() - 1).getName();
//
//            if (tagfrg.equalsIgnoreCase(newFragTag)) {
//                return;
//            }
//        }
//
//        Fragment lastFragment = mFragmentManager.findFragmentByTag(lastFragTag);
//        if (lastFragment != null) {
//            mFragmentTransaction.hide(lastFragment);
//        }
//
//        if (mFragment != null) { // fragment not in back stack, create it.
//            if (mFragment.isAdded()) {
//                mFragmentTransaction.show(mFragment);
//            } else {
//                mFragmentTransaction.add(R.id.mainFrameLayout, mFragment,
//                        newFragTag);
//                lastFragTag = newFragTag;
//            }
//
//            if (bundle != null) {
//                mFragment.setArguments(bundle);
//            }
//
//            mFragmentTransaction.addToBackStack(newFragTag);
//            mFragmentTransaction.commit();
//     mFragmentManager.executePendingTransactions();
//        }
//
//        closeDrawer();
//    }
//
//    private void switchFragment(Fragment mFragment, String newFragTag, String key, WomenBanner womenBanner) {
//        mFragmentTransaction = mFragmentManager.beginTransaction();
//        Log.e("sdfs", "newFragTag::" + newFragTag);
//
//        int count = mFragmentManager.getBackStackEntryCount();
//        if (count >= 1) {
//            String tagfrg = mFragmentManager.getBackStackEntryAt(
//                    mFragmentManager.getBackStackEntryCount() - 1).getName();
//
//            if (tagfrg.equalsIgnoreCase(newFragTag)) {
//                return;
//            }
//        }
//
//        Fragment lastFragment = mFragmentManager.findFragmentByTag(lastFragTag);
//        if (lastFragment != null) {
//            mFragmentTransaction.hide(lastFragment);
//        }
//
//        if (mFragment != null) { // fragment not in back stack, create it.
//            if (mFragment.isAdded()) {
//                mFragmentTransaction.show(mFragment);
//            } else {
//                mFragmentTransaction.add(R.id.mainFrameLayout, mFragment,
//                        newFragTag);
//                lastFragTag = newFragTag;
//            }
//
//            if (womenBanner != null) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(key, womenBanner);
//                mFragment.setArguments(bundle);
//            }
//
//            mFragmentTransaction.addToBackStack(newFragTag);
//            mFragmentTransaction.commit();
//     mFragmentManager.executePendingTransactions();
//        }
//
//        closeDrawer();
//    }

    public void openDrawer() {
        try {
            if (mDrawerLayout != null) {
                if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    mDrawerLayout.openDrawer(GravityCompat.END);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void closeDrawer() {
        try {
            if (mDrawerLayout != null) {
                if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //    public void requestForPayfortPayment(String mOrderId, String mTotalAmount, String mCurrencyCode, String userEmail) {
//        PayFortData payFortData = new PayFortData();
//        if (!TextUtils.isEmpty(mTotalAmount)) {
//            payFortData.amount = mTotalAmount;
//            payFortData.command = PayFortPayment.PURCHASE;
//            payFortData.currency = mCurrencyCode;
//            payFortData.customerEmail = userEmail;
//            payFortData.language = MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE_CODE, MyPref.LANGUAGE_EN);
//            ;
//            payFortData.merchantReference = mOrderId;
//
//            PayFortPayment payFortPayment = new PayFortPayment(getActivity(), fortCallback, this);
//            payFortPayment.requestForPayment(payFortData);
//        }
//    }
//

    String mProductName = "", mSku = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("", "HOme Activity onActivityResult call::" + requestCode + " " + resultCode);

        if (requestCode == MaintenanceScreenActivity.RC_MAINTANANCE_CODE && requestCode == RESULT_OK
                && data != null && data.getExtras() != null && data.getExtras().containsKey(MaintenanceScreenActivity.IS_RETURN)
                && data.getExtras().getBoolean(MaintenanceScreenActivity.IS_RETURN)) {

            try {
                String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
                if (!TextUtils.isEmpty(tag)) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                    if (fragment != null) {
                        if (fragment instanceof WishlistFragment) {
                            ((WishlistFragment) fragment).onStart();
                        } else if (fragment instanceof ShoppingBagsFragment) {
                            ((ShoppingBagsFragment) fragment).onStart();
                        } else if (fragment instanceof SearchAlgoliaFragment) {
                            ((SearchAlgoliaFragment) fragment).onStart();
                        } else if (fragment instanceof ProductsItemsFragment) {
                            ((ProductsItemsFragment) fragment).onStart();
                        } else if (fragment instanceof ProductDetailsFragment) {
                            ((ProductDetailsFragment) fragment).onStart();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == PayFortPayment.RESPONSE_PURCHASE) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(ShipmentPayFragment.TAG_SHIPMENT_PAY_FRAGMENT);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
//            fortCallback.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == RC_FILTER_CODE) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(ProductsItemsFragment.TAG_PRODUCT_ITEMS_FRAGMENT);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == AccountFragment.RC_SELECT_LANGUAGE) {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(AccountFragment.TAG_ACCOUNT_FRAGMENT);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }

        } else if (requestCode == AccountFragment.RC_MY_ORDER) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(AccountFragment.TAG_ACCOUNT_FRAGMENT);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
//        else if (requestCode == MyShy7loFragment.RC_SELECT_COUNTRY
//                || requestCode == MyShy7loFragment.RC_SELECT_LANGUAGE
//                || requestCode == MyShy7loFragment.RC_LOGOUT
//                || requestCode == MyShy7loFragment.RC_SIGNINUP) {
//
//            Fragment fragment = getSupportFragmentManager().findFragmentByTag(MyShy7loFragment.TAG_MYSHY7LO_FRAGMENT);
//            if (fragment != null) {
//                fragment.onActivityResult(requestCode, resultCode, data);
//            }
//
//        }
        else if (requestCode == WishlistFragment.RC_WISHLIST) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(WishlistFragment.TAG_WISHLIST_FRAGMENT);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == ShipmentPayFragment.RC_ADDRESS_CITY) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(ShipmentPayFragment.TAG_SHIPMENT_PAY_FRAGMENT);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == AddAddressActivity.RC_ADD_ADDRESS) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(ShipmentPayFragment.TAG_SHIPMENT_PAY_FRAGMENT);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == RC_SHARE_CODE) {
            if (data != null && data.getComponent() != null && !TextUtils.isEmpty(data.getComponent().flattenToShortString())) {
                String appName = data.getComponent().flattenToShortString();
                LogUtils.e("", "appName::" + appName);
                String activityName = "Message", activityType = "Message";

                if (!TextUtils.isEmpty(appName)) {
                    if (appName.contains("Facebook")) {
                        activityName = "Facebook";
                        activityType = "Post";
                    } else if (appName.contains("twitter")) {
                        activityName = "Twitter";
                        activityType = "Tweet";
                    } else if (appName.contains("Gmail")) {
                        activityName = "Gmail";
                        activityType = "Gmail";
                    } else if (appName.contains("mail")) {
                        activityName = "Mail";
                        activityType = "Mail";
                    } else if (appName.contains("Weibo")) {
                        activityName = "Weibo";
                        activityType = "Weibo message";
                    } else if (appName.contains("print")) {
                        activityName = "Printer";
                        activityType = "Print";
                    } else if (appName.contains("contact")) {
                        activityName = "Contact";
                        activityType = "Contact";
                    } else if (appName.contains("Flicker")) {
                        activityName = "Flicker";
                        activityType = "Flicker";
                    } else if (appName.contains("Vimeo")) {
                        activityName = "Vimeo";
                        activityType = "Vimeo";
                    } else if (appName.contains("whatsapp")) {
                        activityName = "Whatsapp";
                        activityType = "Whatsapp message";
                    } else if (appName.contains("hangouts")) {
                        activityName = "Hangout";
                        activityType = "Hangout message";
                    } else if (appName.contains("skype")) {
                        activityName = "Skype";
                        activityType = "Skype message";
                    } else if (appName.contains("hipchat")) {
                        activityName = "Hipchat";
                        activityType = "Hipchat Message";
                    } else if (appName.contains("pinterest")) {
                        activityName = "Pinterest";
                        activityType = "Pinterest";
                    } else if (appName.contains("snap")) {
                        activityName = "Snapchat";
                        activityType = "Snapchat";
                    }
                    if (!BuildConfig.DEBUG) {
                        Answers.getInstance().logShare(new ShareEvent()
                                .putMethod("" + activityName)
                                .putContentName("" + mProductName)
                                .putContentType("" + activityType)
                                .putContentId("" + mSku));
                    }
                }


                // Now you know the app being picked.
                // data is a copy of your launchIntent with this important extra info added.

                // Start the selected activity
                startActivity(data);
            }
        }


    }

    public void setDrawerGravity() {
        LogUtils.e("", "Gravity Language::" + getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, ""));
        Utils.setLocale(getActivity());

        if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

            DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) nvSidebar.getLayoutParams();
            params.gravity = Gravity.END;
            nvSidebar.setLayoutParams(params);

            tvSidebarProducts.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_home_selector, 0);
            tvSidebarSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_products_selector, 0);
//            tvSidebarShoppingBag.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_shopping_bags_selector, 0);
//            tvSidebarWishlist.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_wishlist_selector, 0);
            tvSidebarMyShy7lo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_myshy7lo_selector, 0);
//            itemBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            lnrSidebarShoppingBag.setScaleX(-1f);
            lnrSidebarWishlist.setScaleX(-1f);
            tvShoppingBagCounter.setScaleX(-1f);
            tvWishlistCounter.setScaleX(-1f);
            tvSidebarShoppingBag.setScaleX(-1f);
            tvSidebarWishlist.setScaleX(-1f);
        } else {

            DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) nvSidebar.getLayoutParams();
            params.gravity = Gravity.START;
            nvSidebar.setLayoutParams(params);

            tvSidebarProducts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_home_selector, 0, 0, 0);
            tvSidebarSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_products_selector, 0, 0, 0);
//            tvSidebarShoppingBag.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_shopping_bags_selector, 0, 0, 0);
//            tvSidebarWishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_wishlist_selector, 0, 0, 0);
            tvSidebarMyShy7lo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_myshy7lo_selector, 0, 0, 0);
//            itemBadgeView.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
            lnrSidebarShoppingBag.setScaleX(1f);
            lnrSidebarWishlist.setScaleX(1f);
            tvShoppingBagCounter.setScaleX(1f);
            tvWishlistCounter.setScaleX(1f);
            tvSidebarShoppingBag.setScaleX(1f);
            tvSidebarWishlist.setScaleX(1f);
        }

        tvSidebarProducts.setText(getString(R.string.shy7lo_homes));
        tvSidebarSearch.setText(getString(R.string.category));
        tvSidebarShoppingBag.setText(getString(R.string.my_bag));
        tvSidebarWishlist.setText(getString(R.string.wishlist));
        tvSidebarMyShy7lo.setText(getString(R.string.account));
//        tvSidebarHelpFAQ.setText(getString(R.string.help_faqs));
//        tvSidebarCustomers.setText(getString(R.string.for_customers));
        mHelpAdapter.setData(Shy7lo.mCMSPage);
        tvSidebarLiveChat.setText(getString(R.string.live_chat));
        tvSidebarAppInfo.setText(getString(R.string.app_info));
        tvSidebarSignOut.setText(getString(R.string.sign_out));
        updateBadgetCount();
        updateWishListBadgetCount();
    }

//
//
//    @Override
//    public void onPaymentRequestResponse(int responseType, PayFortData responseData) {
//        LogUtils.e("", "onPaymentRequestResponse call");
//        if (responseType == PayFortPayment.RESPONSE_GET_TOKEN) {
////            Toast.makeText(getActivity(), "Token not generated", Toast.LENGTH_SHORT).show();
//            Log.e("onPaymentResponse", "Token not generated");
//        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_CANCEL) {
//            Toast.makeText(getActivity(), "Payment cancelled", Toast.LENGTH_SHORT).show();
//            Log.e("onPaymentResponse", "Payment cancelled");
//        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_FAILURE) {
//            Toast.makeText(getActivity(), "Payment failed", Toast.LENGTH_SHORT).show();
//            Log.e("onPaymentResponse", "Payment failed");
//        } else {
//            Toast.makeText(getActivity(), "Payment successful", Toast.LENGTH_SHORT).show();
//            Log.e("onPaymentResponse", "Payment successful");
//
//            Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_order_successful));
//            MyPref.setPref(getActivity(), MyPref.GUEST_CART_ID, "");
//
//            getGuestCartToken();
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mLanguageChangeReceiver != null) {
//            unregisterReceiver(mLanguageChangeReceiver);
//        }
        if (mSessionExpireReceiver != null) {
            unregisterReceiver(mSessionExpireReceiver);
        }

        if (mWishListReceiver != null) {
            unregisterReceiver(mWishListReceiver);
        }
        if (mShoppingBagListReceiver != null) {
            unregisterReceiver(mShoppingBagListReceiver);
        }


    }

    public void showWishListMsg(String msg) {
        if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            tvWishlistText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add_wishlist, 0);
        } else {
            tvWishlistText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_wishlist, 0, 0, 0);
        }

        tvWishlistText.setText("" + msg);
        tvWishlistText.setVisibility(View.VISIBLE);
        tvWishlistText.startAnimation(animOpen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideWishListMsg();
            }
        }, 1000);
    }

    private void hideWishListMsg() {
        tvWishlistText.startAnimation(animClose);
        animClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvWishlistText.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private BroadcastReceiver mSessionExpireReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//
//            MyShy7loFragment fragment = (MyShy7loFragment) getSupportFragmentManager().findFragmentByTag(MyShy7loFragment.TAG_MYSHY7LO_FRAGMENT);
//            if (fragment != null) {
//                fragment.setView();
//            }

        }
    };

    private BroadcastReceiver mWishListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            LogUtils.e("", "mWishListReceiver call");

            WishlistFragment fragment = (WishlistFragment) getSupportFragmentManager().findFragmentByTag(WishlistFragment.TAG_WISHLIST_FRAGMENT);
            if (fragment != null) {
                LogUtils.e("", "mWishListReceiver Data Refreshed");
                fragment.getWishListFromDB();

            }

        }
    };

    private BroadcastReceiver mShoppingBagListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            LogUtils.e("", "mShoppingBagListReceiver call");

//            ShoppingBagsFragment fragment = (ShoppingBagsFragment) getSupportFragmentManager().findFragmentByTag(ShoppingBagsFragment.TAG_SHOPPING_BAGS_FRAGMENT);
//            if (fragment != null) {
//                LogUtils.e("", "mShoppingBagListReceiver Data Refreshed");
//                Bundle bundle = intent.getExtras();
//                if (bundle != null && bundle.containsKey(Constant.BNDL_IS_SYNC_FINISHED)) {
//                    fragment.hideSyncIndicator();
//                } else {
//                    fragment.getShoppingListFromDB();
//                }
//
//            }

        }
    };

//    private BroadcastReceiver mLanguageChangeReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            LogUtils.e("", "Home mLanguageChangeReceiver called");
//
//            MyShy7loFragment fragment = (MyShy7loFragment) getSupportFragmentManager().findFragmentByTag(MyShy7loFragment.TAG_MYSHY7LO_FRAGMENT);
//            if (fragment != null) {
//                fragment.setLanguage();
//                fragment.setCountry();
//                fragment.setSignoutView();
//            }
//
//            setDrawerGravity();
//            getCategoryList();
//
//            setBackChangeSideBar();
//
//        }
//    };

    public void shareProduct(String name, String image, String sku) {
        try {

            this.mProductName = name;
            this.mSku = sku;
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            // Set title and text to share when the user selects an option.
            shareIntent.putExtra(Intent.EXTRA_TEXT, "" + name + "\n" + image);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, name);
            List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(shareIntent, 0);
            if (!resInfo.isEmpty()) {
                for (ResolveInfo info : resInfo) {
                    Intent targetedShare = new Intent(Intent.ACTION_SEND);
                    targetedShare.setType("text/plain"); // put here your mime type
                    targetedShare.setPackage(info.activityInfo.packageName.toLowerCase());
                    targetedShareIntents.add(targetedShare);
                }
                // Then show the ACTION_PICK_ACTIVITY to let the user select it
                Intent intentPick = new Intent();
                intentPick.setAction(Intent.ACTION_PICK_ACTIVITY);
                // Set the title of the dialog
                intentPick.putExtra(Intent.EXTRA_TITLE, name);
                intentPick.putExtra(Intent.EXTRA_INTENT, shareIntent);
                intentPick.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray());
                // Call StartActivityForResult so we can get the app mProductName selected by the user
                startActivityForResult(intentPick, RC_SHARE_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // logout

    private void getGuestCartToken() {

//        if (!Utils.isInternetConnected(getActivity())) {
//            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
//                @Override
//                public void onRetryClicked(Dialog dialog) {
//                    if (Utils.isInternetConnected(getActivity())) {
//                        if (dialog != null && dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        getGuestCartToken();
//                    }
//                }
//            });
//            return;
//        }
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
//                                    MyPref.setPref(getActivity(), MyPref.GUEST_CART_ID, token);
//                                }
//                            }

        MyPref.setPref(getActivity(), MyPref.USER_CART_ID, "");
        MyPref.setPref(getActivity(), CART_ITEM_COUNT, 0);
        MyPref.setPref(getActivity(), MyPref.USER_ID, "");
        MyPref.setPref(getActivity(), MyPref.USER_FIRSTNAME, "");
        MyPref.setPref(getActivity(), MyPref.USER_LASTNAME, "");
        MyPref.setPref(getActivity(), MyPref.USER_PHONE, "");
        MyPref.setPref(getActivity(), MyPref.USER_ADDRESS, "");
        MyPref.setPref(getActivity(), MyPref.USER_CITY, "");

//                            MyPref.setPref(getActivity(), MyPref.TOTAL_AMT_LIST, "");
        dbAdapter.removeAllWishItem();
//                            dbAdapter.removeAllShoppingItem();

        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.updateBadgetCount();
            activity.updateWishListBadgetCount();
        }

        setSignoutView();

        AccountFragment fragment = (AccountFragment) getSupportFragmentManager().findFragmentByTag(AccountFragment.TAG_ACCOUNT_FRAGMENT);
        if (fragment != null) {
            fragment.setView();
        }

//                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
//                            Utils.showInitialScreen(getActivity());
//                            return;
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

    }

    public void setSignoutView() {

        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        String userId = MyPref.getPref(getActivity(), MyPref.USER_ID, "");

        if (!TextUtils.isEmpty(userToken) && !TextUtils.isEmpty(userId)) {

            tvSidebarSignOut.setVisibility(View.VISIBLE);

        } else {
            tvSidebarSignOut.setVisibility(View.GONE);
        }

    }

}
