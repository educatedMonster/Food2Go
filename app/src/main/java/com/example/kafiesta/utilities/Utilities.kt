package com.example.kafiesta.utilities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kafiesta.R
import com.example.kafiesta.constants.RequestCodeTag
import com.example.kafiesta.utilities.dialog.ProductFormDialog
import com.example.kafiesta.utilities.extensions.showToast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
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


fun createImageFileName(): Uri {
    val fileName = "image-${System.currentTimeMillis()}"

    @Suppress("DEPRECATION")
    val rootDirectory =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val imageFile = File.createTempFile(fileName, ".jpg", rootDirectory)
    return Uri.fromFile(imageFile)
}

@SuppressLint("QueryPermissionsNeeded")
fun setFileChooser(activity: Activity, uri: Uri) {
    val intentCameraArray = ArrayList<Intent>()
    val intentCapture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val cameraList = activity.packageManager.queryIntentActivities(intentCapture, 0)
    for (cl in cameraList) {
        val packageName = cl.activityInfo.packageName
        val intent = Intent(intentCapture)
        intent.component = ComponentName(cl.activityInfo.packageName, cl.activityInfo.name)
        intent.setPackage(packageName)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        intentCameraArray.add(intent)
    }
    val intentGallery = Intent()
    intentGallery.type = "image/*"
    intentGallery.action = Intent.ACTION_PICK

    val intentChooser = Intent.createChooser(intentGallery, activity.getString(R.string.title_select_image))
    intentChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentCameraArray.toTypedArray<Parcelable>()
    )

    activity.startActivityForResult(intentChooser, RequestCodeTag.REQUEST_CODE_CAMERA)
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

@BindingAdapter("imageUrl")
fun imageUrl(imageView: ImageView?, url: String?) {
    val imageUrl = url ?: "https://pbs.twimg.com/profile_images/1048086829143011329/W8R1grIh_400x400.jpg"

    imageUrl.let {
        val imgUri = imageUrl.toUri().buildUpon().scheme("http").build()
        if (imageView != null) {
            Glide.with(imageView.context)
                .load(imgUri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                )
                .into(imageView)
        }
    }
}

fun getDialog(fragment: FragmentActivity?, dialogTag: String): Any? {
    return fragment?.supportFragmentManager?.findFragmentByTag(dialogTag)
}