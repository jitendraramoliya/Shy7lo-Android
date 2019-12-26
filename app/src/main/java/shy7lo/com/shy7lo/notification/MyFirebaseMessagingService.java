package shy7lo.com.shy7lo.notification;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    public static final String SCREEN_NAME = "SCREEN_NAME";
    public static final String SCREEN_MESSAGE = "SCREEN_MESSAGE";
    public static final String SCREEN_MAIN = "SCREEN_MAIN";
    public static final String SCREEN_PRODUCT_GENERAL = "SCREEN_PRODUCT_GENERAL";
    public static final String SCREEN_PRODUCT_WOMEN = "SCREEN_PRODUCT_WOMEN";
    public static final String SCREEN_PRODUCT_MEN = "SCREEN_PRODUCT_MEN";
    public static final String SCREEN_PRODUCT_KIDS = "SCREEN_PRODUCT_KIDS";
    public static final String SCREEN_PRODUCT_LIST = "SCREEN_PRODUCT_LIST";
    public static final String SCREEN_DETAILS = "SCREEN_DETAILS";
    public static final String SCREEN_CART = "SCREEN_CART";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
//            handleNotification(remoteMessage.getNotification().getBody());
//        }
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.e(TAG, "Currencies Payload: " + remoteMessage.getData().toString());
//
//            try {
//                JSONObject json = new JSONObject(remoteMessage.getData().toString());
//                handleDataMessage(json);
//            } catch (Exception e) {
//                Log.e(TAG, "Exception: " + e.getMessage());
//            }
//        }

//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
//            handleNotification(remoteMessage.getNotification().getBody());
//        }

        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.e(TAG, "Currencies Payload: " + remoteMessage.getData().toString());
//
//            try {
//                Map<String, String> params = remoteMessage.getData();
//                JSONObject json = new JSONObject(params);
////                JSONObject json = new JSONObject(remoteMessage.getData().toString());
//
////                JSONObject data = json.getJSONObject("data");
//
////                String title = data.getString("title");
////                String message = data.getString("body");
////                String Url = data.getString("imag_uri");
//
//                String title = json.getString("title");
//                String message = json.getString("body");
//                String imageUrl = "";
//                if (json.has("image_uri")) {
//                    imageUrl = json.getString("image_uri");
//                }
//                String main_screen = json.getString("main_screen");
//                String product_listing = json.getString("product_listing");
//                String sku = json.getString("sku");
//
//                Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
////                Bundle bundle = new Bundle();
//
//                String screen = "";
//
//                if (!TextUtils.isEmpty(main_screen)) {
//
//                    if (main_screen.equalsIgnoreCase("women")) {
//                        resultIntent.putExtra(SCREEN_NAME, SCREEN_PRODUCT_WOMEN);
//                    } else if (main_screen.equalsIgnoreCase("men")) {
//                        resultIntent.putExtra(SCREEN_NAME, SCREEN_PRODUCT_MEN);
//                    } else if (main_screen.equalsIgnoreCase("kids")) {
//                        resultIntent.putExtra(SCREEN_NAME, SCREEN_PRODUCT_KIDS);
//                    } else if (main_screen.equalsIgnoreCase("cart")) {
//                        resultIntent.putExtra(SCREEN_NAME, SCREEN_CART);
//                    }
//                    resultIntent.putExtra(SCREEN_MESSAGE, message);
//                    screen = resultIntent.getStringExtra(SCREEN_NAME);
//                    LogUtils.e("", "Main screen " + screen);
//
//                } else if (!TextUtils.isEmpty(product_listing)) {
//
//                    resultIntent.putExtra(SCREEN_NAME, SCREEN_PRODUCT_LIST);
//                    resultIntent.putExtra(SCREEN_MESSAGE, "" + product_listing);
//
//                    screen = resultIntent.getStringExtra(SCREEN_NAME);
//                    LogUtils.e("", "Product Listing " + screen);
//                } else if (!TextUtils.isEmpty(sku)) {
//
//                    resultIntent.putExtra(SCREEN_NAME, SCREEN_DETAILS);
//                    resultIntent.putExtra(SCREEN_MESSAGE, "" + sku);
//
//                    screen = resultIntent.getStringExtra(SCREEN_NAME);
//                    LogUtils.e("", "Sku " + screen);
//                } else {
//
//                    resultIntent.putExtra(SCREEN_NAME, SCREEN_MAIN);
//                    resultIntent.putExtra(SCREEN_MESSAGE, message);
//
//                    screen = resultIntent.getStringExtra(SCREEN_NAME);
//                    LogUtils.e("", "Default " + screen);
//                }
//
////                if (!TextUtils.isEmpty(main_screen)) {
////
////                    if (main_screen.equalsIgnoreCase("women")) {
////                        bundle.putString(SCREEN_NAME, SCREEN_PRODUCT_WOMEN);
////                    } else if (main_screen.equalsIgnoreCase("men")) {
////                        bundle.putString(SCREEN_NAME, SCREEN_PRODUCT_MEN);
////                    } else if (main_screen.equalsIgnoreCase("kids")) {
////                        bundle.putString(SCREEN_NAME, SCREEN_PRODUCT_KIDS);
////                    }
////                    bundle.putString(SCREEN_MESSAGE, message);
////                    screen = bundle.getString(SCREEN_NAME);
////                    LogUtils.e("", "Main screen " + screen);
////
////                } else if (!TextUtils.isEmpty(product_listing)) {
////
////                    bundle.putString(SCREEN_NAME, SCREEN_PRODUCT_LIST);
////                    bundle.putString(SCREEN_MESSAGE, "" + product_listing);
////
////                    screen = bundle.getString(SCREEN_NAME);
////                    LogUtils.e("", "Product Listing " + screen);
////                } else if (!TextUtils.isEmpty(sku)) {
////
////                    bundle.putString(SCREEN_NAME, SCREEN_DETAILS);
////                    bundle.putString(SCREEN_MESSAGE, "" + sku);
////
////                    screen = bundle.getString(SCREEN_NAME);
////                    LogUtils.e("", "Sku " + screen);
////                } else {
////
////                    bundle.putString(SCREEN_NAME, SCREEN_MAIN);
////                    bundle.putString(SCREEN_MESSAGE, message);
////
////                    screen = bundle.getString(SCREEN_NAME);
////                    LogUtils.e("", "Default " + screen);
////                }
////
////                resultIntent.putExtras(bundle);
//
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String timestamp = sdf.format(new Date());
//
//                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
//                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//                }
//
////                NotificationManager mNotificationManager =
////                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////                Log.e("sd", "Url::" + Url);
////                Uri webpage = Uri.parse(Url);
////                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
////
////                PendingIntent resultPendingIntent =
////                        PendingIntent.getActivity(this, 0, intent, 0);
////                NotificationCompat.Builder mBuilder =
////                        new NotificationCompat.Builder(this)
////                                .setSmallIcon(R.drawable.app_icon)
////                                .setContentTitle(title)
////                                .setContentText(message)
////                                .setAutoCancel(true)
////                                .setLights(0xff00ff00, 300, 1000)
////                                .setContentIntent(resultPendingIntent);
////
////                mNotificationManager.notify(123, mBuilder.build());
//
//            } catch (Exception e) {
//                Log.e(TAG, "Exception: " + e.getMessage());
//            }
//        }
    }

//    private void handleNotification(String message) {
//        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//            // app is in foreground, broadcast the push message
//            Intent pushNotification = new Intent(NotificationUtils.PUSH_NOTIFICATION);
//            pushNotification.putExtra("message", message);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//            // play notification sound
//            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//            notificationUtils.playNotificationSound();
//        } else {
//            // If the app is in background, firebase itself handles the notification
//        }
//    }
//
//    private void handleDataMessage(JSONObject json) {
//        Log.e(TAG, "push json: " + json.toString());
//
//        try {
//            JSONObject data = json.getJSONObject("data");
//
//            String title = data.getString("title");
//            String message = data.getString("message");
//            boolean isBackground = data.getBoolean("is_background");
//            String imageUrl = data.getString("image");
//            String timestamp = data.getString("timestamp");
//            JSONObject payload = data.getJSONObject("payload");
//
//            Log.e(TAG, "title: " + title);
//            Log.e(TAG, "message: " + message);
//            Log.e(TAG, "isBackground: " + isBackground);
//            Log.e(TAG, "payload: " + payload.toString());
//            Log.e(TAG, "imageUrl: " + imageUrl);
//            Log.e(TAG, "timestamp: " + timestamp);
//
//
//            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent(NotificationUtils.PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//                // play notification sound
//                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                notificationUtils.playNotificationSound();
//            } else {
//                // app is in background, show the notification in notification tray
//                Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
//                resultIntent.putExtra("message", message);
//
//                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
//                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//                }
//            }
//        } catch (JSONException e) {
//            Log.e(TAG, "Json Exception: " + e.getMessage());
//        } catch (Exception e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Showing notification with text only
//     */
//    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
//    }
//
//    /**
//     * Showing notification with text and image
//     */
//    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
//    }
}
