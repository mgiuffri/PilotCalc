package com.marianogiuffrida.pilotcalc.UI;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.fragments.BackHandledFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.CalculatorFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.ConversionsFragment;
import com.marianogiuffrida.pilotcalc.UI.adapters.NavigationDrawerListAdapter;
import com.marianogiuffrida.pilotcalc.UI.fragments.SplashFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.WindFragment;
import com.marianogiuffrida.pilotcalc.UI.navigation.NavigationDrawerItem;

import java.util.ArrayList;

public class MainActivityDrawer extends ActionBarActivity implements BackHandledFragment.BackHandlerInterface{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavigationDrawerItem> navDrawerItems;
    private NavigationDrawerListAdapter navDrawerAdapter;
    private BackHandledFragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_drawer);

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<>();
        int index = 0;
        for (String s : navMenuTitles) {
            navDrawerItems.add(new NavigationDrawerItem(s, navMenuIcons.getResourceId(index++, -1)));
        }

        // Recycle the typed array
        navMenuIcons.recycle();

        // setting the nav drawer list navDrawerAdapter
        navDrawerAdapter = new NavigationDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(navDrawerAdapter);

        if (mDrawerLayout != null) {
            // enabling action bar app icon and behaving it as toggle button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    R.string.open_drawer,
                    R.string.close_drawer
            ) {
                public void onDrawerClosed(View view) {
                    getSupportActionBar().setTitle(mTitle);
                    // calling onPrepareOptionsMenu() to show action bar icons
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(mDrawerTitle);
                    // calling onPrepareOptionsMenu() to hide action bar icons
                    invalidateOptionsMenu();
                }
            };

            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }
        if (savedInstanceState == null) {
            displayView(-1);
            // on first time display view for first nav item
            //displayView(0);
        }

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        if (mDrawerLayout != null) {
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
            menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerLayout != null)
            mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setSelectedFragment(BackHandledFragment backHandledFragment) {
        selectedFragment = backHandledFragment;
    }

    private class SlideMenuClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            if (displayView(position)) updateDrawerSelection(position);

        }
    }

    private boolean displayView(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new ConversionsFragment();
                break;
            case 1:
                fragment = new CalculatorFragment();
                break;
            case 2:
                fragment = new WindFragment();
                break;
            default:
                fragment = new SplashFragment();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack("Fragment")
                    .commit();
            return true;
        }

        // error in creating fragment
        Log.e("MainActivity", "Error in creating fragment");
        return false;
    }

    private void updateDrawerSelection(int position) {
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(navMenuTitles[position]);
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void onBackPressed() {
        if(selectedFragment == null || !selectedFragment.onBackPressed()) {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }
}