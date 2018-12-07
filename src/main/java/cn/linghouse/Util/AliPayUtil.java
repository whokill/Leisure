package cn.linghouse.Util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AliPayUtil {
    //支付宝包名
    private static final String ALIPAY_PACKAGE_NAME="com.eg.android.AlipayGphone";

    /**
     * 检测手机上是否安装了支付宝客户端
     * @param context：上下文环境
     * @return：返回是否存在(true/false)
     */
    public static boolean hasInstalledAlipayClient(Context context){
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ALIPAY_PACKAGE_NAME,0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *调用者使用此方法跳转到支付宝
     */
    public static boolean startAlipayClient(Activity activity,String urlcode){
        return startIntentUrl(activity, doFormUri(urlcode));
    }

    //格式化urlcode
    private static String doFormUri(String urlcode){
        try {
            urlcode = URLEncoder.encode(urlcode,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String alipayqr = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + urlcode;
        String openUri = alipayqr + "%3F_s%3Dweb-other&_t=" + System.currentTimeMillis();
        return openUri;
    }

    //主要功能代码：跳转到支付宝
    private static boolean startIntentUrl(Activity activity, String intentFullUrl) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentFullUrl));
            activity.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
