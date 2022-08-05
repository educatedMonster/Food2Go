package com.example.kafiesta.utilities.extensions

import com.example.kafiesta.constants.AppConst.EMAIL_REGEX
import com.example.kafiesta.constants.AppConst.EMAIL_REGEX_2

fun String.isEmailValid(): Boolean {
    return EMAIL_REGEX.toRegex().matches(this)
}

fun String.isEmailValid2(): Boolean {
    return EMAIL_REGEX_2.toRegex().matches(this)
}