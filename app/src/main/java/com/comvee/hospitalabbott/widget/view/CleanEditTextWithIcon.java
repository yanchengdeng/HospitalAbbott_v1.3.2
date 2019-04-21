package com.comvee.hospitalabbott.widget.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.comvee.hospitalabbott.R;


/**
 * 带有图标和删除符号的可编辑输入框，用户可以自定义传入的显示图标
 *
 * @author
 */
public class CleanEditTextWithIcon extends EditText implements View.OnTouchListener, TextWatcher,
        View.OnFocusChangeListener {

    // 删除符号
    Drawable deleteImage = getResources().getDrawable(R.drawable.hospital_07);

    Drawable icon;

    public CleanEditTextWithIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CleanEditTextWithIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CleanEditTextWithIcon(Context context) {
        super(context);
        init();
    }

    private void init() {
        CleanEditTextWithIcon.this.setOnTouchListener(this);
        CleanEditTextWithIcon.this.addTextChangedListener(this);
        deleteImage.setBounds(0, 0, deleteImage.getIntrinsicWidth(), deleteImage.getIntrinsicHeight());
        manageClearButton();
    }


    /**
     * 传入显示的图标资源id
     *
     * @param id
     */
    public void setIconResource(int id) {
        icon = getResources().getDrawable(id);
        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        manageClearButton();
    }

    /**
     * 传入删除图标资源id
     *
     * @param id
     */
    public void setDeleteImage(int id) {
        deleteImage = getResources().getDrawable(id);
        deleteImage.setBounds(0, 0, deleteImage.getIntrinsicWidth(), deleteImage.getIntrinsicHeight());
        manageClearButton();
    }

    void manageClearButton() {
        if (this.getText().toString().equals(""))
            removeClearButton();
        else
            addClearButton();
    }

    public void removeClearButton() {
        this.setCompoundDrawables(this.icon, this.getCompoundDrawables()[1], null, this.getCompoundDrawables()[3]);
    }

    public void addClearButton() {
        this.setCompoundDrawables(this.icon, this.getCompoundDrawables()[1], deleteImage,
                this.getCompoundDrawables()[3]);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        CleanEditTextWithIcon et = CleanEditTextWithIcon.this;

        et.setCursorVisible(true);// 再次点击显示光标
        if (et.getText().toString().length() > 0) {
            et.addClearButton();
        }

        if (et.getCompoundDrawables()[2] == null)
            return false;
        if (event.getAction() != MotionEvent.ACTION_UP)
            return false;
        if (event.getX() > et.getWidth() - et.getPaddingRight() - deleteImage.getIntrinsicWidth()) {
            et.setText("");
            CleanEditTextWithIcon.this.removeClearButton();
        }


        return false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        CleanEditTextWithIcon.this.manageClearButton();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    //获取焦点状态 （在内部该方法不走！）
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            // 此处为得到焦点时的处理内容
            if (this.getText().toString().length() > 0)
                addClearButton();
        } else {
            // 此处为失去焦点时的处理内容
            if (this.getText().toString().length() > 0)
                removeClearButton();
        }

    }


}
