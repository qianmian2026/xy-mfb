package com.xymfb.formula.service.impl;

import com.xymfb.formula.config.FormulaEngine;
import com.xymfb.formula.service.FormulaService;
import jakarta.inject.Singleton;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class FormulaServiceImpl implements FormulaService {
    private static final Logger logger = LoggerFactory.getLogger(FormulaServiceImpl.class);
    private final FormulaEngine formulaEngine;

    public FormulaServiceImpl(FormulaEngine formulaEngine) {
        this.formulaEngine = formulaEngine;
    }

    @Override
    public CalculationResult evaluate(String formula, double a1, double a2) {
        try {
            String safeFormula = formula.replace("\\", "\\\\");

            String jsCode = String.format("""
            (function() {
                var A1 = %f;
                var A2 = %f;
                var expr = "%s";
                try {
                    var result = eval(expr);
                    return {success: true, result: result};
                } catch (e) {
                    return {success: false, error: e.message};
                }
            })()
            """, a1, a2, safeFormula);

            Value result = formulaEngine.getContext().eval("js", jsCode);
            
            boolean success = result.getMember("success").asBoolean();
            if (success) {
                Object value = formulaEngine.convertValue(result.getMember("result"));
                return new CalculationResult(true, value, null);
            } else {
                String error = result.getMember("error").asString();
                return new CalculationResult(false, null, error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("公式计算失败", e);
            return new CalculationResult(false, null, e.getMessage());
        }
    }
}
