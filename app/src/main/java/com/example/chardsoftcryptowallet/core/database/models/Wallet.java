package com.example.chardsoftcryptowallet.core.database.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.chardsoftcryptowallet.core.bitcoin.ConnectorP2P;
import com.example.chardsoftcryptowallet.core.cryptography.AES256CBC;

import org.bitcoinj.wallet.UnreadableWalletException;

public class Wallet {
    private int ID;
    private String Name;
    private String CipherBIP39;

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCipherBIP39(String cipherBIP39) {
        CipherBIP39 = cipherBIP39;
    }

    public String getCipherBIP39() {
        return CipherBIP39;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ConnectorP2P getConnector(String password){
        try {
            AES256CBC cipher = new AES256CBC(password);
            String decodedBIP39 = cipher.decrypt(this.CipherBIP39);
            ConnectorP2P connector = new ConnectorP2P(decodedBIP39);

            return connector;
        }catch (IllegalArgumentException | UnreadableWalletException ex){
            return null;
        }
    }

    public Wallet(int id, String name, String cipherBIP39) {
        ID = id;
        Name = name;
        CipherBIP39 = cipherBIP39;
    }
}
