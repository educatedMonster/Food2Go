package com.example.kafiesta.utilities.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import com.example.kafiesta.R
import com.example.kafiesta.constants.AppConst
import com.example.kafiesta.constants.PusherConst.ORDER_DATA
import com.example.kafiesta.screens.main.MainActivity
import com.example.kafiesta.screens.main.fragment.order.OrderFragment

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
        data: String?,
    ): Notification.Builder? {

        // onClick notification triggers here.
        val intent = Intent(baseContext, MainActivity::class.java)
        intent.putExtra(ORDER_DATA, data)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            baseContext, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

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
        data: String,
        id: Int,
    ) {
        val notificationBuilder =
            getChannelNotification(
                data
            )
        getNotificationManager()?.notify(id, notificationBuilder?.build())
    }


}