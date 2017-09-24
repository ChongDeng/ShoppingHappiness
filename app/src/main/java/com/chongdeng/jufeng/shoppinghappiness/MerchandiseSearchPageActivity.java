package com.chongdeng.jufeng.shoppinghappiness;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import model.FlowLayoutBean;

/**
 * Created by chong on 9/23/2017.
 */

public class MerchandiseSearchPageActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        View.OnClickListener{

    SearchView search_view;

    private ListView search_history_listview;
    private Button delete_search_history;
    private RelativeLayout search_history_relavitelayout;

    private String[] HotWords = new String[]
            {"华南理工", "C13 401", "湖北", "iphone 8", "htc", "galaxy s8",
                    "ZTE axon 7"};
    private List<FlowLayoutBean> hot_words_list = new ArrayList<>();
    FlowLayoutAdapter HotWordsFlowLayoutAdapter;

    private String[] SearchHistory = new String[]
            {"Andrew Ng", "Jeff Dean", "Demis Hassabis"};
    private List<FlowLayoutBean> serach_history_list = new ArrayList<>();
    FlowLayoutAdapter SearchHistoryFlowLayoutAdapter;

    private TagFlowLayout SearchPageFlowlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        setTitle("Search");

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        InitFlowLayoutView();
    }

    //初始化数据
    private void InitFlowLayoutView() {
        SearchPageFlowlayout = (TagFlowLayout) findViewById(R.id.search_page_flowlayout);
        search_history_listview = (ListView) findViewById(R.id.search_history_listview);

        delete_search_history = (Button) findViewById(R.id.delete_search_history);
        delete_search_history.setOnClickListener(this);

        search_history_relavitelayout= (RelativeLayout) findViewById(R.id.search_history_rl);


        final LayoutInflater mInflater = LayoutInflater.from(MerchandiseSearchPageActivity.this);
        SearchPageFlowlayout.setAdapter(new TagAdapter<String>(HotWords)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.search_page_flowlayout_tv,
                        SearchPageFlowlayout, false);
                tv.setText(s);
                return tv;
            }
        });

        SearchPageFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                Toast.makeText(MerchandiseSearchPageActivity.this, HotWords[position], Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MerchandiseSearchPageActivity.this, MerchandiseSearchResultActivity.class);
                intent.putExtra("key word", HotWords[position]);
                startActivity(intent);
                finish();

                return true;
            }
        });
        SearchPageFlowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
        {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {

            }
        });

        getHotWords();
        HotWordsFlowLayoutAdapter = new FlowLayoutAdapter(MerchandiseSearchPageActivity.this, hot_words_list);

        getSeachHistory();
        SearchHistoryFlowLayoutAdapter = new FlowLayoutAdapter(MerchandiseSearchPageActivity.this, serach_history_list);
        search_history_listview.setAdapter(SearchHistoryFlowLayoutAdapter);
    }

    //提供数据源
    private void getHotWords(){
        FlowLayoutBean tv = null;
        for (int i = 0; i < HotWords.length; i++) {
            tv = new FlowLayoutBean();
            tv.setTv(HotWords[i]);
            hot_words_list.add(tv);
        }
    }

    private void getSeachHistory(){
        FlowLayoutBean tv = null;
        for (int i = 0; i < 3; ++i) {
            tv = new FlowLayoutBean();
            tv.setTv(SearchHistory[i]);
            serach_history_list.add(tv);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_page_menu, menu);
        setSearchView(menu);
        return true;
    }

    private void setSearchView(Menu menu) {
        MenuItem item = menu.getItem(0);
        search_view = new SearchView(this);
        //设置展开后图标的样式,false时ICON在搜索框外,true为在搜索框内，无法修改
        search_view.setIconifiedByDefault(false);
        search_view.setIconified(false);
        search_view.setQueryHint("搜索商品名称、种类、商家名");
        search_view.setSubmitButtonEnabled(true);//设置最右侧的提交按钮
        item.setActionView(search_view);

        //为该SearchView组件设置事件监听器
        search_view.setOnQueryTextListener(this);
    }

    class FlowLayoutAdapter extends BaseAdapter {
        private Context context;
        private List<FlowLayoutBean> list;

        public FlowLayoutAdapter(Context context, List<FlowLayoutBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            FlowLayoutBean tv = (FlowLayoutBean) getItem(i);
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.search_page_flowlayout_tv, null);
                viewHolder = new ViewHolder();
                viewHolder.flowlayout_tv = (TextView) view.findViewById(R.id.flowlayout_tv);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.flowlayout_tv.setText(tv.getTv());

            return view;
        }

        //创建ViewHolder类
        class ViewHolder {
            TextView flowlayout_tv;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.searchView:
                Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, "您选择的是："+query, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MerchandiseSearchPageActivity.this, MerchandiseSearchResultActivity.class);
        intent.putExtra("keywords", query);
        startActivity(intent);
        finish();

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if(TextUtils.isEmpty(newText))
        {
            //清除ListView的过滤
        }
        else
        {
            //使用用户输入的内容对ListView的列表项进行过滤
            // Toast.makeText(getApplication(), newText, Toast.LENGTH_SHORT).show();

        }
        return true;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete_search_history:
                search_history_relavitelayout.setVisibility(View.VISIBLE);
                search_history_listview.setVisibility(View.GONE);
                delete_search_history.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
