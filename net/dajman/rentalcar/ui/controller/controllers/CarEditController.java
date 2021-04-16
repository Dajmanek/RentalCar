package net.dajman.rentalcar.ui.controller.controllers;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.basic.Car;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.ui.builder.alert.AlertBuilder;
import net.dajman.rentalcar.ui.controller.Controller;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


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
    }

    @Override
    public void initialize(final Object... objects) {
        this.imageBytes = null;
        if (objects.length != 1){
            this.car = null;
            this.imageView.setImage(App.getImageEmpty());
            this.brandField.setText("");
            this.modelField.setText("");
            this.priceField.setText("");
            return;
        }
        this.car = (Car) objects[0];
        this.brandField.setText(car.getBrand());
        this.modelField.setText(car.getModel());
        this.priceField.setText(car.getPrice() + "");
        this.imageView.setImage(car.getImage() != null ? car.getImage() : App.getImageEmpty());
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
            new AlertBuilder(Alert.AlertType.ERROR, "Plik", "Wybrano niepoprawny plik.").buildAndShow();
            return;
        }
        try{
            final BufferedImage bufferedImage = ImageIO.read(file);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            this.imageBytes = baos.toByteArray();
        }catch (IOException | IllegalArgumentException e){
            e.printStackTrace();
            new AlertBuilder(Alert.AlertType.ERROR, "Plik", "Wystapil problem z odczytem pliku.").buildAndShow();
            return;
        }
        this.imageView.setImage(new Image(new ByteArrayInputStream(this.imageBytes)));
    }

    public void onClickSave(ActionEvent event) {
        final float price;
        try{
             price = Float.parseFloat(this.priceField.getText());
            if (price < 0.01){
                new AlertBuilder(Alert.AlertType.ERROR, "Błąd", "Cena nie może być mniejsza niż 0.01").buildAndShow();
                return;
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
            new AlertBuilder(Alert.AlertType.ERROR, "Błąd", "Wpisana wartość cena jest niepoprawna.").buildAndShow();
            return;
        }
        boolean newCar = this.car == null;
        if (newCar){
            newCar = true;
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
