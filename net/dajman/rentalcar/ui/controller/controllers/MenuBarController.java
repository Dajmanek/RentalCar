package net.dajman.rentalcar.ui.controller.controllers;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.FileChooser;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.data.managers.FileDataManager;
import net.dajman.rentalcar.data.storage.EntryStorage;
import net.dajman.rentalcar.ui.builder.alert.AlertBuilder;
import net.dajman.rentalcar.ui.builder.alert.ProgressAlertBuilder;
import net.dajman.rentalcar.ui.controller.Controller;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MenuBarController extends Controller {

    private Task<Boolean> fileTask;

    public MenuBarController(){
        App.getInstance().addController(this);
    }

    @Override
    public NodeType getType() {
        return NodeType.MENU_BAR;
    }

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
            new AlertBuilder(Alert.AlertType.ERROR, "Plik", "Nieprawidłowe rozszerzenie pliku.").buildAndShow();
            return;
        }
        App.getInstance().setFileDataManager(new FileDataManager(file));
        final ProgressIndicator progressBar = new ProgressBar();
        final Alert alert = new ProgressAlertBuilder(Alert.AlertType.INFORMATION, "Plik", "Wczytuję...", progressBar).buildAndShow();
        alert.show();
        this.fileTask = new Task<>() {
            @Override
            protected Boolean call(){
                final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper();
                progress.addListener((obs, oldProgress, newProgress) -> updateProgress(newProgress.doubleValue(), 1));
                return App.getInstance().getFileDataManager().load(progress);
            }
        };
        progressBar.progressProperty().bind(fileTask.progressProperty());

        fileTask.setOnSucceeded(workerStateEvent -> {
            try{
                ProgressAlertBuilder.endLoading(alert, fileTask.get() ? "Wczytywanie zakończone." : "Wystąpił błąd");
            }catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
                ProgressAlertBuilder.endLoading(alert, "Wystąpił błąd.");
            }
            if (!App.getInstance().openGui(NodeType.MAIN)) ((MainController)App.getInstance().getController(NodeType.MAIN)).onClickSearch(null);
        });
        fileTask.setOnFailed(workerStateEvent -> {
            ProgressAlertBuilder.setContentText(alert, "Wystąpił błąd.");
            Arrays.asList(App.getInstance().getCarStorage(), App.getInstance().getClientStorage()).forEach(EntryStorage::clear);
            if (!App.getInstance().openGui(NodeType.MAIN)) ((MainController)App.getInstance().getController(NodeType.MAIN)).onClickSearch(null);
        });
        fileTask.setOnCancelled(workerStateEvent -> {
            Arrays.asList(App.getInstance().getCarStorage(), App.getInstance().getClientStorage()).forEach(EntryStorage::clear);
            if (!App.getInstance().openGui(NodeType.MAIN)) ((MainController)App.getInstance().getController(NodeType.MAIN)).onClickSearch(null);
        });
        alert.setOnCloseRequest(dialogEvent ->{
            if (this.fileTask != null) this.fileTask.cancel();
        });
        new Thread(fileTask).start();
    }


    public void onClickSave(final ActionEvent event){
        if (this.fileTask != null && this.fileTask.isRunning()){
            return;
        }
        if (App.getInstance().getFileDataManager() == null){
            this.onClickSaveAs(event);
            return;
        }
        final ProgressIndicator progressBar = new ProgressBar();
        final Alert alert = new ProgressAlertBuilder(Alert.AlertType.INFORMATION, "Plik", "Zapisywanie...", progressBar).buildAndShow();
        alert.show();
        this.fileTask = new Task<>() {
            @Override
            protected Boolean call() {
                final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper();
                progress.addListener((obs, oldProgress, newProgress) -> updateProgress(newProgress.doubleValue(), 1));
                return App.getInstance().getFileDataManager().save(progress);
            }
        };
        progressBar.progressProperty().bind(fileTask.progressProperty());
        fileTask.setOnSucceeded(workerStateEvent -> {
            try{
                ProgressAlertBuilder.endLoading(alert, fileTask.get() ? "Zapis zakończony" : "Wystąpił błąd.");
                return;
            }catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
            ProgressAlertBuilder.endLoading(alert, "Wystąpił błąd.");
        });
        fileTask.setOnFailed(workerStateEvent -> ProgressAlertBuilder.setContentText(alert, "Wystąpił błąd."));
        new Thread(fileTask).start();
        alert.setOnCloseRequest(dialogEvent -> {
            if (fileTask != null)  fileTask.cancel();
        });
    }


    public void onClickSaveAs(final ActionEvent event){
        File file = new FileChooser().showSaveDialog(App.getInstance().getStage());
        if (file == null){
            return;
        }
        if (!file.getName().contains(".")){
            file = new File(file.getName() + FileDataManager.FILE_EXTENSION);
        }
        else if (!file.getName().endsWith(FileDataManager.FILE_EXTENSION)){
            new AlertBuilder(Alert.AlertType.ERROR, "Plik", "Nieprawidłowy format pliku.").buildAndShow();
            return;
        }
        App.getInstance().setFileDataManager(new FileDataManager(file));
        this.onClickSave(event);
    }

    public void onClickAddCar(final ActionEvent event){
        App.getInstance().openGui(NodeType.CAR_EDIT);
    }

    public void onClickAddClient(final ActionEvent event){
        App.getInstance().openGui(NodeType.CLIENT_EDIT);
    }


}
