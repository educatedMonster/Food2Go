package com.example.kafiesta.utilities.extensions

import com.example.kafiesta.constants.AppConst.EMAIL_REGEX

fun String.isEmailValid(): Boolean {
    return EMAIL_REGEX.toRegex().matches(this)
}