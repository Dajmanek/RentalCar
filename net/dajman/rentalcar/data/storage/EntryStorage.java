package net.dajman.rentalcar.data.storage;

import net.dajman.rentalcar.basic.Entry;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class EntryStorage<T extends Entry<T>>  extends Storage<T> {

    private int maxId = 0;

    public EntryStorage() {
        super();
    }

    public int getNextId(){
        return ++this.maxId;
    }

    public T get(final int id){
        return this.set.parallelStream().filter(entry -> entry.getId() == id).findFirst().orElse(null);
    }

    @Override
    public boolean addAll(Set<T> objects) {
        if (objects == null){
            return false;
        }
        final int maxId = objects.parallelStream().map(T::getId).mapToInt(v -> v).max().orElseGet(() -> 0);
        this.maxId = Math.max(this.maxId, maxId);
        return super.addAll(objects);
    }

    public boolean add(final T entry){
        this.maxId = Math.max(this.maxId, entry.getId());
        return super.add(entry);
    }


    public Set<T> search(final String entireText){
        if (entireText == null || entireText.isBlank()){
            return this.getAll();
        }
        final Set<T> set = new HashSet<>();
        final String[] splited = entireText.toLowerCase().split(" ");
        for(String text: splited){
            if (text.isBlank())
                continue;
            set.addAll(this.getAll().parallelStream().filter(entry -> entry.isSimilar(text)).collect(Collectors.toSet()));
        }
        return set;
    }
}
