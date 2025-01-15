package com.example.demo.common.config;

import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
class JasyptConfigTest {

    @DisplayName("암복호화테스트")
    @Test
    void jasypt(){
        String value = "암호화할값";

        String encryptValue = jasyptEncrypt(value);
        String decryptValue = jasyptDecryt(encryptValue);

        System.out.println("암호화할값 : " + decryptValue);
        System.out.println("암호화된값 : " + encryptValue);

        Assertions.assertThat(value).isEqualTo(jasyptDecryt(encryptValue));
    }

    // 암호화 코드
    private String jasyptEncrypt(String input) {
        String key = "keyValue";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(key);
        config.setPoolSize("1");
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setStringOutputType("base64");
        config.setKeyObtentionIterations("1000");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        encryptor.setConfig(config);
        return encryptor.encrypt(input);

    }
    // 복호화 코드
    private String jasyptDecryt(String input){
        String key = "keyValue";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(key);
        config.setPoolSize("1");
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setStringOutputType("base64");
        config.setKeyObtentionIterations("1000");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        encryptor.setConfig(config);
        return encryptor.decrypt(input);
    }
}