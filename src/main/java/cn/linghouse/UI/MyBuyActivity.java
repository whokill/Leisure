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
import com.zhy.http.okhttp.OkHttpUtils;

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
import cn.linghouse.Adapter.My_Buy_Adapter;
import cn.linghouse.App.Config;
import cn.linghouse.Entity.All_Order_Entity;
import cn.linghouse.Entity.My_Buy_Entity;
import cn.linghouse.leisure.R;

public class MyBuyActivity extends AppCompatActivity {
    @BindView(R.id.iv_my_buy_back)
    ImageView ivMyBuyBack;
    @BindView(R.id.lv_my_buy_order)
    ListView lvMyBuyOrder;
    private String sessionid;
    private List<My_Buy_Entity> listentity;
    private My_Buy_Entity entity;
    private My_Buy_Adapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_buy);
        ImmersionBar.with(this).init();
        ButterKnife.bind(this);
        SharedPreferences share = getSharedPreferences("Session", MODE_PRIVATE);
        sessionid = share.getString("sessionid", "null");
        getMybuy();
        listentity = new ArrayList<>();
        adapter = new My_Buy_Adapter(this,listentity);
        lvMyBuyOrder.setAdapter(adapter);
    }

    @OnClick(R.id.iv_my_buy_back)
    public void onViewClicked() {
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        MyBuyActivity.this.finish();
    }

    private void getMybuy(){
        RequestParams params = new RequestParams(Config.myBuyUrl);
        params.addHeader("cookie",sessionid);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        String editTime = object.getString("editTime");
                        JSONObject commd = object.getJSONObject("commodity");
                        JSONArray images = commd.getJSONArray("images");
                        JSONArray label = commd.getJSONArray("label");
                        String price = commd.getString("price");
                        String commodityName = commd.getString("commodityName");
                        String picurl = images.getString(0);
                        String label1 = label.getString(0);
                        String label2 = label.getString(1);
                        entity = new My_Buy_Entity();
                        entity.setMy_buy_picurl(picurl);
                        entity.setMy_buy_name(commodityName);
                        entity.setMy_buy_price(price);
                        entity.setMy_buy_label1(label1);
                        entity.setMy_buy_label2(label2);
                        entity.setMy_buy_date(editTime);
                        listentity.add(entity);
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
    }
}
