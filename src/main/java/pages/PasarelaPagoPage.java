package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.UtilsMethods;

public class PasarelaPagoPage extends BasePage {
    // Locators
    private By cardField = By.xpath("//input[@id='card-number']");
    private By expirationField = By.xpath("//input[@id='card-expiration']");
    private By totalPricePasarelaLocator = By.xpath("//div[@class='right']");
    private By cvvField = By.xpath("//input[@id='card-cvv']");
    private By btnPayment = By.xpath("//button[@class='btn btn-lg btn-accept validColor']");
    private By popUpError = By.xpath("//div[@id='myModalBody']//li[contains(text(), 'Tarjeta no soportada (RS18)')]");

    // Instances
    private UtilsMethods utilsMethods;

    // Constructor
    public PasarelaPagoPage(WebDriver webDriver) {
        super(webDriver); // Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; // Current instance
        this.utilsMethods = new UtilsMethods(webDriver); // Initialize UtilsMethods instance
    }

    // Methods
    /**
     * Assert I am in the "pasarela de pago" Page
     */
    public void verifyYouAreInPasarelaPagoPage() {
        String currentURL = webDriver.getCurrentUrl();
        String expectedURL = "https://sis.redsys.es/sis/realizarPago";
        Assert.assertEquals("Error: La url que esta cargada en la web es: " + currentURL + ", sin embargo se esperaba:" + expectedURL, currentURL, expectedURL);
    }

    /**
     * Verify the ticket price.
     * @param totalPriceTrip as a String
     */
    public String verifyTotalPricePasarelaPago(String totalPriceTrip) {
        utilsMethods.isElementVisibleInDOM(totalPricePasarelaLocator, 10);
        String totalPricePersonalize = webDriver.findElement(totalPricePasarelaLocator).getText().trim().replaceAll("\\s+", "");
        String totalPrice = webDriver.findElement(totalPricePasarelaLocator).getText().trim().replaceAll("\\s+", "");
        Assert.assertEquals(totalPricePersonalize, totalPrice);
        return totalPriceTrip;
    }

    /**
     * Type the card in the textbox on the "Pasarela de pago" page.
     * @param card as a string
     */
    public void typeBankCard(String card) {
        utilsMethods.isElementVisibleInDOM(cardField, 10);
        setElementText(cardField, card);
    }

    /**
     * Type the Expiration Date in the textbox on the "Pasarela de pago" page.
     * @param expiration as a string
     */
    public void typeExpirationDate(String expiration) {
        utilsMethods.isElementVisibleInDOM(expirationField, 10);
        setElementText(expirationField, expiration);
    }

    /**
     * Type the CVV in the text box on the "Pasarela de pago" page
     * @param cvv as a string
     */
    public void typeCVV(String cvv) {
        utilsMethods.isElementVisibleInDOM(cvvField, 10);
        setElementText(cvvField, cvv);
    }

    /**
     * Click on payment button
     */
    public void clickPaymentButton() {
        utilsMethods.isElementVisibleInDOM(btnPayment, 10);
        clickElement(btnPayment);
        utilsMethods.isElementVisibleInDOM(popUpError, 10);
        boolean popUpErrorExpected = utilsMethods.isElementVisibleInDOM(popUpError, 10);
        Assert.assertTrue(popUpErrorExpected);
    }
}