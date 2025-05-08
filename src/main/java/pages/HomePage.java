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
    private final By monthYearLabel = By.cssSelector("span.rf-daterange-picker-alternative__month-label");

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
     * Selects a departure date a number of days ahead from the current date.
     * @param daysLater Number of days to add to the current date
     */
    /**
     * Selects a departure date a number of days ahead from the current date.
     * @param daysLater Number of days to add to the current date
     */
    public void selectDepartureDateDaysLater(int daysLater) {
        LocalDate targetDate = LocalDate.now().plusDays(daysLater);
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es", "ES"));
        String targetMonthYear = targetDate.format(monthFormatter);

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
        final int maxAttempts = 12;
        int attempts = 0;

        while (attempts < maxAttempts) {
            String currentMonthYear = wait.until(ExpectedConditions
                            .visibilityOfElementLocated(monthYearLabel))
                    .getText()
                    .trim();

            if (normalizeMonth(currentMonthYear).equals(normalizeMonth(targetMonthYear))) {
                break;
            }

            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(nextMonthButton));
            nextButton.click();

            wait.until(ExpectedConditions.not(
                    ExpectedConditions.textToBePresentInElementLocated(monthYearLabel, currentMonthYear)));

            attempts++;
        }

        if (attempts >= maxAttempts) {
            throw new RuntimeException("No se encontró el mes: " + targetMonthYear + " tras " + maxAttempts + " intentos.");
        }

        // Seleccionar el día
        String dayXpath = String.format(
                "//div[contains(@class, 'lightpick__day') and not(contains(@class, 'is-disabled')) and text()='%d']",
                targetDate.getDayOfMonth());

        try {
            WebElement dayElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dayXpath)));
            dayElement.click();
        } catch (TimeoutException e) {
            throw new RuntimeException(
                    "No se pudo seleccionar el día " + targetDate.getDayOfMonth() + " del mes " + targetMonthYear, e);
        }

        clickAcceptButton();
        clickSearchTicketButton();
    }

    private String normalizeMonth(String monthText) {
        return monthText.toLowerCase().replaceAll("[^a-záéíóúüñ0-9]", "");
    }

    /**
     * Method to click the 'Accept' button on the calendar in Home page.
     */
    public void clickAcceptButton() {
        waitUntilElementIsDisplayed(acceptButtonLocator, TIMEOUT);
        clickElement(acceptButtonLocator);
    }

    /**
     * Searches the selected ticket in the Home page.
     */
    public void clickSearchTicketButton() {
        waitUntilElementIsDisplayed(buscarBilleteLocator, Duration.ofSeconds(30));
        scrollElementIntoView(buscarBilleteLocator);
        clickElement(buscarBilleteLocator);
    }

}
