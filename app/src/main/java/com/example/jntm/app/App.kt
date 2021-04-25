package com.example.jntm.app

import android.content.Context
import androidx.multidex.MultiDex
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.example.jetpackmvvm.base.BaseApp
import com.example.jetpackmvvm.ext.util.jetpackMvvmLog
import com.example.jntm.BuildConfig
import com.example.jntm.app.event.AppViewModel
import com.example.jntm.app.event.EventViewModel
import com.example.jntm.app.weight.loadCallBack.EmptyCallback
import com.example.jntm.app.weight.loadCallBack.ErrorCallback
import com.example.jntm.app.weight.loadCallBack.LoadingCallback
import com.example.jntm.ui.activity.WelcomeActivity
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV

val appViewModel: AppViewModel by lazy { App.appViewModelInstance }

val eventViewModel: EventViewModel by lazy { App.eventViewModelInstance }

class App : BaseApp() {

    companion object {
        lateinit var instance: App
        lateinit var eventViewModelInstance: EventViewModel
        lateinit var appViewModelInstance: AppViewModel
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (!isMainProcess()) return
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")
        instance = this
        eventViewModelInstance = getAppViewModelProvider().get(EventViewModel::class.java)
        appViewModelInstance = getAppViewModelProvider().get(AppViewModel::class.java)

        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())
            .addCallback(ErrorCallback())
            .addCallback(EmptyCallback())
            .setDefaultCallback(SuccessCallback::class.java)
            .commit()

        val context = applicationContext
        val packageName = context.packageName
        val processName = com.example.jntm.app.ext.getProcessName(android.os.Process.myPid())
        val strategy = CrashReport.UserStrategy(context)
        strategy.isUploadProcess = processName == null || processName == packageName
        Bugly.init(context, if (BuildConfig.DEBUG) "xxx" else "a52f2b5ebb", BuildConfig.DEBUG)
        jetpackMvvmLog = BuildConfig.DEBUG

        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT)
            .enabled(false)
            .showErrorDetails(false)
            .showRestartButton(false)
            .logErrorOnRestart(false)
            .trackActivities(true)
            .minTimeBetweenCrashesMs(2000)
            .restartActivity(WelcomeActivity::class.java)
            .apply()
    }
}