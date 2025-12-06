package com.saucedemo.pages;

import com.saucedemo.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CartPage extends BasePage {

    // Page Elements
    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> cartItemNames;

    @FindBy(className = "inventory_item_desc")
    private List<WebElement> cartItemDescriptions;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> cartItemPrices;

    @FindBy(css = "button[data-test*='remove']")
    private List<WebElement> removeButtons;

    @FindBy(className = "cart_quantity")
    private List<WebElement> itemQuantities;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    // Constructor
    public CartPage(WebDriver driver) {
        super(driver);
    }

    // Page Actions

    @Step("Verify cart page is displayed")
    public boolean isCartPageDisplayed() {
        waitForElementToBeVisible(pageTitle);
        boolean isDisplayed = getText(pageTitle).equals("Your Cart");
        logger.info("Cart page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Get page title")
    public String getPageTitle() {
        String title = getText(pageTitle);
        logger.info("Cart page title: {}", title);
        return title;
    }

    @Step("Get number of items in cart")
    public int getNumberOfItemsInCart() {
        int count = cartItems.size();
        logger.info("Number of items in cart: {}", count);
        return count;
    }

    @Step("Get all cart item names")
    public List<String> getAllCartItemNames() {
        List<String> names = cartItemNames.stream()
                .map(this::getText)
                .toList();
        logger.info("Cart item names: {}", names);
        return names;
    }

    @Step("Get all cart item prices")
    public List<String> getAllCartItemPrices() {
        List<String> prices = cartItemPrices.stream()
                .map(this::getText)
                .toList();
        logger.info("Cart item prices: {}", prices);
        return prices;
    }

    @Step("Remove item from cart by name: {itemName}")
    public CartPage removeItemFromCart(String itemName) {
        for (int i = 0; i < cartItemNames.size(); i++) {
            if (getText(cartItemNames.get(i)).equals(itemName)) {
                clickElement(removeButtons.get(i));
                logger.info("Removed item from cart: {}", itemName);
                return this;
            }
        }
        logger.warn("Item not found in cart: {}", itemName);
        return this;
    }

    @Step("Remove item from cart by index: {index}")
    public CartPage removeItemFromCartByIndex(int index) {
        if (index >= 0 && index < removeButtons.size()) {
            String itemName = getText(cartItemNames.get(index));
            clickElement(removeButtons.get(index));
            logger.info("Removed item from cart by index {}: {}", index, itemName);
        } else {
            logger.warn("Invalid cart item index: {}", index);
        }
        return this;
    }

    @Step("Continue shopping")
    public ProductsPage continueShopping() {
        clickElement(continueShoppingButton);
        logger.info("Continued shopping - navigated to products page");
        return new ProductsPage(driver);
    }

    @Step("Proceed to checkout")
    public CheckoutPage proceedToCheckout() {
        clickElement(checkoutButton);
        logger.info("Proceeded to checkout");
        return new CheckoutPage(driver);
    }

    @Step("Get cart badge count")
    public String getCartBadgeCount() {
        if (isElementDisplayed(cartBadge)) {
            String count = getText(cartBadge);
            logger.info("Cart badge count: {}", count);
            return count;
        }
        return "0";
    }

    @Step("Get item quantity by name: {itemName}")
    public String getItemQuantity(String itemName) {
        for (int i = 0; i < cartItemNames.size(); i++) {
            if (getText(cartItemNames.get(i)).equals(itemName)) {
                String quantity = getText(itemQuantities.get(i));
                logger.info("Quantity for '{}': {}", itemName, quantity);
                return quantity;
            }
        }
        logger.warn("Item quantity not found for: {}", itemName);
        return "0";
    }

    // Verification Methods

    @Step("Verify cart is empty")
    public boolean isCartEmpty() {
        boolean isEmpty = cartItems.isEmpty();
        logger.info("Cart is empty: {}", isEmpty);
        return isEmpty;
    }

    @Step("Verify item exists in cart: {itemName}")
    public boolean isItemInCart(String itemName) {
        boolean exists = cartItemNames.stream()
                .anyMatch(name -> getText(name).equals(itemName));
        logger.info("Item '{}' in cart: {}", itemName, exists);
        return exists;
    }

    @Step("Verify checkout button is enabled")
    public boolean isCheckoutButtonEnabled() {
        return checkoutButton.isEnabled();
    }

    @Step("Verify continue shopping button is enabled")
    public boolean isContinueShoppingButtonEnabled() {
        return continueShoppingButton.isEnabled();
    }

    @Step("Get item description by name: {itemName}")
    public String getItemDescription(String itemName) {
        for (int i = 0; i < cartItemNames.size(); i++) {
            if (getText(cartItemNames.get(i)).equals(itemName)) {
                String description = getText(cartItemDescriptions.get(i));
                logger.info("Description for '{}': {}", itemName, description);
                return description;
            }
        }
        logger.warn("Item description not found for: {}", itemName);
        return "";
    }

    @Step("Get item price by name: {itemName}")
    public String getItemPrice(String itemName) {
        for (int i = 0; i < cartItemNames.size(); i++) {
            if (getText(cartItemNames.get(i)).equals(itemName)) {
                String price = getText(cartItemPrices.get(i));
                logger.info("Price for '{}': {}", itemName, price);
                return price;
            }
        }
        logger.warn("Item price not found for: {}", itemName);
        return "";
    }
}