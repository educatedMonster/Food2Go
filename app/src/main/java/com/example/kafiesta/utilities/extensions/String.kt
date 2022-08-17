package com.example.kafiesta.utilities.extensions

import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import com.example.kafiesta.R
import com.example.kafiesta.constants.AppConst.EMAIL_REGEX
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.dialog_layout_add_product.view.*

fun String.isEmailValid(): Boolean {
    return EMAIL_REGEX.toRegex().matches(this)
}

fun isEmailValid(textInputEditText: TextInputEditText, showError: Boolean): Boolean {
    if (EMAIL_REGEX.toRegex().matches(textInputEditText.text.toString())) {
        return true
    } else {
        if (showError) {
            textInputEditText.error = textInputEditText.context.getString(R.string.invalid_email)
            textInputEditText.requestFocus()
        }
    }
    return true
}

fun isNotEmpty(editText: EditText, showError: Boolean): Boolean {
    if (editText.text.toString().matches("".toRegex())) {
        if (showError) {
            editText.error = editText.context.getString(R.string.required)
            editText.requestFocus()
        }
        return false
    }
    return true
}


fun isNotEmpty(textInputEditText: TextInputEditText, showError: Boolean): Boolean {
    if (textInputEditText.text.toString().matches("".toRegex())) {
        if (showError) {
            textInputEditText.error = textInputEditText.context.getString(R.string.required)
            textInputEditText.requestFocus()
        }
        return false
    }
    return true
}
