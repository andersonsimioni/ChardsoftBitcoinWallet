package com.example.chardsoftcryptowallet.core.cryptography;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    private byte[] ComputedData;

    /**
     * Compute SHA256 hash without salt
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public byte[] Compute(String data) throws NoSuchAlgorithmException {
        byte[] byteData = data.getBytes();

        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        byte[] hash = digester.digest(byteData);

        this.ComputedData = hash;
        return this.ComputedData;
    }

    /**
     * Compute SHA256 hash with salts
     * @return
     * @throws NoSuchAlgorithmException
     */
    public byte[] compute(String data, int salt) throws NoSuchAlgorithmException {
        byte[] byteData = data.getBytes();

        MessageDigest digester = MessageDigest.getInstance("SHA-256");

        byte[] hash = digester.digest(byteData);
        for (int i = 1; i < salt; i++)
            hash = digester.digest(hash);

        this.ComputedData = hash;
        return this.ComputedData;
    }

    /**
     * Compute SHA256 hash without salt
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public byte[] compute(byte[] data) throws NoSuchAlgorithmException {
        byte[] byteData = data;

        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        byte[] hash = digester.digest(byteData);

        this.ComputedData = hash;
        return this.ComputedData;
    }

    /**
     * Compute SHA256 hash with salts
     * @return
     * @throws NoSuchAlgorithmException
     */
    public byte[] compute(byte[] data, int salt) throws NoSuchAlgorithmException {
        byte[] byteData = data;

        MessageDigest digester = MessageDigest.getInstance("SHA-256");

        byte[] hash = digester.digest(byteData);
        for (int i = 1; i < salt; i++)
            hash = digester.digest(hash);

        this.ComputedData = hash;
        return this.ComputedData;
    }
}
