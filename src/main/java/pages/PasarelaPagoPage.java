package pages;

import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PasarelaPagoPage extends BasePage {
    //Locators
    private By cardField = By.xpath("//input[@id='card-number']");
    private By expirationField = By.xpath("//input[@id='card-expiration']");
    private By totalPricePasarelaLocator = By.xpath("//div[@class='right']");
    private By cvvField = By.xpath("//input[@id='card-cvv']");
    private By btnPayment = By.xpath("//button[@class='btn btn-lg btn-accept validColor']");
    private By popUpError = By.xpath("//div[@id='myModalBody']//li[contains(text(), 'Tarjeta no soportada (RS18)')]");
    public final By disabledPayButton = By.xpath("private final By disabledAcceptButton = By.xpath('//button[@class='btn btn-lg btn-accept' and @disabled]');");

    //Constructor
    public PasarelaPagoPage(WebDriver webDriver) {
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    //Methods
    /**
     * Assert I am in the "pasarela de pago" Page
     */
    public void verifyYouAreInPasarelaPagoPage() {
        String currentURL = webDriver.getCurrentUrl();
        String expectedURL = "https://sis.redsys.es/sis/realizarPago";
        Assert.assertEquals("Error: La url que esta cargada en la web es: " + currentURL + ", sin embargo se esperaba:" + expectedURL, currentURL,expectedURL);
    }

    /**
     * Verify the ticket price.
     * @param totalPriceTrip Precio obtenido previamente, ya normalizado
     */
    public void verifyTotalPricePasarelaPago(String totalPriceTrip) {
        waitUntilElementIsDisplayed(totalPricePasarelaLocator, Duration.ofSeconds(15));

        // Normaliza el precio de la nueva página
        String totalPricePasarela = normalizePrice(webDriver.findElement(totalPricePasarelaLocator).getText());

        // El precio recibido ya debería estar normalizado, pero por seguridad:
        totalPriceTrip = normalizePrice(totalPriceTrip);

        Assert.assertEquals(totalPricePasarela, totalPriceTrip);
    }

    /**
     * Type the card in the textbox on the "Pasarela de pago" page.
     * @param card as a string
     */
    public void typeBankCard(String card) {
        waitUntilElementIsDisplayed(cardField, Duration.ofSeconds(5));
        setElementText(cardField, card);
    }

    /**
     * Type the Expiration Date in the textbox on the "Pasarela de pago" page.
     * @param expirationDate as a string
     */
    public void typeExpirationDate(String expirationDate){
        waitUntilElementIsDisplayed(expirationField, Duration.ofSeconds(5));
        setElementText(expirationField, expirationDate);
    }

    /**
     * Type the CVV in the text box on the "Pasarela de pago" page
     * @param cvv as a string
     */
    public void typeCVV(String cvv){
        waitUntilElementIsDisplayed(cvvField, Duration.ofSeconds(5));
        setElementText(cvvField, cvv);
    }

    /**
     * Click on the payment button
     */
    public void clickPaymentButton() {
        waitUntilElementIsDisplayed(btnPayment, Duration.ofSeconds(30));
        clickElement(btnPayment);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(30));

        // Assert 1: Verify element exists in DOM (presence)
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(popUpError));
            System.out.println("✅ El Pop-up con el error de tarjeta invalida (RS18) existe en el DOM");
        } catch (Exception e) {
            Assert.fail("❌ El Pop-up NO existe en el DOM");
        }

        // Assert 2: Verify the element is actually visible on screen
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(popUpError));
            System.out.println("✅ El Pop-up con el error de tarjeta invalida (RS18) es visible en pantalla");
        } catch (Exception e) {
            Assert.fail("❌ El Pop-up con el error de tarjeta invalida (RS18) pero NO es visible en pantalla");
        }
        Assert.assertTrue(webDriver.findElement(disabledPayButton).isEnabled());
    }

}