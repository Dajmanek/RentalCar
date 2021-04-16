package net.dajman.rentalcar.ui.transition;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

public class BackgroundColorTransition extends Transition {


    private final double rFrom, gFrom, bFrom, rDiff, gDiff, bDiff;
    private final List<Node> nodes;

    public BackgroundColorTransition(final Color colorFrom, final Color colorTo, final Duration duration, final Node... nodes){
        this.rFrom = colorFrom.getRed();
        this.gFrom = colorFrom.getGreen();
        this.bFrom = colorFrom.getBlue();
        this.rDiff = colorTo.getRed() - this.rFrom;
        this.gDiff = colorTo.getGreen() - this.gFrom;
        this.bDiff = colorTo.getBlue() - this.bFrom;

        this.nodes = Arrays.asList(nodes);
        this.setCycleDuration(duration);
    }


    @Override
    protected void interpolate(double v) {
        final String style = "-fx-background-color: rgb(" + ((rFrom + rDiff * v) * 255) + ", " + ((gFrom + gDiff * v) * 255) + ", " + ((bFrom + bDiff * v) * 255) + ")";
        this.nodes.forEach(node -> node.setStyle(style));
    }
}
