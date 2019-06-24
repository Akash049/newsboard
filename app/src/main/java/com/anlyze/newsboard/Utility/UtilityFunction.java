package com.anlyze.newsboard.Utility;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by akashchandra on 5/28/19.
 */

public class UtilityFunction {


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}
