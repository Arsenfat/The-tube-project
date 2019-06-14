package com.tubeproject.core;

import com.tubeproject.crypto.Cryptographic;

public class Login {
    public static boolean checkPassword(String password, String hashPassword, String salt) {
        System.out.println("password = [" + password + "], hashPassword = [" + hashPassword + "], salt = [" + salt + "]");
        return Cryptographic.verifyPassword(password, hashPassword, salt);
    }
}
