package com.taskail.mixion.models;

/**
 * Created by ed on 10/7/17.
 */

public class FullDiscussion {

    private String mString;
    private boolean isText;

    public FullDiscussion(String mString, boolean isText) {
        this.mString = mString;
        this.isText = isText;
    }

    public String getmString() {
        return mString;
    }

    public void setmString(String mString) {
        this.mString = mString;
    }

    public boolean isText() {
        return isText;
    }

    public void setText(boolean text) {
        isText = text;
    }
}
