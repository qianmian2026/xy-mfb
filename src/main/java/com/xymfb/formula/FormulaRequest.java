package com.xymfb.formula;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class FormulaRequest {
    private String formula;
    private double a1;
    private double a2;

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public double getA1() {
        return a1;
    }

    public void setA1(double a1) {
        this.a1 = a1;
    }

    public double getA2() {
        return a2;
    }

    public void setA2(double a2) {
        this.a2 = a2;
    }
}
