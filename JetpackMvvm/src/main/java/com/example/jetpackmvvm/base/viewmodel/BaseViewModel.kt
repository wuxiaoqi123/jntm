package com.example.jetpackmvvm.base.viewmodel

import androidx.lifecycle.ViewModel
import com.example.jetpackmvvm.callback.livedata.event.EventLiveData

open class BaseViewModel : ViewModel() {

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    inner class UiLoadingChange {
        val showDialog by lazy { EventLiveData<String>() }

        val dismissDialog by lazy { EventLiveData<Boolean>() }
    }
}