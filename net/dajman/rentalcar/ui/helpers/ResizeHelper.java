package net.dajman.rentalcar.ui.helpers;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.dajman.rentalcar.App;

public class ResizeHelper {

    private final int borderMin;
    private final int borderMax;
    private final Stage stage;

    private transient double windowMaxX;
    private transient double windowMaxY;
    private transient Side side;

    public ResizeHelper(final Stage stage){
        this(stage, 8, App.SHADOW_RADIUS);
    }


    public ResizeHelper(final Stage stage, final int border, final int shadowRadius){
        this.side = Side.NONE;
        this.stage = stage;
        this.borderMin = Math.min(0, shadowRadius - border);
        this.borderMax = this.borderMin + border;
    }

    public ResizeHelper register(){
        this.stage.addEventHandler(MouseEvent.MOUSE_PRESSED, this::mousePressed);
        this.stage.addEventHandler(MouseEvent.MOUSE_RELEASED, this::mouseReleased);
        this.stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::mouseDragged);
        this.stage.addEventHandler(MouseEvent.MOUSE_MOVED, this::mouseMoved);
        return this;
    }

    public void register(final Node node){
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, this::mousePressed);
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, this::mouseReleased);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::mouseDragged);
        node.addEventHandler(MouseEvent.MOUSE_MOVED, this::mouseMoved);
    }

    private void mouseReleased(final MouseEvent mouseEvent){
        final Side side = this.getSide(mouseEvent);
        if (this.side == side){
            return;
        }
        this.setCursor(side);
    }

    private void mousePressed(final MouseEvent mouseEvent){
        if (this.getSide(mouseEvent.getSceneX(), mouseEvent.getSceneY()) == Side.NONE){
            return;
        }
        this.windowMaxX = this.stage.getX() + this.stage.getWidth();
        this.windowMaxY = this.stage.getY() + this.stage.getHeight();
    }

    private void mouseDragged(final MouseEvent mouseEvent){
        final Side side = this.side;
        if (side == Side.LEFT || side == Side.BOTTOM_LEFT || side == Side.TOP_LEFT){
            final double width = this.windowMaxX - mouseEvent.getScreenX();
            if (width >= this.stage.getMinWidth()){
                this.stage.setWidth(this.windowMaxX - mouseEvent.getScreenX());
                this.stage.setX(mouseEvent.getScreenX());
            }
        }
        if (side == Side.RIGHT || side == Side.BOTTOM_RIGHT || side == Side.TOP_RIGHT){
            this.stage.setWidth(Math.max(this.stage.getMinWidth(), mouseEvent.getScreenX() - this.stage.getX()));
        }
        if (side == Side.TOP || side == Side.TOP_LEFT || side == Side.TOP_RIGHT){
            final double height = this.windowMaxY - mouseEvent.getScreenY();
            if (height >= this.stage.getMinHeight()){
                this.stage.setHeight(height);
                this.stage.setY(mouseEvent.getScreenY());
            }
        }
        if (side == Side.BOTTOM || side == Side.BOTTOM_LEFT || side == Side.BOTTOM_RIGHT){
            this.stage.setHeight(Math.max(this.stage.getMinHeight(), mouseEvent.getScreenY() - this.stage.getY()));
        }
    }

    private void mouseMoved(final MouseEvent mouseEvent){
        final Side side = this.getSide(mouseEvent);
        if (this.side == side){
            return;
        }
        this.side = side;
        this.setCursor(this.side);
    }

    private Side getSide(final MouseEvent mouseEvent){
        return this.getSide(mouseEvent.getSceneX(), mouseEvent.getSceneY());
    }

    private Side getSide(final double mouseSceneX, final double mouseSceneY){
        final double width = this.stage.getWidth();
        final double height = this.stage.getHeight();
        if (mouseSceneX <= this.borderMax && mouseSceneX >= this.borderMin){
            if (mouseSceneY <= this.borderMax && mouseSceneY >= this.borderMin){
                return Side.TOP_LEFT;
            }
            if (mouseSceneY >= height - this.borderMax && mouseSceneY <= height - this.borderMin){
                return Side.BOTTOM_LEFT;
            }
            return Side.LEFT;
        }
        if (mouseSceneX >= width - this.borderMax && mouseSceneX <= width - this.borderMin){
            if (mouseSceneY <= this.borderMax && mouseSceneY >= this.borderMin){
                return Side.TOP_RIGHT;
            }
            if (mouseSceneY >= height - this.borderMax && mouseSceneY <= height - this.borderMin){
                return Side.BOTTOM_RIGHT;
            }
            return Side.RIGHT;
        }
        if (mouseSceneY <= this.borderMax && mouseSceneY >= height ){
            return Side.TOP;
        }
        if (mouseSceneY >= height - this.borderMax && mouseSceneY <= height - this.borderMin){
            return Side.BOTTOM;
        }
        return Side.NONE;
    }

    private void setCursor(final Side side){
        this.stage.getScene().setCursor(
                switch (side) {
                    case TOP -> Cursor.N_RESIZE;
                    case TOP_RIGHT -> Cursor.NE_RESIZE;
                    case RIGHT -> Cursor.E_RESIZE;
                    case BOTTOM_RIGHT -> Cursor.SE_RESIZE;
                    case BOTTOM -> Cursor.S_RESIZE;
                    case BOTTOM_LEFT -> Cursor.SW_RESIZE;
                    case LEFT -> Cursor.W_RESIZE;
                    case TOP_LEFT -> Cursor.NW_RESIZE;
                    default -> Cursor.DEFAULT;
                });
    }

    enum Side{
        TOP,
        TOP_RIGHT,
        RIGHT,
        BOTTOM_RIGHT,
        BOTTOM,
        BOTTOM_LEFT,
        LEFT,
        TOP_LEFT,
        NONE;
    }
}
