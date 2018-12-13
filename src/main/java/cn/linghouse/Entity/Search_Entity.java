package cn.linghouse.Entity;


import java.util.ArrayList;

public class Search_Entity {
    //图片url
    private String picurl;
    //商品名称
    private String name;
    //商品单价
    private String pice;
    //商品评分
    private String score;
    //卖家昵称
    private String seller;
    //商品详情
    private String detail;
    //商品标签
    private String label1;
    private String label2;

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    //商品分类
    private String sortname;

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPice() {
        return pice;
    }

    public void setPice(String pice) {
        this.pice = pice;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String data) {
        this.detail = data;
    }
}
