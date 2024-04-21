package com.example.kafka.utils;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;


public class EncryptionUtil {



    public static <T extends Serializable> byte[] encryptList(List<T> objects,SecretKey secretKey) throws Exception {



        // Initialize AES cipher for encryption
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Serialize the list of objects into a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(objects);
        byte[] serializedObjects = baos.toByteArray();
        oos.close();

        // Encrypt the serialized byte array using the cipher
        byte[] encryptedData = cipher.doFinal(serializedObjects);

        return encryptedData;
    }

    public static <T extends Serializable> List<T> decryptList(byte[] encryptedData, SecretKey secretKey) throws Exception {

        // Initialize AES cipher for decryption
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Decrypt the encrypted byte array
        byte[] decryptedData = cipher.doFinal(encryptedData);

        // Deserialize the decrypted byte array into a list of objects
        ByteArrayInputStream bais = new ByteArrayInputStream(decryptedData);
        ObjectInputStream ois = new ObjectInputStream(bais);
        List<T> decryptedObjects = (List<T>) ois.readObject();
        ois.close();

        return decryptedObjects;
    }

    public static byte[] encrypt(byte[] data, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    // Method to decrypt data using AES encryption
    public static byte[] decrypt(byte[] encryptedData) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, EncryptionUtil.generateAESKey(128));
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0]; // Return empty byte array in case of decryption error
        }
    }


    public static SecretKey generateAESKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keySize); // Initialize the key generator with desired key size (in bits)
        return keyGenerator.generateKey(); // Generate a new AES key
    }

    // Encodes a byte array to a Base64-encoded string
    public static String encodeBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
