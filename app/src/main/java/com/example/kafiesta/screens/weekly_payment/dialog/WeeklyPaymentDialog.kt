package com.example.kafiesta.screens.weekly_payment.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.kafiesta.R
import com.example.kafiesta.constants.RequestCodeTag
import com.example.kafiesta.databinding.DialogLayoutWeeklyPaymentBinding
import com.example.kafiesta.domain.WeeklyPaymentDomain
import com.example.kafiesta.utilities.extensions.showToast
import com.example.kafiesta.utilities.helpers.FileUtils
import com.example.kafiesta.utilities.imageUrl
import com.example.kafiesta.utilities.shakeView
import com.trackerteer.taskmanagement.utilities.extensions.visible
import java.io.File

class WeeklyPaymentDialog(
    private val listener: Listener,
    private val payment: WeeklyPaymentDomain,
) : DialogFragment() {

    interface Listener {
        fun onWeeklyPaymentListener(payment: WeeklyPaymentDomain, file: File)
    }

    private var mOutputFileUri: Uri? = null
    private var mFile: File? = null
    private var isGetImage = false
    private lateinit var binding: DialogLayoutWeeklyPaymentBinding

    // Todo - Get the selected image path here
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCodeTag.REQUEST_CODE_CAMERA -> {
                    val isCamera: Boolean = if (data == null || data.data == null) {
                        true
                    } else {
                        val action = data.action
                        action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
                    }
                    val selectedImageUri: Uri?
                    if (isCamera) {
                        selectedImageUri = mOutputFileUri
                        if (selectedImageUri == null) return
                        imageUrl(binding.imageProof, mOutputFileUri)
                    } else {
                        selectedImageUri = data!!.data
                        if (selectedImageUri == null) return
                        mOutputFileUri = selectedImageUri
                        imageUrl(binding.imageProof, mOutputFileUri)
                    }
                    binding.imageProof.visible()

                    if (mOutputFileUri != null) {
                        mFile = FileUtils.getFile(context, mOutputFileUri)!!
                    }

                    isGetImage = true
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_layout_weekly_payment,
            null,
            false
        ) as DialogLayoutWeeklyPaymentBinding
        val view = binding.root
        dialog.setView(view)

        // onClickListener here
        binding.apply {
            imageProof.setOnClickListener {
                startGettingImage()
            }

            buttonSettle.setOnClickListener {
                if (binding.checkboxVerify.isChecked) {
                    if (mFile == null) {
                        shakeView(binding.imageProof, 10, 5)
                        return@setOnClickListener
                    } else {
                        binding.progressLoading.visible()
                        listener.onWeeklyPaymentListener(payment, mFile!!)
                    }
                } else {
                    shakeView(binding.checkboxVerify, 10, 5)
                    showToast("You must tick the checkbox to continue rejecting the order.")
                }
            }

            buttonCancel.setOnClickListener {
                dismiss()
            }
        }

        val alertDialog = dialog.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)

        return alertDialog
    }

    private fun startGettingImage() {
        val fileName = "food2Go-${System.currentTimeMillis()}"
        @Suppress("DEPRECATION") val rootDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(fileName, ".jpg", rootDirectory)
        mOutputFileUri = Uri.fromFile(imageFile)
        setFileChooser()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun setFileChooser() {
        val intentCameraArray = ArrayList<Intent>()
        val intentCapture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val cameraList = requireContext().packageManager.queryIntentActivities(intentCapture, 0)
        for (cl in cameraList) {
            val packageName = cl.activityInfo.packageName
            val intent = Intent(intentCapture)
            intent.component = ComponentName(cl.activityInfo.packageName, cl.activityInfo.name)
            intent.setPackage(packageName)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri)
            intentCameraArray.add(intent)
        }
        val intentGallery = Intent()
        intentGallery.type = "image/*"
        intentGallery.action = Intent.ACTION_PICK

        val intentChooser =
            Intent.createChooser(intentGallery, getString(R.string.title_select_image))
        intentChooser.putExtra(
            Intent.EXTRA_INITIAL_INTENTS,
            intentCameraArray.toTypedArray<Parcelable>()
        )
        startActivityForResult(intentChooser, RequestCodeTag.REQUEST_CODE_CAMERA)
    }
}