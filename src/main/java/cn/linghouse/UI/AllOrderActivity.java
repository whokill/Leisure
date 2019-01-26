package cn.linghouse.UI;
/*
 *Create by on 2018/12/18
 *Author:Linghouse
 *describe:全部交易
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;

import com.gyf.barlibrary.ImmersionBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.Adapter.My_All_Order_Adapter;
import cn.linghouse.App.ActivityController;
import cn.linghouse.App.Config;
import cn.linghouse.Entity.All_Order_Entity;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;
import okhttp3.Call;

public class AllOrderActivity extends AppCompatActivity {
    @BindView(R.id.iv_all_order_back)
    ImageView ivAllOrderBack;
    @BindView(R.id.lv_all_order)
    ListView lvAllOrder;
    private String sessionid;
    private List<All_Order_Entity> listentity;
    private All_Order_Entity entity;
    private My_All_Order_Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_order_activity);
        ButterKnife.bind(this);
        ImmersionBar.with(this).init();
        SharedPreferences share = getSharedPreferences("Session", MODE_PRIVATE);
        sessionid = share.getString("sessionid", "null");
        getAllorder();
        listentity = new ArrayList<>();
        adapter = new My_All_Order_Adapter(this,listentity);
        lvAllOrder.setAdapter(adapter);
    }

    @OnClick(R.id.iv_all_order_back)
    public void onViewClicked() {
        this.finish();
    }

    private void getAllorder() {
        RequestParams params = new RequestParams(Config.getAllOrder);
        params.addHeader("cookie",sessionid);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data.length()<=0){
                        ToastUtil.ShowShort("还没有交易");
                    }else{
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            int tradstates = object.getInt("tradeStatus");
                            JSONObject commd = object.getJSONObject("commodity");
                            JSONArray images = commd.getJSONArray("images");
                            JSONArray label = commd.getJSONArray("label");
                            String price = commd.getString("price");
                            String commodityName = commd.getString("commodityName");
                            String picurl = images.getString(0);
                            String label1 = label.getString(0);
                            //String label2 = label.getString(1);
                            entity = new All_Order_Entity();
                            entity.setPicurl(picurl);
                            if (tradstates==11){
                                entity.setTradstates("进行中");
                            }else if (tradstates==12){
                                entity.setTradstates("已完成");
                            }
                            entity.setTitle(commodityName);
                            entity.setPrice(price);
                            entity.setLabel1(label1);
                            //entity.setLabel2(label2);
                            listentity.add(entity);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

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