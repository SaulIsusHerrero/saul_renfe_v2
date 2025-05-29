package pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PasarelaPagoPage extends BasePage {
    //Locators
    private By cardNumber = By.xpath("//input[@id='card-number']");
    private By expirationField = By.xpath("//input[@id='card-expiration']");
    private By totalPricePasarelaLocator = By.xpath("//div[@class='right']");
    private By cvvField = By.xpath("//input[@id='card-cvv']");
    private By btnPayment = By.xpath("//button[@onclick='javascript:pago()']");
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
        String actualUrl = webDriver.getCurrentUrl().trim();
        String expectedUrlPattern = "sis.redsys.es/sis/realizarPago";
        try {
            new WebDriverWait(webDriver, TIMEOUT).until(ExpectedConditions.urlContains(expectedUrlPattern));
        } catch (TimeoutException e) {
            Assert.fail("La URL no contiene '" + expectedUrlPattern + "' después de 15 segundos. URL actual: " + actualUrl);
        }
    }

    /**
     * Verify the ticket price.
     *
     * @param totalPriceTrip Precio obtenido previamente, ya normalizado
     */
    public void verifyTotalPrice(String totalPriceTrip) {
        waitUntilElementIsDisplayed(totalPricePasarelaLocator, TIMEOUT);

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
        waitUntilElementIsDisplayed(cardNumber, TIMEOUT);
        setElementText(cardNumber, card);
    }

    /**
     * Type the Expiration Date in the textbox on the "Pasarela de pago" page.
     *
     * @param expirationDate as a string
     */
    public void typeExpirationDate(String expirationDate) {
        waitUntilElementIsDisplayed(expirationField, TIMEOUT);
        setElementText(expirationField, expirationDate);
    }

    /**
     * Type the CVV in the text box on the "Pasarela de pago" page
     *
     * @param cvv as a string
     */
    public void typeCVV(String cvv) {
        waitUntilElementIsDisplayed(cvvField, TIMEOUT);
        setElementText(cvvField, cvv);
    }

    /**
     * Click on the payment button with state verification:
     * - If button is enabled or disabled
     */
    public void clickPaymentButton() {
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);

        // Esperar a que el botón esté presente en el DOM
        wait.until(ExpectedConditions.presenceOfElementLocated(btnPayment));

        // Obtener el botón real
        WebElement paymentButton = webDriver.findElement(btnPayment);

        // Verificar si el botón está habilitado
        if (!paymentButton.isEnabled()) {
            Assert.fail("❌ El botón de pago está deshabilitado, no se puede continuar.");
        }

        // Hacer clic
        paymentButton.click();

        // Verificar presencia del pop-up de error de tarjeta inválida
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(popUpError));
            System.out.println("✅ El Pop-up con el error de tarjeta inválida (RS18) existe en el DOM");
        } catch (Exception e) {
            Assert.fail("❌ El Pop-up NO existe en el DOM después de hacer clic en pagar.");
        }

        // Verificar visibilidad
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(popUpError));
            System.out.println("✅ El Pop-up con el error de tarjeta inválida (RS18) es visible en pantalla");
        } catch (Exception e) {
            Assert.fail("❌ El Pop-up existe pero NO es visible en pantalla");
        }
    }

}


