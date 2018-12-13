package cn.linghouse.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.App.ActivityController;
import cn.linghouse.Util.ToastUtil;
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
    @BindView(R.id.tv_goods_details_sortname)
    TextView tvGoodsDetailsSortname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ActivityController.addActivity(this);
        ImmersionBar.with(GoodDetailsActivity.this).init();
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra("title");
        String price = getIntent().getStringExtra("price");
        String detail = getIntent().getStringExtra("details");
        String sortname = getIntent().getStringExtra("sortname");
        String [] image = getIntent().getStringArrayExtra("imagelist");
        for (int i =0;i<image.length;i++){
            ToastUtil.ShowLong(""+image[i]);
        }
        tvGoodsDetailsTitle.setText(title);
        tvGoodsDetailsPrice.setText(price);
        tvGoodsDetails.setText(detail);
        tvGoodsDetailsSortname.setText(sortname);
    }

    @OnClick({R.id.iv_details_collection, R.id.iv_details_chat, R.id.iv_goods_details_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //收藏图标
            case R.id.iv_details_collection:
                break;
            //咨询图标
            case R.id.iv_details_chat:
                break;
            //返回图标
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
        ActivityController.removeActivity(this);
    }
}
