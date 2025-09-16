package com.wikipedia.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = { "com.wikipedia.steps", "com.wikipedia.support" },
    plugin = {
        "pretty",
        "html:target/cucumber-report.html",
        "json:target/cucumber.json",
        "junit:target/cucumber.xml"
    },
    monochrome = true
)
public class RunnerFile { }