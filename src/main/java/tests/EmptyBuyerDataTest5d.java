package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import steps.Steps;
import utils.TemporaryDataStore;
import utils.CSVDataProvider;

import java.time.Duration;

import static pages.BasePage.TIMEOUT;

public class EmptyBuyerDataTest5d {

    private WebDriver webDriver;
    private Steps steps;
    private SeleccionarTuViajePage seleccionarTuViajePage;
    private IntroduceTusDatosPage introduceTusDatosPage;
    private PersonalizaTuViajePage personalizaTuViajePage;
    private CompraPage compraPage;
    private PasarelaPagoPage pasarelaPagoPage;
    private HomePage homePage;


    @DataProvider(name = "paymentData")
    public Object[][] getPaymentData() {
        return CSVDataProvider.readDatosPasajerosBlankPaymentData5d();
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

        webDriver.manage().timeouts().implicitlyWait(TIMEOUT);
        webDriver.manage().window().maximize();
        webDriver.get("https://www.renfe.com/es/es");

        // Inicialización de páginas y steps
        steps = new Steps(webDriver);
        seleccionarTuViajePage = new SeleccionarTuViajePage(webDriver);
        introduceTusDatosPage = new IntroduceTusDatosPage(webDriver);
        personalizaTuViajePage = new PersonalizaTuViajePage(webDriver);
        compraPage = new CompraPage(webDriver);
        pasarelaPagoPage = new PasarelaPagoPage(webDriver);
        homePage = new HomePage(webDriver);
    }

    @Test(dataProvider = "paymentData")
    public void EmptyBuyerDataTest5d(
            String originStation,
            String destinationStation,
            String firstName,
            String primerApellido,
            String segundoApellido,
            String dni,
            String email,
            String phone
            ) throws InterruptedException {
        TemporaryDataStore.getInstance().set("testCase", "InvalidDataTraveler15d");
        // Bloques reutilizables (steps)
        steps.performSearchOriginAndDestinationStation(originStation, destinationStation);
        steps.selectDepartureDate();
        seleccionarTuViajePage.verifyYouAreInSelecionaTuViaje();
        seleccionarTuViajePage.selectFirstValidBasicFareTrain();
        seleccionarTuViajePage.verifyNumberOfTravelers();
        seleccionarTuViajePage.verifyFareIsBasic();
        String totalPriceTrip = seleccionarTuViajePage.verifyFareAndTotalPricesAreEquals();
        TemporaryDataStore.getInstance().set("totalPriceTrip", totalPriceTrip);
        seleccionarTuViajePage.clickSelectButton();
        seleccionarTuViajePage.verifyElementPresenceAndVisibilityPopUpChangeFare(seleccionarTuViajePage.popUpChangeFare, "El PopUp cambio de tarifa está presente y visible");
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
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT); // espera explicita para Firefox
        compraPage.verifyYouAreInCompraPage();
        wait = new WebDriverWait(webDriver, TIMEOUT); // espera explicita para Firefox
        compraPage.typeEmail(email);
        compraPage.writePhoneField(phone);
        compraPage.clickPurchaseCard();
        compraPage.clickPurchaseCondition();
        compraPage.verifyTotalCompraPrice((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
        compraPage.clickContinuarCompra();
        compraPage.clickContinuarCompra();
        pasarelaPagoPage.verifyYouAreInPasarelaPagoPage();
        pasarelaPagoPage.clickPaymentButton();
        Assert.assertTrue(webDriver.findElement(pasarelaPagoPage.disabledPayButton).isEnabled());
    }

    @AfterMethod
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}