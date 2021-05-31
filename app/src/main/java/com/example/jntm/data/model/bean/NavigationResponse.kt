package com.example.jntm.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NavigationResponse(
    var articles: ArrayList<AriticleResponse>,
    var cid: Int,
    var name: String
) : Parcelable