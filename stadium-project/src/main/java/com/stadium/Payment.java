package com.stadium;
 
public class Payment {
 
    private boolean paid;
 
    public void pay(){
        paid = true;
    }
 
    public boolean isPaid(){
        return paid;
    }
}
 