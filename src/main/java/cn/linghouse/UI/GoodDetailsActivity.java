package cn.linghouse.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.App.ActivityController;
import cn.linghouse.leisure.R;

public class GoodDetailsActivity extends AppCompatActivity {
    //商品图片
    @BindView(R.id.tv_goods_pic)
    ImageView tvGoodsPic;
    //商品价格
    @BindView(R.id.tv_goods_details_price)
    TextView tvGoodsDetailsPrice;
    //商品标题
    @BindView(R.id.tv_goods_details_title)
    TextView tvGoodsDetailsTitle;
    //商品具体描述
    @BindView(R.id.tv_goods_details)
    TextView tvGoodsDetails;
    //标题栏的返回图标
    @BindView(R.id.iv_goods_details_back)
    ImageView ivGoodsDetailsBack;
    @BindView(R.id.iv_details_collection)
    ImageView ivDetailsCollection;
    @BindView(R.id.iv_details_chat)
    ImageView ivDetailsChat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ActivityController.addActivity(this);
        ImmersionBar.with(GoodDetailsActivity.this).init();
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra("title");
        String price = getIntent().getStringExtra("price");
        String detail = getIntent().getStringExtra("detail");
        tvGoodsDetailsTitle.setText(title);
        tvGoodsDetailsPrice.setText(price);
        tvGoodsDetails.setText(detail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(GoodDetailsActivity.this).destroy();
        ActivityController.removeActivity(this);
    }

    @OnClick({R.id.iv_details_collection, R.id.iv_details_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //收藏图标
            case R.id.iv_details_collection:
                break;
            //咨询图标
            case R.id.iv_details_chat:
                break;
        }
    }
}
