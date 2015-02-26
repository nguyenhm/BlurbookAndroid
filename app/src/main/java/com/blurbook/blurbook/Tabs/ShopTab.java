package com.blurbook.blurbook.Tabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blurbook.blurbook.R;

/**
 * Created by Hoang on 2/26/2015.
 */
public class ShopTab extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_shop, container, false);
        return v;
    }
}
