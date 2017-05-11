package com.example.tina.doanmang_tinakeeper.model;


import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by TiNa on 06/03/2017.
 */

public class Expense {
    private int id;
    private String category;
    private String notes;
    private long money;
    private Date date;
    private int photoID;
    public Expense() {
        this.category = null;
        this.notes = null;
        this.money = 0;
        this.date = null;
        this.photoID = 0;
    }
    public Expense(int id, String category, String notes, long money, Date date) {
        this.id = id;
        this.category = category;
        this.notes = notes;
        this.money = money;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Expense(int id, String category, String notes, long money, Date date, int photoID) {
        this.id = id;
        this.category = category;
        this.notes = notes;
        this.money = money;
        this.date = date;
        this.photoID =photoID;

    }
    public Date getDate() {
        return date;
    }

    public String getDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String result = String.valueOf(dateFormat.format(date));
        return result;
    }

    public void setDate(Date date) {this.date = date;}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public int getPhotoID() {
        return photoID;
    }

    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }

    public String toString(){
        return this.category + ";" + this.notes +" "+ this.money+" "+this.getDateString();
    }

}
