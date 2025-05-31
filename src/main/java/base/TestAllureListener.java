package base;

import java.io.ByteArrayInputStream;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

/**
 * Listener class to capture screenshots and logs on test events and attach them to Allure reports.
 */
public class TestAllureListener implements ITestListener {

    /**
     * Utility to extract the test method name from ITestResult.
     */
    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    /**
     * Attach a PNG screenshot to Allure report.
     */
    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshotPNG(WebDriver driver) {
        return (driver instanceof TakesScreenshot)
                ? ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)
                : new byte[0];
    }

    /**
     * Attach plain text log to Allure report.
     */
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    /**
     * Attach raw HTML content to Allure report.
     */
    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("[Suite Start] " + iTestContext.getName());
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("[Suite Finish] " + iTestContext.getName());
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("[Test Start] " + getTestMethodName(iTestResult));
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("[Test Success] " + getTestMethodName(iTestResult));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("[Test Failed] " + getTestMethodName(iTestResult));

        WebDriver driver = DriverInstance.getDriver(); // Make sure DriverInstance uses ThreadLocal
        if (driver != null) {
            Allure.addAttachment("Failed Screenshot",
                    new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        } else {
            System.out.println("WebDriver instance was null; screenshot skipped.");
        }
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("[Test Skipped] " + getTestMethodName(iTestResult));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("[Test Failed But Within Success Percentage] " + getTestMethodName(iTestResult));
    }
}
