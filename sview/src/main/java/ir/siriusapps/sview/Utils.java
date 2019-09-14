package ir.siriusapps.sview;

import android.content.res.Resources;
import android.util.TypedValue;

public class Utils {

    public static float dipToPix(float dp){
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }

    public static int dipToPix(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int spToPix(float sp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                Resources.getSystem().getDisplayMetrics());
    }

}
