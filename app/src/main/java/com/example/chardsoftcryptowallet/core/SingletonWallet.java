package com.example.chardsoftcryptowallet.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.chardsoftcryptowallet.core.bitcoin.ConnectorP2P;
import com.example.chardsoftcryptowallet.core.cryptography.AES256CBC;
import com.example.chardsoftcryptowallet.core.database.RepositoryContext;
import com.example.chardsoftcryptowallet.core.database.models.Wallet;

import java.util.ArrayList;

public class SingletonWallet {
    private static SingletonWallet SingletonApp;

    private Wallet OpenedWallet;
    private ConnectorP2P UserWalletBtcNetworkConnection;
    private RepositoryContext repositoryContext;

    public boolean isWalletOpen() {
        return UserWalletBtcNetworkConnection != null;
    }

    public ConnectorP2P getUserWalletBtcNetworkConnection() {
        return UserWalletBtcNetworkConnection;
    }

    public boolean saveNewWallet(Wallet wallet){
        return repositoryContext.Wallets().insertOrUpdate(wallet);
    }

    public void  deleteWallet(Wallet wallet){
        this.repositoryContext.Wallets().deleteWalletById(wallet.getID());
    }

    /**
     * Check if user contains saved wallet
     * @return
     */
    public boolean haveSomeWallet(){
        return this.repositoryContext.Wallets().getWallets().size() > 0;
    }

    /**
     * Return all available wallets
     * @return
     */
    public ArrayList<Wallet> getWalletsList(){
        return this.repositoryContext.Wallets().getWallets();
    }

    public String getOpenedWalletSecret(String password){
        if(this.OpenedWallet != null){
            try{
                AES256CBC aes = new AES256CBC(password);
                String cipher = this.OpenedWallet.getCipherBIP39();
                String text = aes.decrypt(cipher);

                return text;
            }
            catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    /**
     * Set current P2P connection to wallet with id
     * @param id
     * @param password
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openWalletById(int id, String password){
        this.OpenedWallet = this.repositoryContext.Wallets().getWalletById(id);
        if(this.OpenedWallet != null){
            this.UserWalletBtcNetworkConnection = this.OpenedWallet.getConnector(password);
            if(this.UserWalletBtcNetworkConnection == null)
                this.OpenedWallet = null;
        }
    }

    public static SingletonWallet getApp(){
        return SingletonApp;
    }

    public static void initializeSingletonApp(Context androidContext){
        SingletonApp = new SingletonWallet(androidContext);
    }

    public SingletonWallet(Context androidContext) {
        this.repositoryContext = new RepositoryContext(androidContext);
    }

    public void lock() {
        this.OpenedWallet = null;
        this.UserWalletBtcNetworkConnection = null;
    }
}
