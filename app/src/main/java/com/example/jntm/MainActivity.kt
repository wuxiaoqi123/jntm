package com.example.jntm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.jntm.app.base.BaseActivity
import com.example.jntm.databinding.ActivityMainBinding
import com.example.jntm.viewmodel.state.MainViewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun layoutId() = R.layout.activity_main

    override fun initView(saveInstanceState: Bundle?) {

    }
}