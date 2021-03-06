package com.marianogiuffrida.pilotcalc.UI.navigation;

/**
 * Created by Mariano on 12/01/2015.
 */
public class NavigationDrawerItem {
    private String title;
    private int icon;

    public NavigationDrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }
}
