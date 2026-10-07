package com.mobilkontrol.data

object TimeMath {
    fun remainingMinutes(dailyLimit: Int, bonus: Int, used: Int): Int =
        (dailyLimit + bonus - used).coerceAtLeast(0)

    fun usedFraction(dailyLimit: Int, bonus: Int, used: Int): Float {
        val total = (dailyLimit + bonus).coerceAtLeast(1)
        return (used.toFloat() / total).coerceIn(0f, 1f)
    }
}
