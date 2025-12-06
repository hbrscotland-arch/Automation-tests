package com.saucedemo.pages;

import com.saucedemo.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductDetailsPage extends BasePage {

    // Page Elements
    @FindBy(className = "inventory_details_name")
    private WebElement productName;

    @FindBy(className = "inventory_details_desc")
    private WebElement productDescription;

    @FindBy(className = "inventory_details_price")
    private WebElement productPrice;

    @FindBy(className = "inventory_details_img")
    private WebElement productImage;

    @FindBy(css = "button[data-test*='add-to-cart']")
    private WebElement addToCartButton;

    @FindBy(css = "button[data-test*='remove']")
    private WebElement removeFromCartButton;

    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

    @FindBy(className = "shopping_cart_link")
    private WebElement shoppingCartLink;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    // Constructor
    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    // Page Actions

    @Step("Verify product details page is displayed")
    public boolean isProductDetailsPageDisplayed() {
        waitForElementToBeVisible(productName);
        boolean isDisplayed = isElementDisplayed(productName) && 
                             isElementDisplayed(productPrice) && 
                             isElementDisplayed(productDescription);
        logger.info("Product details page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Get product name")
    public String getProductName() {
        String name = getText(productName);
        logger.info("Product name: {}", name);
        return name;
    }

    @Step("Get product description")
    public String getProductDescription() {
        String description = getText(productDescription);
        logger.info("Product description: {}", description);
        return description;
    }

    @Step("Get product price")
    public String getProductPrice() {
        String price = getText(productPrice);
        logger.info("Product price: {}", price);
        return price;
    }

    @Step("Verify product image is displayed")
    public boolean isProductImageDisplayed() {
        boolean isDisplayed = isElementDisplayed(productImage);
        logger.info("Product image displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Add product to cart")
    public ProductDetailsPage addProductToCart() {
        clickElement(addToCartButton);
        logger.info("Product added to cart from details page");
        return this;
    }

    @Step("Remove product from cart")
    public ProductDetailsPage removeProductFromCart() {
        clickElement(removeFromCartButton);
        logger.info("Product removed from cart from details page");
        return this;
    }

    @Step("Go back to products page")
    public ProductsPage goBackToProducts() {
        clickElement(backToProductsButton);
        logger.info("Navigated back to products page");
        return new ProductsPage(driver);
    }

    @Step("Go to shopping cart")
    public CartPage goToShoppingCart() {
        clickElement(shoppingCartLink);
        logger.info("Navigated to shopping cart from product details");
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

    // Verification Methods

    @Step("Verify add to cart button is displayed")
    public boolean isAddToCartButtonDisplayed() {
        return isElementDisplayed(addToCartButton);
    }

    @Step("Verify remove from cart button is displayed")
    public boolean isRemoveFromCartButtonDisplayed() {
        return isElementDisplayed(removeFromCartButton);
    }

    @Step("Verify cart has items")
    public boolean hasItemsInCart() {
        return isElementDisplayed(cartBadge);
    }

    @Step("Verify product details match expected values")
    public boolean verifyProductDetails(String expectedName, String expectedPrice) {
        boolean nameMatches = getProductName().equals(expectedName);
        boolean priceMatches = getProductPrice().equals(expectedPrice);
        
        logger.info("Product details verification - Name matches: {}, Price matches: {}", 
                   nameMatches, priceMatches);
        
        return nameMatches && priceMatches;
    }
}