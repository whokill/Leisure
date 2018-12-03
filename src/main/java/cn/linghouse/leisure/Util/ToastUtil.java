package cn.linghouse.leisure.Util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil{
    public static void ShowLong(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
    public static void ShowShort(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
