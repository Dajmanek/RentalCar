package net.dajman.rentalcar.ui.transition.fx;

import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

public class BackgroundColorTransition extends Transition {


    private final Color oldColor, newColor;
    private final List<Pane> nodes;

    public BackgroundColorTransition(final Color oldColor, final Color newColor, final Duration duration, final Pane... nodes){
        this.oldColor = oldColor;
        this.newColor = newColor;
        this.nodes = Arrays.asList(nodes);
        this.setCycleDuration(duration);
    }

    @Override
    protected void interpolate(double v) {
        final double red = oldColor.getRed() + (newColor.getRed() - oldColor.getRed()) * v;
        final double green = oldColor.getGreen() + (newColor.getGreen() - oldColor.getGreen()) * v;
        final double blue = oldColor.getBlue() + (newColor.getBlue() - oldColor.getBlue()) * v;
        final double opacity = oldColor.getOpacity() + (newColor.getOpacity() - oldColor.getOpacity()) * v;
        this.nodes.forEach(node -> node.setBackground(new Background(new BackgroundFill(new Color(red, green, blue, opacity), CornerRadii.EMPTY, Insets.EMPTY))));
    }

}
