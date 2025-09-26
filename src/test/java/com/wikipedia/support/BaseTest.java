package com.wikipedia.support;

import com.epam.healenium.SelfHealingDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;   

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

    public static WebDriver getCurrentBrowser() {
        return threadLocalBrowser.get();
    }
}
