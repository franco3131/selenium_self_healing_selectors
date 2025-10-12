************************** Selenium self healing selector project ******************************

> Java 17 • Selenium 4.25 • Healenium 3.5.x • TestNG • Cucumber JVM

This project is a healenium demo for testing the wikipedia page from my wikipedia page project repo https://github.com/franco3131/Wikipedia-Search-Machine-Javascript. 
The purpose is to demo healenium using a github workflow. For the healenium demo workflow, it will 

1) start healenium backend using docker compose file
2) Run 3 successful tests for healenium to gather data
3) Run 1 scenario with a broken selector test that healenium will heal ( the selector is changed at runtime)
4) Generates a report and also takes screenshots. Healing in runtime is shown in the console as well. 

 --- Features --- 
  * Self‑healing WebDriver** via `SelfHealingDriver` wrapper
  * Store selector data into postgress sql
  * Display data in console


--- Actions tab (workflows) ---- 
* Healenium demo wokrflow - Runs docker compose. Healenium starts up. Then Runs 3 regular scenarios that pass ( to gather selector data using healenium backend). Then run a broken selector scenario and healenium should heal that selector. 
* Runs docker compose and runs healenium backend before starting the tests. 
Normal Run:
 * This showcases what happens when healenium is not running. It will runn the regular scenarios and then run the broken scenario. There should be no healing and the test should fail.

--- Docker compose file --- 
When running docker compose file (which is done in the healenium demo yml wokrflow) it does the following:
* runs the healenium demo job to kickstart starting up healenium before running the passing regular tests and broken(intentional test)
PostgreSQL (DB) – stores healing history, selector versions, and run metadata.
* Selector-Imitator – computes new locators that is used by the healenium driver.
* Healenium Backend  – for healing selector purposes and works with selector-imitator. It also reads from the db to display reporting results.

--- Stack ---
* Language: Java 17
* Build: Maven
* Test Frameworks: TestNG, Cucumber JVM
* Browser: Chrome/Chromedriver 
* Self‑Healing: Healenium (`healenium-web`)
* Logging:  SLF4J 2.0 + Logback (single binding)


 -- How to run Tests --
 run tests with a cucumber tag like:
* mvn test -Dcucumber.filter.tags='@smoke'

--- Extra information: ---

*  What you’ll see in the console when something heals:

```
[HEALED] from By.cssSelector(.login-btn) to By.cssSelector([data-test='login']) (score 0.76)
```

* If the backend is running, open the HTML report:

```
http://localhost:7878/healenium/report/
```



