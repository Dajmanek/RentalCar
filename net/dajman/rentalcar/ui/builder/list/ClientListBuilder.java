package net.dajman.rentalcar.ui.builder.list;

import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.basic.Client;
import net.dajman.rentalcar.ui.utils.Colors;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.ui.transition.BackgroundColorTransition;
import net.dajman.rentalcar.ui.utils.Images;

import java.util.Set;

public class ClientListBuilder extends EntryListBuilder<Client> {

    private static final transient int ROW_HEIGHT = 65;
    private static final transient double IMAGE_HEIGHT = 45;

    public ClientListBuilder(final Set<Client> entrySet){
        super(entrySet);
    }

    @Override
    public Node build() {
        if (this.object == null || this.object.isEmpty()){
            return null;
        }
        final GridPane gridPane = new GridPane();
        gridPane.getStylesheets().add(App.class.getResource("ui/css/style.css").toExternalForm());
        gridPane.setMaxWidth(Double.MAX_VALUE);
        final ColumnConstraints column = new ColumnConstraints();
        column.setMinWidth(10);
        column.setHgrow(Priority.SOMETIMES);
        gridPane.getColumnConstraints().addAll(column);
        gridPane.setPrefHeight(ROW_HEIGHT * this.object.size());

        int index = 0;
        // CREATE ROW
        for(Client client : this.object){
            final RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(ROW_HEIGHT);
            gridPane.getRowConstraints().add(rowConstraints);


            // BORDER CONTAINER
            final BorderPane borderPane = new BorderPane();
            borderPane.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            borderPane.getStyleClass().add("underline");
            borderPane.setMaxWidth(Double.MAX_VALUE);
            borderPane.setCursor(Cursor.HAND);
            // BORDER CONTAINER (ANIMATION)
            borderPane.setOnMouseEntered(e -> new BackgroundColorTransition(Colors.LIST_ROW_BACKGROUND, Colors.LIST_ROW_BACKGROUND_HOVER, Duration.millis(200), borderPane).playFromStart());
            borderPane.setOnMouseExited(e -> new BackgroundColorTransition(Colors.LIST_ROW_BACKGROUND_HOVER, Colors.LIST_ROW_BACKGROUND, Duration.millis(200), borderPane).playFromStart());
            // BORDER CONTAINER (ON CLICK)
            borderPane.setOnMouseClicked(e -> App.getInstance().openGui(NodeType.CLIENT, client));
            gridPane.add(borderPane, 0, index++);

            // MAIN HBOX
            final HBox hBox = new HBox();
            hBox.setPrefHeight(60);
            hBox.setSpacing(20);
            hBox.setPadding(new Insets(5, 10, 5, 10));
            BorderPane.setAlignment(hBox, Pos.CENTER_LEFT);
            borderPane.setLeft(hBox);

            // IMAGE
            final BorderPane imageBorderPane = new BorderPane();
            hBox.getChildren().add(imageBorderPane);
            imageBorderPane.setStyle("-fx-background-color: rgba(0,0,0,0)");

            final ImageView imageView = new ImageView(Images.imageUser);
            imageView.setFitHeight(45);
            imageView.setFitWidth(45);
            BorderPane.setAlignment(imageView, Pos.CENTER);
            imageBorderPane.setCenter(imageView);

            // LEFT DATA
            final VBox vBox = new VBox();
            vBox.setPrefHeight(60);
            vBox.setPrefWidth(310);
            vBox.setPadding(new Insets(5, 0, 0, 0));
            hBox.getChildren().add(vBox);

            // LEFT DATA (FIRST LINE)
            final HBox hBoxName = new HBox();
            hBoxName.setSpacing(5);
            vBox.getChildren().add(hBoxName);
            final Label labelName = new Label("Imię i nazwisko:");
            labelName.setPrefHeight(20);
            labelName.setFont(Font.font("System", FontWeight.BOLD, 13));
            labelName.setTextFill(Paint.valueOf("#5b5a5a"));
            final Label labelNameValue = new Label(client.getFirstName() + " " + client.getLastName());
            labelNameValue.setPrefHeight(20);
            labelNameValue.setFont(Font.font("System", FontWeight.NORMAL, 13));
            hBoxName.getChildren().addAll(labelName, labelNameValue);

            // LEFT DATA (SECOND LINE)
            final HBox hBoxPhone = new HBox();
            hBoxPhone.setSpacing(5);
            vBox.getChildren().add(hBoxPhone);
            final Label labelPhone = new Label("Nr telefonu:");
            labelPhone.setPrefHeight(20);
            labelPhone.setFont(Font.font("System", FontWeight.BOLD, 13));
            labelPhone.setTextFill(Paint.valueOf("#5b5a5a"));
            final Label labelPhoneValue = new Label(client.getPhoneNumber());
            labelPhoneValue.setPrefHeight(20);
            labelPhoneValue.setFont(Font.font("System", FontWeight.NORMAL, 13));
            hBoxPhone.getChildren().addAll(labelPhone, labelPhoneValue);



            // RIGHT DATA
            final VBox vBox1 = new VBox();
            vBox1.setPrefHeight(60);
            vBox1.setPrefWidth(270);
            vBox1.setPadding(new Insets(5, 0, 0 ,0));
            hBox.getChildren().add(vBox1);

            // RIGHT (FIRST LINE)
            final HBox hBoxRented = new HBox();
            hBoxRented.setSpacing(5);
            vBox1.getChildren().add(hBoxRented);
            final Label labelRented = new Label("Wypożyczonych samochodów:");
            labelRented.setPrefHeight(20);
            labelRented.setFont(Font.font("System", FontWeight.BOLD, 13));
            labelRented.setTextFill(Paint.valueOf("#5b5a5a"));
            final Label labelRentedValue = new Label(Integer.toString(client.getRentedCars().size()));
            labelRentedValue.setPrefHeight(20);
            labelRentedValue.setFont(Font.font("System", FontWeight.NORMAL, 13));
            hBoxRented.getChildren().addAll(labelRented, labelRentedValue);

            // RIGHT DATA (SECOND LINE)
            final HBox hBoxToPay = new HBox();
            hBoxToPay.setSpacing(5);
            vBox1.getChildren().add(hBoxToPay);
            final Label labelToPay = new Label("Do zapłaty:");
            labelToPay.setPrefHeight(20);
            labelToPay.setFont(Font.font("System", FontWeight.BOLD, 13));
            labelToPay.setTextFill(Paint.valueOf("#5b5a5a"));
            final Label labelToPayValue = new Label(String.format("%.2f", client.toPay()));
            labelToPayValue.setPrefHeight(20);
            labelToPayValue.setFont(Font.font("System", FontWeight.NORMAL, 13));
            hBoxToPay.getChildren().addAll(labelToPay, labelToPayValue);
        }
        return gridPane;
    }
}
