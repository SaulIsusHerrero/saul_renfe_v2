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

public class InvalidCardPaymentTest {

    private WebDriver webDriver;
    private Steps steps;

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
        webDriver = DriverManager.getDriver(browser);
        webDriver.manage().timeouts().implicitlyWait(TIMEOUT);
        webDriver.manage().window().maximize();
        webDriver.get("https://www.renfe.com/es/es");

        steps = new Steps(webDriver);
    }

    /*
    E2E client selects the first available train and tries to pay with an incorrect credit card.
    Check that a pop-up error message appears explaining the error in the payment data.
    */
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
            String emailBuyer,
            String phoneBuyer,
            String bankCard,
            String expirationDate,
            String cvv) {

        TemporaryDataStore.getInstance().set("testCase", "InvalidCardPaymentTest");

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
