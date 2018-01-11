package com.yang.customviews;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/10/20.
 */

public class DialogCreater {
    public static Dialog createLoadingDialog(Context context){
        CustomDialog dialog = new CustomDialog(context, android.R.layout.select_dialog_singlechoice, com.yang.R.style.LoadingDialog, new CustomDialog.InitViewInterface() {
            @Override
            public void init(View view) {
                TextView testTv = (TextView)view.findViewById(android.R.id.text1);
                testTv.setText("Loooooooooading");
            }
        });
        return dialog;
    }

    public static Dialog createSpinnerDialog(Context c, View loopView){
        CustomDialog dialog = new CustomDialog(c, loopView, com.yang.R.style.SpinnerDialog, new CustomDialog.InitViewInterface() {
            @Override
            public void init(View view) {

            }
        });

        return dialog;
    }

//    public static Dialog createInputDialog(Context c){
//        CustomDialog dialog = new CustomDialog(c,)
//        return dialog;
//    }
}
