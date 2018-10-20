package com.example.a7lab204.gadir;


/**
 * Created by slaviK on 31/03/2018.
 */

public class LightingPoles {
    private boolean antennas, cameras, wires, other, sockets, solar_flashlight;
                    /*אנטנות, מצלמות, חוטי עירוב, אחר, שקעים, פנס סולרי */
    private double x, y;
                     /*קואורדינטות*/
    private String type, number, height, model, material;
                    /*סוג עמוד, מספר עמוד, גובה עמוד, דגם עמוד, חומר*/

    private String arm_type, notes, switchboard, color, end_line, base, g_model, g_count, g_power, flag_nests, address;
                    /*סוג זרוע, הערות, מרכזיה, צבע עמוד, סוף קו, בסיס עמוד,דגםת גת, הספק גת, מספר גתים, קנות דגלים*/
    private String picid;

    public LightingPoles(){}

    public LightingPoles(boolean antennas, boolean cameras, boolean wires, boolean sockets, boolean solar_flashlight, double x, double y, String type,
                         String number, String height, String model, String material, String arm_type, String notes, String switchboard,
                         String color, String end_line, String base, String g_model, String g_count, String g_power, String flag_nests,String address, String picid) {
        this.antennas = antennas;
        this.cameras = cameras;
        this.wires = wires;
        this.sockets = sockets;
        this.solar_flashlight = solar_flashlight;
        this.x = x;
        this.y = y;
        this.type = type;
        this.number = number;
        this.height = height;
        this.model = model;
        this.material = material;
        this.arm_type = arm_type;
        this.notes = notes;
        this.switchboard = switchboard;
        this.color = color;
        this.end_line = end_line;
        this.base = base;
        this.g_model = g_model;
        this.g_count = g_count;
        this.g_power = g_power;
        this.flag_nests = flag_nests;
        this.address = address;
        this.picid = picid;
    }

    public boolean isAntennas() {
        return antennas;
    }

    public void setAntennas(boolean antennas) {
        this.antennas = antennas;
    }

    public boolean isCameras() {
        return cameras;
    }

    public void setCameras(boolean cameras) {
        this.cameras = cameras;
    }

    public boolean isWires() {
        return wires;
    }

    public void setWires(boolean wires) {
        this.wires = wires;
    }

    public boolean isOther() {
        return other;
    }

    public void setOther(boolean other) {
        this.other = other;
    }

    public boolean isSockets() {
        return sockets;
    }

    public void setSockets(boolean sockets) {
        this.sockets = sockets;
    }

    public boolean isSolar_flashlight() {
        return solar_flashlight;
    }

    public void setSolar_flashlight(boolean solar_flashlight) {
        this.solar_flashlight = solar_flashlight;
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

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getArm_type() {
        return arm_type;
    }

    public void setArm_type(String arm_type) {
        this.arm_type = arm_type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSwitchboard() {
        return switchboard;
    }

    public void setSwitchboard(String switchboard) {
        this.switchboard = switchboard;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEnd_line() {
        return end_line;
    }

    public void setEnd_line(String end_line) {
        this.end_line = end_line;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getG_model() {
        return g_model;
    }

    public void setG_model(String g_model) {
        this.g_model = g_model;
    }

    public String getG_count() {
        return g_count;
    }

    public void setG_count(String g_count) {
        this.g_count = g_count;
    }

    public String getG_power() {
        return g_power;
    }

    public void setG_power(String g_power) {
        this.g_power = g_power;
    }

    public String getFlag_nests() {
        return flag_nests;
    }

    public void setFlag_nests(String flag_nests) {
        this.flag_nests = flag_nests;
    }

    public String getPicid() {
        return picid;
    }

    public void setPicid(String picid) {
        this.picid = picid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}



