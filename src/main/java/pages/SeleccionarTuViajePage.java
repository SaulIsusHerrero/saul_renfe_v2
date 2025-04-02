package pages;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SeleccionarTuViajePage extends BasePage {

    //Locators
    private By trainAvailable = By.cssSelector("div[id^='precio-viaje']:not(:has(div))");
    private By trainAvailableBasicFare = By.cssSelector("div[id^='precio-viaje']:not(:has(div))+div>div>div[class='planes-opciones']>div:nth-child(1)");
    private By fareTrainBasic = By.xpath("(//span[@style='padding-right:10px;' and text()='Básico'])[1]");
    private By selectDayRightArrow = By.cssSelector("button.move_to_tomorrow");
    private By seleccionaTuViajeLabel = By.xpath("//span[contains(text(), 'Selecciona tu viaje') and not(ancestor::select[@disabled])]");
    private By travelerLocator = By.xpath("(//div[contains(@class, 'viajerosSelected') and contains(text(), '1')])[1]");
    private By basicFareLocator = By.xpath("//div[@id='tarifai']");
    private By totalPriceSelectLocator = By.xpath("//span[@id='totalTrayecto']");
    private By linkContinueSameFare = By.cssSelector("p#aceptarConfirmacionFareUpgrade.link-fareUpg");
    private By btnSeleccionar = By.xpath("(//div[@class='rowitem2']/button[@id='btnSeleccionar' and @title='Elegir el trayecto y pasar al siguiente paso'])[1]");
    private By popUpChangeFare = By.cssSelector("button#closeConfirmacionFareUpgrade.close.modalClose-promoUp");

    //Variables
    private BasePage basePage;
    WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));

    //Constructor
    public SeleccionarTuViajePage(WebDriver webDriver) {
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    //Methods
    /**
     * Checks if we are in the next Page "SeleccionarTuViajePage".
     */
    public void verifyYouAreInSelecionaTuViaje() {
        waitUntilElementIsDisplayed(seleccionaTuViajeLabel, Duration.ofSeconds(5));
        WebElement element = webDriver.findElement(seleccionaTuViajeLabel);
        boolean labelDisplayed = element.isDisplayed();
        boolean labelEnabled = element.isEnabled();
        Assert.assertTrue("Selecciona tu viaje", labelDisplayed);
        Assert.assertTrue("Selecciona tu viaje", labelEnabled);
    }

    /**
     * Encuentra el primer tren disponible en el primer día posible
     */
    public void selectFirstTrainAvailable() {
        boolean control = true;

        while (control) {
            // Encuentra la lista de trenes disponibles
            List<WebElement> trainList = webDriver.findElements(trainAvailable);
            List<WebElement> trainFare = webDriver.findElements(trainAvailableBasicFare);
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));

            if (!trainList.isEmpty()) {
                // Click on the first available train at position [0]
                WebElement firstTrain = trainList.get(0);
                wait.until(ExpectedConditions.visibilityOf(firstTrain));
                wait.until(ExpectedConditions.elementToBeClickable(firstTrain));
                firstTrain.click();
                trainFare.get(0).click();//clicando en la tarifa
                control = false;
            } else {
                // Haz clic en el botón del siguiente día para buscar trenes disponibles
                WebElement nextDayButton = webDriver.findElement(selectDayRightArrow);
                nextDayButton.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(trainAvailable));
            }
        }
    }

    /**
     * Clicks the Basic fare button in the Results page.
     */
    public void clickFareApplied() {
        waitUntilElementIsDisplayed(fareTrainBasic, Duration.ofSeconds(5));
        scrollElementIntoView(fareTrainBasic);
        clickElement(fareTrainBasic);
    }

    /**
    * Verifies the number of travelers for the trip
    */
    public void verifyNumberOfTravelers() {
        waitUntilElementIsDisplayed(travelerLocator, Duration.ofSeconds(5));
        WebElement numberTravelers = webDriver.findElement(travelerLocator);
        String travelerText = numberTravelers.getText();
        Assert.assertTrue("Expected number of travelers to be 1", travelerText.contains("1"));
    }

    public void verifyFareIsBasic(){
        waitUntilElementIsDisplayed(basicFareLocator, Duration.ofSeconds(5));
        boolean basicFare = isElementDisplayed(basicFareLocator); //@todo cambiar selector, apunta a arriba.
        Assert.assertTrue(basicFare);
    }

    public void verifyFarePrice() {
        waitUntilElementIsDisplayed(basicFareLocator, Duration.ofSeconds(5));
        boolean basicPrice = isElementDisplayed(totalPriceSelectLocator);
        Assert.assertTrue(basicPrice);
    }

    public void verifyTotalPrice(){
        waitUntilElementIsDisplayed(totalPriceSelectLocator, Duration.ofSeconds(5));
        boolean totalPriceSelect = isElementDisplayed(totalPriceSelectLocator);
        Assert.assertTrue(totalPriceSelect);
    }

    public void verifyFareAndTotalPricesAreEquals(){
        boolean basicPrice = isElementDisplayed(basicFareLocator); //@todo comprobaer el precio, no la disponibilidad
        boolean totalPrice = isElementDisplayed(totalPriceSelectLocator);
        Assert.assertEquals(basicPrice,totalPrice);
    }

    public void clickSelectButton(){
        waitUntilElementIsDisplayed(btnSeleccionar, Duration.ofSeconds(5));
        clickElement(btnSeleccionar);
    }

    public void popUpFareAppears(){
        waitUntilElementIsDisplayed(popUpChangeFare, Duration.ofSeconds(5)); // @todo usar el atributo stile, donde comprobar si aparece  display:none o block
        boolean popUpAppears = isElementDisplayed(popUpChangeFare);
        Assert.assertTrue(popUpAppears);
    }

    public void linkContinueSameFareAppears(){
        waitUntilElementIsDisplayed(popUpChangeFare, Duration.ofSeconds(5));
        boolean popUpAppears = isElementDisplayed(popUpChangeFare); //@todo comprobar que aparece el link
        Assert.assertTrue(popUpAppears);
    }

    public void clickLinkContinueSameFare(){
        waitUntilElementIsDisplayed(linkContinueSameFare, Duration.ofSeconds(5));
        clickElement(linkContinueSameFare);
    }

}