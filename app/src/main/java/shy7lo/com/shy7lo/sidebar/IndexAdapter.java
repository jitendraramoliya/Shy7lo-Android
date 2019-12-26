package shy7lo.com.shy7lo.sidebar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.model.SortingList;

/**
 * Created by Jiten on 28-08-2017.
 */

public class IndexAdapter extends BaseAdapter {

    ArrayList<Integer> mListSectionPos;
    ArrayList<SortingList.Option> mOptionListItems;
    Context mContext;

    public IndexAdapter(Context mContext, ArrayList<SortingList.Option> mOptionListItems, ArrayList<Integer> mListSectionPos) {
        this.mContext = mContext;
        this.mOptionListItems = mOptionListItems;
        this.mListSectionPos = mListSectionPos;
    }

    @Override
    public int getCount() {
        return mListSectionPos.size();
    }

    @Override
    public Object getItem(int position) {
        return mListSectionPos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            ViewHolder holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sidebar_item_index, null);
            holder.row_title = (TextView) convertView.findViewById(R.id.row_title);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.row_title.setText("" + mOptionListItems.get(mListSectionPos.get(position)).getLabel().charAt(0));

        return convertView;
    }

    private class ViewHolder {
        TextView row_title;
    }

}
