package steps;

import org.openqa.selenium.WebDriver;
import pages.BasePage;
import pages.HomePage;
import utils.TemporaryDataStore;

public class Steps extends BasePage {
    private final WebDriver webDriver;
    private final HomePage homePage;

    public Steps(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
        this.homePage = new HomePage(webDriver);
    }

    public void performSearchOriginAndDestinationStation(String originStation, String destinationStation) {
        homePage.clickAcceptAllCookiesButton();
        homePage.enterOrigin(originStation);
        homePage.enterDestination(destinationStation);
    }

    public void selectDepartureDate() {
        homePage.selectDepartureDate();
        homePage.clickSoloIdaButtonSelected(true); //From the moment, all the test cases require only one way ticket
    }

}
