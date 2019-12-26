package shy7lo.com.shy7lo.typeface;

import android.content.Context;
import android.graphics.Typeface;

public class MyTypeFace {

    public static Typeface RalewayBold, RalewayExtraBold, RalewayExtraLight, RalewayHeavy, RalewayLight,
            RalewayMedium, RalewayRegular, RalewaySemiBold, RalewayThin, DroidKufiBold, DroidKufiRegular,
            DroidNaskhBold, DroidNaskhRegular;

    public static Typeface SFUIDisplayBold, SFUIDisplaySemibold, SFUIDisplayRegular, TahomaBold, TahomaRegular, RobotoMedium;

    public static Typeface RalewayBold(Context context) {
        if (RalewayBold == null) {
            RalewayBold = Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf");
        }
        return RalewayBold;
    }

    public static Typeface RalewayExtraBold(Context context) {
        if (RalewayExtraBold == null) {
            RalewayExtraBold = Typeface.createFromAsset(context.getAssets(), "Raleway-ExtraBold.ttf");
        }
        return RalewayExtraBold;
    }

    public static Typeface RalewayExtraLight(Context context) {
        if (RalewayExtraLight == null) {
            RalewayExtraLight = Typeface.createFromAsset(context.getAssets(), "Raleway-ExtraLight.ttf");
        }
        return RalewayExtraLight;
    }

    public static Typeface RalewayHeavy(Context context) {
        if (RalewayHeavy == null) {
            RalewayHeavy = Typeface.createFromAsset(context.getAssets(), "Raleway-Heavy.ttf");
        }
        return RalewayHeavy;
    }

    public static Typeface RalewayLight(Context context) {
        if (RalewayLight == null) {
            RalewayLight = Typeface.createFromAsset(context.getAssets(), "Raleway-Light.ttf");
        }
        return RalewayLight;
    }

    public static Typeface RalewayMedium(Context context) {
        if (RalewayMedium == null) {
            RalewayMedium = Typeface.createFromAsset(context.getAssets(), "Raleway-Medium.ttf");
        }
        return RalewayMedium;
    }

    public static Typeface RalewayRegular(Context context) {
        if (RalewayMedium == null) {
            RalewayMedium = Typeface.createFromAsset(context.getAssets(), "Raleway-Regular.ttf");
        }
        return RalewayMedium;
    }

    public static Typeface RalewaySemiBold(Context context) {
        if (RalewaySemiBold == null) {
            RalewaySemiBold = Typeface.createFromAsset(context.getAssets(), "Raleway-SemiBold.ttf");
        }
        return RalewaySemiBold;
    }

    public static Typeface RalewayThin(Context context) {
        if (RalewayThin == null) {
            RalewayThin = Typeface.createFromAsset(context.getAssets(), "Raleway-Thin.ttf");
        }
        return RalewayThin;
    }

    public static Typeface DroidKufiBold(Context context) {
        if (DroidKufiBold == null) {
            DroidKufiBold = Typeface.createFromAsset(context.getAssets(), "DroidKufi-Bold.ttf");
        }
        return DroidKufiBold;
    }

    public static Typeface DroidKufiRegular(Context context) {
        if (DroidKufiRegular == null) {
            DroidKufiRegular = Typeface.createFromAsset(context.getAssets(), "DroidKufi-Regular.ttf");
        }
        return DroidKufiRegular;
    }

    public static Typeface DroidNaskhBold(Context context) {
        if (DroidNaskhBold == null) {
            DroidNaskhBold = Typeface.createFromAsset(context.getAssets(), "DroidNaskh-Bold.ttf");
        }
        return DroidNaskhBold;
    }

    public static Typeface DroidNaskhRegular(Context context) {
        if (DroidNaskhRegular == null) {
            return Typeface.createFromAsset(context.getAssets(), "DroidNaskh-Regular.ttf");
        }
        return DroidNaskhRegular;
    }

    public static Typeface TahomaBold(Context context) {
        if (TahomaBold == null) {
            TahomaBold = Typeface.createFromAsset(context.getAssets(), "TahomaBold.TTF");
            return TahomaBold;
        }
        return TahomaBold;
    }

    public static Typeface TahomaRegular(Context context) {
        if (TahomaRegular == null) {
            TahomaRegular = Typeface.createFromAsset(context.getAssets(), "TahomaRegular.ttf");
            return TahomaRegular;
        }
        return TahomaRegular;
    }

    public static Typeface SFUIDisplayBold(Context context) {
        if (SFUIDisplayBold == null) {
            SFUIDisplayBold = Typeface.createFromAsset(context.getAssets(), "SFUIDisplayBold.otf");
            return SFUIDisplayBold;
        }
        return SFUIDisplayBold;
    }


    public static Typeface SFUIDisplaySemibold(Context context) {
        if (SFUIDisplaySemibold == null) {
            SFUIDisplaySemibold = Typeface.createFromAsset(context.getAssets(), "SFUIDisplaySemibold.otf");
            return SFUIDisplaySemibold;
        }
        return SFUIDisplaySemibold;
    }

    public static Typeface SFUIDisplayRegular(Context context) {
        if (SFUIDisplayRegular == null) {
            SFUIDisplayRegular = Typeface.createFromAsset(context.getAssets(), "SFUIDisplayRegular.otf");
            return SFUIDisplayRegular;
        }
        return SFUIDisplayRegular;
    }

    public static Typeface RobotoMedium(Context context) {
        if (RobotoMedium == null) {
            RobotoMedium = Typeface.createFromAsset(context.getAssets(), "RobotoMedium.ttf");
            return RobotoMedium;
        }
        return RobotoMedium;
    }

}
