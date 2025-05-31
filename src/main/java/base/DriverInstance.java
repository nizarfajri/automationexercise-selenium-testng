package base;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.github.bonigarcia.wdm.WebDriverManager;
import com.google.common.collect.ImmutableMap;
import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

public class DriverInstance {
    // Thread-safe WebDriver for parallel execution
private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
private static final ThreadLocal<String> browserName = new ThreadLocal<>();
private static final ThreadLocal<String> browserVersion = new ThreadLocal<>();

// Accessor for driver anywhere in the framework
public static WebDriver getDriver() {
    return driverThreadLocal.get();
}

// Accessors for browser info if needed
public static String getBrowserName() {
    return browserName.get();
}

public static String getBrowserVersion() {
    return browserVersion.get();
}

@BeforeMethod(alwaysRun = true)
public void setUpDriver() throws IOException {
    String browser = Utility.fetchConfigPropertyValue("browserName");
    String appUrl = Utility.fetchConfigPropertyValue("applicationLoginURL");

    WebDriver driver;

    if (browser.equalsIgnoreCase("chrome")) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-gpu", "--window-size=1920,1080");
        driver = new ChromeDriver(options);

    } else if (browser.equalsIgnoreCase("firefox")) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        options.addArguments("--width=1920", "--height=1080");
        driver = new FirefoxDriver(options);

    } else {
        throw new IllegalArgumentException("Unsupported browser: " + browser);
    }

    // Assign the thread-safe driver
    driverThreadLocal.set(driver);

    // Set implicit wait and open URL
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    driver.get(appUrl);

    // Capture and store browser info
    Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
    browserName.set(caps.getBrowserName());
    browserVersion.set(caps.getBrowserVersion());

    // Write environment info for Allure report
    allureEnvironmentWriter(
            ImmutableMap.of(
                    "Browser", browserName.get(),
                    "Browser.Version", browserVersion.get(),
                    "URL", appUrl
            ),
            "../amz-common/allure-results"
    );

    // Log session for debug
    SessionId sessionId = ((RemoteWebDriver) driver).getSessionId();
    System.out.println("[Thread] Session ID: " + sessionId);
}

@AfterMethod(alwaysRun = true)
public void quitDriver() {
    WebDriver driver = driverThreadLocal.get();
    if (Objects.nonNull(driver)) {
        driver.quit();
        driverThreadLocal.remove();
        browserName.remove();
        browserVersion.remove();
    }
}
}
