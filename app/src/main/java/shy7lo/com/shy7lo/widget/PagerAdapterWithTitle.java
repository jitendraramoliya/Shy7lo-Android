package shy7lo.com.shy7lo.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;


//public class PagerAdapterWithTitle extends FragmentPagerAdapter {
public class PagerAdapterWithTitle extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> mFragments;
    private Fragment mFragment = null;
    String[] titles;

    public PagerAdapterWithTitle(FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
        super(fm);
        mFragments = fragments;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        mFragment = mFragments.get(position);
        return mFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
