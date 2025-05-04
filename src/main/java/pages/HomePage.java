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
     * Types the trip origin
     * @param originStation
     * @param expectedValue
     */
    public void enterOrigin(String originStation, String expectedValue) {
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
     * @param expectedValue
     */
    public void enterDestination(String destinationStation, String expectedValue) {
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
     * Marks the 15 days ahead
     */
    public void selectDepartureDate15DaysLater() {
        LocalDate targetDate = LocalDate.now().plusDays(15);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE. dd/MM/yy", new Locale("es", "ES"));
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        // Límite de intentos (12 meses como máximo)
        int maxAttempts = 12;
        int attempts = 0;

        // Navigate to the correct month
        Boolean control = true;
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
     * Marks the 5 days ahead
     */
    public void selectDepartureDate5DaysLater(int defaultDaysLater) {
        LocalDate targetDate = LocalDate.now().plusDays(5);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE. dd/MM/yy", new Locale("es", "ES"));
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        // Límite de intentos (12 meses como máximo)
        int maxAttempts = 12;
        int attempts = 0;

        // Navigate to the correct month
        Boolean control = true;
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
        scrollElementIntoView(acceptButtonLocator);
        waitUntilElementIsDisplayed(acceptButtonLocator, TIMEOUT);
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
