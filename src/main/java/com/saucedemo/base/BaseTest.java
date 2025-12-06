package com.saucedemo.base;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import com.saucedemo.utils.DriverFactory;

@Listeners({BaseTest.class})
public class BaseTest implements ITestListener {
    
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    
    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(String browser) {
        logger.info("Setting up test with browser: " + browser);
        WebDriver driver = DriverFactory.createDriver(browser);
        driverThreadLocal.set(driver);
        
        driver.manage().window().maximize();
        logger.info("Browser maximized");
        
        Allure.addAttachment("Test Environment", "Browser: " + browser);
    }
    
    @AfterMethod
    public void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            logger.info("Closing browser");
            driver.quit();
            driverThreadLocal.remove();
        }
    }
    
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: " + result.getMethod().getMethodName());
        logger.error("Failure reason: " + result.getThrowable().getMessage());
        
        WebDriver driver = getDriver();
        if (driver != null) {
            BasePage basePage = new BasePage(driver);
            byte[] screenshot = basePage.takeScreenshot();
            if (screenshot.length > 0) {
                Allure.addAttachment("Failure Screenshot", "image/png", 
                    new java.io.ByteArrayInputStream(screenshot), ".png");
            }
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: " + result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: " + result.getMethod().getMethodName());
        Allure.addAttachment("Test Method", result.getMethod().getMethodName());
    }
}