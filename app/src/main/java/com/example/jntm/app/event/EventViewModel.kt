package com.example.jntm.app.event

import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.callback.livedata.event.EventLiveData
import com.example.jntm.data.model.bean.CollectBus

class EventViewModel : BaseViewModel() {

    val collectEvent = EventLiveData<CollectBus>()

    val shareArticleEvent = EventLiveData<Boolean>()

    val todoEvent = EventLiveData<Boolean>()
}