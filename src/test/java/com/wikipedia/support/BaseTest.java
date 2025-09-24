package com.wikipedia.support;

import com.epam.healenium.SelfHealingDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.nio.file.Files;
import java.nio.file.Path;

public class BaseTest {

    private static final ThreadLocal<WebDriver> threadLocalBrowser = new ThreadLocal<>();

    @Before(order = 0)
    public void startBrowserSession() {
    ChromeOptions options = new ChromeOptions();

    // Unique profile dir (important for GitHub Actions/Linux)
    Path tmpProfile = Files.createTempDirectory("gha-chrome-profile-");
    options.addArguments("--user-data-dir=" + tmpProfile.toAbsolutePath().toString());

    // Headless & CI-safe flags
    options.addArguments("--headless=new");
    options.addArguments("--disable-gpu");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--window-size=1920,1080");

    // optional: avoid remote origin issues
    options.addArguments("--remote-allow-origins=*");

    WebDriver standardBrowserDriver = new ChromeDriver(options);
    SelfHealingDriver healingBrowserDriver = SelfHealingDriver.create(standardBrowserDriver);

    threadLocalBrowser.set(healingBrowserDriver);
    }

    @After(order = 0)
    public void endBrowserSession() {
        WebDriver activeBrowserDriver = threadLocalBrowser.get();
        if (activeBrowserDriver != null) {
            activeBrowserDriver.quit();
            threadLocalBrowser.remove();
        }
    }

    public static WebDriver getCurrentBrowser() {
        return threadLocalBrowser.get();
    }
}
