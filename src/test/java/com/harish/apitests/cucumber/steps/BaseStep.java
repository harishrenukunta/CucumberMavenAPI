package com.harish.apitests.cucumber.steps;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import com.harish.apitests.config.TestConfig;

import io.cucumber.spring.CucumberContextConfiguration;

//@SpringBootTest -- not required. As this will load context for each and every scenario
@CucumberContextConfiguration //This class conveys to Cucumber that this class to use for loading application context
@ContextConfiguration(classes = {TestConfig.class})
public abstract class BaseStep {

}
