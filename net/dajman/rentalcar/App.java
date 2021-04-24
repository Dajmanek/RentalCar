package net.dajman.rentalcar;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.dajman.rentalcar.basic.Car;
import net.dajman.rentalcar.basic.Client;
import net.dajman.rentalcar.ui.HistoryElement;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.data.managers.FileDataManager;
import net.dajman.rentalcar.data.storage.EntryStorage;
import net.dajman.rentalcar.ui.PrepareFullScreenBox;
import net.dajman.rentalcar.ui.helpers.DragHelper;
import net.dajman.rentalcar.ui.helpers.ResizeHelper;
import net.dajman.rentalcar.ui.loader.MainNodeLoader;
import net.dajman.rentalcar.ui.loader.NodeLoader;
import net.dajman.rentalcar.ui.controller.IController;
import net.dajman.rentalcar.ui.utils.Images;

import java.util.*;
import java.util.List;

public class App extends Application {

    public static final transient int SHADOW_RADIUS = 15;
    public static final transient String STYLESHEET = App.class.getResource("ui/resources/css/style.css").toExternalForm();

    private static App application;

    private FileDataManager fileDataManager;
    private EntryStorage<Car> carStorage;
    private EntryStorage<Client> clientStorage;

    private Stage stage;
    private NodeType openedGui;
    private MainNodeLoader mainNodeLoader;
    private DragHelper dragHelper;
    private ResizeHelper resizeHelper;
    private List<HistoryElement> history;
    private Map<NodeType, NodeLoader> nodeLoaders;
    private Map<NodeType, IController> controllers;


    public static void main(final String... args){
        launch(args);
    }

    public static App getInstance(){
        return App.application;
    }


    public FileDataManager getFileDataManager() {
        return this.fileDataManager;
    }

    public void setFileDataManager(FileDataManager fileDataManager) {
        this.fileDataManager = fileDataManager;
    }

    public EntryStorage<Car> getCarStorage() {
        return carStorage;
    }

    public EntryStorage<Client> getClientStorage() {
        return clientStorage;
    }

    public Stage getStage() {
        return stage;
    }

    public NodeType getOpenedGui() {
        return openedGui;
    }

    public MainNodeLoader getMainNodeLoader() {
        return mainNodeLoader;
    }

    public IController getController(final NodeType nodeType){
        return this.controllers.get(nodeType);
    }

    public void addController(final IController controller){
        this.controllers.put(controller.getType(), controller);
    }

    @Override
    public void start(final Stage stage) {
        App.application = this;
        this.carStorage = new EntryStorage<>();
        this.clientStorage = new EntryStorage<>();
        this.controllers = new HashMap<>();
        this.history = new ArrayList<>();

        this.stage = stage;
        this.stage.setMinWidth(750.0);
        this.stage.setMinHeight(500.0);
        this.stage.setMaxHeight(Double.MAX_VALUE);
        this.stage.setMaxWidth(Double.MAX_VALUE);
        this.stage.setTitle("RentalCar");
        this.stage.getIcons().add(Images.icon);
        this.stage.initStyle(StageStyle.TRANSPARENT);
        this.stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        this.resizeHelper = new ResizeHelper(this.stage).register();
        this.dragHelper = new DragHelper(this.stage, App.SHADOW_RADIUS, true);

        this.mainNodeLoader = new MainNodeLoader("main");
        this.mainNodeLoader.registerDrag(this.dragHelper);
        this.mainNodeLoader.registerResize(this.resizeHelper);

        this.nodeLoaders = new HashMap<>();
        this.nodeLoaders.put(NodeType.LISTS, new NodeLoader("lists"));
        this.nodeLoaders.put(NodeType.CAR, new NodeLoader("car"));
        this.nodeLoaders.put(NodeType.CAR_EDIT, new NodeLoader("car_edit"));
        this.nodeLoaders.put(NodeType.CLIENT, new NodeLoader("client"));
        this.nodeLoaders.put(NodeType.CLIENT_EDIT, new NodeLoader("client_edit"));

        this.stage.setScene(this.mainNodeLoader.getScene());
        this.stage.show();
        this.controllers.get(NodeType.MAIN).init();
        this.openGui(NodeType.LISTS);

        this.stage.fullScreenProperty().addListener((obs, oldValue, newValue) -> this.onFullScreenChange(newValue));

        /*new Thread(() -> {
            try {
                Thread.sleep(2000L);
                Platform.runLater(() -> {
                    final PrepareFullScreenBox prepareFullScreenStage = new PrepareFullScreenBox(this.stage, App.SHADOW_RADIUS);
                    prepareFullScreenStage.show();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();*/

        /*new Thread(() -> {
            try {
                Thread.sleep(3000L);
                Platform.runLater(() -> {
                    final Pane pane = new Pane();
                    pane.setBackground(new Background(new BackgroundFill(new javafx.scene.paint.Color(0.0D, 0.0D, 0.0D, 0.2D), CornerRadii.EMPTY, Insets.EMPTY)));
                    final Screen screen = this.getScreen(this.stage.getX(), this.stage.getY());
                    final double width = screen.getBounds().getWidth();
                    final double height = screen.getBounds().getHeight();
                    pane.setMaxWidth(200);
                    pane.setMaxHeight(200);
                    pane.setPrefHeight(200);
                    pane.setPrefWidth(200);
                    final Scene scene = new Scene(pane);
                    scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

                   // final Stage stage1 = new Stage();
                    //stage1.setWidth(width);
                    //stage1.setHeight(height);
                    //stage1.setScene(scene);
                    //stage1.initOwner(this.stage);
                    //stage1.initStyle(StageStyle.UTILITY);

                    JFrame frame = new JFrame();
                    frame.setType(Window.Type.UTILITY);
                    frame.setUndecorated(true);
                    frame.setSize((int) width, (int) height);

                    frame.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));

                    JFXPanel jfxPanel = new JFXPanel();
                    jfxPanel.setSize((int)width, (int)height);
                    jfxPanel.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
                    jfxPanel.setScene(scene);
                    frame.add(jfxPanel);

                    frame.setVisible(true);

                    //stage1.toBack();;
                    *//*stage1.show();
                    stage1.toBack();
                    this.stage.toFront();*//*
                    this.stage.toFront();


                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();*/


    }
    private Screen getScreen(final double x, final double y){
        return Screen.getScreens().stream().filter(screen -> screen.getBounds().contains(x, y)).findFirst().orElse(null);
    }


    private void onFullScreenChange(boolean newValue){
        Platform.runLater(() -> {
            if (!newValue){
                return;
            }
            this.stage.setWidth(this.stage.getWidth() + App.SHADOW_RADIUS * 2);
            this.stage.setHeight(this.stage.getHeight() + App.SHADOW_RADIUS * 2);
            this.stage.setX(this.stage.getX() - App.SHADOW_RADIUS);
            this.stage.setY(this.stage.getY() - App.SHADOW_RADIUS);
        });
    }


    public HistoryElement getBack(){
        if (this.history.isEmpty()){
            return new HistoryElement(NodeType.MAIN);
        }
        if (this.history.size() > 1){
            this.history.remove(this.history.size() - 1);
        }
        return this.history.get(this.history.size() - 1);
    }

    public boolean openBack(){
        return this.openGui(this.getBack());
    }

    public boolean openGui(final HistoryElement historyElement){
        return this.openGui(historyElement.getNodeType(), false, historyElement.getObjects());
    }

    public boolean openGui(final NodeType nodeType, final Object... objects){
        return this.openGui(nodeType, true, objects);
    }

    public boolean openGui(final NodeType nodeType, boolean history, final Object... objects){
        if (this.openedGui == nodeType){
            return false;
        }
        if (history) this.history.add(new HistoryElement(nodeType, objects));
        this.openedGui = nodeType;
        this.mainNodeLoader.insertNode(this.nodeLoaders.get(nodeType));
        this.getController(nodeType).init(objects);
        return true;
    }


}
