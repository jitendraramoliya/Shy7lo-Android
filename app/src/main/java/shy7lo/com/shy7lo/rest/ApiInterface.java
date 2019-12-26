package shy7lo.com.shy7lo.rest;


import com.google.gson.JsonElement;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import shy7lo.com.shy7lo.BuildConfig;
import shy7lo.com.shy7lo.model.ApiResponse;
import shy7lo.com.shy7lo.model.AppInit;
import shy7lo.com.shy7lo.model.AppVersion;
import shy7lo.com.shy7lo.model.CMSPage;
import shy7lo.com.shy7lo.model.CardConfigration;
import shy7lo.com.shy7lo.model.CategoryList;
import shy7lo.com.shy7lo.model.CategoryProductList;
import shy7lo.com.shy7lo.model.CityResponse;
import shy7lo.com.shy7lo.model.CurrencyList;
import shy7lo.com.shy7lo.model.Maintenance;
import shy7lo.com.shy7lo.model.MyAddressResponse;
import shy7lo.com.shy7lo.model.MyDetailsResponse;
import shy7lo.com.shy7lo.model.MyOrderDetailsResponse;
import shy7lo.com.shy7lo.model.MyOrderResponse;
import shy7lo.com.shy7lo.model.PaymentMethodRespnose;
import shy7lo.com.shy7lo.model.ProductCategoryList;
import shy7lo.com.shy7lo.model.ProductList;
import shy7lo.com.shy7lo.model.RegionsResponse;
import shy7lo.com.shy7lo.model.SearchAlgoriaProductList;
import shy7lo.com.shy7lo.model.ShoppingBag;
import shy7lo.com.shy7lo.model.Signature256;
import shy7lo.com.shy7lo.model.SimilarProducts;
import shy7lo.com.shy7lo.model.SortingList;
import shy7lo.com.shy7lo.model.SortingPojo;
import shy7lo.com.shy7lo.model.WebCacheVersion;
import shy7lo.com.shy7lo.model.WomenBanner;

/**
 * Created by JITEN-PC on 21-12-2016.
 */

public interface ApiInterface {

    String LANG_KEY = "Lang";
    String API_TOKEN_KEY = "Api-Token";
    String X_LANG_KEY = "X-Lang";
    String ACCEPT_KEY = "Accept";
    String CONTENT_TYPE_KEY = "Content-Type";
    String X_API_KEY = "X-Api";
    String X_WEBSITE_KEY = "X-Website";
    String X_DEVICE_KEY = "X-Device";
    String X_VERSION_KEY = "X-Version";
    String mApiVersion = "v11";
    String mType = "application/json";
    String mWebsite = "shy7lo";
    String mDevice = "android";
    String mVersion = BuildConfig.VERSION_NAME;

//    @Headers({
//            ACCEPT_KEY + ": " + mType,
//            CONTENT_TYPE_KEY + ": " + mType,
//            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
//            X_DEVICE_KEY + ": " + mDevice,
//            X_VERSION_KEY + ": " + mVersion,
//
//    })
//    @POST("checkout/cart/guest-carts")
//    Call<JsonElement> getGuestCartToken(@Header(X_LANG_KEY) String lang);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,

    })
    @GET("cart/new-guest-carts")
    Call<JsonElement> getGuestCartToken(@Header(X_LANG_KEY) String lang);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,

    })
    @GET("general/cms-pages")
    Call<CMSPage> getCMSPage(@Header(X_LANG_KEY) String lang);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,

    })
    @GET("general/banner/version")
//    @GET("app/banner/version")
    Call<WebCacheVersion> getWebCacheVersion(@Header(X_LANG_KEY) String lang);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,

    })
    @GET("catalog/sorting/options")
    Call<SortingPojo> getSortingList(@Header(X_LANG_KEY) String lang);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            ////       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,

    })
    @GET("checkout/guest/carts/check/{cart_id}")
    Call<JsonElement> checkGuestCartToken(@Header(X_LANG_KEY) String lang, @Path("cart_id") String cart_id);

//    @Headers({
//            "Accept: application/json",
//            "Content-Type: application/json",
//            "Authorization: Bearer 8ilvs6ke53k10gqxsyr022ok28umaqxr"
//    })
//    @POST("rest/V1/guest-carts/{token}/items")api/init
//    Call<ResponseBody> addProductInCart(@Path("token") String token, @Body RequestBody params);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
//    @POST("checkout/cart/guest-carts/add-item")
    @POST("cart/add-item")
    Call<ResponseBody> addGuestProductInCart(@Header(X_LANG_KEY) String lang, @Body RequestBody params);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("cart/add-item")
//    @POST("checkout/cart/guest-carts/add-item-configure")
    Call<ResponseBody> addGuestProductConfigInCart(@Header(X_LANG_KEY) String lang, @Body RequestBody params);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("cart/add-item")
//    @POST("checkout/cart/add-item")
    Call<ResponseBody> addUserProductInCart(@Header(X_LANG_KEY) String lang, @Header("Authorization") String userToken,
                                            @Body RequestBody params);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("cart/add-item")
//    @POST("checkout/cart/add-itemConfigure")
    Call<ResponseBody> addUserProductConfigInCart(@Header(X_LANG_KEY) String lang, @Header("Authorization") String userToken,
                                                  @Body RequestBody params);


    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("cart/items/{token}")
//    @GET("checkout/guest-carts/{token}")
    Call<ShoppingBag> getGuestCartList(@Header(X_LANG_KEY) String lang, @Path("token") String token);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("cart/items")
    Call<ShoppingBag> getUserCartList(@Header(X_LANG_KEY) String lang, @Header("Authorization") String userToken);

//    @Headers({
//            "Accept: application/json",
//            "Content-Type: application/json",
//            "X-Website: shy7lo"
//    })
//    @POST("rest/V1/products/{sku}/media")
//    Call<JsonElement> getCartItemImage(@Header(X_LANG_KEY) String lang, @Path("sku") String sku);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
//    @DELETE("checkout/cart/guest-carts/delete-item")
    @POST("cart/items/delete/{token}")
//    @HTTP(method = "DELETE", path = "checkout/cart/guest-carts/delete-item", hasBody = true)
    Call<ResponseBody> deleteItemFromGuestCart(@Header(X_LANG_KEY) String lang, @Body RequestBody params, @Path("token") String token);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
//    @DELETE("checkout/cart/delete-item")
//    @HTTP(method = "DELETE", path = "checkout/cart/delete-item", hasBody = true)
    @POST("cart/items/delete")
    Call<ResponseBody> deleteItemFromUserCart(@Header(X_LANG_KEY) String lang, @Header("Authorization") String userToken,
                                              @Body RequestBody params);


//    @Headers({
//            ACCEPT_KEY + ": " + mType,
//            CONTENT_TYPE_KEY + ": " + mType,
//            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
//            X_DEVICE_KEY + ": " + mDevice,
//            X_VERSION_KEY + ": " + mVersion,
//    })
//    @GET("catalog")
//    Call<CategoryList> getCategoriesList(@Header(X_LANG_KEY) String lang);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("catalog/categories")
    Call<CategoryList> getCategoriesList(@Header(X_LANG_KEY) String lang);

//    @Headers({
//            ACCEPT_KEY + ": " + mType,
//            CONTENT_TYPE_KEY + ": " + mType,
////            X_API_KEY + ": " + mApiVersion,
////     //       X_WEBSITE_KEY + ": " + mWebsite,
////            X_DEVICE_KEY + ": " + mDevice,
////            X_VERSION_KEY + ": " + mVersion,
//    })
//    @GET("general/landing-screens")
//    Call<HomeTabList> getHomeTabList(@Header(X_LANG_KEY) String lang);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("screen/banners/1")
    Call<WomenBanner> getBannerWomenList(@Header(X_LANG_KEY) String lang);

    @Headers({
//            ACCEPT_KEY + ": " + mType,
//            CONTENT_TYPE_KEY + ": " + mType,
//            X_API_KEY + ": " + mApiVersion,
//            X_WEBSITE_KEY + ": " + mWebsite
            "Accept: application/json",
            "Content-Type: application/json",
            "X-Website: shy7lo",
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("catalog/subcategory/{category_id}")
    Call<ProductCategoryList> getProductSubCategoryList(@Header(X_LANG_KEY) String lang, @Path("category_id") String category_id);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("catalog/sub-category/{category_id}")
    Call<CategoryProductList> getCategoryForProductList(@Header(X_LANG_KEY) String lang, @Path("category_id") String category_id);
//    @GET("catalog/categorychildlist")
//    Call<CategoryProductList> getCategoryForProductList(@Header(X_LANG_KEY) String lang, @Query("category_id") String category_id);

//    @Headers({
//            ACCEPT_KEY + ": " + mType,
//            CONTENT_TYPE_KEY + ": " + mType,
//            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
//            X_DEVICE_KEY + ": " + mDevice,
//            X_VERSION_KEY + ": " + mVersion,
//    })
//    @GET("catalog/relatedproducts")
//    Call<SimilarProducts> getSimilarProductList(@Header(LANG_KEY) String lang, @Query("category_ids") String category_id, @Query("limit") String limit, @Query("page") String page);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("catalog/related/products/{category_id}")
    Call<SimilarProducts> getSimilarProductList(@Header(X_LANG_KEY) String lang, @Path("category_id") String category_id);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("customer/basic-info")
    Call<MyDetailsResponse> getMyDetailList(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("customer/basic-info")
    Call<MyDetailsResponse> getUpdateDetailList(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("customer/password/update")
    Call<ApiResponse> changePassword(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("customer/all-orders")
    Call<MyOrderResponse> getMyOrderList(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("customer/current/orders/{page}")
    Call<MyOrderResponse> getCurrentOrderList(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization, @Path("page") int page_number);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("customer/history/orders/{page}")
    Call<MyOrderResponse> getHistoryOrderList(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization, @Path("page") int page_number);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("sales/customer/order/tracking")
    Call<MyOrderDetailsResponse> getOrderDetailsList(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("customer/address/all")
    Call<MyAddressResponse> getAllAddress(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("customer/address/add")
    Call<ApiResponse> addAddress(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @DELETE("customer/address/delete/{address_id}")
    Call<ApiResponse> deleteAddress(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization, @Path("address_id") String address_id);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("customer/address/update/{address_id}")
    Call<ApiResponse> updateAddress(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization, @Body RequestBody param, @Path("address_id") String address_id);


//    @Headers({
//            ACCEPT_KEY + ": " + mType,
//            CONTENT_TYPE_KEY + ": " + mType,
//            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
//            X_DEVICE_KEY + ": " + mDevice,
//            X_VERSION_KEY + ": " + mVersion,
//    })
////    @POST("catalog/products")
//    @POST("catalog/products/listing")
//    Call<ProductList> getProductsList(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("catalog/products")
    Call<ProductList> getProductsList(@Header(X_LANG_KEY) String lang, @QueryMap Map<String, String> options);


    //    @Headers({
//            ACCEPT_KEY + ": " + mType,
//            CONTENT_TYPE_KEY + ": " + mType,
//            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
//            X_DEVICE_KEY + ": " + mDevice,
//            X_VERSION_KEY + ": " + mVersion,
//    })
//    @POST("catalog/products/filters")
//    Call<SortingList> getSortingList(@Header(X_LANG_KEY) String lang, @Body RequestBody param);
    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("catalog/products/filters")
    Call<SortingList> getSortingList(@Header(X_LANG_KEY) String lang, @QueryMap Map<String, Object> options);

//    @Headers({
//            ACCEPT_KEY + ": " + mType,
//            CONTENT_TYPE_KEY + ": " + mType,
//            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
//            X_DEVICE_KEY + ": " + mDevice,
//            X_VERSION_KEY + ": " + mVersion,
//    })
//    @POST("app/init")
//    Call<InitialData> getInitialData(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

//    @Headers({
//            "Accept: application/json",
//            "Content-Type: application/json",
//            "X-Website: shy7lo"
//    })
//    @GET()
//    Call<JsonElement> getProductDetails(@Header(X_LANG_KEY) String lang, @Url String url);

//    @Headers({
//            ACCEPT_KEY + ": " + mType,
//            CONTENT_TYPE_KEY + ": " + mType,
//            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
//            X_DEVICE_KEY + ": " + mDevice,
//            X_VERSION_KEY + ": " + mVersion,
//    })
//    @POST("catalog/product/detail")
//    Call<JsonElement> getProductDetails(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("catalog/single-product/sku/{product_id}")
    Call<JsonElement> getProductDetails(@Header(X_LANG_KEY) String lang, @Path("product_id") String product_id);

//    @Headers({
//            ACCEPT_KEY + ": " + mType,
//            CONTENT_TYPE_KEY + ": " + mType,
//            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
//            X_DEVICE_KEY + ": " + mDevice,
//            X_VERSION_KEY + ": " + mVersion,
//    })
//    @POST("catalog/product/detail/new")
//    Call<JsonElement> getProductDetails(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("catalog/productdetail")
    Call<JsonElement> getProductNewDetails(@Header(X_LANG_KEY) String lang, @QueryMap Map<String, String> options);


//    @Headers({
//            ACCEPT_KEY + ": " + mType,
//            CONTENT_TYPE_KEY + ": " + mType,
//            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
//            X_DEVICE_KEY + ": " + mDevice,
//            X_VERSION_KEY + ": " + mVersion,
//    })
//    @POST("catalog/algolia/product")
//    Call<JsonElement> getAlgoliaProductDetails(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("catalog/single-product/id/{product_id}")
    Call<JsonElement> getAlgoliaProductDetails(@Header(X_LANG_KEY) String lang, @Path("product_id") String product_id);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("customer/login")
    Call<JsonElement> getSignIn(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("customer/register")
    Call<JsonElement> getSignUp(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("currency")
    Call<CurrencyList> getCurrencyList(@Header(X_LANG_KEY) String lang);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("cart/add/coupon/{token}")
    Call<JsonElement> validateGuestCouponCode(@Header(X_LANG_KEY) String lang, @Body RequestBody param, @Path("token") String token);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("cart/add/coupon")
    Call<JsonElement> validateUserCouponCode(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("checkout/guest-carts/coupons/{cart_id}")
    Call<JsonElement> getGuestCouponCode(@Header(X_LANG_KEY) String lang, @Path("cart_id") String cart_id);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("checkout/customer-cart/coupons")
    Call<JsonElement> getUserCouponCode(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("customer/dashboard")
    Call<JsonElement> getUserDashboard(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("checkout/update/cart/items/{token}")
    Call<ResponseBody> removeUnavailalbeItemsGuest(@Header(X_LANG_KEY) String lang, String token);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("checkout/update/cart/items")
    Call<ResponseBody> removeUnavailalbeItemsUser(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @DELETE("cart/delete/coupon/{token}")
//    @HTTP(method = "DELETE", path = "checkout/guest-carts/coupons/{cart_id}", hasBody = false)
    Call<JsonElement> deleteGuestCouponCode(@Header(X_LANG_KEY) String lang, @Path("token") String token);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @DELETE("cart/delete/coupon")
//    @HTTP(method = "DELETE", path = "checkout/customer-cart/coupons", hasBody = false)
    Call<JsonElement> deleteUserCouponCode(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization);


    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("cart/address")
    Call<JsonElement> submitGuestAddress(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("cart/address")
    Call<JsonElement> submitUserAddress(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("cart/payment/{guest_id}")
    Call<PaymentMethodRespnose> getGuestPaymentMethod(@Header(X_LANG_KEY) String lang, @Header("X-Version") String version, @Path("guest_id") String guest_id);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("cart/payment")
    Call<PaymentMethodRespnose> getUserPaymentMethod(@Header(X_LANG_KEY) String lang, @Header("X-Version") String version, @Header("Authorization") String authorization);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("cart/payment/set-method")
    Call<JsonElement> setGuestPaymentMethod(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("cart/payment/set-method")
    Call<JsonElement> setUserPaymentMethod(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization,
                                           @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("cart/total/{guest_id}")
//    @GET("checkout/cart/totals/{guest_id}")
    Call<JsonElement> getGuestTotalAmount(@Header(X_LANG_KEY) String lang, @Path("guest_id") String guest_id);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
//    @GET("checkout/cart/totals/mine")
    @GET("cart/total")
    Call<JsonElement> getUserTotalAmount(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("general/maintenance/mode")
    Call<Maintenance> getMaintenanceStatus(@Header(X_LANG_KEY) String lang);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("checkout/get-cart/{guest_id}")
    Call<JsonElement> getGuestReviewOrder(@Header(X_LANG_KEY) String lang, @Path("guest_id") String guest_id);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("checkout/get-cart/mine")
    Call<JsonElement> getUserReviewOrder(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization, @Body RequestBody body);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("cart/place-order")
    Call<JsonElement> guestPlaceOrder(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("cart/place-order")
    Call<JsonElement> userPlaceOrder(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,

    })
    @GET("checkout/cart/order/cancel/{order_id}")
    Call<JsonElement> cancelOrder(@Header(X_LANG_KEY) String lang, @Path("order_id") String order_id);


//    @Headers({
//            ACCEPT_KEY + ": " + mType,
//            CONTENT_TYPE_KEY + ": " + mType,
//            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
//            X_DEVICE_KEY + ": " + mDevice,
//            X_VERSION_KEY + ": " + mVersion,
//    })
//    @POST("catalog/searchProduct")
//    Call<SearchProductList> getSearchProduct(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    //    @Headers({
//            ACCEPT_KEY + ": " + mType,
//            CONTENT_TYPE_KEY + ": " + mType,
//            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
//            X_DEVICE_KEY + ": " + mDevice,
//            X_VERSION_KEY + ": " + mVersion,
//    })
//    @POST("catalog/search/algolia")
//    Call<SearchAlgoriaProductList> getAlgoriaSearchProduct(@Header(X_LANG_KEY) String lang, @Body RequestBody param);
    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("catalog/search")
    Call<SearchAlgoriaProductList> getAlgoriaSearchProduct(@Header(X_LANG_KEY) String lang, @QueryMap Map<String, String> params);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("customer/wishlist/additem")
    Call<JsonElement> addProductToWishlist(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization,
                                           @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("customer/wishlist/items")
    Call<ResponseBody> getWishlist(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("customer/wishlist/deleteitem")
//    @HTTP(method = "DELETE", path = "customer/wishlist/deleteitem", hasBody = true)
    Call<JsonElement> deleteProductFromWishlist(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization,
                                                @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
//    @POST("checkout/cart/guest-carts/update")
    @POST("cart/items/update/{token}")
    Call<JsonElement> setGuestQuantityItem(@Header(X_LANG_KEY) String lang, @Body RequestBody param, @Path("token") String token);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
//    @POST("checkout/cart/update")
    @POST("cart/items/update")
    Call<JsonElement> setUserQuantityItem(@Header(X_LANG_KEY) String lang, @Header("Authorization") String authorization,
                                          @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("general/update/language")
    Call<JsonElement> registerPushToken(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("customer/register")
    Call<JsonElement> registerUser(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("customer/login")
    Call<JsonElement> signInUser(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("customer/forget/password")
    Call<JsonElement> forgotPassword(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("checkout/payfort/config")
    Call<CardConfigration> getCardConfigration(@Header(X_LANG_KEY) String lang);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("checkout/payfort/credit-card/signature")
    Call<Signature256> getSignature(@Header(X_LANG_KEY) String lang, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,
    })
    @GET("general/app/version")
    Call<AppVersion> getAppVersion(@Header(X_LANG_KEY) String lang);

    @Headers({
//            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
//            X_API_KEY + ": " + mApiVersion,
//     //       X_WEBSITE_KEY + ": " + mWebsite,
//            X_DEVICE_KEY + ": " + mDevice,
//            X_VERSION_KEY + ": " + mVersion,
    })
    @POST("init")
    Call<AppInit> getAppInit(@Header(X_LANG_KEY) String lang, @Header(API_TOKEN_KEY) String api_token, @Body RequestBody param);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,

    })
    @GET("general/app/regions/{country_id}")
    Call<RegionsResponse> getRegionList(@Header(LANG_KEY) String lang, @Path("country_id") String cart_id);

    @Headers({
            ACCEPT_KEY + ": " + mType,
            CONTENT_TYPE_KEY + ": " + mType,
            X_API_KEY + ": " + mApiVersion,
            //       X_WEBSITE_KEY + ": " + mWebsite,
            X_DEVICE_KEY + ": " + mDevice,
            X_VERSION_KEY + ": " + mVersion,

    })
    @GET("general/app/cities")
    Call<CityResponse> getCityList(@Header(LANG_KEY) String lang);
}
