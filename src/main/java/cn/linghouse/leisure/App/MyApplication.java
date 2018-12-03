package cn.linghouse.leisure.App;

import android.app.Application;

import com.bumptech.glide.load.model.LazyHeaderFactory;
import com.lzy.imagepicker.ImagePicker;

import cn.linghouse.leisure.Util.PicassoImageLoader;

import static com.lzy.imagepicker.ImagePicker.getInstance;

public class MyApplication extends Application {
    public static int LOGINSTATE;//未登录的状态码
    private static MyApplication mInstance = null;
    @Override
    public void onCreate() {
        super.onCreate();
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
    }

    //单例模式
    public static MyApplication getInstance() {
        if (mInstance == null) {
            synchronized (MyApplication.class) {
                if (mInstance == null) {
                    mInstance = new MyApplication();
                }
            }
        }
        return mInstance;
    }

    public static int getLOGINSTATE() {
        return LOGINSTATE;
    }

    public static void setLOGINSTATE(int LOGINSTATE) {
        MyApplication.LOGINSTATE = LOGINSTATE;
    }
}
