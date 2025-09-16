package com.wikipedia.support;

import com.epam.healenium.SelfHealingDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BaseTest {

    private static final ThreadLocal<WebDriver> threadLocalBrowser = new ThreadLocal<>();

    @Before(order = 0)
    public void startBrowserSession() {
        WebDriver standardBrowserDriver = new ChromeDriver();
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