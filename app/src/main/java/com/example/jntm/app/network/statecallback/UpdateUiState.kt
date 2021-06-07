package com.example.jntm.app.network.statecallback

data class UpdateUiState<T>(
    var isSuccess: Boolean = true,
    var data: T? = null,
    var errorMsg: String = ""
)
