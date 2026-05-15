package com.xymfb.formula.model;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class SumRequest {
    private double[] numbers;

    public double[] getNumbers() {
        return numbers;
    }

    public void setNumbers(double[] numbers) {
        this.numbers = numbers;
    }
}
