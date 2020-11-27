package com.hgosot.skopje;

public class ItemNoResults {

    public static final int TYPE_EVENT = 0;
    public static final int TYPE_PRIVILEGE = 1;
    public static final int TYPE_DISCUSSION = 2;
    public static final int TYPE_GUIDE = 3;
    public static final int TYPE_CONVERSATION = 4;
    public static final int TYPE_CONTACTS = 5;
    public static final int TYPE_NOTIFICATIONS = 6;
    public static final int TYPE_MEMBERS = 7;

    private int type;

    public ItemNoResults(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
