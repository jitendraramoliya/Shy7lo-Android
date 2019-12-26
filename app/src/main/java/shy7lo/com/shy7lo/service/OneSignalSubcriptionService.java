package shy7lo.com.shy7lo.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.notification.NotificationUtils;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.LogUtils;

/**
 * Created by Jiten on 01-10-2017.
 */

public class OneSignalSubcriptionService extends IntentService {

    final Handler handler = new Handler();
    Timer timer;
    TimerTask timerTask;

    public OneSignalSubcriptionService() {
        super("OneSignalSubcriptionService");
        LogUtils.e("", "OneSignalSubcriptionService call");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
//            String CHANNEL_ID = "my_channel_01";
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//
//            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
//
//            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setContentTitle("")
//                    .setContentText("").build();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String NOTIFICATION_CHANNEL_ID = "shy7lo.com.shy7lo";
                String channelName = "Notification Service";
                NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
                chan.setLightColor(Color.BLUE);
                chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                assert manager != null;
                manager.createNotificationChannel(chan);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
                Notification notification = notificationBuilder.setOngoing(true)
                        .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                        .setContentTitle("" + getString(R.string.app_name))
                        .setPriority(NotificationManager.IMPORTANCE_MIN)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .build();
                startForeground(1, notification);
            } else {
                startForeground(1, new Notification());
            }
        }
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        initializeTimerTask();

        timer.schedule(timerTask, 500, 60000); //
    }


    public void stoptimertask() {
        LogUtils.e("", "stoptimertask call");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        stopSelf();
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        startTimer();
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        //get the current timeStamp
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss a");
                        final String strDate = simpleDateFormat.format(calendar.getTime());
                        LogUtils.e("", "strDate::" + strDate);
//
//                        //show the toast
//                        int duration = Toast.LENGTH_SHORT;
//                        Toast toast = Toast.makeText(getApplicationContext(), strDate, duration);
//                        toast.show();

                        if (!MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, false)) {
                            OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
                            if (status != null) {
                                boolean isEnabled = status.getPermissionStatus().getEnabled();

                                boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
                                String mUserID = status.getSubscriptionStatus().getUserId();
                                String mPushToken = status.getSubscriptionStatus().getPushToken();

                                LogUtils.e("", "Service isEnabled: " + isEnabled + "\nisSubscribed: " + isSubscribed);
                                LogUtils.e("", "Service PlayerID: " + mUserID + "\nPushToken: " + mPushToken);

                                MyPref.setPref(getActivity(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, isSubscribed);
                                if (isSubscribed) {
                                    if (!TextUtils.isEmpty(mUserID)) {
                                        MyPref.setPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, mUserID);
                                    }
//                                if (!TextUtils.isEmpty(mPushToken)) {
//                                    MyPref.setPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, mPushToken);
//                                }
                                }
                            }
                        }

                        if (MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, false) && !MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_IS_REGISTERED, false)) {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences(NotificationUtils.SHARED_PREF, 0);
                            if (!TextUtils.isEmpty(pref.getString("regId", ""))) {
                                Map<String, Object> jsonParams = new ArrayMap<>();
//                            jsonParams.put("device_token", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
                                jsonParams.put("device_token", "" + pref.getString("regId", ""));
                                jsonParams.put("device_type", "android");
                                jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));

                                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

                                ApiInterface apiService =
                                        RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
                                Call<JsonElement> callCode = apiService.registerPushToken(Shy7lo.mLangCode, body);
                                callCode.enqueue(new Callback<JsonElement>() {
                                    @Override
                                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                        LogUtils.e("", "response code::" + response.code());
                                        if (response.isSuccessful()) {
                                            MyPref.setPref(getActivity(), MyPref.ONE_SIGNAL_IS_REGISTERED, true);
//                                        Utils.showToast(getActivity(), "Register Token Api Called: " + MyPref.getPref(getApplicationContext(), MyPref.ONE_SIGNAL_USER_ID, ""));


                                        }
                                        stoptimertask();
                                    }


                                    @Override
                                    public void onFailure(Call<JsonElement> call, Throwable t) {
                                        LogUtils.e("", "onFailure call");
                                        stoptimertask();
                                    }
                                });
                            }
                        } else if (MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_IS_SUBCRIBED, false) && MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_IS_REGISTERED, false)) {
                            stoptimertask();
                        }

                    }
                });
            }
        };
    }

    private Context getActivity() {
        return this;
    }
}
