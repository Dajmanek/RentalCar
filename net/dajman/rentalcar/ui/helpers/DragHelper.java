package net.dajman.rentalcar.ui.helpers;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DragHelper {

    private final Stage stage;

    private double x;
    private double y;

    public DragHelper(final Stage stage){
        this.stage = stage;
    }

    public void register(){
        this.stage.addEventHandler(MouseEvent.MOUSE_PRESSED, this::mousePressed);
        this.stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::mouseDragged);
    }

    public void register(final Node node){
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, this::mousePressed);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::mouseDragged);
    }

    private void mousePressed(final MouseEvent mouseEvent){
        this.x = this.stage.getX() - mouseEvent.getScreenX();
        this.y = this.stage.getY() - mouseEvent.getScreenY();
    }

    private void mouseDragged(final MouseEvent mouseEvent){
        if (this.stage.getScene().getCursor().toString().contains("RESIZE")){
            return;
        }
        this.stage.setX(mouseEvent.getScreenX() + this.x);
        this.stage.setY(mouseEvent.getScreenY() + this.y);
    }

}
