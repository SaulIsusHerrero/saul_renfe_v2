package tests;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import steps.Steps;
import tools.CSVDataProvider;
import tools.DriverManager;
import tools.TemporaryDataStore;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import static pages.BasePage.TIMEOUT;

public class EmptyBuyerDataTest5days {

    private WebDriver webDriver;
    private Steps steps;
    private String browser;

    // ✅ Constructor added for browser injection
    public EmptyBuyerDataTest5days(String browser) {
        this.browser = browser;
    }

    @DataProvider(name = "paymentData")
    public Object[][] getPaymentData() {
        return CSVDataProvider.readDataPassengersBlankPaymentData5days();
    }

    @DataProvider(name = "routeData")
    public Object[][] getRouteData() {
        return CSVDataProvider.readTripPrices();
    }

    @BeforeMethod
    public void setup() {
        webDriver = DriverManager.getDriver(browser);
        webDriver.manage().timeouts().implicitlyWait(TIMEOUT);
        webDriver.manage().window().maximize();
        webDriver.get("https://www.renfe.com/es/es");
        steps = new Steps(webDriver);
    }

    @Test(dataProvider = "paymentData")
    public void EmptyBuyerDataTest5days(
            String originStation,
            String destinationStation,
            String firstName,
            String firstSurname,
            String secondSurname,
            String dni,
            String email,
            String phone,
            String emailBuyer,
            String phoneBuyer,
            String bankCard,
            String expirationDate,
            String cvv) {

        TemporaryDataStore.getInstance().set("testCase", "EmptyBuyerDataTest5days");

        steps.performSearchOriginAndDestinationStation(originStation, destinationStation);
        steps.selectDepartureDate();
        steps.selectTrainAndFare();
        steps.getAndStoreDynamicPrice();
        steps.verifyAndConfirmTravel();
        steps.clickPopUpAndLinkAppear();
        steps.verifyPriceIsEqualInData();
        steps.introduceYourDataAndConfirm(firstName, firstSurname, secondSurname, dni, email, phone);
        steps.verifyPriceIsEqualInPersonalize();
        steps.confirmPersonalization();
        steps.verifyPriceIsEqualInCompra();
        steps.confirmPaymentData(emailBuyer, phoneBuyer);
        steps.payment(bankCard, expirationDate, cvv);
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        System.out.println("🧪 Test status: " + result.getStatus() + " (" + result.getName() + ")");
        if (result.getStatus() == ITestResult.FAILURE && webDriver != null) {
            if (result.getThrowable() != null) {
                System.err.println("❗ Test exception: " + result.getThrowable().getMessage());
            }
            File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String testName = result.getName();
            File destino = new File("screenshots/" + testName + "_" + timestamp + ".png");
            destino.getParentFile().mkdirs();
            Files.copy(screenshot.toPath(), destino.toPath());
            System.out.println("📸 Screenshot saved in: " + destino.getAbsolutePath());
        }
        DriverManager.quitDriver();
    }
}