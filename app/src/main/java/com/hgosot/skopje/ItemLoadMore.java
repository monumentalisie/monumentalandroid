package com.hgosot.skopje;

public class ItemLoadMore {

    public static final int TYPE_BUTTON = 0;
    public static final int TYPE_LOADER = 1;

    private String text;
    private int type;

    private boolean isLoading;

    public ItemLoadMore(int type) {
        this.type = type;
    }

    public ItemLoadMore(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

}
