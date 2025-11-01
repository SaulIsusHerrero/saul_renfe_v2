package pages;

import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PersonalizeYourTripPage extends BasePage {
    //Locators
    private By personalizaTuViajeStepper = By.xpath("//li[contains(@class, 'active')]//span[contains(text(), 'Personaliza tu viaje')]");
    private By btnContinuarPersonalize = By.xpath("//button[@id='submitFormaPago']");
    private By totalPricePersonalizeLocator = By.xpath("//span[@id='totalTrayecto']");

    //Constructor
    public PersonalizeYourTripPage(WebDriver webDriver) {
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    //Methods
    /**
     * Assert that I am on the right page and is enabled “Personalize your trip” Page
     */
    public void verifyYouAreInPersonalizeYourTravelPage() {
        waitUntilElementIsDisplayed(personalizaTuViajeStepper, TIMEOUT);
        Assert.assertTrue(webDriver.findElement(personalizaTuViajeStepper).isEnabled(), "No está hablitado este step");
    }

    /**
     * Verify the ticket price.
     * @param totalPriceTrip Price previously obtained, already normalized
     */
    public void verifyTotalPrice(String totalPriceTrip) {
        waitUntilElementIsDisplayed(totalPricePersonalizeLocator, TIMEOUT);

        // Normalize the price from the new page
        String totalPricePersonalize = normalizePrice(webDriver.findElement(totalPricePersonalizeLocator).getText());

        // The received price should already be normalized, but for safety:
        totalPriceTrip = normalizePrice(totalPriceTrip);
        Assert.assertEquals(totalPricePersonalize, totalPriceTrip);
    }

    /**
     * Clicks on continue with the purchase
     */
    public void continueWithPurchase(){
        waitUntilElementIsDisplayed(btnContinuarPersonalize, TIMEOUT);
        scrollElementIntoView(btnContinuarPersonalize);
        clickElement(btnContinuarPersonalize);
    }

}