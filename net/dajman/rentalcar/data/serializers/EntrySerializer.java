package net.dajman.rentalcar.data.serializers;

import net.dajman.rentalcar.basic.Entry;
import net.dajman.rentalcar.data.Serializer;
import net.dajman.rentalcar.exceptions.DeserializationException;

import java.util.*;
import java.util.stream.Collectors;

public class EntrySerializer<T extends Entry<T>> implements Serializer<T> {

    private T entry;

    // Only to serialization
    public EntrySerializer(){
    }

    public EntrySerializer(final T entry){
        this.entry = entry;
    }

    @Override
    public List<String> serialize(final Set<T> entrySet){
        if (entrySet.isEmpty())
            return Collections.emptyList();
        return entrySet.stream().map(T::serialize).collect(Collectors.toList());
    }

    @Override
    public List<String> serialize(final Set<T> entrySet, final int buf){
        if (entrySet.isEmpty() || buf < 1)
            return Collections.emptyList();
        final List<String> list = new ArrayList<>();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for(T entry : entrySet){
            if (i >= buf){
                list.add(sb.toString());
                sb = new StringBuilder();
                i = 0;
            }
            if (i != 0) sb.append(';');
            sb.append(entry.serialize());
            i++;
        }
        return list;
    }

    @Override
    public String serialize(final T entry){
        return entry.serialize();
    }

    @Override
    public T deserialize(final String text) throws DeserializationException {
        return this.entry.deserialize(text);
    }

    @Override
    public Set<T> deserialize(final List<String> list) throws DeserializationException{
        if (list == null || list.isEmpty()){
            return Collections.emptySet();
        }
        final Set<T> entrySet = new HashSet<>();
        for(String text : list){
            final String[] entrySplited = text.split(";");
            for(String entryText: entrySplited){
                entrySet.add(this.entry.deserialize(entryText));
            }
        }
        return entrySet;
    }

}
