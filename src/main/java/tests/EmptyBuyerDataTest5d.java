package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.*;
import steps.Steps;
import utils.CSVDataProvider;
import utils.TemporaryDataStore;

import static pages.BasePage.TIMEOUT;

public class EmptyBuyerDataTest5d {

    private WebDriver webDriver;
    private SeleccionarTuViajePage seleccionarTuViajePage;
    private IntroduceTusDatosPage introduceTusDatosPage;
    private PersonalizaTuViajePage personalizaTuViajePage;
    private CompraPage compraPage;
    private PasarelaPagoPage pasarelaPagoPage;
    private Steps steps;

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

        seleccionarTuViajePage = new SeleccionarTuViajePage(webDriver);
        introduceTusDatosPage = new IntroduceTusDatosPage(webDriver);
        steps = new Steps(webDriver);
    }

    @Test(dataProvider = "paymentData")
    public void EmptyBuyerDataTest5d (
    String originStation,
    String destinationStation,
    String firstName,
    String primerApellido,
    String segundoApellido,
    String dni,
    String email,
    String phone,
    String emailBuyer,
    String phoneBuyer,
    String card,
    String expirationDate,
    String cvv) throws InterruptedException {
        TemporaryDataStore.getInstance().set("testCase", "EmptyBuyerDataTest5d");
        // Bloques reutilizables (steps)
        steps.performSearchOriginAndDestinationStation(originStation, destinationStation);
        steps.selectDepartureDate();
        steps.selectTrainAndFare();
        steps.getAndStoreDynamicPrice();
        steps.verifyAndConfirmTravel();
        steps.clickPopUpAndLinkAppear();
        steps.introduceYourData(firstName, primerApellido, segundoApellido, dni, email, phone);
        steps.verifyPriceIsEqualInData();
        steps.confirmMyData();
        steps.verifyPriceIsEqualInPersonalize();
        steps.confirmPersonalization();
        steps.verifyPriceIsEqualInCompra();
        steps.confirmPaymentData(emailBuyer, phoneBuyer);
        steps.payment(card, expirationDate, cvv);
    }

    @AfterMethod
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
