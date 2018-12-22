package cn.linghouse.Util;

import android.widget.Toast;

import cn.linghouse.App.MyApplication;

public class ToastUtil {
    public static void ShowLong(String msg){
        Toast.makeText(MyApplication.getContext(),msg,Toast.LENGTH_LONG).show();
    }
    public static void ShowShort(String msg){
        Toast.makeText(MyApplication.getContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
