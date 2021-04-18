package net.dajman.rentalcar;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.dajman.rentalcar.basic.Car;
import net.dajman.rentalcar.basic.Client;
import net.dajman.rentalcar.basic.Entry;
import net.dajman.rentalcar.ui.HistoryElement;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.data.managers.FileDataManager;
import net.dajman.rentalcar.data.storage.EntryStorage;
import net.dajman.rentalcar.ui.helpers.DragHelper;
import net.dajman.rentalcar.ui.helpers.ResizeHelper;
import net.dajman.rentalcar.ui.loader.MainNodeLoader;
import net.dajman.rentalcar.ui.loader.NodeLoader;
import net.dajman.rentalcar.ui.controller.IController;

import java.util.*;

public class App extends Application {

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


        //TEST
        for(int i = 0; i < 20; i++){
            final Car car = new Car(20.0F, "Brand" + i, "Model");
            this.carStorage.add(car);
        }

        for(int i = 0; i < 2; i++){
            final Client client = new Client("FN" + i, "LN" + i, "123123123", "25-499", "city", "street", 25, 13);
            if (i == 0){
                final Car car = this.carStorage.getAll().stream().findAny().orElse(null);
                if (car != null){
                    client.addRentedCar(car);
                    car.setRentalDate(System.currentTimeMillis());
                    car.setClient(client);
                }
            }
            this.clientStorage.add(client);
        }
        // END TEST

        this.stage = stage;
        this.stage.setMinWidth(750.0);
        this.stage.setMinHeight(500.0);
        this.stage.setTitle("RentalCar");
        this.stage.getIcons().add(new Image(App.class.getResourceAsStream("ui/img/icon.png")));
        this.stage.initStyle(StageStyle.TRANSPARENT);
        this.resizeHelper = new ResizeHelper(this.stage).register();
        this.dragHelper = new DragHelper(this.stage);

        this.mainNodeLoader = new MainNodeLoader("main");
        this.mainNodeLoader.registerDrag(this.dragHelper);

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



        /*final FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("ui/fxml/test_resize.fxml"));
        dragHelper.register();

        this.stage.initStyle(StageStyle.TRANSPARENT);
        try {
            this.stage.setScene(new Scene((Parent) fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stage.show();
        TestController.inst.init();*/



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
