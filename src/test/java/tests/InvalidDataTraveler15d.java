package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.*;
import pages.*;
import utils.CSVDataProvider;
import utils.DriverManager;
import utils.TemporaryDataStore;
import steps.Steps;

import static pages.BasePage.TIMEOUT;

public class InvalidDataTraveler15d {

    private WebDriver webDriver;
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
        webDriver = DriverManager.getDriver(browser);
        webDriver.manage().timeouts().implicitlyWait(TIMEOUT);
        webDriver.manage().window().maximize();
        webDriver.get("https://www.renfe.com/es/es");
        steps = new Steps(webDriver);

    // Inicialización de páginas y steps
    steps = new Steps(webDriver);
    }

    /*
    E2E cliente selecciona el primer tren disponible, solo de ida, con fecha dentro dentro de 15 dias,
    y al llegar a la parte de datos personales,  elegir un campo sobre el que intruducir un dato no valido y
    rellenar el resto de los campos con datos validos.
    Comprobar que aparece un mensaje de error indicando que el dato introducido no es valido. También el color.
     */
    @Test(dataProvider = "paymentData")
    public void InvalidDataTraveler15d(
            String originStation,
            String destinationStation,
            String firstName,
            String primerApellido,
            String segundoApellido,
            String dni,
            String email,
            String phone) {
        TemporaryDataStore.getInstance().set("testCase", "InvalidDataTraveler15d");
        // Bloques reutilizables (steps)
        steps.performSearchOriginAndDestinationStation(originStation, destinationStation);
        steps.selectDepartureDate();
        steps.selectTrainAndFare();
        steps.getAndStoreDynamicPrice();
        steps.verifyAndConfirmTravel();
        steps.clickPopUpAndLinkAppear();
        steps.verifyPriceIsEqualInData();
        steps.introduceYourDataAndConfirm(firstName, primerApellido, segundoApellido, dni, email, phone);
        }

    @AfterMethod
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}