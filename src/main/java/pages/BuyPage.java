package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BuyPage extends BasePage {
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
    public BuyPage(WebDriver webDriver) {
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    //Methods
    /**
     * Assert that I am on the right page and is enabled “Buy” page
     */
    public void verifyYouAreInCompraPage() {
        waitUntilElementIsDisplayed(compraStepper, TIMEOUT);
        Assert.assertTrue(webDriver.findElement(compraStepper).isEnabled(),"The purchase screen is not enabled");
    }

    /**
     * type the E-mail in the textbox on the "Buy" page.
     * @param email as a string
     */
    public void typeEmail(String email){
        waitUntilElementIsDisplayed(emailField, TIMEOUT);
        setElementText(emailField, email);
    }

    /**
     * type the Phone in the textbox on the "Introduce your data" page.
     * @param phone as a string
     */
    public void writePhoneField(String phone) {
        waitUntilElementIsDisplayed(telefonoField, TIMEOUT);
        setElementText(telefonoField, phone);
    }

    /**
     * Marks the "Bank card" radio button on the "Buy" page
     *
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
     * Marks the "Conditions of purchase" checkbox as selected or unselected in the 'Buy' page
     *
     */
    public void clickPurchaseCondition(){
       scrollElementIntoView(conditionsCheckboxInput);
       clickElement(conditionsCheckboxInput);
    }

    /**
     * Verifies the ticket price.
     * @param totalPriceTrip Price previously obtained, already normalized
     */
    public void verifyTotalPrice(String totalPriceTrip) {
        waitUntilElementIsDisplayed(totalPriceCompraLocator, TIMEOUT);

        // Normalize the price from the new page
        String totalPriceCompra = normalizePrice(webDriver.findElement(totalPriceCompraLocator).getText());

        // The received price should already be normalized, but for safety:
        totalPriceTrip = normalizePrice(totalPriceTrip);

        Assert.assertEquals(totalPriceCompra, totalPriceTrip);
    }

    /**
     * Clicks in the button continue with the purchase on the "Buy" page
     */
    public void clickContinuarCompra(){
        waitUntilElementIsDisplayed(btnContinuarCompra, TIMEOUT);
        clickElement(btnContinuarCompra);
    }

}
