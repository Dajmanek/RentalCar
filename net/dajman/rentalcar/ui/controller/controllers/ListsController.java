package net.dajman.rentalcar.ui.controller.controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.ui.builder.list.CarListBuilder;
import net.dajman.rentalcar.ui.builder.list.ClientListBuilder;
import net.dajman.rentalcar.ui.controller.Controller;
import net.dajman.rentalcar.ui.utils.Images;

import java.util.concurrent.ExecutionException;

public class ListsController extends Controller {


    private transient Task<Node> searchTask;

    @FXML
    private TextField searchField;
    @FXML
    private ImageView loadingGif;
    @FXML
    private TabPane tabs;
    @FXML
    private ScrollPane carsContainer;
    @FXML
    private ScrollPane clientsContainer;

    public ListsController(){
        App.getInstance().addController(this);
    }

    @Override
    public NodeType getType() {
        return NodeType.LISTS;
    }

    @Override
    protected void firstInitialize() {
        this.tabs.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> this.onTabChange(oldValue.intValue(), newValue.intValue()));
        this.searchField.textProperty().addListener(obs -> this.onClickSearch(null));

    }

    @Override
    protected void initialize(Object... objects) {
        this.onClickSearch(null);
    }

    public void cancelSearching(){
        if (this.searchTask == null || !this.searchTask.isRunning()){
            return;
        }
        this.searchTask.cancel();
    }

    @FXML
    public void onClickSearch(final ActionEvent e){
        this.cancelSearching();
        this.showLoadingGif();

        final int tabIndex = tabs.getSelectionModel().getSelectedIndex();
        final ScrollPane scrollPane = tabIndex == 0 ? this.carsContainer : this.clientsContainer;
        this.searchTask = new Task<>() {
            @Override
            protected Node call(){
                return switch (tabIndex){
                    case 0 -> new CarListBuilder(CarListBuilder.ListType.MAIN, App.getInstance().getCarStorage().search(searchField.getText())).build();
                    default -> new ClientListBuilder(App.getInstance().getClientStorage().search(searchField.getText())).build();
                };
            }
        };
        this.searchTask.setOnSucceeded(workerStateEvent -> {
            Node node = null;
            try{
                node = searchTask.get();
            }catch (InterruptedException | ExecutionException e1){
                e1.printStackTrace();
            }
            scrollPane.setContent(node);
            hideLoadingGif();
        });
        this.searchTask.setOnFailed(workerStateEvent -> {
            scrollPane.setContent(null);
            hideLoadingGif();
        });
        this.searchTask.setOnCancelled(workerStateEvent -> hideLoadingGif());
        new Thread(searchTask).start();
    }

    public void onTabChange(final int oldIndex, final int newIndex){
        this.searchField.setText("");
        this.onClickSearch(null);
    }

    private void showLoadingGif(){
        this.loadingGif.setImage(Images.loadingGif);
    }

    private void hideLoadingGif(){
        this.loadingGif.setImage(null);
    }

}
