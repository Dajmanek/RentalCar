package net.dajman.rentalcar.ui.builder.alert;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

public class ProgressAlertBuilder extends AlertBuilder{

    private static final transient String BUTTON_CANCEL = "Anuluj";
    private static final transient String BUTTON_CLOSE = "Zamknij";

    private ProgressIndicator progressIndicator;

    public ProgressAlertBuilder(Alert.AlertType alertType, String title, String contentText, final ProgressIndicator progressIndicator) {
        super(alertType, title, contentText);
        this.progressIndicator = progressIndicator;
    }

    public ProgressAlertBuilder setProgressIndicator(ProgressIndicator progressIndicator) {
        this.progressIndicator = progressIndicator;
        return this;
    }

    @Override
    public Alert build() {
        this.progressIndicator.setPrefWidth(300);

        final Alert alert = super.build();
        alert.getDialogPane().setPrefWidth(400);

        final BorderPane borderPane = new BorderPane();
        ProgressAlertBuilder.setButtonText(alert, BUTTON_CANCEL);
        borderPane.setPrefWidth(400);
        borderPane.setCenter(this.progressIndicator);

        final Label label = new Label(this.contentText);
        label.setId("customContentText");
        label.setPrefHeight(50);
        BorderPane.setAlignment(label, Pos.TOP_CENTER);
        borderPane.setTop(label);

        alert.getDialogPane().setContent(borderPane);
        return alert;
    }

    public static boolean endLoading(final Alert alert, final String contentText){
        if (alert == null){
            return false;
        }
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(new ButtonType(BUTTON_CLOSE, ButtonBar.ButtonData.CANCEL_CLOSE));
        if (contentText != null){
            final Node node = alert.getDialogPane().lookup("#customContentText");
            if (node == null) return false;
            if (!(node instanceof Label)){
                return false;
            }
            ((Label)node).setText(contentText);
        }
        return true;
    }

    public static boolean setContentText(final Alert alert, final String text){
        if (alert == null || text == null){
            return false;
        }
        final Node node = alert.getDialogPane().lookup("#customContentText");
        if (node == null) return false;
        if (!(node instanceof Label)){
            return false;
        }
        ((Label)node).setText(text);
        return true;
    }

    public static boolean setButtonText(final Alert alert, final String text){
        if (alert == null || text == null){
            return false;
        }
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(new ButtonType(text, ButtonBar.ButtonData.CANCEL_CLOSE));
        return true;
    }
}
