package com.example.jetpackmvvm.callback.livedata

import androidx.lifecycle.MutableLiveData

class BooleanLiveData : MutableLiveData<Boolean>() {

    override fun getValue(): Boolean {
        return super.getValue() ?: false
    }
}