package br.com.kuntzedev.moneymaster.config;

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.prometheus.PrometheusMeterRegistry;

@Configuration
public class MetricsConfig {
    @Bean
    MeterRegistryCustomizer<PrometheusMeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "money-master");
    }
}
