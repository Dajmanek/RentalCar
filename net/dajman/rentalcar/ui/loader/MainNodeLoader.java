package net.dajman.rentalcar.ui.loader;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.ui.builder.shadow.ShadowBoxBuilder;
import net.dajman.rentalcar.ui.helpers.DragHelper;
import net.dajman.rentalcar.ui.helpers.ResizeHelper;


public class MainNodeLoader extends NodeLoader{


    private GridPane shadowBox;
    private GridPane mainPane;

    public MainNodeLoader(String styleFileName) {
        super(styleFileName);
        this.mainPane = (GridPane) this.getParent();
        this.shadowBox = new ShadowBoxBuilder(this.mainPane, App.SHADOW_RADIUS).build();
    }

    public GridPane getMainPane() {
        return mainPane;
    }

    public void insertNode(final NodeLoader nodeLoader){
        if (this.mainPane.getChildren().size() > 1){
            this.mainPane.getChildren().remove(1);
        }
        this.mainPane.add(nodeLoader.getParent(), 0, 1);
    }

    public void registerDrag(final DragHelper dragHelper){
        final GridPane topMenu = (GridPane) this.mainPane.getChildren().get(0);
        dragHelper.register(topMenu);
        dragHelper.register(topMenu.getChildren().get(1));
    }

    public void registerResize(final ResizeHelper resizeHelper){
        final GridPane topMenu = (GridPane) this.mainPane.getChildren().get(0);
        resizeHelper.register(topMenu.getChildren().get(1));
    }

    public Scene getScene(){
        final Scene scene = new Scene(this.shadowBox);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(App.STYLESHEET);
        return scene;
    }

}
