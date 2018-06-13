package com.gjn.baseswitcherlibrary;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjn on 2018/6/13.
 */

public class TextSwitcherSet {
    private static final String TAG = "TextSwitcherSet";
    public static final int ANIMAT_TIME = 1000;
    public static final int LOOP_TIME = 2000;

    private Activity activity;
    private TextSwitcher textSwitcher;
    private int textViewLayoutId = -1;
    private List<String> data;
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
            } else {
                handler.postDelayed(loop, delayTime);
            }
        }
    };

    public TextSwitcherSet(Activity activity, TextSwitcher textSwitcher, List<String> data) {
        this.activity = activity;
        this.textSwitcher = textSwitcher;
        this.data = data == null ? new ArrayList<String>() : data;
    }

    public TextSwitcherSet(Activity activity, TextSwitcher textSwitcher, int textViewLayoutId, List<String> data) {
        this.activity = activity;
        this.textSwitcher = textSwitcher;
        this.textViewLayoutId = textViewLayoutId;
        this.data = data == null ? new ArrayList<String>() : data;
    }

    public TextSwitcherSet(Activity activity, TextSwitcher textSwitcher, List<String> data,
                           AnimationSet inAnimationSet, AnimationSet outAnimationSet) {
        this.activity = activity;
        this.textSwitcher = textSwitcher;
        this.data = data == null ? new ArrayList<String>() : data;
        InAnimationSet = inAnimationSet;
        OutAnimationSet = outAnimationSet;
    }

    public TextSwitcherSet(Activity activity, TextSwitcher textSwitcher, int textViewLayoutId, List<String> data,
                           AnimationSet inAnimationSet, AnimationSet outAnimationSet) {
        this.activity = activity;
        this.textSwitcher = textSwitcher;
        this.textViewLayoutId = textViewLayoutId;
        this.data = data == null ? new ArrayList<String>() : data;
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

    public TextSwitcherSet setAnimation(boolean animation) {
        isAnimation = animation;
        return this;
    }

    public boolean isLoop() {
        return isLoop;
    }

    public TextSwitcherSet setLoop(boolean loop) {
        isLoop = loop;
        return this;
    }

    public TextSwitcherSet setData(List<String> data) {
        this.data = data;
        return this;
    }

    public TextSwitcherSet setInAnimationSet(AnimationSet inAnimationSet) {
        InAnimationSet = inAnimationSet;
        if (textSwitcher != null) {
            textSwitcher.setInAnimation(InAnimationSet);
        }
        return this;
    }

    public TextSwitcherSet setOutAnimationSet(AnimationSet outAnimationSet) {
        OutAnimationSet = outAnimationSet;
        if (textSwitcher != null) {
            textSwitcher.setOutAnimation(OutAnimationSet);
        }
        return this;
    }

    public TextSwitcherSet setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
        if (InAnimationSet != null) {
            InAnimationSet.setDuration(animationTime);
        }
        if (OutAnimationSet != null) {
            OutAnimationSet.setDuration(animationTime);
        }
        return this;
    }

    public TextSwitcherSet setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public void updataView(List<String> data) {
        if (this.data.size() == 0) {
            setData(data).create();
        } else {
            setData(data).updataView();
        }
    }

    public void updataView() {
        if (isAnimation) {
            textSwitcher.setInAnimation(InAnimationSet);
            textSwitcher.setOutAnimation(OutAnimationSet);
        } else {
            textSwitcher.setInAnimation(null);
            textSwitcher.setOutAnimation(null);
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
        if (textSwitcher == null) {
            Log.e(TAG, "textSwitcher is null.");
            return;
        }
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                if (textViewLayoutId == -1) {
                    return new TextView(activity);
                }
                return LayoutInflater.from(activity).inflate(textViewLayoutId, null);
            }
        });
        textSwitcher.setText(data.get(0));
        if (InAnimationSet == null) {
            InAnimationSet = SwitcherAnimationSet.defaultInAnimationSet(textSwitcher, animationTime);
        }
        if (OutAnimationSet == null) {
            OutAnimationSet = SwitcherAnimationSet.defaultOutAnimationSet(textSwitcher, animationTime);
        }
        updataView();
    }

    public void nextView() {
        marker = ++marker % data.size();
        textSwitcher.setText(data.get(marker));
    }

    public void previousView() {
        if (marker <= 0) {
            marker = data.size() - 1;
        } else {
            marker = --marker % data.size();
        }
        textSwitcher.setText(data.get(marker));
    }
}
