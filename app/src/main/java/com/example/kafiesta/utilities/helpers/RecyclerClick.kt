package com.example.kafiesta.utilities.helpers

import android.view.View

class RecyclerClick(
    val click: (Any) -> Unit
) {
    fun onClick(model: Any) = click(model)
}

class RecyclerClick2(
    val click1: (Any) -> Unit,
    val click2: (Any) -> Unit
) {
    fun onClick1(model: Any) = click1(model)
    fun onClick2(model: Any) = click2(model)
}

class RecyclerClickView(
    val click: (Any, View) -> Unit
) {
    fun onClick(model: Any, view: View) = click(model, view)
}