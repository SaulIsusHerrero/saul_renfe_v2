package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;


public class CompraPage extends BasePage {
    //Locators
    private By compraLabel = By.xpath("//span[contains(text(), 'Compra') and not(ancestor::select[@disabled])]");
    private By emailField = By.xpath("//input[@id='inputEmail']");
    private By telefonoField = By.xpath("//input[@id='telefonoComprador']");
    private By cardInput = By.xpath("//input[@id='datosPago_cdgoFormaPago_tarjetaRedSys']");
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
        waitUntilElementIsDisplayed(compraLabel, Duration.ofSeconds(5));
        //todo verificar igual que en las anteriores
        //@todo Saul - otro todo sin hacer
        Assert.assertTrue(webDriver.findElement(compraLabel).isEnabled());
    }

    /**
     * type the E-mail in the textbox on the "Compra" page.
     * @param email as a string
     */
    public void typeEmail(String email){
        waitUntilElementIsDisplayed(emailField, Duration.ofSeconds(5));
        setElementText(emailField, email);
    }

    /**
     * type the Phone in the textbox on the "Introduce tus datos" page.
     * @param phone as a string
     */
    public void writePhoneField(String phone) {
        waitUntilElementIsDisplayed(telefonoField, Duration.ofSeconds(5));
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
     * Marks the "Conditions of ourchase" checkbox as selected or unselected in the "Compra" page
     *
     */
    public void clickPurchaseCondition(){
       scrollElementIntoView(conditionsCheckboxInput);
       WebElement conditions = webDriver.findElement(conditionsCheckboxInput);
       JavascriptExecutor js = (JavascriptExecutor) webDriver;
       js.executeScript("arguments[0].click();", conditions);
       //@todo Saul - usas el click con javascript en multiples ocasiones, ¿que te parece si creas un metodo en utils?
    }

    /**
     * Verify the ticket price.
     * @param totalPriceTrip as a String
     */
    public String verifyTotalCompraPrice(String totalPriceTrip){
        waitUntilElementIsDisplayed(totalPriceCompraLocator, Duration.ofSeconds(5));
        //@todo comprobar el precio, no la disponibilidad. los precios se comprueban con el getText
        String totalPriceData = webDriver.findElement(totalPriceCompraLocator).getText().trim().replaceAll("\\s+", "");
        //@todo verificar que el precio es el mismo que en la pagina anterior. Sigue siendo el mismo desde el inicio
        totalPriceTrip = webDriver.findElement(totalPriceCompraLocator).getText().trim().replaceAll("\\s+", "");
        Assert.assertEquals(totalPriceData, totalPriceTrip);
        return totalPriceTrip;
    }

    /**
    * clicks in the button continue with the Purchase on the "Compra" page
    */
    public void clickContinuarCompra(){
        waitUntilElementIsDisplayed(btnContinuarCompra, Duration.ofSeconds(5));
        clickElement(btnContinuarCompra);
    }

}
