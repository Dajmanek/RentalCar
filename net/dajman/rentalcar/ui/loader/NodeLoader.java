package net.dajman.rentalcar.ui.loader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import net.dajman.rentalcar.App;

import java.io.IOException;

public class NodeLoader {

    protected final String css;
    private Parent parent;


    public NodeLoader(final String styleFileName){
        this.css = App.class.getResource("ui/css/style.css").toExternalForm();
        final FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("ui/fxml/" + styleFileName + ".fxml"));
        try{
            this.parent = (Parent)fxmlLoader.load();
            this.parent.getStylesheets().add(css);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public Parent getParent() {
        return parent;
    }


}
