package com.zidian.slidingcalendar;

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

import java.util.List;

public class SlidingCalendarView extends LinearLayout {
    //最大区间
    public static final int MAX_RANGE = 31;

    private Context mContext;
    private boolean isShowWeek;
    private RecyclerView mDateView;

    private List<DateInfoBean> mList;
    private DateAdpater mAdapter;

    private boolean isInAnim;
    //今天日期
    private DateInfoBean todayBean;

    public SlidingCalendarView(Context context) {
        this(context, null);
    }

    public SlidingCalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingCalendar);
        isShowWeek = typedArray.getBoolean(R.styleable.SlidingCalendar_showWeek, false);
        //TODO 自定义属性

        typedArray.recycle();

        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        if (isShowWeek) {
            addHeadView();
        }
    }

    /**
     * 添加星期
     */
    private void addHeadView() {
        LinearLayout weekView = new LinearLayout(mContext);
        //TODO 修改高度
        LayoutParams headParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
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
            //TODO 字体颜色
            tv.setTextColor(Color.BLACK);
            //TODO 字体大小
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
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
                            if (checkChooseDate(firstBean, bean)) {
                                bean.setChooseDay(true);
                                bean.setIntervalType(DateInfoBean.TYPE_INTERVAL_END);
                                refreshChooseUi(firstBean, bean);
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
    }

    /**
     * 初始化日期
     */
    private void initDate() {
        if (null == type || null == info) {
            return
        }

        selectType = type
        selectInfo = info

        val monthList = getDateList(selectInfo)

        //填充日期
        dayList.clear()
        monthList.forEachIndexed { _, monthInfo ->

                val dayInfo = DayInfo(monthInfo.year, monthInfo.month, 0, DayInfo.TYPE_DAY_TITLE)
            val monthNum = if (monthInfo.month < 10) "0${monthInfo.month}" else "${monthInfo.month}"
            dayInfo.groupName = "${monthInfo.year}年${monthNum}月"
            dayList.add(dayInfo)

            dayList.addAll(monthInfo.dayList)
        }

        //滑动到当前选择的位置
        val curPosition = calculationCurPosition(monthList, info)

        refreshData()

        val mouthIndex = curPosition.first
        val mouthRow = curPosition.second

        var countRow = 0
        for (i in 0 until mouthIndex) {
            countRow += if (monthList[i].dayList.size % 7 == 0) monthList[i].dayList.size / 7 else monthList[i].dayList.size / 7 + 1
        }

        val headerHeight = if (mouthIndex > 0) mouthIndex * RangeDateUtils.dp2px(context, 55) else 0f
        val countRowHeight = countRow * RangeDateUtils.dp2px(context, 55)
        val mouthRowHeight = mouthRow * RangeDateUtils.dp2px(context, 55)

        dateRecyclerView?.post {
            dateRecyclerView?.scrollBy(0, (headerHeight + countRowHeight + mouthRowHeight).toInt())
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
     * @param firstBean
     * @param bean
     * @return
     */
    private boolean checkChooseDate(DateInfoBean firstBean, DateInfoBean bean) {
        if (null == firstBean || null == bean) {
            return false;
        }
        //转为时间戳，时间戳差大于0且小于最大区间则在区间内
        long firstLongTime = AppDateTools.getStringToDate(firstBean.dateToString());
        long selectLongTime = AppDateTools.getStringToDate(bean.dateToString());
        long diffLongTime = selectLongTime - firstLongTime;
        if (diffLongTime <= 0) {
            return false;
        } else {
            return AppDateTools.diffTime2diffDay(diffLongTime) <= MAX_RANGE;
        }
    }

    /**
     * 判断bean是否在start-end区间
     * @param startBean
     * @param endBean
     * @param bean
     * @return
     */
    private boolean isInRange(DateInfoBean startBean, DateInfoBean endBean, DateInfoBean bean) {
        if (null == startBean || endBean == bean || null == bean) {
            return false;
        }
        return checkChooseDate(startBean, bean) &&  checkChooseDate(bean, endBean);
    }

    /**
     * 选择完起始、结束后更新UI
     * @param startBean
     * @param endBean
     */
    private void refreshChooseUi(DateInfoBean startBean, DateInfoBean endBean){
        for (DateInfoBean bean : mList){
            if (bean.getType() == DateInfoBean.TYPE_DATE_NORMAL && bean.isChooseDay()){
                if (isSameDay(startBean, bean)){
                    //第一天
                    bean.setIntervalType(DateInfoBean.TYPE_INTERVAL_START);
                }else if (isSameDay(endBean, bean)){
                    //最后一天
                    bean.setIntervalType(DateInfoBean.TYPE_INTERVAL_END);
                }else {
                    //中间天
                    bean.setIntervalType(DateInfoBean.TYPE_INTERVAL_MIDDLE);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 清除选中状态并设置起点
     * @param startDate
     */
    private void clearAndSetStartDate(DateInfoBean startDate) {
        for (DateInfoBean bean : mList){
            if (bean.getType() == DateInfoBean.TYPE_DATE_NORMAL &&  bean.isChooseDay()){
                //清除旧选中区域并设置第一个选中
                bean.setChooseDay(false);
                if (isSameDay(startDate, bean)){
                    bean.setChooseDay(true);
                    bean.setIntervalType(DateInfoBean.TYPE_INTERVAL_START);
                }
            }
        }
    }


}
