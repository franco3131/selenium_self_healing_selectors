# Selenium Self‑Healing Selectors on wikipedia page from this repo 

> Java 17 • Selenium 4.25 • Healenium 3.5.x • TestNG • Cucumber JVM

An example showing how to add **self‑healing locators** to a Selenium/TestNG/Cucumber project using **Healenium**. It also shows in the console when an element is healed.


 -- Features -- 
  * Self‑healing WebDriver** via `SelfHealingDriver` wrapper
  * Store selector data into postgress sql
  * Display data in console


--- Actions tab ---- 

* runs the healenium demo job to kickstart starting up healenium before running the passing regular tests and broken(intentional test)
PostgreSQL (DB) – stores healing history, selector versions, and run metadata.

* Selector-Imitator – computes new locators that is used by the healenium driver.

* Healenium Backend  – for healing selector purposes and works with selector-imitator. It also reads from the db to display reporting results.

---


-- Stack --

* Language: Java 17
* Build: Maven
* Test Frameworks: TestNG, Cucumber JVM
* Browser: Chrome/Chromedriver 
* Self‑Healing: Healenium (`healenium-web`)
* Logging:  SLF4J 2.0 + Logback (single binding)



 -- How to run Tests --
mvn test
or 
mvn test -Dcucumber.filter.tags='@smoke'


 -- What you’ll see in the console when something heals: --

```
[HEALED] from By.cssSelector(.login-btn) to By.cssSelector([data-test='login']) (score 0.76)
```

If the backend is running, open the HTML report:

```
http://localhost:7878/healenium/report/
```



