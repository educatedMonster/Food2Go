package com.example.kafiesta.screens.inventory_product.bottom_dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.kafiesta.R
import com.example.kafiesta.databinding.DialogBottomSheetFragmentBinding
import com.example.kafiesta.domain.ProductInventoryDomain
import com.example.kafiesta.utilities.dialog.ProductAddDialog
import com.example.kafiesta.utilities.extensions.isNotEmpty
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.dialog_bottom_sheet_fragment.*

class BottomSheetFragment(
    private val userId: Long,
    private val model: ProductInventoryDomain,
    private val listener: Listener,
) : BottomSheetDialogFragment() {

    interface Listener {
        fun onAddQuantityListener(quantity: Long, productId: Long)
    }

    private lateinit var binding: DialogBottomSheetFragmentBinding

    @SuppressLint("DefaultLocale")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_bottom_sheet_fragment,
            null,
            false
        ) as DialogBottomSheetFragmentBinding
        binding.model = model

        val view = binding.root
        dialog.setView(view)

        // onClickListener here
        binding.apply {
            buttonSave.setOnClickListener {
                if (onValidate(binding.etQuantity)) {
                    val pQuantity = binding.etQuantity.text.toString()
                    listener.onAddQuantityListener(pQuantity.toLong(), model!!.id)
                }
            }
        }

        val alertDialog = dialog.create()
        alertDialog.window!!.setLayout(ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window?.attributes!!.windowAnimations = R.style.BottomDialogAnimation
        alertDialog.window?.setGravity(Gravity.BOTTOM)
        return alertDialog
    }

    private fun onValidate(quantity: TextInputEditText): Boolean {
        if (!isNotEmpty(quantity, true)) {
            return false
        }
        return true
    }
}