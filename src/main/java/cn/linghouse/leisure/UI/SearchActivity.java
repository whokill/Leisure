package cn.linghouse.leisure.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.linghouse.leisure.Adapter.Search_Adapter;
import cn.linghouse.leisure.Entity.Search_Entity;
import cn.linghouse.leisure.R;
import cn.linghouse.leisure.Util.ToastUtil;
import okhttp3.Call;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView lvsearch;
    private TextView tvsearch;
    private EditText etsearch;
    private NiceSpinner spinner;
    private ImageView searchback;
    private Search_Adapter adapter;
    private TextView searchfaild;
    private Search_Entity entity;
    private TextView tvclassify;
    private ZLoadingDialog dialog;
    private Button restart,sure;
    private SlidingMenu slidingMenu;
    private LinearLayout linbutton;
    private List<Search_Entity> search_entity;
    private String search_url = "http://139.199.2.193:8080/leisure/commodity/search/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //沉浸式状态栏
        ImmersionBar.with(SearchActivity.this).init();
        //初始化控件
        initview();
        //初始化侧滑菜单
        intislidemenu();
        //检查是否存在虚拟按键
        checkDeviceHasNavigationBar(SearchActivity.this);
        search_entity = new ArrayList<>();
        adapter = new Search_Adapter(search_entity,this);
        lvsearch.setAdapter(adapter);
    }

    //初始化第三方对话框
    private void initdialog(){
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
        searchfaild = findViewById(R.id.tv_search_faild);
        searchback = findViewById(R.id.iv_search_back);
        etsearch = findViewById(R.id.et_search_goods);
        lvsearch = findViewById(R.id.lv_search);
        tvsearch = findViewById(R.id.tv_search);
        tvclassify = findViewById(R.id.tv_classify);
        spinner = findViewById(R.id.ns_sorting);
        searchfaild.setVisibility(View.GONE);//默认设置没有搜索出结果的视图为不可见
        tvclassify.setOnClickListener(this);
        searchback.setOnClickListener(this);
        tvsearch.setOnClickListener(this);
        final List<String> data = new ArrayList<>();
        data.add("综合排序");
        data.add("价格升序");
        data.add("价格降序");
        spinner.attachDataSource(data);//设置下拉数据源
        //软键盘点击事件监听
        etsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId==EditorInfo.IME_ACTION_SEARCH){
                    if (TextUtils.isEmpty(etsearch.getText().toString())){
                        etsearch.setError("输入宝贝名称试试看");
                    }else{
                        search_entity.clear();
                        initdialog();
                        searchGoogds(etsearch.getText().toString(),"1","10");
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
                switch (position){
                    case 0:
                        ToastUtil.ShowShort(SearchActivity.this,"综合排序");
                        break;
                    case 1:
                        ToastUtil.ShowShort(SearchActivity.this,"价格升序");
                        break;
                    case 2:
                        ToastUtil.ShowShort(SearchActivity.this,"价格降序");
                        break;
                }
            }
        });

        lvsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String price = entity.getPice();
                String title = entity.getTitle();
                Intent intent = new Intent();
                intent.putExtra("title",title);
                intent.putExtra("price",price);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                intent.setClass(SearchActivity.this,GoodDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化侧滑菜单
     */
    private void intislidemenu(){
        slidingMenu = new SlidingMenu(this);
        slidingMenu.attachToActivity(this,SlidingMenu.SLIDING_WINDOW);
        slidingMenu.setMode(SlidingMenu.RIGHT);//侧滑菜单滑出方向
        slidingMenu.setBehindWidth(800);//侧滑菜单宽度
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setMenu(R.layout.right_menu);//侧滑菜单布局视图
        //初始化侧滑菜单中的控件
        restart = slidingMenu.findViewById(R.id.btn_restart);
        sure = slidingMenu.findViewById(R.id.btn_sure);
        linbutton = slidingMenu.findViewById(R.id.lin_button);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_classify:
                if (!slidingMenu.isSecondaryMenuShowing()){
                    slidingMenu.showSecondaryMenu();
                }else{
                    slidingMenu.toggle();
                }
                break;
            case R.id.tv_search:
                if (TextUtils.isEmpty(etsearch.getText().toString())){
                    etsearch.setError("输入宝贝名称试试看");
                }else{
                    search_entity.clear();
                    initdialog();
                    searchGoogds(etsearch.getText().toString(),"1","10");
                }
                break;
            case R.id.iv_search_back:
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                SearchActivity.this.finish();
                break;
                default:
                    break;
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
                layout.setMargins(0,0,0,getNavigationBarHeight(this)+10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 测量底部导航栏的高度
     * @param mActivity:上下文环境
     * @return：返回测量出的底部导航栏高度
     */
    private int getNavigationBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 搜索商品
     * @param type：商品名
     * @param page：商品页码
     * @param number：一页所要显示的商品数量
     */
    private void searchGoogds(String type,String page,String number){
        String url = search_url+"/"+type+"/"+page+"/"+number;
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length()==0){
                                lvsearch.setVisibility(View.GONE);
                                searchfaild.setVisibility(View.VISIBLE);
                            }else{
                                lvsearch.setVisibility(View.VISIBLE);
                                searchfaild.setVisibility(View.GONE);
                                for (int i =0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String title = object.getString("name");
                                    String price = object.getString("price");
                                    String createTime = object.getString("createTime");
                                    String holder = object.getString("holder");
                                    String creater = object.getString("creator");
                                    //将字符串转换成日期格式
                                    SimpleDateFormat sdf = new SimpleDateFormat( " yyyy年MM月dd日 " );
                                    createTime = sdf.format(new Date());
                                    //将字符串转换成货币类型
                                    Double numDouble = Double.parseDouble(price);
                                    NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);

                                    String numString = format.format(numDouble);
                                    entity = new Search_Entity();
                                    entity.setTitle(title);
                                    entity.setPice(numString);
                                    entity.setPinkage(holder);
                                    entity.setData(createTime);
                                    entity.setPlace(creater);
                                    search_entity.add(entity);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
