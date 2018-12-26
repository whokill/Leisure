package cn.linghouse.App;
/*
 *Create by on 2018/12/17
 *Author:Linghouse
 *describe:后端各个接口地址
 */

public class Config {
    //登录接口地址
    public static String loginUrl = "http://139.199.2.193:8080/leisure/login";
    //注册接口地址
    public static String registerUrl = "http://139.199.2.193:8080/leisure/create";
    //搜索商品接口地址
    public static String searchCommodityUrl = "http://139.199.2.193:8080/leisure/commodities/search";
    //创建商品接口地址
    public static String createCommodityUrl = "http://139.199.2.193:8080/leisure/create/commodity";
    //推荐商品接口地址
    public static String recommendCommodityUrl = "http://139.199.2.193:8080/leisure/commodities/search";
    //热门商品接口地址
    public static String hotCommodityUrl = "http://139.199.2.193:8080/leisure/commodities/search";
    //验证码接口地址
    public static String verification  = "http://139.199.2.193:8080/leisure/verification";
    //我的全部交易接口地址
    public static String getAllOrder = "http://139.199.2.193:8080/leisure/trade?=";
    //注销当前用户接口地址
    public static String chagneAccountUrl = "http://139.199.2.193:8080/leisure/logout";
    //上传用户收款二维码接口地址
    public static String upCollectionImageUrl = "http://139.199.2.193:8080/leisure/alipay/set";
    //我卖出的接口地址
    public static String mySellUrl = "http://139.199.2.193:8080/leisure/commodity/complete";
    //我买到的接口地址
    public static String myBuyUrl = "http://139.199.2.193:8080/leisure/commodity/get";
    //我发布的接口地址
    public static String myReleaseUrl = "http://139.199.2.193:8080/leisure/commodities";
    //判断商品是否被收藏接口地址
    public static String isCollection = "http://139.199.2.193:8080/leisure/commodity/isattention";
    //加入收藏的接口地址
    public static String joinCollection = "http://139.199.2.193:8080/leisure/collection/join";
    //我的收藏接口地址
    public static String myCollection = "http://139.199.2.193:8080/leisure/collection";
    //商品分类接口地址
    public static String sortlist = "http://139.199.2.193:8080/leisure/sort/list";
    //找回密码接口地址
    public static String findPass = "http://139.199.2.193:8080/leisure/forget/find_by_email";
    //账户余额提现接口地址
    public static String deposit = "http://139.199.2.193:8080/leisure/balance/withdrawal";
    //充值余额接口地址
    public static String recharge = "http://139.199.2.193:8080/leisure/recharge";
    //创建收获地址的接口
    public static String createAddress = "http://139.199.2.193:8080/leisure/shipping/address/add";
    //获取收获地址
    public static String getAddress = "http://139.199.2.193:8080/leisure/shipping/address/get";
    //设置默认收货地址
    public static String setDefaultAddress = "http://139.199.2.193:8080/leisure/shipping/address/set";
    //创建交易的地址
    public static String create = "http://139.199.2.193:8080/leisure/trade/create";
    //完成交易的接口地址
    public static String complete = "http://139.199.2.193:8080/leisure/trade/complete";
    //支付账单的接口地址
    public static String pay = "http://139.199.2.193:8080/leisure/pay";
    //获取用户信息接口地址
    public static String infomation = "http://139.199.2.193:8080/leisure/user/";
}
