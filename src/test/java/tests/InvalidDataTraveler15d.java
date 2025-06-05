package tests;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import utils.CSVDataProvider;
import utils.DriverManager;
import utils.TemporaryDataStore;
import steps.Steps;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.testng.ITestResult;

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

    /**
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
    public void capturarPantallaSiFalla(ITestResult result) throws IOException {
        System.out.println("🧪 Estado del test: " + result.getStatus() + " (" + result.getName() + ")");

        if (result.getStatus() == ITestResult.FAILURE && webDriver != null) {
            if (result.getThrowable() != null) {
                System.err.println("❗ Error en test: " + result.getThrowable().getMessage());
            }

            File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String testName = result.getName();
            File destino = new File("screenshots/" + testName + "_" + timestamp + ".png");
            destino.getParentFile().mkdirs();
            Files.copy(screenshot.toPath(), destino.toPath());
            System.out.println("📸 Captura guardada en: " + destino.getAbsolutePath());
        }

        if (webDriver != null) {
            webDriver.quit();
        }
    }

}
