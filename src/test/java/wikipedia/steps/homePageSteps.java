package wikipedia.steps;

import com.example.wikipedia.BaseTest;
import com.example.wikipedia.pages.WikipediaHomePage;
import com.example.wikipedia.pages.WikipediaResultsPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import org.testng.Assert;

public class SearchSteps {
    private WikipediaResultsPage homePage;


  @Before(order = 1)
  public void initPages() {
    homePage = new WikipediaHomePage(BaseTest.driver);
  }

    @When("I search Wikipedia for {string}")
    public void searchWikipedia(String term) {
        homePage.searchFor(term);
    }

    @When("I click on input")
    public void iClickOnInput(){
        homePage.clickOnInput();
    }
    @When("I search for {string)")
    public void iSearchFor(String text){
        homePage.writeTextInInput(text);
    }

    @Then("I should see a matching article heading")
    public void verifyArticleHeading() {
        Assert.assertFalse(resultsPage.headingText().isEmpty(), "Heading is empty!");
    }
}
