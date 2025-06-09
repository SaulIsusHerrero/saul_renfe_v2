package tests;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import steps.Steps;
import utils.CSVDataProvider;
import utils.DriverManager;
import utils.TemporaryDataStore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import static pages.BasePage.TIMEOUT;

public class SelectTrainMinor20€Evenings {

    private WebDriver webDriver;
    private Steps steps;

    @DataProvider(name = "paymentData")
    public Object[][] getPaymentData() {
        return CSVDataProvider.readDatos20€Tarde();
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

    /**
    * Seleccionar un tren de solo de ida para el dia actual y al llegar al formulario 2 donde se seleccionas el tren,
     * buscar un tren disponible, cuyo precio sea inferior a 20 euros entre las 5 de la tarde y las 10 de la noche.
    */
    @Test(dataProvider = "paymentData")
    public void SelectTrainMinor20€Evenings(
            String originStation,
            String destinationStation){

            TemporaryDataStore.getInstance().set("testCase", "SelectTrainMinor20€Evenings");

            steps.performSearchOriginAndDestinationStation(originStation, destinationStation);
            steps.selectDepartureDate();
            steps.selectTrainMinor20EurosAndBetween5To10PM();
    }

    @AfterMethod
    public void capturarPantallaSiFalla(ITestResult result) throws IOException {
        System.out.println("🧪 Estado del test: " + result.getStatus() + " (" + result.getName() + ")");

        if (result.getStatus() == ITestResult.FAILURE && webDriver != null) {
            if (result.getThrowable() != null) {
                System.err.println("❗ Excepción en test: " + result.getThrowable().getMessage());
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
