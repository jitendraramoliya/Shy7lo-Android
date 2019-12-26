package shy7lo.com.shy7lo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.plugin.AdjustCriteo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.crashlytics.android.answers.AddToCartEvent;
import com.crashlytics.android.answers.Answers;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.WebviewActivity;
import shy7lo.com.shy7lo.adapter.ProductImagesAdapter;
import shy7lo.com.shy7lo.adapter.SimilarProductAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.listener.PaginationScrollListener;
import shy7lo.com.shy7lo.model.ProductDetails;
import shy7lo.com.shy7lo.model.SimilarProducts;
import shy7lo.com.shy7lo.model.Wishlist;
import shy7lo.com.shy7lo.notification.NotificationUtils;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.sync.WishListSync;
import shy7lo.com.shy7lo.utils.CustomTypefaceSpan;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;
import shy7lo.com.shy7lo.wheel.WheelSizePicker;
import shy7lo.com.shy7lo.widget.ExtendedViewPager;
import shy7lo.com.shy7lo.widget.ParallaxScrollView;
import shy7lo.com.shy7lo.widget.TouchImageView;

import static shy7lo.com.shy7lo.pref.MyPref.getPref;
import static shy7lo.com.shy7lo.utils.Constant.BNDL_IS_FROM_SEARCH;
import static shy7lo.com.shy7lo.utils.Constant.BNDL_SKU;


/**
 * Created by jiten on 2/9/16.
 */
public class ProductDetailsFragment extends Fragment implements View.OnClickListener {

    public static String TAG_PRODUCT_DETIAILS_FRAGMENT = "ProductDetailsFragment";

    @BindView(R.id.mTopLayout)
    View mTopLayout;
    @BindView(R.id.ibShare)
    ImageButton ibShare;
    @BindView(R.id.ibCancel)
    ImageButton ibCancel;
    @BindView(R.id.vpProductsImages)
//    ViewPager vpProductsImages;
            ExtendedViewPager vpProductsImages;
    @BindView(R.id.tab_layout)
    TabLayout tlIndicator;
    @BindView(R.id.wvDescription)
    WebView wvDescription;
    @BindView(R.id.wvDescriptionInfo)
    WebView wvDescriptionInfo;
    @BindView(R.id.tvFastShipping)
    TextView tvFastShipping;
    @BindView(R.id.tvDayReturn)
    TextView tvDayReturn;
    @BindView(R.id.tvLigal)
    TextView tvLigal;
    @BindView(R.id.tvCashReturn)
    TextView tvCashReturn;
    @BindView(R.id.ivFastShipping)
    ImageView ivFastShipping;
    @BindView(R.id.ivDayReturn)
    ImageView ivDayReturn;
    @BindView(R.id.ivLigal)
    ImageView ivLigal;
    @BindView(R.id.ivCashReturn)
    ImageView ivCashReturn;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvNewPrice)
    TextView tvNewPrice;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvSku)
    TextView tvSku;
    @BindView(R.id.tvNameInfo)
    TextView tvNameInfo;
    @BindView(R.id.tvColorTitle)
    TextView tvColorTitle;
    @BindView(R.id.tvDetailsTitle)
    TextView tvDetailsTitle;
    @BindView(R.id.tvColorInfo)
    TextView tvColorInfo;
    @BindView(R.id.tvProductSizeTitle)
    TextView tvProductSizeTitle;
    @BindView(R.id.tvProductSizeInfo)
    TextView tvProductSizeInfo;
    @BindView(R.id.tvFrameSizeTitle)
    TextView tvFrameSizeTitle;
    @BindView(R.id.tvFrameSizeInfo)
    TextView tvFrameSizeInfo;
    @BindView(R.id.tvFrameTypeTitle)
    TextView tvFrameTypeTitle;
    @BindView(R.id.tvFrameTypeInfo)
    TextView tvFrameTypeInfo;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvAdditionalInfoTitle)
    TextView tvAdditionalInfoTitle;
    @BindView(R.id.tvSkuTitle)
    TextView tvSkuTitle;
    @BindView(R.id.tvNameInfoTitle)
    TextView tvNameInfoTitle;
    @BindView(R.id.tvAddToBag)
    TextView tvAddToBag;
    @BindView(R.id.lnrAddToBag)
    LinearLayout lnrAddToBag;
    @BindView(R.id.tvSize)
    TextView tvSize;
    @BindView(R.id.tvColor)
    TextView tvColor;
    @BindView(R.id.tvColorIndicator)
    TextView tvColorIndicator;
    @BindView(R.id.ivColorIndicator)
    ImageView ivColorIndicator;
    @BindView(R.id.tvItemInfo)
    TextView tvItemInfo;
    @BindView(R.id.tvShopping)
    TextView tvShopping;
    @BindView(R.id.tvSizeTable)
    TextView tvSizeTable;
    @BindView(R.id.tvYouLike)
    TextView tvYouLike;
    @BindView(R.id.tbWishlist)
    ToggleButton tbWishlist;
    @BindView(R.id.lnrContainer)
    LinearLayout lnrContainer;
    @BindView(R.id.lnrItemInfo)
    LinearLayout lnrItemInfo;
    @BindView(R.id.lnrProgressBar)
    LinearLayout lnrProgressBar;
    @BindView(R.id.rltSimilarProduct)
    RelativeLayout rltSimilarProduct;
    @BindView(R.id.rvSimilarProductItems)
    RecyclerView rvSimilarProductItems;
    @BindView(R.id.psvScrollView)
    ParallaxScrollView psvScrollView;
    @BindView(R.id.mViewSpace)
    View mViewSpace;

    SimilarProductAdapter mSimilarProductAdapter;
    List<SimilarProducts.ProductListing> mSimilarProductListing = new ArrayList<>();

    private String mCurrencyCode = "";
    private float mExchangeRate;
    private float mFinalRate;


    ProductDetails productDetails;
    JSONObject jsonObject;

    String mSku = "", mOptionValue = "", mOptionId = "", mSizeLable = "";
    String special_from_date = "", special_to_date = "";
    float price = 0, special_price = 0;

    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private boolean isUsLable = false;
    private boolean isFromSearch = false;

    DBAdapter dbAdapter;

    View mView;

    private int TOTAL_PAGES, PAGE_NO_ITEM = 16, CURRENT_PAGE = 1;
    private boolean isLastPage, isLoading;


    static ProductDetailsFragment productDetailsFragment;

    public static ProductDetailsFragment getInstance() {
//        if (productDetailsFragment == null) {
        productDetailsFragment = new ProductDetailsFragment();
//        }
        return productDetailsFragment;
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
        mView = inflater.inflate(R.layout.fragment_product_details, container, false);
        mContext = getActivity();
        Utils.setLocale(getActivity());
        ButterKnife.bind(this, mView);
        InitializeControls();
        InitializeControlsAction();


        LogUtils.e("", "language::" + getResources().getConfiguration().locale);

        return mView;
    }

    private void InitializeControls() {

        dbAdapter = new DBAdapter(getMyActivity());

        wvDescription.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wvDescription.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvDescription.getSettings().setAppCacheEnabled(false);
        wvDescription.getSettings().setBlockNetworkImage(true);
        wvDescription.getSettings().setLoadsImagesAutomatically(true);
        wvDescription.getSettings().setGeolocationEnabled(false);
        wvDescription.getSettings().setNeedInitialFocus(false);
        wvDescription.getSettings().setSaveFormData(false);
        wvDescription.getSettings().setTextSize(WebSettings.TextSize.NORMAL);

        wvDescriptionInfo.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wvDescriptionInfo.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvDescriptionInfo.getSettings().setAppCacheEnabled(false);
        wvDescriptionInfo.getSettings().setBlockNetworkImage(true);
        wvDescriptionInfo.getSettings().setLoadsImagesAutomatically(true);
        wvDescriptionInfo.getSettings().setGeolocationEnabled(false);
        wvDescriptionInfo.getSettings().setNeedInitialFocus(false);
        wvDescriptionInfo.getSettings().setSaveFormData(false);
        wvDescriptionInfo.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
//        wvDescription.getSettings().setDefaultFontSize((int)getResources().getDimension(R.dimen.h0));

        tvAddToBag.setTypeface(Shy7lo.TahomaBold);

        ibCancel.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

            tvSkuTitle.setGravity(Gravity.END);
            tvSku.setGravity(Gravity.END);
//            tvNameInfo.setGravity(Gravity.END);
//            tvColorInfo.setGravity(Gravity.END);
            tvFrameSizeInfo.setGravity(Gravity.END);
//            tvFrameTypeInfo.setGravity(Gravity.END);
//            tvProductSizeInfo.setGravity(Gravity.END);

//            tvAddToBag.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_shopping_bag_selected, 0);

            mTopLayout.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            lnrContainer.setScaleX(-1f);
            tvPrice.setScaleX(-1f);
            tvNewPrice.setScaleX(-1f);
            tvName.setScaleX(-1f);
            tvSize.setScaleX(-1f);
            wvDescription.setScaleX(-1f);
            wvDescriptionInfo.setScaleX(-1f);
            tvAdditionalInfoTitle.setScaleX(-1f);
            tvSkuTitle.setScaleX(-1f);
            tvSku.setScaleX(-1f);
            tvNameInfoTitle.setScaleX(-1f);
            tvNameInfo.setScaleX(-1f);
            tvColorInfo.setScaleX(-1f);
            tvColorTitle.setScaleX(-1f);
            tvDetailsTitle.setScaleX(-1f);
            tvProductSizeTitle.setScaleX(-1f);
            tvProductSizeInfo.setScaleX(-1f);
            tvFrameSizeTitle.setScaleX(-1f);
            tvFrameSizeInfo.setScaleX(-1f);
            tvFrameTypeTitle.setScaleX(-1f);
            tvFrameTypeInfo.setScaleX(-1f);
            lnrAddToBag.setScaleX(-1f);
//            tvAddToBag.setScaleX(-1f);
            ibShare.setScaleX(-1f);
            ivFastShipping.setScaleX(-1f);
            ivLigal.setScaleX(-1f);
            ivCashReturn.setScaleX(-1f);
            ivDayReturn.setScaleX(-1f);
            tvFastShipping.setScaleX(-1f);
            tvLigal.setScaleX(-1f);
            tvCashReturn.setScaleX(-1f);
            tvDayReturn.setScaleX(-1f);
            tvColor.setScaleX(-1f);
            tvItemInfo.setScaleX(-1f);
            tvShopping.setScaleX(-1f);
            tvSizeTable.setScaleX(-1f);
            tvYouLike.setScaleX(-1f);
            tvSizeTable.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_details_arrow_left, 0, 0, 0);

            tvName.setGravity(Gravity.RIGHT);
            tvNameInfo.setGravity(Gravity.RIGHT);
//            tvPrice.setGravity(Gravity.END);
//            tvSizeTable.setGravity(Gravity.END);
            tvPrice.setTypeface(Shy7lo.TahomaBold);
            tvNewPrice.setTypeface(Shy7lo.TahomaBold);

        } else {

            tvSkuTitle.setGravity(Gravity.START);
            tvSku.setGravity(Gravity.START);
//            tvNameInfo.setGravity(Gravity.START);
//            tvColorInfo.setGravity(Gravity.START);
            tvFrameSizeInfo.setGravity(Gravity.START);
//            tvFrameTypeInfo.setGravity(Gravity.START);
//            tvProductSizeInfo.setGravity(Gravity.START);

//            tvAddToBag.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_shopping_bag_selected, 0, 0, 0);

            mTopLayout.setScaleX(1f);
            tvTitle.setScaleX(1f);
            lnrContainer.setScaleX(1f);
            tvPrice.setScaleX(1f);
            tvNewPrice.setScaleX(1f);
            tvName.setScaleX(1f);
            tvSize.setScaleX(1f);
            wvDescription.setScaleX(1f);
            wvDescriptionInfo.setScaleX(1f);
            tvAdditionalInfoTitle.setScaleX(1f);
            tvSkuTitle.setScaleX(1f);
            tvSku.setScaleX(1f);
            tvNameInfoTitle.setScaleX(1f);
            tvNameInfo.setScaleX(1f);
            tvColorInfo.setScaleX(1f);
            tvColorTitle.setScaleX(1f);
            tvDetailsTitle.setScaleX(1f);
            tvProductSizeTitle.setScaleX(1f);
            tvProductSizeInfo.setScaleX(1f);
            tvFrameSizeTitle.setScaleX(1f);
            tvFrameSizeInfo.setScaleX(1f);
            tvFrameTypeTitle.setScaleX(1f);
            tvFrameTypeInfo.setScaleX(1f);
            lnrAddToBag.setScaleX(1f);
//            tvAddToBag.setScaleX(1f);
            ibShare.setScaleX(1f);
            ibShare.setScaleX(1f);
            ivFastShipping.setScaleX(1f);
            ivLigal.setScaleX(1f);
            ivCashReturn.setScaleX(1f);
            ivDayReturn.setScaleX(1f);
            tvFastShipping.setScaleX(1f);
            tvLigal.setScaleX(1f);
            tvCashReturn.setScaleX(1f);
            tvDayReturn.setScaleX(1f);
            tvColor.setScaleX(1f);
            tvItemInfo.setScaleX(1f);
            tvShopping.setScaleX(1f);
            tvSizeTable.setScaleX(1f);
            tvYouLike.setScaleX(1f);
            tvSizeTable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_details_arrow_right, 0);
            tvName.setGravity(Gravity.LEFT);
            tvNameInfo.setGravity(Gravity.LEFT);
//            tvPrice.setGravity(Gravity.START);
//            tvSizeTable.setGravity(Gravity.START);
            tvPrice.setTypeface(Shy7lo.RalewayBold);
            tvNewPrice.setTypeface(Shy7lo.RalewayBold);
        }

//        rvSimilarProductItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
//        rvSimilarProductItems.setHasFixedSize(true);
//        rvSimilarProductItems.setNestedScrollingEnabled(false);

        mSimilarProductAdapter = new SimilarProductAdapter(getActivity(), new SimilarProductAdapter.OnSimilarItemClickListener() {
            @Override
            public void onItemClickListener(int position, SimilarProducts.ProductListing mProductItem) {
                if (mProductItem != null) {
                    isFromSearch = false;
                    mSku = mProductItem.sku;
                    getProductDetails();
                }
            }
        });
        rvSimilarProductItems.setAdapter(mSimilarProductAdapter);
        rvSimilarProductItems.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) rvSimilarProductItems.getLayoutManager()) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                CURRENT_PAGE += 1;
//                showNextSimilarProductList(productDetails.category_ids);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
//                return isLastPage;
                return !(CURRENT_PAGE <= TOTAL_PAGES);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

//        tvAdditionalInfoTitle.setText(""+getResources().getString(R.string.additional_info));
//        tvAddToBag.setText(""+getResources().getString(R.string.add_to_bag));

        LogUtils.e("", "GUEST_CART_ID::" + getPref(getActivity(), MyPref.GUEST_CART_ID, ""));
        if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""))) {
            Utils.getGuestCartToken(getActivity());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (productDetails == null) {
            getProductDetails();
        } else {
            showDetails();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.setDrawerSwipe(true);
            activity.setFirebaseLog("Product_Details");
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

    private void getProductDetails() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getProductDetails();
                    }
                }
            });
            return;
        }

        mOptionValue = "";
        Utils.showProgressDialog(getActivity());
//        if (productDetails.getTypeId().equalsIgnoreCase("configurable")) {
//        mSku = "07010400900017-guess"; // configurable
//        } else {
//            mSku = "01030100100003"; // simple
//        }

//        mSku = "testwrwadsfasf"; // perfume

        Call<JsonElement> productDetailsCall = null;


//        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API_TEST_URL, true);
        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
//        JSONObject jsonParams = new JSONObject();
        if (!isFromSearch) {
//            JSONObject jsonParams = new JSONObject();
//            try {
//                jsonParams.put("sku", "" + mSku);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
            //            productDetailsCall = serviceAPI.getProductDetails(Shy7lo.mLangCode, body);
            productDetailsCall = serviceAPI.getProductDetails(Shy7lo.mLangCode, mSku);


        } else {

//            try {
//                jsonParams.put("id", "" + mSku);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
            productDetailsCall = serviceAPI.getAlgoliaProductDetails(Shy7lo.mLangCode, mSku);
        }

//        ApiInterface serviceAPI =
//                RestClient.getDynamicClient(Shy7lo.mLangCode.equalsIgnoreCase("ar") ? RestClient.API_SHYLABS_TEST_AR_URL : RestClient.API_SHYLABS_TEST_URL, true);
//        Map<String, String> mValueMap = new HashMap<>();
//        if (!isFromSearch) {
//
//            try {
//                mValueMap.put("sku", "" + mSku);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            productDetailsCall = serviceAPI.getProductNewDetails(Shy7lo.mLangCode, mValueMap);
//
//        } else {
//
//            try {
//                mValueMap.put("entity_id", "" + mSku);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            productDetailsCall = serviceAPI.getProductNewDetails(Shy7lo.mLangCode, mValueMap);
//        }

        productDetailsCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Utils.closeProgressDialog();
                if (!isAdded()) {
                    return;
                }
//                response.code(); /// Jiten said to remove this line.
                if (response.isSuccessful()) {
                    try {
//                        LogUtils.e("", "response.body()::" + response.body());

                        if (response.body() != null) {

                            JSONObject jsonResponse = new JSONObject(response.body().toString());
                            if (jsonResponse != null && jsonResponse.getString("success").equals("1")) {
//                            if (jsonResponse != null && jsonResponse.getBoolean("success")) {

                                jsonObject = jsonResponse.getJSONObject("data");
//                                jsonObject = jsonResponse.getJSONObject("product_detail");
                                LogUtils.e("", "jsonObject::" + jsonObject.toString());

                                if (jsonObject != null && jsonObject.length() > 0) {
                                    Gson gson = new Gson();
                                    productDetails = gson.fromJson(jsonObject.toString(), ProductDetails.class);

                                    psvScrollView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            psvScrollView.scrollTo(0, 0);
                                        }
                                    });

                                    showDetails();

                                }
                            } else if (jsonResponse != null && jsonResponse.getString("success").equals("0")) {
                                Utils.showToast(getActivity(), "" + jsonResponse.getString("message"));
                            } else if (jsonResponse != null && jsonResponse.getString("success").equals("2")) {
                                Utils.showInitialScreen(getActivity());
                                return;
                            } else {
                                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    LogUtils.e(TAG_PRODUCT_DETIAILS_FRAGMENT, "Response is not great");
//                    Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
//                    Utils.showAlertDialog(getActivity(), "" + response.code());
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
            public void onFailure(Call<JsonElement> call, Throwable t) {
                System.out.println(t.getMessage());
                Utils.closeProgressDialog();
                Utils.showAlertDialog(getActivity(), "" + t.getMessage());
//                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    Utils.showToast(getActivity(), "Something went wrong");
//                } else {
//                    Utils.showToast(getActivity(), "خطأ");
//                }
            }
        });

    }

    boolean pageChanged = false;

    private void showDetails() {
        if (productDetails != null) {


            List<ProductDetails.Image_> imageList = productDetails.getImages();
            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                Collections.reverse(imageList);
            }
            if (imageList != null) {
                final ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(getActivity(), imageList, psvScrollView);
                vpProductsImages.setAdapter(productImagesAdapter);
                tlIndicator.setupWithViewPager(vpProductsImages);
                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    vpProductsImages.setCurrentItem(imageList.size() - 1);
                }
                vpProductsImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        pageChanged = true;
//                        TouchImageView touchImageView = ((View) vpProductsImages.getChildAt(position)).findViewById(R.id.ivProductImage);
//                        if (touchImageView.isZoomed()) {
//                            touchImageView.resetZoom();
//                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        if (state == ViewPager.SCROLL_STATE_IDLE && pageChanged) {
                            for (int i = 0; i < vpProductsImages.getChildCount(); i++) {
                                TouchImageView touchImageView = ((View) vpProductsImages.getChildAt(i)).findViewById(R.id.ivProductImage);
                                if (touchImageView.isZoomed()) {
                                    touchImageView.resetZoom();
                                }
                            }
                            pageChanged = false;
                        }
                    }
                });
            }

            if (!TextUtils.isEmpty(productDetails.getDescription())) {
                wvDescription.setVisibility(View.VISIBLE);
                wvDescriptionInfo.setVisibility(View.VISIBLE);
                tvDetailsTitle.setVisibility(View.VISIBLE);
//                                            wvDescription.loadData(productDetails.getDescription(), "text/html", "UTF-8");
                String prefix = "";
                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    prefix = "<html><head><style  type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/TahomaRegular.ttf\") }body {font-family: MyFont;color: #bbbbbb;font-size: small;text-align: left;padding:0; margin:0}</style></head><body>";
                } else {
                    prefix = "<html><head><style  type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/TahomaRegular.ttf\") }body {font-family: MyFont;color: #bbbbbb;font-size: small;text-align: left;padding:0; margin:0}</style></head><body>";
                }

                String postfix = "</body></html>";
                String myHtmlString = prefix + productDetails.getDescription() + postfix;
                wvDescription.loadDataWithBaseURL(null, myHtmlString, "text/html", "UTF-8", null);
                wvDescriptionInfo.loadDataWithBaseURL(null, myHtmlString, "text/html", "UTF-8", null);
            } else {
                wvDescription.setVisibility(View.GONE);
                wvDescriptionInfo.setVisibility(View.GONE);
                tvDetailsTitle.setVisibility(View.GONE);
            }

            if (productDetails.getCustommsg() != null) {
                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    tvFastShipping.setText(productDetails.getCustommsg().shippingMsg.ar);
                    tvLigal.setText(productDetails.getCustommsg().brandMsg.ar);
                    tvDayReturn.setText(productDetails.getCustommsg().returnMsg.ar);
                    tvCashReturn.setText(productDetails.getCustommsg().cashMsg.ar);
                } else {
                    tvFastShipping.setText(productDetails.getCustommsg().shippingMsg.en);
                    tvLigal.setText(productDetails.getCustommsg().brandMsg.en);
                    tvDayReturn.setText(productDetails.getCustommsg().returnMsg.en);
                    tvCashReturn.setText(productDetails.getCustommsg().cashMsg.en);
                }

                tvFastShipping.setTextColor(Color.parseColor(productDetails.getCustommsg().shippingMsg.color));
                tvLigal.setTextColor(Color.parseColor(productDetails.getCustommsg().brandMsg.color));
                tvDayReturn.setTextColor(Color.parseColor(productDetails.getCustommsg().returnMsg.color));
                tvCashReturn.setTextColor(Color.parseColor(productDetails.getCustommsg().cashMsg.color));
            }

            if (!TextUtils.isEmpty("" + productDetails.getPrice()) && productDetails.getPrice() > 0) {
//                                            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                                                tvPrice.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, Float.parseFloat(productDetails.getPrice())));
//                                            } else {
//                tvPrice.setText(Utils.getRealPrice(mExchangeRate, parseFloat(productDetails.getPrice())) + " " + mCurrencyCode);
                SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode + " " + Utils.getIntPrice(mExchangeRate, productDetails.getPrice()));
                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), 0, Utils.getIntPrice(mExchangeRate, parseFloat(productDetails.getPrice())).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                } else {
//                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, Utils.getIntPrice(mExchangeRate, parseFloat(productDetails.getPrice())).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                tvPrice.setText(sb);
                mFinalRate = productDetails.getPrice();
//                                            }

            }

            tvNewPrice.setVisibility(View.GONE);
            tvPrice.setPaintFlags(0);
            tvPrice.setTextColor(getResources().getColor(R.color.black));

            try {
                if (productDetails.getSpecial_price() > 0 && !TextUtils.isEmpty(String.valueOf(productDetails.getSpecial_price()))) {

                    if (!TextUtils.isEmpty(productDetails.getSpecial_from_date()) && !TextUtils.isEmpty(productDetails.getSpecial_to_date())) {

                        Date fromDate = sdfDate.parse(productDetails.getSpecial_from_date());
                        Date toDate = sdfDate.parse(productDetails.getSpecial_to_date());
                        Date currentDate = new Date();

                        if (currentDate.after(fromDate) && currentDate.before(toDate)) {

                            mFinalRate = productDetails.getSpecial_price();

                            SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode + " " + Utils.getIntPrice(mExchangeRate, productDetails.getSpecial_price()));
                            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), 0, Utils.getIntPrice(mExchangeRate, parseFloat(productDetails.getSpecial_price())).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            } else {
//                                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, Utils.getIntPrice(mExchangeRate, parseFloat(productDetails.getSpecial_price())).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            }
                            tvNewPrice.setText(sb);
                            tvNewPrice.setVisibility(View.VISIBLE);

//                            if (!TextUtils.isEmpty("" + productDetails.getPrice()) && productDetails.getPrice() > 0) {
//                                tvPrice.setText(Utils.getIntPrice(mExchangeRate, productDetails.getPrice()) + "");
//                            } else {
//                                tvPrice.setText("" + productDetails.getPrice() + " ");
//                            }
                            tvPrice.setTextColor(mContext.getResources().getColor(R.color.gray_66));
                            tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            price = productDetails.getPrice();
            special_price = productDetails.getSpecial_price();
            special_from_date = productDetails.getSpecial_from_date();
            special_to_date = productDetails.getSpecial_to_date();
            LogUtils.e("", "price::" + price);
            LogUtils.e("", "special_price::" + special_price);
            LogUtils.e("", "special_from_date::" + special_from_date);
            LogUtils.e("", "special_to_date::" + special_to_date);

//            tvName.setText("" + productDetails.getName());
//            tvNameInfo.setText("" + productDetails.getName());
            tvSku.setText("" + productDetails.getSku());


            if (!TextUtils.isEmpty(productDetails.getPerfume_size())) {
                tvProductSizeInfo.setText("" + productDetails.getPerfume_size());
                tvProductSizeTitle.setVisibility(View.VISIBLE);
                tvProductSizeInfo.setVisibility(View.VISIBLE);
            } else {
                tvProductSizeTitle.setVisibility(View.GONE);
                tvProductSizeInfo.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(productDetails.getFrame_size())) {
                tvFrameSizeInfo.setText("" + productDetails.getFrame_size());
                tvFrameSizeTitle.setVisibility(View.VISIBLE);
                tvFrameSizeInfo.setVisibility(View.VISIBLE);
            } else {
                tvFrameSizeTitle.setVisibility(View.GONE);
                tvFrameSizeInfo.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(productDetails.getFrame_type())) {
                tvFrameTypeInfo.setText("" + productDetails.getFrame_type());
                tvFrameTypeTitle.setVisibility(View.VISIBLE);
                tvFrameTypeInfo.setVisibility(View.VISIBLE);
            } else {
                tvFrameTypeTitle.setVisibility(View.GONE);
                tvFrameTypeInfo.setVisibility(View.GONE);
            }


            tvTitle.setText("");
            if (!TextUtils.isEmpty(productDetails.getBrandName()) && !productDetails.getBrandName().equalsIgnoreCase("null")) {
//                tvTitle.setText("" + productDetails.getBrandName());
                tvName.setText("" + productDetails.getBrandName());
                tvNameInfo.setText("" + productDetails.getBrandName());
                tvNameInfoTitle.setVisibility(View.VISIBLE);
                tvNameInfo.setVisibility(View.VISIBLE);
            } else {
//                tvTitle.setText("");
                tvName.setText("");
                tvNameInfo.setText("");
                tvNameInfoTitle.setVisibility(View.GONE);
                tvNameInfo.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(productDetails.getSize_chart())) {
                tvSizeTable.setVisibility(View.VISIBLE);
            } else {
                tvSizeTable.setVisibility(View.GONE);
            }

//            if (!TextUtils.isEmpty(productDetails.getColor())) {
//                tvColorInfo.setText("" + productDetails.getColor());
//                tvColorTitle.setVisibility(View.VISIBLE);
//                tvColorInfo.setVisibility(View.VISIBLE);
//            } else {
//                tvColorTitle.setVisibility(View.GONE);
//                tvColorInfo.setVisibility(View.GONE);
//            }

            if (productDetails.getCircleColor() != null) {

                if (!TextUtils.isEmpty(productDetails.getCircleColor().mode) && !TextUtils.isEmpty(productDetails.getCircleColor().source)) {


                    tvColor.setVisibility(View.VISIBLE);
                    tvColorIndicator.setVisibility(View.GONE);
                    ivColorIndicator.setVisibility(View.GONE);

                    LogUtils.e("", "mode:" + productDetails.getCircleColor().mode);
                    LogUtils.e("", "source:" + productDetails.getCircleColor().source);

                    if (productDetails.getCircleColor().mode.equalsIgnoreCase("rgb")) {
                        tvColorIndicator.setVisibility(View.VISIBLE);


                        try {
                            Drawable background = tvColorIndicator.getBackground();
                            if (background instanceof ShapeDrawable) {
                                ((ShapeDrawable) background).getPaint().setColor(Color.parseColor(productDetails.getCircleColor().source));
                            } else if (background instanceof GradientDrawable) {
                                ((GradientDrawable) background).setColor(Color.parseColor(productDetails.getCircleColor().source));
                            } else if (background instanceof ColorDrawable) {
                                ((ColorDrawable) background).setColor(Color.parseColor(productDetails.getCircleColor().source));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (productDetails.getCircleColor().mode.equalsIgnoreCase("image")) {
                        ivColorIndicator.setVisibility(View.VISIBLE);
                        LogUtils.e("", "Load Images");
                        Glide.with(getActivity()).load(productDetails.getCircleColor().source).apply(RequestOptions.circleCropTransform()).into(ivColorIndicator);
                    }
                } else {
                    tvColorIndicator.setVisibility(View.GONE);
                    ivColorIndicator.setVisibility(View.GONE);
                    tvColor.setVisibility(View.GONE);
                    mViewSpace.setVisibility(View.GONE);
                }


            } else {
                tvColorIndicator.setVisibility(View.GONE);
                ivColorIndicator.setVisibility(View.GONE);
                tvColor.setVisibility(View.GONE);
                mViewSpace.setVisibility(View.GONE);
            }


            if (productDetails.getTypeId().equalsIgnoreCase("configurable")) {
                tvSize.setVisibility(View.VISIBLE);
//                                            List<ProductDetails.AttributeDetail> mAttributeDetailList =
//                                                    productDetails.getConfigurableAttributes().getAttributeDetails();
//                                            if (mAttributeDetailList != null && mAttributeDetailList.size() > 0) {
//                                                for (int i = 0; i < mAttributeDetailList.size(); i++) {
//                                                    ProductDetails.AttributeDetail mAttributeDetail = mAttributeDetailList.get(i);
//                                                    if (mAttributeDetail != null && mAttributeDetail.getAttributeName().contains("Size")) {
//
////                                                        showSizeDialog(mAttributeDetail);
//
//                                                    }
//                                                }
//                                            }

//                tvColorIndicator.setVisibility(View.VISIBLE);
//                tvColor.setVisibility(View.VISIBLE);

            } else {
                tvSize.setVisibility(View.GONE);
                mViewSpace.setVisibility(View.GONE);
//                tvColorIndicator.setVisibility(View.GONE);
//                tvColor.setVisibility(View.GONE);
            }

            // Get wishitem status
            if (dbAdapter.isContainInWishListWithoutSoftDelete(productDetails.getSku())) {
                tbWishlist.setOnCheckedChangeListener(null);
                tbWishlist.setChecked(true);
                tbWishlist.setOnCheckedChangeListener(checkListener);
            }


            try {
                if (jsonObject != null && jsonObject.has("configurable_attributes")) {
                    JSONObject jConfig = jsonObject.getJSONObject("configurable_attributes");
                    JSONArray jsonArray = jConfig.getJSONArray("attribute_details").getJSONObject(0).getJSONArray("option");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jOption = jsonArray.getJSONObject(i);
                            LogUtils.e("", "jOption.toString();::" + jOption.toString());
                            productDetails.getConfigurableAttributes().getAttributeDetails().get(0).getOption().get(i).data = jOption.toString();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String related_product_status = productDetails.related_product_status;
            LogUtils.e("", "related_product_status::" + related_product_status);
            if (!TextUtils.isEmpty(related_product_status) && related_product_status.equalsIgnoreCase("1")) {
                tvYouLike.setVisibility(View.VISIBLE);
                rltSimilarProduct.setVisibility(View.VISIBLE);

                if (productDetails.category_ids != null && productDetails.category_ids.size() > 0) {
                    showSimilarProduct(productDetails.category_ids);
                }
            } else {
                tvYouLike.setVisibility(View.GONE);
                rltSimilarProduct.setVisibility(View.GONE);
            }


            if (RestClient.isFacebookLive) {
                try {
                    // FB Log View Content
                    AppEventsLogger logger = AppEventsLogger.newLogger(getActivity());

                    Bundle parameters = new Bundle();
                    parameters.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR)));
//                    parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "" + productDetails.getTypeId());
                    parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "Product");
                    parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, "" + productDetails.getSku());
                    parameters.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, productDetails.getName());
                    parameters.putString(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, "1");

                    logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, parameters);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                AdjustEvent event = new AdjustEvent("o1eqkj");
                event.addPartnerParameter("SKU", "" + "" + productDetails.getSku());
                //Callback
                event.addCallbackParameter("SKU", "" + "" + productDetails.getSku());

                String mCriteo = MyPref.getPref(getActivity(), MyPref.APP_CRITEO, "");
                if (TextUtils.isEmpty(mCriteo) || mCriteo.equalsIgnoreCase("1")) {
                    AdjustCriteo.injectViewProductIntoEvent(event, "" + productDetails.getSku());
                    String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
                    AdjustCriteo.injectCustomerIdIntoCriteoEvents(TextUtils.isEmpty(userToken) ? userToken : MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""));
                }

                Adjust.trackEvent(event);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

//    private void showSimilarProduct(List<String> category_ids) {
//
//        CURRENT_PAGE = 1;
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(Shy7lo.mLangCode.equalsIgnoreCase("ar") ? RestClient.API_SHYLABS_TEST_AR_URL : RestClient.API_SHYLABS_TEST_URL, true);
//
//        Call<SimilarProducts> callCode = apiService.getSimilarProductList(Shy7lo.mLangCode, category_ids.toString(), "" + PAGE_NO_ITEM, "" + CURRENT_PAGE);
//        callCode.enqueue(new Callback<SimilarProducts>() {
//            @Override
//            public void onResponse(Call<SimilarProducts> call, Response<SimilarProducts> response) {
//
////                Utils.closeProgressDialog();
//                LogUtils.e("", "response.isSuccessful()::" + response.isSuccessful());
//
//                if (response.isSuccessful()) {
//
//                    SimilarProducts mSimilarProducts = response.body();
//
//                    if (mSimilarProducts != null) {
//
//                        TOTAL_PAGES = mSimilarProducts.totalCount / PAGE_NO_ITEM;
//
//                        mSimilarProductListing = mSimilarProducts.productListing;
//                        LogUtils.e("", "mSimilarProductListing::" + mSimilarProductListing.size());
//
//
//                        if (mSimilarProductListing != null && mSimilarProductListing.size() > 0) {
//                            mSimilarProductAdapter.addAll(mSimilarProductListing);
//                        }
//                    } else {
//                        LogUtils.e("", "mSimilarProducts is not null");
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SimilarProducts> call, Throwable t) {
//                LogUtils.e("", "onFailure::" + t.getMessage());
//            }
//        });
//
//    }

    private void showSimilarProduct(List<String> category_ids) {

        CURRENT_PAGE = 1;

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

//        Call<SimilarProducts> callCode = apiService.getSimilarProductList(Shy7lo.mLangCode, category_ids.toString(), "" + PAGE_NO_ITEM, "" + CURRENT_PAGE);
        Call<SimilarProducts> callCode = apiService.getSimilarProductList(Shy7lo.mLangCode, category_ids.toString());
        callCode.enqueue(new Callback<SimilarProducts>() {
            @Override
            public void onResponse(Call<SimilarProducts> call, Response<SimilarProducts> response) {

//                Utils.closeProgressDialog();
                LogUtils.e("", "response.isSuccessful()::" + response.isSuccessful());

                if (response.isSuccessful()) {

                    SimilarProducts mSimilarProducts = response.body();

                    if (mSimilarProducts != null && mSimilarProducts.data != null) {

//                        TOTAL_PAGES = mSimilarProducts.totalCount / PAGE_NO_ITEM;
                        TOTAL_PAGES = 1;

                        mSimilarProductListing = mSimilarProducts.data.productListing;


                        if (mSimilarProductListing != null && mSimilarProductListing.size() > 0) {
                            LogUtils.e("", "mSimilarProductListing::" + mSimilarProductListing.size());
                            mSimilarProductAdapter.addAll(mSimilarProductListing);
                        }
                    } else {
                        LogUtils.e("", "mSimilarProducts is not null");
                    }

                } else {
                    if (String.valueOf(response.code()).startsWith("5")) {
                        Utils.showAlertDialog(getActivity(), "" + response.code());
                    } else {

                        try {
                            JSONObject jResponse = new JSONObject(response.errorBody().string());
                            if (jResponse != null && jResponse.getString("success").equals("0")) {
                                Utils.showToast(mContext, "" + jResponse.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<SimilarProducts> call, Throwable t) {
                LogUtils.e("", "onFailure::" + t.getMessage());
            }
        });

    }

//    private void showNextSimilarProductList(List<String> category_ids) {
//
//        isLoading = true;
//
//        lnrProgressBar.setVisibility(View.VISIBLE);
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(Shy7lo.mLangCode.equalsIgnoreCase("ar") ? RestClient.API_SHYLABS_TEST_AR_URL : RestClient.API_SHYLABS_TEST_URL, true);
//
//        Call<SimilarProducts> callCode = apiService.getSimilarProductList(Shy7lo.mLangCode, category_ids.toString(), "" + PAGE_NO_ITEM, "" + CURRENT_PAGE);
//        callCode.enqueue(new Callback<SimilarProducts>() {
//            @Override
//            public void onResponse(Call<SimilarProducts> call, Response<SimilarProducts> response) {
//
////                Utils.closeProgressDialog();
//                LogUtils.e("", "response.isSuccessful()::" + response.isSuccessful());
//                isLoading = false;
//
//                lnrProgressBar.setVisibility(View.GONE);
//
//                if (response.isSuccessful()) {
//
//                    SimilarProducts mSimilarProducts = response.body();
//
//                    if (mSimilarProducts != null) {
//
//                        TOTAL_PAGES = mSimilarProducts.totalCount / PAGE_NO_ITEM;
//
//                        mSimilarProductListing.addAll(mSimilarProducts.productListing);
//                        LogUtils.e("", "mSimilarProductListing::" + mSimilarProductListing.size());
//
//
//                        if (mSimilarProductListing != null && mSimilarProductListing.size() > 0) {
//                            mSimilarProductAdapter.addAll(mSimilarProductListing);
//                        }
//                    } else {
//                        LogUtils.e("", "mSimilarProducts is not null");
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SimilarProducts> call, Throwable t) {
//                LogUtils.e("", "onFailure::" + t.getMessage());
//            }
//        });
//
//    }

    // Offlish Shopping
//    private void addProductIntoCart() {
//
//        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
//        String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
//
//        if (productDetails != null && productDetails.getStockQty().is_in_stock) {
//
//            //sku soft delete
//            // update cart
//            if (dbAdapter.isSoftDeleteShoppingItem(productDetails.getSku(), productDetails.getTypeId(), mSizeLable)) {
//
//                dbAdapter.updateQtySoftDelete(productDetails.getSku(), 1, "0");
//
//                int mCartItemCount = MyPref.getPref(getActivity(), MyPref.CART_ITEM_COUNT, 0);
//                mCartItemCount = mCartItemCount + 1;
//                MyPref.setPref(getActivity(), MyPref.CART_ITEM_COUNT, mCartItemCount);
//
//            } else if (dbAdapter.ifShoppingItemAvailable(productDetails.getSku(), productDetails.getTypeId(), mSizeLable)) {
//
//                dbAdapter.increaseQtyAndServer(productDetails.getSku(), productDetails.getTypeId(), mSizeLable);
//
//            } else {
//
//                ShoppingBag.Item mShoppingItem = new ShoppingBag().new Item();
//                mShoppingItem.setItemId(Integer.parseInt(productDetails.getEntityId()));
//                mShoppingItem.setName(productDetails.getName());
//                mShoppingItem.setBrand(productDetails.getBrandName());
//                mShoppingItem.setImageFIle(productDetails.getImages().get(0).getUrl());
//                mShoppingItem.setSku(productDetails.getSku());
//                mShoppingItem.setProductType(productDetails.getTypeId());
//                LogUtils.e("", "Bag price::" + price);
//                if (!TextUtils.isEmpty(price)) {
//                    mShoppingItem.setPrice(Float.parseFloat(price));
//                }
//                LogUtils.e("", "Bag special_price::" + special_price);
//                if (!TextUtils.isEmpty(special_price)) {
//                    mShoppingItem.setSpecial_price(Float.parseFloat(special_price));
//                }
//                mShoppingItem.setSpecial_from_date(special_from_date);
//                mShoppingItem.setSpecial_to_date(special_to_date);
//                try {
//                    mShoppingItem.setPrice_excl_tax(productDetails.getPrice_excl_tax());
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                mShoppingItem.setQty(1);
//                mShoppingItem.setStockQty(productDetails.getStockQty().Qty);
//                mShoppingItem.setStockStatus(productDetails.getStockQty().is_in_stock ? "1" : "0");
//                mShoppingItem.setAttribute_id(mOptionId);
//                mShoppingItem.setValue_index(mOptionValue);
//                mShoppingItem.setOption_lable("Size");
//                mShoppingItem.setOption_value(mSizeLable);
//                ShoppingBag.CustomMsg mCustomMsg = new ShoppingBag().new CustomMsg();
//                if (productDetails.getCustommsg() != null && productDetails.getCustommsg().cartMsg != null) {
//                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                        mCustomMsg.setMsg("" + productDetails.getCustommsg().cartMsg.ar);
//                    } else {
//                        mCustomMsg.setMsg("" + productDetails.getCustommsg().cartMsg.en);
//                    }
//                    mCustomMsg.setColor("" + productDetails.getCustommsg().cartMsg.color);
//                } else {
//                    mCustomMsg.setMsg("");
//                    mCustomMsg.setColor("");
//                }
//                mShoppingItem.setCustom_msg(mCustomMsg);
//                mShoppingItem.setIsGuest(TextUtils.isEmpty(userToken) ? "1" : "0");
//                mShoppingItem.setToken(TextUtils.isEmpty(userToken) ? guestToken : userToken);
//                mShoppingItem.setIs_on_server("0");
//                dbAdapter.addShoppingItemFromDetails(mShoppingItem);
//
//                int mCartItemCount = MyPref.getPref(getActivity(), MyPref.CART_ITEM_COUNT, 0);
//                mCartItemCount = mCartItemCount + 1;
//                MyPref.setPref(getActivity(), MyPref.CART_ITEM_COUNT, mCartItemCount);
//
//            }
//
//
//            if (getActivity() instanceof HomeActivity) {
//                HomeActivity activity = (HomeActivity) getActivity();
//                activity.updateBadgetCount();
//            }
//
//            showOrderMoreDialog();
//        } else {
//            Utils.showToast(getActivity(), "" + getString(R.string.msg_outof_order));
//            return;
//        }
//
//        try {
//            JSONObject jsonParams = new JSONObject();
//
//            SharedPreferences pref = getActivity().getSharedPreferences(NotificationUtils.SHARED_PREF, 0);
//
//            if (!TextUtils.isEmpty(userToken)) {
//
//                jsonParams.put("sku", "" + productDetails.getSku());
//                jsonParams.put("qty", "1");
//                jsonParams.put("product_name", "" + productDetails.getName());
//                jsonParams.put("price", "" + productDetails.getPrice());
//                jsonParams.put("product_type", productDetails.getTypeId());
//                jsonParams.put("device_token", "" + pref.getString("regId", ""));
////                jsonParams.put("device_token", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
//                jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));
//
//
//                if (productDetails.getTypeId().equalsIgnoreCase("configurable")) {
//                    JSONArray jsonArray = new JSONArray();
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("option_id", mOptionId);
//                    jsonObject.put("option_value", mOptionValue);
//                    jsonArray.put(jsonObject);
//                    jsonParams.put("configurable_item_options", jsonArray);
//                }
//
//            } else {
//
//                jsonParams.put("sku", "" + productDetails.getSku());
//                jsonParams.put("qty", "1");
//                jsonParams.put("product_name", "" + productDetails.getName());
//                jsonParams.put("price", "" + productDetails.getPrice());
//                jsonParams.put("product_type", productDetails.getTypeId());
//                jsonParams.put("cart_id", "" + guestToken);
//                jsonParams.put("device_token", "" + pref.getString("regId", ""));
////                jsonParams.put("device_token", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
//                jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));
//
//                if (productDetails.getTypeId().equalsIgnoreCase("configurable")) {
//                    JSONArray jsonArray = new JSONArray();
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("option_id", mOptionId);
//                    jsonObject.put("option_value", mOptionValue);
//                    jsonArray.put(jsonObject);
//                    jsonParams.put("configurable_item_options", jsonArray);
//                }
//            }
//
//            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
//
//            ApiInterface apiService =
//                    RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//
//            Call<ResponseBody> call;
//            if (!TextUtils.isEmpty(userToken)) {
//                if (productDetails.getTypeId().equalsIgnoreCase("configurable")) {
//                    call = apiService.addUserProductConfigInCart(Shy7lo.mLangCode, "Bearer " + userToken, body);
//                } else {
//                    call = apiService.addUserProductInCart(Shy7lo.mLangCode, "Bearer " + userToken, body);
//                }
//            } else {
//                if (productDetails.getTypeId().equalsIgnoreCase("configurable")) {
//                    call = apiService.addGuestProductConfigInCart(Shy7lo.mLangCode, body);
//                } else {
//                    call = apiService.addGuestProductInCart(Shy7lo.mLangCode, body);
//                }
//            }
//
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                    LogUtils.e(TAG_PRODUCT_DETIAILS_FRAGMENT, "response code:" + response.code());
//                    if (response.isSuccessful()) {
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response.body().string());
//                            if (jsonObject != null && jsonObject.getString("success").equalsIgnoreCase("1")) {
//                                String item_id = jsonObject.getJSONObject("data").getString("item_id");
//                                dbAdapter.updateIsOnServerShoppingFromDetail(productDetails.getSku(), item_id, productDetails.getTypeId(), mOptionId, mOptionValue, "1");
//
//                                try {
//                                    if (!BuildConfig.DEBUG) {
//                                        Answers.getInstance().logAddToCart(new AddToCartEvent()
//                                                .putItemPrice(BigDecimal.valueOf(Utils.getAnswerPrice(mExchangeRate, mFinalRate)))
//                                                .putCurrency(Currency.getInstance("" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")))
//                                                .putItemName("" + productDetails.getName())
//                                                .putItemType(productDetails.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
//                                                .putItemId("" + productDetails.getSku()));
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                if (RestClient.isTuneEnable) {
//                                    try {
//                                        Tune tune = Tune.getInstance();
//                                        List<TuneEventItem> mTuneEventItemsList = new ArrayList<TuneEventItem>();
//                                        TuneEventItem mTuneEventItem = new TuneEventItem("" + productDetails.getName());
//                                        mTuneEventItemsList.add(mTuneEventItem);
//                                        tune.measureEvent(new TuneEvent(TuneEvent.ADD_TO_CART)
//                                                .withEventItems(mTuneEventItemsList)
//                                                .withQuantity(1)
//                                                .withContentId(productDetails.getSku())
//                                                .withRevenue(0)
//                                                .withContentType(productDetails.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
//                                                .withCurrencyCode("" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")));
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                                if (getActivity() instanceof HomeActivity) {
//                                    HomeActivity activity = (HomeActivity) getActivity();
//                                    activity.setDrawerSwipe(false);
//                                    activity.setFirebaseLog("Add_to_cart");
//                                }
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
////                            dbAdapter.updateIsOnServerShoppingFromDetail(productDetails.getSku(), "", productDetails.getTypeId(), mOptionId, mOptionValue, "1");
//                        }
//
//                    } else {
//                        if (response.code() == 400) {
//                            ShoppingBag.Item mShoppingItem = dbAdapter.getShoppingItemBySku(productDetails.getSku(), productDetails.getTypeId(), mSizeLable);
//                            if (mShoppingItem != null && mShoppingItem.getQty() > 1) {
//                                dbAdapter.decreaseQtyAndServer(productDetails.getSku(), productDetails.getTypeId(), mSizeLable);
//                            } else {
//                                dbAdapter.deleteShoppingItem(productDetails.getSku(), productDetails.getTypeId(), mSizeLable);
//                            }
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    System.out.println(t.getMessage());
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    private void addProductIntoCart() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        addProductIntoCart();
                    }
                }
            });
            return;
        }

        LogUtils.e(TAG_PRODUCT_DETIAILS_FRAGMENT, "getGuestCartToken call");
        Utils.showProgressDialog(getActivity());
        try {

            String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");

            JSONObject jsonParams = new JSONObject();

            SharedPreferences pref = getActivity().getSharedPreferences(NotificationUtils.SHARED_PREF, 0);

            if (!TextUtils.isEmpty(userToken)) {

                jsonParams.put("sku", "" + productDetails.getSku());
                jsonParams.put("qty", "1");
                jsonParams.put("product_name", "" + productDetails.getName());
                jsonParams.put("price", "" + productDetails.getPrice());
                jsonParams.put("product_type", productDetails.getTypeId());
                jsonParams.put("device_token", "" + pref.getString("regId", ""));
//                jsonParams.put("device_token", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
                jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));


                if (productDetails.getTypeId().equalsIgnoreCase("configurable")) {
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("option_id", mOptionId);
                    jsonObject.put("option_value", mOptionValue);
                    jsonArray.put(jsonObject);
                    jsonParams.put("configurable_item_options", jsonArray);
                }

            } else {

                String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
                if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""))) {
                    Utils.getGuestCartToken(getActivity());
                    return;
                }

//                jsonParams.put("sku", "" + testSku);
                jsonParams.put("sku", "" + productDetails.getSku());
                jsonParams.put("qty", "1");
                jsonParams.put("product_name", "" + productDetails.getName());
                jsonParams.put("price", "" + productDetails.getPrice());
                jsonParams.put("product_type", productDetails.getTypeId());
                jsonParams.put("cart_id", "" + guestToken);
                jsonParams.put("device_token", "" + pref.getString("regId", ""));
//                jsonParams.put("device_token", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
                jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));

                if (productDetails.getTypeId().equalsIgnoreCase("configurable")) {
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("option_id", mOptionId);
                    jsonObject.put("option_value", mOptionValue);
                    jsonArray.put(jsonObject);
                    jsonParams.put("configurable_item_options", jsonArray);
                }
            }

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());

            ApiInterface apiService =
                    RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

            Call<ResponseBody> call;
            if (!TextUtils.isEmpty(userToken)) {
                if (productDetails.getTypeId().equalsIgnoreCase("configurable")) {
                    call = apiService.addUserProductConfigInCart(Shy7lo.mLangCode, "Bearer " + userToken, body);
                } else {
                    call = apiService.addUserProductInCart(Shy7lo.mLangCode, "Bearer " + userToken, body);
                }
            } else {
                if (productDetails.getTypeId().equalsIgnoreCase("configurable")) {
                    call = apiService.addGuestProductConfigInCart(Shy7lo.mLangCode, body);
                } else {
                    call = apiService.addGuestProductInCart(Shy7lo.mLangCode, body);
                }
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    LogUtils.e(TAG_PRODUCT_DETIAILS_FRAGMENT, "response code:" + response.code());
                    Utils.closeProgressDialog();
                    if (response.isSuccessful()) {

                        int mCartItemCount = MyPref.getPref(getActivity(), MyPref.CART_ITEM_COUNT, 0);
                        mCartItemCount = mCartItemCount + 1;
                        MyPref.setPref(getActivity(), MyPref.CART_ITEM_COUNT, mCartItemCount);

                        if (getActivity() instanceof HomeActivity) {
                            HomeActivity activity = (HomeActivity) getActivity();
                            activity.updateBadgetCount();
                        }

                        showOrderMoreDialog();
                        try {
                            Answers.getInstance().logAddToCart(new AddToCartEvent()
                                    .putItemPrice(BigDecimal.valueOf(Utils.getAnswerPrice(mExchangeRate, mFinalRate)))
                                    .putCurrency(Currency.getInstance("" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")))
                                    .putItemName("" + productDetails.getName())
                                    .putItemType(productDetails.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
                                    .putItemId("" + productDetails.getSku()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        if (RestClient.isTuneEnable) {
//                            try {
//                                Tune tune = Tune.getInstance();
//                                List<TuneEventItem> mTuneEventItemsList = new ArrayList<TuneEventItem>();
//                                TuneEventItem mTuneEventItem = new TuneEventItem("" + productDetails.getName());
//                                mTuneEventItemsList.add(mTuneEventItem);
//                                tune.measureEvent(new TuneEvent(TuneEvent.ADD_TO_CART)
//                                        .withEventItems(mTuneEventItemsList)
//                                        .withQuantity(1)
//                                        .withContentId(productDetails.getSku())
//                                        .withRevenue(0)
//                                        .withContentType(productDetails.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
//                                        .withCurrencyCode("" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")));
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }

                        try {
                            AdjustEvent event = new AdjustEvent("rd7dpc");
                            event.addPartnerParameter("Name", "" + "" + productDetails.getName());
                            event.addPartnerParameter("Price", "" + "" + Utils.getAnswerPrice(mExchangeRate, mFinalRate));
                            event.addPartnerParameter("Currency", "" + "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, ""));
                            event.addPartnerParameter("Product Type", "" + (productDetails.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple"));
                            event.addPartnerParameter("SKU", "" + "" + productDetails.getSku());

                            //callback
                            event.addCallbackParameter("Name", "" + "" + productDetails.getName());
                            event.addCallbackParameter("Price", "" + "" + Utils.getAnswerPrice(mExchangeRate, mFinalRate));
                            event.addCallbackParameter("Currency", "" + "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, ""));
                            event.addCallbackParameter("Product Type", "" + (productDetails.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple"));
                            event.addCallbackParameter("SKU", "" + "" + productDetails.getSku());

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
                                // FB Log Add to cart
                                AppEventsLogger logger = AppEventsLogger.newLogger(getActivity());

                                Bundle parameters = new Bundle();
                                parameters.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR)));
//                                parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "" + productDetails.getTypeId());
                                parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "Product");
                                parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, "" + productDetails.getSku());
                                parameters.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, productDetails.getName());
                                parameters.putString(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, "1");

                                logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, Utils.getAnswerPrice(mExchangeRate, mFinalRate), parameters);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (getActivity() instanceof HomeActivity) {
                            HomeActivity activity = (HomeActivity) getActivity();
                            activity.setDrawerSwipe(false);
                            activity.setFirebaseLog("Add_to_cart");
                        }

                    } else {

                        if (String.valueOf(response.code()).startsWith("5")) {
                            Utils.showAlertDialog(getActivity(), "" + response.code());
                        } else {

                            try {
                                JSONObject jResponse = new JSONObject(response.errorBody().string());
                                if (jResponse != null && jResponse.getString("success").equals("0")) {
                                    Utils.showToast(mContext, "" + jResponse.getString("message"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
//                        if (response.code() == 400) {
//                            Utils.showToast(getActivity(), response.code() + " " + getString(R.string.msg_outof_order));
//                        } else {
//                            Utils.showAlertDialog(getActivity(), "" + response.code());
//                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println(t.getMessage());
                    Utils.closeProgressDialog();
                    Utils.showAlertDialog(getActivity(), "" + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Utils.showAlertDialog(getActivity(), "" + e.getMessage());
        }

    }

    private void InitializeControlsAction() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            mSku = bundle.getString(BNDL_SKU);
            LogUtils.e("", "mSku::" + mSku);
            if (bundle.containsKey(BNDL_IS_FROM_SEARCH)) {
                isFromSearch = bundle.getBoolean(BNDL_IS_FROM_SEARCH);
                LogUtils.e("", "isFromSearch::" + isFromSearch);
            }
        }

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

            tvPrice.setTypeface(Shy7lo.DroidKufiRegular);
            tvNewPrice.setTypeface(Shy7lo.DroidKufiRegular);
            tvName.setTypeface(Shy7lo.DroidKufiRegular);
            tvSize.setTypeface(Shy7lo.DroidKufiRegular);
            tvSku.setTypeface(Shy7lo.DroidKufiRegular);
            tvNameInfo.setTypeface(Shy7lo.DroidKufiRegular);
            tvTitle.setTypeface(Shy7lo.DroidKufiBold);
            tvAdditionalInfoTitle.setTypeface(Shy7lo.DroidKufiBold);
            tvSkuTitle.setTypeface(Shy7lo.DroidKufiRegular);
            tvNameInfoTitle.setTypeface(Shy7lo.DroidKufiRegular);
            tvAddToBag.setTypeface(Shy7lo.DroidKufiRegular);

        } else {

            tvPrice.setTypeface(Shy7lo.RalewayRegular);
            tvNewPrice.setTypeface(Shy7lo.RalewayRegular);
            tvName.setTypeface(Shy7lo.RalewayRegular);
            tvSize.setTypeface(Shy7lo.RalewayRegular);
            tvSku.setTypeface(Shy7lo.RalewayRegular);
            tvNameInfo.setTypeface(Shy7lo.RalewayRegular);
            tvTitle.setTypeface(Shy7lo.RalewayBold);
            tvAdditionalInfoTitle.setTypeface(Shy7lo.RalewayBold);
            tvSkuTitle.setTypeface(Shy7lo.RalewayRegular);
            tvNameInfoTitle.setTypeface(Shy7lo.RalewayRegular);
            tvAddToBag.setTypeface(Shy7lo.RalewayRegular);
        }

        mCurrencyCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR));
        mExchangeRate = MyPref.getPref(getActivity(), MyPref.DEFAULT_EXCHANGE_RATE, 1f);


        wvDescription.getSettings().setJavaScriptEnabled(true);
        wvDescription.setBackgroundColor(Color.TRANSPARENT);

        wvDescriptionInfo.getSettings().setJavaScriptEnabled(true);
        wvDescriptionInfo.setBackgroundColor(Color.TRANSPARENT);

        ibCancel.setOnClickListener(this);
        lnrAddToBag.setOnClickListener(this);
        tvSize.setOnClickListener(this);
        tvItemInfo.setOnClickListener(this);
        ivFastShipping.setOnClickListener(this);
        ivCashReturn.setOnClickListener(this);
        ivDayReturn.setOnClickListener(this);
        ivLigal.setOnClickListener(this);
        tvSizeTable.setOnClickListener(this);

        tbWishlist.setOnCheckedChangeListener(checkListener);

        ibShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (productDetails != null) {

                    Uri imageUri = Uri.parse("");

                    List<ProductDetails.Image_> imageList = productDetails.getImages();
                    String image = "";
//                    if (imageList != null && imageList.size() > 0) {
//
//                        image = imageList.get(0).getUrl();
//                        LogUtils.e("", "image::" + image);
//                        imageUri = Uri.parse(image);
//                    }

                    if (!TextUtils.isEmpty(productDetails.getUrlPath())) {

                        image = productDetails.getUrlPath();
                        imageUri = Uri.parse(productDetails.getUrlPath());

                    } else {
                        if (!TextUtils.isEmpty(productDetails.getUrlKey())) {
                            image = productDetails.getUrlKey();
                            imageUri = Uri.parse(productDetails.getUrlKey());
                        }
                    }


                    LogUtils.e("", "image::" + image);
                    LogUtils.e("", "imageUri::" + imageUri);

//                    Intent shareIntent = new Intent();
//                    shareIntent.setAction(Intent.ACTION_SEND);
//                    shareIntent.putExtra(Intent.EXTRA_TEXT, "" + productDetails.getName() + "\n" + image);
//                    shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, productDetails.getName());
//                    shareIntent.setType("text/plain");
////                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
////                    shareIntent.setType("*/*");
////                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    startActivity(Intent.createChooser(shareIntent, "Share"));


                    if (getActivity() instanceof HomeActivity) {
                        HomeActivity activity = (HomeActivity) getActivity();
                        activity.shareProduct(productDetails.getName(), image, productDetails.getSku());
                    }

                }

            }
        });
    }

    private CompoundButton.OnCheckedChangeListener checkListener
            = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {

            LogUtils.e("", "wishlist isChecked::" + isChecked);

            String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
            if (TextUtils.isEmpty(userToken)) {
                Utils.showToast(getActivity(), "" + getString(R.string.msg_need_login));
                tbWishlist.setOnCheckedChangeListener(null);
                tbWishlist.setChecked(!tbWishlist.isChecked());
                tbWishlist.setOnCheckedChangeListener(checkListener);
            } else {

                if (productDetails != null) {

                    String sku = "";
//                if (!TextUtils.isEmpty(userToken)) {
                    if (!isFromSearch) {
                        sku = mSku;
                    } else {
                        if (productDetails != null && !TextUtils.isEmpty(productDetails.getSku())) {
                            sku = productDetails.getSku();
                        }
                    }
//                }

                    if (isChecked) {

                        Wishlist.WishlistData mWishItem = new Wishlist().new WishlistData();
                        mWishItem.setId(productDetails.getEntityId());
                        mWishItem.setName(productDetails.getName());
//                    mWishItem.setDescription(productDetails.getDescription());
                        mWishItem.setDescription("");
                        mWishItem.setSku(sku);
                        mWishItem.setProductId(productDetails.getEntityId());
                        mWishItem.setTypeId(productDetails.getTypeId());
                        mWishItem.setQty(1);
                        if (!TextUtils.isEmpty("" + productDetails.getPrice()) && productDetails.getPrice() > 0) {
                            mWishItem.setPrice(productDetails.getPrice());
                        }
                        if (!TextUtils.isEmpty("" + productDetails.getSpecial_price()) && productDetails.getSpecial_price() > 0) {
                            mWishItem.setSpecial_price(productDetails.getSpecial_price());
                        }
                        mWishItem.setSpecial_from_date(productDetails.getSpecial_from_date());
                        mWishItem.setSpecial_to_date(productDetails.getSpecial_to_date());
                        mWishItem.setThumbNail(productDetails.getImages().get(0).getUrl());
                        mWishItem.setBrand(productDetails.getBrandName());
                        mWishItem.setRating(0);
//                    mWishItem.setStockQty(productDetails.getStockQty());
                        mWishItem.setStockQty(1);
                        if (!TextUtils.isEmpty(mSizeLable)) {
                            mWishItem.setSize(mSizeLable);
                        }
                        mWishItem.setStockStatus(productDetails.getStockStatus());

                        if (!TextUtils.isEmpty(userToken)) {
                            mWishItem.setIsGuest("0");
                            mWishItem.setToken(userToken);

//                        addProducToWishlist(sku);

//                        mWishItem.setIs_on_server("1");

                        } else {
                            mWishItem.setIsGuest("1");
                            String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
                            mWishItem.setToken(guestToken);
//                        mWishItem.setIs_on_server("1");
                        }

                        if (Utils.isInternetConnected(getActivity())) {
                            mWishItem.setIs_on_server("0");
                        } else {
                            mWishItem.setIs_on_server("0");
                        }

                        mWishItem.setSoft_delete("0");
                        mWishItem.setSize(mSizeLable);
                        if (dbAdapter.isContainInWishList(mSku)) {
                            dbAdapter.updateSoftDeleteWishItem(mSku, "0");
                        } else {
                            dbAdapter.addWishItem(mWishItem);
                        }

                        if (productDetails.getTypeId().equalsIgnoreCase("configurable")) {
                            WishListSync mWishListSync = new WishListSync(getActivity());
                            mWishListSync.syncWishList(getActivity(), dbAdapter);
                        }

//                    tbWishlist.setOnCheckedChangeListener(null);
//                    tbWishlist.setEnabled(false);

//                    Utils.showToast(getActivity(), "" + getString(R.string.msg_add_wishlist));
                        if (getActivity() instanceof HomeActivity) {
                            HomeActivity activity = (HomeActivity) getActivity();
                            activity.showWishListMsg(getResources().getString(R.string.msg_item_saved));
                        }

                        try {
                            AdjustEvent event = new AdjustEvent("wxnr5r");
                            event.addPartnerParameter("Name", "" + "" + productDetails.getName());
                            event.addPartnerParameter("Price", "" + "" + Utils.getAnswerPrice(mExchangeRate, mFinalRate));
                            event.addPartnerParameter("Currency", "" + "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, ""));
                            event.addPartnerParameter("Product Type", "" + (productDetails.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple"));
                            event.addPartnerParameter("SKU", "" + "" + productDetails.getSku());

                            //Callback
                            event.addCallbackParameter("Name", "" + "" + productDetails.getName());
                            event.addCallbackParameter("Price", "" + "" + Utils.getAnswerPrice(mExchangeRate, mFinalRate));
                            event.addCallbackParameter("Currency", "" + "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, ""));
                            event.addCallbackParameter("Product Type", "" + (productDetails.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple"));
                            event.addCallbackParameter("SKU", "" + "" + productDetails.getSku());

                            String mCriteo = MyPref.getPref(getActivity(), MyPref.APP_CRITEO, "");
                            if (TextUtils.isEmpty(mCriteo) || mCriteo.equalsIgnoreCase("1")) {
                                AdjustCriteo.injectCustomerIdIntoCriteoEvents(TextUtils.isEmpty(userToken) ? userToken : MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""));
                            }

                            Adjust.trackEvent(event);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {

                        if (TextUtils.isEmpty(userToken) && dbAdapter.isGuestWishItem(sku)) {
                            dbAdapter.removeWishItem(sku);
                        } else {
                            dbAdapter.updateSoftDeleteWishItem(sku, "1");

                        }

                        if (getActivity() instanceof HomeActivity) {
                            HomeActivity activity = (HomeActivity) getActivity();
                            activity.showWishListMsg(getResources().getString(R.string.msg_item_removed));
                        }
//                    Utils.showToast(getActivity(), "" + getString(R.string.product_removed));

//                    if (!TextUtils.isEmpty(userToken)) {
//                        Utils.deleteProduct(mContext, dbAdapter, value.getId(), sku);
//                    }

                    }

                    Intent broadIntent = new Intent();
                    broadIntent.setAction(ProductCategoryFragment.WISHLIST_CHANGE_EXPIRE);
                    mContext.sendBroadcast(broadIntent);

                    if (getActivity() instanceof HomeActivity) {
                        ((HomeActivity) getActivity()).updateWishListBadgetCount();
                    }

                    ((HomeActivity) getActivity()).isShoppingBagRefreshed = true;
                    ((HomeActivity) getActivity()).isWishListRefreshed = true;
                }
            }

//            if (isChecked) {
//                if (!TextUtils.isEmpty(userToken)) {
//                    if (!isFromSearch) {
//                        addProducToWishlist(mSku);
//                    } else {
//                        if (productDetails != null && !TextUtils.isEmpty(productDetails.getSku())) {
//                            addProducToWishlist(productDetails.getSku());
//                        }
//                    }
//                } else {
//                    tbWishlist.setOnCheckedChangeListener(null);
//                    tbWishlist.setChecked(false);
//                    tbWishlist.setOnCheckedChangeListener(checkListener);
////                    Utils.showToast(getActivity(), "" + getString(R.string.msg_need_login));
//                    showLoginDialog();
//                }
//            } else {
////                if (!TextUtils.isEmpty(userToken)) {
////                    deleteProduct(mSku);
////                } else {
//                tbWishlist.setOnCheckedChangeListener(null);
//                tbWishlist.setChecked(true);
//                tbWishlist.setOnCheckedChangeListener(checkListener);
////                    Utils.showToast(getActivity(), "" + getString(R.string.msg_need_login));
////                }
//            }

        }
    };


//    private void showWishListMsg(String msg) {
//        tvWishlistText.setText("" + msg);
//        tvWishlistText.setVisibility(View.VISIBLE);
//        tvWishlistText.startAnimation(animOpen);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hideWishListMsg();
//            }
//        }, 1000);
//    }
//
//    private void hideWishListMsg() {
//        tvWishlistText.startAnimation(animClose);
//        animClose.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                tvWishlistText.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }

//    private void addProducToWishlist(final String sku) {
//
//        if (!Utils.isInternetConnected(getActivity())) {
//            return;
//        }
//
//        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
//
//        if (!TextUtils.isEmpty(userToken)) {
//
//            JSONObject jsonParams = new JSONObject();
//            try {
//                jsonParams.put("sku", "" + sku);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
//
//            try {
//                ApiInterface apiService =
//                        RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//                Call<JsonElement> call = apiService.addProductToWishlist(Shy7lo.mLangCode, "Bearer " + userToken, body);
//                call.enqueue(new Callback<JsonElement>() {
//                    @Override
//                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//
//                        if (response.isSuccessful()) {
//
//                            try {
//                                JSONObject jResponse = new JSONObject(response.body().toString());
//
//                                if (jResponse != null && jResponse.getString("success").equals("1")) {
//                                    dbAdapter.updateIsOnServerWishItem(sku, "1");
//                                    if (RestClient.isTuneEnable) {
//                                        try {
//                                            Tune tune = Tune.getInstance();
//                                            List<TuneEventItem> mTuneEventItemsList = new ArrayList<TuneEventItem>();
//                                            TuneEventItem mTuneEventItem = new TuneEventItem("" + productDetails.getName());
//                                            mTuneEventItemsList.add(mTuneEventItem);
//                                            tune.measureEvent(new TuneEvent(TuneEvent.ADD_TO_WISHLIST)
//                                                    .withEventItems(mTuneEventItemsList)
//                                                    .withQuantity(1)
//                                                    .withContentId(productDetails.getSku())
//                                                    .withRevenue(0)
//                                                    .withContentType(productDetails.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
//                                                    .withCurrencyCode("" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")));
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//                                } else if (jResponse != null && jResponse.getString("success").equals("0")) {
//                                    dbAdapter.updateIsOnServerWishItem(sku, "0");
//                                } else if (jResponse != null && jResponse.getString("success").equals("2")) {
//                                    dbAdapter.updateIsOnServerWishItem(sku, "0");
//                                    Utils.showInitialScreen(mContext);
//                                    return;
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<JsonElement> call, Throwable t) {
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    private void addProducToWishlist(final String sku) {
//
//        if (!Utils.isInternetConnected(getActivity())) {
//            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
//                @Override
//                public void onRetryClicked(Dialog dialog) {
//                    if (Utils.isInternetConnected(getActivity())) {
//                        if (dialog != null && dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        addProducToWishlist(sku);
//                    }
//                }
//            });
//            return;
//        }
//
//        Utils.showProgressDialog(getActivity());
//
//        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
//
//        if (!TextUtils.isEmpty(userToken)) {
//
//            JSONObject jsonParams = new JSONObject();
//            try {
//                jsonParams.put("sku", "" + sku);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
//
//            try {
//                ApiInterface apiService =
//                        RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//                Call<JsonElement> call = apiService.addProductToWishlist(Shy7lo.mLangCode, "Bearer " + userToken, body);
//                call.enqueue(new Callback<JsonElement>() {
//                    @Override
//                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                        Utils.closeProgressDialog();
//                        if (response.isSuccessful()) {
//
//                            try {
//                                Tune tune = Tune.getInstance();
//                                List<TuneEventItem> mTuneEventItemsList = new ArrayList<TuneEventItem>();
//                                TuneEventItem mTuneEventItem = new TuneEventItem("" + productDetails.getName());
//                                mTuneEventItemsList.add(mTuneEventItem);
//                                tune.measureEvent(new TuneEvent(TuneEvent.ADD_TO_WISHLIST)
//                                        .withEventItems(mTuneEventItemsList)
//                                        .withQuantity(1)
//                                        .withContentId(productDetails.getSku())
//                                        .withRevenue(0)
//                                        .withContentType(productDetails.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
//                                        .withCurrencyCode("" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")));
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                            Utils.showToast(getActivity(), "" + getString(R.string.msg_add_wishlist));
//                        } else {
//                            Utils.showAlertDialog(getActivity());
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<JsonElement> call, Throwable t) {
//                        Utils.closeProgressDialog();
//                        Utils.showToast(getActivity(), "" + getString(R.string.msg_something_wrong));
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//                Utils.showAlertDialog(getActivity());
//            }
//        }
//    }

//    private void deleteProduct(String item_id) {
//
//        Utils.showProgressDialog(getActivity());
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//
//        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
//
//        Map<String, Object> jsonParams = new ArrayMap<>();
//        jsonParams.put("item_id", "" + item_id);
//
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
//                (new JSONObject(jsonParams)).toString());
//
//        Call<JsonElement> callCode = apiService.deleteProductFromWishlist(Shy7lo.mLangCode, "Bearer " + userToken, body);
//
//        callCode.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//
//                if (response.isSuccessful()) {
//                    try {
//
//                        JSONObject jResponse = new JSONObject(response.body().toString());
//                        if (jResponse != null && jResponse.getString("success").equals("1")) {
//
//                        } else if (jResponse != null && jResponse.getString("success").equals("0")) {
//                            Utils.showToast(getActivity(), "" + jResponse.getString("message"));
//                        }  else if (jResponse != null && jResponse.getString("success").equals("2")) {
//                            Utils.showInitialScreen(getActivity());
//                            return;
//                        }else {
//                            Utils.showProgressDialog(getActivity());
//                            Utils.showToast(getActivity(), "" + getString(R.string.msg_something_wrong));
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Utils.showProgressDialog(getActivity());
//                        Utils.showToast(getActivity(), "" + getString(R.string.msg_something_wrong));
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Utils.showToast(getActivity(), "" + getString(R.string.msg_something_wrong));
//                Utils.closeProgressDialog();
//            }
//        });
//    }

    @Override
    public void onClick(View view) {
        if (view == ibCancel) {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.openDrawer();
//                activity.onBackPressed();
            }

        } else if (view == lnrAddToBag) {
            if (productDetails != null) {
                if (productDetails.getTypeId().equalsIgnoreCase("configurable")) {
                    if (TextUtils.isEmpty(mOptionValue)) {
//                        Utils.showToast(getActivity(), "" + getString(R.string.msg_select_size));
                        List<ProductDetails.AttributeDetail> mAttributeDetailList =
                                productDetails.getConfigurableAttributes().getAttributeDetails();
                        if (mAttributeDetailList != null && mAttributeDetailList.size() > 0) {
                            for (int i = 0; i < mAttributeDetailList.size(); i++) {
                                ProductDetails.AttributeDetail mAttributeDetail = mAttributeDetailList.get(i);
//                                if (mAttributeDetail != null && mAttributeDetail.getAttributeName().contains("Size")) {
                                showSizeDialog(mAttributeDetail, productDetails.countrySizes);
//                                }
                            }
                        }
                        return;
                    }
                }


//                String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
//                if (TextUtils.isEmpty(userToken)) {
//
//                    Utils.showProgressDialog(getActivity());
//
//                    String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
//
//                    ApiInterface apiService =
//                            RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//                    Call<JsonElement> call = apiService.checkGuestCartToken(Shy7lo.mLangCode, guestToken);
//                    call.enqueue(new Callback<JsonElement>() {
//                        @Override
//                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//
//                            LogUtils.e(TAG, "response code:" + response.code());
//
//                            if (response.isSuccessful()) {
//                                try {
//
//                                    JSONObject jResponse = new JSONObject(response.body().toString());
//                                    if (jResponse != null && jResponse.getString("success").equalsIgnoreCase("1")) {
//                                        JSONObject jData = jResponse.getJSONObject("data");
//                                        if (jData != null && jData.has("cart_id")) {
//                                            String token = jData.getString("cart_id");
//                                            LogUtils.e(TAG, "response token:" + token);
//
//                                            if (!TextUtils.isEmpty(token)) {
//                                                MyPref.setPref(mContext, MyPref.GUEST_CART_ID, token);
//                                            }
//
//                                            addProductIntoCart();
//                                        }
//                                    }
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    Utils.closeProgressDialog();
//                                }
//
//                            } else {
//                                Utils.closeProgressDialog();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<JsonElement> call, Throwable t) {
//                            System.out.println(t.getMessage());
//                            Utils.closeProgressDialog();
//                        }
//                    });
//
//                } else {
                addProductIntoCart();
//                }
            }

        } else if (view == tvSize) {
            if (productDetails != null) {
//                List<ProductDetails.Option> optionList = productDetails.getOptions();
//                if (optionList != null && optionList.size() > 0) {
//                    for (ProductDetails.Option option : optionList) {
//                        if (option.getLabel().equalsIgnoreCase("Size")) {
//
//                            showSizeDialog(option);
//                            break;
//                        }
//                    }
//                }
                if (productDetails.getTypeId().equalsIgnoreCase("configurable")) {
                    tvSize.setVisibility(View.VISIBLE);
                    List<ProductDetails.AttributeDetail> mAttributeDetailList =
                            productDetails.getConfigurableAttributes().getAttributeDetails();
                    if (mAttributeDetailList != null && mAttributeDetailList.size() > 0) {
                        for (int i = 0; i < mAttributeDetailList.size(); i++) {
                            ProductDetails.AttributeDetail mAttributeDetail = mAttributeDetailList.get(i);
//                            if (mAttributeDetail != null && mAttributeDetail.getAttributeName().contains("Size")) {
                            showSizeDialog(mAttributeDetail, productDetails.countrySizes);
//                            }
                        }
                    }
                } else {
                    tvSize.setVisibility(View.GONE);
                    mViewSpace.setVisibility(View.GONE);
                }


            }
        } else if (view == tvItemInfo) {
//            if (lnrItemInfo.getVisibility() != View.VISIBLE) {
//                lnrItemInfo.setVisibility(View.VISIBLE);
//            } else {
//                lnrItemInfo.setVisibility(View.GONE);
//            }
        } else if (view == ivFastShipping) {
//            if (productDetails != null) {
//                showMsgDialog(tvFastShipping.getText().toString());
//            }
        } else if (view == ivCashReturn) {
//            if (productDetails != null) {
//                showMsgDialog(tvCashReturn.getText().toString());
//            }
        } else if (view == ivDayReturn) {
//            if (productDetails != null) {
//                showMsgDialog(tvDayReturn.getText().toString());
//            }
        } else if (view == ivLigal) {
//            if (productDetails != null) {
//                showMsgDialog(tvLigal.getText().toString());
//            }
        } else if (view == tvSizeTable) {
            if (productDetails != null) {
                Bundle bundle = new Bundle();
                bundle.putString(WebviewActivity.BNDL_TITLE, "" + getString(R.string.sizes_table));
                bundle.putString(WebviewActivity.BNDL_URL, productDetails.getSize_chart());
                IntentHandler.startActivity(getActivity(), WebviewActivity.class, bundle);
            }
        }

    }

    private void showMsgDialog(String msg) {


        final Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_offline);

        Button btnRetry = (Button) mDialog.findViewById(R.id.btnRetry);
        TextView tvOfflineText = (TextView) mDialog.findViewById(R.id.tvOfflineText);

        btnRetry.setText(getResources().getString(R.string.pf_ok));
        tvOfflineText.setText("" + msg);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                    mDialog.cancel();
                }

            }
        });

        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        mDialog.show();

    }

    public void showOrderMoreDialog() {

        final Dialog dialog = new Dialog(getMyActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_order_more);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        Button btnContinueShopping = (Button) dialog.findViewById(R.id.btnContinueShopping);
        Button btnViewCart = (Button) dialog.findViewById(R.id.btnViewCart);
        TextView tvQuestion = (TextView) dialog.findViewById(R.id.tvQuestion);


        if (MyPref.getPref(getMyActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            tvQuestion.setVisibility(View.GONE);
        } else {
            tvQuestion.setVisibility(View.VISIBLE);
        }

        btnContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }

                if (getMyActivity() instanceof HomeActivity) {

                    HomeActivity activity = (HomeActivity) getMyActivity();
                    activity.onBackPressed();
                }
            }
        });

        btnViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }

                if (getMyActivity() instanceof HomeActivity) {

                    HomeActivity activity = (HomeActivity) getMyActivity();
                    activity.loadShoppingBags();
//                    activity.loadShoppingBagsFromDetails();
                }

            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        dialog.show();

    }

    int position = 0;
    String mSelectedName = "";

    public void showSizeDialog(ProductDetails.AttributeDetail mAttributeDetail, final List<ProductDetails.CountrySizes> countrySizes) {

        final Animation animClose, animOpen;
        animOpen = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_open);
        animClose = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_close);

//        final Dialog dialog = new Dialog(new ContextThemeWrapper(getActivity(), R.style.DialogSlideAnim));
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_size_list);

        final List<ProductDetails.AttributeOption> mAttributeOptionList = mAttributeDetail.getOption();
        LogUtils.e("", "mAttributeOptionList::" + mAttributeOptionList);
        mOptionId = mAttributeDetail.getAttributeId();
        for (int j = 0; j < mAttributeOptionList.size(); j++) {
            mAttributeOptionList.get(j).setSelected(false);
        }

        // set values for custom dialog components - text, image and button
        final RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.rlMain);
        final LinearLayout lnrContainer = (LinearLayout) dialog.findViewById(R.id.lnrContainer);
        final LinearLayout lnrLocal = (LinearLayout) dialog.findViewById(R.id.lnrLocal);
        final TextView tvSelectSize = (TextView) dialog.findViewById(R.id.tvSelectSize);
        final TextView tvDone = (TextView) dialog.findViewById(R.id.tvDone);
//        final TextView tvInternational = (TextView) dialog.findViewById(R.id.tvInternational);
//        final TextView tvUs = (TextView) dialog.findViewById(R.id.tvUs);

//        tvInternational.setTextColor(getResources().getColor(R.color.black));
//        tvUs.setTextColor(getResources().getColor(R.color.gray_89));
        GridView gvSize = (GridView) dialog.findViewById(R.id.gvSize);

//        ExpandableHeightGridView gvSize = (ExpandableHeightGridView) dialog.findViewById(R.id.gvSize);
        final WheelSizePicker pickerSize = (WheelSizePicker) dialog.findViewById(R.id.pickerSize);

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            lnrContainer.setScaleX(-1f);
            tvSelectSize.setScaleX(-1f);
            pickerSize.setScaleX(-1f);
            tvDone.setScaleX(-1f);
        } else {
            lnrContainer.setScaleX(1f);
            tvSelectSize.setScaleX(1f);
            pickerSize.setScaleX(1f);
            tvDone.setScaleX(1f);
        }


        pickerSize.updateSize(mAttributeOptionList);
        pickerSize.updateIndex(position);
//        pickerSize.setOnSizeScrollListener(new WheelSizePicker.OnSizeScrollListener() {
//            @Override
//            public void onCountryScroll(WheelSizePicker picker, int index, String name) {
//
//                position = index;
//                mSelectedName = name;
//
//            }
//        });

//        final ProductSizeAdapter productSizeAdapter = new ProductSizeAdapter(getActivity(), mAttributeOptionList, new ProductSizeAdapter.OnSizeSelectListener() {
//            @Override
//            public void onSizeSelect(String lable, int position, boolean isLable) {
//
//                tvSize.setText("" + lable);
//                isUsLable = isLable;
//                lnrContainer.startAnimation(animClose);
//                mOptionValue = mAttributeOptionList.get(position).getValueIndex();
//                mSizeLable = mAttributeOptionList.get(position).getDefaultLabel();
//                ProductDetails.AttributeOption mAttributeOption = mAttributeOptionList.get(position);
//                if (mAttributeOption != null) {
//
//                    if (!TextUtils.isEmpty(mAttributeOption.getPrice())) {
////                                            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
////                                                tvPrice.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, Float.parseFloat(productDetails.getPrice())));
////                                            } else {
////                        tvPrice.setText(Utils.getRealPriceWithQty(mExchangeRate, Float.parseFloat(mAttributeOption.getPrice()), mAttributeOption.getQty()) + " " + mCurrencyCode);
////                        tvPrice.setText(Utils.getRealPrice(mExchangeRate, parseFloat(mAttributeOption.getPrice())) + " " + mCurrencyCode);
//                        SpannableStringBuilder sb = new SpannableStringBuilder(Utils.getIntPrice(mExchangeRate, parseFloat(mAttributeOption.getPrice())) + " " + mCurrencyCode);
//                        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), 0, Utils.getIntPrice(mExchangeRate, parseFloat(mAttributeOption.getPrice())).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                        } else {
//                            sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, Utils.getIntPrice(mExchangeRate, parseFloat(mAttributeOption.getPrice())).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                        }
//                        tvPrice.setText(sb);
//                        mFinalRate = parseFloat(mAttributeOption.getPrice());
//
//                        price = mAttributeOption.getPrice();
//                        special_price = mAttributeOption.getSpecialPrice();
//                        special_from_date = mAttributeOption.getSpecialFromDate();
//                        special_to_date = mAttributeOption.getSpecialToDate();
////                                            }
//
//                    }
//
//                    tvNewPrice.setVisibility(View.GONE);
//                    tvPrice.setPaintFlags(0);
//                    tvPrice.setTextColor(getResources().getColor(R.color.black));
//
//                    try {
//                        if (mAttributeOption.getSpecialPrice() != null && !TextUtils.isEmpty(String.valueOf(mAttributeOption.getSpecialPrice()))) {
//
//                            if (!TextUtils.isEmpty(mAttributeOption.getSpecialFromDate()) && !TextUtils.isEmpty(mAttributeOption.getSpecialToDate())) {
//
//                                Date fromDate = sdfDate.parse(mAttributeOption.getSpecialFromDate());
//                                Date toDate = sdfDate.parse(mAttributeOption.getSpecialToDate());
//                                Date currentDate = new Date();
//
//                                if (currentDate.after(fromDate) && currentDate.before(toDate)) {
//                                    mFinalRate = Float.parseFloat(mAttributeOption.getSpecialPrice());
//
////                                    tvNewPrice.setText(Utils.getRealPrice(mExchangeRate, parseFloat(mAttributeOption.getSpecialPrice())) + " " + mCurrencyCode);
////                                    tvNewPrice.setText(Utils.getRealPriceWithQty(mExchangeRate, Float.parseFloat(mAttributeOption.getSpecialPrice()), mAttributeOption.getQty()) + " " + mCurrencyCode);
//
//                                    SpannableStringBuilder sb = new SpannableStringBuilder(Utils.getIntPrice(mExchangeRate, parseFloat(mAttributeOption.getSpecialPrice())) + " " + mCurrencyCode);
//                                    if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                                        sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), 0, Utils.getIntPrice(mExchangeRate, parseFloat(mAttributeOption.getSpecialPrice())).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                                    } else {
//                                        sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, Utils.getIntPrice(mExchangeRate, parseFloat(mAttributeOption.getSpecialPrice())).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                                    }
//                                    tvNewPrice.setText(sb);
//                                    tvNewPrice.setVisibility(View.VISIBLE);
//
//                                    if (!TextUtils.isEmpty(productDetails.getPrice())) {
//                                        tvPrice.setText(Utils.getIntPrice(mExchangeRate, parseFloat(mAttributeOption.getPrice())) + "");
//                                    } else {
//                                        tvPrice.setText("" + parseFloat(productDetails.getPrice()) + " ");
//                                    }
//                                    tvPrice.setTextColor(mContext.getResources().getColor(R.color.gray_66));
//                                    tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                                }
//
//                            }
//
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        });

//        if (productSizeAdapter != null) {
//            productSizeAdapter.setLableLocal(isUsLable);

//            if (isUsLable){
//                tvInternational.setTextColor(getResources().getColor(R.color.gray_89));
//                tvUs.setTextColor(getResources().getColor(R.color.black));
//            }else{
//                tvInternational.setTextColor(getResources().getColor(R.color.black));
//                tvUs.setTextColor(getResources().getColor(R.color.gray_89));
//            }
//        }

//        gvSize.setAdapter(productSizeAdapter);
//        gvSize.setExpanded(true);

        tvSelectSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (productDetails != null && !TextUtils.isEmpty(productDetails.getSize_chart())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(WebviewActivity.BNDL_TITLE, "" + getString(R.string.sizes_table));
                    bundle.putString(WebviewActivity.BNDL_URL, productDetails.getSize_chart());
                    IntentHandler.startActivity(getActivity(), WebviewActivity.class, bundle);
                }

            }
        });

        if (mAttributeDetail.getAttributeId().equalsIgnoreCase("147")) {
            if (countrySizes != null && countrySizes.size() > 0) {
                lnrLocal.setVisibility(View.VISIBLE);
                if (lnrLocal != null && lnrLocal.getChildCount() > 0) {
                    lnrLocal.removeAllViews();
                }
                for (int i = 0; i < countrySizes.size(); i++) {
                    final TextView tvLocal = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_size_local, null);
                    tvLocal.setText("" + countrySizes.get(i).name);
                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                        tvLocal.setScaleX(-1f);
                    } else {
                        tvLocal.setScaleX(1f);
                    }
                    if (i == 0) {
                        tvLocal.setTextColor(getResources().getColor(R.color.black));
                    }

                    final int finalI = i;
                    tvLocal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            for (int j = 0; j < lnrLocal.getChildCount(); j++) {
                                TextView tvChild = (TextView) lnrLocal.getChildAt(j);
                                tvChild.setTextColor(getResources().getColor(R.color.gray_89));
                            }

                            tvLocal.setTextColor(getResources().getColor(R.color.black));
                            pickerSize.updateLocalLable(countrySizes.get(finalI).labelName);
                            pickerSize.updateSize(mAttributeOptionList);
                            position = 0;
                            pickerSize.updateIndex(position);
//                            if (productSizeAdapter != null) {
//                                productSizeAdapter.setLableLocal(countrySizes.get(finalI).labelName);
//                                productSizeAdapter.notifyDataSetChanged();
//                            }

                        }
                    });

                    lnrLocal.addView(tvLocal);
                }

                String mSize = MyPref.getPref(getActivity(), MyPref.DEFAULT_SIZE, "");
                if (!TextUtils.isEmpty(mSize)) {
                    for (int j = 0; j < lnrLocal.getChildCount(); j++) {
                        TextView tvChild = (TextView) lnrLocal.getChildAt(j);
                        if (!TextUtils.isEmpty(tvChild.getText().toString()) && tvChild.getText().toString().equalsIgnoreCase(mSize)) {
                            tvChild.setTextColor(getResources().getColor(R.color.black));
                            pickerSize.updateLocalLable(countrySizes.get(j).labelName);
                            pickerSize.updateSize(mAttributeOptionList);
                            position = 0;
                            pickerSize.updateIndex(position);
                        } else {
                            tvChild.setTextColor(getResources().getColor(R.color.gray_89));
                        }

                    }
                }

            } else {
                lnrLocal.setVisibility(View.GONE);
            }
        } else {
            lnrLocal.setVisibility(View.GONE);
        }


//        tvInternational.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tvInternational.setTextColor(getResources().getColor(R.color.black));
//                tvUs.setTextColor(getResources().getColor(R.color.gray_89));
//                if (productSizeAdapter != null) {
//                    productSizeAdapter.setLableLocal(false);
//                    productSizeAdapter.notifyDataSetChanged();
//                }
//            }
//        });

//        tvUs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tvInternational.setTextColor(getResources().getColor(R.color.gray_89));
//                tvUs.setTextColor(getResources().getColor(R.color.black));
//                if (productSizeAdapter != null) {
//                    productSizeAdapter.setLableLocal(true);
//                    productSizeAdapter.notifyDataSetChanged();
//                }
//            }
//        });

//        gvSize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//
//                for (int j = 0; j < valueList.size(); j++) {
//                    valueList.get(j).setSelected(false);
//                }
//                valueList.get(position).setSelected(true);
//
//                for (int j = 0; j < valueList.size(); j++) {
//                    LogUtils.e("", "position::" + j + "" + valueList.get(j).getSelected());
//                }
//
//                if (productSizeAdapter != null) {
//                    productSizeAdapter.notifyDataSetChanged();
//                }
//
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

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                position = pickerSize.getCurrentItemPosition();
                mSelectedName = pickerSize.getCurrentSize();

                isUsLable = false;
                lnrContainer.startAnimation(animClose);
                mOptionValue = mAttributeOptionList.get(position).getValueIndex();
                mSizeLable = mAttributeOptionList.get(position).getDefaultLabel();
                if (TextUtils.isEmpty(mSelectedName)) {
                    mSelectedName = mSizeLable;
                }
                tvSize.setText("" + mSelectedName);
                ProductDetails.AttributeOption mAttributeOption = mAttributeOptionList.get(position);
                if (mAttributeOption != null) {

                    if (!TextUtils.isEmpty("" + mAttributeOption.getPrice()) && mAttributeOption.getPrice() > 0) {
//                                            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                                                tvPrice.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, Float.parseFloat(productDetails.getPrice())));
//                                            } else {
//                        tvPrice.setText(Utils.getRealPriceWithQty(mExchangeRate, Float.parseFloat(mAttributeOption.getPrice()), mAttributeOption.getQty()) + " " + mCurrencyCode);
//                        tvPrice.setText(Utils.getRealPrice(mExchangeRate, parseFloat(mAttributeOption.getPrice())) + " " + mCurrencyCode);
                        SpannableStringBuilder sb = new SpannableStringBuilder(Utils.getIntPrice(mExchangeRate, mAttributeOption.getPrice()) + " " + mCurrencyCode);
                        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                            sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), 0, Utils.getIntPrice(mExchangeRate, mAttributeOption.getPrice()).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        } else {
                            sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, Utils.getIntPrice(mExchangeRate, mAttributeOption.getPrice()).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        }
                        tvPrice.setText(sb);
                        mFinalRate = mAttributeOption.getPrice();

                        price = mAttributeOption.getPrice();
                        special_price = mAttributeOption.getSpecialPrice();
                        special_from_date = mAttributeOption.getSpecialFromDate();
                        special_to_date = mAttributeOption.getSpecialToDate();
//                                            }

                    }

                    tvNewPrice.setVisibility(View.GONE);
                    tvPrice.setPaintFlags(0);
                    tvPrice.setTextColor(getResources().getColor(R.color.black));

                    try {
                        if (mAttributeOption.getSpecialPrice() > 0 && !TextUtils.isEmpty(String.valueOf(mAttributeOption.getSpecialPrice()))) {

                            if (!TextUtils.isEmpty(mAttributeOption.getSpecialFromDate()) && !TextUtils.isEmpty(mAttributeOption.getSpecialToDate())) {

                                Date fromDate = sdfDate.parse(mAttributeOption.getSpecialFromDate());
                                Date toDate = sdfDate.parse(mAttributeOption.getSpecialToDate());
                                Date currentDate = new Date();

                                if (currentDate.after(fromDate) && currentDate.before(toDate)) {
                                    mFinalRate = mAttributeOption.getSpecialPrice();

//                                    tvNewPrice.setText(Utils.getRealPrice(mExchangeRate, parseFloat(mAttributeOption.getSpecialPrice())) + " " + mCurrencyCode);
//                                    tvNewPrice.setText(Utils.getRealPriceWithQty(mExchangeRate, Float.parseFloat(mAttributeOption.getSpecialPrice()), mAttributeOption.getQty()) + " " + mCurrencyCode);

                                    SpannableStringBuilder sb = new SpannableStringBuilder(Utils.getIntPrice(mExchangeRate, mAttributeOption.getSpecialPrice()) + " " + mCurrencyCode);
                                    if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                                        sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), 0, Utils.getIntPrice(mExchangeRate, mAttributeOption.getSpecialPrice()).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                    } else {
                                        sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, Utils.getIntPrice(mExchangeRate, mAttributeOption.getSpecialPrice()).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                    }
                                    tvNewPrice.setText(sb);
                                    tvNewPrice.setVisibility(View.VISIBLE);

                                    if (!TextUtils.isEmpty("" + productDetails.getPrice()) && productDetails.getPrice() > 0) {
                                        tvPrice.setText(Utils.getIntPrice(mExchangeRate, mAttributeOption.getPrice()) + "");
                                    } else {
                                        tvPrice.setText("" + productDetails.getPrice() + " ");
                                    }
                                    tvPrice.setTextColor(mContext.getResources().getColor(R.color.gray_66));
                                    tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                }

                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                rlMain.performClick();
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

//    public void showLoginDialog() {
//
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setContentView(R.layout.dialog_login);
//
//        Button btnLoginNow = (Button) dialog.findViewById(R.id.btnLoginNow);
//        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                    dialog.cancel();
//                }
//
//            }
//        });
//
//        btnLoginNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                    dialog.cancel();
//                }
//
//                if (getActivity() instanceof HomeActivity) {
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString(SignInUpActivity.BNDL_VIEW_TYPE, "SignIn");
//                    bundle.putString(SignInUpActivity.BNDL_SCREEN_FROM, "ProductDetailsFragment");
//                    IntentHandler.startActivity(getActivity(), SignInUpActivity.class, bundle);
//                }
//
//            }
//        });
//
//        dialog.show();
//
//    }


}
