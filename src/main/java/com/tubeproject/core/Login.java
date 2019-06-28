package com.tubeproject.core;

import com.tubeproject.crypto.Cryptographic;

public class Login {
    public static boolean checkPassword(String password, String hashPassword, String salt) {
        return Cryptographic.verifyPassword(password, hashPassword, salt);
    }
}
