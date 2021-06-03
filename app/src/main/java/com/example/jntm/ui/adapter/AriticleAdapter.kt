package com.example.jntm.ui.adapter

import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.jntm.data.model.bean.AriticleResponse

class AriticleAdapter(data: MutableList<AriticleResponse>?) :
    BaseDelegateMultiAdapter<AriticleResponse, BaseViewHolder>(data) {

    private val ariticle = 1
    private val project = 2
    private var showTag = false

    override fun convert(holder: BaseViewHolder, item: AriticleResponse) {

    }
}