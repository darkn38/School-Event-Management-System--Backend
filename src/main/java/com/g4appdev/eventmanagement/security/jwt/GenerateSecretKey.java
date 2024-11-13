package com.g4appdev.eventmanagement.security.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class GenerateSecretKey {
    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Generates a key suitable for HS512
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Base64-encoded secret key: " + base64Key);
    }
}
