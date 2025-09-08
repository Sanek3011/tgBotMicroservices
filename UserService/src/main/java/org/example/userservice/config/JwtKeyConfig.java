package org.example.userservice.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;


import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.Security;

@Configuration
@RequiredArgsConstructor
public class JwtKeyConfig {

    private final ResourceLoader resourceLoader;

    @PostConstruct
    public void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Bean
    public PrivateKey privateKey(@Value("${jwt.privateKey}") String path) {
        Resource resource = resourceLoader.getResource(path);
        try (PEMParser pemParser = new PEMParser(new InputStreamReader(resource.getInputStream()))) {
            Object o = pemParser.readObject();
            return new JcaPEMKeyConverter()
                    .setProvider("BC")
                    .getKeyPair((PEMKeyPair) o)
                    .getPrivate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public PrivateKey refreshKey(@Value("${jwt.refreshKey}") String path) {
        try (PEMParser pemParser = new PEMParser(new InputStreamReader(resourceLoader.getResource(path).getInputStream()))) {
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
