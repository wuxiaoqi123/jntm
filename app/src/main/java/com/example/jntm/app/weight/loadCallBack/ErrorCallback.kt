package com.example.jntm.app.weight.loadCallBack

import com.example.jntm.R

class ErrorCallback : com.kingja.loadsir.callback.Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_error
    }
}