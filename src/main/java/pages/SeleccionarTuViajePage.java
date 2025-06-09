package pages;

import org.testng.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class SeleccionarTuViajePage extends BasePage {

    //Locators
    private By seleccionaTuViajeStepper = By.xpath("//li[contains(@class, 'active')]//span[contains(text(), 'Selecciona tu viaje')]");
    private By trainAvailable =  By.cssSelector("div[id^='precio-viaje']");
    private By trainAvailableBasicFare = By.cssSelector("[data-titulo-tarifa='Básico']");
    private By selectDayRightArrow = By.cssSelector(".rescalendar_controls > button.move_to_tomorrow");
    private By travelerLocator = By.xpath("(//div[contains(@class, 'viajerosSelected') and contains(text(), '1')])[1]");
    private By basicFareLocator = By.xpath("//div[@class='asient']//div[contains(@class, 'rowitem1')]/span[contains(text(), 'Básico')]");
    private By basicFarePriceLocator = By.xpath("(//div[@class='rowitem2 precioTarifa']/span)[1]");
    private By totalPriceLocator = By.xpath("(//span[@id='totalTrayectoBanner'])[1]");
    private By btnSeleccionar = By.xpath("(//button[@id='btnSeleccionar'])[1]");
    public  By popUpChangeFare = By.xpath("//button[@id='closeConfirmacionFareUpgrade' and " + "contains(@class, 'close') and " + "not(contains(@style, 'display: none'))]");
    private By linkContinueSameFare = By.xpath("//div/p[@class and contains(text(), 'No')]");
    private By popupIncidenciaLocator = By.xpath("//div[contains(@class, 'viaje-incidencia-style')]");
    private By closepopupIncidenciaLocator = By.xpath("(//h6[contains(@class, 'estilo-titulo-modal') and contains(text(), 'Viaje con incidencia')]/ancestor::div[contains(@class,'modal')]//button[@aria-label='Close'])[1]");
    private By timeTravelLocatorList = By.xpath("//div[@class='col-md-8 trenes']//h5[contains(text(), 'h')]");
    private By priceTravelLocatorList = By.xpath("//span[@class='precio-final' and contains(text(), '€')]");

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
        waitUntilElementIsDisplayed(seleccionaTuViajeStepper, TIMEOUT);
        Boolean youAreInSeleccionaTuViaje = webDriver.findElement(seleccionaTuViajeStepper).isEnabled();
        Assert.assertTrue(youAreInSeleccionaTuViaje);
    }

    /**
     * Selecciona el primer tren disponible que cumpla:
     * 1. Tarifa "Básico".
     * 2. No tener en cuenta que tenga badge o no.
     */
    public void selectFirstValidBasicFareTrain() {
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);
        boolean control = true;
        while (control) {
            // Encuentra la lista de trenes disponibles
            List<WebElement> trainList = webDriver.findElements(trainAvailable);
            List<WebElement> trainFare = webDriver.findElements(trainAvailableBasicFare);
            if (!trainList.isEmpty())
            {
                // Click on the first available train at position [0]
                WebElement firstTrain = trainList.get(0);
                wait.until(ExpectedConditions.visibilityOf(firstTrain));
                wait.until(ExpectedConditions.elementToBeClickable(firstTrain));
                //click con JavascriptExecutor para que no sea interceptado.
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", firstTrain);
                WebElement firstBasicFare = trainFare.get(0);
                scrollElementIntoViewElement(firstBasicFare);
                wait.until(ExpectedConditions.visibilityOf(firstBasicFare));
                wait.until(ExpectedConditions.elementToBeClickable(firstBasicFare));
                //click con JavascriptExecutor para que no sea interceptado.
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", firstBasicFare);
                control = false;
            } else {
                // Haz clic en el botón del siguiente día para buscar trenes disponibles
                clickElement(selectDayRightArrow);
                wait = new WebDriverWait(webDriver, TIMEOUT);
            }
        }
    }

    /**
     * Selecciona el primer tren disponible que cumpla:
     * 1. Precio inferior a 20€.
     * 2. Hora de salida entre las 17:00 (5 PM) y las 22:00 (10 PM).
     */
    public void selectFirstTrainUnder20EurosAndBetween5To10PM() {
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);
        boolean found = true;

        while (found) {
            // Encuentra la lista de trenes disponibles
            List<WebElement> trainList = webDriver.findElements(trainAvailable);
            List<WebElement> trainPriceList = webDriver.findElements(priceTravelLocatorList);
            List<WebElement> trainTimeList = webDriver.findElements(timeTravelLocatorList);

            // Asegura que todas las listas estén alineadas
            int total = Math.min(Math.min(trainList.size(), trainPriceList.size()), trainTimeList.size());

            for (int i = 0; i < total; i++) {
                WebElement trainElement = trainList.get(i);
                WebElement priceElement = trainPriceList.get(i);
                WebElement timeElement = trainTimeList.get(i);

                // Normalización del formato de hora
                String timeText = timeElement.getText().trim().replace("h", "").trim(); // Ej: "18:45"
                LocalTime departureTime = LocalTime.parse(timeText, DateTimeFormatter.ofPattern("HH:mm"));

                // Normalización del formato de precio
                String priceText = priceElement.getText().replaceAll("[^\\d,\\.]", "").replace(",", ".").trim(); // Ej: "19.90"
                double price = Double.parseDouble(priceText);

                // Validación de condiciones
                if (price < 20.0 &&
                        !departureTime.isBefore(LocalTime.of(17, 0)) &&
                        !departureTime.isAfter(LocalTime.of(22, 0))) {
                    wait.until(ExpectedConditions.visibilityOf(trainElement));
                    wait.until(ExpectedConditions.elementToBeClickable(trainElement));
                    scrollElementIntoViewElement(trainElement);
                    ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", trainElement);
                    found = false;
                }
            }
            if (!found) {
                // Si no se encontró tren válido, pasa al siguiente día
                clickElement(selectDayRightArrow);
                wait = new WebDriverWait(webDriver, TIMEOUT);
            }
        }

        if (!found) {
            Assert.fail("❌ No hay trenes disponibles con estas características, test fallido");
        }
    }


    /**
     * Verifies the number of travelers for the trip in the semimodal
     */
    public void verifyNumberOfTravelers() {
        waitUntilElementIsDisplayed(travelerLocator, TIMEOUT);
        String traveler = webDriver.findElement(travelerLocator).getText();
        Assert.assertTrue(traveler.contains("1"));
    }

    /**
     * Verifies the fare applied for the trip in the semimodal is "Básico"
     */
    public void verifyFareIsBasic() {
        waitUntilElementIsDisplayed(basicFareLocator, TIMEOUT);
        String fareText = webDriver.findElement(basicFareLocator).getText();
        Assert.assertTrue(fareText.contains("Básico"));
    }

    /**
    * Verifies that the fare and the total prices applied for the trip in the semimodal are equals
    */
    public String verifyFareAndTotalPricesAreEquals() {
        waitUntilElementIsDisplayed(basicFarePriceLocator, TIMEOUT);
        waitUntilElementIsDisplayed(totalPriceLocator, TIMEOUT);

        // Obtiene y normaliza los precios
        String basicFarePrice = normalizePrice(webDriver.findElement(basicFarePriceLocator).getText());
        String totalPriceTrip = normalizePrice(webDriver.findElement(totalPriceLocator).getText());

        Assert.assertEquals(basicFarePrice, totalPriceTrip);
        return totalPriceTrip;
    }

    /**
     * Clicks the Seleccionar button in the semimodal
     */
    public void clickSelectButton(){
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(btnSeleccionar));
        button.click();
    }

    /**
     * Verifies that the fare change pop-up exists in DOM and is visible on screen
     * @param locator Localizador del elemento
     * @param elementName Nombre descriptivo del elemento para mensajes
     */
    public void verifyElementPresenceAndVisibilityPopUpChangeFare(By locator, String elementName) {
        elementName = "Pop-up de cambio de tarifa";
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(popUpChangeFare));
            wait.until(ExpectedConditions.visibilityOfElementLocated(popUpChangeFare));
            System.out.println("✅ " + elementName + " está presente en el DOM y visible");
        } catch (Exception e) {
            Assert.fail("❌ " + elementName + " no está visible o presente: " + e.getMessage());
        }
    }

    /**
     * Verifies that the link of into the pop-up Change fare exists in DOM and is visible on screen
     */
    public void linkPopUpFareAppears() {
        String elementLink = "No, quiero continuar con Básico";
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(linkContinueSameFare));
            wait.until(ExpectedConditions.visibilityOfElementLocated(linkContinueSameFare));
            System.out.println("✅ " + elementLink + " está presente en el DOM y visible");
        } catch (Exception e) {
            Assert.fail("❌ " + elementLink + " no está visible o presente: " + e.getMessage());
        }
    }

    /**
     * Click in the link to continue with the same fare
     */
    public void clickLinkContinueSameFare(){
        waitUntilElementIsDisplayed(linkContinueSameFare, TIMEOUT);
        clickElement(linkContinueSameFare);
        WebElement popupIncidencia = webDriver.findElement(popupIncidenciaLocator);
        //Este if es por si aparece un pop up de aviso/incidencia.
        if (popupIncidencia.isDisplayed()){
            WebElement closePopUpAviso = webDriver.findElement(closepopupIncidenciaLocator);
            closePopUpAviso.click();
        }
    }

}