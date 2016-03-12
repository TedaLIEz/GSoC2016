package com.hustunique.jianguo.openkeychaindemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.navigation)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.fab_main)
    FloatingActionsMenu mFam;
    @Bind(R.id.key_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.search_view)
    MaterialSearchView materialSearchView;
    @Bind(R.id.sheets_main)
    CoordinatorLayout coordinatorLayout;

    private ActionBarDrawerToggle mDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initDrawer();
        initFab();
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(coordinatorLayout);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        KeyAdapter keyAdapter = new KeyAdapter(this);
        keyAdapter.setOnItemListener(new KeyAdapter.OnMyItemClickListener() {
            @Override
            public void onClick() {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
//                bottomSheetDialog.setContentView(R.layout.dialog_key);
//                bottomSheetDialog.show();
            }
        });
        mRecyclerView.setAdapter(keyAdapter);

        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                mFam.setVisibility(View.GONE);
            }

            @Override
            public void onSearchViewClosed() {
                if (mFam.getVisibility() == View.GONE) {
                    mFam.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void initDrawer() {
        setSupportActionBar(toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    private void initFab() {
        FloatingActionButton fileSearch = (FloatingActionButton) mFam.findViewById(R.id.fab_add_file);
        FloatingActionButton cloudSearch = (FloatingActionButton) mFam.findViewById(R.id.fab_add_cloud);
        fileSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFam.collapse();
            }
        });
        cloudSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFam.collapse();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_key_list_search);
        materialSearchView.setMenuItem(menuItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) return true;

        int id = item.getGroupId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


}
