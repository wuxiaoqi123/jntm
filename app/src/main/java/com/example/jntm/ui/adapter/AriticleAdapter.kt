package com.example.jntm.ui.adapter

import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.jetpackmvvm.ext.util.toHtml
import com.example.jntm.R
import com.example.jntm.app.ext.setAdapterAnimation
import com.example.jntm.app.util.SettingUtil
import com.example.jntm.app.weight.customview.CollectView
import com.example.jntm.data.model.bean.AriticleResponse

class AriticleAdapter(data: MutableList<AriticleResponse>?) :
    BaseDelegateMultiAdapter<AriticleResponse, BaseViewHolder>(data) {

    private val Ariticle = 1
    private val Project = 2
    private var showTag = false

    private var collectAction: (item: AriticleResponse, v: CollectView, position: Int) -> Unit =
        { _: AriticleResponse, _: CollectView, _: Int -> }

    constructor(data: MutableList<AriticleResponse>?, showTag: Boolean) : this(data) {
        this.showTag = showTag
    }

    init {
        setAdapterAnimation(SettingUtil.getListMode())
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<AriticleResponse>() {
            override fun getItemType(data: List<AriticleResponse>, position: Int): Int {
                return if (TextUtils.isEmpty(data[position].envelopePic)) Ariticle else Project
            }
        })
        getMultiTypeDelegate()?.let {
            it.addItemType(Ariticle, R.layout.item_ariticle)
            it.addItemType(Project, R.layout.item_project)
        }
    }

    override fun convert(helper: BaseViewHolder, item: AriticleResponse) {
        when (helper.itemViewType) {
            Ariticle -> {
                item.run {
                    helper.setText(
                        R.id.item_home_author,
                        if (author.isNotEmpty()) author else shareUser
                    )
                    helper.setText(R.id.item_home_content, title.toHtml())
                    helper.setText(R.id.item_home_type2, "$superChapterName·$chapterName".toHtml())
                    helper.setText(R.id.item_home_date, niceDate)
                    helper.getView<CollectView>(R.id.item_home_collect).isChecked = collect
                    if (showTag) {
                        helper.setGone(R.id.item_home_new, !fresh)
                        helper.setGone(R.id.item_home_top, type != 1)
                        if (tags.isNotEmpty()) {
                            helper.setGone(R.id.item_home_type1, false)
                            helper.setText(R.id.item_home_type1, tags[0].name)
                        } else {
                            helper.setGone(R.id.item_home_type1, true)
                        }
                    } else {
                        helper.setGone(R.id.item_home_top, true)
                        helper.setGone(R.id.item_home_type1, true)
                        helper.setGone(R.id.item_home_new, true)
                    }
                }
                helper.getView<CollectView>(R.id.item_home_collect)
                    .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            collectAction.invoke(item, v, helper.adapterPosition)
                        }
                    })
            }
            Project -> {
                item.run {
                    helper.setText(
                        R.id.item_project_author,
                        if (author.isNotEmpty()) author else shareUser
                    )
                    helper.setText(R.id.item_project_title, title.toHtml())
                    helper.setText(R.id.item_project_content, desc.toHtml())
                    helper.setText(
                        R.id.item_project_type,
                        "$superChapterName·$chapterName".toHtml()
                    )
                    helper.setText(R.id.item_project_date, niceDate)
                    if (showTag) {
                        //展示标签
                        helper.setGone(R.id.item_project_new, !fresh)
                        helper.setGone(R.id.item_project_top, type != 1)
                        if (tags.isNotEmpty()) {
                            helper.setGone(R.id.item_project_type1, false)
                            helper.setText(R.id.item_project_type1, tags[0].name)
                        } else {
                            helper.setGone(R.id.item_project_type1, true)
                        }
                    } else {
                        //隐藏所有标签
                        helper.setGone(R.id.item_project_top, true)
                        helper.setGone(R.id.item_project_type1, true)
                        helper.setGone(R.id.item_project_new, true)
                    }
                    helper.getView<CollectView>(R.id.item_project_collect).isChecked = collect
                    Glide.with(context).load(envelopePic)
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(helper.getView(R.id.item_project_imageview))
                }
                helper.getView<CollectView>(R.id.item_project_collect)
                    .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            collectAction.invoke(item, v, helper.adapterPosition)
                        }
                    })
            }
        }
    }

    fun setCollectClick(inputCollectAction: (item: AriticleResponse, v: CollectView, position: Int) -> Unit) {
        this.collectAction = inputCollectAction
    }
}