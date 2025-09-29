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

public class BaseTest {

    private static final ThreadLocal<WebDriver> threadLocalBrowser = new ThreadLocal<>();

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
        return threadLocalBrowser.get();;
    }

    /**
     * Run first (higher order) — take and attach screenshot if scenario failed.
     */
    @After(order = 1)
    public void takeScreenshotIfFailed(Scenario scenario) {
        WebDriver driver = getDriver();
        if (driver == null) {
            return;
        }

        try {
            if (scenario.isFailed()) {
                // attach bytes to Cucumber report
                byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshotBytes, "image/png", scenario.getName());

                // also save to disk so CI can upload as artifact
                Path screenshotsDir = Path.of("target", "screenshots");
                Files.createDirectories(screenshotsDir);

                // safe file name
                String fileName = scenario.getName()
                        .replaceAll("[^a-zA-Z0-9-_\\.]", "_")
                        + "_" + System.currentTimeMillis() + ".png";
                Path dest = screenshotsDir.resolve(fileName);

                // write bytes
                Files.write(dest, screenshotBytes);
                System.out.println("[CucumberHooks] Screenshot saved to: " + dest.toAbsolutePath());
            }
        } catch (ClassCastException e) {
            // driver doesn't support screenshots
            System.err.println("[CucumberHooks] Driver doesn't support screenshots: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[CucumberHooks] Failed to write screenshot: " + e.getMessage());
        } catch (Throwable t) {
            System.err.println("[CucumberHooks] Unexpected error taking screenshot: " + t.getMessage());
        }
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
            BaseTest.removeCurrentBrowserIfPresent(); // see note below
        }
    }

    
}
