package base;

import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseClass {

private static final int SHORT_WAIT = 5;
private static final int MEDIUM_WAIT = 10;
private static final int LONG_WAIT = 30;

private static WebDriver getDriver() {
    return DriverInstance.getDriver();
}

private static WebElement waitForClickable(By locator, int timeoutSeconds) {
    return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds))
            .until(ExpectedConditions.elementToBeClickable(locator));
}

private static WebElement waitForVisible(By locator, int timeoutSeconds) {
    return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds))
            .until(ExpectedConditions.visibilityOfElementLocated(locator));
}

public static void click(By locator) {
    try {
        waitForClickable(locator, MEDIUM_WAIT).click();
    } catch (Exception e) {
        System.err.println("Click failed on element: " + locator);
        throw e;
    }
}

public static void enterText(By locator, String value) {
    try {
        WebElement element = waitForClickable(locator, MEDIUM_WAIT);
        element.clear();
        element.sendKeys(value);
    } catch (Exception e) {
        System.err.println("Failed to enter text on element: " + locator);
        throw e;
    }
}

public static void selectDropdownByValue(By locator, String value) {
    new Select(waitForVisible(locator, MEDIUM_WAIT)).selectByValue(value);
}

public static void selectDropdownByText(By locator, String text) {
    new Select(waitForVisible(locator, MEDIUM_WAIT)).selectByVisibleText(text);
}

public static void scrollToElement(By locator) {
    WebElement element = waitForVisible(locator, MEDIUM_WAIT);
    ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
}

public static boolean isElementPresent(By locator, int timeoutSeconds) {
    try {
        new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
        return true;
    } catch (TimeoutException e) {
        System.err.println("Element not present: " + locator);
        return false;
    }
}

public static boolean isElementVisible(By locator, int timeoutSeconds) {
    try {
        return waitForVisible(locator, timeoutSeconds).isDisplayed();
    } catch (Exception e) {
        System.err.println("Element not visible: " + locator);
        return false;
    }
}

public static void mouseHover(By locator) {
    WebElement element = waitForVisible(locator, MEDIUM_WAIT);
    new Actions(getDriver()).moveToElement(element).perform();
}

public static void doubleClick(By locator) {
    WebElement element = waitForClickable(locator, MEDIUM_WAIT);
    new Actions(getDriver()).doubleClick(element).perform();
}

public static void rightClick(By locator) {
    WebElement element = waitForClickable(locator, MEDIUM_WAIT);
    new Actions(getDriver()).contextClick(element).perform();
}

public static String getText(By locator) {
    return waitForVisible(locator, MEDIUM_WAIT).getText();
}

public static String getAttribute(By locator, String attribute) {
    return waitForVisible(locator, MEDIUM_WAIT).getAttribute(attribute);
}

public static void assertTextEquals(By locator, String expectedText) {
    String actualText = getText(locator);
    if (!actualText.equals(expectedText)) {
        throw new AssertionError(String.format("Assertion failed for element [%s]. Expected: '%s', Found: '%s'", locator, expectedText, actualText));
    }
}

public static void assertTextNotEquals(By locator, String expectedSubstring) {
    String actualText = getText(locator);
    if (!actualText.contains(expectedSubstring)) {
        throw new AssertionError(String.format("Expected substring: '%s' not found in actual text: '%s'", expectedSubstring, actualText));
    }
}

public static void waitForInvisibility(By locator, int timeoutSeconds) {
    new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds))
            .until(ExpectedConditions.invisibilityOfElementLocated(locator));
}

public static void waitForPageLoad() {
    new WebDriverWait(getDriver(), Duration.ofSeconds(LONG_WAIT)).until(
            webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete")
    );
}

@Attachment(value = "Screenshot", type = "image/png")
public static byte[] captureScreenshot() {
    return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
}

}
