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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.linghouse.Adapter.Index_Adapter;
import cn.linghouse.App.Config;
import cn.linghouse.Entity.Index_Pic_Entity;
import cn.linghouse.Entity.MessageEvent;
import cn.linghouse.UI.SearchActivity;
import cn.linghouse.Util.ToastUtil;
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
    private String[][] img = new String[0][0];
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
        HotCommodity();
        listView.addHeaderView(headview);
        EventBus.getDefault().register(this);
        pic_entity = new ArrayList<>();
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
    }

    private void HotCommodity() {
        OkHttpUtils.post()
                .url(Config.hotCommodityUrl)
                .addParams("searchMethod", "goods")
                .addParams("page", "0")
                .addParams("size", "20")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("IndexError",e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray commend = data.getJSONArray("commodities");
                    if (commend.length()<=0){
                        ToastUtil.ShowShort("商品数据暂时为空");
                    }else{
                        for (int i = 0; i < commend.length(); i++) {
                            JSONObject object = commend.getJSONObject(i);
                            JSONArray images = object.getJSONArray("images");
                            String price = object.getString("price");
                            String cnumber = object.getString("commodityNumber");
                            String commodityName = object.getString("commodityName");
                            String details = object.getString("details");
                            String picurl = images.getString(0);
                            img = Arrays.copyOf(img, img.length + 1);
                            img[img.length - 1] = new String[0];
                            String[] img1 = img[img.length - 1];
                            for (int k = 0; k < images.length(); k++) {
                                img1 = Arrays.copyOf(img1, img1.length + 1);
                                img1[img1.length - 1] = images.getString(k);
                            }
                            img[img.length - 1] = img1;
                            String[] img2 = img[i];
                            entity = new Index_Pic_Entity();
                            entity.setPrice(price);
                            entity.setCnumber(cnumber);
                            entity.setTitle(commodityName);
                            entity.setDetail(details);
                            entity.setPic_url(picurl);
                            entity.setImages(img2);
                            pic_entity.add(entity);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(MessageEvent event){
        pic_entity.clear();
        HotCommodity();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}