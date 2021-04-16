package net.dajman.rentalcar.ui.builder.alert;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.ui.builder.Builder;

public class AlertBuilder implements Builder<Alert> {

    protected Alert.AlertType alertType;
    protected String title;
    protected String headerText;
    protected String contentText;
    protected Node graphic;

    public AlertBuilder(final Alert.AlertType alertType, final String contentText){
        this(alertType, null, contentText);
    }

    public AlertBuilder(final Alert.AlertType alertType, final String title, final String contentText){
        this(alertType, title, contentText, null, null);
    }

    public AlertBuilder(final Alert.AlertType alertType, final String title, final String contentText, final String headerText, final Node graphic){
        this.alertType = alertType;
        this.title = title;
        this.contentText = contentText;
        this.headerText = headerText;
        this.graphic = graphic;
    }

    public AlertBuilder setAlertType(Alert.AlertType alertType) {
        this.alertType = alertType;
        return this;
    }

    public AlertBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public AlertBuilder setHeaderText(String headerText) {
        this.headerText = headerText;
        return this;
    }

    public AlertBuilder setContentText(String contentText) {
        this.contentText = contentText;
        return this;
    }

    public AlertBuilder setGraphic(Node graphic) {
        this.graphic = graphic;
        return this;
    }

    public Alert buildAndShow(){
        final Alert alert = this.build();
        alert.show();
        return alert;
    }

    @Override
    public Alert build() {
        final Alert alert = new Alert(alertType);
        if (alertType == Alert.AlertType.ERROR && this.title == null){
            alert.setTitle("Błąd");
        }else alert.setTitle(title);
        alert.setContentText(contentText);
        alert.setGraphic(graphic);
        alert.setHeaderText(headerText);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(App.getInstance().getStage().getIcons().get(0));
        return alert;
    }
}
