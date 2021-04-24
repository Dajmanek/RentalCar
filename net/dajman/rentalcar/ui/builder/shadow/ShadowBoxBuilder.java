package net.dajman.rentalcar.ui.builder.shadow;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import net.dajman.rentalcar.ui.builder.Builder;

public class ShadowBoxBuilder implements Builder<GridPane> {

    private final Node node;
    private final int shadowRadius;

    public ShadowBoxBuilder(final Node node){
        this(node, 10);
    }

    public ShadowBoxBuilder(final Node node, final int shadowRadius){
        this.node = node;
        this.shadowRadius = shadowRadius;
    }

    @Override
    public GridPane build() {
        final GridPane gridPane = new GridPane();
        final ColumnConstraints columnConstraints = new ColumnConstraints();
        final RowConstraints rowConstraints = new RowConstraints();
        columnConstraints.setMaxWidth(Double.MAX_VALUE);
        columnConstraints.setHgrow(Priority.SOMETIMES);
        rowConstraints.setMaxHeight(Double.MAX_VALUE);
        rowConstraints.setVgrow(Priority.SOMETIMES);
        gridPane.getColumnConstraints().add(columnConstraints);
        gridPane.getRowConstraints().add(rowConstraints);
        gridPane.setPadding(new Insets(this.shadowRadius));
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gridPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
        this.node.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.color(0, 0,0, 0.3), this.shadowRadius, 0, 0, 0));
        gridPane.getChildren().add(this.node);
        return gridPane;
    }
}
