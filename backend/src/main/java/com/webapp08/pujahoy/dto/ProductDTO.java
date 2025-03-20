package com.webapp08.pujahoy.dto;

import java.sql.Date;


public class ProductDTO {
    private long id;
    private String name;
    private String description;
    private double iniValue;
    private Date iniHour;
    private Date endHour;
    private String state;
    private String imgURL;
    private Long sellerId;
    //private List<OfferBasicDTO> offers;
    

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getIniValue() {
        return iniValue;
    }
    public void setIniValue(double iniValue) {
        this.iniValue = iniValue;
    }
    public Date getIniHour() {
        return iniHour;
    }
    public void setIniHour(Date iniHour) {
        this.iniHour = iniHour;
    }
    public Date getEndHour() {
        return endHour;
    }
    public void setEndHour(Date endHour) {
        this.endHour = endHour;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getImgURL() {
        return imgURL;
    }
    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
    public Long getSellerId() {
        return sellerId;
    }
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    } 
    
    
}
