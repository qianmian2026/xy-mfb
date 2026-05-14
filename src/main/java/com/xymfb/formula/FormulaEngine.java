package com.xymfb.formula;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

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
        loadFormulaJs();
        logger.info("GraalVM JavaScript引擎初始化完成");
    }

    private String loadFormulaJs() {
        try {
            // 尝试多种类加载器（Native Image 中更可靠）
            InputStream is = null;
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl != null) {
                is = cl.getResourceAsStream("js/formula.js");
            }
            if (is == null) {
                is = getClass().getResourceAsStream("/js/formula.js");
            }
            if (is == null) {
                is = ClassLoader.getSystemResourceAsStream("js/formula.js");
            }
            if (is == null) {
                logger.error("formula.js not found in any classloader");
                return null;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            logger.error("Failed to load formulajs.js", e);
            return null;
        }
    }

    public CalculationResult calculate(String formula, double a1, double a2) {
        try {
            // 1. 准备参数
            String safeFormula = formula.replace("\\", "\\\\"); // 简单转义反斜杠

            // 2. 构建 JavaScript 调用代码
            String jsCode = String.format("""
            (function() {
                var A1 = %f;
                var A2 = %f;
                var expr = "%s";
                try {
                    // 解析表达式并调用对应的 Formula.js 函数
                    // 注意：formulajs.SUM, formulajs.IF 等函数是同步的
                    var result = eval(expr);
                    return {success: true, result: result};
                } catch (e) {
                    return {success: false, error: e.message};
                }
            })()
            """, a1, a2, safeFormula);

            // 3. 执行并处理结果
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
            e.printStackTrace();
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
