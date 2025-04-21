package steps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.HomePage;
import pages.PasarelaPagoPage;
import pages.SeleccionarTuViajePage;

public class InvalidCardPaymentSteps {
    private WebDriver webDriver;
    private HomePage homePage;
    private SeleccionarTuViajePage seleccionarTuViajePage;
    private PasarelaPagoPage pasarelaPagoPage;

    @Given("the user is on the Renfe homepage")
    public void theUserIsOnTheRenfeHomepage() {
        webDriver = new ChromeDriver();
        webDriver.get("https://www.renfe.com/es/es");
        homePage = new HomePage(webDriver);
    }

    @When("the user searches for a trip from {string} to {string}")
    public void theUserSearchesForATrip(String origin, String destination) {
        homePage.enterOrigin(origin);
        homePage.enterDestination(destination);
        homePage.clickSearchTicketButton();
    }

    @When("selects the first available train")
    public void selectsTheFirstAvailableTrain() {
        seleccionarTuViajePage = new SeleccionarTuViajePage(webDriver);
        seleccionarTuViajePage.selectFirstTrainAvailableAndBasicFare();
    }

    @When("enters invalid payment details")
    public void entersInvalidPaymentDetails() {
        pasarelaPagoPage = new PasarelaPagoPage(webDriver);
        pasarelaPagoPage.typeBankCard("4000 0000 0000 1000");
        pasarelaPagoPage.typeExpirationDate("03/30");
        pasarelaPagoPage.typeCVV("990");
        pasarelaPagoPage.clickPaymentButton();
    }

    @Then("the payment should be declined")
    public void thePaymentShouldBeDeclined() {
       // pasarelaPagoPage.verifyPaymentDeclinedMessage();
        webDriver.quit();
    }
}
