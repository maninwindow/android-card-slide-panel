package com.stone.card;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 卡片View项
 *
 * @author xmuSistone
 */
@SuppressLint("NewApi")
public class CardItemView extends CardView {
    private Spring springX, springY;
    public ImageView imageView;
    public View maskView;
    private TextView userNameTv;
    private TextView imageNumTv;
    private TextView likeNumTv;
    private CardSlidePanel parentView;

    public CardItemView(Context context) {
        this(context, null);
    }

    public CardItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.card_item, this);
        imageView = (ImageView) findViewById(R.id.card_image_view);
        maskView = findViewById(R.id.maskView);
        userNameTv = (TextView) findViewById(R.id.card_user_name);
        imageNumTv = (TextView) findViewById(R.id.card_pic_num);
        likeNumTv = (TextView) findViewById(R.id.card_like);

        try {
            setCardBackgroundColor(Color.WHITE);
            setRadius(20);
            setClipToOutline(false);
        } catch (Exception e) {
        }

        initSpring();
    }

    private void initSpring() {
        SpringSystem mSpringSystem = SpringSystem.create();
        springX = mSpringSystem.createSpring();
        springY = mSpringSystem.createSpring();

        springX.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                int xPos = (int) spring.getCurrentValue();
                setScreenX(xPos);
                parentView.onViewPosChanged(CardItemView.this);
            }
        });

        springY.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                int yPos = (int) spring.getCurrentValue();
                setScreenY(yPos);
                parentView.onViewPosChanged(CardItemView.this);
            }
        });
    }

    public void fillData(CardDataItem itemData) {
        ImageLoader.getInstance().displayImage(itemData.imagePath, imageView);
        userNameTv.setText(itemData.userName);
        imageNumTv.setText(itemData.imageNum + "");
        likeNumTv.setText(itemData.likeNum + "");
    }


    /**
     * 动画移动到某个位置
     */
    public void animTo(int xPos, int yPos) {
        setCurrentSpringPos(getLeft(), getTop());
        springX.setEndValue(xPos);
        springY.setEndValue(yPos);
    }

    /**
     * 设置当前spring位置
     */
    private void setCurrentSpringPos(int xPos, int yPos) {
        springX.setCurrentValue(xPos);
        springY.setCurrentValue(yPos);
    }

    public void setScreenX(int screenX) {
        this.offsetLeftAndRight(screenX - getLeft());
    }

    public void setScreenY(int screenY) {
        this.offsetTopAndBottom(screenY - getTop());
    }

    /**
     * alpha从0到1的动画
     */
    public void startShowingUpAnimation() {
        ObjectAnimator alphaAnimator = ObjectAnimator
                .ofFloat(this, "alpha", 0, 1)
                .setDuration(300);
        alphaAnimator.start();
    }

    public void setParentView(CardSlidePanel parentView) {
        this.parentView = parentView;
    }

    public void onStartDragging() {
        springX.setAtRest();
        springY.setAtRest();
    }
}
