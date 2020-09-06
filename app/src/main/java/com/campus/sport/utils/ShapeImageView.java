package com.campus.sport.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.campus.sport.R;

/**
 * Created by Administrator on 2019/5/20.
 */

@SuppressLint("AppCompatCustomView")
public class ShapeImageView extends ImageView {
    public static final int SHAPE_CIRCLE = 0;  //圆形
    public static final int SHAPE_ROUND = 1;   //圆角
    private final int DEFAULT_BORDER_WIDTH = 0;
    private final int DEFAULT_ROUND_RADIUS = 0;
    private final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private final int DEFAULT_FILL_COLOR = Color.TRANSPARENT;
    private final boolean DEFAULT_BORDER_OVERLAY = false;
    private final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private final int DEFAULT_DRAWABLE_DIMENSION = 2;

    private int mRoundRadius; //圆角
    private int mBorderWidth; //边框宽
    private int mBorderColor; //边框颜色
    private int mFillColor;   //填充颜色
    private boolean mBorderOverlay;  //边框叠加
    private int mShapeType; //形状

    private RectF mContentRect = new RectF();
    private Paint mBorderPaint = new Paint();
    private Paint mBitmapPaint = new Paint();
    private Paint mFillPaint = new Paint();

    private float mContentRadius;
    private float mBorderRadius;
    private Bitmap mBitmap;
    private int mWidth;
    private int mHeight;

    public ShapeImageView(Context context) {
        this(context, null);
    }

    public ShapeImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShapeImageView, defStyleAttr, 0);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.ShapeImageView_siv_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.ShapeImageView_siv_border_color, DEFAULT_BORDER_COLOR);
        mFillColor = a.getColor(R.styleable.ShapeImageView_siv_fill_color, DEFAULT_FILL_COLOR);
        mBorderOverlay = a.getBoolean(R.styleable.ShapeImageView_siv_border_overlay, DEFAULT_BORDER_OVERLAY);
        mShapeType = a.getInt(R.styleable.ShapeImageView_siv_shape_type, SHAPE_CIRCLE);
        mRoundRadius = a.getDimensionPixelSize(R.styleable.ShapeImageView_siv_round_radius, DEFAULT_ROUND_RADIUS);
        a.recycle();
    }

    @Override
    public void invalidate() {
        mBitmap = getBitmapFormDrawable();
        updateSetup();
        super.invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0) {
            mWidth = w;
        }
        if (h > 0) {
            mHeight = h;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }
        if (mShapeType == SHAPE_CIRCLE) {
            drawCircle(canvas);
        } else if (mShapeType == SHAPE_ROUND) {
            drawRoundRect(canvas);
        }
    }

    //圆角矩形
    private void drawRoundRect(Canvas canvas) {
        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawRoundRect(mContentRect, mRoundRadius, mRoundRadius, mFillPaint);
        }
        canvas.drawRoundRect(mContentRect, mRoundRadius, mRoundRadius, mBitmapPaint);
        if (mBorderWidth > 0) {
            canvas.drawRoundRect(mContentRect, mRoundRadius, mRoundRadius, mBorderPaint);
        }
    }

    //圆形
    private void drawCircle(Canvas canvas) {
        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawCircle(mContentRect.centerX(), mContentRect.centerY(), mContentRadius, mFillPaint);
        }
        canvas.drawCircle(mContentRect.centerX(), mContentRect.centerY(), mContentRadius, mBitmapPaint);
        if (mBorderWidth > 0) {
            canvas.drawCircle(mContentRect.centerX(), mContentRect.centerY(), mBorderRadius, mBorderPaint);
        }
    }

    private void updateSetup() {
        if (mWidth == 0 && mHeight == 0) {
            return;
        }
        if (mBitmap == null) {
            super.invalidate();
            return;
        }
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(bitmapShader);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);
        //边框
        mContentRect.set(calculateRect());
        if (!mBorderOverlay && mBorderWidth > 0) {
            mContentRect.inset(mBorderWidth - 0.0f, mBorderWidth - 0.0f);
        }
        if (mShapeType == SHAPE_CIRCLE) {
            //边框半径
            mBorderRadius = Math.min((mContentRect.width() - mBorderWidth) / 2.0f, (mContentRect.height() - mBorderWidth) / 2.0f);
            //内容半径
            mContentRadius = Math.min(mContentRect.width() / 2.0f, mContentRect.height() / 2.0f);
        } else if (mShapeType == SHAPE_ROUND) {
            // TODO: 2017/8/21
        }
        updateMatrix(bitmapShader);
    }

    private void updateMatrix(BitmapShader bitmapShader) {
        float scale;
        float dx = 0, dy = 0;
        final int bHeight = mBitmap.getHeight();
        final int bWidth = mBitmap.getWidth();
        final float cWidth = mContentRect.width();
        final float cHeight = mContentRect.height();
        //计算缩放比例 平移距离
        if (bWidth * cHeight > cWidth * bHeight) {
            //宽度比 > 高度比 取高度比缩放
            scale = cHeight / (float) bHeight;
            //计算横向移动距离
            dx = (cWidth - bWidth * scale) * 0.5f;
        } else {
            scale = cWidth / (float) bWidth;
            dy = (cHeight - bHeight * scale) * 0.5f;
        }
        Matrix mMatrix = new Matrix();
        mMatrix.setScale(scale, scale);
        mMatrix.postTranslate(Math.round(dx) + mContentRect.left, Math.round(dy) + mContentRect.left);
        bitmapShader.setLocalMatrix(mMatrix);
    }

    private RectF calculateRect() {
        int width = mWidth - getPaddingLeft() - getPaddingRight();
        int height = mHeight - getPaddingTop() - getPaddingBottom();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        return new RectF(left, top, left + width, top + height);
    }

    private Bitmap getBitmapFormDrawable() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            int dWidth = drawable.getIntrinsicWidth() <= 0 ? DEFAULT_DRAWABLE_DIMENSION : drawable.getIntrinsicWidth();
            int dHeight = drawable.getIntrinsicHeight() <= 0 ? DEFAULT_DRAWABLE_DIMENSION : drawable.getIntrinsicHeight();
            Bitmap bitmap = Bitmap.createBitmap(dWidth, dHeight, BITMAP_CONFIG);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
