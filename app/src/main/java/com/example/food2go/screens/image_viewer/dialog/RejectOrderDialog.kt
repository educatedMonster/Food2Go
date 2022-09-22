package com.example.food2go.screens.image_viewer.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.food2go.R
import com.example.food2go.databinding.DialogRejectOrderBinding
import com.example.food2go.utilities.extensions.showToast
import com.example.food2go.utilities.shakeView

class RejectOrderDialog(
    private val listener: Listener,
) : DialogFragment() {

    interface Listener {
        fun onRejectOrder(remark: String)
    }

    @SuppressLint("DefaultLocale")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //Initialize the AlertDialog.Builder
        val builderDialog = AlertDialog.Builder(context)
        //Initialize LayoutInflater
        val layoutInflater = LayoutInflater.from(context)
        //Initialize DataBindingUtil
        //**make sure your layout was inside the <layout> view
        val binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_reject_order, null, false
        ) as DialogRejectOrderBinding

        //Use the binding.root to get the view on our binding
        val view = binding.root

        builderDialog.setView(view)

        binding.buttonPositive.setOnClickListener {
            val remarks = binding.etRemarks.text.toString()
            val isVerify = binding.checkboxVerify.isChecked

            if (isVerify) {
                if (remarks.isEmpty()) {
                    shakeView(binding.etRemarks, 10, 5)
                    binding.etRemarks.error = getString(R.string.required)
                    return@setOnClickListener
                } else {
                    binding.buttonPositive.isEnabled = false
                    listener.onRejectOrder(remarks)
                }
            } else {
                shakeView(binding.checkboxVerify, 10, 5)
                showToast("You must tick the checkbox to continue rejecting the order.")
            }
        }

        binding.buttonNeutral.setOnClickListener {
            dialog?.dismiss()
        }

        val alertDialog = builderDialog.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(true)
        alertDialog.setCanceledOnTouchOutside(true)

        return alertDialog
    }
}