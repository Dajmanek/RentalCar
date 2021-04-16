package net.dajman.rentalcar.ui.builder.menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.ui.NodeType;
import net.dajman.rentalcar.ui.builder.Builder;
import net.dajman.rentalcar.ui.controller.controllers.MenuBarController;

public class MainMenuBarBuilder implements Builder<MenuBar> {

    @Override
    public MenuBar build() {
        final MenuBar menuBar = new MenuBar();
        /*menuBar.setPrefHeight(26);
        menuBar.setPrefWidth(600);*/

        final MenuBarController controller = (MenuBarController) App.getInstance().getController(NodeType.MENU_BAR);
        // FILE
        final Menu fileMenu = new Menu("Plik");
        final MenuItem openItem = new MenuItem("Otwórz");
        openItem.setOnAction(controller::onClickOpen);
        final MenuItem saveItem = new MenuItem("Zapisz");
        saveItem.setOnAction(controller::onClickSave);
        final MenuItem saveAsItem = new MenuItem("Zapisz jako...");
        saveAsItem.setOnAction(controller::onClickSaveAs);
        fileMenu.getItems().addAll(openItem, saveItem, saveAsItem);
        menuBar.getMenus().add(fileMenu);

        // ADD
        final Menu addMenu = new Menu("Dodaj");
        final MenuItem carItem = new MenuItem("Dodaj samochód");
        carItem.setOnAction(controller::onClickAddCar);
        final MenuItem clientItem = new MenuItem("Dodaj klienta");
        clientItem.setOnAction(controller::onClickAddClient);
        addMenu.getItems().addAll(carItem, clientItem);
        menuBar.getMenus().add(addMenu);

        GridPane.setColumnIndex(menuBar, 0);
        GridPane.setRowIndex(menuBar, 0);
        return menuBar;
    }
}
