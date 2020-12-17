package com.lyf.vibi.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.lyf.vibi.ui.refresh.OverView.RefreshState;
/**
 * 下拉刷新View
 */
public class RefreshLayout extends FrameLayout implements Refresh {
    private static final String TAG = RefreshLayout.class.getSimpleName();
    private OverView.RefreshState mState;
    private android.view.GestureDetector mGestureDetector;
    private AutoScroller mAutoScroller;
    private Refresh.HiRefreshListener mHiRefreshListener;
    protected OverView mOverView;
    private int mLastY;
    //刷新时是否禁止滚动
    private boolean disableRefreshScroll;

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RefreshLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        mGestureDetector = new android.view.GestureDetector(getContext(), gestureDetector);
        mAutoScroller = new AutoScroller();
    }

    @Override
    public void setDisableRefreshScroll(boolean disableRefreshScroll) {
        this.disableRefreshScroll = disableRefreshScroll;
    }

    @Override
    public void refreshFinished() {
        final View head = getChildAt(0);
        Log.i(this.getClass().getSimpleName(), "refreshFinished head-bottom:" + head.getBottom());
        mOverView.onFinish();
        mOverView.setState(RefreshState.STATE_INIT);
        final int bottom = head.getBottom();
        if (bottom > 0) {
            //下over pull 200，height 100
             //  bottom  =100 ,height 100
            recover(bottom);
        }
        mState = OverView.RefreshState.STATE_INIT;
    }

    @Override
    public void setRefreshListener(Refresh.HiRefreshListener hiRefreshListener) {
        mHiRefreshListener = hiRefreshListener;
    }

    /**
     * 设置下拉刷新的视图
     *
     * @param overView
     */
    @Override
    public void setRefreshOverView(OverView overView) {
        if (this.mOverView != null) {
            removeView(mOverView);
        }
        this.mOverView = overView;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mOverView, 0, params);
    }

    RefreshGestureDetector gestureDetector = new RefreshGestureDetector() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float disX, float disY) {
            if (Math.abs(disX) > Math.abs(disY) || mHiRefreshListener != null && !mHiRefreshListener.enableRefresh()) {
                //横向滑动，或刷新被禁止则不处理
                return false;
            }
            if (disableRefreshScroll && mState == RefreshState.STATE_REFRESH) {//刷新时是否禁止滑动
                return true;
            }

            View head = getChildAt(0);
            View child = ScrollUtil.findScrollableChild(RefreshLayout.this);
            if (ScrollUtil.childScrolled(child)) {
                //如果列表发生了滚动则不处理
                return false;
            }
            //没有刷新或没有达到可以刷新的距离，且头部已经划出或下拉
            if ((mState != RefreshState.STATE_REFRESH || head.getBottom() <= mOverView.mPullRefreshHeight) && (head.getBottom() > 0 || disY <= 0.0F)) {
                //还在滑动中
                if (mState != RefreshState.STATE_OVER_RELEASE) {
                    int speed;
                    //阻尼计算
                    if (child.getTop() < mOverView.mPullRefreshHeight) {
                        speed = (int) (mLastY / mOverView.minDamp);
                    } else {
                        speed = (int) (mLastY / mOverView.maxDamp);
                    }
                    //如果是正在刷新状态，则不允许在滑动的时候改变状态
                    boolean bool = moveDown(speed, true);
                    mLastY = (int) (-disY);
                    return bool;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //事件分发处理
        if (!mAutoScroller.isFinished()) {
            return false;
        }

        View head = getChildAt(0);
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL
                || ev.getAction() == MotionEvent.ACTION_POINTER_INDEX_MASK) {//松开手
            if (head.getBottom() > 0) {
                if (mState != RefreshState.STATE_REFRESH) {//非正在刷新
                    recover(head.getBottom());
                    return false;
                }
            }
            mLastY = 0;
        }
        boolean consumed = mGestureDetector.onTouchEvent(ev);
        Log.i(TAG, "gesture consumed：" + consumed);
        if ((consumed || (mState != RefreshState.STATE_INIT && mState != RefreshState.STATE_REFRESH)) && head.getBottom() != 0) {
            // ev.setAction(MotionEvent.ACTION_CANCEL);//让父类接受不到真实的事件
            return super.dispatchTouchEvent(ev);
        }

        if (consumed) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //定义head和child的排列位置
        View head = getChildAt(0);
        View child = getChildAt(1);
        if (head != null && child != null) {
            Log.i(TAG, "onLayout head-height:" + head.getMeasuredHeight());
            int childTop = child.getTop();
            if (mState == RefreshState.STATE_REFRESH) {
                head.layout(0, mOverView.mPullRefreshHeight - head.getMeasuredHeight(), right, mOverView.mPullRefreshHeight);
                child.layout(0, mOverView.mPullRefreshHeight, right, mOverView.mPullRefreshHeight + child.getMeasuredHeight());
            } else {
                //left,top,right,bottom
                head.layout(0, childTop - head.getMeasuredHeight(), right, childTop);
                child.layout(0, childTop, right, childTop + child.getMeasuredHeight());
            }

            View other;
            //让HiRefreshLayout节点下两个以上的child能够不跟随手势移动以实现一些特殊效果，如悬浮的效果
            for (int i = 2; i < getChildCount(); ++i) {
                other = getChildAt(i);
                other.layout(0, top, right, bottom);
            }
            Log.i(TAG, "onLayout head-bottom:" + head.getBottom());
        }
    }

    private void recover(int dis) {//dis =200  200-100
        if (mHiRefreshListener != null && dis > mOverView.mPullRefreshHeight) {
            mAutoScroller.recover(dis - mOverView.mPullRefreshHeight);
            mState = RefreshState.STATE_OVER_RELEASE;
        } else {
            mAutoScroller.recover(dis);
        }
    }

    /**
     * 根据偏移量移动header与child
     *
     * @param offsetY 偏移量
     * @param nonAuto 是否非自动滚动触发
     * @return
     */
    private boolean moveDown(int offsetY, boolean nonAuto) {
        Log.i("111", "changeState:" + nonAuto);
        View head = getChildAt(0);
        View child = getChildAt(1);
        int childTop = child.getTop() + offsetY;

        Log.i("-----", "moveDown head-bottom:" + head.getBottom() + ",child.getTop():" + child.getTop() + ",offsetY:" + offsetY);
        if (childTop <= 0) {//异常情况的补充
            Log.i(TAG, "childTop<=0,mState" + mState);
            offsetY = -child.getTop();
            //移动head与child的位置，到原始位置
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (mState != RefreshState.STATE_REFRESH) {
                mState = RefreshState.STATE_INIT;
            }
        } else if (mState == RefreshState.STATE_REFRESH && childTop > mOverView.mPullRefreshHeight) {
            //如果正在下拉刷新中，禁止继续下拉
            return false;
        } else if (childTop <= mOverView.mPullRefreshHeight) {//还没超出设定的刷新距离
            if (mOverView.getState() != RefreshState.STATE_VISIBLE && nonAuto) {//头部开始显示
                mOverView.onVisible();
                mOverView.setState(RefreshState.STATE_VISIBLE);
                mState = RefreshState.STATE_VISIBLE;
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (childTop == mOverView.mPullRefreshHeight && mState == RefreshState.STATE_OVER_RELEASE) {
                Log.i(TAG, "refresh，childTop：" + childTop);
                refresh();
            }
        } else {
            if (mOverView.getState() != RefreshState.STATE_OVER && nonAuto) {
                //超出刷新位置
                mOverView.onOver();
                mOverView.setState(RefreshState.STATE_OVER);
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
        }
        if (mOverView != null) {
            mOverView.onScroll(head.getBottom(), mOverView.mPullRefreshHeight);
        }
        return true;
    }


    /**
     * 刷新
     */
    private void refresh() {
        if (mHiRefreshListener != null) {
            mState = RefreshState.STATE_REFRESH;
            mOverView.onRefresh();
            mOverView.setState(RefreshState.STATE_REFRESH);
            mHiRefreshListener.onRefresh();
        }
    }


    /**
     * 借助Scroller实现视图的自动滚动
     * https://juejin.im/post/5c7f4f0351882562ed516ab6
     */
    private class AutoScroller implements Runnable {
        private Scroller mScroller;
        private int mLastY;
        private boolean mIsFinished;

        AutoScroller() {
            mScroller = new Scroller(getContext(), new LinearInterpolator());
            mIsFinished = true;
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {//还未滚动完成
                moveDown(mLastY - mScroller.getCurrY(), false);
                mLastY = mScroller.getCurrY();
                post(this);
            } else {
                removeCallbacks(this);
                mIsFinished = true;
            }
        }

        void recover(int dis) {
            if (dis <= 0) {
                return;
            }
            removeCallbacks(this);
            mLastY = 0;
            mIsFinished = false;
            mScroller.startScroll(0, 0, 0, dis, 300);
            post(this);
        }

        boolean isFinished() {
            return mIsFinished;
        }

    }
}