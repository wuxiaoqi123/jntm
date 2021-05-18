package com.example.jetpackmvvm.ext

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.jetpackmvvm.navigation.NavHostFragment

fun Fragment.nav(): NavController {
    return NavHostFragment.findNavController(this)
}

fun nav(view: View): NavController {
    return Navigation.findNavController(view)
}

var lastNavTime = 0L

fun NavController.navigateAction(resId: Int, bundle: Bundle? = null, interval: Long = 500) {
    val currentTime = System.currentTimeMillis()
    if (currentTime >= lastNavTime + interval) {
        lastNavTime = currentTime
        try {
            navigate(resId, bundle)
        } catch (e: Exception) {

        }
    }
}