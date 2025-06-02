package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CompraPage extends BasePage {
    //Locators
    private By compraStepper = By.xpath("//li[contains(@class, 'active')]//span[contains(text(), 'Compra')]");
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
        waitUntilElementIsDisplayed(compraStepper, TIMEOUT);
        Assert.assertTrue(webDriver.findElement(compraStepper).isEnabled(),"No está habilitado este step");
    }

    /**
     * type the E-mail in the textbox on the "Compra" page.
     * @param email as a string
     */
    public void typeEmail(String email){
        waitUntilElementIsDisplayed(emailField, TIMEOUT);
        setElementText(emailField, email);
    }

    /**
     * type the Phone in the textbox on the "Introduce tus datos" page.
     * @param phone as a string
     */
    public void writePhoneField(String phone) {
        waitUntilElementIsDisplayed(telefonoField, TIMEOUT);
        setElementText(telefonoField, phone);
    }

    /**
     * Marks the "Bank card" radio button on the "Compra" page
     *
     * @return
     */
    public void clickPurchaseCard() {
        scrollElementIntoView(cardInput);
        waitUntilElementIsDisplayed(cardInput, TIMEOUT);
        clickElement(cardInput);
    }

    /**
     * Clicks on new card
     */
     public void clickNewCard(){
         scrollElementIntoView(newCard);
         WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);
         WebElement cardToClick = webDriver.findElement(newCard);
         wait.until(ExpectedConditions.visibilityOf(cardToClick));
         wait.until(ExpectedConditions.elementToBeClickable(cardToClick));
         cardToClick.click();
     }

    /**
     * Marks the "Conditions of purchase" checkbox as selected or unselected in the "Compra" page
     *
     */
    public void clickPurchaseCondition(){
       scrollElementIntoView(conditionsCheckboxInput);
       clickElement(conditionsCheckboxInput);
    }

    /**
     * Verify the ticket price.
     * @param totalPriceTrip Precio obtenido previamente, ya normalizado
     */
    public void verifyTotalPrice(String totalPriceTrip) {
        waitUntilElementIsDisplayed(totalPriceCompraLocator, TIMEOUT);

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
        waitUntilElementIsDisplayed(btnContinuarCompra, TIMEOUT);
        clickElement(btnContinuarCompra);
    }

}