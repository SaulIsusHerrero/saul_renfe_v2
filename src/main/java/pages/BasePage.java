package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.*;
import org.testng.Assert;
import java.time.Duration;

public class BasePage {
    //Driver initialization
    protected WebDriver webDriver; // Protected in order to be used by child classes.

    //Constructor with WebDriver as a parameter.
    public BasePage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);  // SAÚL : Inicializa los elementos con @FindBy en otras clases y el codigo es mas reusable siguiendo la estructura POM.
    }

    //Locators
    protected By acceptAllCookiesButton = By.id("onetrust-accept-btn-handler");

    //Variables
    Duration timeout = Duration.ofSeconds(5);

    /**
     * Writes text inside a given element locator.
     *
     * @param locator By with the locator of the element.
     * @param text    String with the text that should be written.
     */
    public void setElementText(By locator, String text) {
        webDriver.findElement(locator).sendKeys(text);
    }

    /**
     * Clicks a given element locator.
     *
     * @param locator By with the locator of the element.
     */
    public void clickElement(By locator) {
        WebElement element = webDriver.findElement(locator);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", element);
    }

    /**
     * Returns "true" or "false" depending on if a given element locator is currently selected or unselected.
     * Normally used to interact with checkboxes or radio buttons.
     *
     * @param inputLocator By with the input locator of the element.
     */
    public boolean isElementSelected(By inputLocator) {
        return webDriver.findElement(inputLocator).isSelected();
    }

    /**
     * Scrolls a given element locator to the center of the screen.
     *
     * @param locator By with the locator of the element.
     */
    public void scrollElementIntoView(By locator) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView({block: 'center'});",
                webDriver.findElement(locator));
    }

    /**
     * Desplaza el elemento a la vista
     */
    void scrollElementIntoViewElement(WebElement element) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Marks a given element locator as selected or unselected.
     * Normally used to interact with checkboxes or radio buttons.
     *
     * @param inputLocator     By with the input locator of the element
     * @param labelLocator     By with the label locator of the element
     * @param expectedSelected boolean with the expected selected state of the element
     */
    public void setElementSelected(By inputLocator, By labelLocator, boolean expectedSelected) {
        boolean actualSelected = isElementSelected(inputLocator);
        if (actualSelected != expectedSelected) {
            scrollElementIntoView(labelLocator);
            clickElement(labelLocator);
        }
    }

    /**
     * Accepts all cookies in any Page.
     */
    public void clickAcceptAllCookiesButton() {
        WebElement acceptButton = new WebDriverWait(webDriver, timeout).
        until(ExpectedConditions.elementToBeClickable(acceptAllCookiesButton));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", acceptButton);
    }

    /**
     * Waits until an element is displayed in any Page.
     * @param locator as a By
     * @param timeout as a Duration
     * @return locator as a String
     */
    public String waitUntilElementIsDisplayed(By locator, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(webDriver, timeout);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        Assert.assertTrue(element.isDisplayed(), "The element located by " + locator.toString() + " is not displayed");
        return locator.toString();
    }

    /**
     * Returns "true" or "false" depending on if a given element locator currently appears or not
     *
     * @param locator By with the locator of the element
     */
    public boolean isElementDisplayed(By locator) {
        try {
            return webDriver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns the text inside a given element locator
     *
     * @param locator By with the locator of the element
     */
    public String getElementText(By locator) {
        return webDriver.findElement(locator).getText();
    }
}