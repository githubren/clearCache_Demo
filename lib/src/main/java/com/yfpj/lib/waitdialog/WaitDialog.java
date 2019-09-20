package com.yfpj.lib.waitdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yfpj.lib.R;

import java.util.HashMap;
import java.util.Map;


public class WaitDialog extends Dialog{
    private static Map<String, WaitDialog> listWaitDialog = new HashMap();

    private WaitDialog(Context context, int theme) {
        super(context, theme);
    }

    private WaitDialog(Context context) {
        super(context);
    }

    public static WaitDialog getInstance(Context context) {
        return getInstance(context, false);
    }

    public static WaitDialog getInstance(Context context, boolean isCancelable) {
        String key = context.getClass().getSimpleName();
        if(null == listWaitDialog.get(key)) {
            Class var3 = WaitDialog.class;
            synchronized(WaitDialog.class) {
                if(null == listWaitDialog.get(key)) {
                    View view = View.inflate(context, R.layout.loading, (ViewGroup)null);
                    WaitDialog dialog = new WaitDialog(context, R.style.alert_dialog_style);
                    dialog.setContentView(view);
                    dialog.setCancelable(isCancelable);
                    listWaitDialog.put(key, dialog);
                }
            }
        }

        return (WaitDialog)listWaitDialog.get(key);
    }

    public void showWaitDialog(int msgId) {
        this.showWaitDialog(this.getContext().getString(msgId));
    }

    public void showWaitDialog() {
        this.showWaitDialog(this.getContext().getString(R.string.str_loading_wait));
    }

    public void showWaitDialog(String msg) {
        TextView msgTv = (TextView)this.findViewById(R.id.loading_msg);
        msgTv.setText(msg);
        boolean isCanShow = true;
        if(this.getContext() instanceof Activity) {
            isCanShow = !((Activity)this.getContext()).isFinishing();
        }

        if(isCanShow && !this.isShowing()) {
            this.show();
        }

    }

    public void dismissWaitDialog() {
        if(this.isShowing()) {
            this.dismiss();
        }

    }

    public static void destroyDialog(Context context) {
        String key = context.getClass().getSimpleName();
        if(null != listWaitDialog.get(key)) {
            ((WaitDialog)listWaitDialog.get(key)).dismissWaitDialog();
            listWaitDialog.remove(key);
        }

    }
}
