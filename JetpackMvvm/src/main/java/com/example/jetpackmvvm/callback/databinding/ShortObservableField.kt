package com.example.jetpackmvvm.callback.databinding

import androidx.databinding.ObservableField

class ShortObservableField(value: Short = 0) : ObservableField<Short>(value) {

    override fun get(): Short {
        return super.get()!!
    }
}