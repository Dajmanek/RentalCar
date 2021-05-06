package net.dajman.rentalcar.ui.builder.list;

import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.basic.Car;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.ui.utils.Colors;
import net.dajman.rentalcar.ui.controller.controllers.ClientController;
import net.dajman.rentalcar.ui.transition.fx.BackgroundColorTransition;
import net.dajman.rentalcar.ui.utils.Images;
import net.dajman.rentalcar.utils.TimeUtil;

import java.util.Optional;
import java.util.Set;

public class CarListBuilder extends EntryListBuilder<Car> {

    private final ListType listType;

    public CarListBuilder(final ListType listType, final Set<Car> entrySet){
        super(entrySet);
        this.listType = listType;
    }

    @Override
    public Node build() {
        if (this.object == null || this.object.isEmpty()){
            return null;
        }
        return switch (this.listType){
            case MAIN -> this.buildMain();
            case CLIENT_RENTED -> this.buildClientRented();
            case CLIENT_AVAILABLE -> this.buildClientAvailable();
        };
    }

    private Node buildMain(){
        final GridPane gridPane = new GridPane();
        gridPane.getStylesheets().add(App.STYLESHEET);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        final ColumnConstraints column = new ColumnConstraints();
        column.setMinWidth(10);
        column.setHgrow(Priority.SOMETIMES);
        gridPane.getColumnConstraints().addAll(column);
        gridPane.setPrefHeight(88 * this.object.size());
        gridPane.setMaxHeight(Double.MAX_VALUE);

        int index = 0;
        for(Car car : this.object){
            // CREATE ROW
            final RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(88);
            gridPane.getRowConstraints().add(rowConstraints);
            // BORDER CONTAINER
            final BorderPane borderPane = new BorderPane();
            borderPane.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            borderPane.getStyleClass().add("underline");
            borderPane.setMaxWidth(Double.MAX_VALUE);
            borderPane.setCursor(Cursor.HAND);
            // ON CLICK
            borderPane.setOnMouseClicked(e -> App.getInstance().openGui(NodeType.CAR, car));
            // ANIMATION
            borderPane.setOnMouseEntered(e -> new BackgroundColorTransition(Colors.LIST_ROW_BACKGROUND, Colors.LIST_ROW_BACKGROUND_HOVER, Duration.millis(200), borderPane).playFromStart());
            borderPane.setOnMouseExited(e -> new BackgroundColorTransition(Colors.LIST_ROW_BACKGROUND_HOVER, Colors.LIST_ROW_BACKGROUND, Duration.millis(200), borderPane).playFromStart());
            gridPane.add(borderPane, 0, index++);
            // LEFT HBOX
            final HBox hBox = new HBox();
            hBox.setPadding(new Insets(5, 10, 5, 10));
            hBox.setSpacing(10);
            BorderPane.setAlignment(hBox, Pos.CENTER_LEFT);
            borderPane.setLeft(hBox);
            // IMAGE
            final BorderPane imageBorderPane = new BorderPane();
            imageBorderPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
            imageBorderPane.setPrefWidth(180);
            imageBorderPane.setMaxWidth(180);
            hBox.getChildren().add(imageBorderPane);
            final Image image = Optional.ofNullable(car.getImage()).orElse(Images.imageEmpty);
            final ImageView imageView = new ImageView(image);
            imageView.setFitHeight(75);
            imageView.setPreserveRatio(true);
            imageBorderPane.setCenter(imageView);
            // LEFT HBOX DATA
            final VBox vBox = new VBox();
            vBox.setPrefWidth(340);
            vBox.setPadding(new Insets(18, 0, 0, 0));
            hBox.getChildren().add(vBox);
            // LEFT BOX (Car.brand)
            final HBox hBoxBrand = new HBox();
            hBoxBrand.setSpacing(5);
            vBox.getChildren().add(hBoxBrand);
            final Label labelBrand = new Label("Marka:");
            labelBrand.setPrefHeight(20);
            labelBrand.setFont(Font.font("System", FontWeight.BOLD, 13));
            labelBrand.setTextFill(Paint.valueOf("#5b5a5a"));
            final Label labelBrandValue = new Label(car.getBrand());
            labelBrandValue.setPrefHeight(20);
            labelBrandValue.setFont(Font.font("System", FontWeight.NORMAL, 13));
            hBoxBrand.getChildren().addAll(labelBrand, labelBrandValue);
            // LEFT BOX (Car.model)
            final HBox hBoxModel = new HBox();
            hBoxModel.setSpacing(5);
            vBox.getChildren().add(hBoxModel);
            final Label labelModel = new Label("Model:");
            labelModel.setPrefHeight(20);
            labelModel.setFont(Font.font("System", FontWeight.BOLD, 13));
            labelModel.setTextFill(Paint.valueOf("#5b5a5a"));
            final Label labelModelValue = new Label(car.getModel());
            labelModelValue.setPrefHeight(20);
            labelModelValue.setFont(Font.font("System", FontWeight.NORMAL, 13));
            hBoxModel.getChildren().addAll(labelModel, labelModelValue);
            // PRICE
            final HBox hBoxPrice = new HBox();
            hBoxPrice.setPadding(new Insets(29, 50, 0, 0));
            hBoxPrice.setSpacing(5);
            BorderPane.setAlignment(hBoxPrice, Pos.CENTER_RIGHT);
            borderPane.setRight(hBoxPrice);
            final Label labelPrice = new Label("Cena:");
            labelPrice.setPrefHeight(20);
            labelPrice.setFont(Font.font("System", FontWeight.BOLD, 13));
            labelPrice.setTextFill(Paint.valueOf("#5b5a5a"));
            final Label labelPriceValue = new Label(String.format("%.2f zł/h", car.getPrice()));
            labelPriceValue.setPrefHeight(20);
            labelPriceValue.setFont(Font.font("System", FontWeight.NORMAL, 13));
            hBoxPrice.getChildren().addAll(labelPrice, labelPriceValue);
        }

        return gridPane;
    }

    private Node buildClientRented(){
        final GridPane gridPane = new GridPane();
        gridPane.getStylesheets().add(App.STYLESHEET);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        final ColumnConstraints column = new ColumnConstraints();
        column.setMinWidth(10);
        column.setHgrow(Priority.SOMETIMES);
        gridPane.getColumnConstraints().add(column);
        gridPane.setPrefHeight(80 * this.object.size());

        int index = 0;
        for(Car car : this.object){
            final BorderPane borderPane = new BorderPane();
            borderPane.getStyleClass().add("underline");
            borderPane.setPadding(new Insets(3, 5, 3, 5));
            GridPane.setRowIndex(borderPane, index++);
            GridPane.setColumnIndex(borderPane, 0);
            gridPane.getChildren().add(borderPane);
            borderPane.setCursor(Cursor.HAND);
            borderPane.setOnMouseEntered(e -> new BackgroundColorTransition(Colors.LIST_ROW_BACKGROUND, Colors.LIST_ROW_BACKGROUND_HOVER, Duration.millis(200), borderPane).playFromStart());
            borderPane.setOnMouseExited(e -> new BackgroundColorTransition(Colors.LIST_ROW_BACKGROUND_HOVER, Colors.LIST_ROW_BACKGROUND, Duration.millis(200), borderPane).playFromStart());
            borderPane.setOnMouseClicked(mouseEvent -> App.getInstance().openGui(NodeType.CAR, car));
            // LEFT
            final HBox hBox = new HBox();
            hBox.setMinWidth(280);
            hBox.setSpacing(5);
            BorderPane.setAlignment(hBox, Pos.CENTER_LEFT);
            borderPane.setLeft(hBox);
            final BorderPane imageBorderPane = new BorderPane();
            hBox.getChildren().add(imageBorderPane);
            imageBorderPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
            final ImageView imageView = new ImageView(Optional.ofNullable(car.getImage()).orElse(Images.imageEmpty));
            imageView.setFitWidth(45);
            imageView.setPreserveRatio(true);
            BorderPane.setAlignment(imageBorderPane, Pos.CENTER);
            imageBorderPane.setCenter(imageView);
            final VBox vBox = new VBox();
            vBox.setMinWidth(230);
            hBox.getChildren().add(vBox);
            final HBox hBox1 = new HBox();
            hBox1.setSpacing(3);
            hBox1.setFillHeight(true);
            vBox.getChildren().add(hBox1);
            final Label key1 = new Label("Samochód:");
            key1.setFont(Font.font("System", FontWeight.BOLD, 12));
            final Label value1 = new Label(car.getBrand() + " " + car.getModel());
            hBox1.getChildren().addAll(key1, value1);
            final HBox hBox2 = new HBox();
            hBox2.setSpacing(3);
            hBox2.setFillHeight(true);
            vBox.getChildren().add(hBox2);
            final Label key2 = new Label("Data wypożyczenia:");
            key2.setFont(Font.font("System", FontWeight.BOLD, 12));
            final Label value2 = new Label(TimeUtil.getDate(car.getRentalDate()));
            hBox2.getChildren().addAll(key2, value2);
            final HBox hBox3 = new HBox();
            hBox3.setSpacing(3);
            hBox3.setFillHeight(true);
            vBox.getChildren().add(hBox3);
            final Label key3 = new Label("Upłynęło:");
            key3.setFont(Font.font("System", FontWeight.BOLD, 12));
            final Label value3 = new Label(TimeUtil.getDurationBreakdownShort(System.currentTimeMillis() - car.getRentalDate()));
            hBox3.getChildren().addAll(key3, value3);
            final HBox hBox4 = new HBox();
            hBox4.setSpacing(3);
            hBox4.setFillHeight(true);
            vBox.getChildren().add(hBox4);
            final Label key4 = new Label("Do zapłaty:");
            key4.setFont(Font.font("System", FontWeight.BOLD, 12));
            final Label value4 = new Label(String.format("%.02f", car.toPay()));
            hBox4.getChildren().addAll(key4, value4);
            // BUTTON (RIGHT)
            final Button button = new Button("Zwrot");
            button.setOnAction(actionEvent -> ((ClientController)App.getInstance().getController(NodeType.CLIENT)).returnCar(car));
            BorderPane.setAlignment(button, Pos.CENTER_RIGHT);
            borderPane.setRight(button);

        }

        return gridPane;
    }

    private Node buildClientAvailable(){
        final GridPane gridPane = new GridPane();
        gridPane.getStylesheets().add(App.STYLESHEET);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        final ColumnConstraints column = new ColumnConstraints();
        column.setMinWidth(10);
        column.setHgrow(Priority.SOMETIMES);
        gridPane.getColumnConstraints().add(column);
        gridPane.setPrefHeight(50 * this.object.size());

        int index = 0;
        for(Car car : this.object){
            final BorderPane borderPane = new BorderPane();
            borderPane.getStyleClass().add("underline");
            borderPane.setPadding(new Insets(3, 5, 3, 5));
            GridPane.setRowIndex(borderPane, index++);
            GridPane.setColumnIndex(borderPane, 0);
            gridPane.getChildren().add(borderPane);
            borderPane.setCursor(Cursor.HAND);
            borderPane.setOnMouseEntered(e -> new BackgroundColorTransition(Colors.LIST_ROW_BACKGROUND, Colors.LIST_ROW_BACKGROUND_HOVER, Duration.millis(200), borderPane).playFromStart());
            borderPane.setOnMouseExited(e -> new BackgroundColorTransition(Colors.LIST_ROW_BACKGROUND_HOVER, Colors.LIST_ROW_BACKGROUND, Duration.millis(200), borderPane).playFromStart());
            borderPane.setOnMouseClicked(mouseEvent -> App.getInstance().openGui(NodeType.CAR, car));
            // LEFT
            final HBox hBox = new HBox();
            hBox.setMinWidth(280);
            hBox.setSpacing(5);
            BorderPane.setAlignment(hBox, Pos.CENTER_LEFT);
            borderPane.setLeft(hBox);
            final BorderPane imageBorderPane = new BorderPane();
            hBox.getChildren().add(imageBorderPane);
            imageBorderPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
            final ImageView imageView = new ImageView(Optional.ofNullable(car.getImage()).orElse(Images.imageEmpty));
            imageView.setFitWidth(45);
            imageView.setPreserveRatio(true);
            BorderPane.setAlignment(imageBorderPane, Pos.CENTER);
            imageBorderPane.setCenter(imageView);
            final BorderPane dataBorderPane = new BorderPane();
            dataBorderPane.setMinWidth(190);
            dataBorderPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
            hBox.getChildren().add(dataBorderPane);
            final VBox vBox = new VBox();
            vBox.setMinWidth(180);
            dataBorderPane.setCenter(vBox);
            final HBox hBox1 = new HBox();
            hBox1.setPadding(new Insets(5, 0, 0 ,0));
            hBox1.setSpacing(3);
            hBox1.setFillHeight(true);
            vBox.getChildren().add(hBox1);
            final Label key1 = new Label("Samochód:");
            key1.setFont(Font.font("System", FontWeight.BOLD, 12));
            final Label value1 = new Label(car.getBrand() + " " + car.getModel());
            hBox1.getChildren().addAll(key1, value1);
            final HBox hBox2 = new HBox();
            hBox2.setSpacing(3);
            hBox2.setFillHeight(true);
            vBox.getChildren().add(hBox2);
            final Label key2 = new Label("Cena:");
            key2.setFont(Font.font("System", FontWeight.BOLD, 12));
            final Label value2 = new Label(String.format("%.02f", car.getPrice()));
            hBox2.getChildren().addAll(key2, value2);
            // BUTTON (RIGHT)
            final Button button = new Button("Wypożycz");
            button.setOnAction(actionEvent -> ((ClientController)App.getInstance().getController(NodeType.CLIENT)).rentCar(car));
            BorderPane.setAlignment(button, Pos.CENTER_RIGHT);
            borderPane.setRight(button);
        }
        return gridPane;
    }


    public enum ListType{
        MAIN,
        CLIENT_RENTED,
        CLIENT_AVAILABLE;
    }

}
