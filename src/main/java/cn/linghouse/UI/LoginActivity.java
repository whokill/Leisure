package cn.linghouse.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import cn.linghouse.App.ActivityController;
import cn.linghouse.App.Config;
import cn.linghouse.Fragment.IndexFragment;
import cn.linghouse.Util.CodeUtils;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etCode;
    private ImageView iv_code;
    private Button btnLogin;
    private CodeUtils codeUtils;
    private TextView tvfindpass;
    private TextView tvRegister;
    private TextView tvchangecode;
    private Handler handler;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityController.addActivity(this);
        initView();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 2) {
                    try {
                        JSONObject jsonObject = new JSONObject((String) msg.obj);
                        String message = jsonObject.getString("message");
                        String code = jsonObject.getString("code");
                        String name = jsonObject.getString("data");
                        if (code.equals("200")) {
                            SharedPreferences pre = LoginActivity.this.getSharedPreferences("data", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pre.edit();
                            editor.putString("username", name);
                            editor.commit();
                            ToastUtil.ShowShort("登录成功");
                            ToastUtil.ShowLong(pre.getString("username", "未写入"));
                            startActivity(new Intent(LoginActivity.this, IndexActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            LoginActivity.this.finish();
                        } else {
                            ToastUtil.ShowShort(message);
                            etCode.setText("");
                            GetCode();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void initView() {
        GetCode();
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etCode = findViewById(R.id.et_code);
        btnLogin = findViewById(R.id.btn_login);
        iv_code = findViewById(R.id.iv_code);
        tvfindpass = findViewById(R.id.tv_find_pass);

        tvchangecode = findViewById(R.id.tv_change_code);
        tvRegister = findViewById(R.id.tv_register);

        btnLogin.setOnClickListener(this);
        tvfindpass.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvchangecode.setOnClickListener(this);
        //TextView设置下划线
        tvRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvchangecode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        etUsername.setText("miao");
        etPassword.setText("123456");
    }

    private void GetCode() {
        OkHttpUtils.get()
                .url(Config.verification)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String data = jsonObject.getString("data");
                            if (data.equals("null")) {
                                ToastUtil.ShowShort("操作频繁,获取验证码失败,请稍后重试");
                            } else {
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
                if (TextUtils.isEmpty(etUsername.getText().toString())) {
                    etUsername.setError("用户名不能为空");
                } else if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    etPassword.setError("密码不能为空");
                } else if (TextUtils.isEmpty(etCode.getText().toString())) {
                    etCode.setError("验证码不能为空");
                } else {
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    String code = etCode.getText().toString();
                    OkHttpClient client = new OkHttpClient();
                    FormBody body = new FormBody.Builder()
                            .add("username", username)
                            .add("password", password)
                            .add("verification", code)
                            .build();
                    Request request = new Request.Builder()
                            .url(Config.loginUrl)
                            .post(body)
                            .build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //获取sessionid,并写入sp
                            Headers headers = response.headers();
                            List<String> cookies = headers.values("Set-Cookie");
                            String session = cookies.get(0);
                            String sessionid = session.substring(0, session.indexOf(";"));
                            SharedPreferences share = LoginActivity.this.getSharedPreferences("Session", MODE_PRIVATE);
                            SharedPreferences.Editor edit = share.edit();//编辑文件
                            edit.putString("sessionid", sessionid);
                            edit.commit();
                            Message message = new Message();
                            message.what = 2;
                            message.obj = response.body().string();
                            handler.sendMessage(message);
                        }
                    });
                }
                break;
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.tv_change_code:
                GetCode();
                break;
            case R.id.tv_find_pass:
                startActivity(new Intent(LoginActivity.this, FindPassActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this,IndexActivity.class));
        this.finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
