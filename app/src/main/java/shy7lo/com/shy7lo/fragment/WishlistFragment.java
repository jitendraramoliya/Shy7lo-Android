package shy7lo.com.shy7lo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.adapter.WishlistAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.model.Wishlist;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.sync.WishListSync;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by jiten on 2/9/16.
 */
//offlineWishlist
public class WishlistFragment extends Fragment implements View.OnClickListener {

    public static String TAG_WISHLIST_FRAGMENT = "WishlistFragment";

    public static int RC_WISHLIST = 1500;

    @BindView(R.id.mTopLayout)
    View mTopLayout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ibMore)
    ImageButton ibMore;
    //    @BindView(R.id.ibShoppingBags)
//    ImageButton ibShoppingBags;
    @BindView(R.id.lvWishList)
    ListView lvWishList;
    @BindView(R.id.lnrLogin)
    LinearLayout lnrLogin;
    @BindView(R.id.tvLoginText)
    TextView tvLoginText;
    @BindView(R.id.btnLoginNow)
    Button btnLoginNow;
    @BindView(R.id.lnrWishListEmpty)
    LinearLayout lnrWishListEmpty;
    @BindView(R.id.lnrWishList)
    LinearLayout lnrWishList;
    @BindView(R.id.lnrShareWishlist)
    LinearLayout lnrShareWishlist;
    @BindView(R.id.lnrEmpty)
    LinearLayout lnrEmpty;
    @BindView(R.id.tvShareYour)
    TextView tvShareYour;
    @BindView(R.id.tvWishList)
    TextView tvWishList;
    @BindView(R.id.tvWishlistEmptyText)
    TextView tvWishlistEmptyText;
    @BindView(R.id.btnGoToProducts)
    Button btnGoToProducts;
    @BindView(R.id.btnAddAllToCart)
    Button btnAddAllToCart;

    WishlistAdapter mWishlistAdapter;

    List<Wishlist.WishlistData> mWishlistDatas = new ArrayList<>();

    private float mExchangeRate;

    DBAdapter dbAdapter;

    View mView;

    private static WishlistFragment wishlistFragment;

    public static WishlistFragment getInstance() {
//        if (wishlistFragment == null) {
        wishlistFragment = new WishlistFragment();
//        }
        return wishlistFragment;
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

    //    public BadgeView itemBadgeView;
    private int mCartItemCount;
    private int fiveDp, threeDp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_wishlist, container, false);
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

        fiveDp = (int) Utils.pxFromDp(getActivity(), 5);
        threeDp = (int) Utils.pxFromDp(getActivity(), 2);

        mExchangeRate = MyPref.getPref(getActivity(), MyPref.DEFAULT_EXCHANGE_RATE, 1f);

//        itemBadgeView = new BadgeView(getActivity(), ibShoppingBags, 11);
//        itemBadgeView.setBadgeMargin(threeDp, threeDp);
//
//        mCartItemCount = MyPref.getPref(getActivity(), MyPref.CART_ITEM_COUNT, 0);
////        mCartItemCount = 5;
//        itemBadgeView.setText("" + mCartItemCount);
//
////        if (mCartItemCount > 0) {
////            itemBadgeView.show();
////        } else {
//        itemBadgeView.hide();
////        }
//
//        ibShoppingBags.setVisibility(View.GONE);

        tvTitle.setText(getResources().getString(R.string.wishlist));
        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            tvTitle.setBackgroundResource(R.drawable.ic_top_logo_ar);
            tvTitle.setScaleX(-1f);
//            itemBadgeView.setScaleX(-1f);
            mTopLayout.setScaleX(-1f);
            lnrShareWishlist.setScaleX(-1f);
            tvShareYour.setScaleX(-1f);
            tvWishList.setScaleX(-1f);
//            lvWishList.setScaleX(-1f);

            tvShareYour.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_share_gray, 0);
            tvTitle.setTypeface(Shy7lo.DroidKufiBold);
        } else {
//            tvTitle.setBackgroundResource(R.drawable.ic_top_logo);
            tvTitle.setScaleX(1f);
//            itemBadgeView.setScaleX(1f);
            mTopLayout.setScaleX(1f);
            lnrShareWishlist.setScaleX(1f);
            tvShareYour.setScaleX(1f);
            tvWishList.setScaleX(1f);
//            lvWishList.setScaleX(1f);
            tvShareYour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share_gray, 0, 0, 0);

            tvTitle.setTypeface(Shy7lo.RalewayBold);
        }

        getWishListFromDB();

        // for offline uncoment this
        WishListSync mWishListSync = new WishListSync(getActivity());
        mWishListSync.syncWishList(getActivity(), dbAdapter);

//        final String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
//        if (!TextUtils.isEmpty(userToken)) {
//            showWishList();
//        }

    }

    public void getWishListFromDB() {

        if (!isAdded()) {
            return;
        }

        if (dbAdapter == null) {
            dbAdapter = new DBAdapter(getActivity());
        }
        mWishlistDatas = dbAdapter.getAllWishList();

        final String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        LogUtils.e("", "userToken::" + userToken);
        if (TextUtils.isEmpty(userToken)) {
            lnrLogin.setVisibility(View.VISIBLE);
            lnrWishListEmpty.setVisibility(View.GONE);
            lnrWishList.setVisibility(View.GONE);
        } else {
            lnrLogin.setVisibility(View.GONE);
            if (mWishlistDatas != null && mWishlistDatas.size() > 0) {
                lnrWishListEmpty.setVisibility(View.GONE);
                lnrWishList.setVisibility(View.VISIBLE);
                setView();
            } else {

                lnrWishListEmpty.setVisibility(View.VISIBLE);
//            lvWishList.setVisibility(View.GONE);
                lnrWishList.setVisibility(View.GONE);
            }
        }


        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.updateWishListBadgetCount();
        }
    }

    private void InitializeControlsAction() {
        ibMore.setOnClickListener(this);
//        ibShoppingBags.setOnClickListener(this);
        btnLoginNow.setOnClickListener(this);
        btnGoToProducts.setOnClickListener(this);
        btnAddAllToCart.setOnClickListener(this);
    }

    public void showWishList() {

        final String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");

        if (!Utils.isInternetConnected(getMyActivity()) || TextUtils.isEmpty(userToken)) {
            return;
        }

        lnrEmpty.setVisibility(View.VISIBLE);

        Utils.showProgressDialog(getActivity());

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, false);
        Call<ResponseBody> call = apiService.getWishlist(Shy7lo.mLangCode, "Bearer " + userToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                LogUtils.e("", "code::" + response.code());
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {


                    try {
                        Wishlist wishlist = new Gson().fromJson(response.body().string(), Wishlist.class);

                        LogUtils.e("", "wishlist success::" + wishlist.success.equals("1"));
                        if (wishlist != null && wishlist.success.equals("1")) {

                            List<Wishlist.WishlistData> mWishlistDatas = wishlist.data.getWishlistData();
                            if (mWishlistDatas != null && mWishlistDatas.size() > 0) {
                                for (int i = 0; i < mWishlistDatas.size(); i++) {
                                    mWishlistDatas.get(i).setIsGuest("0");
                                    mWishlistDatas.get(i).setToken(userToken);
                                    if (dbAdapter.isContainInWishList(mWishlistDatas.get(i).getSku())) {
                                        dbAdapter.updateWishItem(mWishlistDatas.get(i));
                                    } else {
                                        mWishlistDatas.get(i).setIs_on_server("1");
                                        dbAdapter.addWishItem(mWishlistDatas.get(i));
                                    }

                                }
                                getWishListFromDB();
                            }

                        } else if (wishlist != null && wishlist.success.equals("2")) {

                        }
                    } catch (IOException e) {
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

                lnrEmpty.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.closeProgressDialog();
                lnrEmpty.setVisibility(View.GONE);
                LogUtils.e("", "onFailure call::");
            }
        });

    }

//    public void showWishList() {
//
//        if (!Utils.isInternetConnected(getMyActivity())) {
//            Utils.showOfflineMsgDialog(getMyActivity(), new Utils.OnOfflineRetryDialogClick() {
//                @Override
//                public void onRetryClicked(Dialog dialog) {
//                    if (Utils.isInternetConnected(getMyActivity())) {
//                        if (dialog != null && dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        showWishList();
//                    }
//                }
//            });
//            return;
//        }
//
//        final boolean isProgressShowing = Utils.isProgressDialogShowing();
//        if (!isProgressShowing) {
//            Utils.showProgressDialog(getActivity());
//        }
//
//        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, false);
//        Call<ResponseBody> call = apiService.getWishlist(Shy7lo.mLangCode, "Bearer " + userToken);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                LogUtils.e("", "code::" + response.code());
//
//                if (response.isSuccessful()) {
//
//
//                    try {
//                        Wishlist wishlist = new Gson().fromJson(response.body().string(), Wishlist.class);
//
//
////                    Wishlist wishlist = response.body();
//                        LogUtils.e("", "success::" + wishlist.success.equals("1"));
//                        if (wishlist != null && wishlist.success.equals("1")) {
//
//                            mWishlistDatas = wishlist.data.getWishlistData();
//                            if (mWishlistDatas != null && mWishlistDatas.size() > 0) {
//                                setSignoutView();
//                            } else {
//                                lnrLogin.setVisibility(View.GONE);
//                                lnrWishListEmpty.setVisibility(View.VISIBLE);
//                                lvWishList.setVisibility(View.GONE);
//                            }
//
//                        } else if (wishlist != null && wishlist.success.equals("0")) {
//                            lnrLogin.setVisibility(View.GONE);
//                            lnrWishListEmpty.setVisibility(View.VISIBLE);
//                            lvWishList.setVisibility(View.GONE);
////                            Utils.showToast(getActivity(), "" + wishlist.message);
//                        } else if (wishlist != null && wishlist.success.equals("2")) {
//                            Utils.showInitialScreen(getActivity());
//                            return;
//                        } else {
//                            Utils.showToast(getMyActivity(), "" + getResources().getString(R.string.msg_something_wrong));
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Utils.showAlertDialog(getMyActivity());
//                }
//
//                Utils.closeProgressDialog();
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                LogUtils.e("", "onFailure call::");
////                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
//                Utils.closeProgressDialog();
//                Utils.showAlertDialog(getMyActivity());
//
//            }
//        });
//
//    }

    private void setView() {
        if (mWishlistAdapter == null) {
            mWishlistAdapter = new WishlistAdapter(getActivity(), mWishlistDatas, dbAdapter, this);
            lvWishList.setAdapter(mWishlistAdapter);
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWishlistAdapter.refill(mWishlistDatas);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view == ibMore) {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.openDrawer();
            }
        }
//        else if (view == ibShoppingBags) {
//            if (getActivity() instanceof HomeActivity) {
//                HomeActivity activity = (HomeActivity) getActivity();
//                activity.loadShoppingBags();
//            }
//        }
        else if (view == btnGoToProducts) {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.loadProducts();
            }
        } else if (view == btnAddAllToCart) {
//            addAllItemToCart();
        } else if (view == btnLoginNow) {


            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.loadMyShy7lo();
            }

//            Bundle bundle = new Bundle();
//            bundle.putString(SignInUpActivity.BNDL_VIEW_TYPE, "SignIn");
//            bundle.putString(SignInUpActivity.BNDL_SCREEN_FROM, "WishlistFragment");
//            IntentHandler.startActivityForResult(getActivity(), SignInUpActivity.class, bundle, RC_WISHLIST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LogUtils.e("", " wish list onActivityResult::" + requestCode);

        if (requestCode == RC_WISHLIST && resultCode == Activity.RESULT_OK) {

            String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");

            if (!TextUtils.isEmpty(userToken)) {

                lnrLogin.setVisibility(View.GONE);
                lnrWishListEmpty.setVisibility(View.GONE);
//                lvWishList.setVisibility(View.VISIBLE);
                lnrWishList.setVisibility(View.VISIBLE);

                showWishList();
            }
        }
    }

//    private void addAllItemToCart() {
//
//        if (!Utils.isInternetConnected(getActivity())) {
//            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
//                @Override
//                public void onRetryClicked(Dialog dialog) {
//                    if (Utils.isInternetConnected(getActivity())) {
//                        if (dialog != null && dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        addAllItemToCart();
//                    }
//                }
//            });
//            return;
//        }
//
//        if (mWishlistDatas != null && mWishlistDatas.size() > 0) {
//
//            Utils.showProgressDialog(getActivity());
//
//            for (int i = 0; i < mWishlistDatas.size(); i++) {
//
//                final Wishlist.WishlistData mWishlistData =  mWishlistDatas.get(i);
//
//                if (TextUtils.isEmpty(mWishlistData.getSize())){
//                    continue;
//                }
//
//                    try {
//
//                        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
//
//                        JSONObject jsonParams = new JSONObject();
//
//                        SharedPreferences pref = getActivity().getSharedPreferences(NotificationUtils.SHARED_PREF, 0);
//
//                        if (!TextUtils.isEmpty(userToken)) {
//
//                            jsonParams.put("sku", "" + mWishlistData.getSku());
//                            jsonParams.put("qty", "1");
//                            jsonParams.put("product_name", "" + mWishlistData.getName());
//                            jsonParams.put("price", "" + mWishlistData.getPrice());
//                            jsonParams.put("product_type", mWishlistData.getTypeId());
//                            jsonParams.put("device_token", "" + pref.getString("regId", ""));
////                jsonParams.put("device_token", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
//                            jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));
//
//
//                            if (mWishlistData.getTypeId().equalsIgnoreCase("configurable")) {
//                                JSONArray jsonArray = new JSONArray();
//                                JSONObject jsonObject = new JSONObject();
////                                jsonObject.put("option_id", mOptionId);
////                                jsonObject.put("option_value", mOptionValue);
//                                jsonArray.put(jsonObject);
//                                jsonParams.put("configurable_item_options", jsonArray);
//                            }
//
//                        } else {
//
//                            String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
//
////                jsonParams.put("sku", "" + testSku);
//                            jsonParams.put("sku", "" + mWishlistData.getSku());
//                            jsonParams.put("qty", "1");
//                            jsonParams.put("product_name", "" + mWishlistData.getName());
//                            jsonParams.put("price", "" + mWishlistData.getPrice());
//                            jsonParams.put("product_type", mWishlistData.getTypeId());
//                            jsonParams.put("cart_id", "" + guestToken);
//                            jsonParams.put("device_token", "" + pref.getString("regId", ""));
////                jsonParams.put("device_token", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
//                            jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));
//
//                            if (mWishlistData.getTypeId().equalsIgnoreCase("configurable")) {
//                                JSONArray jsonArray = new JSONArray();
//                                JSONObject jsonObject = new JSONObject();
////                                jsonObject.put("option_id", mOptionId);
////                                jsonObject.put("option_value", mOptionValue);
//                                jsonArray.put(jsonObject);
//                                jsonParams.put("configurable_item_options", jsonArray);
//                            }
//                        }
//
//                        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
//
//                        ApiInterface apiService =
//                                RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//
//                        Call<ResponseBody> call;
//                        if (!TextUtils.isEmpty(userToken)) {
//                            if (mWishlistData.getTypeId().equalsIgnoreCase("configurable")) {
//                                call = apiService.addUserProductConfigInCart(Shy7lo.mLangCode, "Bearer " + userToken, body);
//                            } else {
//                                call = apiService.addUserProductInCart(Shy7lo.mLangCode, "Bearer " + userToken, body);
//                            }
//                        } else {
//                            if (mWishlistData.getTypeId().equalsIgnoreCase("configurable")) {
//                                call = apiService.addGuestProductConfigInCart(Shy7lo.mLangCode, body);
//                            } else {
//                                call = apiService.addGuestProductInCart(Shy7lo.mLangCode, body);
//                            }
//                        }
//
//                        call.enqueue(new Callback<ResponseBody>() {
//                            @Override
//                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                                LogUtils.e(TAG_WISHLIST_FRAGMENT, "response code:" + response.code());
//                                Utils.closeProgressDialog();
//                                if (response.isSuccessful()) {
//
//                                    int mCartItemCount = MyPref.getPref(getActivity(), MyPref.CART_ITEM_COUNT, 0);
//                                    mCartItemCount = mCartItemCount + 1;
//                                    MyPref.setPref(getActivity(), MyPref.CART_ITEM_COUNT, mCartItemCount);
//
//                                    if (getActivity() instanceof HomeActivity) {
//                                        HomeActivity activity = (HomeActivity) getActivity();
//                                        activity.updateBadgetCount();
//                                    }
//
//                                    try {
//                                        Answers.getInstance().logAddToCart(new AddToCartEvent()
//                                                .putItemPrice(BigDecimal.valueOf(Utils.getAnswerPrice(mExchangeRate, mWishlistData.getPrice())))
//                                                .putCurrency(Currency.getInstance("" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")))
//                                                .putItemName("" + mWishlistData.getName())
//                                                .putItemType(mWishlistData.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
//                                                .putItemId("" + mWishlistData.getSku()));
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    if (RestClient.isTuneEnable) {
//                                        try {
//                                            Tune tune = Tune.getInstance();
//                                            List<TuneEventItem> mTuneEventItemsList = new ArrayList<TuneEventItem>();
//                                            TuneEventItem mTuneEventItem = new TuneEventItem("" + mWishlistData.getName());
//                                            mTuneEventItemsList.add(mTuneEventItem);
//                                            tune.measureEvent(new TuneEvent(TuneEvent.ADD_TO_CART)
//                                                    .withEventItems(mTuneEventItemsList)
//                                                    .withQuantity(1)
//                                                    .withContentId(mWishlistData.getSku())
//                                                    .withRevenue(0)
//                                                    .withContentType(mWishlistData.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
//                                                    .withCurrencyCode("" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")));
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//                                    if (getActivity() instanceof HomeActivity) {
//                                        HomeActivity activity = (HomeActivity) getActivity();
//                                        activity.setFirebaseLog("Add_to_cart");
//                                    }
//
//                                } else {
////                                    if (response.code() == 400) {
////                                        Utils.showToast(getActivity(), "" + getString(R.string.msg_outof_order));
////                                    } else {
////                                        Utils.showAlertDialog(getActivity());
////                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                System.out.println(t.getMessage());
////                                Utils.closeProgressDialog();
////                                Utils.showAlertDialog(getActivity());
//                            }
//                        });
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
////                        Utils.showAlertDialog(getActivity());
//                    }
//
//            }
//        }
//    }


    @Override
    public void onStart() {
        super.onStart();

//        showWishList();

//        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
//        if (!TextUtils.isEmpty(userToken)) {
//
//            lnrLogin.setVisibility(View.GONE);
//            lnrWishListEmpty.setVisibility(View.GONE);
//            lvWishList.setVisibility(View.VISIBLE);
//
//            showWishList();
//
//        } else {
////            Utils.showToast(getActivity(), "" + getString(R.string.msg_need_login));
//            lnrLogin.setVisibility(View.VISIBLE);
//            lnrWishListEmpty.setVisibility(View.GONE);
//            lvWishList.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.setFirebaseLog("Wishlist");
        }

    }

}
