package cn.linghouse.Entity;

/**
 * 根据用户的日常操作，推荐商品
 */
public class Recommend_Entity {
    //图片url
    private String url;
    //商品标题
    private String title;
    //商品价格
    private String price;
    //商品评分
    private String score;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
