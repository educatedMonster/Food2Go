package com.example.kafiesta

import android.app.Application
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import com.facebook.stetho.Stetho
import timber.log.Timber

class KaFiestaApplication : Application() {
    companion object {
        var taskActivityIsOpen = false
    }

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     *
     * This where I can initialize all the needs I needed to do to this app
     *
     * Where this class is attached to our android manifest
     */
    override fun onCreate() {
        super.onCreate()
        /**
         * This is for logging
         */
        Timber.plant(Timber.DebugTree())
        /**
         * This is use to see the dataResult on our database
         */
        Stetho.initializeWithDefaults(this)

//        FirebaseApp.initializeApp(applicationContext)
//        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }
}