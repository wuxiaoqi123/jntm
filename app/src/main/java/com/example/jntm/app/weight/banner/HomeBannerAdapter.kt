package com.example.jntm.app.weight.banner

import android.view.View
import com.example.jntm.R
import com.example.jntm.data.model.bean.BannerResponse
import com.zhpan.bannerview.BaseBannerAdapter

class HomeBannerAdapter : BaseBannerAdapter<BannerResponse, HomeBannerViewHolder>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_itemhome
    }

    override fun createViewHolder(itemView: View, viewType: Int): HomeBannerViewHolder {
        return HomeBannerViewHolder(itemView);
    }

    override fun onBind(
        holder: HomeBannerViewHolder?,
        data: BannerResponse?,
        position: Int,
        pageSize: Int
    ) {
        holder?.bindData(data, position, pageSize);
    }


}
