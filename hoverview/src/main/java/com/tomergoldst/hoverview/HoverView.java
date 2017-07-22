
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

import android.content.Context;
import android.support.annotation.IntDef;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tomer on 01/07/2016.
 *
 *
 */
public class HoverView {

    @IntDef({POSITION_ABOVE, POSITION_BELOW, POSITION_LEFT_TO, POSITION_RIGHT_TO})
    public @interface Position {}
    public static final int POSITION_ABOVE = 0;
    public static final int POSITION_BELOW = 1;
    public static final int POSITION_LEFT_TO = 3;
    public static final int POSITION_RIGHT_TO = 4;

    @IntDef({ALIGN_CENTER, ALIGN_LEFT, ALIGN_RIGHT})
    public @interface Align {}
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;

    private Context mContext;
    private View mAnchorView;
    private ViewGroup mRootViewGroup;
    private @Position int mPosition;
    private @Align int mAlign;
    private int mOffsetX;
    private int mOffsetY;
    private View mView;

    public HoverView(Builder builder){
        mContext = builder.mContext;
        mAnchorView = builder.mAnchorView;
        mRootViewGroup = builder.mRootViewGroup;
        mPosition = builder.mPosition;
        mAlign = builder.mAlign;
        mOffsetX = builder.mOffsetX;
        mOffsetX = builder.mOffsetX;
        mOffsetY = builder.mOffsetY;
        mView = builder.mView;
    }

    public Context getContext() {
        return mContext;
    }

    public View getAnchorView() {
        return mAnchorView;
    }

    public ViewGroup getRootView() {
        return mRootViewGroup;
    }

    public int getPosition() {
        return mPosition;
    }

    public int getAlign() {
        return mAlign;
    }

    public int getOffsetX() {
        return mOffsetX;
    }

    public int getOffsetY() {
        return mOffsetY;
    }

    public boolean positionedLeftTo(){
        return POSITION_LEFT_TO == mPosition;
    }

    public boolean positionedRightTo(){
        return POSITION_RIGHT_TO == mPosition;
    }

    public boolean positionedAbove(){
        return POSITION_ABOVE == mPosition;
    }

    public boolean positionedBelow(){
        return POSITION_BELOW == mPosition;
    }

    public boolean alignedCenter(){
        return ALIGN_CENTER == mAlign;
    }

    public boolean alignedLeft(){
        return ALIGN_LEFT == mAlign;
    }

    public boolean alignedRight(){
        return ALIGN_RIGHT == mAlign;
    }

    public void setPosition(@Position int position){
        mPosition = position;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }

    public static class Builder {
        private Context mContext;
        private View mAnchorView;
        private ViewGroup mRootViewGroup;
        private @Position int mPosition;
        private @Align int mAlign;
        private int mOffsetX;
        private int mOffsetY;
        private View mView;

        /**
         * @param context context
         * @param anchorView the view which near it we want to put the tip
         * @param root a class extends ViewGroup which the created tip view will be added to
         * @param view view to show
         * @param position  put the tip above / below / left to / right to
         */
        public Builder(Context context, View anchorView, ViewGroup root, View view, @Position int position){
            mContext = context;
            mAnchorView = anchorView;
            mRootViewGroup = root;
            mPosition = position;
            mAlign = ALIGN_CENTER;
            mOffsetX = 0;
            mOffsetY = 0;
            mView = view;
        }

        public Builder setPosition(@Position int position){
            mPosition = position;
            return this;
        }

        public Builder setAlign(@Align int align){
            mAlign = align;
            return this;
        }

        /**
         * @param offset offset to move the tip on x axis after tip was positioned
         * @return offset
         */
        public Builder setOffsetX(int offset){
            mOffsetX = offset;
            return this;
        }

        /**
         * @param offset offset to move the tip on y axis after tip was positioned
         * @return offset
         */
        public Builder setOffsetY(int offset){
            mOffsetY = offset;
            return this;
        }

        public Builder setView(View view) {
            mView = view;
            return this;
        }

        public HoverView build(){
            return new HoverView(this);
        }

    }
}
