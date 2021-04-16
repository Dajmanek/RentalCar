package net.dajman.rentalcar.ui.controller.controllers;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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


    private double endX;
    private double endY;


    @Override
    public NodeType getType() {
        return null;
    }

    @Override
    protected void firstInitialize() {
        final Stage stage = App.getInstance().getStage();
        stage.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {

        });
        leftBorder.setCursor(Cursor.W_RESIZE);
        topBorder.setCursor(Cursor.N_RESIZE);
        rightBorder.setCursor(Cursor.W_RESIZE);
        bottomBorder.setCursor(Cursor.S_RESIZE);
        rightTopBorder.setCursor(Cursor.NE_RESIZE);
        rightBottomBorder.setCursor(Cursor.SE_RESIZE);
        leftBottomBorder.setCursor(Cursor.SW_RESIZE);
        leftTopBorder.setCursor(Cursor.SE_RESIZE);

        final double minWidth = stage.getMinWidth();
        final double minHeight = stage.getMinHeight();

        // RIGHT
        rightBorder.setOnMouseDragged(mouseEvent -> stage.setWidth(Math.max(mouseEvent.getScreenX() - stage.getX(), minWidth)));
        // BOTTOM
        bottomBorder.setOnMouseDragged(mouseEvent -> stage.setHeight(Math.max(mouseEvent.getScreenY() - stage.getY(), minHeight)));

        // LEFT
        leftBorder.setOnMousePressed(mouseEvent -> {
            this.endX = stage.getWidth() + stage.getX();
        });
        leftBorder.setOnMouseDragged(mouseEvent -> {
            final double width = this.endX - mouseEvent.getScreenX();
            if (width < minWidth){
                return;
            }
            stage.setWidth(width);
            stage.setX(mouseEvent.getScreenX());
        });

        // TOP
        topBorder.setOnMousePressed(mouseEvent -> {
            this.endY = stage.getHeight() + stage.getY();
        });
        topBorder.setOnMouseDragged(mouseEvent -> {
            final double height = this.endY - mouseEvent.getScreenY();
            if (height < minHeight){
                return;
            }
            stage.setHeight(height);
            stage.setY(mouseEvent.getScreenY());
        });


        // CORNER_RIGHT_BOTTOM
        rightBottomBorder.setOnMouseDragged(mouseEvent -> {
            stage.setWidth(Math.max(mouseEvent.getScreenX() - stage.getX(), minWidth));
            stage.setHeight(Math.max(mouseEvent.getScreenY() - stage.getY(), minHeight));
        });



        // CORNER_RIGHT_TOP
        rightTopBorder.setOnMousePressed(mouseEvent -> {
            this.endY = stage.getHeight() + stage.getY();
        });
        rightTopBorder.setOnMouseDragged(mouseEvent -> {
            stage.setWidth(Math.max(mouseEvent.getScreenX() - stage.getX(), minWidth));
            final double height = this.endY - mouseEvent.getScreenY();
            if (height < minHeight){
                return;
            }
            stage.setHeight(height);
            stage.setY(mouseEvent.getScreenY());
        });

    }
}
