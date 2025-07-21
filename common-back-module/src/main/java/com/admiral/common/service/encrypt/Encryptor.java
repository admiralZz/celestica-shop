package com.admiral.common.service.encrypt;

public interface Encryptor {
    String encrypt(String value);
    String decrypt(String encryptedValue);
}
