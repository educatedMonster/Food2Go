package com.example.kafiesta.utilities.helpers

class GlobalDialogClicker(val click: () -> Unit) {
    fun onClick() = click()
}

class RecyclerClick(
    val click: (Any) -> Unit,
) {
    fun onClick(model: Any) = click(model)
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