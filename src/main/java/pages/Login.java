package pages;

import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import base.BaseClass;
import io.qameta.allure.Step;

public class Login {
    WebDriver driver;
    public Login (WebDriver driver) {
        this.driver = driver;
    }

    @Step("Wait for page full load")
    public Login waitForPageLoad()
        throws IOException {
        BaseClass.waitForPageLoad();
        BaseClass.captureScreenshot();
        System.out.println("Page alread full load");
        return this;
    }

    By LoginTab_xpath = By.xpath("//ul[@class='nav navbar-nav']/li[4]/a");
    @Step("Click Login / Sign Up tab")
    public Login clickLoginSignUpTab()
        throws IOException {
        BaseClass.click(LoginTab_xpath);
        BaseClass.captureScreenshot();
        System.out.println("User click Login / Sign up tab");
        return this;
    }

    By loginEmailField_xpath = By.xpath("//input[@data-qa='login-email']");
    @Step("Fill email address for login")
    public Login enterEmailLogin(String emailAddress)
        throws IOException {
        BaseClass.enterText(loginEmailField_xpath, emailAddress);
        BaseClass.captureScreenshot();
        System.out.println("User input email address for login");
        return this;
    }

    By loginPasswordField_xpath = By.xpath("//input[@data-qa='login-password']");
    @Step("Fill password for login")
    public Login enterPasswordLogin(String PasswordLogin)
        throws IOException {
        BaseClass.enterText(loginPasswordField_xpath, PasswordLogin);
        BaseClass.captureScreenshot();
        System.out.println("User input password for login");
        return this;
    }

    By loginButton_xpath = By.xpath("//button[@data-qa='login-button']");
    @Step("Click Login Button")
    public Login clickLoginBtn()
        throws IOException {
        BaseClass.click(loginButton_xpath);
        BaseClass.captureScreenshot();
        System.out.println("User click login button");
        return this;
    }

    By signupNameField_xpath = By.xpath("//input[@data-qa='signup-name']");
    @Step("Input Name for SignUp")
    public Login inputNameSignup(String nameSignUp)
        throws IOException {
        BaseClass.enterText(signupNameField_xpath, nameSignUp);
        BaseClass.captureScreenshot();
        System.out.println("User input name for Sign Up");
        return this;
    }

    By signupEmailField_xpath = By.xpath("//input[@data-qa='signup-email']");
    @Step("Input Email for SignUp")
    public Login inputEmailSignUp(String emailSignUp)
        throws IOException {
        BaseClass.enterText(signupEmailField_xpath, emailSignUp);
        BaseClass.captureScreenshot();
        System.out.println("User input email address for Sign Up");
        return this;
    }

    By signUp_Gender1_radioBtn_id = By.id("id_gender1");
    By signUp_Gender2_radioBtn_id = By.id("id_gender2");
    @Step("Choose the user title")
    public Login chooseTitle(String titleUser)
        throws IOException {
        switch (titleUser.toLowerCase()) {
            case "mr" : {
                BaseClass.click(signUp_Gender1_radioBtn_id);
                System.out.println("User choose Mr. as a title");
                break;
            }
            case "mrs" : {
                BaseClass.click(signUp_Gender2_radioBtn_id);
                System.out.println("User choose Mrs. as a title");
                break;
            }
            default : {
                System.out.println("Title option are not available");
                break;
            }
        }
        BaseClass.captureScreenshot();
        return this;
    }

    By signUpPasswordField_id = By.id("password");
    @Step("Input Password for login")
    public Login inputPasswordSignup(String passwordSignUp)
        throws IOException {
        BaseClass.scrollToElement(signUpPasswordField_id);
        BaseClass.enterText(signUpPasswordField_id, passwordSignUp);
        BaseClass.captureScreenshot();
        return this;
    }

    By signupDaysOption_id = By.id("days");
    @Step("Choose Day of DOB")
    public Login chooseDaysDOB(String daysDOB)
        throws IOException {
        BaseClass.click(signupDaysOption_id);
        BaseClass.selectDropdownByValue(signupDaysOption_id, daysDOB);
        BaseClass.captureScreenshot();
        return this;
    }

}
