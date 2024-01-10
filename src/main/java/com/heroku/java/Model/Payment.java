package com.heroku.java.Model;

import org.springframework.web.multipart.MultipartFile;

public class Payment {
    private int paymentid;
    private int bookid;
    private MultipartFile receipt;
    private String imageSrc;

    public Payment(){
        
    }

    public Payment(int paymentid, int bookid, MultipartFile receipt, String imageSrc) {
        this.paymentid = paymentid;
        this.bookid = bookid;
        this.receipt = receipt;
        this.imageSrc = imageSrc;
    }

    public int getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(int paymentid) {
        this.paymentid = paymentid;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public MultipartFile getReceipt() {
        return receipt;
    }

    public void setReceipt(MultipartFile receipt) {
        this.receipt = receipt;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    
}
