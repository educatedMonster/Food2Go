package com.example.food2go.screens.test_order.interfaces_test_order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.example.food2go.screens.test_order.pages.OrderCompletedPage
import com.example.food2go.screens.test_order.pages.OrderDeliveryPage
import com.example.food2go.screens.test_order.pages.OrderPendingPage
import com.example.food2go.screens.test_order.pages.OrderPreparingPage

interface PageFactory {
    fun type(orderPendingPage: OrderPendingPage): Int
    fun type(orderPreparingPage: OrderPreparingPage): Int
    fun type(orderDeliveryPage: OrderDeliveryPage): Int
    fun type(orderCompletedPage: OrderCompletedPage): Int

    fun createViewGroup(type: Int, inflater: LayoutInflater, @Nullable parent: ViewGroup): ViewGroup
}