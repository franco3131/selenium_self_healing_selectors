package com.wikipedia.support;

import com.epam.healenium.SelfHealingDriver;
import io.cucumber.java.After;
import io.github.shelennium.healenium.events.HealingEvent;
import io.github.shelennium.healenium.events.HealingEventListener;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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

    @After(order = 0)
    public void endBrowserSession() {
        WebDriver activeBrowserDriver = threadLocalBrowser.get();
        if (activeBrowserDriver != null) {
            activeBrowserDriver.quit();
            threadLocalBrowser.remove();
        }
    }
    @AfterMethod
    public void tearDown(ITestResult result) {
        WebDriver driver = getCurrentBrowser();
        if (driver != null && !result.isSuccess()) {
            takeScreenshot(driver, result.getName());
        }
    }
    public static WebDriver getCurrentBrowser() {
        return threadLocalBrowser.get();
    }
    private void takeScreenshot(WebDriver driver, String testName) {
        try {
            Path dir = Paths.get("target", "screenshots");
            Files.createDirectories(dir);
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path dest = dir.resolve(testName + ".png");
            Files.copy(src.toPath(), dest);
            System.out.println("Saved screenshot: " + dest.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Could not save screenshot: " + e.getMessage());
        }
    }
    @AfterMethod(alwaysRun = true)
public void takeScreenshotOnFailure(ITestResult result) {
    WebDriver driver = getCurrentBrowser();
    if (driver != null && !result.isSuccess()) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String filename = "screenshots/fail-" + result.getName() + "-" + System.currentTimeMillis() + ".png";
            File dest = new File(filename);
            dest.getParentFile().mkdirs(); // make sure dir exists
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("[TESTNG] Screenshot saved: " + dest.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("[TESTNG] Could not capture screenshot: " + e.getMessage());
        }
    }
}
    
}
