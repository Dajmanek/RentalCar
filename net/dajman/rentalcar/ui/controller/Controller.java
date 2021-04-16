package net.dajman.rentalcar.ui.controller;

import net.dajman.rentalcar.ui.NodeType;

public abstract class Controller implements IController{

    private boolean firstInit = false;

    public void init(Object... objects){
        if (!this.firstInit){
            this.firstInit = true;
            this.firstInitialize();
        }
        this.initialize(objects);
    }

    protected void initialize(Object... objects){}

    protected void firstInitialize(){}

    public abstract NodeType getType();

}
