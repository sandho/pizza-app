package com.san.pizzaapp.utils;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NetworkChangeReceiver(var networkListener: NetworkListener) : BroadcastReceiver() {

    @Override
    override fun onReceive(context: Context?, intent: Intent?) {
        networkListener.isNetWorkEnable(context!!.isNetworkAvailable())
    }


}