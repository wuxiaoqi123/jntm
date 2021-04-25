package com.example.jntm

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ToastUtils
import com.example.jetpackmvvm.network.manager.NetState
import com.example.jntm.app.appViewModel
import com.example.jntm.app.base.BaseActivity
import com.example.jntm.app.util.StatusBarUtil
import com.example.jntm.databinding.ActivityMainBinding
import com.example.jntm.viewmodel.state.MainViewModel
import com.tencent.bugly.beta.Beta

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    var exitTime = 0L

    override fun layoutId() = R.layout.activity_main

    override fun initView(saveInstanceState: Bundle?) {
        Beta.checkUpgrade(false, false)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nav = Navigation.findNavController(this@MainActivity, R.id.host_fragment)
                if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.mainfragment) {
                    nav.navigateUp()
                } else {
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        ToastUtils.showShort("再按一次退出程序")
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
            }
        })

        appViewModel.appColor.value?.let {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
            StatusBarUtil.setColor(this, it, 0)
        }
    }

    override fun createObserver() {
        appViewModel.appColor.observeInActivity(this, {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
            StatusBarUtil.setColor(this, it, 0)
        })
    }

    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        if (netState.isSuccess) {
            Toast.makeText(applicationContext, "我特么终于有网了啊!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "我特么怎么断网了!", Toast.LENGTH_SHORT).show()
        }
    }
}