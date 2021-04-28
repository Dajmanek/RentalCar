package net.dajman.rentalcar.ui.alert;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ProgressAlert extends Alert{

    private static final transient String BUTTON_CANCEL = "Anuluj";
    private final ProgressIndicator progressIndicator;

    public ProgressAlert(final String text){
        this(null, text, BUTTON_CANCEL, new ProgressBar(0));
    }

    public ProgressAlert(final String text, final ProgressIndicator progressIndicator){
        this(null, text, BUTTON_CANCEL, progressIndicator);
    }

    public ProgressAlert(final String title, final String text, final ProgressIndicator progressIndicator){
        this(title, text, BUTTON_CANCEL, progressIndicator);
    }

    public ProgressAlert(final String title, final String text, final String buttonText, final ProgressIndicator progressIndicator){
        super(title, text, buttonText);
        this.progressIndicator = progressIndicator;
        if (this.progressIndicator instanceof ProgressBar){
            this.progressIndicator.setPrefWidth(250);
        }
        final VBox vBox = new VBox();
        BorderPane.setAlignment(vBox, Pos.CENTER);
        vBox.setSpacing(15);
        VBox.setMargin(vBox, new Insets(50, 0 ,0 ,0));
        this.centerPane.setCenter(vBox);

        final BorderPane labelPane = new BorderPane();
        labelPane.setCenter(this.label);

        final BorderPane progressPane = new BorderPane();
        progressPane.setCenter(this.progressIndicator);

        vBox.getChildren().addAll(labelPane, progressPane);
    }


    public void setProgress(final double progress){
        this.progressIndicator.setProgress(progress);
    }

    public void setText(final String text){
        this.label.setText(text);
    }

    public void setButtonText(final String text){
        this.button.setText(text);
    }

    public void bind(final ReadOnlyDoubleProperty progressProperty){
        this.progressIndicator.progressProperty().bind(progressProperty);
    }

    public void endLoading(final String text){
        this.label.setText(text);
        this.button.setText(BUTTON_CLOSE);
    }

    public ProgressAlert show(){
        super.show();
        return this;
    }

}
