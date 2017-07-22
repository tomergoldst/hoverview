# hoverview
Add popup view near any other view with ease

## Instructions

1. Add a dependency to your app build.gradle
```groovy
dependencies {
    compile 'com.tomergoldst.android:hoverview:1.0.0'
}
```

2. Create an HoverViewManager object
```java
HoverViewManager mHoverViewManager;
mHoverViewManager = new HoverViewManager();
```

3. Create your custom view to show. In following example we have created a TextView layout named `hover_view.xml` and customized it like this:
```xml
<?xml version="1.0" encoding="utf-8"?>
<TextView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Hoverview"
    android:textColor="@android:color/white"
    android:background="@drawable/box_shape_drawable"
    tools:ignore="HardcodedText">
</TextView>
```
Important: Do not use layout_margin
  
4. Use the HoverView Builder to construct your hoverview
```java
View view = LayoutInflater.from(this).inflate(R.layout.hover_view, mRootLayout, false);
HoverView.Builder builder = new HoverView.Builder(this, mTextView, mRootLayout, view, HoverView.POSITION_ABOVE);
```
'mTextView' here is the view which near it the hover view will be shown and 'mRootLayout' is the layout where the hoverview will be added to.
The root layout must be of RelativeLayout, FrameLayout or similar. LinearLayout won't work but you can always wrap your LinearLayout
with another layout. Prefer to pass in a layout which is higher in the xml tree as this will give the
hoverview more visible space.

5. Use HoverViewManager to show the tip

IMPORTANT: This must be called after the layout has been drawn
You can override the 'onWindowFocusChanged()' of an Activity and show there, Start a delayed runnable from onStart() , React to user action or any other method that works for you
```java
mHoverViewManager.show(builder.build());
```

Each hoverview is dismissable by clicking on it, if you want to dismiss an hoverview from code there are a few options, The most simple way is to do the following
```java
mHoverViewManager.findAndDismiss(mTextView);
```
Where 'mTextView' is the same view we asked to position an hoverview near it

If you want to react when tip has been dismissed, Implement HoverViewManager.HoverViewListener interface and use appropriate HoverViewManager constructor
```java
public class MainActivity extends AppCompatActivity implements ToolTipsManager.HoverViewListener
.
.
@Override
protected void onCreate(Bundle savedInstanceState) {
    mHoverViewManager = new HoverViewManager(this);
}
.
.
@Override
public void onHoverViewDismissed(View view, int anchorViewId, boolean byUser) {
    Log.d(TAG, "hoverview near anchor view " + anchorViewId + " dismissed");

    if (anchorViewId == R.id.text_view) {
        // Do something when an hoverview near view with id "R.id.text_view" has been dismissed
    }
}
```

### License
```
Copyright 2016 Tomer Goldstein

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```  
