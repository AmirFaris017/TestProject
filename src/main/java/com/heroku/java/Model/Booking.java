package com.heroku.java.Model;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Booking {
    public int bookingid;
    public int adminId;
    public int homestayid;
    public int userid;
    public String status;
    public Double totalamount;
    private Homestay homestay;
    private Users user;
    public int day;
    // @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date bookDate;
    // @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date startDate;
    // @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date endDate;

    public Booking(int bookingid, int adminId, Date bookDate, Date startDate, Date endDate, int homestayid, int userid,
            String status, Double totalamount,Homestay homestay,Users user) {
        this.bookingid = bookingid;
        this.adminId = adminId;
        this.bookDate = bookDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.homestayid = homestayid;
        this.userid = userid;
        this.status = status;
        this.totalamount = totalamount;
        this.homestay = homestay;
        this.user = user;
    }

    public Booking(){

    }

    public Booking(Date startTime, Date endTime,int day) {
        this.startDate = startTime;
        this.endDate = endTime;
        this.day = day;
    }

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public Date getBookDate() {
        return bookDate;
    }

    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getHomestayid() {
        return homestayid;
    }

    public void setHomestayid(int homestayid) {
        this.homestayid = homestayid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(Double totalamount) {
        this.totalamount = totalamount;
    }

    public void setHomestay(Homestay homestay) {
        this.homestay = homestay;
    }

    public Homestay getHomestay(){
        return homestay;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    
    
    
    
    

    
    // Getters and setters...
}
