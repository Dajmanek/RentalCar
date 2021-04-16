package net.dajman.rentalcar.ui.controller.controllers;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.ui.controller.Controller;

public class TestController extends Controller {


    public static TestController inst;

    public TestController(){
        inst = this;
    }

    @FXML
    public AnchorPane topBorder;
    @FXML
    public AnchorPane rightTopBorder;
    @FXML
    public AnchorPane rightBorder;
    @FXML
    public AnchorPane rightBottomBorder;
    @FXML
    public AnchorPane bottomBorder;
    @FXML
    public AnchorPane leftBottomBorder;
    @FXML
    public AnchorPane leftBorder;
    @FXML
    public AnchorPane leftTopBorder;

    public Window window;

    private double x;
    private double y;
    private double endX;
    private double endY;

    private int test = -1;


    @Override
    public NodeType getType() {
        return null;
    }

    @Override
    protected void firstInitialize() {
        final Stage stage = App.getInstance().getStage();
        this.window = rightBorder.getScene().getWindow();
        topBorder.setCursor(Cursor.N_RESIZE);
        rightTopBorder.setCursor(Cursor.NE_RESIZE);
        rightBorder.setCursor(Cursor.W_RESIZE);
        rightBottomBorder.setCursor(Cursor.SE_RESIZE);
        bottomBorder.setCursor(Cursor.S_RESIZE);
        leftBottomBorder.setCursor(Cursor.SW_RESIZE);
        leftBorder.setCursor(Cursor.W_RESIZE);
        leftTopBorder.setCursor(Cursor.SE_RESIZE);

        final double minWidth = stage.getMinWidth();
        final double minHeight = stage.getMinHeight();

        // BOTTOM, RIGHT
        rightBorder.setOnMouseDragged(mouseEvent -> stage.setWidth(Math.max(mouseEvent.getScreenX() - stage.getX(), minWidth)));
        bottomBorder.setOnMouseDragged(mouseEvent -> stage.setHeight(Math.max(mouseEvent.getScreenY() - stage.getY(), minHeight)));
        rightBottomBorder.setOnMouseDragged(mouseEvent -> {
            stage.setWidth(Math.max(mouseEvent.getScreenX() - stage.getX(), minWidth));
            stage.setHeight(Math.max(mouseEvent.getScreenY() - stage.getY(), minHeight));
        });


        // LEFT
        leftBorder.setOnMousePressed(mouseEvent -> {
            this.x = mouseEvent.getSceneX();
            this.endX = stage.getWidth() + stage.getX();
            this.test = 1;
        });

        leftBorder.setOnMouseDragged(mouseEvent -> {
            final double newWidth = this.endX - mouseEvent.getScreenX();
            if (newWidth < minWidth){
                return;
            }
            stage.setWidth(newWidth);
            stage.setX(mouseEvent.getScreenX() - this.x);
        });

    }
}
