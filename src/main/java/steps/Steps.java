package steps;

import org.openqa.selenium.WebDriver;

import pages.BasePage;
import pages.HomePage;
import utils.TemporaryDataStore;

public class Steps extends BasePage {

    public Steps(WebDriver webDriver) {
        // Empty constructor
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    public void performSearchOriginAndDestinationStation(String originStation, String destinationStation) {

        new BasePage(webDriver).clickAcceptAllCookiesButton();
        HomePage homePage = new HomePage(webDriver);
        homePage.enterOrigin(originStation);
        homePage.enterDestination(destinationStation);

    }

    public void selectDepartureDate() throws InterruptedException {

        HomePage homePage = new HomePage(webDriver);
        homePage.selectDepartureDate();
        homePage.clickSoloIdaButtonSelected(true); //From the moment, all test cases requires only one way ticket

        //In case test: InvalidCardPaymentTest15dmas
        if("InvalidCardPaymentTest15dmas".equalsIgnoreCase((String) TemporaryDataStore.getInstance().get("totalPriceTrip"))) {
            homePage.selectDepartureDate15DaysLater();  //click en el datepicker 15 más respecto al actual
        } else {
            System.out.println("El precio del trayecto no es 0");
        }
        
        homePage.clickAcceptButton();
    }
}
