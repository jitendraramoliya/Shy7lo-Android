package shy7lo.com.shy7lo.notification;

import android.content.SharedPreferences;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.LogUtils;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        LogUtils.e("", "Firebase Token::" + refreshedToken);



//        if (!TextUtils.isEmpty(refreshedToken)) {
        SharedPreferences pref = Shy7lo.getAppContext().getSharedPreferences(NotificationUtils.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", refreshedToken);
        editor.commit();

        setOneSignalRegister();
//        }

//        if (!TextUtils.isEmpty(refreshedToken)) {
//            MyPref.setPref(getApplicationContext(), MyPref.ONE_SIGNAL_PUSH_TOKEN, "" + refreshedToken);
//        }

    }

    private void setOneSignalRegister() {

        if (MyPref.getPref(Shy7lo.getAppContext(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, false) && !MyPref.getPref(Shy7lo.getAppContext(), MyPref.ONE_SIGNAL_IS_REGISTERED, false)) {
            SharedPreferences pref = Shy7lo.getAppContext().getSharedPreferences(NotificationUtils.SHARED_PREF, 0);
            if (!TextUtils.isEmpty(pref.getString("regId", ""))) {
                Map<String, Object> jsonParams = new ArrayMap<>();
//            jsonParams.put("device_token", "" + MyPref.getPref(getApplicationContext(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
                jsonParams.put("device_token", "" + pref.getString("regId", ""));
                jsonParams.put("device_type", "android");
                jsonParams.put("subcribe_id", "" + MyPref.getPref(Shy7lo.getAppContext(), MyPref.ONE_SIGNAL_USER_ID, ""));

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

                ApiInterface apiService =
                        RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
                Call<JsonElement> callCode = apiService.registerPushToken(Shy7lo.mLangCode, body);
                callCode.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        if (response.isSuccessful()) {
                            MyPref.setPref(Shy7lo.getAppContext(), MyPref.ONE_SIGNAL_IS_REGISTERED, true);
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

