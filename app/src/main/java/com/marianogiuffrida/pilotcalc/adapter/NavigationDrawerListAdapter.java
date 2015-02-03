package com.marianogiuffrida.pilotcalc.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.navigation.model.NavigationDrawerItem;

import java.util.ArrayList;

/**
 * Created by Mariano on 12/01/2015.
 */
public class NavigationDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavigationDrawerItem> navigationItems;

    public NavigationDrawerListAdapter(Context context, ArrayList<NavigationDrawerItem> navDrawerItems){
        this.context = context;
        this.navigationItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navigationItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navigationItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        imgIcon.setImageResource(navigationItems.get(position).getIcon());
        txtTitle.setText(navigationItems.get(position).getTitle());

        return convertView;
    }

}
