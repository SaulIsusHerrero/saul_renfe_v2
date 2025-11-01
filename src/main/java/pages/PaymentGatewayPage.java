package pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PaymentGatewayPage extends BasePage {
    //Locators
    private By cardNumber = By.xpath("//input[@id='card-number']");
    private By expirationField = By.xpath("//input[@id='card-expiration']");
    private By totalPricePasarelaLocator = By.xpath("//div[@class='right']");
    private By cvvField = By.xpath("//input[@id='card-cvv']");
    private By btnPayment = By.xpath("//button[@onclick='javascript:pago()']");
    private By popUpError = By.xpath("//div[@id='myModalBody']//li[contains(text(), 'Tarjeta no soportada (RS18)')]");

    //Constructor
    public PaymentGatewayPage(WebDriver webDriver) {
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    //Methods
    /**
     * Asserts that I am in the "PaymentGateway" Page
     */
    public void verifyYouAreInPasarelaPagoPage() {
        String actualUrl = webDriver.getCurrentUrl().trim();
        String expectedUrlPattern = "sis.redsys.es/sis/realizarPago";
        try {
            new WebDriverWait(webDriver, TIMEOUT).until(ExpectedConditions.urlContains(expectedUrlPattern));
        } catch (TimeoutException e) {
            Assert.fail("The URL doesn´t contain '" + expectedUrlPattern + "' after 15 seconds. present URL : " + actualUrl);
        }
    }

    /**
     * Verifies the ticket price.
     *
     * @param totalPriceTrip Price previously obtained, already normalized
     */
    public void verifyTotalPrice(String totalPriceTrip) {
        waitUntilElementIsDisplayed(totalPricePasarelaLocator, TIMEOUT);

        // Normalize the price from the new Page
        String totalPricePasarela = normalizePrice(webDriver.findElement(totalPricePasarelaLocator).getText());

        // The received price should already be normalized, but for safety:
        totalPriceTrip = normalizePrice(totalPriceTrip);

        Assert.assertEquals(totalPricePasarela, totalPriceTrip);
    }

    /**
     * Types the bank card in the textbox on the "PaymentGateway" Page.
     * @param bankCard as a string
     */
    public void typeBankCard(String bankCard) {
        waitUntilElementIsDisplayed(cardNumber, TIMEOUT);
        setElementText(cardNumber, bankCard);
    }

    /**
     * Types the Expiration Date in the textbox on the "PaymentGateway" Page.
     *
     * @param expirationDate as a string
     */
    public void typeExpirationDate(String expirationDate) {
        waitUntilElementIsDisplayed(expirationField, TIMEOUT);
        setElementText(expirationField, expirationDate);
    }

    /**
     * Types the CVV in the text box on the "PaymentGateway" Page.
     *
     * @param cvv as a string
     */
    public void typeCVV(String cvv) {
        waitUntilElementIsDisplayed(cvvField, TIMEOUT);
        setElementText(cvvField, cvv);
    }

    /**
    * Checks if "PAGAR" button is enabled
    */
    public void checkEnabledButton() {
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);

        // Waits for the button to be present in the DOM
        wait.until(ExpectedConditions.presenceOfElementLocated(btnPayment));

        // Gets the real button
        WebElement paymentButton = webDriver.findElement(btnPayment);

        // Verify if the button is enabled
        if (!paymentButton.isEnabled()) {
            Assert.fail("❌ Payment details are missing, therefore the 'PAGAR' button is disabled. And test is FAILED");
        }
    }

    /**
     * Clicks on "PAGAR" button.
     */
    public void  clickButtonPagar() {
        // Make a click
        WebElement paymentButton = webDriver.findElement(btnPayment);
        paymentButton.click();
    }

    /**
     * Checks if the pop-up of the error appears
     */
    public void  checkPoupUpError() {

        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);

        // Verify visibility
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(popUpError));
            System.out.println("✅ The Pop-up with the error of no valid card (RS18) is visible on the screen");
        } catch (Exception e) {
            Assert.fail("❌ The Pop-up exists but is NOT visble on the screen");
        }
    }

}