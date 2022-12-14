package com.example.food2go.utilities

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.food2go.R
import com.example.food2go.constants.ServerConst.IMAGE_PLACE_HOLDER
import com.example.food2go.utilities.dialog.GlobalDialog
import com.example.food2go.utilities.extensions.showToast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

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
                token: PermissionToken,
            ) {
                token.continuePermissionRequest()
            }
        }).check()
}

fun getMimeType(context: Context, uri: Uri?): String? {
    if (uri == null) return null

    return if (uri.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
        val cr: ContentResolver = context.contentResolver
        cr.getType(uri)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
            uri
                .toString()
        )
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            fileExtension.toLowerCase(Locale.getDefault())
        )
    }
}

@BindingAdapter("loadItemImage")
fun loadItemImage(imageView: ImageView, url: Uri?) {
    val imageUrl = url ?: IMAGE_PLACE_HOLDER
    imageUrl.let {
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(
                RequestOptions()
                    .fitCenter()
            )
            .into(imageView)
    }
}

@BindingAdapter("loadItemImage")
fun loadItemImage(imageView: ImageView, url: String?) {
    val imageUrl = url ?: IMAGE_PLACE_HOLDER
    url.let {
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(
                RequestOptions()
                    .fitCenter()
            )
            .into(imageView)
    }
}

@BindingAdapter("imageUrl")
fun imageUrl(imageView: ImageView, url: String?) {
    val imageUrl = url ?: IMAGE_PLACE_HOLDER
    imageUrl.let {
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imageView)
    }
}

@BindingAdapter("imageUrl")
fun imageUrl(imageView: ImageView, url: Uri?) {
    val imageUrl = url ?: IMAGE_PLACE_HOLDER
    imageUrl.let {
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imageView)
    }
}

fun getDialog(fragment: FragmentActivity?, dialogTag: String): Any? {
    return fragment?.supportFragmentManager?.findFragmentByTag(dialogTag)
}

fun getGlobalDialog(fragment: FragmentActivity, dialogTag: String): GlobalDialog? {
    return fragment.supportFragmentManager.findFragmentByTag(dialogTag) as GlobalDialog?
}

fun getDateNow(): String {
    val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDateTime.now()
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    val formatted = current.format(formatter)
    println("Current Date and Time is: $formatted")
    return formatted
}