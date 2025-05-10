package pages;

import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IntroduceTusDatosPage extends BasePage {
    //Locators
    private By introduceTusDatosStepper = By.xpath("//ul[@class='stepper stepper-horizontal']//li[contains(@class, 'active')]//span[contains(text(), 'Introduce tus datos')]");
    private By firstNameField = By.xpath("//input[@id='nombre0']");
    private By firstSurnameField = By.xpath("//input[@id='apellido10']");
    private By secondSurnameField = By.xpath("//input[@id='apellido20']");
    private By dniField = By.xpath("//input[@id='documento0']");
    private By telefonoField = By.xpath("//input[@id='telefono0']");
    private By emailField = By.xpath("//input[@id='email0']");
    private By totalPriceDataLocator = By.xpath("//span[@id='totalTrayecto']");
    private By btnPersonalizar = By.cssSelector("#btn-responsive > #submitpersonaliza");

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
        waitUntilElementIsDisplayed(introduceTusDatosStepper, TIMEOUT);
        Assert.assertTrue(webDriver.findElement(introduceTusDatosStepper).isEnabled(),"No está habilitado este step");
    }

    /**
     * type the name in the textbox on the "Introduce tus datos" page.
     *
     * @param firstName as a string
     */
    public void writeFirstNameField(String firstName) {
        waitUntilElementIsDisplayed(firstNameField, TIMEOUT);
        setElementText(firstNameField, firstName);
    }

    /**
     * type the first surname in the textbox on the "Introduce tus datos" page.
     *
     * @param primerApellido as a string
     */
    public void writeFirstSurnameField(String primerApellido) {
        waitUntilElementIsDisplayed(firstSurnameField, TIMEOUT);
        setElementText(firstSurnameField, primerApellido);
    }

    /**
     * type the second surname in the textbox on the "Introduce tus datos" page.
     *
     * @param segundoApellido as a string
     */
    public void writeSecondSurnameField(String segundoApellido) {
        waitUntilElementIsDisplayed(secondSurnameField, TIMEOUT);
        setElementText(secondSurnameField, segundoApellido);
    }

    /**
     * type the DNI in the textbox on the "Introduce tus datos" page.
     *
     * @param dni as a string
     */
    public void writeDNIField(String dni) {
        waitUntilElementIsDisplayed(dniField, TIMEOUT);
        setElementText(dniField, dni);
    }

    /**
     * type the E-mail in the textbox on the "Introduce tus datos" page.
     *
     * @param email as a string
     */
    public void writeEmailField(String email) {
        waitUntilElementIsDisplayed(emailField, TIMEOUT);
        setElementText(emailField, email);
    }

    /**
     * type the Phone in the textbox on the "Introduce tus datos" page.
     *
     * @param phone as a string
     */
    public void writePhoneField(String phone) {
        waitUntilElementIsDisplayed(telefonoField, TIMEOUT);
        setElementText(telefonoField, phone);
    }

    /**
     * Verify the ticket price.
     * @param totalPriceTrip Precio obtenido previamente, ya normalizado
     */
    public void verifyTotalPriceData(String totalPriceTrip) {
        waitUntilElementIsDisplayed(totalPriceDataLocator, TIMEOUT);

        // Normaliza el precio de la nueva página
        String totalPriceData = normalizePrice(webDriver.findElement(totalPriceDataLocator).getText());

        // El precio recibido ya debería estar normalizado, pero por seguridad:
        totalPriceTrip = normalizePrice(totalPriceTrip);

        Assert.assertEquals(totalPriceData, totalPriceTrip);
    }

    /**
     * Clic "Personalizar viaje" button
     */
    public void clickPersonalizeTrip() {
        scrollElementIntoView(btnPersonalizar);
        waitUntilElementIsDisplayed(btnPersonalizar, TIMEOUT);
        clickElement(btnPersonalizar);
    }
}