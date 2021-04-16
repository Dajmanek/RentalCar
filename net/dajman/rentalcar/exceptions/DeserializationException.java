package net.dajman.rentalcar.exceptions;

public class DeserializationException extends Exception{

    private String comment;

    public DeserializationException(){
        this.comment = null;
    }

    public DeserializationException(final String comment){
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

}
