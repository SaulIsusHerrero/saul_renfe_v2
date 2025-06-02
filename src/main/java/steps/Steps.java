package steps;

import org.openqa.selenium.WebDriver;
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

    public void performSearchOriginAndDestinationStation(String originStation, String destinationStation) {
        new HomePage(webDriver).clickAcceptAllCookiesButton();
        HomePage homePage = new HomePage(webDriver);
        homePage.enterOrigin(originStation);
        homePage.enterDestination(destinationStation);

    }

    /**
     * 
     */
    public void selectDepartureDate() {
        HomePage homePage = new HomePage(webDriver);
        homePage.selectDepartureDate();
        homePage.clickSoloIdaButtonSelected(true);

        // Verificar usando la clave "testCase"
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

    public void selectTrainAndFare(){
        new SeleccionarTuViajePage(webDriver).verifyYouAreInSelecionaTuViaje();
        new SeleccionarTuViajePage(webDriver).selectFirstValidBasicFareTrain();
    }

    public void getAndStoreDynamicPrice() {
        SeleccionarTuViajePage seleccionarTuViajePage = new SeleccionarTuViajePage(webDriver);
        // Obtiene el precio dinámico de la página
        String totalPriceTrip = seleccionarTuViajePage.verifyFareAndTotalPricesAreEquals();
        // Almacena el precio en TemporaryDataStore
        TemporaryDataStore.getInstance().set("totalPriceTrip", totalPriceTrip);
    }

    public void verifyAndConfirmTravel(){
        new SeleccionarTuViajePage(webDriver).verifyNumberOfTravelers();
        new SeleccionarTuViajePage(webDriver).verifyFareIsBasic();
        new SeleccionarTuViajePage(webDriver).verifyFareAndTotalPricesAreEquals();
        new SeleccionarTuViajePage(webDriver).clickSelectButton();
    }

    public void clickPopUpAndLinkAppear(){
        SeleccionarTuViajePage seleccionarTuViajePage = new SeleccionarTuViajePage(webDriver);
        new SeleccionarTuViajePage(webDriver).verifyElementPresenceAndVisibilityPopUpChangeFare(seleccionarTuViajePage.popUpChangeFare, "El PopUp cambio de tarifa está presente y visible");
        new SeleccionarTuViajePage(webDriver).linkPopUpFareAppears();
        new SeleccionarTuViajePage(webDriver).clickLinkContinueSameFare();
    }

    public void verifyPriceIsEqualInData(){
        IntroduceTusDatosPage introduceTusDatosPage = new IntroduceTusDatosPage(webDriver);
        introduceTusDatosPage.verifyTotalPrice((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
    }

    public void introduceYourDataAndConfirm(String firstName,String primerApellido,String segundoApellido,String dni,String email,String phone){
        IntroduceTusDatosPage introduceTusDatosPage = new IntroduceTusDatosPage(webDriver);
        introduceTusDatosPage.verifyYouAreInIntroduceYourDataPage();
        introduceTusDatosPage.writeFirstNameField(firstName);
        introduceTusDatosPage.writeFirstSurnameField(primerApellido);
        introduceTusDatosPage.writeSecondSurnameField(segundoApellido);
        introduceTusDatosPage.writeDNIField(dni);
        introduceTusDatosPage.writeEmailField(email);
        introduceTusDatosPage.writePhoneField(phone);
        introduceTusDatosPage.clickPersonalizeTrip();
    }

    public void verifyPriceIsEqualInPersonalize(){
        PersonalizaTuViajePage personalizaTuViajePage = new PersonalizaTuViajePage(webDriver);
        personalizaTuViajePage.verifyTotalPrice((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
    }

    public void confirmPersonalization(){
        new PersonalizaTuViajePage(webDriver).verifyYouAreInPersonalizeYourTravelPage();
        new PersonalizaTuViajePage(webDriver).continueWithPurchase();
    }

    public void verifyPriceIsEqualInCompra(){
        CompraPage compraPage = new CompraPage(webDriver);
        compraPage.verifyTotalPrice((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
    }

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

    public void payment(String bankCard, String expirationDate, String cvv){
        new PasarelaPagoPage(webDriver).verifyYouAreInPasarelaPagoPage();
        PasarelaPagoPage pasarelaPagoPage = new PasarelaPagoPage(webDriver);
        pasarelaPagoPage.verifyTotalPrice((String) TemporaryDataStore.getInstance().get("totalPriceTrip"));
        pasarelaPagoPage.typeBankCard(bankCard);
        pasarelaPagoPage.typeExpirationDate(expirationDate);
        pasarelaPagoPage.typeCVV(cvv);
        pasarelaPagoPage.clickPaymentButton();
    }

}