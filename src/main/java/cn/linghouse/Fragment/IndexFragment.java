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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.linghouse.Adapter.Index_Adapter;
import cn.linghouse.App.Config;
import cn.linghouse.Entity.Index_Pic_Entity;
import cn.linghouse.Entity.MessageEvent;
import cn.linghouse.UI.SearchActivity;
import cn.linghouse.UI.SortActivity;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;
import okhttp3.Call;

public class IndexFragment extends Fragment {
    @BindView(R.id.rb_head_view_phone)
    RadioButton rbHeadViewPhone;
    @BindView(R.id.rb_head_view_pet)
    RadioButton rbHeadViewPet;
    @BindView(R.id.rb_head_view_drinks)
    RadioButton rbHeadViewDrinks;
    @BindView(R.id.rb_head_view_gifts)
    RadioButton rbHeadViewGifts;
    @BindView(R.id.rb_head_view_baby)
    RadioButton rbHeadViewBaby;
    @BindView(R.id.rb_head_view_beautiful)
    RadioButton rbHeadViewBeautiful;
    @BindView(R.id.rb_head_view_foods)
    RadioButton rbHeadViewFoods;
    @BindView(R.id.rb_head_view_watches)
    RadioButton rbHeadViewWatches;
    @BindView(R.id.rb_head_view_outdoor)
    RadioButton rbHeadViewOutdoor;
    @BindView(R.id.rb_head_view_health)
    RadioButton rbHeadViewHealth;
    Unbinder unbinder;
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
        initview();
        listView.addHeaderView(headview);
        HotCommodity();
        pic_entity = new ArrayList<>();
        adapter = new Index_Adapter(getContext(), pic_entity);
        listView.setAdapter(adapter);
        unbinder = ButterKnife.bind(this, view);
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

    @OnClick({R.id.rb_head_view_phone, R.id.rb_head_view_pet,
            R.id.rb_head_view_drinks, R.id.rb_head_view_gifts,
            R.id.rb_head_view_baby, R.id.rb_head_view_beautiful,
            R.id.rb_head_view_foods, R.id.rb_head_view_watches,
            R.id.rb_head_view_outdoor, R.id.rb_head_view_health})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //数码3C
            case R.id.rb_head_view_phone:
                intentsort("手机数码");
                break;

            //生活宠物
            case R.id.rb_head_view_pet:
                intentsort("宠物生活");
                break;

            //酒水饮料
            case R.id.rb_head_view_drinks:
                intentsort("酒水饮料");
                break;

            //礼品鲜花
            case R.id.rb_head_view_gifts:
                intentsort("礼品鲜花");
                break;
            //母婴童装
            case R.id.rb_head_view_baby:
                intentsort("母婴童装");
                break;
            //美妆护肤
            case R.id.rb_head_view_beautiful:
                intentsort("美妆护肤");
                break;

            //食品生鲜
            case R.id.rb_head_view_foods:
                intentsort("食品生鲜");
                break;

            //钟表珠宝
            case R.id.rb_head_view_watches:
                intentsort("钟表珠宝");
                break;

            //运动户外
            case R.id.rb_head_view_outdoor:
                intentsort("运动户外");
                break;

            //医保健康
            case R.id.rb_head_view_health:
                intentsort("医保健康");
                break;

            default:
                break;
        }
    }

    private void HotCommodity() {
        OkHttpUtils.post()
                .url(Config.hotCommodityUrl)
                .addParams("searchMethod", "goods")
                .addParams("page", "0")
                .addParams("size", "13")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("IndexError", e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray commend = data.getJSONArray("commodities");
                    if (commend.length() <= 0) {
                        ToastUtil.ShowShort("商品数据暂时为空");
                    } else {
                        for (int i = 0; i < commend.length(); i++) {
                            JSONObject object = commend.getJSONObject(i);
                            JSONObject sort = object.getJSONObject("sort");
                            JSONArray images = object.getJSONArray("images");
                            String price = object.getString("price");
                            String cnumber = object.getString("commodityNumber");
                            String commodityName = object.getString("commodityName");
                            String details = object.getString("details");
                            String picurl = images.getString(0);
                            String sortname = sort.getString("sortName");
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
                            entity.setSortname(sortname);
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

    private void intentsort(String sortname) {
        Intent intent = new Intent();
        intent.putExtra("sortname", sortname);
        intent.setClass(getContext(), SortActivity.class);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
    }
}