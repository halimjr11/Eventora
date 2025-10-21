package com.halimjr11.eventora.utils

enum class AppTheme { LIGHT, DARK, SYSTEM }

fun String.toAppTheme(): AppTheme = when (this.uppercase()) {
    "LIGHT" -> AppTheme.LIGHT
    "DARK" -> AppTheme.DARK
    "SYSTEM" -> AppTheme.SYSTEM
    else -> AppTheme.SYSTEM
}
