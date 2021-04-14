package com.example.jntm.app.weight.loadCallBack

import android.content.Context
import android.view.View
import com.example.jntm.R

class LoadingCallback : com.kingja.loadsir.callback.Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_loading
    }

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return true;
    }
}