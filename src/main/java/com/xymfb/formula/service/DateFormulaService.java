package com.xymfb.formula.service;

import com.xymfb.formula.config.FormulaEngine;
import jakarta.inject.Singleton;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

@Singleton
public class DateFormulaService {
    private static final Logger logger = LoggerFactory.getLogger(DateFormulaService.class);
    private final FormulaEngine formulaEngine;

    public DateFormulaService(FormulaEngine formulaEngine) {
        this.formulaEngine = formulaEngine;
    }

    public FormulaService.CalculationResult date(int year, int month, int day) {
        try {
            String jsCode = String.format("""
            (function() {
                try {
                    var result = formulajs.DATE(%d, %d, %d);
                    return {success: true, result: result.getTime()};
                } catch (e) {
                    return {success: false, error: e.message};
                }
            })()
            """, year, month, day);

            Value result = formulaEngine.getContext().eval("js", jsCode);
            boolean success = result.getMember("success").asBoolean();
            if (success) {
                long timestamp = result.getMember("result").asLong();
                Date date = new Date(timestamp);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return new FormulaService.CalculationResult(true, sdf.format(date), null);
            } else {
                String error = result.getMember("error").asString();
                return new FormulaService.CalculationResult(false, null, error);
            }
        } catch (Exception e) {
            logger.error("DATE计算失败", e);
            return new FormulaService.CalculationResult(false, null, e.getMessage());
        }
    }

    public FormulaService.CalculationResult now() {
        try {
            String jsCode = """
            (function() {
                try {
                    var result = formulajs.NOW();
                    return {success: true, result: result.getTime()};
                } catch (e) {
                    return {success: false, error: e.message};
                }
            })()
            """;

            Value result = formulaEngine.getContext().eval("js", jsCode);
            boolean success = result.getMember("success").asBoolean();
            if (success) {
                long timestamp = result.getMember("result").asLong();
                Date date = new Date(timestamp);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return new FormulaService.CalculationResult(true, sdf.format(date), null);
            } else {
                String error = result.getMember("error").asString();
                return new FormulaService.CalculationResult(false, null, error);
            }
        } catch (Exception e) {
            logger.error("NOW计算失败", e);
            return new FormulaService.CalculationResult(false, null, e.getMessage());
        }
    }

    public FormulaService.CalculationResult today() {
        try {
            String jsCode = """
            (function() {
                try {
                    var result = formulajs.TODAY();
                    return {success: true, result: result.getTime()};
                } catch (e) {
                    return {success: false, error: e.message};
                }
            })()
            """;

            Value result = formulaEngine.getContext().eval("js", jsCode);
            boolean success = result.getMember("success").asBoolean();
            if (success) {
                long timestamp = result.getMember("result").asLong();
                Date date = new Date(timestamp);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return new FormulaService.CalculationResult(true, sdf.format(date), null);
            } else {
                String error = result.getMember("error").asString();
                return new FormulaService.CalculationResult(false, null, error);
            }
        } catch (Exception e) {
            logger.error("TODAY计算失败", e);
            return new FormulaService.CalculationResult(false, null, e.getMessage());
        }
    }

    public FormulaService.CalculationResult year(Date date) {
        try {
            long timestamp = date.getTime();
            String jsCode = String.format("""
            (function() {
                try {
                    var d = new Date(%d);
                    var result = formulajs.YEAR(d);
                    return {success: true, result: result};
                } catch (e) {
                    return {success: false, error: e.message};
                }
            })()
            """, timestamp);

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
            logger.error("YEAR计算失败", e);
            return new FormulaService.CalculationResult(false, null, e.getMessage());
        }
    }

    public FormulaService.CalculationResult month(Date date) {
        try {
            long timestamp = date.getTime();
            String jsCode = String.format("""
            (function() {
                try {
                    var d = new Date(%d);
                    var result = formulajs.MONTH(d);
                    return {success: true, result: result};
                } catch (e) {
                    return {success: false, error: e.message};
                }
            })()
            """, timestamp);

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
            logger.error("MONTH计算失败", e);
            return new FormulaService.CalculationResult(false, null, e.getMessage());
        }
    }

    public FormulaService.CalculationResult day(Date date) {
        try {
            long timestamp = date.getTime();
            String jsCode = String.format("""
            (function() {
                try {
                    var d = new Date(%d);
                    var result = formulajs.DAY(d);
                    return {success: true, result: result};
                } catch (e) {
                    return {success: false, error: e.message};
                }
            })()
            """, timestamp);

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
            logger.error("DAY计算失败", e);
            return new FormulaService.CalculationResult(false, null, e.getMessage());
        }
    }
}
