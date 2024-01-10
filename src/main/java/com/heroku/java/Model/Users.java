package com.heroku.java.Model;

public class Users {
    private String username;
    private String password;
    private String email;
    private String address;
    private String phoneNo;
    private String role;
    
  public Users(){
    
  }
    

    public Users(String username, String password, String email, String address,String role) {
      this.username = username;
      this.password = password;
      this.email = email;
      this.address = address;
      this.role=role;
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

    public String getPhoneNo() {
      return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
      this.phoneNo = phoneNo;
    }

    public String getRole() {
      return role;
    }

    public void setRole(String role) {
      this.role = role;
    }
    
    
  }