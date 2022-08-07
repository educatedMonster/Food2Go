package com.example.kafiesta.screens.add_product

import android.os.Bundle
import com.example.kafiesta.R
import com.example.kafiesta.screens.BaseActivity

class ShowProductActivity : BaseActivity() {

    override val hideStatusBar: Boolean get() = false
    override val showBackButton: Boolean get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
    }
}