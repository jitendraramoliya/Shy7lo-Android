package shy7lo.com.shy7lo.notification;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import shy7lo.com.shy7lo.R;

/**
 * Created by Jiten on 20-11-2017.
 */

public class MyNotificationExtenderService extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                // Sets the background notification color to Red on Android 5.0+ devices.
                Resources resources = getResources();
                String mLargename = resources.getResourceEntryName(R.drawable.ic_onesignal_large_icon_default);
                String mSmallname = resources.getResourceEntryName(R.drawable.ic_stat_onesignal_default);
                int mLargeResId = resources.getIdentifier(mLargename, "drawable", getPackageName());
                int mSmallResId = resources.getIdentifier(mSmallname, "drawable", getPackageName());

                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        mLargeResId);
                builder.setLargeIcon(icon);
                builder.setSmallIcon(mSmallResId);

//                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
//                        R.drawable.ic_onesignal_large_icon_default);
//                builder.setLargeIcon(icon);
//                builder.setSmallIcon(R.drawable.ic_stat_onesignal_default);
                return builder;
            }
        };

        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

        return true;
    }
}
