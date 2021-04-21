package net.dajman.rentalcar.ui.controller.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.basic.Client;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.ui.builder.alert.AlertBuilder;
import net.dajman.rentalcar.ui.controller.Controller;

import java.util.Optional;

public class ClientEditController extends Controller {


    private Client client;

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField postCode;
    private String postCodeValue;
    @FXML
    private TextField city;
    @FXML
    private TextField street;
    @FXML
    private TextField buildingNumber;
    @FXML
    private TextField flatNumber;
    int i = 0;

    public ClientEditController(){
        App.getInstance().addController(this);
        this.postCodeValue = "";
    }

    @Override
    public NodeType getType() {
        return NodeType.CLIENT_EDIT;
    }

    @Override
    protected void firstInitialize() {
        this.postCode.textProperty().addListener((obs, oldValue, newValue) -> {
            if (this.postCodeValue.equals(newValue)){
                return;
            }
            String text = oldValue;
            if (newValue.matches("[0-9]*-*[0-9]*")){
                text = newValue.replace("-", "");
                text = text.length() > 2 ? text.substring(0, 2) + '-' + text.substring(2, Math.min(5, text.length())) : text;
            }
            final String finalText = text;
            this.postCodeValue = finalText;
            Platform.runLater(() -> {
                this.postCode.setText(finalText);
                this.postCode.positionCaret(this.postCode.getText().length());
            });
        });
    }

    @Override
    public void initialize(Object... objects) {
        if (objects.length != 1){
            this.client = null;
            this.firstName.setText("");
            this.lastName.setText("");
            this.phoneNumber.setText("");
            this.postCode.setText("");
            this.city.setText("");
            this.street.setText("");
            this.buildingNumber.setText("");
            this.flatNumber.setText("");
            return;
        }
        this.client = (Client) objects[0];
        this.firstName.setText(this.client.getFirstName());
        this.lastName.setText(this.client.getLastName());
        this.phoneNumber.setText(this.client.getPhoneNumber());
        this.postCode.setText(this.client.getPostCode());
        this.city.setText(this.client.getCity());
        this.street.setText(Optional.ofNullable(this.client.getStreet()).orElse(""));
        this.buildingNumber.setText(Integer.toString(this.client.getBuildingNumber()));
        this.flatNumber.setText(this.client.getFlatNumber() != -1 ? Integer.toString(this.client.getFlatNumber()) : "");
    }

    @FXML
    public void onClickBack(ActionEvent event){
        App.getInstance().openBack();
    }

    @FXML
    public void onClickSave(ActionEvent event){
        final String firstName = this.firstName.getText();
        if (!this.emptyValidate(firstName, "Imię")){
            return;
        }
        final String lastName = this.lastName.getText();
        if (!this.emptyValidate(lastName, "Nazwisko")){
            return;
        }
        final String phoneNumber = this.phoneNumber.getText();
        if (!this.emptyValidate(phoneNumber, "Nr telefonu")){
            return;
        }
        if (!phoneNumber.matches("[0-9]+")){
            new AlertBuilder(Alert.AlertType.ERROR, "Pole \"Nr telefonu\" może składać się tylko z liczb.").buildAndShow();
            return;
        }
        final String postCode = this.postCode.getText();
        if (!this.emptyValidate(postCode, "Kod pocztowy")){
            return;
        }
        if (postCode.length() != 6 || postCode.charAt(2) != '-'){
            new AlertBuilder(Alert.AlertType.ERROR, "Pole \"Kod pocztowy\" zostało błędnie wypełnione.");
            return;
        }
        final String city = this.city.getText();
        if (!this.emptyValidate(city, "Miasto")){
            return;
        }
        final String streetText = this.street.getText();
        final String street = streetText.isBlank() ? null : streetText;
        final String buildingNumberText = this.buildingNumber.getText();
        if (!this.emptyValidate(buildingNumberText, "Nr budynku")){
            return;
        }
        final int buildingNumber;
        try{
            buildingNumber = Integer.parseInt(buildingNumberText);
        }catch (NumberFormatException e){
            new AlertBuilder(Alert.AlertType.ERROR,  "Pole \"Nr budynku\" musi być liczbą.").buildAndShow();
            e.printStackTrace();
            return;
        }
        int flatNumber = -1;
        if (!this.flatNumber.getText().isBlank()){
            try{
                flatNumber = Integer.parseInt(this.flatNumber.getText());
            }catch (NumberFormatException e){
                new AlertBuilder(Alert.AlertType.ERROR,  "Pole \"Nr mieszkania\" musi być liczbą.").buildAndShow();
                e.printStackTrace();
                return;
            }
        }
        final boolean newClient = this.client == null;
        if (newClient){
            this.client = new Client();
            App.getInstance().getClientStorage().add(this.client);
        }
        this.client.setFirstName(firstName);
        this.client.setLastName(lastName);
        this.client.setPhoneNumber(phoneNumber);
        this.client.setPostCode(postCode);
        this.client.setCity(city);
        this.client.setStreet(street);
        this.client.setBuildingNumber(buildingNumber);
        this.client.setFlatNumber(flatNumber);
        this.onClickBack(null);
        if (newClient) App.getInstance().openGui(NodeType.CLIENT, this.client);
    }

    private boolean emptyValidate(final String text, final String fieldName){
        if (!text.isBlank()){
            return true;
        }
        new AlertBuilder(Alert.AlertType.ERROR, "Pole \"" + fieldName + "\" nie może być puste.").buildAndShow();
        return false;
    }
}
