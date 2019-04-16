package com.zidian.slidingcalendar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zidian.slidingcalendar.R;
import com.zidian.slidingcalendar.bean.DateInfoBean;
import com.zidian.slidingcalendar.bean.MonthInfoBean;
import com.zidian.slidingcalendar.tools.AppDateTools;
import com.zidian.slidingcalendar.tools.UIUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SlidingCalendarView extends LinearLayout {
    //最大区间
    public static final int MAX_RANGE = 31;
    //最多显示月 = 当前月+下个月+之前若干个月
    public static final int MAX_MONTH_COUNT = 14;

    private Context mContext;
    private boolean isShowWeek;
    private RecyclerView mDateView;

    private List<DateInfoBean> mList;
    private DateAdpater mAdapter;

    private DateInfoBean mStartBean;
    private DateInfoBean mEndBean;

    private boolean isInAnim;

    public SlidingCalendarView(Context context) {
        this(context, null);
    }

    public SlidingCalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingCalendar);
        isShowWeek = typedArray.getBoolean(R.styleable.SlidingCalendar_showWeek, true);

        typedArray.recycle();

        init();
    }

    private void init() {
        if (isShowWeek) {
            addHeadView();
        }
        initDate();
        addCalendarView();
    }

    /**
     * 添加星期
     */
    private void addHeadView() {
        setOrientation(LinearLayout.VERTICAL);
        LinearLayout weekView = new LinearLayout(mContext);
        LayoutParams headParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UIUtils.dp2px(mContext, 32));
        weekView.setLayoutParams(headParams);
        weekView.setOrientation(LinearLayout.HORIZONTAL);
        weekView.setBackgroundColor(Color.WHITE);

        String[] arry = {"日", "一", "二", "三", "四", "五", "六"};
        LayoutParams itemParams = new LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        itemParams.weight = 1;
        for (String i : arry) {
            TextView tv = new TextView(mContext);
            tv.setLayoutParams(itemParams);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setText(i);
            weekView.addView(tv);
        }
        addView(weekView);
    }

    /**
     * 添加日期
     */
    private void addCalendarView() {
        mDateView = new RecyclerView(mContext);
        mDateView.setBackgroundColor(Color.WHITE);
        LayoutParams dateParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mDateView.setLayoutParams(dateParams);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 7);
        mDateView.setLayoutManager(gridLayoutManager);

        mDateView.addItemDecoration(new CalendarDateDecoration(mContext, new CalendarDateDecoration.ChooseCallback() {
            @Override
            public String getGroupId(int position) {
                int size = mList.size();
                if (position < size) {
                    return mList.get(position).getGroupName();
                } else {
                    return "";
                }
            }
        }));

        mAdapter = new DateAdpater(mContext, mList);
        mAdapter.setListener(new DateAdpater.OnClickDayListener() {
            @Override
            public void onClickDay(View view, DateInfoBean bean, int position) {
                if (isInAnim) {
                    return;
                }
                int count = getSelectDayCount();

                switch (count) {
                    case 0:
                        //尚未选择日期
                        bean.setChooseDay(true);
                        bean.setIntervalType(DateInfoBean.TYPE_INTERVAL_START);

                        //刷新当前View
                        mAdapter.notifyItemChanged(position);
                        break;
                    case 1:
                        //已选择一天
                        DateInfoBean firstBean = getFirstSelectDay();
                        if (isSameDay(firstBean, bean)) {
                            //同一天则取消选择
                            firstBean.setChooseDay(false);
                            mAdapter.notifyItemChanged(position);
                        } else {
                            //非同一天,为区间结束天
                            if (checkChooseDate(firstBean, bean) == 1) {
                                bean.setChooseDay(true);
                                refreshChooseUi(firstBean, bean);
                            }else if (checkChooseDate(firstBean, bean) == 0) {
                                bean.setChooseDay(true);
                                refreshChooseUi(bean, firstBean);
                            }
                        }
                        break;
                    default:
                        //已存在区间
                        clearAndSetStartDate(bean);

                }
            }
        });

        mDateView.setAdapter(mAdapter);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return mList.get(i).getType() == DateInfoBean.TYPE_DATE_TITLE ? 7 : 1;
            }
        });

        addView(mDateView);

        //滑动到当前月，即最后一项
        mDateView.scrollToPosition(mList.size() - 1);
    }

    /**
     * 初始化日期
     */
    private void initDate() {

        List<MonthInfoBean> monthList = new ArrayList<>();

        //设置月份
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -MAX_MONTH_COUNT + 2);
        for (int i = 0; i < MAX_MONTH_COUNT; i++) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            MonthInfoBean bean = new MonthInfoBean();
            bean.setYear(year);
            bean.setMonth(month);
            monthList.add(bean);

            calendar.add(Calendar.MONTH, 1);
        }
        //设置日期
        calendar = Calendar.getInstance();
        for (MonthInfoBean bean : monthList) {
            List<DateInfoBean> dateList = new ArrayList<>();
            //设置当月第一天
            calendar.set(bean.getYear(), bean.getMonth() - 1, 1);
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            //第一天之前空几天
            int firstOffset = dayOfWeek - 1;
            //当月最后一天
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            int dayOfSum = calendar.get(Calendar.DATE);
            //设置每月的dateList
            //每月开始空白
            for (int i = 0; i < firstOffset; i++) {
                DateInfoBean dateBean = new DateInfoBean();
                dateBean.setYear(currentYear);
                dateBean.setMonth(currentMonth + 1);
                dateBean.setDate(0);
                dateBean.setType(DateInfoBean.TYPE_DATE_BLANK);
                dateBean.setGroupName(dateBean.monthToString());
                dateList.add(dateBean);
            }
            //每月日期
            for (int i = 0; i < dayOfSum; i++) {
                DateInfoBean dateBean = new DateInfoBean();
                dateBean.setYear(currentYear);
                dateBean.setMonth(currentMonth + 1);
                dateBean.setDate(i + 1);
                dateBean.setType(DateInfoBean.TYPE_DATE_NORMAL);
                dateBean.setGroupName(dateBean.monthToString());
                checkRecentDay(dateBean);
                dateList.add(dateBean);
            }
            //每月结束空白
            int lastDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int lastOffset = 7 - lastDayOfWeek;
            for (int i = 0; i < lastOffset; i++) {
                DateInfoBean dateBean = new DateInfoBean();
                dateBean.setYear(currentYear);
                dateBean.setMonth(currentMonth + 1);
                dateBean.setDate(0);
                dateBean.setType(DateInfoBean.TYPE_DATE_BLANK);
                dateBean.setGroupName(dateBean.monthToString());
                dateList.add(dateBean);
            }

            bean.setDateList(dateList);
        }

        //填充日期
        mList = new ArrayList<>();
        for (MonthInfoBean bean : monthList) {
            DateInfoBean titleBean = new DateInfoBean();
            titleBean.setYear(bean.getYear());
            titleBean.setMonth(bean.getMonth());
            titleBean.setGroupName(titleBean.monthToString());
            titleBean.setType(DateInfoBean.TYPE_DATE_TITLE);
            mList.add(titleBean);
            mList.addAll(bean.getDateList());
        }
    }

    /**
     * 判断是否今天后天明天
     *
     * @param bean
     */
    private void checkRecentDay(DateInfoBean bean) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDate = calendar.get(Calendar.DATE);
        if (bean.getYear() == currentYear && bean.getMonth() == currentMonth && bean.getDate() == currentDate) {
            //今天
            bean.setRecentDay(true);
            bean.setRecentDayName(DateInfoBean.STR_RECENT_TODAY);
            //默认选择今天
            bean.setChooseDay(true);
            bean.setIntervalType(DateInfoBean.TYPE_INTERVAL_START);
            return;
        }
        calendar.add(Calendar.DATE, 1);
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentDate = calendar.get(Calendar.DATE);
        if (bean.getYear() == currentYear && bean.getMonth() == currentMonth && bean.getDate() == currentDate) {
            //明天
            bean.setRecentDay(true);
            bean.setRecentDayName(DateInfoBean.STR_RECENT_TOMORROW);
            return;
        }
        calendar.add(Calendar.DATE, 1);
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentDate = calendar.get(Calendar.DATE);
        if (bean.getYear() == currentYear && bean.getMonth() == currentMonth && bean.getDate() == currentDate) {
            //后天
            bean.setRecentDay(true);
            bean.setRecentDayName(DateInfoBean.STR_RECENT_ACQUIRED);
            return;
        }
    }

    /**
     * 获取总选中天数
     */
    private int getSelectDayCount() {
        int selectNum = 0;
        for (DateInfoBean bean : mList) {
            if (bean.getType() == DateInfoBean.TYPE_DATE_NORMAL && bean.isChooseDay()) {
                selectNum++;
            }
        }
        return selectNum;
    }

    /**
     * 获取第一个选中的
     */
    private DateInfoBean getFirstSelectDay() {
        for (DateInfoBean bean : mList) {
            if (bean.getType() == DateInfoBean.TYPE_DATE_NORMAL && bean.isChooseDay()) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 判断是否同一天
     *
     * @param bean1
     * @param bean2
     * @return
     */
    private boolean isSameDay(DateInfoBean bean1, DateInfoBean bean2) {
        if (null == bean1 || null == bean2) {
            return false;
        }
        return bean1.getYear() == bean2.getYear() && bean1.getMonth() == bean2.getMonth() && bean1.getDate() == bean2.getDate();
    }

    /**
     * 判断bean是否在firstBean之后且不超过最大范围
     *
     * @param firstBean
     * @param bean
     * @return
     */
    /**
     * 判断bean和firstBean日期前后
     * -1：无效或超出最大范围或同一天
     *  0: bean在firstBean之前
     *  1：bean在firstBean之后
     * @param firstBean
     * @param bean
     * @return
     */
    private int checkChooseDate(DateInfoBean firstBean, DateInfoBean bean) {
        if (null == firstBean || null == bean || isSameDay(firstBean, bean)) {
            return -1;
        }
        long firstLongTime = AppDateTools.getStringToDate(firstBean.dateToString());
        long selectLongTime = AppDateTools.getStringToDate(bean.dateToString());
        long diffLongTime = selectLongTime - firstLongTime;
        if (AppDateTools.diffTime2diffDay(Math.abs(diffLongTime)) > MAX_RANGE){
            return -1;
        }
        return selectLongTime - firstLongTime > 0 ? 1 : 0;
    }

    /**
     * 判断bean是否在start-end区间
     *
     * @param startBean
     * @param endBean
     * @param bean
     * @return
     */
    private boolean isInRange(DateInfoBean startBean, DateInfoBean endBean, DateInfoBean bean) {
        if (null == startBean || endBean == bean || null == bean) {
            return false;
        }
        return checkChooseDate(startBean, bean) == 1 && checkChooseDate(bean, endBean) == 1;
    }

    /**
     * 选择完起始、结束后更新UI
     *
     * @param startBean
     * @param endBean
     */
    private void refreshChooseUi(DateInfoBean startBean, DateInfoBean endBean) {
        for (DateInfoBean bean : mList) {
            if (bean.getType() == DateInfoBean.TYPE_DATE_NORMAL) {
                if (bean.isChooseDay()) {
                    if (isSameDay(startBean, bean)) {
                        //第一天
                        bean.setIntervalType(DateInfoBean.TYPE_INTERVAL_START);
                        mStartBean = startBean;
                    } else if (isSameDay(endBean, bean)) {
                        //最后一天
                        bean.setIntervalType(DateInfoBean.TYPE_INTERVAL_END);
                        mEndBean = endBean;
                    }
                } else if (isInRange(startBean, endBean, bean)) {
                    //中间天
                    bean.setChooseDay(true);
                    bean.setIntervalType(DateInfoBean.TYPE_INTERVAL_MIDDLE);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 清除选中状态并设置起点
     *
     * @param startDate
     */
    private void clearAndSetStartDate(DateInfoBean startDate) {
        mStartBean = null;
        mEndBean = null;
        for (DateInfoBean bean : mList) {
            if (bean.getType() == DateInfoBean.TYPE_DATE_NORMAL && bean.isChooseDay()) {
                //清除旧选中区域并设置第一个选中
                bean.setChooseDay(false);
            }
            if (isSameDay(startDate, bean)) {
                bean.setChooseDay(true);
                bean.setIntervalType(DateInfoBean.TYPE_INTERVAL_START);
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    public void onDestroy() {
        mAdapter = null;
    }

    public void setShowWeek(boolean showWeek) {
        isShowWeek = showWeek;
        init();
    }

    public DateInfoBean getStartBean() {
        return mStartBean;
    }

    public DateInfoBean getEndBean() {
        return mEndBean;
    }
}
