package steps;

import org.openqa.selenium.WebDriver;
import pages.BasePage;
import pages.HomePage;
import utils.TemporaryDataStore;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
        homePage.enterOrigin(originStation, originStation);  // Asume que el valor esperado es igual al ingresado
        homePage.enterDestination(destinationStation, destinationStation);
    }

    public void selectDepartureDate(int defaultDaysLater) {
        homePage.selectDepartureDate();
        homePage.clickSoloIdaButtonSelected(true);

        String targetDate = (String) TemporaryDataStore.getInstance().get("targetDate");
        if (Boolean.parseBoolean(targetDate = "5")) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate parsedDate = LocalDate.parse(targetDate, formatter);
                homePage.selectDepartureDate5DaysLater(defaultDaysLater);wait((5));
                parsedDate.until(LocalDate.now()).getDays();
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Formato de fecha inválido. Use dd/MM/yyyy");
            }
        } else {
            homePage.selectDepartureDate15DaysLater();
        }

    }

}
