package com.vidaplus.sghss.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

/**
 * Utilitario simples de criptografia simetrica (AES) usado para protegerdados sensiveis
 */
@Component
public class AesEncryptor {

    private static final String ALGORITHM = "AES";

    @Value("${app.encryption.secret-key}")
    private String secretKey;

    public String encrypt(String valorPuro) {
        if (valorPuro == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, buildKey());
            byte[] criptografado = cipher.doFinal(valorPuro.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(criptografado);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Erro ao criptografar dado sensivel", e);
        }
    }

    public String decrypt(String valorCriptografado) {
        if (valorCriptografado == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, buildKey());
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(valorCriptografado));
            return new String(original, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Erro ao descriptografar dado sensivel", e);
        }
    }

    private SecretKeySpec buildKey() {
        byte[] chave = secretKey.getBytes(StandardCharsets.UTF_8);
        // AES exige chave de 16, 24 ou 32 bytes
        byte[] chave16 = new byte[16];
        System.arraycopy(chave, 0, chave16, 0, Math.min(chave.length, 16));
        return new SecretKeySpec(chave16, ALGORITHM);
    }
}
