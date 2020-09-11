package com.example.secondhand.Models;

public class Products
{
    String Date,Description,Duration,ModelName,Price,Productname,Time,category,image,pid,sellerrollno;

    public Products()
    {


    }

    public Products(String date, String description, String duration, String modelName, String price, String productname, String time, String category, String image, String pid, String sellerrollno) {
        Date = date;
        Description = description;
        Duration = duration;
        ModelName = modelName;
        Price = price;
        Productname = productname;
        Time = time;
        this.category = category;
        this.image = image;
        this.pid = pid;
        this.sellerrollno = sellerrollno;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String modelName) {
        ModelName = modelName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProductname() {
        return Productname;
    }

    public void setProductname(String productname) {
        Productname = productname;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSellerrollno() {
        return sellerrollno;
    }

    public void setSellerrollno(String sellerrollno) {
        this.sellerrollno = sellerrollno;
    }
}
