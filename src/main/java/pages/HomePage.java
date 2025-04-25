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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage extends BasePage {
    // Locators
    public By  originInputLocator = By.xpath("//input[@id='origin']");
    public By  destinationInputLocator = By.xpath("//input[@id='destination']");
    private By dateDepartureInput = By.xpath("//input[@id='first-input']");
    private By onlyDepartureRadioButtonLabel = By.xpath("//label[@for='trip-go']");
    private By onlyDepartureRadioButtonInput = By.xpath("//input[@id='trip-go']");
    private By nextMonthButton = By.xpath("//button[contains(@class, 'lightpick__next-action')]");
    private By targetDayButton = By.xpath("//div[@class='lightpick__day is-available ']");
    private By monthYearLabel = By.cssSelector("span.rf-daterange-picker-alternative__month-label");
    private By acceptButtonLocator = By.xpath("//button[contains(text(),'Aceptar')]");
    private By buscarBilleteLocator = By.xpath("//button[@title='Buscar billete']");

    //Variables and Constants
    private final Duration TIMEOUT = Duration.ofSeconds(30);

    //Constructor
    public HomePage(WebDriver webDriver) {
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    // Methods
    /**
     * Types the trip origin
     * @param origin
     */
    public void enterOrigin(String origin) {
        WebElement originInput = webDriver.findElement(originInputLocator);

        //Enter the destination
        originInput.click();
        originInput.sendKeys(origin);
        originInput.sendKeys(Keys.DOWN);
        originInput.sendKeys(Keys.ENTER);

        //Asserts the origin station
        Assert.assertEquals("VALENCIA JOAQUÍN SOROLLA", originInput.getAttribute("value"));
    }

    /**
     * Types the trip destination
     * @param destination
     */
    public void enterDestination(String destination) {
        WebElement destinationInput = webDriver.findElement(destinationInputLocator);

        //Enter the destination
        destinationInput.click();
        destinationInput.sendKeys(destination);
        destinationInput.sendKeys(Keys.DOWN);
        destinationInput.sendKeys(Keys.ENTER);

        //Asserts the destination station
        Assert.assertEquals("BARCELONA-SANTS", destinationInput.getAttribute("value"));
    }

    /**
     * clicks on the departure date calendar in the Home page
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
     * Selecciona la fecha que es 15 días después del día actual en el datepicker
     * Navega por los meses si es necesario y selecciona el día correspondiente
     */

    public void selectDepartureDate15DaysLater() throws InterruptedException {
        LocalDate targetDate = LocalDate.now().plusDays(15);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE. dd/MM/yy", new Locale("es", "ES"));
        String targetDateText = targetDate.format(dateFormatter).toLowerCase();
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        // Navigate to the correct month
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es", "ES"));
        while (true) {
        String dateLabel = webDriver.findElement(monthYearLabel).getText().toLowerCase();
        if (dateLabel.contains(targetDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toLowerCase())) {
        break;
        }
        webDriver.findElement(nextMonthButton).click();
         wait.until(ExpectedConditions.visibilityOfElementLocated(monthYearLabel));
    }

    // Select the correct day
    String dayXpath = String.format("//div[contains(@class, 'lightpick__day') and text()='%d']", targetDate.getDayOfMonth());
    WebElement dayElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dayXpath)));

    // Scroll into view and click
    ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", dayElement);
    dayElement.click();
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
