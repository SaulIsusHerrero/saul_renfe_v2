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

    public void selectDepartureDate(int daysLater) {
        HomePage homePage = new HomePage(webDriver);
        homePage.selectDepartureDate();
        homePage.clickSoloIdaButtonSelected(true); //From the moment, all the test cases require only one way ticket

        //In the current tests: InvalidDataTraveler15d and EmptyBuyerDataTest5d
        if("InvalidDataTraveler15d".equalsIgnoreCase((String) TemporaryDataStore.getInstance().get("totalPriceTrip"))) {
            homePage.selectDepartureDateDaysLater(15);  //click en el datepicker 15 más respecto al actual
        } else {
            System.out.println("El precio del trayecto no es 0");
        }
        if("EmptyBuyerDataTest5d".equalsIgnoreCase((String) TemporaryDataStore.getInstance().get("totalPriceTrip"))) {
            homePage.selectDepartureDateDaysLater(5);  //click en el datepicker 5 más respecto al actual
        } else {
            System.out.println("El precio del trayecto no es 0");
        }
        homePage.clickAcceptButton();
        homePage.clickSearchTicketButton();
    }

}
