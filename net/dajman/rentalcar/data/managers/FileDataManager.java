package net.dajman.rentalcar.data.managers;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.basic.Car;
import net.dajman.rentalcar.basic.Client;
import net.dajman.rentalcar.data.DataManager;
import net.dajman.rentalcar.data.Serializer;
import net.dajman.rentalcar.data.serializers.EntrySerializer;
import net.dajman.rentalcar.exceptions.DeserializationException;

import java.io.*;
import java.util.*;

public class FileDataManager implements DataManager {

    public static final transient String FILE_EXTENSION = ".rcdat";
    private final File file;

    public FileDataManager(final File file){
        this.file = file;
    }

    @Override
    public boolean save(){
        return this.save(new ReadOnlyDoubleWrapper());
    }

    @Override
    public boolean load() {
        return this.load(new ReadOnlyDoubleWrapper());
    }


    @Override
    public boolean save(final ReadOnlyDoubleWrapper progress) {
        if (this.file == null){
            return false;
        }
        try {
            final FileOutputStream fos = new FileOutputStream(this.file);
            final Set<Client> clients = App.getInstance().getClientStorage().getAll();
            final Set<Car> cars = App.getInstance().getCarStorage().getAll();
            final int clientsAmount = clients.size();
            final int carsAmount = cars.size();
            // WRITING HEADER
            final byte[] headerBytes = new byte[12];
            this.writeInt(headerBytes, 0, clientsAmount);
            this.writeInt(headerBytes, 4, carsAmount);
            final List<String> clientsSerialized = new EntrySerializer<Client>().serialize(clients, 100);
            this.writeInt(headerBytes, 8, clientsSerialized.size());
            fos.write(headerBytes);
            progress.set(0.05);
            // WRITING CLIENTS
            int i = 0;
            for(String text : clientsSerialized){
                byte[] textLength = new byte[4];
                this.writeInt(textLength, 0, text.length());
                fos.write(textLength);
                fos.write(text.getBytes());
                i++;
                progress.set(0.05 + (double) i / clientsAmount * 0.4) ;
            }
            progress.set(0.45);
            // WRITING CARS
            for(Car car : cars){
                final byte[] carBytes = car.serialize().getBytes();
                final byte[] carBytesAmount = new byte[4];
                final byte[] imageBytes = car.getImageBytes();
                this.writeInt(carBytesAmount, 0, carBytes.length);
                fos.write(carBytesAmount);
                fos.write(carBytes);
                if (imageBytes != null && imageBytes.length > 0) fos.write(car.getImageBytes());
                progress.set(0.45 + (double) i / carsAmount);
            }
            fos.flush();
            fos.close();
            progress.set(1);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean load(final ReadOnlyDoubleWrapper progress){
        if (this.file == null || !this.file.getName().endsWith(FileDataManager.FILE_EXTENSION) || !this.file.exists()){
            return false;
        }
        final boolean b = this.load1(progress);
        if (!b){
            App.getInstance().getClientStorage().clear();
            App.getInstance().getCarStorage().clear();
        }
        return b;
    }

    private boolean load1(final ReadOnlyDoubleWrapper progress){
        try{
            final FileInputStream fis = new FileInputStream(this.file);
            App.getInstance().getClientStorage().clear();
            App.getInstance().getCarStorage().clear();

            final byte[] headerBytes = new byte[12];
            fis.read(headerBytes);
            final int clientsAmount = this.readInt(headerBytes, 0);
            final int carsAmount = this.readInt(headerBytes, 4);
            // LOAD CLIENTS
            final List<String> clientsSerialized = new ArrayList<>();
            final int clientsLinesAmount = this.readInt(headerBytes, 8);
            for(int i = 0; i < clientsLinesAmount; i++){
                byte[] textLength = new byte[4];
                fis.read(textLength);
                byte[] textBytes = new byte[this.readInt(textLength, 0)];
                fis.read(textBytes);
                clientsSerialized.add(new String(textBytes));
                progress.set(((double) (i + 1) / clientsAmount)  * 0.4);
            }
            final Set<Client> clients;
            try{
                clients = new EntrySerializer<Client>(new Client()).deserialize(clientsSerialized);
            }catch (DeserializationException e){
                System.out.println("Error while deserializing clients. " + (e.getComment() == null ? "" : "(" + e.getComment() + ")"));
                if (e.getComment() != null){
                    System.out.println("Error while deserializing clients (" + e.getComment() + ")");
                }
                e.printStackTrace();
                return false;
            }
            App.getInstance().getClientStorage().addAll(clients);
            progress.set(0.45);
            // LOAD CARS
            final Serializer<Car> carSerializer = new EntrySerializer<>(new Car());
            final Set<Car> cars = new HashSet<>();
            for(int i = 0; i < carsAmount; i++){
                final byte[] carBytesAmount = new byte[4];
                fis.read(carBytesAmount);
                final byte[] carBytes = new byte[this.readInt(carBytesAmount, 0)];
                fis.read(carBytes);
                final String carText = new String(carBytes);
                final Car car;
                try{
                    car = carSerializer.deserialize(carText);
                }catch (DeserializationException e){
                    System.out.println("Error while deserializing Car. " + (e.getComment() == null ? "" : "(" + e.getComment() + ")"));
                    System.out.println("TEXT: " + carText);
                    e.printStackTrace();
                    return false;
                }
                if (car.getImageBytes().length > 0){
                    fis.read(car.getImageBytes());
                }
                cars.add(car);
                progress.set(((double) (i + 1) / carsAmount) * 0.55 + 0.45);
            }
            fis.close();
            progress.set(1);
            App.getInstance().getCarStorage().addAll(cars);
            return true;
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    private void writeInt(final byte[] array, int index, final int value){
        array[index++] = (byte)((value & 0xFF000000) >> 24);
        array[index++] = (byte)((value & 0x00FF0000) >> 16);
        array[index++] = (byte)((value & 0x0000FF00) >> 8);
        array[index] = (byte)(value & 0x000000FF);
    }

    private int readInt(final byte[] array, int index){
        return (array[index++] & 0xFF) << 24 | (array[index++] & 0xFF) << 16 | (array[index++] & 0xFF) << 8 | array[index] & 0xFF;
    }

}
