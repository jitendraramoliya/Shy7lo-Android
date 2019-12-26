package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.LogUtils;

public class CustomCityListAdapter extends ArrayAdapter {

    private List<String> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private List<String> dataListAllItems;
    private String mOthers = "";
    private AutoCompleteTextView etCity;


    public CustomCityListAdapter(Context context, int resource, List<String> storeDataLst, AutoCompleteTextView etCity) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        mContext = context;
        itemLayout = resource;
        this.etCity = etCity;

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            mOthers = "اخرى";
        } else {
            mOthers = "Other";
        }
    }

    @Override
    public int getCount() {
        if (dataList == null){
//            etCity.setDropDownHeight(0);
            return 0;
        }
        if (dataList.size() > 4){
            etCity.setDropDownHeight((int) mContext.getResources().getDimension(R.dimen._90sdp));
        }else{
            etCity.setDropDownHeight((int) (mContext.getResources().getDimension(R.dimen._22sdp) * dataList.size()));
        }
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        Log.d("CustomListAdapter",
                dataList.get(position));
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.tvCity);
        strName.setText(getItem(position));
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            LogUtils.e("", "performFiltering prefix::"+prefix);
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<String>(dataList);
                    if (!dataListAllItems.contains(mOthers)) {
                        dataListAllItems.add(dataListAllItems.size(), mOthers);
                    }
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    if (!dataListAllItems.contains(mOthers)) {
                        dataListAllItems.add(dataListAllItems.size(), mOthers);
                    }
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<String> matchValues = new ArrayList<String>();

                for (String dataItem : dataListAllItems) {
                    if (dataItem.toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(dataItem);
                    }
                }

                if (!matchValues.contains(mOthers)) {
                    matchValues.add(matchValues.size(), mOthers);
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

//            if (results != null && ((List) results.values).size() > 0) {
//                ((List) results.values).add(((List) results.values).size() - 1, mOthers);
//                results.count = ((List) results.values).size();
//            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<String>) results.values;
            } else {
                dataList = null;
            }

            LogUtils.e("", "dataList::"+dataList);
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
