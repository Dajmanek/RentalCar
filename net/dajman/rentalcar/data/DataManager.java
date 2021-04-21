package net.dajman.rentalcar.data;

import javafx.beans.property.ReadOnlyDoubleWrapper;

public interface DataManager {

    boolean load();
    boolean load(final ReadOnlyDoubleWrapper progress);
    boolean save();
    boolean save(final ReadOnlyDoubleWrapper progress);

}
