package com.hustunique.jianguo.openkeychaindemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroupOverlay;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hustunique.jianguo.openkeychaindemo.ui.MaterialSearchView;
import com.hustunique.jianguo.openkeychaindemo.utils.AnimationUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.content_main)
    CoordinatorLayout mainLayout;
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
    @Bind(R.id.sheets_key_detail)
    CoordinatorLayout sheetLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private BottomSheetBehavior<CoordinatorLayout> bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initDrawer();
        initFab();
        initRecyclerView();
        initSearchView();
    }

    private void initSearchView() {
        materialSearchView.setQrClickListener(new MaterialSearchView.QrClickListener() {
            @Override
            public void onScanClicked() {
                Intent intent = new IntentIntegrator(MainActivity.this).createScanIntent();
                startActivityForResult(intent, IntentIntegrator.REQUEST_CODE);
            }
        });
        materialSearchView.setAnimationDuration(AnimationUtil.ANIMATION_DURATION_SHORT);
        materialSearchView.setSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                mRecyclerView.removeOnScrollListener(null);
                Window window = getWindow();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void onSearchViewStartShow() {
                ViewGroupOverlay overlay = mContainer.getOverlay();
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        recyclerView.smoothScrollBy(0, 0);
                    }
                });
                final View bgdView = new View(MainActivity.this);
                bgdView.setBottom(mContainer.getHeight());
                bgdView.setRight(mContainer.getWidth());
                bgdView.setAlpha(0.3f);
                bgdView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                overlay.add(bgdView);
                Window window = getWindow();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorBgDark));
                }
            }
        });
        //TODO: searchView doesn't work properly in AppBarLayout
//        materialSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        KeyAdapter keyAdapter = new KeyAdapter(this);
        keyAdapter.setOnItemListener(new KeyAdapter.OnMyItemClickListener() {
            @Override
            public void onClick() {
                //TODO: Call onLayoutChild when setState in onCreate, see https://code.google.com/p/android/issues/detail?id=202174
                bottomSheetBehavior.onLayoutChild(mainLayout, sheetLayout, ViewCompat.LAYOUT_DIRECTION_LTR);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        mRecyclerView.setAdapter(keyAdapter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            String scannedContent = scanResult.getContents();
            Uri uri = Uri.parse(scannedContent);
            Log.d("searchView", "scanned:" + uri);
        }
    }

    @Override
    public void onBackPressed() {
        if (materialSearchView.isSearchOpen()) {
            materialSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
