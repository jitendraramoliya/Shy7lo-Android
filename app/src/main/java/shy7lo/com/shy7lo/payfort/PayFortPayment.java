package shy7lo.com.shy7lo.payfort;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.payfort.fort.android.sdk.base.FortSdk;
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
import com.payfort.sdk.android.dependancies.base.FortInterfaces;
import com.payfort.sdk.android.dependancies.models.FortRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.CardConfigration;
import shy7lo.com.shy7lo.model.Signature256;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by JITEN-PC on 02-05-2017.
 */

public class PayFortPayment {

    private static String TAG = "PayFortPayment";

    //Request key for response
    public static final int RESPONSE_GET_TOKEN = 111;
    public static final int RESPONSE_PURCHASE = 222;
    public static final int RESPONSE_PURCHASE_CANCEL = 333;
    public static final int RESPONSE_PURCHASE_SUCCESS = 444;
    public static final int RESPONSE_PURCHASE_FAILURE = 555;

    //WS params
    private final static String KEY_MERCHANT_IDENTIFIER = "merchant_identifier";
    private final static String KEY_SERVICE_COMMAND = "service_command";
    private final static String KEY_DEVICE_ID = "device_id";
    private final static String KEY_LANGUAGE = "language";
    private final static String KEY_ACCESS_CODE = "access_code";
    private final static String KEY_SIGNATURE = "signature";

    //Commands
    public final static String AUTHORIZATION = "AUTHORIZATION";
    public final static String PURCHASE = "PURCHASE";
    private final static String SDK_TOKEN = "SDK_TOKEN";


    //Test token url
//    private final static String TEST_TOKEN_URL = "https://sbpaymentservices.payfort.com/FortAPI/paymentApi";
    //Live token url
//    public final static String LIVE_TOKEN_URL = "https://paymentservices.payfort.com/FortAPI/paymentApi";
    //Make a change for live or test token url from WS_GET_TOKEN variable
//    private final static String WS_GET_TOKEN = TEST_TOKEN_URL;
//    private final static String WS_GET_TOKEN = LIVE_TOKEN_URL;
//    private final static String FortSdk_ENVIRONMENT_TEST = FortSdk.ENVIRONMENT.TEST;
//    private final static String FortSdk_ENVIRONMENT_PRODCUTION = FortSdk.ENVIRONMENT.PRODUCTION;


    //Statics
    private static String MERCHANT_IDENTIFIER = "";//"Cyc0HZ9xV6j";//qxbVGjJK;
    private static String ACCESS_CODE = "";//"zx0IPmPy5jp1vAz8Kpg7";//XNEw23o2DodbqTOLR6V3;
    private final static String SHA_TYPE = "SHA-256";
    private static String SIGNATURE_256 = "";
    //    private final static String SHA_REQUEST_PHRASE = "fRawuF2DreDrace";
    public final static String SHA_RESPONSE_PHRASE = "fRawuF2DreDrace";
    public final static String CURRENCY_TYPE = "SAR";
    public static String LANGUAGE_TYPE = Shy7lo.mLangCode;//Arabic - ar //English - en

    private final Gson gson;
    private Activity context;
    private IPaymentRequestCallBack iPaymentRequestCallBack;
    private FortCallBackManager fortCallback = null;
    private ProgressDialog progressDialog;
    private String sdkToken;
    private PayFortData payFortData;

    public PayFortPayment(Activity context, FortCallBackManager fortCallback, IPaymentRequestCallBack iPaymentRequestCallBack) {
        this.context = context;
        this.fortCallback = fortCallback;
        this.iPaymentRequestCallBack = iPaymentRequestCallBack;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Processing your payment\nPlease wait...");
        progressDialog.setCancelable(false);
        sdkToken = "";
        gson = new Gson();
    }

    public void requestForPayment(PayFortData payFortData) {
        this.payFortData = payFortData;
        LANGUAGE_TYPE = Shy7lo.mLangCode;
        getCardConfigration();

    }

    private void getCardConfigration() {

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<CardConfigration> call = apiService.getCardConfigration(Shy7lo.mLangCode);
        call.enqueue(new Callback<CardConfigration>() {
            @Override
            public void onResponse(Call<CardConfigration> call, Response<CardConfigration> response) {

                LogUtils.e(TAG, "response code:" + response.code());

                if (response.isSuccessful()) {
                    try {

                        CardConfigration mCardConfigration = response.body();
                        if (mCardConfigration != null && mCardConfigration.success.equals("1")) {
                            MERCHANT_IDENTIFIER = mCardConfigration.data.merchantIdentifier;
                            ACCESS_CODE = mCardConfigration.data.accessCode;

                            getSignature();
                        } else if (mCardConfigration != null && mCardConfigration.success.equals("2")) {
                            Utils.showInitialScreen(context);
                            if (context instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) context;
                                activity.loadShoppingCartsWithClearStack();
                            }
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    if (String.valueOf(response.code()).startsWith("5")) {
                        Utils.showAlertDialog(context, "" + response.code());
                    } else {

                        try {
                            JSONObject jResponse = new JSONObject(response.errorBody().string());
                            if (jResponse != null && jResponse.getString("success").equals("0")) {
                                Utils.showToast(context, "" + jResponse.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CardConfigration> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    private void getSignature() {

        Map<String, Object> jsonParams = new ArrayMap<>();

        String device_id = FortSdk.getDeviceId(context);

        jsonParams.put("service_command", SDK_TOKEN);
        jsonParams.put("device_id", device_id);
        jsonParams.put("language", Shy7lo.mLangCode);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<Signature256> call = apiService.getSignature(Shy7lo.mLangCode, body);
        call.enqueue(new Callback<Signature256>() {
            @Override
            public void onResponse(Call<Signature256> call, Response<Signature256> response) {

                LogUtils.e(TAG, "response code:" + response.code());

                if (response.isSuccessful()) {
                    try {

                        Signature256 mSignature256 = response.body();
                        if (mSignature256 != null && mSignature256.success.equals("1")) {
                            SIGNATURE_256 = mSignature256.data;
                            new GetTokenFromServer().execute(RestClient.WS_GET_TOKEN);
                        } else if (mSignature256 != null && mSignature256.success.equals("2")) {
                            Utils.showInitialScreen(context);
                            if (context instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) context;
                                activity.loadShoppingCartsWithClearStack();
                            }
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    if (String.valueOf(response.code()).startsWith("5")) {
                        Utils.showAlertDialog(context, "" + response.code());
                    } else {

                        try {
                            JSONObject jResponse = new JSONObject(response.errorBody().string());
                            if (jResponse != null && jResponse.getString("success").equals("0")) {
                                Utils.showToast(context, "" + jResponse.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Signature256> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    private void requestPurchase() {
        LogUtils.e("", "requestPurchase call");
        try {
            FortSdk.getInstance().registerCallback(context, getPurchaseFortRequest(), RestClient.FORT_SDK_ENVIRONMENT, RESPONSE_PURCHASE,
                    fortCallback, new FortInterfaces.OnTnxProcessed() {
                        @Override
                        public void onCancel(Map<String, String> requestParamsMap, Map<String,
                                String> responseMap) {
                            JSONObject response = new JSONObject(responseMap);
                            PayFortData payFortData = gson.fromJson(response.toString(), PayFortData.class);
                            payFortData.paymentResponse = response.toString();
                            LogUtils.e("Cancel Response", response.toString());
                            if (iPaymentRequestCallBack != null) {
                                iPaymentRequestCallBack.onPaymentRequestResponse(RESPONSE_PURCHASE_CANCEL, payFortData);
                            }
                        }

                        @Override
                        public void onSuccess(Map<String, String> requestParamsMap, Map<String,
                                String> fortResponseMap) {
                            JSONObject response = new JSONObject(fortResponseMap);
                            PayFortData payFortData = gson.fromJson(response.toString(), PayFortData.class);
                            payFortData.paymentResponse = response.toString();
                            LogUtils.e("Success Response", response.toString());
                            if (iPaymentRequestCallBack != null) {
                                iPaymentRequestCallBack.onPaymentRequestResponse(RESPONSE_PURCHASE_SUCCESS, payFortData);
                            }
                        }

                        @Override
                        public void onFailure(Map<String, String> requestParamsMap, Map<String,
                                String> fortResponseMap) {
                            JSONObject response = new JSONObject(fortResponseMap);
                            PayFortData payFortData = gson.fromJson(response.toString(), PayFortData.class);
                            payFortData.paymentResponse = response.toString();
                            LogUtils.e("Failure Response", response.toString());
                            if (iPaymentRequestCallBack != null) {
                                iPaymentRequestCallBack.onPaymentRequestResponse(RESPONSE_PURCHASE_FAILURE, payFortData);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FortRequest getPurchaseFortRequest() {
        LogUtils.e("", "getPurchaseFortRequest call");
        FortRequest fortRequest = new FortRequest();
        if (payFortData != null) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("amount", String.valueOf(payFortData.amount));
            parameters.put("command", payFortData.command);
            parameters.put("currency", payFortData.currency);
            parameters.put("customer_email", payFortData.customerEmail);
            parameters.put("language", payFortData.language);
            parameters.put("merchant_reference", payFortData.merchantReference);
            parameters.put("sdk_token", sdkToken);
            LogUtils.e("", "parameters::" + parameters);
            fortRequest.setRequestMap(parameters);
        } else {
            LogUtils.e("", "payFortData is null");
        }
        return fortRequest;
    }

    private class GetTokenFromServer extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {
            String response = "";
            try {
                HttpURLConnection conn;
                URL url = new URL(postParams[0].replace(" ", "%20"));
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("content-type", "application/json");

                String str = getTokenParams();
                byte[] outputInBytes = str.getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(outputInBytes);
                os.close();
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = conn.getInputStream();
                    response = convertStreamToString(inputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            progressDialog.hide();
            Log.e("Response", response + "");
            try {
                PayFortData payFortData = gson.fromJson(response, PayFortData.class);
                LogUtils.e("", "payFortData.sdkToken::" + payFortData.sdkToken);
                if (!TextUtils.isEmpty(payFortData.sdkToken)) {
                    sdkToken = payFortData.sdkToken;
                    requestPurchase();
                } else {
                    payFortData.paymentResponse = response;
                    iPaymentRequestCallBack.onPaymentRequestResponse(RESPONSE_GET_TOKEN, payFortData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getTokenParams() {
        JSONObject jsonObject = new JSONObject();
        try {
            String device_id = FortSdk.getDeviceId(context);
//            String concatenatedString = SHA_REQUEST_PHRASE +
//                    KEY_ACCESS_CODE + "=" + ACCESS_CODE +
//                    KEY_DEVICE_ID + "=" + device_id +
//                    KEY_LANGUAGE + "=" + LANGUAGE_TYPE +
//                    KEY_MERCHANT_IDENTIFIER + "=" + MERCHANT_IDENTIFIER +
//                    KEY_SERVICE_COMMAND + "=" + SDK_TOKEN +
//                    SHA_REQUEST_PHRASE;

            jsonObject.put(KEY_SERVICE_COMMAND, SDK_TOKEN);
            jsonObject.put(KEY_MERCHANT_IDENTIFIER, MERCHANT_IDENTIFIER);
            jsonObject.put(KEY_ACCESS_CODE, ACCESS_CODE);
//            String signature = getSignatureSHA256(concatenatedString);
            jsonObject.put(KEY_SIGNATURE, SIGNATURE_256);
            jsonObject.put(KEY_DEVICE_ID, device_id);
            jsonObject.put(KEY_LANGUAGE, LANGUAGE_TYPE);

            Log.e("signature", SIGNATURE_256);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("JsonString", String.valueOf(jsonObject));
        return String.valueOf(jsonObject);
    }

    private static String convertStreamToString(InputStream inputStream) {
        if (inputStream == null)
            return null;
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream), 1024);
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static String getSignatureSHA256(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(SHA_TYPE);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            return String.format("%0" + (messageDigest.length * 2) + 'x', new BigInteger(1, messageDigest));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}