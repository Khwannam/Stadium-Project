package com.stadium;
 
public class User {
 
    private final String username;
    private final String passwordHash;
 
    public User(String username,String rawPassword){
        this.username = username;
        this.passwordHash = SecurityUtil.hash(rawPassword);
    }
 
    public boolean checkPassword(String rawPassword){
        return passwordHash.equals(
                SecurityUtil.hash(rawPassword));
    }
 
    public String getUsername(){
        return username;
    }
 
    public String getPasswordHash(){
        return passwordHash;
    }
}
 