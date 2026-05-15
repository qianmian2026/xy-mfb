package com.xymfb.formula.service;

import com.xymfb.formula.config.FormulaEngine;
import jakarta.inject.Singleton;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

@Singleton
public class DateCalculationService {
    private static final Logger logger = LoggerFactory.getLogger(DateCalculationService.class);
    private final FormulaEngine formulaEngine;

    public DateCalculationService(FormulaEngine formulaEngine) {
        this.formulaEngine = formulaEngine;
    }

    public FormulaService.CalculationResult dateDiff(Date startDate, Date endDate, String unit) {
        try {
            long startTimestamp = startDate.getTime();
            long endTimestamp = endDate.getTime();
            String jsCode = String.format("""
            (function() {
                try {
                    var start = new Date(%d);
                    var end = new Date(%d);
                    var unit = "%s";
                    var diff;
                    
                    switch(unit.toUpperCase()) {
                        case "D":
                        case "DAY":
                        case "DAYS":
                            diff = formulajs.DAYS(end, start);
                            break;
                        case "M":
                        case "MONTH":
                        case "MONTHS":
                            diff = formulajs.DATEDIF(start, end, "m");
                            break;
                        case "Y":
                        case "YEAR":
                        case "YEARS":
                            diff = formulajs.DATEDIF(start, end, "y");
                            break;
                        case "H":
                        case "HOUR":
                        case "HOURS":
                            diff = (end - start) / (1000 * 60 * 60);
                            break;
                        case "MIN":
                        case "MINUTE":
                        case "MINUTES":
                            diff = (end - start) / (1000 * 60);
                            break;
                        case "S":
                        case "SECOND":
                        case "SECONDS":
                            diff = (end - start) / 1000;
                            break;
                        default:
                            diff = formulajs.DAYS(end, start);
                    }
                    
                    return {success: true, result: diff};
                } catch (e) {
                    return {success: false, error: e.message};
                }
            })()
            """, startTimestamp, endTimestamp, unit);

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
            logger.error("日期差值计算失败", e);
            return new FormulaService.CalculationResult(false, null, e.getMessage());
        }
    }

    public FormulaService.CalculationResult formatDate(Date date, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            String formatted = sdf.format(date);
            return new FormulaService.CalculationResult(true, formatted, null);
        } catch (Exception e) {
            logger.error("日期格式化失败", e);
            return new FormulaService.CalculationResult(false, null, e.getMessage());
        }
    }

    public FormulaService.CalculationResult addDays(Date date, int days) {
        try {
            long timestamp = date.getTime();
            String jsCode = String.format("""
            (function() {
                try {
                    var d = new Date(%d);
                    d.setDate(d.getDate() + %d);
                    return {success: true, result: d.getTime()};
                } catch (e) {
                    return {success: false, error: e.message};
                }
            })()
            """, timestamp, days);

            Value result = formulaEngine.getContext().eval("js", jsCode);
            boolean success = result.getMember("success").asBoolean();
            if (success) {
                long resultTimestamp = result.getMember("result").asLong();
                Date resultDate = new Date(resultTimestamp);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return new FormulaService.CalculationResult(true, sdf.format(resultDate), null);
            } else {
                String error = result.getMember("error").asString();
                return new FormulaService.CalculationResult(false, null, error);
            }
        } catch (Exception e) {
            logger.error("日期加减天失败", e);
            return new FormulaService.CalculationResult(false, null, e.getMessage());
        }
    }

    public FormulaService.CalculationResult addMonths(Date date, int months) {
        try {
            long timestamp = date.getTime();
            String jsCode = String.format("""
            (function() {
                try {
                    var d = new Date(%d);
                    d.setMonth(d.getMonth() + %d);
                    return {success: true, result: d.getTime()};
                } catch (e) {
                    return {success: false, error: e.message};
                }
            })()
            """, timestamp, months);

            Value result = formulaEngine.getContext().eval("js", jsCode);
            boolean success = result.getMember("success").asBoolean();
            if (success) {
                long resultTimestamp = result.getMember("result").asLong();
                Date resultDate = new Date(resultTimestamp);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return new FormulaService.CalculationResult(true, sdf.format(resultDate), null);
            } else {
                String error = result.getMember("error").asString();
                return new FormulaService.CalculationResult(false, null, error);
            }
        } catch (Exception e) {
            logger.error("日期加减月失败", e);
            return new FormulaService.CalculationResult(false, null, e.getMessage());
        }
    }

    public FormulaService.CalculationResult addYears(Date date, int years) {
        try {
            long timestamp = date.getTime();
            String jsCode = String.format("""
            (function() {
                try {
                    var d = new Date(%d);
                    d.setFullYear(d.getFullYear() + %d);
                    return {success: true, result: d.getTime()};
                } catch (e) {
                    return {success: false, error: e.message};
                }
            })()
            """, timestamp, years);

            Value result = formulaEngine.getContext().eval("js", jsCode);
            boolean success = result.getMember("success").asBoolean();
            if (success) {
                long resultTimestamp = result.getMember("result").asLong();
                Date resultDate = new Date(resultTimestamp);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return new FormulaService.CalculationResult(true, sdf.format(resultDate), null);
            } else {
                String error = result.getMember("error").asString();
                return new FormulaService.CalculationResult(false, null, error);
            }
        } catch (Exception e) {
            logger.error("日期加减年失败", e);
            return new FormulaService.CalculationResult(false, null, e.getMessage());
        }
    }
}
