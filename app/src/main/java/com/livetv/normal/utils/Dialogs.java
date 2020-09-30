package com.livetv.normal.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.livetv.normal.R;
import com.livetv.normal.LiveTvApplication;
import com.livetv.normal.listeners.DialogListener;

public class Dialogs {
    public static void showCantGoBackToast() {
        showToast((int) R.string.cant_go_back_here);
    }

    public static void showToast(int messageId) {
        Toast.makeText(LiveTvApplication.getAppContext(), messageId, Toast.LENGTH_LONG).show();
    }

    public static void showToast(String message) {
        Toast.makeText(LiveTvApplication.getAppContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void showCantGoBackSnack(View view) {
        showCantGoBackToast();
    }

    public static void showSnack(View view, String s) {
        showCantGoBackToast();
    }

    public static void showOneButtonDialog(Activity activity, int message) {
        showOneButtonDialog(activity, activity.getString(R.string.attention), activity.getString(message), (OnClickListener) null);
    }
    public static void showOneButtonDialog(Activity activity, String message) {
        showOneButtonDialog(activity, activity.getString(R.string.attention), message, (OnClickListener) null);
    }
    public static void showOneButtonDialog(Activity activity, int title, int message) {
        showOneButtonDialog(activity, activity.getString(title), activity.getString(message), (OnClickListener) null);
    }

    public static void showOneButtonDialog(Activity activity, String message, OnClickListener onClickListener) {
        showOneButtonDialog(activity, activity.getString(R.string.attention), message, (OnClickListener) null);
    }

    public static void showOneButtonDialog(Activity activity, int message, OnClickListener dialogListener) {
        showOneButtonDialog(activity, activity.getString(R.string.attention), activity.getString(message), dialogListener);
    }

    public static void showOneButtonDialog(Activity activity, int title, int message, OnClickListener dialogListener) {
        showOneButtonDialog(activity, activity.getString(title), activity.getString(message), dialogListener);
    }

    public static void showOneButtonDialog(Activity activity, String title, String message, OnClickListener dialogListener) {
        Builder dialog = new Builder(activity, R.style.AppThemeDialog);
        dialog.setCancelable(false);
        dialog.setTitle((CharSequence) title);
        dialog.setMessage((CharSequence) message);
        if (dialogListener != null) {
            dialog.setPositiveButton((int) R.string.accept, dialogListener);
        } else {
            dialog.setPositiveButton((int) R.string.accept, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
        AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
        Button ne=alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button po=alert.getButton(DialogInterface.BUTTON_POSITIVE);
        ne.setBackground(((Context)activity).getDrawable(R.drawable.dialog_btn_background));
        po.setBackground(((Context)activity).getDrawable(R.drawable.dialog_btn_background));
        ne.setPadding(16,4,16,4);
        po.setPadding(16,4,16,4);
    }

    public static void showTwoButtonsDialog(Activity activity, int message, DialogListener dialogListener) {
        showTwoButtonsDialog(activity, (int) R.string.accept, (int) R.string.cancel, message, dialogListener);
    }

    public static void showTwoButtonsDialog(Activity activity, int accept, int cancel, int message, final DialogListener dialogListener) {
        Builder dialog = new Builder(activity, R.style.AppThemeDialog);
        dialog.setCancelable(false);
        dialog.setTitle((int) R.string.attention);
        dialog.setMessage(message);
        dialog.setPositiveButton(accept, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                dialogListener.onAccept();
            }
        }).setNegativeButton(cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogListener.onCancel();
            }
        });
        AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();

        Button ne=alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button po=alert.getButton(DialogInterface.BUTTON_POSITIVE);
        ne.setBackground(((Context)activity).getDrawable(R.drawable.dialog_btn_background));
        po.setBackground(((Context)activity).getDrawable(R.drawable.dialog_btn_background));
        ne.setPadding(16,4,16,4);
        po.setPadding(16,4,16,4);
    }

    public static void showTwoButtonsDialog(Activity activity, int accept, int cancel, String message, final DialogListener dialogListener) {
        Builder dialog = new Builder(activity, R.style.AppThemeDialog);
        dialog.setCancelable(false);
        dialog.setTitle((int) R.string.attention);
        dialog.setMessage((CharSequence) message);
        dialog.setPositiveButton(accept, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                dialogListener.onAccept();
            }
        }).setNegativeButton(cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogListener.onCancel();
            }
        });
        AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
        Button ne=alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button po=alert.getButton(DialogInterface.BUTTON_POSITIVE);
        ne.setBackground(((Context)activity).getDrawable(R.drawable.dialog_btn_background));
        po.setBackground(((Context)activity).getDrawable(R.drawable.dialog_btn_background));
        ne.setPadding(16,4,16,4);
        po.setPadding(16,4,16,4);
    }
}
