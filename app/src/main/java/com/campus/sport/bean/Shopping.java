package com.campus.sport.bean;

/**
 * Created by Administrator on 2019/5/21 0021.
 */

public class Shopping {
    private int Id;
    private int Img;
    private String Title;
    private String Lable;
    private String Hint1;
    private String Hint2;
    private String Price;
    private String Number;

    public Shopping() {
    }

    public Shopping(int id, int img, String title, String lable, String hint1, String hint2, String price, String number) {
        Id = id;
        Img = img;
        Title = title;
        Lable = lable;
        Hint1 = hint1;
        Hint2 = hint2;
        Price = price;
        Number = number;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getImg() {
        return Img;
    }

    public void setImg(int img) {
        Img = img;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLable() {
        return Lable;
    }

    public void setLable(String lable) {
        Lable = lable;
    }

    public String getHint1() {
        return Hint1;
    }

    public void setHint1(String hint1) {
        Hint1 = hint1;
    }

    public String getHint2() {
        return Hint2;
    }

    public void setHint2(String hint2) {
        Hint2 = hint2;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }
}
