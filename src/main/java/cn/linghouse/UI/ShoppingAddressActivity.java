package cn.linghouse.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.leisure.App.ActivityController;
import cn.linghouse.leisure.R;

public class ShoppingAddressActivity extends AppCompatActivity {
    @BindView(R.id.iv_shopping_address_back)
    ImageView ivShoppingAddressBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_address_activity);
        ActivityController.addActivity(this);
        //沉浸式状态栏
        ImmersionBar.with(this).init();
        ButterKnife.bind(this);

    }

    @OnClick(R.id.iv_shopping_address_back)
    public void onViewClicked() {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        ActivityController.removeActivity(this);
    }
}
