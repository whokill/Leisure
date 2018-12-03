package cn.linghouse.leisure.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Adapter extends FragmentPagerAdapter{
    private List<Fragment> mfragments;

    public void setList(ArrayList<Fragment> fragments) {
        this.mfragments = fragments;
    }

    public Fragment_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mfragments.get(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return mfragments.size();
    }
}
