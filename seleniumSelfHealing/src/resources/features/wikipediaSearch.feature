Feature: Wikipedia search
  Scenario: Search for a term
    When I search Wikipedia for "Selenium (software)"
    Then I should see a matching article heading
