package com.ntt.client.utils;

import java.util.Base64;

public class Base64Encoder {

    public static String encode(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    public static String decode(String encodedPassword) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
        return new String(decodedBytes);
    }

    public static void main(String[] args) {
        String password = "mySecretPassword";
        String encodedPassword = encode(password);
        System.out.println("Encoded Password: " + encodedPassword);
        String decodedPassword = decode(encodedPassword);
        System.out.println("Decoded Password: " + decodedPassword);
    }
}
