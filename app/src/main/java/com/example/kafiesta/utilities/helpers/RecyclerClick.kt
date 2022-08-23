package com.example.kafiesta.utilities.helpers

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
    val reject: (Any) -> Unit
) {
    fun onAcceptClick(model: Any) = accept(model)
    fun onRejectClick(model: Any) = reject(model)
}