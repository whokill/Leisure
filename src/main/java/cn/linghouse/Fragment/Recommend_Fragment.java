package cn.linghouse.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.linghouse.Adapter.Recommend_Adapter;
import cn.linghouse.App.Config;
import cn.linghouse.Entity.Recommend_Entity;
import cn.linghouse.UI.LoginActivity;
import cn.linghouse.leisure.R;
import okhttp3.Call;

public class Recommend_Fragment extends Fragment {
    @BindView(R.id.lv_recommend)
    ListView lvRecommend;
    Unbinder unbinder;
    @BindView(R.id.line_commend_loggin)
    LinearLayout lineCommendLoggin;
    @BindView(R.id.tv_recommend_click_login)
    TextView tvRecommendClickLogin;
    @BindView(R.id.line_commend_not_logged)
    LinearLayout lineCommendNotLogged;
    private View view;
    private List<Recommend_Entity> entity;
    private Recommend_Entity recommend_entity;
    private Recommend_Adapter adapter;
    private String isloggin;
    private String[][] img = new String[0][0];
    private SharedPreferences sp;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            sp = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
            isloggin = sp.getString("username", "false");
            if (isloggin.equals("false")) {
                //用户未登录
                lineCommendLoggin.setVisibility(View.GONE);
                lineCommendNotLogged.setVisibility(View.VISIBLE);
            } else {
                //用户已登录
                lineCommendNotLogged.setVisibility(View.GONE);
                lineCommendLoggin.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recommend, container, false);
        unbinder = ButterKnife.bind(this, view);
        ImmersionBar.with(this).init();
        getRecommend();
        entity = new ArrayList<>();
        adapter = new Recommend_Adapter(entity, getContext());
        lvRecommend.setAdapter(adapter);
        return view;
    }

    private void getRecommend() {
        OkHttpUtils.post()
                .url(Config.recommendCommodityUrl)
                .addParams("searchMethod", "goods")
                .addParams("page", "0")
                .addParams("size", "20")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray commend = data.getJSONArray("commodities");
                    for (int i = 0; i < commend.length(); i++) {
                        JSONObject object = commend.getJSONObject(i);
                        JSONArray images = object.getJSONArray("images");
                        String commodityName = object.getString("commodityName");
                        String price = object.getString("price");
                        String details = object.getString("details");
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
                        String[] img2 = img[i];
                        recommend_entity = new Recommend_Entity();
                        recommend_entity.setRecommed_title(commodityName);
                        recommend_entity.setRecommed_price(price);
                        recommend_entity.setCnumber(cnumber);
                        recommend_entity.setRecommed_detail(details);
                        recommend_entity.setRecommed_pic_url(picurl);
                        recommend_entity.setRecommed_images(img2);
                        entity.add(recommend_entity);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        ImmersionBar.with(this).destroy();
    }

    @OnClick({R.id.tv_recommend_click_login})
    public void onViewClicked() {
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
