package br.com.maiconribeiro.popularmovies.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by maiconwillianribeiro on 18/09/16.
 */
public class Util {

    public static boolean checkConnection(Context ctx) {
        ConnectivityManager conMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null) {
            return false;
        }
        if (!i.isConnected()) {
            return false;
        }
        if (!i.isAvailable()) {
            return false;
        }
        return true;
    }
}
