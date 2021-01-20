package com.harish.apitests.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.harish.apitests.services"})
public class TestConfig {

}
