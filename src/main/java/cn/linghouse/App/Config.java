package cn.linghouse.App;
/*
 *Create by on 2018/12/17
 *Author:Linghouse
 */

public class Config {
    //登录接口地址
    public static String loginUrl = "http://192.168.137.1:8080/leisure/login";
    //注册接口地址
    public static String registerUrl = "http://139.199.2.193:8080/leisure/user/create";
    //搜索商品接口地址
    public static String searchCommodityUrl = "http://192.168.137.1:8080/leisure/commodities/search";
    //创建商品接口地址
    public static String createCommodityUrl = "http://192.168.137.1:8080/leisure/create/commodity";
    //推荐商品接口地址
    public static String recommendCommodityUrl = "http://192.168.137.1:8080/leisure/commodities/search";
    //热门商品接口地址
    public static String hotCommodityUrl = "http://192.168.137.1:8080/leisure/commodities/search";
    //登录验证码接口地址
    public static String loginCode = "http://192.168.137.1:8080/leisure/verification";
    //我的全部交易接口地址
    public static String getAllOrder = "http:192.168.137.1:8080/leisure/trade";
    //注销当前用户接口地址
    public static String chagneAccountUrl = "http://192.168.137.1:8080/leisure/logout";
    //上传用户收款二维码接口地址
    public static String upCollectionImageUrl = "http://192.168.137.1:8080/leisure/alipay/set";
    //我卖出的接口地址
    public static String mySellUrl = "http://192.168.137.1:8080/leisure/commodity/complete";
    //我买到的接口地址
    public static String myBuyUrl = "http://192.168.137.1:8080/leisure/commodity/get";
    //我发布的接口地址
    public static String myReleaseUrl = "http://192.168.137.1:8080/leisure/commodities";
    //判断商品是否被收藏接口地址
    public static String isCollection = "http://192.168.137.1:8080/leisure/commodity/isattention";
    //加入收藏的接口地址
    public static String joinCollection = "http://192.168.137.1:8080/leisure/collection/join";
    //我的收藏接口地址
    public static String myCollection = "http://192.168.137.1:8080/leisure/collection";
}
