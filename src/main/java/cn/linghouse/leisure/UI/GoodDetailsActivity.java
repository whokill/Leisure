package cn.linghouse.leisure.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.leisure.R;
import cn.linghouse.leisure.Util.ToastUtil;

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
    //加入购物车
    @BindView(R.id.btn_add_shoppingcar)
    Button btnAddShoppingcar;
    //立即购买
    @BindView(R.id.btn_shopping)
    Button btnShopping;
    //标题栏的返回图标
    @BindView(R.id.iv_goods_details_back)
    ImageView ivGoodsDetailsBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ImmersionBar.with(GoodDetailsActivity.this).init();
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra("title");
        String price = getIntent().getStringExtra("price");
        tvGoodsDetailsTitle.setText(title);
        tvGoodsDetailsPrice.setText(price);
    }
    @OnClick({R.id.btn_add_shoppingcar, R.id.btn_shopping,R.id.iv_goods_details_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_shoppingcar:
                ToastUtil.ShowLong("加入购物车");
                break;
            case R.id.btn_shopping:
                ToastUtil.ShowLong("立即购买");
                break;
            case R.id.iv_goods_details_back:
                GoodDetailsActivity.this.finish();
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(GoodDetailsActivity.this).destroy();
    }
}
