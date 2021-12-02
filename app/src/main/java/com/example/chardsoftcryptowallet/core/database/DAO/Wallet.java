package com.example.chardsoftcryptowallet.core.database.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.chardsoftcryptowallet.core.database.RepositoryContext;

import java.util.ArrayList;

public class Wallet {
    private final RepositoryContext Repository;

    public ArrayList<com.example.chardsoftcryptowallet.core.database.models.Wallet> getWallets(){
        SQLiteDatabase conn = this.Repository.getWritableDatabase();
        ArrayList<com.example.chardsoftcryptowallet.core.database.models.Wallet> wallets = new ArrayList<com.example.chardsoftcryptowallet.core.database.models.Wallet>();

        try{
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM wallet");

            Cursor dados = conn.rawQuery(query.toString(), null);
            while (dados.moveToNext()){
                wallets.add(new com.example.chardsoftcryptowallet.core.database.models.Wallet(
                        dados.getInt(dados.getColumnIndex("id")),
                        dados.getString(dados.getColumnIndex("name")),
                        dados.getString(dados.getColumnIndex("cipherBIP39"))
                ));
            }

        }catch (SQLException sqlException){
            //Logger.log(sqlException)
        }finally {
            conn.close();
            return wallets;
        }
    }

    public com.example.chardsoftcryptowallet.core.database.models.Wallet getWalletById(int id){
        for(com.example.chardsoftcryptowallet.core.database.models.Wallet w : getWallets())
            if(w.getID() == id)
                return w;

        return null;
    }

    public com.example.chardsoftcryptowallet.core.database.models.Wallet getWalletByName(String name){
        for(com.example.chardsoftcryptowallet.core.database.models.Wallet w : getWallets())
            if(w.getName().equals(name))
                return w;

        return null;
    }

    public void deleteWalletById(int id){
        SQLiteDatabase conn = this.Repository.getConnection();

        try{
            StringBuilder query = new StringBuilder();
            query.append("DELETE FROM wallet WHERE id = "+ id);
            conn.execSQL(query.toString());

        }catch (SQLException sqlException){
            //Logger.log(sqlException)
        }finally {
            conn.close();
        }
    }

    public boolean insertOrUpdate(com.example.chardsoftcryptowallet.core.database.models.Wallet wallet){
        boolean existWallet = getWalletById(wallet.getID()) == null;
        SQLiteDatabase conn = this.Repository.getConnection();

        try{
            StringBuilder query = new StringBuilder();

            if(existWallet)
                query.append("INSERT INTO wallet VALUES(NULL,'"+wallet.getName()+"','"+wallet.getCipherBIP39()+"')");
            else{
               query.append("UPDATE wallet ");
               query.append("SET name = '" + wallet.getName() + "', ");
                query.append("cipherBIP39 = '" + (wallet.getCipherBIP39()) + "', ");
                query.append("WHERE id = " + wallet.getID());
            }
            conn.execSQL(query.toString());

        }catch (SQLException sqlException){
            //Logger.log(sqlException)
            conn.close();
            return false;
        }
        catch (Exception ex){
            //Logger.log(ex)
            conn.close();
            return false;
        }
        finally {
            conn.close();
            ArrayList<com.example.chardsoftcryptowallet.core.database.models.Wallet> wallets = getWallets();
            if(wallets.size() > 0)
                wallet.setID(wallets.get(wallets.size()-1).getID());

            return true;
        }
    }

    private void createTable(){
        SQLiteDatabase conn = this.Repository.getConnection();

        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE wallet ");
        query.append("(id INTEGER PRIMARY KEY, ");
        query.append("name VARCHAR(50), ");
        query.append("cipherBIP39 VARCHAR(100000))");

        try{
            conn.execSQL(query.toString());
        }catch (SQLException sqlException){
            //Logger.log(sqlException)
            SQLException e = sqlException;
        }finally {
            conn.close();
        }
    }

    public Wallet(RepositoryContext repository){
        this.Repository = repository;
        createTable();
    }
}
