package com.mobilkontrol

import com.mobilkontrol.data.TimeMath
import com.mobilkontrol.security.PinHasher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TimeMathTest {
    @Test
    fun remaining_is_limit_plus_bonus_minus_used() {
        assertEquals(17, TimeMath.remainingMinutes(60, 0, 43))
        assertEquals(32, TimeMath.remainingMinutes(60, 15, 43))
    }

    @Test
    fun remaining_never_negative() {
        assertEquals(0, TimeMath.remainingMinutes(60, 0, 120))
    }

    @Test
    fun fraction_is_clamped() {
        assertEquals(0.5f, TimeMath.usedFraction(60, 0, 30), 0.001f)
        assertEquals(1f, TimeMath.usedFraction(60, 0, 120), 0.001f)
        assertEquals(0f, TimeMath.usedFraction(60, 0, 0), 0.001f)
    }
}

class PinHasherTest {
    @Test
    fun hash_is_stable_and_salted() {
        assertEquals(PinHasher.hash("2580"), PinHasher.hash("2580"))
        assertNotEquals(PinHasher.hash("2580"), "2580")
    }

    @Test
    fun verify_works() {
        assertTrue(PinHasher.verify("1234", PinHasher.hash("1234")))
        assertTrue(!PinHasher.verify("0000", PinHasher.hash("1234")))
    }
}
