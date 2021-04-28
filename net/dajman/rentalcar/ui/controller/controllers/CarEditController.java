package net.dajman.rentalcar.ui.controller.controllers;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import net.dajman.rentalcar.App;
import net.dajman.rentalcar.basic.Car;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.ui.alert.Alert;
import net.dajman.rentalcar.ui.controller.Controller;
import net.dajman.rentalcar.ui.utils.Images;
import net.dajman.rentalcar.ui.utils.TextFields;


public class CarEditController extends Controller {

    private Car car;
    private byte[] imageBytes;

    @FXML
    private TextField brandField;
    @FXML
    private TextField modelField;
    @FXML
    private TextField priceField;
    @FXML
    private ImageView imageView;

    public CarEditController(){
        App.getInstance().addController(this);
    }

    @Override
    public NodeType getType() {
        return NodeType.CAR_EDIT;
    }

    @Override
    protected void firstInitialize() {
        final ReadOnlyDoubleWrapper sizeProperty = new ReadOnlyDoubleWrapper();
        final GridPane gridPane = (GridPane)this.imageView.getParent().getParent().getParent();
        sizeProperty.set(Math.min(gridPane.heightProperty().doubleValue() * 0.7, gridPane.widthProperty().doubleValue()));
        gridPane.widthProperty().addListener((obs, oldValue, newValue) -> sizeProperty.set(Math.min(newValue.doubleValue(), gridPane.heightProperty().doubleValue() * 0.7)));
        gridPane.heightProperty().addListener((obs, oldValue, newValue) ->  sizeProperty.set(Math.min(newValue.doubleValue() * 0.7, gridPane.widthProperty().doubleValue())));
        this.imageView.fitWidthProperty().bind(sizeProperty);
        this.imageView.fitHeightProperty().bind(sizeProperty);

        this.brandField.focusedProperty().addListener((obs) -> TextFields.setIncorrect(false, this.brandField));
        this.modelField.focusedProperty().addListener((obs) -> TextFields.setIncorrect(false, this.modelField));
        this.priceField.focusedProperty().addListener((obs) -> TextFields.setIncorrect(false, this.priceField));
    }

    @Override
    public void initialize(final Object... objects) {
        TextFields.setIncorrect(false, this.brandField, this.modelField, this.priceField);

        this.imageBytes = null;
        if (objects.length != 1){
            this.car = null;
            this.imageView.setImage(Images.imageEmpty);
            this.brandField.setText("");
            this.modelField.setText("");
            this.priceField.setText("");
            return;
        }
        this.car = (Car) objects[0];
        this.brandField.setText(car.getBrand());
        this.modelField.setText(car.getModel());
        this.priceField.setText(car.getPrice() + "");
        this.imageView.setImage(car.getImage() != null ? car.getImage() : Images.imageEmpty);
    }

    public void onClickChooseFile(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG/JPG Images", "*.png", "*.jpg"));
        final File file = fileChooser.showOpenDialog(App.getInstance().getStage());
        if (file == null){
            return;
        }
        final String fileExtension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
        if (!fileExtension.equalsIgnoreCase("png") && !fileExtension.equalsIgnoreCase("jpg")){
            new Alert("Wybrano niepoprawny plik.").show();
            return;
        }
        try{
            final BufferedImage bufferedImage = ImageIO.read(file);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            this.imageBytes = baos.toByteArray();
        }catch (IOException | IllegalArgumentException e){
            e.printStackTrace();
            new Alert("Wystapil problem z odczytem pliku.").show();
            return;
        }
        this.imageView.setImage(new Image(new ByteArrayInputStream(this.imageBytes)));
    }

    public void onClickSave(ActionEvent event) {
        if (TextFields.checkIfBlank(this.priceField, this.brandField, this.modelField)){
            return;
        }
        final float price;
        try{
             price = Float.parseFloat(this.priceField.getText());
            if (price < 0.01){
                new Alert("Cena nie może być mniejsza niż 0.01.").show();
                return;
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
            new Alert("Wpisana wartość cena jest niepoprawna.").show();
            return;
        }
        boolean newCar = this.car == null;
        if (newCar) {
            this.car = new Car();
            App.getInstance().getCarStorage().add(this.car);
        }
        this.car.setBrand(this.brandField.getText());
        this.car.setModel(this.modelField.getText());
        this.car.setPrice(price);
        if (this.imageBytes != null) this.car.setImageBytes(this.imageBytes);
        this.onClickBack(null);
        if (newCar) App.getInstance().openGui(NodeType.CAR, this.car);
    }

    public void onClickBack(ActionEvent event) {
        App.getInstance().openBack();
    }

}
