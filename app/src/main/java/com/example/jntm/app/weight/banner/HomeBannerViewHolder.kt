package com.example.jntm.app.weight.banner

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.jetpackmvvm.base.appContext
import com.example.jntm.R
import com.example.jntm.data.model.bean.BannerResponse
import com.zhpan.bannerview.BaseViewHolder

class HomeBannerViewHolder(view: View) : BaseViewHolder<BannerResponse>(view) {
    override fun bindData(data: BannerResponse?, position: Int, pageSize: Int) {
        val img = itemView.findViewById<ImageView>(R.id.bannerhome_img)
        data?.let {
            Glide.with(appContext)
                .load(it.imagePath)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(img)
        }
    }

}
