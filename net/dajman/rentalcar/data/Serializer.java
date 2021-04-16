package net.dajman.rentalcar.data;

import net.dajman.rentalcar.exceptions.DeserializationException;

import java.util.List;
import java.util.Set;

public interface Serializer<T> {

    String serialize(T object);
    List<String> serialize(Set<T> objects);
    List<String> serialize(Set<T> object, int buf);
    T deserialize(String text) throws DeserializationException;
    Set<T> deserialize(List<String> list) throws DeserializationException;

}
