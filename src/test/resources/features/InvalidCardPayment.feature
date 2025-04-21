@InvalidCardPayment
Feature: Invalid Card Payment

  Scenario: User tries to pay with an invalid card
    Given the user is on the Renfe homepage
    When the user searches for a trip from "VALENCIA JOAQUÍN SOROLLA" to "BARCELONA-SANTS"
    And selects the first available train
    And enters invalid payment details
    Then the payment should be declined
