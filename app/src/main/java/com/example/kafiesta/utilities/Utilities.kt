package com.example.kafiesta.utilities

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager

fun setFullscreen(activity: Activity) {
    activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
    activity.window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}

fun setToken(token: String): String {
    return token
}

fun hideKeyboard(activity: Activity?) {
    val view = activity?.currentFocus
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun hideKeyboard(activity: Activity, view: View) {
    val im =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    im.hideSoftInputFromWindow(view.windowToken, 0)
}

fun shakeView(view: View, duration: Int, offset: Int): View {
    val anim = TranslateAnimation((-offset).toFloat(), offset.toFloat(), 0f, 0f)
    anim.duration = duration.toLong()
    anim.repeatMode = Animation.REVERSE
    anim.repeatCount = 5
    view.startAnimation(anim)
    return view
}