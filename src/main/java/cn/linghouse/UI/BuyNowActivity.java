package cn.linghouse.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.leisure.R;

public class BuyNowActivity extends AppCompatActivity {

    @BindView(R.id.tv_buy_now_pic)
    ImageView tvBuyNowPic;
    @BindView(R.id.tv_buy_now_title)
    TextView tvBuyNowTitle;
    @BindView(R.id.tv_buy_now_price)
    TextView tvBuyNowPrice;
    //收件人
    @BindView(R.id.tv_buy_now_recipient)
    TextView tvBuyNowRecipient;
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
    private String picurl,title,price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);
        ButterKnife.bind(this);
        ImmersionBar.with(this).init();
        picurl = getIntent().getStringExtra("picurl");
        title = getIntent().getStringExtra("title");
        price = getIntent().getStringExtra("price");

        tvBuyNowTitle.setText(title);
        tvBuyNowPrice.setText(price);
    }

    @OnClick({R.id.iv_buy_now_back, R.id.btn_buy_now_shopping})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_buy_now_back:
                BuyNowActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            case R.id.btn_buy_now_shopping:
                //发起创建交易的网络请求

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
