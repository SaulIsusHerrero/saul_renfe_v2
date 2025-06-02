package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.util.Locale;

public class DriverManager {

    public static WebDriver getDriver(String browser) {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String driverPathPrefix;

        if (os.contains("win")) {
            driverPathPrefix = "drivers/windows/";
        } else if (os.contains("mac")) {
            driverPathPrefix = "drivers/mac/";
        } else {
            driverPathPrefix = "drivers/linux/";
        }

        switch (browser.toLowerCase(Locale.ROOT)) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver",
                        driverPathPrefix + (os.contains("win") ? "chromedriver.exe" : "chromedriver"));
                return new ChromeDriver();
            case "firefox":
                System.setProperty("webdriver.gecko.driver",
                        driverPathPrefix + (os.contains("win") ? "geckodriver.exe" : "geckodriver"));
                return new FirefoxDriver();
            case "edge":
                System.setProperty("webdriver.edge.driver",
                        driverPathPrefix + (os.contains("win") ? "msedgedriver.exe" : "msedgedriver"));
                return new EdgeDriver();
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
}

