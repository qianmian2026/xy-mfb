package com.xymfb.formula;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        String formulaJSScript = """
            const FORMULAS = {
                SUM: function(...args) {
                    return args.flat(Infinity).reduce((acc, val) => acc + (Number(val) || 0), 0);
                },
                AVERAGE: function(...args) {
                    const flat = args.flat(Infinity);
                    const sum = flat.reduce((acc, val) => acc + (Number(val) || 0), 0);
                    return sum / flat.length;
                },
                MAX: function(...args) {
                    return Math.max(...args.flat(Infinity).map(v => Number(v) || 0));
                },
                MIN: function(...args) {
                    return Math.min(...args.flat(Infinity).map(v => Number(v) || 0));
                },
                COUNT: function(...args) {
                    return args.flat(Infinity).filter(v => v !== null && v !== undefined && v !== '').length;
                },
                IF: function(condition, trueVal, falseVal) {
                    return condition ? trueVal : falseVal;
                },
                ROUND: function(num, decimals) {
                    const factor = Math.pow(10, decimals || 0);
                    return Math.round(Number(num) * factor) / factor;
                },
                ABS: function(num) {
                    return Math.abs(Number(num));
                },
                SQRT: function(num) {
                    return Math.sqrt(Number(num));
                },
                POWER: function(base, exponent) {
                    return Math.pow(Number(base), Number(exponent));
                },
                INT: function(num) {
                    return Math.floor(Number(num));
                },
                MOD: function(number, divisor) {
                    return Number(number) % Number(divisor);
                },
                LEFT: function(text, numChars) {
                    return String(text).substring(0, numChars);
                },
                RIGHT: function(text, numChars) {
                    const str = String(text);
                    return str.substring(str.length - numChars);
                },
                MID: function(text, startNum, numChars) {
                    return String(text).substring(startNum - 1, startNum - 1 + numChars);
                },
                LEN: function(text) {
                    return String(text).length;
                },
                UPPER: function(text) {
                    return String(text).toUpperCase();
                },
                LOWER: function(text) {
                    return String(text).toLowerCase();
                },
                TRIM: function(text) {
                    return String(text).trim();
                },
                CONCAT: function(...args) {
                    return args.join('');
                },
                AND: function(...args) {
                    return args.every(v => v);
                },
                OR: function(...args) {
                    return args.some(v => v);
                },
                NOT: function(value) {
                    return !value;
                },
                TRUE: function() {
                    return true;
                },
                FALSE: function() {
                    return false;
                },
                ISNUMBER: function(value) {
                    return typeof value === 'number' && !isNaN(value);
                },
                ISTEXT: function(value) {
                    return typeof value === 'string';
                },
                ISBLANK: function(value) {
                    return value === null || value === undefined || value === '';
                }
            };

            function parseFormula(formula, variables) {
                let expr = formula;
                
                for (const [key, value] of Object.entries(variables)) {
                    const regex = new RegExp('\\\\b' + key + '\\\\b', 'gi');
                    if (typeof value === 'string') {
                        expr = expr.replace(regex, '"' + value.replace(/"/g, '\\\\"') + '"');
                    } else {
                        expr = expr.replace(regex, value);
                    }
                }
                
                for (const funcName of Object.keys(FORMULAS)) {
                    const regex = new RegExp('\\\\b' + funcName + '\\\\s*\\\\(', 'gi');
                    expr = expr.replace(regex, 'FORMULAS.' + funcName + '(');
                }
                
                expr = expr.replace(/，/g, ',');
                
                return expr;
            }

            function evaluate(formula, variables) {
                try {
                    const parsed = parseFormula(formula, variables);
                    const result = eval(parsed);
                    return { success: true, result: result, parsed: parsed };
                } catch (e) {
                    return { success: false, error: e.message };
                }
            }
            """;
        context.eval("js", formulaJSScript);
    }

    public CalculationResult calculate(String formula, double a1, double a2) {
        try {
            Value evaluateFunc = context.getBindings("js").getMember("evaluate");
            Value vars = context.eval("js", "({A1: " + a1 + ", A2: " + a2 + "})");
            Value result = evaluateFunc.execute(formula, vars);
            
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

    private Object convertValue(Value value) {
        if (value.isNull()) return null;
        if (value.isBoolean()) return value.asBoolean();
        if (value.isNumber()) {
            if (value.fitsInInt()) return value.asInt();
            if (value.fitsInLong()) return value.asLong();
            return value.asDouble();
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
