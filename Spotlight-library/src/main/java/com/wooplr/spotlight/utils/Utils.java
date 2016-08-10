package com.wooplr.spotlight.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * @author jitender on 10/06/16
 */
@SuppressWarnings("unused")
public class Utils {

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Calculated the px value to the given dip value.
     *
     * @param context Context required to calculate the new value
     * @param dip     the dip value to convert into pixel
     * @return the pixel value for the given dip value
     */
    public static float dipToPx(Context context, int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
    }
}
