package net.dajman.rentalcar.basic;

import net.dajman.rentalcar.App;
import net.dajman.rentalcar.exceptions.DeserializationException;

import java.util.*;

public class Client extends Entry<Client>{

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String postCode;
    private String city;
    private String street;
    private int buildingNumber;
    private int flatNumber;
    private List<Car> rentedCars;

    public Client(){
    }

    public Client(final String text) throws DeserializationException{
        if (text == null || text.isEmpty() || text.isBlank()) {
            throw new DeserializationException( "Text is empty");
        }
        final String[] splited = text.split(",");
        if (splited.length < 8) {
            throw new DeserializationException("Text is incomplete");
        }
        try{
            this.id = Integer.parseInt(splited[0]);
            this.buildingNumber = Integer.parseInt(splited[7]);
            this.flatNumber = Integer.parseInt(splited[8]);
        }catch (NumberFormatException e){
            throw new DeserializationException("Error while parsing number.");
        }
        this.firstName = splited[1];
        this.lastName = splited[2];
        this.phoneNumber = splited[3];
        this.postCode = splited[4];
        this.city = splited[5];
        this.street = splited[6];
        this.rentedCars = new ArrayList<>();
    }

    public Client(String firstName, String lastName, String phoneNumber, String postCode, String city, String street, int buildingNumber) {
        this(firstName, lastName, phoneNumber, postCode, city, street, buildingNumber, -1);
    }

    public Client(String firstName, String lastName, String phoneNumber, String postCode, String city, String street, int buildingNumber, int flatNumber) {
        super(App.getInstance().getClientStorage());
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.postCode = postCode;
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.flatNumber = flatNumber;
        this.rentedCars = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getBuildingNumber() {
        return buildingNumber;
    }

    public int getFlatNumber() {
        return flatNumber;
    }

    public List<Car> getRentedCars() {
        return rentedCars;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setBuildingNumber(int buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public void setFlatNumber(int flatNumber) {
        this.flatNumber = flatNumber;
    }

    public boolean addRentedCar(final Car car){
        if (this.rentedCars.contains(car)){
            return false;
        }
        return this.rentedCars.add(car);
    }

    public boolean removeRentedCar(final Car car){
        if (!this.rentedCars.contains(car)){
            return false;
        }
        car.setClient(null);
        car.setRentalDate(0L);
        return this.rentedCars.remove(car);
    }

    public double toPay(){
        return this.rentedCars.parallelStream().mapToDouble(Car::toPay).sum();
    }

    @Override
    public String serialize() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.id).append(',');
        sb.append(this.firstName).append(',');
        sb.append(this.lastName).append(',');
        sb.append(this.phoneNumber).append(',');
        sb.append(this.phoneNumber).append(',');
        sb.append(this.postCode).append(',');
        sb.append(this.city).append(',');
        sb.append(this.street).append(',');
        sb.append(this.buildingNumber).append(',');
        sb.append(this.flatNumber);
        return sb.toString();
    }

    @Override
    public Client deserialize(final String text) throws DeserializationException{
        return new Client(text);
    }

    @Override
    public boolean isSimilar(final String text){
        return this.firstName.toLowerCase().startsWith(text) || this.lastName.toLowerCase().startsWith(text);
    }

    public static Client get(final int id){
        return App.getInstance().getClientStorage().get(id);
    }


}
