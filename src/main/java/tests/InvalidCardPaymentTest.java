package tests;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;

import pages.*;

import steps.Steps;
import utils.CSVDataProvider;
import utils.TemporaryDataStore;

import static pages.SeleccionarTuViajePage.popUpChangeFare;

public class InvalidCardPaymentTest {
    private WebDriver webDriver;
    private BasePage basePage;
    private HomePage homePage;
    private SeleccionarTuViajePage seleccionarTuViajePage;
    private IntroduceTusDatosPage introduceTusDatosPage;
    private PersonalizaTuViajePage personalizaTuViajePage;
    private CompraPage compraPage;
    private PasarelaPagoPage pasarelaPagoPage;
    private Steps steps;

    @DataProvider(name = "paymentData")
    public Object[][] getPaymentData() {
        return CSVDataProvider.readDatosPasajeros();
    }

    @DataProvider(name = "routeData")
    public Object[][] getRouteData() {
        return CSVDataProvider.readPreciosTrayectos();
    }

    @BeforeMethod
    @Parameters("browser")
    public void setup(@Optional("chrome") String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                webDriver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                webDriver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                webDriver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        webDriver.manage().window().maximize();
        webDriver.get("https://www.renfe.com/es/es");

        basePage = new BasePage(webDriver);
        homePage = new HomePage(webDriver);
        seleccionarTuViajePage = new SeleccionarTuViajePage(webDriver);
        introduceTusDatosPage = new IntroduceTusDatosPage(webDriver);
        personalizaTuViajePage = new PersonalizaTuViajePage(webDriver);
        compraPage = new CompraPage(webDriver);
        pasarelaPagoPage = new PasarelaPagoPage(webDriver);
        steps = new Steps(webDriver);
    }

    @Test(dataProvider = "paymentData")
    public void InvalidCardPaymentTest(
            String originStation,
            String destinationStation,
            String firstName,
            String primerApellido,
            String segundoApellido,
            String dni,
            String email,
            String phone,
            String card,
            String expiration,
            String cvv) throws InterruptedException {
        steps.performSearchOriginAndDestinationStation(originStation, destinationStation);
        steps.selectDepartureDate();
        seleccionarTuViajePage.verifyYouAreInSelecionaTuViaje();
        seleccionarTuViajePage.selectFirstTrainAvailableAndBasicFare();
        seleccionarTuViajePage.verifyNumberOfTravelers();
        seleccionarTuViajePage.verifyFareIsBasic();
        String totalPriceTrip = seleccionarTuViajePage.verifyFareAndTotalPricesAreEquals();
        TemporaryDataStore.getInstance().set("totalPriceTrip", totalPriceTrip);
        seleccionarTuViajePage.clickSelectButton();
        seleccionarTuViajePage.verifyElementPresenceAndVisibility(popUpChangeFare, "Cambio de tarifa");
        seleccionarTuViajePage.popUpFareAppears();
        seleccionarTuViajePage.linkPopUpFareAppears();
        seleccionarTuViajePage.clickLinkContinueSameFare();
        introduceTusDatosPage.verifyYouAreInIntroduceYourDataPage();
        introduceTusDatosPage.writeFirstNameField(firstName);
        introduceTusDatosPage.writeFirstSurnameField(primerApellido);
        introduceTusDatosPage.writeSecondSurnameField(segundoApellido);
        introduceTusDatosPage.writeDNIField(dni);
        introduceTusDatosPage.writeEmailField(email);
        introduceTusDatosPage.writePhoneField(phone);
        introduceTusDatosPage.verifyTotalPriceData((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
        introduceTusDatosPage.clickPersonalizeTrip();
        personalizaTuViajePage.verifyYouAreInPersonalizedYourTravelPage();
        personalizaTuViajePage.verifyTotalPersonalizePrice((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
        personalizaTuViajePage.continueWithPurchase();
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10)); // espera explicita para Firefox
        compraPage.verifyYouAreInCompraPage();
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10)); // espera explicita para Firefox
        compraPage.typeEmail(email);
        compraPage.writePhoneField(phone);
        compraPage.clickPurchaseCard();
        compraPage.clickPurchaseCondition();
        compraPage.verifyTotalCompraPrice((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
        compraPage.clickContinuarCompra();
        pasarelaPagoPage.verifyYouAreInPasarelaPagoPage();
        pasarelaPagoPage.verifyTotalPricePasarelaPago((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
        pasarelaPagoPage.typeBankCard(card);
        pasarelaPagoPage.typeExpirationDate(expiration);
        pasarelaPagoPage.typeCVV(cvv);
        pasarelaPagoPage.clickPaymentButton();
    }

    @AfterMethod
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
