package com.example.food2go.utilities.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.Toast
import com.example.food2go.R

fun Context.hasInternet(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT < 23) {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        activeNetworkInfo != null && activeNetworkInfo.isConnected
    } else {
        val nc = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (nc == null) {
            false
        } else {
            nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
}

fun Context.hasInternet(notifyNoInternet: Boolean = true, trueFunc: (internet: Boolean) -> Unit) {
    if (hasInternet()) {
        trueFunc(true)
    } else if (notifyNoInternet) {
        showToast(getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
    }
}

fun Context.hasInternet(
    trueFunc: (internet: Boolean) -> Unit,
    falseFunc: (internet: Boolean) -> Unit
) {
    if (hasInternet()) {
        trueFunc(true)
    } else {
        falseFunc(true)
    }
}

fun Context.showToast(message: String, length: Int) {
    Toast.makeText(this, message, length).show()
}


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


fun Context.showToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.inviView(view : View){
    view.visibility = View.INVISIBLE
}

fun Context.hideView(view : View){
    view.visibility = View.GONE
}

fun Context.showView(view: View){
    view.visibility = View.VISIBLE
}
