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
    private By seleccionaTuViajeLabel = By.xpath("//span[contains(text(), 'Selecciona tu viaje') and not(ancestor::select[@disabled])]");
    private By trainAvailable = By.cssSelector("div[id^='precio-viaje']:not(:has(div))");
    private By trainAvailableBasicFare = By.cssSelector("div[id^='precio-viaje']:not(:has(div))+div>div>div[class='planes-opciones']>div:nth-child(1)");
    private By selectDayRightArrow = By.cssSelector(".rescalendar_controls > button.move_to_tomorrow");
    private By travelerLocator = By.xpath("(//div[contains(@class, 'viajerosSelected') and contains(text(), '1')])[1]");
    private By basicFareLocator = By.xpath("//div[@class='asient']//div[contains(@class, 'rowitem1')]/span[contains(text(), 'Básico')]");
    private By basicFarePriceLocator = By.xpath("(//div[@class='rowitem2 precioTarifa']/span)[1]");
    private By totalPriceLocator = By.xpath("(//span[@id='totalTrayectoBanner'])[1]");
    private By btnSeleccionar = By.xpath("(//button[@id='btnSeleccionar'])[1]");
    private By popUpChangeFare = By.xpath("//button[@id='closeConfirmacionFareUpgrade' and " + "contains(@class, 'close') and " + "not(contains(@style, 'display: none'))]");
    private By linkContinueSameFare = By.cssSelector("p.link-fareUpg[id='aceptarConfirmacionFareUpgrade']");

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
        waitUntilElementIsDisplayed(seleccionaTuViajeLabel, Duration.ofSeconds(10));
        WebElement element = webDriver.findElement(seleccionaTuViajeLabel);
        Assert.assertEquals("Selecciona tu viaje", element.getText());
        Assert.assertTrue(element.isEnabled());
    }

    /**
     * Encuentra el primer tren disponible en el primer día posible con la tarifa Básica
     */
    public void selectFirstTrainAvailableAndBasicFare() {
        boolean control = true;

        while (control) {
            // Encuentra la lista de trenes disponibles
            List<WebElement> trainList = webDriver.findElements(trainAvailable);
            List<WebElement> trainFare = webDriver.findElements(trainAvailableBasicFare);
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));

            if (!trainList.isEmpty()) {
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
                webDriver.findElement(selectDayRightArrow).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(trainAvailable));
            }
        }
    }

    /**
     * Verifies the number of travelers for the trip in the semimodal
     */
    public void verifyNumberOfTravelers() {
        waitUntilElementIsDisplayed(travelerLocator, Duration.ofSeconds(15));
        Assert.assertTrue(webDriver.findElement(travelerLocator).getText().contains("1"));
    }

    /**
     * Verifies the fare applied for the trip in the semimodal is "Básico"
     */
    public void verifyFareIsBasic() {
        waitUntilElementIsDisplayed(basicFareLocator, Duration.ofSeconds(15));
        String fareText = webDriver.findElement(basicFareLocator).getText();
        Assert.assertTrue(fareText.contains("Básico"));
    }

    /**
    * Verifies that the fare and the total prices applied for the trip in the semimodal are equals
    */
    public String verifyFareAndTotalPricesAreEquals() {
        waitUntilElementIsDisplayed(basicFarePriceLocator, Duration.ofSeconds(15));
        waitUntilElementIsDisplayed(totalPriceLocator, Duration.ofSeconds(15));

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
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(btnSeleccionar));
        button.click();
    }

    /**
     * Verifies that the fare change pop-up exists in DOM and is visible on screen
     */
    public void popUpFareAppears() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        // Assert 1: Verify element exists in DOM (presence)
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(popUpChangeFare));
            System.out.println("✅ El Pop-up existe en el DOM");
        } catch (Exception e) {
            Assert.fail("❌ El Pop-up NO existe en el DOM");
        }

        // Assert 2: Verify element is actually visible on screen
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(popUpChangeFare));
            System.out.println("✅ El Pop-up es visible en pantalla");
        } catch (Exception e) {
            Assert.fail("❌ El Pop-up existe en el DOM pero NO es visible en pantalla");
        }
    }

    /**
     * Verifica que el enlace "Continuar con Básico" existe en el DOM y es visible en pantalla
     */
    public void linkContinueSameFareAppears() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        // Verificación 1: Presencia en DOM
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(linkContinueSameFare));
            System.out.println("✅ Enlace 'Continuar con Básico' existe en el DOM");
        } catch (Exception e) {
            Assert.fail("❌ Enlace 'Continuar con Básico' NO encontrado en el DOM");
        }

        // Verificación 2: Visibilidad en pantalla
        try {
            WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(linkContinueSameFare));
            Assert.assertTrue("El enlace debería estar visible", link.isDisplayed());
            Assert.assertEquals(link.getText().trim(), "No, quiero continuar con Básico");
            System.out.println("✅ El Enlace 'No, quiero continuar con Básico' es visible y tiene el texto correcto");
        } catch (Exception e) {
            Assert.fail("❌ El Enlace existe en DOM pero NO es visible o tiene texto incorrecto");
        }
    }

    /**
     * Click in the link to continue with the same fare
     */
    public void clickLinkContinueSameFare(){
        waitUntilElementIsDisplayed(linkContinueSameFare, Duration.ofSeconds(5));
        clickElement(linkContinueSameFare);
    }

}