package com.example.jntm.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.example.jntm.app.util.DatetimeUtil
import kotlinx.android.parcel.Parcelize

/**
 * 项目分类
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class TodoResponse(
    var completeDate: Long,
    var completeDateStr: String,
    var content: String,
    var date: Long,
    var dateStr: String,
    var id: Int,
    var priority: Int,
    var status: Int,
    var title: String,
    var type: Int,
    var userId: Int
) : Parcelable {
    fun isDone(): Boolean {
        //判断是否已完成或者已过期
        return if (status == 1) {
            true
        } else {
            DatetimeUtil.nows.time > DatetimeUtil.formatDate(
                DatetimeUtil.DATE_PATTERN,
                dateStr
            ).time
        }
    }
}
