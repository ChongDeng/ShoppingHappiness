package fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chongdeng.jufeng.shoppinghappiness.FavouritesMerchandiseAdapter;
import com.chongdeng.jufeng.shoppinghappiness.HomeActivity;
import com.chongdeng.jufeng.shoppinghappiness.HomeAdapter;
import com.chongdeng.jufeng.shoppinghappiness.MerchandiseAdapter;
import com.chongdeng.jufeng.shoppinghappiness.MerchandiseInfoActivity;
import com.chongdeng.jufeng.shoppinghappiness.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import app.AppController;
import butterknife.BindView;
import butterknife.ButterKnife;
import configs.AppConfig;
import helper.MerchandiseLab;
import model.HomePageBean;
import model.Merchandise;

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String TAG = "HomeFragment";

    @BindView(R.id.home_SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.home_recyclerView)
    RecyclerView recyclerView;

    //private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private List<HomePageBean> home_page_bean_list;

    private Handler handler = new Handler();


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
   // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);



     //   recyclerView = (RecyclerView) view.findViewById(R.id.home_recyclerView);

        home_page_bean_list = new ArrayList<>();
        adapter = new HomeAdapter(getActivity(), home_page_bean_list);
        //布局1： 设为网格排列，并可以修改下面函数的第二个参数来设置一行显示几列！！
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int span = 2;
                int type = adapter.getItemViewType(position);
                switch (type){
                    case HomeAdapter.BANNER_VIEW_TYPE:
                        span = 2;
                        break;
                    case HomeAdapter.MERCHANDISE_VIEW_TYPE:
                        span = 1;
                        break;
                }
                return span;
            }
        });
        recyclerView.setLayoutManager(mLayoutManager);

        //recyclerView.addItemDecoration(new HomeFragment.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.color_home);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                gethome_page_bean_list();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gethome_page_bean_list();
                    }
                }, 2000);
            }
        });

        //gethome_page_bean_list();

        recyclerView.addOnItemTouchListener(new HomeAdapter.RecyclerTouchListener(getActivity(), recyclerView, new HomeAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HomePageBean home_page_bean = home_page_bean_list.get(position);
                Toast.makeText(getActivity(), home_page_bean.getName() + " is selected!", Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(getActivity(), MerchandiseInfoActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(MerchandiseInfoActivity.MerchandiseInfoTag, merchandise);
//                intent.putExtras(bundle);
//                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        gethome_page_bean_list();
    }

    private void gethome_page_bean_list() {

        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_HOME_PAGE_DATA,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        home_page_bean_list.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);

                                HomePageBean home_page_bean = new HomePageBean();
                                int view_type = object.getInt("view_type");
                                home_page_bean.setView_type(view_type);

                                switch(view_type){

                                    case HomeAdapter.MERCHANDISE_VIEW_TYPE:
                                        home_page_bean.setName(object.getString("name"));
                                        home_page_bean.setThumbnail(object.getString("thumbnail_url"));
                                        home_page_bean.setNumOfSongs(object.getInt("numOfSongs"));
                                        home_page_bean.setId(object.getInt("id"));
                                        break;

                                    case HomeAdapter.BANNER_VIEW_TYPE:
                                        JSONArray banner_urls = object.getJSONArray("banner_urls");
                                        int banner_cnt = banner_urls.length();
                                        ArrayList<String> urls = new ArrayList<>();
                                        for(int index = 0; index < banner_cnt; ++index){
                                            urls.add(banner_urls.get(index).toString());
                                           // Toast.makeText(getContext(), "url: " + banner_urls.get(index).toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        home_page_bean.setBanner_urls(urls);

                                        //Toast.makeText(getContext(), "size: " + banner_urls.length(), Toast.LENGTH_SHORT).show();
                                        break;

                                    default:
                                        Log.e(TAG, "wrong view type from json data");
                                        break;
                                }

                                home_page_bean_list.add(home_page_bean);
                            } catch (JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
                        }

                        adapter.notifyDataSetChanged();

                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req);
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

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
