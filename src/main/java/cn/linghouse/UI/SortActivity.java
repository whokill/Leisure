package cn.linghouse.UI;
/*
 *Create by on 2018/12/25
 *Author:Linghouse
 *describe:根据商品分类查找商品
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.Adapter.SortAdapter;
import cn.linghouse.App.Config;
import cn.linghouse.Entity.Search_Entity;
import cn.linghouse.Entity.Sort_Entity;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;
import okhttp3.Call;

public class SortActivity extends AppCompatActivity {
    @BindView(R.id.iv_sort_back)
    ImageView ivSortBack;
    @BindView(R.id.tv_sort_name)
    TextView tvSortName;
    @BindView(R.id.lv_sort)
    ListView lvSort;
    private String sortname;
    private String sessionid;
    private List<Sort_Entity> entityList;
    private Sort_Entity entity;
    private SortAdapter adapter;
    private String[][] img = new String[0][0];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        ButterKnife.bind(this);
        ImmersionBar.with(SortActivity.this).init();
        SharedPreferences share = getSharedPreferences("Session", MODE_PRIVATE);
        sessionid = share.getString("sessionid", "null");
        sortname = getIntent().getStringExtra("sortname");
        getData();
        entityList = new ArrayList<>();
        adapter = new SortAdapter(entityList,this);
        lvSort.setAdapter(adapter);
        tvSortName.setText(sortname);
        lvSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String price = entityList.get(position).getPice();
                String title = entityList.get(position).getName();
                String detail = entityList.get(position).getDetail();
                String sortname = entityList.get(position).getSortname();
                String cnumber = entityList.get(position).getCnumber();
                String[] img2 = img[position];
                Intent intent = new Intent();
                intent.putExtra("title", title);
                intent.putExtra("price", price);
                intent.putExtra("details", detail);
                intent.putExtra("sortname",sortname);
                intent.putExtra("cnumber",cnumber);
                intent.putExtra("imagelist",img2);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                intent.setClass(SortActivity.this, GoodDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.iv_sort_back)
    public void onViewClicked() {
        SortActivity.this.finish();
    }

    private void getData() {
        OkHttpUtils.post()
                .url(Config.searchCommodityUrl)
                .addHeader("cookie", sessionid)
                .addParams("searchMethod", "sort")
                .addParams("page", "0")
                .addParams("size", "20")
                .addParams("sortName", sortname)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray commd = data.getJSONArray("commodities");
                    if (commd.length() <= 0) {
                        ToastUtil.ShowShort("该分类下还没有商品");
                    } else {
                        for (int i =0;i<commd.length();i++){
                            JSONObject object = commd.getJSONObject(i);
                            JSONObject sort = object.getJSONObject("sort");
                            JSONArray label = object.getJSONArray("label");
                            JSONArray images = object.getJSONArray("images");
                            String name = object.getString("commodityName");
                            String price = object.getString("price");
                            String details = object.getString("details");
                            String score = object.getString("score");
                            String sortname = sort.getString("sortName");
                            String label1 = label.getString(0);
                            //String label2 = label.getString(1);
                            String picurl = images.getString(0);
                            String cnumber = object.getString("commodityNumber");
                            img = Arrays.copyOf(img, img.length + 1);
                            img[img.length - 1] = new String[0];
                            String[] img1 = img[img.length - 1];
                            for (int k = 0; k < images.length(); k++) {
                                img1 = Arrays.copyOf(img1, img1.length + 1);
                                img1[img1.length - 1] = images.getString(k);
                            }
                            img[img.length - 1] = img1;
                            //将String类型的单价转换成货币类型
                            Double numdouble = Double.parseDouble(price);
                            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
                            String numprice = format.format(numdouble);
                            entity = new Sort_Entity();
                            entity.setPicurl(picurl);
                            entity.setName(name);
                            entity.setPice(numprice);
                            entity.setDetail(details);
                            entity.setScore(score);
                            entity.setSortname(sortname);
                            entity.setCnumber(cnumber);
                            entity.setLabel1(label1);
                            //entity.setLabel2(label2);
                            entityList.add(entity);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(SortActivity.this).destroy();
    }
}
