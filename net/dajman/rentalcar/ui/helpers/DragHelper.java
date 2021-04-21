package net.dajman.rentalcar.ui.helpers;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.dajman.rentalcar.App;

public class DragHelper {

    private final Stage stage;

    private double x;
    private double y;

    public DragHelper(final Stage stage, final Node... nodes){
        this.stage = stage;
        for (Node node : nodes) {
            this.register(node);
        }
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
        if (this.stage.getScene().getCursor() != null && this.stage.getScene().getCursor().toString().contains("RESIZE")){
            return;
        }
        final double x = mouseEvent.getScreenX() + this.x;
        final double y = mouseEvent.getScreenY() + this.y;
        this.stage.setX(x);
        if (this.containsYOnScreens(y + App.SHADOW_RADIUS)){
            this.stage.setY(y);
        }
    }

    private boolean containsXOnScreens(final double x){
        for (Screen screen : Screen.getScreens())
            if (screen.getBounds().getMinX() <= x && screen.getBounds().getMaxX() >= x)
                return true;
        return false;
    }

    private boolean containsYOnScreens(final double y){
        for (Screen screen : Screen.getScreens())
            if (screen.getBounds().getMinY() <= y && screen.getBounds().getMaxY() >= y)
                return true;
        return false;
    }

    private boolean containsOnScreens(final double x, final double y){
        for (Screen screen : Screen.getScreens())
            if (screen.getBounds().contains(x ,y))
                return true;
        return false;
    }





}
