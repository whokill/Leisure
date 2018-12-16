package cn.linghouse.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.linghouse.App.ActivityController;
import cn.linghouse.Util.ToastUtil;
import cn.linghouse.leisure.R;

public class GoodDetailsActivity extends AppCompatActivity {
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
    //商品轮播图
    @BindView(R.id.con_goods_banner)
    ConvenientBanner conGoodsBanner;
    //接收的images数组，作为轮播图的数据源
    private String[] image;
    //接收到的分类
    private String sortname;
    //接收到的详细描述
    private String detail;
    //接收到的商品价格
    private String price;
    //接收到的商品名称
    private String title;
    private List<String> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ActivityController.addActivity(this);
        ImmersionBar.with(GoodDetailsActivity.this).init();
        ButterKnife.bind(this);
        title = getIntent().getStringExtra("title");
        price = getIntent().getStringExtra("price");
        detail = getIntent().getStringExtra("details");
        sortname = getIntent().getStringExtra("sortname");
        image = getIntent().getStringArrayExtra("imagelist");
        tvGoodsDetailsTitle.setText(title);
        tvGoodsDetailsPrice.setText(price);
        tvGoodsDetails.setText(detail);
        tvGoodsDetailsSortname.setText(sortname);
        list = Arrays.asList(image);
        conGoodsBanner.setPointViewVisible(true);
        conGoodsBanner.setPageIndicator(new int[]{R.mipmap.unselect, R.mipmap.select});
        conGoodsBanner.setMotionEventSplittingEnabled(true);
        conGoodsBanner.startTurning(1500);
        conGoodsBanner.setPages(new CBViewHolderCreator<NetworkImageLoader>() {
            @Override
            public NetworkImageLoader createHolder() {
                return new NetworkImageLoader();
            }
        }, list);
    }

    @OnClick({R.id.iv_details_collection, R.id.iv_details_chat, R.id.iv_goods_details_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //收藏图标
            case R.id.iv_details_collection:

                break;
            //咨询图标
            case R.id.iv_details_chat:
                ToastUtil.ShowLong("咨询");
                break;
            //返回图标
            case R.id.iv_goods_details_back:
                GoodDetailsActivity.this.finish();
                break;
            default:
                break;
        }
    }

    public class NetworkImageLoader implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(context).load(data).into(imageView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(GoodDetailsActivity.this).destroy();
        ActivityController.removeActivity(this);
    }
}
