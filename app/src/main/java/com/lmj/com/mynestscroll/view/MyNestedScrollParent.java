package com.lmj.com.mynestscroll.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;

/**
 * Created by lmj on 2016/10/27 0027. on 下午 8:38
 * limengjie
 */
public class MyNestedScrollParent extends LinearLayout implements NestedScrollingParent {
    private String Tag = "MyNestedScrollParent";
    private View topView;
    private View scrollBar;
    private ViewGroup currentContentView;
    private ViewPager viewPager;
    private NestedScrollingParentHelper mParentHelper;
    private int imgHeight;
    private int barHeight;
    private int mTouchSlop = 0;
    private Context context;
    private int maxVelocity;
    private int minVelocity;
    private VelocityTracker mVelocityTracker;
    private OverScroller mScroller;
    private int yVelocity;
    private int mLastTouchY, mLastTouchX;
    private boolean scrollHorizontal, firstCheck = true;
    
    public MyNestedScrollParent(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    
    public MyNestedScrollParent(Context context) {
        super(context);
        this.context = context;
        init();
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        topView = getChildAt(0);
        scrollBar = getChildAt(1);
        viewPager = (ViewPager) getChildAt(2);
        topView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (imgHeight <= 0) {
                    imgHeight = topView.getMeasuredHeight();
                    Log.i(Tag, "imgHeight:" + imgHeight + ",tvHeight:" + barHeight);
                }
            }
        });
        scrollBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (barHeight <= 0) {
                    barHeight = scrollBar.getMeasuredHeight();
                    Log.i(Tag, "imgHeight:" + imgHeight + ",tvHeight:" + barHeight);
                }
            }
        });
//        ((TabLayout)scrollBar)
//                .setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//                currentContentView = (ViewGroup) viewPager.getChildAt(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int
// positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                    currentContentView = (ViewGroup) viewPager.getChildAt(position);
//            }
//
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        
        
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        currentContentView = (ViewGroup) viewPager.getChildAt(0);
    }
    
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }
    
    private void init() {
        mParentHelper = new NestedScrollingParentHelper(this);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        maxVelocity = configuration.getScaledMaximumFlingVelocity();
        minVelocity = configuration.getScaledMinimumFlingVelocity();
        mScroller = new OverScroller(context);
    }
    
    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }
    
    @Override
    public void onStopNestedScroll(View target) {
        mParentHelper.onStopNestedScroll(target);
    }
    
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int
            dyUnconsumed) {
    }
    
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        
        if ((showImg(dy) || hideImg(dy)) && !scrollHorizontal) {//如果父亲自己要滑动
            consumed[1] = dy;
            scrollBy(0, dy);
            consumed[0]=dx;
            Log.d("tedu", "onNestedPreScroll");
//            Log.i("onNestedPreScroll", "Parent滑动：" + dy);
        }
//        Log.i(Tag, "onNestedPreScroll--getScrollY():" + getScrollY() + ",dx:" + dx + ",dy:" + dy
//                + ",consumed:" + consumed);
    }
    
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }
    
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
//        CoordinatorLayout
//        RecyclerView
        
        return true;
    }
    
    @Override
    public int getNestedScrollAxes() {
        return 0;
    }
    
    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > imgHeight) {
            y = imgHeight;
        }
        
        super.scrollTo(x, y);
    }
    
    /**
     * 下拉的时候是否要向下滑动显示图片
     */
    public boolean showImg(int dy) {
        if (dy <= 0) {
            if (getScrollY() > 0) {//如果parent外框，还可以往上滑动
                if (currentContentView instanceof ScrollView && currentContentView.getScrollY()
                        == 0) {
                    return true;
                } else if (currentContentView instanceof RecyclerView) {
                    
                    if (!currentContentView.canScrollVertically(-1)) {
                        return true;
                    }
                }
                
            }
        }
        return false;
    }
    
    /**
     * 上拉的时候，是否要向上滑动，隐藏图片
     *
     * @return
     */
    public boolean hideImg(int dy) {
        if (dy >=0) {
            if (getScrollY() < imgHeight) {//如果parent外框，还可以往下滑动
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        
        return super.onTouchEvent(event);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        initVelocity(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstCheck = true;
                mLastTouchY = (int) (event.getRawY() + 0.5f);
                mLastTouchX = (int) (event.getRawX() + 0.5f);
                reset();
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) (event.getRawY() + 0.5f);
                int x = (int) (event.getRawX() + 0.5f);
                int dy = mLastTouchY - y;
                int dx = mLastTouchX - x;
                if (firstCheck) {
                    firstCheck = false;
                    scrollHorizontal = Math.abs(dx) > Math.abs(dy);
                }
                mLastTouchY = y;
                Log.e("tedu", "dispatchTouchEvent: "+showImg(dy) +"    "+hideImg(dy));
                Log.d("tedu", "getScrollY"+getScrollY());
                if ((showImg(dy) || hideImg(dy)) && !scrollHorizontal) {//如果父亲自己要滑动
                    Log.d("tedu", "scrollBy");
                    scrollBy(0, dy);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(300, maxVelocity);
                yVelocity = (int) mVelocityTracker.getYVelocity();
                Log.i(Tag, "getYVelocity:" + yVelocity + ",minVelocity:" + minVelocity);
                if (Math.abs(yVelocity) > minVelocity) {
//                    mScroller.fling(0,getScrollY(), 0, -yVelocity, 0, 0, 0, Math.max(0, nsc
// .getMeasuredHeight()+imgHeight), 0, nsc.getMeasuredHeight()/2);
                    if (!scrollHorizontal) {
                        mScroller.fling(0, getScrollY(), 0, -yVelocity , 0, 0, -50000, 5000);
                        postInvalidate();
        
                    }
                }
                recycleVelocity();
                break;
        }
    
        Log.i("tedu", "dispatch");
        return super.dispatchTouchEvent(event);
    }
    
    public void reset() {
        if (mScroller.computeScrollOffset()) {
            mScroller.abortAnimation();
        }
        yVelocity = 0;
    }
    
    public void initVelocity(MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        
    }
    
    public void recycleVelocity() {
        if (null != mVelocityTracker) {
            mVelocityTracker.recycle();
        }
        mVelocityTracker = null;
    }
    
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            postInvalidate();
            int dy = mScroller.getFinalY() - mScroller.getCurrY();
            if (yVelocity > 0) {
                if (getScrollY() >= imgHeight) {//此时top完全隐藏
                    
                    if (isChildScrollToTop()) {//如果子view已经滑动到顶部，这个时候父亲自己滑动
                        scrollBy(0, dy);
                    } else {
                        scrollContentView(dy);
                    }
                    
                    
                } else if (getScrollY() == 0) {//parent自己完全显示，交给子view滑动
                    if (!isChildScrollToTop()) {
                        scrollContentView(dy);
                    }
                } else {//此时top没有完全显示，让parent自己滑动
                    scrollBy(0, dy);
                }
            } else if (yVelocity < 0) {
                
                if (getScrollY() >= imgHeight) {//此时top完全隐藏
                    scrollContentView(dy);
                } else {
                    
                    scrollBy(0, dy);
                }
                
            }
            
        }
    }
    
    public void scrollContentView(int dy) {
        if (currentContentView instanceof ScrollView) {
            
            ((ScrollView) currentContentView).smoothScrollBy(0, dy);
        } else if (currentContentView instanceof RecyclerView) {
            ((RecyclerView) currentContentView).smoothScrollBy(0, dy);
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.height = getMeasuredHeight() - barHeight;
    }
    
    
    /**
     * 判断子view是否已经滑动到顶部
     */
    public boolean isChildScrollToTop() {
        if (currentContentView instanceof ScrollView && currentContentView.getScrollY() == 0) {
            return true;
        } else if (currentContentView instanceof RecyclerView) {
            
            if (!currentContentView.canScrollVertically(-1)) {
                return true;
            }
            
        }
        return false;
    }
    
    public void setCurrentContentView(ViewGroup view) {
        currentContentView = view;
    }
}
