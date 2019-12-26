package shy7lo.com.shy7lo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TabWidget;
import android.widget.TextView;

import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.LogUtils;

public class BadgeView extends TextView {

    public static final int POSITION_TOP_LEFT = 1;
    public static final int POSITION_TOP_RIGHT = 2;
    public static final int POSITION_BOTTOM_LEFT = 3;
    public static final int POSITION_BOTTOM_RIGHT = 4;
    public static final int POSITION_CENTER = 5;

    private final int DEFAULT_MARGIN_DIP = 2;
    private int DEFAULT_LR_PADDING_DIP = 4;
    private int DEFAULT_CORNER_RADIUS_DIP = 8;
    private int DEFAULT_TEXTSIZE = 10;
    private int DEFAULT_CIRCLE_RADIUS = 20;
    private final int DEFAULT_POSITION = POSITION_TOP_RIGHT;
    private static final int DEFAULT_BADGE_COLOR = Color.RED;
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    private static Animation fadeIn;
    private static Animation fadeOut;

    private Context context;
    private View target;

    private int badgePosition;
    private int badgeMarginH;
    private int badgeMarginV;
    private int badgeColor;

    private boolean isShown;

    private ShapeDrawable badgeBg;

    private int targetTabIndex;

    public BadgeView(Context context, int textSize) {
        this(context, (AttributeSet) null, android.R.attr.textViewStyle, textSize);
        setFont();
    }

    public BadgeView(Context context, AttributeSet attrs, int textSize) {
        this(context, attrs, android.R.attr.textViewStyle, textSize);
        setFont();
    }

    /**
     * Constructor -
     * <p/>
     * create a new BadgeView instance attached to a target {@link View}.
     *
     * @param context context for this view.
     * @param target  the View to attach the badge to.
     */
    public BadgeView(Context context, View target, int textSize) {
        this(context, null, android.R.attr.textViewStyle, target, 0, textSize);
        setFont();
    }

    public BadgeView(Context context, TabWidget target, int index, int textSize) {
        this(context, null, android.R.attr.textViewStyle, target, index, textSize);
        setFont();
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyle, int textSize) {
        this(context, attrs, defStyle, null, 0, textSize);
        setFont();
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyle, View target, int tabIndex, int textSize) {
        super(context, attrs, defStyle);
        init(context, target, tabIndex, textSize);
        setFont();
    }

    private void setFont() {
        if (!isInEditMode()) {
            if (MyPref.getPref(context, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                setTypeface(Shy7lo.DroidKufiRegular);
            }else{
                setTypeface(Shy7lo.RalewayRegular);
            }
        }
    }

    private void init(Context context, View target, int tabIndex, int textSize) {

        this.context = context;
        this.target = target;
        this.targetTabIndex = tabIndex;

        // apply defaults
        DEFAULT_TEXTSIZE = textSize;
        DEFAULT_CORNER_RADIUS_DIP = (int) (textSize * 0.8);
        DEFAULT_LR_PADDING_DIP = (int) (textSize / 2.4);
        badgePosition = DEFAULT_POSITION;
        badgeMarginH = dipToPixels(DEFAULT_MARGIN_DIP);
        badgeMarginV = badgeMarginH;
        badgeColor = DEFAULT_BADGE_COLOR;

        setTypeface(Typeface.DEFAULT_BOLD);
        int paddingPixels = dipToPixels(DEFAULT_LR_PADDING_DIP);
        setPadding((int) (paddingPixels * 1.2), 0, paddingPixels, (int) (paddingPixels * 0.2));
        setTextColor(DEFAULT_TEXT_COLOR);
        setTextSize(DEFAULT_TEXTSIZE);
        setGravity(Gravity.CENTER);

        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(200);

        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(200);

        isShown = false;

        if (this.target != null) {
            applyTo(this.target);
        } else {
            show();
        }

    }

    private void applyTo(View target) {

        LayoutParams lp = target.getLayoutParams();
        ViewParent parent = target.getParent();
        FrameLayout container = new FrameLayout(context);

        if (target instanceof TabWidget) {

            // set target to the relevant tab infoChild container
            target = ((TabWidget) target).getChildTabViewAt(targetTabIndex);
            this.target = target;

            ((ViewGroup) target).addView(container,
                    new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

            this.setVisibility(View.GONE);
            container.addView(this);

        } else {

            // TODO verify that parent is indeed a ViewGroup
            ViewGroup group = (ViewGroup) parent;
            int index = group.indexOfChild(target);

            group.removeView(target);
            group.addView(container, index, lp);

            container.addView(target);

            this.setVisibility(View.GONE);
            container.addView(this);

            group.invalidate();

        }

    }


    public void show() {
        show(false, null);
    }

    public void show(boolean animate) {
        show(animate, fadeIn);
    }

    public void show(Animation anim) {
        show(true, anim);
    }

    public void hide() {
        hide(false, null);
    }

    public void hide(boolean animate) {
        hide(animate, fadeOut);
    }

    public void hide(Animation anim) {
        hide(true, anim);
    }

    private void show(boolean animate, Animation anim) {
//        if (getBackground() == null) {
//            if (badgeBg == null) {
        badgeBg = getDefaultBackground();
//            }
        setBackgroundDrawable(badgeBg);
//        }
        applyLayoutParams();


        if (animate) {
            this.startAnimation(anim);
        }
        this.setVisibility(View.VISIBLE);
        isShown = true;
    }

    private void hide(boolean animate, Animation anim) {
        this.setVisibility(View.GONE);
        if (animate) {
            this.startAnimation(anim);
        }
        isShown = false;
    }

    private ShapeDrawable getDefaultBackground() {

        if (getText().length() <= 1) {
            DEFAULT_CIRCLE_RADIUS = DEFAULT_CORNER_RADIUS_DIP + DEFAULT_TEXTSIZE - DEFAULT_LR_PADDING_DIP;

            int r = dipToPixels(DEFAULT_CIRCLE_RADIUS);
            LogUtils.e("", "r::" + r);
            ShapeDrawable oval = new ShapeDrawable(new OvalShape());
            oval.setIntrinsicHeight(r);
            oval.setIntrinsicWidth(r);
            oval.getPaint().setColor(badgeColor);

            return oval;
        } else {
            int r = dipToPixels(DEFAULT_CORNER_RADIUS_DIP);
            float[] outerR = new float[]{r, r, r, r, r, r, r, r};
            RoundRectShape rr = new RoundRectShape(outerR, null, null);
            ShapeDrawable drawable = new ShapeDrawable(rr);
            drawable.getPaint().setColor(badgeColor);

            return drawable;
        }
    }

    private void applyLayoutParams() {

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        switch (badgePosition) {
            case POSITION_TOP_LEFT:
                lp.gravity = Gravity.LEFT | Gravity.TOP;
                lp.setMargins(badgeMarginH, badgeMarginV, 0, 0);
                break;
            case POSITION_TOP_RIGHT:
                lp.gravity = Gravity.RIGHT | Gravity.TOP;
                lp.setMargins(0, badgeMarginV, badgeMarginH, 0);
                break;
            case POSITION_BOTTOM_LEFT:
                lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
                lp.setMargins(badgeMarginH, 0, 0, badgeMarginV);
                break;
            case POSITION_BOTTOM_RIGHT:
                lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
                lp.setMargins(0, 0, badgeMarginH, badgeMarginV);
                break;
            case POSITION_CENTER:
                lp.gravity = Gravity.CENTER;
                lp.setMargins(0, 0, 0, 0);
                break;
            default:
                break;
        }

        setLayoutParams(lp);

        if (getMeasuredHeight() > 0) {
            setMinimumWidth(getMeasuredHeight());
        } else {
            DEFAULT_CIRCLE_RADIUS = DEFAULT_CORNER_RADIUS_DIP + DEFAULT_TEXTSIZE - DEFAULT_LR_PADDING_DIP;
            int r = dipToPixels(DEFAULT_CIRCLE_RADIUS);
            setMinimumWidth(r);
            setMinHeight(r);
            setMeasuredDimension(r, r);
        }

        invalidate();

    }

    public View getTarget() {
        return target;
    }

    @Override
    public boolean isShown() {
        return isShown;
    }

    public int getBadgePosition() {
        return badgePosition;
    }

    public void setBadgePosition(int layoutPosition) {
        this.badgePosition = layoutPosition;
    }

    public int getHorizontalBadgeMargin() {
        return badgeMarginH;
    }

    public int getVerticalBadgeMargin() {
        return badgeMarginV;
    }

    public void setBadgeMargin(int badgeMargin) {
        this.badgeMarginH = badgeMargin;
        this.badgeMarginV = badgeMargin;
    }

    public void setBadgeMargin(int horizontal, int vertical) {
        this.badgeMarginH = dipToPixels(horizontal);
        this.badgeMarginV = dipToPixels(vertical);
    }

    public int getBadgeBackgroundColor() {
        return badgeColor;
    }

    public void setBadgeBackgroundColor(int badgeColor) {
        this.badgeColor = badgeColor;
        badgeBg = getDefaultBackground();
    }

    private int dipToPixels(int dip) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }

}
