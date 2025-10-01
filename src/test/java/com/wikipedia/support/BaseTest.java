package com.wikipedia.support;

import com.epam.healenium.SelfHealingDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
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
    /**
     * Run first (higher order) — take and attach screenshot if scenario failed.
     */
    @After(order = 1)
    public void takeScreenshotIfFailed(Scenario scenario) throws IOException{
        System.out.println("here");
        logger.fine("here2");

        WebDriver driver = getDriver();
        if (driver == null) {
            return;
        }

       WebDriver raw = unwrap(driver);               // your unwrap(driver) that peels WrapsDriver
        WebDriver shotDriver = raw;

        // Some remote drivers need Augmenter to expose TakesScreenshot
        if (!(shotDriver instanceof TakesScreenshot)) {
            shotDriver = new Augmenter().augment(shotDriver);
        }

        if (shotDriver instanceof TakesScreenshot) {
            byte[] png = ((TakesScreenshot) shotDriver).getScreenshotAs(OutputType.BYTES);

            // 1) attach to Cucumber report
            scenario.attach(png, "image/png", scenario.getName());

            // 2) also save to disk for CI artifacts
            Path dir = Paths.get("target", "screenshots");
            Files.createDirectories(dir);
            String safe = scenario.getName().replaceAll("[^a-zA-Z0-9-_\\.]", "_");
            Path dest = dir.resolve(safe + "_" + System.currentTimeMillis() + ".png");
            Files.write(dest, png);
            System.out.println("[screenshot] saved: " + dest.toAbsolutePath());
            }
        // } catch (ClassCastException e) {
        //     // driver doesn't support screenshots
        //     System.err.println("[CucumberHooks] Driver doesn't support screenshots: " + e.getMessage());
        // } catch (IOException e) {
        //     System.err.println("[CucumberHooks] Failed to write screenshot: " + e.getMessage());
        // } catch (Throwable t) {
        //     System.err.println("[CucumberHooks] Unexpected error taking screenshot: " + t.getMessage());
        // }
    }


private static WebDriver unwrap(WebDriver d) {
    WebDriver cur = d;
    while (cur instanceof WrapsDriver) {
        cur = ((WrapsDriver) cur).getWrappedDriver();
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
