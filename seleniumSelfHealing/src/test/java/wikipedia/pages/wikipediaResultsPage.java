package wikipedia.pages;

import com.example.wikipedia.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WikipediaResultsPage extends BasePage {
    private final By firstHeading = By.id("firstHeading");

    public WikipediaResultsPage(WebDriver driver) {
        super(driver);
    }

    public String headingText() {
        return getText(firstHeading);
    }
}
