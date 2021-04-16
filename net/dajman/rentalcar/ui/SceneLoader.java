package net.dajman.rentalcar.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.ui.builder.menu.MainMenuBarBuilder;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

public class SceneLoader{

    private final String path;
    private final String css;
    private Parent parent;

    public SceneLoader(final String styleFileName){
        this.path = "ui/fxml/" + styleFileName + ".fxml";
        this.css = App.class.getResource("ui/css/style.css").toExternalForm();
        final FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(this.path));
        try{
            this.parent = (Parent)fxmlLoader.load();
            if (this.parent instanceof GridPane){
                final MenuBar menuBar = new MainMenuBarBuilder().build();
                App.getInstance().getResizeHelper().addListener(menuBar);
                App.getInstance().getDragHelper().register(menuBar);
                ((GridPane)this.parent).getChildren().add(menuBar);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Scene load(){
        final Scene scene = new Scene(this.parent);
        scene.getStylesheets().add(css);
        return scene;
    }



    public void show(){
        final Stage stage = App.getInstance().getStage();
        if (stage.isShowing()){
            stage.getScene().setRoot(this.parent);
            return;
        }
        stage.setScene(this.load());
        stage.show();
    }

}
