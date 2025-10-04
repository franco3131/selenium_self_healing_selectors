package com.wikipedia.pages;

import com.wikipedia.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;


public class WikipediaResultsPage extends BasePage {
    private final By firstHeading = By.id("firstHeading");

    public WikipediaResultsPage(WebDriver driver) {
        super(driver);
    }

    public Boolean isWordPresentInFirstNRow(int rows, String word) throws Exception{
        	String sentence = "";
        	for(int i=1;i<=rows;i++) {
        		waitForVisible(By.cssSelector("body > div > a > div > a:nth-child("+i+") > div"));
        		sentence = driver.findElement(By.cssSelector("body > div > a > div > a:nth-child("+i+") > div>p>b:nth-child(1)")).getText();
        		System.out.println("the sentence from element is " + sentence+" and expected word contained is"+ word);
        		if(!sentence.toLowerCase().contains(word)) {
        			return false;
        		}
        	}
        	return true;
    }
    
    public Boolean doesSearchedTextDisplayTopResults(int rows,String word) {
        try{
         return isWordPresentInFirstNRows(rows, word);
        }catch(Exception e){
            //unfocus on red box error and try again
        ((JavascriptExecutor) driver).executeScript("document.activeElement.blur();");
        return isWordPresentInFirstNRow(rows, word);
        }
    
}
}
