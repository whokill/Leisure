package cn.linghouse.leisure.UI;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import cn.linghouse.leisure.App.ActivityController;
import cn.linghouse.leisure.R;
import cn.linghouse.leisure.Util.ToastUtil;
import okhttp3.Call;

public class FinalRegisActivity extends AppCompatActivity implements View.OnClickListener{
    private String register_url = "http://139.199.2.193:8080/leisure/user/create";
    private String username,password;
    private EditText email;
    private TextView tvbacklogin;
    private Button register;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_final);
        ActivityController.addActivity(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        initview();
    }

    private void initview() {
        email = findViewById(R.id.et_mail);
        register = findViewById(R.id.btn_register);
        tvbacklogin = findViewById(R.id.tv_back_login);

        register.setOnClickListener(this);
        tvbacklogin.setOnClickListener(this);

        //为TextView设置下划线
        tvbacklogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                int mail = email.getInputType();
                if (mail!=InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS){
                    email.setError("邮箱格式错误");
                }else{
                    OkHttpUtils.post().url(register_url)
                            .addParams("username",username)
                            .addParams("password",password)
                            .addParams("email",email.getText().toString())
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
                                        ToastUtil.ShowShort(msg);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
                break;
            case R.id.tv_back_login:
                startActivity(new Intent(FinalRegisActivity.this,LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                FinalRegisActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
