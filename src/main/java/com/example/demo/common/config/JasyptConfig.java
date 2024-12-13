package com.example.demo.common.config;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    private String encryptKey;

    @Bean(name = "jasyptEncryptor")
    public PooledPBEStringEncryptor encryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(encryptKey); // 암호화 키
        config.setAlgorithm("PBEWithHMACSHA512AndAES_256"); // 강력한 암호화 알고리즘 (기존 PBEWithMD5AndDES)
        config.setKeyObtentionIterations("20000"); // 반복 해싱 회수 증가 (기존 1000)
        config.setPoolSize("4"); // 병렬 처리 풀 크기 (기존 1)
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스 default
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }

}
