package net.dajman.rentalcar.ui.controller.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.basic.Client;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.ui.alert.Alert;
import net.dajman.rentalcar.ui.controller.Controller;
import net.dajman.rentalcar.ui.utils.TextFields;

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
        this.phoneNumber.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.length() < 10){
                return;
            }
            Platform.runLater(() -> {
                this.phoneNumber.setText(newValue.substring(0, 9));
                this.phoneNumber.positionCaret(this.phoneNumber.getText().length());
            });
        });

        this.firstName.focusedProperty().addListener((obs) -> TextFields.setIncorrect(false, this.firstName));
        this.lastName.focusedProperty().addListener((obs) -> TextFields.setIncorrect(false, this.lastName));
        this.phoneNumber.focusedProperty().addListener((obs) -> TextFields.setIncorrect(false, this.phoneNumber));
        this.postCode.focusedProperty().addListener((obs) -> TextFields.setIncorrect(false, this.postCode));
        this.city.focusedProperty().addListener((obs) -> TextFields.setIncorrect(false, this.city));
        this.buildingNumber.focusedProperty().addListener((obs) -> TextFields.setIncorrect(false, this.buildingNumber));
    }

    @Override
    public void initialize(Object... objects) {
        TextFields.setIncorrect(false, this.firstName, this.lastName, this.phoneNumber, this.postCode, this.city, this.buildingNumber);
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
        if (TextFields.checkIfBlank(this.firstName, this.lastName, this.phoneNumber, this.postCode, this.city, this.buildingNumber)){
            return;
        }
        final String phoneNumber = this.phoneNumber.getText();
        if (!phoneNumber.matches("[0-9]+")){
            new Alert("Pole \"Nr telefonu\" zawiera niedozwolone znaki.").show();
            return;
        }
        if (phoneNumber.length() != 9){
            new Alert("Nr telefonu powinien skladac sie z 9 cyfr.").show();
            return;
        }
        final String postCode = this.postCode.getText();
        if (postCode.length() != 6 || postCode.charAt(2) != '-'){
            new Alert("Pole \"Kod pocztowy\" zostało błędnie wypełnione.").show();
            return;
        }
        final String streetText = this.street.getText();
        final String street = streetText.isBlank() ? null : streetText;
        final int buildingNumber;
        try{
            buildingNumber = Integer.parseInt(this.buildingNumber.getText());
        }catch (NumberFormatException e){
            new Alert("Pole \"Nr budynku\" zawiera niedozwolone znaki.").show();
            e.printStackTrace();
            return;
        }
        int flatNumber = -1;
        if (!this.flatNumber.getText().isBlank()){
            try{
                flatNumber = Integer.parseInt(this.flatNumber.getText());
            }catch (NumberFormatException e){
                new Alert("Pole \"Nr mieszkania\" zawiera niedozwolone znaki.").show();
                e.printStackTrace();
                return;
            }
        }
        if (this.client == null){
            this.client = new Client(firstName.getText(), lastName.getText(), phoneNumber, postCode, city.getText(), street, buildingNumber, flatNumber);
            this.onClickBack(null);
            App.getInstance().openGui(NodeType.CLIENT, this.client);
            return;
        }
        this.client.setFirstName(firstName.getText());
        this.client.setLastName(lastName.getText());
        this.client.setPhoneNumber(phoneNumber);
        this.client.setPostCode(postCode);
        this.client.setCity(city.getText());
        this.client.setStreet(street);
        this.client.setBuildingNumber(buildingNumber);
        this.client.setFlatNumber(flatNumber);
        this.onClickBack(null);
    }
}
