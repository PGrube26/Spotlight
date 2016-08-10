package com.wooplr.spotlight;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.wooplr.spotlight.prefs.ViewShowStore;
import com.wooplr.spotlight.shape.Circle;
import com.wooplr.spotlight.shape.NormalLineAnimDrawable;
import com.wooplr.spotlight.shape.Rectangle;
import com.wooplr.spotlight.shape.TargetShape;
import com.wooplr.spotlight.target.AnimPoint;
import com.wooplr.spotlight.target.Target;
import com.wooplr.spotlight.target.ViewTarget;
import com.wooplr.spotlight.utils.LinePointCalculator;
import com.wooplr.spotlight.utils.PositionCalculator;
import com.wooplr.spotlight.utils.SpotlightListener;
import com.wooplr.spotlight.utils.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * @author jitender on 10/06/16
 */

public class SpotlightView extends FrameLayout {

    /**
     * Possible shape types.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SHAPE_TYPE_CIRCLE, SHAPE_TYPE_RECTANGLE})
    public @interface ShapeType {
    }

    //CS: STOP JAVADOC CHECK
    public static final int SHAPE_TYPE_CIRCLE = 0;
    public static final int SHAPE_TYPE_RECTANGLE = 1;
    //CS: RESUME JAVADOC CHECK

    /**
     * OverLay color
     */
    private int maskColor = 0x70000000;

    /**
     * Intro Animation Duration
     */
    private long introAnimationDuration = 400;

    /**
     * Toggel between reveal and fadein animation
     */
    private boolean isRevealAnimationEnabled = true;

    /**
     * Start intro once view is ready to showCircle
     */
    private boolean isReady;

    /**
     * Overlay circle above the view
     */
    private TargetShape targetShape;

    /**
     * Target View
     */
    private Target targetView;

    /**
     * Eraser to erase the circle area
     */
    private final Paint eraser = new Paint();

    /**
     * Delay the intro view
     */
    private final Handler handler = new Handler();
    private Bitmap bitmap;
    private Canvas canvas;

    /**
     * Padding for circle
     */
    private int padding = 20;

    /**
     * View Width
     */
    private int width;

    /**
     * View Height
     */
    private int height;

    /**
     * Dismiss layout on touch
     */
    private boolean dismissOnTouch;
    private boolean dismissOnBackPress;

    /**
     * Listener for spotLight when user clicks on the view
     */
    private SpotlightListener listener;

    /**
     * Perform click when user clicks on the targetView
     */
    private boolean isPerformClick;

    /**
     * Extra padding around the arc
     */
    private int extraPaddingForHalfShape = 24;

    /**
     * Values for line animation
     */
    private long lineAnimationDuration = 300;
    private float lineStroke;

    private int lineAndArcColor = Color.parseColor("#eb273f");

    private View descriptionView;

    private final LinePointCalculator linePointCalculator;
    private final ViewShowStore viewShowStore;
    private boolean showOnlyOnce;
    private int spotLightId;
    private int shapeType;

    public SpotlightView(Context context) {
        super(context);
        init(context);
        linePointCalculator = new LinePointCalculator(context);
        viewShowStore = new ViewShowStore(context);
    }

    public SpotlightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        linePointCalculator = new LinePointCalculator(context);
        viewShowStore = new ViewShowStore(context);
    }

    public SpotlightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        linePointCalculator = new LinePointCalculator(context);
        viewShowStore = new ViewShowStore(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public SpotlightView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        linePointCalculator = new LinePointCalculator(context);
        viewShowStore = new ViewShowStore(context);
    }

    private void init(Context context) {
        setWillNotDraw(false);
        setVisibility(INVISIBLE);

        lineStroke = Utils.dipToPx(context, 2);
        isReady = false;
        isRevealAnimationEnabled = true;
        dismissOnTouch = false;
        dismissOnBackPress = false;
        isPerformClick = false;

        eraser.setColor(0xFFFFFFFF);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        eraser.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isReady) return;

        if (bitmap == null || canvas == null) {
            if (bitmap != null) bitmap.recycle();

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            this.canvas = new Canvas(bitmap);
        }

        this.canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        this.canvas.drawColor(maskColor);

        targetShape.draw(this.canvas, eraser, padding);

        if (canvas != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float xT = event.getX();
        final float yT = event.getY();

        final int xV = targetShape.getPoint().x;
        final int yV = targetShape.getPoint().y;

        final int radiusX = targetShape.getXRadius();
        final int radiusY = targetShape.getYRadius();

        final double dx = Math.pow(xT - xV, 2);
        final double dy = Math.pow(yT - yV, 2);

        final boolean isTouchOnFocus = (dx + dy) <= radiusX * radiusY;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (isTouchOnFocus && isPerformClick) {
                    targetView.getView().setPressed(true);
                    targetView.getView().invalidate();
                }

                return true;
            case MotionEvent.ACTION_UP:
                if (isTouchOnFocus || dismissOnTouch)
                    dismiss();

                if (isTouchOnFocus && isPerformClick) {
                    targetView.getView().performClick();
                    targetView.getView().setPressed(true);
                    targetView.getView().invalidate();
                    targetView.getView().setPressed(false);
                    targetView.getView().invalidate();
                }

                return true;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * Sets a {@link SpotlightListener} called when the spotlight is dismissed.
     *
     * @param listener the SpotlightListener to set
     */
    public void setListener(SpotlightListener listener) {
        this.listener = listener;
    }

    /**
     * Show the view based on the configuration
     * Reveal is available only for Lollipop and above in other only fadein will work
     * To support reveal in older versions use github.com/ozodrukh/CircularReveal
     *
     * @param activity the activity where to showCircle the spotlight
     */
    public void show(final Activity activity) {
        if (!viewShowStore.shouldShowHighlight(spotLightId)) {
            return;
        }

        ((ViewGroup) activity.getWindow().getDecorView()).addView(this);

        setReady(true);

        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                                        if (isRevealAnimationEnabled)
                                            startRevealAnimation(activity);
                                        else {
                                            startFadeInAnimation(activity);
                                        }
                                    } else {
                                        startFadeInAnimation(activity);
                                    }
                                }
                            }
                , 100);

    }

    /**
     * Dissmiss view with reverse animation
     */
    private void dismiss() {
        if (showOnlyOnce) {
            viewShowStore.saveSpotLightShown(spotLightId);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isRevealAnimationEnabled)
                exitRevealAnimation();
            else
                startFadeout();
        } else {
            startFadeout();
        }

    }

    /**
     * Remove the spotlight view
     */
    private void removeSpotlightView() {
        if (listener != null)
            listener.onSpotlightDismissed(spotLightId);

        if (getParent() != null)
            ((ViewGroup) getParent()).removeView(this);
    }

    /**
     * Revel animation from target center to screen width and height
     *
     * @param activity the activity where to showCircle the spotlight
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startRevealAnimation(final Activity activity) {
        final float finalRadius = (float) Math.hypot(getWidth(), getHeight());
        final Animator anim = ViewAnimationUtils.createCircularReveal(this, targetView.getPoint().x, targetView.getPoint().y, 0, finalRadius);
        anim.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.interpolator.fast_out_linear_in));
        anim.setDuration(introAnimationDuration);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (shapeType == SHAPE_TYPE_CIRCLE) {
                    addArcAnimation(activity);
                } else {
                    addRectAnimation(activity);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        setVisibility(View.VISIBLE);
        anim.start();
    }

    /**
     * Reverse reveal animation
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void exitRevealAnimation() {
        final float finalRadius = (float) Math.hypot(getWidth(), getHeight());
        final Animator anim = ViewAnimationUtils.createCircularReveal(this, targetView.getPoint().x, targetView.getPoint().y, finalRadius, 0);
        anim.setInterpolator(AnimationUtils.loadInterpolator(getContext(),
                android.R.interpolator.accelerate_decelerate));
        anim.setDuration(introAnimationDuration);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setVisibility(GONE);
                removeSpotlightView();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        anim.start();
    }

    private void startFadeInAnimation(final Activity activity) {
        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(introAnimationDuration);
        fadeIn.setFillAfter(true);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (shapeType == SHAPE_TYPE_CIRCLE) {
                    addArcAnimation(activity);
                } else {
                    addRectAnimation(activity);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        setVisibility(VISIBLE);
        startAnimation(fadeIn);
    }

    private void startFadeout() {
        final AlphaAnimation fadeIn = new AlphaAnimation(1.0f, 0.0f);
        fadeIn.setDuration(introAnimationDuration);
        fadeIn.setFillAfter(true);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
                removeSpotlightView();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        startAnimation(fadeIn);
    }

    private void addRectAnimation(final Activity activity) {
        final AppCompatImageView imageView = new AppCompatImageView(activity);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        final LayoutParams params = new LayoutParams(targetShape.getXRadius() * 2 + 2 * extraPaddingForHalfShape,
                targetShape.getYRadius() + 2 * extraPaddingForHalfShape);

        final PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(lineAndArcColor, PorterDuff.Mode.SRC_ATOP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final AnimatedVectorDrawable avd = (AnimatedVectorDrawable) ContextCompat.getDrawable(activity, R.drawable.avd_spotlight_rect);
            avd.setColorFilter(porterDuffColorFilter);
            imageView.setImageDrawable(avd);
            avd.start();
        } else {
            final AnimatedVectorDrawableCompat avdc = AnimatedVectorDrawableCompat.create(activity, R.drawable.avd_spotlight_rect);
            if (avdc != null) {
                avdc.setColorFilter(porterDuffColorFilter);
                imageView.setImageDrawable(avdc);
                avdc.start();
            }
        }

        boolean playNextAnim = true;
        final int targetPosition = PositionCalculator.calculatePosition(getWidth(), getHeight(), targetView);
        switch (targetPosition) {
            case PositionCalculator.TOP_LEFT:
            case PositionCalculator.TOP_MIDDLE:
            case PositionCalculator.TOP_RIGHT:
            case PositionCalculator.MIDDLE_LEFT://Rect nach unten
                imageView.setRotation(180);
                params.leftMargin = targetShape.getRect().left - extraPaddingForHalfShape;
                params.topMargin = targetShape.getRect().bottom - params.height + extraPaddingForHalfShape;
                params.gravity = Gravity.START | Gravity.TOP;
                break;
            case PositionCalculator.MIDDLE_MIDDLE: //nach oben und unten
                playNextAnim = false;
                dismissOnTouch = true;
                dismissOnBackPress = true;
                Toast.makeText(getContext(), "No implemented yet", Toast.LENGTH_LONG).show();
                //TODO
                break;
            case PositionCalculator.MIDDLE_RIGHT:
            case PositionCalculator.BOTTOM_LEFT:
            case PositionCalculator.BOTTOM_MIDDLE:
            case PositionCalculator.BOTTOM_RIGHT://nach links
                params.leftMargin = targetShape.getRect().left - extraPaddingForHalfShape;
                params.topMargin = targetShape.getRect().top - extraPaddingForHalfShape;
                params.gravity = Gravity.START | Gravity.TOP;
                break;
            default:
                break;
        }
        imageView.postInvalidate();
        imageView.setLayoutParams(params);
        addView(imageView);

        if (!playNextAnim) {
            return;
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                addPathAnimation(activity);
            }
        }, 400);
    }

    /**
     * Add arch above and below the circle overlay
     * Using AnimatedVectorDrawableCompat for pre-Lollipop device
     *
     * @param activity the activty where to showCircle the spotlight
     */
    private void addArcAnimation(final Activity activity) {
        final AppCompatImageView imageView = new AppCompatImageView(activity);
        final LayoutParams params = new LayoutParams(2 * (targetShape.getXRadius() + extraPaddingForHalfShape),
                2 * (targetShape.getXRadius() + extraPaddingForHalfShape));

        final PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(lineAndArcColor, PorterDuff.Mode.SRC_ATOP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final AnimatedVectorDrawable avd = (AnimatedVectorDrawable) ContextCompat.getDrawable(activity, R.drawable.avd_spotlight_arc);
            avd.setColorFilter(porterDuffColorFilter);
            imageView.setImageDrawable(avd);
            avd.start();
        } else {
            final AnimatedVectorDrawableCompat avdc = AnimatedVectorDrawableCompat.create(activity, R.drawable.avd_spotlight_arc);
            if (avdc != null) {
                avdc.setColorFilter(porterDuffColorFilter);
                imageView.setImageDrawable(avdc);
                avdc.start();
            }
        }

        boolean playNextAnim = true;
        final int targetPosition = PositionCalculator.calculatePosition(getWidth(), getHeight(), targetView);
        switch (targetPosition) {
            case PositionCalculator.TOP_LEFT: //Arc nach rechts
                imageView.setRotation(90);
                params.leftMargin = targetView.getPoint().x - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.bottomMargin = getHeight() - targetView.getPoint().y - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.gravity = Gravity.START | Gravity.BOTTOM;
                break;
            case PositionCalculator.TOP_MIDDLE: //Arc nach unten
                imageView.setRotation(180);
                params.leftMargin = targetView.getPoint().x - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.bottomMargin = getHeight() - targetView.getPoint().y - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.gravity = Gravity.START | Gravity.BOTTOM;
                break;
            case PositionCalculator.TOP_RIGHT: //Arc nach links
                imageView.setRotation(-90);
                params.rightMargin = getWidth() - targetView.getPoint().x - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.bottomMargin = getHeight() - targetView.getPoint().y - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.gravity = Gravity.END | Gravity.BOTTOM;
                break;
            case PositionCalculator.MIDDLE_LEFT: //nach rechts
                imageView.setRotation(90);
                params.leftMargin = targetView.getPoint().x - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.bottomMargin = getHeight() - targetView.getPoint().y - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.gravity = Gravity.START | Gravity.BOTTOM;
                break;
            case PositionCalculator.MIDDLE_MIDDLE: //nach oben und unten
                playNextAnim = false;
                dismissOnTouch = true;
                dismissOnBackPress = true;
                Toast.makeText(getContext(), "No implemented yet", Toast.LENGTH_LONG).show();
                //TODO
                break;
            case PositionCalculator.MIDDLE_RIGHT: //nach links
                imageView.setRotation(-90);
                params.rightMargin = getWidth() - targetView.getPoint().x - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.bottomMargin = getHeight() - targetView.getPoint().y - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.gravity = Gravity.END | Gravity.BOTTOM;
                break;
            case PositionCalculator.BOTTOM_LEFT: //nach recht nach oben
                imageView.setRotation(90);
                params.leftMargin = targetView.getPoint().x - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.bottomMargin = getHeight() - targetView.getPoint().y - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.gravity = Gravity.START | Gravity.BOTTOM;
                break;
            case PositionCalculator.BOTTOM_MIDDLE: //nach oben
                params.leftMargin = targetView.getPoint().x - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.bottomMargin = getHeight() - targetView.getPoint().y - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.gravity = Gravity.START | Gravity.BOTTOM;
                break;
            case PositionCalculator.BOTTOM_RIGHT: //nach links, nach oben
                imageView.setRotation(-90);
                params.rightMargin = getWidth() - targetView.getPoint().x - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.bottomMargin = getHeight() - targetView.getPoint().y - targetShape.getXRadius() - extraPaddingForHalfShape;
                params.gravity = Gravity.END | Gravity.BOTTOM;
                break;
            default:
                break;
        }
        imageView.postInvalidate();
        imageView.setLayoutParams(params);
        addView(imageView);

        if (!playNextAnim) {
            return;
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                addPathAnimation(activity);
            }
        }, 400);
    }

    private void addPathAnimation(final Activity activity) {
        final View pathView = new View(activity);
        final LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.width = getWidth();
        params.height = getHeight();
        addView(pathView, params);

        //Line animation
        final Paint p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(lineStroke);
        p.setColor(lineAndArcColor);

        final NormalLineAnimDrawable pathAnimDrawable = new NormalLineAnimDrawable(p);
        if (lineAnimationDuration > 0) {
            pathAnimDrawable.setLineAnimDuration(lineAnimationDuration);
        }
        pathView.setBackground(pathAnimDrawable);

        final int targetPosition = PositionCalculator.calculatePosition(getWidth(), getHeight(), targetView);
        //noinspection ResourceType
        final List<AnimPoint> animPoints = linePointCalculator.calcLinePoints(targetPosition, getWidth(), getHeight(), targetShape, shapeType);
        pathAnimDrawable.setPoints(animPoints);
        pathAnimDrawable.setAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                addBoundingAnimation(activity, animPoints.get(animPoints.size() - 1).getMoveX(), animPoints.get(animPoints.size() - 1).getMoveY(), targetPosition);
                descriptionView.setAlpha(0.0f);
                descriptionView.setVisibility(VISIBLE);
                descriptionView.animate().alpha(1.0f).setDuration(800);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        pathAnimDrawable.playAnim();
    }

    private void addBoundingAnimation(Activity activity, float lastX, float lastY, int targetPosition) {
        final View boundingView = new View(activity);
        final LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.width = getWidth();
        params.height = getHeight();
        addView(boundingView, params);

        //Line animation
        final Paint p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(lineStroke);
        p.setColor(lineAndArcColor);

        final NormalLineAnimDrawable boundingAnimDrawable = new NormalLineAnimDrawable(p);
        if (lineAnimationDuration > 0) {
            boundingAnimDrawable.setLineAnimDuration(lineAnimationDuration);
        }
        boundingView.setBackground(boundingAnimDrawable);
        boundingAnimDrawable.setPoints(linePointCalculator.calcLeftBoundingLinePoints(lastX, lastY, targetPosition));
        boundingAnimDrawable.setSecondPoints(linePointCalculator.calcRightBoundingLinePoints(lastX, lastY, targetPosition));
        boundingAnimDrawable.setAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                dismissOnTouch = true;
                dismissOnBackPress = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        boundingAnimDrawable.playAnim();
    }

    private void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
    }

    private void setReady(boolean ready) {
        isReady = ready;
    }

    private void setPadding(int padding) {
        this.padding = padding;
    }

    private void setDismissOnTouch(boolean dismissOnTouch) {
        this.dismissOnTouch = dismissOnTouch;
    }

    private void setDismissOnBackPress(boolean dismissOnBackPress) {
        this.dismissOnBackPress = dismissOnBackPress;
    }

    private void setPerformClick(boolean performClick) {
        isPerformClick = performClick;
    }

    private void setExtraPaddingForHalfShape(int extraPaddingForHalfShape) {
        linePointCalculator.setExtraPaddingForArc(extraPaddingForHalfShape);
        this.extraPaddingForHalfShape = extraPaddingForHalfShape;
    }

    private void setIntroAnimationDuration(long introAnimationDuration) {
        this.introAnimationDuration = introAnimationDuration;
    }

    private void setRevealAnimationEnabled(boolean revealAnimationEnabled) {
        isRevealAnimationEnabled = revealAnimationEnabled;
    }

    private void setTargetShape(TargetShape targetShape) {
        this.targetShape = targetShape;
    }

    private void setTargetView(Target targetView) {
        this.targetView = targetView;
        linePointCalculator.setTargetView(targetView);
    }

    private void setSpotLightId(int spotLightId) {
        this.spotLightId = spotLightId;
    }

    private void setLineAnimationDuration(long lineAnimationDuration) {
        this.lineAnimationDuration = lineAnimationDuration;
    }

    private void setLineAndArcColor(int lineAndArcColor) {
        this.lineAndArcColor = lineAndArcColor;
    }

    private void showOnlyOnce() {
        showOnlyOnce = true;
    }

    private void setLineStroke(int lineStroke) {
        this.lineStroke = lineStroke;
    }

    private void setLayoutResource(int layoutResource, int buttonId, final OnClickListener listener) {
        descriptionView = LayoutInflater.from(getContext()).inflate(layoutResource, this, false);
        addView(descriptionView);
        descriptionView.setVisibility(INVISIBLE);
        linePointCalculator.setDescriptionView(descriptionView);
        if (buttonId != -1) {
            final View okButton = descriptionView.findViewById(buttonId);
            okButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    if (listener != null) {
                        listener.onClick(view);
                    }
                }
            });
        }
    }

    private void setConfiguration(SpotlightConfig configuration) {
        if (configuration != null) {
            this.maskColor = configuration.getMaskColor();
            this.introAnimationDuration = configuration.getIntroAnimationDuration();
            this.isRevealAnimationEnabled = configuration.isRevealAnimationEnabled();
            this.padding = configuration.getPadding();
            this.dismissOnTouch = configuration.isDismissOnTouch();
            this.dismissOnBackPress = configuration.isDismissOnBackPress();
            this.isPerformClick = configuration.isPerformClick();
            this.lineAnimationDuration = configuration.getLineAnimationDuration();
            this.lineStroke = configuration.getLineStroke();
            this.lineAndArcColor = configuration.getLineAndArcColor();
        }
    }

    private void setShapeType(int shapeType) {
        this.shapeType = shapeType;
    }

    /**
     * Builder Class
     */
    @SuppressWarnings("unused")
    public static class Builder {

        private SpotlightView spotlightView;

        private Activity activity;

        /**
         * Constructor of the builder.
         *
         * @param activity the activity where to showCircle the spotlight
         */
        public Builder(Activity activity) {
            this.activity = activity;
            spotlightView = new SpotlightView(activity);
        }

        /**
         * Sets a new background color for the whole overlay. For a better user experience the set color should have a little bit transparency.
         *
         * @param maskColor the new background color of the overlay
         * @return the same builder instance
         */
        public Builder maskColor(@ColorInt int maskColor) {
            spotlightView.setMaskColor(maskColor);
            return this;
        }

        /**
         * The duration of the animation to showCircle the background of the spotlight.
         *
         * @param delayMillis the duration of the animation to showCircle the background
         * @return the same builder
         */
        public Builder introAnimationDuration(long delayMillis) {
            spotlightView.setIntroAnimationDuration(delayMillis);
            return this;
        }

        /**
         * Enables the reveal animation used in android lollipop and above.
         *
         * @param isFadeAnimationEnabled set to true if the reveal animation should be enabled
         * @return the same builder
         */
        public Builder enableRevealAnimation(boolean isFadeAnimationEnabled) {
            spotlightView.setRevealAnimationEnabled(isFadeAnimationEnabled);
            return this;
        }

        /**
         * Set the given view as the spotlight to showCircle in your base view. This will showCircle a spot on the given view.
         *
         * @param view the View to spot
         * @return the same builder instance
         */
        public Builder target(View view) {
            spotlightView.setTargetView(new ViewTarget(view));
            return this;
        }

        /**
         * Sets the padding around the spotlight view. That means how much space is between the spotlight view and the semi transparent background.
         *
         * @param padding the padding to set
         * @return the same builder instance
         */
        public Builder targetPadding(@Dimension int padding) {
            spotlightView.setPadding(padding);
            return this;
        }

        /**
         * Sets an extra padding for the arc around the spotlight view.
         *
         * @param padding padding for the arc around the spotlight view
         * @return the same builder
         */
        public Builder setExtraPaddingForArc(int padding) {
            spotlightView.setExtraPaddingForHalfShape(padding);
            return this;
        }

        /**
         * Enables or disables the view to dismiss itself if the user touches somewhere on the view.
         *
         * @param dismissOnTouch set to true if the view may dismiss itself on touch, false otherwise
         * @return the same builder instance
         */
        public Builder dismissOnTouch(boolean dismissOnTouch) {
            spotlightView.setDismissOnTouch(dismissOnTouch);
            return this;
        }

        /**
         * Sets a listener which is called when the spotlight view was removed from the view and your base view will be fully visible to the user.
         *
         * @param spotlightListener a {@link SpotlightListener} called when the spotlight was removed
         * @return the same builder instance
         */
        public Builder setListener(SpotlightListener spotlightListener) {
            spotlightView.setListener(spotlightListener);
            return this;
        }

        /**
         * If set to true the user can click on the spotlight view and the view will receive the click event to handle itself.
         *
         * @param isPerformClick set to true to send the spotlight view click to the view
         * @return the same builder
         */
        public Builder performClick(boolean isPerformClick) {
            spotlightView.setPerformClick(isPerformClick);
            return this;
        }

        /**
         * The color of the lines and arc.
         *
         * @param color color of th lines and arc
         * @return the same builder
         */
        public Builder lineAndArcColor(int color) {
            spotlightView.setLineAndArcColor(color);
            return this;
        }

        /**
         * The duration of the line animation.
         *
         * @param duration long value as the duration of the line animation
         * @return the same builder
         */
        public Builder lineAnimDuration(long duration) {
            spotlightView.setLineAnimationDuration(duration);
            return this;
        }

        /**
         * Lets the user dismiss the spotlight only when whe the full spotlight is shown.
         *
         * @param enable set to true when the user can dismiss the spotlight only when the full spotlight is shown, if set to false the user can dismiss the
         *               spotlight anytime
         * @return the same builder
         */
        public Builder enableDismissAfterShown(boolean enable) {
            if (enable)
                spotlightView.setDismissOnTouch(false);
            return this;
        }

        /**
         * Lets the user dismiss the spotlight on back press.
         *
         * @param dismissOnBackPress set to true to let the user dismiss the spotlight on back click
         * @return the same builder
         */
        public Builder dismissOnBackPress(boolean dismissOnBackPress) {
            spotlightView.setDismissOnBackPress(dismissOnBackPress);
            return this;
        }

        /**
         * Sets the layout resource id of the view to showCircle on the spotlight with description and images for the spotlight view.
         *
         * @param layoutId the layout resource id to inflate
         * @return the same builder
         */
        public Builder setDescriptionView(@LayoutRes int layoutId) {
            setDescriptionView(layoutId, -1, null);
            return this;
        }

        /**
         * Sets the layout resource id of the view to showCircle on the spotlight with description and images for the spotlight view. If the view includes a button
         * and a button click should dismiss the spotlight use this method and provide the button id of the included button. You can set an additional
         * listener which is called on button click, too.
         *
         * @param layoutId the layout id of the view to showCircle on the spotlight
         * @param buttonId the button if of the included button
         * @param listener a {@link android.view.View.OnClickListener} called when the button is clicked, after the view will be dismissed
         * @return the same builder
         */
        public Builder setDescriptionView(@LayoutRes int layoutId, @IdRes int buttonId, OnClickListener listener) {
            spotlightView.setLayoutResource(layoutId, buttonId, listener);
            return this;
        }

        /**
         * Call this function if the spotlight should be only shown once.
         *
         * @return the same builder
         */
        public Builder showOnlyOnce() {
            spotlightView.showOnlyOnce();
            return this;
        }

        /**
         * Sets a new line stroke in dip used to draw the lines in the spotlight.
         *
         * @param context context used to convert the dip to pixels
         * @param stroke  the stroke to use
         * @return the same builder
         */
        public Builder lineStroke(Context context, @Dimension int stroke) {
            spotlightView.setLineStroke(Math.round(Utils.dipToPx(context, stroke)));
            return this;
        }

        /**
         * Sets a full {@link SpotlightConfig} to use to draw the spotlight.
         *
         * @param configuration the {@link SpotlightConfig} to use to draw the spotlight
         * @return the same builder
         */
        public Builder setConfiguration(SpotlightConfig configuration) {
            spotlightView.setConfiguration(configuration);
            return this;
        }

        /**
         * Builds a spotlight with circle target view but does not show it. The id you set here will be the parameter in a listener call when the spotlight is
         * dismissed.
         *
         * @param spotLightId an id for the spotlight to identify it
         * @return a new created Spotlight
         */
        public SpotlightView buildCircleSpot(int spotLightId) {
            spotlightView.setSpotLightId(spotLightId);
            final TargetShape circle = new Circle(
                    spotlightView.targetView,
                    spotlightView.padding);
            spotlightView.setShapeType(SHAPE_TYPE_CIRCLE);
            spotlightView.setTargetShape(circle);
            return spotlightView;
        }

        /**
         * Builds a  spotlight with rectangle target view but does not show it. The id you set here will be the parameter in a listener call when the
         * spotlight is dismissed.
         *
         * @param spotLightId an id for the spotlight to identify it
         * @return a new created Spotlight
         */
        public SpotlightView buildRectSpot(int spotLightId) {
            spotlightView.setSpotLightId(spotLightId);
            final TargetShape rectangle = new Rectangle(spotlightView.targetView, spotlightView.padding);
            spotlightView.setShapeType(SHAPE_TYPE_RECTANGLE);
            spotlightView.setTargetShape(rectangle);
            return spotlightView;
        }

        /**
         * Builds and shows a spotlight with a circle target view. The id you set here will be the parameter in a listener call when the spotlight is dismissed.
         *
         * @param spotLightId an id for the spotlight to identify it
         */
        public void showCircle(int spotLightId) {
            buildCircleSpot(spotLightId).show(activity);
        }

        /**
         * Builds and shows a spotlight with a rectangle target view. The id you set here will be the parameter in a listener call when the spotlight is
         * dismissed.
         *
         * @param spotLightId an id for the spotlight to identify it
         */
        public void showRectangle(int spotLightId) {
            buildRectSpot(spotLightId).show(activity);
        }

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (dismissOnBackPress) {
            if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                dismiss();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
