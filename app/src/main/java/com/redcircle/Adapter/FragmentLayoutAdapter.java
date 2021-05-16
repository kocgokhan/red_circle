package com.redcircle.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.redcircle.TabFragment.PostedFragment;
import com.redcircle.TabFragment.PreviewFragment;

public class FragmentLayoutAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public FragmentLayoutAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PreviewFragment previewFragment = new PreviewFragment();
                return previewFragment;
            case 1:
                PostedFragment postedFragment = new PostedFragment();
                return postedFragment;
            default:
                return null;
        }
    }
// this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
