package com.chongdeng.jufeng.shoppinghappiness;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import model.MerchandiseReview;

/**
 * Author: Chong on 14/09/17.
 */
public class MerchandiseReviewAdapter extends RecyclerView.Adapter<MerchandiseReviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<MerchandiseReview> merchandise_review_list;

    public class MyViewHolder extends RecyclerView.ViewHolder {       
        public de.hdodenhof.circleimageview.CircleImageView thumbnail;
        public TextView name, time, review_content;

        public ImageView review_star1, review_star2, review_star3,
                review_star4, review_star5;

        public MyViewHolder(View view) {
            super(view);            
            
            thumbnail = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.avatar);
            name = (TextView) view.findViewById(R.id.name);
            time = (TextView) view.findViewById(R.id.review_time);
            review_content = (TextView) view.findViewById(R.id.review_content);

            review_star1 = (ImageView) view.findViewById(R.id.review_star1);
            review_star2 = (ImageView) view.findViewById(R.id.review_star2);
            review_star3 = (ImageView) view.findViewById(R.id.review_star3);
            review_star4 = (ImageView) view.findViewById(R.id.review_star4);
            review_star5 = (ImageView) view.findViewById(R.id.review_star5);
        }
    }


    public MerchandiseReviewAdapter(Context mContext, List<MerchandiseReview> merchandise_review_list) {
        this.mContext = mContext;
        this.merchandise_review_list = merchandise_review_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.merchandise_review_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MerchandiseReview review = merchandise_review_list.get(position);
        holder.name.setText(review.getName());
        holder.time.setText(review.getTime());
        holder.review_content.setText(review.getReview());

        // loading thumbnail cover using Glide library
        Glide.with(mContext).load(review.getAvatar()).into(holder.thumbnail);

        switch(review.getRateStar()){
            case 0:
                holder.review_star1.setVisibility(View.GONE);
                holder.review_star2.setVisibility(View.GONE);
                holder.review_star3.setVisibility(View.GONE);
                holder.review_star4.setVisibility(View.GONE);
                holder.review_star5.setVisibility(View.GONE);
                break;

            case 1:
                holder.review_star1.setVisibility(View.GONE);
                holder.review_star2.setVisibility(View.GONE);
                holder.review_star3.setVisibility(View.GONE);
                holder.review_star4.setVisibility(View.GONE);
                break;

            case 2:
                holder.review_star1.setVisibility(View.GONE);
                holder.review_star2.setVisibility(View.GONE);
                holder.review_star3.setVisibility(View.GONE);
                break;

            case 3:
                holder.review_star1.setVisibility(View.GONE);
                holder.review_star2.setVisibility(View.GONE);
                break;

            case 4:
                holder.review_star1.setVisibility(View.GONE);
                break;

            case 5:
                break;

            default:
                Log.e("rate", "wrong rates");
                break;
        }
    }




    @Override
    public int getItemCount() {
        return merchandise_review_list.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MerchandiseReviewAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MerchandiseReviewAdapter.ClickListener clickListener) {
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

}
