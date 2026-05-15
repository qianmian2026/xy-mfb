package com.xymfb.formula.model;

import io.micronaut.core.annotation.Introspected;

import java.util.Date;

@Introspected
public class DateDiffRequest {
    private Date startDate;
    private Date endDate;
    private String unit;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
