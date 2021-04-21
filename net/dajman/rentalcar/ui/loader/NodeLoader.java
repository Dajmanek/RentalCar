package net.dajman.rentalcar.ui.loader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import net.dajman.rentalcar.App;

import java.io.IOException;

public class NodeLoader {

    private Parent parent;


    public NodeLoader(final String styleFileName){
        final FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("ui/resources/fxml/" + styleFileName + ".fxml"));
        try{
            this.parent = (Parent)fxmlLoader.load();
            this.parent.getStylesheets().add(App.STYLESHEET);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public Parent getParent() {
        return parent;
    }


}
