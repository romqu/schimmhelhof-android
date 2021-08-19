package de.romqu.schimmelhof_android.data.security

import javax.crypto.Cipher

interface CipherProvider {
    val encryptCipher: Cipher
    fun decryptCipher(iv: ByteArray): Cipher
}