package com.rescuex_za.rescuex;

/**
 * Created by Asus on 12/31/2017.
 */

public class AlertConv {
    public boolean seen;
    public long timestamp;

    public AlertConv(){

    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public AlertConv(boolean seen, long timestamp) {
        this.seen = seen;
        this.timestamp = timestamp;
    }
}
