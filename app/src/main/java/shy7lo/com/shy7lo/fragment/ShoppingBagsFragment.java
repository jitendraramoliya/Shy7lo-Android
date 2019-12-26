package shy7lo.com.shy7lo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.plugin.AdjustCriteo;
import com.adjust.sdk.plugin.CriteoProduct;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.StartCheckoutEvent;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.tune.TuneEventItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.BuildConfig;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.adapter.CartItemsAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.model.ShoppingBag;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.Constant;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

//import shy7lo.com.shy7lo.sync.ShoppingBagSync;

/**
 * Created by jiten on 2/9/16.
 */
public class ShoppingBagsFragment extends Fragment implements View.OnClickListener {

    public static String TAG_SHOPPING_BAGS_FRAGMENT = "ShoppingBagsFragment";

    @BindView(R.id.mTopLayout)
    View mTopLayout;
    //    @BindView(R.id.mSyncIndicator)
//    AVLoadingIndicatorView mSyncIndicator;
    @BindView(R.id.ibMore)
    ImageButton ibMore;
    @BindView(R.id.btnGoToProducts)
    Button btnGoToProducts;
    @BindView(R.id.lnrEmpty)
    LinearLayout lnrEmpty;
    @BindView(R.id.lnrCart)
    LinearLayout lnrCart;
    @BindView(R.id.lvCartProducts)
    ListView lvCartProducts;
    @BindView(R.id.tvSubPrice)
    TextView tvSubPrice;
    @BindView(R.id.tvTotalProducts)
    TextView tvTotalProducts;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvEmptyText)
    TextView tvEmptyText;
    @BindView(R.id.tvSubTotal)
    TextView tvSubTotal;
    @BindView(R.id.tvCheckout)
    TextView tvCheckout;
    @BindView(R.id.tvContinueShopping)
    TextView tvContinueShopping;
    @BindView(R.id.rltSubTotal)
    RelativeLayout rltSubTotal;
    LinearLayout lnrAmount;
    @BindView(R.id.ivPromotionalMsg)
    ImageView ivPromotionalMsg;

    DBAdapter dbAdapter;
//    ShoppingBagSync mShoppingBagSync;

    int mTotalItem = 0;

    private String mCurrencyCode = "";
    private float mExchangeRate;
    private float mTotalRate;

    List<ShoppingBag.Item> carItemList;

    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    List<TuneEventItem> mTuneEventItemsList = new ArrayList<TuneEventItem>();
    CartItemsAdapter cartItemsAdapter;
    View mView;

    private static ShoppingBagsFragment shoppingBagsFragment;

    public static ShoppingBagsFragment getInstance() {
        if (shoppingBagsFragment == null) {
            shoppingBagsFragment = new ShoppingBagsFragment();
        }
        return shoppingBagsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e(TAG_SHOPPING_BAGS_FRAGMENT, "onCreate");
//        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.e(TAG_SHOPPING_BAGS_FRAGMENT, "onCreateView");
        mView = inflater.inflate(R.layout.fragment_shopping_bags, container, false);
        Utils.setLocale(getActivity());
        ButterKnife.bind(this, mView);
        InitializeControls();
        InitializeControlsAction();

        LogUtils.e("", "language::" + getResources().getConfiguration().locale);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e(TAG_SHOPPING_BAGS_FRAGMENT, "onStart");

//        if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""))) {
//            Utils.getGuestCartToken(getActivity());
//        }

//        getShoppingListFromDB();
        if (!isShoppingBagRunning) {
            getShoppingBag();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.e(TAG_SHOPPING_BAGS_FRAGMENT, "hidden::" + hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e(TAG_SHOPPING_BAGS_FRAGMENT, "isVisibleToUser::" + isVisibleToUser);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.e(TAG_SHOPPING_BAGS_FRAGMENT, "onViewCreated");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.e(TAG_SHOPPING_BAGS_FRAGMENT, "onPause");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.e(TAG_SHOPPING_BAGS_FRAGMENT, "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.e(TAG_SHOPPING_BAGS_FRAGMENT, "onDetach");
    }


    private void InitializeControls() {

        dbAdapter = new DBAdapter(getActivity());

        mCurrencyCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR));
        mExchangeRate = MyPref.getPref(getActivity(), MyPref.DEFAULT_EXCHANGE_RATE, 1f);


        View mFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_shopping_bag_amount, null);
        lnrAmount = (LinearLayout) mFooterView.findViewById(R.id.lnrAmount);
        lvCartProducts.addFooterView(mFooterView, null, false);

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

            tvTitle.setTypeface(Shy7lo.DroidKufiBold);
            btnGoToProducts.setTypeface(Shy7lo.DroidKufiRegular);
            tvSubTotal.setTypeface(Shy7lo.DroidKufiBold);
            tvSubPrice.setTypeface(Shy7lo.DroidKufiBold);
            tvEmptyText.setTypeface(Shy7lo.DroidKufiRegular);
            tvTotalProducts.setTypeface(Shy7lo.DroidKufiRegular);
            tvCheckout.setTypeface(Shy7lo.DroidKufiBold);
            tvContinueShopping.setTypeface(Shy7lo.DroidKufiBold);

            tvTitle.setScaleX(-1f);
            mTopLayout.setScaleX(-1f);
            rltSubTotal.setScaleX(-1f);
//            lvCartProducts.setScaleX(-1f);
            tvSubTotal.setScaleX(-1f);
            tvSubPrice.setScaleX(-1f);
            tvTotalProducts.setScaleX(-1f);
            mFooterView.setScaleX(-1f);

        } else {

            tvTitle.setTypeface(Shy7lo.RalewayBold);
            btnGoToProducts.setTypeface(Shy7lo.RalewayRegular);
            tvSubTotal.setTypeface(Shy7lo.RalewayBold);
            tvSubPrice.setTypeface(Shy7lo.RalewayBold);
            tvEmptyText.setTypeface(Shy7lo.RalewayRegular);
            tvTotalProducts.setTypeface(Shy7lo.RalewayRegular);
            tvCheckout.setTypeface(Shy7lo.RalewayBold);
            tvContinueShopping.setTypeface(Shy7lo.RalewayBold);

            tvTitle.setScaleX(1f);
            mTopLayout.setScaleX(1f);
            rltSubTotal.setScaleX(1f);
//            lvCartProducts.setScaleX(1f);
            tvSubTotal.setScaleX(1f);
            tvSubPrice.setScaleX(1f);
            tvTotalProducts.setScaleX(1f);
            mFooterView.setScaleX(1f);
        }

        // Offline cart
//        getShoppingListFromDB();
//        showTotalAmount();
//        if (Utils.isInternetConnected(getActivity()) && dbAdapter.isSyncAvailable()) {
//            showSyncIndicator();
//        } else {
//            hideSyncIndicator();
//        }

//        mShoppingBagSync = new ShoppingBagSync(getActivity());
//        mShoppingBagSync.syncShoppingBagList(getActivity(), dbAdapter);
    }

    //Offlineshoping Bag
//    public void getShoppingListFromDB() {
//
//        if (mTuneEventItemsList != null && mTuneEventItemsList.size() > 0) {
//            mTuneEventItemsList.clear();
//        }
//
//        carItemList = dbAdapter.getAllShoppingBagList();
//
//        if (carItemList != null && carItemList.size() > 0) {
//
//            try {
//
//                for (int i = 0; i < carItemList.size(); i++) {
//                    ShoppingBag.Item cartItem = carItemList.get(i);
//
//                    String sku = (!TextUtils.isEmpty(cartItem.getParent_sku()) ? cartItem.getParent_sku() : cartItem.getSku());
//                    if (dbAdapter.isContainInWishListWithoutSoftDelete(sku)) {
//                        cartItem.setWishListItem(true);
//                    } else {
//                        cartItem.setWishListItem(false);
//                    }
//
//                    TuneEventItem mTuneEventItem = new TuneEventItem("" + cartItem.getName());
//                    mTuneEventItem.quantity = cartItem.getQty();
//                    mTuneEventItem.itemname = cartItem.getName();
//                    mTuneEventItem.revenue = 0;
//
//                    boolean isSpecialPrice = false;
//                    if (cartItem.getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(cartItem.getSpecial_price()))) {
//                        if (!TextUtils.isEmpty(cartItem.getSpecial_from_date()) && !TextUtils.isEmpty(cartItem.getSpecial_to_date())) {
//                            Date fromDate = sdfDate.parse(cartItem.getSpecial_from_date());
//                            Date toDate = sdfDate.parse(cartItem.getSpecial_to_date());
//                            Date currentDate = new Date();
//
//                            if (currentDate.after(fromDate) && currentDate.before(toDate)) {
//                                isSpecialPrice = true;
//                            }
//                        }
//                    }
//                    if (isSpecialPrice) {
//                        mTuneEventItem.unitPrice = Utils.getAnswerPrice(mExchangeRate, cartItem.getSpecial_price());
//                    } else {
//                        mTuneEventItem.unitPrice = Utils.getAnswerPrice(mExchangeRate, cartItem.getPrice());
//                    }
//                    LogUtils.e("", "Price in Tunes::" + mTuneEventItem.unitPrice);
//
//                    mTuneEventItem.attribute1 = "" + (!TextUtils.isEmpty(cartItem.getParent_sku()) ? cartItem.getParent_sku() : cartItem.getSku());
//                    mTuneEventItem.attribute2 = "" + (cartItem.getProductType().equalsIgnoreCase("configurable") ? "Configurable" : "Simple");
//
//                    mTuneEventItemsList.add(mTuneEventItem);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            getTotalAmount();
//
//            mTotalItem = 0;
//
//            float mTotalPrice = 0f;
//
//            lnrEmpty.setVisibility(View.GONE);
//            lnrCart.setVisibility(View.VISIBLE);
//
//            if (cartItemsAdapter == null) {
//
//                cartItemsAdapter = new CartItemsAdapter(getActivity(), dbAdapter, carItemList, ShoppingBagsFragment.this, new CartItemsAdapter.OnPlusMinusListener() {
//                    @Override
//                    public void onPlusClick(String sku, int position, int quantity) {
//
//                        getTotalAmount();
//
//                        float mTotalInnerPrice = 0;
//                        int mTotalInnerItem = 0;
//
//                        for (int i = 0; i < carItemList.size(); i++) {
//
//                            if (carItemList.get(i).getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(carItemList.get(i).getSpecial_price()))) {
//                                if (!TextUtils.isEmpty(carItemList.get(i).getSpecial_from_date()) && !TextUtils.isEmpty(carItemList.get(i).getSpecial_to_date())) {
//                                    try {
//                                        Date fromDate = sdfDate.parse(carItemList.get(i).getSpecial_from_date());
//                                        Date toDate = sdfDate.parse(carItemList.get(i).getSpecial_to_date());
//                                        Date currentDate = new Date();
//
//                                        if (currentDate.after(fromDate) && currentDate.before(toDate)) {
//                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getSpecial_price() * carItemList.get(i).getQty();
//                                        } else {
//                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                        mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                                    }
//
//                                } else {
//                                    mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                                }
//                            } else {
//                                mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                            }
////                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                            mTotalInnerItem = mTotalInnerItem + carItemList.get(i).getQty();
//
//                        }
//
//                        tvTotalProducts.setText(mTotalInnerItem + " " + getResources().getString(R.string.products));
//                        if (TextUtils.isEmpty(tvSubPrice.getText().toString())) {
//                            tvSubPrice.setText(Utils.getIntPrice(mExchangeRate, mTotalInnerPrice) + " " + mCurrencyCode);
//                            mTotalRate = mTotalInnerPrice;
//                        }
//
//                    }
//
//                    @Override
//                    public void onMinusClick(String sku, int position, int quantity) {
//
//                        getTotalAmount();
//
//                        float mTotalInnerPrice = 0;
//                        int mTotalInnerItem = 0;
//
//                        for (int i = 0; i < carItemList.size(); i++) {
//
//
//                            if (carItemList.get(i).getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(carItemList.get(i).getSpecial_price()))) {
//                                if (!TextUtils.isEmpty(carItemList.get(i).getSpecial_from_date()) && !TextUtils.isEmpty(carItemList.get(i).getSpecial_to_date())) {
//                                    try {
//                                        Date fromDate = sdfDate.parse(carItemList.get(i).getSpecial_from_date());
//                                        Date toDate = sdfDate.parse(carItemList.get(i).getSpecial_to_date());
//                                        Date currentDate = new Date();
//
//                                        if (currentDate.after(fromDate) && currentDate.before(toDate)) {
//                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getSpecial_price() * carItemList.get(i).getQty();
//                                        } else {
//                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                        mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                                    }
//
//                                } else {
//                                    mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                                }
//                            } else {
//                                mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                            }
////                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                            mTotalInnerItem = mTotalInnerItem + carItemList.get(i).getQty();
//
//                        }
//
//                        tvTotalProducts.setText(mTotalInnerItem + " " + getResources().getString(R.string.products));
//                        if (TextUtils.isEmpty(tvSubPrice.getText().toString())) {
//                            tvSubPrice.setText(Utils.getIntPrice(mExchangeRate, mTotalInnerPrice) + " " + mCurrencyCode);
//                            mTotalRate = mTotalInnerPrice;
//                        }
//
//                    }
//                });
//                lvCartProducts.setAdapter(cartItemsAdapter);
//            } else {
//                cartItemsAdapter.refill(carItemList);
//            }
//
//            for (int i = 0; i < carItemList.size(); i++) {
//
//                if (carItemList.get(i).getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(carItemList.get(i).getSpecial_price()))) {
//                    if (!TextUtils.isEmpty(carItemList.get(i).getSpecial_from_date()) && !TextUtils.isEmpty(carItemList.get(i).getSpecial_to_date())) {
//                        try {
//                            Date fromDate = sdfDate.parse(carItemList.get(i).getSpecial_from_date());
//                            Date toDate = sdfDate.parse(carItemList.get(i).getSpecial_to_date());
//                            Date currentDate = new Date();
//
//                            if (currentDate.after(fromDate) && currentDate.before(toDate)) {
//                                mTotalPrice = mTotalPrice + carItemList.get(i).getSpecial_price() * carItemList.get(i).getQty();
//                            } else {
//                                mTotalPrice = mTotalPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            mTotalPrice = mTotalPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                        }
//
//                    } else {
//                        mTotalPrice = mTotalPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                    }
//                } else {
//                    mTotalPrice = mTotalPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                }
//
//                mTotalItem = mTotalItem + carItemList.get(i).getQty();
//
//            }
//
//            tvTotalProducts.setText(mTotalItem + " " + getResources().getString(R.string.products));
//            LogUtils.e("", "tvSubPrice::" + tvSubPrice.getText().toString() + " " + tvSubPrice.getText().toString().length());
//            if (TextUtils.isEmpty(tvSubPrice.getText().toString().trim())) {
//                tvSubPrice.setText(Utils.getIntPrice(mExchangeRate, mTotalPrice) + " " + mCurrencyCode);
//                mTotalRate = mTotalPrice;
//            }
//
//
//            MyPref.setPref(getActivity(), MyPref.CART_ITEM_COUNT, carItemList.size());
//        } else {
//            lnrEmpty.setVisibility(View.VISIBLE);
//            lnrCart.setVisibility(View.GONE);
//
//            MyPref.setPref(getActivity(), MyPref.CART_ITEM_COUNT, 0);
//        }
//
//        if (getActivity() instanceof HomeActivity) {
//            HomeActivity activity = (HomeActivity) getActivity();
//            activity.updateBadgetCount();
//        }
//
////        if (mShoppingBagSync != null && mShoppingBagSync.isSyncFinished) {
////            hideSyncIndicator();
////        }
//
//    }


    private boolean isShoppingBagRunning = false;

    public void getShoppingBag() {

        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");

        if (TextUtils.isEmpty(userToken)) {
            if (TextUtils.isEmpty(guestToken)) {
                lnrEmpty.setVisibility(View.VISIBLE);
                lnrCart.setVisibility(View.GONE);
                return;
            }
        }

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getShoppingBag();
                    }
                }
            });
            return;
        }

        isShoppingBagRunning = true;

        LogUtils.e("", "getShoppingBag call");

        final boolean isProgressShowing = Utils.isProgressDialogShowing();
        if (!isProgressShowing) {
            Utils.showProgressDialog(getActivity());
        }

//        if (mTuneEventItemsList != null && mTuneEventItemsList.size() > 0) {
//            mTuneEventItemsList.clear();
//        }


        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        Call<ShoppingBag> call;
        if (!TextUtils.isEmpty(userToken)) {

            Map<String, Object> jsonParams = new ArrayMap<>();
            jsonParams.put("cart_id", guestToken);
//            jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));

//            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

            call = apiService.getUserCartList(Shy7lo.mLangCode, "Bearer " + userToken);
        } else {
            call = apiService.getGuestCartList(Shy7lo.mLangCode, guestToken);
        }


        call.enqueue(new Callback<ShoppingBag>() {
            @Override
            public void onResponse(Call<ShoppingBag> call, Response<ShoppingBag> response) {

                LogUtils.e(TAG_SHOPPING_BAGS_FRAGMENT, "response code:" + response.code());
//                Utils.closeProgressDialog();
                isShoppingBagRunning = false;

                if (response.isSuccessful()) {
                    try {


                        ShoppingBag shoppingBag = response.body();
                        if (shoppingBag != null && shoppingBag.success.equals("1")) {
                            carItemList = shoppingBag.data.getItems();
                            LogUtils.e("", carItemList.size() + " carItemList::" + carItemList);

                            if (!TextUtils.isEmpty(shoppingBag.data.getCartPromotionMsg())) {
                                Picasso.with(getActivity()).load(shoppingBag.data.getCartPromotionMsg()).into(ivPromotionalMsg);
                                ivPromotionalMsg.setVisibility(View.VISIBLE);
                            } else {
                                ivPromotionalMsg.setVisibility(View.GONE);
                            }

//                            tvTotalProducts.setText(shoppingBag.data.getItemsCount() + " " + getResources().getString(R.string.products));

                            MyPref.setPref(getActivity(), MyPref.CART_ITEM_COUNT, carItemList.size());

                            if (getActivity() instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) getActivity();
                                activity.updateBadgetCount();
                            }


                            if (carItemList != null && carItemList.size() > 0) {

                                try {

                                    for (int i = 0; i < carItemList.size(); i++) {
                                        ShoppingBag.Item cartItem = carItemList.get(i);

                                        String sku = (!TextUtils.isEmpty(cartItem.getParent_sku()) ? cartItem.getParent_sku() : cartItem.getSku());
                                        if (dbAdapter.isContainInWishListWithoutSoftDelete(sku)) {
                                            cartItem.setWishListItem(true);
                                        } else {
                                            cartItem.setWishListItem(false);
                                        }

                                        TuneEventItem mTuneEventItem = new TuneEventItem("" + cartItem.getName());
                                        mTuneEventItem.quantity = cartItem.getQty();
                                        mTuneEventItem.itemname = cartItem.getName();
                                        mTuneEventItem.revenue = 0;

                                        boolean isSpecialPrice = false;
                                        if (cartItem.getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(cartItem.getSpecial_price()))) {
                                            if (!TextUtils.isEmpty(cartItem.getSpecial_from_date()) && !TextUtils.isEmpty(cartItem.getSpecial_to_date())) {
                                                Date fromDate = sdfDate.parse(cartItem.getSpecial_from_date());
                                                Date toDate = sdfDate.parse(cartItem.getSpecial_to_date());
                                                Date currentDate = new Date();

                                                if (currentDate.after(fromDate) && currentDate.before(toDate)) {
                                                    isSpecialPrice = true;
                                                }
                                            }
                                        }
                                        if (isSpecialPrice) {
                                            mTuneEventItem.unitPrice = Utils.getAnswerPrice(mExchangeRate, cartItem.getSpecial_price());
                                        } else {
                                            mTuneEventItem.unitPrice = Utils.getAnswerPrice(mExchangeRate, cartItem.getPrice());
                                        }
                                        LogUtils.e("", "Price in Tunes::" + mTuneEventItem.unitPrice);

                                        mTuneEventItem.attribute1 = "" + (!TextUtils.isEmpty(cartItem.getParent_sku()) ? cartItem.getParent_sku() : cartItem.getSku());
                                        mTuneEventItem.attribute2 = "" + (cartItem.getProductType().equalsIgnoreCase("configurable") ? "Configurable" : "Simple");

                                        mTuneEventItemsList.add(mTuneEventItem);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                getTotalAmount();

                                mTotalItem = 0;

                                float mTotalPrice = 0f;

                                lnrEmpty.setVisibility(View.GONE);
                                lnrCart.setVisibility(View.VISIBLE);

                                CartItemsAdapter cartItemsAdapter = new CartItemsAdapter(getActivity(), dbAdapter, carItemList, ShoppingBagsFragment.this, new CartItemsAdapter.OnPlusMinusListener() {
                                    @Override
                                    public void onPlusClick(String sku, int position, int quantity) {

                                        getTotalAmount();

                                        float mTotalInnerPrice = 0;
                                        int mTotalInnerItem = 0;

                                        for (int i = 0; i < carItemList.size(); i++) {

                                            if (carItemList.get(i).getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(carItemList.get(i).getSpecial_price()))) {
                                                if (!TextUtils.isEmpty(carItemList.get(i).getSpecial_from_date()) && !TextUtils.isEmpty(carItemList.get(i).getSpecial_to_date())) {
                                                    try {
                                                        Date fromDate = sdfDate.parse(carItemList.get(i).getSpecial_from_date());
                                                        Date toDate = sdfDate.parse(carItemList.get(i).getSpecial_to_date());
                                                        Date currentDate = new Date();

                                                        if (currentDate.after(fromDate) && currentDate.before(toDate)) {
                                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getSpecial_price() * carItemList.get(i).getQty();
                                                        } else {
                                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                                    }

                                                } else {
                                                    mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                                }
                                            } else {
                                                mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                            }
//                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                            mTotalInnerItem = mTotalInnerItem + carItemList.get(i).getQty();

                                        }

                                        tvTotalProducts.setText(getResources().getString(R.string.item) + " " + mTotalInnerItem);
                                        if (TextUtils.isEmpty(tvSubPrice.getText().toString())) {
//                                            tvSubPrice.setText(Utils.getIntPrice(mExchangeRate, mTotalInnerPrice) + " " + mCurrencyCode);
                                            mTotalRate = mTotalInnerPrice;
                                        }

                                    }

                                    @Override
                                    public void onMinusClick(String sku, int position, int quantity) {

                                        getTotalAmount();

                                        float mTotalInnerPrice = 0;
                                        int mTotalInnerItem = 0;

                                        for (int i = 0; i < carItemList.size(); i++) {


                                            if (carItemList.get(i).getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(carItemList.get(i).getSpecial_price()))) {
                                                if (!TextUtils.isEmpty(carItemList.get(i).getSpecial_from_date()) && !TextUtils.isEmpty(carItemList.get(i).getSpecial_to_date())) {
                                                    try {
                                                        Date fromDate = sdfDate.parse(carItemList.get(i).getSpecial_from_date());
                                                        Date toDate = sdfDate.parse(carItemList.get(i).getSpecial_to_date());
                                                        Date currentDate = new Date();

                                                        if (currentDate.after(fromDate) && currentDate.before(toDate)) {
                                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getSpecial_price() * carItemList.get(i).getQty();
                                                        } else {
                                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                                    }

                                                } else {
                                                    mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                                }
                                            } else {
                                                mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                            }
//                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                            mTotalInnerItem = mTotalInnerItem + carItemList.get(i).getQty();

                                        }

                                        tvTotalProducts.setText(getResources().getString(R.string.item) + " " + mTotalInnerItem);
                                        if (TextUtils.isEmpty(tvSubPrice.getText().toString())) {
//                                            tvSubPrice.setText(Utils.getIntPrice(mExchangeRate, mTotalInnerPrice) + " " + mCurrencyCode);
                                            mTotalRate = mTotalInnerPrice;
                                        }

                                    }

                                    @Override
                                    public void onQtyClick(String sku, int position, int quantity) {

                                        getTotalAmount();

                                        float mTotalInnerPrice = 0;
                                        int mTotalInnerItem = 0;

                                        for (int i = 0; i < carItemList.size(); i++) {


                                            if (carItemList.get(i).getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(carItemList.get(i).getSpecial_price()))) {
                                                if (!TextUtils.isEmpty(carItemList.get(i).getSpecial_from_date()) && !TextUtils.isEmpty(carItemList.get(i).getSpecial_to_date())) {
                                                    try {
                                                        Date fromDate = sdfDate.parse(carItemList.get(i).getSpecial_from_date());
                                                        Date toDate = sdfDate.parse(carItemList.get(i).getSpecial_to_date());
                                                        Date currentDate = new Date();

                                                        if (currentDate.after(fromDate) && currentDate.before(toDate)) {
                                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getSpecial_price() * carItemList.get(i).getQty();
                                                        } else {
                                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                                    }

                                                } else {
                                                    mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                                }
                                            } else {
                                                mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                            }
//                                            mTotalInnerPrice = mTotalInnerPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                            mTotalInnerItem = mTotalInnerItem + carItemList.get(i).getQty();

                                        }

                                        tvTotalProducts.setText(getResources().getString(R.string.item) + " " + mTotalInnerItem);
                                        if (TextUtils.isEmpty(tvSubPrice.getText().toString())) {
//                                            tvSubPrice.setText(Utils.getIntPrice(mExchangeRate, mTotalInnerPrice) + " " + mCurrencyCode);
                                            mTotalRate = mTotalInnerPrice;
                                        }

                                    }
                                });
                                lvCartProducts.setAdapter(cartItemsAdapter);

                                for (int i = 0; i < carItemList.size(); i++) {

//                                    if (carItemList.get(i).getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(carItemList.get(i).getSpecial_price()))) {
//                                        mTotalPrice = mTotalPrice + carItemList.get(i).getSpecial_price() * carItemList.get(i).getQty();
//                                    } else {
//                                        mTotalPrice = mTotalPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
//                                    }

                                    if (carItemList.get(i).getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(carItemList.get(i).getSpecial_price()))) {
                                        if (!TextUtils.isEmpty(carItemList.get(i).getSpecial_from_date()) && !TextUtils.isEmpty(carItemList.get(i).getSpecial_to_date())) {
                                            try {
                                                Date fromDate = sdfDate.parse(carItemList.get(i).getSpecial_from_date());
                                                Date toDate = sdfDate.parse(carItemList.get(i).getSpecial_to_date());
                                                Date currentDate = new Date();

                                                if (currentDate.after(fromDate) && currentDate.before(toDate)) {
                                                    mTotalPrice = mTotalPrice + carItemList.get(i).getSpecial_price() * carItemList.get(i).getQty();
                                                } else {
                                                    mTotalPrice = mTotalPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                mTotalPrice = mTotalPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                            }

                                        } else {
                                            mTotalPrice = mTotalPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                        }
                                    } else {
                                        mTotalPrice = mTotalPrice + carItemList.get(i).getPrice() * carItemList.get(i).getQty();
                                    }

                                    mTotalItem = mTotalItem + carItemList.get(i).getQty();

//                                    String sku = carItemList.get(i).getSku();
//                                    ApiInterface apiService =
//                                            RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//                                    Call<JsonElement> callImage = apiService.getCartItemImage(Shy7lo.mLangCode, sku);
//                                    final int finalI = i;
//
//                                    callImage.enqueue(new Callback<JsonElement>() {
//                                        @Override
//                                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                                            if (response.isSuccessful()) {
//                                                JSONArray jsonArray = null;
//                                                try {
//                                                    jsonArray = new JSONArray(response.body().toString());
//
//                                                    if (jsonArray != null && jsonArray.length() > 0) {
//                                                        JSONObject jObj = jsonArray.getJSONObject(0);
//                                                        if (jObj != null) {
//                                                            String imagefile = jObj.getString("file");
//                                                            carItemList.get(finalI).setImageFIle(imagefile);
//
//                                                            float mTotalPrice = 0f;
//
//                                                            if (carItemList.size() == mTotalItem) {
//
//                                                                lnrEmpty.setVisibility(View.GONE);
//                                                                lnrCart.setVisibility(View.VISIBLE);
//
//                                                                CartItemsAdapter cartItemsAdapter = new CartItemsAdapter(getActivity(), carItemList, ShoppingBagsFragment.this);
//                                                                lvCartProducts.setAdapter(cartItemsAdapter);
//
//                                                                for (int j = 0; j < carItemList.size(); j++) {
//                                                                    mTotalPrice = mTotalPrice + carItemList.get(j).getPrice() * carItemList.get(j).getQty();
//                                                                }
//                                                            }
//
//                                                            tvSubPrice.setText(Utils.getRealPrice(mExchangeRate, mTotalPrice) + " " + mCurrencyCode);
//                                                        }
//                                                    }
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<JsonElement> call, Throwable t) {
//
//                                        }
//                                    });
                                }

                                tvTotalProducts.setText(getResources().getString(R.string.item) + " " + mTotalItem);
                                LogUtils.e("", "tvSubPrice::" + tvSubPrice.getText().toString() + " " + tvSubPrice.getText().toString().length());
                                if (TextUtils.isEmpty(tvSubPrice.getText().toString().trim())) {
//                                    tvSubPrice.setText(Utils.getIntPrice(mExchangeRate, mTotalPrice) + " " + mCurrencyCode);
                                    mTotalRate = mTotalPrice;
                                }

                                Utils.closeProgressDialog();
                            } else {
                                Utils.closeProgressDialog();
                                lnrEmpty.setVisibility(View.VISIBLE);
                                lnrCart.setVisibility(View.GONE);
                            }

                        } else if (shoppingBag != null && shoppingBag.success.equals("0")) {
                            Utils.closeProgressDialog();
                            Utils.showToast(getActivity(), "" + shoppingBag.message);
                        } else if (shoppingBag != null && shoppingBag.success.equals("2")) {
                            Utils.closeProgressDialog();
                            Utils.showInitialScreen(getActivity());
                            return;
                        } else {
                            Utils.closeProgressDialog();
//                            Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
                            lnrEmpty.setVisibility(View.VISIBLE);
                            lnrCart.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.closeProgressDialog();
                    }

                } else {
                    Utils.closeProgressDialog();
//                    Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
                    lnrEmpty.setVisibility(View.VISIBLE);
                    lnrCart.setVisibility(View.GONE);
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
            public void onFailure(Call<ShoppingBag> call, Throwable t) {
                System.out.println("abc:" + t.getMessage());
                Utils.closeProgressDialog();
//                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
                lnrEmpty.setVisibility(View.VISIBLE);
                lnrCart.setVisibility(View.GONE);
//                Utils.showAlertDialog(getActivity());
            }
        });
    }

    public void showEmptyScreen() {
        lnrEmpty.setVisibility(View.VISIBLE);
        lnrCart.setVisibility(View.GONE);
    }

    private void InitializeControlsAction() {

        ibMore.setOnClickListener(this);
        btnGoToProducts.setOnClickListener(this);
        tvCheckout.setOnClickListener(this);
        tvContinueShopping.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == ibMore) {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.openDrawer();
            }
        } else if (view == btnGoToProducts) {

            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.showContinueShopping();
            }
        } else if (view == tvContinueShopping) {

            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.showContinueShopping();
            }

        } else if (view == tvCheckout) {

//            if (mTotalItem > 0) {

//            ShoppingBagSync mShoppingBagSync = new ShoppingBagSync(getActivity());
//            mShoppingBagSync.syncShoppingBagList(getActivity(), dbAdapter);

            if (getActivity() instanceof HomeActivity) {

                String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");

                try {
                    LogUtils.e("", "Price::" + Utils.getAnswerPrice(mExchangeRate, mTotalRate));
                    if (!BuildConfig.DEBUG) {
                        Answers.getInstance().logStartCheckout(new StartCheckoutEvent()
                                .putTotalPrice(BigDecimal.valueOf(Utils.getAnswerPrice(mExchangeRate, mTotalRate)))
                                .putCurrency(Currency.getInstance("" + mCurrencyCode))
                                .putItemCount(MyPref.getPref(getActivity(), MyPref.CART_ITEM_COUNT, 0))
                                .putCustomAttribute("userType", "" + (TextUtils.isEmpty(userToken) ? "Guest" : "LoggedIn")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    AdjustEvent event = new AdjustEvent("myx39n");

                    List<CriteoProduct> products = new ArrayList<>();
                    for (int i = 0; i < carItemList.size(); i++) {

                        float mPrice = carItemList.get(i).getPrice();

                        if (carItemList.get(i).getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(carItemList.get(i).getSpecial_price()))) {

                            if (!TextUtils.isEmpty(carItemList.get(i).getSpecial_from_date()) && !TextUtils.isEmpty(carItemList.get(i).getSpecial_to_date())) {

                                Date fromDate = sdfDate.parse(carItemList.get(i).getSpecial_from_date());
                                Date toDate = sdfDate.parse(carItemList.get(i).getSpecial_to_date());
                                Date currentDate = new Date();

                                if (currentDate.after(fromDate) && currentDate.before(toDate)) {
                                    mPrice = carItemList.get(i).getSpecial_price();
                                }
                            }
                        }
                        CriteoProduct product = new CriteoProduct((float) Utils.getAnswerPrice(mExchangeRate, mPrice), carItemList.get(i).getQty(), "" + carItemList.get(i).getSku());
                        products.add(product);
                    }

                    event.addPartnerParameter("Currency Code", "" + "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR)));
                    event.addPartnerParameter("No Of Items", "" + "" + carItemList.size());

                    //callback
                    event.addCallbackParameter("Currency Code", "" + "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR)));
                    event.addCallbackParameter("No Of Items", "" + "" + carItemList.size());

                    String mCriteo = MyPref.getPref(getActivity(), MyPref.APP_CRITEO, "");
                    if (TextUtils.isEmpty(mCriteo) || mCriteo.equalsIgnoreCase("1")) {
                        AdjustCriteo.injectCartIntoEvent(event, products);
                        AdjustCriteo.injectCustomerIdIntoCriteoEvents(TextUtils.isEmpty(userToken) ? userToken : MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""));
                    }

                    Adjust.trackEvent(event);

                } catch (Exception e) {
                    e.printStackTrace();
                }


//                if (RestClient.isTuneEnable) {
//                    try {
//                        Tune tune = Tune.getInstance();
//                        tune.measureEvent(new TuneEvent(TuneEvent.CHECKOUT_INITIATED)
//                                .withEventItems(mTuneEventItemsList)
//                                .withCurrencyCode("" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")));
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }

                if (RestClient.isFacebookLive) {
                    try {
                        // FB Log Initiate Checkout
                        AppEventsLogger logger = AppEventsLogger.newLogger(getActivity());

                        Bundle parameters = new Bundle();
                        parameters.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR)));
//                        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "" + productDetails.getTypeId());
//                        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, "" + productDetails.getSku());
//                        parameters.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, productDetails.getName());
//                        parameters.putString(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, ""+mTuneEventItemsList.size());
                        parameters.putString(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, "" + carItemList.size());

                        logger.logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT, Utils.getAnswerPrice(mExchangeRate, mTotalRate), parameters);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!TextUtils.isEmpty(userToken)) {

                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.BNDL_USER_TYPE, Constant.BNDL_USER_TYPE_ME);
                    bundle.putString(Constant.BNDL_USER_EMAIL, MyPref.getPref(getActivity(), MyPref.USER_EMAIL, ""));

                    HomeActivity activity = (HomeActivity) getActivity();
                    activity.loadShipmentPay(bundle);
                } else {

                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.BNDL_USER_TYPE, Constant.BNDL_USER_TYPE_GUEST);
                    bundle.putString(Constant.BNDL_USER_EMAIL, "");

                    HomeActivity activity = (HomeActivity) getActivity();
                    activity.loadShipmentPay(bundle);

//                    HomeActivity activity = (HomeActivity) getActivity();
//                    activity.loadLogin();
                }
            }
//            }else{
//                Utils.showToast(getActivity(), ""+getResources().getString(R.string.shopping_bag_empty));
//            }

        }
    }

//    private void showTotalAmount() {
//
//        String prefCartTotal = MyPref.getPref(getActivity(), MyPref.TOTAL_AMT_LIST, "");
//        LogUtils.e("", "prefCartTotal::" + prefCartTotal);
//        if (!TextUtils.isEmpty(prefCartTotal)) {
//            CartTotalAmount mCartTotalAmount = new Gson().fromJson(prefCartTotal, CartTotalAmount.class);
//            if (mCartTotalAmount != null) {
//                LogUtils.e("", "mCartTotalAmount is not null");
//                List<CartTotalAmount.CartTotal> mCartTotalList = mCartTotalAmount.data;
//                LogUtils.e("", "mCartTotalList::" + mCartTotalList.size());
//                if (mCartTotalList != null && mCartTotalList.size() > 0) {
//
//                    if (lnrAmount.getChildCount() > 0) {
//                        lnrAmount.removeAllViews();
//                    }
//
//                    for (int i = 0; i < mCartTotalList.size(); i++) {
//                        CartTotalAmount.CartTotal mCartTotal = mCartTotalList.get(i);
//
//                        if (mCartTotal != null) {
//
//                            if (i == mCartTotalList.size() - 1) {
//                                tvSubPrice.setText(Utils.getIntPrice(mExchangeRate, Float.parseFloat(mCartTotal.value)) + " " + mCurrencyCode);
//                                mTotalRate = Float.parseFloat(mCartTotal.value);
//                                continue;
//                            }
//
//                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_amount, null);
//
//                            TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
//                            TextView tvAmount = (TextView) view.findViewById(R.id.tvAmount);
//
//                            tvTitle.setTextColor(getResources().getColor(R.color.black));
//                            tvAmount.setTextColor(getResources().getColor(R.color.black));
//
//                            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                                tvTitle.setScaleX(-1f);
//                                tvAmount.setScaleX(-1f);
//                                tvTitle.setGravity(Gravity.RIGHT);
//                            } else {
//                                tvTitle.setScaleX(1f);
//                                tvAmount.setScaleX(1f);
//                                tvTitle.setGravity(Gravity.LEFT);
//                            }
//
//                            tvTitle.setText("" + mCartTotal.title);
//
//                            if (mCartTotal.value != null && !TextUtils.isEmpty(mCartTotal.value) && !mCartTotal.value.equalsIgnoreCase("null")) {
//                                tvAmount.setText(Utils.getIntPrice(mExchangeRate, Float.parseFloat(mCartTotal.value)) + " " + mCurrencyCode);
//                            } else {
//                                tvAmount.setText("0 " + mCurrencyCode);
//                            }
//
//                            lnrAmount.addView(view);
//                        }
//                    }
//                }
//            }
//
//        }
//    }

//    public void getTotalAmount() {
//
//        Call<JsonElement> callCode;
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
//
//        if (!TextUtils.isEmpty(userToken)) {
//            callCode = apiService.getUserTotalAmount("en", "Bearer " + userToken);
//        } else {
//            String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
//            callCode = apiService.getGuestTotalAmount(Shy7lo.mLangCode, guestToken);
//        }
//
//        callCode.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                Utils.closeProgressDialog();
//
//                if (response.isSuccessful()) {
//                    try {
//
//                        JSONObject jsonResponse = new JSONObject(response.body().toString());
//                        if (jsonResponse != null && jsonResponse.getString("success").equals("1")) {
//                            CartTotalAmount mCartTotalAmount = new Gson().fromJson(jsonResponse.toString(), CartTotalAmount.class);
//                            MyPref.setPref(getActivity(), MyPref.TOTAL_AMT_LIST, "" + new Gson().toJson(mCartTotalAmount));
//                            showTotalAmount();
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
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//            }
//        });
//
//    }

//    public void showSyncIndicator() {
//        if (mSyncIndicator != null) {
//            mSyncIndicator.show();
//        }
//    }
//
//    public void hideSyncIndicator() {
//        if (mSyncIndicator != null) {
//            mSyncIndicator.hide();
//        }
//    }

    private void getTotalAmount() {

        final boolean isProgressShowing = Utils.isProgressDialogShowing();
        if (!isProgressShowing) {
            Utils.showProgressDialog(getActivity());
        }

        Call<JsonElement> callCode;
        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");

        if (!TextUtils.isEmpty(userToken)) {
            callCode = apiService.getUserTotalAmount(Shy7lo.mLangCode, "Bearer " + userToken);
        } else {
            String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
            callCode = apiService.getGuestTotalAmount(Shy7lo.mLangCode, guestToken);
        }

        callCode.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonResponse = new JSONObject(response.body().toString());
                        if (jsonResponse != null && jsonResponse.getString("success").equals("1")) {
                            JSONObject jsonObject = jsonResponse.getJSONObject("data");
                            JSONArray jsonArray = null;
                            if (jsonObject != null) {
                                jsonArray = jsonObject.getJSONArray("totals");
                            }
                            if (jsonArray != null && jsonArray.length() > 0) {

                                if (lnrAmount.getChildCount() > 0) {
                                    lnrAmount.removeAllViews();
                                }

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jObj = jsonArray.getJSONObject(i);

                                    if (jObj != null) {
                                        String title = jObj.getString("title");
                                        String value = jObj.getString("value");

//                                        if (title.contains("Total")) {
                                        if (i == jsonArray.length() - 1) {
//                                            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                                                tvTotal.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, Float.parseFloat(value)));
//                                            } else {
                                            tvSubPrice.setText(Utils.getIntPrice(mExchangeRate, Float.parseFloat(value)) + " " + mCurrencyCode);
                                            mTotalRate = Float.parseFloat(value);
//                                            }

//                                            mTotalAmount = Utils.getRealPrice(mExchangeRate, Float.parseFloat(value));
                                            continue;
                                        }

                                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_amount, null);

                                        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                                        TextView tvAmount = (TextView) view.findViewById(R.id.tvAmount);

                                        tvTitle.setTextColor(getResources().getColor(R.color.black));
                                        tvAmount.setTextColor(getResources().getColor(R.color.black));

                                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                                            tvTitle.setScaleX(-1f);
                                            tvAmount.setScaleX(-1f);
                                            tvTitle.setGravity(Gravity.RIGHT);
                                        } else {
                                            tvTitle.setScaleX(1f);
                                            tvAmount.setScaleX(1f);
                                            tvTitle.setGravity(Gravity.LEFT);
                                        }

                                        tvTitle.setText("" + title);

//                                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                                            if (value != null && !TextUtils.isEmpty(value)) {
//                                                tvAmount.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, Float.parseFloat(value)));
//                                            } else {
//                                                tvAmount.setText(mCurrencyCode + " 0");
//                                            }
//                                        } else {

                                        LogUtils.e("", title + " value::" + value);

                                        if (value != null && !TextUtils.isEmpty(value)) {
                                            LogUtils.e("", title + " value::" + value + " " + Utils.getRealPrice(mExchangeRate, Float.parseFloat(value)));
                                            tvAmount.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, Float.parseFloat(value)));
                                        } else {
                                            tvAmount.setText(mCurrencyCode + " 0");
                                        }
//                                        }


//                                            if (title.contains("Shipping")) {
//                                                tvTitle.setTextColor(getResources().getColor(R.color.btn_color));
//                                                tvAmount.setTextColor(getResources().getColor(R.color.btn_color));
//                                            }

                                        lnrAmount.addView(view);

//                                        if (title.equalsIgnoreCase("Subtotal")) {
//                                            tvSubTotal.setText(value + " SAR");
//                                        }
//
//                                        if (title.contains("Shipping")) {
//                                            tvShipping.setText(value + " SAR");
//                                        }
//

                                    }
                                }
                            }
                        } else if (jsonResponse != null && jsonResponse.getString("success").equals("0")) {

                        } else if (jsonResponse != null && jsonResponse.getString("success").equals("2")) {
                            Utils.showInitialScreen(getActivity());
                            return;
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
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e(TAG_SHOPPING_BAGS_FRAGMENT, "onResume");
        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.setFirebaseLog("My_cart");
        }

    }

}
