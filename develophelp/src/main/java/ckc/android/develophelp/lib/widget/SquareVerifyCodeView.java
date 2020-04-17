package ckc.android.develophelp.lib.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 方形验证码控件
 * ckc 20200215
 *
 * 功能：
 * 输入一个数字后，选中框自动移动到下一个框；
 * 删除一个数字后，选中框自动移动到上一个框；
 * 输入完成后回调；
 * 个性化属性，见声明文件。
 * */
public class SquareVerifyCodeView extends RelativeLayout {

    private int mSquareNumber = 4, mSquareWidth = 50, mSquareInterval = 10;
    private Drawable mSquareSelectedBg, mSquareUnselectedBg;
    private int mTextSize = 15;
    private ColorStateList mTextColor;
    private int mTextStyle = Typeface.NORMAL;

    private EditText mEditText;
    private TextView[] mTvSquares;

    private OnVerifyCodeListener mOnVerifyCodeListener;

    private int mWidth, mHeight;

    public SquareVerifyCodeView(Context context) {
        this(context, null);
    }

    public SquareVerifyCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareVerifyCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SquareVerifyCodeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        //读取属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, ckc.android.develophelp.lib.R.styleable.SquareVerifyCodeView);
        mSquareNumber = typedArray.getInteger(ckc.android.develophelp.lib.R.styleable.SquareVerifyCodeView_svcv_square_number, mSquareNumber);
        mSquareWidth = typedArray.getDimensionPixelSize(ckc.android.develophelp.lib.R.styleable.SquareVerifyCodeView_svcv_square_width, mSquareWidth);
        mSquareInterval = typedArray.getDimensionPixelSize(ckc.android.develophelp.lib.R.styleable.SquareVerifyCodeView_svcv_square_interval, mSquareInterval);
        mSquareSelectedBg = typedArray.getDrawable(ckc.android.develophelp.lib.R.styleable.SquareVerifyCodeView_svcv_square_selected_bg);
        mSquareUnselectedBg = typedArray.getDrawable(ckc.android.develophelp.lib.R.styleable.SquareVerifyCodeView_svcv_square_unselected_bg);
        mTextSize = (int) typedArray.getDimension(ckc.android.develophelp.lib.R.styleable.SquareVerifyCodeView_android_textSize, mTextSize);
        mTextColor = typedArray.getColorStateList(ckc.android.develophelp.lib.R.styleable.SquareVerifyCodeView_android_textColor);
        mTextStyle = typedArray.getInt(ckc.android.develophelp.lib.R.styleable.SquareVerifyCodeView_android_textStyle, mTextStyle);
        typedArray.recycle();

        mWidth = mSquareWidth * mSquareNumber + mSquareInterval * (mSquareNumber - 1);
        mHeight = mSquareWidth;

        fillView();
    }

    private void fillView(){
        LayoutParams lpEt = new LayoutParams(mWidth, mHeight);

        //填充EditText控件，用于获取焦点
        EditText editText = new EditText(getContext());
        mEditText = editText;
        editText.setLayoutParams(lpEt);
        editText.setCursorVisible(false);//隐藏光标
        editText.setBackground(null);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        addView(editText);
        //填充LinearLayout控件
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(lpEt);
        //填充TextView控件,用来显示输入数字
        mTvSquares = new TextView[mSquareNumber];
        LayoutParams tvLp = new LayoutParams(mSquareWidth, mSquareWidth);
        LayoutParams tvMarginLeftLp = new LayoutParams(mSquareWidth, mSquareWidth);
        tvMarginLeftLp.setMargins(mSquareInterval, 0, 0 , 0);
        for (int i = 0; i < mSquareNumber; i++) {
            TextView textView = new TextView(getContext());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            textView.setTextColor(mTextColor);
            textView.setTypeface(Typeface.defaultFromStyle(mTextStyle));
            textView.setGravity(Gravity.CENTER);

            if (i == 0){
                textView.setBackground(mSquareSelectedBg);
                textView.setLayoutParams(tvLp);
            } else {
                textView.setBackground(mSquareUnselectedBg);
                textView.setLayoutParams(tvMarginLeftLp);
            }

            mTvSquares[i] = textView;

            linearLayout.addView(textView);
        }
        addView(linearLayout);

        //对输入框设置监听
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!TextUtils.isEmpty(text)){
                    for (int i = 0; i < mSquareNumber; i++) {
                        if (TextUtils.isEmpty(mTvSquares[i].getText().toString())){
                            mTvSquares[i].setText(text);
                            mTvSquares[i].setBackground(mSquareUnselectedBg);

                            if (i < mSquareNumber - 1){
                                mTvSquares[i+1].setBackground(mSquareSelectedBg);
                            }

                            if (i == mSquareNumber - 1){
                                onInputComplete();
                            }
                            break;
                        }
                    }
                    mEditText.setText(null);
                }
            }
        });

        // 监听删除按键
        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onKeyDelete();
                    return true;
                }
                return false;
            }
        });
    }

    private void onInputComplete(){
        if (mOnVerifyCodeListener != null){
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mSquareNumber; i++) {
                builder.append(mTvSquares[i].getText().toString());
            }
            mOnVerifyCodeListener.onVerifyCodeInputComplete(builder.toString());
        }
    }

    private void onKeyDelete() {
        for (int i = mSquareNumber - 1; i >= 0; i--) {
            if (!TextUtils.isEmpty(mTvSquares[i].getText().toString())){
                mTvSquares[i].setText(null);
                mTvSquares[i].setBackground(mSquareSelectedBg);
                if (i < mSquareNumber - 1){
                    mTvSquares[i+1].setBackground(mSquareUnselectedBg);
                }
                break;
            }
        }
    }

    public void setOnVerifyCodeListener(OnVerifyCodeListener onVerifyCodeListener) {
        mOnVerifyCodeListener = onVerifyCodeListener;
    }

    public interface OnVerifyCodeListener{
        void onVerifyCodeInputComplete(String code);
    }
}
