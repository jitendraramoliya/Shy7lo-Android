package shy7lo.com.shy7lo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class IntentHandler {

    public static void startActivity(Context mContext, Class<?> mClassName) {
        Intent next = new Intent(mContext, mClassName);
        mContext.startActivity(next);
    }

    public static void startActivity(Context mContext, Class<?> mClassName,
                                     Bundle mBundle) {
        Intent next = new Intent(mContext, mClassName);
        next.putExtras(mBundle);
        mContext.startActivity(next);
    }

    public static void startActivity(Context mContext, Class<?> mClassName, int code) {
        Intent next = new Intent(mContext, mClassName);
//        mContext.startActivity(next);
        ((Activity) mContext).startActivityForResult(next, code);
    }

    public static void startActivityForResult(Context mContext, Class<?> mClassName,
                                              Bundle mBundle, int code) {
        Intent next = new Intent(mContext, mClassName);
        next.putExtras(mBundle);
        ((Activity) mContext).startActivityForResult(next, code);
    }

    public static void startActivityReorderFront(Context activity,
                                                 Class<?> activityClass) {
        Intent next = new Intent(activity, activityClass);
        next.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(next);
    }

    public static void startActivityReorderFront(Context activity,
                                                 Class<?> activityClass, Bundle bundle) {
        Intent next = new Intent(activity, activityClass);
        next.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        next.putExtras(bundle);
        activity.startActivity(next);
    }

    public static void startActivityNewTask(Context activity,
                                            Class<?> activityClass) {
        Intent next = new Intent(activity, activityClass);
        next.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(next);
    }

    public static void startActivityNewTask(Context activity,
                                            Class<?> activityClass, Bundle bundle) {
        Intent next = new Intent(activity, activityClass);
        next.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        next.putExtras(bundle);
        activity.startActivity(next);
    }

    public static void startActivityClearTop(Context activity,
                                             Class<?> activityClass) {
        Intent next = new Intent(activity, activityClass);
        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(next);
    }

    public static void startActivityClearTop(Context activity,
                                             Class<?> activityClass, Bundle bundle) {
        Intent next = new Intent(activity, activityClass);
        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        next.putExtras(bundle);
        activity.startActivity(next);
    }

    // service

    public static void startService(Context context, Class<?> serviceClass) {
        Intent next = new Intent(context, serviceClass);
        context.startService(next);
    }

    public static void startForgroundService(Context context, Class<?> serviceClass) {
        Intent next = new Intent(context, serviceClass);
        context.startForegroundService(next);
    }

    public static void stopService(Context context, Class<?> serviceClass) {
        Intent next = new Intent(context, serviceClass);
        context.stopService(next);
    }

    public static void startService(Context context, Class<?> serviceClass,
                                    Bundle bundle) {
        Intent next = new Intent(context, serviceClass);
        next.putExtras(bundle);
        context.startService(next);
    }

//	public static void startActivity(Context mContext, Class<?> mClassName,
//			String bndlDeliveryTimeList, DeliveryTimePojo mDeliveryTimePojo) {
//		Gson gson = new Gson();
//		Intent next = new Intent(mContext, mClassName);
//		next.putExtra(bndlDeliveryTimeList, gson.toJson(mDeliveryTimePojo));
//		mContext.startActivity(next);
//	}

    // public static void startActivity(Context mContext, Class<?> mClassName,
    // String bndlSelectedNotice, NoticesData mNoticesData) {
    //
    // Gson gson = new Gson();
    // Intent next = new Intent(mContext, mClassName);
    // next.putExtra(bndlSelectedNotice, gson.toJson(mNoticesData));
    // mContext.startActivity(next);
    //
    // }
}
