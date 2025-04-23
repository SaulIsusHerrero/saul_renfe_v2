package tests;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import pages.BasePage;
import pages.CompraPage;
import pages.HomePage;
import pages.IntroduceTusDatosPage;
import pages.PasarelaPagoPage;
import pages.PersonalizaTuViajePage;
import pages.SeleccionarTuViajePage;
import utils.CSVDataProvider;
import utils.TemporaryDataStore;
import utils.DriverManager;

public class InvalidCardPaymentTest {
    // Instances
    private WebDriver webDriver;
    private BasePage basePage;
    private HomePage homePage;
    private SeleccionarTuViajePage seleccionarTuViajePage;
    private IntroduceTusDatosPage introduceTusDatosPage;
    private PersonalizaTuViajePage personalizaTuViajePage;
    private CompraPage compraPage;
    private PasarelaPagoPage pasarelaPagoPage;

    // Variables and Constants
    private final Duration TIMEOUT = Duration.ofSeconds(30);

    @DataProvider(name = "paymentData")
    public Object[][] getPaymentData() {
        return CSVDataProvider.readDatosPasajeros();
    }

    @DataProvider(name = "routeData")
    public Object[][] getRouteData() {
        return CSVDataProvider.readPreciosTrayectos();
    }

    @BeforeMethod
    public void setup() throws InterruptedException {
        // Chrome: Initialization of the ChromeDriver.
        WebDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(TIMEOUT);
        webDriver.manage().window().maximize();
        webDriver.get("https://www.renfe.com/es/es"); // URL page.

        // Initialize all pages
        basePage = new BasePage(webDriver);
        homePage = new HomePage(webDriver);
        seleccionarTuViajePage = new SeleccionarTuViajePage(webDriver);
        introduceTusDatosPage = new IntroduceTusDatosPage(webDriver);
        personalizaTuViajePage = new PersonalizaTuViajePage(webDriver);
        compraPage = new CompraPage(webDriver);
        pasarelaPagoPage = new PasarelaPagoPage(webDriver);
    }

    @Test(dataProvider = "paymentData")
    public void RenfeInvalidCardPaymentTest(
            String originStation, // Cambiado de 'origin' a 'originStation' para mayor claridad
            String destinationStation, // Cambiado de 'destination' a 'destinationStation'
            String firstName,
            String primerApellido,
            String segundoApellido,
            String dni,
            String email,
            String phone,
            String card,
            String expiration,
            String cvv) {

        basePage.clickAcceptAllCookiesButton();

        // Usar datos del CSV para origen y destino (ESTACIONES, no nombres de personas)
        homePage.enterOrigin(originStation); // Usa originStation (ej. "VALENCIA JOAQUÍN SOROLLA")
        homePage.enterDestination(destinationStation); // Usa destinationStation (ej. "BARCELONA-SANTS")

        homePage.selectDepartureDate();
        homePage.clickSoloIdaButtonSelected(true);
        homePage.clickAcceptButton();
        homePage.clickSearchTicketButton();

        seleccionarTuViajePage.verifyYouAreInSelecionaTuViaje();
        seleccionarTuViajePage.selectFirstTrainAvailableAndBasicFare();
        seleccionarTuViajePage.verifyNumberOfTravelers();
        seleccionarTuViajePage.verifyFareIsBasic();

        String totalPriceTrip = seleccionarTuViajePage.verifyFareAndTotalPricesAreEquals();
        TemporaryDataStore.getInstance().set("totalPriceTrip", totalPriceTrip); // Guardar el precio total

        seleccionarTuViajePage.clickSelectButton();
        seleccionarTuViajePage.popUpFareAppears();
        seleccionarTuViajePage.linkContinueSameFareAppears();
        seleccionarTuViajePage.clickLinkContinueSameFare();

        introduceTusDatosPage.verifyYouAreInIntroduceYourDataPage();

        // Usar datos del CSV para información personal
        introduceTusDatosPage.writeFirstNameField(firstName); // "John"
        introduceTusDatosPage.writeFirstSurnameField(primerApellido); // "Doe"
        introduceTusDatosPage.writeSecondSurnameField(segundoApellido); // "López"
        introduceTusDatosPage.writeDNIField(dni); // "46131651E"
        introduceTusDatosPage.writeEmailField(email); // "test@qa.com"
        introduceTusDatosPage.writePhoneField(phone); // "696824570"

        introduceTusDatosPage.verifyTotalPriceData((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
        introduceTusDatosPage.clickPersonalizeTrip();

        personalizaTuViajePage.verifyYouAreInPersonalizedYourTravelPage();
        personalizaTuViajePage
                .verifyTotalPersonalizePrice((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
        personalizaTuViajePage.continueWithPurchase();

        compraPage.verifyYouAreInCompraPage();
        compraPage.typeEmail(email);
        compraPage.writePhoneField(phone);
        compraPage.clickPurchaseCard();
        compraPage.clickPurchaseCondition();
        compraPage.verifyTotalCompraPrice((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
        compraPage.clickContinuarCompra();

        pasarelaPagoPage.verifyYouAreInPasarelaPagoPage();
        pasarelaPagoPage.verifyTotalPricePasarelaPago((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));

        // Usar datos del CSV para información de pago
        pasarelaPagoPage.typeBankCard(card); // "4000 0000 0000 1000"
        pasarelaPagoPage.typeExpirationDate(expiration); // "03/30"
        pasarelaPagoPage.typeCVV(cvv); // "990"
        pasarelaPagoPage.clickPaymentButton();
    }

    /**
     * @Test(dataProvider = "routeData")
     *                    public void testWithDifferentRoutesAndPrices(String
     *                    origin, String destination, String expectedPrice) {
     *                    // Test para probar diferentes rutas con precios
     *                    System.out.println("Testing route from " + origin + " to "
     *                    + destination +
     *                    " with expected price: " + expectedPrice);
     *                    // Aquí puedes implementar la lógica para verificar los
     *                    precios
     *                    }
     */

    /**
     * @AfterMethod
     *              public void tearDown() {
     *              if (webDriver != null) {
     *              webDriver.quit();
     *              }
     *              }
     */
}