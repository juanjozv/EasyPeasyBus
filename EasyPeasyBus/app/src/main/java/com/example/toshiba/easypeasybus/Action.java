package com.example.toshiba.easypeasybus;

public class Action {
    private String mail;
    private String date;
    private Integer amount;
    private String busName;
    private String hour;

    public Action(String mail, String date, Integer amount, String busName, String hour) {
        this.mail = mail;
        this.date = date;
        this.amount = amount;
        this.busName = busName;
        this.hour = hour;
    }

    public Action() {}

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
