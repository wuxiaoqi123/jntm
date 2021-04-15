package com.example.jntm.app.ext

import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

fun getProcessName(pid: Int): String? {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
        var processName = reader.readLine()
        if (!TextUtils.isEmpty(processName)) {
            processName = processName.trim { it <= ' ' }
        }
        return processName
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            reader?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return null
}