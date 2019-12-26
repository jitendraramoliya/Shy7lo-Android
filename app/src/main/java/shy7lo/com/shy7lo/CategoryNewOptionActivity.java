package shy7lo.com.shy7lo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.adapter.CategoryLevel;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.CategoryList;
import shy7lo.com.shy7lo.model.SubCategoryList;
import shy7lo.com.shy7lo.model.SubChildData;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by Jiten on 29-08-2017.
 */

public class CategoryNewOptionActivity extends AppCompatActivity {

    CategoryLevel mCategoryLevel;

    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.lnrTitle)
    RelativeLayout lnrTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvClear)
    TextView tvClear;
    //    @BindView(R.id.lvCategoryOptionList)
//    ListView lvCategoryOptionList;
    @BindView(R.id.elvCategoryOptionList)
    ExpandableListView elvCategoryOptionList;
    @BindView(R.id.lnrApplyFilter)
    LinearLayout lnrApplyFilter;
    @BindView(R.id.tvApplyFilter)
    TextView tvApplyFilter;

    //    CategoryOptionAdapter mCategoryOptionAdapter;
    ArrayList<SubCategoryList> mChildList = new ArrayList<>();

    CategoryList mCategoryList;

    String category_id = "", category_name = "", recievedCategoryId = "", selected_category_id = "", selected_category_name = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_category_new_option_list);
        ButterKnife.bind(getActivity());
        InitilizeControls();

    }

    private void InitilizeControls() {

        Bundle b = getIntent().getExtras();
        if (b != null) {
            category_id = b.getString(FilterNewActivity.BNDL_CATEGORY_ID, category_id);
            category_name = b.getString(FilterNewActivity.BNDL_CATEGORY_NAME, category_name);
            recievedCategoryId = b.getString(FilterNewActivity.BNDL_INITIAL_CATEGORY_ID, recievedCategoryId);
            String categoryList = b.getString(FilterNewActivity.BNDL_CATEGORY_LIST, "");
            LogUtils.e("", "category_id::" + category_id);
            LogUtils.e("", "category_name::" + category_name);
            LogUtils.e("", "recievedCategoryId::" + recievedCategoryId);
//            LogUtils.e("", "categoryList::" + categoryList);
            if (!TextUtils.isEmpty(categoryList)) {
                mCategoryList = new Gson().fromJson(categoryList, CategoryList.class);
            }

        }

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            lnrTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
//            tvClear.setScaleX(-1f);
//            elvWomen.setScaleX(-1f);
//            elvMen.setScaleX(-1f);
//            elvKid.setScaleX(-1f);
            tvTitle.setGravity(Gravity.RIGHT);
        } else {
            lnrTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);
//            tvClear.setScaleX(1f);
//            elvWomen.setScaleX(1f);
//            elvMen.setScaleX(1f);
//            elvKid.setScaleX(1f);
            tvTitle.setGravity(Gravity.LEFT);
        }

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvApplyFilter.performClick();
            }
        });

        tvApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent back = new Intent();
                Bundle bundle = new Bundle();
                LogUtils.e("", "back selected_category_id::" + selected_category_id);
                LogUtils.e("", "back selected_category_name::" + selected_category_name);

                for (int i = 0; i < mChildList.size(); i++) {
                    SubCategoryList childData = mChildList.get(i);
                    if (childData.isSelected()) {
                        if (TextUtils.isEmpty(selected_category_id)) {
                            selected_category_id = selected_category_id + childData.id;
                        } else {
                            selected_category_id = selected_category_id + "," + childData.id;
                        }
                        if (TextUtils.isEmpty(selected_category_name)) {
                            selected_category_name = selected_category_name + childData.name;
                        } else {
                            selected_category_name = selected_category_name + "," + childData.name;
                        }
                    }

                    if (childData.childrenData != null && childData.childrenData.size() > 0) {
                        for (int j = 0; j < childData.childrenData.size(); j++) {

                            SubCategoryList.CategoryData subChildData = childData.childrenData.get(j);
                            if (subChildData.isSelected()) {
                                if (TextUtils.isEmpty(selected_category_id)) {
                                    selected_category_id = selected_category_id + subChildData.id;
                                } else {
                                    selected_category_id = selected_category_id + "," + subChildData.id;
                                }
                                if (TextUtils.isEmpty(selected_category_name)) {
                                    selected_category_name = selected_category_name + subChildData.name;
                                } else {
                                    selected_category_name = selected_category_name + "," + subChildData.name;
                                }
                            }

                            if (subChildData.childrenDataInner != null && subChildData.childrenDataInner.size() > 0) {
                                for (int k = 0; k < subChildData.childrenDataInner.size(); k++) {
                                    SubCategoryList.CategoryDataInner mChileData = subChildData.childrenDataInner.get(k);
                                    if (mChileData.isSelected()) {
                                        if (TextUtils.isEmpty(selected_category_id)) {
                                            selected_category_id = selected_category_id + mChileData.id;
                                        } else {
                                            selected_category_id = selected_category_id + "," + mChileData.id;
                                        }
                                        if (TextUtils.isEmpty(selected_category_name)) {
                                            selected_category_name = selected_category_name + mChileData.name;
                                        } else {
                                            selected_category_name = selected_category_name + "," + mChileData.name;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                LogUtils.e("", "selected_category_id::" + selected_category_id);
                LogUtils.e("", "selected_category_name::" + selected_category_name);

                if (!TextUtils.isEmpty(selected_category_id)) {
                    bundle.putString(FilterNewActivity.BNDL_CATEGORY_ID, "" + selected_category_id);
                    if (!selected_category_id.equalsIgnoreCase(category_id)) {
                        bundle.putBoolean(FilterNewActivity.BNDL_CATEGORY_ID_CHANGED, true);
                    } else {
                        bundle.putBoolean(FilterNewActivity.BNDL_CATEGORY_ID_CHANGED, false);
                    }
                } else {
                    bundle.putString(FilterNewActivity.BNDL_CATEGORY_ID, "" + category_id);
                    bundle.putBoolean(FilterNewActivity.BNDL_CATEGORY_ID_CHANGED, false);

                }
                bundle.putString(FilterNewActivity.BNDL_SELECTED_CATEGORY_NAME, selected_category_name);
                back.putExtras(bundle);
                setResult(RESULT_OK, back);
                finish();

            }
        });

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                for (int i = 0; i < mChildList.size(); i++) {
//                    mChildList.get(i).setSelected(false);
//                }
//
////                if (mCategoryOptionAdapter != null) {
////                    mCategoryOptionAdapter.clear();
////                    mCategoryOptionAdapter.notifyDataSetChanged();
////                }
//                tvClear.setEnabled(false);
//
//                lnrApplyFilter.setVisibility(View.GONE);

                for (int i = 0; i < mChildList.size(); i++) {
                    SubCategoryList childData = mChildList.get(i);
                    childData.setSelected(false);

                    if (childData.childrenData != null && childData.childrenData.size() > 0) {
                        for (int j = 0; j < childData.childrenData.size(); j++) {

                            SubCategoryList.CategoryData subChildData = childData.childrenData.get(j);
                            subChildData.setSelected(false);

                            if (subChildData.childrenDataInner != null && subChildData.childrenDataInner.size() > 0) {
                                for (int k = 0; k < subChildData.childrenDataInner.size(); k++) {
                                    SubCategoryList.CategoryDataInner mChileData = subChildData.childrenDataInner.get(k);
                                    mChileData.setSelected(false);
                                }
                            }
                        }
                    }
                }

                if (mCategoryLevel != null) {
                    mCategoryLevel.notifyDataSetChanged();
                }

                showForApplyFilter();
            }
        });

//        mCategoryOptionAdapter = new CategoryOptionAdapter(getActivity(), new CategoryOptionAdapter.OnFilterOptionSelectListener() {
//            @Override
//            public void onFilterOptionSelected(boolean isOptionSelected, int position) {
//                if (isOptionSelected) {
//                    if (lnrApplyFilter.getVisibility() != View.VISIBLE) {
//                        lnrApplyFilter.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    lnrApplyFilter.setVisibility(View.GONE);
//                }
//            }
//        });
//        lvCategoryOptionList.setAdapter(mCategoryOptionAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Shy7lo.setActivityContext(getActivity());
        if (mCategoryList == null) {
            getCategoryList();
        } else {
            showCategoryList();
        }
    }

    private Activity getActivity() {
        return CategoryNewOptionActivity.this;
    }

    public void showCategoryList() {

        if (mCategoryList != null && mCategoryList.success.equals("1")) {

            if (mChildList != null && mChildList.size() > 0) {
                mChildList.clear();
            }

            if (mCategoryList.data != null && mCategoryList.data.getChildrenData() != null && mCategoryList.data.getChildrenData().size() > 0) {

                LogUtils.e("", "category_id.contains(,)::" + category_id.contains(","));
//                if (category_id.contains(",")) {

                for (CategoryList.MainCategoryData mainCategoryData : mCategoryList.data.getChildrenData()) {
//                    if (recievedCategoryId.equalsIgnoreCase("" + mainCategoryData.getId())) {
//                        LogUtils.e("", category_id + " Main category_id.contains(" + mainCategoryData.getId() + ")::" + category_id.contains("" + mainCategoryData.getId()) + " " + mainCategoryData.getName());


                    if (isContainCategoryID(category_id, "" + mainCategoryData.getId())) {
                        for (int i = 0; i < mainCategoryData.getChildrenData().size(); i++) {
                            CategoryList.ChildData childData = mainCategoryData.getChildrenData().get(i);


                            SubCategoryList mSubCategoryList = new SubCategoryList();
                            if (childData.getIsActive() && childData.getProductCount() > 0) {
                                mSubCategoryList.id = childData.getId();
                                mSubCategoryList.parentId = childData.getParentId();
                                mSubCategoryList.isActive = childData.getIsActive();
                                mSubCategoryList.level = childData.getLevel();
                                mSubCategoryList.name = childData.getName();
                                mSubCategoryList.productCount = childData.getProductCount();
                                mSubCategoryList.position = childData.getPosition();
                                mChildList.add(mSubCategoryList);
                            }

                            if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
                                mSubCategoryList.childrenData = new ArrayList<>();

                                for (int j = 0; j < childData.getChildrenData().size(); j++) {

                                    SubChildData mChildData1 = childData.getChildrenData().get(j);
                                    SubCategoryList.CategoryData mCategoryData = new SubCategoryList().new CategoryData();

                                    if (mChildData1.getIsActive() && mChildData1.getProductCount() > 0) {
                                        mCategoryData.id = mChildData1.getId();
                                        mCategoryData.parentId = mChildData1.getParentId();
                                        mCategoryData.isActive = mChildData1.getIsActive();
                                        mCategoryData.level = mChildData1.getLevel();
                                        mCategoryData.name = mChildData1.getName();
                                        mCategoryData.productCount = mChildData1.getProductCount();
                                        mCategoryData.position = mChildData1.getPosition();

                                        mSubCategoryList.childrenData.add(mCategoryData);
                                    }

                                    if (mChildData1.getChildrenData() != null && mChildData1.getChildrenData().size() > 0) {
                                        mSubCategoryList.childrenData.get(j).childrenDataInner = new ArrayList<>();
                                        for (int k = 0; k < mChildData1.getChildrenData().size(); k++) {

                                            SubChildData.ChildData mChildData2 = mChildData1.getChildrenData().get(k);
                                            SubCategoryList.CategoryDataInner CategoryDataInner1 = new SubCategoryList().new CategoryDataInner();

                                            if (mChildData2.getIsActive() && mChildData2.getProductCount() > 0) {
                                                CategoryDataInner1.id = mChildData2.getId();
                                                CategoryDataInner1.parentId = mChildData2.getParentId();
                                                CategoryDataInner1.isActive = mChildData2.getIsActive();
                                                CategoryDataInner1.level = mChildData2.getLevel();
                                                CategoryDataInner1.name = mChildData2.getName();
                                                CategoryDataInner1.productCount = mChildData2.getProductCount();
                                                CategoryDataInner1.position = mChildData2.getPosition();
                                                mSubCategoryList.childrenData.get(j).childrenDataInner.add(CategoryDataInner1);
                                            }

                                            if (k == mChildData1.getChildrenData().size() - 1) {
                                                SubCategoryList.CategoryDataInner CategoryDataInner11 = new SubCategoryList().new CategoryDataInner();

                                                if (mChildData1.getIsActive() && mChildData1.getProductCount() > 0) {
                                                    CategoryDataInner11.id = mChildData1.getId();
                                                    CategoryDataInner11.parentId = mChildData1.getParentId();
                                                    CategoryDataInner11.isActive = mChildData1.getIsActive();
                                                    CategoryDataInner11.level = mChildData1.getLevel();
                                                    CategoryDataInner11.name = getString(R.string.all) + " " + mChildData1.getName();
                                                    CategoryDataInner11.productCount = mChildData1.getProductCount();
                                                    CategoryDataInner11.position = mChildData1.getPosition();
                                                    mSubCategoryList.childrenData.get(j).childrenDataInner.add(0, CategoryDataInner11);
                                                }
                                            }
                                        }

                                    }

                                    if (j == childData.getChildrenData().size() - 1) {
                                        SubCategoryList.CategoryData mCategoryData12 = new SubCategoryList().new CategoryData();

                                        if (childData.getIsActive() && childData.getProductCount() > 0) {

                                            mCategoryData12.id = childData.getId();
                                            mCategoryData12.parentId = childData.getParentId();
                                            mCategoryData12.isActive = childData.getIsActive();
                                            mCategoryData12.level = childData.getLevel();
                                            mCategoryData12.name = getString(R.string.all) + " " + childData.getName();
                                            mCategoryData12.productCount = childData.getProductCount();
                                            mCategoryData12.position = childData.getPosition();

                                            mSubCategoryList.childrenData.add(0, mCategoryData12);
                                        }
                                    }

                                }
                            }
                        }
                    } else {

                        for (int i = 0; i < mainCategoryData.getChildrenData().size(); i++) {
                            CategoryList.ChildData childData = mainCategoryData.getChildrenData().get(i);

                            SubCategoryList mSubCategoryList = new SubCategoryList();

                            if (childData.getIsActive() && childData.getProductCount() > 0) {
                                mSubCategoryList.id = childData.getId();
                                mSubCategoryList.parentId = childData.getParentId();
                                mSubCategoryList.isActive = childData.getIsActive();
                                mSubCategoryList.level = childData.getLevel();
                                mSubCategoryList.name = childData.getName();
                                mSubCategoryList.productCount = childData.getProductCount();
                                mSubCategoryList.position = childData.getPosition();
                                LogUtils.e("", category_id + " top equal " + childData.getId() + " = " + isContainCategoryID(category_id, "" + childData.getId().toString()));
                                if (isContainCategoryID(category_id, "" + childData.getId().toString()) && childData.getChildrenData().size() <= 0) {
                                    mSubCategoryList.setSelected(true);
                                    mChildList.add(mSubCategoryList);

                                }
                            }

                            if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
                                mSubCategoryList.childrenData = new ArrayList<>();

                                boolean isSelected = false;
                                boolean isInnerSelected = false;
                                for (int j = 0; j < childData.getChildrenData().size(); j++) {
                                    SubChildData mChildData1 = childData.getChildrenData().get(j);

                                    if (isContainCategoryID(category_id, "" + mChildData1.getId().toString()) || isContainCategoryID(category_id, "" + childData.getId().toString())) {
                                        isSelected = true;
//                                            break;
                                        mChildList.add(mSubCategoryList);

                                        for (int x = 0; x < childData.getChildrenData().size(); x++) {
//
                                            SubChildData mChildData5 = childData.getChildrenData().get(x);
                                            SubCategoryList.CategoryData mCategoryData = new SubCategoryList().new CategoryData();

                                            if (mChildData5.getIsActive() && mChildData5.getProductCount() > 0) {
                                                mCategoryData.id = mChildData5.getId();
                                                mCategoryData.parentId = mChildData5.getParentId();
                                                mCategoryData.isActive = mChildData5.getIsActive();
                                                mCategoryData.level = mChildData5.getLevel();
                                                mCategoryData.name = mChildData5.getName();
                                                mCategoryData.productCount = mChildData5.getProductCount();
                                                mCategoryData.position = mChildData5.getPosition();
                                                LogUtils.e("", category_id + " equal " + mChildData5.getId() + "  = " + isContainCategoryID(category_id, "" + mChildData5.getId().toString()) + " " + mChildData5.getName() + " " + mChildData5.getChildrenData().size());
//                                            mCategoryData.setSelected(category_id.contains(mChildData5.getId().toString()));
                                                if (isContainCategoryID(category_id, "" + mChildData5.getId().toString()) && mChildData5.getChildrenData().size() <= 0) {
                                                    mCategoryData.setSelected(true);
                                                } else {
                                                    mCategoryData.setSelected(false);
                                                }
                                                mSubCategoryList.childrenData.add(mCategoryData);
                                            }

                                            if (mChildData5.getChildrenData() != null && mChildData5.getChildrenData().size() > 0) {
                                                mSubCategoryList.childrenData.get(mSubCategoryList.childrenData.size() - 1).childrenDataInner = new ArrayList<>();
                                                for (int k = 0; k < mChildData5.getChildrenData().size(); k++) {

                                                    SubChildData.ChildData mChildData2 = mChildData5.getChildrenData().get(k);
                                                    SubCategoryList.CategoryDataInner CategoryDataInner1 = new SubCategoryList().new CategoryDataInner();

                                                    if (mChildData2.getIsActive() && mChildData2.getProductCount() > 0) {
                                                        CategoryDataInner1.id = mChildData2.getId();
                                                        CategoryDataInner1.parentId = mChildData2.getParentId();
                                                        CategoryDataInner1.isActive = mChildData2.getIsActive();
                                                        CategoryDataInner1.level = mChildData2.getLevel();
                                                        CategoryDataInner1.name = mChildData2.getName();
                                                        CategoryDataInner1.productCount = mChildData2.getProductCount();
                                                        CategoryDataInner1.position = mChildData2.getPosition();
//                                                        CategoryDataInner1.setSelected(category_id.equals(mChildData2.getId().toString()));
                                                        if (isContainCategoryID(category_id, "" + mChildData2.getId().toString()) && mChildData2.getChildrenData().size() < 0) {
                                                            CategoryDataInner1.setSelected(true);
                                                        } else {
                                                            CategoryDataInner1.setSelected(false);
                                                        }
                                                        mSubCategoryList.childrenData.get(mSubCategoryList.childrenData.size() - 1).childrenDataInner.add(CategoryDataInner1);
                                                    }

                                                    if (k == mChildData5.getChildrenData().size() - 1) {
                                                        SubCategoryList.CategoryDataInner CategoryDataInner11 = new SubCategoryList().new CategoryDataInner();

                                                        if (mChildData5.getIsActive() && mChildData5.getProductCount() > 0) {
                                                            CategoryDataInner11.id = mChildData5.getId();
                                                            CategoryDataInner11.parentId = mChildData5.getParentId();
                                                            CategoryDataInner11.isActive = mChildData5.getIsActive();
                                                            CategoryDataInner11.level = mChildData5.getLevel();
                                                            CategoryDataInner11.name = getString(R.string.all) + " " + mChildData5.getName();
                                                            CategoryDataInner11.productCount = mChildData5.getProductCount();
                                                            CategoryDataInner11.position = mChildData5.getPosition();
                                                            CategoryDataInner11.setSelected(isContainCategoryID(category_id, "" + mChildData5.getId().toString()));
                                                            mSubCategoryList.childrenData.get(mSubCategoryList.childrenData.size() - 1).childrenDataInner.add(0, CategoryDataInner11);
                                                        }
                                                    }
                                                }
                                            }

                                            if (x == childData.getChildrenData().size() - 1) {
                                                SubCategoryList.CategoryData mCategoryData12 = new SubCategoryList().new CategoryData();

                                                if (childData.getIsActive() && childData.getProductCount() > 0) {
                                                    mCategoryData12.id = childData.getId();
                                                    mCategoryData12.parentId = childData.getParentId();
                                                    mCategoryData12.isActive = childData.getIsActive();
                                                    mCategoryData12.level = childData.getLevel();
                                                    mCategoryData12.name = getString(R.string.all) + " " + childData.getName();
                                                    mCategoryData12.productCount = childData.getProductCount();
                                                    mCategoryData12.position = childData.getPosition();
                                                    LogUtils.e("", category_id + " equal " + childData.getId() + "  = " + isContainCategoryID(category_id, "" + childData.getId().toString()) + " " + childData.getName());
                                                    mCategoryData12.setSelected(isContainCategoryID(category_id, "" + childData.getId().toString()));
                                                    mSubCategoryList.childrenData.add(0, mCategoryData12);
                                                }
                                            }

                                        }
                                        break;
                                    }
                                    for (int k = 0; k < mChildData1.getChildrenData().size(); k++) {
                                        SubChildData.ChildData mChildData2 = mChildData1.getChildrenData().get(k);

                                        if (isContainCategoryID(category_id, "" + mChildData2.getId().toString())) {
                                            LogUtils.e("", "Inner Name::" + mChildData2.getName());

                                            if (!mChildList.equals(mSubCategoryList)) {
                                                mChildList.add(mSubCategoryList);
                                            }

                                            SubCategoryList.CategoryData mCategoryData = new SubCategoryList().new CategoryData();

                                            if (mChildData1.getIsActive() && mChildData1.getProductCount() > 0) {
                                                mCategoryData.id = mChildData1.getId();
                                                mCategoryData.parentId = mChildData1.getParentId();
                                                mCategoryData.isActive = mChildData1.getIsActive();
                                                mCategoryData.level = mChildData1.getLevel();
                                                mCategoryData.name = mChildData1.getName();
                                                mCategoryData.productCount = mChildData1.getProductCount();
                                                mCategoryData.position = mChildData1.getPosition();
                                                mCategoryData.setSelected(isContainCategoryID(category_id, "" + mChildData1.getId().toString()));

                                                if (!mSubCategoryList.childrenData.contains(mCategoryData)) {
                                                    mSubCategoryList.childrenData.add(mCategoryData);
                                                }
                                            }

                                            LogUtils.e("", "Outer Name::" + mChildData1.getName());

                                            isInnerSelected = true;
                                            mSubCategoryList.childrenData.get(mSubCategoryList.childrenData.size() - 1).childrenDataInner = new ArrayList<>();

                                            for (int m = 0; m < mChildData1.getChildrenData().size(); m++) {
//
                                                SubChildData.ChildData mChildData3 = mChildData1.getChildrenData().get(m);
                                                SubCategoryList.CategoryDataInner CategoryDataInner1 = new SubCategoryList().new CategoryDataInner();

                                                LogUtils.e("", "Inner Name::" + mChildData3.getName());

                                                if (mChildData3.getIsActive() && mChildData3.getProductCount() > 0) {
                                                    CategoryDataInner1.id = mChildData3.getId();
                                                    CategoryDataInner1.parentId = mChildData3.getParentId();
                                                    CategoryDataInner1.isActive = mChildData3.getIsActive();
                                                    CategoryDataInner1.level = mChildData3.getLevel();
                                                    CategoryDataInner1.name = mChildData3.getName();
                                                    CategoryDataInner1.productCount = mChildData3.getProductCount();
                                                    CategoryDataInner1.position = mChildData3.getPosition();
                                                    CategoryDataInner1.setSelected(isContainCategoryID(category_id, "" + mChildData3.getId().toString()));
                                                    mSubCategoryList.childrenData.get(mSubCategoryList.childrenData.size() - 1).childrenDataInner.add(CategoryDataInner1);
                                                }

                                                if (m == mChildData1.getChildrenData().size() - 1) {
                                                    SubCategoryList.CategoryDataInner CategoryDataInner11 = new SubCategoryList().new CategoryDataInner();

                                                    if (mChildData1.getIsActive() && mChildData1.getProductCount() > 0) {
                                                        CategoryDataInner11.id = mChildData1.getId();
                                                        CategoryDataInner11.parentId = mChildData1.getParentId();
                                                        CategoryDataInner11.isActive = mChildData1.getIsActive();
                                                        CategoryDataInner11.level = mChildData1.getLevel();
                                                        CategoryDataInner11.name = getString(R.string.all) + " " + mChildData1.getName();
                                                        CategoryDataInner11.productCount = mChildData1.getProductCount();
                                                        CategoryDataInner11.position = mChildData1.getPosition();
                                                        CategoryDataInner11.setSelected(isContainCategoryID(category_id, "" + mChildData1.getId().toString()));
                                                        mSubCategoryList.childrenData.get(mSubCategoryList.childrenData.size() - 1).childrenDataInner.add(0, CategoryDataInner11);
                                                    }
                                                }

                                            }

                                            break;
                                        }
                                    }
                                }
                                LogUtils.e("", "isSelected::" + isSelected);
                                LogUtils.e("", "isInnerSelected::" + isInnerSelected);
                            }
//                                    if (isSelected) {
//
//                                        mChildList.add(mSubCategoryList);
//
//                                        for (int j = 0; j < childData.getChildrenData().size(); j++) {
//
//                                            SubChildData mChildData1 = childData.getChildrenData().get(j);
//                                            SubCategoryList.CategoryData mCategoryData = new SubCategoryList().new CategoryData();
//
//                                            mCategoryData.id = mChildData1.getId();
//                                            mCategoryData.parentId = mChildData1.getParentId();
//                                            mCategoryData.isActive = mChildData1.getIsActive();
//                                            mCategoryData.level = mChildData1.getLevel();
//                                            mCategoryData.name = mChildData1.getName();
//                                            mCategoryData.productCount = mChildData1.getProductCount();
//                                            mCategoryData.position = mChildData1.getPosition();
//                                            mCategoryData.setSelected(category_id.contains(mChildData1.getId().toString()));
//                                            mSubCategoryList.childrenData.add(mCategoryData);
//
////                                            if (mChildData1.getChildrenData() != null && mChildData1.getChildrenData().size() > 0) {
////                                                mSubCategoryList.childrenData.get(j).childrenDataInner = new ArrayList<>();
////                                                for (int k = 0; k < mChildData1.getChildrenData().size(); k++) {
////
////                                                    SubChildData.ChildData mChildData2 = mChildData1.getChildrenData().get(k);
////                                                    SubCategoryList.CategoryDataInner CategoryDataInner1 = new SubCategoryList().new CategoryDataInner();
////
////                                                    CategoryDataInner1.id = mChildData2.getId();
////                                                    CategoryDataInner1.parentId = mChildData2.getParentId();
////                                                    CategoryDataInner1.isActive = mChildData2.getIsActive();
////                                                    CategoryDataInner1.level = mChildData2.getLevel();
////                                                    CategoryDataInner1.name = mChildData2.getName();
////                                                    CategoryDataInner1.productCount = mChildData2.getProductCount();
////                                                    CategoryDataInner1.position = mChildData2.getPosition();
////                                                    CategoryDataInner1.setSelected(category_id.contains(mChildData2.getId().toString()));
////                                                    mSubCategoryList.childrenData.get(j).childrenDataInner.add(CategoryDataInner1);
////                                                }
////
////                                            }
//
//                                        }
//
//                                    }
//
//                                    if (isInnerSelected){
//                                        for (int j = 0; j < childData.getChildrenData().size(); j++) {
//
//                                            SubChildData mChildData1 = childData.getChildrenData().get(j);
//                                            SubCategoryList.CategoryData mCategoryData = new SubCategoryList().new CategoryData();
//
//                                            mCategoryData.id = mChildData1.getId();
//                                            mCategoryData.parentId = mChildData1.getParentId();
//                                            mCategoryData.isActive = mChildData1.getIsActive();
//                                            mCategoryData.level = mChildData1.getLevel();
//                                            mCategoryData.name = mChildData1.getName();
//                                            mCategoryData.productCount = mChildData1.getProductCount();
//                                            mCategoryData.position = mChildData1.getPosition();
//                                            mCategoryData.setSelected(category_id.contains(mChildData1.getId().toString()));
//
//                                            if (mChildData1.getChildrenData() != null && mChildData1.getChildrenData().size() > 0) {
//                                                mSubCategoryList.childrenData.get(j).childrenDataInner = new ArrayList<>();
//                                                for (int k = 0; k < mChildData1.getChildrenData().size(); k++) {
//
//                                                    SubChildData.ChildData mChildData2 = mChildData1.getChildrenData().get(k);
//                                                    SubCategoryList.CategoryDataInner CategoryDataInner1 = new SubCategoryList().new CategoryDataInner();
//
//                                                    CategoryDataInner1.id = mChildData2.getId();
//                                                    CategoryDataInner1.parentId = mChildData2.getParentId();
//                                                    CategoryDataInner1.isActive = mChildData2.getIsActive();
//                                                    CategoryDataInner1.level = mChildData2.getLevel();
//                                                    CategoryDataInner1.name = mChildData2.getName();
//                                                    CategoryDataInner1.productCount = mChildData2.getProductCount();
//                                                    CategoryDataInner1.position = mChildData2.getPosition();
//                                                    CategoryDataInner1.setSelected(category_id.contains(mChildData2.getId().toString()));
//                                                    mSubCategoryList.childrenData.get(j).childrenDataInner.add(CategoryDataInner1);
//                                                }
//
//                                            }
//
//                                        }
//                                    }

//                                    else {
//
//
//                                        for (int j = 0; j < childData.getChildrenData().size(); j++) {
//                                            SubChildData mChildData1 = childData.getChildrenData().get(j);
//                                            SubCategoryList.CategoryData mCategoryData = new SubCategoryList().new CategoryData();
//
//                                            mCategoryData.id = mChildData1.getId();
//                                            mCategoryData.parentId = mChildData1.getParentId();
//                                            mCategoryData.isActive = mChildData1.getIsActive();
//                                            mCategoryData.level = mChildData1.getLevel();
//                                            mCategoryData.name = mChildData1.getName();
//                                            mCategoryData.productCount = mChildData1.getProductCount();
//                                            mCategoryData.position = mChildData1.getPosition();
//                                            mCategoryData.setSelected(category_id.contains(mChildData1.getId().toString()));
//
//                                            boolean isInnerSelected = false;
//                                            if (mChildData1.getChildrenData() != null && mChildData1.getChildrenData().size() > 0) {
//                                                for (int k = 0; k < mChildData1.getChildrenData().size(); k++) {
//                                                    SubChildData.ChildData mChildData2 = mChildData1.getChildrenData().get(k);
//                                                    if (category_id.contains(mChildData2.getId().toString())) {
//                                                        isInnerSelected = true;
//                                                        mSubCategoryList.childrenData.add(mCategoryData);
//                                                        if (mChildData1.getChildrenData() != null && mChildData1.getChildrenData().size() > 0) {
//                                                            mSubCategoryList.childrenData.get(j).childrenDataInner = new ArrayList<>();
//                                                            for (int l = 0; l < mChildData1.getChildrenData().size(); l++) {
//
//                                                                SubChildData.ChildData mChildData3 = mChildData1.getChildrenData().get(l);
//                                                                SubCategoryList.CategoryDataInner CategoryDataInner1 = new SubCategoryList().new CategoryDataInner();
//
//                                                                CategoryDataInner1.id = mChildData3.getId();
//                                                                CategoryDataInner1.parentId = mChildData3.getParentId();
//                                                                CategoryDataInner1.isActive = mChildData3.getIsActive();
//                                                                CategoryDataInner1.level = mChildData3.getLevel();
//                                                                CategoryDataInner1.name = mChildData3.getName();
//                                                                CategoryDataInner1.productCount = mChildData3.getProductCount();
//                                                                CategoryDataInner1.position = mChildData3.getPosition();
//                                                                CategoryDataInner1.setSelected(category_id.contains(mChildData3.getId().toString()));
//                                                                mSubCategoryList.childrenData.get(j).childrenDataInner.add(CategoryDataInner1);
//                                                            }
//
//                                                        }
//                                                        mSubCategoryList.childrenData.add(mCategoryData);
//                                                        break;
//                                                    }
//                                                }
//                                            }
//
////                                            if (isInnerSelected) {
////
////                                                if (mChildData1.getChildrenData() != null && mChildData1.getChildrenData().size() > 0) {
////                                                    mSubCategoryList.childrenData.get(j).childrenDataInner = new ArrayList<>();
////                                                    for (int k = 0; k < mChildData1.getChildrenData().size(); k++) {
////
////                                                        SubChildData.ChildData mChildData2 = mChildData1.getChildrenData().get(k);
////                                                        SubCategoryList.CategoryDataInner CategoryDataInner1 = new SubCategoryList().new CategoryDataInner();
////
////                                                        CategoryDataInner1.id = mChildData2.getId();
////                                                        CategoryDataInner1.parentId = mChildData2.getParentId();
////                                                        CategoryDataInner1.isActive = mChildData2.getIsActive();
////                                                        CategoryDataInner1.level = mChildData2.getLevel();
////                                                        CategoryDataInner1.name = mChildData2.getName();
////                                                        CategoryDataInner1.productCount = mChildData2.getProductCount();
////                                                        CategoryDataInner1.position = mChildData2.getPosition();
////                                                        CategoryDataInner1.setSelected(category_id.contains(mChildData2.getId().toString()));
////                                                        mSubCategoryList.childrenData.get(j).childrenDataInner.add(CategoryDataInner1);
////                                                    }
////
////                                                }
////                                                mSubCategoryList.childrenData.add(mCategoryData);
////                                            }
//
//
//                                        }
//
//                                    }


//                                }

                        }

                    }
//                        if (category_id.contains("" + mainCategoryData.getId())) {
//                            for (int i = 0; i < mainCategoryData.getChildrenData().size(); i++) {
//                                CategoryList.ChildData childData = mainCategoryData.getChildrenData().get(i);
//                                if (childData != null) {
//                                    SubCategoryList mSubCategoryList = new SubCategoryList();
//                                    mSubCategoryList.id = childData.getId();
//                                    mSubCategoryList.parentId = childData.getParentId();
//                                    mSubCategoryList.isActive = childData.getIsActive();
//                                    mSubCategoryList.level = childData.getLevel();
//                                    mSubCategoryList.name = childData.getName();
//                                    mSubCategoryList.productCount = childData.getProductCount();
//                                    mSubCategoryList.position = childData.getPosition();
//
//                                    mChildList.add(mSubCategoryList);
//
//                                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
//                                        mSubCategoryList.childrenData = new ArrayList<>();
//                                        for (int j = 0; j < childData.getChildrenData().size(); j++) {
//                                            SubChildData mChildData1 = childData.getChildrenData().get(j);
//                                            SubCategoryList.CategoryData mCategoryData = new SubCategoryList().new CategoryData();
//
//                                            mCategoryData.id = mChildData1.getId();
//                                            mCategoryData.parentId = mChildData1.getParentId();
//                                            mCategoryData.isActive = mChildData1.getIsActive();
//                                            mCategoryData.level = mChildData1.getLevel();
//                                            mCategoryData.name = mChildData1.getName();
//                                            mCategoryData.productCount = mChildData1.getProductCount();
//                                            mCategoryData.position = mChildData1.getPosition();
//
//                                            mSubCategoryList.childrenData.add(mCategoryData);
//
//                                            if (mChildData1.getChildrenData() != null && mChildData1.getChildrenData().size() > 0) {
//                                                mSubCategoryList.childrenData.get(j).childrenDataInner = new ArrayList<>();
//                                                for (int k = 0; k < mChildData1.getChildrenData().size(); k++) {
//
//                                                    SubChildData.ChildData mChildData2 = mChildData1.getChildrenData().get(k);
//                                                    SubCategoryList.CategoryDataInner CategoryDataInner1 = new SubCategoryList().new CategoryDataInner();
//
//                                                    CategoryDataInner1.id = mChildData2.getId();
//                                                    CategoryDataInner1.parentId = mChildData2.getParentId();
//                                                    CategoryDataInner1.isActive = mChildData2.getIsActive();
//                                                    CategoryDataInner1.level = mChildData2.getLevel();
//                                                    CategoryDataInner1.name = mChildData2.getName();
//                                                    CategoryDataInner1.productCount = mChildData2.getProductCount();
//                                                    CategoryDataInner1.position = mChildData2.getPosition();
//                                                    mSubCategoryList.childrenData.get(j).childrenDataInner.add(CategoryDataInner1);
//                                                }
//
//                                            }
//
//
//                                        }
//
//                                    }
//
//
//                                }
//
//                            }
//                            break;
//                        } else {
//                            for (int i = 0; i < mainCategoryData.getChildrenData().size(); i++) {
//                                CategoryList.ChildData childData = mainCategoryData.getChildrenData().get(i);
//                                LogUtils.e("", category_id + " Sub category_id.contains(" + childData.getId() + ")::" + category_id.contains("" + childData.getId()) + " " + childData.getName());
//                                if (category_id.contains("" + childData.getId())) {
//
//                                    SubCategoryList mSubCategoryList = new SubCategoryList();
//                                    mSubCategoryList.id = childData.getId();
//                                    mSubCategoryList.parentId = childData.getParentId();
//                                    mSubCategoryList.isActive = childData.getIsActive();
//                                    mSubCategoryList.level = childData.getLevel();
//                                    mSubCategoryList.name = childData.getName();
//                                    mSubCategoryList.productCount = childData.getProductCount();
//                                    mSubCategoryList.position = childData.getPosition();
//
//                                    mChildList.add(mSubCategoryList);
//                                    mSubCategoryList.childrenData = new ArrayList<>();
//
//                                    for (int j = 0; j < childData.getChildrenData().size(); j++) {
//                                        SubChildData subChildData = childData.getChildrenData().get(j);
//                                        if (subChildData != null) {
//
//                                            SubCategoryList.CategoryData mCategoryData = new SubCategoryList().new CategoryData();
//
//                                            mCategoryData.id = subChildData.getId();
//                                            mCategoryData.parentId = subChildData.getParentId();
//                                            mCategoryData.isActive = subChildData.getIsActive();
//                                            mCategoryData.level = subChildData.getLevel();
//                                            mCategoryData.name = subChildData.getName();
//                                            mCategoryData.productCount = subChildData.getProductCount();
//                                            mCategoryData.position = subChildData.getPosition();
//
//                                            mSubCategoryList.childrenData.add(mCategoryData);
//
//                                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
//                                                mSubCategoryList.childrenData.get(j).childrenDataInner = new ArrayList<>();
//                                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
//                                                    SubChildData.ChildData mChildData1 = subChildData.getChildrenData().get(k);
//                                                    SubCategoryList.CategoryDataInner CategoryDataInner1 = new SubCategoryList().new CategoryDataInner();
//
//                                                    CategoryDataInner1.id = mChildData1.getId();
//                                                    CategoryDataInner1.parentId = mChildData1.getParentId();
//                                                    CategoryDataInner1.isActive = mChildData1.getIsActive();
//                                                    CategoryDataInner1.level = mChildData1.getLevel();
//                                                    CategoryDataInner1.name = mChildData1.getName();
//                                                    CategoryDataInner1.productCount = mChildData1.getProductCount();
//                                                    CategoryDataInner1.position = mChildData1.getPosition();
//
//                                                    mSubCategoryList.childrenData.get(j).childrenDataInner.add(CategoryDataInner1);
//                                                }
//
//                                            }
//
//
//                                        }
//
//                                    }
//                                    break;
//                                } else {
//                                    for (int j = 0; j < childData.getChildrenData().size(); j++) {
//                                        SubChildData subChildData = childData.getChildrenData().get(j);
//                                        LogUtils.e("", category_id + " Inner category_id.contains(" + subChildData.getId() + ")::" + category_id.contains("" + subChildData.getId()) + " " + subChildData.getName());
//                                        if (category_id.contains("" + subChildData.getId())) {
//
//                                            SubCategoryList mSubCategoryList = new SubCategoryList();
//
//                                            mSubCategoryList.id = subChildData.getId();
//                                            mSubCategoryList.parentId = subChildData.getParentId();
//                                            mSubCategoryList.isActive = subChildData.getIsActive();
//                                            mSubCategoryList.level = subChildData.getLevel();
//                                            mSubCategoryList.name = subChildData.getName();
//                                            mSubCategoryList.productCount = subChildData.getProductCount();
//                                            mSubCategoryList.position = subChildData.getPosition();
//
//                                            mChildList.add(mSubCategoryList);
//
//                                            mSubCategoryList.childrenData = new ArrayList<>();
//
//                                            for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
//                                                SubChildData.ChildData mChildData2 = subChildData.getChildrenData().get(k);
//
//                                                if (mChildData2 != null) {
//
//                                                    SubCategoryList.CategoryData mCategoryData = new SubCategoryList().new CategoryData();
//                                                    mCategoryData.id = mChildData2.getId();
//                                                    mCategoryData.parentId = mChildData2.getParentId();
//                                                    mCategoryData.isActive = mChildData2.getIsActive();
//                                                    mCategoryData.level = mChildData2.getLevel();
//                                                    mCategoryData.name = mChildData2.getName();
//                                                    mCategoryData.productCount = mChildData2.getProductCount();
//                                                    mCategoryData.position = mChildData2.getPosition();
//
//                                                    mSubCategoryList.childrenData.add(mCategoryData);
//                                                }
//
//                                            }
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//                        }
                }
            }


//                } else {
//
//                    for (CategoryList.MainCategoryData mainCategoryData : mCategoryList.data.getChildrenData()) {
//                        if (category_id.equalsIgnoreCase("" + mainCategoryData.getId())) {
//                            for (int i = 0; i < mainCategoryData.getChildrenData().size(); i++) {
//                                CategoryList.ChildData childData = mainCategoryData.getChildrenData().get(i);
//                                if (childData != null) {
//                                    SubCategoryList mSubCategoryList = new SubCategoryList();
//                                    mSubCategoryList.id = childData.getId();
//                                    mSubCategoryList.parentId = childData.getParentId();
//                                    mSubCategoryList.isActive = childData.getIsActive();
//                                    mSubCategoryList.level = childData.getLevel();
//                                    mSubCategoryList.name = childData.getName();
//                                    mSubCategoryList.productCount = childData.getProductCount();
//                                    mSubCategoryList.position = childData.getPosition();
//
//                                    mChildList.add(mSubCategoryList);
//
//                                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
//                                        mSubCategoryList.childrenData = new ArrayList<>();
//                                        for (int j = 0; j < childData.getChildrenData().size(); j++) {
//                                            SubChildData mChildData1 = childData.getChildrenData().get(j);
//                                            SubCategoryList.CategoryData mCategoryData = new SubCategoryList().new CategoryData();
//
//                                            mCategoryData.id = mChildData1.getId();
//                                            mCategoryData.parentId = mChildData1.getParentId();
//                                            mCategoryData.isActive = mChildData1.getIsActive();
//                                            mCategoryData.level = mChildData1.getLevel();
//                                            mCategoryData.name = mChildData1.getName();
//                                            mCategoryData.productCount = mChildData1.getProductCount();
//                                            mCategoryData.position = mChildData1.getPosition();
//
//                                            mSubCategoryList.childrenData.add(mCategoryData);
//
//                                            if (mChildData1.getChildrenData() != null && mChildData1.getChildrenData().size() > 0) {
//                                                mSubCategoryList.childrenData.get(j).childrenDataInner = new ArrayList<>();
//                                                for (int k = 0; k < mChildData1.getChildrenData().size(); k++) {
//
//                                                    SubChildData.ChildData mChildData2 = mChildData1.getChildrenData().get(k);
//                                                    SubCategoryList.CategoryDataInner CategoryDataInner1 = new SubCategoryList().new CategoryDataInner();
//
//                                                    CategoryDataInner1.id = mChildData2.getId();
//                                                    CategoryDataInner1.parentId = mChildData2.getParentId();
//                                                    CategoryDataInner1.isActive = mChildData2.getIsActive();
//                                                    CategoryDataInner1.level = mChildData2.getLevel();
//                                                    CategoryDataInner1.name = mChildData2.getName();
//                                                    CategoryDataInner1.productCount = mChildData2.getProductCount();
//                                                    CategoryDataInner1.position = mChildData2.getPosition();
//                                                    mSubCategoryList.childrenData.get(j).childrenDataInner.add(CategoryDataInner1);
//                                                }
//
//                                            }
//
//
//                                        }
//
//                                    }
//
//
//                                }
//
//                            }
//                            break;
//                        } else {
//                            for (int i = 0; i < mainCategoryData.getChildrenData().size(); i++) {
//                                CategoryList.ChildData childData = mainCategoryData.getChildrenData().get(i);
//                                if (category_id.equalsIgnoreCase("" + childData.getId())) {
//                                    for (int j = 0; j < childData.getChildrenData().size(); j++) {
//                                        SubChildData subChildData = childData.getChildrenData().get(j);
//                                        if (subChildData != null) {
//                                            SubCategoryList mSubCategoryList = new SubCategoryList();
//                                            mSubCategoryList.id = subChildData.getId();
//                                            mSubCategoryList.parentId = subChildData.getParentId();
//                                            mSubCategoryList.isActive = subChildData.getIsActive();
//                                            mSubCategoryList.level = subChildData.getLevel();
//                                            mSubCategoryList.name = subChildData.getName();
//                                            mSubCategoryList.productCount = subChildData.getProductCount();
//                                            mSubCategoryList.position = subChildData.getPosition();
//
//                                            mChildList.add(mSubCategoryList);
//
//                                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
//                                                mSubCategoryList.childrenData = new ArrayList<>();
//                                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
//                                                    SubChildData.ChildData mChildData1 = subChildData.getChildrenData().get(k);
//                                                    SubCategoryList.CategoryData mCategoryData = new SubCategoryList().new CategoryData();
//
//                                                    mCategoryData.id = mChildData1.getId();
//                                                    mCategoryData.parentId = mChildData1.getParentId();
//                                                    mCategoryData.isActive = mChildData1.getIsActive();
//                                                    mCategoryData.level = mChildData1.getLevel();
//                                                    mCategoryData.name = mChildData1.getName();
//                                                    mCategoryData.productCount = mChildData1.getProductCount();
//                                                    mCategoryData.position = mChildData1.getPosition();
//
//                                                    mSubCategoryList.childrenData.add(mCategoryData);
//                                                }
//
//                                            }
//
//
//                                        }
//
//                                    }
//                                    break;
//                                } else {
//                                    for (int j = 0; j < childData.getChildrenData().size(); j++) {
//                                        SubChildData subChildData = childData.getChildrenData().get(j);
//                                        if (category_id.equalsIgnoreCase("" + subChildData.getId())) {
//                                            for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
//                                                SubChildData.ChildData mChildData2 = subChildData.getChildrenData().get(k);
//                                                if (mChildData2 != null) {
//                                                    SubCategoryList mSubCategoryList = new SubCategoryList();
//                                                    mSubCategoryList.id = mChildData2.getId();
//                                                    mSubCategoryList.parentId = mChildData2.getParentId();
//                                                    mSubCategoryList.isActive = mChildData2.getIsActive();
//                                                    mSubCategoryList.level = mChildData2.getLevel();
//                                                    mSubCategoryList.name = mChildData2.getName();
//                                                    mSubCategoryList.productCount = mChildData2.getProductCount();
//                                                    mSubCategoryList.position = mChildData2.getPosition();
//
//                                                    mChildList.add(mSubCategoryList);
//                                                }
//
//                                            }
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }

//            }

            setView();
//            showForApplyFilter();
        } else if (mCategoryList != null && mCategoryList.success.equals("0")) {
            Utils.showToast(getActivity(), "" + mCategoryList.message);
        } else {
            Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
        }

    }

    private boolean isContainCategoryID(String mCategoryID, String mSubCatID) {
        if (mCategoryID.contains(",")) {
            String[] mCategoryIDArray = mCategoryID.split(",");
            for (int i = 0; i < mCategoryIDArray.length; i++) {
                if (mCategoryIDArray[i].equals(mSubCatID)) {
                    return true;
                }
            }

        } else {
            return mCategoryID.equals(mSubCatID);
        }
        return false;
    }

    private void getCategoryList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getCategoryList();
                    }
                }
            });
            return;
        }

        LogUtils.e("", "getCategoryList call");
//        Utils.showProgressDialog(getActivity());

//        ApiInterface apiService =
//                RestClient.getIndexClient();
        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<CategoryList> call = serviceAPI.getCategoriesList(Shy7lo.mLangCode);
        call.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {

                LogUtils.e("", "response code:" + response.code());
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    try {

                        mCategoryList = response.body();
                        if (mCategoryList != null && mCategoryList.success.equals("2")) {
                            Utils.showInitialScreen(getActivity());
                            return;
                        }
                        showCategoryList();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    if (String.valueOf(response.code()).startsWith("5")) {
                        Utils.showAlertDialog(getActivity(), "" + response.code());
                    } else {

                        try {
                            JSONObject jResponse = new JSONObject(response.errorBody().string());
                            if (jResponse != null && jResponse.getString("success").equals("0")) {
                                Utils.showToast(getActivity(), "" + jResponse.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
//                    Utils.showAlertDialog(getActivity(), "" + response.code());
//                    Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
                }
            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                System.out.println(t.getMessage());
                Utils.closeProgressDialog();
                Utils.showAlertDialog(getActivity(), "" + t.getMessage());
//                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
            }
        });
    }

    private void setView() {

        mCategoryLevel = new CategoryLevel(getActivity(), mChildList, elvCategoryOptionList, new CategoryLevel.OnOptionClickListener() {
            @Override
            public void onOptionClicked(int level, int id) {
                LogUtils.e("", "level::" + level + " id::" + id);
                for (int i = 0; i < mChildList.size(); i++) {
                    SubCategoryList childData = mChildList.get(i);
                    if (childData.id == id && level == 1) {
                        childData.setSelected(!childData.isSelected());
                    }
//                    else {
//                        childData.setSelected(!childData.isSelected());
//                    }

                    if (childData.childrenData != null && childData.childrenData.size() > 0) {
                        for (int j = 0; j < childData.childrenData.size(); j++) {

                            SubCategoryList.CategoryData subChildData = childData.childrenData.get(j);
                            if (subChildData.id == id && level == 2) {
                                subChildData.setSelected(!subChildData.isSelected());
                            }
//                            else {
//                                subChildData.setSelected(!subChildData.isSelected());
//                            }

                            if (subChildData.childrenDataInner != null && subChildData.childrenDataInner.size() > 0) {
                                for (int k = 0; k < subChildData.childrenDataInner.size(); k++) {
                                    SubCategoryList.CategoryDataInner mChileData = subChildData.childrenDataInner.get(k);
//                                    LogUtils.e("", "isParent::" + isParent + " id::" + id + " mChileData.getId()::" + mChileData.getId());
                                    if (mChileData.id == id && level == 3) {
                                        LogUtils.e("", "mChileData.isSelected()::" + mChileData.isSelected());
                                        mChileData.setSelected(!mChileData.isSelected());

                                    }
//                                    else {
//                                        mChileData.setSelected(!mChileData.isSelected());
//                                    }
                                }
                            }
                        }
                    }
                }


                if (mCategoryLevel != null) {
                    mCategoryLevel.notifyDataSetChanged();
                }

//                if (level == 3){
//                    setSignoutView();
//                }

                showForApplyFilter();

            }
        });
        elvCategoryOptionList.setAdapter(mCategoryLevel);

//        mCategoryOptionAdapter.addAll(mChildList);
//        mWomenParentLevel = new ParentLevel(getActivity(), mWomenCategoryData, elvWomen, new ParentLevel.OnOptionClickListener() {
//            @Override
//            public void onOptionClicked(int level, int id) {
//                LogUtils.e("", "level::" + level + " id::" + id);
//
//                selected_category_id = "" + id;
//
//                for (int i = 0; i < mWomenCategoryData.getChildrenData().size(); i++) {
//                    CategoryList.ChildData childData = mWomenCategoryData.getChildrenData().get(i);
//                    if (childData.getId() == id && level == 1) {
//                        childData.setSelected(true);
//                        if (i == 0) {
//                            selected_category_name = "Women->All";
//                        } else {
//                            selected_category_name = "Women->" + childData.getName();
//                        }
//                        ProductsItemsFragment.main_category = "women";
//                        ibBack.performClick();
//
//                    } else {
//                        childData.setSelected(false);
//                    }
//
//                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
//                        for (int j = 0; j < childData.getChildrenData().size(); j++) {
//
//                            SubChildData subChildData = childData.getChildrenData().get(j);
//                            if (subChildData.getId() == id && level == 2) {
//                                subChildData.setSelected(true);
//
//                                if (j == 0) {
//                                    selected_category_name = "Women->" + childData.getName().replaceFirst("All ", "").trim();
//                                } else {
//                                    selected_category_name = "Women->" + childData.getName().trim() + "->" + subChildData.getName();
//                                }
//                                ProductsItemsFragment.main_category = "women";
//                                ibBack.performClick();
//
//                            } else {
//                                subChildData.setSelected(false);
//                            }
//
//                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
//                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
//                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
////                                    LogUtils.e("", "isParent::" + isParent + " id::" + id + " mChileData.getId()::" + mChileData.getId());
//                                    if (mChileData.getId() == id && level == 3) {
//                                        mChileData.setSelected(true);
//                                        if (k == 0) {
//                                            selected_category_name = "Women->" + childData.getName().trim() + "->" + mChileData.getName().replace("All ", "");
//                                        } else {
//                                            selected_category_name = "Women->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChileData.getName();
//                                        }
//                                        ProductsItemsFragment.main_category = "women";
//                                        ibBack.performClick();
//
//                                    } else {
//                                        mChileData.setSelected(false);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
////                for (int i = 0; i < mWomenCategoryData.getChildrenData().size(); i++) {
////                    CategoryList.ChildData childData = mWomenCategoryData.getChildrenData().get(i);
////                    LogUtils.e("", "Main getId:" + childData.getId() + " isSelected:" + childData.isSelected());
////
////                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
////                        for (int j = 0; j < childData.getChildrenData().size(); j++) {
////                            SubChildData subChildData = childData.getChildrenData().get(j);
////                            LogUtils.e("", "Sub InfoChild getId:" + subChildData.getId() + " isSelected:" + subChildData.isSelected());
////
////                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
////                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
////                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
////                                    LogUtils.e("", "Sub InfoChild InfoChild getId:" + mChileData.getId() + " isSelected:" + mChileData.isSelected());
////                                }
////                            }
////                        }
////                    }
////
////                }
//
//                if (level == 1 || level == 2) {
//                    mWomenParentLevel.notifyDataSetChanged();
//                }
//
////                LogUtils.e("", "isWomenOptionSelected::" + isWomenOptionSelected);
////                LogUtils.e("", "isMenOptionSelected::" + isMenOptionSelected);
////                LogUtils.e("", "isKidOptionSelected::" + isKidOptionSelected);
//
//                isWomenOptionSelected = true;
//
//                if (isMenOptionSelected) {
//                    clearMenSelection();
//                }
//
//                if (isKidOptionSelected) {
//                    clearKidSelection();
//                }
//
//
//            }
//        });
//        elvWomen.setAdapter(mWomenParentLevel);


    }

    private void showForApplyFilter() {
        boolean isSelected = false;
        for (int i = 0; i < mChildList.size(); i++) {
            SubCategoryList childData = mChildList.get(i);
            if (childData.isSelected()) {
                isSelected = true;
                break;
            }

            if (childData.childrenData != null && childData.childrenData.size() > 0) {
                for (int j = 0; j < childData.childrenData.size(); j++) {

                    SubCategoryList.CategoryData subChildData = childData.childrenData.get(j);
                    if (subChildData.isSelected()) {
                        isSelected = true;
                        break;
                    }

                    if (subChildData.childrenDataInner != null && subChildData.childrenDataInner.size() > 0) {
                        for (int k = 0; k < subChildData.childrenDataInner.size(); k++) {
                            SubCategoryList.CategoryDataInner mChileData = subChildData.childrenDataInner.get(k);
                            if (mChileData.isSelected()) {
                                isSelected = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (isSelected) {
            lnrApplyFilter.setVisibility(View.VISIBLE);
            tvClear.setEnabled(true);
        } else {
            lnrApplyFilter.setVisibility(View.GONE);
            tvClear.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        ibBack.performClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MaintenanceScreenActivity.RC_MAINTANANCE_CODE && requestCode == RESULT_OK
                && data != null && data.getExtras() != null && data.getExtras().containsKey(MaintenanceScreenActivity.IS_RETURN)
                && data.getExtras().getBoolean(MaintenanceScreenActivity.IS_RETURN)) {
            getCategoryList();
        }
    }
}
