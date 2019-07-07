package com.tubeproject.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CryptographicTest {

    @Test
    void verifyPassword() {
        String salt = Cryptographic.generateSalt(512).get();
        String password = "patate";
        String hash = Cryptographic.hashPassword(password, salt).get();
        assertTrue(Cryptographic.verifyPassword(password, hash, salt));
    }

    @Test
    void falsePassword() {
        String salt = Cryptographic.generateSalt(512).get();
        String password = "werleh";
        String hash = Cryptographic.hashPassword(password, salt).get();
        assertFalse(Cryptographic.verifyPassword("werloh", hash, salt));
    }
}