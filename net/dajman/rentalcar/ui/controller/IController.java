package net.dajman.rentalcar.ui.controller;

import net.dajman.rentalcar.ui.NodeType;

public interface IController {

    void init(Object... objects);
    NodeType getType();
}
