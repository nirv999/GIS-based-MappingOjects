package com.example.a7lab204.gadir;

/**
 * Created by slavik on 31/03/2018.
 */


public class SwitchBoard {
    private String type, number, gadir_num;
                    /*סוג מרכזיה, מספר מרכזיה, מספר גדיר*/
    private double x,y;
                    /*קואורדינטות*/
    private String iec_num, contract_num ,num_of_circles,controller_power ,connection_size, notes, address, place_desc;
                    /*מספר מונה ח"ח, מספר חוזה, מספר מעגלים, עוצמת בקר, גודל חיבור, הערות, כתובת, תיאור מקום */
    private int day, month, year;

    public SwitchBoard(){}

    public SwitchBoard(String type, String number, String gadir_num, double x, double y, String iec_num, String contract_num, String num_of_circles,
                       String controller_power, String connection_size, String notes, String address, String place_desc, int day, int month, int year) {
        this.type = type;
        this.number = number;
        this.gadir_num = gadir_num;
        this.x = x;
        this.y = y;
        this.iec_num = iec_num;
        this.contract_num = contract_num;
        this.num_of_circles = num_of_circles;
        this.controller_power = controller_power;
        this.connection_size = connection_size;
        this.notes = notes;
        this.address = address;
        this.place_desc = place_desc;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGadir_num() {
        return gadir_num;
    }

    public void setGadir_num(String gadir_num) {
        this.gadir_num = gadir_num;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getIec_num() {
        return iec_num;
    }

    public void setIec_num(String iec_num) {
        this.iec_num = iec_num;
    }

    public String getContract_num() {
        return contract_num;
    }

    public void setContract_num(String contract_num) {
        this.contract_num = contract_num;
    }

    public String getNum_of_circles() {
        return num_of_circles;
    }

    public void setNum_of_circles(String num_of_circles) {
        this.num_of_circles = num_of_circles;
    }

    public String getController_power() {
        return controller_power;
    }

    public void setController_power(String controller_power) {
        this.controller_power = controller_power;
    }

    public String getConnection_size() {
        return connection_size;
    }

    public void setConnection_size(String connection_size) {
        this.connection_size = connection_size;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlace_desc() {
        return place_desc;
    }

    public void setPlace_desc(String place_desc) {
        this.place_desc = place_desc;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
