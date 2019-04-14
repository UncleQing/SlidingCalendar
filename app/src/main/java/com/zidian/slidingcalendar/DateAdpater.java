package com.zidian.slidingcalendar;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DateAdpater extends RecyclerView.Adapter<DateAdpater.ViewHolder> {

    private Context mContext;
    private List<DateInfoBean> mList;
    private OnClickDayListener mListener;

    public DateAdpater(Context context, List<DateInfoBean> list) {
        mContext = context;
        mList = list;
    }

    public void setListener(OnClickDayListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        switch (i) {
            case DateInfoBean.TYPE_DATE_BLANK:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_calendar_blank, viewGroup, false);
                break;
            case DateInfoBean.TYPE_DATE_TITLE:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_calendar_title, viewGroup, false);
                break;
            case DateInfoBean.TYPE_DATE_NORMAL:
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_calendar_date, viewGroup, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        final DateInfoBean bean = mList.get(i);

        switch (getItemViewType(i)) {
            case DateInfoBean.TYPE_DATE_BLANK:
                //空
                break;
            case DateInfoBean.TYPE_DATE_TITLE:
                //title
                viewHolder.tvTitle.setText(bean.getGroupName());
                break;
            case DateInfoBean.TYPE_DATE_NORMAL:
            default:
                //日期
                if (bean.isToday()) {
                    viewHolder.tvDay.setText("今天");
                } else if (bean.getDate() > 0) {
                    viewHolder.tvDay.setText("");
                } else {
                    viewHolder.tvDay.setText(bean.getDate());
                }

                if (bean.isChooseDay()) {
                    //选择日期
                    viewHolder.tvDay.setTextColor(Color.WHITE);
                    viewHolder.tvState.setTextColor(Color.WHITE);
                    switch (bean.getIntervalType()) {
                        case DateInfoBean.TYPE_INTERVAL_START:
                            //开始
                            viewHolder.itemView.setBackgroundResource(R.drawable.bg_calendar_select);
                            viewHolder.tvState.setText("开始");
                            break;
                        case DateInfoBean.TYPE_INTERVAL_MIDDLE:
                            //中间
                            viewHolder.itemView.setBackgroundResource(R.drawable.bg_calendar_select_mid);
                            viewHolder.tvState.setText("");
                            break;
                        case DateInfoBean.TYPE_INTERVAL_END:
                            //结束
                            viewHolder.itemView.setBackgroundResource(R.drawable.bg_calendar_select);
                            viewHolder.tvState.setText("结束");
                            break;

                    }
                } else {
                    //正常日期
                    viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
                    viewHolder.viewDay.setBackgroundColor(Color.TRANSPARENT);
                    if (bean.isWeekend()) {
                        //TODO 日历颜色
                        viewHolder.tvDay.setTextColor(Color.parseColor("#F8C300"));
                        viewHolder.tvState.setTextColor(Color.parseColor("#F8C300"));
                    } else {
                        //TODO 日历颜色
                        viewHolder.tvDay.setTextColor(Color.parseColor("#333333"));
                        viewHolder.tvState.setTextColor(Color.parseColor("#333333"));
                    }
                }

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.isChooseDay()) {
                            if (mListener != null) {
                                mListener.onClickDay(v, bean, i);
                            }
                        }
                    }
                });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        View viewDay;
        TextView tvDay;
        TextView tvState;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            viewDay = itemView.findViewById(R.id.view_content);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvState = itemView.findViewById(R.id.tv_state);
        }
    }

    interface OnClickDayListener {
        void onClickDay(View view, DateInfoBean bean, int position);

    }
}
