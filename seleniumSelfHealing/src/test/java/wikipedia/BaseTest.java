package wikipedia;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class BaseTest {
    public static WebDriver driver;

    @Before(order = 0)
    public void setUp() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1400");
        options.addArguments("--height=900");
        driver = new FirefoxDriver(options);
    }

    @After(order = 0)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
