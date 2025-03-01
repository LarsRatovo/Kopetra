package com.ratovo.KitapoMbola.port;

import com.ratovo.KitapoMbola.domain.ValidationCode;
import com.ratovo.KitapoMbola.enumeration.ValidationCodeType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public interface ValidationCodeRepository {
    ValidationCode generateCode(ValidationCodeType type);
    void save(ValidationCode code);
    void invalidateCodeSentTo(String targetUuid);
    ValidationCode findByCode(String code,String targetUuid);

    static String generateCode(String key,long timestamp) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        mac.init(keySpec);
        byte[] hash = mac.doFinal(String.valueOf(timestamp).getBytes());
        return Base64.getEncoder().encodeToString(hash).substring(0,6);
    }

    static String getMailContent(String code){
        ClassLoader classLoader = ValidationCodeRepository.class.getClassLoader();
        try(InputStream inputStream = classLoader.getResourceAsStream("static/mail.html")){
            if(inputStream == null)throw new IOException();
            return new String(inputStream.readAllBytes()).replace("{{code}}",code);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
