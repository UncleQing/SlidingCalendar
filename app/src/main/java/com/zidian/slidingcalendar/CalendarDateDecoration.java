package com.zidian.slidingcalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

public class CalendarDateDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    //选中框画笔
    private Paint mPaint;
    //选中框文字画笔
    private TextPaint mTextPaint;
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
        mPaint.setColor(Color.WHITE);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        //TODO 字体大小
        mTextPaint.setTextSize(28f);
        //TODO 字体颜色
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mFontMetrics = mTextPaint.getFontMetrics();
        //TODO 设置高度
        mTop = 70;
        mTopPadding = -((mFontMetrics.bottom - mFontMetrics.top) / 2 + mFontMetrics.top);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
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

        RectF rect = new RectF(
                parent.getPaddingLeft(),
                parent.getPaddingTop(),
                (parent.getRight() - parent.getPaddingRight()),
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
     * 判断是否是组中的第一个
     */
    private boolean isFirstInGroup(int pos) {
        return !TextUtils.equals(mCallback.getGroupId(pos - 1), mCallback.getGroupId(pos));
    }

    /**
     * 判断是否是组中的最后一个
     */
    private boolean isLastInGroup(int pos) {
        return !TextUtils.equals(mCallback.getGroupId(pos), mCallback.getGroupId(pos + 7));
    }

    interface ChooseCallback {
        String getGroupId(int position);
    }
}
