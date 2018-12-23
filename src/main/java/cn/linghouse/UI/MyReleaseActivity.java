package cn.linghouse.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import cn.linghouse.Adapter.My_Release_Adapter;
import cn.linghouse.App.Config;
import cn.linghouse.Entity.My_Release_Entity;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;

public class MyReleaseActivity extends AppCompatActivity {
    @BindView(R.id.iv_my_release_back)
    ImageView ivMyReleaseBack;
    @BindView(R.id.lv_my_release_order)
    ListView lvMyReleaseOrder;
    private My_Release_Adapter adapter;
    private List<My_Release_Entity> listentity;
    private My_Release_Entity entity;
    private String sessionid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_release);
        ButterKnife.bind(this);
        ImmersionBar.with(this).init();
        SharedPreferences share = getSharedPreferences("Session", MODE_PRIVATE);
        sessionid = share.getString("sessionid", "null");
        getRelease();
        listentity = new ArrayList<>();
        adapter = new My_Release_Adapter(this, listentity);
        lvMyReleaseOrder.setAdapter(adapter);
    }

    private void getRelease() {
        RequestParams params = new RequestParams(Config.myReleaseUrl);
        params.addHeader("cookie", sessionid);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data.length() <= 0) {
                        ToastUtil.ShowShort("还没有发布过商品");
                    } else {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            JSONArray label = object.getJSONArray("label");
                            JSONArray images = object.getJSONArray("images");
                            String price = object.getString("price");
                            String commodityName = object.getString("commodityName");
                            String createTime = object.getString("createTime");
                            String label1 = label.getString(0);
                            String label2 = label.getString(0);
                            String picurl = images.getString(0);
                            entity = new My_Release_Entity();
                            entity.setName(commodityName);
                            entity.setDate(createTime);
                            entity.setLabel1(label1);
                            entity.setLabel2(label2);
                            entity.setPrice(price);
                            entity.setPicurl(picurl);
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

    @OnClick(R.id.iv_my_release_back)
    public void onViewClicked() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        MyReleaseActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
