package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.time.Duration;

public class PasarelaPagoPage extends BasePage {
    //Locators
    private By cardField = By.xpath("//input[@id='card-number']");
    private By expirationField = By.xpath("//input[@id='card-expiration']");
    private By totalPricePasarelaLocator = By.xpath("//div[@class='right']");
    private By cvvField = By.xpath("//input[@id='card-cvv']");
    private By btnPayment = By.xpath("//button[@class='btn btn-lg btn-accept validColor']");
    private By popUpError = By.xpath("//div[@id='myModalBody']//li[contains(text(), 'Tarjeta no soportada (RS18)')]");

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
        Assert.assertEquals("Error: La url que esta cargada en la web es: " + currentURL + ", sin embargo se esperaba:" + expectedURL, currentURL,expectedURL); //ejemplo
    }

    /**
     * Assert that the total price on the "Pasarela de Pago" page is the same than the other pages
     */
    public void verifyTotalPricePasarelaPago(){
        waitUntilElementIsDisplayed(totalPricePasarelaLocator, Duration.ofSeconds(5));
        String totalPricePasarela = webDriver.findElement(totalPricePasarelaLocator).getText().trim().replaceAll("\\s+", "");
        //@todo verificar precio, no que este disponible
        System.out.println("El precio total sigue siendo en la pantalla 'Pasarela de pago': " + totalPricePasarela + "\n");
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
     * @param expiration as a string
     */
    public void typeExpirationDate(String expiration){
        waitUntilElementIsDisplayed(expirationField, Duration.ofSeconds(5));
        setElementText(expirationField, expiration);
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
     * Click on payment button
     */
    public void clickPaymentButton(){
        waitUntilElementIsDisplayed(btnPayment, Duration.ofSeconds(5));
        clickElement(btnPayment);
        waitUntilElementIsDisplayed(popUpError, Duration.ofSeconds(5));
        boolean popUpErrorExpected = isElementDisplayed(popUpError);
        Assert.assertTrue(popUpErrorExpected);
    }

}