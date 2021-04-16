package net.dajman.rentalcar.basic;

import javafx.scene.image.Image;
import net.dajman.rentalcar.App;
import net.dajman.rentalcar.exceptions.DeserializationException;

import java.io.ByteArrayInputStream;


public class Car extends Entry<Car>{

    private float price;
    private String brand;
    private String model;
    private Client client;
    private long rentalDate;

    private byte[] imageBytes;
    private transient Image image;

    public Car(){
    }

    public Car(final String text) throws DeserializationException {
        if (text == null || text.isEmpty() || text.isBlank()) {
            throw new DeserializationException( "Text is empty");
        }
        final String[] splited = text.split(",");
        if (splited.length < 7) {
            throw new DeserializationException("Text is incomplete");
        }
        try{
            this.id = Integer.parseInt(splited[0]);
            this.price = Float.parseFloat(splited[1]);
            this.brand = splited[2];
            this.model = splited[3];
            final int clientId = Integer.parseInt(splited[4]);
            this.client = clientId == -1 ? null : Client.get(clientId);
            this.rentalDate = Long.parseLong(splited[5]);
            this.imageBytes = new byte[Integer.parseInt(splited[6])];
        }catch (NumberFormatException e){
            throw new DeserializationException("Error while parsing number.");
        }
    }

    public Car(final float price, final String brand, final String model){
        this.id = App.getInstance().getCarStorage().getNextId();
        this.price = price;
        this.brand = brand;
        this.model = model;
    }

    public float getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public Client getClient() {
        return client;
    }

    public long getRentalDate() {
        return rentalDate;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public Image getImage() {
        if (this.image == null){
            if (this.imageBytes == null || this.imageBytes.length == 0){
                return null;
            }
            this.image = new Image(new ByteArrayInputStream(this.imageBytes));
        }
        return this.image;
    }

    public boolean isRented(){
        return this.client != null;
    }

    public boolean isAvailable(){
        return this.client == null;
    }

    public double toPay(){
        final double diff = System.currentTimeMillis() - this.rentalDate;
        int hours = (int) diff / 3600000;
        if (diff % 3600000 > 0) hours++;
        return hours * this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setRentalDate(long rentalDate) {
        this.rentalDate = rentalDate;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    @Override
    public String serialize() {
        return this.id + "," + this.price + "," + this.brand + "," + this.model + "," + (this.client != null ? this.client.getId() : "-1") + "," + this.rentalDate + "," + (this.imageBytes != null ? this.imageBytes.length : 0);
    }

    @Override
    public Car deserialize(String text) throws DeserializationException {
        return new Car(text);
    }

    @Override
    public boolean isSimilar(String text) {
        return this.brand.toLowerCase().startsWith(text) || this.model.toLowerCase().startsWith(text);
    }

    public static Car get(final int id){
        return App.getInstance().getCarStorage().get(id);
    }
}
