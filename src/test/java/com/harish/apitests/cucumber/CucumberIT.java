package com.harish.apitests.cucumber;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.harish.apitests.CucumberMavenApiApplication;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;

@RunWith(Cucumber.class)
//@CucumberContextConfiguration
//@SpringBootTest(classes = {CucumberMavenApiApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberOptions(
		plugin = {"pretty"},
		//tags="@meme",
		features="src/test/resources/features"
		//glue={"com.harish.apitests.cucumber.steps"}
		)
public class CucumberIT {

}
