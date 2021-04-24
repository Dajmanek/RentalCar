package net.dajman.rentalcar.ui.alert;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.ui.builder.shadow.ShadowBoxBuilder;
import net.dajman.rentalcar.ui.helpers.DragHelper;
import net.dajman.rentalcar.ui.transition.ImageTransition;
import net.dajman.rentalcar.ui.utils.Images;

public class Alert {

    protected static final transient String BUTTON_CLOSE = "Zamknij";

    protected Stage stage;
    private Runnable onClose;


    protected Label label;
    protected Button button;
    protected BorderPane centerPane;

    public Alert(final String text){
        this(null, text, BUTTON_CLOSE);
    }

    public Alert(final String title, final String text){
        this(title, text, BUTTON_CLOSE);
    }

    public Alert(final String title, final String text, final String buttonText){
        this.stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        final GridPane gridPane = new GridPane();

        gridPane.getRowConstraints().add(new RowConstraints(30, 30, 30));
        gridPane.getRowConstraints().add(new RowConstraints(30, -1, 300));
        gridPane.getRowConstraints().add(new RowConstraints(0, -1, 30));
        gridPane.getColumnConstraints().add(new ColumnConstraints(400));
        gridPane.setMaxWidth(400);
        gridPane.getStyleClass().add("mainContainer");


        final BorderPane topPane = new BorderPane();
        topPane.getStyleClass().add("menuBar");
        topPane.setPadding(new Insets(0, 5, 0, 5));
        gridPane.add(topPane, 0, 0);
        final ImageView imageView = new ImageView(Images.btnClose);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        BorderPane.setAlignment(imageView, Pos.CENTER_RIGHT);
        topPane.setRight(imageView);

        imageView.setOnMouseClicked(mouseEvent -> this.close());
        imageView.setOnMouseEntered(mouseEvent -> new ImageTransition(imageView, Images.btnClose, Images.btnCloseHover, Duration.millis(200)).playFromStart());
        imageView.setOnMouseExited(mouseEvent -> new ImageTransition(imageView, Images.btnCloseHover, Images.btnClose, Duration.millis(200)).playFromStart());

        final HBox hBox = new HBox();
        hBox.setSpacing(10);
        BorderPane.setAlignment(hBox, Pos.CENTER_LEFT);
        topPane.setLeft(hBox);

        final BorderPane iconPane = new BorderPane();
        hBox.getChildren().add(iconPane);
        final ImageView imageViewIcon = new ImageView(Images.icon);
        imageViewIcon.setFitWidth(20);
        imageViewIcon.setFitHeight(20);
        iconPane.setCenter(imageViewIcon);

        final BorderPane titlePane = new BorderPane();
        hBox.getChildren().add(titlePane);
        titlePane.setCenter(new Label(title));


        this.centerPane = new BorderPane();
        this.centerPane.setPadding(new Insets(30, 30, 30, 30));
        gridPane.add(this.centerPane, 0, 1);
        this.label = new Label(text);
        BorderPane.setAlignment(label, Pos.TOP_CENTER);
        this.centerPane.setCenter(this.label);

        final BorderPane bottomPane = new BorderPane();
        bottomPane.setPadding(new Insets(0, 15, 15 ,0));
        gridPane.add(bottomPane, 0, 2);

        this.button = new Button(buttonText);
        this.button.setOnAction(event -> this.close());
        bottomPane.setRight(this.button);


        final GridPane container = new ShadowBoxBuilder(gridPane, 10).build();
        final Scene scene = new Scene(container);
        scene.getStylesheets().add(App.STYLESHEET);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setAlwaysOnTop(true);

        final DragHelper dragHelper = new DragHelper(this.stage, App.SHADOW_RADIUS);
        dragHelper.register(topPane);
    }


    public void setText(final String text){
        this.label.setText(text);
    }

    public void setButtonText(final String text){
        this.button.setText(text);
    }

    public void runOnClose(final Runnable runnable){
        this.onClose = runnable;
    }


    public void close(){
        this.stage.close();
        if (this.onClose != null) this.onClose.run();
    }

    public Alert show(){
        this.stage.show();
        return this;
    }

}
