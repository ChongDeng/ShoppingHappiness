package com.chongdeng.jufeng.shoppinghappiness;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import configs.AppConfig;
import helper.GlideImageLoader;
import model.HomePageBean;
import model.Merchandise;

/**
 * Created by chong on 10/1/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> implements OnBannerListener {

    public static final int BANNER_VIEW_TYPE = 1;
    public static final int COMMONFUNC_VIEW_TYPE = 2;
    public static final int NEWS_VIEW_TYPE = 3;
    public static final int QUERYMORE_VIEW_TYPE = 4;
    public static final int MERCHANDISE_VIEW_TYPE = 5;


    private Context mContext;
    private List<HomePageBean> home_page_bean_list;


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private Map<Integer, View> mCacheView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mCacheView = new HashMap<>();
        }

        public View getView(int resId) {
            View view;
            if (mCacheView.containsKey(resId)) {
                view = mCacheView.get(resId);
            } else {
                view = itemView.findViewById(resId);
                mCacheView.put(resId, view);
            }
            return view;
        }
    }


    public HomeAdapter(FragmentActivity mContext, List<HomePageBean> home_page_bean_list) {
        this.mContext = mContext;
        this.home_page_bean_list = home_page_bean_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        switch (viewType) {
            case BANNER_VIEW_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.home_banner, parent, false);
                break;

            case COMMONFUNC_VIEW_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.home_common_func, parent, false);
                break;

            case HomeAdapter.QUERYMORE_VIEW_TYPE:

                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.home_query_more, parent, false);
                break;

            case MERCHANDISE_VIEW_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.merchandise_cardview_item, parent, false);
                break;
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        HomePageBean home_page_bean = home_page_bean_list.get(position);

        switch (getItemViewType(position)) {
            case BANNER_VIEW_TYPE:

                List<String> images = new ArrayList<String>();
                for(HomePageBean.BannerBean banner_bean : home_page_bean.getBannerBeans()){
                    images.add(banner_bean.getUrl());
                }

                Banner banner = (Banner)  holder.getView(R.id.home_banner);

                //List<String> titles = Arrays.asList("Silicon Valley", "华南理工大学", "圣何塞州立大学");
                List<String> titles = Arrays.asList(new String[images.size()]);

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
                break;

            case COMMONFUNC_VIEW_TYPE:

                ImageView pic1 = (ImageView) holder.getView(R.id.home_common_func_pic1);
                ImageView pic2 = (ImageView) holder.getView(R.id.home_common_func_pic2);
                ImageView pic3 = (ImageView) holder.getView(R.id.home_common_func_pic3);
                ImageView pic4 = (ImageView) holder.getView(R.id.home_common_func_pic4);

                TextView pic1_description = (TextView) holder.getView(R.id.pic1_description);
                TextView pic2_description = (TextView) holder.getView(R.id.pic2_description);
                TextView pic3_description = (TextView) holder.getView(R.id.pic3_description);
                TextView pic4_description = (TextView) holder.getView(R.id.pic4_description);

                ArrayList<HomePageBean.CommonFunctionBean> common_func_beans =  home_page_bean.getCommonFuncBeans();

                Glide.with(mContext).load(common_func_beans.get(0).getPicUrl()).into(pic1);
                Glide.with(mContext).load(common_func_beans.get(1).getPicUrl()).into(pic2);
                Glide.with(mContext).load(common_func_beans.get(2).getPicUrl()).into(pic3);
                Glide.with(mContext).load(common_func_beans.get(3).getPicUrl()).into(pic4);

                pic1_description.setText(common_func_beans.get(0).getPicDescription());
                pic2_description.setText(common_func_beans.get(1).getPicDescription());
                pic3_description.setText(common_func_beans.get(2).getPicDescription());
                pic4_description.setText(common_func_beans.get(3).getPicDescription());

                break;

            case QUERYMORE_VIEW_TYPE:

                break;

            case MERCHANDISE_VIEW_TYPE:
                TextView title = (TextView) holder.getView(R.id.title);
                TextView count = (TextView) holder.getView(R.id.count);
                ImageView thumbnail = (ImageView) holder.getView(R.id.thumbnail);

                title.setText(home_page_bean.getName());
                count.setText(home_page_bean.getNumOfSongs() + " views");

                // loading album cover using Glide library
                Glide.with(mContext).load(home_page_bean.getThumbnail()).into(thumbnail);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return home_page_bean_list.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private HomeAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final HomeAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    @Override
    public int getItemViewType(int position) {
       return  home_page_bean_list.get(position).getView_type();
    }

    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(mContext, "你点击了："+position,Toast.LENGTH_SHORT).show();
    }

}
