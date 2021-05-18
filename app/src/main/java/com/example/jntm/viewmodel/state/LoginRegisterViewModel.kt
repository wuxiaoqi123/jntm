package com.example.jntm.viewmodel.state

import android.view.View
import androidx.databinding.ObservableInt
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.callback.databinding.BooleanObservableField
import com.example.jetpackmvvm.callback.databinding.StringObservableField

class LoginRegisterViewModel : BaseViewModel() {

    var username = StringObservableField()

    var password = StringObservableField()

    var password2 = StringObservableField()

    var isShowPwd = BooleanObservableField()

    var isShowPwd2 = BooleanObservableField()

    var clearVisible = object : ObservableInt(username) {
        override fun get(): Int {
            return if (username.get().isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    var passwordVisible = object : ObservableInt(password) {
        override fun get(): Int {
            return if (password.get().isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }
    var passwordVisible2 = object : ObservableInt(password2) {
        override fun get(): Int {
            return if (password2.get().isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }
}