package com.example.food2go.utilities.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.food2go.R
import com.example.food2go.databinding.DialogGlobalBinding
import com.example.food2go.utilities.helpers.GlobalDialogClicker

class GlobalDialog(
    private val configureDialog: ConfigureDialog,
    private val listener: Listener?,
) : DialogFragment() {

    interface Listener {
        fun onRejectedListener(remark: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = configureDialog.activity

        //Initialize the AlertDialog.Builder
        val builderDialog = AlertDialog.Builder(activity)

        //Initialize LayoutInflater
        val layoutInflater = LayoutInflater.from(activity)

        //Initialize DataBindingUtil
        //**make sure your layout was inside the <layout> view
        val binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_global, null, false
        ) as DialogGlobalBinding

        binding.lifecycleOwner = this

        binding.configureDialog = configureDialog

        binding.buttonPositive.setOnClickListener {
            configureDialog.positiveButtonListener
        }
        //Use the binding.root to get the view on our binding
        val view = binding.root

        builderDialog.setView(view)

        val alertDialog = builderDialog.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val isLoading = configureDialog.message == null
        alertDialog.setCancelable(isLoading)
        alertDialog.setCanceledOnTouchOutside(isLoading)

        return alertDialog
    }
}

data class ConfigureDialog(
    val activity: Activity,
    val title: String,
    val message: String? = null,

    val neutralButtonName: String? = activity.getString(R.string.dialog_cancel_button),
    val negativeButtonName: String? = activity.getString(R.string.dialog_no_button),
    val positiveButtonName: String? = activity.getString(R.string.dialog_yes_button),

    val neutralButtonListener: GlobalDialogClicker? = null,
    val negativeButtonListener: GlobalDialogClicker? = null,
    val positiveButtonListener: GlobalDialogClicker? = null,

) {
    val showLayoutButton: Boolean = (positiveButtonListener != null) || (negativeButtonListener != null) || (neutralButtonListener != null)
    val showDescription: Boolean = message != null

    val showNeutralButton: Boolean = neutralButtonListener != null
    val showNegativeButton: Boolean = negativeButtonListener != null
    val showPositiveButton: Boolean = positiveButtonListener != null
}