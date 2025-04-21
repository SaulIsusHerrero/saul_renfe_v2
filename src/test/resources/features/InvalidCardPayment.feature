@InvalidCardPayment
Feature: Invalid Card Payment

  Scenario Outline: User completes the full flow and tries to pay with invalid card details
    Given the user is on the Renfe homepage
    When the user searches for a trip from "<originStation>" to "<destinationStation>"
    And selects the first available train
    And enters personal details with "<firstName>", "<primerApellido>", "<segundoApellido>", "<dni>", "<email>", and "<phone>"
    And verifies the total price "<totalPrice>"
    And the user completes the purchase details
    And personalizes the trip
    And proceeds to payment with "<card>", "<expiration>", and "<cvv>"
    Then the payment should be declined

    Examples:
      | originStation            | destinationStation     | firstName | primerApellido | segundoApellido | dni        | email          | phone      | totalPrice | card              | expiration | cvv |
      | VALENCIA JOAQUÍN SOROLLA | BARCELONA-SANTS        | John      | Doe            | López           | 46131651E | test1@qa.com   | 696824570  | 50.00      | 4000 0000 0000 1000 | 03/30      | 990 |
      | MADRID-PUERTA DE ATOCHA  | SEVILLA-SANTA JUSTA    | Jane      | Smith          | García          | 12345678A | test2@qa.com   | 612345678  | 75.00      | 4111 1111 1111 1111 | 01/25      | 123 |
