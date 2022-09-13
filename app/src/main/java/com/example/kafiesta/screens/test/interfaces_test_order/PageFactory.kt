package com.example.kafiesta.screens.test.interfaces_test_order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.example.kafiesta.screens.test.pages.OrderCompletedPage
import com.example.kafiesta.screens.test.pages.OrderDeliveryPage
import com.example.kafiesta.screens.test.pages.OrderPendingPage
import com.example.kafiesta.screens.test.pages.OrderPreparingPage

interface PageFactory {
    fun type(orderPendingPage: OrderPendingPage): Int
    fun type(orderPreparingPage: OrderPreparingPage): Int
    fun type(orderDeliveryPage: OrderDeliveryPage): Int
    fun type(orderCompletedPage: OrderCompletedPage): Int

    fun createViewGroup(type: Int, inflater: LayoutInflater, @Nullable parent: ViewGroup): ViewGroup
}