package com.saucedemo.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {
    
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    
    public static WebDriver createDriver(String browserName) {
        WebDriver driver = null;
        
        try {
            switch (browserName.toLowerCase()) {
                case "chrome":
                    logger.info("Initializing Chrome browser");
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = getChromeOptions();
                    driver = new ChromeDriver(chromeOptions);
                    break;
                    
                case "firefox":
                    logger.info("Initializing Firefox browser");
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = getFirefoxOptions();
                    driver = new FirefoxDriver(firefoxOptions);
                    break;
                    
                case "edge":
                    logger.info("Initializing Edge browser");
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = getEdgeOptions();
                    driver = new EdgeDriver(edgeOptions);
                    break;
                    
                case "headless-chrome":
                    logger.info("Initializing headless Chrome browser");
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions headlessChromeOptions = getChromeOptions();
                    headlessChromeOptions.addArguments("--headless");
                    driver = new ChromeDriver(headlessChromeOptions);
                    break;
                    
                default:
                    logger.warn("Browser '{}' not supported. Defaulting to Chrome", browserName);
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(getChromeOptions());
                    break;
            }
            
            logger.info("WebDriver initialized successfully for browser: {}", browserName);
            
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver for browser: {}", browserName, e);
            throw new RuntimeException("Failed to create WebDriver for browser: " + browserName, e);
        }
        
        return driver;
    }
    
    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        
        // Performance optimizations
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        
        // Security and privacy
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        
        // UI improvements
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        
        // Set window size for consistency
        options.addArguments("--window-size=1920,1080");
        
        logger.debug("Chrome options configured");
        return options;
    }
    
    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        
        // Performance optimizations
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("media.autoplay.default", 2);
        
        logger.debug("Firefox options configured");
        return options;
    }
    
    private static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        
        // Performance optimizations
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        
        // Set window size for consistency
        options.addArguments("--window-size=1920,1080");
        
        logger.debug("Edge options configured");
        return options;
    }
}