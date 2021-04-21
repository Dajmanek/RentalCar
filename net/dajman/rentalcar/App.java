package net.dajman.rentalcar;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.dajman.rentalcar.basic.Car;
import net.dajman.rentalcar.basic.Client;
import net.dajman.rentalcar.basic.Entry;
import net.dajman.rentalcar.ui.HistoryElement;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.data.managers.FileDataManager;
import net.dajman.rentalcar.data.storage.EntryStorage;
import net.dajman.rentalcar.ui.alert.ProgressAlert;
import net.dajman.rentalcar.ui.helpers.DragHelper;
import net.dajman.rentalcar.ui.helpers.ResizeHelper;
import net.dajman.rentalcar.ui.loader.MainNodeLoader;
import net.dajman.rentalcar.ui.loader.NodeLoader;
import net.dajman.rentalcar.ui.controller.IController;
import net.dajman.rentalcar.ui.utils.Images;

import java.util.*;

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

    public List<EntryStorage<? extends Entry<?>>> getEntryStorages(){
        return Arrays.asList(carStorage, clientStorage);
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
        this.resizeHelper = new ResizeHelper(this.stage).register();
        this.dragHelper = new DragHelper(this.stage);

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
        new ProgressAlert(System.getProperty("java.version")).show();
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
