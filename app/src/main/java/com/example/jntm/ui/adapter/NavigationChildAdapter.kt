package com.example.jntm.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.jetpackmvvm.ext.util.toHtml
import com.example.jntm.R
import com.example.jntm.app.ext.setAdapterAnimation
import com.example.jntm.app.util.ColorUtil
import com.example.jntm.app.util.SettingUtil
import com.example.jntm.data.model.bean.AriticleResponse

class NavigationChildAdapter(data: ArrayList<AriticleResponse>) :
    BaseQuickAdapter<AriticleResponse, BaseViewHolder>(R.layout.flow_layout, data) {

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: AriticleResponse) {
        holder.setText(R.id.flow_tag, item.title.toHtml())
        holder.setTextColor(R.id.flow_tag, ColorUtil.randomColor())
    }
}