package com.heroku.java.Model;

import org.springframework.web.multipart.MultipartFile;

public class Homestay {
    private Integer homestayid;
    private String homestayname;
    private String homestaylocation;
    private Double homestayprice;
    private MultipartFile homestaypic;
    private String homestaydetails;
    private String imageSrc;

    public Homestay(){
        
    }
    public Homestay(Integer homestayid, String homestayname, String homestaylocation, Double homestayprice,
            MultipartFile homestaypic, String homestaydetails, String imageSrc) {
        this.homestayid = homestayid;
        this.homestayname = homestayname;
        this.homestaylocation = homestaylocation;
        this.homestayprice = homestayprice;
        this.homestaypic = homestaypic;
        this.homestaydetails = homestaydetails;
        this.imageSrc = imageSrc;
    }


    public Integer getHomestayid() {
        return homestayid;
    }
    public void setHomestayid(Integer homestayid) {
        this.homestayid = homestayid;
    }
    public String getHomestayname() {
        return homestayname;
    }
    public void setHomestayname(String homestayname) {
        this.homestayname = homestayname;
    }
    public String getHomestaylocation() {
        return homestaylocation;
    }
    public void setHomestaylocation(String homestaylocation) {
        this.homestaylocation = homestaylocation;
    }
    public Double getHomestayprice() {
        return homestayprice;
    }
    public void setHomestayprice(Double homestayprice) {
        this.homestayprice = homestayprice;
    }
    public MultipartFile getHomestaypic() {
        return homestaypic;
    }
    public void setHomestaypic(MultipartFile homestaypic) {
        this.homestaypic = homestaypic;
    }
    public String getHomestaydetails() {
        return homestaydetails;
    }
    public void setHomestaydetails(String homestaydetails) {
        this.homestaydetails = homestaydetails;
    }
    public String getImageSrc() {
        return imageSrc;
    }
    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }
    

    
    
}
