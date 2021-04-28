package net.dajman.rentalcar.ui.transition.swing;

import javafx.animation.Transition;
import javafx.util.Duration;

import javax.swing.*;

public class FadeTransition extends Transition {

    private final JFrame jFrame;

    private final float oldOpaciy, diffOpacity;


    public FadeTransition(final JFrame jFrame, final float newOpacity, final Duration duration){
        this.jFrame = jFrame;
        this.diffOpacity = newOpacity - (this.oldOpaciy = this.jFrame.getOpacity());
        this.setCycleDuration(duration);
    }

    @Override
    protected void interpolate(double v) {
        this.jFrame.setOpacity(this.oldOpaciy + (float) (this.diffOpacity * v));
    }
}
