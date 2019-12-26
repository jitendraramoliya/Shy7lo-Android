package shy7lo.com.shy7lo.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.plugin.AdjustCriteo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.FilterNewActivity;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.adapter.DialogSortingAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.CategoryList;
import shy7lo.com.shy7lo.model.ProductCategoryList;
import shy7lo.com.shy7lo.model.ProductList;
import shy7lo.com.shy7lo.model.SortingList;
import shy7lo.com.shy7lo.model.SortingPojo;
import shy7lo.com.shy7lo.model.SubChildData;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;
import shy7lo.com.shy7lo.widget.CategoryPagerAdapterWithTitle;

import static shy7lo.com.shy7lo.fragment.ProductCategoryFragment.TAG_PRODUCT_CATEGORY_FRAGMENT;
import static shy7lo.com.shy7lo.pref.MyPref.getPref;


/**
 * Created by JITEN-PC on 22-09-2016.
 */

public class ProductsItemsFragment extends Fragment implements View.OnClickListener {

    public static String TAG_PRODUCT_ITEMS_FRAGMENT = "ProductsItemsFragment";

    public static String BK_PRODUCT_ITEMS = "BK_PRODUCT_ITEMS";
    public static String IS_FROM_BANNER = "IS_FROM_BANNER";
    public static String BNDL_CATEGORY = "BNDL_CATEGORY";
    public static String BNDL_MAIN_CATE_ID = "BNDL_MAIN_CATE_ID";
    public static int RC_FILTER_CODE = 1003;

    @BindView(R.id.mTopLayout)
    View mTopLayout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;


//    @BindView(R.id.gvProductItems)
//    GridView gvProductItems;
//    ProductsBaseAdapter mProductsBaseAdapter;


    List<ProductList.ProductInfo> productItemList = new ArrayList<>();
    //    List<SortingList.SortingData> sortingItemList = new ArrayList<>();
    List<SortingList.FilterData> filterItemList = new ArrayList<>();

    private final String SORT_BY_DEFAULT = "created_at", DIRECTION_DEFAULT = "ASC", VIEWTYPE_DEFAULT = "grid";

    String searchCriteria = "", category_id = "", category_name = "", recievedCategoryId = "", sortby = SORT_BY_DEFAULT,
            direction = DIRECTION_DEFAULT, viewType = VIEWTYPE_DEFAULT, recievedDirection = "", recievedCategoryName = "", price = "";
    public static String mMainCateName = "", mMainCateID = "";
    private int page_number = 1;
    //    private int page_number = 0;
    private int mPagerPosition = 0;
    JSONObject jFilterObject;
    private String mUrlQueryPostFix = "";

    @BindView(R.id.ibMore)
    ImageButton ibMore;
    @BindView(R.id.ibSearch)
    ImageButton ibSearch;
    @BindView(R.id.ibSortings)
    ImageButton ibSortings;
    @BindView(R.id.ibOptionMore)
    ImageButton ibOptionMore;
    //    @BindView(R.id.lnrProgressBar)
//    LinearLayout lnrProgressBar;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ibCloseIcons)
    ImageButton ibCloseIcons;
    @BindView(R.id.lnrSearchBar)
    LinearLayout lnrSearchBar;

    Runnable mRunnable;
    Handler mHandler = new Handler();

    boolean isFromBanner;

    //    public BadgeView itemBadgeView;
    private int mCartItemCount;
    private int fiveDp, threeDp;

    private boolean isLastPage = false;
    private boolean isLoading = false, isLoadingMore = false;
    public static int PAGE_SIZE = 1;
    int visibleItemCount = 0;
    int firstVisibleItemPosition = 0;
//    ArrayList<String> mPageNumberList = new ArrayList<>();

    List<ProductCategoryList.ChildCategory> childCategory = new ArrayList<>();
    CategoryPagerAdapterWithTitle mCategoryPagerAdapterWithTitle;

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.tlSlidingTabs)
    TabLayout tlSlidingTabs;

    CategoryList.MainCategoryData mWomenCategoryData, mMenCategoryData, mKidCategoryData;

    View mView, mLine;

    public enum ItemType {
        ListType, GridType
    }

    private ItemType mSelectedItemType = ItemType.GridType;

    static ProductsItemsFragment productsItemsFragment;

    public static ProductsItemsFragment getInstance() {
//        if (mSubCategoryForProductFragment == null) {
        productsItemsFragment = new ProductsItemsFragment();
//        }
        return productsItemsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_product_new_items, container, false);
        Utils.setLocale(getActivity());
        ButterKnife.bind(this, mView);
        InitializeControls();
        InitializeControlsAction();

        LogUtils.e("", "language::" + getResources().getConfiguration().locale);

        return mView;
    }

    private void InitializeControls() {

        mLine = mView.findViewById(R.id.mLine);
        setItemTypeView(mSelectedItemType);
        MyPref.setPref(getActivity(), FilterNewActivity.PREF_CATEGORY_NAME, "");

        ibMore.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));

//        rvProductItems.setItemViewCacheSize(8);
//        rvProductItems.setDrawingCacheEnabled(true);
//        rvProductItems.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

//        mProductsBaseAdapter = new ProductsBaseAdapter(getActivity(), R.layout.grid_item_products, productItemList, gvProductItems);
//        gvProductItems.setAdapter(mProductsBaseAdapter);
//        gvProductItems.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                firstVisibleItemPosition = firstVisibleItem;
////                LogUtils.e("", "firstVisibleItem::" + firstVisibleItem);
////                LogUtils.e("", "visibleItemCount::" + visibleItemCount);
////                LogUtils.e("", "totalItemCount::" + totalItemCount);
//
//                if (productItemList.size() > 1) {
//                    page_number = productItemList.size() / 16;
//                }
//
//                int boundCount = (16 * (page_number - 1)) + 1;
////                LogUtils.e("", "firstVisibleItem::" + firstVisibleItem + " boundCount::" + boundCount + " page_number::" + page_number);
//                if (page_number > 1) {
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
//                }
//
////                if (!isLoading && !isLastPage) {
//                if (!isLoadingMore && !isLastPage) {
//                    if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {
//
//                        isLoadingMore = true;
//                        page_number = page_number + 1;
////                        LogUtils.e("", "Load more page_number::" + page_number);
//                        getProductNextLoadMorePageView(true, page_number);
//                    }
//                }
//
//
//            }
//        });


//        rvProductItems.setNestedScrollingEnabled(false);

//        rvProductItems.addOnScrollListener(recyclerViewOnScrollListener);

        page_number = 1;
//        page_number = 0;

        mUrlQueryPostFix = "";
        // get data

        if (Shy7lo.sortingDataList == null || Shy7lo.sortingDataList.size() == 0) {
            getSortingList(getActivity());
        }

        if (getActivity() instanceof HomeActivity) {

            HomeActivity activity = (HomeActivity) getActivity();
            activity.setDrawerSwipe(true);

        }

        Bundle bundle = getArguments();
        if (bundle != null && TextUtils.isEmpty(searchCriteria)) {
            searchCriteria = bundle.getString(BK_PRODUCT_ITEMS);
            LogUtils.e("", "top main_category_id::" + bundle.getString(BNDL_MAIN_CATE_ID));
            if (bundle.containsKey(BNDL_CATEGORY)) {
                mMainCateName = bundle.getString(BNDL_CATEGORY);
                mMainCateID = bundle.getString(BNDL_MAIN_CATE_ID);
            } else {
                mMainCateName = "";
                mMainCateID = "";
            }
            isFromBanner = bundle.getBoolean(IS_FROM_BANNER);
            LogUtils.e("", "searchCriteria::" + searchCriteria);
            LogUtils.e("", "main_category_id::" + mMainCateID);
            LogUtils.e("", "mMainCateName::" + mMainCateName);

            if (isFromBanner) {
                try {

                    if (searchCriteria.contains("?")) {
                        String postfix = searchCriteria.split("\\?")[1];
                        LogUtils.e("", "postfix::" + postfix);
                        if (!TextUtils.isEmpty(postfix) && postfix.contains("query=true&")) {

                            mUrlQueryPostFix = postfix.replace("query=true&", "");

                            if (!TextUtils.isEmpty(postfix)) {
                                String[] param = mUrlQueryPostFix.split("&");

                                jFilterObject = new JSONObject();
                                for (int i = 0; i < param.length; i++) {
                                    LogUtils.e("", i + " param::" + param[i]);
                                    if (param[i].contains("category_id=")) {
                                        String catId = param[i].replace("category_id=", "");
                                        if (!TextUtils.isEmpty(catId) && !TextUtils.equals(catId, "null")) {
                                            category_id = catId;
                                            recievedCategoryId = category_id;
                                        } else {
                                            category_id = "";
                                        }
                                    } else if (param[i].contains("sortby=")) {
                                        String sortBy = param[i].replace("sortby=", "");
                                        if (!TextUtils.isEmpty(sortBy) && !TextUtils.equals(sortBy, "null")) {
                                            sortby = sortBy;
                                        } else {
                                            sortby = SORT_BY_DEFAULT;
                                        }
                                    } else if (param[i].contains("sort_by=")) {
                                        String sortBy = param[i].replace("sort_by=", "");
                                        if (!TextUtils.isEmpty(sortBy) && !TextUtils.equals(sortBy, "null")) {
                                            sortby = sortBy;
                                        } else {
                                            sortby = SORT_BY_DEFAULT;
                                        }
                                    } else if (param[i].contains("direction=")) {
                                        String direcTion = param[i].replace("direction=", "");
                                        if (!TextUtils.isEmpty(direcTion) && !TextUtils.equals(direcTion, "null")) {
                                            direction = direcTion;
                                            recievedDirection = direcTion;
                                        } else {
                                            direction = DIRECTION_DEFAULT;
                                            recievedDirection = DIRECTION_DEFAULT;
                                        }
                                    } else if (param[i].contains("brand=")) {
                                        String brand = param[i].replace("brand=", "");
                                        if (!TextUtils.isEmpty(brand) && !TextUtils.equals(brand, "null")) {
                                            jFilterObject.put("brand", "" + brand);
                                        }
                                    } else if (param[i].contains("filters[brand]=")) {
                                        String brand = param[i].replace("filters[brand]=", "");
                                        if (!TextUtils.isEmpty(brand) && !TextUtils.equals(brand, "null")) {
                                            jFilterObject.put("brand", "" + brand);
                                        }
                                    } else if (param[i].contains("filters")) {
                                        String key = param[i].substring(param[i].indexOf("[") + 1, param[i].indexOf("]"));
                                        String value = param[i].replace("filters[" + key + "]=", "");
                                        LogUtils.e("", "filter key::" + key + " value::" + value);
                                        if (!TextUtils.isEmpty(value) && !TextUtils.equals(value, "null")) {
                                            jFilterObject.put(key, "" + value);
                                        }
                                    }
                                }
                            }
                        } else if (!TextUtils.isEmpty(postfix) && postfix.contains("&")) {
                            String[] param = postfix.split("&");

                            jFilterObject = new JSONObject();
                            for (int i = 0; i < param.length; i++) {
                                LogUtils.e("", i + " param::" + param[i]);
                                if (param[i].contains("category_id=")) {
                                    String catId = param[i].replace("category_id=", "");
                                    if (!TextUtils.isEmpty(catId) && !TextUtils.equals(catId, "null")) {
                                        category_id = catId;
                                        recievedCategoryId = category_id;
                                    } else {
                                        category_id = "";
                                    }
                                } else if (param[i].contains("sortby=")) {
                                    String sortBy = param[i].replace("sortby=", "");
                                    if (!TextUtils.isEmpty(sortBy) && !TextUtils.equals(sortBy, "null")) {
                                        sortby = sortBy;
                                    } else {
                                        sortby = SORT_BY_DEFAULT;
                                    }
                                } else if (param[i].contains("sort_by=")) {
                                    String sortBy = param[i].replace("sort_by=", "");
                                    if (!TextUtils.isEmpty(sortBy) && !TextUtils.equals(sortBy, "null")) {
                                        sortby = sortBy;
                                    } else {
                                        sortby = SORT_BY_DEFAULT;
                                    }
                                }else if (param[i].contains("direction=")) {
                                    String direcTion = param[i].replace("direction=", "");
                                    if (!TextUtils.isEmpty(direcTion) && !TextUtils.equals(direcTion, "null")) {
                                        direction = direcTion;
                                        recievedDirection = direcTion;
                                    } else {
                                        direction = DIRECTION_DEFAULT;
                                        recievedDirection = DIRECTION_DEFAULT;
                                    }
                                } else if (param[i].contains("brand=")) {
                                    String brand = param[i].replace("brand=", "");
                                    if (!TextUtils.isEmpty(brand) && !TextUtils.equals(brand, "null")) {
                                        jFilterObject.put("brand", "" + brand);
                                    }
                                } else if (param[i].contains("filters[brand]=")) {
                                    String brand = param[i].replace("filters[brand]=", "");
                                    if (!TextUtils.isEmpty(brand) && !TextUtils.equals(brand, "null")) {
                                        jFilterObject.put("brand", "" + brand);
                                    }
                                } else if (param[i].contains("filters")) {
                                    String key = param[i].substring(param[i].indexOf("[") + 1, param[i].indexOf("]"));
                                    String value = param[i].replace("filters[" + key + "]=", "");
                                    LogUtils.e("", "filter key::" + key + " value::" + value);
                                    if (!TextUtils.isEmpty(value) && !TextUtils.equals(value, "null")) {
                                        jFilterObject.put(key, "" + value);
                                    }
                                }
                            }
                        }
                    } else {
                        String postfix = searchCriteria;
                        LogUtils.e("", "postfix::" + postfix);
                        if (!TextUtils.isEmpty(postfix) && postfix.contains("query=true&")) {
                            mUrlQueryPostFix = postfix.replace("query=true&", "");

                            if (!TextUtils.isEmpty(postfix)) {
                                String[] param = mUrlQueryPostFix.split("&");

                                jFilterObject = new JSONObject();
                                for (int i = 0; i < param.length; i++) {
                                    LogUtils.e("", i + " param::" + param[i]);
                                    if (param[i].contains("category_id=")) {
                                        String catId = param[i].replace("category_id=", "");
                                        if (!TextUtils.isEmpty(catId) && !TextUtils.equals(catId, "null")) {
                                            category_id = catId;
                                            recievedCategoryId = category_id;
                                        } else {
                                            category_id = "";
                                        }
                                    } else if (param[i].contains("sortby=")) {
                                        String sortBy = param[i].replace("sortby=", "");
                                        if (!TextUtils.isEmpty(sortBy) && !TextUtils.equals(sortBy, "null")) {
                                            sortby = sortBy;
                                        } else {
                                            sortby = SORT_BY_DEFAULT;
                                        }
                                    }else if (param[i].contains("sort_by=")) {
                                        String sortBy = param[i].replace("sort_by=", "");
                                        if (!TextUtils.isEmpty(sortBy) && !TextUtils.equals(sortBy, "null")) {
                                            sortby = sortBy;
                                        } else {
                                            sortby = SORT_BY_DEFAULT;
                                        }
                                    } else if (param[i].contains("direction=")) {
                                        String direcTion = param[i].replace("direction=", "");
                                        if (!TextUtils.isEmpty(direcTion) && !TextUtils.equals(direcTion, "null")) {
                                            direction = direcTion;
                                            recievedDirection = direcTion;
                                        } else {
                                            direction = DIRECTION_DEFAULT;
                                            recievedDirection = DIRECTION_DEFAULT;
                                        }
                                    } else if (param[i].contains("brand=")) {
                                        String brand = param[i].replace("brand=", "");
                                        if (!TextUtils.isEmpty(brand) && !TextUtils.equals(brand, "null")) {
                                            jFilterObject.put("brand", "" + brand);
                                        }
                                    } else if (param[i].contains("filters[brand]=")) {
                                        String brand = param[i].replace("filters[brand]=", "");
                                        if (!TextUtils.isEmpty(brand) && !TextUtils.equals(brand, "null")) {
                                            jFilterObject.put("brand", "" + brand);
                                        }
                                    } else if (param[i].contains("filters")) {
                                        String key = param[i].substring(param[i].indexOf("[") + 1, param[i].indexOf("]"));
                                        String value = param[i].replace("filters[" + key + "]=", "");
                                        LogUtils.e("", "filter key::" + key + " value::" + value);
                                        if (!TextUtils.isEmpty(value) && !TextUtils.equals(value, "null")) {
                                            jFilterObject.put(key, "" + value);
                                        }
                                    }
                                }
                            }

                        } else if (!TextUtils.isEmpty(postfix) && postfix.contains("&")) {
                            String[] param = postfix.split("&");

                            jFilterObject = new JSONObject();
                            for (int i = 0; i < param.length; i++) {
                                LogUtils.e("", i + " param::" + param[i]);
                                if (param[i].contains("category_id=")) {
                                    String catId = param[i].replace("category_id=", "");
                                    if (!TextUtils.isEmpty(catId) && !TextUtils.equals(catId, "null")) {
                                        category_id = catId;
                                        recievedCategoryId = category_id;
                                    } else {
                                        category_id = "";
                                    }
                                } else if (param[i].contains("sortby=")) {
                                    String sortBy = param[i].replace("sortby=", "");
                                    if (!TextUtils.isEmpty(sortBy) && !TextUtils.equals(sortBy, "null")) {
                                        sortby = sortBy;
                                    } else {
                                        sortby = SORT_BY_DEFAULT;
                                    }
                                }else if (param[i].contains("sort_by=")) {
                                    String sortBy = param[i].replace("sort_by=", "");
                                    if (!TextUtils.isEmpty(sortBy) && !TextUtils.equals(sortBy, "null")) {
                                        sortby = sortBy;
                                    } else {
                                        sortby = SORT_BY_DEFAULT;
                                    }
                                } else if (param[i].contains("direction=")) {
                                    String direcTion = param[i].replace("direction=", "");
                                    if (!TextUtils.isEmpty(direcTion) && !TextUtils.equals(direcTion, "null")) {
                                        direction = direcTion;
                                        recievedDirection = direcTion;
                                    } else {
                                        direction = DIRECTION_DEFAULT;
                                        recievedDirection = DIRECTION_DEFAULT;
                                    }
                                } else if (param[i].contains("brand=")) {
                                    String brand = param[i].replace("brand=", "");
                                    if (!TextUtils.isEmpty(brand) && !TextUtils.equals(brand, "null")) {
                                        jFilterObject.put("brand", "" + brand);
                                    }
                                } else if (param[i].contains("filters[brand]=")) {
                                    String brand = param[i].replace("filters[brand]=", "");
                                    if (!TextUtils.isEmpty(brand) && !TextUtils.equals(brand, "null")) {
                                        jFilterObject.put("brand", "" + brand);
                                    }
                                } else if (param[i].contains("filters")) {
                                    String key = param[i].substring(param[i].indexOf("[") + 1, param[i].indexOf("]"));
                                    String value = param[i].replace("filters[" + key + "]=", "");
                                    LogUtils.e("", "filter key::" + key + " value::" + value);
                                    if (!TextUtils.isEmpty(value) && !TextUtils.equals(value, "null")) {
                                        jFilterObject.put(key, "" + value);
                                    }
                                }
                            }
                        }
                    }

//                    JSONObject jsonObject = new JSONObject(searchCriteria);
//                    category_id = jsonObject.getString("category_id");
//                    sortby = jsonObject.getString("sortby");
//                    direction = jsonObject.getString("direction");
//                    jFilterObject = jsonObject.getJSONObject("filterdata");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mUrlQueryPostFix = "";
                category_id = searchCriteria;
                recievedCategoryId = category_id;
                sortby = SORT_BY_DEFAULT;
                direction = DIRECTION_DEFAULT;
                jFilterObject = new JSONObject();
            }

            recievedCategoryName = getCategoryName(category_id);
            category_name = recievedCategoryName;


            LogUtils.e(TAG_PRODUCT_ITEMS_FRAGMENT, isFromBanner + " " + searchCriteria);

//            if (!TextUtils.isEmpty(searchCriteria) && productItemList.size() == 0) {


        } else {
            LogUtils.e("", "Bundle is null");
        }

        fiveDp = (int) Utils.pxFromDp(getActivity(), 5);
        threeDp = (int) Utils.pxFromDp(getActivity(), 2);

//        itemBadgeView = new BadgeView(getActivity(), ibRightIcons, 11);
//        itemBadgeView.setBadgeMargin(threeDp, threeDp);

        mCartItemCount = getPref(getActivity(), MyPref.CART_ITEM_COUNT, 0);
//        mCartItemCount = 5;
//        itemBadgeView.setText("" + mCartItemCount);

//        if (mCartItemCount > 0) {
//            itemBadgeView.show();
//        } else {
//            itemBadgeView.hide();
//        }

        if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

            tvTitle.setBackgroundResource(R.drawable.ic_top_logo_ar);
            mTopLayout.setScaleX(-1f);
            lnrSearchBar.setScaleX(-1f);
//            itemBadgeView.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
//            ivSearch.setScaleX(-1f);
            tvTitle.setTypeface(Shy7lo.RobotoMedium);
            etSearch.setScaleX(-1f);
            etSearch.setGravity(Gravity.RIGHT);
        } else {

            tvTitle.setBackgroundResource(R.drawable.ic_top_logo);
            mTopLayout.setScaleX(1f);
            lnrSearchBar.setScaleX(1f);
//            itemBadgeView.setScaleX(1f);
            tvTitle.setScaleX(1f);
//            ivSearch.setScaleX(1f);
            tvTitle.setTypeface(Shy7lo.RobotoMedium);
            etSearch.setScaleX(1f);
            etSearch.setGravity(Gravity.LEFT);
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

        if (((HomeActivity) getActivity()).categoryList != null) {
            CategoryList mCategoryList = ((HomeActivity) getActivity()).categoryList;
            if (mCategoryList != null && mCategoryList.success.equals("1")) {

                if (mCategoryList.data != null && mCategoryList.data.getChildrenData() != null && mCategoryList.data.getChildrenData().size() > 0) {

                    for (CategoryList.MainCategoryData mainCategoryData : mCategoryList.data.getChildrenData()) {
                        if (mainCategoryData.getId() == 143) {
                            mWomenCategoryData = mainCategoryData;
                        } else if (mainCategoryData.getId() == 144) {
                            mMenCategoryData = mainCategoryData;
                        } else if (mainCategoryData.getId() == 145) {
                            mKidCategoryData = mainCategoryData;
                        }
                    }
                }
            }
        }


    }


    public void resetData() {
        LogUtils.e("", "resetData call");
//        if (productItemList != null && productItemList.size() > 0) {
//            productItemList.clear();
//        }
        if (filterItemList != null && filterItemList.size() > 0) {
            filterItemList.clear();
        }
//        if (sortingItemList != null && sortingItemList.size() > 0) {
//            sortingItemList.clear();
//        }


        sortby = SORT_BY_DEFAULT;

        if (!TextUtils.isEmpty(recievedDirection)) {
            direction = recievedDirection;
        } else {
            direction = DIRECTION_DEFAULT;
        }

        viewType = VIEWTYPE_DEFAULT;
        price = "";
        searchCriteria = "";
        mSelectedItemType = ItemType.GridType;
    }

    @Override
    public void onStart() {
        super.onStart();

//        if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""))) {
//            Utils.getGuestCartToken(getActivity());
//        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(searchCriteria) && isAdded()) {
                    showProductView();
//            getCategoryList();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    getFilterData();
//                }
//            }, 10);
                }
            }
        });

    }

    //    private void getCategoryList() {
//
//
//        LogUtils.e(TAG_PRODUCT_ITEMS_FRAGMENT, "getCategoryList call:");
//        Utils.showProgressDialog(getActivity());
//
//        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//        Call<ProductCategoryList> productListCall = serviceAPI.getProductCategoryList(Shy7lo.mLangCode, category_id);
//
//        productListCall.enqueue(new Callback<ProductCategoryList>() {
//            @Override
//            public void onResponse(Call<ProductCategoryList> call, Response<ProductCategoryList> response) {
//                Utils.closeProgressDialog();
//                if (response.isSuccessful()) {
//                    ProductCategoryList mProductCategoryList = response.body();
//                    if (mProductCategoryList != null && mProductCategoryList.success.equals("1")) {
//                        childCategory = mProductCategoryList.data.category.childCategory;
//
//                        if (childCategory != null && childCategory.size() > 0) {
//                            mCategoryPagerAdapterWithTitle = new CategoryPagerAdapterWithTitle(getChildFragmentManager());
//                            LogUtils.e("", "category size: " + childCategory.size());
//                            for (int i = 0; i < childCategory.size(); i++) {
////                            for (int i = 0; i < 1; i++) {
//
//                                Fragment mFragment = new ProductCategoryFragment();
//                                Bundle bundle = new Bundle();
//                                bundle.putString(ProductCategoryFragment.BNDL_CATEGORY_ID, new Gson().toJson(childCategory.get(i)));
//                                bundle.putString(ProductCategoryFragment.BNDL_SORT_BY, sortby);
//                                bundle.putString(ProductCategoryFragment.BNDL_DIRECTION, direction);
//                                bundle.putString(ProductCategoryFragment.BNDL_VIEW_TYPE, viewType);
//                                bundle.putString(ProductCategoryFragment.BNDL_FILTER_OBJECT, jFilterObject.toString());
//                                if (bundle != null) {
//                                    if (mFragment.getArguments() == null) {
//                                        mFragment.setArguments(bundle);
//                                    } else {
//                                        mFragment.getArguments().putAll(bundle);
//                                    }
//
//                                }
//
//                                mCategoryPagerAdapterWithTitle.addFragment(mFragment, childCategory.get(i).name);
//                            }
//
//                            mViewPager.setAdapter(mCategoryPagerAdapterWithTitle);
//                            mViewPager.setPageMargin(10);
//                            mViewPager.setOffscreenPageLimit(2);
//                            mViewPager.setCurrentItem(0);
//
//                            TabLayout mTabStrip = (TabLayout) mView.findViewById(R.id.sliding_tabs);
//                            if (childCategory.size() < 2) {
//                                mTabStrip.setVisibility(View.GONE);
//                            } else {
//                                mTabStrip.setVisibility(View.VISIBLE);
//                                mTabStrip.setupWithViewPager(mViewPager);
//                            }
//
//                        }
//
//                    } else if (mProductCategoryList != null && mProductCategoryList.success.equals("2")) {
//                        Utils.showInitialScreen(getActivity());
//                        return;
//                    }
//                }
//
//
//
//            }
//
//            @Override
//            public void onFailure(Call<ProductCategoryList> call, Throwable t) {
//                System.out.println(t.getMessage());
//                Utils.closeProgressDialog();
//            }
//        });
//
//    }


    private void showProductView() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                showProductView();
                            }
                        });

                    }
                }
            });
            return;
        }

//        mPageNumberList = new ArrayList<>();


        LogUtils.e(TAG_PRODUCT_CATEGORY_FRAGMENT, "getProductItemsList call:");
        Utils.showProgressDialog(getActivity());

        if (productItemList != null && productItemList.size() > 0) {
            productItemList.clear();
        }

        page_number = 1;
//        page_number = 0;

        Map<String, String> jsonParams = new ArrayMap<>();
        if (!TextUtils.isEmpty(mUrlQueryPostFix)) {
            String[] param = mUrlQueryPostFix.split("&");
            for (int i = 0; i < param.length; i++) {
                String[] paramValue = param[i].split("=");
                if (paramValue.length > 1) {
                    jsonParams.put(paramValue[0], paramValue[1]);
                    if (paramValue[0].equalsIgnoreCase(category_id)) {
                        category_id = paramValue[1];
                    }
                }
            }
            jsonParams.put("page", "" + page_number);
            mUrlQueryPostFix = "";
        } else {
            jsonParams.put("category_id", category_id);
            jsonParams.put("sort_by", sortby);
            jsonParams.put("direction", direction);
            jsonParams.put("page", "" + page_number);
            if (!TextUtils.isEmpty(price)) {
                jsonParams.put("price", "" + price);
            }
//        jsonParams.put("catIndex", "1");
//        jsonParams.put("filter", "" + jFilterObject.toString());
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
        if (jFilterObject != null) {
            LogUtils.e("", "jFilterObject.toString()::" + jFilterObject.toString());
        }
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());


        try {
            ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
            Call<ProductList> productListCall = serviceAPI.getProductsList(Shy7lo.mLangCode, jsonParams);

            productListCall.enqueue(new Callback<ProductList>() {
                @Override
                public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                    Utils.closeProgressDialog();
//                lnrNothingFound.setVisibility(View.GONE);
                    isLoading = false;

                    if (response.isSuccessful()) {
                        ProductList productList = response.body();

                        if (productList != null && productList.success.equals("1")) {
//                        if (productList != null && productList.data.getProductInfoData().size() > 0) {
                            if (productList != null) {

//                                productItemList = productList.data.getProductInfoData();
                                category_name = productList.data.getCategoryName();
                                if (!TextUtils.isEmpty(category_id)) {
                                    category_id = productList.data.getCategoryId();
                                    mMainCateID = category_id;
                                    recievedCategoryId = category_id;
                                    recievedCategoryName = category_name;
                                }

//                            if (mPageNumberList != null && mPageNumberList.size() > 0) {
//                                mPageNumberList.clear();
//                                mPageNumberList.add("" + page_number);
//                            }

//                                List<ProductList.ChildCategory> mChildCategoryList = productList.data.category;
                                List<ProductList.ChildCategory> mChildCategoryList = productList.data.category.childCategory;
                                if (mChildCategoryList.size() == 0) {
                                    ProductList.ChildCategory mChildCategory = new ProductList().new ChildCategory();
                                    mChildCategory.id = productList.data.getCategoryId();
//                                mChildCategory.name = productList.data.getCategoryName();
                                    mChildCategory.name = getString(R.string.all);
                                    mChildCategoryList.add(0, mChildCategory);
                                } else {
//                                        ProductList.ChildCategory mChildCategory = mChildCategoryList.remove(0);
//                                        Collections.sort(mChildCategoryList, new Comparator<ProductList.ChildCategory>() {
//                                            @Override
//                                            public int compare(ProductList.ChildCategory t1, ProductList.ChildCategory t2) {
//                                                return t1.name.compareTo(t2.name);
//                                            }
//                                        });
//                                        mChildCategoryList.add(0, mChildCategory);
                                }

                                if (TextUtils.isEmpty(recievedCategoryName)) {
                                    recievedCategoryName = mChildCategoryList.get(0).name;
                                }

                                if (!isAdded()) return;
                                mCategoryPagerAdapterWithTitle = new CategoryPagerAdapterWithTitle(getChildFragmentManager());
                                for (int i = 0; i < mChildCategoryList.size(); i++) {

                                    Fragment mFragment = new ProductCategoryFragment();
                                    Bundle bundle = new Bundle();
                                    if (i == 0) {
                                        bundle.putString(ProductCategoryFragment.BNDL_PRODUCT_LIST, new Gson().toJson(productList.data));
                                    }
                                    bundle.putString(ProductCategoryFragment.BNDL_URLQUERY_POSTFIX, mUrlQueryPostFix);
                                    bundle.putString(ProductCategoryFragment.BNDL_CATEGORY_ID, mChildCategoryList.get(i).id);
                                    bundle.putString(ProductCategoryFragment.BNDL_CATEGORY_NAME, mChildCategoryList.get(i).name);

                                    LogUtils.e("", i + " mChildCategoryList.get(i).id::" + mChildCategoryList.get(i).id);
                                    LogUtils.e("", i + " mChildCategoryList.get(i).name::" + mChildCategoryList.get(i).name);

                                    bundle.putString(ProductCategoryFragment.BNDL_SORT_BY, sortby);
                                    bundle.putString(ProductCategoryFragment.BNDL_DIRECTION, direction);
                                    bundle.putString(ProductCategoryFragment.BNDL_VIEW_TYPE, viewType);
                                    bundle.putString(ProductCategoryFragment.BNDL_PRICE, price);
                                    bundle.putString(ProductCategoryFragment.BNDL_FILTER_OBJECT, jFilterObject.toString());
                                    if (bundle != null) {
                                        if (mFragment.getArguments() == null) {
                                            mFragment.setArguments(bundle);
                                        } else {
                                            mFragment.getArguments().putAll(bundle);
                                        }

                                    }

                                    mCategoryPagerAdapterWithTitle.addFragment(mFragment, mChildCategoryList.get(i).name);
                                }

                                mViewPager.setAdapter(mCategoryPagerAdapterWithTitle);
                                mViewPager.setPageMargin(10);

//                                if (mChildCategoryList != null && mChildCategoryList.size() < 2) {
                                    mViewPager.setOffscreenPageLimit(1);
//                                } else {
//                                    mViewPager.setOffscreenPageLimit(2);
//                                }

                                try {
                                    mViewPager.setCurrentItem(mPagerPosition);
                                } catch (Exception e) {
                                    mViewPager.setCurrentItem(0);
                                }

                                mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                    }

                                    @Override
                                    public void onPageSelected(int position) {
                                        mPagerPosition = position;
                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int state) {

                                    }
                                });

                                if (mChildCategoryList.size() < 2) {
                                    tlSlidingTabs.setVisibility(View.GONE);
                                    mLine.setVisibility(View.GONE);

                                    if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                                        mViewPager.setScaleX(-1f);
                                    } else {
                                        mViewPager.setScaleX(1f);
                                    }
                                } else {
                                    tlSlidingTabs.setVisibility(View.VISIBLE);
                                    mLine.setVisibility(View.VISIBLE);
//                                    tlSlidingTabs.setVisibility(View.GONE);
//                                    mLine.setVisibility(View.GONE);
                                    tlSlidingTabs.setupWithViewPager(mViewPager, true);
//
                                    if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

//                                            mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                                        mViewPager.setScaleX(-1f);
                                        Utils.setTabsFont(tlSlidingTabs, Shy7lo.DroidKufiRegular);

//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                                                    tlSlidingTabs.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//                                                } else {
                                        tlSlidingTabs.setScaleX(-1f);
                                        LinearLayout tabStrip = (LinearLayout) tlSlidingTabs.getChildAt(0);
                                        for (int i = 0; i < tabStrip.getChildCount(); i++) {
                                            View tabView = tabStrip.getChildAt(i);
                                            if (tabView != null) {
                                                tabView.setScaleX(-1f);
                                            }
                                        }
//                                                    for (int i = 0; i < childCategory.size(); i++) {
//                                                        ((LinearLayout) ((LinearLayout) tlSlidingTabs.getChildAt(0)).getChildAt(i)).setScaleX(-1f);
////                                                        TextView mTab1 = (TextView) (((LinearLayout) ((LinearLayout) tlSlidingTabs.getChildAt(0)).getChildAt(i)).getChildAt(1));
////                                                        mTab1.setScaleX(-1f);
////                                                        View mainTab = ((ViewGroup) tlSlidingTabs.getChildAt(0)).getChildAt(i);
////                                                        mainTab.setScaleX(-1f);
//                                                    }
//                                                }

//
                                    } else {

//                                            mViewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                        mViewPager.setScaleX(1f);
                                        Utils.setTabsFont(tlSlidingTabs, Shy7lo.RalewayRegular);

//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                                                    tlSlidingTabs.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//                                                } else {
                                        tlSlidingTabs.setScaleX(1f);

                                        LinearLayout tabStrip = (LinearLayout) tlSlidingTabs.getChildAt(0);
                                        for (int i = 0; i < tabStrip.getChildCount(); i++) {
                                            View tabView = tabStrip.getChildAt(i);
                                            if (tabView != null) {
                                                tabView.setScaleX(1f);
                                            }
                                        }

//                                                    for (int i = 0; i < childCategory.size(); i++) {
//                                                        ((LinearLayout) ((LinearLayout) tlSlidingTabs.getChildAt(0)).getChildAt(i)).setScaleX(1f);
////                                                        TextView mTab1 = (TextView) (((LinearLayout) ((LinearLayout) tlSlidingTabs.getChildAt(0)).getChildAt(i)).getChildAt(1));
////                                                        mTab1.setScaleX(1f);
////                                                        View mainTab = ((ViewGroup) tlSlidingTabs.getChildAt(0)).getChildAt(i);
////                                                        mainTab.setScaleX(1f);
//                                                    }
//                                                }
                                    }

                                }


                                PAGE_SIZE = productList.data.getTotalCount();

//                            if (productList.data.getTotalCount())


                                if (productItemList.size() % 16 == 0 && productItemList.size() < PAGE_SIZE) {
                                    isLastPage = false;
                                } else {
                                    isLastPage = true;
                                }

//                            if (productItemList != null && productItemList.size() > 0) {
//                                if (mAdapter == null) {
////                                    LogUtils.e("", "mAdapter is null");
//                                    setSignoutView();
//                                } else {
//                                    LogUtils.e("", "mAdapter is not null");
//                                    mAdapter.changeData(productItemList);
//                                    rvProductItems.setAdapter(mAdapter);
////                                    mAdapter.notifyDataSetChanged();
//                                }
//                            mProductsBaseAdapter.changeData(productItemList);

//                            if (productItemList == null || productItemList.size() == 0) {
////                                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
////                                    Utils.showToast(getActivity(), "لم يتم العثور على نتائج مطابقة للبحث");
////                                } else {
////                                    Utils.showToast(getActivity(), "No result found");
////                                }
//                                lnrNothingFound.setVisibility(View.VISIBLE);
////                                Utils.showToast(getActivity(), "" + getString(R.string.msg_no_result));
//                            } else {
//                                if (!isLoading && !isLastPage) {
////                                    if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {
//                                    LogUtils.e("", "Load more");
//                                    isLoading = true;
//                                    page_number = page_number + 1;
//                                    getProductNextPageView(false, page_number);
////                                    }
//                                }
//                            }
//                            }
                            }

                            try {
                                AdjustEvent event = new AdjustEvent("9dnoop");
                                event.addPartnerParameter("Category ID", "" + "" + category_id);

//                                Callback
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

                        } else if (productList != null && productList.success.equals("2")) {
                            Utils.showInitialScreen(getActivity());
                            return;
                        } else {
                            if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                                Utils.showToast(getActivity(), "خطأ");
                            } else {
                                Utils.showToast(getActivity(), "Something went wrong");
                            }
                            page_number = 1;
                        }

                    } else {

                        LogUtils.e(TAG_PRODUCT_CATEGORY_FRAGMENT, "Response is not great");
//                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            Utils.showToast(getActivity(), "خطأ");
//                        } else {
//                            Utils.showToast(getActivity(), "Something went wrong");
//                        }
                        page_number = 1;
//                        page_number = 0;
//                        Utils.showAlertDialog(getActivity(), "" + response.code());
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
                    if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                        Utils.showToast(getActivity(), "خطأ");
                    } else {
                        Utils.showToast(getActivity(), "Something went wrong");
                    }
                    isLoading = false;
                    page_number = 1;
//                    page_number = 0;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    private void getFilterData() {
//
//
////        if (sortingItemList != null && sortingItemList.size() > 0) {
////            sortingItemList.clear();
////        }
//
//        if (filterItemList != null && filterItemList.size() > 0) {
//            filterItemList.clear();
//        }
//
//        Map<String, Object> jsonParams = new ArrayMap<>();
//        jsonParams.put("category_id", category_id);
//        jsonParams.put("sortby", sortby);
//        jsonParams.put("direction", direction);
//        jsonParams.put("page_number", 1);
//        jsonParams.put("filterdata", jFilterObject);
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
//
//
//        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//        Call<SortingList> productListCall = serviceAPI.getSortingList(Shy7lo.mLangCode, body);
//
//        productListCall.enqueue(new Callback<SortingList>() {
//            @Override
//            public void onResponse(Call<SortingList> call, Response<SortingList> response) {
//
//
//                if (response.isSuccessful() && response.body() != null) {
//
//                    SortingList sortingList = response.body();
//
//                    if (sortingList != null && sortingList.success.equals("1")) {
//                        if (sortingList != null) {
//
////                            sortingItemList = sortingList.data.getSortingData();
//                            filterItemList = sortingList.data.getFilterData();
//
//                            if (filterItemList != null && filterItemList.size() > 0) {
//                                for (int i = 0; i < filterItemList.size(); i++) {
//                                    SortingList.FilterData filterData = filterItemList.get(i);
//                                    if (filterData != null) {
//                                        if (filterData.getOptions() == null || filterData.getOptions().size() == 0) {
//                                            filterItemList.remove(i);
//                                        }
//                                    }
//                                }
//
//                                Collections.sort(filterItemList, new Comparator<SortingList.FilterData>() {
//                                    @Override
//                                    public int compare(SortingList.FilterData t1, SortingList.FilterData t2) {
//                                        return t1.getLabel().compareTo(t2.getLabel());
//                                    }
//
//                                });
//                            }
//
////                            if (sortingItemList != null && sortingItemList.size() > 0) {
////
////                                for (int i = 0; i < sortingItemList.size(); i++) {
////
////                                    if (sortingItemList.get(i).getCode().equals("price")) {
////
////                                        SortingList.SortingData sortingData = sortingItemList.get(i);
////                                        sortingData.setDirection("ASC");
////                                        if (isAdded()) {
////                                            sortingData.setLabel("" + getString(R.string.price_low_to_high));
////                                        } else {
//////                                            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//////                                                sortingData.setLabel("السعر (الأقل)");
//////                                            }else{
////                                            sortingData.setLabel("Price (Low to High)");
//////                                            }
////                                        }
////                                        SortingList.SortingData sortingData1 = new SortingList().new SortingData();
////                                        if (isAdded()) {
////                                            sortingData1.setLabel("" + getString(R.string.price_high_to_low));
////                                        } else {
//////                                            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//////                                                sortingData1.setLabel("السعر (الأعلى)");
//////                                            }else{
////                                            sortingData1.setLabel("Price (High to Low)");
//////                                            }
////                                        }
////                                        sortingData1.setDirection("DESC");
////                                        sortingData1.setCode("price");
////                                        sortingItemList.add((i + 1), sortingData1);
////                                        break;
////                                    }
////
////                                }
////
////                            }
//
//
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SortingList> call, Throwable t) {
//                System.out.println(t.getMessage());
//
//            }
//        });
//
//    }


//    private void getProductNextPageView(boolean isLoadingShow, final int page_number) {
//        LogUtils.e(TAG_PRODUCT_CATEGORY_FRAGMENT, "getProductNextPageView call:");
////        Utils.showProgressDialog(getActivity());
//        if (isLoadingShow) {
////            lnrProgressBar.setVisibility(View.VISIBLE);
//        }
//
//        Map<String, Object> jsonParams = new ArrayMap<>();
//        jsonParams.put("category_id", category_id);
//        jsonParams.put("sortby", sortby);
//        jsonParams.put("direction", direction);
////        jsonParams.put("page_number", this.page_number);
//        jsonParams.put("page_number", page_number);
//        jsonParams.put("filterdata", jFilterObject);
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
//
//
//        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//        Call<ProductList> productListCall = serviceAPI.getProductsList(Shy7lo.mLangCode, body);
//
//        productListCall.enqueue(new Callback<ProductList>() {
//            @Override
//            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
//
//                isLoading = false;
//                isLoadingMore = false;
////                lnrProgressBar.setVisibility(View.GONE);
//
//                if (response.isSuccessful()) {
//                    ProductList productList = response.body();
//
//                    if (productList != null && productList.success.equals("1")) {
//                        if (productList != null) {
//
//                            LogUtils.e("", page_number + " page_number Contains" + mPageNumberList.contains("" + page_number));
//                            if (!mPageNumberList.contains("" + page_number)) {
//                                productItemList.addAll(productList.data.getProductInfoData());
//                                mPageNumberList.add("" + page_number);
//                            }
//
////                        sortingItemList = productList.data.getSortingData();
////                        mFilterItemList = productList.data.getFilterData();
//
////                            if (productList.data.getTotalCount())
//
//                            if (productItemList.size() % 16 == 0 && productItemList.size() < PAGE_SIZE) {
//                                isLastPage = false;
//                            } else {
//                                isLastPage = true;
//                            }
//
////                            if (productItemList != null && productItemList.size() > 0) {
////                                if (mAdapter == null) {
////                                    LogUtils.e("", "mAdapter is null");
////                                    setSignoutView();
////                                } else {
////                                    LogUtils.e("", "mAdapter is not null");
////                                    mAdapter.changeData(productItemList);
//////                                    rvProductItems.setAdapter(mAdapter);
//////                                    mAdapter.notifyDataSetChanged();
////                                }
////                            mProductsBaseAdapter.changeData(productItemList);
////                            }
//                        }
//                    } else {
////                        Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
////                        page_number = page_number - 1;
//                    }
//
//                } else {
//                    LogUtils.e(TAG_PRODUCT_CATEGORY_FRAGMENT, "Response is not great");
////                    Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
////                    page_number = page_number - 1;
//                }
////                Utils.closeProgressDialog();
//            }
//
//            @Override
//            public void onFailure(Call<ProductList> call, Throwable t) {
//                System.out.println(t.getMessage());
////                Utils.closeProgressDialog();
////                lnrProgressBar.setVisibility(View.GONE);
////                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
//                isLoading = false;
////                page_number = page_number - 1;
//            }
//        });
//
//    }
//
//    private void getProductNextLoadMorePageView(boolean isLoadingShow, final int page_number) {
//        LogUtils.e(TAG_PRODUCT_CATEGORY_FRAGMENT, "getProductNextLoadMorePageView call:");
////        Utils.showProgressDialog(getActivity());
//        if (isLoadingShow) {
////            lnrProgressBar.setVisibility(View.VISIBLE);
//        }
//
//        Map<String, Object> jsonParams = new ArrayMap<>();
//        jsonParams.put("category_id", category_id);
//        jsonParams.put("sortby", sortby);
//        jsonParams.put("direction", direction);
////        jsonParams.put("page_number", this.page_number);
//        jsonParams.put("page_number", page_number);
//        jsonParams.put("filterdata", jFilterObject);
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
//
//
//        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//        Call<ProductList> productListCall = serviceAPI.getProductsList(Shy7lo.mLangCode, body);
//
//        productListCall.enqueue(new Callback<ProductList>() {
//            @Override
//            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
//
//                isLoadingMore = false;
////                lnrProgressBar.setVisibility(View.GONE);
//
//                if (response.isSuccessful()) {
//                    ProductList productList = response.body();
//
//                    if (productList != null && productList.success.equals("1")) {
//                        if (productList != null) {
//
//                            LogUtils.e("getProductNextLoadMorePageView", page_number + " page_number Contains" + mPageNumberList.contains("" + page_number));
//                            if (!mPageNumberList.contains("" + page_number)) {
//                                productItemList.addAll(productList.data.getProductInfoData());
//                                mPageNumberList.add("" + page_number);
//                            }
////                        sortingItemList = productList.data.getSortingData();
////                        mFilterItemList = productList.data.getFilterData();
//
////                            if (productList.data.getTotalCount())
//
//                            if (productItemList.size() % 16 == 0 && productItemList.size() < PAGE_SIZE) {
//                                isLastPage = false;
//                            } else {
//                                isLastPage = true;
//                            }
//
////                            if (productItemList != null && productItemList.size() > 0) {
////                                if (mAdapter == null) {
////                                    LogUtils.e("", "mAdapter is null");
////                                    setSignoutView();
////                                } else {
////                                    LogUtils.e("", "mAdapter is not null");
////                                    mAdapter.changeData(productItemList);
//////                                    rvProductItems.setAdapter(mAdapter);
//////                                    mAdapter.notifyDataSetChanged();
////                                }
////                            mProductsBaseAdapter.changeData(productItemList);
////                            }
//                        }
//                    } else {
////                        Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
////                        page_number = page_number - 1;
//                    }
//
//                } else {
//                    LogUtils.e(TAG_PRODUCT_CATEGORY_FRAGMENT, "Response is not great");
////                    Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
////                    page_number = page_number - 1;
//                }
////                Utils.closeProgressDialog();
//            }
//
//            @Override
//            public void onFailure(Call<ProductList> call, Throwable t) {
//                System.out.println(t.getMessage());
////                Utils.closeProgressDialog();
////                lnrProgressBar.setVisibility(View.GONE);
////                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
//                isLoadingMore = false;
////                page_number = page_number - 1;
//            }
//        });
//
//    }

    private void InitializeControlsAction() {

        ibMore.setOnClickListener(this);
        ibSearch.setOnClickListener(this);
        ibSortings.setOnClickListener(this);
        ibOptionMore.setOnClickListener(this);
        ibCloseIcons.setOnClickListener(this);
//        tvChangeFilters.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ibRightIcons.performClick();
//            }
//        });
    }

    private void setItemTypeView(ItemType itemType) {
        switch (itemType) {
            case ListType:
                mSelectedItemType = ItemType.ListType;
//                gvProductItems.setNumColumns(1);
                viewType = "list";
                break;
            case GridType:
                mSelectedItemType = ItemType.GridType;
//                gvProductItems.setNumColumns(2);
                viewType = VIEWTYPE_DEFAULT;
                break;
        }
    }

    @Override
    public void onClick(View view) {

        if (view == ibMore) {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.openDrawer();
//                activity.onBackPressed();
            }
        } else if (view == ibSortings) {

//            if (getActivity() instanceof HomeActivity) {
//                HomeActivity activity = (HomeActivity) getActivity();
//                activity.loadShoppingBags();
//            }
            LogUtils.e("", "filter category_id::" + category_id);
            if (mViewPager == null) {
                LogUtils.e("", "mViewPager is null");
            } else {
                LogUtils.e("", "mViewPager is not null");
            }

            if (mCategoryPagerAdapterWithTitle == null) {
                LogUtils.e("", "mCategoryPagerAdapterWithTitle is null");
            } else {
                LogUtils.e("", "mCategoryPagerAdapterWithTitle is not null");
            }

            if (mViewPager != null && mCategoryPagerAdapterWithTitle != null) {
                ProductCategoryFragment mFragment = (ProductCategoryFragment) mCategoryPagerAdapterWithTitle.getItem(mViewPager.getCurrentItem());
                filterItemList = mFragment.getCategoryFilterData();
                category_id = mFragment.getCategory();
                category_name = mFragment.getCategoryName();
//                category_name = getCategoryName(category_id);
                LogUtils.e("", "category_name::" + category_name);
//                if (TextUtils.isEmpty(category_name) && mFragment.getCategoryName() != null) {
//                    category_name = mFragment.getCategoryName().name;
//                    LogUtils.e("", "in category_name::" + category_name);
//                }
            }

            if (filterItemList != null && filterItemList.size() > 0) {

                Intent next = new Intent(getActivity(), FilterNewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(FilterNewActivity.BNDL_FILTER_LIST, (Serializable) filterItemList);
                if (jFilterObject == null) {
                    jFilterObject = new JSONObject();
                }
                bundle.putString(FilterNewActivity.BNDL_FILTER_JSON, jFilterObject.toString());
                bundle.putString(FilterNewActivity.BNDL_SORT_BY, sortby);
                bundle.putString(FilterNewActivity.BNDL_DIRECTION, direction);
                bundle.putString(FilterNewActivity.BNDL_VIEW_TYPE, viewType);
                bundle.putString(FilterNewActivity.BNDL_CATEGORY_ID, category_id);
                bundle.putString(FilterNewActivity.BNDL_MAIN_CATEGORY_ID, mMainCateID);
                bundle.putString(FilterNewActivity.BNDL_CATEGORY_NAME, category_name);
                bundle.putString(FilterNewActivity.BNDL_INITIAL_CATEGORY_ID, recievedCategoryId);
                bundle.putString(FilterNewActivity.BNDL_INITIAL_CATEGORY_NAME, recievedCategoryName);
                bundle.putString(FilterNewActivity.BNDL_PRICE, price);

                if (((HomeActivity) getActivity()).categoryList != null) {
                    bundle.putString(FilterNewActivity.BNDL_CATEGORY_LIST, new Gson().toJson(((HomeActivity) getActivity()).categoryList));
                } else {
                    bundle.putString(FilterNewActivity.BNDL_CATEGORY_LIST, "");
                }
                next.putExtras(bundle);
                startActivityForResult(next, RC_FILTER_CODE);
            }
        } else if (view == ibSearch) {
//            showSearchBar();
//            if (getActivity() instanceof HomeActivity) {
//
//                HomeActivity activity = (HomeActivity) getActivity();
//                activity.loadSeach();
//            }

            if (getActivity() instanceof HomeActivity) {

                Bundle bundle = new Bundle();
                bundle.putString(SearchAlgoliaFragment.BNDL_SEARCH_KEY, "");
                HomeActivity activity = (HomeActivity) getActivity();
                activity.loadSearchWithBundle(bundle);

            }

        } else if (view == ibOptionMore) {
            showSortingDialog();
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

    private void showSortingDialog() {

        final Animation animClose, animOpen;
        animOpen = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_open);
        animClose = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_close);

        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_sorting);

        final RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.rlMain);
        final LinearLayout lnrContainer = (LinearLayout) dialog.findViewById(R.id.lnrContainer);
        final ListView lvSorting = (ListView) dialog.findViewById(R.id.lvSorting);
//        final TextView tvPoularity = (TextView) dialog.findViewById(R.id.tvPoularity);
//        final TextView tvNew = (TextView) dialog.findViewById(R.id.tvNew);
//        final TextView tvPriceLow = (TextView) dialog.findViewById(R.id.tvPriceLow);
//        final TextView tvPriceHigh = (TextView) dialog.findViewById(R.id.tvPriceHigh);
//        final TextView tvDiscount = (TextView) dialog.findViewById(R.id.tvDiscount);

        final String previousSortby = sortby, previousDirection = direction;

        LogUtils.e("", "sortby:" + sortby);
        LogUtils.e("", "direction:" + direction);

        List<SortingPojo.SortingData> sortingDataList = Shy7lo.sortingDataList;
        for (int i = 0; i < sortingDataList.size(); i++) {
//            if (sortby.equalsIgnoreCase("price")) {
//                if (sortby.equalsIgnoreCase(sortingDataList.get(i).code) && direction.equalsIgnoreCase(sortingDataList.get(i).direction)) {
//                    sortingDataList.get(i).isSelected = true;
//                } else {
//                    sortingDataList.get(i).isSelected = false;
//                }
//
//            } else
            LogUtils.e("", "sortby:" + sortby + " sortingDataList.get(i).code:" + sortingDataList.get(i).code + " result:" + sortby.equalsIgnoreCase(sortingDataList.get(i).code) + " direction:" + direction + " sortingDataList.get(i).direction::" + sortingDataList.get(i).direction + " result:" + direction.equalsIgnoreCase(sortingDataList.get(i).direction) + " final result:" + (sortby.equalsIgnoreCase(sortingDataList.get(i).code) && direction.equalsIgnoreCase(sortingDataList.get(i).direction)));
            if (sortby.equalsIgnoreCase(sortingDataList.get(i).code) && direction.equalsIgnoreCase(sortingDataList.get(i).direction)) {
                sortingDataList.get(i).isSelected = true;
            } else {
                sortingDataList.get(i).isSelected = false;
            }
        }

        DialogSortingAdapter mDialogSortingAdapter = new DialogSortingAdapter(getActivity(), new DialogSortingAdapter.OnSortClickListener() {
            @Override
            public void onSortClicked(SortingPojo.SortingData mSortingData) {
                sortby = mSortingData.code;
                direction = mSortingData.direction;
                rlMain.performClick();
            }
        });
        lvSorting.setAdapter(mDialogSortingAdapter);

        mDialogSortingAdapter.setData(sortingDataList);


//        tvPoularity.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//        tvNew.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//        tvPriceLow.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//        tvPriceHigh.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//        tvDiscount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//
//        if (sortby.equalsIgnoreCase("most_viewed")) {
//            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                tvPoularity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_black, 0, 0, 0);
//            } else {
//                tvPoularity.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_black, 0);
//            }
//        } else if (sortby.equalsIgnoreCase("created_at")) {
//            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                tvNew.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_black, 0, 0, 0);
//            } else {
//                tvNew.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_black, 0);
//            }
//        } else if (sortby.equalsIgnoreCase("price") && direction.equalsIgnoreCase("DESC")) {
//            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                tvPriceLow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_black, 0, 0, 0);
//            } else {
//                tvPriceLow.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_black, 0);
//            }
//        } else if (sortby.equalsIgnoreCase("price") && direction.equalsIgnoreCase("ASC")) {
//            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                tvPriceHigh.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_black, 0, 0, 0);
//            } else {
//                tvPriceHigh.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_black, 0);
//            }
//        } else if (sortby.equalsIgnoreCase("saving")) {
//            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                tvDiscount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_black, 0, 0, 0);
//            } else {
//                tvDiscount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_black, 0);
//            }
//        } else {
//            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                tvNew.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_black, 0, 0, 0);
//            } else {
//                tvNew.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_black, 0);
//            }
//        }
//
//        tvPoularity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                tvPoularity.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvNew.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvPriceLow.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvPriceHigh.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvDiscount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//
//                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    tvPoularity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_black, 0, 0, 0);
//                } else {
//                    tvPoularity.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_black, 0);
//                }
//                sortby = "most_viewed";
//                if (TextUtils.isEmpty(direction)) {
//                    direction = DIRECTION_DEFAULT;
//                }
//                rlMain.performClick();
////                new Handler().post(new Runnable() {
////                    @Override
////                    public void run() {
////                        showProductView();
////                    }
////                });
//            }
//        });
//
//        tvNew.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                LogUtils.e("", "tvNew clicked");
//                tvPoularity.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvNew.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvPriceLow.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvPriceHigh.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvDiscount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//
//                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    tvNew.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_black, 0, 0, 0);
//                } else {
//                    tvNew.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_black, 0);
//                }
//                sortby = SORT_BY_DEFAULT;
//                if (TextUtils.isEmpty(direction)) {
//                    direction = DIRECTION_DEFAULT;
//                }
//                rlMain.performClick();
////                new Handler().post(new Runnable() {
////                    @Override
////                    public void run() {
////                        showProductView();
////                    }
////                });
//            }
//        });
//
//        tvPriceLow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                tvPoularity.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvNew.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvPriceLow.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvPriceHigh.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvDiscount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//
//                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    tvPriceLow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_black, 0, 0, 0);
//                } else {
//                    tvPriceLow.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_black, 0);
//                }
//                sortby = "price";
//                direction = "DESC";
//                rlMain.performClick();
////                new Handler().post(new Runnable() {
////                    @Override
////                    public void run() {
////                        showProductView();
////                    }
////                });
//            }
//        });
//
//        tvPriceHigh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                tvPoularity.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvNew.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvPriceLow.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvPriceHigh.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvDiscount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//
//                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    tvPriceHigh.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_black, 0, 0, 0);
//                } else {
//                    tvPriceHigh.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_black, 0);
//                }
//                sortby = "price";
//                direction = DIRECTION_DEFAULT;
//                rlMain.performClick();
////                new Handler().post(new Runnable() {
////                    @Override
////                    public void run() {
////                        showProductView();
////                    }
////                });
//            }
//        });
//
//        tvDiscount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                tvPoularity.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvNew.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvPriceLow.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvPriceHigh.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                tvDiscount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//
//                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    tvDiscount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_black, 0, 0, 0);
//                } else {
//                    tvDiscount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_black, 0);
//                }
//                sortby = "saving";
//                if (TextUtils.isEmpty(direction)) {
//                    direction = DIRECTION_DEFAULT;
//                }
//                rlMain.performClick();
////                new Handler().post(new Runnable() {
////                    @Override
////                    public void run() {
////                        showProductView();
////                    }
////                });
//            }
//        });

        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lnrContainer.startAnimation(animClose);
                    }
                }, 10);

            }
        });

        animClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                        }

                        mUrlQueryPostFix = "";

                        if (!previousSortby.equalsIgnoreCase(sortby) || !previousDirection.equalsIgnoreCase(direction)) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    showProductView();
                                }
                            });
                        }
                    }
                }, 10);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        lnrContainer.startAnimation(animOpen);
        dialog.show();

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

    private String getCategoryName(String category_id) {
        LogUtils.e("", "getCategoryName mMainCateName::" + mMainCateName + " category_id::" + category_id);

        String mCategoryName = "";
        if (mMainCateName.equalsIgnoreCase("women")) {

            if (mWomenCategoryData != null) {
                for (int i = 0; i < mWomenCategoryData.getChildrenData().size(); i++) {
                    CategoryList.ChildData childData = mWomenCategoryData.getChildrenData().get(i);
                    if (category_id.equalsIgnoreCase("" + childData.getId())) {
//                        if (i == 0) {
//                            mCategoryName = "Women->All";
//                        } else {
                        mCategoryName = getResources().getString(R.string.women) + "->" + childData.getName();
//                        }
                        return mCategoryName;
                    }

                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
                        for (int j = 0; j < childData.getChildrenData().size(); j++) {

                            SubChildData subChildData = childData.getChildrenData().get(j);
                            if (category_id.equalsIgnoreCase("" + subChildData.getId())) {
//                                if (j == 0) {
//                                    mCategoryName = "Women->" + childData.getName().trim() + "->All";
//                                } else {
                                mCategoryName = getResources().getString(R.string.women) + "->" + childData.getName().trim() + "->" + subChildData.getName();
//                                }
                                return mCategoryName;
                            }

                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
                                    if (category_id.equalsIgnoreCase("" + mChileData.getId())) {
//                                        if (k == 0) {
//                                            mCategoryName = "Women->" + childData.getName().trim() + "->" + mChileData.getName().trim() + "->All";
//                                        } else {
                                        mCategoryName = getResources().getString(R.string.women) + "->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChileData.getName();
//                                        }
                                        return mCategoryName;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } else if (mMainCateName.equalsIgnoreCase("men")) {

            if (mMenCategoryData != null) {
                for (int i = 0; i < mMenCategoryData.getChildrenData().size(); i++) {
                    CategoryList.ChildData childData = mMenCategoryData.getChildrenData().get(i);
                    if (category_id.equalsIgnoreCase("" + childData.getId())) {
//                        if (i == 0) {
//                            mCategoryName = "Men->All";
//                        } else {
                        mCategoryName = getResources().getString(R.string.men) + "->" + childData.getName();
//                        }
                        return mCategoryName;
                    }

                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
                        for (int j = 0; j < childData.getChildrenData().size(); j++) {

                            SubChildData subChildData = childData.getChildrenData().get(j);
                            if (category_id.equalsIgnoreCase("" + subChildData.getId())) {
//                                if (j == 0) {
//                                    mCategoryName = "Men->" + childData.getName().trim() + "->All";
//                                } else {
                                mCategoryName = getResources().getString(R.string.men) + "->" + childData.getName().trim() + "->" + subChildData.getName();
//                                }
                                return mCategoryName;
                            }

                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
                                    if (category_id.equalsIgnoreCase("" + mChileData.getId())) {
//                                        if (k == 0) {
//                                            mCategoryName = "Men->" + childData.getName().trim() + "->" + mChileData.getName().trim() + "->All";
//                                        } else {
                                        mCategoryName = getResources().getString(R.string.men) + "->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChileData.getName();
//                                        }
                                        return mCategoryName;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } else if (mMainCateName.equalsIgnoreCase("Kids")) {

            if (mKidCategoryData != null) {
                for (int i = 0; i < mKidCategoryData.getChildrenData().size(); i++) {
                    CategoryList.ChildData childData = mKidCategoryData.getChildrenData().get(i);
                    LogUtils.e("1", category_id + " - " + childData.getId() + "  = " + category_id.equalsIgnoreCase("" + childData.getId()));
                    if (category_id.equalsIgnoreCase("" + childData.getId())) {
//                        if (i == 0) {
//                            mCategoryName = "Kids->All";
//                        } else {
                        mCategoryName = getResources().getString(R.string.kids) + "->" + childData.getName();
//                        }
                        return mCategoryName;
                    }

                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
                        for (int j = 0; j < childData.getChildrenData().size(); j++) {

                            SubChildData subChildData = childData.getChildrenData().get(j);
                            LogUtils.e("2", category_id + " - " + subChildData.getId() + "  = " + category_id.equalsIgnoreCase("" + subChildData.getId()));
                            if (category_id.equalsIgnoreCase("" + subChildData.getId())) {
//                                if (j == 0) {
//                                    mCategoryName = "Kids->" + childData.getName().trim() + "->All";
//                                } else {
                                mCategoryName = getResources().getString(R.string.kids) + "->" + childData.getName().trim() + "->" + subChildData.getName();
//                                }
                                return mCategoryName;
                            }

                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
                                    LogUtils.e("3", category_id + " - " + mChileData.getId() + "  = " + category_id.equalsIgnoreCase("" + mChileData.getId()));
                                    if (category_id.equalsIgnoreCase("" + mChileData.getId())) {
//                                        if (k == 0) {
//                                            mCategoryName = "Kids->" + childData.getName().trim() + "->" + mChileData.getName().trim() + "->All";
//                                        } else {
                                        mCategoryName = getResources().getString(R.string.kids) + "->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChileData.getName();
//                                        }
                                    }
                                    return mCategoryName;
                                }
                            }
                        }
                    }
                }
            }

        } else {

            if (mWomenCategoryData != null) {
                for (int i = 0; i < mWomenCategoryData.getChildrenData().size(); i++) {
                    CategoryList.ChildData childData = mWomenCategoryData.getChildrenData().get(i);
                    if (category_id.equalsIgnoreCase("" + childData.getId())) {
//                        if (i == 0) {
//                            mCategoryName = "Women->All";
//                        } else {
                        mCategoryName = getResources().getString(R.string.women) + "->" + childData.getName();
//                        }
                        return mCategoryName;
                    }

                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
                        for (int j = 0; j < childData.getChildrenData().size(); j++) {

                            SubChildData subChildData = childData.getChildrenData().get(j);
                            if (category_id.equalsIgnoreCase("" + subChildData.getId())) {
//                                if (j == 0) {
//                                    mCategoryName = "Women->" + childData.getName().trim() + "->All";
//                                } else {
                                mCategoryName = getResources().getString(R.string.women) + "->" + childData.getName().trim() + "->" + subChildData.getName();
//                                }
                                return mCategoryName;
                            }

                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
                                    if (category_id.equalsIgnoreCase("" + mChileData.getId())) {
//                                        if (k == 0) {
//                                            mCategoryName = "Women->" + childData.getName().trim() + "->" + mChileData.getName().trim() + "->All";
//                                        } else {
                                        mCategoryName = getResources().getString(R.string.women) + "->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChileData.getName();
//                                        }
                                        return mCategoryName;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (mMenCategoryData != null) {
                for (int i = 0; i < mMenCategoryData.getChildrenData().size(); i++) {
                    CategoryList.ChildData childData = mMenCategoryData.getChildrenData().get(i);
                    if (category_id.equalsIgnoreCase("" + childData.getId())) {
//                        if (i == 0) {
//                            mCategoryName = "Men->All";
//                        } else {
                        mCategoryName = getResources().getString(R.string.men) + "->" + childData.getName();
//                        }
                        return mCategoryName;
                    }

                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
                        for (int j = 0; j < childData.getChildrenData().size(); j++) {

                            SubChildData subChildData = childData.getChildrenData().get(j);
                            if (category_id.equalsIgnoreCase("" + subChildData.getId())) {
//                                if (j == 0) {
//                                    mCategoryName = "Men->" + childData.getName().trim() + "->All";
//                                } else {
                                mCategoryName = getResources().getString(R.string.men) + "->" + childData.getName().trim() + "->" + subChildData.getName();
//                                }
                                return mCategoryName;
                            }

                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
                                    if (category_id.equalsIgnoreCase("" + mChileData.getId())) {
//                                        if (k == 0) {
//                                            mCategoryName = "Men->" + childData.getName().trim() + "->" + mChileData.getName().trim() + "->All";
//                                        } else {
                                        mCategoryName = getResources().getString(R.string.men) + "->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChileData.getName();
//                                        }
                                        return mCategoryName;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (mKidCategoryData != null) {
                for (int i = 0; i < mKidCategoryData.getChildrenData().size(); i++) {
                    CategoryList.ChildData childData = mKidCategoryData.getChildrenData().get(i);
                    if (category_id.equalsIgnoreCase("" + childData.getId())) {
//                        if (i == 0) {
//                            mCategoryName = "Kids->All";
//                        } else {
                        mCategoryName = getResources().getString(R.string.kids) + "->" + childData.getName();
//                        }
                        return mCategoryName;
                    }

                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
                        for (int j = 0; j < childData.getChildrenData().size(); j++) {

                            SubChildData subChildData = childData.getChildrenData().get(j);
                            if (category_id.equalsIgnoreCase("" + subChildData.getId())) {
//                                if (j == 0) {
//                                    mCategoryName = "Kids->" + childData.getName().trim() + "->All";
//                                } else {
                                mCategoryName = getResources().getString(R.string.kids) + "->" + childData.getName().trim() + "->" + subChildData.getName();
//                                }
                                return mCategoryName;
                            }

                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
                                    if (category_id.equalsIgnoreCase("" + mChileData.getId())) {
//                                        if (k == 0) {
//                                            mCategoryName = "Kids->" + childData.getName().trim() + "->" + mChileData.getName().trim() + "->All";
//                                        } else {
                                        mCategoryName = getResources().getString(R.string.kids) + "->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChileData.getName();
//                                        }
                                    }
                                    return mCategoryName;
                                }
                            }
                        }
                    }
                }
            }

        }


        return mCategoryName;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_FILTER_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    jFilterObject = new JSONObject(data.getStringExtra(FilterNewActivity.BNDL_FILTER_JSON));
//                    sortby = data.getStringExtra(FilterNewActivity.BNDL_SORT_BY);
//                    if (sortby.equalsIgnoreCase("price")) {
//                        direction = data.getStringExtra(FilterNewActivity.BNDL_DIRECTION);
//                    } else {
//                        direction = recievedDirection;
//                    }
                    viewType = data.getStringExtra(FilterNewActivity.BNDL_VIEW_TYPE);
                    category_id = data.getStringExtra(FilterNewActivity.BNDL_CATEGORY_ID);
                    price = data.getStringExtra(FilterNewActivity.BNDL_PRICE);
                    boolean isReset = data.getBooleanExtra(FilterNewActivity.BNDL_IS_RESET, false);
                    if (isReset) {
                        category_id = recievedCategoryId;
                    }
//                    if (!TextUtils.isEmpty(viewType) && viewType.equalsIgnoreCase("list")) {
//                        setItemTypeView(ItemType.ListType);
//                    } else {
//                        setItemTypeView(ItemType.GridType);
//                    }

                    LogUtils.e("", "sortby::" + sortby);
                    LogUtils.e("", "direction::" + direction);
                    LogUtils.e("", "1viewType::" + viewType);
                    LogUtils.e("", "price::" + price);
                    mUrlQueryPostFix = "";
                    mPagerPosition = 0;
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            showProductView();
                        }
                    });

                    LogUtils.e("", "jFilterObject::" + jFilterObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.setFirebaseLog("Product_Items");
        }

//        if (!TextUtils.isEmpty(searchCriteria) && isAdded()) {
//            showProductView();
////            getCategoryList();
////            new Handler().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    getFilterData();
////                }
////            }, 10);
//        }
    }

}
