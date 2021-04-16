package net.dajman.rentalcar.ui.controller.controllers;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.basic.Car;
import net.dajman.rentalcar.basic.Client;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.ui.controller.Controller;
import net.dajman.rentalcar.utils.TimeUtil;


public class CarController extends Controller {

    private Car car;
    private Client openOnBack;

    @FXML
    private Label brandLabel;
    @FXML
    private Label modelLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label clientLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private ImageView imageView;


    public CarController(){
        App.getInstance().addController(this);
    }

    @Override
    public NodeType getType() {
        return NodeType.CAR;
    }


    @Override
    protected void firstInitialize() {
        final ReadOnlyDoubleWrapper sizeProperty = new ReadOnlyDoubleWrapper();
        final GridPane gridPane = (GridPane)this.imageView.getParent().getParent();
        sizeProperty.set(Math.min(gridPane.heightProperty().doubleValue() * 0.7, gridPane.widthProperty().doubleValue()));
        gridPane.widthProperty().addListener((obs, oldValue, newValue) -> sizeProperty.set(Math.min(newValue.doubleValue(), gridPane.heightProperty().doubleValue() * 0.7)));
        gridPane.heightProperty().addListener((obs, oldValue, newValue) ->  sizeProperty.set(Math.min(newValue.doubleValue() * 0.7, gridPane.widthProperty().doubleValue())));
        this.imageView.fitWidthProperty().bind(sizeProperty);
        this.imageView.fitHeightProperty().bind(sizeProperty);
    }

    @Override
    public void initialize(final Object... objects) {
        if (objects.length == 2){
            this.openOnBack = (Client) objects[1];
        }
        this.car = (Car)objects[0];
        if (this.car == null){
            this.onClickBack(null);
            return;
        }
        this.brandLabel.setText(car.getBrand());
        this.modelLabel.setText(car.getModel());
        this.priceLabel.setText(car.getPrice() + " zl/godz.");
        this.imageView.setImage(car.getImage() == null ? App.getImageEmpty() : car.getImage());
        if (car.isRented()){
            final Client client = car.getClient();
            this.statusLabel.setText("wypożyczony");
            this.clientLabel.setText(client.getFirstName() + " " + client.getLastName());
            this.dateLabel.setText(TimeUtil.getDate(car.getRentalDate()) + " (Upłynęło: " + TimeUtil.getDurationBreakdown(System.currentTimeMillis() - car.getRentalDate()) + " )");
            return;
        }
        this.statusLabel.setText("dostępny");
        this.clientLabel.setText(" - ");
        this.dateLabel.setText(" - ");
    }


    @FXML
    public void onClickBack(final ActionEvent event){
        App.getInstance().openBack();
    }

    @FXML
    public void onClickEdit(final ActionEvent event){
        App.getInstance().openGui(NodeType.CAR_EDIT, this.car);
    }


    @FXML
    public void onClickDelete(final ActionEvent event){
        if (this.car != null){
            if (this.car.isRented()){
                this.car.getClient().removeRentedCar(this.car);
            }
            App.getInstance().getCarStorage().remove(this.car);
        }
        this.onClickBack(null);
    }





}
