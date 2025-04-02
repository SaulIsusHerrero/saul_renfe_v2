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
    private By newBankCard = By.cssSelector("button.target-puntos-renfe.selecTarjeta");
    private By totalPriceCompralizeLocator = By.cssSelector("span#totalTrayecto.dinero-total");
    private By conditionsCheckboxInput = By.xpath("//input[@id='aceptarCondiciones']");
    private By btnContinuarCompra = By.cssSelector("button#butonPagar.pagar-a");

    //Constructor
    public CompraPage(WebDriver webDriver) {
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    //Methods
    /**
     * Assert que estoy en la Page y esta habilitada “Personaliza tu viaje”
     */
    public void verifyYouAreInCompraPage() {
        waitUntilElementIsDisplayed(compraLabel, Duration.ofSeconds(5));
        WebElement element = webDriver.findElement(compraLabel);
        boolean labelDisplayed = element.isDisplayed();
        boolean labelEnabled = element.isEnabled();
        Assert.assertTrue("Compra", labelDisplayed); //todo verificar igual que en las anteriores
        Assert.assertTrue("Compra", labelEnabled);
    }

    /**
     * type the E-mail in the textbox on the "Compra" page.
     *
     * @param email as a string
     */
    public void typeEmail(String email){
        waitUntilElementIsDisplayed(emailField, Duration.ofSeconds(5));
        setElementText(emailField, email);
    }

    /**
     * type the Phone in the textbox on the "Introduce tus datos" page.
     *
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
     * Clicks in new card in the Compra page.
     */
    public void clickNewBankCard() {
        WebElement newCard = webDriver.findElement(newBankCard);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", newCard);
    }

    /**
     * Marks the "Conditions of ourchase" checkbox as selected or unselected in the "Compra" page
     *
     */
    public void clickPurchaseCondition(){
       WebElement conditions = webDriver.findElement(conditionsCheckboxInput);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", conditions);
    }
    /**
     * Verify the ticket price on the "Compra" page
     */
    public void verifyTotalPurchasePrice(){
        waitUntilElementIsDisplayed(totalPriceCompralizeLocator, Duration.ofSeconds(5));
        boolean totalPurchasePrice = isElementDisplayed(totalPriceCompralizeLocator); //todo verificar el precio
        Assert.assertTrue(totalPurchasePrice);
    }

    /**
    * clicks in the button continue with the Purchase on the "Compra" page
    */
    public void clickContinuarCompra(){
        waitUntilElementIsDisplayed(btnContinuarCompra, Duration.ofSeconds(5));
        clickElement(btnContinuarCompra);
    }

}