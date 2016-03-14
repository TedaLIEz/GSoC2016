package com.hustunique.jianguo.openkeychaindemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.hustunique.jianguo.openkeychaindemo.ui.KeyDetailFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.main_content)
    BottomSheetLayout bottomSheetLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.navigation)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.key_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.search_view)
    MaterialSearchView materialSearchView;
    @Bind(R.id.main_container)
    FrameLayout mContainer;

    private ActionBarDrawerToggle mDrawerToggle;
    //TODO: Some bug in bottomsheet support library currently
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initDrawer();
        initFab();
        initBottomSheet();
        initRecyclerView();
        initSearchView();
    }

    private void initSearchView() {
        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {

            @Override
            public void onSearchViewShown() {
                mContainer.setVisibility(View.GONE);
//                mFab.setVisibility(View.GONE);
            }

            @Override
            public void onSearchViewClosed() {
                if (mContainer.getVisibility() == View.GONE) {
                    mContainer.setVisibility(View.VISIBLE);
                }
                if (mFab.getVisibility() == View.GONE) {
                    mFab.setVisibility(View.VISIBLE);
                }
            }
        });
        //TODO: searchView doesn't work properly in AppBarLayout
        materialSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        KeyAdapter keyAdapter = new KeyAdapter(this);
        keyAdapter.setOnItemListener(new KeyAdapter.OnMyItemClickListener() {
            @Override
            public void onClick() {
                new KeyDetailFragment().show(getSupportFragmentManager(), R.id.main_content);
            }
        });
        mRecyclerView.setAdapter(keyAdapter);
    }

    private void initBottomSheet() {
        bottomSheetLayout.setPeekOnDismiss(true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        bottomSheetLayout.setPeekSheetTranslation(displayMetrics.heightPixels / 16 * 11);
    }

    private void initDrawer() {

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
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Import From file", Toast.LENGTH_SHORT).show();
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
