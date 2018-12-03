package cn.linghouse.leisure.Entity;


public class Search_Entity {
    //图片url
    private String picurl;
    //商品名称
    private String title;
    //商品单价
    private String pice;
    //是否包邮
    private String pinkage;
    //发货地
    private String place;
    //发货时间
    private String data;

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPice() {
        return pice;
    }

    public void setPice(String pice) {
        this.pice = pice;
    }

    public String getPinkage() {
        return pinkage;
    }

    public void setPinkage(String pinkage) {
        this.pinkage = pinkage;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
