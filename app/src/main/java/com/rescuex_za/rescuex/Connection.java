package com.rescuex_za.rescuex;

/**
 * Created by Asus on 1/5/2018.
 */

public class Connection {


    String userName,open_location ,latitude, longitude,type,from;
    long time;
    boolean seen;


    public Connection(){

    }

    public Connection(String userName,String open_location,String latitude,String longitude, String type, long time, boolean seen, String from) {
        this.userName = userName;
        this.open_location = open_location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.time = time;
        this.seen = seen;
        this.from=from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOpen_location() {
        return open_location;
    }

    public void setOpen_location(String open_location) {
        this.open_location = open_location;
    }
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude= latitude;
    }
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude= longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
