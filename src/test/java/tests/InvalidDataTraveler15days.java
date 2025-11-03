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

public class InvalidDataTraveler15days {

    private WebDriver webDriver;
    private Steps steps;

    @DataProvider(name = "paymentData")
    public Object[][] getPaymentData() {
        return CSVDataProvider.readPassengersDataError15days();
    }

    @DataProvider(name = "routeData")
    public Object[][] getRouteData() {
        return CSVDataProvider.readTripPrices();
    }

    @BeforeMethod
    @Parameters("browser")
    public void setup(@Optional("chrome") String browser) {
        webDriver = DriverManager.getDriver(browser);
        webDriver.manage().timeouts().implicitlyWait(TIMEOUT);
        webDriver.manage().window().maximize();
        webDriver.get("https://www.renfe.com/es/es");
        steps = new Steps(webDriver);

    // Pages and Steps initialization
    steps = new Steps(webDriver);
    }

    /**
    * E2E Test: Client selects the first available train, one-way only, with a date within 15 days,
    * and upon reaching the personal information section, selects one field to enter an invalid value,
    * and fills in the rest of the fields with valid data.
    * Verify that an error message appears indicating that the entered data is invalid, including the color of the message.
    */
    @Test(dataProvider = "paymentData")
    public void InvalidDataTraveler15days(
            String originStation,
            String destinationStation,
            String firstName,
            String firstSurname,
            String secondSurname,
            String dni,
            String email,
            String phone) {
        TemporaryDataStore.getInstance().set("testCase", "InvalidDataTraveler15days");
        // Reusable components (steps)
        steps.performSearchOriginAndDestinationStation(originStation, destinationStation);
        steps.selectDepartureDate();
        steps.selectTrainAndFare();
        steps.getAndStoreDynamicPrice();
        steps.verifyAndConfirmTravel();
        steps.clickPopUpAndLinkAppear();
        steps.verifyPriceIsEqualInData();
        steps.introduceYourDataAndConfirm(firstName, firstSurname, secondSurname, dni, email, phone);
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
