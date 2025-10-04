package com.wikipedia.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;

public class WikipediaHomePage extends BasePage {

    private final By searchInput = By.cssSelector("input#string");
    private final By goButton = By.cssSelector(".testSearchBtn");

    public WikipediaHomePage(WebDriver driver) {
        super(driver);
    }

    public void clickOnInput() {
        waitForVisible(searchInput).click();
    }

    public void writeTextInInput(String text) {
        type(searchInput,text);
    }
    public void clickOnGoButton() {
        try{
        	waitForVisible(goButton);
        	click(goButton);
        }catch(Exception e){
            //unfocus from red box error that shows up for chrome from time to time and try again
            ((JavascriptExecutor) driver).executeScript("document.activeElement.blur();");
            waitForVisible(goButton);
        	click(goButton);
        }
    }

}
