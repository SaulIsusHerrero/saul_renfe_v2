package pages;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SeleccionarTuViajePage extends BasePage {

    //Locators
    //@TODO cambiar el selector de tarifa y precio tarifa al de la semimodal
    private By seleccionaViajeLabel = By.xpath("//span[contains(text(), 'Selecciona tu viaje') and not(ancestor::select[@disabled])]");
    private By trainAvailable = By.cssSelector("div[id^='precio-viaje']:not(:has(div))");
    private By trainAvailableBasicFare = By.cssSelector("div[id^='precio-viaje']:not(:has(div))+div>div>div[class='planes-opciones']>div:nth-child(1)");
    private By selectDayRightArrow = By.cssSelector(".rescalendar_controls > button.move_to_tomorrow");
    private By travelerLocator = By.xpath("(//div[contains(@class, 'viajerosSelected') and contains(text(), '1')])[1]");
    private By basicFareLocator = By.xpath("(//div[@class='ida-slim' and @id='banneri']//div[@class='rowitem1 viajerosSelected']/span[contains(text(), 'Básico')])[1]");
    private By basicFarePriceLocator = By.xpath("(//div[@class='rowitem2 precioTarifa']/span)[1]");
    private By totalPriceLocator = By.xpath("(//span[@id='totalTrayectoBanner'])[1]");
    private By btnSeleccionar = By.xpath("(//div[@class='rowitem2']/button[@id='btnSeleccionar' and @title='Elegir el trayecto y pasar al siguiente paso'])[1]");
    private By popUpChangeFare = By.cssSelector("div.modal-content");
    private By linkContinueSameFare = By.xpath("//p[@id='aceptarConfirmacionFareUpgrade' and text()='No, quiero continuar con Básico']");
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
        //Saúl : hago un getText.
        waitUntilElementIsDisplayed(seleccionaViajeLabel, timeout);
        Assert.assertEquals("Selecciona tu viaje", webDriver.findElement(seleccionaViajeLabel).getText());
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
            WebDriverWait wait = new WebDriverWait(webDriver, timeout);

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
        waitUntilElementIsDisplayed(travelerLocator, timeout);
        Assert.assertTrue(webDriver.findElement(travelerLocator).getText().contains("1"));
    }

    /**
     * Verifies the fare applied for the trip in the semimodal
     */
    public void verifyFareIsBasic(){
        waitUntilElementIsDisplayed(basicFareLocator, timeout);
        Assert.assertTrue(webDriver.findElement(basicFareLocator).getText().contains("Básico"));
    }

    /**
     * Verifies that the fare and the total prices applied for the trip in the semimodal are equals
     */
    public String verifyFareAndTotalPricesAreEquals() {
        waitUntilElementIsDisplayed(basicFarePriceLocator, timeout);
        waitUntilElementIsDisplayed(totalPriceLocator, timeout);
        String basicFarePrice = webDriver.findElement(basicFarePriceLocator).getText().trim().replaceAll("\\s+", "");
        String totalPriceTrip = webDriver.findElement(totalPriceLocator).getText().trim().replaceAll("\\s+", "");
        Assert.assertEquals(basicFarePrice, totalPriceTrip);
        return totalPriceTrip;
    }

    /**
     * clicks the Seleccionar button in the semimodal
     */
    public void clickSelectButton(){
        waitUntilElementIsDisplayed(btnSeleccionar, timeout);
        clickElement(btnSeleccionar);
    }

    /**
     * Verifies that the pop-up to change the fare applied appears on the screen
     */
    //@todo usar el atributo style, donde comprobar si aparece  display:none o block. En el locator popUpChangeFare. La IA me dice que en un locator no se puede incluir el atributo sytle que esta en el ccs no en el html.
    //@todo Saúl -- como estas comprobando aqui que el pop up aparece? he cambiado el metodo.Con un try-catch que comprueba el style, si realmente es visible en pantalla.
    public void popUpFareAppears() {
        WebElement popUpElement = webDriver.findElement(popUpChangeFare);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        String displayStylePopup = (String) js.executeScript("return window.getComputedStyle(arguments[0]).display;", popUpElement);
        if (!"none".equals(displayStylePopup)) {
            System.out.println("✅ El Pop-up para un posible cambio de tarifa es visible en pantalla");
        } else {
            System.out.println("❌ El Pop-up para un posible cambio de tarifa NO es visible en pantalla");
        }
    }

    /**
    * Verifies that the link to follow with the same fare appears on the screen insde the pop-up and is enabled to click on it
    */
    //@todo Saúl - ¿recuerdas que hablamos de que aqui estas comprobando que esta disponible? Hablamos de comprobar con el texto en pantalla.
    //Recuerda que en esta web, el selector esta presente incluso si el pop up no lo está.
    //SAÚL : El Locator del popup está en pantalla (se ha comprobado antes que el popup aparece en pantalla en el metodo popUpFareAppears.Y el locator usado contiene el texto.
    public void linkContinueSameFareAppears(){
        waitUntilElementIsDisplayed(linkContinueSameFare, timeout);
        Assert.assertTrue("❌ El link para continuar con la misma tarifa no está disponible", webDriver.findElement(linkContinueSameFare).isEnabled());
    }

    /**
     * Click in the link to continue with the same fare
     */
    public void clickLinkContinueSameFare(){
        waitUntilElementIsDisplayed(linkContinueSameFare, timeout);
        clickElement(linkContinueSameFare);
    }

}