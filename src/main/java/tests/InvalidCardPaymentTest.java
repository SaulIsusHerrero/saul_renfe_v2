package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;

import java.time.Duration;

public class InvalidCardPaymentTest {
    //Instances
    private WebDriver webDriver;
    private BasePage basePage;
    private HomePage homePage;
    private SeleccionarTuViajePage seleccionarTuViajePage;
    private IntroduceTusDatosPage introduceTusDatosPage;
    private PersonalizaTuViajePage personalizaTuViajePage;
    private CompraPage compraPage;
    private PasarelaPagoPage pasarelaPagoPage;

    @BeforeMethod
    public void setup() throws InterruptedException {
        //Chrome: Initialization of the ChromeDriver.
        WebDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        webDriver.manage().window().maximize();
        webDriver.get("https://www.renfe.com/es/es"); //URL page.
        basePage = new BasePage(webDriver); //Initialization of the Base Page.
        homePage = new HomePage(webDriver); //Initialization of the Home Page.
        seleccionarTuViajePage = new SeleccionarTuViajePage(webDriver); //Initialization of the SeleccionarTuViaje Page.
        introduceTusDatosPage = new IntroduceTusDatosPage(webDriver); //Initialization of the IntroduceTusDatos Page.
        personalizaTuViajePage = new PersonalizaTuViajePage(webDriver); //Initialization of the PersonalizaTuViaje Page.
        compraPage = new CompraPage(webDriver); //Initialization of the Compra Page.
        pasarelaPagoPage = new PasarelaPagoPage(webDriver); //Initialization of the pasarelaPago Page.
    }

    @Test
    public void RenfeInvalidCardPaymentTest() {
        basePage.clickAcceptAllCookiesButton();
        homePage.enterOrigin("VALENCIA JOAQUÍN SOROLLA");
        homePage.enterDestination("BARCELONA-SANTS");
        homePage.selectDepartureDate();
        homePage.clickSoloIdaButtonSelected(true);
        homePage.clickAcceptButton();
        homePage.clickSearchTicketButton();
        seleccionarTuViajePage.verifyYouAreInSelecionaTuViaje();
        seleccionarTuViajePage.selectFirstTrainAvailableAndBasicFare();
        seleccionarTuViajePage.verifyNumberOfTravelers();
        seleccionarTuViajePage.verifyFareIsBasic();
        seleccionarTuViajePage.verifyFareAndTotalPricesAreEquals();
        seleccionarTuViajePage.clickSelectButton();
        seleccionarTuViajePage.popUpFareAppears();
        seleccionarTuViajePage.linkContinueSameFareAppears();
        seleccionarTuViajePage.clickLinkContinueSameFare();
        //introduceTusDatosPage.verifyYouAreInIntroduceYourDataPage();
        //introduceTusDatosPage.writeFirstNameField("John");
        //introduceTusDatosPage.writeFirstSurnameField("Doe");
        //introduceTusDatosPage.writeSecondSurnameField("López");
        //introduceTusDatosPage.writeDNIField("46131651E");
        //introduceTusDatosPage.writeEmailField("test@qa.com");
        //introduceTusDatosPage.writePhoneField("696824570");
        //introduceTusDatosPage.verifyTotalPriceData();
        //introduceTusDatosPage.clickPersonalizeTrip();
        //personalizaTuViajePage.verifyYouAreInPersonalizedYourTravelPage();
        //personalizaTuViajePage.continueWithPurchase();
        //personalizaTuViajePage.verifyTotalPersonalizePrice();
        //compraPage.verifyYouAreInCompraPage();
        //compraPage.typeEmail("test@qa.com");
        //compraPage.writePhoneField("696824570");
        //compraPage.clickPurchaseCard();
        //compraPage.clickNewBankCard();
        //compraPage.clickPurchaseCondition();
        //compraPage.verifyTotalPurchasePrice();
        //compraPage.clickContinuarCompra();
        //pasarelaPagoPage.verifyYouAreInPasarelaPagoPage();
        //pasarelaPagoPage.verifyTotalPricePasarelaPago();
        //pasarelaPagoPage.typeBankCard("4000 0000 0000 1000");
        //pasarelaPagoPage.typeExpirationDate("03/30");
        //pasarelaPagoPage.typeCVV("990");
        //pasarelaPagoPage.clickPaymentButton();
    }
    @AfterMethod
    public void tearDown() {
    if (webDriver != null) {
    webDriver.quit(); //Closes the current instance of the browser
    }
    }

}