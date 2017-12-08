package com.rescuex_za.rescuex;

/**
 * Created by Asus on 12/8/2017.
 */

public class Users {

    public String Name;
    public String Profile_picture;
    public String status;
    public String thumb_image;


    public Users(){

    }

    public Users(String name, String image, String status, String thumb_image) {
        this.Name = name;
        this.Profile_picture = image;
        this.status = status;
        this.thumb_image = thumb_image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getImage() {
        return Profile_picture;
    }

    public void setImage(String image) {
        this.Profile_picture = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

}



