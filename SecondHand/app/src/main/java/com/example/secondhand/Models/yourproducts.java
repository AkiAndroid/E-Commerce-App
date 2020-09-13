package com.example.secondhand.Models;

public class yourproducts {

    String pid,Productname;

    public yourproducts() {

    }

    public yourproducts(String pid, String productname) {
        this.pid = pid;
        Productname = productname;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProductname() {
        return Productname;
    }

    public void setProductname(String productname) {
        Productname = productname;
    }
}
