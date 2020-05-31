package com.linchtech.boot.starter.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 密码加密
 *
 * @author: 107
 * @date: 2019-03-19 17:30
 */
public class EncryptUtil {
    private static final int HASH_ITERATIONS = 1000;
    private static final String EMPTY_STRING = "";
    private static final int HASH_KEY_LENGTH = 192;
    private static final int DEFAULT_SALT_SIZE = 32;
    private static final int SALT_PASS_LENGTH = 2;

    public static boolean isEmpty(String source) {
        return (source == null || "".equals(source));
    }

    private static String generateSalt() {
        Random r = new SecureRandom();
        byte[] saltBinary = new byte[DEFAULT_SALT_SIZE];
        r.nextBytes(saltBinary);
        return Base64.encodeBase64String(saltBinary);
    }

    private static String hashPasswordAddingSalt(String password, byte[] salt) {
        if (isEmpty(password)) {
            return EMPTY_STRING;
        }
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey key = secretKeyFactory.generateSecret(new PBEKeySpec(
                    password.toCharArray(), salt, HASH_ITERATIONS, HASH_KEY_LENGTH)
            );
            return Base64.encodeBase64String(key.getEncoded());
        } catch (Exception e) {
            return EMPTY_STRING;
        }
    }

    /**
     * 密码加密
     * @param password
     * @return
     */
    public static String hashPasswordAddingSalt(String password) {
        byte[] salt = generateSalt().getBytes();
        return Base64.encodeBase64String(salt) + '$' + hashPasswordAddingSalt(password, salt);
    }

    /**
     * 验证密码是否正确
     * @param password
     * @param hashedPassword
     * @return
     */
    public static boolean isValidPassword(String password, String hashedPassword) {
        String[] saltAndPass = hashedPassword.split("\\$");
        if (saltAndPass.length != SALT_PASS_LENGTH) {
            throw new IllegalStateException(
                    "The stored password have the form 'salt$hash'");
        }
        String hashOfInput = hashPasswordAddingSalt(password, Base64.decodeBase64(saltAndPass[0]));
        return hashOfInput.equals(saltAndPass[1]);
    }
}