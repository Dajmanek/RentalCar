package net.dajman.rentalcar.ui.builder.list;

import javafx.scene.Node;
import net.dajman.rentalcar.basic.Entry;
import net.dajman.rentalcar.ui.builder.Builder;

import java.util.Set;

public abstract class EntryListBuilder<T extends Entry<T>> implements Builder<Node> {

    protected Set<T> object;

    public EntryListBuilder(Set<T> object) {
        this.object = object;
    }


}
