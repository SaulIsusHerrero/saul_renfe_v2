package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import pages.*;
import steps.Steps;
import utils.CSVDataProvider;
import utils.DriverManager;
import utils.TemporaryDataStore;

import static pages.BasePage.TIMEOUT;

public class EmptyBuyerDataTest5d {

    private WebDriver webDriver;
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
        webDriver = DriverManager.getDriver(browser);
        webDriver.manage().timeouts().implicitlyWait(TIMEOUT);
        webDriver.manage().window().maximize();
        webDriver.get("https://www.renfe.com/es/es");

    steps = new Steps(webDriver);
    }

    /*
    Este caso de prueba, selecciona origen, destino, viaje de solo ida, con fecha de salida 5 dias + dia actual,
    primer tren disponible el primer dia de la modalidad basica,
    rellena datos basicos del pasajero y deja los datos de compra de la tarjeta vacios.
    Comprueba que el botón "Pagar" no se habilita.
     */
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
    String bankCard,
    String expirationDate,
    String cvv) {
        TemporaryDataStore.getInstance().set("testCase", "EmptyBuyerDataTest5d");
        // Bloques reutilizables (steps)
        steps.performSearchOriginAndDestinationStation(originStation, destinationStation);
        steps.selectDepartureDate();
        steps.selectTrainAndFare();
        steps.performSearchOriginAndDestinationStation(originStation, destinationStation);
        steps.selectDepartureDate();
        steps.selectTrainAndFare();
        steps.getAndStoreDynamicPrice();
        steps.verifyAndConfirmTravel();
        steps.clickPopUpAndLinkAppear();
        steps.verifyPriceIsEqualInData();
        steps.introduceYourDataAndConfirm(firstName, primerApellido, segundoApellido, dni, email, phone);
        steps.verifyPriceIsEqualInPersonalize();
        steps.confirmPersonalization();
        steps.verifyPriceIsEqualInCompra();
        steps.confirmPaymentData(emailBuyer, phoneBuyer);
        steps.payment(bankCard, expirationDate, cvv);


    }

    @AfterMethod
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
