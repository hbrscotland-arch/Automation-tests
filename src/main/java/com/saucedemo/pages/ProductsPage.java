package com.saucedemo.pages;

import com.saucedemo.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ProductsPage extends BasePage {

    // Page Elements
    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "shopping_cart_link")
    private WebElement shoppingCartLink;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;

    @FindBy(className = "inventory_item")
    private List<WebElement> productItems;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(css = "button[data-test*='add-to-cart']")
    private List<WebElement> addToCartButtons;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @FindBy(id = "inventory_sidebar_link")
    private WebElement allItemsLink;

    @FindBy(id = "about_sidebar_link")
    private WebElement aboutLink;

    @FindBy(id = "reset_sidebar_link")
    private WebElement resetAppStateLink;

    // Constructor
    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    // Page Actions

    @Step("Verify products page is displayed")
    public boolean isProductsPageDisplayed() {
        waitForElementToBeVisible(pageTitle);
        boolean isDisplayed = getText(pageTitle).equals("Products");
        logger.info("Products page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Get page title")
    public String getPageTitle() {
        String title = getText(pageTitle);
        logger.info("Page title: {}", title);
        return title;
    }

    @Step("Get number of products")
    public int getNumberOfProducts() {
        int count = productItems.size();
        logger.info("Number of products: {}", count);
        return count;
    }

    @Step("Get all product names")
    public List<String> getAllProductNames() {
        List<String> names = productNames.stream()
                .map(this::getText)
                .toList();
        logger.info("Product names: {}", names);
        return names;
    }

    @Step("Get all product prices")
    public List<String> getAllProductPrices() {
        List<String> prices = productPrices.stream()
                .map(this::getText)
                .toList();
        logger.info("Product prices: {}", prices);
        return prices;
    }

    @Step("Add product to cart by name: {productName}")
    public ProductsPage addProductToCart(String productName) {
        String buttonId = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        WebElement addButton = driver.findElement(By.id(buttonId));
        clickElement(addButton);
        logger.info("Added product to cart: {}", productName);
        return this;
    }

    @Step("Add product to cart by index: {index}")
    public ProductsPage addProductToCartByIndex(int index) {
        if (index >= 0 && index < addToCartButtons.size()) {
            clickElement(addToCartButtons.get(index));
            String productName = getText(productNames.get(index));
            logger.info("Added product to cart by index {}: {}", index, productName);
        } else {
            logger.warn("Invalid product index: {}", index);
        }
        return this;
    }

    @Step("Remove product from cart by name: {productName}")
    public ProductsPage removeProductFromCart(String productName) {
        String buttonId = "remove-" + productName.toLowerCase().replace(" ", "-");
        WebElement removeButton = driver.findElement(By.id(buttonId));
        clickElement(removeButton);
        logger.info("Removed product from cart: {}", productName);
        return this;
    }

    @Step("Click on product: {productName}")
    public ProductDetailsPage clickOnProduct(String productName) {
        WebElement product = productNames.stream()
                .filter(name -> getText(name).equals(productName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found: " + productName));
        
        clickElement(product);
        logger.info("Clicked on product: {}", productName);
        return new ProductDetailsPage(driver);
    }

    @Step("Sort products by: {sortOption}")
    public ProductsPage sortProducts(String sortOption) {
        selectFromDropdown(sortDropdown, sortOption);
        logger.info("Sorted products by: {}", sortOption);
        return this;
    }

    @Step("Go to shopping cart")
    public CartPage goToShoppingCart() {
        clickElement(shoppingCartLink);
        logger.info("Navigated to shopping cart");
        return new CartPage(driver);
    }

    @Step("Get cart items count")
    public String getCartItemsCount() {
        if (isElementDisplayed(cartBadge)) {
            String count = getText(cartBadge);
            logger.info("Cart items count: {}", count);
            return count;
        }
        return "0";
    }

    @Step("Open hamburger menu")
    public ProductsPage openMenu() {
        clickElement(menuButton);
        logger.info("Hamburger menu opened");
        return this;
    }

    @Step("Logout from application")
    public LoginPage logout() {
        openMenu();
        waitForElementToBeVisible(logoutLink);
        clickElement(logoutLink);
        logger.info("Logged out from application");
        return new LoginPage(driver);
    }

    @Step("Reset application state")
    public ProductsPage resetAppState() {
        openMenu();
        waitForElementToBeVisible(resetAppStateLink);
        clickElement(resetAppStateLink);
        logger.info("Application state reset");
        return this;
    }

    // Verification Methods

    @Step("Verify product exists: {productName}")
    public boolean isProductDisplayed(String productName) {
        boolean exists = productNames.stream()
                .anyMatch(name -> getText(name).equals(productName));
        logger.info("Product '{}' displayed: {}", productName, exists);
        return exists;
    }

    @Step("Verify cart has items")
    public boolean hasItemsInCart() {
        return isElementDisplayed(cartBadge);
    }

    @Step("Get product price by name: {productName}")
    public String getProductPrice(String productName) {
        for (int i = 0; i < productNames.size(); i++) {
            if (getText(productNames.get(i)).equals(productName)) {
                String price = getText(productPrices.get(i));
                logger.info("Price for '{}': {}", productName, price);
                return price;
            }
        }
        logger.warn("Product price not found for: {}", productName);
        return "";
    }
}