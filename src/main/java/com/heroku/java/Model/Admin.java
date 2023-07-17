package com.heroku.java.Model;

public class Admin  {
  private String username;
  private String password;
  private String email;
  private String address;
  private int adminid;
  
  public Admin(){

  }
  public Admin(String username, String password, String email, String address,int adminid) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.address = address;
    this.adminid = adminid;
  }

  public Admin(String username2, String password2, String email2, String address2, Object object) {
  }
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getAdminid() {
    return adminid;
  }

  public void setAdminid(int adminid) {
    this.adminid = adminid;
  }

  

  

  
}


    



