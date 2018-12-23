package cn.linghouse.UI;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.Adapter.Shopping_Address_Adapter;
import cn.linghouse.App.ActivityController;
import cn.linghouse.App.Config;
import cn.linghouse.Entity.Shopping_Address_Entity;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;
import okhttp3.Call;

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
    private EditText etdetailsaddress,etreapername,etreaperphone;
    private AddressPicker picker;
    private String sessionid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_address_activity);
        ActivityController.addActivity(this);
        //沉浸式状态栏
        ImmersionBar.with(this).init();
        ButterKnife.bind(this);
        SharedPreferences share = getSharedPreferences("Session", MODE_PRIVATE);
        sessionid = share.getString("sessionid", "null");
        ivShoppingAddressBack.setOnClickListener(this);
        btnAddAddress.setOnClickListener(this);
        getAdderss();
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
        etreapername = dialog.findViewById(R.id.et_reaper_name);
        etreaperphone = dialog.findViewById(R.id.et_reaper_phone);
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
                String address = mprovince+mcity+mdistrict;
                addAddress(address,etreapername.getText().toString(),
                        etreaperphone.getText().toString(),
                        etdetailsaddress.getText().toString());
                break;
            case R.id.tv_dialog_cancel_address:
                dialog.dismiss();
                picker.dismiss();
                break;
            case R.id.iv_shopping_address_back:
                this.finish();
                break;
            default:
                break;
        }
    }

    //添加收获地址
    private void addAddress(String address,String consignee,String cellphone,String detailed) {
        OkHttpUtils.post()
                .url(Config.createAddress)
                .addHeader("cookie",sessionid)
                .addParams("address",address)
                .addParams("consignee",consignee)
                .addParams("cellphone",cellphone)
                .addParams("detailed",detailed)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String consignee = data.getString("consignee");
                    String cellphone = data.getString("cellphone");
                    String address = data.getString("address");
                    String detailed = data.getString("detailed");
                    address_entity = new Shopping_Address_Entity();
                    address_entity.setCellphone(cellphone);
                    address_entity.setAddress(address);
                    address_entity.setName(consignee);
                    address_entity.setDetail_address(detailed);
                    entity.add(address_entity);
                    adaper.notifyDataSetChanged();
                    dialog.dismiss();
                    picker.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取收货地址
    private void getAdderss(){
        OkHttpUtils.get()
                .url(Config.getAddress)
                .addHeader("cookie",sessionid)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i =0;i<data.length();i++){
                        JSONObject object = data.getJSONObject(i);
                        String id1 = object.getString("id");
                        //收件人姓名
                        String name = object.getString("consignee");
                        //收件人联系电话
                        String cellphone = object.getString("cellphone");
                        //大概收货地址
                        String address = object.getString("address");
                        //详细收货地址
                        String detail = object.getString("detailed");
                        address_entity = new Shopping_Address_Entity();
                        address_entity.setId(id1);
                        address_entity.setName(name);
                        address_entity.setAddress(address);
                        address_entity.setCellphone(cellphone);
                        address_entity.setDetail_address(detail);
                        entity.add(address_entity);
                        adaper.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        ActivityController.removeActivity(this);
    }
}
