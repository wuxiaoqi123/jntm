package com.example.jetpackmvvm.base

import android.app.Application
import android.content.ContentProvider

val appContext: Application by lazy { Ktx.app }

class Ktx : ContentProvider() {

    companion object {
        lateinit var app:Application
    }
}