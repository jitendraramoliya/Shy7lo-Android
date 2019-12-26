package shy7lo.com.shy7lo.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.plugin.AdjustCriteo;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.adapter.ProductsBaseAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.ProductList;
import shy7lo.com.shy7lo.model.SortingList;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;
import shy7lo.com.shy7lo.widget.HeaderGridView;

/**
 * Created by Jiten on 06-08-2017.
 */

public class ProductCategoryFragment extends Fragment {

    public static String TAG_PRODUCT_CATEGORY_FRAGMENT = "ProductCategoryFragment";

    View mView;


    @BindView(R.id.tvChangeFilters)
    TextView tvChangeFilters;
    @BindView(R.id.lnrNothingFound)
    LinearLayout lnrNothingFound;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;

    @BindView(R.id.lnrProgressBar)
    LinearLayout lnrProgressBar;

    @BindView(R.id.gvProductItems)
    HeaderGridView gvProductItems;
    ImageView ivTop;
    ProductsBaseAdapter mProductsBaseAdapter;

    public static String BNDL_PRODUCT_CATEGORY = "BNDL_PRODUCT_CATEGORY";
    public static String BNDL_PRODUCT_LIST = "BNDL_PRODUCT_LIST";
    public static String BNDL_CATEGORY_ID = "BNDL_CATEGORY_ID";
    public static String BNDL_URLQUERY_POSTFIX = "BNDL_URLQUERY_POSTFIX";
    public static String BNDL_CATEGORY_NAME = "BNDL_CATEGORY_NAME";
    public static String BNDL_SORT_BY = "BNDL_SORT_BY";
    public static String BNDL_DIRECTION = "BNDL_DIRECTION";
    public static String BNDL_VIEW_TYPE = "BNDL_VIEW_TYPE";
    public static String BNDL_PRICE = "BNDL_PRICE";
    public static String BNDL_FILTER_OBJECT = "BNDL_FILTER_OBJECT";

    //    ProductCategoryList.ChildCategory mChildCategory;
//    ProductList.ChildCategory mChildCategory;

    private boolean isLastPage = false;
    private boolean isLoading = false, isLoadingMore = false;
    public static int PAGE_SIZE = 1;

    private String mUrlQueryPostFix = "";

    private final String SORT_BY_DEFAULT = "created_at", DIRECTION_DEFAULT = "ASC", VIEWTYPE_DEFAULT = "grid";

    private String child_category = "", searchCriteria = "", category_id = "", category_name = "", sortby = SORT_BY_DEFAULT, direction = DIRECTION_DEFAULT, viewType = VIEWTYPE_DEFAULT, recievedDirection = "", price = "";
    private JSONObject jFilterObject;

    private int page_number = 1;
    //    private int page_number = 0;
    ArrayList<String> mPageNumberList = new ArrayList<>();

    List<ProductList.ProductInfo> productItemList = new ArrayList<>();
    List<SortingList.FilterData> mFilterItemList = new ArrayList<>();

    int firstVisibleItemPosition = 0;

    private boolean isViewShown = false;
    private boolean isVisibleToUser = false;

    public static String WISHLIST_CHANGE_EXPIRE = "WISHLIST_CHANGE_EXPIRE";
//    ProductList.Category mCategory;


    static ProductCategoryFragment mProductCategoryFragment;


    public static ProductCategoryFragment getInstance() {
        if (mProductCategoryFragment == null) {
            mProductCategoryFragment = new ProductCategoryFragment();
        }
        return mProductCategoryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Context mContext;

    private Context getMyActivity() {
        return mContext;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_category_product, container, false);
        mContext = getActivity();
        Utils.setLocale(getActivity());
        ButterKnife.bind(this, mView);

        InitializeControls();


        return mView;
    }

    private void InitializeControls() {

        getActivity().registerReceiver(mWishListChangedReceiver, new IntentFilter(WISHLIST_CHANGE_EXPIRE));

        View mHeader = LayoutInflater.from(getActivity()).inflate(R.layout.view_header_banner_top, null);
        ivTop = mHeader.findViewById(R.id.ivTop);
        gvProductItems.addHeaderView(mHeader);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            ivTop.setScaleX(-1f);
        } else {
            ivTop.setScaleX(1f);
        }


        mProductsBaseAdapter = new ProductsBaseAdapter(getActivity(), R.layout.grid_item_products, productItemList, gvProductItems);
        gvProductItems.setAdapter(mProductsBaseAdapter);


        gvProductItems.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstVisibleItemPosition = firstVisibleItem;
                totalItemCount = totalItemCount - 2;
//                LogUtils.e("", "firstVisibleItem::" + firstVisibleItem);
//                LogUtils.e("", "visibleItemCount::" + visibleItemCount);
//                LogUtils.e("", "totalItemCount::" + totalItemCount);

                if (productItemList.size() > 1) {
                    page_number = productItemList.size() / 16;
                    if (page_number < 1) {
                        page_number = 1;
                    }
//                    page_number = (productItemList.size() / 16) - 1;
//                    if (page_number < 0) {
//                        page_number = 0;
//                    }
                }


//                int boundCount = (16 * (page_number - 1)) + 1;
////                LogUtils.e("", "firstVisibleItem::" + firstVisibleItem + " boundCount::" + boundCount + " page_number::" + page_number);
////                if (page_number > 1) {
//                if (page_number > 0) {
//                    if (firstVisibleItem >= boundCount && boundCount > 16) {
//                        if (!isLoading && !isLastPage) {
////                        if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {
//
//                            isLoading = true;
//                            page_number = page_number + 1;
////                            LogUtils.e("", "Load New more::" + page_number);
//                            getProductNextPageView(false, page_number);
////                        }
//                        }
//                    }
//                if (!isLoading && !isLastPage) {

                LogUtils.e("", "isLoadingMore::" + isLoadingMore + " isLastPage::" + isLastPage + " totalItemCount::" + totalItemCount + " " + (firstVisibleItem + visibleItemCount));
                if (!isLoadingMore && !isLastPage) {
                    if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {

                        isLoadingMore = true;
                        page_number = page_number + 1;
//                        LogUtils.e("", "Load more page_number::" + page_number);
                        getProductNextLoadMorePageView(true, page_number);
                    }
                }


            }
        });


        if (!isViewShown) {
            LogUtils.e("", "Main call");
            getInitialData(isVisibleToUser);
        }

        tvChangeFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsItemsFragment fragment = (ProductsItemsFragment) getActivity().getSupportFragmentManager()
                        .findFragmentByTag(ProductsItemsFragment.TAG_PRODUCT_ITEMS_FRAGMENT);
                fragment.ibOptionMore.performClick();
            }
        });


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (getView() != null) {
            isViewShown = true;
            LogUtils.e("", "Visit call");
            getInitialData(isVisibleToUser);
        } else {
            isViewShown = false;
        }

//        if (isVisibleToUser && !isViewShown) {
//            if (productItemList == null || productItemList.size() == 0) {
//                showProductView();
//            }
//
//            if (mFilterItemList == null || mFilterItemList.size() == 0) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        getFilterData();
//                    }
//                }, 10);
//            }
//        }

    }

    private void getInitialData(boolean isVisibleToUser) {
        Bundle extras = getArguments();
        if (extras != null) {
            mUrlQueryPostFix = "" + extras.getString(BNDL_URLQUERY_POSTFIX);
            category_id = "" + extras.getString(BNDL_CATEGORY_ID);
            child_category = "" + extras.getString(BNDL_CATEGORY_NAME);
            category_name = child_category;
            sortby = extras.getString(BNDL_SORT_BY);
            direction = extras.getString(BNDL_DIRECTION);
            viewType = extras.getString(BNDL_VIEW_TYPE);
            price = extras.getString(BNDL_PRICE);
            LogUtils.e("", "category viewType::" + viewType);
            LogUtils.e("", "category category_id::" + category_id);
            LogUtils.e("", "category category_name::" + category_name);
            LogUtils.e("", "category mUrlQueryPostFix::" + mUrlQueryPostFix);

            try {
                jFilterObject = new JSONObject(extras.getString(BNDL_FILTER_OBJECT));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            LogUtils.e("", "category jFilterObject::" + jFilterObject);

            ProductList.CategoryBanner category_banner;
            if (extras.containsKey(BNDL_PRODUCT_LIST)) {
                String product_list = extras.getString(BNDL_PRODUCT_LIST);
                ProductList.Data mData = new Gson().fromJson(product_list, ProductList.Data.class);
                productItemList = mData.getProductInfoData();
                category_banner = mData.category_banner;

                if (mPageNumberList != null && mPageNumberList.size() > 0) {
                    mPageNumberList.clear();
                    mPageNumberList.add("" + page_number);
                    LogUtils.e("", "productItemList size:" + productItemList.size());
                } else {
                    LogUtils.e("", "productItemList is null");
                }

//                if (mCategory != null && !TextUtils.isEmpty(mCategory.id)) {
//
//                }
//                ProductList.Data mData = new Gson().fromJson(product_list, ProductList.Data.class);
                if (mData != null && mData.getTotalCount() != null) {

                    PAGE_SIZE = mData.getTotalCount();
                }

//                    PAGE_SIZE = new Gson().fromJson(product_list, ProductList.Data.class).getTotalCount();


                if (productItemList.size() % 16 == 0 && productItemList.size() < PAGE_SIZE) {
                    isLastPage = false;
                } else {
                    isLastPage = true;
                }

                setBanner(category_banner);

                if (mFilterItemList == null || mFilterItemList.size() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getFilterData();
                        }
                    }, 10);
                }

                if (isVisibleToUser) {

//                            if (productList.data.getTotalCount())


//                            if (productItemList != null && productItemList.size() > 0) {
//                                if (mAdapter == null) {
////                                    LogUtils.e("", "mAdapter is null");
//                                    setView();
//                                } else {
//                                    LogUtils.e("", "mAdapter is not null");
//                                    mAdapter.changeData(productItemList);
//                                    rvProductItems.setAdapter(mAdapter);
////                                    mAdapter.notifyDataSetChanged();
//                                }
                    if (mProductsBaseAdapter != null && productItemList != null && productItemList.size() > 0) {
                        mProductsBaseAdapter.changeData(productItemList);
                    }

                    LogUtils.e("", "viewType::" + viewType);
                    if (viewType.equalsIgnoreCase("list")) {
                        if (gvProductItems != null) {
                            gvProductItems.setNumColumns(1);
                        }
                    } else {
                        if (gvProductItems != null) {
                            gvProductItems.setNumColumns(2);
                        }
                    }

                    if (productItemList == null || productItemList.size() == 0) {
//                                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                                    Utils.showToast(getActivity(), "لم يتم العثور على نتائج مطابقة للبحث");
//                                } else {
//                                    Utils.showToast(getActivity(), "No result found");
//                                }
                        lnrNothingFound.setVisibility(View.VISIBLE);
//                                Utils.showToast(getActivity(), "" + getString(R.string.msg_no_result));
                    } else {
                        if (!isLoading && !isLastPage) {
//                                    if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {
                            LogUtils.e("", "Load more");
                            isLoading = true;
                            page_number = page_number + 1;
                            getProductNextPageView(false, page_number);
//                                    }
                        }
                    }


                }

                return;
            }

        }


        if (isVisibleToUser) {
            if (productItemList == null || productItemList.size() == 0) {
                showProductView();
            }

            if (mFilterItemList == null || mFilterItemList.size() == 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFilterData();
                    }
                }, 10);
            }


        }
    }

    private void setBanner(final ProductList.CategoryBanner category_banner) {

        if (category_banner != null) {
            LogUtils.e("", "productList.data.category_banner::" + category_banner.image);
            if (ivTop != null && !TextUtils.isEmpty(category_banner.image)) {
                LogUtils.e("", "ivTop is not null");
                ivTop.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(category_banner.image).into(ivTop);
            } else {
                LogUtils.e("", "ivTop is null");
                ivTop.setVisibility(View.GONE);
            }
        }

        ivTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (category_banner != null && !TextUtils.isEmpty(category_banner.url)) {

//                    String mMainCategoryName = "";
//                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                        mMainCategoryName = mBaseScreen.titleAr;
//                    } else {
//                        mMainCategoryName = mBaseScreen.titleEn;
//                    }

                    String mSearchCriteria = category_banner.url;
                    Bundle bundle = new Bundle();
                    bundle.putString(ProductsItemsFragment.BK_PRODUCT_ITEMS, mSearchCriteria);
                    bundle.putString(ProductsItemsFragment.BNDL_CATEGORY, "" + category_name);
                    bundle.putString(ProductsItemsFragment.BNDL_MAIN_CATE_ID, "");
                    bundle.putBoolean(ProductsItemsFragment.IS_FROM_BANNER, true);

                    HomeActivity activity = (HomeActivity) getActivity();
                    if (activity != null) {
                        activity.loadBannerItems(bundle);
                    }

                }
            }
        });

    }

    private void showProductView() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        showProductView();

                        if (mFilterItemList == null || mFilterItemList.size() == 0) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getFilterData();
                                }
                            }, 10);
                        }
                    }
                }
            });
            return;
        }

        mPageNumberList = new ArrayList<>();

        LogUtils.e("", "getProductItemsList call:");
        Utils.showProgressDialog(getActivity());

        if (productItemList != null && productItemList.size() > 0) {
            productItemList.clear();
        }

        page_number = 1;
//        page_number = 0;

        Map<String, String> jsonParams = new ArrayMap<>();
//        mUrlQueryPostFix = "";
        if (!TextUtils.isEmpty(mUrlQueryPostFix)) {
            String[] param = mUrlQueryPostFix.split("&");
            for (int i = 0; i < param.length; i++) {
                String[] paramValue = param[i].split("=");
                if (paramValue.length > 1) {
                    jsonParams.put(paramValue[0], paramValue[1]);
                }
            }
            jsonParams.put("page", "" + page_number);
        } else {
            jsonParams.put("category_id", category_id);
            jsonParams.put("sort_by", sortby);
            jsonParams.put("direction", direction);
            jsonParams.put("page", "" + page_number);
            if (!TextUtils.isEmpty(price)) {
                jsonParams.put("price", "" + price);
            }
//        jsonParams.put("catIndex", "1");
//        jsonParams.put("filter", jFilterObject.toString());
            if (jFilterObject != null && jFilterObject.length() > 0) {
                Iterator<String> iterator = jFilterObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    try {
                        jsonParams.put("filters[" + key + "]", "" + jFilterObject.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());


        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<ProductList> productListCall = serviceAPI.getProductsList(Shy7lo.mLangCode, jsonParams);

        productListCall.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {

                lnrNothingFound.setVisibility(View.GONE);
                isLoading = false;

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    ProductList productList = response.body();

                    if (productList != null && productList.success.equals("1")) {
                        if (productList != null && productList.data.getProductInfoData().size() > 0) {

                            category_name = productList.data.getCategoryName();

                            productItemList = productList.data.getProductInfoData();

                            if (mPageNumberList != null && mPageNumberList.size() > 0) {
                                mPageNumberList.clear();
                                mPageNumberList.add("" + page_number);
                            }


                            PAGE_SIZE = productList.data.getTotalCount();

//                            if (productList.data.getTotalCount())

                            LogUtils.e("", "viewType::" + viewType);
                            if (viewType.equalsIgnoreCase("list")) {
                                if (gvProductItems != null) {
                                    gvProductItems.setNumColumns(1);
                                }
                            } else {
                                if (gvProductItems != null) {
                                    gvProductItems.setNumColumns(2);
                                }
                            }


                            if (productItemList.size() % 16 == 0 && productItemList.size() < PAGE_SIZE) {
                                isLastPage = false;
                            } else {
                                isLastPage = true;
                            }

//                            if (productItemList != null && productItemList.size() > 0) {
//                                if (mAdapter == null) {
////                                    LogUtils.e("", "mAdapter is null");
//                                    setView();
//                                } else {
//                                    LogUtils.e("", "mAdapter is not null");
//                                    mAdapter.changeData(productItemList);
//                                    rvProductItems.setAdapter(mAdapter);
////                                    mAdapter.notifyDataSetChanged();
//                                }
                            mProductsBaseAdapter.changeData(productItemList);

                            setBanner(productList.data.category_banner);

                            if (productItemList == null || productItemList.size() == 0) {
//                                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                                    Utils.showToast(getActivity(), "لم يتم العثور على نتائج مطابقة للبحث");
//                                } else {
//                                    Utils.showToast(getActivity(), "No result found");
//                                }
                                lnrNothingFound.setVisibility(View.VISIBLE);
//                                Utils.showToast(getActivity(), "" + getString(R.string.msg_no_result));
                            } else {
                                if (!isLoading && !isLastPage) {
//                                    if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {
                                    LogUtils.e("", "Load more");
                                    isLoading = true;
                                    page_number = page_number + 1;
                                    getProductNextPageView(false, page_number);
//                                    }
                                }
                            }

                            try {
                                AdjustEvent event = new AdjustEvent("9dnoop");
                                event.addPartnerParameter("Category ID", "" + "" + category_id);
                                //Callback
                                event.addCallbackParameter("Category ID", "" + "" + category_id);

                                String mCriteo = MyPref.getPref(getActivity(), MyPref.APP_CRITEO, "");
                                if (TextUtils.isEmpty(mCriteo) || mCriteo.equalsIgnoreCase("1")) {
                                    String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
                                    AdjustCriteo.injectCustomerIdIntoCriteoEvents(TextUtils.isEmpty(userToken) ? userToken : MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""));
                                }

                                Adjust.trackEvent(event);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            }
                        }
                    } else if (productList != null && productList.success.equals("2")) {
                        Utils.showInitialScreen(getActivity());
                        return;
                    } else {
//                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            Utils.showToast(getActivity(), "خطأ");
//                        } else {
//                            Utils.showToast(getActivity(), "Something went wrong");
//                        }
                        page_number = 1;
//                        page_number = 0;
                        Utils.showAlertDialog(getMyActivity(), "" + response.code());
                    }

                } else {
                    LogUtils.e(TAG_PRODUCT_CATEGORY_FRAGMENT, "Response is not great");
//                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                        Utils.showToast(getActivity(), "خطأ");
//                    } else {
//                        Utils.showToast(getActivity(), "Something went wrong");
//                    }

                    page_number = 1;
//                    page_number = 0;
//                    Utils.showAlertDialog(getMyActivity(), "" + response.code());
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
            public void onFailure(Call<ProductList> call, Throwable t) {
                System.out.println(t.getMessage());
                Utils.closeProgressDialog();
                if (MyPref.getPref(getMyActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    Utils.showToast(getMyActivity(), "خطأ");
                } else {
                    Utils.showToast(getMyActivity(), "Something went wrong");
                }
                isLoading = false;
                page_number = 1;
//                page_number = 0;
            }
        });

    }

    private void getProductNextPageView(boolean isLoadingShow, final int page_number) {
        LogUtils.e(TAG_PRODUCT_CATEGORY_FRAGMENT, "getProductNextPageView call:");
//        Utils.showProgressDialog(getActivity());
        if (isLoadingShow) {
            lnrProgressBar.setVisibility(View.VISIBLE);
        }

        Map<String, String> jsonParams = new ArrayMap<>();
        if (!TextUtils.isEmpty(mUrlQueryPostFix)) {
            String[] param = mUrlQueryPostFix.split("&");
            for (int i = 0; i < param.length; i++) {
                String[] paramValue = param[i].split("=");
                if (paramValue.length > 1) {
                    jsonParams.put(paramValue[0], paramValue[1]);
                }
            }
            jsonParams.put("page", "" + page_number);
        } else {
            jsonParams.put("category_id", category_id);
            jsonParams.put("sort_by", sortby);
            jsonParams.put("direction", direction);
            jsonParams.put("page", "" + page_number);
            if (!TextUtils.isEmpty(price)) {
                jsonParams.put("price", "" + price);
            }
//        jsonParams.put("catIndex", "1");
//        jsonParams.put("filter", jFilterObject.toString());
            if (jFilterObject != null && jFilterObject.length() > 0) {
                Iterator<String> iterator = jFilterObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    try {
                        jsonParams.put("filters[" + key + "]", "" + jFilterObject.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());


        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<ProductList> productListCall = serviceAPI.getProductsList(Shy7lo.mLangCode, jsonParams);

        productListCall.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {

                isLoading = false;
                isLoadingMore = false;
                lnrProgressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    ProductList productList = response.body();

                    if (productList != null && productList.success.equals("1")) {
                        if (productList != null) {

                            LogUtils.e("", page_number + " page_number Contains" + mPageNumberList.contains("" + page_number));
                            if (!mPageNumberList.contains("" + page_number)) {
                                productItemList.addAll(productList.data.getProductInfoData());
                                mPageNumberList.add("" + page_number);
                            }

//                        sortingItemList = productList.data.getSortingData();
//                        mFilterItemList = productList.data.getFilterData();

//                            if (productList.data.getTotalCount())

                            if (productItemList.size() % 16 == 0 && productItemList.size() < PAGE_SIZE) {
                                isLastPage = false;
                            } else {
                                isLastPage = true;
                            }

//                            if (productItemList != null && productItemList.size() > 0) {
//                                if (mAdapter == null) {
//                                    LogUtils.e("", "mAdapter is null");
//                                    setView();
//                                } else {
//                                    LogUtils.e("", "mAdapter is not null");
//                                    mAdapter.changeData(productItemList);
////                                    rvProductItems.setAdapter(mAdapter);
////                                    mAdapter.notifyDataSetChanged();
//                                }
                            mProductsBaseAdapter.changeData(productItemList);
//                            }
                        }
                    } else if (productList != null && productList.success.equals("2")) {
                        Utils.showInitialScreen(getActivity());
                        return;
                    } else {
//                        Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
//                        page_number = page_number - 1;
                    }

                } else {
                    LogUtils.e(TAG_PRODUCT_CATEGORY_FRAGMENT, "Response is not great");
//                    Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
//                    page_number = page_number - 1;
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
//                Utils.closeProgressDialog();
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                System.out.println(t.getMessage());
//                Utils.closeProgressDialog();
//                lnrProgressBar.setVisibility(View.GONE);
//                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
                isLoading = false;
//                page_number = page_number - 1;
            }
        });

    }

    private void getProductNextLoadMorePageView(boolean isLoadingShow, final int page_number) {
        LogUtils.e(TAG_PRODUCT_CATEGORY_FRAGMENT, "getProductNextLoadMorePageView call:");
//        Utils.showProgressDialog(getActivity());
        if (isLoadingShow) {
            lnrProgressBar.setVisibility(View.VISIBLE);
        }

        Map<String, String> jsonParams = new ArrayMap<>();
        if (!TextUtils.isEmpty(mUrlQueryPostFix)) {
            String[] param = mUrlQueryPostFix.split("&");
            for (int i = 0; i < param.length; i++) {
                String[] paramValue = param[i].split("=");
                if (paramValue.length > 1) {
                    jsonParams.put(paramValue[0], paramValue[1]);
                }
            }
            jsonParams.put("page", "" + page_number);
        } else {
            jsonParams.put("category_id", category_id);
            jsonParams.put("sort_by", sortby);
            jsonParams.put("direction", direction);
//        jsonParams.put("page_number", this.page_number);
            jsonParams.put("page", "" + page_number);
            jsonParams.put("catIndex", "1");
            if (!TextUtils.isEmpty(price)) {
                jsonParams.put("price", "" + price);
            }
            if (jFilterObject != null && jFilterObject.length() > 0) {
                Iterator<String> iterator = jFilterObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    try {
                        jsonParams.put("filters[" + key + "]", "" + jFilterObject.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());


        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<ProductList> productListCall = serviceAPI.getProductsList(Shy7lo.mLangCode, jsonParams);

        productListCall.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {

                isLoading = false;
                isLoadingMore = false;
                lnrProgressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    ProductList productList = response.body();

                    if (productList != null && productList.success.equals("1")) {
                        if (productList != null) {

                            LogUtils.e("getProductNextLoadMorePageView", page_number + " page_number Contains" + mPageNumberList.contains("" + page_number));
                            if (!mPageNumberList.contains("" + page_number)) {
                                productItemList.addAll(productList.data.getProductInfoData());
                                mPageNumberList.add("" + page_number);
                            }
//                        sortingItemList = productList.data.getSortingData();
//                        mFilterItemList = productList.data.getFilterData();

//                            if (productList.data.getTotalCount())

                            if (productItemList.size() % 16 == 0 && productItemList.size() < PAGE_SIZE) {
                                isLastPage = false;
                            } else {
                                isLastPage = true;
                            }

//                            if (productItemList != null && productItemList.size() > 0) {
//                                if (mAdapter == null) {
//                                    LogUtils.e("", "mAdapter is null");
//                                    setView();
//                                } else {
//                                    LogUtils.e("", "mAdapter is not null");
//                                    mAdapter.changeData(productItemList);
////                                    rvProductItems.setAdapter(mAdapter);
////                                    mAdapter.notifyDataSetChanged();
//                                }
                            mProductsBaseAdapter.changeData(productItemList);
//                            }
                        }
                    } else if (productList != null && productList.success.equals("2")) {
                        Utils.showInitialScreen(getActivity());
                        return;
                    } else {
//                        Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
//                        page_number = page_number - 1;
                    }

                } else {
                    LogUtils.e(TAG_PRODUCT_CATEGORY_FRAGMENT, "Response is not great");
//                    Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
//                    page_number = page_number - 1;
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
//                Utils.closeProgressDialog();
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                System.out.println(t.getMessage());
//                Utils.closeProgressDialog();
                lnrProgressBar.setVisibility(View.GONE);
//                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
                isLoadingMore = false;
//                page_number = page_number - 1;
            }
        });

    }

    public List<SortingList.FilterData> getCategoryFilterData() {
        return mFilterItemList;
    }

    public String getCategory() {
        return category_id;
    }

    public String getCategoryName() {
        return category_name;
    }

    private void getFilterData() {

        if (mFilterItemList != null && mFilterItemList.size() > 0) {
            mFilterItemList.clear();
        }

        Map<String, Object> jsonParams = new ArrayMap<>();
        if (!TextUtils.isEmpty(mUrlQueryPostFix)) {
            String[] param = mUrlQueryPostFix.split("&");
            for (int i = 0; i < param.length; i++) {
                String[] paramValue = param[i].split("=");
                if (paramValue.length > 1) {
                    jsonParams.put(paramValue[0], paramValue[1]);
                }
            }
            jsonParams.put("page", 1);
        } else {
            jsonParams.put("category_id", category_id);
            jsonParams.put("sort_by", sortby);
            jsonParams.put("direction", direction);
            jsonParams.put("page", 1);
            if (!TextUtils.isEmpty(price)) {
                jsonParams.put("price", "" + price);
            }
//        jsonParams.put("filterdata", jFilterObject);
            if (jFilterObject != null && jFilterObject.length() > 0) {
                Iterator<String> iterator = jFilterObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    try {
                        jsonParams.put("filters[" + key + "]", "" + jFilterObject.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());


        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<SortingList> productListCall = serviceAPI.getSortingList(Shy7lo.mLangCode, jsonParams);

        productListCall.enqueue(new Callback<SortingList>() {
            @Override
            public void onResponse(Call<SortingList> call, Response<SortingList> response) {


                if (response.isSuccessful() && response.body() != null) {

                    SortingList sortingList = response.body();

                    if (sortingList != null && sortingList.success.equals("1")) {
                        if (sortingList != null) {

//                            sortingItemList = sortingList.data.getSortingData();
                            mFilterItemList = sortingList.data.getFilterData();

                            if (mFilterItemList != null && mFilterItemList.size() > 0) {
                                for (int i = 0; i < mFilterItemList.size(); i++) {
                                    SortingList.FilterData filterData = mFilterItemList.get(i);
                                    if (filterData != null) {
                                        if (filterData.getOptions() == null || filterData.getOptions().size() == 0) {
                                            mFilterItemList.remove(i);
                                        }
                                    }
                                }

                                Collections.sort(mFilterItemList, new Comparator<SortingList.FilterData>() {
                                    @Override
                                    public int compare(SortingList.FilterData t1, SortingList.FilterData t2) {
                                        return t1.getLabel().compareTo(t2.getLabel());
                                    }

                                });
                            }


                        }
                    } else if (sortingList != null && sortingList.success.equals("2")) {
                        Utils.showInitialScreen(getActivity());
                        return;
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
            public void onFailure(Call<SortingList> call, Throwable t) {
                System.out.println(t.getMessage());
                Utils.showAlertDialog(getMyActivity(), "" + t.getMessage());
            }
        });

    }

    private BroadcastReceiver mWishListChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (mProductsBaseAdapter != null) {
                mProductsBaseAdapter.notifyDataSetChanged();
            }

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mWishListChangedReceiver != null) {
            getActivity().unregisterReceiver(mWishListChangedReceiver);
        }
    }
}
