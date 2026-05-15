package com.xymfb.formula.controller;

import com.xymfb.formula.model.FormulaResponse;
import com.xymfb.formula.model.SumRequest;
import com.xymfb.formula.service.FormulaService;
import com.xymfb.formula.service.MathFormulaService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/api/formula/math")
public class FormulaMathController {
    private static final Logger logger = LoggerFactory.getLogger(FormulaMathController.class);
    private final MathFormulaService mathFormulaService;

    @Inject
    public FormulaMathController(MathFormulaService mathFormulaService) {
        this.mathFormulaService = mathFormulaService;
    }

    @Post(uri = "/sum", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public FormulaResponse sum(@Body SumRequest request) {
        logger.info("收到SUM计算请求: numbers长度={}", request.getNumbers() != null ? request.getNumbers().length : 0);
        
        FormulaService.CalculationResult result = mathFormulaService.sum(request.getNumbers());
        
        if (result.isSuccess()) {
            logger.info("SUM计算成功: result={}", result.getResult());
            return new FormulaResponse(true, result.getResult(), null);
        } else {
            logger.error("SUM计算失败: error={}", result.getError());
            return new FormulaResponse(false, null, result.getError());
        }
    }
}
