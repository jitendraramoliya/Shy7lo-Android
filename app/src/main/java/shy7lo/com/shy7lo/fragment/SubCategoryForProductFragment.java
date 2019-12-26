package shy7lo.com.shy7lo.fragment;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.adapter.SubCategoryProductAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.CategoryProductList;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

import static shy7lo.com.shy7lo.pref.MyPref.getPref;

/**
 * Created by Jiten on 31-03-2018.
 */

public class SubCategoryForProductFragment extends Fragment implements View.OnClickListener {

    public static String TAG_SUB_CATEGORY_FOR_PRODUCT_FRAGMENT = "SubCategoryForProductFragment";

    View mView, mHeaderView;

    //    @BindView(R.id.rltTopLayout)
//    RelativeLayout rltTopLayout;
//    @BindView(R.id.ibBack)
//    ImageButton ibBack;
    @BindView(R.id.mTopLayout)
    View mTopLayout;
    @BindView(R.id.ibMore)
    ImageButton ibMore;
    @BindView(R.id.ibRightIcons)
    ImageButton ibRightIcons;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.lvCategoryProduct)
    ListView lvCategoryProduct;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ibCloseIcons)
    ImageButton ibCloseIcons;
    @BindView(R.id.lnrSearchBar)
    LinearLayout lnrSearchBar;

    Runnable mRunnable;
    Handler mHandler = new Handler();

    LinearLayout lnrCategoryTop;


    SubCategoryProductAdapter mSubCategoryProductAdapter;
    List<CategoryProductList.CategoryChild> mCategoryChildList = new ArrayList<>();

    String mProductID = "", mMainCateID = "", mMainCateName = "";

    public static String BK_PRODUCT_ID = "BK_PRODUCT_ID";
    public static String BNDL_MAIN_CATE_ID = "BNDL_MAIN_CATE_ID";
    public static String BNDL_MAIN_CATE_NAME = "BNDL_MAIN_CATE_NAME";

    static SubCategoryForProductFragment mSubCategoryForProductFragment;

    public static SubCategoryForProductFragment getInstance() {
//        if (mSubCategoryForProductFragment == null) {
        mSubCategoryForProductFragment = new SubCategoryForProductFragment();
//        }
        return mSubCategoryForProductFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_category_for_product, container, false);
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
            activity.setDrawerSwipe(false);
        }

    }

    private void InitializeControls() {

        mSubCategoryProductAdapter = new SubCategoryProductAdapter(getActivity(), this);
        lvCategoryProduct.setAdapter(mSubCategoryProductAdapter);

        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_category, null);
        lnrCategoryTop = (LinearLayout) mHeaderView.findViewById(R.id.lnrCategoryTop);
        lvCategoryProduct.addHeaderView(mHeaderView);

        ibMore.setImageResource(R.drawable.ic_back);

        if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

            tvTitle.setBackgroundResource(R.drawable.ic_top_logo_ar);

            mTopLayout.setScaleX(-1f);
//            itemBadgeView.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            lvCategoryProduct.setScaleX(-1f);
            tvTitle.setTypeface(Shy7lo.RobotoMedium);
            etSearch.setScaleX(-1f);
            etSearch.setGravity(Gravity.RIGHT);

        } else {

            tvTitle.setBackgroundResource(R.drawable.ic_top_logo);

            mTopLayout.setScaleX(1f);
//            itemBadgeView.setScaleX(1f);
            tvTitle.setScaleX(1f);
            lvCategoryProduct.setScaleX(1f);
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

        Bundle bundle = getArguments();
        if (bundle != null) {
            mProductID = bundle.getString(BK_PRODUCT_ID);
            mMainCateID = bundle.getString(BNDL_MAIN_CATE_ID);
            mMainCateName = bundle.getString(BNDL_MAIN_CATE_NAME);
            LogUtils.e("", "main_category_id::" + mMainCateID);
            getCategoryForProductList();
        }


    }

    private void InitializeControlsAction() {
        ibMore.setOnClickListener(this);
        ibRightIcons.setOnClickListener(this);
        ibCloseIcons.setOnClickListener(this);
    }

    private void getCategoryForProductList() {

        Utils.showProgressDialog(getActivity());

//        ApiInterface apiService =
//                RestClient.getDynamicClient(Shy7lo.mLangCode.equalsIgnoreCase("ar") ? RestClient.API_SHYLABS_TEST_AR_URL : RestClient.API_SHYLABS_TEST_URL, true);

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        Call<CategoryProductList> callCode = apiService.getCategoryForProductList(Shy7lo.mLangCode, mProductID);
        callCode.enqueue(new Callback<CategoryProductList>() {
            @Override
            public void onResponse(Call<CategoryProductList> call, Response<CategoryProductList> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {

                    CategoryProductList mCategoryProductList = response.body();
                    if (mCategoryProductList != null && mCategoryProductList.success.equalsIgnoreCase("1")) {

                        setCategoryHeader(mCategoryProductList.data.category.banner);

                        mCategoryChildList = mCategoryProductList.data.category.childData;
                        mSubCategoryProductAdapter.addAll(mCategoryChildList);

                    }

                }else{
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
            public void onFailure(Call<CategoryProductList> call, Throwable t) {

            }
        });

//        Call<ProductCategoryList> callCode = apiService.getProductSubCategoryList(Shy7lo.mLangCode, mProductID);
//        callCode.enqueue(new Callback<ProductCategoryList>() {
//            @Override
//            public void onResponse(Call<ProductCategoryList> call, Response<ProductCategoryList> response) {
//
//                Utils.closeProgressDialog();
//
//                if (response.isSuccessful()) {
//
//                    ProductCategoryList mCategoryProductList = response.body();
//                    if (mCategoryProductList != null) {
//
////                        setCategoryHeader(mCategoryProductList.category.banner);
////
////                        mCategoryChildList = mCategoryProductList.category.childData;
////                        mSubCategoryProductAdapter.addAll(mCategoryChildList);
//
//
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ProductCategoryList> call, Throwable t) {
//
//            }
//        });

    }

    private void setCategoryHeader(final List<CategoryProductList.Banner> banner) {

        lnrCategoryTop.removeAllViews();

        if (banner != null && banner.size() > 0) {
            for (int i = 0; i < banner.size(); i++) {

                View mView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_sub_category_top_list, null);
                ImageView ivTopHeaderItem = (ImageView) mView.findViewById(R.id.ivTopHeaderItem);
                Picasso.with(getActivity()).load(banner.get(i).image).into(ivTopHeaderItem);

                if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    ivTopHeaderItem.setScaleX(-1f);
                } else {
                    ivTopHeaderItem.setScaleX(1f);
                }

                final int finalI = i;
                ivTopHeaderItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bundle bundle = new Bundle();
                        bundle.putString(ProductsItemsFragment.BK_PRODUCT_ITEMS, banner.get(finalI).url);
                        bundle.putString(ProductsItemsFragment.BNDL_CATEGORY, "" + mMainCateName);
                        bundle.putString(ProductsItemsFragment.BNDL_MAIN_CATE_ID, "" + mMainCateID);
                        bundle.putBoolean(ProductsItemsFragment.IS_FROM_BANNER, true);
                        LogUtils.e("123", "mMainCateID::" + mMainCateID);

                        HomeActivity activity = (HomeActivity) getActivity();
                        if (activity != null) {
                            activity.loadBannerItems(bundle);
                        }
                    }
                });

                lnrCategoryTop.addView(mView);

                if (i == banner.size() - 1){
                    View mViewAll = LayoutInflater.from(getActivity()).inflate(R.layout.header_view_all, null);
                    TextView tvViewAll = (TextView) mViewAll.findViewById(R.id.tvViewAll);
                    if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                        tvViewAll.setScaleX(-1f);
                    } else {
                        tvViewAll.setScaleX(1f);
                    }
                    mViewAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String mSearchCriteria = "category_id="+mProductID+"&sortby=created_at&direction=DESC&brand=null";
                            loadBundleItemWithID(mSearchCriteria);
                        }
                    });
                    lnrCategoryTop.addView(mViewAll);
                }
            }
        } else {
            View mViewAll = LayoutInflater.from(getActivity()).inflate(R.layout.header_view_all, null);
            TextView tvViewAll = (TextView) mViewAll.findViewById(R.id.tvViewAll);
            if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                tvViewAll.setScaleX(-1f);
            } else {
                tvViewAll.setScaleX(1f);
            }
            mViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mSearchCriteria = "category_id="+mProductID+"&sortby=created_at&direction=DESC&brand=null";
                    loadBundleItemWithID(mSearchCriteria);
                }
            });
            lnrCategoryTop.addView(mViewAll);
        }

    }

    public void loadBundleItemWithID(String mSearchCriteria) {

        Bundle bundle = new Bundle();
        bundle.putString(ProductsItemsFragment.BK_PRODUCT_ITEMS, mSearchCriteria);
        bundle.putString(ProductsItemsFragment.BNDL_CATEGORY, "" + mMainCateName);
        bundle.putString(ProductsItemsFragment.BNDL_MAIN_CATE_ID, "" + mMainCateID);
        bundle.putBoolean(ProductsItemsFragment.IS_FROM_BANNER, true);
        LogUtils.e("123", "mMainCateID::" + mMainCateID);

        HomeActivity activity = (HomeActivity) getActivity();
        if (activity != null) {
            activity.loadBannerItems(bundle);
        }
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
