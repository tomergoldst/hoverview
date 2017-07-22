/*
Copyright 2017 Tomer Goldstein

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.tomergoldst.hoverview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class HoverViewManager {

    private static final String TAG = HoverViewManager.class.getSimpleName();

    private static final int DEFAULT_ANIM_DURATION = 400;

    // Parameter for managing view creation or reuse
    private Map<Integer, HoverView> mHoverViewsMap = new HashMap<>();

    private int mAnimationDuration;
    private HoverViewListener mListener;

    public interface HoverViewListener {
        void onHoverViewDismissed(View view, int anchorViewId, boolean byUser);
    }

    public HoverViewManager(){
        mAnimationDuration = DEFAULT_ANIM_DURATION;
    }

    public HoverViewManager(HoverViewListener listener){
        mAnimationDuration = DEFAULT_ANIM_DURATION;
        mListener = listener;
    }

    public View show(HoverView hoverView) {
        View view = create(hoverView);
        if (view == null) {
            return null;
        }

        // animate view visibility
        AnimationUtils.popup(view, mAnimationDuration).start();

        return view;
    }

    private View create(HoverView hoverView) {

        if (hoverView.getAnchorView() == null) {
            Log.e(TAG, "Unable to create a hoverview, anchor view is null");
            return null;
        }

        if (hoverView.getRootView() == null) {
            Log.e(TAG, "Unable to create a hoverview, root layout is null");
            return null;
        }

        // only one hoverview is allowed near an anchor view at the same time, thus
        // reuse hoverview if already exist
        if (mHoverViewsMap.containsKey(hoverView.getAnchorView().getId())) {
            return mHoverViewsMap.get(hoverView.getAnchorView().getId()).getView();
        }

        // init view parameters
        hoverView.getView().setVisibility(View.INVISIBLE);

        // on RTL languages replace sides
        if (UiUtils.isRtl()) {
            switchHoverViewSidePosition(hoverView);
        }

        // add hoverview to root layout
        hoverView.getRootView().addView(hoverView.getView());

        // find where to position the hoverview
        Point point = ViewCoordinatesFinder.getCoordinates(hoverView);

        // move hoverview to correct position
        moveHoverViewToCorrectPosition(hoverView.getView(), point);

        // set dismiss on click
        hoverView.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(view, true);
            }
        });

        // bind hoverview with anchorView id
        int anchorViewId = hoverView.getAnchorView().getId();
        hoverView.getView().setTag(anchorViewId);

        // insert hoverview to map by 'anchorView' id
        mHoverViewsMap.put(anchorViewId, hoverView);

        return hoverView.getView();

    }

    private void moveHoverViewToCorrectPosition(View view, Point p) {
        Coordinates coordinates = new Coordinates(view);
        int translationX = p.x - coordinates.left;
        int translationY = p.y - coordinates.top;
        view.setTranslationX(!UiUtils.isRtl() ? translationX : -translationX);
        view.setTranslationY(translationY);
    }

    private void switchHoverViewSidePosition(HoverView hoverView) {
        if (hoverView.positionedLeftTo()) {
            hoverView.setPosition(HoverView.POSITION_RIGHT_TO);
        } else if (hoverView.positionedRightTo()) {
            hoverView.setPosition(HoverView.POSITION_LEFT_TO);
        }
    }

    public void setAnimationDuration(int duration){
        mAnimationDuration = duration;
    }

    public boolean dismiss(View view, boolean byUser) {
        if (view != null && isVisible(view)) {
            int key = (int) view.getTag();
            HoverView hoverView = mHoverViewsMap.get(key);
            mHoverViewsMap.remove(key);
            animateDismiss(hoverView, byUser);
            return true;
        }
        return false;
    }

    public boolean dismiss(Integer key) {
        return mHoverViewsMap.containsKey(key) && dismiss(mHoverViewsMap.get(key).getView(), false);
    }

    public View find(Integer key) {
        if (mHoverViewsMap.containsKey(key)) {
            return mHoverViewsMap.get(key).getView();
        }
        return null;
    }

    public boolean findAndDismiss(final View anchorView) {
        View view = find(anchorView.getId());
        return view != null && dismiss(view, false);
    }

    public void clear() {
        if (!mHoverViewsMap.isEmpty()) {
            for (Map.Entry<Integer, HoverView> entry : mHoverViewsMap.entrySet()) {
                dismiss(entry.getValue().getView(), false);
            }
        }
        mHoverViewsMap.clear();
    }

    private void animateDismiss(final HoverView hoverView, final boolean byUser) {
        AnimationUtils.popout(hoverView.getView(), mAnimationDuration, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                hoverView.getRootView().removeView(hoverView.getView());
                if (mListener != null){
                    mListener.onHoverViewDismissed(hoverView.getView(),
                            (Integer) hoverView.getView().getTag(), byUser);
                }
            }
        }).start();
    }

    public boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

}
