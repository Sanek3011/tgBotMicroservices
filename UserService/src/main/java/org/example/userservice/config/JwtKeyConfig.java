package org.example.userservice.config;

import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.security.PrivateKey;

@Configuration
public class JwtKeyConfig {

    @Bean
    public PrivateKey privateKey(@Value("${jwt.privateKey}") String path) {
        try (PEMParser pemParser = new PEMParser(new FileReader(path))) {
            Object o = pemParser.readObject();
            return new JcaPEMKeyConverter()
                    .setProvider("BC")
                    .getKeyPair((PEMKeyPair) o)
                    .getPrivate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
