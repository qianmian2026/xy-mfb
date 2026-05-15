package com.xymfb.formula.model;

import io.micronaut.core.annotation.Introspected;

import java.util.Date;

@Introspected
public class DateAddRequest {
    private Date date;
    private int value;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
