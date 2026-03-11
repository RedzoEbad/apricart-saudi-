package com.apricart.consumer.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {

    // 2048 bit public and private keys
    public static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1k08m1AVSxALg1H7oKlMcvudV/nQ8vndz5HGTla5JLgsqOoYysDVpvjjLMQFjozTk1pbL6dHkyWJ6O6i2h93Hq6cATKmEHSAn7jblidpASmT6Le9+1FOUpdTpskAYh2acWh8YUZktfQ/uzVyoTYqF6aLtJACZM89PsLWECzz7vRPtp9SiJP9CP80ezqiDVyNmpSn2kS++PzTOwCSX3/fGDfU6hj3tYMzt7RjTNm8Bufd3v/QmXfSaeu7Y8oQ/ScVD4w56i8lGAT6XoN68+oSYjJPwCrhNFsKChcfKlUgsi4IQPgR+wQ+mxZWQbEQxwQ6WqY6m1srgJk7BLyyLcDFpwIDAQAB";
    public static String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDWTTybUBVLEAuDUfugqUxy+51X+dDy+d3PkcZOVrkkuCyo6hjKwNWm+OMsxAWOjNOTWlsvp0eTJYno7qLaH3cerpwBMqYQdICfuNuWJ2kBKZPot737UU5Sl1OmyQBiHZpxaHxhRmS19D+7NXKhNioXpou0kAJkzz0+wtYQLPPu9E+2n1KIk/0I/zR7OqINXI2alKfaRL74/NM7AJJff98YN9TqGPe1gzO3tGNM2bwG593e/9CZd9Jp67tjyhD9JxUPjDnqLyUYBPpeg3rz6hJiMk/AKuE0WwoKFx8qVSCyLghA+BH7BD6bFlZBsRDHBDpapjqbWyuAmTsEvLItwMWnAgMBAAECggEADo8zyWeA8tcVuSfGPS2TXJJ8p5/40rEoITSJsQPSU0zR9+3jnk7IlS3+nSl6KSunKjEu1cd7JOOdgX5JASG3IZYCUlCDGMYZgQB5prNpvmL5QgvNs/fbpt/SFpg8Vu4k8oSDQsaaDASamdjxP0OjS3UMs8k+6GNuCUgKbBHpxreho/MTrcBic3e8i4PfPAYEmyVzEUYLxSE4DjIPn6dvbiTOloYh3BKNAWaI/v9C0YwkPWLc5emw2E7baJaUonXvStWHRCuwhRk1vaKtPuZkOQ7QtPCErQGkdMZ4SgyXeoWBDpBASMs4eQ/itPfa+7nPegi09Xx3gMGNPYmuM1ZRIQKBgQDh7lZE1f7qob1I8n4B/FgrA1eDxafm9NlTUxvHXUo6gHU+mlD9c+/9hQBizpXvOv/m0KCIg4oUtZFhTx5n1ETw+fxobqaql1bDBTpzxuPfn5iaZ37t+cyMLkVbXXZLmoJcccMKZVIyovqSHhyKj/zgWJ8ShIj8XTNM/4R4lhgYBwKBgQDy0q364KlbM5Y1SWj0XH6dHK7xCkkgn4zoUrbV5EeTp9/AwNV+6nlc5UEEK80U750Atr4iH2Gx+bXB+pFuDTlRmv23OWpeV4dxzVhi4JNfQ8qQiM+8D2NBx8HzJZ89jkkPtFbCeOPLRSk0aG2EI/O0g+Z/2GnjLGv+60PL8mo9YQKBgFG7AoJMV7dRY0QsVOErBEorH1GQAzlNggDeo8HvQZcAOWm4MWw+sKapooYb7alpUoGRH0HDNARh2oG1OW6eH4J9LI4q2SgFL2HFVmvjmzOebHoOzqAQKarcYkK3AMBA2L/Qd5ofJSkkRDjvo6uMgxI1fPYZ8+Q3YwOLu8RTp8upAoGBAJohLnvjJLyWJw8V1L/fAiM59LVdYQOLIsbI0fM8nk5ULYvNGvcfX/+eKUG7h8Z1PcQZOuCv6LQum7dQFAW18R8icU9e62yMxJVw0VyZzhGXFzndDXcCkW9Mm4h55fUNnsSQJM32LCl3cespKfLJW0TGwIV/2d5v8PDzZqpK6jKhAoGBAIk+L80QjorCfnkB4vMAb7bd0qnFpCaMBKt4GNhu3YXHgGZCF9u7iic2RVjYFWBYHymY1tre9IFREjgWzLrSflOI3XAAo/9UM3QXHRgxGMQGkdv77kpzKGYE8Rx/sNjvFcv0B/vWpJHXcKmlHIGbFohA1LIdS+ZfZQe29Sz0pkHD";

    public static String JSZindagiPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiO1lWgkTZeDWQgXlDF8t92YLYZm/ENvCvKPJNuj9WZfGCF5RIUFaYolb/HAhoAHKxgYRUS81WFfHuMROT+B/d0cW+Ii/sqLzTfFjepExonCj1I8m4WLdBAdZCRlWLo+bdO39OpxfK14XaPmRMdb8+uTpZ0hZBhDzZDnXChCm4fgsn63ZT2VEHdHX8PgmKTViR4VXsvyZCkT60FiEix2JdLCuSGF+tPr9GQnlSDJK4vRCZl+/TD/IaIbeAFWcx0Y6kdLpUBBUHbxY8cXcsr/HfJ6/WMEBSlUCOvbZhrx41yC/182WMPppaqCDeDamDV2T+ufzrQkT1nU40gm9h7uoXwIDAQAB";

    public static PublicKey getPublicKey(String base64PublicKey){
        PublicKey publicKey = null;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey){
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static byte[] encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
    }

    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {
        try {
            String encryptedString = Base64.getEncoder().encodeToString(encrypt("test string", publicKey));
            System.out.println(encryptedString);
            String decryptedString = RSAUtil.decrypt(encryptedString, privateKey);
            System.out.println(decryptedString);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }

    }
}
