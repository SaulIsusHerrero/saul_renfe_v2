package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class IntroduceYourDataPage extends BasePage {
    //Locators
    private By introduceTusDatosStepper = By.xpath("//li[contains(@class, 'active')]//span[contains(text(), 'Introduce tus datos')]");
    private By firstNameField = By.xpath("//input[@id='nombre0']");
    private By firstSurnameField = By.xpath("//input[@id='apellido10']");
    private By dniField = By.xpath("//input[@id='documento0']");
    private By secondSurnameField = By.xpath("//input[@id='apellido20']");
    private By telefonoField = By.xpath("//input[@id='telefono0']");
    private By emailField = By.xpath("//input[@id='email0']");
    private By totalPriceDataLocator = By.xpath("//span[@id='totalTrayecto']");
    private By btnPersonalizar = By.cssSelector("#btn-responsive > #submitpersonaliza");
    private By errorDNI = By.xpath("//div[@class='error-validacion' and @role='alert' and text()='El número de documento tiene un formato incorrecto']");

    //Constructor
    public IntroduceYourDataPage(WebDriver webDriver) {
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    //Methods
    /**
     * Assert that I am on the Page and that the 'Enter your data' section is enabled
     */
    public void verifyYouAreInIntroduceYourDataPage() {
        waitUntilElementIsDisplayed(totalPriceDataLocator, TIMEOUT);
        Assert.assertTrue(webDriver.findElement(introduceTusDatosStepper).isEnabled(), "Step disabled");
    }

    /**
     * Verify the ticket price.
     *
     * @param totalPriceTrip Price previously obtained, already normalized
     */
    public void verifyTotalPrice(String totalPriceTrip) {
    WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);
    WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(totalPriceDataLocator));
    String totalPriceData = normalizePrice(priceElement.getText());
    totalPriceTrip = normalizePrice(totalPriceTrip);
    Assert.assertEquals(totalPriceData, totalPriceTrip, "The display price does not concur with the expected one.");
}

    /**
     * Types the name in the textbox on the 'Introduce your data' page.
     *
     * @param firstName as a string
     */
    public void writeFirstNameField(String firstName) {
        waitUntilElementIsDisplayed(firstNameField, TIMEOUT);
        setElementText(firstNameField, firstName);
    }

    /**
     * Types the first surname in the textbox on the "Introduce your data" page.
     *
     * @param firstSurname as a string
     */
    public void writeFirstSurnameField(String firstSurname) {
        waitUntilElementIsDisplayed(firstSurnameField, TIMEOUT);
        setElementText(firstSurnameField, firstSurname);
    }

    /**
     * type the second surname in the textbox on the "Introduce your data" page.
     *
     * @param secondSurname as a string
     */
    public void writeSecondSurnameField(String secondSurname) {
        waitUntilElementIsDisplayed(secondSurnameField, TIMEOUT);
        setElementText(secondSurnameField, secondSurname);
    }

    /**
     * Types the DNI in the textbox on the "Introduce your data" page.
     *
     * @param dni as a string
     */
    public void writeDNIField(String dni) {
        waitUntilElementIsDisplayed(dniField, TIMEOUT);
        setElementText(dniField, dni);
    }

    /**
     * Types the E-mail in the textbox on the "Introduce your data" page.
     *
     * @param email as a string
     */
    public void writeEmailField(String email) {
        waitUntilElementIsDisplayed(emailField, TIMEOUT);
        setElementText(emailField, email);
    }

    /**
     * Types the Phone in the textbox on the "Introduce your data" page.
     *
     * @param phone as a string
     */
    public void writePhoneField(String phone) {
        waitUntilElementIsDisplayed(telefonoField, TIMEOUT);
        setElementText(telefonoField, phone);
    }

    public void checkErrorInDataField() {

        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT.plusSeconds(5)); // Increased timeout
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorDNI));

        // If appears, verify text and colour
        String text = error.getText().trim();
        Assert.assertEquals(text, "El número de documento tiene un formato incorrecto");

        String color = error.getCssValue("color");
        Color actual = Color.fromString(color);
        Color esperado = Color.fromString("#ff0000");
        Assert.assertEquals(actual, esperado, "The error message color should be red");
        Assert.assertFalse(!actual.equals(esperado), "The DNI field contains invalid data, therefore it is NOT possible to continue with the flow. Test FAILED");
    }

    /**
     * Clicks in Personalize trip to follow the flow.
     *
     */
    public void clickPersonalizeTrip() {
        scrollElementIntoView(btnPersonalizar);
        waitUntilElementIsDisplayed(btnPersonalizar, TIMEOUT);
        clickElement(btnPersonalizar);
    }

}