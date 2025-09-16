package wikipedia.steps;

import com.example.wikipedia.BaseTest;
import com.example.wikipedia.pages.WikipediaHomePage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class SearchSteps {
    private WikipediaHomePage homePage;

    @When("I go to the wikipedia home page")
    public void searchWikipedia(String term) {
        homePage.goToWikipediaPage();

    }


}
