package com.example.chardsoftcryptowallet.core.database.DAO;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.chardsoftcryptowallet.core.database.RepositoryContext;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Transaction {
    private final RepositoryContext Repository;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<com.example.chardsoftcryptowallet.core.database.models.Transaction> getAll(){
        SQLiteDatabase conn = this.Repository.getWritableDatabase();
        ArrayList<com.example.chardsoftcryptowallet.core.database.models.Transaction> transactions = new ArrayList<>();

        try{
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM transaction");

            Cursor dados = conn.rawQuery(query.toString(), null);
            while (dados.moveToNext()){
                transactions.add(new com.example.chardsoftcryptowallet.core.database.models.Transaction(
                        dados.getString(dados.getColumnIndex("id")),
                        dados.getString(dados.getColumnIndex("from")),
                        dados.getString(dados.getColumnIndex("to")),
                        dados.getFloat(dados.getColumnIndex("value")),
                        LocalDateTime.parse(dados.getString(dados.getColumnIndex("registerDate"))),
                        dados.getType(dados.getColumnIndex("success")) == 1
                ));
            }

        }catch (SQLException sqlException){
            //Logger.log(sqlException)
        }finally {
            conn.close();
            return transactions;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public com.example.chardsoftcryptowallet.core.database.models.Transaction getById(String id){
        for(com.example.chardsoftcryptowallet.core.database.models.Transaction t : getAll())
            if(t.getId() == id)
                return t;

        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public com.example.chardsoftcryptowallet.core.database.models.Transaction getByFrom(String name){
        for(com.example.chardsoftcryptowallet.core.database.models.Transaction t : getAll())
            if(t.getFrom().equals(name))
                return t;

        return null;
    }

    public void deleteById(int id){
        SQLiteDatabase conn = this.Repository.getConnection();

        try{
            StringBuilder query = new StringBuilder();
            query.append("DELETE FROM transaction WHERE id = "+ id);
            conn.execSQL(query.toString());

        }catch (SQLException sqlException){
            //Logger.log(sqlException)
        }finally {
            conn.close();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean insertOrUpdate(com.example.chardsoftcryptowallet.core.database.models.Transaction transaction){
        boolean existTransaction = getById(transaction.getId()) == null;
        SQLiteDatabase conn = this.Repository.getConnection();

        try{
            StringBuilder query = new StringBuilder();

            if(existTransaction)
                query.append("INSERT INTO transaction VALUES('"+transaction.getId()+"','"+transaction.getFrom()+"','"+transaction.getTo()+"',"+transaction.getValue()+",'"+transaction.getDatetime().toString()+"')");
            else{
                query.append("UPDATE transaction ");
                query.append("SET from = '" + transaction.getFrom() + "', ");
                query.append("to = '" + (transaction.getTo()) + "', ");
                query.append("value = '" + (transaction.getValue()) + "', ");
                query.append("registerDate = '" + (transaction.getDatetime().toString()) + "', ");
                query.append("success = " + (transaction.isSuccess() ? "1":"0") + " ");
                query.append("WHERE id = '" + transaction.getId()+"'");
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
            return true;
        }
    }


    private void createTable(){
        SQLiteDatabase conn = this.Repository.getConnection();

        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE transaction ");
        query.append("(id VARCHAR(100) PRIMARY KEY, ");
        query.append("from VARCHAR(100), ");
        query.append("to VARCHAR(100), ");
        query.append("value DOUBLE, ");
        query.append("registerDate DATETIME, ");
        query.append("success TINYINT) ");

        try{
            conn.execSQL(query.toString());
        }catch (SQLException sqlException){
            //Logger.log(sqlException)
            SQLException e = sqlException;
        }finally {
            conn.close();
        }
    }

    public Transaction(RepositoryContext repository){
        this.Repository = repository;
        createTable();
    }
}
