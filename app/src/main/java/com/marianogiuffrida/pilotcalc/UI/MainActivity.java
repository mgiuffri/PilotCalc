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
import com.marianogiuffrida.pilotcalc.UI.fragments.Altimetry.AltimetryFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.Atmosphere.StandardAtmosphereFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.CalculatorFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.ConversionsFragment;
import com.marianogiuffrida.pilotcalc.UI.adapters.NavigationDrawerListAdapter;
import com.marianogiuffrida.pilotcalc.UI.fragments.Speed.SpeedFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.SplashFragment;
import com.marianogiuffrida.pilotcalc.UI.fragments.WindTriangle.WindFragment;
import com.marianogiuffrida.pilotcalc.UI.navigation.NavigationDrawerItem;
import com.marianogiuffrida.pilotcalc.UI.notification.OnTitleChangeListener;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity
        implements BackButtonHandledFragment.OnSelectedFragmentListener, OnTitleChangeListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavigationDrawerItem> navDrawerItems;
    private NavigationDrawerListAdapter navDrawerAdapter;
    private BackButtonHandledFragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load menu items and icons
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

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
                    getSupportActionBar().setTitle(R.string.choose_operation);
                    // calling onPrepareOptionsMenu() to hide action bar icons
                    invalidateOptionsMenu();
                }
            };

            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }
        if (savedInstanceState == null) {
            displayView(-1);
        }

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // display view for selected nav drawer item
                if (displayView(position)) updateDrawerSelection(position);
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
//        // Handle action bar actions click
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
    }

//    /***
//     * Called when invalidateOptionsMenu() is triggered
//     */
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // if nav drawer is opened, hide the action items
//        if (mDrawerLayout != null) {
//            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//            menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }

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
    public void setSelectedFragment(BackButtonHandledFragment backHandledFragment) {
        selectedFragment = backHandledFragment;
    }

    @Override
    public void newTitle(String title) {
        setTitle(title);
    }

    @Override
    public void newTitle(int resourceId) {
        setTitle(getResources().getString(resourceId));
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
            case 3:
                fragment = new StandardAtmosphereFragment();
                break;
            case 4:
                fragment = new AltimetryFragment();
                break;
            case 5:
                fragment = new SpeedFragment();
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
                    .addToBackStack(fragment.getTag())
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
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void onBackPressed() {
        if (selectedFragment != null && !selectedFragment.isVisible()) {
            getFragmentManager().popBackStack();
            return;
        }

        if (selectedFragment == null || !selectedFragment.onBackPressed()) {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }
}