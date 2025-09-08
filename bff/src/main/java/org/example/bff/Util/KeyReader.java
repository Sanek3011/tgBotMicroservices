package org.example.bff.Util;

import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

@Component
@RequiredArgsConstructor
public class KeyReader {

    private final ResourceLoader resourceLoader;

    public PublicKey readPublicKey(String path) {
        Resource resource = resourceLoader.getResource(path);
        try (PEMParser pemParser = new PEMParser(new InputStreamReader(resource.getInputStream()))){
            Object o = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            return converter.getPublicKey((SubjectPublicKeyInfo) o);


        } catch (IOException e) {
            throw new RuntimeException();
        }

    }
}
