package com.example.jntm.app.ext

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.jetpackmvvm.base.appContext
import com.example.jetpackmvvm.ext.util.toHtml
import com.example.jntm.R
import com.example.jntm.app.util.SettingUtil
import com.example.jntm.ui.fragment.home.HomeFragment
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx

fun hideSoftKeyboard(activity: Activity?) {
    activity?.let { act ->
        val view = act.currentFocus
        view?.let {
            val inputMethodManager =
                act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}

fun BottomNavigationViewEx.init(navigationItemSelectedAction: (Int) -> Unit): BottomNavigationViewEx {
    enableAnimation(true)
    enableShiftingMode(false)
    enableItemShiftingMode(true)
    itemIconTintList = SettingUtil.getColorStateList(SettingUtil.getColor(appContext))
    itemTextColor = SettingUtil.getColorStateList(appContext)
    setTextSize(12F)
    setOnNavigationItemSelectedListener {
        navigationItemSelectedAction.invoke(it.itemId)
        true
    }
    return this
}

fun ViewPager2.initMain(fragment: Fragment): ViewPager2 {
    this.isUserInputEnabled = false
    this.offscreenPageLimit = 5

    adapter = object : FragmentStateAdapter(fragment) {

        override fun getItemCount() = 5

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0, 1, 2, 3, 4 -> return HomeFragment()
                else -> return HomeFragment()
            }
        }

    }
    return this
}

fun BottomNavigationViewEx.interceptLongClick(vararg ids: Int) {
    val bottomNavigationMenuView: ViewGroup = (this.getChildAt(0) as ViewGroup)
    for (index in ids.indices) {
        bottomNavigationMenuView.getChildAt(index).findViewById<View>(ids[index])
            .setOnLongClickListener {
                true
            }
    }
}

fun Toolbar.initClose(
    titleStr: String = "",
    backImg: Int = R.drawable.ic_back,
    onBack: (toolbar: Toolbar) -> Unit
): Toolbar {
    setBackgroundColor(SettingUtil.getColor(appContext))
    title = titleStr.toHtml()
    setNavigationIcon(backImg)
    setNavigationOnClickListener { onBack.invoke(this) }
    return this
}

fun BaseQuickAdapter<*, *>.setAdapterAnimation(mode: Int) {
    if (mode == 0) {
        this.animationEnable = false
    } else {
        this.animationEnable = true
        this.setAnimationWithDefault(BaseQuickAdapter.AnimationType.values()[mode - 1])
    }
}