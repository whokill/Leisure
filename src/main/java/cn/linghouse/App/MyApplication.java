package cn.linghouse.App;

import android.app.Application;
import android.content.Context;

import com.lzy.imagepicker.ImagePicker;

import org.xutils.x;

import cn.linghouse.Util.PicassoImageLoader;


public class MyApplication extends Application {
    //private static MyApplication mInstance = null;
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //不允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);
        imagePicker.setFocusWidth(800);
        imagePicker.setFocusHeight(800);
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }


    //单例模式
    /*public static MyApplication getInstance() {
        if (mInstance == null) {
            synchronized (MyApplication.class) {
                if (mInstance == null) {
                    mInstance = new MyApplication();
                }
            }
        }
        return mInstance;
    }*/
}
