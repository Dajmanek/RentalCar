package net.dajman.rentalcar.ui.helpers;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.dajman.rentalcar.ui.PrepareFullScreenBox;

public class DragHelper {

    private final Stage stage;
    private final double shadowRadius;
    private final boolean topFullScreen;
    private final PrepareFullScreenBox prepareFullScreenBox;

    private transient double x;
    private transient double y;


    public DragHelper(final Stage stage, final double shadowRadius, final Node... nodes){
        this(stage, shadowRadius, false, nodes);
    }

    public DragHelper(final Stage stage, final double shadowRadius, final boolean topFullScreen, final Node... nodes){
        this.stage = stage;
        this.shadowRadius = shadowRadius;
        this.topFullScreen = topFullScreen;
        this.prepareFullScreenBox = topFullScreen ? new PrepareFullScreenBox(stage, shadowRadius) : null;
        for (Node node : nodes) {
            this.register(node);
        }
    }

    public void register(){
        this.stage.addEventHandler(MouseEvent.MOUSE_PRESSED, this::mousePressed);
        this.stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::mouseDragged);
        if (this.topFullScreen){
            this.stage.addEventHandler(MouseEvent.MOUSE_RELEASED, this::mouseReleased);
        }
    }

    public void register(final Node node){
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, this::mousePressed);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::mouseDragged);
        if (this.topFullScreen){
            node.addEventHandler(MouseEvent.MOUSE_RELEASED, this::mouseReleased);
        }
    }


    private void mousePressed(final MouseEvent mouseEvent){
        this.x = mouseEvent.getScreenX() - this.stage.getX();
        this.y = mouseEvent.getScreenY() - this.stage.getY();
    }

    private void mouseReleased(final MouseEvent mouseEvent){
        this.prepareFullScreenBox.hide();
        final Screen screen = this.getScreen(mouseEvent.getScreenX(), mouseEvent.getScreenY());
        if (screen == null){
            return;
        }
        final double stageY = this.stage.getY() + this.shadowRadius;
        if (stageY >= screen.getBounds().getMinY() && stageY <= screen.getBounds().getMinY() + 4){
            if (!stage.isFullScreen()){
                stage.setX(screen.getBounds().getMinX());
                stage.setFullScreen(true);
            }
            return;
        }
    }

    private void mouseDragged(final MouseEvent mouseEvent){
        if (this.stage.getScene().getCursor() != null && this.stage.getScene().getCursor().toString().contains("RESIZE")){
            return;
        }
        final double x = mouseEvent.getScreenX() - this.x;
        final double y = mouseEvent.getScreenY() - this.y;

        final Screen screen = this.getScreen(mouseEvent.getScreenX(), mouseEvent.getScreenY());
        final double stageY = this.stage.getY() + this.shadowRadius;

        if (this.topFullScreen){
            if (this.stage.isFullScreen()){
                if (screen.getBounds().getMinY() == y){
                    return;
                }
                this.stage.setFullScreen(false);
                this.x = this.x / (screen.getBounds().getWidth() / this.stage.getWidth());
            }
        }

        this.stage.setX(x);
        if (this.containsYOnScreens(y + this.shadowRadius)){
            this.stage.setY(y);
        }

        if (topFullScreen){
            if (stageY >= screen.getBounds().getMinY() && stageY < 5){
                if (!stage.isFullScreen()){
                    this.prepareFullScreenBox.show(mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }
            }else{
                this.prepareFullScreenBox.hide();
            }
        }
    }

    private boolean containsYOnScreens(final double y){
        for (Screen screen : Screen.getScreens())
            if (screen.getBounds().getMinY() <= y && screen.getBounds().getMaxY() >= y)
                return true;
        return false;
    }

    private Screen getScreen(final double x, final double y){
        return Screen.getScreens().stream().filter(screen -> screen.getBounds().contains(x, y)).findFirst().orElse(null);
    }

}
