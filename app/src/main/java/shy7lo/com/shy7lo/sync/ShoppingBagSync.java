//package shy7lo.com.shy7lo.sync;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.support.v4.util.ArrayMap;
//import android.text.TextUtils;
//
//import com.crashlytics.android.answers.AddToCartEvent;
//import com.crashlytics.android.answers.Answers;
//import com.tune.Tune;
//import com.tune.TuneEvent;
//import com.tune.TuneEventItem;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Currency;
//import java.util.List;
//import java.util.Map;
//
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import shy7lo.com.shy7lo.BuildConfig;
//import shy7lo.com.shy7lo.HomeActivity;
//import shy7lo.com.shy7lo.application.Shy7lo;
//import shy7lo.com.shy7lo.database.DBAdapter;
//import shy7lo.com.shy7lo.model.ShoppingBag;
//import shy7lo.com.shy7lo.notification.NotificationUtils;
//import shy7lo.com.shy7lo.pref.MyPref;
//import shy7lo.com.shy7lo.rest.ApiInterface;
//import shy7lo.com.shy7lo.rest.RestClient;
//import shy7lo.com.shy7lo.utils.Constant;
//import shy7lo.com.shy7lo.utils.LogUtils;
//import shy7lo.com.shy7lo.utils.Utils;
//
///**
// * Created by Jiten on 04-01-2018.
// */
//
//public class ShoppingBagSync {
//
//    private static String TAG = "ShoppingBagSync";
//    Context mContext;
//    public boolean isSyncFinished = true;
//
//    public ShoppingBagSync(Context mContext) {
//        this.mContext = mContext;
//    }
//
//    public void syncShoppingBagList(Context mContext, DBAdapter dbAdapter) {
//        isSyncFinished = false;
//        getShoppingItemList(mContext, dbAdapter);
//    }
//
//    private void getShoppingItemList(final Context mContext, final DBAdapter dbAdapter) {
//
//        LogUtils.e(TAG, "getShoppingItemList Call");
//
//        final String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
//        final String guestToken = MyPref.getPref(mContext, MyPref.GUEST_CART_ID, "");
//
//        if (!Utils.isInternetConnected(mContext)) {
//            return;
//        }
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//
//        Call<ShoppingBag> call;
//        if (!TextUtils.isEmpty(userToken)) {
//
//            Map<String, Object> jsonParams = new ArrayMap<>();
//            jsonParams.put("cart_id", guestToken);
////            jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));
//
//            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
//
//            call = apiService.getUserCartList(Shy7lo.mLangCode, "Bearer " + userToken, body);
//        } else {
//            call = apiService.getGuestCartList(Shy7lo.mLangCode, guestToken);
//        }
//
//        call.enqueue(new Callback<ShoppingBag>() {
//            @Override
//            public void onResponse(Call<ShoppingBag> call, Response<ShoppingBag> response) {
//                if (response.isSuccessful()) {
//                    try {
//
//                        ShoppingBag shoppingBag = response.body();
//                        if (shoppingBag != null && shoppingBag.success.equals("1")) {
//                            final List<ShoppingBag.Item> carItemList = shoppingBag.data.getItems();
//                            if (carItemList != null && carItemList.size() > 0) {
//                                for (int i = 0; i < carItemList.size(); i++) {
//                                    ShoppingBag.Item mShoppingItem = carItemList.get(i);
//
//                                    if (TextUtils.isEmpty(userToken)) {
//                                        mShoppingItem.setIsGuest("1");
//                                        mShoppingItem.setToken(guestToken);
//                                    } else {
//                                        mShoppingItem.setIsGuest("0");
//                                        mShoppingItem.setToken(userToken);
//                                    }
//                                    mShoppingItem.setSoft_delete("0");
//                                    mShoppingItem.setIs_on_server("1");
//
//                                    String mSizeLable = "";
//                                    if (mShoppingItem.getProductType().equalsIgnoreCase("simple")) {
//                                        mShoppingItem.setOption_value(mSizeLable);
//
//                                        if (dbAdapter.isContainInWishList(TextUtils.isEmpty(mShoppingItem.getParent_sku()) ? mShoppingItem.getSku() : mShoppingItem.getParent_sku(), mShoppingItem.getProductType(), mSizeLable)) {
//                                            dbAdapter.updateShoppingItemFromServer(mShoppingItem, mShoppingItem.getProductType(), mSizeLable);
//                                        } else {
//                                            dbAdapter.addShoppingItemFromServer(mShoppingItem);
//                                        }
//
//                                    } else {
//
//                                        List<ShoppingBag.ConfigureOption> mConfigureOptionList = mShoppingItem.getConfigureOptionList();
//                                        if (mConfigureOptionList != null && mConfigureOptionList.size() > 0) {
//                                            for (int j = 0; j < mConfigureOptionList.size(); j++) {
//                                                if (mConfigureOptionList.get(j).getOption_label().contains("Size")) {
//                                                    mSizeLable = mConfigureOptionList.get(j).getOption_value();
//                                                    break;
//                                                }
//                                            }
//                                        }
//
//                                        mShoppingItem.setOption_value(mSizeLable);
//                                        if (dbAdapter.isContainInWishList(TextUtils.isEmpty(mShoppingItem.getParent_sku()) ? mShoppingItem.getSku() : mShoppingItem.getParent_sku(), mShoppingItem.getProductType(), mSizeLable)) {
//                                            dbAdapter.updateShoppingItemFromServer(mShoppingItem, mShoppingItem.getProductType(), mSizeLable);
//                                        } else {
//                                            dbAdapter.addShoppingItemFromServer(mShoppingItem);
//                                        }
//
//                                    }
//
//                                }
//                                Intent broadIntent = new Intent();
//                                broadIntent.setAction(HomeActivity.FILTER_SHOPPING_BAG_CHANGE);
//                                mContext.sendBroadcast(broadIntent);
//                            }
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                uploadShoppingBagToServer(mContext, dbAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<ShoppingBag> call, Throwable t) {
//                uploadShoppingBagToServer(mContext, dbAdapter);
//            }
//        });
//
//    }
//
//    int mLocalShoppingListCount = 0;
//
//    private void uploadShoppingBagToServer(final Context mContext, final DBAdapter dbAdapter) {
//        LogUtils.e(TAG, "uploadShoppingBagToServer Call");
//        mLocalShoppingListCount = 0;
//
//        final String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
//        final String guestToken = MyPref.getPref(mContext, MyPref.GUEST_CART_ID, "");
//
//        if (!Utils.isInternetConnected(mContext)) {
//            return;
//        }
//        final ArrayList<ShoppingBag.Item> mLocalShoppingList = dbAdapter.getLocalShoppingBagList();
//
//        if (mLocalShoppingList != null && mLocalShoppingList.size() > 0) {
//            for (int i = 0; i < mLocalShoppingList.size(); i++) {
//                final ShoppingBag.Item mShoppingItem = mLocalShoppingList.get(i);
//
//                try {
//                    JSONObject jsonParams = new JSONObject();
//
//                    SharedPreferences pref = mContext.getSharedPreferences(NotificationUtils.SHARED_PREF, 0);
//
//                    if (!TextUtils.isEmpty(userToken)) {
//
//                        jsonParams.put("sku", "" + mShoppingItem.getSku());
//                        jsonParams.put("qty", "" + mShoppingItem.getQty());
//                        jsonParams.put("product_name", "" + mShoppingItem.getName());
//                        jsonParams.put("price", "" + mShoppingItem.getPrice());
//                        jsonParams.put("product_type", mShoppingItem.getProductType());
//                        jsonParams.put("device_token", "" + pref.getString("regId", ""));
////                jsonParams.put("device_token", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
//                        jsonParams.put("subcribe_id", "" + MyPref.getPref(mContext, MyPref.ONE_SIGNAL_USER_ID, ""));
//
//
//                        if (mShoppingItem.getProductType().equalsIgnoreCase("configurable")) {
//                            JSONArray jsonArray = new JSONArray();
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("option_id", mShoppingItem.getAttribute_id());
//                            jsonObject.put("option_value", mShoppingItem.getValue_index());
//                            jsonArray.put(jsonObject);
//                            jsonParams.put("configurable_item_options", jsonArray);
//                        }
//
//                    } else {
//
//                        jsonParams.put("sku", "" + mShoppingItem.getSku());
//                        jsonParams.put("qty", "" + mShoppingItem.getQty());
//                        jsonParams.put("product_name", "" + mShoppingItem.getName());
//                        jsonParams.put("price", "" + mShoppingItem.getPrice());
//                        jsonParams.put("product_type", mShoppingItem.getProductType());
//                        jsonParams.put("cart_id", "" + guestToken);
//                        jsonParams.put("device_token", "" + pref.getString("regId", ""));
////                jsonParams.put("device_token", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
//                        jsonParams.put("subcribe_id", "" + MyPref.getPref(mContext, MyPref.ONE_SIGNAL_USER_ID, ""));
//
//                        if (mShoppingItem.getProductType().equalsIgnoreCase("configurable")) {
//                            JSONArray jsonArray = new JSONArray();
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("option_id", mShoppingItem.getAttribute_id());
//                            jsonObject.put("option_value", mShoppingItem.getValue_index());
//                            jsonArray.put(jsonObject);
//                            jsonParams.put("configurable_item_options", jsonArray);
//                        }
//                    }
//
//                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
//
//                    ApiInterface apiService =
//                            RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//
//                    Call<ResponseBody> call;
//                    if (!TextUtils.isEmpty(userToken)) {
//                        if (mShoppingItem.getProductType().equalsIgnoreCase("configurable")) {
//                            call = apiService.addUserProductConfigInCart(Shy7lo.mLangCode, "Bearer " + userToken, body);
//                        } else {
//                            call = apiService.addUserProductInCart(Shy7lo.mLangCode, "Bearer " + userToken, body);
//                        }
//                    } else {
//                        if (mShoppingItem.getProductType().equalsIgnoreCase("configurable")) {
//                            call = apiService.addGuestProductConfigInCart(Shy7lo.mLangCode, body);
//                        } else {
//                            call = apiService.addGuestProductInCart(Shy7lo.mLangCode, body);
//                        }
//                    }
//
//                    call.enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            ++mLocalShoppingListCount;
//
//                            LogUtils.e(TAG, "response code:" + response.code());
//                            if (response.isSuccessful()) {
//                                try {
//                                    JSONObject jsonObject = new JSONObject(response.body().string());
//                                    if (jsonObject != null && jsonObject.getString("success").equalsIgnoreCase("1")) {
//                                        String item_id = jsonObject.getJSONObject("data").getString("item_id");
//                                        dbAdapter.updateIsOnServerShoppingFromDetail(mShoppingItem.getSku(), item_id, mShoppingItem.getProductType(), mShoppingItem.getAttribute_id(), mShoppingItem.getOption_value(), "1");
//
//                                        try {
//                                            float mExchangeRate = MyPref.getPref(mContext, MyPref.DEFAULT_EXCHANGE_RATE, 1f);
//                                            if (!BuildConfig.DEBUG) {
//                                                Answers.getInstance().logAddToCart(new AddToCartEvent()
//                                                        .putItemPrice(BigDecimal.valueOf(Utils.getAnswerPrice(mExchangeRate, mShoppingItem.getPrice())))
//                                                        .putCurrency(Currency.getInstance("" + MyPref.getPref(mContext, MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")))
//                                                        .putItemName("" + mShoppingItem.getName())
//                                                        .putItemType(mShoppingItem.getProductType().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
//                                                        .putItemId("" + mShoppingItem.getSku()));
//                                            }
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        if (RestClient.isLive) {
//                                            try {
//                                                Tune tune = Tune.getInstance();
//                                                List<TuneEventItem> mTuneEventItemsList = new ArrayList<TuneEventItem>();
//                                                TuneEventItem mTuneEventItem = new TuneEventItem("" + mShoppingItem.getName());
//                                                mTuneEventItemsList.add(mTuneEventItem);
//                                                tune.measureEvent(new TuneEvent(TuneEvent.ADD_TO_CART)
//                                                        .withEventItems(mTuneEventItemsList)
//                                                        .withQuantity(1)
//                                                        .withContentId(mShoppingItem.getSku())
//                                                        .withRevenue(0)
//                                                        .withContentType(mShoppingItem.getProductType().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
//                                                        .withCurrencyCode("" + MyPref.getPref(mContext, MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")));
//
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//
//                                        if (mContext instanceof HomeActivity) {
//                                            HomeActivity activity = (HomeActivity) mContext;
//                                            activity.setDrawerSwipe(false);
//                                            activity.setFirebaseLog("Add_to_cart");
//                                        }
//                                    }
//
//                                    ++mLocalShoppingListCount;
//                                    LogUtils.e(TAG, "mLocalShoppingListCount::" + mLocalShoppingListCount + " Size::" + mLocalShoppingList.size());
//                                    if (mLocalShoppingListCount == mLocalShoppingList.size()) {
//                                        getUpdatedShoppingItemList(mContext, dbAdapter);
//                                    }
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    ++mLocalShoppingListCount;
//                                    LogUtils.e(TAG, "mLocalShoppingListCount::" + mLocalShoppingListCount + " Size::" + mLocalShoppingList.size());
//                                    if (mLocalShoppingListCount == mLocalShoppingList.size()) {
//                                        getUpdatedShoppingItemList(mContext, dbAdapter);
//                                    }
//                                }
//
//                            } else {
//                                ++mLocalShoppingListCount;
//                                LogUtils.e(TAG, "mLocalShoppingListCount::" + mLocalShoppingListCount + " Size::" + mLocalShoppingList.size());
//                                if (mLocalShoppingListCount == mLocalShoppingList.size()) {
//                                    getUpdatedShoppingItemList(mContext, dbAdapter);
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            ++mLocalShoppingListCount;
//                            LogUtils.e(TAG, "mLocalShoppingListCount::" + mLocalShoppingListCount + " Size::" + mLocalShoppingList.size());
//                            if (mLocalShoppingListCount == mLocalShoppingList.size()) {
//                                getUpdatedShoppingItemList(mContext, dbAdapter);
//                            }
//                        }
//                    });
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            getUpdatedShoppingItemList(mContext, dbAdapter);
//        }
//    }
//
//    private void getUpdatedShoppingItemList(final Context mContext, final DBAdapter dbAdapter) {
//
//        LogUtils.e(TAG, "getUpdatedShoppingItemList Call");
//
//        final String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
//        final String guestToken = MyPref.getPref(mContext, MyPref.GUEST_CART_ID, "");
//
//        if (!Utils.isInternetConnected(mContext)) {
//            return;
//        }
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//
//        Call<ShoppingBag> call;
//        if (!TextUtils.isEmpty(userToken)) {
//
//            Map<String, Object> jsonParams = new ArrayMap<>();
//            jsonParams.put("cart_id", guestToken);
////            jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));
//
//            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
//
//            call = apiService.getUserCartList(Shy7lo.mLangCode, "Bearer " + userToken, body);
//        } else {
//            call = apiService.getGuestCartList(Shy7lo.mLangCode, guestToken);
//        }
//
//        call.enqueue(new Callback<ShoppingBag>() {
//            @Override
//            public void onResponse(Call<ShoppingBag> call, Response<ShoppingBag> response) {
//                if (response.isSuccessful()) {
//                    try {
//
//                        ShoppingBag shoppingBag = response.body();
//                        if (shoppingBag != null && shoppingBag.success.equals("1")) {
//                            final List<ShoppingBag.Item> carItemList = shoppingBag.data.getItems();
//                            if (carItemList != null && carItemList.size() > 0) {
//                                for (int i = 0; i < carItemList.size(); i++) {
//                                    ShoppingBag.Item mShoppingItem = carItemList.get(i);
//
//                                    if (TextUtils.isEmpty(userToken)) {
//                                        mShoppingItem.setIsGuest("1");
//                                        mShoppingItem.setToken(guestToken);
//                                    } else {
//                                        mShoppingItem.setIsGuest("0");
//                                        mShoppingItem.setToken(userToken);
//                                    }
//                                    mShoppingItem.setSoft_delete("0");
//                                    mShoppingItem.setIs_on_server("1");
//
//                                    String mSizeLable = "";
//                                    if (mShoppingItem.getProductType().equalsIgnoreCase("simple")) {
//                                        mShoppingItem.setOption_value(mSizeLable);
//
//                                        if (dbAdapter.isContainInWishList(TextUtils.isEmpty(mShoppingItem.getParent_sku()) ? mShoppingItem.getSku() : mShoppingItem.getParent_sku(), mShoppingItem.getProductType(), mSizeLable)) {
//                                            dbAdapter.updateShoppingItemFromServer(mShoppingItem, mShoppingItem.getProductType(), mSizeLable);
//                                        } else {
//                                            dbAdapter.addShoppingItemFromServer(mShoppingItem);
//                                        }
//
//                                    } else {
//
//                                        List<ShoppingBag.ConfigureOption> mConfigureOptionList = mShoppingItem.getConfigureOptionList();
//                                        if (mConfigureOptionList != null && mConfigureOptionList.size() > 0) {
//                                            for (int j = 0; j < mConfigureOptionList.size(); j++) {
//                                                if (mConfigureOptionList.get(j).getOption_label().contains("Size")) {
//                                                    mSizeLable = mConfigureOptionList.get(j).getOption_value();
//                                                    break;
//                                                }
//                                            }
//                                        }
//
//                                        mShoppingItem.setOption_value(mSizeLable);
//                                        if (dbAdapter.isContainInWishList(TextUtils.isEmpty(mShoppingItem.getParent_sku()) ? mShoppingItem.getSku() : mShoppingItem.getParent_sku(), mShoppingItem.getProductType(), mSizeLable)) {
//                                            dbAdapter.updateShoppingItemFromServer(mShoppingItem, mShoppingItem.getProductType(), mSizeLable);
//                                        } else {
//                                            dbAdapter.addShoppingItemFromServer(mShoppingItem);
//                                        }
//
//                                    }
//
//                                    Intent broadIntent = new Intent();
//                                    broadIntent.setAction(HomeActivity.FILTER_SHOPPING_BAG_CHANGE);
//                                    mContext.sendBroadcast(broadIntent);
//
//                                }
//                            }
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                deleteShoppingItemFromServer(mContext, dbAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<ShoppingBag> call, Throwable t) {
//                deleteShoppingItemFromServer(mContext, dbAdapter);
//            }
//        });
//
//    }
//
//    public void deleteShoppingItemFromServer(Context mContext, final DBAdapter dbAdapter) {
//        LogUtils.e(TAG, "deleteShoppingItemFromServer call");
//
//        isSyncFinished = true;
//
//        if (!Utils.isInternetConnected(mContext)) {
//            return;
//        }
//
//        ArrayList<ShoppingBag.Item> mLocalSoftDeleteShoppingList = dbAdapter.getLocalSoftDeleteShoppingList();
//        if (mLocalSoftDeleteShoppingList != null && mLocalSoftDeleteShoppingList.size() > 0) {
//            for (int i = 0; i < mLocalSoftDeleteShoppingList.size(); i++) {
//                final ShoppingBag.Item cartItem = mLocalSoftDeleteShoppingList.get(i);
//
//                String guestToken = MyPref.getPref(mContext, MyPref.GUEST_CART_ID, "");
//                String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
//
//                ApiInterface apiService =
//                        RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//                Call<ResponseBody> call;
//
//                SharedPreferences pref = mContext.getSharedPreferences(NotificationUtils.SHARED_PREF, 0);
//
//                if (!TextUtils.isEmpty(userToken)) {
//
//                    JSONObject jsonParams = new JSONObject();
//                    try {
//                        jsonParams.put("item_id", "" + cartItem.getItemId());
//                        jsonParams.put("device_token", "" + pref.getString("regId", ""));
//                        jsonParams.put("subcribe_id", "" + MyPref.getPref(mContext, MyPref.ONE_SIGNAL_USER_ID, ""));
////                jsonParams.put("device_token", "" + MyPref.getPref(mContext, MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
//
//                    call = apiService.deleteItemFromUserCart(Shy7lo.mLangCode, "Bearer " + userToken, body);
//                } else {
//
//                    JSONObject jsonParams = new JSONObject();
//                    try {
//                        jsonParams.put("cart_id", "" + guestToken);
//                        jsonParams.put("item_id", "" + cartItem.getItemId());
//                        jsonParams.put("device_token", "" + pref.getString("regId", ""));
//                        jsonParams.put("subcribe_id", "" + MyPref.getPref(mContext, MyPref.ONE_SIGNAL_USER_ID, ""));
////                jsonParams.put("device_token", "" + MyPref.getPref(mContext, MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
//
//                    call = apiService.deleteItemFromGuestCart(Shy7lo.mLangCode, body);
//                }
//
//                call.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                        LogUtils.e(TAG, "response code:" + response.code());
//
//                        if (response.isSuccessful()) {
//                            dbAdapter.deleteShoppingItem(TextUtils.isEmpty(cartItem.getParent_sku()) ?
//                                    cartItem.getSku() : cartItem.getParent_sku(), cartItem.getProductType(), cartItem.getOption_value());
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        System.out.println(t.getMessage());
//                    }
//                });
//
//            }
//
//        }
//
//        Intent broadIntent = new Intent();
//        broadIntent.putExtra(Constant.BNDL_IS_SYNC_FINISHED, true);
//        broadIntent.setAction(HomeActivity.FILTER_SHOPPING_BAG_CHANGE);
//        mContext.sendBroadcast(broadIntent);
//
//
//    }
//
//}
