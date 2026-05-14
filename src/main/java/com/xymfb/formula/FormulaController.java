package com.xymfb.formula;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/api/formula")
public class FormulaController {
    private static final Logger logger = LoggerFactory.getLogger(FormulaController.class);
    private final FormulaEngine formulaEngine;

    @Inject
    public FormulaController(FormulaEngine formulaEngine) {
        this.formulaEngine = formulaEngine;
    }

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public FormulaResponse calculate(@Body FormulaRequest request) {
        logger.info("收到公式计算请求: formula={}, A1={}, A2={}", 
                request.getFormula(), request.getA1(), request.getA2());
        
        FormulaEngine.CalculationResult result = formulaEngine.calculate(
                request.getFormula(),
                request.getA1(),
                request.getA2()
        );
        
        if (result.isSuccess()) {
            logger.info("计算成功: result={}", result.getResult());
            return new FormulaResponse(true, result.getResult(), null);
        } else {
            logger.error("计算失败: error={}", result.getError());
            return new FormulaResponse(false, null, result.getError());
        }
    }
}
