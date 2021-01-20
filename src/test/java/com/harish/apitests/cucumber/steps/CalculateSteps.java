package com.harish.apitests.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;

import com.harish.apitests.services.CalculationService;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CalculateSteps extends BaseStep {
	
	@Autowired
	private CalculationService calcService;
	
	private int result;
	
	@When("add two numbers {int} and {int}")
	public void add(Integer a, Integer b) {
		this.result = calcService.add(a.intValue(),  b.intValue());
		System.out.println("Add two numbers:" + result);
	}
	
	@Then("verify the result is {int}")
	public void verifyResult(final Integer expResult) {
		assertThat(result).as("Result is " + result).isEqualTo(expResult.intValue());
	}
}
