package com.example.kafiesta.repository

import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.utilities.helpers.SharedPrefs

class EmptyRepository(
    private val sharedPrefs: SharedPrefs,
) {
    private val token = sharedPrefs.getString(UserConst.TOKEN)!!
    private val userid = sharedPrefs.getString(UserConst.USER_ID)!!

}