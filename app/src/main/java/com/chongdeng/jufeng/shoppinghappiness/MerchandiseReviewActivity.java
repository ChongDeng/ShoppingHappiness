package com.chongdeng.jufeng.shoppinghappiness;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.AppController;
import configs.AppConfig;
import fragments.MerchandiseFragment;
import model.Merchandise;
import model.MerchandiseReview;

public class MerchandiseReviewActivity extends Activity {

    private String TAG = "MerchandiseReview";

    private RecyclerView review_recycler_view;
    private MerchandiseReviewAdapter adapter;
    private List<MerchandiseReview> merchandise_review_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchandise_review);


        review_recycler_view = (RecyclerView) findViewById(R.id.review_recycler_view);
        merchandise_review_list = new ArrayList<>();

        adapter = new MerchandiseReviewAdapter(getApplication(), merchandise_review_list);
        //布局1： 设为网格排列，并可以修改下面函数的第二个参数来设置一行显示几列！！
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplication(), 1);
        review_recycler_view.setLayoutManager(mLayoutManager);


        review_recycler_view.setItemAnimator(new DefaultItemAnimator());
        review_recycler_view.setAdapter(adapter);

        getMerchandiseReview();
    }

    private void getMerchandiseReview() {
        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_MERCHANDISE_Review,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        merchandise_review_list.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);

                                MerchandiseReview review = new MerchandiseReview();

                                review.setAvatar(object.getString("thumbnail_url"));
                                review.setName(object.getString("name"));
                                review.setTime(object.getString("time"));
                                review.setReview(object.getString("review"));

                                merchandise_review_list.add(review);
                            } catch (JSONException e) {
                                Log.e(TAG, "Review Json parsing error: " + e.getMessage());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Review Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }




}
