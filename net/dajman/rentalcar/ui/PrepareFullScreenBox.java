package net.dajman.rentalcar.ui;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.dajman.rentalcar.ui.transition.PaddingTransition;
import net.dajman.rentalcar.ui.transition.SizeLocTransition;

import javax.swing.*;
import java.awt.*;

public class PrepareFullScreenBox {

    private final Stage stage;
    private final double shadowRadius;
    private JFrame prepareWindow;
    private JFXPanel jfxPanel;
    private Pane mainPane;
    private Pane pane;


    private SizeLocTransition sizeLocTransition;

    private PaddingTransition paddingTransition;
    private FadeTransition fadeTransition;

    private Task<Void> showTask;


    public PrepareFullScreenBox(final Stage stage){
        this(stage, 0);
    }

    public PrepareFullScreenBox(final Stage stage, final double shadowRadius){
        this.stage = stage;
        this.shadowRadius = shadowRadius;

        this.prepareWindow = new JFrame();
        this.prepareWindow.setType(Window.Type.UTILITY);
        this.prepareWindow.setUndecorated(true);
        this.prepareWindow.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));

        this.jfxPanel = new JFXPanel();

        this.jfxPanel.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        this.prepareWindow.add(this.jfxPanel);

        this.mainPane = new Pane();
        this.mainPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.mainPane.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        final Scene scene = new Scene(this.mainPane);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

        this.pane = new Pane();
        this.pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);


        pane.setStyle("" +
                "-fx-background-color: rgba(255.0, 255.0, 255.0, 0.06);" +
                "-fx-border-style: solid;" +
                "-fx-border-width: 1;" +
                "-fx-border-color: rgba(224.0, 224.0, 224.0, 0.1);" +
                "-fx-effect: dropshadow(three-pass-box, rgba(181, 181, 181, 0.5), 8, 0 ,0 ,0);");

        this.mainPane.getChildren().add(pane);
        jfxPanel.setScene(scene);

    }

    public void show(){
        if (this.prepareWindow != null && this.prepareWindow.isShowing()){
            return;
        }
        if (this.fadeTransition != null){
            this.fadeTransition.stop();
        }
        if (this.prepareWindow == null){
            return;
        }
        if (this.showTask != null && this.showTask.isRunning()){
            return;
        }

        final Screen screen = this.getScreen(this.stage.getX() + this.shadowRadius, this.stage.getY() + this.shadowRadius);
        final double screenWidth = screen.getBounds().getWidth();
        final double screenHeight = screen.getBounds().getHeight();

        this.prepareWindow.setSize((int) screenWidth, (int) screenHeight);
        this.prepareWindow.setLocation((int) screen.getBounds().getMinX(), (int)screen.getBounds().getMinY());
        jfxPanel.setSize((int) screen.getBounds().getWidth(), (int) screen.getBounds().getHeight());


        this.mainPane.setPrefSize(screenWidth, screenHeight);
        this.pane.setLayoutX(stage.getX() - screen.getBounds().getMinX());
        this.pane.setLayoutY(stage.getY() - screen.getBounds().getMinY());
        this.pane.setPrefSize(stage.getWidth(), stage.getHeight());
        this.mainPane.setOpacity(1.0);
        this.prepareWindow.setVisible(true);
        this.stage.toFront();
        this.sizeLocTransition = new SizeLocTransition(this.pane, screenWidth - 20, screenHeight - 10, 10, 10, Duration.millis(200));
        this.sizeLocTransition.setRate(1);

        this.sizeLocTransition.playFromStart();


        /*final Screen screen = this.getScreen(this.stage.getX() + this.shadowRadius, this.stage.getY() + this.shadowRadius);
        final double screenWidth = screen.getBounds().getWidth();
        final double screenHeight = screen.getBounds().getHeight();

        this.prepareWindow.setSize((int) screenWidth, (int) screenHeight);
        this.prepareWindow.setLocation((int) screen.getBounds().getMinX(), (int)screen.getBounds().getMinY());
        jfxPanel.setSize((int) screen.getBounds().getWidth(), (int) screen.getBounds().getHeight());

        this.gridPane.setPadding(new Insets(5, screenWidth - (stage.getX() + stage.getWidth() - this.shadowRadius), screenHeight - (stage.getY() + stage.getHeight() - this.shadowRadius), stage.getX() + this.shadowRadius));
        this.gridPane.setPrefSize(screenWidth, screenHeight);
        this.pane.setPrefSize(screenWidth, screenHeight);
        this.gridPane.setOpacity(1.0);
        this.prepareWindow.setVisible(true);
        this.stage.toFront();
        this.paddingTransition = new PaddingTransition(this.gridPane, new Insets(10, 10, 10, 10), Duration.millis(150));
        this.paddingTransition.playFromStart();*/

    }

    public void hide(){
        if (this.paddingTransition != null ){
            this.paddingTransition.stop();
        }
        if (this.prepareWindow == null || !this.prepareWindow.isShowing()){
            return;
        }
        // TEST
        this.pane.setPrefSize(5, 5);
        PrepareFullScreenBox.this.prepareWindow.setVisible(false);

        /*this.fadeTransition = new FadeTransition(Duration.millis(300), this.gridPane);
        fadeTransition.setOnFinished(event -> {
            this.pane.setPrefSize(5, 5);
            PrepareFullScreenBox.this.prepareWindow.setVisible(false);
        });
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        this.fadeTransition.playFromStart();*/


    }

    private Screen getScreen(final double x, final double y){
        return Screen.getScreens().stream().filter(screen -> screen.getBounds().contains(x, y)).findFirst().orElse(null);
    }



}
