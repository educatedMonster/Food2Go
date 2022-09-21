package com.example.kafiesta.utilities.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import com.example.kafiesta.R
import com.example.kafiesta.constants.AppConst
import com.example.kafiesta.constants.PusherConst
import com.example.kafiesta.constants.PusherConst.ORDER_ID
import com.example.kafiesta.constants.PusherConst.MERCHANT_ID
import com.example.kafiesta.screens.main.MainActivity
import com.example.kafiesta.screens.splash.SplashActivity
import com.example.kafiesta.screens.weekly_payment.WeeklyPaymentActivity

class NotificationHelper(base: Context?) : ContextWrapper(base) {

    private val CHANNEL_ID = packageName
    private val CHANNEL_NAME = AppConst.app_name_channel
    private var notificationManager: NotificationManager? = null

    init {
        createChannel()
    }


    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.lightColor = Color.YELLOW
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationChannel.setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build()
            )
            getNotificationManager()?.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotificationManager(): NotificationManager? {
        if (notificationManager == null)
            notificationManager =
                baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager
    }


    private fun getChannelNotification(
        action: String?,
        uid: Long?,
        data: String?,
    ): Notification.Builder? {
        // onClick notification triggers here.
        val intent = when (action) {
            PusherConst.PUSHER_ORDER_EVENT  -> {
                val intent = Intent(baseContext, MainActivity::class.java)
                intent.putExtra(ORDER_ID, uid!!)
            }
            PusherConst.PUSHER_TRANSACTION_EVENT  -> {
                val intent = Intent(baseContext, WeeklyPaymentActivity::class.java)  // proceed to WeeklyPaymentActivity when notification received
                intent.putExtra(MERCHANT_ID, uid!!)
            }
            else -> {
                Intent(baseContext, SplashActivity::class.java)
            }
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                baseContext, 0, intent,
    //            PendingIntent.FLAG_ONE_SHOT
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            TODO("VERSION.SDK_INT < M")
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(baseContext, CHANNEL_ID)
//                .setContentTitle(title)
                .setContentText(data)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        } else {
            Notification.Builder(baseContext)
//                .setContentTitle(title)
                .setContentText(data)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        }
    }

    fun displayNotification(
        action: String?,
        uid: Long?,
        data: String,
        id: Int,
    ) {
        val notificationBuilder =
            getChannelNotification(
                action,
                uid!!,
                data
            )
        getNotificationManager()?.notify(id, notificationBuilder?.build())
    }


}