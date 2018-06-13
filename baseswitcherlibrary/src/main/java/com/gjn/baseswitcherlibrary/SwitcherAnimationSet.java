package com.gjn.baseswitcherlibrary;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

/**
 * Created by gjn on 2018/6/13.
 */

public class SwitcherAnimationSet {

    public static AnimationSet defaultInAnimationSet(View view, int animationTime){
        int h = view.getHeight();
        if (h <= 0) {
            view.measure(0, 0);
            h = view.getMeasuredHeight();
        }
        AnimationSet InAnimationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, h, Animation.ABSOLUTE, 0);
        InAnimationSet.addAnimation(alphaAnimation);
        InAnimationSet.addAnimation(translateAnimation);
        InAnimationSet.setDuration(animationTime);
        return InAnimationSet;
    }

    public static AnimationSet defaultOutAnimationSet(View view, int animationTime){
        int h = view.getHeight();
        if (h <= 0) {
            view.measure(0, 0);
            h = view.getMeasuredHeight();
        }
        AnimationSet OutAnimationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -h);
        OutAnimationSet.addAnimation(alphaAnimation);
        OutAnimationSet.addAnimation(translateAnimation);
        OutAnimationSet.setDuration(animationTime);
        return OutAnimationSet;
    }
}
