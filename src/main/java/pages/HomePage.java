package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
    private By nextDay = By.xpath("//div[contains(@class, 'lightpick__day') and not(contains(@class, 'is-disabled')) and text()='%d']");
    private final By monthYearLabel = By.cssSelector("span.rf-daterange-picker-alternative__month-label");

    // Constructor
    public HomePage(WebDriver webDriver) {
        super(webDriver); // Calls to the constructor from parent class and their variable
    }

    // Methods
    /**
     * Accepts all cookies in any Page.
     */
    public void clickAcceptAllCookiesButton() {
        waitUntilElementIsDisplayed(acceptAllCookiesButton, TIMEOUT);
        scrollElementIntoView(acceptAllCookiesButton);
        clickElement(acceptAllCookiesButton);
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
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);
        WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(dateDepartureInput));
        button.click();
    }

    /**
     * Marks the "only go trip" radio button as selected or unselected.
     * @param expectedSelected boolean with the expected selected state of the element
     */
    public void clickSoloIdaButtonSelected(boolean expectedSelected) {
        waitUntilElementIsDisplayed(onlyDepartureRadioButtonLabel, TIMEOUT);
        scrollElementIntoView(onlyDepartureRadioButtonLabel);
        setElementSelected(onlyDepartureRadioButtonInput, onlyDepartureRadioButtonLabel, expectedSelected);
    }

    /**
     * Selects a departure date, a number of days ahead from the current date.
     * @param diasDespues Number of days to add to the current date
     */
    public void selectDateDaysLater(WebDriver webDriver, int diasDespues) {
        // 1. Configura el formato en español
        Locale espanol = new Locale("es", "ES");
        DateTimeFormatter formatoMes = DateTimeFormatter.ofPattern("MMMM", espanol);
        DateTimeFormatter formatoMesAnio = DateTimeFormatter.ofPattern("MMMM yyyy", espanol);

        // 2. Calcula la fecha objetivo (hoy + días)
        LocalDate fechaObjetivo = LocalDate.now().plusDays(diasDespues);
        int dia = fechaObjetivo.getDayOfMonth();
        String mes = fechaObjetivo.format(formatoMes).toLowerCase(); // "mayo"
        String mesAnio = fechaObjetivo.format(formatoMesAnio).toLowerCase(); // "mayo 2025"

        // 3. Navega al mes/año correcto
        navegarAlMesAnio(webDriver, mesAnio);
    }

    private void navegarAlMesAnio(WebDriver driver, String mesAnioObjetivo) {
        while (true) {
            String mesAnioActual = driver.findElement(By.cssSelector(".datepicker-switch")).getText().toLowerCase();
            if (mesAnioActual.equals(mesAnioObjetivo)) {
                break;
            }
            driver.findElement(By.cssSelector(".next")).click(); // Flecha "siguiente mes"
        }
    }

    /**
     * Method to click the 'Accept' button on the calendar in Home page.
     */
    public void clickAcceptButton() {
        waitUntilElementIsDisplayed(acceptButtonLocator, TIMEOUT);
        scrollElementIntoView(buscarBilleteLocator);
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
