package net.karmats.ilovegratis.dto;

import java.io.Serializable;
import java.util.Date;

import android.graphics.Bitmap;

public class AdDto implements Serializable, Comparable<AdDto> {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private String county;
    private String municipality;
    private String title;
    private String phoneNumber;
    private String category;
    private String imgThumbSrc;
    private Bitmap imgThumb;
    private Date dateUploaded;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgThumbSrc() {
        return imgThumbSrc;
    }

    public void setImgThumbSrc(String imgThumbSrc) {
        this.imgThumbSrc = imgThumbSrc;
    }

    public Bitmap getImgThumb() {
        return imgThumb;
    }

    public void setImgThumb(Bitmap imgThumb) {
        this.imgThumb = imgThumb;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    @Override
    public String toString() {
        return title + " i " + county;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false; 
        }
        if (o instanceof AdDto) {
            AdDto other = (AdDto) o;
            return this.getId().equals(other.getId());
        }
        return false;
        
    }

    @Override
    public int compareTo(AdDto another) {
        if (this.getDateUploaded() != null && another.getDateUploaded() != null) {
            return another.getDateUploaded().compareTo(this.getDateUploaded());
        }
        return 0;
    }

}
