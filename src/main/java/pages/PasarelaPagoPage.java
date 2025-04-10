package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PasarelaPagoPage extends BasePage {
    //Locators
    private By cardField = By.xpath("//input[@id='card-number']");
    private By expirationField = By.xpath("//input[@id='card-expiration']");
    private By totalPricePasarelaLocator = By.xpath("//div[@class='right']");
    private By cvvField = By.xpath("//input[@id='card-cvv']");
    private By btnPayment = By.xpath("//button[@class='btn btn-lg btn-accept validColor']");
    private By popUpError = By.xpath("//div[@id='myModalBody']//li[contains(text(), 'Tarjeta no soportada (RS18)')]");
    private By totalPriceLocator = By.xpath("(//span[@id='totalTrayectoBanner'])[1]");

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
     * @param totalPriceTrip as a String
     */
    public String verifyTotalPricePasarelaPago(String totalPriceTrip){
        waitUntilElementIsDisplayed(totalPricePasarelaLocator, timeout);
        String totalPrice = webDriver.findElement(totalPriceLocator).getText().trim().replaceAll("\\s+", "");
        return totalPriceTrip;
    }

    /**
     * Type the card in the textbox on the "Pasarela de pago" page.
     * @param card as a string
     */
    public void typeBankCard(String card) {
        waitUntilElementIsDisplayed(cardField, timeout);
        setElementText(cardField, card);
    }

    /**
     * Type the Expiration Date in the textbox on the "Pasarela de pago" page.
     * @param expiration as a string
     */
    public void typeExpirationDate(String expiration){
        waitUntilElementIsDisplayed(expirationField, timeout);
        setElementText(expirationField, expiration);
    }

    /**
     * Type the CVV in the text box on the "Pasarela de pago" page
     * @param cvv as a string
     */
    public void typeCVV(String cvv){
        waitUntilElementIsDisplayed(cvvField, timeout);
        setElementText(cvvField, cvv);
    }

    /**
     * Click on payment button
     */
    public void clickPaymentButton(){
        //@Todo Saul- ¿Que esta comprobando este assert? ¿Estas seguro de que este selector no esta presente en el dom siempre?
        waitUntilElementIsDisplayed(btnPayment, timeout);
        clickElement(btnPayment);
        waitUntilElementIsDisplayed(popUpError, timeout);
        boolean popUpErrorExpected = isElementDisplayed(popUpError);
        Assert.assertTrue(popUpErrorExpected);

    }

}