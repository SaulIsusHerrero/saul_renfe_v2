package pages;

import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class PersonalizaTuViajePage extends BasePage {
    //Locators
    private By personalizaTuViajeStepper = By.xpath("//ul[@class='stepper stepper-horizontal']//li[contains(@class, 'active')]//span[contains(text(), 'Personaliza tu viaje')]");
    private By btnContinuarPersonalize = By.xpath("//button[@id='submitFormaPago']");
    private By totalPricePersonalizeLocator = By.xpath("//span[@id='totalTrayecto']");

    //Constructor
    public PersonalizaTuViajePage(WebDriver webDriver) {
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    //Methods
    /**
     * Assert that I am on the right page and is enabled “Personaliza tu viaje” page
     */
    public void verifyYouAreInPersonalizedYourTravelPage() {
        waitUntilElementIsDisplayed(personalizaTuViajeStepper, Duration.ofSeconds(10));
        Assert.assertTrue(webDriver.findElement(personalizaTuViajeStepper).isEnabled(), "No está hablitado este step");
    }

    /**
     * Clicks on continues with the purchase
     */
    public void continueWithPurchase(){
        waitUntilElementIsDisplayed(btnContinuarPersonalize, Duration.ofSeconds(15));
        scrollElementIntoView(btnContinuarPersonalize);
        clickElement(btnContinuarPersonalize);
    }

    /**
     * Verify the ticket price.
     * @param totalPriceTrip Precio obtenido previamente, ya normalizado
     */
    public void verifyTotalPersonalizePrice(String totalPriceTrip) {
        waitUntilElementIsDisplayed(totalPricePersonalizeLocator, Duration.ofSeconds(15));

        // Normaliza el precio de la nueva página
        String totalPricePersonalize = normalizePrice(webDriver.findElement(totalPricePersonalizeLocator).getText());

        // El precio recibido ya debería estar normalizado, pero por seguridad:
        totalPriceTrip = normalizePrice(totalPriceTrip);

        Assert.assertEquals(totalPricePersonalize, totalPriceTrip);
    }

}