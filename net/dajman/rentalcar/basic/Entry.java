package net.dajman.rentalcar.basic;

import net.dajman.rentalcar.data.storage.EntryStorage;
import net.dajman.rentalcar.exceptions.DeserializationException;

public abstract class Entry<T extends Entry<T>> {

    protected int id;

    protected Entry(){
    }
    
    protected Entry(final EntryStorage<T> entryStorage){
        this.id = entryStorage.getNextId();
    }

    public int getId() {
        return id;
    }

    public abstract String serialize();

    public abstract T deserialize(final String text) throws DeserializationException;

    public abstract boolean isSimilar(final String text);

}
