package com.heroku.java.Model;

public class Homestay {
    
    private String homestayName;
    private String homestayLocation;
    private Double homestayPrice;
    private String homestayPic;
    

    public String getHomestayName(){
        return this.homestayName;
    }

    public void setHomestayName(String homestayName){
        this.homestayName = homestayName;
    }

    public String getHomestayLocation(){
        return this.homestayLocation;
    }

    public void setHomestayLocation(String homestayLocation){
         this.homestayLocation = homestayLocation;
    }

    public Double getHomestayPrice(){
        return this.homestayPrice ;
    }

    public void setHomestayPrice(Double homestayPrice){
        this.homestayPrice = homestayPrice;
    }

    public String getHomestayPic(){
        return this.homestayPic;
    }

    public void setHomestayPic(String homestayPic){
        this.homestayPic = homestayPic;
    }


}

