package net.dajman.rentalcar.ui.transition;

import javafx.animation.Transition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class SizeLocTransition extends Transition {


    private final Pane pane;
    private final double oldWidth, oldHeight, oldX, oldY, diffWidth, diffHeight, diffX, diffY;


    public SizeLocTransition(final Pane pane, double newWidth, double newHeight, double newX, double newY, Duration duration){
        this.pane = pane;
        oldWidth = pane.getWidth();
        oldHeight = pane.getHeight();
        oldX = pane.getLayoutX();
        oldY = pane.getLayoutY();

        diffWidth = newWidth - oldWidth;
        diffHeight = newHeight - oldHeight;
        diffX = newX - oldX;
        diffY = newY - oldY;

        setCycleDuration(duration);

    }

    @Override
    protected void interpolate(double v) {
        pane.setPrefSize(oldWidth + (diffWidth * v), oldHeight + (diffHeight * v));
        pane.setLayoutX(oldX + (diffX * v));
        pane.setLayoutY(oldY + (diffY * v));
    }
}
