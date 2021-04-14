package com.example.jntm.app.event

import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.callback.livedata.event.EventLiveData
import com.example.jntm.app.util.CacheUtil
import com.example.jntm.app.util.SettingUtil
import com.example.jntm.data.model.bean.UserInfo
import com.kunminx.architecture.ui.callback.UnPeekLiveData

class AppViewModel : BaseViewModel() {

    var userInfo = UnPeekLiveData.Builder<UserInfo>().setAllowNullValue(true).create()

    var appColor = EventLiveData<Int>()

    var appAnimation = EventLiveData<Int>()

    init {
        userInfo.value = CacheUtil.getUser()
        appColor.value = SettingUtil.getColor()

    }
}