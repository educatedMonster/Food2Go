package com.example.food2go.utilities.extensions

import android.widget.EditText
import androidx.annotation.StringRes
import com.example.food2go.R
import com.example.food2go.autocomplete.ArrayAdapterInstantAuto
import com.example.food2go.autocomplete.InstantAutoComplete
import com.example.food2go.constants.AppConst.EMAIL_REGEX
import com.google.android.material.textfield.TextInputEditText
import java.util.*

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

fun hasSelected(
    autoComplete: InstantAutoComplete,
    adapter: ArrayAdapterInstantAuto,
    showError: Boolean,
    @StringRes errorMess: Int,
): Boolean {
    if (adapter.getSelected() == null) {
        val message =
            if (autoComplete.text.toString().isEmpty()) R.string.required else errorMess
        if (showError) {
            autoComplete.error = autoComplete.context.getString(message)
            autoComplete.requestFocus()
        }
        return false
    }
    val selectedItem = formatText(adapter.getSelected()!!.text.lowercase(Locale.getDefault()))
    val autoText = formatText(autoComplete.text.toString().lowercase(Locale.getDefault()))
    if (!selectedItem.matches(autoText.toRegex())) {
        if (showError) {
            autoComplete.error = autoComplete.context.getString(errorMess)
            autoComplete.requestFocus()
        }
        return false
    }
    return true
}

private fun formatText(text: String): String {
    return text.replace("[^a-zA-Z0-9]".toRegex(), "")
}