package net.dajman.rentalcar.ui.controller.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.ui.controller.Controller;
import net.dajman.rentalcar.ui.transition.ImageTransition;

public class TestController extends Controller {


    public static TestController inst;

    public TestController(){
        inst = this;
    }


    @FXML
    private ImageView imageView1;
    @FXML
    private ImageView imageView2;
    @FXML
    private ImageView imageView3;

    private Image btn_exit;
    private Image btn_exit_t;
    private Image btn_restore;
    private Image btn_restore_t;
    private Image btn_minimize;
    private Image btn_minimize_t;




    @Override
    public NodeType getType() {
        return null;
    }

    @Override
    protected void firstInitialize() {
        btn_exit = new Image(App.class.getResourceAsStream("ui/img/btn_close.png"));
        btn_exit_t = new Image(App.class.getResourceAsStream("ui/img/btn_close_t.png"));
        btn_restore = new Image(App.class.getResourceAsStream("ui/img/btn_restore.png"));
        btn_restore_t = new Image(App.class.getResourceAsStream("ui/img/btn_restore_t.png"));
        btn_minimize = new Image(App.class.getResourceAsStream("ui/img/btn_minimize.png"));
        btn_minimize_t = new Image(App.class.getResourceAsStream("ui/img/btn_minimize_t.png"));

        imageView1.setImage(btn_minimize);
        imageView2.setImage(btn_restore);
        imageView3.setImage(btn_exit);

        imageView1.setOnMouseEntered(mouseEvent -> new ImageTransition(imageView1, btn_minimize, btn_minimize_t, Duration.millis(200)).playFromStart());
        imageView2.setOnMouseEntered(mouseEvent -> new ImageTransition(imageView2, btn_restore, btn_restore_t, Duration.millis(200)).playFromStart());
        imageView3.setOnMouseEntered(mouseEvent -> new ImageTransition(imageView3, btn_exit, btn_exit_t, Duration.millis(200)).playFromStart());

        imageView1.setOnMouseExited(mouseEvent -> new ImageTransition(imageView1, btn_minimize_t, btn_minimize, Duration.millis(200)).playFromStart());
        imageView2.setOnMouseExited(mouseEvent -> new ImageTransition(imageView2, btn_restore_t, btn_restore, Duration.millis(200)).playFromStart());
        imageView3.setOnMouseExited(mouseEvent -> new ImageTransition(imageView3, btn_exit_t, btn_exit, Duration.millis(200)).playFromStart());

    }

    @FXML
    public void onClick(final ActionEvent event){

    }
}
