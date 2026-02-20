package com.example.urduenglishkeyboard.keyboard

data class KeyData(
    val code: Int,
    val label: String,
    val shiftLabel: String = "",
    val isFunctional: Boolean = false,
    val weight: Float = 1f,
    val longPressOptions: List<String> = emptyList()
)
