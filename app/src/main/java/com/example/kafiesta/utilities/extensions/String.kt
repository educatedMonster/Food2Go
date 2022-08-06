package com.example.kafiesta.utilities.extensions

import android.widget.EditText
import com.example.kafiesta.R
import com.example.kafiesta.constants.AppConst.EMAIL_REGEX
import com.example.kafiesta.constants.AppConst.EMAIL_REGEX_2
import com.google.android.material.textfield.TextInputEditText



    fun String.isEmailValid(): Boolean {
        return EMAIL_REGEX.toRegex().matches(this)
    }

    fun isEmailValid2(textInputEditText: TextInputEditText, showError: Boolean): Boolean {
        if (showError) {
            textInputEditText.error = textInputEditText.context.getString(R.string.required)
            textInputEditText.requestFocus()
            return false
        }
        return EMAIL_REGEX_2.toRegex().matches(textInputEditText.toString())
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
