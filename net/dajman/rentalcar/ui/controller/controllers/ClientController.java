package net.dajman.rentalcar.ui.controller.controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.basic.Car;
import net.dajman.rentalcar.basic.Client;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.ui.builder.list.CarListBuilder;
import net.dajman.rentalcar.ui.controller.Controller;
import net.dajman.rentalcar.ui.utils.Images;

import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ClientController extends Controller {

    private Client client;

    private transient Task<Node> searchTask;

    @FXML
    private TextField searchField;
    @FXML
    private ImageView loadingGif;
    @FXML
    private TabPane tabs;
    @FXML
    private ScrollPane rentedCars;
    @FXML
    private ScrollPane availableCars;
    @FXML
    private Label firstName;
    @FXML
    private Label lastName;
    @FXML
    private Label phoneNumber;
    @FXML
    private Label address1;
    @FXML
    private Label address2;
    @FXML
    private Label numberOfRentedCars;
    @FXML
    private Label toPay;


    public ClientController(){
        App.getInstance().addController(this);
    }

    @Override
    public NodeType getType() {
        return NodeType.CLIENT;
    }

    @Override
    protected void firstInitialize() {
        this.tabs.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> this.onTabChange(oldValue.intValue(), newValue.intValue()));
        this.searchField.textProperty().addListener(obs -> this.onClickSearch(null));
    }

    @Override
    public void initialize(Object... objects) {
        this.client = (Client) objects[0];
        this.firstName.setText(this.client.getFirstName());
        this.lastName.setText(this.client.getLastName());
        this.phoneNumber.setText(this.client.getPhoneNumber());
        if (this.client.getStreet() == null){
            this.address1.setText(this.client.getPostCode() + ", " + this.client.getCity() + " " + this.client.getBuildingNumber() + (this.client.getFlatNumber() == -1 ? "" : "/" + this.client.getFlatNumber()));
            this.address2.setText("");
            this.address2.setPrefHeight(0);
        }else{
            this.address1.setText(this.client.getPostCode() + ", " + this.client.getCity());
            this.address2.setText("ul. " + this.client.getStreet() + " " + this.client.getBuildingNumber() + (this.client.getFlatNumber() == -1 ? "" : "/" + this.client.getFlatNumber()));
            this.address2.setPrefHeight(20);
        }
        this.refreshData();
        this.onClickSearch(null);
    }

    @FXML
    public void onClickBack(final ActionEvent event){
        App.getInstance().openBack();
    }

    @FXML
    public void onClickEdit(final ActionEvent event){
        if (this.client == null){
            return;
        }
        App.getInstance().openGui(NodeType.CLIENT_EDIT, this.client);
    }

    @FXML
    public void onClickDelete(final ActionEvent event){
        if (this.client != null){
            this.client.getRentedCars().forEach(car -> {
                car.setClient(null);
                car.setRentalDate(-1);
            });
            App.getInstance().getClientStorage().remove(this.client);
        }
        this.onClickBack(null);
    }


    public void cancelSearching(){
        if (this.searchTask == null || !this.searchTask.isRunning()){
            return;
        }
        this.searchTask.cancel();
    }

    @FXML
    public void onClickSearch(final ActionEvent event){
        this.cancelSearching();
        this.showLoadingGif();

        final int tabIndex = this.tabs.getSelectionModel().getSelectedIndex();
        final ScrollPane scrollPane = tabIndex == 0 ? this.rentedCars : this.availableCars;
        final String searchText = this.searchField.getText();

        this.searchTask = new Task<Node>() {
            @Override
            protected Node call() throws Exception {
                return switch (tabIndex){
                    case 0 -> new CarListBuilder(CarListBuilder.ListType.CLIENT_RENTED, client.getRentedCars().parallelStream().filter(car -> car.isSimilar(searchText)).collect(Collectors.toSet())).build();
                    default -> new CarListBuilder(CarListBuilder.ListType.CLIENT_AVAILABLE, App.getInstance().getCarStorage().getAll().parallelStream().filter(Car::isAvailable).filter(car -> car.isSimilar(searchText)).collect(Collectors.toSet())).build();
                };
            }
        };
        this.searchTask.setOnSucceeded(workerStateEvent -> {
            Node node = null;
            try{
                node = searchTask.get();
            }catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
            scrollPane.setContent(node);
            this.hideLoadingGif();
        });
        this.searchTask.setOnFailed(workerStateEvent -> {
            scrollPane.setContent(null);
            this.hideLoadingGif();
        });
        this.searchTask.setOnCancelled(workerStateEvent -> this.hideLoadingGif());
        new Thread(this.searchTask).start();
    }


    public void returnCar(final Car car){
        this.client.removeRentedCar(car);
        car.setRentalDate(0);
        car.setClient(null);
        this.refreshData();
        this.onClickSearch(null);
    }

    public void rentCar(final Car car){
        if (car.getClient() != null){
            return;
        }
        car.setClient(this.client);
        car.setRentalDate(System.currentTimeMillis() - 1L);
        this.client.addRentedCar(car);
        this.refreshData();
        this.onClickSearch(null);
    }

    public void onTabChange(final int oldIndex, final int newIndex){
        this.searchField.setText("");
        this.onClickSearch(null);
    }

    private void refreshData(){
        this.numberOfRentedCars.setText(Integer.toString(this.client.getRentedCars().size()));
        this.toPay.setText(String.format("%.2f", this.client.toPay()) + " zł");
    }

    private void showLoadingGif(){
        this.loadingGif.setImage(Images.loadingGif);
    }

    private void hideLoadingGif(){
        this.loadingGif.setImage(null);
    }

}
