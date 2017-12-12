package com.rescuex_za.rescuex;

/**
 * Created by Asus on 12/8/2017.
 */

public class Users {

    public String name;
    public String profile_picture;
    public String status;
    public String thumb_image;


    public Users(){

    }

    public Users(String name, String profile_picture, String status, String thumb_image) {
        this.name = name;
        this.profile_picture = profile_picture;
        this.status = status;
        this.thumb_image = thumb_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
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



