package com.example.jetpackmvvm.ext

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.jetpackmvvm.base.activity.BaseVmActivity
import com.example.jetpackmvvm.base.fragment.BaseVmFragment
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.ext.util.loge
import com.example.jetpackmvvm.network.AppException
import com.example.jetpackmvvm.network.BaseResponse
import com.example.jetpackmvvm.network.ExceptionHandle
import com.example.jetpackmvvm.state.ResultState
import com.example.jetpackmvvm.state.paresException
import com.example.jetpackmvvm.state.paresResult
import kotlinx.coroutines.*

fun <T> BaseVmActivity<*>.parseState(
    resultState: ResultState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.run { this }
        }
        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }
        }
    }
}

fun <T> BaseVmFragment<*>.parseState(
    resultState: ResultState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.invoke()
        }
        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }
        }
    }
}

fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    resultState: MutableLiveData<ResultState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return viewModelScope.launch {
        kotlin.runCatching {
            if (isShowDialog) resultState.value = ResultState.onAppLoading(loadingMessage)
            block()
        }.onSuccess {
            resultState.paresResult(it)
        }.onFailure {
            it.message?.loge()
            resultState.paresException(it)
        }
    }
}

fun <T> BaseViewModel.requestNoCheck(
    block: suspend () -> T,
    resultState: MutableLiveData<ResultState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return viewModelScope.launch {
        kotlin.runCatching {
            if (isShowDialog) resultState.value = ResultState.onAppLoading(loadingMessage)
            block()
        }.onSuccess {
            resultState.paresResult(it)
        }.onFailure {
            it.message?.loge()
            resultState.paresException(it)
        }
    }
}

fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return viewModelScope.launch {
        kotlin.runCatching {
            if (isShowDialog) loadingChange.showDialog.postValue(loadingMessage)
            block()
        }.onSuccess {
            loadingChange.dismissDialog.postValue(false)
            kotlin.runCatching {
                executeResponse(it) { t ->
                    success(t)
                }
            }.onFailure { e ->
                e.message?.loge()
                error(ExceptionHandle.handleException(e))
            }
        }.onFailure {
            loadingChange.dismissDialog.postValue(false)
            it.message?.loge()
            error(ExceptionHandle.handleException(it))
        }
    }
}

fun <T> BaseViewModel.requestNoCheck(
    block: suspend () -> T,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    if (isShowDialog) loadingChange.showDialog.postValue(loadingMessage)
    return viewModelScope.launch {
        kotlin.runCatching {
            block()
        }.onSuccess {
            loadingChange.dismissDialog.postValue(false)
            success(it)
        }.onFailure {
            loadingChange.dismissDialog.postValue(false)
            it.message?.loge()
            error(ExceptionHandle.handleException(it))
        }
    }
}

suspend fun <T> executeResponse(
    response: BaseResponse<T>,
    success: suspend CoroutineScope.(T) -> Unit
) {
    coroutineScope {
        when {
            response.isSucces() -> {
                success(response.getResponseData())
            }
            else -> {
                throw AppException(
                    response.getResponseCode(),
                    response.getResponseMsg(),
                    response.getResponseMsg()
                )
            }
        }
    }
}

fun <T> BaseViewModel.launch(
    block: () -> T,
    success: (T) -> Unit,
    error: (Throwable) -> Unit = {}
) {
    viewModelScope.launch {
        runCatching {
            withContext(Dispatchers.IO) {
                block()
            }
        }.onSuccess {
            success(it)
        }.onFailure {
            error(it)
        }
    }
}