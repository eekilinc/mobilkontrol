package com.mobilkontrol.security

import java.security.MessageDigest

object PinHasher {
    private const val SALT = "mobilkontrol-v1-static-salt"

    fun hash(pin: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest((SALT + pin).toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun verify(pin: String, expectedHash: String): Boolean = hash(pin) == expectedHash
}
