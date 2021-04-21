package com.example.jntm.app.weight.banner

import android.view.View
import com.example.jntm.R
import com.zhpan.bannerview.BaseBannerAdapter

class WelcomeBannerAdapter : BaseBannerAdapter<String, WelcomeBannerViewHolder>() {

    override fun getLayoutId(viewType: Int) = R.layout.banner_itemwelcome

    override fun createViewHolder(itemView: View, viewType: Int): WelcomeBannerViewHolder {
        return WelcomeBannerViewHolder(itemView)
    }

    override fun onBind(
        holder: WelcomeBannerViewHolder?,
        data: String?,
        position: Int,
        pageSize: Int
    ) {
        holder?.bindData(data, position, pageSize)
    }
}