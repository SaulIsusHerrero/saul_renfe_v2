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
    //@TODO cambiar el selector de tarifa y precio tarifa al de la semimodal
    private By trainAvailable = By.cssSelector("div[id^='precio-viaje']:not(:has(div))");
    private By trainAvailableBasicFare = By.cssSelector("div[id^='precio-viaje']:not(:has(div))+div>div>div[class='planes-opciones']>div:nth-child(1)");
    private By selectDayRightArrow = By.cssSelector("button.move_to_tomorrow");
    private By seleccionaTuViajeLabel = By.xpath("//span[contains(text(), 'Selecciona tu viaje') and not(ancestor::select[@disabled])]");
    private By travelerLocator = By.xpath("(//div[contains(@class, 'viajerosSelected') and contains(text(), '1')])[1]");
    private By basicFareLocator = By.xpath("//div[contains(@class, 'viajerosSelected')]/span[text()='Básico']");
    //private By basicFareLocatorPrice = By.xpath("//div[@id='tarifai']");
    private By totalPriceSelectLocator = By.xpath("//span[@id='totalTrayecto']");
    private By linkContinueSameFare = By.cssSelector("p#aceptarConfirmacionFareUpgrade.link-fareUpg");
    private By btnSeleccionar = By.xpath("(//div[@class='rowitem2']/button[@id='btnSeleccionar' and @title='Elegir el trayecto y pasar al siguiente paso'])[1]");
    private By popUpChangeFare = By.cssSelector("button#closeConfirmacionFareUpgrade.close.modalClose-promoUp");

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
        Assert.assertTrue(webDriver.findElement(seleccionaTuViajeLabel).isEnabled());
    }

    /**
     * Encuentra el primer tren disponible en el primer día posible
     */
    public void selectFirstTrainAvailableAndBasicFare() {
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
                trainFare.get(0).click(); //click on the fare
                control = false;
            } else {
                // Haz clic en el botón del siguiente día para buscar trenes disponibles
                webDriver.findElement(selectDayRightArrow).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(trainAvailable));
            }
        }
    }

   /**
    * Verifies the number of travelers for the trip
    */
    public void verifyNumberOfTravelers() {
        waitUntilElementIsDisplayed(travelerLocator, Duration.ofSeconds(5));
        Assert.assertTrue(webDriver.findElement(travelerLocator).getText().contains("1"));
    }

    public void verifyFareIsBasic(){
        waitUntilElementIsDisplayed(basicFareLocator, Duration.ofSeconds(5));
        Assert.assertTrue(webDriver.findElement(basicFareLocator).getText().contains("Básico"));
    }

    public void verifyFarePrice() {
        //waitUntilElementIsDisplayed(basicFareLocatorPrice, Duration.ofSeconds(5));
        //boolean basicPrice = isElementDisplayed(totalPriceSelectLocator);
        //Assert.assertTrue(basicPrice);
    }

    public void verifyTotalPrice(){
        waitUntilElementIsDisplayed(totalPriceSelectLocator, Duration.ofSeconds(5));
        boolean totalPriceSelect = isElementDisplayed(totalPriceSelectLocator);
        Assert.assertTrue(totalPriceSelect);
    }

    public void verifyFareAndTotalPricesAreEquals(){
        //boolean basicPrice = isElementDisplayed(basicFareLocator); //@todo comprobaer el precio, no la disponibilidad. los precios se comprueban con el getText
        boolean totalPrice = isElementDisplayed(totalPriceSelectLocator);
        //Assert.assertEquals(basicPrice,totalPrice);
    }

    public void clickSelectButton(){
        waitUntilElementIsDisplayed(btnSeleccionar, Duration.ofSeconds(5));
        clickElement(btnSeleccionar);
    }

    public void popUpFareAppears(){
        waitUntilElementIsDisplayed(popUpChangeFare, Duration.ofSeconds(5)); //@todo usar el atributo style, donde comprobar si aparece  display:none o block
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