package com.example.food2go.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.food2go.constants.PusherConst.PUSHER_MY_CHANNEL
import com.example.food2go.constants.PusherConst.PUSHER_ORDER_PIPELINE_EVENT
import com.example.food2go.constants.UserConst
import com.example.food2go.network.AppNetwork
import com.example.food2go.network.paramsToRequestBody
import com.example.food2go.utilities.helpers.SharedPrefs
import com.example.food2go.utilities.setBearer
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class EmptyRepository(
    private val sharedPrefs: SharedPrefs,
) {
    private val token = sharedPrefs.getString(UserConst.TOKEN)!!
    private val userid = sharedPrefs.getString(UserConst.USER_ID)!!

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    suspend fun onTriggerPusher(data: Any, event: String? = PUSHER_ORDER_PIPELINE_EVENT, channelName: String? = PUSHER_MY_CHANNEL ) {
        withContext(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)

                val params = HashMap<String, Any>()
                params["data"] = data
                params["event_name"] = event!!
                params["channel_name"] = channelName!!

                val network = AppNetwork.service.onTriggerPusherAsync(
                    bearer = setBearer(token),
                    params = paramsToRequestBody(params)
                ).await()
                Timber.d(Gson().toJson(network))
            } catch (e: HttpException) {
                _isLoading.postValue(false)
            }
        }
    }

}