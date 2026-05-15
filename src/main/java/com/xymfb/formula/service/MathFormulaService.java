package com.xymfb.formula.service;

import com.xymfb.formula.config.FormulaEngine;
import jakarta.inject.Singleton;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class MathFormulaService {
    private static final Logger logger = LoggerFactory.getLogger(MathFormulaService.class);
    private final FormulaEngine formulaEngine;

    public MathFormulaService(FormulaEngine formulaEngine) {
        this.formulaEngine = formulaEngine;
    }

    public FormulaService.CalculationResult sum(double[] numbers) {
        try {
            String jsArray = arrayToJs(numbers);
            String jsCode = String.format("""
            (function() {
                try {
                    var result = formulajs.SUM(%s);
                    return {success: true, result: result};
                } catch (e) {
                    return {success: false, error: e.message};
                }
            })()
            """, jsArray);

            Value result = formulaEngine.getContext().eval("js", jsCode);
            boolean success = result.getMember("success").asBoolean();
            if (success) {
                Object value = formulaEngine.convertValue(result.getMember("result"));
                return new FormulaService.CalculationResult(true, value, null);
            } else {
                String error = result.getMember("error").asString();
                return new FormulaService.CalculationResult(false, null, error);
            }
        } catch (Exception e) {
            logger.error("SUM计算失败", e);
            return new FormulaService.CalculationResult(false, null, e.getMessage());
        }
    }

    private String arrayToJs(double[] numbers) {
        if (numbers == null || numbers.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < numbers.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(numbers[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}
