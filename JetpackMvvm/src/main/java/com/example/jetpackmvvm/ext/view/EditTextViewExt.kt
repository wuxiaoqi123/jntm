package com.example.jetpackmvvm.ext.view

import android.widget.EditText

fun EditText.textString(): String {
    return text.toString()
}