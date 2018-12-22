package cn.linghouse.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.App.Config;
import cn.linghouse.App.MyApplication;
import cn.linghouse.Util.CodeUtils;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;
import okhttp3.Call;

public class FindPassActivity extends AppCompatActivity {

    @BindView(R.id.et_findpass_username)
    EditText etFindpassUsername;
    @BindView(R.id.et_findpass_code)
    EditText etFindpassCode;
    @BindView(R.id.iv_findpass_code)
    ImageView ivFindpassCode;
    @BindView(R.id.tv_findpass_change_code)
    TextView tvFindpassChangeCode;
    @BindView(R.id.btn_findpass_complete)
    Button btnFindpassComplete;
    private CodeUtils codeUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass);
        ImmersionBar.with(this).init();
        ButterKnife.bind(this);
        getCode();
    }

    @OnClick({R.id.tv_findpass_change_code, R.id.btn_findpass_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_findpass_change_code:
                getCode();
                break;
            case R.id.btn_findpass_complete:
                findpass();
                break;
        }
    }

    //获取验证码
    private void getCode() {
        OkHttpUtils.get()
                .url(Config.verification)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    if (data.equals("null")) {
                        ToastUtil.ShowLong(message+"验证码获取失败,请稍后重试");
                    } else {
                        codeUtils = CodeUtils.getInstance();
                        Bitmap bitmap = codeUtils.createBitmap(data);
                        ivFindpassCode.setImageBitmap(bitmap);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //找回密码,参数发送到后端
    private void findpass() {
        OkHttpUtils.post()
                .url(Config.findPass)
                .addParams("username", etFindpassUsername.getText().toString())
                .addParams("verification", etFindpassCode.getText().toString())
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
                    if (code!=200){
                        ToastUtil.ShowShort(message);
                    }else{
                        ToastUtil.ShowShort(data);
                        startActivity(new Intent(FindPassActivity.this,LoginActivity.class));
                        FindPassActivity.this.finish();
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
