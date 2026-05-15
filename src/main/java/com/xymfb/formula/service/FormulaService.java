package com.xymfb.formula.service;

public interface FormulaService {
    CalculationResult evaluate(String formula, double a1, double a2);

    class CalculationResult {
        private final boolean success;
        private final Object result;
        private final String error;

        public CalculationResult(boolean success, Object result, String error) {
            this.success = success;
            this.result = result;
            this.error = error;
        }

        public boolean isSuccess() {
            return success;
        }

        public Object getResult() {
            return result;
        }

        public String getError() {
            return error;
        }
    }
}
