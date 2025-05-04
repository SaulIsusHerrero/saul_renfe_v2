package pages;

import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CompraPage extends BasePage {
    //Locators
    private By compraLabel = By.xpath("//span[contains(text(), 'Compra') and not(ancestor::select[@disabled])]");
    private By emailField = By.xpath("//input[@id='inputEmail']");
    private By telefonoField = By.xpath("//input[@id='telefonoComprador']");
    private By cardInput = By.xpath("//input[@id='datosPago_cdgoFormaPago_tarjetaRedSys']");
    private By newCard = By.cssSelector("button.target-puntos-renfe.selecTarjeta");
    private By totalPriceCompraLocator = By.cssSelector("span#totalTrayecto.dinero-total");
    private By conditionsCheckboxInput = By.xpath("//input[@id='aceptarCondiciones']");
    private By btnContinuarCompra = By.cssSelector("button#butonPagar.pagar-a");

    //Constructor
    public CompraPage(WebDriver webDriver) {
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    //Methods
    /**
     * Assert that I am on the right page and is enabled “Compra” page
     */
    public void verifyYouAreInCompraPage() {
        waitUntilElementIsDisplayed(compraLabel, Duration.ofSeconds(15));
        WebElement element = webDriver.findElement(compraLabel);
        Assert.assertEquals("Compra", element.getText());
        Assert.assertTrue(webDriver.findElement(compraLabel).isEnabled());
    }

    /**
     * type the E-mail in the textbox on the "Compra" page.
     * @param email as a string
     */
    public void typeEmail(String email){
        waitUntilElementIsDisplayed(emailField, Duration.ofSeconds(15));
        setElementText(emailField, email);
    }

    /**
     * type the Phone in the textbox on the "Introduce tus datos" page.
     * @param phone as a string
     */
    public void writePhoneField(String phone) {
        waitUntilElementIsDisplayed(telefonoField, Duration.ofSeconds(15));
        setElementText(telefonoField, phone);
    }

    /**
     * Marks the "Bank card" radio button on the "Compra" page
     *
     */
    public void clickPurchaseCard() {
        WebElement card = webDriver.findElement(cardInput);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", card);
    }

    /**
     * Clicks on new card
     */
     public void clickNewCard(){
         scrollElementIntoView(newCard);
         waitUntilElementIsDisplayed(newCard, Duration.ofSeconds(15));
         clickElement(newCard);
     }

    /**
     * Marks the "Conditions of purchase" checkbox as selected or unselected in the "Compra" page
     *
     */
    public void clickPurchaseCondition(){
       scrollElementIntoView(conditionsCheckboxInput);
       WebElement conditions = webDriver.findElement(conditionsCheckboxInput);
       JavascriptExecutor js = (JavascriptExecutor) webDriver;
       js.executeScript("arguments[0].click();", conditions);
    }

    /**
     * Verify the ticket price.
     * @param totalPriceTrip Precio obtenido previamente, ya normalizado
     */
    public void verifyTotalCompraPrice(String totalPriceTrip) {
        waitUntilElementIsDisplayed(totalPriceCompraLocator, Duration.ofSeconds(15));

        // Normaliza el precio de la nueva página
        String totalPriceCompra = normalizePrice(webDriver.findElement(totalPriceCompraLocator).getText());

        // El precio recibido ya debería estar normalizado, pero por seguridad:
        totalPriceTrip = normalizePrice(totalPriceTrip);

        Assert.assertEquals(totalPriceCompra, totalPriceTrip);
    }

    /**
     * clicks in the button continue with the Purchase on the "Compra" page
     */
    public void clickContinuarCompra(){
        waitUntilElementIsDisplayed(btnContinuarCompra, Duration.ofSeconds(15));
        clickElement(btnContinuarCompra);
    }

    /**
     *
     * @param popup boolean
     * @return false
     */
    public boolean isErrorMessageDisplayed(boolean popup) {
        try {
            By errorLocator = By.xpath(String.format("//div[contains(text(), '%s')]"));
            new WebDriverWait(webDriver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(errorLocator));
        } catch (Exception e) {
            System.out.println("Error no se han insertado los datos obligatorios para la compra");
            return false;
        }

        return popup;
    }

}