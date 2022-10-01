package com.example.food2go.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.food2go.Food2GoApplication
import com.example.food2go.constants.ThemeConst
import com.example.food2go.screens.login.LoginActivity
import com.example.food2go.utilities.setFullscreen
import kotlin.jvm.internal.Intrinsics

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val hideStatusBar: Boolean
    protected abstract val showBackButton: Boolean

    private var requestCode = -1
    private var resultHandler: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        registerForActivityResult()
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
        Food2GoApplication.taskActivityIsOpen = false
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun registerForActivityResult() {
        if (shouldRegisterForActivityResult()) {
            resultHandler =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    Intrinsics.checkNotNullExpressionValue(result, "result")
                    onActivityResult(requestCode, result)
                    requestCode = -1
                }
        }
    }

    fun startActivityForResult(requestCode: Int, intent: Intent?) {
        this.requestCode = requestCode
        resultHandler?.launch(intent)
    }

    protected open fun onActivityResult(requestCode: Int, result: ActivityResult) {}

    protected open fun shouldRegisterForActivityResult(): Boolean {
        return false
    }
}