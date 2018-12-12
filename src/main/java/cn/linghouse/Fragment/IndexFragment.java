package cn.linghouse.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.linghouse.Adapter.Index_Adapter;
import cn.linghouse.Entity.Index_Pic_Entity;
import cn.linghouse.UI.SearchActivity;
import cn.linghouse.leisure.R;
import okhttp3.Call;

public class IndexFragment extends Fragment {
    private View view;
    private ConvenientBanner convenientBanner;
    private List<String> list;
    private Index_Pic_Entity entity;
    private List<Index_Pic_Entity> pic_entity;
    private ListView listView;
    private Index_Adapter adapter;
    private View headview;
    private SharedPreferences sp;
    private TextView search;
    private String[] images = {
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment_main, container, false);
        headview = getLayoutInflater().inflate(R.layout.head_view, null);
        sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String name = sp.getString("username", "false");
        if (name.equals("false")) {
            //用户未登录
        } else {
            //用户已登录
        }
        initview();
        listView.addHeaderView(headview);
        pic_entity = new ArrayList<>();
        HotCommodity();
        adapter = new Index_Adapter(getContext(), pic_entity);
        listView.setAdapter(adapter);
        return view;
    }

    private void initview() {
        listView = view.findViewById(R.id.lv_content);
        convenientBanner = headview.findViewById(R.id.convenientbanner);
        search = headview.findViewById(R.id.et_search);
        list = Arrays.asList(images);
        convenientBanner.setPointViewVisible(true);
        convenientBanner.setPageIndicator(new int[]{R.mipmap.unselect, R.mipmap.select});
        convenientBanner.setMotionEventSplittingEnabled(true);
        convenientBanner.startTurning(1500);
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageLoader>() {
            @Override
            public NetworkImageLoader createHolder() {
                return new NetworkImageLoader();
            }
        }, list);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void HotCommodity() {
        OkHttpUtils.post().url("http://192.168.137.1:8080/leisure/commodities/search")
                .addParams("searchMethod", "goods")
                .addParams("page", "0")
                .addParams("size", "20")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("热门推荐加载失败",e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray commd = data.getJSONArray("commodities");
                    for (int i = 0; i < commd.length(); i++) {
                        JSONObject object = commd.getJSONObject(i);
                        String price = object.getString("price");
                        String commodityName = object.getString("commodityName");
                        String details = object.getString("details");
                        entity = new Index_Pic_Entity();
                        entity.setPrice(price);
                        entity.setTitle(commodityName);
                        entity.setDetail(details);
                        entity.setPic_url("http://139.199.2.193/leisure/commodity/xujunwei/290bbb197c3d4949bd9cfde56f24c3fc.jpg");
                        pic_entity.add(entity);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class NetworkImageLoader implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(context).load(data).into(imageView);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }
}