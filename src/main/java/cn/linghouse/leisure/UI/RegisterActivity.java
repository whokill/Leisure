package cn.linghouse.leisure.UI;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.linghouse.leisure.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etUsername;
    private EditText etPassword;
    private EditText etrepass;
    private TextView tvbacklogin;
    private Button next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etrepass = findViewById(R.id.et_repassword);
        tvbacklogin = findViewById(R.id.tv_back_login);
        next = findViewById(R.id.btn_netxt);

        next.setOnClickListener(this);
        tvbacklogin.setOnClickListener(this);
        //设置下划线
        tvbacklogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back_login:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            case R.id.btn_netxt:
                if (etUsername.getText().toString().isEmpty()){
                    etUsername.setError("用户名不能为空哦！");
                }else if (etPassword.getText().toString().isEmpty()){
                etPassword.setError("密码不能为空哦！");
                }else if (etrepass.getText().toString().isEmpty()){
                 etrepass.setError("请确认你的密码！");
                }else if (etPassword.getText().toString().equals(etrepass.getText().toString())){
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra("username",username);
                    intent.putExtra("password",password);
                    intent.setClass(RegisterActivity.this,FinalRegisActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }else{
                    etrepass.setError("两次密码不一致,请检查！");
                }
                break;
        }
    }
}
