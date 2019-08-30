package com.spectraparent.Helpers;

import android.content.Context;

import com.spectraparent.Helpers.colordialog.PromptDialog;

public class DialogsHelper {

    public static void showAlert(Context context, String title, String body, String okTitle, String cancelTitle, int type){
        new PromptDialog(context)
                .setDialogType(type)
                .setAnimationEnable(true)
                .setTitleText(title)
                .setContentText(body)
                .setPositiveListener(okTitle, new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public static void showAlert(Context context, String title, String body, String okTitle, String cancelTitle, int type, final Runnable onAnyPress){
        new PromptDialog(context)
                .setDialogType(type)
                .setAnimationEnable(true)
                .setTitleText(title)
                .setContentText(body)
                .setPositiveListener(okTitle, new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        onAnyPress.run();
                    }
                }).show();
    }

}
