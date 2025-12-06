package com.saucedemo.pages;

import com.saucedemo.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CheckoutPage extends BasePage {

    // Checkout Information Page Elements
    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // Checkout Overview Page Elements
    @FindBy(className = "cart_item")
    private List<WebElement> checkoutItems;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> itemNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> itemPrices;

    @FindBy(className = "summary_subtotal_label")
    private WebElement subtotalLabel;

    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;

    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;

    @FindBy(id = "finish")
    private WebElement finishButton;

    // Checkout Complete Page Elements
    @FindBy(className = "complete-header")
    private WebElement completeHeader;

    @FindBy(className = "complete-text")
    private WebElement completeText;

    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

    // Constructor
    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // Checkout Information Page Actions

    @Step("Verify checkout information page is displayed")
    public boolean isCheckoutInformationPageDisplayed() {
        waitForElementToBeVisible(pageTitle);
        boolean isDisplayed = getText(pageTitle).equals("Checkout: Your Information");
        logger.info("Checkout information page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Fill checkout information with first name: {firstName}, last name: {lastName}, postal code: {postalCode}")
    public CheckoutPage fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        return this;
    }

    @Step("Enter first name: {firstName}")
    public CheckoutPage enterFirstName(String firstName) {
        sendKeys(firstNameField, firstName);
        return this;
    }

    @Step("Enter last name: {lastName}")
    public CheckoutPage enterLastName(String lastName) {
        sendKeys(lastNameField, lastName);
        return this;
    }

    @Step("Enter postal code: {postalCode}")
    public CheckoutPage enterPostalCode(String postalCode) {
        sendKeys(postalCodeField, postalCode);
        return this;
    }

    @Step("Click continue to checkout overview")
    public CheckoutPage clickContinue() {
        clickElement(continueButton);
        logger.info("Clicked continue to proceed to checkout overview");
        return this;
    }

    @Step("Cancel checkout and return to cart")
    public CartPage cancelCheckout() {
        clickElement(cancelButton);
        logger.info("Cancelled checkout and returned to cart");
        return new CartPage(driver);
    }

    // Checkout Overview Page Actions

    @Step("Verify checkout overview page is displayed")
    public boolean isCheckoutOverviewPageDisplayed() {
        waitForElementToBeVisible(pageTitle);
        boolean isDisplayed = getText(pageTitle).equals("Checkout: Overview");
        logger.info("Checkout overview page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Get number of items in checkout")
    public int getNumberOfItemsInCheckout() {
        int count = checkoutItems.size();
        logger.info("Number of items in checkout: {}", count);
        return count;
    }

    @Step("Get all checkout item names")
    public List<String> getAllCheckoutItemNames() {
        List<String> names = itemNames.stream()
                .map(this::getText)
                .toList();
        logger.info("Checkout item names: {}", names);
        return names;
    }

    @Step("Get subtotal amount")
    public String getSubtotal() {
        String subtotal = getText(subtotalLabel);
        logger.info("Subtotal: {}", subtotal);
        return subtotal;
    }

    @Step("Get tax amount")
    public String getTax() {
        String tax = getText(taxLabel);
        logger.info("Tax: {}", tax);
        return tax;
    }

    @Step("Get total amount")
    public String getTotal() {
        String total = getText(totalLabel);
        logger.info("Total: {}", total);
        return total;
    }

    @Step("Finish checkout")
    public CheckoutPage finishCheckout() {
        clickElement(finishButton);
        logger.info("Clicked finish to complete checkout");
        return this;
    }

    // Checkout Complete Page Actions

    @Step("Verify checkout complete page is displayed")
    public boolean isCheckoutCompletePageDisplayed() {
        waitForElementToBeVisible(completeHeader);
        boolean isDisplayed = getText(completeHeader).contains("Thank you");
        logger.info("Checkout complete page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Get completion header text")
    public String getCompleteHeader() {
        String header = getText(completeHeader);
        logger.info("Completion header: {}", header);
        return header;
    }

    @Step("Get completion message text")
    public String getCompleteText() {
        String text = getText(completeText);
        logger.info("Completion text: {}", text);
        return text;
    }

    @Step("Go back to products after checkout completion")
    public ProductsPage goBackToProducts() {
        clickElement(backToProductsButton);
        logger.info("Returned to products page after checkout completion");
        return new ProductsPage(driver);
    }

    // Common Verification Methods

    @Step("Get page title")
    public String getPageTitle() {
        String title = getText(pageTitle);
        logger.info("Current page title: {}", title);
        return title;
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

    // Validation Methods

    @Step("Verify all required fields are filled")
    public boolean areRequiredFieldsFilled() {
        boolean firstNameFilled = !firstNameField.getAttribute("value").isEmpty();
        boolean lastNameFilled = !lastNameField.getAttribute("value").isEmpty();
        boolean postalCodeFilled = !postalCodeField.getAttribute("value").isEmpty();
        
        boolean allFilled = firstNameFilled && lastNameFilled && postalCodeFilled;
        logger.info("All required fields filled: {}", allFilled);
        return allFilled;
    }

    @Step("Verify item exists in checkout: {itemName}")
    public boolean isItemInCheckout(String itemName) {
        boolean exists = itemNames.stream()
                .anyMatch(name -> getText(name).equals(itemName));
        logger.info("Item '{}' in checkout: {}", itemName, exists);
        return exists;
    }

    @Step("Complete full checkout process with user info")
    public CheckoutPage completeCheckout(String firstName, String lastName, String postalCode) {
        fillCheckoutInformation(firstName, lastName, postalCode);
        clickContinue();
        waitForElementToBeVisible(finishButton);
        finishCheckout();
        logger.info("Completed full checkout process");
        return this;
    }
}