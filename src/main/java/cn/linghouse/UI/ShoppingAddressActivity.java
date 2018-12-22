package cn.linghouse.UI;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class ShoppingAddressActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.iv_shopping_address_back)
    ImageView ivShoppingAddressBack;
    @BindView(R.id.rl_shopping_address)
    RecyclerView rlShoppingAddress;
    @BindView(R.id.btn_add_address)
    Button btnAddAddress;
    private Dialog dialog;
    private Shopping_Address_Entity address_entity;
    private List<Shopping_Address_Entity> entity = new ArrayList<>();
    private Shopping_Address_Adapter adaper;
    private TextView addresssure, addresscancel;
    private String mprovince,mcity,mdistrict;
    private EditText etdetailsaddress;
    private AddressPicker picker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_address_activity);
        ActivityController.addActivity(this);
        //沉浸式状态栏
        ImmersionBar.with(this).init();
        ButterKnife.bind(this);
        ivShoppingAddressBack.setOnClickListener(this);
        btnAddAddress.setOnClickListener(this);
        adaper = new Shopping_Address_Adapter(this, entity);
        rlShoppingAddress.setLayoutManager(new LinearLayoutManager(this));
        rlShoppingAddress.setAdapter(adaper);
    }

    private void initDialog() {
        dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_detail_address, null);
        dialog.setContentView(view);
        addresssure = dialog.findViewById(R.id.tv_dialog_sure_address);
        addresscancel = dialog.findViewById(R.id.tv_dialog_cancel_address);
        etdetailsaddress = dialog.findViewById(R.id.et_detail_address);
        addresscancel.setOnClickListener(this);
        addresssure.setOnClickListener(this);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_shopping_address_back:
                this.finish();
                break;
            case R.id.btn_add_address:
                //三级地址联动选择器
                picker = new AddressPicker(this);
                picker.setAddressListener(new AddressPicker.OnAddressListener() {
                    @Override
                    public void onAddressSelected(String province, String city, String district) {
                        mprovince = province;
                        mcity = city;
                        mdistrict = district;
                    }
                });
                picker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        initDialog();
                    }
                });
                picker.show();
                break;
            case R.id.tv_dialog_sure_address:
                address_entity = new Shopping_Address_Entity();
                address_entity.setProvince(mprovince);
                address_entity.setCity(mcity);
                address_entity.setArea(mdistrict);
                address_entity.setDetail_address(etdetailsaddress.getText().toString());
                entity.add(address_entity);
                adaper.notifyDataSetChanged();
                dialog.dismiss();
                picker.dismiss();
                ToastUtil.ShowShort(mprovince+mcity+mdistrict+"-"+etdetailsaddress.getText().toString());
                break;
            case R.id.tv_classify_dialog_cancel:
                dialog.dismiss();
                picker.dismiss();
                break;
            default:
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
