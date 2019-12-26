package shy7lo.com.shy7lo;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.fragment.CurrentOrderFragment;
import shy7lo.com.shy7lo.fragment.HistoryOrderFragment;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.Utils;
import shy7lo.com.shy7lo.widget.PagerAdapterWithTitle;

public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ibCancel)
    ImageButton ibCancel;
    @BindView(R.id.rltTitle)
    RelativeLayout rltTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tlSlidingTabs)
    TabLayout tlSlidingTabs;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    ArrayList<Fragment> mFragments = new ArrayList<>();
    String[] titles;

    CurrentOrderFragment mCurrentOrderFragment;
    HistoryOrderFragment mHistoryOrderFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_my_orders);
        ButterKnife.bind(this);

        InitializeControls();
        InitializeControlsAction();


    }

    private void InitializeControls() {

        titles = getResources().getStringArray(R.array.order_tab_array);

        mCurrentOrderFragment = new CurrentOrderFragment();
        mHistoryOrderFragment = new HistoryOrderFragment();
        mFragments.add(mCurrentOrderFragment);
        mFragments.add(mHistoryOrderFragment);

        PagerAdapterWithTitle mPagerAdapter = new PagerAdapterWithTitle(getSupportFragmentManager(), mFragments, titles);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(0);

        tlSlidingTabs.setupWithViewPager(mViewPager);

        TextView mTab1 = (TextView) (((LinearLayout) ((LinearLayout) tlSlidingTabs.getChildAt(0)).getChildAt(0)).getChildAt(1));
        TextView mTab2 = (TextView) (((LinearLayout) ((LinearLayout) tlSlidingTabs.getChildAt(0)).getChildAt(1)).getChildAt(1));

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

            mTab1.setTypeface(Shy7lo.DroidKufiBold, Typeface.NORMAL);
            mTab2.setTypeface(Shy7lo.DroidKufiBold, Typeface.NORMAL);

            tlSlidingTabs.setScaleX(-1f);
            mTab1.setScaleX(-1f);
            mTab2.setScaleX(-1f);

            rltTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);

        } else {

            mTab1.setTypeface(Shy7lo.RalewayBold, Typeface.NORMAL);
            mTab2.setTypeface(Shy7lo.RalewayBold, Typeface.NORMAL);

            tlSlidingTabs.setScaleX(1f);
            mTab1.setScaleX(1f);
            mTab2.setScaleX(1f);

            rltTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);
        }

    }

    private Context getActivity() {
        return MyOrderActivity.this;
    }

    private void InitializeControlsAction() {
        ibCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == ibCancel) {
            finish();
        }
    }
}
