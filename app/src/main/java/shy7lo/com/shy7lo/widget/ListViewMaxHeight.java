package shy7lo.com.shy7lo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ListView;

import shy7lo.com.shy7lo.R;

/**
 * Created by JITEN-PC on 09-05-2017.
 */

public class ListViewMaxHeight extends ListView {

    private final int maxHeight;

    public ListViewMaxHeight(Context context) {
        this(context, null);
    }

    public ListViewMaxHeight(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListViewMaxHeight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ListViewMaxHeight);
            maxHeight = a.getDimensionPixelSize(R.styleable.ListViewMaxHeight_maxListHeight, Integer.MAX_VALUE);
            a.recycle();
        } else {
            maxHeight = 0;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (maxHeight > 0 && maxHeight < measuredHeight) {
            int measureMode = MeasureSpec.getMode(heightMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, measureMode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
