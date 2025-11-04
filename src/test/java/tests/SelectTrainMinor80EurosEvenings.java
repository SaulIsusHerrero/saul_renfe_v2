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

public class SelectTrainMinor80EurosEvenings {

    private WebDriver webDriver;
    private Steps steps;
    private String browser;

    public SelectTrainMinor80EurosEvenings(String browser) {
        this.browser = browser;
    }

    @DataProvider(name = "paymentData")
    public Object[][] getPaymentData() {
        return CSVDataProvider.readData80EurosEvenings();
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
    public void SelectTrainMinor80EurosEvenings(String originStation, String destinationStation) {
        TemporaryDataStore.getInstance().set("testCase", "SelectTrainMinor80EurosEvenings");

        steps.performSearchOriginAndDestinationStation(originStation, destinationStation);
        steps.selectDepartureDate();
        steps.selectTrainMinor80EurosAndBetween5To10PM();
        steps.getAndStoreDynamicPrice();
        steps.verifyAndConfirmTravel();
        steps.clickPopUpAndLinkAppear();
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE && webDriver != null) {
            File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File destino = new File("screenshots/" + result.getName() + "_" + timestamp + ".png");
            destino.getParentFile().mkdirs();
            Files.copy(screenshot.toPath(), destino.toPath());
        }
        DriverManager.quitDriver();
    }
}