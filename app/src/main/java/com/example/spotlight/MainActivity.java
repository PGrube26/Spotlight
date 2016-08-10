package com.example.spotlight;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wooplr.spotlight.SpotlightView;
import com.wooplr.spotlight.prefs.ViewShowStore;
import com.wooplr.spotlight.utils.SpotlightListener;
import com.wooplr.spotlight.utils.Utils;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;
    private static final int INTRO_CARD = 1;
    private boolean isRevealEnabled = true;

    @BindView(R.id.switchAnimation)
    TextView switchAnimation;
    @BindView(R.id.reset)
    TextView reset;
    @BindView(R.id.resetAndPlay)
    TextView resetAndPlay;
    @BindView(R.id.changePosAndPlay)
    TextView changePosAndPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Fab Clicked!", Toast.LENGTH_LONG).show();
            }
        });

        switchAnimation.setOnClickListener(this);
        reset.setOnClickListener(this);
        resetAndPlay.setOnClickListener(this);
        changePosAndPlay.setOnClickListener(this);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showIntro(fab, INTRO_CARD);
            }
        }, 1000);
    }

    @Override
    public void onClick(View view) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;

        switch (view.getId()) {

            case R.id.switchAnimation:
                if (isRevealEnabled) {
                    switchAnimation.setText("Switch to Reveal");
                    isRevealEnabled = false;
                } else {
                    switchAnimation.setText("Switch to Fadein");
                    isRevealEnabled = true;
                }
                ViewShowStore.resetPref(this);
                break;
            case R.id.reset:
                ViewShowStore.resetPref(this);
                break;
            case R.id.resetAndPlay:
                ViewShowStore.resetPref(this);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showIntro(fab, INTRO_CARD);
                    }
                }, 400);
                break;
            case R.id.changePosAndPlay:
                ViewShowStore.resetPref(this);
                Random r = new Random();
                int right = r.nextInt((screenWidth - Utils.dpToPx(16)) - 16) + 16;
                int bottom = r.nextInt((screenHeight - Utils.dpToPx(16)) - 16) + 16;
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                params.setMargins(Utils.dpToPx(16), Utils.dpToPx(16), right, bottom);
                fab.setLayoutParams(params);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showIntro(fab, INTRO_CARD);
                    }
                }, 200);
                break;
        }
    }

    private void showIntro(View view, int usageId) {
        new SpotlightView.Builder(this)
                .introAnimationDuration(400)
                .enableRevealAnimation(isRevealEnabled)
                .performClick(true)
                .maskColor(Color.parseColor("#dc000000"))
                .target(view)
                .lineAnimDuration(200)
                .setDescriptionView(R.layout.sample_description_view)
                .lineAndArcColor(Color.parseColor("#e1e1e1"))
                .dismissOnTouch(true)
                .enableDismissAfterShown(true)
                .targetPadding(36)
                .setListener(new SpotlightListener() {
                    @Override
                    public void onSpotlightDismissed(int spotlightViewId) {
                        Toast.makeText(MainActivity.this, "Spotlight " + spotlightViewId + " was dismissed.", Toast.LENGTH_LONG).show();
                    }
                })
                .showCircle(usageId);
    }
}

