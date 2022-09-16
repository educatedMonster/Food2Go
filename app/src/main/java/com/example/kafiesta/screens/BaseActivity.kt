package com.example.kafiesta.screens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kafiesta.KaFiestaApplication
import com.example.kafiesta.constants.PusherConst
import com.example.kafiesta.constants.ThemeConst
import com.example.kafiesta.screens.login.LoginActivity
import com.example.kafiesta.utilities.setFullscreen
import com.pusher.client.ChannelAuthorizer
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import timber.log.Timber
import java.nio.channels.Channel

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val hideStatusBar: Boolean
    protected abstract val showBackButton: Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(ThemeConst.ColorTheme)
        if (hideStatusBar) {
            setFullscreen(this)
        } else {
            if (showBackButton) {
                if (supportActionBar != null) {
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    supportActionBar!!.setDisplayShowHomeEnabled(true)
                }
            }
        }
    }

    open fun proceedToActivity(setClass: Class<*>, finish: Boolean = false) {
        val intent = Intent(this, setClass)
        startActivity(intent)
        if (finish) {
            finish()
        }
    }

    open fun proceedToActivity(intent: Intent, finish: Boolean = false) {
        startActivity(intent)
        if (finish) {
            finish()
        }
    }

    open fun proceedToLogout() {
        KaFiestaApplication.taskActivityIsOpen = false
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

//    fun initPusher(): com.pusher.client.channel.Channel {
//        val options = PusherOptions()
//        options.setCluster(PusherConst.PUSHER_CLUSTER)
//
//        val pusher = Pusher(PusherConst.PUSHER_API_KEY, options)
//
//        pusher.connect(object : ConnectionEventListener {
//            override fun onConnectionStateChange(change: ConnectionStateChange?) {
//                Timber.d("State changed from ${change!!.previousState} to ${change.currentState}")
//            }
//
//            override fun onError(message: String?, code: String?, e: Exception?) {
//                Timber.d("There was a problem connecting! code ($code), message ($message), exception($e)")
//            }
//        }, ConnectionState.ALL)
//
//        return pusher.subscribe(PusherConst.PUSHER_MY_CHANNEL)
//    }
}