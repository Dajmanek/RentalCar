package net.dajman.rentalcar.ui.controller.controllers;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.data.managers.FileDataManager;
import net.dajman.rentalcar.data.storage.EntryStorage;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.ui.alert.Alert;
import net.dajman.rentalcar.ui.alert.ProgressAlert;
import net.dajman.rentalcar.ui.controller.Controller;
import net.dajman.rentalcar.ui.transition.fx.ImageTransition;
import net.dajman.rentalcar.ui.utils.Images;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MainController extends Controller {


    private Task<Boolean> fileTask;

    @FXML
    private ImageView imageClose;
    @FXML
    private ImageView imageRestore;
    @FXML
    private ImageView imageMinimize;



    public MainController(){
        App.getInstance().addController(this);
    }

    @Override
    public NodeType getType() {
        return NodeType.MAIN;
    }

    @Override
    protected void firstInitialize() {
        imageClose.setOnMouseEntered(mouseEvent -> new ImageTransition(imageClose, Images.btnClose, Images.btnCloseHover, Duration.millis(200)).playFromStart());
        imageClose.setOnMouseExited(mouseEvent -> new ImageTransition(imageClose, Images.btnCloseHover, Images.btnClose, Duration.millis(200)).playFromStart());
        imageRestore.setOnMouseEntered(mouseEvent -> new ImageTransition(imageRestore, Images.btnRestore, Images.btnRestoreHover, Duration.millis(200)).playFromStart());
        imageRestore.setOnMouseExited(mouseEvent -> new ImageTransition(imageRestore, Images.btnRestoreHover, Images.btnRestore, Duration.millis(200)).playFromStart());
        imageMinimize.setOnMouseEntered(mouseEvent -> new ImageTransition(imageMinimize, Images.btnMinimalize, Images.btnMinimalizeHover, Duration.millis(200)).playFromStart());
        imageMinimize.setOnMouseExited(mouseEvent -> new ImageTransition(imageMinimize, Images.btnMinimalizeHover, Images.btnMinimalize, Duration.millis(200)).playFromStart());
    }

    @FXML
    public void onClickOpen(final ActionEvent event){
        if (this.fileTask != null && this.fileTask.isRunning()){
            return;
        }
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("RCData File", "*" + FileDataManager.FILE_EXTENSION));
        final File file = fileChooser.showOpenDialog(App.getInstance().getStage());
        if (file == null){
            return;
        }
        if (!file.getName().endsWith(FileDataManager.FILE_EXTENSION)){
            new Alert("Nieprawidłowe rozszerzenie pliku.").show();
            return;
        }
        App.getInstance().setFileDataManager(new FileDataManager(file));
        final ProgressAlert progressAlert = new ProgressAlert("Wczytuję...").show();
        this.fileTask = new Task<>() {
            @Override
            protected Boolean call(){
                final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper();
                progress.addListener((obs, oldProgress, newProgress) -> updateProgress(newProgress.doubleValue(), 1));
                return App.getInstance().getFileDataManager().load(progress);
            }
        };
        progressAlert.bind(this.fileTask.progressProperty());

        fileTask.setOnSucceeded(workerStateEvent -> {
            try{
                progressAlert.endLoading(fileTask.get() ? "Wczytywanie zakończone." : "Wystąpił błąd.");
            }catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
                progressAlert.endLoading("Wystąpił błąd.");
            }
            if (!App.getInstance().openGui(NodeType.LISTS)) ((ListsController)App.getInstance().getController(NodeType.LISTS)).onClickSearch(null);
        });
        fileTask.setOnFailed(workerStateEvent -> {
            progressAlert.setText("Wystąpił błąd.");
            Arrays.asList(App.getInstance().getCarStorage(), App.getInstance().getClientStorage()).forEach(EntryStorage::clear);
            if (!App.getInstance().openGui(NodeType.LISTS)) ((ListsController)App.getInstance().getController(NodeType.LISTS)).onClickSearch(null);
        });
        fileTask.setOnCancelled(workerStateEvent -> {
            Arrays.asList(App.getInstance().getCarStorage(), App.getInstance().getClientStorage()).forEach(EntryStorage::clear);
            if (!App.getInstance().openGui(NodeType.LISTS)) ((ListsController)App.getInstance().getController(NodeType.LISTS)).onClickSearch(null);
        });
        progressAlert.runOnClose(() -> {
            if (this.fileTask != null) this.fileTask.cancel();
        });
        new Thread(fileTask).start();
    }

    @FXML
    public void onClickSave(final ActionEvent event){
        if (this.fileTask != null && this.fileTask.isRunning()){
            return;
        }
        if (App.getInstance().getFileDataManager() == null){
            this.onClickSaveAs(event);
            return;
        }
        final ProgressAlert progressAlert = new ProgressAlert("Zapis...").show();
        this.fileTask = new Task<>() {
            @Override
            protected Boolean call() {
                final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper();
                progress.addListener((obs, oldProgress, newProgress) -> updateProgress(newProgress.doubleValue(), 1));
                return App.getInstance().getFileDataManager().save(progress);
            }
        };
        progressAlert.bind(fileTask.progressProperty());
        fileTask.setOnSucceeded(workerStateEvent -> {
            try{
                progressAlert.endLoading(fileTask.get() ? "Zapis zakończony." : "Wystąpił błąd.");
                return;
            }catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
            progressAlert.endLoading("Wystąpił błąd.");
        });
        fileTask.setOnFailed(workerStateEvent -> progressAlert.endLoading("Wystąpił błąd."));
        new Thread(fileTask).start();
        progressAlert.runOnClose(() -> {
            if (fileTask != null)  fileTask.cancel();
        });
    }

    @FXML
    public void onClickSaveAs(final ActionEvent event){
        File file = new FileChooser().showSaveDialog(App.getInstance().getStage());
        if (file == null){
            return;
        }
        if (!file.getName().contains(".")){
            file = new File(file.getParent(), file.getName() + FileDataManager.FILE_EXTENSION);
        }
        else if (!file.getName().endsWith(FileDataManager.FILE_EXTENSION)){
            new Alert("Nieprawidłowy format pliku").show();
            return;
        }
        App.getInstance().setFileDataManager(new FileDataManager(file));
        this.onClickSave(event);
    }

    @FXML
    public void onClickAddCar(final ActionEvent event){
        App.getInstance().openGui(NodeType.CAR_EDIT);
    }

    @FXML
    public void onClickAddClient(final ActionEvent event){
        App.getInstance().openGui(NodeType.CLIENT_EDIT);
    }

    @FXML
    public void onClickClose(final MouseEvent event){
        App.getInstance().getStage().close();
        System.exit(0);
    }

    @FXML
    public void onClickMinimize(final MouseEvent event){
       App.getInstance().getStage().setIconified(true);
    }

    @FXML
    public void onClickRestore(final MouseEvent event){
        App.getInstance().getStage().setFullScreen(!App.getInstance().getStage().isFullScreen());
    }

}
