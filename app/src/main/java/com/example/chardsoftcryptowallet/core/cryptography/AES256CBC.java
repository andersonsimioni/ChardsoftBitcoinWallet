package com.example.chardsoftcryptowallet.core.cryptography;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.time.LocalDateTime;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256CBC {
    private static final byte[] ENCRYPTION_BASE_KEY = "G`$pijtg230hg2bg9ugfbsogrg".getBytes();
    private static final byte[] ENCRYPTION_BASE_IV = "G($(*%(@)#*&$WR40e5yu04ehng9noger".getBytes();
    private final byte[] PASS_KEY;

    /**
     * Encode data text into cipher hex data
     * @param src
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String encrypt(String src) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec initialVector = new IvParameterSpec(getInitialVector());
            SecretKeySpec key = new SecretKeySpec(get256BitsEncryptionKey(), "AES");

            cipher.init(Cipher.ENCRYPT_MODE, key, initialVector);
            return BytesOperator.bytesToHex(cipher.doFinal(src.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decode cipher data from hex string and return original text string
     * @param src
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String decrypt(String src) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec initialVector = new IvParameterSpec(getInitialVector());
            SecretKeySpec key = new SecretKeySpec(get256BitsEncryptionKey(), "AES");

            cipher.init(Cipher.DECRYPT_MODE, key, initialVector);
            byte[] decrypted = cipher.doFinal(BytesOperator.hexToBytes(src));
            return BytesOperator.bytesToString(decrypted);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate 256bis random information with pass key
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private byte[] get256BitsEncryptionKey() throws NoSuchAlgorithmException {
        int len = PASS_KEY.length;
        int sum = 0;
        int mult = 0;

        for (int i = 0; i < len; i++){
            sum += PASS_KEY[i];
            mult *= PASS_KEY[i];
        }

        int year = 2021;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            year = LocalDateTime.now().getYear();
        }
        int salt = (int) len * sum * mult * year;
        byte[] concatKey = (BytesOperator.bytesToString(ENCRYPTION_BASE_KEY) + BytesOperator.bytesToString(PASS_KEY)).getBytes();

        SHA256 sha256Alg = new SHA256();

        byte[] newKey = sha256Alg.compute(concatKey, salt);

        return newKey;
    }

    /**
     * Derive initial vector to encryption
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private byte[] getInitialVector() throws NoSuchAlgorithmException {
        int len = PASS_KEY.length;
        int sum = 0;
        int mult = 0;

        for (int i = 0; i < len; i++){
            sum += PASS_KEY[i];
            mult *= PASS_KEY[i];
        }

        int year = 2021;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            year = LocalDateTime.now().getYear();
        }
        int salt = (int) len * sum * mult * year;
        byte[] concatKey = (BytesOperator.bytesToString(ENCRYPTION_BASE_KEY) + BytesOperator.bytesToString(PASS_KEY)).getBytes();

        MD5 md5Alg = new MD5();

        byte[] newKey = md5Alg.compute(concatKey, salt);

        return newKey;
    }

    public AES256CBC(String pass_key){
        this.PASS_KEY = pass_key.getBytes();
    }
}