package shy7lo.com.shy7lo.fragment;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.plugin.AdjustCriteo;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.adapter.SearchProductsAlgoliaAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.SearchAlgoriaProductList;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;
import shy7lo.com.shy7lo.widget.BadgeView;

/**
 * Created by jiten on 2/9/16.
 */
public class SearchAlgoliaFragment extends Fragment implements View.OnClickListener {

    public static String TAG_SEARCH_FRAGMENT = "SearchAlgoliaFragment";

    @BindView(R.id.mTopLayout)
    View mTopLayout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvEmptySearch)
    TextView tvEmptySearch;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ibMore)
    ImageButton ibMore;
    @BindView(R.id.ibCancel)
    ImageButton ibCancel;
    @BindView(R.id.ibShoppingBags)
    ImageButton ibShoppingBags;
    @BindView(R.id.lnrProgressBar)
    LinearLayout lnrProgressBar;
    @BindView(R.id.lnrCancel)
    LinearLayout lnrCancel;

    @BindView(R.id.ivHeaderList)
    ImageView ivHeaderList;
    @BindView(R.id.ivHeaderGrid)
    ImageView ivHeaderGrid;
    @BindView(R.id.pbSearch)
    ProgressBar pbSearch;
    @BindView(R.id.lnrSearchView)
    LinearLayout lnrSearchView;
    @BindView(R.id.lnrTitle)
    LinearLayout lnrTitle;
    @BindView(R.id.rltSearch)
    RelativeLayout rltSearch;

    @BindView(R.id.gvProductItems)
    GridView gvProductItems;
    SearchProductsAlgoliaAdapter mSearchProductsAlgoliaAdapter;

//    @BindView(R.id.rvProductItems)
//    RecyclerView rvProductItems;
//    private SearchProductItemsRecylerAdapter mAdapter;
//    private LinearLayoutManager mLayoutManager;
//    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    List<SearchAlgoriaProductList.Hit> productItemList = new ArrayList<>();
    private int firstVisibleItemPosition;

    public BadgeView itemBadgeView;
    private int mCartItemCount;
    private int fiveDp, threeDp;
    Handler mHandler = new Handler();
    Runnable mRunnable;
    String searchString = "", mPreviousSearchKey = "";

    private boolean isLastPage = false;
    private boolean isLoading = false, isLoadingMore = false;
    public static int PAGE_SIZE = 1;
    private int page_number = 0; //1
    ArrayList<String> mPageNumberList = new ArrayList<>();

    public static String BNDL_SEARCH_KEY = "BNDL_SEARCH_KEY";

    View mView;

    private static SearchAlgoliaFragment searchFragment;

    public static SearchAlgoliaFragment getInstance() {
        if (searchFragment == null) {
            searchFragment = new SearchAlgoliaFragment();
        }
        return searchFragment;
    }

    private Context mContext;

    private Context getMyActivity() {
        return mContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        mContext = getActivity();
        Utils.setLocale(getActivity());
        ButterKnife.bind(this, mView);
        resetData();
        InitializeControls();
        InitializeControlsAction();

        LogUtils.e("", "language::" + getResources().getConfiguration().locale);
        return mView;
    }

    private void resetData() {
        mCartItemCount = 0;
        searchString = "";
        mPreviousSearchKey = "";
        isLastPage = false;
        isLoading = false;
        isLoadingMore = false;
        PAGE_SIZE = 1;
        page_number = 0;
        if (mPageNumberList != null && mPageNumberList.size() > 0){
            mPageNumberList.clear();
        }
    }

    private void InitializeControls() {

//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        rvProductItems.setLayoutManager(mStaggeredGridLayoutManager);
        setItemTypeView(ProductsItemsFragment.ItemType.GridType);

//        rvProductItems.setItemViewCacheSize(20);
//        rvProductItems.setDrawingCacheEnabled(true);
//        rvProductItems.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        ibMore.setImageResource(R.drawable.ic_back);

        tvEmptySearch.setVisibility(View.VISIBLE);

        fiveDp = (int) Utils.pxFromDp(getActivity(), 5);
        threeDp = (int) Utils.pxFromDp(getActivity(), 2);

        itemBadgeView = new BadgeView(getActivity(), ibShoppingBags, 11);
        itemBadgeView.setBadgeMargin(threeDp, threeDp);

        mCartItemCount = MyPref.getPref(getActivity(), MyPref.CART_ITEM_COUNT, 0);
//        mCartItemCount = 5;
        itemBadgeView.setText("" + mCartItemCount);

//        if (mCartItemCount > 0) {
//            itemBadgeView.show();
//        } else {
        itemBadgeView.hide();
//        }
        ibShoppingBags.setVisibility(View.GONE);


        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            tvTitle.setBackgroundResource(R.drawable.ic_top_logo_ar);
            tvTitle.setScaleX(-1f);
            itemBadgeView.setScaleX(-1f);
            mTopLayout.setScaleX(-1f);
            lnrCancel.setScaleX(-1f);
            gvProductItems.setScaleX(-1f);
            lnrTitle.setScaleX(-1f);
            rltSearch.setScaleX(-1f);
            etSearch.setGravity(Gravity.RIGHT);
            etSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lnrCancel.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lnrCancel.setLayoutParams(params);

        } else {
            tvTitle.setBackgroundResource(R.drawable.ic_top_logo);
            tvTitle.setScaleX(1f);
            itemBadgeView.setScaleX(1f);
            mTopLayout.setScaleX(1f);
            lnrCancel.setScaleX(1f);
            gvProductItems.setScaleX(1f);
            lnrTitle.setScaleX(1f);
            rltSearch.setScaleX(1f);
            etSearch.setGravity(Gravity.LEFT);
            etSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lnrCancel.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lnrCancel.setLayoutParams(params);

        }

        mRunnable = new Runnable() {
            @Override
            public void run() {
                searchString = etSearch.getText().toString();
                if (!TextUtils.isEmpty(searchString) && searchString.length() > 2) {
                    showSearchView(searchString);
                }
            }
        };

        searchString = "";


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    ibCancel.setVisibility(View.VISIBLE);
                } else {
                    ibCancel.setVisibility(View.GONE);
                    lnrSearchView.setVisibility(View.GONE);
                    tvEmptySearch.setVisibility(View.VISIBLE);
//                    tvEmptySearch.setText(getString(R.string.msg_empty_search));
                    tvEmptySearch.setText("");
                }
                searchString = charSequence.toString();
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 1000);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSearchProductsAlgoliaAdapter = new SearchProductsAlgoliaAdapter(getActivity(), R.layout.grid_item_products, productItemList, gvProductItems);
        gvProductItems.setAdapter(mSearchProductsAlgoliaAdapter);
        gvProductItems.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstVisibleItemPosition = firstVisibleItem;

                LogUtils.e("", "firstVisibleItemPosition::" + firstVisibleItemPosition);
                LogUtils.e("", "visibleItemCount::" + visibleItemCount);
                LogUtils.e("", "totalItemCount::" + totalItemCount);

                if (productItemList.size() > 1) {
                    page_number = (productItemList.size() / 16) - 1;
                    if (page_number < 0) {
                        page_number = 0;
                    }
                }

                int boundCount = (16 * (page_number - 1)) + 1;
//                LogUtils.e("", "firstVisibleItem::" + firstVisibleItem + " boundCount::" + boundCount + " page_number::" + page_number);
//                if (page_number > 1) {
                if (page_number > 0) {
                    if (firstVisibleItem >= boundCount && boundCount > 16) {
                        if (!isLoading && !isLastPage) {
//                        if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {

                            isLoading = true;
                            page_number = page_number + 1;
//                            LogUtils.e("", "Load New more::" + page_number);
                            showNextSearchView(searchString, page_number);
//                        }
                        }
                    }
                }

                if (!isLoadingMore && !isLastPage) {
                    if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {
                        LogUtils.e("", "Load more");
                        isLoadingMore = true;
                        page_number = page_number + 1;
                        showNextLoadMoreSearchView(searchString, page_number);
                    }
                }
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(BNDL_SEARCH_KEY)) {
            String searchStr = bundle.getString(BNDL_SEARCH_KEY);
            if (!TextUtils.isEmpty(searchStr)) {
                etSearch.setText(searchStr);
            }
        }


    }

    private void InitializeControlsAction() {
        ibMore.setOnClickListener(this);
        ibShoppingBags.setOnClickListener(this);
        ivHeaderList.setOnClickListener(this);
        ivHeaderGrid.setOnClickListener(this);
        ibCancel.setOnClickListener(this);
        tvBack.setOnClickListener(this);
    }

    private void setItemTypeView(ProductsItemsFragment.ItemType itemType) {
        switch (itemType) {
            case ListType:
                ivHeaderList.setSelected(true);
                ivHeaderGrid.setSelected(false);
                break;
            case GridType:
                ivHeaderList.setSelected(false);
                ivHeaderGrid.setSelected(true);
                break;
        }
    }

    public void setView() {
//        mAdapter = new SearchProductItemsRecylerAdapter(getActivity(), R.layout.grid_item_products, productItemList, ProductsItemsFragment.ItemType.GridType, rvProductItems);
//        rvProductItems.setAdapter(mAdapter);
    }

//    public void showSearchView(final String searchKey) {
//
//        if (!Utils.isInternetConnected(getActivity())) {
//            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
//                @Override
//                public void onRetryClicked(Dialog dialog) {
//                    if (Utils.isInternetConnected(getActivity())) {
//                        if (dialog != null && dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        showSearchView(searchKey);
//                    }
//                }
//            });
//            return;
//        }
//
////        Utils.showProgressDialog(getActivity());
//        mPageNumberList = new ArrayList<>();
//        pbSearch.setVisibility(View.VISIBLE);
//
//        if (productItemList != null && productItemList.size() > 0) {
//            productItemList.clear();
//        }
//
////        page_number = 1;
//        page_number = 0;
//
//        Map<String, Object> jsonParams = new ArrayMap<>();
//        jsonParams.put("searchQuery", "" + searchKey);
//        jsonParams.put("currentPage", page_number);
//
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//        Call<SearchAlgoriaProductList> callCode = apiService.getAlgoriaSearchProduct(Shy7lo.mLangCode, body);
//
//        callCode.enqueue(new Callback<SearchAlgoriaProductList>() {
//            @Override
//            public void onResponse(Call<SearchAlgoriaProductList> call, Response<SearchAlgoriaProductList> response) {
//
//                pbSearch.setVisibility(View.GONE);
//                isLoading = false;
//                Utils.closeProgressDialog();
//
//                if (response.isSuccessful()) {
//
//                    SearchAlgoriaProductList productList = response.body();
//                    if (productList != null && productList.success.equals("1")) {
//
//                        if (productList.data != null && productList.data.hits.size() > 0) {
//                            productItemList = productList.data.hits;
//                            lnrSearchView.setVisibility(View.VISIBLE);
//                            tvEmptySearch.setVisibility(View.GONE);
//
//                            if (mPageNumberList != null && mPageNumberList.size() > 0) {
//                                mPageNumberList.clear();
//                                mPageNumberList.add("" + page_number);
//                            }
//
//                            PAGE_SIZE = productList.data.nbHits;
//
//                            if (productItemList.size() % 16 == 0 && productItemList.size() < PAGE_SIZE) {
//                                isLastPage = false;
//                            } else {
//                                isLastPage = true;
//                            }
//
//                            mSearchProductsAlgoliaAdapter.changeData(productItemList);
//
//                            if (productItemList == null || productItemList.size() == 0) {
//                                Utils.showToast(getMyActivity(), "" + getString(R.string.msg_no_result));
//                            } else {
//                                if (!isLoading && !isLastPage) {
////                                    if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {
//                                    LogUtils.e("", "Load more");
//                                    isLoading = true;
//                                    page_number = page_number + 1;
//                                    showNextSearchView(searchString, page_number);
////                                    }
//                                }
//                            }
////                            setSignoutView();
//                        } else {
////                            Utils.showToast(getActivity(), "" + getString(R.string.msg_no_result));
//                            if (MyPref.getPref(getMyActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                                tvEmptySearch.setText("لم يتم العثور على نتائج مطابقة للبحث");
//                            } else {
//                                tvEmptySearch.setText("No result found");
//                            }
//
//                            lnrSearchView.setVisibility(View.GONE);
//                            tvEmptySearch.setVisibility(View.VISIBLE);
//                        }
//                    } else if (productList != null && productList.success.equals("0")) {
//                        Utils.showToast(getMyActivity(), "" + productList.message);
//                        lnrSearchView.setVisibility(View.GONE);
//                        tvEmptySearch.setVisibility(View.VISIBLE);
//                    } else if (productList != null && productList.success.equals("2")) {
//                        Utils.showInitialScreen(getActivity());
//                        return;
//                    } else {
//                        Utils.showToast(getMyActivity(), "" + getResources().getString(R.string.msg_something_wrong));
//                        lnrSearchView.setVisibility(View.GONE);
//                        tvEmptySearch.setVisibility(View.VISIBLE);
//                    }
//
//
//                } else {
//                    LogUtils.e("", "respnse not great");
//                    lnrSearchView.setVisibility(View.GONE);
//                    Utils.showAlertDialog(getMyActivity());
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<SearchAlgoriaProductList> call, Throwable t) {
//                LogUtils.e("", "onFailure call::" + t.getMessage());
//                Utils.closeProgressDialog();
//                pbSearch.setVisibility(View.GONE);
//                lnrSearchView.setVisibility(View.GONE);
//                tvEmptySearch.setVisibility(View.VISIBLE);
////                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
////                    Utils.showToast(getActivity(), "خطأ");
////                } else {
////                    Utils.showToast(getActivity(), "Something went wrong");
////                }
//                isLoading = false;
////                page_number = 1;
//                page_number = 0;
//                Utils.showAlertDialog(getMyActivity());
//            }
//        });
//    }

    public void showSearchView(final String searchKey) {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        showSearchView(searchKey);
                    }
                }
            });
            return;
        }

//        Utils.showProgressDialog(getActivity());
        mPageNumberList = new ArrayList<>();
        pbSearch.setVisibility(View.VISIBLE);

        if (productItemList != null && productItemList.size() > 0) {
            productItemList.clear();
        }

//        page_number = 1;
        page_number = 0;

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("searchQuery", "" + searchKey);
        jsonParams.put("currentPage", "" + page_number);

//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<SearchAlgoriaProductList> callCode = apiService.getAlgoriaSearchProduct(Shy7lo.mLangCode, jsonParams);

        callCode.enqueue(new Callback<SearchAlgoriaProductList>() {
            @Override
            public void onResponse(Call<SearchAlgoriaProductList> call, Response<SearchAlgoriaProductList> response) {

                pbSearch.setVisibility(View.GONE);
                isLoading = false;
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {

                    SearchAlgoriaProductList productList = response.body();
                    if (productList != null && productList.success.equals("1")) {

                        if (productList.data != null && productList.data.hits.size() > 0) {
                            productItemList = productList.data.hits;
                            lnrSearchView.setVisibility(View.VISIBLE);
                            tvEmptySearch.setVisibility(View.GONE);

                            if (mPageNumberList != null && mPageNumberList.size() > 0) {
                                mPageNumberList.clear();
                                mPageNumberList.add("" + page_number);
                            }

                            PAGE_SIZE = productList.data.nbHits;

                            if (productItemList.size() % 16 == 0 && productItemList.size() < PAGE_SIZE) {
                                isLastPage = false;
                            } else {
                                isLastPage = true;
                            }

                            mSearchProductsAlgoliaAdapter.changeData(productItemList);

                            if (productItemList == null || productItemList.size() == 0) {
                                Utils.showToast(getMyActivity(), "" + getString(R.string.msg_no_result));
                            } else {
                                if (!isLoading && !isLastPage) {
//                                    if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {
                                    LogUtils.e("", "Load more");
                                    isLoading = true;
                                    page_number = page_number + 1;
                                    showNextSearchView(searchString, page_number);
//                                    }
                                }
                            }

//                            setSignoutView();
                        } else {
//                            Utils.showToast(getActivity(), "" + getString(R.string.msg_no_result));
                            if (MyPref.getPref(getMyActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                                tvEmptySearch.setText("لم يتم العثور على نتائج مطابقة للبحث");
                            } else {
                                tvEmptySearch.setText("No result found");
                            }

                            lnrSearchView.setVisibility(View.GONE);
                            tvEmptySearch.setVisibility(View.VISIBLE);
                        }

                        try {
                            AdjustEvent event = new AdjustEvent("f7uwk3");
                            event.addPartnerParameter("Keyword", "" + searchKey);
                            //Callback
                            event.addCallbackParameter("Keyword", "" + searchKey);

                            String mCriteo = MyPref.getPref(getActivity(), MyPref.APP_CRITEO, "");
                            if (TextUtils.isEmpty(mCriteo) || mCriteo.equalsIgnoreCase("1")) {
                                String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
                                AdjustCriteo.injectCustomerIdIntoCriteoEvents(TextUtils.isEmpty(userToken) ? userToken : MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""));
                            }

                            Adjust.trackEvent(event);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (RestClient.isFacebookLive) {
                            try {
                                if (!mPreviousSearchKey.equals(searchKey)) {
                                    // FB Log Search Product
                                    AppEventsLogger logger = AppEventsLogger.newLogger(getActivity());

                                    Bundle parameters = new Bundle();
                                    parameters.putString(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, "" + searchKey);

                                    logger.logEvent(AppEventsConstants.EVENT_NAME_SEARCHED, parameters);
                                    mPreviousSearchKey = searchKey;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } else if (productList != null && productList.success.equals("0")) {
                        Utils.showToast(getMyActivity(), "" + productList.message);
                        lnrSearchView.setVisibility(View.GONE);
                        tvEmptySearch.setVisibility(View.VISIBLE);
                    } else if (productList != null && productList.success.equals("2")) {
                        Utils.showInitialScreen(getActivity());
                        return;
                    } else {
                        Utils.showToast(getMyActivity(), "" + getResources().getString(R.string.msg_something_wrong));
                        lnrSearchView.setVisibility(View.GONE);
                        tvEmptySearch.setVisibility(View.VISIBLE);
                    }


                } else {
                    LogUtils.e("", "respnse not great");
                    lnrSearchView.setVisibility(View.GONE);
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
            public void onFailure(Call<SearchAlgoriaProductList> call, Throwable t) {
                LogUtils.e("", "onFailure call::" + t.getMessage());
                Utils.closeProgressDialog();
                pbSearch.setVisibility(View.GONE);
                lnrSearchView.setVisibility(View.GONE);
                tvEmptySearch.setVisibility(View.VISIBLE);
//                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    Utils.showToast(getActivity(), "خطأ");
//                } else {
//                    Utils.showToast(getActivity(), "Something went wrong");
//                }
                isLoading = false;
//                page_number = 1;
                page_number = 0;
                Utils.showAlertDialog(getMyActivity(), "" + t.getMessage());
            }
        });
    }

    public void showNextSearchView(String searchKey, final int page_number) {

//        Utils.showProgressDialog(getActivity());

//        pbSearch.setVisibility(View.VISIBLE);

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("searchQuery", "" + searchKey);
        jsonParams.put("currentPage", "" + page_number);

//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<SearchAlgoriaProductList> callCode = apiService.getAlgoriaSearchProduct(Shy7lo.mLangCode, jsonParams);

        callCode.enqueue(new Callback<SearchAlgoriaProductList>() {
            @Override
            public void onResponse(Call<SearchAlgoriaProductList> call, Response<SearchAlgoriaProductList> response) {

                pbSearch.setVisibility(View.GONE);
                isLoading = false;
                isLoadingMore = false;
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {

                    SearchAlgoriaProductList productList = response.body();
                    if (productList != null && productList.success.equals("1")) {

                        if (productList.data != null && productList.data.hits.size() > 0) {

                            LogUtils.e("", page_number + " page_number Contains" + mPageNumberList.contains("" + page_number));
                            if (!mPageNumberList.contains("" + page_number)) {
                                productItemList.addAll(productList.data.hits);
                                mPageNumberList.add("" + page_number);
                            }
//                            lnrSearchView.setVisibility(View.VISIBLE);
//                            tvEmptySearch.setVisibility(View.GONE);

//                            PAGE_SIZE = productList.data.getTotalCount();

                            if (productItemList.size() % 16 == 0 && productItemList.size() < PAGE_SIZE) {
                                isLastPage = false;
                            } else {
                                isLastPage = true;
                            }

                            mSearchProductsAlgoliaAdapter.changeData(productItemList);

//                            setSignoutView();
                        } else {
//                            Utils.showToast(getActivity(), "" + getString(R.string.msg_no_result));
//                            tvEmptySearch.setText("" + getString(R.string.msg_no_result));
//                            lnrSearchView.setVisibility(View.GONE);
//                            tvEmptySearch.setVisibility(View.VISIBLE);
                        }
                    } else if (productList != null && productList.success.equals("0")) {
//                        Utils.showToast(getActivity(), "" + productList.message);
//                        lnrSearchView.setVisibility(View.GONE);
//                        tvEmptySearch.setVisibility(View.VISIBLE);
                    } else if (productList != null && productList.success.equals("2")) {
                        Utils.showInitialScreen(getActivity());
                        return;
                    } else {
//                        Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
//                        lnrSearchView.setVisibility(View.GONE);
//                        tvEmptySearch.setVisibility(View.VISIBLE);
                    }


                } else {
                    lnrSearchView.setVisibility(View.GONE);
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
            public void onFailure(Call<SearchAlgoriaProductList> call, Throwable t) {
                LogUtils.e("", "onFailure call::" + t.getMessage());
                Utils.closeProgressDialog();
                pbSearch.setVisibility(View.GONE);
                isLoading = false;
//                lnrSearchView.setVisibility(View.GONE);
//                tvEmptySearch.setVisibility(View.VISIBLE);
//                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
            }
        });
    }

    public void showNextLoadMoreSearchView(String searchKey, final int page_number) {

//        Utils.showProgressDialog(getActivity());

        lnrProgressBar.setVisibility(View.VISIBLE);

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("searchQuery", "" + searchKey);
        jsonParams.put("currentPage", "" + page_number);

//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<SearchAlgoriaProductList> callCode = apiService.getAlgoriaSearchProduct(Shy7lo.mLangCode, jsonParams);

        callCode.enqueue(new Callback<SearchAlgoriaProductList>() {
            @Override
            public void onResponse(Call<SearchAlgoriaProductList> call, Response<SearchAlgoriaProductList> response) {

                lnrProgressBar.setVisibility(View.GONE);
                isLoadingMore = false;

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {

                    SearchAlgoriaProductList productList = response.body();
                    if (productList != null && productList.success.equals("1")) {

                        if (productList.data != null && productList.data.hits.size() > 0) {

                            LogUtils.e("showNextLoadMoreSearchView", page_number + " page_number Contains" + mPageNumberList.contains("" + page_number));
                            if (!mPageNumberList.contains("" + page_number)) {
                                productItemList.addAll(productList.data.hits);
                                mPageNumberList.add("" + page_number);
                            }
//                            lnrSearchView.setVisibility(View.VISIBLE);
//                            tvEmptySearch.setVisibility(View.GONE);

//                            PAGE_SIZE = productList.data.getTotalCount();

                            if (productItemList.size() % 16 == 0 && productItemList.size() < PAGE_SIZE) {
                                isLastPage = false;
                            } else {
                                isLastPage = true;
                            }

                            mSearchProductsAlgoliaAdapter.changeData(productItemList);

//                            setSignoutView();
                        } else {
//                            Utils.showToast(getActivity(), "" + getString(R.string.msg_no_result));
//                            tvEmptySearch.setText("" + getString(R.string.msg_no_result));
//                            lnrSearchView.setVisibility(View.GONE);
//                            tvEmptySearch.setVisibility(View.VISIBLE);
                        }
                    } else if (productList != null && productList.success.equals("0")) {
//                        Utils.showToast(getActivity(), "" + productList.message);
//                        lnrSearchView.setVisibility(View.GONE);
//                        tvEmptySearch.setVisibility(View.VISIBLE);
                    } else if (productList != null && productList.success.equals("2")) {
                        Utils.showInitialScreen(getActivity());
                        return;
                    } else {
//                        Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
//                        lnrSearchView.setVisibility(View.GONE);
//                        tvEmptySearch.setVisibility(View.VISIBLE);
                    }


                } else {
                    lnrSearchView.setVisibility(View.GONE);
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
            public void onFailure(Call<SearchAlgoriaProductList> call, Throwable t) {
                LogUtils.e("", "onFailure call::" + t.getMessage());
                Utils.closeProgressDialog();
                pbSearch.setVisibility(View.GONE);
                isLoadingMore = false;
//                lnrSearchView.setVisibility(View.GONE);
//                tvEmptySearch.setVisibility(View.VISIBLE);
//                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == ibMore) {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
//                activity.openDrawer();
                activity.onBackPressed();
            }
        } else if (view == ibShoppingBags) {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.loadShoppingBags();
            }
        } else if (view == tvBack) {

            etSearch.setText("");
            searchString = "";
            if (productItemList != null && productItemList.size() > 0) {
                productItemList.clear();
            }
            if (mSearchProductsAlgoliaAdapter != null) {
                mSearchProductsAlgoliaAdapter.changeData(productItemList);
            }

            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.onBackPressed();
            }

        } else if (view == ivHeaderList) {
            setItemTypeView(ProductsItemsFragment.ItemType.ListType);
//
////            int firstVisiblePosition = ((StaggeredGridLayoutManager)rvProductItems.getLayoutManager()).findFirstVisibleItemPositions();
//            int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
//            int secondVisibleItem = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(null)[0];
//            Log.e("", "firstVisibleItem::" + firstVisibleItem + " secondVisibleItem::" + secondVisibleItem);
//
//            rvProductItems.setLayoutManager(mLayoutManager);
////            mAdapter = new ProductItemsRecylerAdapter(getActivity(), R.layout.list_item_products, productItemList, ItemType.GridType);
//            rvProductItems.setAdapter(mAdapter);
////            mAdapter.setItemType(ItemType.ListType);
////            mAdapter.notifyDataSetChanged();

            gvProductItems.setNumColumns(1);
            gvProductItems.setSelection(firstVisibleItemPosition);

        } else if (view == ivHeaderGrid) {
            setItemTypeView(ProductsItemsFragment.ItemType.GridType);

//            int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
//            int secondVisibleItem = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(null)[0];
//            Log.e("", "firstVisibleItem::" + firstVisibleItem + " secondVisibleItem::" + secondVisibleItem);
//
//            rvProductItems.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
////            mAdapter = new ProductItemsRecylerAdapter(getActivity(), R.layout.grid_item_products, productItemList, ItemType.GridType);
//            rvProductItems.setAdapter(mAdapter);
////            mAdapter.setItemType(ItemType.GridType);
////            mAdapter.notifyDataSetChanged();

            gvProductItems.setNumColumns(2);
            gvProductItems.setSelection(firstVisibleItemPosition);

        } else if (view == ibCancel) {
            etSearch.setText("");
            searchString = "";
            if (productItemList != null && productItemList.size() > 0) {
                productItemList.clear();
            }
            if (mSearchProductsAlgoliaAdapter != null) {
                mSearchProductsAlgoliaAdapter.changeData(productItemList);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e("", "onStart call");

//        if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""))) {
//            Utils.getGuestCartToken(getActivity());
//        }

        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.setDrawerSwipe(false);
            activity.setFirebaseLog("Search_Products");
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

}
