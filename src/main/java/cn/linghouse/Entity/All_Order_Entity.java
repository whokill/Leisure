package cn.linghouse.Entity;
/*
 *Create by on 2018/12/18
 *Author:Linghouse
 *describe:
 */

public class All_Order_Entity {
    private String picurl;
    private String title;
    private String price;
    private String label1;
    private String label2;
    private String tradstates;

    public String getTradstates() {
        return tradstates;
    }

    public void setTradstates(String tradstates) {
        this.tradstates = tradstates;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
