package com.chongdeng.jufeng.shoppinghappiness;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.Arrays;
import java.util.List;

import helper.GlideImageLoader;

public class MerchandiseInfoActivity extends AppCompatActivity implements OnBannerListener {

    public String MerchandiseInfoTag = "Merchandise";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchandise_info);

        View view = getWindow().getDecorView().findViewById(R.id.merchandise_info);
        initCollapsingToolbar(view);

        List<String> images = Arrays.asList("http://10.4.1.72/ShoppingBackend/banner/b1.jpg",
                "http://10.4.1.72/ShoppingBackend/banner/b2.jpg",
                "http://10.4.1.72/ShoppingBackend/banner/b3.jpg"
        );
        List<String> titles = Arrays.asList("武汉科技大学", "华南理工大学", "圣何塞州立大学");

        Banner banner = (Banner) findViewById(R.id.banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        banner.setOnBannerListener(this);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);

        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getApplicationContext(),"你点击了："+position,Toast.LENGTH_SHORT).show();
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar(View view) {

        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.merchandise_info_appbar);
        appBarLayout.setExpanded(true);

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.merchandise_info_collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        //collapsingToolbar.setTitle("hello world!");

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Merchandise details");
                    collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.colortext));
                    collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.indianred));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    //collapsingToolbar.setTitle("test");
                    isShow = false;
                }
            }
        });
    }
}
