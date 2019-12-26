package shy7lo.com.shy7lo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shy7lo.com.shy7lo.adapter.FilterAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.CategoryList;
import shy7lo.com.shy7lo.model.SortingList;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rangeseekbar.CrystalRangeSeekbar;
import shy7lo.com.shy7lo.rangeseekbar.OnRangeSeekbarChangeListener;
import shy7lo.com.shy7lo.utils.CustomTypefaceSpan;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

import static shy7lo.com.shy7lo.FilterOptionActivity.BNDL_IS_APPLY_FILTER;

/**
 * Created by Jiten on 16-07-2017.
 */

public class FilterNewActivity extends AppCompatActivity {

    public static String BNDL_FILTER_LIST = "BNDL_FILTER_LIST";
    public static String BNDL_FILTER_JSON = "BNDL_FILTER_JSON";
    public static String BNDL_SORT_BY = "BNDL_SORT_BY";
    public static String BNDL_DIRECTION = "BNDL_DIRECTION";
    public static String BNDL_PRICE = "BNDL_PRICE";
    public static String BNDL_VIEW_TYPE = "BNDL_VIEW_TYPE";
    public static String BNDL_CATEGORY_ID = "BNDL_CATEGORY_ID";
    public static String BNDL_MAIN_CATEGORY_ID = "BNDL_MAIN_CATEGORY_ID";
    public static String BNDL_CATEGORY_NAME = "BNDL_CATEGORY_NAME";
    public static String BNDL_SELECTED_CATEGORY_NAME = "BNDL_SELECTED_CATEGORY_NAME";
    public static String BNDL_IS_RESET = "BNDL_IS_RESET";
    public static String BNDL_CATEGORY_LIST = "BNDL_CATEGORY_LIST";
    public static String BNDL_CATEGORY_ID_CHANGED = "BNDL_CATEGORY_ID_CHANGED";
    public static String PREF_CATEGORY_NAME = "PREF_CATEGORY_NAME";
    public static String BNDL_INITIAL_CATEGORY_ID = "BNDL_INITIAL_CATEGORY_ID";
    public static String BNDL_INITIAL_CATEGORY_NAME = "BNDL_INITIAL_CATEGORY_NAME";


    @BindView(R.id.tvResetAll)
    TextView tvResetAll;
    @BindView(R.id.tvApplyFilter)
    TextView tvApplyFilter;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ibCancel)
    ImageButton ibCancel;
    @BindView(R.id.ibShoppingBags)
    ImageButton ibShoppingBags;
    @BindView(R.id.rltTitle)
    RelativeLayout rltTitle;
    @BindView(R.id.trBottom)
    TableRow trBottom;
    @BindView(R.id.tvFilterTitle)
    TextView tvFilterTitle;
    @BindView(R.id.tvCancel)
    TextView tvCancel;

    ImageView ivHeaderList;
    ImageView ivHeaderGrid;

    LinearLayout lnrPopularity, lnrNew, lnrPriceHigh, lnrPriceLow, lnrDiscount, lnrOption, lnrGridView, lnrListView;
    Button btnPopularity, btnNew, btnPriceHigh, btnPriceLow, btnDiscount;
    TextView tvPopularityTitle, tvNewTitle, tvPriceHighTitle, tvPriceLowTitle, tvDiscountTitle, tvSortTitle, tvViewTitle, tvGridViewTitle, tvListViewTitle, tvPriceMin, tvPriceMax, tvPriceTitle;
    CrystalRangeSeekbar rangeSbPrice;

    private String mCurrencyCode = "";
    private float mExchangeRate;

    CategoryList mCategory;

    List<SortingList.FilterData> mFilterDataList;

    View mFooterView;

    private int RC_FILTTER_OPTION = 1001;
    private int RC_CATEGORY_OPTION = 1002;

    @BindView(R.id.lvSortingList)
    ListView lvSortingList;
    FilterAdapter filterAdapter;
    //    FilterExpandableListView expFilterList;
//    CategoryExpandableAdapter expListAdapter;
    int lastExpandedPosition = -1;

    private boolean isReset = false;

    public enum ItemType {
        ListType, GridType
    }

    public enum SortType {
        PopularitySort, NewSort, PriceHighSort, PriceLowSort, DiscountSort
    }

    private final String SORT_BY_DEFAULT = "created_at", DIRECTION_DEFAULT = "ASC", VIEWTYPE_DEFAULT = "grid";
    int PRICE_MIN = 10, PRICE_MAX = 5000, PRICE_STEP = 10;

    String sortby = SORT_BY_DEFAULT, direction = DIRECTION_DEFAULT, viewType = VIEWTYPE_DEFAULT, category_id = "", main_category_id = "",
            category_name = "", last_category_id = "", selected_category_id = "", selected_category_name = "", recievedCategoryId = "", recievedCategoryName = "", price = "";
    private boolean isCategoryChanged = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_filters_new_list);
        ButterKnife.bind(getActivity());
        InitializeControls();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Shy7lo.setActivityContext(getActivity());
    }

    private void InitializeControls() {

        mCurrencyCode = MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getResources().getString(R.string.SAR));
        mExchangeRate = MyPref.getPref(getActivity(), MyPref.DEFAULT_EXCHANGE_RATE, 1f);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            mFilterDataList = (List<SortingList.FilterData>) b.getSerializable(BNDL_FILTER_LIST);
            sortby = b.getString(FilterNewActivity.BNDL_SORT_BY, sortby);
            direction = b.getString(FilterNewActivity.BNDL_DIRECTION, direction);
            viewType = b.getString(FilterNewActivity.BNDL_VIEW_TYPE, viewType);
            category_id = b.getString(FilterNewActivity.BNDL_CATEGORY_ID, category_id);
            main_category_id = b.getString(FilterNewActivity.BNDL_MAIN_CATEGORY_ID, main_category_id);
            category_name = b.getString(FilterNewActivity.BNDL_CATEGORY_NAME, category_name);
            recievedCategoryId = b.getString(FilterNewActivity.BNDL_INITIAL_CATEGORY_ID, recievedCategoryId);
            recievedCategoryName = b.getString(FilterNewActivity.BNDL_INITIAL_CATEGORY_NAME, recievedCategoryName);
            price = b.getString(FilterNewActivity.BNDL_PRICE, price);
            String categoryList = b.getString(FilterNewActivity.BNDL_CATEGORY_LIST, "");
            if (!TextUtils.isEmpty(categoryList)) {
                mCategory = new Gson().fromJson(categoryList, CategoryList.class);
            }


            last_category_id = category_id;

            LogUtils.e("", "sortby::" + sortby);
            LogUtils.e("", "direction::" + direction);
            LogUtils.e("", "viewType::" + viewType);
            LogUtils.e("", "category_id::" + category_id);
            LogUtils.e("", "main_category_id::" + main_category_id);
            LogUtils.e("", "recievedCategoryId::" + recievedCategoryId);
            LogUtils.e("", "category_name::" + category_name);
            LogUtils.e("", "price::" + price);
//            LogUtils.e("", "categoryList::" + categoryList);

            String filterStr = b.getString(BNDL_FILTER_JSON);
            LogUtils.e("", "mFilterDataList::" + mFilterDataList.size());
            LogUtils.e("", "filterStr::" + filterStr);
//            if (!TextUtils.isEmpty(MyPref.getPref(getActivity(), FilterNewActivity.PREF_CATEGORY_NAME, ""))) {
//                category_name = MyPref.getPref(getActivity(), FilterNewActivity.PREF_CATEGORY_NAME, "");
//            }

            if (mFilterDataList != null && mFilterDataList.size() > 0) {
                for (int i = 0; i < mFilterDataList.size(); i++) {
                    if (mFilterDataList.get(i).getOptions() == null || mFilterDataList.get(i).getOptions().size() == 0) {
                        mFilterDataList.remove(i);
                    }else{
                        if (mFilterDataList.get(i).getCode().equalsIgnoreCase("price")) {
                            if (TextUtils.isEmpty(price)) {
                                List<SortingList.Option> mOptionPriceList = mFilterDataList.get(i).getOptions();
                                if (mOptionPriceList != null && mOptionPriceList.size() > 0) {
                                    int mMin = 0, mMax = 0;

                                    if (mOptionPriceList.get(0).minPrice > 0) {
                                        mMin = mOptionPriceList.get(0).minPrice;
                                    }

                                    if (mOptionPriceList.get(0).maxPrice > 0) {
                                        mMax = mOptionPriceList.get(0).maxPrice;
                                    }

                                    price = mMin + "," + mMax;

                                    if (mOptionPriceList.get(0).stepSize > 0) {
                                        PRICE_STEP = mOptionPriceList.get(0).stepSize;
                                    }
                                }
                            }
                            mFilterDataList.remove(i);
                        }
                    }
                }
                for (int i = 0; i < mFilterDataList.size(); i++) {
                    mFilterDataList.get(i).setFilterSelected(false);
                    mFilterDataList.get(i).setFilterValue("" + getString(R.string.all));
                    mFilterDataList.get(i).setFilterId("");
                    List<SortingList.Option> mOptionList = mFilterDataList.get(i).getOptions();
                    if (mOptionList != null && mOptionList.size() > 0) {
                        for (int j = 0; j < mOptionList.size(); j++) {
                            mOptionList.get(j).setStatus(false);
                        }
                    }

//                    if (TextUtils.isEmpty(price) && mFilterDataList.get(i).getCode().equalsIgnoreCase("price")) {
//                        List<SortingList.Option> mOptionPriceList = mFilterDataList.get(i).getOptions();
//                        if (mOptionPriceList != null && mOptionPriceList.size() > 0) {
//                            int mMin = 0, mMax = 0;
//
//                            if (mOptionPriceList.get(0).minPrice > 0) {
//                                mMin = mOptionPriceList.get(0).minPrice;
//                            }
//
//                            if (mOptionPriceList.get(0).maxPrice > 0) {
//                                mMax = mOptionPriceList.get(0).maxPrice;
//                            }
//
//                            price = mMin + "," + mMax;
//
//                            if (mOptionPriceList.get(0).stepSize > 0) {
//                                PRICE_STEP = mOptionPriceList.get(0).stepSize;
//                            }
//                        }
//                    }
                }
                SortingList.FilterData mFiterCategory = new SortingList().new FilterData();
                mFiterCategory.setFilterSelected(false);
                mFiterCategory.setFilterValue("" + category_name);
                mFiterCategory.setFilterId("" + category_id);
                mFiterCategory.setLabel("" + getResources().getString(R.string.category));
                mFiterCategory.setCode("category");
                mFiterCategory.setOptions(new ArrayList<SortingList.Option>());
                mFilterDataList.add(0, mFiterCategory);
            } else {
                mFilterDataList = new ArrayList<>();
                SortingList.FilterData mFiterCategory = new SortingList().new FilterData();
                mFiterCategory.setFilterSelected(false);
                mFiterCategory.setFilterValue("" + category_name);
                mFiterCategory.setFilterId("" + category_id);
                mFiterCategory.setLabel("" + getResources().getString(R.string.category));
                mFiterCategory.setCode("category");
                mFiterCategory.setOptions(new ArrayList<SortingList.Option>());
                mFilterDataList.add(0, mFiterCategory);
            }

            if (!TextUtils.isEmpty(filterStr) && !TextUtils.equals(filterStr, "{}")) {
                try {
                    JSONObject jsonObject = new JSONObject(filterStr);
//                    {"climate":"204,205","features_bags":"72,73"}
                    if (jsonObject != null) {


                        if (mFilterDataList != null && mFilterDataList.size() > 0) {

                            for (int i = 0; i < mFilterDataList.size(); i++) {

                                Iterator<String> iterator = jsonObject.keys();
                                while (iterator.hasNext()) {

                                    String key = iterator.next();
                                    LogUtils.i("TAG", "key:" + key + " filter code:" + mFilterDataList.get(i).getCode()
                                            + " result:" + mFilterDataList.get(i).getCode().equalsIgnoreCase(key));

                                    if (mFilterDataList.get(i).getCode().equalsIgnoreCase(key)) {

                                        String value = jsonObject.getString(key);
                                        LogUtils.i("TAG", "value:" + value);

                                        mFilterDataList.get(i).setFilterSelected(true);
                                        mFilterDataList.get(i).setFilterId("" + value);

                                        List<String> mValueList = new ArrayList<>();
                                        if (value.contains(",")) {
                                            mValueList = Arrays.asList(value.split(","));
                                        } else {
                                            mValueList.add(value);
                                        }

                                        List<String> mFilterOptionList = new ArrayList<>();
                                        List<SortingList.Option> mOptionList = mFilterDataList.get(i).getOptions();
                                        if (mOptionList != null && mOptionList.size() > 0) {
                                            for (int j = 0; j < mOptionList.size(); j++) {
                                                if (mValueList.contains(mOptionList.get(j).getId())) {
                                                    mFilterOptionList.add(mOptionList.get(j).getLabel());
                                                    mOptionList.get(j).setStatus(true);
                                                }

                                            }
                                            mFilterDataList.get(i).setFilterValue("" + Joiner.on(",").join(mFilterOptionList));
                                        }


                                    }
//                                    else {
//                                        List<SortingList.Option> mOptionList = mFilterDataList.get(i).getOptions();
//                                        if (mOptionList != null && mOptionList.size() > 0) {
//
//                                            mOptionList.get(0).setStatus(true);
//                                            LogUtils.e("", "Lable ::" + mOptionList.get(0).getLabel() + " " + mOptionList.get(0).isStatus());
////                                            for (int j = 0; j < mOptionList.size(); j++) {
////                                                if (mOptionList.get(j).getId().equals("")) {
////                                                    mFilterOptionList.add(mOptionList.get(j).getLabel());
////                                                    mOptionList.get(j).setStatus(true);
////                                                }
////
////                                            }
////                                            mFilterDataList.get(i).setFilterValue("" + Joiner.on(",").join(mFilterOptionList));
//                                        }
//                                    }

                                }

                            }
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            else {
//                if (mFilterDataList != null && mFilterDataList.size() > 0) {
//                    for (int i = 0; i < mFilterDataList.size(); i++) {
//                        List<SortingList.Option> mOptionList = mFilterDataList.get(i).getOptions();
//                        if (mOptionList != null && mOptionList.size() > 0) {
//                            mOptionList.get(0).setStatus(true);
//                        }
//                    }
//                }
//            }

//            if (mFilterDataList != null && mFilterDataList.size() > 0) {
//                for (int i = 0; i < mFilterDataList.size(); i++) {
//                    List<SortingList.Option> mOptionList = mFilterDataList.get(i).getOptions();
//                    if (mOptionList != null && mOptionList.size() > 0) {
//                        boolean isOptionSelected = false;
//                        for (int j = 0; j < mOptionList.size(); j++) {
//                            if (mOptionList.get(j).isStatus()) {
//                                isOptionSelected = true;
//                                break;
//                            }
//                        }
//                        if (!isOptionSelected) {
//                            mOptionList.get(0).setStatus(true);
//                        }
//                    }
//                }
//            }


        }


//        expFilterList = (FilterExpandableListView) findViewById(R.id.expFilterList);
        mFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_footer_sorting, null);
//        expFilterList.addFooterView(mFooterView, null, false);
        lvSortingList.addFooterView(mFooterView, null, false);

        ivHeaderList = (ImageView) mFooterView.findViewById(R.id.ivHeaderList);
        ivHeaderGrid = (ImageView) mFooterView.findViewById(R.id.ivHeaderGrid);

        lnrPopularity = (LinearLayout) mFooterView.findViewById(R.id.lnrPopularity);
        lnrNew = (LinearLayout) mFooterView.findViewById(R.id.lnrNew);
        lnrPriceHigh = (LinearLayout) mFooterView.findViewById(R.id.lnrPriceHigh);
        lnrPriceLow = (LinearLayout) mFooterView.findViewById(R.id.lnrPriceLow);
        lnrDiscount = (LinearLayout) mFooterView.findViewById(R.id.lnrDiscount);
        lnrOption = (LinearLayout) mFooterView.findViewById(R.id.lnrOption);
        lnrGridView = (LinearLayout) mFooterView.findViewById(R.id.lnrGridView);
        lnrListView = (LinearLayout) mFooterView.findViewById(R.id.lnrListView);


        btnPopularity = (Button) mFooterView.findViewById(R.id.btnPopularity);
        btnNew = (Button) mFooterView.findViewById(R.id.btnNew);
        btnPriceHigh = (Button) mFooterView.findViewById(R.id.btnPriceHigh);
        btnPriceLow = (Button) mFooterView.findViewById(R.id.btnPriceLow);
        btnDiscount = (Button) mFooterView.findViewById(R.id.btnDiscount);

        tvPopularityTitle = (TextView) mFooterView.findViewById(R.id.tvPopularityTitle);
        tvNewTitle = (TextView) mFooterView.findViewById(R.id.tvNewTitle);
        tvPriceHighTitle = (TextView) mFooterView.findViewById(R.id.tvPriceHighTitle);
        tvPriceLowTitle = (TextView) mFooterView.findViewById(R.id.tvPriceLowTitle);
        tvDiscountTitle = (TextView) mFooterView.findViewById(R.id.tvDiscountTitle);
        tvViewTitle = (TextView) mFooterView.findViewById(R.id.tvViewTitle);
        tvSortTitle = (TextView) mFooterView.findViewById(R.id.tvSortTitle);
        tvGridViewTitle = (TextView) mFooterView.findViewById(R.id.tvGridViewTitle);
        tvListViewTitle = (TextView) mFooterView.findViewById(R.id.tvListViewTitle);

        tvPriceTitle = (TextView) mFooterView.findViewById(R.id.tvPriceTitle);
        tvPriceMin = (TextView) mFooterView.findViewById(R.id.tvPriceMin);
        tvPriceMax = (TextView) mFooterView.findViewById(R.id.tvPriceMax);
        rangeSbPrice = (CrystalRangeSeekbar) mFooterView.findViewById(R.id.rangeSbPrice);

        if (!TextUtils.isEmpty(price)) {
            price = PRICE_MIN + "," + PRICE_MAX;
        }

        int mMin = PRICE_MIN, mMax = PRICE_MAX;
        if (!TextUtils.isEmpty(price) && price.contains(",")) {
            String min = price.split(",")[0];
            String max = price.split(",")[1];
            LogUtils.e("", "min::" + min);
            LogUtils.e("", "max::" + max);
            if (!TextUtils.isEmpty(min) && !TextUtils.isEmpty(max)) {
                mMin = Integer.parseInt(min);
                mMax = Integer.parseInt(max);
            }
        }

        rangeSbPrice
                .setCornerRadius(10f)
                .setBarHeight(Utils.pxFromDp(getActivity(), 2))
                .setBarColor(getResources().getColor(R.color.gray_d5))
                .setBarHighlightColor(getResources().getColor(R.color.gray_85))
                .setMinValue(mMin)
                .setMaxValue(mMax)
                .setMinStartValue(mMin)
                .setMaxStartValue(mMax)
                .setSteps(PRICE_STEP)
                .setLeftThumbDrawable(R.drawable.ic_seek_thumb)
                .setLeftThumbHighlightDrawable(R.drawable.ic_seek_thumb)
                .setRightThumbDrawable(R.drawable.ic_seek_thumb)
                .setRightThumbHighlightDrawable(R.drawable.ic_seek_thumb)
                .setDataType(CrystalRangeSeekbar.DataType.INTEGER)
                .apply();

        // set listener
        rangeSbPrice.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    tvPriceMin.setText(String.valueOf(minValue) + " " + mCurrencyCode);
//                    tvPriceMax.setText(String.valueOf(maxValue) + " " + mCurrencyCode);

                    SpannableStringBuilder sbMin = new SpannableStringBuilder(String.valueOf(minValue) + " " + mCurrencyCode);
                    sbMin.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, String.valueOf(minValue).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    tvPriceMin.setText(sbMin);
                    SpannableStringBuilder sbMax = new SpannableStringBuilder(String.valueOf(maxValue) + " " + mCurrencyCode);
                    sbMax.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, String.valueOf(maxValue).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    tvPriceMax.setText(sbMax);
                } else {
//                    tvPriceMin.setText(mCurrencyCode + " " + String.valueOf(minValue));
//                    tvPriceMax.setText(mCurrencyCode + " " + String.valueOf(maxValue));

                    SpannableStringBuilder sbMin = new SpannableStringBuilder(mCurrencyCode + " " + String.valueOf(minValue));
                    sbMin.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), mCurrencyCode.length() + 1, mCurrencyCode.length() + 1 + String.valueOf(minValue).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    tvPriceMin.setText(sbMin);
                    SpannableStringBuilder sbMax = new SpannableStringBuilder(mCurrencyCode + " " + String.valueOf(maxValue));
                    sbMax.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), mCurrencyCode.length() + 1, mCurrencyCode.length() + 1 + String.valueOf(maxValue).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    tvPriceMax.setText(sbMax);

                }

                price = minValue.intValue() + "," + maxValue.intValue();

            }
        });

//        SpannableString content = new SpannableString("" + tvResetAll.getText().toString());
//        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//        tvResetAll.setText(content);

        if (!TextUtils.isEmpty(viewType) && viewType.equalsIgnoreCase("list")) {
            setItemTypeView(ItemType.ListType);
        } else {
            setItemTypeView(ItemType.GridType);
        }

//        if (sortby.equalsIgnoreCase("most_viewed")) {
//            setSortTypeView(SortType.PopularitySort);
//        } else if (sortby.equalsIgnoreCase("created_at")) {
//            setSortTypeView(SortType.NewSort);
//        } else if (sortby.equalsIgnoreCase("price") && direction.equalsIgnoreCase("DESC")) {
//            setSortTypeView(SortType.PriceHighSort);
//        } else if (sortby.equalsIgnoreCase("price") && direction.equalsIgnoreCase("ASC")) {
//            setSortTypeView(SortType.PriceLowSort);
//        } else if (sortby.equalsIgnoreCase("saving")) {
//            setSortTypeView(SortType.DiscountSort);
//        } else {
//            setSortTypeView(SortType.NewSort);
//        }

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            tvTitle.setBackgroundResource(R.drawable.ic_top_logo_ar);
            rltTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
//            tvResetAll.setScaleX(-1f);
            tvApplyFilter.setScaleX(-1f);
            trBottom.setScaleX(-1f);
            mFooterView.setScaleX(-1f);
            lnrPopularity.setScaleX(-1f);
            lnrNew.setScaleX(-1f);
            lnrPriceHigh.setScaleX(-1f);
            lnrPriceLow.setScaleX(-1f);
            lnrDiscount.setScaleX(-1f);
            tvViewTitle.setScaleX(-1f);
            tvGridViewTitle.setScaleX(-1f);
            tvListViewTitle.setScaleX(-1f);
            tvFilterTitle.setScaleX(-1f);
            tvCancel.setScaleX(-1f);
            tvSortTitle.setScaleX(-1f);
            tvPriceTitle.setScaleX(-1f);
            tvPriceMin.setScaleX(-1f);
            tvPriceMax.setScaleX(-1f);
//            lvSortingList.setScaleX(-1f);

            tvApplyFilter.setTypeface(Shy7lo.TahomaBold);
            tvFilterTitle.setTypeface(Shy7lo.TahomaBold);

        } else {
            tvTitle.setBackgroundResource(R.drawable.ic_top_logo);

            rltTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);
//            tvResetAll.setScaleX(1f);
            tvApplyFilter.setScaleX(1f);
            trBottom.setScaleX(1f);
            mFooterView.setScaleX(1f);
            lnrPopularity.setScaleX(1f);
            lnrNew.setScaleX(1f);
            lnrPriceHigh.setScaleX(1f);
            lnrPriceLow.setScaleX(1f);
            lnrDiscount.setScaleX(1f);
            tvViewTitle.setScaleX(1f);
            tvGridViewTitle.setScaleX(1f);
            tvListViewTitle.setScaleX(1f);
            tvFilterTitle.setScaleX(1f);
            tvCancel.setScaleX(1f);
            tvSortTitle.setScaleX(1f);
            tvPriceTitle.setScaleX(1f);
            tvPriceMin.setScaleX(1f);
            tvPriceMax.setScaleX(1f);
//            lvSortingList.setScaleX(1f);

            tvApplyFilter.setTypeface(Shy7lo.TahomaBold);
            tvFilterTitle.setTypeface(Shy7lo.TahomaBold);
        }

        filterAdapter = new FilterAdapter(getActivity(), mFilterDataList);
        lvSortingList.setAdapter(filterAdapter);

        lvSortingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String title = mFilterDataList.get(position).getLabel();
                String code = mFilterDataList.get(position).getCode();
                List<SortingList.Option> mOptionList = mFilterDataList.get(position).getOptions();

                if (code.equalsIgnoreCase("category")) {

                    Bundle bundle = new Bundle();
                    bundle.putString(BNDL_CATEGORY_ID, "" + category_id);
                    bundle.putString(BNDL_MAIN_CATEGORY_ID, "" + main_category_id);
                    bundle.putString(BNDL_CATEGORY_NAME, "" + category_name);
                    bundle.putString(BNDL_INITIAL_CATEGORY_ID, "" + recievedCategoryId);
                    if (mCategory != null) {
                        bundle.putString(BNDL_CATEGORY_LIST, "" + new Gson().toJson(mCategory));
                    } else {
                        bundle.putString(BNDL_CATEGORY_LIST, "");
                    }

                    IntentHandler.startActivityForResult(getActivity(), CategoryNewOptionActivity.class, bundle, RC_CATEGORY_OPTION);
//                    IntentHandler.startActivityForResult(getActivity(), CategoryOptionActivity.class, bundle, RC_CATEGORY_OPTION);

                } else if (code.equalsIgnoreCase("brand")) {
//                    showBrandDialog(title, position, mOptionList);
                    Bundle bundle = new Bundle();
                    bundle.putString(FilterOptionActivity.BNDL_TITLE, "" + title);
                    bundle.putInt(FilterOptionActivity.BNDL_POSITION, position);
                    bundle.putSerializable(FilterOptionActivity.BNDL_FILTER_LIST, (Serializable) mFilterDataList);
                    IntentHandler.startActivityForResult(getActivity(), BrandOptionActivity.class, bundle, RC_FILTTER_OPTION);

                } else {
//                    showFilterDialog(title, position, mOptionList);
                    boolean isMultiSelect = false;
                    if (code.equalsIgnoreCase("color") || code.contains("size")) {
                        isMultiSelect = true;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(FilterOptionActivity.BNDL_TITLE, "" + title);
                    bundle.putInt(FilterOptionActivity.BNDL_POSITION, position);
                    bundle.putSerializable(FilterOptionActivity.BNDL_FILTER_LIST, (Serializable) mFilterDataList);
                    bundle.putBoolean(FilterOptionActivity.BNDL_IS_MULTI_SELECT, isMultiSelect);
                    IntentHandler.startActivityForResult(getActivity(), FilterOptionActivity.class, bundle, RC_FILTTER_OPTION);
//                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                }


            }
        });

//        expListAdapter = new CategoryExpandableAdapter(getActivity(), mFilterDataList);
////        expFilterList.setAdapter(expListAdapter);
//        expFilterList.setExternalAdapter(expListAdapter);

//        expFilterList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                LogUtils.e("", "groupPosition::" + groupPosition + " lastExpandedPosition::" + lastExpandedPosition);
//
//                if (expFilterList.isGroupExpanded(groupPosition)) {
//                    expFilterList.collapseGroupWithAnimation(groupPosition);
//                } else {
//                    expFilterList.expandGroupWithAnimation(groupPosition);
//                    if (lastExpandedPosition != -1
//                            && groupPosition != lastExpandedPosition) {
//                        expFilterList.collapseGroup(lastExpandedPosition);
//                    }
//                    lastExpandedPosition = groupPosition;
//                }
//                return true;
//            }
//
//        });
//
//        expFilterList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//
//            public boolean onChildClick(ExpandableListView parent, View v,
//                                        int groupPosition, int childPosition, long id) {
//
//                List<SortingList.Option> mOptionList = mFilterDataList.get(groupPosition).getOptions();
//
//                if (childPosition == 0) {
//                    mFilterDataList.get(groupPosition).setFilterSelected(false);
//                    mFilterDataList.get(groupPosition).setFilterValue("" + getString(R.string.all));
//                    mFilterDataList.get(groupPosition).setFilterId("");
//                } else {
//                    mFilterDataList.get(groupPosition).setFilterSelected(true);
//                    mFilterDataList.get(groupPosition).setFilterValue(mOptionList.get(childPosition).getLabel());
//                    mFilterDataList.get(groupPosition).setFilterId("" + mOptionList.get(childPosition).getId());
//                }
//
//                for (int j = 0; j < mOptionList.size(); j++) {
//                    if (childPosition == j) {
//                        if (mOptionList.get(j).isStatus()) {
//                            mFilterDataList.get(groupPosition).setFilterSelected(false);
//                            mFilterDataList.get(groupPosition).setFilterValue("" + getString(R.string.all));
//                            mFilterDataList.get(groupPosition).setFilterId("");
//
//                        }
//                        mOptionList.get(j).setStatus(!mOptionList.get(j).isStatus());
//                        if (!mOptionList.get(j).isStatus()) {
//                            mOptionList.get(0).setStatus(true);
//                        }
//                    } else {
//                        mOptionList.get(j).setStatus(false);
//                    }
//
//                }
//
//                if (expListAdapter != null) {
//                    expListAdapter.notifyDataSetChanged();
//                }
//
//                return true;
//            }
//        });

//        lnrPopularity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setSortTypeView(SortType.PopularitySort);
//            }
//        });
//
//        lnrNew.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setSortTypeView(SortType.NewSort);
//            }
//        });
//
//        lnrPriceHigh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setSortTypeView(SortType.PriceHighSort);
//            }
//        });
//
//        lnrPriceLow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setSortTypeView(SortType.PriceLowSort);
//            }
//        });
//
//        lnrDiscount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setSortTypeView(SortType.DiscountSort);
//            }
//        });

        lnrGridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivHeaderGrid.performClick();
            }
        });

        lnrListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivHeaderList.performClick();
            }
        });

        ivHeaderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemTypeView(ItemType.ListType);
            }
        });

        ivHeaderGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemTypeView(ItemType.GridType);
            }
        });


        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent();
                Bundle bundle = new Bundle();
                back.putExtras(bundle);
                setResult(RESULT_CANCELED, back);
                finish();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibCancel.performClick();
            }
        });

        tvResetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFilterDataList != null && mFilterDataList.size() > 0) {

                    for (int i = 0; i < mFilterDataList.size(); i++) {

                        if (i == 0) {
                            mFilterDataList.get(i).setFilterSelected(false);
                            mFilterDataList.get(i).setFilterValue("" + recievedCategoryName);
                            mFilterDataList.get(i).setFilterId("" + recievedCategoryId);
                            isCategoryChanged = false;
                            selected_category_name = recievedCategoryName;
                        } else {
                            mFilterDataList.get(i).setFilterSelected(false);
                            mFilterDataList.get(i).setFilterValue("" + getString(R.string.all));
                            mFilterDataList.get(i).setFilterId("");
                        }

                        List<SortingList.Option> mOptionList = mFilterDataList.get(i).getOptions();
                        if (mOptionList != null && mOptionList.size() > 0) {
                            for (int j = 0; j < mOptionList.size(); j++) {
                                if (j == 0) {
                                    mOptionList.get(j).setStatus(true);
                                } else {
                                    mOptionList.get(j).setStatus(false);
                                }
                            }
                        }


                    }

                    isReset = true;
                    if (filterAdapter != null) {
                        filterAdapter.notifyDataSetChanged();
                    }

//                    if (expListAdapter != null) {
//                        expListAdapter.notifyDataSetChanged();
//                    }

                }

//                setSortTypeView(SortType.NewSort);
                setItemTypeView(ItemType.GridType);

            }
        });

        tvApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFilterDataList != null && mFilterDataList.size() > 0) {
                    JSONObject jsonObject = new JSONObject();
                    if (!isCategoryChanged) {
                        for (int i = 0; i < mFilterDataList.size(); i++) {
                            if (mFilterDataList.get(i).isFilterSelected()) {
                                try {
                                    jsonObject.put(mFilterDataList.get(i).getCode(), mFilterDataList.get(i).getFilterId());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    Intent back = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(BNDL_FILTER_JSON, jsonObject.toString());
                    bundle.putString(BNDL_SORT_BY, "" + sortby);
                    bundle.putString(BNDL_DIRECTION, "" + direction);
                    bundle.putString(BNDL_VIEW_TYPE, "" + viewType);
                    bundle.putString(BNDL_PRICE, "" + price);
                    if (isCategoryChanged) {
                        bundle.putString(BNDL_CATEGORY_ID, "" + selected_category_id);
                        MyPref.setPref(getActivity(), FilterNewActivity.PREF_CATEGORY_NAME, selected_category_name);
                    } else {
                        bundle.putString(BNDL_CATEGORY_ID, "" + category_id);
                        MyPref.setPref(getActivity(), FilterNewActivity.PREF_CATEGORY_NAME, "");
                    }
                    bundle.putBoolean(BNDL_IS_RESET, isReset);
                    back.putExtras(bundle);
                    setResult(RESULT_OK, back);
                    finish();
                }

            }
        });

//        ibShoppingBags.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Bundle bundle = new Bundle();
//                bundle.putString(BNDL_CATEGORY_ID, "" + category_id);
//                bundle.putString(BNDL_CATEGORY_NAME, "" + category_name);
//                if (mCategory != null) {
//                    bundle.putString(BNDL_CATEGORY_LIST, "" + mCategory);
//                } else {
//                    bundle.putString(BNDL_CATEGORY_LIST, "");
//                }
//
//                IntentHandler.startActivity(getActivity(), CategoryOptionActivity.class, bundle);
//
//            }
//        });
    }

    private void setItemTypeView(ItemType itemType) {
        switch (itemType) {
            case ListType:
                ivHeaderList.setSelected(true);
                ivHeaderGrid.setSelected(false);
                tvListViewTitle.setTextColor(getResources().getColor(R.color.black));
                tvGridViewTitle.setTextColor(getResources().getColor(R.color.gray_8a));
                viewType = "list";
                break;
            case GridType:
                ivHeaderList.setSelected(false);
                ivHeaderGrid.setSelected(true);
                tvGridViewTitle.setTextColor(getResources().getColor(R.color.black));
                tvListViewTitle.setTextColor(getResources().getColor(R.color.gray_8a));
                viewType = VIEWTYPE_DEFAULT;
                break;
        }
    }

//    private void setSortTypeView(SortType sortType) {
//        switch (sortType) {
//            case PopularitySort:
//                btnPopularity.setBackgroundResource(R.drawable.ic_star_selected);
//                btnNew.setBackgroundResource(R.drawable.ic_new_sort);
//                btnPriceHigh.setBackgroundResource(R.drawable.ic_price);
//                btnPriceLow.setBackgroundResource(R.drawable.ic_price_high);
//                btnDiscount.setBackgroundResource(R.drawable.ic_discount);
//
//                tvPopularityTitle.setTextColor(getResources().getColor(R.color.black));
//                tvNewTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvPriceHighTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvPriceLowTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvDiscountTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//
//                sortby = "most_viewed";
//                if (TextUtils.isEmpty(direction)){
//                    direction = DIRECTION_DEFAULT;
//                }
//
//
//                break;
//            case NewSort:
//                btnPopularity.setBackgroundResource(R.drawable.ic_star);
//                btnNew.setBackgroundResource(R.drawable.ic_new_sort_seleted);
//                btnPriceHigh.setBackgroundResource(R.drawable.ic_price);
//                btnPriceLow.setBackgroundResource(R.drawable.ic_price_high);
//                btnDiscount.setBackgroundResource(R.drawable.ic_discount);
//
//                tvPopularityTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvNewTitle.setTextColor(getResources().getColor(R.color.black));
//                tvPriceHighTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvPriceLowTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvDiscountTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//
//                sortby = SORT_BY_DEFAULT;
//                if (TextUtils.isEmpty(direction)){
//                    direction = DIRECTION_DEFAULT;
//                }
//
//                break;
//            case PriceHighSort:
//                btnPopularity.setBackgroundResource(R.drawable.ic_star);
//                btnNew.setBackgroundResource(R.drawable.ic_new_sort);
//                btnPriceHigh.setBackgroundResource(R.drawable.ic_price_selected);
//                btnPriceLow.setBackgroundResource(R.drawable.ic_price_high);
//                btnDiscount.setBackgroundResource(R.drawable.ic_discount);
//
//                tvPopularityTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvNewTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvPriceHighTitle.setTextColor(getResources().getColor(R.color.black));
//                tvPriceLowTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvDiscountTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//
//                sortby = "price";
//                direction = "DESC";
//
//                break;
//            case PriceLowSort:
//                btnPopularity.setBackgroundResource(R.drawable.ic_star);
//                btnNew.setBackgroundResource(R.drawable.ic_new_sort);
//                btnPriceHigh.setBackgroundResource(R.drawable.ic_price);
//                btnPriceLow.setBackgroundResource(R.drawable.ic_price_high_selected);
//                btnDiscount.setBackgroundResource(R.drawable.ic_discount);
//
//                tvPopularityTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvNewTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvPriceHighTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvPriceLowTitle.setTextColor(getResources().getColor(R.color.black));
//                tvDiscountTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//
//                sortby = "price";
//                direction = DIRECTION_DEFAULT;
//
//                break;
//            case DiscountSort:
//                btnPopularity.setBackgroundResource(R.drawable.ic_star);
//                btnNew.setBackgroundResource(R.drawable.ic_new_sort);
//                btnPriceHigh.setBackgroundResource(R.drawable.ic_price);
//                btnPriceLow.setBackgroundResource(R.drawable.ic_price_high);
//                btnDiscount.setBackgroundResource(R.drawable.ic_discount_selected);
//
//                tvPopularityTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvNewTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvPriceHighTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvPriceLowTitle.setTextColor(getResources().getColor(R.color.gray_8a));
//                tvDiscountTitle.setTextColor(getResources().getColor(R.color.black));
//
//                sortby = "saving";
//                if (TextUtils.isEmpty(direction)){
//                    direction = DIRECTION_DEFAULT;
//                }
//                break;
//
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_FILTTER_OPTION && resultCode == RESULT_OK) {
            Bundle b = data.getExtras();
            if (b != null) {
                List<SortingList.FilterData> mDataList = (List<SortingList.FilterData>) b.getSerializable(FilterOptionActivity.BNDL_FILTER_LIST);
                int position = b.getInt(FilterOptionActivity.BNDL_POSITION);
                mFilterDataList.get(position).setFilterValue(mDataList.get(position).getFilterValue());
                mFilterDataList.get(position).setFilterSelected(mDataList.get(position).isFilterSelected());
                mFilterDataList.get(position).setFilterId(mDataList.get(position).getFilterId());
                mFilterDataList.get(position).setOptions(mDataList.get(position).getOptions());

                LogUtils.e("", "getFilterValue::" + mFilterDataList.get(b.getInt(FilterOptionActivity.BNDL_POSITION)).getFilterValue());
                LogUtils.e("", "isFilterSelected::" + mFilterDataList.get(b.getInt(FilterOptionActivity.BNDL_POSITION)).isFilterSelected());
                List<SortingList.Option> mList = mFilterDataList.get(b.getInt(FilterOptionActivity.BNDL_POSITION)).getOptions();
                for (int i = 0; i < mList.size(); i++) {
                    LogUtils.e("ds", mList.get(i).getLabel() + " " + mList.get(i).isStatus());
                }
//                if (filterAdapter != null) {
                filterAdapter.notifyDataSetChanged();
//                }
                if (b.getBoolean(BNDL_IS_APPLY_FILTER)) {
                    tvApplyFilter.performClick();
                }

            }
        } else if (requestCode == RC_CATEGORY_OPTION && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bundle bundle = data.getExtras();
//                isCategoryChanged = bundle.getBoolean(BNDL_CATEGORY_ID_CHANGED);
//                if (isCategoryChanged) {
                if (bundle != null) {
                    selected_category_id = bundle.getString(BNDL_CATEGORY_ID);
                    selected_category_name = bundle.getString(BNDL_SELECTED_CATEGORY_NAME);
                    if (!TextUtils.isEmpty(selected_category_id)) {
                        category_id = selected_category_id;
                    }
//                }
                    if (!TextUtils.isEmpty(category_id) && !last_category_id.equalsIgnoreCase(category_id)) {
                        isCategoryChanged = true;
                        if (mFilterDataList != null && mFilterDataList.size() > 0) {
                            mFilterDataList.get(0).setFilterValue("" + selected_category_name);
                            mFilterDataList.get(0).setFilterId("" + selected_category_id);
                        }
                        filterAdapter.notifyDataSetChanged();
                        tvApplyFilter.performClick();
                    } else {
                        isCategoryChanged = false;
                    }
                }

                LogUtils.e("", "isCategoryChanged::" + isCategoryChanged);
                LogUtils.e("", "selected_category_id::" + selected_category_id);
            }


        }
    }

    private Activity getActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ibCancel.performClick();
    }
}
