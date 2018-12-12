package cn.linghouse.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cf.androidpickerlibrary.AddressPicker;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.Adapter.Shopping_Address_Adapter;
import cn.linghouse.App.ActivityController;
import cn.linghouse.Entity.Shopping_Address_Entity;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;

public class ShoppingAddressActivity extends AppCompatActivity {
    @BindView(R.id.iv_shopping_address_back)
    ImageView ivShoppingAddressBack;
    @BindView(R.id.rl_shopping_address)
    RecyclerView rlShoppingAddress;
    @BindView(R.id.btn_add_address)
    Button btnAddAddress;
    private Shopping_Address_Entity address_entity;
    private List<Shopping_Address_Entity> entity = new ArrayList<>();
    private Shopping_Address_Adapter adaper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_address_activity);
        ActivityController.addActivity(this);
        //沉浸式状态栏
        ImmersionBar.with(this).init();
        ButterKnife.bind(this);
        adaper = new Shopping_Address_Adapter(this, entity);
        rlShoppingAddress.setLayoutManager(new LinearLayoutManager(this));
        rlShoppingAddress.setAdapter(adaper);
    }

    @OnClick({R.id.iv_shopping_address_back, R.id.btn_add_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_shopping_address_back:
                this.finish();
                break;
            case R.id.btn_add_address:
                //三级地址联动选择器
                AddressPicker picker = new AddressPicker(this);
                picker.setAddressListener(new AddressPicker.OnAddressListener() {
                    @Override
                    public void onAddressSelected(String province, String city, String district) {
                        address_entity = new Shopping_Address_Entity();
                        address_entity.setProvince(province);
                        address_entity.setCity(city);
                        address_entity.setArea(district);
                        address_entity.setDetail_address("尚未编写");
                        entity.add(address_entity);
                        adaper.notifyDataSetChanged();
                    }
                });
                picker.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        ActivityController.removeActivity(this);
    }

}
