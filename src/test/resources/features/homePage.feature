Feature: Wikipedia search

@baseline
Scenario: Search for an article
When I go to the wikipedia home page
And I click on input
When I search for "cucumber"
When I click on go button
Then The top 1 rows displays the text "cucumber"



@baseline
Scenario: Search for an article
When I go to the wikipedia home page
And I click on input
When I search for "cucumber"
When I click on go button
Then The top 1 rows displays the text "cucumber"

@baseline
Scenario: Search for an article
When I go to the wikipedia home page
And I click on input
When I search for "cucumber"
When I click on go button
Then The top 1 rows displays the text "cucumber"

@healing
Scenario: Search for an article
When I go to the wikipedia home page
And I click on input
When I search for "cucumber"
And Break the search element on the page
When I click on go button
Then The top 1 rows displays the text "cucumber"


