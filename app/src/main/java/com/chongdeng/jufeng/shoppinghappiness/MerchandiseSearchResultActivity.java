package com.chongdeng.jufeng.shoppinghappiness;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import fragments.FavoritesFragment;
import model.Merchandise;

/**
 * Created by chong on 9/23/2017.
 */

public class MerchandiseSearchResultActivity extends AppCompatActivity {

    String keyword = null;

    private String TAG = "MerchandiseSearchResultActivity";

    private RecyclerView recyclerView;
    private MerchandiseSearchResultAdapter adapter;
    private List<Merchandise> merchandiseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        keyword = getIntent().getStringExtra("key word");
        //Toast.makeText(getApplication(), keyword, Toast.LENGTH_SHORT).show();


        recyclerView = (RecyclerView) findViewById(R.id.favourites_list_recycler_view);

        merchandiseList = new ArrayList<>();
        adapter = new MerchandiseSearchResultAdapter(this, merchandiseList);

        // 布局2： 设为纵向排列
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplication());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new MerchandiseSearchResultActivity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getMerchandiseList();

        recyclerView.addOnItemTouchListener(new MerchandiseSearchResultAdapter.RecyclerTouchListener(getApplication(), recyclerView, new MerchandiseSearchResultAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Merchandise merchandise = merchandiseList.get(position);

                Intent intent = new Intent(getApplicationContext(), MerchandiseInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MerchandiseInfoActivity.MerchandiseInfoTag, merchandise);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void getMerchandiseList() {
        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_MERCHANDISE_LIST,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        merchandiseList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);

                                Merchandise merchandise = new Merchandise();

                                merchandise.setName(object.getString("name"));
                                merchandise.setThumbnail(object.getString("thumbnail_url"));
                                merchandise.setNumOfSongs(object.getInt("numOfSongs"));
                                merchandise.setId(object.getInt("id"));

                                merchandiseList.add(merchandise);
                            } catch (JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }


    private void showResult(){

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
