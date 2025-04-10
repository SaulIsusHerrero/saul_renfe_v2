package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IntroduceTusDatosPage extends BasePage {
    //Locators
    private By introduceDatosLabel = By.xpath("//span[contains(text(), 'Introduce tus datos') and not(ancestor::select[@disabled])]");
    private By firstNameField = By.xpath("//input[@id='nombre0']");
    private By firstSurnameField = By.xpath("//input[@id='apellido10']");
    private By secondSurnameField = By.xpath("//input[@id='apellido20']");
    private By dniField = By.xpath("//input[@id='documento0']");
    private By telefonoField = By.xpath("//input[@id='telefono0']");
    private By emailField = By.xpath("//input[@id='email0']");
    private By totalPriceDataLocator = By.xpath("//span[@id='totalTrayecto']");
    private By btnPersonalizar = By.cssSelector("#btn-responsive > #submitpersonaliza");
    private By totalPriceLocator = By.xpath("(//span[@id='totalTrayectoBanner'])[1]");


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
        //@todo Saúl. otro todo sin hacer. Saúl : hago un getText en el assert.
        waitUntilElementIsDisplayed(introduceDatosLabel, timeout);
        Assert.assertEquals("Introduce tus datos", webDriver.findElement(introduceDatosLabel).getText());
    }

    /**
     * type the name in the textbox on the "Introduce tus datos" page.
     *
     * @param firstName as a string
     */
    public void writeFirstNameField(String firstName) {
        waitUntilElementIsDisplayed(firstNameField, timeout);
        setElementText(firstNameField, firstName);
    }

    /**
     * type the first surname in the textbox on the "Introduce tus datos" page.
     *
     * @param primerApellido as a string
     */
    public void writeFirstSurnameField(String primerApellido) {
        waitUntilElementIsDisplayed(firstSurnameField, timeout);
        setElementText(firstSurnameField, primerApellido);
    }

    /**
     * type the second surname in the textbox on the "Introduce tus datos" page.
     *
     * @param segundoApellido as a string
     */
    public void writeSecondSurnameField(String segundoApellido) {
        waitUntilElementIsDisplayed(secondSurnameField, timeout);
        setElementText(secondSurnameField, segundoApellido);
    }

    /**
     * type the DNI in the textbox on the "Introduce tus datos" page.
     *
     * @param dni as a string
     */
    public void writeDNIField(String dni) {
        waitUntilElementIsDisplayed(dniField, timeout);
        setElementText(dniField, dni);
    }

    /**
     * type the E-mail in the textbox on the "Introduce tus datos" page.
     *
     * @param email as a string
     */
    public void writeEmailField(String email) {
        waitUntilElementIsDisplayed(emailField, timeout);
        setElementText(emailField, email);
    }

    /**
     * type the Phone in the textbox on the "Introduce tus datos" page.
     *
     * @param phone as a string
     */
    public void writePhoneField(String phone) {
        waitUntilElementIsDisplayed(telefonoField, timeout);
        setElementText(telefonoField, phone);
    }

    /**
     * Verify the ticket price.
     * @param totalPrice as a String
     */
    public String verifyTotalPriceData(String totalPrice){
        waitUntilElementIsDisplayed(totalPriceDataLocator, timeout);
        String totalPriceData = webDriver.findElement(totalPriceDataLocator).getText().trim().replaceAll("\\s+", "");
        String totalPriceTrip = webDriver.findElement(totalPriceDataLocator).getText().trim().replaceAll("\\s+", "");
        Assert.assertEquals(totalPriceData, totalPriceTrip);
        return totalPriceTrip;
    }

    /**
     * Click "Personalizar viaje" button
     */
    public void clickPersonalizeTrip() {
        waitUntilElementIsDisplayed(btnPersonalizar, timeout);
        clickElement(btnPersonalizar);
    }

}