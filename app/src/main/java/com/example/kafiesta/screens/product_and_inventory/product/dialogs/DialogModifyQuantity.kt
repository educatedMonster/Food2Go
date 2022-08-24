package com.example.kafiesta.screens.product_and_inventory.product.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.example.kafiesta.R
import com.example.kafiesta.databinding.DialogBottomModifyQuantityBinding
import com.example.kafiesta.domain.InventoryDomain
import com.example.kafiesta.utilities.extensions.isNotEmpty
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

class DialogModifyQuantity(
    private val userId: Long,
    private val model: InventoryDomain,
    private val listener: Listener,
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomModifyQuantityBinding
    private lateinit var mActivity: Activity
    private lateinit var mFragment: FragmentActivity

    interface Listener {
        fun onAddQuantityListener(quantity: String, productId: Long)
    }

    override fun getTheme(): Int = R.style.NoMarginsDialog

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    @SuppressLint("DefaultLocale")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_bottom_modify_quantity,
            null,
            false
        ) as DialogBottomModifyQuantityBinding
        binding.model = model

        val view = binding.root
        dialog.setView(view)

        // onClickListener here
        binding.apply {
            buttonSave.setOnClickListener {
                if (onValidate(binding.etQuantity)) {
                    val pQuantity = binding.etQuantity.text.toString()
                    listener.onAddQuantityListener(pQuantity, model!!.id)
                }
            }
        }

        val alertDialog = dialog.create()
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

    fun setParentActivity(activity: Activity, fragment: FragmentActivity) {
        mActivity = activity
        mFragment = fragment
    }
}