package com.heroku.java.Model;

public class Customer {
private String username;
  private String password;
  private String email;
  private String address;
  private int customerid;

  public Customer(){

  }

public Customer(String username, String password, String email, String address, int customerid) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.address = address;
    this.customerid = customerid;
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

public int getCustomerid() {
    return customerid;
}

public void setCustomerid(int customerid) {
    this.customerid = customerid;
}

  

}
