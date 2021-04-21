package com.example.jntm.app.ext

import android.app.Activity
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.example.jntm.R
import com.example.jntm.app.util.SettingUtil


private var loadingDialog: MaterialDialog? = null

fun AppCompatActivity.showLoadingExt(message: String = "请求网络中") {
    if (!this.isFinishing) {
        if (loadingDialog == null) {
            loadingDialog = MaterialDialog(this)
                .cancelable(true)
                .cancelOnTouchOutside(false)
                .cornerRadius(12f)
                .customView(R.layout.layout_custom_progress_dialog_view)
                .lifecycleOwner(this)
            loadingDialog?.getCustomView()?.run {
                findViewById<TextView>(R.id.loading_tips).text = message
                findViewById<ProgressBar>(R.id.progressBar).indeterminateTintList =
                    SettingUtil.getOneColorStateList(this@showLoadingExt)
            }
        }
        loadingDialog?.show()
    }
}

fun AppCompatActivity.dismissLoadingExt() {
    loadingDialog?.dismiss()
    loadingDialog = null
}

