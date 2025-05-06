package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class HomePage extends BasePage {
    // Locators
    public By acceptAllCookiesButton = By.id("onetrust-accept-btn-handler");
    public By originInputLocator = By.xpath("//input[@id='origin']");
    public By destinationInputLocator = By.xpath("//input[@id='destination']");
    private By dateDepartureInput = By.xpath("//input[@id='first-input']");
    private By onlyDepartureRadioButtonLabel = By.xpath("//label[@for='trip-go']");
    private By onlyDepartureRadioButtonInput = By.xpath("//input[@id='trip-go']");
    private By acceptButtonLocator = By.xpath("//button[contains(text(),'Aceptar')]");
    private By buscarBilleteLocator = By.xpath("//button[@title='Buscar billete']");
    private By nextMonthButton = By.xpath("//button[contains(@class, 'lightpick__next-action')]");
    private By monthYearLabel = By.cssSelector("span.rf-daterange-picker-alternative__month-label");

    // Variables and Constants
    private final Duration TIMEOUT = Duration.ofSeconds(30);

    // Constructor
    public HomePage(WebDriver webDriver) {
        super(webDriver); // Calls to the constructor from parent class and their variable
    }

    // Methods
    /**
     * Accepts all cookies in any Page.
     */
    public void clickAcceptAllCookiesButton() {
        WebElement acceptButton = new WebDriverWait(webDriver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(acceptAllCookiesButton));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", acceptButton);
    }

    /**
     * Types the trip origin
     * @param originStation
     */
    public void enterOrigin(String originStation) {
        WebElement originInput = webDriver.findElement(originInputLocator);

        // Enter the origin
        originInput.click();
        originInput.sendKeys(originStation);
        originInput.sendKeys(Keys.DOWN);
        originInput.sendKeys(Keys.ENTER);

        // Asserts the origin station
        Assert.assertEquals("VALENCIA JOAQUÍN SOROLLA", originInput.getAttribute("value"));
    }

    /**
     * Types the trip destination
     * @param destinationStation
     */
    public void enterDestination(String destinationStation) {
        WebElement destinationInput = webDriver.findElement(destinationInputLocator);

        // Enter the destination
        destinationInput.click();
        destinationInput.sendKeys(destinationStation);
        destinationInput.sendKeys(Keys.DOWN);
        destinationInput.sendKeys(Keys.ENTER);

        // Asserts the destination station
        Assert.assertEquals("BARCELONA-SANTS", destinationInput.getAttribute("value"));
    }

    /**
     * Clicks on the departure date calendar in the Home page
     */
    public void selectDepartureDate() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(dateDepartureInput));
        button.click();
    }

    /**
     * Marks the "only go trip" radio button as selected or unselected.
     * @param expectedSelected boolean with the expected selected state of the element
     */
    public void clickSoloIdaButtonSelected(boolean expectedSelected) {
        waitUntilElementIsDisplayed(onlyDepartureRadioButtonLabel, Duration.ofSeconds(30));
        scrollElementIntoView(onlyDepartureRadioButtonLabel);
        setElementSelected(onlyDepartureRadioButtonInput, onlyDepartureRadioButtonLabel, expectedSelected);
    }
    /**
     * Marks the days ahead
     */
    public void selectDepartureDateDaysLater(int daysLater) {
        LocalDate targetDate = LocalDate.now().plusDays(daysLater);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE. dd/MM/yy", new Locale("es", "ES"));
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        // Límite de intentos (12 meses como máximo)
        int maxAttempts = 12;
        int attempts = 0;

        // Navigate to the correct month
        while (attempts<maxAttempts) {
            String dateLabel = webDriver.findElement(monthYearLabel).getText().toLowerCase();
            if (dateLabel.contains(targetDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toLowerCase())) {
                while (attempts < maxAttempts) {
                    if (dateLabel.contains(targetDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toLowerCase())) {
                        break;
                    }
                    webDriver.findElement(nextMonthButton).click();
                    wait.until(ExpectedConditions.visibilityOfElementLocated(monthYearLabel));
                    attempts++;
                }
            }
            // Verificar si se encontró el mes o si se agotaron los intentos
            if (attempts >= maxAttempts) {
                throw new RuntimeException("No se encontró el mes objetivo después de " + maxAttempts + " intentos");
            }

            // Select the correct day
            String dayXpath = String.format("//div[contains(@class, 'lightpick__day') and text()='%d']", targetDate.getDayOfMonth());
            WebElement dayElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dayXpath)));

            // Scroll into view and click
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", dayElement);
            dayElement.click();
        }
    }

    /**
     * Method to click the 'Accept' button on the calendar in Home page.
     */
    public void clickAcceptButton() {
        waitUntilElementIsDisplayed(acceptButtonLocator, TIMEOUT);
        scrollElementIntoView(acceptButtonLocator);
        clickElement(acceptButtonLocator);
    }

    /**
     * Searches the selected ticket in the Home page.
     */
    public void clickSearchTicketButton() {
        waitUntilElementIsDisplayed(buscarBilleteLocator, TIMEOUT);
        scrollElementIntoView(buscarBilleteLocator);
        clickElement(buscarBilleteLocator);
    }

}
