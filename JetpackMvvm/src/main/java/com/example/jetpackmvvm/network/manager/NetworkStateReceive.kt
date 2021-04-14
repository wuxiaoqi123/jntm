package com.example.jetpackmvvm.network.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.example.jetpackmvvm.network.NetworkUtil

class NetworkStateReceive : BroadcastReceiver() {

    var isInit = true

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            if (!isInit) {
                if (!NetworkUtil.isNetworkAvailable(context)) {
                    NetworkStateManager.instance.mNetworkStateCallback.value?.let {
                        if (it.isSuccess) {
                            NetworkStateManager.instance.mNetworkStateCallback.value =
                                NetState(isSuccess = false)
                        }
                        return
                    }
                    NetworkStateManager.instance.mNetworkStateCallback.value = NetState(false)
                } else {
                    NetworkStateManager.instance.mNetworkStateCallback.value?.let {
                        if (!it.isSuccess) {
                            NetworkStateManager.instance.mNetworkStateCallback.value =
                                NetState(isSuccess = true)
                            return;
                        }
                    }
                    NetworkStateManager.instance.mNetworkStateCallback.value =
                        NetState(isSuccess = true)
                }
            }
            isInit = false
        }
    }
}