package com.stadium;
 
import java.security.SecureRandom;
import java.time.*;
 
public class Booking {
 
    private final String id;
    private final String field;
    private final LocalDate date;
    private final LocalTime start;
    private final LocalTime end;
    private final double total;
 
    public Booking(String field,
                   LocalDate date,
                   LocalTime start,
                   LocalTime end){
 
        this.id = generateId();
        this.field = field;
        this.date = date;
        this.start = start;
        this.end = end;
 
        long hours =
                Duration.between(start,end).toHours();
 
        double price = switch (field){
            case "Football" -> 1000;
            case "Badminton" -> 300;
            case "Basketball" -> 500;
            default -> 0;
        };
 
        this.total = price * hours;
    }
 
    private String generateId(){
        SecureRandom r = new SecureRandom();
        return String.valueOf(100000 + r.nextInt(900000));
    }
 
    public String getId(){ return id; }
    public String getField(){ return field; }
    public LocalDate getDate(){ return date; }
    public LocalTime getStart(){ return start; }
    public LocalTime getEnd(){ return end; }
    public double getTotal(){ return total; }
}
 