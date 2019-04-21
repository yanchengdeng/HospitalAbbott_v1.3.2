package com.comvee.hospitalabbott.widget;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.helper.UserHelper;

/**
 * Created by comv098 on 2017/2/17.
 */

public class AddressDialog extends DialogFragment {

    public static AddressDialog newInstantiate() {
        AddressDialog dialog = new AddressDialog();
        return dialog;
    }

    @Override //在onCreate中设置对话框的风格、属性等
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        setCancelable(true);
        //可以设置dialog的显示风格，如style为STYLE_NO_TITLE，将被显示title。遗憾的是，我没有在DialogFragment中找到设置title内容的方法。theme为0，表示由系统选择合适的theme。
//        int style = DialogFragment.STYLE_NO_NORMAL, theme = 0;
//        setStyle(style,theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //1、通过inflate，根据layout XML定义，创建view
        final View v = inflater.inflate(R.layout.dialog_address, container, false);
        final EditText fourEdt = (EditText) v.findViewById(R.id.address_et);

        //2、注册三个button的按键监听listener
        Button dismissBtn = (Button) v.findViewById(R.id.button_dismiss);
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Button saveBtn = (Button) v.findViewById(R.id.button_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str4 = fourEdt.getText().toString();
                if (!TextUtils.isEmpty(str4)) {
                    UserHelper.setBaseUrl(str4);
                    dismiss();
                }else {
                    ToastUtil.showToast(getActivity(), "请输入地址...");
                }
            }
        });

        return v;
    }


}
