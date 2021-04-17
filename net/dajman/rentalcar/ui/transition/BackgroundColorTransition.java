package net.dajman.rentalcar.ui.transition;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

public class BackgroundColorTransition extends Transition {


    private final Color oldColor, newColor;
    private final List<Node> nodes;

    public BackgroundColorTransition(final Color oldColor, final Color newColor, final Duration duration, final Node... nodes){
        this.oldColor = oldColor;
        this.newColor = newColor;

        this.nodes = Arrays.asList(nodes);
        this.setCycleDuration(duration);
    }


    @Override
    protected void interpolate(double v) {
        final double red = (oldColor.getRed() + (newColor.getRed() - oldColor.getRed()) * v) * 255;
        final double green = (oldColor.getGreen() + (newColor.getGreen() - oldColor.getGreen()) * v) * 255;
        final double blue = (oldColor.getBlue() + (newColor.getBlue() - oldColor.getBlue()) * v) * 255;
        final double opacity = oldColor.getOpacity() + (newColor.getOpacity() - oldColor.getOpacity()) * v;
        final String style = "-fx-background-color: rgba(" + red + ", " + green + ", " + blue + ", " + opacity + ")";
        this.nodes.forEach(node -> node.setStyle(style));
    }
}
