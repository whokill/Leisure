package cn.linghouse.Entity;

/**
 * 根据用户的日常操作，推荐商品
 */
public class Recommend_Entity {
    //商品图片url
    private String recommed_pic_url;
    //商品标题
    private String recommed_title;
    //商品的详细内容
    private String recommed_detail;
    //商品价格
    private String recommed_price;
    //商品标签
    private String recommed_label;
    private String[] recommed_images;

    public String getRecommed_pic_url() {
        return recommed_pic_url;
    }

    public void setRecommed_pic_url(String recommed_pic_url) {
        this.recommed_pic_url = recommed_pic_url;
    }

    public String getRecommed_title() {
        return recommed_title;
    }

    public void setRecommed_title(String recommed_title) {
        this.recommed_title = recommed_title;
    }

    public String getRecommed_detail() {
        return recommed_detail;
    }

    public void setRecommed_detail(String recommed_detail) {
        this.recommed_detail = recommed_detail;
    }

    public String getRecommed_price() {
        return recommed_price;
    }

    public void setRecommed_price(String recommed_price) {
        this.recommed_price = recommed_price;
    }

    public String getRecommed_label() {
        return recommed_label;
    }

    public void setRecommed_label(String recommed_label) {
        this.recommed_label = recommed_label;
    }

    public String[] getRecommed_images() {
        return recommed_images;
    }

    public void setRecommed_images(String[] recommed_images) {
        this.recommed_images = recommed_images;
    }
}
