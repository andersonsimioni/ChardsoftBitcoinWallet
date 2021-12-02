package com.example.chardsoftcryptowallet.core.cryptography;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class BytesOperator {

    /**
     * Convert hex string into byte array
     * @param hex
     * @return
     */
    public static byte[] hexToBytes(String hex){
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Convert byte array into hex string
     * @param byteArray
     * @return
     */
    public static String bytesToHex(byte[] byteArray){
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }

        return new String(hexChars);
    }

    /**
     * Convert byte array to UTF8 string
     * @param byteArray
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String bytesToString(byte[] byteArray){
        return new String(byteArray, StandardCharsets.UTF_8);
    }
}
