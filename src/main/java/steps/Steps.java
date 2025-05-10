package steps;

import org.openqa.selenium.WebDriver;

import pages.BasePage;
import pages.HomePage;
import utils.TemporaryDataStore;
import resources;

public class Steps extends BasePage {

    public Steps(WebDriver webDriver) {
        // Empty constructor
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    public void performSearchOriginAndDestinationStation(String originStation, String destinationStation) {

        new HomePage(webDriver).clickAcceptAllCookiesButton();
        HomePage homePage = new HomePage(webDriver);
        homePage.enterOrigin(originStation);
        homePage.enterDestination(destinationStation);

    }

    public void selectDepartureDate() throws InterruptedException {

        HomePage homePage = new HomePage(webDriver);
        homePage.selectDepartureDate();
        homePage.clickSoloIdaButtonSelected(true); //From the moment, all the test cases require only one way ticket

        //In case test: InvalidDataTraveler15d or EmptyBuyerDataTest5d
        if ("InvalidDataTraveler15d".equalsIgnoreCase((String) TemporaryDataStore.getInstance().get("totalPriceTrip"))) {
            homePage.selectDateDaysLater(webDriver, 15);
        } else if ("EmptyBuyerDataTest5d".equalsIgnoreCase((String) TemporaryDataStore.getInstance().get("totalPriceTrip"))) {
            homePage.selectDateDaysLater(webDriver, 5);
        }
        homePage.clickAcceptButton();
        homePage.clickSearchTicketButton();
    }

}