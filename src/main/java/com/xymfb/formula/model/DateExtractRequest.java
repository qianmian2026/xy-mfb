package com.xymfb.formula.model;

import io.micronaut.core.annotation.Introspected;

import java.util.Date;

@Introspected
public class DateExtractRequest {
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
