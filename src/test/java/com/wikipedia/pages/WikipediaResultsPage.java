package com.wikipedia.pages;

import com.wikipedia.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WikipediaResultsPage extends BasePage {
    private final By firstHeading = By.id("firstHeading");

    public WikipediaResultsPage(WebDriver driver) {
        super(driver);
    }
    public Boolean doesSearchedTextDisplayTopResults(int rows,String word) {
    	String sentence = "";
    	for(int i=1;i<=rows;i++) {
    		System.out.println("body > div > a > div > a:nth-child("+i+") > div>p>b:nth-child(1)");
    		waitForVisible(By.cssSelector("body > div > a > div > a:nth-child("+i+") > div"));
    		sentence = driver.findElement(By.cssSelector("body > div > a > div > a:nth-child("+i+") > div>p>b:nth-child(1)")).getText();
    		System.out.println(sentence);
            System.out.println(word);
    		if(!sentence.toLowerCase().contains(word)) {
    			return false;
    		}
    	}
    	return true;
    }
    
}
