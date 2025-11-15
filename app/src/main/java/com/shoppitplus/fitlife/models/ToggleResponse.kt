package com.shoppitplus.fitlife.models

data class ToggleResponse(
    val message: String,
    val toggled_items: List<ToggledItem>
)

data class ToggledItem(
    val id: Int,
    val done: Boolean
)
