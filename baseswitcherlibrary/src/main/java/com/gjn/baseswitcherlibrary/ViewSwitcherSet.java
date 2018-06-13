package com.gjn.baseswitcherlibrary;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjn on 2018/6/12.
 */

public abstract class ViewSwitcherSet<T> {
    private static final String TAG = "ViewSwitcherSet";
    public static final int ANIMAT_TIME = 1000;
    public static final int LOOP_TIME = 2000;

    private Activity activity;
    private ViewSwitcher viewSwitcher;
    private int viewLayoutId;
    private List<T> data;
    private int marker = 0;
    private boolean isAnimation = true;
    private AnimationSet InAnimationSet;
    private AnimationSet OutAnimationSet;

    private boolean isLoop = false;
    private int animationTime = ANIMAT_TIME;
    private int delayTime = LOOP_TIME;
    private Handler handler = new Handler();
    private Runnable loop = new Runnable() {
        @Override
        public void run() {
            nextView();
            if (isAnimation) {
                handler.postDelayed(loop, delayTime * 2);
            }else {
                handler.postDelayed(loop, delayTime);
            }
        }
    };

    public ViewSwitcherSet(Activity activity, ViewSwitcher viewSwitcher, int viewLayoutId, List<T> data) {
        this.activity = activity;
        this.viewSwitcher = viewSwitcher;
        this.viewLayoutId = viewLayoutId;
        this.data = data == null ? new ArrayList<T>() : data;
    }

    public ViewSwitcherSet(Activity activity, ViewSwitcher viewSwitcher, int viewLayoutId, List<T> data,
                           AnimationSet inAnimationSet, AnimationSet outAnimationSet) {
        this.activity = activity;
        this.viewSwitcher = viewSwitcher;
        this.viewLayoutId = viewLayoutId;
        this.data = data == null ? new ArrayList<T>() : data;
        InAnimationSet = inAnimationSet;
        OutAnimationSet = outAnimationSet;
    }

    public void start() {
        stop();
        isLoop = true;
        handler.postDelayed(loop, delayTime);
    }

    public void stop() {
        isLoop = false;
        handler.removeCallbacks(loop);
    }

    public int getMarker() {
        return marker;
    }

    public boolean isAnimation() {
        return isAnimation;
    }

    public ViewSwitcherSet setAnimation(boolean animation) {
        isAnimation = animation;
        return this;
    }

    public ViewSwitcherSet setInAnimationSet(AnimationSet inAnimationSet) {
        InAnimationSet = inAnimationSet;
        if (viewSwitcher != null) {
            viewSwitcher.setInAnimation(InAnimationSet);
        }
        return this;
    }

    public ViewSwitcherSet setOutAnimationSet(AnimationSet outAnimationSet) {
        OutAnimationSet = outAnimationSet;
        if (viewSwitcher != null) {
            viewSwitcher.setOutAnimation(OutAnimationSet);
        }
        return this;
    }

    public boolean isLoop() {
        return isLoop;
    }

    public ViewSwitcherSet setLoop(boolean loop) {
        isLoop = loop;
        return this;
    }

    public ViewSwitcherSet setData(List<T> data) {
        this.data = data;
        return this;
    }

    public ViewSwitcherSet setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
        if (InAnimationSet != null) {
            InAnimationSet.setDuration(animationTime);
        }
        if (OutAnimationSet != null) {
            OutAnimationSet.setDuration(animationTime);
        }
        return this;
    }

    public ViewSwitcherSet setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public void updataView(List<T> data) {
        if (this.data.size() == 0) {
            setData(data).create();
        } else {
            setData(data).updataView();
        }
    }

    public void updataView() {
        if (isAnimation) {
            viewSwitcher.setInAnimation(InAnimationSet);
            viewSwitcher.setOutAnimation(OutAnimationSet);
        } else {
            viewSwitcher.setInAnimation(null);
            viewSwitcher.setOutAnimation(null);
        }
        if (isLoop) {
            start();
        } else {
            stop();
        }
    }

    public void create() {
        if (data.size() == 0) {
            Log.e(TAG, "data is null.");
            return;
        }
        if (viewSwitcher == null) {
            Log.e(TAG, "viewSwitcher is null.");
            return;
        }
        viewSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                View view = LayoutInflater.from(activity).inflate(viewLayoutId, null);
                bindData(activity, view, 0, data.get(0));
                return view;
            }
        });
        if (InAnimationSet == null) {
            InAnimationSet = SwitcherAnimationSet.defaultInAnimationSet(viewSwitcher, animationTime);
        }
        if (OutAnimationSet == null) {
            OutAnimationSet = SwitcherAnimationSet.defaultOutAnimationSet(viewSwitcher, animationTime);
        }
        updataView();
    }

    public void nextView() {
        marker = ++marker % data.size();
        upData();
        viewSwitcher.showNext();
    }

    public void previousView() {
        if (marker <= 0) {
            marker = data.size() - 1;
        } else {
            marker = --marker % data.size();
        }
        upData();
        viewSwitcher.showPrevious();
    }

    private void upData() {
        View view = viewSwitcher.getNextView();
        bindData(activity, view, marker, data.get(marker));
    }

    protected abstract void bindData(Activity activity, View view, int marker, T t);
}
