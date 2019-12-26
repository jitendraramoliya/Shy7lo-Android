package shy7lo.com.shy7lo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shy7lo.com.shy7lo.adapter.FilterOptionAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.SortingList;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by Jiten on 23-08-2017.
 */

public class FilterOptionActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.tvApplyFilter)
    TextView tvApplyFilter;
    @BindView(R.id.tvClear)
    TextView tvClear;
    @BindView(R.id.lvFilterOptionList)
    ListView lvFilterOptionList;
    @BindView(R.id.lnrApplyFilter)
    LinearLayout lnrApplyFilter;
    @BindView(R.id.lnrTitle)
    LinearLayout lnrTitle;

    public static String BNDL_TITLE = "TITLE";
    public static String BNDL_POSITION = "POSITION";
    public static String BNDL_FILTER_LIST = "FILTER_LIST";
    public static String BNDL_IS_APPLY_FILTER = "BNDL_IS_APPLY_FILTER";
    public static String BNDL_IS_MULTI_SELECT = "BNDL_IS_MULTI_SELECT";

    List<SortingList.FilterData> mFilterDataList;
    private String title = "";
    private int position = 0;
    List<SortingList.Option> mOptionList;
    boolean isSelected = false, isMultiSelected= false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_filter_option_list);
        ButterKnife.bind(getActivity());
        InitializeControls();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Shy7lo.setActivityContext(getActivity());
    }

    private void InitializeControls() {

        Bundle b = getIntent().getExtras();
        if (b != null) {
            mFilterDataList = (List<SortingList.FilterData>) b.getSerializable(BNDL_FILTER_LIST);
            title = b.getString(BNDL_TITLE);
            position = b.getInt(BNDL_POSITION);
            isMultiSelected = b.getBoolean(BNDL_IS_MULTI_SELECT);
            mOptionList = mFilterDataList.get(position).getOptions();
        }

        tvApplyFilter.setTypeface(Shy7lo.TahomaBold);
        tvTitle.setTypeface(Shy7lo.TahomaBold);

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            lnrTitle.setScaleX(-1f);
//            ibBack.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
//            tvApplyFilter.setScaleX(-1f);
//            tvClear.setScaleX(-1f);
            lvFilterOptionList.setScaleX(-1f);
//            tvTitle.setGravity(Gravity.RIGHT);
        } else {
            lnrTitle.setScaleX(1f);
//            ibBack.setScaleX(1f);
            tvTitle.setScaleX(1f);
//            tvApplyFilter.setScaleX(1f);
//            tvClear.setScaleX(1f);
            lvFilterOptionList.setScaleX(1f);
//            tvTitle.setGravity(Gravity.LEFT);
        }

        tvTitle.setText("" + title);


        for (int i = 0; i < mOptionList.size(); i++) {
            if (mOptionList.get(i).isStatus()) {
                isSelected = true;
                break;
            }
        }

        if (isSelected) {
            lnrApplyFilter.setVisibility(View.VISIBLE);
            tvClear.setEnabled(true);
        } else {
            lnrApplyFilter.setVisibility(View.GONE);
            tvClear.setEnabled(false);
        }


        final FilterOptionAdapter filterOptionAdapter = new FilterOptionAdapter(getActivity(), mOptionList, isMultiSelected, new FilterOptionAdapter.OnFilterOptionSelectListener() {
            @Override
            public void onFilterOptionSelected(boolean isOptionSelected, int position) {
                if (isOptionSelected) {
                    if (lnrApplyFilter.getVisibility() != View.VISIBLE) {
                        lnrApplyFilter.setVisibility(View.VISIBLE);
                    }
                    tvClear.setEnabled(true);
                } else {
                    lnrApplyFilter.setVisibility(View.GONE);
                    tvClear.setEnabled(false);
                }
            }
        });
        lvFilterOptionList.setAdapter(filterOptionAdapter);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filterOptionAdapter != null) {
                    String[] mSelectedArray = filterOptionAdapter.getSelectedValue();
                    if (mSelectedArray.length > 0) {
                        if (!TextUtils.isEmpty(mSelectedArray[0]) && !TextUtils.isEmpty(mSelectedArray[1])) {

                            mFilterDataList.get(position).setFilterValue("" + mSelectedArray[0]);
                            mFilterDataList.get(position).setFilterId("" + mSelectedArray[1]);
                            mFilterDataList.get(position).setFilterSelected(true);

                        } else {

                            mFilterDataList.get(position).setFilterSelected(false);
                            mFilterDataList.get(position).setFilterValue("" + getString(R.string.all));
                            mFilterDataList.get(position).setFilterId("");

                            if (filterOptionAdapter != null) {
                                filterOptionAdapter.clear();
                                filterOptionAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                }

                Intent back = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(FilterOptionActivity.BNDL_FILTER_LIST, (Serializable) mFilterDataList);
                bundle.putInt(FilterOptionActivity.BNDL_POSITION, position);
                bundle.putBoolean(FilterOptionActivity.BNDL_IS_APPLY_FILTER, false);
                back.putExtras(bundle);
                setResult(RESULT_OK, back);
                finish();
            }
        });

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFilterDataList.get(position).setFilterSelected(false);
                mFilterDataList.get(position).setFilterValue("" + getString(R.string.all));
                mFilterDataList.get(position).setFilterId("");

                for (int i = 0; i < mOptionList.size(); i++) {
                    mOptionList.get(i).setStatus(false);
                }

                if (filterOptionAdapter != null) {
                    filterOptionAdapter.clear();
                    filterOptionAdapter.notifyDataSetChanged();
                }

                lnrApplyFilter.setVisibility(View.GONE);


            }
        });

        tvApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filterOptionAdapter != null) {
                    String[] mSelectedArray = filterOptionAdapter.getSelectedValue();
                    if (mSelectedArray.length > 0) {
                        if (!TextUtils.isEmpty(mSelectedArray[0]) && !TextUtils.isEmpty(mSelectedArray[1])) {

                            mFilterDataList.get(position).setFilterValue("" + mSelectedArray[0]);
                            mFilterDataList.get(position).setFilterId("" + mSelectedArray[1]);
                            mFilterDataList.get(position).setFilterSelected(true);

                        } else {

                            mFilterDataList.get(position).setFilterSelected(false);
                            mFilterDataList.get(position).setFilterValue("" + getString(R.string.all));
                            mFilterDataList.get(position).setFilterId("");

                            if (filterOptionAdapter != null) {
                                filterOptionAdapter.clear();
                                filterOptionAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                }

                Intent back = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(FilterOptionActivity.BNDL_FILTER_LIST, (Serializable) mFilterDataList);
                bundle.putInt(FilterOptionActivity.BNDL_POSITION, position);
                bundle.putBoolean(FilterOptionActivity.BNDL_IS_APPLY_FILTER, true);
                back.putExtras(bundle);
                setResult(RESULT_OK, back);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ibBack.performClick();
    }

    private Activity getActivity() {
        return FilterOptionActivity.this;
    }
}
