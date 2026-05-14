package com.xymfb.formula;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Singleton
public class FormulaEngine {
    private static final Logger logger = LoggerFactory.getLogger(FormulaEngine.class);
    private Context context;

    @PostConstruct
    public void init() {
        logger.info("初始化GraalVM JavaScript引擎...");
        context = Context.newBuilder("js")
                .allowAllAccess(true)
                .build();
        loadFormulaJS();
        logger.info("GraalVM JavaScript引擎初始化完成");
    }

    private void loadFormulaJS() {
        try (InputStream is = getClass().getResourceAsStream("/formula.min.js")) {
            if (is != null) {
                String formulaJSScript = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                context.eval("js", formulaJSScript);
                logger.info("Formula.js 加载成功");
            } else {
                logger.error("无法找到 formula.min.js");
                throw new RuntimeException("无法找到 formula.min.js");
            }
        } catch (IOException e) {
            logger.error("加载Formula.js失败", e);
            throw new RuntimeException("加载Formula.js失败", e);
        }
    }

    public CalculationResult calculate(String formula, double a1, double a2) {
        try {
            String jsCode = """
                (function() {
                    var A1 = $A1$;
                    var A2 = $A2$;
                    var expr = '$FORMULA$';
                    expr = expr.replace(/，/g, ',');
                    try {
                        var result = FORMULAS.EVALUATE(expr, {A1: A1, A2: A2});
                        return {success: true, result: result};
                    } catch (e) {
                        return {success: false, error: e.message};
                    }
                })()
                """.replace("$A1$", String.valueOf(a1))
                .replace("$A2$", String.valueOf(a2))
                .replace("$FORMULA$", escapeFormula(formula));

            Value result = context.eval("js", jsCode);
            
            boolean success = result.getMember("success").asBoolean();
            if (success) {
                Object value = convertValue(result.getMember("result"));
                return new CalculationResult(true, value, null);
            } else {
                String error = result.getMember("error").asString();
                return new CalculationResult(false, null, error);
            }
        } catch (Exception e) {
            logger.error("公式计算失败", e);
            return new CalculationResult(false, null, e.getMessage());
        }
    }

    private String escapeFormula(String formula) {
        return formula.replace("\\", "\\\\").replace("'", "\\'");
    }

    private Object convertValue(Value value) {
        if (value.isNull()) return null;
        if (value.isBoolean()) return value.asBoolean();
        if (value.isNumber()) {
            if (value.fitsInInt()) return value.asInt();
            if (value.fitsInLong()) return value.asLong();
            double d = value.asDouble();
            if (d == Math.floor(d) && !Double.isInfinite(d)) {
                return (long) d;
            }
            return d;
        }
        if (value.isString()) return value.asString();
        return value.toString();
    }

    @PreDestroy
    public void cleanup() {
        if (context != null) {
            context.close();
        }
    }

    public static class CalculationResult {
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
