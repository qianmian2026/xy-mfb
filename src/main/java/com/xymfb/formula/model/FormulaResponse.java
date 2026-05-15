package com.xymfb.formula.model;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class FormulaResponse {
    private boolean success;
    private Object result;
    private String error;

    public FormulaResponse() {
    }

    public FormulaResponse(boolean success, Object result, String error) {
        this.success = success;
        this.result = result;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
