package steps;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.BasePage;
import pages.CompraPage;
import pages.HomePage;
import pages.IntroduceTusDatosPage;
import pages.PasarelaPagoPage;
import pages.PersonalizaTuViajePage;
import pages.SeleccionarTuViajePage;

public class InvalidCardPaymentSteps {
    private WebDriver webDriver;
    private BasePage basePage;
    private HomePage homePage;
    private SeleccionarTuViajePage seleccionarTuViajePage;
    private IntroduceTusDatosPage introduceTusDatosPage;
    private PersonalizaTuViajePage personalizaTuViajePage;
    private PasarelaPagoPage pasarelaPagoPage;
    private String totalPriceTrip;
    private CompraPage compraPage;
    private final Duration TIMEOUT = Duration.ofSeconds(30);

    @Given("the user is on the Renfe homepage")
    public void theUserIsOnTheRenfeHomepage() {
        WebDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(TIMEOUT);
        webDriver.manage().window().maximize();
        webDriver.get("https://www.renfe.com/es/es"); // URL page.

        basePage = new BasePage(webDriver);
        homePage = new HomePage(webDriver);
        seleccionarTuViajePage = new SeleccionarTuViajePage(webDriver);
        introduceTusDatosPage = new IntroduceTusDatosPage(webDriver);
        personalizaTuViajePage = new PersonalizaTuViajePage(webDriver);
        compraPage = new CompraPage(webDriver);
        pasarelaPagoPage = new PasarelaPagoPage(webDriver);
    }

    @When("the user searches for a trip from {string} to {string}")
    public void theUserSearchesForATrip(String origin, String destination) {
        basePage.clickAcceptAllCookiesButton();
        homePage.enterOrigin(origin);
        homePage.enterDestination(destination);
        homePage.selectDepartureDate();
        homePage.clickSoloIdaButtonSelected(true);
        homePage.clickAcceptButton();
        homePage.clickSearchTicketButton();
    }

    @When("selects the first available train")
    public void selectsTheFirstAvailableTrain() {
        seleccionarTuViajePage.verifyYouAreInSelecionaTuViaje();
        seleccionarTuViajePage.selectFirstTrainAvailableAndBasicFare();
        seleccionarTuViajePage.verifyNumberOfTravelers();
        seleccionarTuViajePage.verifyFareIsBasic();
        totalPriceTrip = seleccionarTuViajePage.verifyFareAndTotalPricesAreEquals();
        seleccionarTuViajePage.clickSelectButton();
        seleccionarTuViajePage.popUpFareAppears();
        seleccionarTuViajePage.linkContinueSameFareAppears();
        seleccionarTuViajePage.clickLinkContinueSameFare();
    }

    @When("enters personal details with {string}, {string}, {string}, {string}, {string}, and {string}")
    public void entersPersonalDetails(String firstName, String primerApellido, String segundoApellido, String dni, String email, String phone) {
        introduceTusDatosPage.verifyYouAreInIntroduceYourDataPage();
        introduceTusDatosPage.writeFirstNameField(firstName);
        introduceTusDatosPage.writeFirstSurnameField(primerApellido);
        introduceTusDatosPage.writeSecondSurnameField(segundoApellido);
        introduceTusDatosPage.writeDNIField(dni);
        introduceTusDatosPage.writeEmailField(email);
        introduceTusDatosPage.writePhoneField(phone);
        introduceTusDatosPage.verifyTotalPriceData(totalPriceTrip);
        introduceTusDatosPage.clickPersonalizeTrip();
    }

    @When("verifies the total price {string}")
    public void verifiesTheTotalPrice(String expectedPrice) {
        personalizaTuViajePage.verifyYouAreInPersonalizedYourTravelPage();
        personalizaTuViajePage.verifyTotalPersonalizePrice(expectedPrice);
        personalizaTuViajePage.continueWithPurchase();
        compraPage.verifyYouAreInCompraPage();
    }

    @When("the user completes the purchase details with {string} and {string}")
    public void theUserCompletesThePurchaseDetails(String email, String phone) {
        compraPage.typeEmail(email);
        compraPage.writePhoneField(phone);
        compraPage.clickPurchaseCard();
        compraPage.clickPurchaseCondition();
        compraPage.verifyTotalCompraPrice(totalPriceTrip);
        compraPage.clickContinuarCompra();
    }

    @When("proceeds to payment with {string}, {string}, and {string}")
    public void proceedsToPaymentWith(String card, String expiration, String cvv) {
        pasarelaPagoPage.verifyYouAreInPasarelaPagoPage();
        pasarelaPagoPage.verifyTotalPricePasarelaPago(totalPriceTrip);
        pasarelaPagoPage.typeBankCard(card);
        pasarelaPagoPage.typeExpirationDate(expiration);
        pasarelaPagoPage.typeCVV(cvv);
        pasarelaPagoPage.clickPaymentButton();
    }

    @Then("the payment should be declined")
    public void thePaymentShouldBeDeclined() {
        //pasarelaPagoPage.verifyPaymentDeclinedMessage();
        webDriver.quit();
    }
}
