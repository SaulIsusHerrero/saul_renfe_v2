package tests;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
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
import utils.DataStore;

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

    // Variable global para el precio total del trayecto
    String totalPriceTrip = "";
    // Variables and Constants
    private final Duration TIMEOUT = Duration.ofSeconds(30);

    @DataProvider(name = "CP002")
    public Object[][] getPaymentData() {
        return CSVDataProvider.readData("datos_pasajeros.csv"); // Lee y almacena en DataStore
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

    @Test(dataProvider = "CP002")
    public void RenfeInvalidCardPaymentTest(Object testData) {
        // Realizar un casting a Object[] para procesar los datos
        Object[] data = (Object[]) testData;
        System.out.println("MAYÚSCULAS: " + data[0]);

        // Almacenar automáticamente los datos en DataStore desde el CSV
        storeTestData(data);

        basePage.clickAcceptAllCookiesButton();

        // Recuperar valores directamente desde DataStore
        String originStation = DataStore.getInstance().getValue("originStation");
        System.out.println("Valor recuperado para 'originStation': " + originStation);
        String destinationStation = DataStore.getInstance().getValue("destinationStation");

        homePage.enterOrigin(originStation);
        homePage.enterDestination(destinationStation);

        homePage.selectDepartureDate();
        homePage.clickSoloIdaButtonSelected(true);
        homePage.clickAcceptButton();
        homePage.clickSearchTicketButton();

        seleccionarTuViajePage.verifyYouAreInSelecionaTuViaje();
        seleccionarTuViajePage.selectFirstTrainAvailableAndBasicFare();
        seleccionarTuViajePage.verifyNumberOfTravelers();
        seleccionarTuViajePage.verifyFareIsBasic();

        totalPriceTrip = seleccionarTuViajePage.verifyFareAndTotalPricesAreEquals();
        seleccionarTuViajePage.clickSelectButton();
        seleccionarTuViajePage.popUpFareAppears();
        seleccionarTuViajePage.linkContinueSameFareAppears();
        seleccionarTuViajePage.clickLinkContinueSameFare();

        introduceTusDatosPage.verifyYouAreInIntroduceYourDataPage();

        // Usar datos del CSV para información personal
        String firstName = DataStore.getInstance().getValue("firstName");
        String primerApellido = DataStore.getInstance().getValue("primerApellido");
        String segundoApellido = DataStore.getInstance().getValue("segundoApellido");
        String dni = DataStore.getInstance().getValue("dni");
        String email = DataStore.getInstance().getValue("email");
        String phone = DataStore.getInstance().getValue("phone");

        introduceTusDatosPage.writeFirstNameField(firstName);
        introduceTusDatosPage.writeFirstSurnameField(primerApellido);
        introduceTusDatosPage.writeSecondSurnameField(segundoApellido);
        introduceTusDatosPage.writeDNIField(dni);
        introduceTusDatosPage.writeEmailField(email);
        introduceTusDatosPage.writePhoneField(phone);

        introduceTusDatosPage.verifyTotalPriceData(totalPriceTrip);
        introduceTusDatosPage.clickPersonalizeTrip();

        personalizaTuViajePage.verifyYouAreInPersonalizedYourTravelPage();
        personalizaTuViajePage.verifyTotalPersonalizePrice(totalPriceTrip);
        personalizaTuViajePage.continueWithPurchase();

        compraPage.verifyYouAreInCompraPage();
        compraPage.typeEmail(email);
        compraPage.writePhoneField(phone);
        compraPage.clickPurchaseCard();
        compraPage.clickPurchaseCondition();
        compraPage.verifyTotalCompraPrice(totalPriceTrip);
        compraPage.clickContinuarCompra();

        pasarelaPagoPage.verifyYouAreInPasarelaPagoPage();
        pasarelaPagoPage.verifyTotalPricePasarelaPago(totalPriceTrip);

        // Usar datos del CSV para información de pago
        String card = DataStore.getInstance().getValue("card");
        String expiration = DataStore.getInstance().getValue("expiration");
        String cvv = DataStore.getInstance().getValue("cvv");

        pasarelaPagoPage.typeBankCard(card);
        pasarelaPagoPage.typeExpirationDate(expiration);
        pasarelaPagoPage.typeCVV(cvv);
        pasarelaPagoPage.clickPaymentButton();
    }

    /**
     * Método auxiliar para almacenar datos en DataStore.
     * @param testData Array de datos del CSV (primera fila son claves, las demás son valores).
     */
    private void storeTestData(Object[] testData) {
        if (testData.length % 2 != 0) {
            throw new IllegalArgumentException("El conjunto de datos debe contener pares clave-valor.");
        }
        for (int i = 0; i < testData.length; i += 2) {
            String key = testData[i].toString();
            String value = testData[i + 1].toString();
            DataStore.getInstance().setElement(key, value);
        }
    }

    @AfterMethod
    public void logTestData(ITestResult result) {
        System.out.println("Datos utilizados en el test: " + result.getName());
        DataStore.getInstance().getData().forEach((key, value) -> 
            System.out.println("Clave: " + key + ", Valor: " + value)
        );

        // Limpiar los datos después de imprimirlos para evitar conflictos entre tests
        DataStore.getInstance().clearData();
    }
}