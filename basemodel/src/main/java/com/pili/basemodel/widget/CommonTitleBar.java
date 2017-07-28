
package com.pili.basemodel.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pili.basemodel.R;

/**
 * 首页的标题栏
 */
public class CommonTitleBar extends LinearLayout {

    /**
     * UI元素
     */
    ImageView mLeftBtn;
    ImageView mRightBtn;
    TextView mTitleText;

    public CommonTitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUI(context);
    }

    public CommonTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTitleBar(Context context) {
        this(context, null, 0);
    }

    private void initUI(Context ctx) {
        View rootLy = LayoutInflater.from(ctx).inflate(R.layout.layout_common_title_bar, this);
        mLeftBtn= (ImageView) rootLy.findViewById(R.id.left_text);
        mRightBtn= (ImageView) rootLy.findViewById(R.id.right_text);
        mTitleText= (TextView) rootLy.findViewById(R.id.title_text);
    }

    /**
     * 左边按钮点击
     */
    public CommonTitleBar setLeftListener(OnClickListener l) {
        mLeftBtn.setOnClickListener(l);
        return this;
    }

    /**
     * 设置左边按钮的显示 默认是显示
     */
    public void setLeftImageVisible(int visible) {
        mLeftBtn.setVisibility(visible);
    }

    /**
     * 设置左边按钮图标 默认是返回键
     */
    public void setLeftImageResource(int resourceID) {
        mLeftBtn.setImageResource(resourceID);
    }

    /**
     * 右边按钮点击
     */
    public CommonTitleBar setRightListener(OnClickListener l) {
        mRightBtn.setOnClickListener(l);
        return this;
    }

    /**
     * 设置左边按钮的显示 默认不显示
     */
    public void setRightImageVisible(int visible) {
        mRightBtn.setVisibility(visible);
    }

    /**
     * 设置右边按钮图标 没有默认图标，需要自己设置
     */
    public void setRightImageResource(int resourceID) {
        mRightBtn.setImageResource(resourceID);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        mTitleText.setText(title);
    }

    /**
     * 设置标题
     */
    public void setTitleResource(int resourceID) {
        mTitleText.setText(resourceID);
    }

    /**
     * 设置title是否显示 默认显示title，没有文字
     */
    public void setTitleVisible(int visible) {
        mTitleText.setVisibility(visible);
    }

    /**
     * 设置title 颜色
     */
    public void setTitleTextColor(int color) {
        mTitleText.setTextColor(color);
    }


    /**
     * 获取右边按钮
     */
    public ImageView getRightButton() {
        return mRightBtn;
    }

    /**
     * 获取左边按钮
     */
    public ImageView getLeftButton() {
        return mLeftBtn;
    }
}
