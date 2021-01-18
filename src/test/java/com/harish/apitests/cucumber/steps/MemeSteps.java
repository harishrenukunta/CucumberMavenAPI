package com.harish.apitests.cucumber.steps;

import java.util.List;
import java.util.Map;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MemeSteps {
	
	@Given("^the following memes$")
	public void givenTheMemes(List<Map<String, String>> memes) {
		System.out.println("Step defs - the following memes - " + memes);
	}
	
	@When("the user requests all the memes$")
	public void whenUserRequest() {
		System.out.println("Step defs - when user requests");
	}
	
	@Then("^all the memes are returned$")
	public void thenTheMemesReturned() {
		System.out.println("Step defs - then memes are returned");
	}

}
