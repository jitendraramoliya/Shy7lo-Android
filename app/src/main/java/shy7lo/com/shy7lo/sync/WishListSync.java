package shy7lo.com.shy7lo.sync;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.model.Wishlist;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by Jiten on 02-01-2018.
 */

public class WishListSync {

    private static String TAG = "WishListSync";
    Context mContext;

    public WishListSync(Context mContext) {
        this.mContext = mContext;
    }

    public void syncWishList(Context mContext, DBAdapter dbAdapter) {
        getWishList(mContext, dbAdapter);
    }

    private void getWishList(final Context mContext, final DBAdapter dbAdapter) {
        LogUtils.e(TAG, "getWishList Call");

        final String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");

        if (!Utils.isInternetConnected(mContext) || TextUtils.isEmpty(userToken)) {
            return;
        }

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, false);
        Call<ResponseBody> call = apiService.getWishlist(Shy7lo.mLangCode, "Bearer " + userToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                LogUtils.e(TAG, "getWishList code::" + response.code());

                if (response.isSuccessful()) {


                    try {
                        Wishlist wishlist = new Gson().fromJson(response.body().string(), Wishlist.class);

                        LogUtils.e(TAG, "wishlist success::" + wishlist.success);
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

                                Intent broadIntent = new Intent();
                                broadIntent.setAction(HomeActivity.FILTER_WISHLIST_CHANGE);
                                mContext.sendBroadcast(broadIntent);
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                uploadWishListToServer(mContext, dbAdapter);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtils.e(TAG, "getWishList onFailure call::");
                uploadWishListToServer(mContext, dbAdapter);
            }
        });
    }

    int mLocalWishListCount = 0;

    private void uploadWishListToServer(final Context mContext, final DBAdapter dbAdapter) {
        LogUtils.e(TAG, "uploadWishListToServer Call");

        mLocalWishListCount = 0;

        String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
        if (!TextUtils.isEmpty(userToken)) {

            final ArrayList<Wishlist.WishlistData> mLocalWishList = dbAdapter.getLocalWishList();
            if (mLocalWishList != null && mLocalWishList.size() > 0) {
                for (int i = 0; i < mLocalWishList.size(); i++) {

                    final Wishlist.WishlistData mWishItem = mLocalWishList.get(i);

                    JSONObject jsonParams = new JSONObject();
                    try {
                        jsonParams.put("sku", "" + mWishItem.getSku());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());

                    try {
                        ApiInterface apiService =
                                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
                        Call<JsonElement> call = apiService.addProductToWishlist(Shy7lo.mLangCode, "Bearer " + userToken, body);
                        call.enqueue(new Callback<JsonElement>() {
                            @Override
                            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                                ++mLocalWishListCount;
                                LogUtils.e(TAG, "uploadWishListToServer code::" + response.code() + " count" + mLocalWishListCount);

                                if (response.isSuccessful()) {

                                    try {
                                        JSONObject jResponse = new JSONObject(response.body().toString());

                                        if (jResponse != null && jResponse.getString("success").equals("1")) {
                                            dbAdapter.updateIsOnServerWishItem(mWishItem.getSku(), "1");
                                            LogUtils.e(TAG, "Wish Item Is On server:" + mWishItem.getName() + " " + mWishItem.getSku());
//                                            if (RestClient.isTuneEnable) {
//                                                try {
//                                                    Tune tune = Tune.getInstance();
//                                                    List<TuneEventItem> mTuneEventItemsList = new ArrayList<TuneEventItem>();
//                                                    TuneEventItem mTuneEventItem = new TuneEventItem("" + mWishItem.getName());
//                                                    mTuneEventItemsList.add(mTuneEventItem);
//                                                    tune.measureEvent(new TuneEvent(TuneEvent.ADD_TO_WISHLIST)
//                                                            .withEventItems(mTuneEventItemsList)
//                                                            .withQuantity(1)
//                                                            .withContentId(mWishItem.getSku())
//                                                            .withRevenue(0)
//                                                            .withContentType(mWishItem.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
//                                                            .withCurrencyCode("" + MyPref.getPref(mContext, MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")));
//
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }

                                            if (RestClient.isFacebookLive) {
                                                try {
                                                    // FB Log Add to wishlist
                                                    AppEventsLogger logger = AppEventsLogger.newLogger(mContext);

                                                    Bundle parameters = new Bundle();
                                                    parameters.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "" + MyPref.getPref(mContext,
                                                            MyPref.DEFAULT_CURRENCY_CODE, mContext.getResources().getString(R.string.SAR)));
//                                                    parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "" + mWishItem.getTypeId());
                                                    parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "Product");
                                                    parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, "" + mWishItem.getSku());

                                                    logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_WISHLIST, parameters);

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        } else if (jResponse != null && jResponse.getString("success").equals("0")) {
                                            dbAdapter.updateIsOnServerWishItem(mWishItem.getSku(), "0");
                                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
                                            dbAdapter.updateIsOnServerWishItem(mWishItem.getSku(), "0");
                                        }

                                        LogUtils.e(TAG, "mLocalShoppingListCount::" + mLocalWishListCount + " Size::" + mLocalWishList.size());
                                        if (mLocalWishListCount == mLocalWishList.size()) {
                                            getUpdatedWishList(mContext, dbAdapter);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                        LogUtils.e(TAG, "mLocalShoppingListCount::" + mLocalWishListCount + " Size::" + mLocalWishList.size());
                                        if (mLocalWishListCount == mLocalWishList.size()) {
                                            getUpdatedWishList(mContext, dbAdapter);
                                        }
                                    }

                                } else {
                                    LogUtils.e(TAG, "mLocalShoppingListCount::" + mLocalWishListCount + " Size::" + mLocalWishList.size());
                                    if (mLocalWishListCount == mLocalWishList.size()) {
                                        getUpdatedWishList(mContext, dbAdapter);
                                    }
                                    if (String.valueOf(response.code()).startsWith("5")) {
                                        Utils.showAlertDialog(mContext, "" + response.code());
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
                            public void onFailure(Call<JsonElement> call, Throwable t) {
                                ++mLocalWishListCount;
                                LogUtils.e(TAG, "mLocalShoppingListCount::" + mLocalWishListCount + " Size::" + mLocalWishList.size());
                                if (mLocalWishListCount == mLocalWishList.size()) {
                                    getUpdatedWishList(mContext, dbAdapter);
                                }
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                getUpdatedWishList(mContext, dbAdapter);
            }
        }
    }

    private void getUpdatedWishList(final Context mContext, final DBAdapter dbAdapter) {
        LogUtils.e(TAG, "getUpdatedWishList Call");

        final String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");

        if (!Utils.isInternetConnected(mContext) || TextUtils.isEmpty(userToken)) {
            return;
        }

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, false);
        Call<ResponseBody> call = apiService.getWishlist(Shy7lo.mLangCode, "Bearer " + userToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                LogUtils.e(TAG, "getUpdatedWishList code::" + response.code());

                if (response.isSuccessful()) {


                    try {
                        Wishlist wishlist = new Gson().fromJson(response.body().string(), Wishlist.class);

                        LogUtils.e(TAG, "wishlist success::" + wishlist.success);
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

                                Intent broadIntent = new Intent();
                                broadIntent.setAction(HomeActivity.FILTER_WISHLIST_CHANGE);
                                mContext.sendBroadcast(broadIntent);
                            }

                        }

                        deleteWishItemFromServer(mContext, dbAdapter);

                    } catch (IOException e) {
                        e.printStackTrace();
                        deleteWishItemFromServer(mContext, dbAdapter);
                    }


                } else {
                    deleteWishItemFromServer(mContext, dbAdapter);
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtils.e(TAG, "getUpdatedWishList onFailure call::");
                deleteWishItemFromServer(mContext, dbAdapter);
            }
        });
    }

    int mLocalDeleteWishListCount = 0;

    private void deleteWishItemFromServer(final Context mContext, final DBAdapter dbAdapter) {
        LogUtils.e(TAG, "deleteWishItemFromServer Call");

        mLocalDeleteWishListCount = 0;

        String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
        if (!TextUtils.isEmpty(userToken)) {

            ArrayList<Wishlist.WishlistData> mLocalSoftDeleteWishList = dbAdapter.getLocalSoftDeleteWishList();
            if (mLocalSoftDeleteWishList != null && mLocalSoftDeleteWishList.size() > 0) {
                for (int i = 0; i < mLocalSoftDeleteWishList.size(); i++) {

                    final Wishlist.WishlistData mWishItem = mLocalSoftDeleteWishList.get(i);

                    ApiInterface apiService =
                            RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

                    Map<String, Object> jsonParams = new ArrayMap<>();
                    jsonParams.put("item_id", "" + mWishItem.getId());

                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            (new JSONObject(jsonParams)).toString());

                    Call<JsonElement> callCode = apiService.deleteProductFromWishlist(Shy7lo.mLangCode, "Bearer " + userToken, body);

                    callCode.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                            LogUtils.e(TAG, "deleteWishItemFromServer code::" + response.code());

                            if (response.isSuccessful()) {
                                try {

                                    JSONObject jResponse = new JSONObject(response.body().toString());
                                    if (jResponse != null && jResponse.getString("success").equals("1")) {
                                        LogUtils.e(TAG, "Wish Soft Item Deleted: " + mWishItem.getName() + " " + mWishItem.getSku());
                                        dbAdapter.removeWishItem(mWishItem.getSku());
                                    } else if (jResponse != null && jResponse.getString("success").equals("0")) {
                                    } else if (jResponse != null && jResponse.getString("success").equals("2")) {
                                    } else {
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else{
                                if (String.valueOf(response.code()).startsWith("5")) {
                                    Utils.showAlertDialog(mContext, "" + response.code());
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
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                        }
                    });

                }
            }
        }

    }

}
