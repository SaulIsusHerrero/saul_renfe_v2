package tests;

import java.time.Duration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import steps.Steps;
import utils.CSVDataProvider;
import utils.TemporaryDataStore;

public class InvalidCardPaymentTest {

    // Locators
    private final By popUpError = By.xpath("//div[@id='myModalBody']//li[contains(text(), 'Tarjeta no soportada (RS18)')]");

    private WebDriver webDriver;
    private Steps steps;
    private BasePage basePage;
    private HomePage homePage;
    private SeleccionarTuViajePage seleccionarTuViajePage;
    private IntroduceTusDatosPage introduceTusDatosPage;
    private PersonalizaTuViajePage personalizaTuViajePage;
    private CompraPage compraPage;
    private PasarelaPagoPage pasarelaPagoPage;

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
            String expirationDate,
            String cvv) {
        // acepta cookies y escoge estacion de origen y destino.
        steps.performSearchOriginAndDestinationStation(originStation, destinationStation);
        // click en seleccionar salida de viaje y selecciona el número de días para escoger tu viaje hacia adelante (en este caso el mismo día = 0), sólo de ida, acepta y busca billete
        steps.selectDepartureDate(0);
        seleccionarTuViajePage.verifyYouAreInSelecionaTuViaje();
        seleccionarTuViajePage.selectFirstTrainAvailableAndBasicFare();
        seleccionarTuViajePage.verifyNumberOfTravelers();
        seleccionarTuViajePage.verifyFareIsBasic();
        String totalPriceTrip = seleccionarTuViajePage.verifyFareAndTotalPricesAreEquals();
        TemporaryDataStore.getInstance().set("totalPriceTrip", totalPriceTrip);
        seleccionarTuViajePage.clickSelectButton();
        seleccionarTuViajePage.linkPopUpFareAppears();
        seleccionarTuViajePage.popUpFareAppears();
        seleccionarTuViajePage.clickLinkContinueSameFare();
        introduceTusDatosPage.verifyYouAreInIntroduceYourDataPage();
        introduceTusDatosPage.writeFirstNameField(firstName);
        introduceTusDatosPage.writeFirstSurnameField(primerApellido);
        introduceTusDatosPage.writeSecondSurnameField(segundoApellido);
        introduceTusDatosPage.writeDNIField(dni);
        introduceTusDatosPage.writeEmailField(email);
        introduceTusDatosPage.writePhoneField(phone);
        introduceTusDatosPage.verifyTotalPriceData(totalPriceTrip);
        introduceTusDatosPage.clickPersonalizeTrip();
        personalizaTuViajePage.verifyYouAreInPersonalizedYourTravelPage();
        personalizaTuViajePage.continueWithPurchase();
        personalizaTuViajePage.verifyTotalPersonalizePrice(totalPriceTrip);
        compraPage.verifyYouAreInCompraPage();
        compraPage.typeEmail(email);
        compraPage.writePhoneField(phone);
        compraPage.clickPurchaseCard();
        compraPage.clickNewCard();
        compraPage.clickPurchaseCondition();
        compraPage.verifyTotalCompraPrice(totalPriceTrip);
        compraPage.clickContinuarCompra();
        pasarelaPagoPage.verifyYouAreInPasarelaPagoPage();
        pasarelaPagoPage.verifyTotalPricePasarelaPago(totalPriceTrip);
        pasarelaPagoPage.typeBankCard(card);
        pasarelaPagoPage.typeExpirationDate(expirationDate);
        pasarelaPagoPage.typeCVV(cvv);
        pasarelaPagoPage.clickPaymentButton();
        // Verificar si aparece el mensaje de error por tarjeta inválida
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        boolean isErrorVisible = wait.until(driver -> driver.findElements(popUpError).size() > 0);
        Assert.assertTrue(isErrorVisible, "El mensaje de error por tarjeta inválida no apareció.");
    }

    @AfterMethod
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}