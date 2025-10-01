package com.wikipedia.support;

import com.epam.healenium.SelfHealingDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.cucumber.java.Scenario;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;  
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.remote.Augmenter;

public class BaseTest {

    private static final ThreadLocal<WebDriver> threadLocalBrowser = new ThreadLocal<>();
     private static final Logger logger = Logger.getLogger(BaseTest.class.getName());

@Before(order = 0)
public void startBrowserSession() throws IOException {
    ChromeOptions options = new ChromeOptions();
    Path tmpProfile = Files.createTempDirectory("gha-chrome-profile-");
    options.addArguments("--user-data-dir=" + tmpProfile.toAbsolutePath());
    options.addArguments("--headless=new","--disable-gpu","--no-sandbox",
                         "--disable-dev-shm-usage","--window-size=1920,1080");
    // optional if you ever hit origin issues
    options.addArguments("--remote-allow-origins=*");
     WebDriver standard = new ChromeDriver(options);
    WebDriver healing = SelfHealingDriver.create(standard);
    threadLocalBrowser.set(healing);
    
}
  // Uses your BaseTest.getCurrentBrowser() thread-local accessor
    private WebDriver getDriver() {
        return threadLocalBrowser.get();
    }
   // Uses your BaseTest.getCurrentBrowser() thread-local accessor
    public static WebDriver getCurrentBrowser() {
        return threadLocalBrowser.get();
    }
@After(order = 1)
public void attachScreenshotIfFailed(Scenario scenario) throws Exception {
    if (!scenario.isFailed()) return;

    WebDriver driver = BaseTest.getCurrentBrowser();
    if (driver == null) return;


        WebDriver raw = unwrapToRealDriver(driver);

        byte[] png = null;

        // 1) direct if supported
        if (raw instanceof TakesScreenshot) {
            png = ((TakesScreenshot) raw).getScreenshotAs(OutputType.BYTES);
        } else if (raw instanceof HasCapabilities) {
            // 2) augment only when capabilities are present
            WebDriver augmented = new Augmenter().augment(raw);
            if (augmented instanceof TakesScreenshot) {
                png = ((TakesScreenshot) augmented).getScreenshotAs(OutputType.BYTES);
            }
        } else {
            System.err.println("[screenshot] driver is neither TakesScreenshot nor HasCapabilities: "
                               + raw.getClass().getName());
        }

        if (png != null) {
            scenario.attach(png, "image/png", scenario.getName());
            Path dir = Paths.get("target", "screenshots");
            Files.createDirectories(dir);
            String safe = scenario.getName().replaceAll("[^a-zA-Z0-9-_\\.]", "_");
            Path dest = dir.resolve(safe + "_" + System.currentTimeMillis() + ".png");
            Files.write(dest, png);
            System.out.println("[screenshot] saved: " + dest.toAbsolutePath());
        }
    
}

private static WebDriver unwrapToRealDriver(WebDriver d) {
    WebDriver cur = d;
    // unwrap nested wrappers (SelfHealingDriver, listeners, etc.)
    while (cur instanceof WrapsDriver) {
        WebDriver next = ((WrapsDriver) cur).getWrappedDriver();
        if (next == cur) break; // safety
        cur = next;
    }
    return cur;
}


    
    
    /**
     * Run after screenshot hook — quit the browser here so screenshot is still possible.
     */
    @After(order = 0)
    public void quitDriver() {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Throwable ignored) {}
            // If you manage the ThreadLocal here, remove it
              threadLocalBrowser.remove();
        }
    }

    
}
