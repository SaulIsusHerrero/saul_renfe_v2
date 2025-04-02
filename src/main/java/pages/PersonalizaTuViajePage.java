package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class PersonalizaTuViajePage extends BasePage {
    //Locators
    private By personalizaTuViajeLabel = By.xpath("//span[contains(text(), 'Personaliza tu viaje') and not(ancestor::select[@disabled])]");
    private By btnContinuarPersonalize = By.cssSelector("button#submitFormaPago.form-button.btn.btn-accordion");
    private By totalPricePersonalizeLocator = By.xpath("//span[@id='totalTrayecto']");

    //Variables
    WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));

    //Constructor
    public PersonalizaTuViajePage(WebDriver webDriver) {
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    //Methods

    /**
     * Assert que estoy en la Page y esta habilitada “Personaliza tu viaje”
     */
    public void verifyYouAreInPersonalizedYourTravelPage() {
        waitUntilElementIsDisplayed(personalizaTuViajeLabel, Duration.ofSeconds(5));
        WebElement element = webDriver.findElement(personalizaTuViajeLabel);
        boolean labelDisplayed = element.isDisplayed();
        boolean labelEnabled = element.isEnabled();
        Assert.assertTrue("Personaliza tu viaje", labelDisplayed); //@todo igual que en el titulo de la pag anterior
        Assert.assertTrue("Personaliza tu viaje", labelEnabled);
    }

    /**
     * Clic on continue with the purchase
     */
    public void continueWithPurchase(){
        waitUntilElementIsDisplayed(btnContinuarPersonalize, Duration.ofSeconds(5));
        clickElement(btnContinuarPersonalize);
    }

    /**
     * Verify the ticket price.
     */
    public void verifyTotalPersonalizePrice(){
        waitUntilElementIsDisplayed(totalPricePersonalizeLocator, Duration.ofSeconds(5));
        boolean totalPricePersonalize = isElementDisplayed(totalPricePersonalizeLocator); //todo comprobar que el precio sigue siendo el mismo
        Assert.assertTrue(totalPricePersonalize);
    }

}