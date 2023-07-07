package com.heroku.java.Model;

public class Users {
    private String username;
    private String password;
    private String email;
    private String address;
  
    

    public Users(String username, String password, String email, String address) {
      this.username = username;
      this.password = password;
      this.email = email;
      this.address = address;
    }

    public String getUsername() { 
      return username;
    }
  
    public void setUsername(String name) {
      this.username = name;
    }
  
    public String getPassword() {
      return password;
    }
  
    public void setPassword(String password) {
      this.password = password;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getAddress(){
      return address;
    }

    public void setAddress(String address){
      this.address = address;
    }
    
    
  }