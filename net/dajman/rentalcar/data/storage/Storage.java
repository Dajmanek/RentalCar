package net.dajman.rentalcar.data.storage;

import java.util.HashSet;
import java.util.Set;

public class Storage<T> {

    protected final Set<T> set;

    public Storage(){
        this.set = new HashSet<>();
    }

    public boolean add(final T object){
        return this.set.add(object);
    }

    public boolean addAll(final Set<T> objects){
        return this.set.addAll(objects);
    }

    public boolean remove(final T object){
        return this.set.remove(object);
    }

    public Set<T> getAll(){
        return new HashSet<>(this.set);
    }

    public void clear(){
        this.set.clear();
    }

}
