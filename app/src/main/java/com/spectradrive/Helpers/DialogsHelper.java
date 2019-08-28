package com.spectradrive.Helpers;

import android.content.Context;
import android.widget.Toast;

import com.spectradrive.Activities.MainActivity;
import com.spectradrive.Helpers.colordialog.ColorDialog;
import com.spectradrive.Helpers.colordialog.PromptDialog;
import com.spectradrive.SpectraDrive;
import com.spectradrive.android.R;

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
