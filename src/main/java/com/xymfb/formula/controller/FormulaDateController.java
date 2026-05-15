package com.xymfb.formula.controller;

import com.xymfb.formula.model.*;
import com.xymfb.formula.service.DateFormulaService;
import com.xymfb.formula.service.FormulaService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/api/formula/date")
public class FormulaDateController {
    private static final Logger logger = LoggerFactory.getLogger(FormulaDateController.class);
    private final DateFormulaService dateFormulaService;

    @Inject
    public FormulaDateController(DateFormulaService dateFormulaService) {
        this.dateFormulaService = dateFormulaService;
    }

    @Post(uri = "/date", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public FormulaResponse date(@Body DateRequest request) {
        logger.info("收到DATE计算请求: year={}, month={}, day={}", request.getYear(), request.getMonth(), request.getDay());
        
        FormulaService.CalculationResult result = dateFormulaService.date(request.getYear(), request.getMonth(), request.getDay());
        
        if (result.isSuccess()) {
            logger.info("DATE计算成功: result={}", result.getResult());
            return new FormulaResponse(true, result.getResult(), null);
        } else {
            logger.error("DATE计算失败: error={}", result.getError());
            return new FormulaResponse(false, null, result.getError());
        }
    }

    @Post(uri = "/now", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public FormulaResponse now() {
        logger.info("收到NOW计算请求");
        
        FormulaService.CalculationResult result = dateFormulaService.now();
        
        if (result.isSuccess()) {
            logger.info("NOW计算成功: result={}", result.getResult());
            return new FormulaResponse(true, result.getResult(), null);
        } else {
            logger.error("NOW计算失败: error={}", result.getError());
            return new FormulaResponse(false, null, result.getError());
        }
    }

    @Post(uri = "/today", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public FormulaResponse today() {
        logger.info("收到TODAY计算请求");
        
        FormulaService.CalculationResult result = dateFormulaService.today();
        
        if (result.isSuccess()) {
            logger.info("TODAY计算成功: result={}", result.getResult());
            return new FormulaResponse(true, result.getResult(), null);
        } else {
            logger.error("TODAY计算失败: error={}", result.getError());
            return new FormulaResponse(false, null, result.getError());
        }
    }

    @Post(uri = "/year", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public FormulaResponse year(@Body DateExtractRequest request) {
        logger.info("收到YEAR计算请求: date={}", request.getDate());
        
        FormulaService.CalculationResult result = dateFormulaService.year(request.getDate());
        
        if (result.isSuccess()) {
            logger.info("YEAR计算成功: result={}", result.getResult());
            return new FormulaResponse(true, result.getResult(), null);
        } else {
            logger.error("YEAR计算失败: error={}", result.getError());
            return new FormulaResponse(false, null, result.getError());
        }
    }

    @Post(uri = "/month", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public FormulaResponse month(@Body DateExtractRequest request) {
        logger.info("收到MONTH计算请求: date={}", request.getDate());
        
        FormulaService.CalculationResult result = dateFormulaService.month(request.getDate());
        
        if (result.isSuccess()) {
            logger.info("MONTH计算成功: result={}", result.getResult());
            return new FormulaResponse(true, result.getResult(), null);
        } else {
            logger.error("MONTH计算失败: error={}", result.getError());
            return new FormulaResponse(false, null, result.getError());
        }
    }

    @Post(uri = "/day", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public FormulaResponse day(@Body DateExtractRequest request) {
        logger.info("收到DAY计算请求: date={}", request.getDate());
        
        FormulaService.CalculationResult result = dateFormulaService.day(request.getDate());
        
        if (result.isSuccess()) {
            logger.info("DAY计算成功: result={}", result.getResult());
            return new FormulaResponse(true, result.getResult(), null);
        } else {
            logger.error("DAY计算失败: error={}", result.getError());
            return new FormulaResponse(false, null, result.getError());
        }
    }
}
