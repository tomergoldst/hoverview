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

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tomer on 01/07/2016.
 */
class ViewCoordinatesFinder {

    /**
     * return the top left coordinates for positioning the view
     * 
     * @param hoverview - hoverview object
     * @return point
     */
    static Point getCoordinates(HoverView hoverview) {
        Point point = new Point();
        final Coordinates anchorViewCoordinates = new Coordinates(hoverview.getAnchorView());
        final Coordinates rootCoordinates = new Coordinates(hoverview.getRootView());

        View view = hoverview.getView();
        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        switch (hoverview.getPosition()) {
            case HoverView.POSITION_ABOVE:
                point = getPositionAbove(view, hoverview,
                        anchorViewCoordinates, rootCoordinates);
                break;
            case HoverView.POSITION_BELOW:
                point = getPositionBelow(view, hoverview,
                        anchorViewCoordinates, rootCoordinates);
                break;
            case HoverView.POSITION_LEFT_TO:
                point = getPositionLeftTo(view, hoverview,
                        anchorViewCoordinates, rootCoordinates);
                break;
            case HoverView.POSITION_RIGHT_TO:
                point = getPositionRightTo(view, hoverview,
                        anchorViewCoordinates, rootCoordinates);
                break;
        }

        // add user defined offset values
        point.x += UiUtils.isRtl() ? -hoverview.getOffsetX() : hoverview.getOffsetX();
        point.y += hoverview.getOffsetY();

        // coordinates retrieved are relative to 0,0 of the root layout
        // added view to root is subject to root padding
        // we need to subtract the top and left padding of root from coordinates. to adjust
        // top left view coordinates
        point.x -= hoverview.getRootView().getPaddingLeft();
        point.y -= hoverview.getRootView().getPaddingTop();

        return point;

    }

    private static Point getPositionRightTo(View view, HoverView hoverView, Coordinates anchorViewCoordinates, Coordinates rootLocation) {
        Point point = new Point();
        point.x = anchorViewCoordinates.right;
        AdjustRightToOutOfBounds(view, hoverView.getRootView(), point, anchorViewCoordinates, rootLocation);
        point.y = anchorViewCoordinates.top + getYCenteringOffset(view, hoverView);
        return point;
    }

    private static Point getPositionLeftTo(View view, HoverView hoverView, Coordinates anchorViewCoordinates, Coordinates rootLocation) {
        Point point = new Point();
        point.x = anchorViewCoordinates.left - view.getMeasuredWidth();
        AdjustLeftToOutOfBounds(view, hoverView.getRootView(), point, anchorViewCoordinates, rootLocation);
        point.y = anchorViewCoordinates.top + getYCenteringOffset(view, hoverView);
        return point;
    }

    private static Point getPositionBelow(View view, HoverView hoverView, Coordinates anchorViewCoordinates, Coordinates rootLocation) {
        Point point = new Point();
        point.x = anchorViewCoordinates.left + getXOffset(view, hoverView);
        if (hoverView.alignedCenter()) {
            AdjustHorizontalCenteredOutOfBounds(view, hoverView.getRootView(), point, rootLocation);
        } else if (hoverView.alignedLeft()){
            AdjustHorizontalLeftAlignmentOutOfBounds(view, hoverView.getRootView(), point, anchorViewCoordinates, rootLocation);
        } else if (hoverView.alignedRight()){
            AdjustHorizontalRightAlignmentOutOfBounds(view, hoverView.getRootView(), point, anchorViewCoordinates, rootLocation);
        }
        point.y = anchorViewCoordinates.bottom;
        return point;
    }

    private static Point getPositionAbove(View view, HoverView hoverView,
                                          Coordinates anchorViewCoordinates, Coordinates rootLocation) {
        Point point = new Point();
        point.x = anchorViewCoordinates.left + getXOffset(view, hoverView);
        if (hoverView.alignedCenter()) {
            AdjustHorizontalCenteredOutOfBounds(view, hoverView.getRootView(), point, rootLocation);
        } else if (hoverView.alignedLeft()){
            AdjustHorizontalLeftAlignmentOutOfBounds(view, hoverView.getRootView(), point, anchorViewCoordinates, rootLocation);
        } else if (hoverView.alignedRight()){
            AdjustHorizontalRightAlignmentOutOfBounds(view, hoverView.getRootView(), point, anchorViewCoordinates, rootLocation);
        }
        point.y = anchorViewCoordinates.top - view.getMeasuredHeight();
        return point;
    }

    private static void AdjustRightToOutOfBounds(View view, ViewGroup root, Point point, Coordinates anchorViewCoordinates, Coordinates rootLocation) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int availableSpace = rootLocation.right - root.getPaddingRight() - anchorViewCoordinates.right;
        if (point.x + view.getMeasuredWidth() > rootLocation.right - root.getPaddingRight()){
            params.width = availableSpace;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(params);
            measureViewWithFixedWidth(view, params.width);
        }
    }

    private static void AdjustLeftToOutOfBounds(View view, ViewGroup root, Point point, Coordinates anchorViewCoordinates, Coordinates rootLocation) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int rootLeft = rootLocation.left + root.getPaddingLeft();
        if (point.x < rootLeft){
            int availableSpace = anchorViewCoordinates.left - rootLeft;
            point.x = rootLeft;
            params.width = availableSpace;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(params);
            measureViewWithFixedWidth(view, params.width);
        }
    }

    private static void AdjustHorizontalRightAlignmentOutOfBounds(View view, ViewGroup root,
                                                                  Point point, Coordinates anchorViewCoordinates,
                                                                  Coordinates rootLocation) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int rootLeft = rootLocation.left + root.getPaddingLeft();
        if (point.x < rootLeft){
            int availableSpace = anchorViewCoordinates.right - rootLeft;
            point.x = rootLeft;
            params.width = availableSpace;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(params);
            measureViewWithFixedWidth(view, params.width);
        }
    }

    private static void AdjustHorizontalLeftAlignmentOutOfBounds(View view, ViewGroup root,
                                                                 Point point, Coordinates anchorViewCoordinates,
                                                                 Coordinates rootLocation) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int rootRight = rootLocation.right - root.getPaddingRight();
        if (point.x + view.getMeasuredWidth() > rootRight){
            params.width = rootRight - anchorViewCoordinates.left;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(params);
            measureViewWithFixedWidth(view, params.width);
        }
    }

    private static void AdjustHorizontalCenteredOutOfBounds(View view, ViewGroup root,
                                                            Point point, Coordinates rootLocation) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int rootWidth = root.getWidth() - root.getPaddingLeft() - root.getPaddingRight();
        if (view.getMeasuredWidth() > rootWidth) {
            point.x = rootLocation.left + root.getPaddingLeft();
            params.width = rootWidth;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(params);
            measureViewWithFixedWidth(view, rootWidth);
        }
    }


    private static void measureViewWithFixedWidth(View view, int width) {
        view.measure(View.MeasureSpec.makeMeasureSpec(width,
                View.MeasureSpec.EXACTLY), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * calculate the amount of movement need to be taken inorder to align view
     * on X axis according to "align" parameter
     * @return int
     */
    private static int getXOffset(View view, HoverView hoverView) {
        int offset;

        switch (hoverView.getAlign()) {
            case HoverView.ALIGN_CENTER:
                offset = ((hoverView.getAnchorView().getWidth() - view.getMeasuredWidth()) / 2);
                break;
            case HoverView.ALIGN_LEFT:
                offset = 0;
                break;
            case HoverView.ALIGN_RIGHT:
                offset = hoverView.getAnchorView().getWidth() - view.getMeasuredWidth();
                break;
            default:
                offset = 0;
                break;
        }

        return offset;
    }

    /**
     * calculate the amount of movement need to be taken inorder to center view
     * on Y axis
     * @return int
     */
    private static int getYCenteringOffset(View view, HoverView hoverView) {
        return (hoverView.getAnchorView().getHeight() - view.getMeasuredHeight()) / 2;
    }

}
