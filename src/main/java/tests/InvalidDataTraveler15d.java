package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import utils.CSVDataProvider;
import utils.TemporaryDataStore;
import steps.Steps;
import java.time.Duration;

import static pages.BasePage.TIMEOUT;

public class InvalidDataTraveler15d {

    private WebDriver webDriver;
    private HomePage homePage;
    private SeleccionarTuViajePage seleccionarTuViajePage;
    private IntroduceTusDatosPage introduceTusDatosPage;
    private Steps steps;

    @DataProvider(name = "paymentData")
    public Object[][] getPaymentData() {
        return CSVDataProvider.readDatosPasajerosError15d();
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
        homePage = new HomePage(webDriver);
    }

    @Test(dataProvider = "paymentData")
    public void InvalidDataTraveler15d(
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
        introduceTusDatosPage.verifyTotalPriceData(totalPriceTrip);
        introduceTusDatosPage.clickPersonalizeTrip();
        Assert.assertTrue(Boolean.parseBoolean(firstName));
        }

    @AfterMethod
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}