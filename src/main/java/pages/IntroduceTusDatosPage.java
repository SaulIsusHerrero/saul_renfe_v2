package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.time.Duration;

public class IntroduceTusDatosPage extends BasePage {
    //Locators
    private By introduceTusDatosLabel = By.xpath("//span[contains(text(), 'Introduce tus datos') and not(ancestor::select[@disabled])]");
    private By firstNameField = By.xpath("//input[@id='nombre0']");
    private By firstSurnameField = By.xpath("//input[@id='apellido10']");
    private By secondSurnameField = By.xpath("//input[@id='apellido20']");
    private By dniField = By.xpath("//input[@id='documento0']");
    private By telefonoField = By.xpath("//input[@id='telefono0']");
    private By emailField = By.xpath("//input[@id='email0']");
    private By totalPriceDataLocator = By.xpath("//span[@id='totalTrayecto']");
    private By btnPersonalizar = By.cssSelector("#btn-responsive > #submitpersonaliza");

    //Instance
    private SeleccionarTuViajePage seleccionarTuViajePage;

    //Constructor
    public IntroduceTusDatosPage(WebDriver webDriver) {
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    //Methods
    /**
     * Assert que estoy en la Page y esta habilitada “introduce tus datos”
     */
    public void verifyYouAreInIntroduceYourDataPage() {
        //@todo investigar como se comprueba el texto, el tipo de assert que necesitas
        //@todo Saúl. otro todo sin hacer
        waitUntilElementIsDisplayed(introduceTusDatosLabel, Duration.ofSeconds(5));
        Assert.assertTrue(webDriver.findElement(introduceTusDatosLabel).isEnabled());
    }

    /**
     * type the name in the textbox on the "Introduce tus datos" page.
     *
     * @param firstName as a string
     */
    public void writeFirstNameField(String firstName) {
        waitUntilElementIsDisplayed(firstNameField, Duration.ofSeconds(5));
        setElementText(firstNameField, firstName);
    }

    /**
     * type the first surname in the textbox on the "Introduce tus datos" page.
     *
     * @param primerApellido as a string
     */
    public void writeFirstSurnameField(String primerApellido) {
        waitUntilElementIsDisplayed(firstSurnameField, Duration.ofSeconds(5));
        setElementText(firstSurnameField, primerApellido);
    }

    /**
     * type the second surname in the textbox on the "Introduce tus datos" page.
     *
     * @param segundoApellido as a string
     */
    public void writeSecondSurnameField(String segundoApellido) {
        waitUntilElementIsDisplayed(secondSurnameField, Duration.ofSeconds(5));
        setElementText(secondSurnameField, segundoApellido);
    }

    /**
     * type the DNI in the textbox on the "Introduce tus datos" page.
     *
     * @param dni as a string
     */
    public void writeDNIField(String dni) {
        waitUntilElementIsDisplayed(dniField, Duration.ofSeconds(5));
        setElementText(dniField, dni);
    }

    /**
     * type the E-mail in the textbox on the "Introduce tus datos" page.
     *
     * @param email as a string
     */
    public void writeEmailField(String email) {
        waitUntilElementIsDisplayed(emailField, Duration.ofSeconds(5));
        setElementText(emailField, email);
    }

    /**
     * type the Phone in the textbox on the "Introduce tus datos" page.
     *
     * @param phone as a string
     */
    public void writePhoneField(String phone) {
        waitUntilElementIsDisplayed(telefonoField, Duration.ofSeconds(5));
        setElementText(telefonoField, phone);
    }

    /**
     * Verify the ticket price.
     * @param totalPriceTrip as a String
     */
    public String verifyTotalPriceData(String totalPriceTrip){
        waitUntilElementIsDisplayed(totalPriceDataLocator, Duration.ofSeconds(5));
        //@todo comprobar el precio, no la disponibilidad. los precios se comprueban con el getText
        String totalPriceData = webDriver.findElement(totalPriceDataLocator).getText().trim().replaceAll("\\s+", "");
        //@todo verificar que el precio es el mismo que en la pagina anterior. Sigue siendo el mismo desde el inicio
        Assert.assertEquals(totalPriceData, totalPriceTrip);
        return totalPriceTrip;
    }

    /**
     * Clic "Personalizar viaje" button
     */
    public void clickPersonalizeTrip() {
        waitUntilElementIsDisplayed(btnPersonalizar, Duration.ofSeconds(5));
        clickElement(btnPersonalizar);
    }

}