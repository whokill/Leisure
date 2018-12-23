package cn.linghouse.UI;
/*
 *Create by on 2018/12/18
 *Author:Linghouse
 *describe:
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;

import com.gyf.barlibrary.ImmersionBar;

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
import cn.linghouse.Adapter.My_Sell_Adapter;
import cn.linghouse.App.ActivityController;
import cn.linghouse.App.Config;
import cn.linghouse.Entity.All_Order_Entity;
import cn.linghouse.Entity.My_Sell_Entity;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;

public class MySellActivity extends AppCompatActivity {
    @BindView(R.id.iv_my_sell_back)
    ImageView ivMySellBack;
    @BindView(R.id.lv_my_sell_order)
    ListView lvMySellOrder;
    private List<My_Sell_Entity> sell_entityList;
    private My_Sell_Entity entity;
    private My_Sell_Adapter adapter;
    private String sessionid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sell);
        ButterKnife.bind(this);
        ImmersionBar.with(this).init();
        SharedPreferences share = getSharedPreferences("Session", MODE_PRIVATE);
        sessionid = share.getString("sessionid", "null");
        getMysell();
        sell_entityList = new ArrayList<>();
        adapter = new My_Sell_Adapter(this,sell_entityList);
        lvMySellOrder.setAdapter(adapter);
    }

    private void getMysell(){
        RequestParams params = new RequestParams(Config.mySellUrl);
        params.addHeader("cookie",sessionid);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data.length()<=0){
                        ToastUtil.ShowShort("还没有卖出过商品");
                    }else{
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String tradstates = object.getString("tradeStatus");
                            JSONObject commd = object.getJSONObject("commodity");
                            JSONArray images = commd.getJSONArray("images");
                            JSONArray label = commd.getJSONArray("label");
                            String price = commd.getString("price");
                            String commodityName = commd.getString("commodityName");
                            String picurl = images.getString(0);
                            String label1 = label.getString(0);
                            String label2 = label.getString(1);
                            entity = new My_Sell_Entity();
                            entity.setPicurl(picurl);
                            entity.setTitle(commodityName);
                            entity.setPrice(price);
                            entity.setLabel1(label1);
                            entity.setLabel2(label2);
                            entity.setTradstates(tradstates);
                            sell_entityList.add(entity);
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

    @OnClick(R.id.iv_my_sell_back)
    public void onViewClicked() {
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        MySellActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        ActivityController.removeActivity(this);
    }
}
