package com.blurbook.blurbook.Services;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.blurbook.blurbook.Tabs.BlurbTab;
import com.blurbook.blurbook.Tabs.CategoryTab;
import com.blurbook.blurbook.Tabs.SellTab;
import com.blurbook.blurbook.Tabs.ShopTab;

/**
 * Created by Hoang on 2/26/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) // if the position is 0 we are returning the First tab
        {
            CategoryTab categoryTab = new CategoryTab();
            return categoryTab;
        }
        else if(position == 1)             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            BlurbTab blurbTab = new BlurbTab();
            return blurbTab;
        }
        else if(position == 2)
        {
            SellTab sellTab = new SellTab();
            return sellTab;
        }
        else
        {
            ShopTab shopTab = new ShopTab();
            return shopTab;
        }


    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
