package wikipedia.pages;

import com.example.wikipedia.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class WikipediaHomePage extends BasePage {

    private final By searchInput = By.cssSelector("input#string");

    public WikipediaHomePage(WebDriver driver) {
        super(driver);
    }

    public void clickOnInput() {
        waitForVisible(searchInput).click();
    }

    public void writeTextInInput(String text) {
        type(searchInput,text);
    }
}
