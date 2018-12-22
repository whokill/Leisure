package cn.linghouse.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import cn.linghouse.Adapter.My_Collection_Adapter;
import cn.linghouse.App.Config;
import cn.linghouse.Entity.MessageEvent;
import cn.linghouse.Entity.My_Collection_Entity;
import cn.linghouse.UI.GoodDetailsActivity;
import cn.linghouse.UI.LoginActivity;
import cn.linghouse.UI.SearchActivity;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;
import okhttp3.Call;

import static android.content.Context.MODE_PRIVATE;

public class MyCollectionFragment extends Fragment implements View.OnClickListener {
    private View view;
    private My_Collection_Adapter adapter;
    private My_Collection_Entity entity;
    private List<My_Collection_Entity> listentity;
    private TextView jumplogin;
    private LinearLayout shoppingempty, linenotlogged;//购物车为空显示的视图
    private ListView lvcollection;//购物车非空显示的视图
    private SharedPreferences sp;
    private String[][] img;

    /**
     * 通过这个方法来判断当前是哪个fragment，
     * 如果是当前这个fragment，就获取一次sp里面的值
     *
     * @param isVisibleToUser：是否是当前页面
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            sp = getContext().getSharedPreferences("data", MODE_PRIVATE);
            String name = sp.getString("username", "false");
            if (name.equals("false")) {
                lvcollection.setVisibility(View.GONE);
                shoppingempty.setVisibility(View.GONE);
                linenotlogged.setVisibility(View.VISIBLE);
            } else {
                //这里根据购物车是否为空进行对应视图的隐藏与显示,这里先将listview隐藏
                lvcollection.setVisibility(View.VISIBLE);
                linenotlogged.setVisibility(View.GONE);
                shoppingempty.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_collection_fragment, container, false);
        /**
         * 视图显示问题：
         * 1、当用户未登录时，所展示的界面应该是提示用户登录
         * 2、当用户登录后，向后端发送网络请求获取该用户名下对应的购物车内的物品,展示lvshoppingcar视图
         * 3、如果后后端返回该用户名下的购物车为空，再展示shoppingempty该视图
         */
        EventBus.getDefault().register(this);
        shoppingempty = view.findViewById(R.id.ll_shopping_empty);
        lvcollection = view.findViewById(R.id.lv_collection);
        linenotlogged = view.findViewById(R.id.line_not_logged_shopping_car);
        jumplogin = view.findViewById(R.id.tv_click_jump_login);
        jumplogin.setOnClickListener(this);
        getCollection();
        listentity = new ArrayList<>();
        adapter = new My_Collection_Adapter(getContext(), listentity);
        lvcollection.setAdapter(adapter);
        lvcollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String price = listentity.get(position).getPrice();
                String name = listentity.get(position).getName();
                String details = listentity.get(position).getDetails();
                String sortname = listentity.get(position).getSortname();
                String cnumber = listentity.get(position).getCnumber();
                String[] img2 = img[position];
                Intent intent = new Intent();
                intent.putExtra("title", name);
                intent.putExtra("price", price);
                intent.putExtra("details", details);
                intent.putExtra("cnumber",cnumber);
                intent.putExtra("sortname", sortname);
                intent.putExtra("imagelist", img2);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                intent.setClass(getContext(), GoodDetailsActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_click_jump_login:
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            default:
                break;
        }
    }

    /**
     * 获取登录用户名下面所对应的收藏内容
     */
    public void getCollection() {
        img = new String[0][0];
        SharedPreferences share = getContext().getSharedPreferences("Session", MODE_PRIVATE);
        String sessionid = share.getString("sessionid", "null");
        //网络请求
        OkHttpUtils.get()
                .addHeader("cookie", sessionid)
                .url(Config.myCollection)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        JSONObject sort = object.getJSONObject("sort");
                        JSONArray images = object.getJSONArray("images");
                        String picurl = images.getString(0);
                        String commodityName = object.getString("commodityName");
                        String price = object.getString("price");
                        Double numdouble = Double.parseDouble(price);
                        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
                        String numprice = format.format(numdouble);
                        String details = object.getString("details");
                        String cnumber = object.getString("commodityNumber");
                        String sortname = sort.getString("sortName");
                        img = Arrays.copyOf(img, img.length + 1);
                        img[img.length - 1] = new String[0];
                        String[] img1 = img[img.length - 1];
                        for (int k = 0; k < images.length(); k++) {
                            img1 = Arrays.copyOf(img1, img1.length + 1);
                            img1[img1.length - 1] = images.getString(k);
                        }
                        img[img.length - 1] = img1;
                        entity = new My_Collection_Entity();
                        entity.setPicurl(picurl);
                        entity.setCnumber(cnumber);
                        entity.setName(commodityName);
                        entity.setDetails(details);
                        entity.setPrice(numprice);
                        entity.setSortname(sortname);
                        listentity.add(entity);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventBus(MessageEvent event){
        listentity.clear();
        getCollection();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
