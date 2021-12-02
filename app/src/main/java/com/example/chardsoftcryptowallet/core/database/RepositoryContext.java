package com.example.chardsoftcryptowallet.core.database;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.transition.Transition;

import androidx.annotation.Nullable;

import com.example.chardsoftcryptowallet.core.database.DAO.Transaction;
import com.example.chardsoftcryptowallet.core.database.DAO.Wallet;

import java.util.ArrayList;

public class RepositoryContext extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DbName = "db.ChardsoftWallet";

    public RepositoryContext(@Nullable Context context) {
        super(context, DbName, null, DATABASE_VERSION);
    }

    /**
     * Return database communication object
     * @return
     */
    public SQLiteDatabase getConnection(){
        return this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Get wallets DAO object
     * @return
     */
    public Wallet Wallets(){
        return new Wallet(this);
    }

    /**
     * Get transactions DAO object
     * @return
     */
    public Transaction Transactions(){ return new Transaction(this); }
}
