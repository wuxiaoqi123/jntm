package com.example.jntm.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CollectResponse(
    var chapterId: Int,
    var author: String,
    var chapterName: String,
    var courseId: Int,
    var desc: String,
    var envelopePic: String,
    var id: Int,
    var link: String,
    var niceData: String,
    var origin: String,
    var originId: String,
    var publishTime: Long,
    var title: String,
    var userId: Int,
    var visible: Int,
    var zan: Int
) : Parcelable