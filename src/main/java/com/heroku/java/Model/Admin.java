package com.heroku.java.Model;

public class Admin  {
  private String name;
  private String password;
  private String email;
  private String address;
  private int adminid;
  
  public Admin(){

  }
  public Admin(String name, String password, String email, String address,int adminid) {
    this.name = name;
    this.password = password;
    this.email = email;
    this.address = address;
    this.adminid = adminid;
  }

  public Admin(String adminname, String password2, String email2, String adminaddress, Object object) {
  }
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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


  

  

  
}


    



