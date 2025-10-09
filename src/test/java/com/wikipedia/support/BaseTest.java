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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * BaseTest: starts Chrome, wraps it with Healenium for normal use,
 * and captures screenshots (using the raw driver) when a scenario fails.
 */
public class BaseTest {

    // Healenium-wrapped driver used by your tests
    private static final ThreadLocal<WebDriver> threadLocalHealing = new ThreadLocal<>();
    // RAW Selenium driver used ONLY for screenshots (avoids proxy casting issues)
    private static final ThreadLocal<WebDriver> threadLocalRaw = new ThreadLocal<>();

    public static WebDriver getCurrentBrowser() { return threadLocalHealing.get(); }
    public static WebDriver getRawBrowser()     { return threadLocalRaw.get(); }

    @Before(order = 0)
    public void startBrowserSession() throws java.io.IOException {
        // System.setProperty("webdriver.http.factory", "jdk-http-client");
        // System.setProperty("hlm.server.url", "http://127.0.0.1:7878");
        // System.setProperty("hlm.imitator.url", "http://127.0.0.1:8000");
        // // optional but explicit:
        // System.setProperty("hlm.healing.enabled", "true");
        // System.setProperty("hlm.sessionkey",System.getenv().getOrDefault("GITHUB_RUN_ID","local-run"));
        ChromeOptions options = new ChromeOptions();
        Path tmpProfile = Files.createTempDirectory("gha-chrome-profile-");
        options.addArguments("--user-data-dir=" + tmpProfile.toAbsolutePath());
        options.addArguments("--headless=new","--disable-gpu","--no-sandbox",
                             "--disable-dev-shm-usage","--window-size=1920,1080");
        // optional if you ever hit origin issues
        options.addArguments("--remote-allow-origins=*");
         options.addArguments("--disable-infobars");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--allow-file-access-from-files","--disable-web-security");
        WebDriver raw = new ChromeDriver(options);
        threadLocalRaw.set(raw);
        WebDriver healing = SelfHealingDriver.create(raw);
        threadLocalHealing.set(healing);
    }


    @After(order = 1)
    public void attachScreenshotIfFailed(Scenario scenario) throws Exception {
        if (!scenario.isFailed()) return;

        WebDriver raw = getRawBrowser();
        if (raw instanceof TakesScreenshot) {
       
                byte[] png = ((TakesScreenshot) raw).getScreenshotAs(OutputType.BYTES);

                // 1) Attach to Cucumber report
                scenario.attach(png, "image/png", scenario.getName());

                // 2) Also save to disk for CI artifacts
                Path dir = Paths.get("target", "screenshots");
                Files.createDirectories(dir);
                String safe = scenario.getName().replaceAll("[^a-zA-Z0-9._-]", "_");
                Path dest = dir.resolve(safe + "_" + System.currentTimeMillis() + ".png");
                Files.write(dest, png);
                System.out.println("[screenshot] saved: " + dest.toAbsolutePath());
     
        } else {
            System.err.println("[screenshot] RAW driver not TakesScreenshot: "
                    + (raw == null ? "null" : raw.getClass().getName()));
        }
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
}
