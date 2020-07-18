package com.example.websiteapi.logger;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;

;

public class Logger {
    private final String KEY_PREFIX = "msg.";
    private final org.slf4j.Logger logger;

    Logger(Class<?> clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    public void debug(@NonNull String message, @NonNull ImmutableMap<String, String> data) {
        log(data, () -> logger.debug(message));
    }

    public void info(@NonNull String message, @NonNull ImmutableMap<String, String> data) {
        log(data, () -> logger.info(message));
    }

    public void warn(@NonNull String message, @NonNull ImmutableMap<String, String> data) {
        log(data, () -> logger.warn(message));
    }

    public void error(@NonNull String message, @NonNull ImmutableMap<String, String> data) {
        log(data, () -> logger.error(message));
    }

    public void bind(@NonNull ImmutableMap<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            MDC.put(KEY_PREFIX + entry.getKey(), entry.getValue());
        }
    }

    public void unbind(@NonNull ImmutableCollection<String> keys) {
        for (String key : keys) {
            MDC.remove(KEY_PREFIX + key);
        }
    }

    private void log(@NonNull ImmutableMap<String, String> data, @NonNull Runnable log) {
        bind(data);
        log.run();
        unbind(data.keySet());
    }
}

