package com.example.kafiesta.utilities.extensions

import androidx.fragment.app.Fragment
import com.example.kafiesta.utilities.extensions.hasInternet
import com.example.kafiesta.utilities.extensions.showToast
import com.example.kafiesta.utilities.extensions.showToastLong

fun Fragment.hasInternet(): Boolean = context!!.hasInternet()

fun Fragment.hasInternet(notifyNoInternet: Boolean = true, trueFunc: (internet: Boolean) -> Unit) =
    context!!.hasInternet(notifyNoInternet, trueFunc)

fun Fragment.hasInternet(
    trueFunc: (internet: Boolean) -> Unit, falseFunc: (internet: Boolean) -> Unit
) = context!!.hasInternet(trueFunc, falseFunc)

fun Fragment.showToast(message: String, length: Int) = context!!.showToast(message, length)
fun Fragment.showToast(message: String) = context!!.showToast(message)
fun Fragment.showToast(message: Int) = context!!.showToast(message)
fun Fragment.showToastLong(message: String) = context!!.showToastLong(message)