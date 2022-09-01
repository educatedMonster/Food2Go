package com.example.kafiesta.autocomplete

import android.text.Editable
import android.text.TextWatcher
import java.util.*

class InstantAutoTextWatcher(
    private val mArrayAdapterInstantAuto: ArrayAdapterInstantAuto,
) :
    TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (mArrayAdapterInstantAuto.getSelected() != null) {
            var selectedString: String =
                mArrayAdapterInstantAuto.getSelected()!!.text.lowercase(Locale.getDefault())
                    .replace("(", "\\(")
            selectedString = selectedString.replace(")", "\\)")
            var inputString = s.toString().lowercase(Locale.getDefault()).replace("(", "\\(")
            inputString = inputString.replace(")", "\\)")
            if (!selectedString.matches(inputString.toRegex())) {
                mArrayAdapterInstantAuto.setSelected(null)
            }

//            if (selectedString.isEmpty()) {
//                mArrayAdapterInstantAuto.setSelected(null)
//            }
        }
    }

    override fun afterTextChanged(s: Editable) {}
}