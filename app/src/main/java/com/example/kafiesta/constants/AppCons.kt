package com.example.kafiesta.constants

import com.example.kafiesta.R

object ServerConst {
    private const val SERVER_URL = "https://food2go.osc-fr1.scalingo.io/"
    const val API_SERVER_URL = "${SERVER_URL}v1/"
    const val IMAGE_PLACE_HOLDER =
        "https://pbs.twimg.com/profile_images/1048086829143011329/W8R1grIh_400x400.jpg"

    //Test Account
    const val TEST_EMAIL = "jadalmario.freelancer@gmail.com"
    const val TEST_PASSWORD = "@Unknown0322"

    const val IS_CLIENT = "client"
    const val IS_SUCCESS = "success"
}

object PusherConst {
    const val PUSHER_MY_CHANNEL = "my-channel"
    const val PUSHER_MY_EVENT = "my-event"
    const val PUSHER_ORDER_PIPELINE_EVENT = "order-pipeline-event"
    const val PUSHER_TRANSACTION_EVENT = "transaction-event"
    const val PUSHER_API_KEY = "5916385ce66c483b7a01"
    const val PUSHER_CLUSTER = "ap1"

    const val NOTIFICATION_ID = "INTENT_KEY_NOTIFICATION_ID"
    const val ORDER_DATA = "ORDER_DATA"
}

object IntentConst {
    const val ORDER_ID = "ORDER_ID"
    const val CUSTOMER_ID = "CUSTOMER_ID"
    const val PAYMENT_PROOF_URL = "PAYMENT_PROOF_URL"
}

object AppConst {
    const val app_name_channel = "Food2Go Channel"
    const val XML_SECURE_SHARED_PREFS_NAME = "secure_preference"
    const val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    const val EMAIL_REGEX_2 =
        "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
}

object UserConst {
    const val SP_USER_REMEMBER_ME = "key_user_remember_me"
    const val TOKEN = "key_auth_token"
    const val TOKEN_TYPE = "key_token_type"
    const val EXPIRES_IN = "key_expires_in"
    const val USER_ID = "key_id"
    const val FIRSTNAME = "key_firstname"
    const val LASTNAME = "key_lastname"
    const val EMAIL = "key_email"
    const val STATUS = "key_address"
    const val ROLE = "key_role"
    const val INFO_ID = "key_info_id"
    const val INFO_ID_USER_ID = "key_info_id"
    const val COMPLETE_ADDRESS = "key_complete_address"
    const val PRIMARY_CONTACT = "key_secondary_address"
    const val SECONDARY_CONTACT = "key_primary_address"
    const val SHOP_ID = "key_shop_id"


    const val PROFILE_STATUS = "active"
    const val PROFILE_ROLE = "client"
}

object ThemeConst {
    const val ColorTheme = R.style.AppTheme_Theme_ColorTheme
}

object DialogTag {
    const val DIALOG_MAIN_LOGOUT = "NAV_LOGOUT_GLOBAL_DIALOG"
    const val DIALOG_MAIN_LOGOUT_FORM_STATE = "FORM_STATE_LOGOUT_GLOBAL_DIALOG"
    const val DIALOG_FORM_INITIAL_PRODUCT = "DIALOG_FORM_INITIAL_PRODUCT"
    const val DIALOG_FORM_EDIT_PRODUCT = "DIALOG_FORM_INITIAL_PRODUCT"
    const val DIALOG_DELETE_PRODUCT = "DIALOG_DELETE_PRODUCT"
    const val DIALOG_BOTTOM_QUANTITY = "DIALOG_BOTTOM_QUANTITY"
    const val DIALOG_BOTTOM_ADD_INVENTORY_PRODUCT = "DIALOG_BOTTOM_ADD_INVENTORY_PRODUCT"
    const val DIALOG_BOTTOM_SEARCH_INVENTORY = "DIALOG_BOTTOM_SEARCH_INVENTORY"
    const val DIALOG_ORDER_DETAILS = "DIALOG_ORDER_DETAILS"
    const val DIALOG_WEB_VIEW_TAG = "DIALOG_WEB_VIEW_TAG"
    const val DIALOG_IMAGE_SLIDER = "DIALOG_IMAGE_SLIDER"
    const val DIALOG_REJECT_REMARK = "DIALOG_REJECT_REMARK"
    const val DIALOG_WEEKLY_PAYMENT = "DIALOG_WEEKLY_PAYMENT"
    const val DIALOG_WEEKLY_PAYMENT_URL = "DIALOG_WEEKLY_PAYMENT_URL"
}

object RequestCodeTag {
    const val UPDATE_REFRESH_TASK = 9991
    const val REQUEST_CODE_CAMERA = 1001
    const val REQUEST_CODE_FCM = 1002
}

object ProductConst {
    const val PRODUCT_ACTIVE = "active"
    const val PRODUCT_IN_ACTIVE = "inactive"
}

object OrderConst {
    const val ORDER_ALL = "all"
    const val ORDER_PENDING = "pending"
    const val ORDER_PREPARING = "preparing"
    const val ORDER_DELIVERY = "outfordelivery"
    const val ORDER_COMPLETED = "completed"
    const val ORDER_REJECTED = "rejected"
}
