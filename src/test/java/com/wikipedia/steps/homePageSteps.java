package com.wikipedia.steps;

import com.wikipedia.support.BaseTest;
import com.wikipedia.pages.WikipediaHomePage;
import com.wikipedia.pages.WikipediaResultsPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class homePageSteps {
    private WikipediaHomePage homePage;
    

  @Before(order = 1)
  public void initPages() {
	  homePage = new WikipediaHomePage(BaseTest.getCurrentBrowser());
  }

    @When("I search Wikipedia for {string}")
    public void searchWikipedia(String text) {
        homePage.writeTextInInput(text);
    
    }
    
    @When("I go to the wikipedia home page")
    public void searchWikipedia() {
        homePage.goToWikipediaPage();

    }
    
    @When("I click on input")
    public void iClickOnInput(){
        homePage.clickOnInput();
    }
    
    @When("I search for {string}")
    public void iSearchFor(String text){
        homePage.writeTextInInput(text);
    }
    
    @When("Break the search element on the page")
    public void pageChangesSearchId() {
        JavascriptExecutor js = (JavascriptExecutor) BaseTest.getCurrentBrowser();
        js.executeScript(
        		"var el = document.querySelector('.testSearchBtn'); " +
        				 "if (el) { el.classList.remove('testSearchBtn'); }"
        		      
        );
    }
    @When("I click on go button")
    public void iClickOnButton() {
    	homePage.clickOnGoButton();
    }
    


}
