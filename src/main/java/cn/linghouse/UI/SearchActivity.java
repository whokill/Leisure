package cn.linghouse.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.linghouse.Adapter.Search_Adapter;
import cn.linghouse.App.ActivityController;
import cn.linghouse.App.Config;
import cn.linghouse.App.MyApplication;
import cn.linghouse.Entity.Search_Entity;
import cn.linghouse.leisure.R;
import okhttp3.Call;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView lvsearch;
    private SmartRefreshLayout refreshLayout;
    private TextView tvsearch;
    private EditText etsearch;
    private NiceSpinner spinner;
    private ImageView searchback;
    private Search_Adapter adapter;
    private TextView searchfaild;
    private Search_Entity entity;
    private TextView tvclassify, tvnews;
    private ZLoadingDialog dialog;
    private Button restart, sure;
    private SlidingMenu slidingMenu;
    private RadioGroup rgutils;
    private View view;
    private EditText slmin, slmax;
    private LinearLayout linbutton, llscreening;
    private List<Search_Entity> search_entity;
    private String[][] img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActivityController.addActivity(this);
        //沉浸式状态栏
        ImmersionBar.with(SearchActivity.this).init();
        view = LayoutInflater.from(this).inflate(R.layout.search_listview_footview, null);
        //初始化控件
        initview();
        //初始化侧滑菜单
        intislidemenu();
        //检查是否存在底部导航栏
        checkDeviceHasNavigationBar(SearchActivity.this);
        search_entity = new ArrayList<>();
        adapter = new Search_Adapter(search_entity, SearchActivity.this);
        lvsearch.setAdapter(adapter);
    }

    //初始化第三方对话框
    private void initdialog() {
        dialog = new ZLoadingDialog(SearchActivity.this);
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//对话框样式
                .setLoadingColor(Color.BLACK)//颜色
                .setHintText("努力搜索中.....")//提示文字
                .setHintTextSize(16)//提示文字大小
                .setHintTextColor(Color.GRAY)//提示文字颜色
                .setDurationTime(0.5)//动画时间百分比 0.5倍
                .setDialogBackgroundColor(Color.parseColor("#DEDEDE"))//设置背景色，默认白色
                .show();
    }

    private void initview() {
        llscreening = findViewById(R.id.ll_screening);
        llscreening.setVisibility(View.GONE);
        tvnews = findViewById(R.id.tv_news);
        refreshLayout = findViewById(R.id.refresh);
        searchfaild = findViewById(R.id.tv_search_faild);
        searchback = findViewById(R.id.iv_search_back);
        etsearch = findViewById(R.id.et_search_goods);
        lvsearch = findViewById(R.id.lv_search);
        tvsearch = findViewById(R.id.tv_search);
        tvclassify = findViewById(R.id.tv_classify);
        spinner = findViewById(R.id.ns_sorting);
        etsearch.setFocusable(true);
        etsearch.setFocusableInTouchMode(true);
        etsearch.requestFocus();
        //设置禁止下拉刷新
        refreshLayout.setEnableRefresh(false);
        searchfaild.setVisibility(View.GONE);//默认设置没有搜索出结果的视图为不可见
        tvclassify.setOnClickListener(this);
        searchback.setOnClickListener(this);
        tvsearch.setOnClickListener(this);
        tvnews.setOnClickListener(this);
        final List<String> data = new ArrayList<>();
        data.add("综合排序");
        data.add("价格升序");
        data.add("价格降序");
        spinner.attachDataSource(data);//设置下拉数据源
        //软键盘点击事件监听
        etsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(etsearch.getText().toString())) {
                        etsearch.setError("输入宝贝名称试试看");
                    } else {
                        img = new String[0][0];
                        search_entity.clear();
                        initdialog();
                        searchGoogds_Default(etsearch.getText().toString(), "0", "20");
                        hideSoftKeyboard(SearchActivity.this);
                    }
                    return true;
                }
                return false;
            }
        });

        /**
         * 下拉框的点击事件
         */
        spinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        search_entity.clear();
                        initdialog();
                        searchGoogds_Default(etsearch.getText().toString(), "0", "20");
                        break;
                    case 1:
                        search_entity.clear();
                        initdialog();
                        priceWay(etsearch.getText().toString(), "desc");
                        break;
                    case 2:
                        search_entity.clear();
                        initdialog();
                        priceWay(etsearch.getText().toString(), "asc");
                        break;
                    default:
                        break;
                }
            }
        });

        lvsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String price = search_entity.get(position).getPice();
                String title = search_entity.get(position).getName();
                String detail = search_entity.get(position).getDetail();
                String sortname = search_entity.get(position).getSortname();
                String cnumber = search_entity.get(position).getCnumber();
                String[] img2 = img[position];
                Intent intent = new Intent();
                intent.putExtra("title", title);
                intent.putExtra("price", price);
                intent.putExtra("details", detail);
                intent.putExtra("sortname", sortname);
                intent.putExtra("cnumber",cnumber);
                intent.putExtra("imagelist",img2);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                intent.setClass(SearchActivity.this, GoodDetailsActivity.class);
                startActivity(intent);
            }
        });

        if (checkDeviceHasNavigationBar(MyApplication.getContext()) == true) {
            //存在底部导航栏
            view.setMinimumHeight(getNavigationBarHeight(SearchActivity.this));
            lvsearch.addFooterView(view, null, false);
            lvsearch.setFooterDividersEnabled(false);
        } else if (checkDeviceHasNavigationBar(MyApplication.getContext()) == false) {
            //不存在底部导航栏
            view.setVisibility(View.GONE);
            lvsearch.removeFooterView(view);
        }
    }

    /**
     * 初始化侧滑菜单
     */
    private void intislidemenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        slidingMenu.setMode(SlidingMenu.RIGHT);//侧滑菜单滑出方向
        slidingMenu.setBehindWidth(800);//侧滑菜单宽度
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setMenu(R.layout.right_menu);//侧滑菜单布局视图
        //初始化侧滑菜单中的控件
        restart = slidingMenu.findViewById(R.id.btn_restart);
        sure = slidingMenu.findViewById(R.id.btn_sure);
        linbutton = slidingMenu.findViewById(R.id.lin_button);
        slmin = slidingMenu.findViewById(R.id.sl_et_price_min);
        slmax = slidingMenu.findViewById(R.id.sl_et_price_max);
        slmin.hasFocus();

        restart.setOnClickListener(this);
        sure.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_classify:
                if (!slidingMenu.isSecondaryMenuShowing()) {
                    slidingMenu.showSecondaryMenu();
                } else {
                    //隐藏侧滑菜单
                    slidingMenu.toggle();
                }
                break;
            case R.id.tv_search:
                if (TextUtils.isEmpty(etsearch.getText().toString())) {
                    etsearch.setError("输入宝贝名称试试看");
                } else {
                    img = new String[0][0];
                    search_entity.clear();
                    searchGoogds_Default(etsearch.getText().toString(), "0", "20");
                    initdialog();
                }
                break;
            case R.id.iv_search_back:
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                SearchActivity.this.finish();
                break;
            //搜索界面上的最新发布
            case R.id.tv_news:
                search_entity.clear();
                initdialog();
                searchGoods_news(etsearch.getText().toString(), "0", "20");
                break;
            //侧滑菜单中的重置按钮,清除所有已经设置了的数据
            case R.id.btn_restart:
                rgutils.clearCheck();
                slmin.setText("");
                slmax.setText("");
                slidingMenu.toggle();
                break;
            //侧滑菜单中的确定按钮,发起网络请求，将筛选条件发送到后端
            case R.id.btn_sure:
                search_entity.clear();
                initdialog();
                search_Goods_Area(etsearch.getText().toString(), slmax.getText().toString(), slmin.getText().toString(), "0", "20");
                slidingMenu.toggle();
                break;
            default:
                break;
        }
    }

    /**
     * 搜索商品
     *
     * @param name:商品名称
     * @param page：商品页码
     * @param size：一页显示的商品数量
     */
    private void searchGoogds_Default(String name, String page, String size) {
        OkHttpUtils.post()
                .url(Config.searchCommodityUrl)
                .addParams("commodityName",name)
                .addParams("searchMethod", "default")
                .addParams("page", page)
                .addParams("size", size)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        llscreening.setVisibility(View.VISIBLE);
                        SaxJson(response);
                    }
                });
    }

    /**
     * 通过价格升序商品
     *
     * @param name：商品名称
     */
    private void priceWay(String name, String priceway) {
        OkHttpUtils.post()
                .url(Config.searchCommodityUrl)
                .addParams("commodityName", name)
                .addParams("searchMethod", "price")
                .addParams("order", priceway)
                .addParams("page", "0")
                .addParams("size", "20")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                SaxJson(response);
            }
        });
    }

    /**
     * 根据发布时间排序
     *
     * @param name：商品名称
     * @param page：商品页码
     * @param size：一页的数量
     */
    private void searchGoods_news(String name, String page, String size) {
        OkHttpUtils.post()
                .url(Config.searchCommodityUrl)
                .addParams("commodityName",name)
                .addParams("searchMethod", "latest")
                .addParams("page", page)
                .addParams("size", size)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                SaxJson(response);
            }
        });
    }

    /**
     * 侧滑菜单里面根据价格区间排序商品
     *
     * @param name：商品名称
     * @param max：商品的最高价
     * @param min：商品的最低价
     * @param page：商品的页码
     * @param size：一页的数量
     */
    private void search_Goods_Area(String name, String max, String min, String page, String size) {
        OkHttpUtils.post()
                .url(Config.searchCommodityUrl)
                .addParams("commodityName", name)
                .addParams("searchMethod", "area")
                .addParams("page", page)
                .addParams("size", size)
                .addParams("max", max)
                .addParams("min", min)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                SaxJson(response);
            }
        });
    }

    /**
     * 将统一的解析JSON的方式封装成一个方法
     *
     * @param response：返回的JSON数据
     */
    private void SaxJson(String response) {
        dialog.dismiss();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray commd = data.getJSONArray("commodities");
            if (commd.length() < 1) {
                //后台返回的商品数组为空
                searchfaild.setVisibility(View.VISIBLE);
                llscreening.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.GONE);
            } else {
                //商品数组不为空
                llscreening.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.VISIBLE);
                searchfaild.setVisibility(View.GONE);
                for (int i = 0; i < commd.length(); i++) {
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
                    String label2 = label.getString(1);
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
                    entity = new Search_Entity();
                    entity.setPicurl(picurl);
                    entity.setName(name);
                    entity.setPice(numprice);
                    entity.setDetail(details);
                    entity.setCnumber(cnumber);
                    entity.setSortname(sortname);
                    entity.setLabel1(label1);
                    entity.setLabel2(label2);
                    search_entity.add(entity);
                }
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 判断是否存在NavigationBar
     *
     * @param context：上下文环境
     * @return：返回是否存在(true/false)
     */
    public boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                //不存在底部导航栏
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                //存在底部导航栏
                hasNavigationBar = true;
                //手动设置控件的margin
                LinearLayout.LayoutParams layout = (LinearLayout.LayoutParams) linbutton.getLayoutParams();
                layout.setMargins(0, 0, 0, getNavigationBarHeight(this) + 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 测量底部导航栏的高度
     *
     * @param mActivity:上下文环境
     * @return：返回测量出的底部导航栏高度
     */
    private int getNavigationBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        ActivityController.removeActivity(this);
    }
}
