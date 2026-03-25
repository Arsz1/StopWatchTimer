package com.example.timer.domain

internal actual fun getCurrentTimeMillis(): Long {
    return System.currentTimeMillis()
}
