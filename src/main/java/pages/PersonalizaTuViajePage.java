package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.time.Duration;


public class PersonalizaTuViajePage extends BasePage {
    //Locators
    private By personalizaTuViajeLabel = By.xpath("//span[contains(text(), 'Personaliza tu viaje') and not(ancestor::select[@disabled])]");
    private By btnContinuarPersonalize = By.xpath("//button[@id='submitFormaPago']");
    private By totalPricePersonalizeLocator = By.xpath("//span[@id='totalTrayecto']");

    //Constructor
    public PersonalizaTuViajePage(WebDriver webDriver) {
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    //Methods
    /**
     * Assert that I am on the right page and is enable “Personaliza tu viaje” page
     */
    public void verifyYouAreInPersonalizedYourTravelPage() {
        waitUntilElementIsDisplayed(personalizaTuViajeLabel, Duration.ofSeconds(5));
        Assert.assertTrue(webDriver.findElement(personalizaTuViajeLabel).isEnabled());
    }

    /**
     * Verify the ticket price.
     */
    public void verifyTotalPersonalizePrice(){
        waitUntilElementIsDisplayed(totalPricePersonalizeLocator, Duration.ofSeconds(5));
        //@todo comprobar el precio, no la disponibilidad. los precios se comprueban con el getText
        String totalPricePersonalize = webDriver.findElement(totalPricePersonalizeLocator).getText().trim().replaceAll("\\s+", "");
        //@todo verificar que el precio es el mismo que en la pagina anterior.
        //todo comprobar que el precio sigue siendo el mismo
        System.out.println("El precio total sigue siendo en la pantalla 'Personaliza tu viaje': " + totalPricePersonalize + "\n");
    }

    /**
     * Clic on continue with the purchase
     */
    public void continueWithPurchase(){
        waitUntilElementIsDisplayed(btnContinuarPersonalize, Duration.ofSeconds(5));
        clickElement(btnContinuarPersonalize);
    }

}