package com.wikipedia.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.wikipedia.steps", "com.wikipedia.support"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber.html",
        "json:target/cucumber.json",
        "junit:target/cucumber.xml"
    },
    monochrome = true
)
public class RunnerTest extends AbstractTestNGCucumberTests { }
