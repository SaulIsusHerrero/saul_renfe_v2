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

public class EmptyBuyerDataTest5d {
    private WebDriver webDriver;
    private Steps steps;
    private PersonalizaTuViajePage personalizaTuViajePage;
    private CompraPage compraPage;
    private IntroduceTusDatosPage introduceTusDatosPage;
    private BasePage basePage;
    private HomePage homePage;
    private SeleccionarTuViajePage seleccionarTuViajePage;
    private PasarelaPagoPage pasarelaPagoPage;

    @DataProvider(name = "paymentData")
    public Object[][] getPaymentData() {
        return CSVDataProvider.readDatosPasajerosBlankPaymentData();
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

        // Inicialización de páginas y steps
        steps = new Steps(webDriver);
        basePage = new BasePage(webDriver);
        homePage = new HomePage(webDriver);
        seleccionarTuViajePage = new SeleccionarTuViajePage(webDriver);
        introduceTusDatosPage = new IntroduceTusDatosPage(webDriver);
        personalizaTuViajePage = new PersonalizaTuViajePage(webDriver);
        compraPage = new CompraPage(webDriver);
        pasarelaPagoPage = new PasarelaPagoPage(webDriver);
    }

    @Test(dataProvider = "paymentData")
    public void testEmptyBuyerData(
            String originStation,
            String destinationStation,
            int daysLater,
            String firstName,
            String primerApellido,
            String segundoApellido,
            String dni,
            String email,
            String phone
    )
    {
        // Configurar fecha 5 días después (formato validado)
        steps.selectDepartureDate(daysLater);
        // Búsqueda de ruta y selección de fecha
        steps.performSearchOriginAndDestinationStation(originStation, destinationStation);
        homePage.clickSoloIdaButtonSelected(true);
        //click en el datepicker 5 más respecto al actual
        homePage.selectDepartureDate5DaysLater(daysLater);
        homePage.clickAcceptButton();
        homePage.clickSearchTicketButton();
        seleccionarTuViajePage.verifyYouAreInSelecionaTuViaje();
        seleccionarTuViajePage.selectFirstTrainAvailableAndBasicFare();
        seleccionarTuViajePage.verifyNumberOfTravelers();
        seleccionarTuViajePage.verifyFareIsBasic();
        String totalPriceTrip = seleccionarTuViajePage.verifyFareAndTotalPricesAreEquals();
        TemporaryDataStore.getInstance().set("totalPriceTrip", totalPriceTrip);
        seleccionarTuViajePage.clickSelectButton();
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
        //Verificar mensajes de error en campos obligatorios
        compraPage.isErrorMessageDisplayed(true);
        Assert.assertTrue(compraPage.isErrorMessageDisplayed(true), "No se mostraron todos los mensajes de error esperados");
    }

    @AfterMethod
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
        TemporaryDataStore.getInstance().clear();
    }
}