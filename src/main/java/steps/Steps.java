package steps;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.*;
import utils.TemporaryDataStore;

public class Steps extends BasePage {

    private final TemporaryDataStore dataStore;

    public Steps(WebDriver webDriver) {
        // Empty constructor
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
        this.dataStore = TemporaryDataStore.getInstance();
    }

      if ("InvalidDataTraveler15d".equalsIgnoreCase(testCase)) {
        TemporaryDataStore.getInstance().get("totalPriceTrip");
        homePage.selectDateDaysLater(webDriver, 15);
    } else if ("EmptyBuyerDataTest5d".equalsIgnoreCase(testCase)) {
        TemporaryDataStore.getInstance().get("totalPriceTrip");
        homePage.selectDateDaysLater(webDriver, 5);
    } else{
        System.out.println("El precio del trayecto no es 0");
    }

    /**
     * Select origin and destination stations,
     * @param originStation
     * @param destinationStation
     */
    public void performSearchOriginAndDestinationStation(String originStation, String destinationStation) {
        new HomePage(webDriver).clickAcceptAllCookiesButton();
        HomePage homePage = new HomePage(webDriver);
        homePage.enterOrigin(originStation);
        homePage.enterDestination(destinationStation);

    }

    /**
    * Select the current date as a departure date
    */
    public void selectDepartureDate() {
        HomePage homePage = new HomePage(webDriver);
        homePage.selectDepartureDate();
        homePage.clickSoloIdaButtonSelected(true);

        // Verificar usando la clave "testCase" si el trayecto son 5 o 15 días adelante
        String testCase = (String) TemporaryDataStore.getInstance().get("testCase");

        if ("InvalidDataTraveler15d".equalsIgnoreCase(testCase)) {
            TemporaryDataStore.getInstance().get("totalPriceTrip");
            homePage.selectDateDaysLater(webDriver, 15);
        } else if ("EmptyBuyerDataTest5d".equalsIgnoreCase(testCase)) {
            TemporaryDataStore.getInstance().get("totalPriceTrip");
            homePage.selectDateDaysLater(webDriver, 5);
        } else{
            System.out.println("El precio del trayecto no es 0");
        }

        homePage.clickAcceptButton();
        homePage.clickSearchTicketButton();
    }

    /**
     * Select train and basic fare
     */
    public void selectTrainAndFare(){
        new SeleccionarTuViajePage(webDriver).verifyYouAreInSelecionaTuViaje();
        new SeleccionarTuViajePage(webDriver).selectFirstValidBasicFareTrain();
    }

    /**
     * Gets the dynamic price of the page and stores the price in TemporaryDataStore
     */
    public void getAndStoreDynamicPrice() {
        SeleccionarTuViajePage seleccionarTuViajePage = new SeleccionarTuViajePage(webDriver);
        // Obtiene el precio dinámico de la página
        String totalPriceTrip = seleccionarTuViajePage.verifyFareAndTotalPricesAreEquals();
        // Almacena el precio en TemporaryDataStore
        TemporaryDataStore.getInstance().set("totalPriceTrip", totalPriceTrip);
    }

    /**
     * Verifications of: traveler/s, fare, price and click
     */
    public void verifyAndConfirmTravel(){
        new SeleccionarTuViajePage(webDriver).verifyNumberOfTravelers();
        new SeleccionarTuViajePage(webDriver).verifyFareIsBasic();
        new SeleccionarTuViajePage(webDriver).verifyFareAndTotalPricesAreEquals();
        new SeleccionarTuViajePage(webDriver).clickSelectButton();
    }

    /**
     * Verify the Pop-up and link to continue with the same fare appear, and click continue with the same fare
     */
    public void clickPopUpAndLinkAppear(){
        SeleccionarTuViajePage seleccionarTuViajePage = new SeleccionarTuViajePage(webDriver);
        new SeleccionarTuViajePage(webDriver).verifyElementPresenceAndVisibilityPopUpChangeFare(seleccionarTuViajePage.popUpChangeFare, "El PopUp cambio de tarifa está presente y visible");
        new SeleccionarTuViajePage(webDriver).linkPopUpFareAppears();
        new SeleccionarTuViajePage(webDriver).clickLinkContinueSameFare();
    }

    /**
     * Verify the price in the Page, must the same in all the process
     */
    public void verifyPriceIsEqualInData(){
        IntroduceTusDatosPage introduceTusDatosPage = new IntroduceTusDatosPage(webDriver);
        introduceTusDatosPage.verifyTotalPrice((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
    }

    /**
     * Fills in and confirms the traveler's personal data form.
     *
     * This method interacts with the "Introduce Your Data" page. It first verifies that the user
     * is on the correct page, then fills in the traveler's first name, surnames, ID (DNI),
     * email, and phone number. Finally, it clicks the button to continue personalizing the trip.
     *
     * @param firstName       The traveler's first name.
     * @param primerApellido  The traveler's first surname.
     * @param segundoApellido The traveler's second surname.
     * @param dni             The traveler's national ID (DNI).
     * @param email           The traveler's email address.
     * @param phone           The traveler's phone number.
     */
    public void introduceYourDataAndConfirm(String firstName,String primerApellido,String segundoApellido,String dni,String email,String phone){
        IntroduceTusDatosPage introduceTusDatosPage = new IntroduceTusDatosPage(webDriver);
        introduceTusDatosPage.verifyYouAreInIntroduceYourDataPage();
        introduceTusDatosPage.writeFirstNameField(firstName);
        introduceTusDatosPage.writeFirstSurnameField(primerApellido);
        introduceTusDatosPage.writeSecondSurnameField(segundoApellido);
        introduceTusDatosPage.writeDNIField(dni);
        introduceTusDatosPage.writeEmailField(email);
        introduceTusDatosPage.writePhoneField(phone);

        String testCase = (String) TemporaryDataStore.getInstance().get("testCase");

        if ("InvalidDataTraveler15d".equalsIgnoreCase(testCase)) {
            introduceTusDatosPage.checkErrorInDataField();
        }else {
            introduceTusDatosPage.clickPersonalizeTrip();
        }
    }

    /**
     * Verify the price in the Page, must the same in all the process
     */
    public void verifyPriceIsEqualInPersonalize(){
        PersonalizaTuViajePage personalizaTuViajePage = new PersonalizaTuViajePage(webDriver);
        personalizaTuViajePage.verifyTotalPrice((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
    }

    /**
     * Personalize the travel
     */
    public void confirmPersonalization(){
        new PersonalizaTuViajePage(webDriver).verifyYouAreInPersonalizeYourTravelPage();
        new PersonalizaTuViajePage(webDriver).continueWithPurchase();
    }

    /**
     * Verify the price in the Page, must the same in all the process
     */
    public void verifyPriceIsEqualInCompra(){
        CompraPage compraPage = new CompraPage(webDriver);
        compraPage.verifyTotalPrice((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
    }

    /**
     * Completes and confirms the payment data form on the purchase page.
     *
     * This method verifies that the user is on the purchase page, then enters the buyer's
     * email and phone number. It proceeds to simulate selecting a new payment card,
     * accepting the purchase conditions, and clicking the button to continue the purchase process.
     *
     * @param emailBuyer  The buyer's email address.
     * @param phoneBuyer  The buyer's phone number.
     */
    public void confirmPaymentData(String emailBuyer, String phoneBuyer){
        new CompraPage(webDriver).verifyYouAreInCompraPage();
        CompraPage compraPage = new CompraPage(webDriver);
        compraPage.typeEmail(emailBuyer);
        compraPage.writePhoneField(phoneBuyer);
        compraPage.clickPurchaseCard();
        compraPage.clickNewCard();
        compraPage.clickPurchaseCondition();
        compraPage.clickContinuarCompra();
    }

    /**
     * Enters and submits the payment information on the payment gateway page.
     *
     * This method verifies that the user is on the payment gateway page, checks the total price
     * against stored temporary data, and fills in the payment form with the card number, expiration date,
     * and CVV code. Finally, it clicks the button to complete the payment process.
     *
     * @param bankCard        The credit/debit card number.
     * @param expirationDate  The card's expiration date (MM/YY format).
     * @param cvv             The card's CVV security code.
     */
    public void payment(String bankCard, String expirationDate, String cvv){
        new PasarelaPagoPage(webDriver).verifyYouAreInPasarelaPagoPage();
        PasarelaPagoPage pasarelaPagoPage = new PasarelaPagoPage(webDriver);
        pasarelaPagoPage.verifyTotalPrice((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
        pasarelaPagoPage.typeBankCard(bankCard);
        pasarelaPagoPage.typeExpirationDate(expirationDate);
        pasarelaPagoPage.typeCVV(cvv);
        //pasarelaPagoPage.clickPaymentButton();
        String testCase = (String) TemporaryDataStore.getInstance().get("testCase");

        if ("InvalidCardPaymentTest".equalsIgnoreCase(testCase)) {
            pasarelaPagoPage.checkEnabledButton();
            pasarelaPagoPage.clickButtonPagar();
            pasarelaPagoPage.checkPoupUpError();
        }else if( "EmptyBuyerDataTest5d".equalsIgnoreCase(testCase)) {
            pasarelaPagoPage.checkDisabledButton();
        }


    }

    public PersonalizaTuasdfasdfViajePage(WebDriver webDriver) {
        super(webDriver); //Calls to the constructor from parent class and their variable
        this.webDriver = webDriver; //Current instance
    }

    //Methods
    /**
     * Assert that I am on the right page and is enabled “Personaliza tu viaje” page
     */
    public void verifyYouAreInPersonalizeasdfasdfYourTravelPage() {
        waitUntilElementIsDisplayed(personaasdflizaTuViajeStepper, TIMEOUT);
        Assert.assertTrue(webDriver.findElement(persfffonalizaTuViajeStepper).isEnabled(), "No está hablitado este step");
    }

    public void selectDepartureDate3() {
        HomePage homePage = new HomePage(webDriver);
        homePage.selectDepartureDate();
        homePage.clickSoloIdaButtonSelected(true);

        // Verificar usando la clave "testCase" si el trayecto son 5 o 15 días adelante
        String testCase = (String) TemporaryDataStore.getInstance().get("testCase");

        if ("InvalidDataTraveler15d".equalsIgnoreCase(testCase)) {
            TemporaryDataStore.getInstance().get("totalPriceTrip");
            homePage.selectDateDaysLater(webDriver, 15);
        } else if ("EmptyBuyerDataTest5d".equalsIgnoreCase(testCase)) {
            TemporaryDataStore.getInstance().get("totalPriceTrip");
            homePage.selectDateDaysLater(webDriver, 5);
        } else{
            System.out.println("El precio del trayecto no es 0");
        }

        homePage.clickAcceptButton();
        homePage.clickSearchTicketButton();
    }

}