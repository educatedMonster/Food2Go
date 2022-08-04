package com.example.kafiesta.constants

object ServerConst {
    private const val SERVER_URL = "https://kafiesta.com/"
    const val API_SERVER_URL = "${SERVER_URL}api/"
    const val IMAGE_PLACE_HOLDER = "${SERVER_URL}public/images/icons/default-user.png"
}

object AppConst {
    const val XML_SECURE_SHARED_PREFS_NAME = "secure_preference"
    const val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
}