package cn.linghouse.UI;

import android.app.Dialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gyf.barlibrary.ImmersionBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;
import okhttp3.Call;

public class wallpaper extends AppCompatActivity {

    @BindView(R.id.iv_wallpaper)
    ImageView ivWallpaper;
    @BindView(R.id.btn_set_wallpaper)
    Button btnSetWallpaper;
    private ZLoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        ButterKnife.bind(this);
        ImmersionBar.with(this).init();
        initview();
    }

    private void initview() {
        Glide.with(this)
                .load("https://picsum.photos/1080/1920/?image=2")
                .error(R.mipmap.logo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.logo)
                .into(ivWallpaper);
    }

    @OnClick(R.id.btn_set_wallpaper)
    public void onViewClicked() {
        dialog = new ZLoadingDialog(this);
        dialog.setHintText("壁纸设置中,请稍等");
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE);
        dialog.show();
        OkHttpUtils.get()
                .url("https://picsum.photos/1080/1920/?image=1")
                .build().execute(new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Bitmap response, int id) {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    wallpaperManager.setBitmap(response);
                    dialog.dismiss();
                    ToastUtil.ShowLong("设置成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
