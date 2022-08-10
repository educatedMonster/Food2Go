package com.example.kafiesta.utilities.dialog

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
import com.bumptech.glide.Glide
import com.example.kafiesta.R
import com.example.kafiesta.constants.RequestCodeTag
import com.example.kafiesta.databinding.DialogLayoutAddProductBinding
import com.example.kafiesta.domain.ProductDomain
import com.example.kafiesta.utilities.extensions.isNotEmpty
import com.example.kafiesta.utilities.helpers.FileUtils
import com.google.android.material.textfield.TextInputEditText
import com.trackerteer.taskmanagement.utilities.extensions.showToast
import com.trackerteer.taskmanagement.utilities.extensions.visible
import java.io.File

class AddProductDialog(private val userId: Long, private val listener: Listener) :
    DialogFragment() {

    interface Listener {
        fun onAddProductListener(product: ProductDomain, selectedFile: File)
    }

    private var mOutputFileUri: Uri? = null
    private var mSelectedFile: File? = null
    private var isGetImage = false
    private lateinit var binding: DialogLayoutAddProductBinding

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
                        Glide.with(this).load(selectedImageUri).into(binding.circleAddProduct)
                    } else {
                        selectedImageUri = data!!.data
                        if (selectedImageUri == null) return
                        mOutputFileUri = selectedImageUri
                        Glide.with(this).load(selectedImageUri)
                            .into(binding.circleAddProduct)
                    }
                    binding.circleAddProduct.visible()

                    if (mOutputFileUri != null) {
                        mSelectedFile = FileUtils.getFile(context, mOutputFileUri)!!
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
            R.layout.dialog_layout_add_product,
            null,
            false
        ) as DialogLayoutAddProductBinding

        val view = binding.root

        dialog.setView(view)

        binding.circleAddProduct.setOnClickListener {
//                mOutputFileUri = createImageFileName()
//                setFileChooser(this@ProductActivity, mOutputFileUri!!)
            startGettingImage()
        }


        binding.buttonCreate.setOnClickListener {
            val pName = binding.etProductName.text.toString()
            val pDesc = binding.etProductDesc.text.toString()
            val pPrice = binding.etProductPrice.text.toString()
            val pTags = binding.etProductTags.text.toString()

            if (onValidate(
                    binding.etProductName,
                    binding.etProductDesc,
                    binding.etProductPrice,
                    binding.etProductTags,
                )
            ) {
                val product = ProductDomain(
                    id = -1L,
                    userID = userId,
                    name = pName,
                    description = pDesc,
                    imageURL = mSelectedFile,
                    price = pPrice.toDouble(),
                    tags = pTags,
                    status = validateCb(binding.cbProductCheck)
                )

                if(mSelectedFile != null){
                    listener.onAddProductListener(product, mSelectedFile!!)
                } else {
                    return@setOnClickListener
                    showToast("Product image needed.")
                }
            }
            dismiss()
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        val alertDialog = dialog.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(true)
        alertDialog.setCanceledOnTouchOutside(true)

        return alertDialog
    }

    private fun validateCb(checkBox: AppCompatCheckBox): String {
        return if (checkBox.isChecked) {
            "active"
        } else {
            "inactive"
        }
    }

    private fun onValidate(
        firstName: TextInputEditText,
        lastName: TextInputEditText,
        price: TextInputEditText,
        tags: TextInputEditText,
    ): Boolean {
        if (!isNotEmpty(firstName, true)) {
            return false
        } else if (!isNotEmpty(lastName, true)) {
            return false
        } else if (!isNotEmpty(lastName, true)) {
            return false
        } else if (!isNotEmpty(price, true)) {
            return false
        } else if (!isNotEmpty(tags, true)) {
            return false
        }
        return true
    }


    private fun startGettingImage() {
        val fileName = "kafiesta-${System.currentTimeMillis()}"
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

