package com.wikipedia.support;

import com.epam.healenium.SelfHealingDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import util.HealeniumConsoleReporter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BaseTest {

    // Healenium driver used by tests
    private static final ThreadLocal<WebDriver> threadLocalHealing = new ThreadLocal<>();
    // Raw Selenium driver for screenshots only
    private static final ThreadLocal<WebDriver> threadLocalRaw = new ThreadLocal<>();

    public static WebDriver getCurrentBrowser() { return threadLocalHealing.get(); }
    //for screenshots healenium driver and raw driver seperate 
    public static WebDriver getRawBrowser()     { return threadLocalRaw.get(); }

    

    @Before(order = 0)
    public void startBrowserSession() throws java.io.IOException {
        HealeniumConsoleReporter.install();
        System.setProperty("hlm.server.url", "http://127.0.0.1:7878");
        System.setProperty("hlm.imitator.url", "http://127.0.0.1:8000");
        // optional but explicit:
        System.setProperty("hlm.healing.enabled", "true");
        ChromeOptions options = new ChromeOptions();
        Path tmpProfile = Files.createTempDirectory("gha-chrome-profile-");
        options.addArguments("--user-data-dir=" + tmpProfile.toAbsolutePath());
        options.addArguments("--headless=new","--disable-gpu","--no-sandbox",
                             "--disable-dev-shm-usage","--window-size=1920,1080");
        // optional if you ever hit origin issues
        options.addArguments("--remote-allow-origins=*");
        //  options.addArguments("--disable-infobars");
        // options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        // options.setExperimentalOption("useAutomationExtension", false);
        // options.addArguments("--allow-file-access-from-files","--disable-web-security");
        WebDriver raw = new ChromeDriver(options);
        threadLocalRaw.set(raw);
        WebDriver healing = SelfHealingDriver.create(raw);
        threadLocalHealing.set(healing);
    }


    @After(order = 0)
    public void quitDriver() {
        WebDriver healing = threadLocalHealing.get();
        if (healing != null) {
            try { healing.quit(); } catch (Exception ignored) {}
            threadLocalHealing.remove();
        }
        threadLocalRaw.remove();
    }
    
    @After(order = 1)
    public void attachScreenshotIfFailed(Scenario scenario) throws Exception {
        if (!scenario.isFailed()) return;

        WebDriver raw = getRawBrowser();
        if (raw instanceof TakesScreenshot) {
       
                byte[] png = ((TakesScreenshot) raw).getScreenshotAs(OutputType.BYTES);
                // Attach to Cucumber report
                scenario.attach(png, "image/png", scenario.getName());

                // 2) Also save to disk for CI 
                Path dir = Paths.get("target", "screenshots");
                Files.createDirectories(dir);
                String safe = scenario.getName().replaceAll("[^a-zA-Z0-9._-]", "_");
                Path dest = dir.resolve(safe + "_" + System.currentTimeMillis() + ".png");
                Files.write(dest, png);
     
        } else {
            System.err.println("[screenshot] RAW driver not TakesScreenshot: "
                    + (raw == null ? "null" : raw.getClass().getName()));
        }
    }



    
}
