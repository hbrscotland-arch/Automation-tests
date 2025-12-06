package com.saucedemo.pages;

import com.saucedemo.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    // Page Elements using Page Factory
    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    @FindBy(className = "login_logo")
    private WebElement loginLogo;

    @FindBy(className = "login_credentials")
    private WebElement acceptedUsernames;

    @FindBy(className = "login_password")
    private WebElement passwordInfo;

    // Constructor
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // Page Actions

    @Step("Navigate to login page")
    public LoginPage navigateToLoginPage(String url) {
        driver.get(url);
        waitForPageLoad();
        logger.info("Navigated to login page: {}", url);
        return this;
    }

    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        sendKeys(usernameField, username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        sendKeys(passwordField, password);
        return this;
    }

    @Step("Click login button")
    public ProductsPage clickLoginButton() {
        clickElement(loginButton);
        logger.info("Login button clicked");
        return new ProductsPage(driver);
    }

    @Step("Login with credentials: {username}")
    public ProductsPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    @Step("Login with invalid credentials: {username}")
    public LoginPage loginWithInvalidCredentials(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickElement(loginButton);
        logger.info("Attempted login with invalid credentials");
        return this;
    }

    // Verification Methods

    @Step("Verify login page is displayed")
    public boolean isLoginPageDisplayed() {
        boolean isDisplayed = isElementDisplayed(loginLogo) && 
                             isElementDisplayed(usernameField) && 
                             isElementDisplayed(passwordField) && 
                             isElementDisplayed(loginButton);
        logger.info("Login page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Verify error message is displayed")
    public boolean isErrorMessageDisplayed() {
        boolean isDisplayed = isElementDisplayed(errorMessage);
        logger.info("Error message displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Get error message text")
    public String getErrorMessage() {
        if (isErrorMessageDisplayed()) {
            String errorText = getText(errorMessage);
            logger.info("Error message: {}", errorText);
            return errorText;
        }
        return "";
    }

    @Step("Get accepted usernames")
    public String getAcceptedUsernames() {
        String usernames = getText(acceptedUsernames);
        logger.info("Accepted usernames: {}", usernames);
        return usernames;
    }

    @Step("Get password information")
    public String getPasswordInfo() {
        String passwordText = getText(passwordInfo);
        logger.info("Password info: {}", passwordText);
        return passwordText;
    }

    @Step("Clear login form")
    public LoginPage clearForm() {
        usernameField.clear();
        passwordField.clear();
        logger.info("Login form cleared");
        return this;
    }

    // Validation Methods

    @Step("Verify username field is enabled")
    public boolean isUsernameFieldEnabled() {
        return usernameField.isEnabled();
    }

    @Step("Verify password field is enabled")
    public boolean isPasswordFieldEnabled() {
        return passwordField.isEnabled();
    }

    @Step("Verify login button is enabled")
    public boolean isLoginButtonEnabled() {
        return loginButton.isEnabled();
    }
}