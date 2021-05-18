package com.example.jetpackmvvm.callback.databinding

import androidx.databinding.ObservableField

class BooleanObservableField(value: Boolean = false) : ObservableField<Boolean>(value) {

    override fun get(): Boolean {
        return super.get()!!
    }
}