package com.zidian.slidingcalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.zidian.slidingcalendar.R;
import com.zidian.slidingcalendar.bean.DateInfoBean;
import com.zidian.slidingcalendar.tools.UIUtils;

public class CalendarDateDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    //悬停栏背景画笔
    private Paint mPaint;
    //悬停栏文字画笔
    private TextPaint mTextPaint;
    //分割线
    private Paint mDividerPaint;
    private int mDividerHeight;

    private Paint.FontMetrics mFontMetrics;
    private int mTop;
    private float mTopPadding;

    private ChooseCallback mCallback;

    public CalendarDateDecoration(Context context, ChooseCallback callback) {
        mContext = context;
        mCallback = callback;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mContext.getResources().getColor(R.color.colorTitle));

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(UIUtils.sp2px(mContext, 14));
        mTextPaint.setColor(mContext.getResources().getColor(R.color.colorText));
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mFontMetrics = mTextPaint.getFontMetrics();
        mTop = UIUtils.dp2px(mContext, 32);
        mTopPadding = -((mFontMetrics.bottom - mFontMetrics.top) / 2 + mFontMetrics.top);

        mDividerPaint = new Paint();
        mDividerPaint.setColor(mContext.getResources().getColor(R.color.colorLine));
        mDividerHeight = UIUtils.dp2px(mContext, 1);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //设置padding
        outRect.left = 5;
        outRect.right = 5;
        outRect.bottom = mDividerHeight;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //画分割线
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        DateAdpater adpater = (DateAdpater) parent.getAdapter();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(view);
            if (adpater.getItemViewType(pos) == DateInfoBean.TYPE_DATE_TITLE) {
                //title下不画
                continue;
            }
            if (isLastInGroup(pos)) {
                //最后一行下不画
                continue;
            }
            float top = view.getBottom();
            float bottom = view.getBottom() + mDividerHeight;
            c.drawRect(left, top, right, bottom, mDividerPaint);
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //悬停月份栏
        GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
        int position = manager.findFirstVisibleItemPosition();
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        RecyclerView.ViewHolder viewHolder = parent.findViewHolderForAdapterPosition(position);
        View child = null;
        if (viewHolder != null) {
            child = viewHolder.itemView;
        }

        boolean flag = false;
        if (isLastInGroup(position) && null != child) {
            if (child.getHeight() + child.getTop() < mTop) {
                c.save();
                flag = true;
                c.translate(0f, (child.getHeight() + child.getTop() - mTop));
            }
        }

//        RectF rect = new RectF(
//                parent.getPaddingLeft(),
//                parent.getPaddingTop(),
//                (parent.getRight() - parent.getPaddingRight()),
//                (parent.getPaddingTop() + mTop));
        RectF rect = new RectF(
                0,
                parent.getPaddingTop(),
                (parent.getRight()),
                (parent.getPaddingTop() + mTop));
        c.drawRect(rect, mPaint);

        c.drawText(mCallback.getGroupId(position),
                rect.centerX(),
                rect.centerY() + mTopPadding,
                mTextPaint);

        if (flag) {
            c.restore();
        }
    }

    /**
     * 判断是否是月中的最后一排
     */
    private boolean isLastInGroup(int pos) {
        return !TextUtils.equals(mCallback.getGroupId(pos), mCallback.getGroupId(pos + 7));
    }

    interface ChooseCallback {
        String getGroupId(int position);
    }
}
