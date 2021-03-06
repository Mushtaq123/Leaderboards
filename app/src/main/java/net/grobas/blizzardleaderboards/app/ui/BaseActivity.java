package net.grobas.blizzardleaderboards.app.ui;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.grobas.blizzardleaderboards.R;
import net.grobas.blizzardleaderboards.app.util.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public abstract class BaseActivity extends AppCompatActivity {

    @Optional
    @InjectView(R.id.layout_drawer)
    DrawerLayout mDrawerLayout;
    @Optional
    @InjectView(R.id.nav_main_start)
    NavigationView mNavigationView;
    @Optional
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        ButterKnife.inject(this);
        setUpDrawerLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getInstance().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getInstance().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen())
            closeNavDrawer();
        else
            super.onBackPressed();
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null &&
                (mDrawerLayout.isDrawerOpen(GravityCompat.START) || mDrawerLayout.isDrawerOpen(GravityCompat.END));
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
                mDrawerLayout.closeDrawer(GravityCompat.START);
            if (mDrawerLayout.isDrawerOpen(GravityCompat.END))
                mDrawerLayout.closeDrawer(GravityCompat.END);
        }
    }

    protected void openDrawer(int gravity) {
        if (mDrawerLayout != null)
            mDrawerLayout.openDrawer(gravity);
    }

    private void setUpDrawerLayout() {
        //toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.leaderboard_toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
        //drawer
        if (mDrawerLayout != null) {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
            ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    mToolbar, R.string.open, R.string.close) {

                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    invalidateOptionsMenu();
                }
            };
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            createNavDrawerContents(mNavigationView);
        }

        //swipe refresh
        if (mSwipeSwipeRefreshLayout != null) {
            mSwipeSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    updateContents();
                }
            });
            mSwipeSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_refresh_color);
        }
    }

    protected abstract void updateContents();

    protected abstract void createNavDrawerContents(NavigationView mNavigationView);


    protected void setToolbarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void setToolbarTitle(@StringRes int titleId) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(titleId);
        }
    }

    protected void setToolbarSubtitle(String subtitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(subtitle);
        }
    }

    protected void setToolbarSubtitle(@StringRes int subtitleId) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(subtitleId);
        }
    }

}
