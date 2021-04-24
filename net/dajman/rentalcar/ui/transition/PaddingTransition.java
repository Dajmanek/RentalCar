package net.dajman.rentalcar.ui.transition;

import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


public class PaddingTransition extends Transition {

    private final Pane pane;
    private final Insets oldPadding;
    private final Insets diffPadding;

    public PaddingTransition(final Pane pane, final Insets newPadding){
        this(pane, newPadding, Duration.millis(200));
    }

    public PaddingTransition(final Pane pane, final Insets toPadding, final Duration duration){
        this.pane = pane;
        this.oldPadding = new Insets(pane.getPadding().getTop(), pane.getPadding().getRight(), pane.getPadding().getBottom(), pane.getPadding().getLeft());
        this.diffPadding = new Insets(toPadding.getTop() - this.oldPadding.getTop(), toPadding.getRight() - this.oldPadding.getRight(), toPadding.getBottom() - this.oldPadding.getBottom(), toPadding.getLeft() - this.oldPadding.getLeft());
        setCycleDuration(duration);
    }


    @Override
    protected void interpolate(double v) {
        pane.setPadding(new Insets(oldPadding.getTop() + (diffPadding.getTop() * v), oldPadding.getRight() + (diffPadding.getRight() * v), oldPadding.getBottom() + (diffPadding.getBottom() * v), oldPadding.getLeft() + (diffPadding.getLeft() * v)));
    }

}
