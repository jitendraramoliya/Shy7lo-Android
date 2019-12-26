package shy7lo.com.shy7lo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.SortingList;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.sidebar.IndexAdapter;
import shy7lo.com.shy7lo.sidebar.SectionHeaderAdapter;
import shy7lo.com.shy7lo.sidebar.SectionHeaderListView;
import shy7lo.com.shy7lo.utils.Utils;


/**
 * Created by Jiten on 23-08-2017.
 */

public class BrandOptionActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.tvApplyFilter)
    TextView tvApplyFilter;
    @BindView(R.id.tvClear)
    TextView tvClear;
    //    @BindView(R.id.lvBrandOptionList)
//    IndexableListView lvBrandOptionList;
    //    ListView lvBrandOptionList;
    @BindView(R.id.lnrApplyFilter)
    LinearLayout lnrApplyFilter;
    @BindView(R.id.lnrTitle)
    LinearLayout lnrTitle;

    @BindView(R.id.lnrSearch)
    LinearLayout lnrSearch;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
//    @BindView(mSideBar)
//    SideBar mSideBar;

    ArrayList<Integer> mListSectionPos;
    ArrayList<SortingList.Option> mListItems;
    SectionHeaderListView lvBrandOptionList;
    SectionHeaderAdapter mSectionHeaderAdapter;
    IndexAdapter mIndexAdapter;
    EditText etSearch;

    ListView lvIndexList;

    public static String BNDL_TITLE = "TITLE";
    public static String BNDL_POSITION = "POSITION";
    public static String BNDL_FILTER_LIST = "FILTER_LIST";
    public static String BNDL_IS_APPLY_FILTER = "BNDL_IS_APPLY_FILTER";

    List<SortingList.FilterData> mFilterDataList;
    private String title = "";
    private int position = 0;
    List<SortingList.Option> mOptionList;
    boolean isSelected = false;
    Locale mLocal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_brand_option_list);
        ButterKnife.bind(getActivity());
        InitializeControls();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Shy7lo.setActivityContext(getActivity());
    }

    private void InitializeControls() {

        mListSectionPos = new ArrayList<Integer>();
        mListItems = new ArrayList<SortingList.Option>();
        lvBrandOptionList = (SectionHeaderListView) findViewById(R.id.lvBrandOptionList);
        etSearch = (EditText) findViewById(R.id.etSearch);
        lvIndexList = (ListView) findViewById(R.id.lvIndexList);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            mFilterDataList = (List<SortingList.FilterData>) b.getSerializable(BNDL_FILTER_LIST);
            title = b.getString(BNDL_TITLE);
            position = b.getInt(BNDL_POSITION);
            mOptionList = mFilterDataList.get(position).getOptions();

            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                mLocal = new Locale(MyPref.LANGUAGE_AR);
            } else {
                mLocal = new Locale(MyPref.LANGUAGE_EN);
            }

            new Poplulate().execute(mOptionList);

            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = s.toString();
                    if (mSectionHeaderAdapter != null && str != null)
                        mSectionHeaderAdapter.getFilter().filter(str);
                }
            });

        }

        tvApplyFilter.setTypeface(Shy7lo.TahomaBold);
        tvTitle.setTypeface(Shy7lo.TahomaBold);

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            lnrTitle.setScaleX(-1f);
//            ibBack.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
//            tvApplyFilter.setScaleX(-1f);
//            tvClear.setScaleX(-1f);
            lvBrandOptionList.setScaleX(-1f);
            lnrSearch.setScaleX(-1f);
            etSearch.setScaleX(-1f);
//            tvTitle.setGravity(Gravity.RIGHT);
//            etSearch.setGravity(Gravity.RIGHT);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lvIndexList.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lvIndexList.setLayoutParams(params);

        } else {
            lnrTitle.setScaleX(1f);
//            ibBack.setScaleX(1f);
            tvTitle.setScaleX(1f);
//            tvApplyFilter.setScaleX(1f);
//            tvClear.setScaleX(1f);
            lvBrandOptionList.setScaleX(1f);
            lnrSearch.setScaleX(1f);
            etSearch.setScaleX(1f);
//            tvTitle.setGravity(Gravity.LEFT);
//            etSearch.setGravity(Gravity.LEFT);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lvIndexList.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lvIndexList.setLayoutParams(params);
        }

        tvTitle.setText("" + title);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() > 0) {
                    ivSearch.setVisibility(View.GONE);
                } else {
                    ivSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




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


//        final BrandOptionAdapter brandOptionAdapter = new BrandOptionAdapter(getActivity(), mOptionList, new BrandOptionAdapter.OnBrandOptionSelectListener() {
//            @Override
//            public void onBrandOptionSelected(boolean isOptionSelected, int position) {
//                if (isOptionSelected) {
//                    if (lnrApplyFilter.getVisibility() != View.VISIBLE) {
//                        lnrApplyFilter.setVisibility(View.VISIBLE);
//                    }
//                    tvClear.setEnabled(true);
//                } else {
//                    lnrApplyFilter.setVisibility(View.GONE);
//                    tvClear.setEnabled(false);
//                }
//            }
//        });
//        lvBrandOptionList.setAdapter(brandOptionAdapter);
//        lvBrandOptionList.setFastScrollEnabled(true);
//        mSideBar.setListView(lvBrandOptionList);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSectionHeaderAdapter != null) {
                    String[] mSelectedArray = mSectionHeaderAdapter.getSelectedValue();
                    if (mSelectedArray.length > 0) {
                        if (!TextUtils.isEmpty(mSelectedArray[0]) && !TextUtils.isEmpty(mSelectedArray[1])) {

                            mFilterDataList.get(position).setFilterValue("" + mSelectedArray[0]);
                            mFilterDataList.get(position).setFilterId("" + mSelectedArray[1]);
                            mFilterDataList.get(position).setFilterSelected(true);

                        } else {

                            mFilterDataList.get(position).setFilterSelected(false);
                            mFilterDataList.get(position).setFilterValue("" + getString(R.string.all));
                            mFilterDataList.get(position).setFilterId("");

                            if (mSectionHeaderAdapter != null) {
                                mSectionHeaderAdapter.clear();
                                mSectionHeaderAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                }

                Intent back = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(BrandOptionActivity.BNDL_FILTER_LIST, (Serializable) mFilterDataList);
                bundle.putInt(BrandOptionActivity.BNDL_POSITION, position);
                bundle.putBoolean(BrandOptionActivity.BNDL_IS_APPLY_FILTER, false);
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

                if (mSectionHeaderAdapter != null) {
                    mSectionHeaderAdapter.clear();
                    mSectionHeaderAdapter.notifyDataSetChanged();
                }

                lnrApplyFilter.setVisibility(View.GONE);


            }
        });

        tvApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSectionHeaderAdapter != null) {
                    String[] mSelectedArray = mSectionHeaderAdapter.getSelectedValue();
                    if (mSelectedArray.length > 0) {
                        if (!TextUtils.isEmpty(mSelectedArray[0]) && !TextUtils.isEmpty(mSelectedArray[1])) {

                            mFilterDataList.get(position).setFilterValue("" + mSelectedArray[0]);
                            mFilterDataList.get(position).setFilterId("" + mSelectedArray[1]);
                            mFilterDataList.get(position).setFilterSelected(true);

                        } else {

                            mFilterDataList.get(position).setFilterSelected(false);
                            mFilterDataList.get(position).setFilterValue("" + getString(R.string.all));
                            mFilterDataList.get(position).setFilterId("");

                            if (mSectionHeaderAdapter != null) {
                                mSectionHeaderAdapter.clear();
                                mSectionHeaderAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                }

                Intent back = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(BrandOptionActivity.BNDL_FILTER_LIST, (Serializable) mFilterDataList);
                bundle.putInt(BrandOptionActivity.BNDL_POSITION, position);
                bundle.putBoolean(BrandOptionActivity.BNDL_IS_APPLY_FILTER, true);
                back.putExtras(bundle);
                setResult(RESULT_OK, back);
                finish();

            }
        });
    }

    private class Poplulate extends AsyncTask<List<SortingList.Option>, Void, Void> {

        @Override
        protected Void doInBackground(List<SortingList.Option>... params) {
            mListItems.clear();
            mListSectionPos.clear();
            List<SortingList.Option> items = params[0];
            if (mOptionList.size() > 0) {

                // NOT forget to sort array
                final Collator mCollator = Collator.getInstance(mLocal);
                Collections.sort(items, new Comparator<SortingList.Option>() {
                    @Override
                    public int compare(SortingList.Option t1, SortingList.Option t2) {
                        return mCollator.compare(t1.getLabel().toLowerCase().trim(), t2.getLabel().toLowerCase().trim());
//                return t1.getLabel().toLowerCase().compareTo(t2.getLabel().toLowerCase());
                    }
                });

                String prev_section = "";
                for (SortingList.Option mOption : items) {
                    String current_section = mOption.getLabel().substring(0, 1).toUpperCase(Locale.getDefault());

                    if (!prev_section.equals(current_section)) {
                        SortingList.Option mLetterOption = new SortingList().new Option();
                        mLetterOption.setStatus(false);
                        mLetterOption.setLabel("" + current_section);
                        mListItems.add(mLetterOption);
                        mListItems.add(mOption);
                        // array list of section positions
                        mListSectionPos.add(mListItems.indexOf(mLetterOption));
                        prev_section = current_section;
                    } else {
                        mListItems.add(mOption);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (!isCancelled()) {
                setListAdaptor();
            }
            super.onPostExecute(result);
        }
    }

    private void setListAdaptor() {

        mSectionHeaderAdapter = new SectionHeaderAdapter(getActivity(), mListItems, mListSectionPos, new SectionHeaderAdapter.OnBrandOptionSelectListener() {
            @Override
            public void onBrandOptionSelected(boolean isOptionSelected, int position) {
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
        lvBrandOptionList.setAdapter(mSectionHeaderAdapter);

        mIndexAdapter = new IndexAdapter(getActivity(), mListItems, mListSectionPos);
        lvIndexList.setAdapter(mIndexAdapter);
        lvIndexList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (lvBrandOptionList != null){
                    lvBrandOptionList.setSelection(mListSectionPos.get(position));
                }

            }
        });

        lvBrandOptionList.setOnScrollListener(mSectionHeaderAdapter);

    }


    public class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // NOTE: this function is *always* called from a background thread,
            // and
            // not the UI thread.
            String constraintStr = constraint.toString().toLowerCase(Locale.getDefault());
            FilterResults result = new FilterResults();

            if (constraint != null && constraint.toString().length() > 0) {
                List<SortingList.Option> filterItems = new ArrayList<SortingList.Option>();

                synchronized (this) {
                    for (SortingList.Option mOption : mOptionList) {
                        if (mOption.getLabel().toLowerCase(Locale.getDefault()).contains(constraintStr)) {
                            filterItems.add(mOption);
                        }
                    }
                    result.count = filterItems.size();
                    result.values = filterItems;
                }
            } else {
                synchronized (this) {
                    result.count = mOptionList.size();
                    result.values = mOptionList;
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<SortingList.Option> filtered = (ArrayList<SortingList.Option>) results.values;
//            setIndexBarViewVisibility(constraint.toString());
            new Poplulate().execute(filtered);
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ibBack.performClick();
    }

    private Activity getActivity() {
        return BrandOptionActivity.this;
    }
}
