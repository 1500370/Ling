package ling.testapp.ui.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;

import ling.testapp.ui.object.LApplication;
import ling.testapp.ui.object.LUtilityManager;

/**
 * 用來接收WifiManager發出的廣播，並判斷是否可以連接到網路
 * Created by jlchen on 2016/10/20.
 */

public class LWifiConnectionStateReceiver extends BroadcastReceiver {

    public LWifiConnectionStateReceiver(Callback callback){
        this.m_callback = callback;
    }

    public interface Callback{
        void onNetworkConnect();

        void onNetworkDisconnect();
    }

    private Callback m_callback = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        //網路狀態改變
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){

            final boolean isConnection
                    = LUtilityManager.getInstance(LApplication.getContext()).bIsNetworkAvailable();

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    //網路連線
                    if(true == isConnection) {
                        m_callback.onNetworkConnect();
                    } else {
                        m_callback.onNetworkDisconnect();
                    }
                }
            });
        }
    }
}