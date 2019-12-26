package shy7lo.com.shy7lo.widget;

import android.content.Context;
import android.os.Handler;
import android.widget.ExpandableListView;

import shy7lo.com.shy7lo.R;

/**
 * Created by Jiten on 06-09-2017.
 */

public class CustExpListview extends ExpandableListView {

    int totalHeight;

    public CustExpListview(Context context, int totalHeight, boolean isSelected) {
        super(context);
        this.totalHeight = totalHeight;
        if (isSelected){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    expandGroup(1);
                }
            }, 500);

        }
        setSelector(getResources().getDrawable(R.drawable.bg_transparent));
    }

    public CustExpListview(Context context, int totalHeight) {
        super(context);
        this.totalHeight = totalHeight;
        setSelector(getResources().getDrawable(R.drawable.bg_transparent));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthMeasureSpec,
                MeasureSpec.AT_MOST);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(totalHeight,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
