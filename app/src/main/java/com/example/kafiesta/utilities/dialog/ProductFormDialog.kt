package com.example.kafiesta.utilities.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
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
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.kafiesta.R
import com.example.kafiesta.constants.RequestCodeTag
import com.example.kafiesta.databinding.DialogProductFormBinding
import com.example.kafiesta.domain.ProductDomain
import com.example.kafiesta.screens.add_product.ProductViewModel
import com.example.kafiesta.utilities.extensions.isNotEmpty
import com.example.kafiesta.utilities.helpers.AddEditProductDialogClicker
import com.example.kafiesta.utilities.helpers.CancelProductDialogClicker
import com.example.kafiesta.utilities.helpers.DeleteProductDialogClicker
import com.example.kafiesta.utilities.helpers.FileUtils
import com.google.android.material.textfield.TextInputEditText
import com.trackerteer.taskmanagement.utilities.extensions.visible
import java.io.File

class ProductFormDialog(
    private val configureFormDialog: ConfigureProductFormDialog,
    private val application: Application,
    private val prodId: Long,
    private val userId: Long,
) :
    DialogFragment() {

    private var mOutputFileUri: Uri? = null
    private var mFile: File? = null
    private var isGetImage = false
    private lateinit var binding: DialogProductFormBinding
    private lateinit var productViewModel: ProductViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        productViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                .create(ProductViewModel::class.java)

        val activity = configureFormDialog.activity

        //Initialize the AlertDialog.Builder
        val builderDialog = AlertDialog.Builder(activity)

        //Initialize LayoutInflater
        val layoutInflater = LayoutInflater.from(activity)

        //Initialize DataBindingUtil
        //**make sure your layout was inside the <layout> view
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_product_form, null, false
        ) as DialogProductFormBinding

        binding.lifecycleOwner = this
        binding.configureFormDialog = configureFormDialog

        val pName = binding.etProductName.text.toString()
        val pDesc = binding.etProductDesc.text.toString()
        val pPrice = binding.etProductPrice.text.toString()
        val pTags = binding.etProductTags.text.toString()
        val pStatus = validateCb(binding.cbProductCheck)


        // onClickListener
        binding.buttonCreate.setOnClickListener {

            configureFormDialog.createButtonListener
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
                    price = pPrice,
                    tags = pTags,
                    status = pStatus
                )

                productViewModel.addProduct(product, mFile!!)
            }

        }

        binding.buttonUpdate.setOnClickListener {

            configureFormDialog.updateButtonListener
            if (onValidate(
                    binding.etProductName,
                    binding.etProductDesc,
                    binding.etProductPrice,
                    binding.etProductTags,
                )
            ) {
                val product = ProductDomain(
                    id = prodId,
                    userID = userId,
                    name = pName,
                    description = pDesc,
//                            imageURL = mSelectedFile,
                    price = pPrice,
                    tags = pTags,
                    status = pStatus
                )
                productViewModel.editProduct(product, mFile!!)
            }

        }

        binding.buttonDelete.setOnClickListener {
            configureFormDialog.deleteButtonListener
            productViewModel.deleteProduct(prodId)
        }

        binding.buttonCancel.setOnClickListener {
            configureFormDialog.cancelButtonListener
            dismiss()
        }

        binding.circleAddProduct.setOnClickListener {
            startGettingImage()
        }

        //Use the binding.root to get the view on our binding
        val view = binding.root

        builderDialog.setView(view)

        val alertDialog = builderDialog.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val isLoading = configureFormDialog.title == null
        alertDialog.setCancelable(isLoading)
        alertDialog.setCanceledOnTouchOutside(isLoading)

        return alertDialog
    }


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
                        mFile = FileUtils.getFile(context, mOutputFileUri)!!
                    }

                    isGetImage = true
                }
            }
        }
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

data class ConfigureProductFormDialog(
    val activity: Activity,
    val title: String? = null,

    val createButtonName: String? = activity.getString(R.string.dialog_product_form_create_button),
    val cancelButtonName: String? = activity.getString(R.string.dialog_product_form_cancel_button),
    val deleteButtonName: String? = activity.getString(R.string.dialog_product_form_delete_button),
    val updateButtonName: String? = activity.getString(R.string.dialog_product_form_update_button),

    val createButtonListener: AddEditProductDialogClicker? = null,
    val updateButtonListener: AddEditProductDialogClicker? = null,
    val cancelButtonListener: CancelProductDialogClicker? = null,
    val deleteButtonListener: DeleteProductDialogClicker? = null,
) {

    val showLayoutButton: Boolean =
        (createButtonListener != null) ||
                (updateButtonListener != null) ||
                (cancelButtonListener != null) ||
                (deleteButtonListener != null)

    val showDescription: Boolean = title != null

    val showCreateButton: Boolean = createButtonListener != null
    val showUpdateButton: Boolean = updateButtonListener != null
    val showCancelButton: Boolean = cancelButtonListener != null
    val showDeleteButton: Boolean = deleteButtonListener != null

}