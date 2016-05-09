package com.weigan.googlephotoselect;

/**
 * Created by Administrator on 2016/5/7.
 */
public class DataModel {


    private boolean isSelected;
    private int position;

    public DataModel(boolean isSelected, int position) {
        this.isSelected = isSelected;
        this.position = position;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
