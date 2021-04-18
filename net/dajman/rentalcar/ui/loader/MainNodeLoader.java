package net.dajman.rentalcar.ui.loader;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import net.dajman.rentalcar.ui.helpers.DragHelper;

public class MainNodeLoader extends NodeLoader{

    public MainNodeLoader(String styleFileName) {
        super(styleFileName);
    }

    public void insertNode(final NodeLoader nodeLoader){
        final GridPane gridPane = (GridPane) this.getParent();
        if (gridPane.getChildren().size() > 1){
            gridPane.getChildren().remove(1);
        }
        gridPane.add(nodeLoader.getParent(), 0, 1);
    }


    public void registerDrag(final DragHelper dragHelper){
        final GridPane gridPane = (GridPane) this.getParent();
        final GridPane topMenu = (GridPane) gridPane.getChildren().get(0);
        dragHelper.register(topMenu);
        dragHelper.register(topMenu.getChildren().get(1));
    }

    public Scene getScene(){
        final Scene scene = new Scene(this.getParent());
        scene.getStylesheets().add(this.css);
        return scene;
    }


}
