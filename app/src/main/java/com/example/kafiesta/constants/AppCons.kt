package com.example.kafiesta.constants

object ServerConst {

    //https://kafiesta-api.osc-fr1.scalingo.io/v1/login

    private const val SERVER_URL = "https://kafiesta-api.osc-fr1.scalingo.io/"
    const val API_SERVER_URL = "${SERVER_URL}v1/"
    const val IMAGE_PLACE_HOLDER = "${SERVER_URL}public/images/icons/default-user.png"
}

object AppConst {
    const val XML_SECURE_SHARED_PREFS_NAME = "secure_preference"
    const val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
}

object UserConst {
    const val TOKEN = "key_auth_token"
    const val ID = "key_id"
    const val FIRSTNAME = "key_firstname"
    const val LASTNAME = "key_lastname"
    const val EMAIL = "key_email"
    const val ADDRESS = "key_address"
    const val ROLE = "key_role"
    const val USERINFORMATION = "key_userInformations"
}