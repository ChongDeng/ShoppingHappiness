package com.chongdeng.jufeng.shoppinghappiness;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import fragments.BottomNavFragment;
import fragments.FavoritesFragment;
import fragments.MerchandiseFragment;
import helper.BottomNavigationViewHelper;


/**
 * Created by chong on 8/31/2017.
 */

public class HomeActivity extends AppCompatActivity {
    private static final String SELECTED_ITEM = "arg_selected_item";

    private BottomNavigationView mBottomNav;
    private int mSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);

        //disable shift mode
        BottomNavigationViewHelper.disableShiftMode(mBottomNav);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(0);
        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }

    private void selectFragment(MenuItem item) {
        Fragment frag = null;
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.action_home:
                  frag = BottomNavFragment.newInstance(getString(R.string.text_home),
                        getColorFromRes(R.color.color_home));
                  break;
            case R.id.action_merchandise:
//                frag = BottomNavFragment.newInstance(getString(R.string.text_merchandise),
//                        getColorFromRes(R.color.color_home));
                  frag = MerchandiseFragment.newInstance(getString(R.string.text_merchandise),
                           "");
                    break;
            case R.id.action_favorites:
                frag = FavoritesFragment.newInstance("Fovorites Fragments",
                        "");
//                frag = BottomNavFragment.newInstance("favorites",
//                        getColorFromRes(R.color.color_home));
                break;
            case R.id.action_me:
                frag = BottomNavFragment.newInstance("About Me",
                        getColorFromRes(R.color.color_home));
                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, frag, frag.getTag());
            ft.commit();
        }
        else{
            System.out.println("hello");
        }
    }

    private int getColorFromRes(@ColorRes int resId) {
        return ContextCompat.getColor(this, resId);
    }
}
