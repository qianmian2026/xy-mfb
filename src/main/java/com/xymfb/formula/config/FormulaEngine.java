package com.xymfb.formula.config;

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

    private void loadFormulaJs() {
        try {
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
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String jsContent = reader.lines().collect(Collectors.joining("\n"));
                context.eval("js", jsContent);
            }
        } catch (Exception e) {
            logger.error("Failed to load formula.js", e);
        }
    }

    public Context getContext() {
        return context;
    }

    public Object convertValue(Value value) {
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
}
