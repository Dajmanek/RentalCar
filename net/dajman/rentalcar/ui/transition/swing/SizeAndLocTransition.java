package net.dajman.rentalcar.ui.transition.swing;

import javafx.animation.Transition;
import javafx.util.Duration;
import javax.swing.*;

public class SizeAndLocTransition extends Transition {

    private final JComponent jComponent;
    private final int oldWidth, oldHeight, oldX, oldY, diffWidth, diffHeight, diffX, diffY;

    public SizeAndLocTransition(final JComponent jComponent, final int newWidth, final int newHeight, final int newX, final int newY, final Duration duration){
        this.jComponent = jComponent;
        this.diffWidth = newWidth - (this.oldWidth = jComponent.getWidth());
        this.diffHeight = newHeight - (this.oldHeight = jComponent.getHeight());
        this.diffX = newX - (this.oldX = jComponent.getX());
        this.diffY = newY - (this.oldY = jComponent.getY());
        setCycleDuration(duration);
    }

    @Override
    protected void interpolate(double v) {
        jComponent.setSize(this.oldWidth + (int) (this.diffWidth * v), this.oldHeight + (int) (this.diffHeight * v));
        jComponent.setLocation(this.oldX + (int) (this.diffX * v), this.oldY + (int) (this.diffY * v));
    }

}
