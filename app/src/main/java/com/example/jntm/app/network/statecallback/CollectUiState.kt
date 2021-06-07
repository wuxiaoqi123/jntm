package com.example.jntm.app.network.statecallback

data class CollectUiState(
    var isSuccess: Boolean = true,
    var collect: Boolean = false,
    var id: Int = -1,
    var errorMsg: String = ""
)