package com.ccsw.tutorial.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    // Esta configuración nos permitirá hacer transformaciones entre objetos de forma muy sencilla.
    @Bean
    public ModelMapper getModelMapper() {

        return new ModelMapper();
    }
}
