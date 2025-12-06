package com.saucedemo.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        try {
            properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fileInputStream);
            fileInputStream.close();
            logger.info("Configuration properties loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load configuration properties from: {}", CONFIG_FILE_PATH, e);
            throw new RuntimeException("Failed to load configuration properties", e);
        }
    }
    
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            logger.debug("Retrieved property '{}' = '{}'", key, value);
        } else {
            logger.warn("Property '{}' not found in configuration", key);
        }
        return value;
    }
    
    public static String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        logger.debug("Retrieved property '{}' = '{}' (default: '{}')", key, value, defaultValue);
        return value;
    }
    
    // Convenience methods for common configurations
    public static String getBaseUrl() {
        return getProperty("base.url", "https://www.saucedemo.com/");
    }
    
    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    public static int getImplicitWait() {
        String wait = getProperty("implicit.wait", "10");
        try {
            return Integer.parseInt(wait);
        } catch (NumberFormatException e) {
            logger.warn("Invalid implicit wait value '{}', using default: 10", wait);
            return 10;
        }
    }
    
    public static int getExplicitWait() {
        String wait = getProperty("explicit.wait", "10");
        try {
            return Integer.parseInt(wait);
        } catch (NumberFormatException e) {
            logger.warn("Invalid explicit wait value '{}', using default: 10", wait);
            return 10;
        }
    }
    
    public static boolean isHeadlessMode() {
        String headless = getProperty("headless.mode", "false");
        return Boolean.parseBoolean(headless);
    }
    
    // Test data getters
    public static String getValidUsername() {
        return getProperty("valid.username", "standard_user");
    }
    
    public static String getValidPassword() {
        return getProperty("valid.password", "secret_sauce");
    }
    
    public static String getLockedUsername() {
        return getProperty("locked.username", "locked_out_user");
    }
    
    public static String getInvalidUsername() {
        return getProperty("invalid.username", "invalid_user");
    }
    
    public static String getInvalidPassword() {
        return getProperty("invalid.password", "invalid_password");
    }
}