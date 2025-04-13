package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UtilsMethods {

    private WebDriver webDriver;
    private final int DEFAULT_TIMEOUT = 10;

    /**
     * Constructor que recibe la instancia de WebDriver
     *
     * @param webDriver el WebDriver usado en el test
     */
    public UtilsMethods(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    /**
     * Verifica si un elemento está presente en el DOM y visible.
     *
     * @param locator        By: el localizador del elemento.
     * @param timeoutSeconds int: segundos máximos de espera.
     * @return true si el elemento está visible, false si no lo está o no aparece.
     */
    public boolean isElementVisibleInDOM(By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(timeoutSeconds));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

}