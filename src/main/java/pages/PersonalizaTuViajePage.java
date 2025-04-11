package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
     * Assert that I am on the right page and is enabled “Personaliza tu viaje” page
     */
    public void verifyYouAreInPersonalizedYourTravelPage() {
        //@todo verificar igual que en las anteriores
        //@todo Saúl= otro todo sin hacer. Saúl : hago un getText.
        waitUntilElementIsDisplayed(personalizaTuViajeLabel, timeout);
        Assert.assertEquals("Personaliza tu viaje", webDriver.findElement(personalizaTuViajeLabel).getText());
    }

    /**
     * Verify the ticket price.
     * @param totalPriceTrip as a String
     */
    public String verifyTotalPersonalizePrice(String totalPriceTrip){
        waitUntilElementIsDisplayed(totalPricePersonalizeLocator, timeout);
        String totalPricePersonalize = webDriver.findElement(totalPricePersonalizeLocator).getText().trim().replaceAll("\\s+", "");
        String totalPrice = webDriver.findElement(totalPricePersonalizeLocator).getText().trim().replaceAll("\\s+", "");
        Assert.assertEquals(totalPricePersonalize, totalPrice);
        return totalPriceTrip;
    }

    /**
     * Clic on continue with the purchase
     */
    public void continueWithPurchase(){
        waitUntilElementIsDisplayed(btnContinuarPersonalize, timeout);
        scrollElementIntoView(btnContinuarPersonalize);
        clickElement(btnContinuarPersonalize);
    }

}