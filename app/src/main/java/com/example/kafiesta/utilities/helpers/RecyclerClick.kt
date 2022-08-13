package com.example.kafiesta.utilities.helpers

import com.example.kafiesta.domain.TestUnit

class RecyclerClick(
    val click: (Any) -> Unit,
) {
    fun onClick(model: Any) = click(model)
}

class ProductRecyclerClick(
    val addClick: (testUnit: TestUnit?) -> Unit,
    val cancelClick: () -> Unit,
    val editClick: (testUnit: TestUnit) -> Unit,
    val removeClick: (Long) -> Unit,
) {
    fun onAddClick(testUnit: TestUnit? = null) = addClick(testUnit)
    fun onCancelClick() = cancelClick()
    fun onEditClick(testUnit: TestUnit) = editClick(testUnit)
    fun onRemoveClick(id: Long) = removeClick(id)
}

class GlobalDialogClicker(val click: () -> Unit) {
    fun onClick() = click()
}

class AddEditProductDialogClicker(
    val click: (testUnit: TestUnit) -> Unit,
) {
    fun onClick(testUnit: TestUnit) =  click(testUnit)
}

class DeleteProductDialogClicker(
    val click: (prodId: Long) -> Unit,
) {
    fun onClick(prodId: Long) = click(prodId)
}

class CancelProductDialogClicker(val click: () -> Unit) {
    fun onClick() = click()
}