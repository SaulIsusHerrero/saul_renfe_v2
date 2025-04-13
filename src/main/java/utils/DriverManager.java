package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.time.Duration;

public class DriverManager {
    private static WebDriver webDriver;
    private static final String BROWSER = System.getProperty("browser", "chrome"); // Default: Chrome

    //Variables
    static Duration timeoutLong = Duration.ofSeconds(20);

    public static WebDriver getDriver() {
        if (webDriver == null) {
            switch (BROWSER.toLowerCase()) {
                case "firefox":
                    webDriver = new ChromeDriver();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("-private"); // Modo incógnito en Firefox
                    webDriver = new FirefoxDriver(firefoxOptions);
                    break;
                case "edge":
                    webDriver = new EdgeDriver();
                    break;
                case "chrome":
                default:
                    webDriver = new ChromeDriver();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--incognito");
                    webDriver = new ChromeDriver(chromeOptions);
                    break;
            }
            webDriver.manage().timeouts().implicitlyWait(timeoutLong);
            webDriver.manage().window().maximize();
        }
        return webDriver;
    }

    public static void closeDriver() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

}
