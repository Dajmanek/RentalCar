package net.dajman.rentalcar.ui;

public class HistoryElement {

    private final NodeType nodeType;
    private final Object[] objects;

    public HistoryElement(final NodeType nodeType, final Object... objects){
        this.nodeType = nodeType;
        this.objects = objects;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public Object[] getObjects() {
        return objects;
    }
}
