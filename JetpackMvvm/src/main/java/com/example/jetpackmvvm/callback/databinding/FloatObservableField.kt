package com.example.jetpackmvvm.callback.databinding

import androidx.databinding.ObservableField

class FloatObservableField(value: Float = 0f) : ObservableField<Float>(value) {

    override fun get(): Float {
        return super.get()!!
    }
}