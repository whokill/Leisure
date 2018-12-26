package cn.linghouse.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lmj.mypwdinputlibrary.InputPwdView;
import com.lmj.mypwdinputlibrary.MyInputPwdUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.App.Config;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;
import okhttp3.Call;

public class SafeSettingActivity extends AppCompatActivity {

    @BindView(R.id.iv_safe_setting_back)
    ImageView ivSafeSettingBack;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    @BindView(R.id.tv_deposit)
    TextView tvDeposit;
    @BindView(R.id.tv_trading_password)
    TextView tvChangeLogginPassword;
    @BindView(R.id.tv_trading_pass_state)
    TextView tvTradingPassState;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    private MyInputPwdUtil util;
    private SharedPreferences sp;
    private String tradingpass;
    private Dialog depositDialog;
    private TextView tvdepositcancel;
    private TextView tvdepositnext;
    private EditText etbankcard;
    private EditText etdeposit;
    private EditText etrechargenum;
    private Button btnrecharge;
    private Dialog rechargeDialog;
    private String sessionid;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_setting);
        SharedPreferences share = getSharedPreferences("Session", MODE_PRIVATE);
        sessionid = share.getString("sessionid", "null");
        sp = getSharedPreferences("data",MODE_PRIVATE);
        name = sp.getString("username","未登录");
        getBalance(name);
        util = new MyInputPwdUtil(this);
        util.getMyInputDialogBuilder().setAnimStyle(R.style.dialog_anim);
        ImmersionBar.with(this).init();
        ButterKnife.bind(this);
        tradingpass = sp.getString("tradingpass", "未设置");
        ToastUtil.ShowShort(name);
        if (tradingpass.equals("未设置")) {
            tvTradingPassState.setText("未设置");
        } else {
            tvTradingPassState.setText("已设置");
        }
    }

    @OnClick({R.id.iv_safe_setting_back, R.id.tv_recharge, R.id.tv_deposit, R.id.tv_trading_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回图标
            case R.id.iv_safe_setting_back:
                this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            //余额提现
            case R.id.tv_deposit:
                if (depositDialog == null) {
                    depositDialog = new Dialog(this, R.style.Dialog_Fullscreen);
                }
                View view1 = LayoutInflater.from(this).inflate(R.layout.depoit_dialog, null);
                depositDialog.setContentView(view1);
                tvdepositcancel = depositDialog.findViewById(R.id.tv_deposit_cancel);
                tvdepositnext = depositDialog.findViewById(R.id.tv_deposit_next);
                etbankcard = depositDialog.findViewById(R.id.et_bank_card_num);
                etdeposit = depositDialog.findViewById(R.id.et_deposit);
                setDialogWindowAttr(depositDialog, getApplicationContext(), Gravity.CENTER);
                tvdepositcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        depositDialog.dismiss();
                    }
                });
                tvdepositnext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(etbankcard.getText().toString())) {
                            etbankcard.setError("银行卡号为空,你要提现到哪里去？");
                        } else if (TextUtils.isEmpty(etdeposit.getText().toString())) {
                            etdeposit.setError("提现金额为空,你要提个寂寞？");
                        } else {
                            depositDialog.dismiss();
                            util.show();
                            util.setListener(new InputPwdView.InputPwdListener() {
                                @Override
                                public void hide() {

                                }

                                @Override
                                public void forgetPwd() {

                                }

                                @Override
                                public void finishPwd(String pwd) {
                                    sp = getSharedPreferences("data", MODE_PRIVATE);
                                    tradingpass = sp.getString("tradingpass", "未设置");
                                    if (pwd.equals(tradingpass)) {
                                        //发起网络请求
                                        ToastUtil.ShowShort("密码正确,发起网络请求");
                                        util.hide();
                                    } else {
                                        ToastUtil.ShowShort("交易密码错误");
                                    }
                                }
                            });
                        }
                    }
                });
                break;
            //交易密码
            case R.id.tv_trading_password:
                util.setListener(new InputPwdView.InputPwdListener() {
                    @Override
                    public void hide() {
                        util.hide();
                    }

                    @Override
                    public void forgetPwd() {

                    }

                    @Override
                    public void finishPwd(String pwd) {
                        util.hide();
                        tradingpass = sp.getString("tradingpass", "未设置");
                        if (pwd.equals(tradingpass)) {
                        } else {
                            ToastUtil.ShowShort("密码已更新");
                        }
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("tradingpass", pwd);
                        editor.commit();
                        tvTradingPassState.setText("已设置");
                    }
                });
                util.show();
                break;
            //余额充值
            case R.id.tv_recharge:
                if (rechargeDialog == null) {
                    rechargeDialog = new Dialog(this, R.style.Dialog_Fullscreen);
                }
                View view2 = LayoutInflater.from(this).inflate(R.layout.recharge_dialog, null);
                rechargeDialog.setContentView(view2);
                etrechargenum = rechargeDialog.findViewById(R.id.et_recharge_num);
                btnrecharge = rechargeDialog.findViewById(R.id.btn_recharge);
                setDialogWindowAttr(rechargeDialog, this, Gravity.CENTER);
                btnrecharge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //发起网络请求
                        if (TextUtils.isEmpty(etrechargenum.getText().toString())){
                            etrechargenum.setError("充值金额为空,你是打算充个寂寞?");
                        }else{
                            OkHttpUtils.post()
                                    .url(Config.recharge)
                                    .addHeader("cookie",sessionid)
                                    .addParams("RMB", etrechargenum.getText().toString())
                                    .build().execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {

                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String message = jsonObject.getString("message");
                                        int code = jsonObject.getInt("code");
                                        String data = jsonObject.getString("data");
                                        if (code == 200) {
                                            ToastUtil.ShowShort(message + "," + data);
                                            rechargeDialog.dismiss();
                                            getBalance(name);
                                        } else {
                                            ToastUtil.ShowShort(message);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    //获取对应账户的余额
    private void getBalance(String name){
        OkHttpUtils.get()
                .addHeader("cookie",sessionid)
                .url(Config.infomation+name)
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
                        JSONObject data = jsonObject.getJSONObject("data");
                        String balance = data.getString("balance");
                        tvBalance.setText(balance);
                    }else{
                        ToastUtil.ShowShort(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void setDialogWindowAttr(Dialog dlg, Context ctx, int gravity) {
        Window window = dlg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = gravity;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dlg.getWindow().setAttributes(lp);
        dlg.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}