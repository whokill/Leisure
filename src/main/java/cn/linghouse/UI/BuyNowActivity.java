package cn.linghouse.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lmj.mypwdinputlibrary.InputPwdView;
import com.lmj.mypwdinputlibrary.MyInputPwdUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.App.Config;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;
import okhttp3.Call;

public class BuyNowActivity extends AppCompatActivity {

    @BindView(R.id.tv_buy_now_pic)
    ImageView tvBuyNowPic;
    @BindView(R.id.tv_buy_now_title)
    TextView tvBuyNowTitle;
    @BindView(R.id.tv_buy_now_price)
    TextView tvBuyNowPrice;
    //收件人
    @BindView(R.id.tv_buy_now_recipient_name)
    TextView tvBuyNowRecipientname;
    @BindView(R.id.tv_buy_now_recipient_phone)
    TextView tvBuyNowRecipientphone;
    //大概地址
    @BindView(R.id.tv_buy_now_about_address)
    TextView tvBuyNowAboutAddress;
    //详细地址
    @BindView(R.id.tv_buy_now_details_address)
    TextView tvBuyNowDetailsAddress;
    @BindView(R.id.iv_buy_now_back)
    ImageView ivBuyNowBack;
    @BindView(R.id.btn_buy_now_shopping)
    Button btnBuyNowShopping;
    private String picurl, title, price, cnumber;
    private String sessionid;
    private String bnumber;
    private SharedPreferences sp;
    private MyInputPwdUtil util;
    private String tradingpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);
        ButterKnife.bind(this);
        ImmersionBar.with(this).init();
        SharedPreferences share = getSharedPreferences("Session", MODE_PRIVATE);
        sessionid = share.getString("sessionid", "null");
        sp = getSharedPreferences("data", MODE_PRIVATE);
        tradingpass = sp.getString("tradingpass", "未设置");
        util = new MyInputPwdUtil(this);
        picurl = getIntent().getStringExtra("picurl");
        title = getIntent().getStringExtra("title");
        price = getIntent().getStringExtra("price");
        cnumber = getIntent().getStringExtra("cnumber");

        tvBuyNowTitle.setText(title);
        tvBuyNowPrice.setText(price);

        getAddress();
    }

    //获取默认收货地址
    private void getAddress() {
        OkHttpUtils.get()
                .url(Config.getAddress)
                .addHeader("cookie", sessionid)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");
                    JSONObject object = data.getJSONObject(0);
                    String consignee = object.getString("consignee");
                    String phone = object.getString("cellphone");
                    String address = object.getString("address");
                    String detailed = object.getString("detailed");
                    tvBuyNowAboutAddress.setText(address);
                    tvBuyNowDetailsAddress.setText(detailed);
                    tvBuyNowRecipientphone.setText(phone);
                    tvBuyNowRecipientname.setText(consignee);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.iv_buy_now_back, R.id.btn_buy_now_shopping})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_buy_now_back:
                BuyNowActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btn_buy_now_shopping:
                util.show();
                sp = getSharedPreferences("data", MODE_PRIVATE);
                tradingpass = sp.getString("tradingpass", "未设置");
                if (tradingpass.equals("未设置")) {
                    ToastUtil.ShowShort("你还没有设置交易密码");
                    util.hide();
                }
                util.setListener(new InputPwdView.InputPwdListener() {
                    @Override
                    public void hide() {

                    }

                    @Override
                    public void forgetPwd() {

                    }

                    @Override
                    public void finishPwd(String pwd) {
                        if (pwd.equals(tradingpass)) {
                            create();
                            util.hide();
                        } else {
                            ToastUtil.ShowShort("密码错误");
                        }
                    }
                });
                break;
        }
    }

    private void create(){
        OkHttpUtils.post()
                .url(Config.create)
                .addHeader("cookie",sessionid)
                .addParams("commodityNumber",cnumber)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    String message = jsonObject.getString("message");
                    if (code==200){
                        ToastUtil.ShowShort(message);
                        bnumber = jsonObject.getString("data");
                        paybill(bnumber);
                    }else{
                        ToastUtil.ShowShort(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void paybill(String bnumber) {
        OkHttpUtils.post()
                .url(Config.pay)
                .addHeader("cookie", sessionid)
                .addParams("BN", bnumber)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    String data = jsonObject.getString("data");
                    String message = jsonObject.getString("message");
                    if (code == 200) {
                        ToastUtil.ShowShort(response);
                        complete(bnumber);
                    } else {
                        ToastUtil.ShowShort(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void complete(String bnumber) {
        OkHttpUtils.post()
                .url(Config.complete)
                .addHeader("cookie", sessionid)
                .addParams("BN", bnumber)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    String message = jsonObject.getString("message");
                    if (code == 200) {
                        ToastUtil.ShowLong(message);
                    } else {
                        ToastUtil.ShowShort(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
