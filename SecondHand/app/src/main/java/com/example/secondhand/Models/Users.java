package com.example.secondhand.Models;

public class Users {
    private String mailid,name,password,phoneno,rollno,image,hostelname;

    public Users(){

    }

    public Users(String mailid, String name, String password, String phoneno, String rollno, String image, String hostelname) {
        this.mailid = mailid;
        this.name = name;
        this.password = password;
        this.phoneno = phoneno;
        this.rollno = rollno;
        this.image = image;
        this.hostelname = hostelname;
    }

    public String getMailid() {
        return mailid;
    }

    public void setMailid(String mailid) {
        this.mailid = mailid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHostelname() {
        return hostelname;
    }

    public void setHostelname(String hostelname) {
        this.hostelname = hostelname;
    }
}
