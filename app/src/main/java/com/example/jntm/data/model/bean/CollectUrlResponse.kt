package com.example.jntm.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CollectUrlResponse(
    var icon: String,
    var link: String,
    var name: String,
    var order: Int,
    var userId: Int,
    var visible: Int
) : Parcelable