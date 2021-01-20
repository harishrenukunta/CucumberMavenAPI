#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
Feature: Meme feature
@meme
  Scenario: A user gets the meme
    Given the following memes
      | meme          | darkness |
      | olong johnson |        1 |
      | classic angus |        2 |
    When the user requests all the memes
    Then all the memes are returned

@add
  Scenario: Add two numbers
    When add two numbers 3 and 4
    Then verify the result is 7
