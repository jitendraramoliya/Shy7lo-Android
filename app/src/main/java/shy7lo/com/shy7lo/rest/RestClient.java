package shy7lo.com.shy7lo.rest;

/**
 * Created by JITEN-PC on 28-12-2016.
 */

import com.adjust.sdk.AdjustConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payfort.fort.android.sdk.base.FortSdk;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import shy7lo.com.shy7lo.BuildConfig;

public class RestClient {

    //    public static boolean isTuneEnable = true;
    public static boolean isFabricEnable = true;
    public static boolean isOneSignalEnable = true;
    public static boolean isFirebaseEnable = true;

    //Debug
//    private static String API2_PREFIX = "https://api2.shylabs.com";
//    private static String API_PORTAL_PREFIX = "https://portal.shylabs.com";
//    private static String API_NEW_PREFIX = "http://new.shylabs.com";
//    public static String MINT_KEY = "083b028a";
//    public static String ADJUST_ENVIRONMENT = AdjustConfig.ENVIRONMENT_SANDBOX;
//    public static boolean isLive = false;
//    public static boolean isFacebookLive = false;
//    public final static String FORT_SDK_ENVIRONMENT = FortSdk.ENVIRONMENT.TEST;
//    public final static String WS_GET_TOKEN = "https://sbpaymentservices.payfort.com/FortAPI/paymentApi";

    // release
    private static String API2_PREFIX = "https://api2.shy7lo.com";
    private static String API_PORTAL_PREFIX = "https://portal.shylabs.com";
    private static String API_NEW_PREFIX = "http://new.shylabs.com";
    public static String MINT_KEY = "db8b2126";
    public static String ADJUST_ENVIRONMENT = AdjustConfig.ENVIRONMENT_PRODUCTION;
    public static boolean isLive = true;
    public static boolean isFacebookLive = true;
    public final static String FORT_SDK_ENVIRONMENT = FortSdk.ENVIRONMENT.PRODUCTION;
    public final static String WS_GET_TOKEN = "https://paymentservices.payfort.com/FortAPI/paymentApi";

    public static final String API2_SHYLABS_API_URL = API2_PREFIX + "/api/";
    public static final String API_PORTAL_SHYLABS_URL = API_PORTAL_PREFIX;
//    public static final String API_NEW_URL = API_NEW_PREFIX;

    public static ApiInterface getDynamicClient(String url, boolean isGson) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient.Builder httpClient = getUnsafeOkHttpClient();
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.writeTimeout(60, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.interceptors().add(logging);
        }

        Retrofit retrofit = null;

        if (isGson) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build()).build();
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(httpClient.build()).build();
        }


        return retrofit.create(ApiInterface.class);
    }

    private static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

//            OkHttpClient okHttpClient = builder.build();
//            return okHttpClient;
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
