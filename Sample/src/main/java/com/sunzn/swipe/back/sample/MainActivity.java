package com.sunzn.swipe.back.sample;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.RadioGroup;

import com.sunzn.swipe.back.library.SwipeBackLayout;
import com.sunzn.swipe.back.library.app.SwipeBackActivity;

public class MainActivity extends SwipeBackActivity implements View.OnClickListener {

    private static final int VIBRATE_DURATION = 20;

    private int[] mBgColors;

    private static int mBgIndex = 0;

    private String mKeyTrackingMode;

    private RadioGroup mTrackingModeGroup;

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        changeActionBarColor();
        mKeyTrackingMode = getString(R.string.key_tracking_mode);
        mSwipeBackLayout = getSwipeBackLayout();

        mTrackingModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int edgeFlag;
                switch (checkedId) {
                    case R.id.mode_left:
                        edgeFlag = SwipeBackLayout.EDGE_LEFT;
                        break;
                    case R.id.mode_right:
                        edgeFlag = SwipeBackLayout.EDGE_RIGHT;
                        break;
                    case R.id.mode_bottom:
                        edgeFlag = SwipeBackLayout.EDGE_BOTTOM;
                        break;
                    default:
                        edgeFlag = SwipeBackLayout.EDGE_ALL;
                }
                mSwipeBackLayout.setEdgeTrackingEnabled(edgeFlag);
                saveTrackingMode(edgeFlag);
            }
        });
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {

            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                vibrate(VIBRATE_DURATION);
            }

            @Override
            public void onScrollOverThreshold() {
                vibrate(VIBRATE_DURATION);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreTrackingMode();
    }

    private void saveTrackingMode(int flag) {
        PreferenceUtils.setPrefInt(getApplicationContext(), mKeyTrackingMode, flag);
    }

    private void restoreTrackingMode() {
        int flag = PreferenceUtils.getPrefInt(getApplicationContext(), mKeyTrackingMode, SwipeBackLayout.EDGE_LEFT);
        mSwipeBackLayout.setEdgeTrackingEnabled(flag);
        switch (flag) {
            case SwipeBackLayout.EDGE_LEFT:
                mTrackingModeGroup.check(R.id.mode_left);
                break;
            case SwipeBackLayout.EDGE_RIGHT:
                mTrackingModeGroup.check(R.id.mode_right);
                break;
            case SwipeBackLayout.EDGE_BOTTOM:
                mTrackingModeGroup.check(R.id.mode_bottom);
                break;
            case SwipeBackLayout.EDGE_ALL:
                mTrackingModeGroup.check(R.id.mode_all);
                break;
        }
    }

    private void changeActionBarColor() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColors()[mBgIndex]));
        mBgIndex++;
        if (mBgIndex >= getColors().length) {
            mBgIndex = 0;
        }
    }

    private void findViews() {
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_finish).setOnClickListener(this);
        mTrackingModeGroup = (RadioGroup) findViewById(R.id.tracking_mode);
    }

    private int[] getColors() {
        if (mBgColors == null) {
            Resources resource = getResources();
            mBgColors = new int[]{
                    resource.getColor(R.color.androidColorA, null),
                    resource.getColor(R.color.androidColorB, null),
                    resource.getColor(R.color.androidColorC, null),
                    resource.getColor(R.color.androidColorD, null),
                    resource.getColor(R.color.androidColorE, null),
            };
        }
        return mBgColors;
    }

    private void vibrate(long duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {
                0, duration
        };
        vibrator.vibrate(pattern, -1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                break;
            case R.id.btn_finish:
                scrollToFinishActivity();
                break;
        }
    }

}
