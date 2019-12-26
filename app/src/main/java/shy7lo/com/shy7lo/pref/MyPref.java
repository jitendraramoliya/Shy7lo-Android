package shy7lo.com.shy7lo.pref;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Set;

import shy7lo.com.shy7lo.application.Shy7lo;

/**
 * Created by JitenRamen on 27-08-2016.
 */
public class MyPref {

    public static final String GUEST_CART_ID = "GUEST_CART_ID";
    public static final String GUEST_EMAIL = "GUEST_EMAIL";
    public static final String USER_CART_ID = "USER_CART_ID";
    public static final String USER_ID = "USER_ID";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PWD = "USER_PWD";
    public static final String USER_FIRSTNAME = "USER_FIRSTNAME";
    public static final String USER_LASTNAME = "USER_LASTNAME";
    public static final String USER_NEIGHBERHOOD = "USER_NEIGHBERHOOD";
    public static final String USER_PHONE = "USER_PHONE";
    public static final String USER_ADDRESS = "USER_ADDRESS";
    public static final String USER_CITY = "USER_CITY";
    public static final String USER_REGION = "USER_REGION";
    public static final String USER_COUPON_CODE = "USER_COUPON_CODE";

    public static final String WEB_CACHE_VERSION = "WEB_CACHE_VERSION";

    public static final String CURRENCY_LIST = "CURRENCY_LIST";
    public static final String TOTAL_AMT_LIST = "TOTAL_AMT_LIST";

    public static final String CART_ITEM_COUNT = "CART_ITEM_COUNT";
    public static final String WISHLIST_ITEM_COUNT = "WISHLIST_ITEM_COUNT";
    public static final String IS_FIRST_TIME = "IS_FIRST_TIME";
    public static final String IS_LANGUAGE_SELECT = "IS_LANGUAGE_SELECT";
    public static final String APP_VERSION = "APP_VERSION";
    public static final String APP_TUNE = "APP_TUNE";
    public static final String APP_FABRIC = "APP_FABRIC";
    public static final String APP_ONE_SIGNAL = "APP_ONE_SIGNAL";
    public static final String APP_FIREBASE = "APP_FIREBASE";
    public static final String APP_CRITEO = "APP_CRITEO";
    public static final String APP_ADJUST = "APP_ADJUST";
    public static final String LAST_UPGRADE_DATE = "LAST_UPGRADE_DATE";
    public static final String ADJUST_GOOGLE_ID = "ADJUST_GOOGLE_ID";

    public static final String DEFAULT_COUNTRY_CODE = "DEFAULT_COUNTRY_CODE";
    public static final String DEFAULT_CURRENCY_CODE = "DEFAULT_CURRENCY_CODE";
    public static final String DEFAULT_ENGLISH_CURRENCY_CODE = "DEFAULT_ENGLISH_CURRENCY_CODE";
    public static final String DEFAULT_ARABIC_CURRENCY_CODE = "DEFAULT_ARABIC_CURRENCY_CODE";
    public static final String DEFAULT_COUNTRY_ID = "DEFAULT_COUNTRY_ID";
    public static final String DEFAULT_EXCHANGE_RATE = "DEFAULT_EXCHANGE_RATE";
    public static final String DEFAULT_COUNTRY_POSITION = "DEFAULT_COUNTRY_POSITION";

    public static final String DEFAULT_CATEGORY_POSITION = "DEFAULT_CATEGORY_POSITION";

    public static final String ONE_SIGNAL_IS_REGISTERED = "ONE_SIGNAL_IS_REGISTERED";
    public static final String ONE_SIGNAL_IS_SUBCRIBED = "ONE_SIGNAL_IS_SUBCRIBED";
    public static final String ONE_SIGNAL_USER_ID = "ONE_SIGNAL_USER_ID";
    public static final String ONE_SIGNAL_PUSH_TOKEN = "ONE_SIGNAL_PUSH_TOKEN";

    public static final String IS_DIRECT_WEBVIEW = "IS_DIRECT_WEBVIEW";

    public static String DEFAULT_SIZE = "DEFAULT_SIZE";

    public static String PREF_ONE_SIGNAL_IS_SUBCRIBED = "PREF_ONE_SIGNAL_IS_SUBCRIBED";
    public static String DEFAULT_LANGUAGE = "DEFAULT_LANGUAGE";
    public static String LANGUAGE_English = "LANGUAGE_English";
    public static String LANGUAGE_Arabic = "LANGUAGE_Arabic";

    public static String DEFAULT_LANGUAGE_CODE = "DEFAULT_LANGUAGE_CODE";
    public static String LANGUAGE_EN = "en";
    public static String LANGUAGE_AR = "ar";

    public static void setPref(Context mContext, String key, String value) {
        if (mContext == null) {
            mContext = Shy7lo.getAppContext();
        }
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(mContext).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setPref(Context mContext, String key, Boolean value) {
        if (mContext == null) {
            mContext = Shy7lo.getAppContext();
        }
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(mContext).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setPref(Context mContext, String key, Float value) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(mContext).edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void setPref(Context mContext, String key, int value) {
        if (mContext == null) {
            mContext = Shy7lo.getAppContext();
        }
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(mContext).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setPref(Context mContext, String key, long value) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(mContext).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void setPref(Context mContext, String key,
                               Set<String> valueSet) {
        if (mContext == null) {
            mContext = Shy7lo.getAppContext();
        }
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(mContext).edit();
        editor.putStringSet(key, valueSet);
        editor.commit();
    }

    public static String getPref(Context mContext, String key, String defValue) {
        if (mContext == null) {
            mContext = Shy7lo.getAppContext();
        }
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(key, defValue);
    }

    public static boolean getPref(Context mContext, String key, boolean defValue) {
        if (mContext == null) {
            mContext = Shy7lo.getAppContext();
        }
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getBoolean(key, defValue);
    }

    public static float getPref(Context mContext, String key, float defValue) {
        if (mContext == null) {
            mContext = Shy7lo.getAppContext();
        }
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getFloat(key, defValue);
    }

    public static int getPref(Context mContext, String key, int defValue) {
        if (mContext == null) {
            mContext = Shy7lo.getAppContext();
        }
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt(
                key, defValue);
    }

    public static long getPref(Context mContext, String key, long defValue) {
        if (mContext == null) {
            mContext = Shy7lo.getAppContext();
        }
        return PreferenceManager.getDefaultSharedPreferences(mContext).getLong(
                key, defValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Set<String> getPref(Context mContext, String key,
                                      Set<String> defValueSet) {
        if (mContext == null) {
            mContext = Shy7lo.getAppContext();
        }
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getStringSet(key, defValueSet);
    }
}

