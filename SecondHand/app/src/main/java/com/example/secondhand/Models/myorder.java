package com.example.secondhand.Models;

public class myorder
{
    String pid,productName,sellerrollno;

    public myorder()
    {

    }

    public myorder(String pid, String productName, String sellerrollno) {
        this.pid = pid;
        this.productName = productName;
        this.sellerrollno = sellerrollno;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSellerrollno() {
        return sellerrollno;
    }

    public void setSellerrollno(String sellerrollno) {
        this.sellerrollno = sellerrollno;
    }
}
