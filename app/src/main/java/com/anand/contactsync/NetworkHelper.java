package com.anand.contactsync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

/**
 * Created by RiderRebrn on 7/30/2017.
 */

public class NetworkHelper{
    private static final String TAG = "NetworkHelper=>";
    private Context context;

    public NetworkHelper(Context context){
        this.context = context;
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null & connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
