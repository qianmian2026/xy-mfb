package com.xymfb.formula.model;

import io.micronaut.core.annotation.Introspected;

import java.util.Date;

@Introspected
public class DateFormatRequest {
    private Date date;
    private String pattern;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
