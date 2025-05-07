package com.aisadsa.aisadsabackend.core.function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class AiFunctionBeans {

    // It is not used, needed as a placeholder to correctly process the spring cloud function configurations
    @Bean
    public Function<String, String> noopFunction() {
        return input -> input;
    }
}
