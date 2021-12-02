package com.example.chardsoftcryptowallet.core.database.models;

import java.time.LocalDateTime;

public class Transaction {
    private final String Id;
    private final String From;
    private final String To;
    private final float Value;
    private final LocalDateTime Datetime;
    private final boolean Success;

    public String getId() {
        return Id;
    }

    public String getFrom() {
        return From;
    }

    public String getTo() {
        return To;
    }

    public float getValue() {
        return Value;
    }

    public LocalDateTime getDatetime() {
        return Datetime;
    }

    public boolean isSuccess() {
        return Success;
    }

    public Transaction(String id, String from, String to, float value, LocalDateTime datetime, boolean success) {
        Id = id;
        From = from;
        To = to;
        Value = value;
        Datetime = datetime;
        Success = success;
    }
}
