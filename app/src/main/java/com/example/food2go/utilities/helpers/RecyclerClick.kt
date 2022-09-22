package com.example.food2go.utilities.helpers

import android.view.View

class GlobalDialogClicker(val click: () -> Unit) {
    fun onClick() = click()
}

class RecyclerClick(
    val click: (Any) -> Unit,
) {
    fun onClick(model: Any) = click(model)
}

class RecyclerClick2View(
    val click1: (Any) -> Unit,
    val click2: (Any, View) -> Unit
) {
    fun onClick1(model: Any) = click1(model)
    fun onClick2(model: Any, view: View) = click2(model, view)
}

class AddInventoryRecyclerClick(
    val click: (Any) -> Unit,
    val clickAdd: (Any) -> Unit,
) {
    fun onClick(model: Any) = click(model)
    fun onClickAdd(productId: Long) = clickAdd(productId)
}

class OrderRecyclerClick(
    val accept: (Any) -> Unit,
    val move_delivery: (Any) -> Unit,
    val move_completed: (Any) -> Unit,
    val reject: (Any) -> Unit,
    val proofURL: (Any) -> Unit
) {
    fun onProceedClick(model: Any) = accept(model)
    fun onMoveDeliveryClick(model: Any) = move_delivery(model)
    fun onCompletedClick(model: Any) = move_completed(model)
    fun onRejectClick(model: Any) = reject(model)
    fun onProofURLClick(model: Any) = proofURL(model)
}

class WeeklyPaymentRecyclerClick(
    val click: (Any) -> Unit,
    val proofURL: (Any) -> Unit
) {
    fun onClick(model: Any) = click(model)
    fun onProofURLClick(model: Any) = proofURL(model)
}