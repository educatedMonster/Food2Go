package com.example.kafiesta.utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import com.example.kafiesta.R
import com.example.kafiesta.utilities.extensions.showToast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

fun setFullscreen(activity: Activity) {
    activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
    activity.window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}

fun setBearer(token: String): String {
    return "bearer $token"
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

/**
 * In this function we will need to initialize the permissions needed to this app in order to used it by the user
 *
 * @param activity we need the activity class here to
 */
fun initMultiplePermission(activity: Activity) {
    Dexter.withActivity(activity)
        .withPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object :
            MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (!report.areAllPermissionsGranted()) {
                    if (report.isAnyPermissionPermanentlyDenied) {
                        activity.showToast(activity.getString(R.string.permission_denied_warning))
                    } else {
                        activity.showToast(activity.getString(R.string.permission_warning))
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest>,
                token: PermissionToken
            ) {
                token.continuePermissionRequest()
            }
        }).check()
}