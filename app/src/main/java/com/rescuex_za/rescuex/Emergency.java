package com.rescuex_za.rescuex;

/**
 * Created by Asus on 12/21/2017.
 */

public class Emergency {

    public String name;
    public String thumb_image;


    public Emergency(){

    }

    public Emergency(String name, String thumb_image) {
        this.name = name;
        this.thumb_image = thumb_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }


}
