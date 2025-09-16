package com.wikipedia.steps;

import static org.junit.Assert.assertTrue;


import com.wikipedia.pages.WikipediaHomePage;
import com.wikipedia.pages.WikipediaResultsPage;
import com.wikipedia.support.BaseTest;

import io.cucumber.java.Before;
import io.cucumber.java.en.When;

public class resultPageSteps {
    private WikipediaResultsPage resultPage;
    @Before(order = 1)
    public void initPages() {
    	 resultPage = new WikipediaResultsPage(BaseTest.getCurrentBrowser());
    }
    
    @When("The top {int} rows displays the text {string}")
    public void theTopRowsDisplaysTheText(int rows,String text) {
    	assertTrue(resultPage.doesSearchedTextDisplayTopResults(rows,text));
    }

}

