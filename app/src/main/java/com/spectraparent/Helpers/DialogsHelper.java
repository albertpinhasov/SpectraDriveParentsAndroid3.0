package com.spectraparent.Helpers;

import android.content.Context;
import android.content.DialogInterface;

import com.ligl.android.widget.iosdialog.IOSSheetDialog;
import com.spectraparent.Helpers.colordialog.PromptDialog;
import com.spectraparent.Models.Child;

public class DialogsHelper {

    public static void showAlert(Context context, String title, String body, String okTitle, String cancelTitle, int type) {
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

    public static void showAlert(Context context, String title, String body, String okTitle, String cancelTitle, int type, final Runnable onAnyPress) {
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
//
//    public static void showAlertWithCloseActivity(final Context context, String title, String body, String okTitle, String cancelTitle, int type) {
//        new PromptDialog(context)
//                .setDialogType(type)
//                .setAnimationEnable(true)
//                .setTitleText(title)
//                .setContentText(body)
//                .setPositiveListener(okTitle, new PromptDialog.OnPositiveListener() {
//                    @Override
//                    public void onClick(PromptDialog dialog) {
//                        dialog.dismiss();
//                        ((UpdateLocationActivity) context).finish();
//                    }
//                }).show();
//    }

    public static void updateChildDialog(final Context context, final Child childModel, final EditOrDeleteChildInterface editOrDeleteChildInterface) {
        IOSSheetDialog.SheetItem[] items = new IOSSheetDialog.SheetItem[2];
        items[0] = new IOSSheetDialog.SheetItem("Edit", IOSSheetDialog.SheetItem.BLUE);
        items[1] = new IOSSheetDialog.SheetItem("Delete", IOSSheetDialog.SheetItem.BLUE);
        IOSSheetDialog dialog2 = new IOSSheetDialog.Builder(context)
                .setData(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            editOrDeleteChildInterface.editChild(childModel);
                        } else if (which == 1) {
                            editOrDeleteChildInterface.deleteChild(childModel);

                        }
                    }
                }).show();
    }
}
