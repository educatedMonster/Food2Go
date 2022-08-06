package com.example.kafiesta.constants

import com.example.kafiesta.R

object ServerConst {

    //https://kafiesta-api.osc-fr1.scalingo.io/v1/login

    private const val SERVER_URL = "https://kafiesta-api.osc-fr1.scalingo.io/"
    const val API_SERVER_URL = "${SERVER_URL}v1/"
    const val IMAGE_PLACE_HOLDER = "${SERVER_URL}public/images/icons/default-user.png"

    //Test Account
    const val TEST_EMAIL = "jadalmario.freelancer@gmail.com"
    const val TEST_PASSWORD = "@Unknown0322"
}

object AppConst {
    const val XML_SECURE_SHARED_PREFS_NAME = "secure_preference"
    const val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    const val EMAIL_REGEX_2 = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
}

object UserConst {
    const val TOKEN = "key_auth_token"
    const val TOKEN_TYPE = "key_token_type"
    const val EXPIRES_IN = "key_expires_in"
    const val ID = "key_id"
    const val FIRSTNAME = "key_firstname"
    const val LASTNAME = "key_lastname"
    const val EMAIL = "key_email"
    const val ADDRESS = "key_address"
    const val ROLE = "key_role"
    const val USERINFORMATION = "key_userInformations"
}

object ThemeConst {
    const val ColorTheme = R.style.AppTheme_Theme_ColorTheme
}

object DialogTag {
    const val DIALOG_MAIN_LOGOUT = "NAV_LOGOUT_GLOBAL_DIALOG"
    const val DIALOG_MAIN_LOGOUT_FORM_STATE = "FORM_STATE_LOGOUT_GLOBAL_DIALOG"
}