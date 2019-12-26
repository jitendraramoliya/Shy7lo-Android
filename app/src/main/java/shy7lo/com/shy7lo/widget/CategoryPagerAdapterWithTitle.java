package shy7lo.com.shy7lo.widget;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


//public class PagerAdapterWithTitle extends FragmentPagerAdapter {
public class CategoryPagerAdapterWithTitle extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public CategoryPagerAdapterWithTitle(FragmentManager childFragmentManager) {
        super(childFragmentManager);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
//        LogUtils.e("", "addFragment: " + title);
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public List<Fragment> getFragmentList(){
        return mFragmentList;
    }
}
