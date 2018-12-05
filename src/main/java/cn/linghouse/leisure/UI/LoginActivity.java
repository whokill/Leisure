package cn.linghouse.leisure.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import cn.linghouse.leisure.App.MyApplication;
import cn.linghouse.leisure.Fragment.PersonalFragment;
import cn.linghouse.leisure.R;
import cn.linghouse.leisure.Util.CodeUtils;
import cn.linghouse.leisure.Util.ToastUtil;
import okhttp3.Call;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etCode;
    private ImageView iv_code;
    private Button btnLogin;
    private CodeUtils codeUtils;
    private String login_url = "http://139.199.2.193:8080/leisure/user/login";
    private String data;
    private TextView tvRegister;
    private TextView tvchangecode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        GetCode();
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etCode = findViewById(R.id.et_code);
        btnLogin = findViewById(R.id.btn_login);
        iv_code = findViewById(R.id.iv_code);

        tvchangecode = findViewById(R.id.tv_change_code);
        tvRegister = findViewById(R.id.tv_register);

        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvchangecode.setOnClickListener(this);
        //TextView设置下划线
        tvRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvchangecode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        etUsername.setText("miao");
        etPassword.setText("123");
    }

    private void GetCode() {
        OkHttpUtils.get().url("http://139.199.2.193:8080/leisure/user/verification/get")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtil.ShowShort(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            data = jsonObject.getString("data");
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("true")){
                                codeUtils = CodeUtils.getInstance();
                                Bitmap bitmap = codeUtils.createBitmap(data);
                                iv_code.setImageBitmap(bitmap);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (TextUtils.isEmpty(etUsername.getText().toString())){
                    etUsername.setError("用户名不能为空");
                }else if (TextUtils.isEmpty(etPassword.getText().toString())){
                    etPassword.setError("密码不能为空");
                }else if (TextUtils.isEmpty(etCode.getText().toString())){
                    etCode.setError("验证码不能为空");
                }else{
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    String code = etCode.getText().toString();
                    OkHttpUtils.post()
                            .url(login_url)
                            .addParams("username",username)
                            .addParams("password",password)
                            .addParams("verification",code)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {

                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String msg = jsonObject.getString("msg");
                                        if (msg.equals("true")){
                                            JSONObject object = jsonObject.getJSONObject("data");
                                            JSONObject object1 = object.getJSONObject("user");
                                            String username = object1.getString("username");
                                            SharedPreferences pre = LoginActivity.this.getSharedPreferences("data",MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pre.edit();
                                            editor.putString("username",username);
                                            editor.commit();
                                            ToastUtil.ShowShort("登录成功");
                                            startActivity(new Intent(LoginActivity.this,IndexActivity.class));
                                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                        }else{
                                            ToastUtil.ShowShort(msg);
                                            etCode.setText("");
                                            GetCode();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }

                break;
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            case R.id.tv_change_code:
                GetCode();
                break;
        }
    }
}
