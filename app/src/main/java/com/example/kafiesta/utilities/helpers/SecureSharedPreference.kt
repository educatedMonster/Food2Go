package com.example.kafiesta.utilities.helpers

import android.content.Context
import com.example.kafiesta.constants.AppConst
import com.securepreferences.SecurePreferences

private lateinit var SECURED_INSTANCE: SecurePreferences

fun getSecurePrefs(context: Context): SecurePreferences {
    synchronized(SecurePreferences::class.java){
        if(!::SECURED_INSTANCE.isInitialized){
            SECURED_INSTANCE = SecurePreferences(context, "", AppConst.XML_SECURE_SHARED_PREFS_NAME)
        }
    }
    return SECURED_INSTANCE
}