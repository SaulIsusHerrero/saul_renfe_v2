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

        new HomePage(webDriver).clickAcceptAllCookiesButton();
        HomePage homePage = new HomePage(webDriver);
        homePage.enterOrigin(originStation);
        homePage.enterDestination(destinationStation);

    }

    public void selectDepartureDate() throws InterruptedException {
        HomePage homePage = new HomePage(webDriver);
        homePage.selectDepartureDate();
        homePage.clickSoloIdaButtonSelected(true);

        // Verificar usando la clave "testCase" en lugar de "totalPriceTrip"
        String testCase = (String) TemporaryDataStore.getInstance().get("testCase");

        if ("InvalidDataTraveler15d".equalsIgnoreCase(testCase)) {
            TemporaryDataStore.getInstance().get("totalPriceTrip");
            homePage.selectDateDaysLater(webDriver, 15);
        } else if ("EmptyBuyerDataTest5d".equalsIgnoreCase(testCase)) {
            TemporaryDataStore.getInstance().get("totalPriceTrip");
            homePage.selectDateDaysLater(webDriver, 5);
        } else{
            System.out.println("El precio del trayecto no es 0");
        }

        homePage.clickAcceptButton();
        homePage.clickSearchTicketButton();
    }

}