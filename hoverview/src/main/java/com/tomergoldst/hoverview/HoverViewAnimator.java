package com.tomergoldst.hoverview;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Animator for the tooltip view.
 */
public interface HoverViewAnimator {
    /**
     * Object animator for the tooltip view to pop-up.
     * @param view The tooltip view.
     * @param duration Duration for the animator.
     * @return ObjectAnimator
     */
    ValueAnimator popup(final View view, final long duration);

    /**
     * Object animator for the tooltip view to pop-out/hide.
     * @param view The tooltip view.
     * @param duration Duration for the animator.
     * @param animatorListenerAdapter The animator listener adapter to listen for animation event.
     * @return ObjectAnimator
     */
    ValueAnimator popout(final View view, final long duration, final AnimatorListenerAdapter animatorListenerAdapter);
}
