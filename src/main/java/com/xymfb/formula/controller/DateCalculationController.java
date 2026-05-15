package com.xymfb.formula.controller;

import com.xymfb.formula.model.*;
import com.xymfb.formula.service.DateCalculationService;
import com.xymfb.formula.service.FormulaService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/api/formula/date/calc")
public class DateCalculationController {
    private static final Logger logger = LoggerFactory.getLogger(DateCalculationController.class);
    private final DateCalculationService dateCalculationService;

    @Inject
    public DateCalculationController(DateCalculationService dateCalculationService) {
        this.dateCalculationService = dateCalculationService;
    }

    @Post(uri = "/diff", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public FormulaResponse dateDiff(@Body DateDiffRequest request) {
        logger.info("收到日期差值计算请求: startDate={}, endDate={}, unit={}", 
                request.getStartDate(), request.getEndDate(), request.getUnit());
        
        FormulaService.CalculationResult result = dateCalculationService.dateDiff(
                request.getStartDate(), request.getEndDate(), request.getUnit());
        
        if (result.isSuccess()) {
            logger.info("日期差值计算成功: result={}", result.getResult());
            return new FormulaResponse(true, result.getResult(), null);
        } else {
            logger.error("日期差值计算失败: error={}", result.getError());
            return new FormulaResponse(false, null, result.getError());
        }
    }

    @Post(uri = "/format", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public FormulaResponse formatDate(@Body DateFormatRequest request) {
        logger.info("收到日期格式化请求: date={}, pattern={}", request.getDate(), request.getPattern());
        
        FormulaService.CalculationResult result = dateCalculationService.formatDate(
                request.getDate(), request.getPattern());
        
        if (result.isSuccess()) {
            logger.info("日期格式化成功: result={}", result.getResult());
            return new FormulaResponse(true, result.getResult(), null);
        } else {
            logger.error("日期格式化失败: error={}", result.getError());
            return new FormulaResponse(false, null, result.getError());
        }
    }

    @Post(uri = "/addDays", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public FormulaResponse addDays(@Body DateAddRequest request) {
        logger.info("收到日期加减天请求: date={}, days={}", request.getDate(), request.getValue());
        
        FormulaService.CalculationResult result = dateCalculationService.addDays(
                request.getDate(), request.getValue());
        
        if (result.isSuccess()) {
            logger.info("日期加减天成功: result={}", result.getResult());
            return new FormulaResponse(true, result.getResult(), null);
        } else {
            logger.error("日期加减天失败: error={}", result.getError());
            return new FormulaResponse(false, null, result.getError());
        }
    }

    @Post(uri = "/addMonths", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public FormulaResponse addMonths(@Body DateAddRequest request) {
        logger.info("收到日期加减月请求: date={}, months={}", request.getDate(), request.getValue());
        
        FormulaService.CalculationResult result = dateCalculationService.addMonths(
                request.getDate(), request.getValue());
        
        if (result.isSuccess()) {
            logger.info("日期加减月成功: result={}", result.getResult());
            return new FormulaResponse(true, result.getResult(), null);
        } else {
            logger.error("日期加减月失败: error={}", result.getError());
            return new FormulaResponse(false, null, result.getError());
        }
    }

    @Post(uri = "/addYears", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public FormulaResponse addYears(@Body DateAddRequest request) {
        logger.info("收到日期加减年请求: date={}, years={}", request.getDate(), request.getValue());
        
        FormulaService.CalculationResult result = dateCalculationService.addYears(
                request.getDate(), request.getValue());
        
        if (result.isSuccess()) {
            logger.info("日期加减年成功: result={}", result.getResult());
            return new FormulaResponse(true, result.getResult(), null);
        } else {
            logger.error("日期加减年失败: error={}", result.getError());
            return new FormulaResponse(false, null, result.getError());
        }
    }
}
